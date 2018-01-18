package sm.tools.ba2veda.impl;

import java.util.ArrayList;
import java.util.List;

import ru.mndsc.bigarchive.server.kernel.document.beans.XmlAttribute;
import ru.mndsc.bigarchive.server.kernel.document.beans.XmlDocument;
import sm.tools.ba2veda.Ba2VedaTransform;
import sm.tools.ba2veda.BaSystem;
import sm.tools.ba2veda.Replacer;
import sm.tools.veda_client.Individual;
import sm.tools.veda_client.Resource;
import sm.tools.veda_client.Resources;
import sm.tools.veda_client.Type;
import sm.tools.veda_client.VedaConnection;

public class _050ce_mnd_s_Pass extends Ba2VedaTransform {
	public _050ce_mnd_s_Pass(BaSystem _ba, VedaConnection _veda, Replacer replacer)  {
		super(_ba, _veda, replacer, "050ceb6074164a0aab1a00edd404e044", "mnd-s:Pass");
	}
	
	public void inital_set() {
		fields_map.put("number_reg", "v-s:registrationNumber");
		fields_map.put("date_from", "v-s:dateFromPlan");
		fields_map.put("date_to", "v-s:dateToPlan");
		fields_map.put("area", "mnd-s:passRequestKpp");
		fields_map.put("duration", "v-s:hasWorkMode");
		fields_map.put("place", "mnd-s:passRequestObject");
		fields_map.put("classifier_doc", "mnd-s:hasPassKind");
		fields_map.put("date_reg", "v-s:dateFromFact");
		fields_map.put("date_back", "v-s:dateToFact");
		fields_map.put("initiator", "v-s:initiator");
		fields_map.put("compound_title", "rdfs:label");
		fields_map.put("responsible", "v-s:correspondentPerson");
		fields_map.put("department", "v-s:correspondentDepartment");
		fields_map.put("responsible_add",  "v-s:correspondentPersonDescription");
		fields_map.put("date_born", "v-s:birthday");
		fields_map.put("trade_mark", "v-s:hasVehicleModel");
		fields_map.put("classifier_product", "v-s:hasVehicleCategory");
		fields_map.put("description", "mnd-s:passPassengerList");
		fields_map.put("resource", "mnd-s:passEquipment");
		
		fields_map.put("addresse_from", "?");
		fields_map.put("comment", "?");
		fields_map.put("contractor", "?");
		fields_map.put("owner", "?");
		
		fields_map.put("from_doc_certificate_number_reg", "?");
	}
	
	private String changeNum(String num) {
		String digits = "0123456789";
		String outp = "";
		
		Boolean prev_digit = false;
		for (int i = 0; i < num.length(); i++) {
			String smb = num.substring(i, i + 1);
			if (digits.indexOf(smb) >= 0 && !prev_digit) {
				outp += " ";
				prev_digit = true;
			} else if (digits.indexOf(smb) < 0 && prev_digit) {
				outp += " ";
				prev_digit = false;
			}
			outp += smb;
		}
		
		return outp.trim();
	}
	
	public List<Individual> transform(int level, XmlDocument doc, String ba_id, String parent_veda_doc_uri,
			String parent_ba_doc_id, String path) throws Exception {
		String uri = prepare_uri(ba_id);
		List<Individual> res = new ArrayList<Individual>();

		Individual new_individual = new Individual();
		new_individual.setUri(uri);

		set_basic_fields(level, new_individual, doc);

		new_individual.addProperty("rdf:type", to_class, Type._Uri);
		
//		if (parent_ba_doc_id != null)
//			new_individual.addProperty("v-s:parent", new Resource("d:" + parent_ba_doc_id, Type._Uri));
		
		List<XmlAttribute> atts = doc.getAttributes();
		for (XmlAttribute att : atts) {
			String code = att.getCode();

			String predicate = fields_map.get(code);
			System.out.println("CODE PASS: " + code);
			if (predicate != null) {
				Resources rss = ba_field_to_veda(level, att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri,
						true);

				if (predicate.equals("?") == false)
					new_individual.addProperty(predicate, rss);
				
				if (rss == null)
					continue;
				
				if (rss.resources.size() < 1)
					continue;
				
				if (code.equals("addresse_from")) {
					if (att.getLinkValue() == null) 
						continue;
					Individual individual = veda.getIndividual("d:" + att.getLinkValue());
					if (individual == null)
						continue;
					Resources resources = individual.getResources("v-s:backwardTarget");
					if (resources == null)
						continue;
					new_individual.addProperty("v-s:customer", resources);
				} else if (code.equals("comment")) {
					Individual comment = new Individual();
					comment.setUri(new_individual.getUri() + "_comment");
					comment.addProperty("rdfs:label", rss);
					comment.addProperty("rdf:type", new Resource("v-s:Comment", Type._Uri));
					comment.addProperty("v-s:creator", new_individual.getResources("v-s:creator"));
					comment.addProperty("v-s:created", new_individual.getResources("v-s:created"));
					putIndividual(level, comment, ba_id);
					new_individual.addProperty("v-s:hasComment", new Resource(comment.getUri(), Type._Uri));
					
				} else if (code.equals("contractor")) {
					if (att.getLinkValue() == null) 
						continue;
					Individual individual = veda.getIndividual("d:" + att.getLinkValue());
					if (individual == null)
						continue;
					Resources resources = individual.getResources("v-s:backwardTarget");
					if (resources == null)
						continue;
					
					
					
					new_individual.addProperty("v-s:correspondentOrganization", resources);
				} else if (code.equals("owner")) {
					if (att.getLinkValue() == null) 
						continue;
					Individual individual = veda.getIndividual("d:" + att.getLinkValue());
					if (individual == null)
						continue;
					Resources resources = individual.getResources("v-s:backwardTarget");
					if (resources == null)
						continue;
					new_individual.addProperty("v-s:owner", resources);
				} else if (code.equals("from_doc_certificate_number_reg")) {
					String num = att.getTextValue();
//					num = num.replaceAll("/\\s*(\\d+)\\s*/gi", " $1 ");
					num = changeNum(num);
					new_individual.addProperty("mnd-s:passVehicleRegistrationNumber", new Resource(num));
				}
			}
		}
		
		res.add(new_individual);
		return res;
	}
}
