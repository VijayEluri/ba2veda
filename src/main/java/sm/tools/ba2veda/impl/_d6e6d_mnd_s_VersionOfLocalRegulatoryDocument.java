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

public class _d6e6d_mnd_s_VersionOfLocalRegulatoryDocument extends Ba2VedaTransform {
	public _d6e6d_mnd_s_VersionOfLocalRegulatoryDocument(BaSystem _ba, VedaConnection _veda, Replacer replacer)  {
		super(_ba, _veda, replacer, "d6e6d10816cd4234921000b48fa593b3", "mnd-s:VersionOfLocalRegulatoryDocument");
	}
	
	public void inital_set() {
		fields_map.put("classifier_doc", "v-s:hasDocumentKind");
		fields_map.put("nomenclature", "v-s:backwardTarget");
		fields_map.put("number_reg", "v-s:registrationNumber");
		fields_map.put("title", "v-s:title");
		fields_map.put("date_reg", "v-s:registrationDate");
		fields_map.put("date_from", "v-s:dateFrom");
		fields_map.put("date_to", "v-s:dateTo");
		fields_map.put("signer", "v-s:signedBy");
		fields_map.put("department", "v-s:initiator");
		fields_map.put("attachment", "v-s:attachment");
		fields_map.put("add_doc_appendix", "v-s:hasAddendum");
		fields_map.put("comment", "rdfs:comment");
		fields_map.put("change_log", "v-s:hasDescriptionOfChange");
		fields_map.put("add_to_doc", "v-s:isDefinedBy");
		fields_map.put("add_info", "v-s:hasComment");
		
		
		fields_map.put("actual_to", "?");
		fields_map.put("inherit_rights_from", "?");
//		fields_map.put("inherit_rights_from", "?");
		fields_map.put("compound_title", "rdfs:label");
	}
	
	@Override
	public List<Individual> transform(XmlDocument doc, String ba_id, String parent_veda_doc_uri,
		String parent_ba_doc_id, String path) throws Exception {
		
		String uri = prepare_uri(ba_id);
		List<Individual> res = new ArrayList<Individual>();

		Individual new_individual = new Individual();
		new_individual.setUri(uri);

		set_basic_fields(new_individual, doc);
		
		new_individual.addProperty("rdf:type", new Resource(to_class, Type._Uri));
		new_individual.addProperty("v-s:backwardProperty", 
				new Resource("v-s:hasVersionOfLocalRegulatoryDocument", Type._Uri));

		List<XmlAttribute> atts = doc.getAttributes();
		for (XmlAttribute att : atts) {
			String code = att.getCode();

			String predicate = fields_map.get(code);
			System.out.println("CODE: " + code);
			
			if (predicate != null) {
				Resources rss = ba_field_to_veda(att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri,
						true);
					
				if (predicate.equals("?") == false)
					new_individual.addProperty(predicate, rss);
				
				if (rss == null)
					continue;
				
				if (rss.resources.size() < 1)
					continue;
				
				if (code.equals("actual_to")) {
					if (att.getRecordIdValue().equals("67c37a15c1bc4bfaa92e16ff09c3a349"))
						new_individual.addProperty("v-s:valid", new Resource(true, Type._Bool));
					else 
						new_individual.addProperty("v-s:valid", new Resource(false, Type._Bool));
				}
			}
		}
		
		if (new_individual.getResources("v-s:valid") == null)
			new_individual.addProperty("v-s:valid", new Resource(false, Type._Bool));
		
		res.add(new_individual);
		return res;
	}
}
