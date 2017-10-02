package sm.tools.ba2veda.impl;

import java.util.ArrayList;
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

public class _e5312_mnd_s_ActFailureReason extends Ba2VedaTransform {
	public _e5312_mnd_s_ActFailureReason(BaSystem _ba, VedaConnection _veda, Replacer replacer)  {
		super(_ba, _veda, replacer, "e53127f26d2042868e62f8383d65caaf", "mnd-s:ActFailureReason");
	}
	
	public void inital_set() {
		fields_map.put("direct", "mnd-s:hasDirectionOfActFailureReason");
		fields_map.put("description", "v-s:description");
		fields_map.put("s1", "mnd-s:why1");
		fields_map.put("s2", "mnd-s:why2");
		fields_map.put("s3", "mnd-s:why3");
		fields_map.put("s4", "mnd-s:why4");
		fields_map.put("s5", "mnd-s:why5");
		fields_map.put("reason", "mnd-s:hasCatalogOfActFailureReason");
		fields_map.put("failure_act", "v-s:hasParentLink");
		
		fields_map.put("decision", "?");
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
				
				if (code.equals("decision"))
					if (att.getRecordIdValue().equals("67c37a15c1bc4bfaa92e16ff09c3a349"))
						new_individual.addProperty("v-s:hasStatus", new Resource("v-s:StatusConfirmed", Type._Uri));
					else
						new_individual.addProperty("v-s:hasStatus", new Resource("v-s:StatusNotConfirmed", Type._Uri));
			}
		}
		
		if (new_individual.getResources("v-s:hasStatus") == null)
			new_individual.addProperty("v-s:hasStatus", new Resource("v-s:StatusInvestigation", Type._Uri));
		
		String[] langs_out1 = { "EN", "RU" };
		String[] langs_out2 = { "NONE" };
		
//		Сформировать (rdfs:label индивида  в mnd-s:hasCatalogOfActFailureReason) + ' - ' + v-s:description
		ArrayList<Object> label_parts = new ArrayList<Object>();
		if (new_individual.getResources("mnd-s:hasCatalogOfActFailureReason") != null) {
			Individual i = veda.getIndividual(new_individual.getResources("mnd-s:hasCatalogOfActFailureReason").resources.get(0).getData());
			if (i != null) {
				if (i.getResources("rdfs:label") != null) {
					label_parts.add(i.getResources("rdfs:label"));
					label_parts.add(" - ");
				}
			}
		}
		
		if (new_individual.getResources("v-s:description") != null)
			label_parts.add(new_individual.getResources("v-s:description"));

		Resources rss = rs_assemble(label_parts.toArray(), langs_out1);
		if (rss.resources.size() == 0)
			rss = rs_assemble(label_parts.toArray(), langs_out2);
			
		new_individual.addProperty("rdfs:label", rss);
		res.add(new_individual);
		return res;
	}
}
