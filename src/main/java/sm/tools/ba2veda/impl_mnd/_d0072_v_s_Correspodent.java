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

public class _d0072_v_s_Correspodent extends Ba2VedaTransform {
	public _d0072_v_s_Correspodent(BaSystem _ba, VedaConnection _veda, Replacer replacer) {
		super(_ba, _veda, replacer, "d0072c0a5a9f47308c3b23af968b11b2", "v-s:Correspondent");
	}
	
	public void inital_set() {
		fields_map.put("employee", "v-s:correspondentPersonDescription");
		
		fields_map.put("organization", "?");
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
		new_individual.addProperty("v-s:parent", new Resource(parent_veda_doc_uri, Type._Uri));
		
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
				
				if (code.equals("organization")) {
					if (att.getLinkValue() == null) 
						continue;
					Individual individual = veda.getIndividual("d:" + att.getLinkValue());
					if (individual == null)
						continue;
					Resources resources = individual.getResources("v-s:backwardTarget");
					if (resources == null)
						continue;
					new_individual.addProperty("v-s:correspondentOrganization", resources);
				}
			}
		}
		
		new_individual.addProperty("rdf:type", to_class, Type._Uri);
		res.add(new_individual);
		return res;
	}
}
