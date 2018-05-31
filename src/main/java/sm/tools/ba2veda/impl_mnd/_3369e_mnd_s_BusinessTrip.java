package sm.tools.ba2veda.impl_mnd;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

public class _3369e_mnd_s_BusinessTrip extends Ba2VedaTransform {
	
	public _3369e_mnd_s_BusinessTrip(BaSystem _ba, VedaConnection _veda, Replacer replacer) {
		super(_ba, _veda, replacer, "3369eb40218943c2a1befe068b86fd3c", "mnd-s:BusinessTrip");
	}

	@Override
	public void inital_set() {
		fields_map.put("department", "v-s:businessTripDepartment");
		fields_map.put("objective", "v-s:businessTripGoal");
		fields_map.put("firm_to", "mnd-s:businessTripOrganization");
		
		fields_map.put("duration", "v-s:duration");
		fields_map.put("date_from", "v-s:dateFrom");
		fields_map.put("date_to", "v-s:dateTo");
		fields_map.put("contacts_with_competitors", "mnd-s:hasContactsWithCompetitors");
		fields_map.put("theme", "mnd-s:hasBusinessTripPurpose");
		fields_map.put("responsible", "v-s:businessTripEmployee");
		fields_map.put("transport", "mnd-s:hasBusinessTripTransportKind");
		fields_map.put("compound_title", "rdfs:label");
		fields_map.put("add_info", "v-s:hasComment");
		
		fields_map.put("addresse_to", "?");
		fields_map.put("country", "?");
		
		fields_map.put("reg_note", "?");
		
		fields_map.put("cause", "?");
		fields_map.put("date_from_fact", "?");
		fields_map.put("date_to_fact", "?");
		
		fields_map.put("cause2", "?");
		fields_map.put("date_from_fact_2", "?");
		fields_map.put("date_to_fact_2", "?");
		
//		fields_map.put("cause", "?");
//		fields_map.put("", "?");
//		fields_map.put("", "?");
//		fields_map.put("", "?");
//		fields_map.put("", "?");
//		fields_map.put("", "?");
	}

