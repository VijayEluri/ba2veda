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

public class _d5c66_mnd_s_RepresentativeCostsForEvent extends Ba2VedaTransform{
	public _d5c66_mnd_s_RepresentativeCostsForEvent(BaSystem _ba, VedaConnection _veda, Replacer replacer)  {
		super(_ba, _veda, replacer, "d5c663f55f654c6488f7e9165d450c0b", "mnd-s:RepresentativeCostsForEvent");
	}
	
	public void inital_set() {
		fields_map.put("service", "mnd-s:hasRepresentativeCostsKind");
		fields_map.put("costs", "?");
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
				
				if (code.equals("costs")) {
					Individual price = new Individual();
					price.setUri(new_individual.getUri() + "_1");
					price.addProperty("rdf:type", new Resource("v-s:Price", Type._Uri));
					price.addProperty("v-s:sum", rss);
					price.addProperty("v-s:creator", new_individual.getResources("v-s:creator"));
					price.addProperty("v-s:created", new_individual.getResources("v-s:created"));
					price.addProperty("v-s:parent", new Resource(new_individual.getUri(), Type._Uri));
					putIndividual(level, price, ba_id);
					new_individual.addProperty("v-s:hasPrice", new Resource(price.getUri(), Type._Uri));
				}
			}
		}
		
		new_individual.addProperty("rdf:type", to_class, Type._Uri);
		res.add(new_individual);
		return res;
	}
	
}