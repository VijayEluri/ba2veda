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

public class _a5931_mnd_s_DescriptionOfChange extends Ba2VedaTransform {
	public _a5931_mnd_s_DescriptionOfChange(BaSystem _ba, VedaConnection _veda, Replacer replacer)  {
		super(_ba, _veda, replacer, "a593174e33ac4b8c8a0c5b0ee8216f75", "mnd-s:DescriptionOfChange");
	}
	
	public void inital_set() {
		fields_map.put("asis", "v-s:asis");
		fields_map.put("tobe", "v-s:tobe");
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

			}
		}
		
		res.add(new_individual);
		return res;
	}
}
