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

public class _bcc80_mnd_s_AuditSafetyRating extends Ba2VedaTransform{
	public  _bcc80_mnd_s_AuditSafetyRating(BaSystem _ba, VedaConnection _veda, Replacer replacer) {
		super(_ba, _veda, replacer, "bcc80c192e1d45d6a1351dd82284b1c5", "mnd-s:AuditSafetyRating");
	}
	
	public void inital_set() {
		fields_map.put("comment", "rdfs:comment");
		fields_map.put("grade", "v-s:hasRatingLevel");
		fields_map.put("add_to_doc", "v-s:backwardTarget");
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
		
		new_individual.addProperty("v-s:backwardProperty", new Resource("mnd-s:hasAuditSafetyRating", 
			Type._Uri));
		new_individual.addProperty("v-s:omitBackwardTargetGroup", new Resource("false", Type._Bool));
		
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
			}
		}
		

		res.add(new_individual);
		return res;
	}
}
