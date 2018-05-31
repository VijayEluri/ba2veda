package sm.tools.ba2veda.impl_mnd;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.mndsc.bigarchive.server.kernel.document.beans.XmlAttribute;
import ru.mndsc.bigarchive.server.kernel.document.beans.XmlDocument;
import sm.tools.ba2veda.Ba2VedaTransform;
import sm.tools.ba2veda.BaSystem;
import sm.tools.ba2veda.Pair;
import sm.tools.ba2veda.Replacer;
import sm.tools.veda_client.Individual;
import sm.tools.veda_client.Resource;
import sm.tools.veda_client.Resources;
import sm.tools.veda_client.Type;
import sm.tools.veda_client.VedaConnection;

public class _4748a_mnd_s_ItRequest extends Ba2VedaTransform {
	public _4748a_mnd_s_ItRequest(BaSystem _ba, VedaConnection _veda, Replacer replacer) {
		super(_ba, _veda, replacer, "4748a9cb154747d9b144b9ccb5e39062", "mnd-s:ItRequest");
	}

	@Override
	public void inital_set() {
		// TODO Auto-generated method stub
		fields_map.put("number", "v-s:registrationNumber");
		fields_map.put("initiator", "v-s:initiator");
		fields_map.put("description_short", "v-s:theme");
		fields_map.put("description", "v-s:content");
		fields_map.put("attachment", "v-s:attachment");
		fields_map.put("date_from", "v-s:date");
		fields_map.put("name", "rdfs:label");
		
		fields_map.put("service", "?");
		fields_map.put("procedure", "?");
		fields_map.put("status", "?");
		fields_map.put("status_manual", "?");
		fields_map.put("object_it", "?");
		fields_map.put("work", "?");
		fields_map.put("work_time", "?");
		fields_map.put("comment", "?");
		fields_map.put("date_to", "?");
		fields_map.put("add_doc", "?");
	}
	
