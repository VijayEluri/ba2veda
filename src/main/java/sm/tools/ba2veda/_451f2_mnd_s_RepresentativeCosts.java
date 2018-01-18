package sm.tools.ba2veda;

import java.util.ArrayList;
import java.util.List;

import ru.mndsc.bigarchive.server.kernel.document.beans.XmlAttribute;
import ru.mndsc.bigarchive.server.kernel.document.beans.XmlDocument;
import sm.tools.veda_client.Individual;
import sm.tools.veda_client.Resource;
import sm.tools.veda_client.Resources;
import sm.tools.veda_client.Type;
import sm.tools.veda_client.VedaConnection;

public class _451f2_mnd_s_RepresentativeCosts extends Ba2VedaTransform {
	public _451f2_mnd_s_RepresentativeCosts(BaSystem _ba, VedaConnection _veda, Replacer replacer) {
		super(_ba, _veda, replacer, "451f2693b5264031a9182eaed80892c5", "mnd-s:RepresentativeCosts");
	}
	
	public void inital_set() {
		fields_map.put("budget_holder", "v-s:stakeholder");
		fields_map.put("target", "v-s:goal");
		fields_map.put("date_from", "v-s:dateFrom");
		fields_map.put("date_to", "v-s:dateTo");
		fields_map.put("area", "v-s:placeDescription");
		fields_map.put("number_persons", "v-s:count");
		fields_map.put("persons_mondi", "v-s:member");
		fields_map.put("attachment", "v-s:attachment");
		fields_map.put("type_payment", "v-s:hasPaymentForm");
		fields_map.put("payment_comment", "rdfs:comment");
		fields_map.put("report", "mnd-s:representativeCostsReport");
		
		fields_map.put("persons_other", "?");
		
		fields_map.put("currency", "?");
		fields_map.put("total", "?");
		fields_map.put("total_per_person", "?");
		
		fields_map.put("add_info", "?");
		fields_map.put("event", "?");
		fields_map.put("event_costs", "?");
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
		
		Resources currency = null;
		Resources total = null;
		Resources total_per_person = null;
		
		List<XmlAttribute> atts = doc.getAttributes();
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
				
				if (code.equals("persons_other")) {
					new_individual.addProperty("mnd-s:hasThirdPartyParticipant", rss);
				} else if (code.equals("event")) {
					new_individual.addProperty("mnd-s:hasRepresentativeCostsEvent", rss);
				} else if (code.equals("event_costs")) {
					new_individual.addProperty("mnd-s:hasRepresentativeCostsForEvent", rss);
				} else if (code.equals("currency"))
					currency = rss;
				else if (code.equals("total"))
					total = rss;
				else if (code.equals("total_per_person"))
					total_per_person = rss;
				else if (code.equals("add_info")) {
				/*	Individual comment = new Individual();
					comment.setUri(new_individual.getUri() + "_comment");
					comment.addProperty("rdf:type", new Resource("v-s:Comment", Type._Uri));
					comment.addProperty("v-s:creator", new_individual.getResources("v-s:creator"));
					comment.addProperty("v-s:created", new_individual.getResources("v-s:created"));
					String irf = att.getLinkValue();
					XmlDocument irf_doc = ba.getActualDocument(irf).getLeft();
					String inherit_rights_from = ba.get_first_value_of_field(irf_doc, "inherit_rights_from");
					
					if (inherit_rights_from == null)
					{
						comment.addProperty("v-s:linkedObject", new Resource("d:" + att.getLinkValue(), Type._Uri));
					} 
					else
					{
						if (irf_doc.getTypeId().equals("ead1b2fa113c45a8b79d093e8ec14728")
								|| irf_doc.getTypeId().equals("15206d33eafa440c84c02c8d912bce7a")
								|| irf_doc.getTypeId().equals("ec6d76a99d814d0496d5d879a0056428")
								|| irf_doc.getTypeId().equals("a0e50600ebe9450e916469ee698e3faa")
								|| irf_doc.getTypeId().equals("71e3890b3c77441bad288964bf3c3d6a")
								|| irf_doc.getTypeId().equals("cab21bf8b68a4b87ac37a5b41adad8a8")
								|| irf_doc.getTypeId().equals("110fa1f351e24a2bbc187c872b114ea4")
								|| irf_doc.getTypeId().equals("d539b253cb6247a381fb51f4ee34b9d8")
								|| irf_doc.getTypeId().equals("a7b5b15a99704c9481f777fa941506c0")
								|| irf_doc.getTypeId().equals("67588724c4c54b25a2c84906613bd15a"))
						{
							att.setLinkValue(inherit_rights_from);
							Resources rss1 = ba_field_to_veda(att, uri, inherit_rights_from, doc, path, parent_ba_doc_id, parent_veda_doc_uri,
									true);

							comment.addProperty("v-s:linkedObject", rss1);
						}
						else
						{
							comment.addProperty("v-s:linkedObject", new Resource(att.getLinkValue(), Type._Uri));
						}
					}
					
					putIndividual(comment, ba_id, true);
					new_individual.addProperty("v-s:hasComment", new Resource(comment.getUri(), Type._Uri));*/
					new_individual.addProperty("v-s:hasComment", rss);
				}
			}
		}
			
		if (currency != null && total != null) {
			Individual price = new Individual();
			price.setUri(new_individual.getUri() + "_1");
			price.addProperty("rdf:type", new Resource("v-s:Price", Type._Uri));
			price.addProperty("v-s:hasCurrency", currency);
			price.addProperty("v-s:sum", total);
			price.addProperty("v-s:creator", new_individual.getResources("v-s:creator"));
			price.addProperty("v-s:created", new_individual.getResources("v-s:created"));
			price.addProperty("v-s:parent", new Resource(new_individual.getUri(), Type._Uri));
			putIndividual(level, price, ba_id);
			new_individual.addProperty("v-s:hasPrice", new Resource(price.getUri(), Type._Uri));
		}
		
		if (currency != null && total_per_person != null) {
			Individual price = new Individual();
			price.setUri(new_individual.getUri() + "_2");
			price.addProperty("rdf:type", new Resource("v-s:Price", Type._Uri));
			price.addProperty("v-s:hasCurrency", currency);
			price.addProperty("v-s:sum", total_per_person);
			price.addProperty("v-s:creator", new_individual.getResources("v-s:creator"));
			price.addProperty("v-s:created", new_individual.getResources("v-s:created"));
			price.addProperty("v-s:parent", new Resource(new_individual.getUri(), Type._Uri));
			putIndividual(level, price, ba_id);
			new_individual.addProperty("v-s:hasPricePerUnit", new Resource(price.getUri(), Type._Uri));
		}
		
		new_individual.addProperty("rdf:type", to_class, Type._Uri);
		res.add(new_individual);
		return res;
	}
	
}
