package sm.tools.ba2veda.impl_mnd;

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

public class _252bf_mnd_s_LocalRegulatoryDocument extends Ba2VedaTransform {
	public _252bf_mnd_s_LocalRegulatoryDocument(BaSystem _ba, VedaConnection _veda, Replacer replacer)  {
		super(_ba, _veda, replacer, "252bfb50f42c44ccacf3fc108dd9bff6", "mnd-s:LocalRegulatoryDocument");
	}
	
	public void inital_set() {
		fields_map.put("classifier_doc", "v-s:hasDocumentKind");
		fields_map.put("number_reg", "v-s:registrationNumber");
		fields_map.put("title", "v-s:title");
		fields_map.put("comment", "rdfs:comment");
		fields_map.put("nomenclature", "v-s:owner");
		fields_map.put("kind", "v-s:hasSector");
		fields_map.put("scope", "mnd-s:appliesTo");
		fields_map.put("add_doc_version", "v-s:hasVersionOfLocalRegulatoryDocument");
		fields_map.put("attachment_order", "v-s:isDefinedBy");
		fields_map.put("compound_title", "rdfs:label");
		
		fields_map.put("actual_to", "?");
		fields_map.put("access_level", "?");
	}
	
	@Override
	public List<Individual> transform(int level, XmlDocument doc, String ba_id, String parent_veda_doc_uri,
		String parent_ba_doc_id, String path) throws Exception {
		
		String uri = prepare_uri(ba_id);
		List<Individual> res = new ArrayList<Individual>();

		Individual new_individual = new Individual();
		new_individual.setUri(uri);

		set_basic_fields(level, new_individual, doc);
		
		new_individual.addProperty("rdf:type", new Resource(to_class, Type._Uri));

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
				
				if (code.equals("actual_to")) {
					if (att.getRecordIdValue().equals("67c37a15c1bc4bfaa92e16ff09c3a349"))
						new_individual.addProperty("v-s:valid", new Resource(true, Type._Bool));
					else 
						new_individual.addProperty("v-s:valid", new Resource(false, Type._Bool));
				} else if (code.equals("access_level")) {
					if (att.getRecordIdValue().equals("e156798f959744cd8ef06c012c5553b3"))
						new_individual.addProperty("mnd-s:isAccessLimited", new Resource(true, Type._Bool));
					else
						new_individual.addProperty("mnd-s:isAccessLimited", new Resource(false, Type._Bool));
				}
			}
		}
		
		if (new_individual.getResources("v-s:valid") == null)
			new_individual.addProperty("v-s:valid", new Resource(false, Type._Bool));
		
		if (new_individual.getResources("mnd-s:isAccessLimited") == null)
			new_individual.addProperty("mnd-s:isAccessLimited", new Resource(false, Type._Bool));
		
		res.add(new_individual);
		return res;
	}
}