	@Override
	public List<Individual> transform(int level, XmlDocument doc, String ba_id, String parent_veda_doc_uri,
			String parent_ba_doc_id, String path) throws Exception {
		String uri = prepare_uri(ba_id);
		List<Individual> res = new ArrayList<Individual>();

		Individual new_individual = new Individual();
		new_individual.setUri(uri);

		set_basic_fields(level, new_individual, doc);

		new_individual.addProperty("rdf:type", to_class, Type._Uri);
		
		Individual reqAction = null;
		
		String status = null, statusManual = null;
		
		List<XmlAttribute> atts = doc.getAttributes();
		
		Date from = null, to = null;
		int reqAction2Count = 1;
		ArrayList<Individual> reqActions2 = new ArrayList<Individual>();
		int linkCount = 0;
		for (XmlAttribute att : atts) {
			String code = att.getCode();

			String predicate = fields_map.get(code);
			System.out.println("CODE: " + code);
			if (predicate != null) {
				Resources rss = ba_field_to_veda(level, att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri,
						true);

				if (predicate.equals("?") == false) 
					new_individual.addProperty(predicate, rss);
				
				if (rss == null)
					continue;
				
				if (rss.resources.size() < 1)
					continue;
				
				if (code.equals("service")) {
					if (reqAction == null)
						reqAction = new Individual();
					
					reqAction.addProperty("mnd-s:hasItSector", rss);
					
				} else if (code.equals("add_doc")) {
					Individual link = new Individual();
					linkCount++;
					link.setUri(String.format("%s_link%d", new_individual.getUri(), linkCount));
					link.addProperty("rdf:type", "v-s:Link", Type._Uri);
					link.addProperty("v-s:from", new_individual.getUri(), Type._Uri);
					link.addProperty("v-s:to", rss);
					new_individual.addProperty("v-s:hasLink", link.getUri(), Type._Uri);
					putIndividual(level, link, ba_id);
				} else if (code.equals("comment")) {
					Individual comment = new Individual();
					comment.setUri(new_individual.getUri() + "_comment");
					comment.addProperty("rdf:type", "v-s:Comment", Type._Uri);
					comment.addProperty("rdfs:label", rss);
					comment.addProperty("v-s:backwardTarget", new_individual.getUri(), Type._Uri);
					comment.addProperty("v-s:backwardProperty", "v-s:hasComment", Type._Uri);
					comment.addProperty("v-s:created", new_individual.getResources("v-s:created"));
					comment.addProperty("v-s:creator", new_individual.getResources("v-s:creator"));
					new_individual.addProperty("v-s:hasComment", comment.getUri(), Type._Uri);
					putIndividual(level, comment, ba_id);
				} else if (code.equals("procedure")) {
					XmlDocument doc2 = ba.getActualDocument(att.getLinkValue()).getLeft();
					if (doc2 != null) {
						if (reqAction == null)
							reqAction = new Individual();
						List<XmlAttribute> atts2 = doc2.getAttributes();
						for (XmlAttribute att2 : atts2) {
							System.out.println("\tCODE2: " + att2.getCode());
							if (att2.getCode().equals("kind")) {
								Resources rss2 = ba_field_to_veda(level, att2, uri, att.getLinkValue(), 
									doc2, path, parent_ba_doc_id, parent_veda_doc_uri,
									true);
								reqAction.addProperty("mnd-s:hasItService", rss2);
							}
						}
					}
				} else if (code.equals("status"))
					status = att.getRecordIdValue();
				else if (code.equals("status_manual"))
					statusManual = att.getRecordIdValue();
				else if (code.equals("work_time")) {
					if (reqAction == null)
						reqAction = new Individual();
					
					reqAction.addProperty("v-s:count", rss);
				} else if (code.equals("date_to")) {
					if (reqAction == null)
						reqAction = new Individual();
					
					reqAction.addProperty("v-s:datePlan", rss);
				} else if (code.equals("work")) {
					String linkStr = att.getLinkValue();
					if (linkStr == null)
						continue;
					Pair<XmlDocument, Long> pair = ba.getActualDocument(linkStr);
					if (pair == null)
						continue;
					
					XmlDocument doc2 = pair.getLeft();
					if (doc2 != null) {
						Individual reqAction2 = null;
						
						List<XmlAttribute> atts2 = doc2.getAttributes();
						for (XmlAttribute att2 : atts2) {
							System.out.println("\tCODE WORK: " + att2.getCode());
							if (att2.getCode().equals("work_kind")) {
								Resources rss2 = ba_field_to_veda(level, att2, uri, att.getLinkValue(), 
										doc2, path, parent_ba_doc_id, parent_veda_doc_uri,
										true);
								if (reqAction2 == null)
									reqAction2 = new Individual();
								
								reqAction2.addProperty("mnd-s:hasItService", rss2);
							} else if (att2.getCode().equals("work_time")) {
								Resources rss2 = ba_field_to_veda(level, att2, uri, att.getLinkValue(), 
										doc2, path, parent_ba_doc_id, parent_veda_doc_uri,
										true);
														
								if (reqAction2 == null)
									reqAction2 = new Individual();
								
								reqAction2.addProperty("v-s:timeEffort", rss2);		
							} else if (att2.getCode().equals("object_it")) {
								linkStr = att2.getLinkValue();
								if (linkStr == null)
									continue;
								pair = ba.getActualDocument(linkStr);
								if (pair == null)
									continue;
								XmlDocument doc3 = pair.getLeft();
								if (doc3 != null) {
									if (reqAction2 == null)
										reqAction2 = new Individual();
									List<XmlAttribute> atts3 = doc3.getAttributes();
									for (XmlAttribute att3 : atts3) {
										
										if (att3.getCode().equals("theme")) {
											XmlDocument doc4 = ba.getActualDocument(att3.getRecordIdValue()).getLeft();
											if (doc4 != null) {
												List<XmlAttribute> atts4 = doc4.getAttributes();
												for (XmlAttribute att4 : atts4) {
													if (att4.getCode().equals("kind")) {
														Resources rss2 = ba_field_to_veda(level, att4, uri, att.getLinkValue(), 
															doc4, path, parent_ba_doc_id, parent_veda_doc_uri,
																true);
														reqAction2.addProperty("mnd-s:hasItSector", rss2);
													}
												}
											}
											
										}
									}
								} 
							} else if (att2.getCode().equals("comment")) {
								Resources rss2 = ba_field_to_veda(level, att2, uri, att.getLinkValue(), 
										doc2, path, parent_ba_doc_id, parent_veda_doc_uri,
										true);
								if (reqAction2 == null)
									reqAction2 = new Individual();
								
								reqAction2.addProperty("v-s:reportDescription", rss2);
							} else if (att2.getCode().equals("responsible")) {
								if (reqAction2 == null)
									reqAction2 = new Individual();
								Resources rss2 = ba_field_to_veda(level, att2, uri, att.getLinkValue(), 
										doc2, path, parent_ba_doc_id, parent_veda_doc_uri,
										true);
								reqAction2.addProperty("v-s:responsible", rss2);
							} else if (att2.getCode().equals("date_to")) {	
								to = att2.getDateValue();
							} else if (att2.getCode().equals("date_from")) {
								from = att2.getDateValue();
							}
						}
						
						if (reqAction2 != null) {
							reqAction2Count++;
							reqAction2.setUri(String.format("%s_request_action%d", new_individual.getUri(), reqAction2Count));
							reqAction2.addProperty("v-s:created", new_individual.getResources("v-s:created"));
							reqAction2.addProperty("v-s:creator", new_individual.getResources("v-s:creator"));
							reqAction2.addProperty("rdf:type", "mnd-s:ItRequestAction", Type._Uri);
							reqAction2.addProperty("v-s:backwardTarget", "d:" + ba_id, Type._Uri);
							reqAction2.addProperty("v-s:backwardProperty", "mnd-s:hasItRequestAction", Type._Uri);
							reqAction2.addProperty("v-s:canRead", "true", Type._Bool);
							reqAction2.addProperty("v-s:parent", new_individual.getUri(), Type._Uri);
							
							if (from != null && to == null || (from != null && to != null && from.getTime() > to.getTime()))
								reqAction2.addProperty("v-s:dateFact", from);
							else if (from == null && to != null || (from != null && to != null && from.getTime() < to.getTime()))
								reqAction2.addProperty("v-s:dateFact", to);
							reqActions2.add(reqAction2);
						}
					}
				}
			}
		}
		
		if (reqAction != null) {
			reqAction.setUri(new_individual.getUri() + "_request_action1");
			reqAction.addProperty("v-s:created", new_individual.getResources("v-s:created"));
			reqAction.addProperty("v-s:creator", new_individual.getResources("v-s:creator"));
			reqAction.addProperty("rdf:type", "mnd-s:ItRequestAction", Type._Uri);
			reqAction.addProperty("v-s:backwardTarget", "d:" + ba_id, Type._Uri);
			reqAction.addProperty("v-s:backwardProperty", "mnd-s:hasItRequestAction", Type._Uri);
			reqAction.addProperty("v-s:canRead", "true", Type._Bool);
			reqAction.addProperty("v-s:parent", new_individual.getUri(), Type._Uri);
			new_individual.addProperty("mnd-s:hasItRequestAction", reqAction.getUri(), Type._Uri);
			putIndividual(level, reqAction, ba_id);
		}
		
		for (int i = 0; i < reqActions2.size(); i++) {
			new_individual.addProperty("mnd-s:hasItRequestAction", reqActions2.get(i).getUri(), Type._Uri);
			putIndividual(level, reqActions2.get(i), ba_id);
		}
		
		if (statusManual != null) {
			switch (statusManual) {
			case "4822712deeb748cba3deb8ffdd1d46f4":
				new_individual.addProperty("v-s:hasStatus", "v-s:StatusAgreement", Type._Uri);
				break;
			case "7fe77f5249b847d7b7c7d964aefdf4a3":
				new_individual.addProperty("v-s:hasStatus", "v-s:StatusImplemented", Type._Uri);
				break;
			case "1fbfad9afc0143ed9bf7d972f6469a3b":
				new_individual.addProperty("v-s:hasStatus", " v-s:StatusRejected", Type._Uri);
				break;
			case "3d76ee20655644ef8655261777e80ec0":
				new_individual.addProperty("v-s:hasStatus", "v-s:StatusImplemented", Type._Uri);
				break;
			case "218e3b3f1367416f86fa0615a5b222b6":
				new_individual.addProperty("v-s:hasStatus", "v-s:StatusAgreement", Type._Uri);
				break;
			case "61f3406017d9416e9267ee0490839fbd":
				new_individual.addProperty("v-s:hasStatus", "", Type._Uri);
				break;
			case "v-s:StatusPostponed":
				new_individual.addProperty("v-s:hasStatus", "", Type._Uri);
				break;
			case "300412c2909e4eee8da03fbed23290a7":
				new_individual.addProperty("v-s:hasStatus", "v-s:StatusMoreInformationPending", Type._Uri);
				break;
			case "ea602cb7c96d4b43bc0ddc882bce8c8b":
				new_individual.addProperty("v-s:hasStatus", "v-s:StatusMoreInformationPending", Type._Uri);
				break;
			case "8e1d546b-e70e-4f88-a4de-091fc7a247c3":
				new_individual.addProperty("v-s:hasStatus", "v-s:StatusImplemented", Type._Uri);
				break;
			case "4363a85a-1b4f-4c0b-89be-fe1326b9bae9":
				new_individual.addProperty("v-s:hasStatus", "v-s:StatusImplementation", Type._Uri);
				break;
			case "17b8b7d1-0f94-4d5f-a0df-46b16a720564":
				new_individual.addProperty("v-s:hasStatus", "v-s:StatusAgreement", Type._Uri);
				break;
			case "0aa4f347-a28f-4606-9917-0a1481685fd4":
				new_individual.addProperty("v-s:hasStatus", "v-s:StatusPostponed", Type._Uri);
				break;
			}
		} else if (status != null) {			
			switch (status) {
			case "9928f560ac224b40aa7c16ab4153aa4d":
				new_individual.addProperty("v-s:hasStatus", "v-s:StatusImplementation", Type._Uri);
				break;
			case "e9ede869a25845d3a4d715af0839c344":
				new_individual.addProperty("v-s:hasStatus", "v-s:StatusImplemented", Type._Uri);
				break;
			case "70d56b3afb9e4993adcefd49a2e92101":
				new_individual.addProperty("v-s:hasStatus", "v-s:StatusAgreement", Type._Uri);
				break;
			case "6625fa6206964e75bf6a0d3005da53f7":
				new_individual.addProperty("v-s:hasStatus", "v-s:StatusArchived", Type._Uri);
				break;
			case "c4d48f2d229c4db5b7f3a0a4ce4c7e58":
				new_individual.addProperty("v-s:hasStatus", "v-s:StatusRejected", Type._Uri);
				break;
			case "3cfd3647405f4dd6bbcd26605b41490d":
				new_individual.addProperty("v-s:hasStatus", "v-s:StatusRejected", Type._Uri);
				break;
			case "92150c847735453c98e68b4d57ac7d10":
				new_individual.addProperty("v-s:hasStatus", "v-s:StatusAgreement", Type._Uri);
				break;
			case "83bb30a9d8d943f99d0c2d0eab2f478c":
				new_individual.addProperty("v-s:hasStatus", "v-s:StatusImplementation", Type._Uri);
				break;
			}
		}
				
		
		res.add(new_individual);
		return res;
	}
}
