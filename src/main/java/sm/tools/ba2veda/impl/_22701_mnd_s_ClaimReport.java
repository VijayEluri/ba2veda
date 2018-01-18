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

public class _22701_mnd_s_ClaimReport extends Ba2VedaTransform {
	public _22701_mnd_s_ClaimReport(BaSystem _ba, VedaConnection _veda, Replacer replacer)  {
		super(_ba, _veda, replacer, "227010a3c26c4ecb96ec65ac4f26bba1", "mnd-s:ClaimReport");
	}

	public void inital_set() {
		fields_map.put("stage", "v-s:hasStatus");
		fields_map.put("comment", "rdfs:comment");
		fields_map.put("resolution", "v-s:hasDecision");
		fields_map.put("file", "v-s:attachment");
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
		
		new_individual.addProperty("v-s:backwardTarget", new Resource(parent_veda_doc_uri, Type._Uri));
		new_individual.addProperty("v-s:parent", new Resource(parent_veda_doc_uri, Type._Uri));
		new_individual.addProperty("v-s:backwardProperty", new Resource("mnd-s:hasAuditQualityReport"));
		
		XmlDocument parent = ba.getActualDocument(parent_ba_doc_id).getLeft();
		ArrayList<Object> label = new ArrayList<Object>();
		
		for (XmlAttribute att : parent.getAttributes()) {
			if (att.getCode().equals("compound_title"))
				label.add(ba_field_to_veda(level, att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri,
						true));
		}
		
		if (label.size() > 0) 
			label.add(",");
		label.add(new_individual.getResources("v-s:created"));
		
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
			}
		}
		
		String[] langs_out1 = { "EN", "RU" };
		String[] langs_out2 = { "NONE" };
		
		Resources rss = rs_assemble(label.toArray(), langs_out1);
		if (rss.resources.size() == 0)
			rss = rs_assemble(label.toArray(), langs_out2);
			
		if (rss.resources.size() > 0)
			new_individual.addProperty("rdfs:label", rss);
			
		
		res.add(new_individual);
		return res;
	}
}