	@Override
	public List<Individual> transform(int level, XmlDocument doc, String ba_id, String parent_veda_doc_uri,
			String parent_ba_doc_id, String path) throws Exception {
		String uri = prepare_uri(ba_id);
		List<Individual> res = new ArrayList<Individual>();

		Individual new_individual = new Individual();
		new_individual.setUri(uri);

		set_basic_fields(level, new_individual, doc);
		
		Resources adresse_to = null;
		Resources country = null;
		
		Individual cot = null;
		Individual cot2 = null;
		
		List<XmlAttribute> atts = doc.getAttributes();
		for (XmlAttribute att : atts) {
			String code = att.getCode();

			String predicate = fields_map.get(code);
//			System.out.println("CODE: " + code);
			if (predicate != null) {
				Resources rss = ba_field_to_veda(level, att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri,
						true);
				
				if (predicate.equals("?") == false)
					new_individual.addProperty(predicate, rss);
				
				if (rss == null)
					continue;
				
				if (code.equals("addresse_to"))
					adresse_to = rss;
				else if (code.equals("country")) {
					country = rss;
					String country_name = att.getValue();
					String result = null;
					switch (country_name) {
					case "текст":
						result = "справочник";
						break;
						
					case "Российская Федерация":
					case "РФ":
					case "Россия":
					case "РОССИЯ":
						result = "d:Country_RUS";
						break;
					case "Австрия":
						result = "d:Country_AUT";
						break;
						
					case "Lithuania":
						result = "?";
						break;
						
					case "Польша":
						result = "d:Country_POL";
						break;
						
					case "Швеция":
						result = "d:Country_SWE";
						break;
						
					case "Финляндия":
						result = "d:Country_FIN";
						break;
						
					case "ЮАР":
						result = "d:Country_ZAF";
						break;	
					}
					
					if (result != null)
						new_individual.addProperty("v-s:hasClassifierCountry", new Resource(result, Type._Uri));
				} else if (code.equals("reg_note")) {
					String rn = att.getLinkValue();
					if (rn == null)
						continue;
					Pair<XmlDocument, Long> pair = ba.getActualDocument(rn);
					if (pair == null)
						continue;
					
					XmlDocument rn_doc = pair.getLeft();
					
					Individual btrr = new Individual();
					String rn_uri = prepare_uri(rn);
					btrr.setUri(rn_uri);
					btrr.addProperty("rdf:type", "v-s:BusinessTripRegistrationRecord", Type._Uri);
					btrr.addProperty("v-s:parent", 	new Resource(new_individual.getUri(), Type._Uri));
					btrr.addProperty("v-s:backwardTarget", new Resource(new_individual.getUri(), Type._Uri));
					btrr.addProperty("v-s:creator", new_individual.getResources("v-s:creator"));
					btrr.addProperty("v-s:created", new_individual.getResources("v-s:created"));
					
					for (XmlAttribute rn_att : rn_doc.getAttributes()) {
						String rn_code = rn_att.getCode();
						
//						System.out.println("\t RN_CODE: " + code);
						
						if (rn_code.equals("reg_number")) {
							String reg_number = rn_att.getTextValue();
							btrr.addProperty("v-s:registrationNumber", new Resource(reg_number));
						} else if (rn_code.equals("date_from")) {
							Date date_from = null;
							date_from = rn_att.getDateValue();
							System.out.println("\t\t" + date_from.toString());
//							btrr.addProperty("v-s:registrationDate", new Resource(date_from.toString()));
							
//							Date date_from = null;
//							date_from = rn_att.getDateValue();
//							System.out.println("\t\t" + date_from.toString());
//							SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
							SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
							SimpleDateFormat format2 = new SimpleDateFormat("HH:mm:ss");
							Calendar calendar = Calendar.getInstance();
							calendar.setTime(date_from);
							String date_from_str1 = format1.format(date_from);
							String date_from_str2 = format2.format(date_from);
							String date_from_str = date_from_str1 + "T" + date_from_str2 + "Z";
//							String date_from_str = String.format("%4d-%2d-%2dT%2d:%2d:%2dZ", 
//									calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
//									calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
							btrr.addProperty("v-s:registrationDate", new Resource(date_from_str, Type._Datetime));
						}
					}
					
					new_individual.addProperty("v-s:hasBusinessTripRegistrationRecord", new Resource(rn_uri, Type._Uri));
					putIndividual(level, btrr, ba_id);
				} else if (code.equals("cause")) {
					if (rss.resources.size() < 1)
						continue;
					
					if (cot == null) 
						cot = new Individual();
					
					cot.addProperty("rdfs:comment", rss);
				} else if (code.equals("date_from_fact")) {
					if (rss.resources.size() < 1)
						continue;
					
					if (cot == null)
						cot = new Individual();
					
					cot.addProperty("v-s:dateFrom", rss);
				} else if (code.equals("date_to_fact")) {
					if (rss.resources.size() < 1)
						continue;
					
					if (cot == null)
						cot = new Individual();
					
					cot.addProperty("v-s:dateTo", rss);
				} else if (code.equals("cause2")) {
					if (rss.resources.size() < 1)
						continue;
					
					if (cot2 == null) 
						cot2 = new Individual();
					
					cot2.addProperty("rdfs:comment", rss);
				} else if (code.equals("date_from_fact_2")) {
					if (rss.resources.size() < 1)
						continue;
					
					if (cot2 == null)
						cot2 = new Individual();
					
					cot2.addProperty("v-s:dateFrom", rss);
				} else if (code.equals("date_to_fact_2")) {
					if (rss.resources.size() < 1)
						continue;
					
					if (cot2 == null)
						cot2 = new Individual();
					
					cot2.addProperty("v-s:dateTo", rss);
				}
			}
		}
		
		String[] langs_out1 = { "EN", "RU" };
		String[] langs_out2 = { "NONE" };
		if (adresse_to != null && country != null) {
			Object[] btodescr = { adresse_to, ", ", country };
			Resources rss = rs_assemble(btodescr, langs_out1);
			if (rss.resources.size() == 0)
				rss = rs_assemble(btodescr, langs_out2);
			
			if (rss.resources.size() > 0)
				new_individual.addProperty("mnd-s:businessTripOrganizationDescription", rss);
				
			
		} if (adresse_to == null) {
			Object[] btodescr = { country };
			Resources rss = rs_assemble(btodescr, langs_out1);
			if (rss.resources.size() == 0)
				rss = rs_assemble(btodescr, langs_out2);
			
			if (rss.resources.size() > 0)
				new_individual.addProperty("mnd-s:businessTripOrganizationDescription", rss);
		} else if (country == null) {
			Object[] btodescr = { adresse_to };
			Resources rss = rs_assemble(btodescr, langs_out1);
			if (rss.resources.size() == 0)
				rss = rs_assemble(btodescr, langs_out2);
			
			if (rss.resources.size() > 0)
				new_individual.addProperty("mnd-s:businessTripOrganizationDescription", rss);
		}
		
		if (cot != null) {
			cot.setUri(uri + "_change_of_terms");
			cot.addProperty("rdf:type", new Resource("mnd-s:ChangeOfTerms", Type._Uri));
			cot.addProperty("v-s:creator", new Resource("d:rimert_DocRegistrator_SLPK", Type._Uri));
			cot.addProperty("v-s:created", new_individual.getResources("v-s:created"));
			new_individual.addProperty("mnd-s:hasChangeOfTerms", new Resource(cot.getUri(), Type._Uri));
			putIndividual(level, cot, ba_id);
		}
		
		if (cot2 != null) {
			cot2.setUri(uri + "_change_of_terms2");
			cot2.addProperty("rdf:type", new Resource("mnd-s:ChangeOfTerms", Type._Uri));
			cot2.addProperty("v-s:creator", new Resource("d:rimert_DocRegistrator_SLPK", Type._Uri));
			cot2.addProperty("v-s:created", new_individual.getResources("v-s:created"));
			new_individual.addProperty("mnd-s:hasChangeOfTerms", new Resource(cot2.getUri(), Type._Uri));
			putIndividual(level, cot2, ba_id);
		}
		
		new_individual.addProperty("rdf:type", to_class, Type._Uri);
		res.add(new_individual);
		return res;
	}
}

