package sm.tools.ba2veda.impl;

import java.sql.ResultSet;
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

public class _73d7c_mnd_s_Equipmentlocation extends Ba2VedaTransform {
	public _73d7c_mnd_s_Equipmentlocation(BaSystem _ba, VedaConnection _veda, Replacer replacer)  {
		super(_ba, _veda, replacer, "73d7cc09-71a6-47a3-8454-8e4bbe5e514d", "mnd-s:Equipmentlocation");
	}
	
	public void inital_set() {
		fields_map.put("1b073c10-91fb-451e-b636-8c5bfe77c598_4", "v-s:shortLabel");
		fields_map.put("1b073c10-91fb-451e-b636-8c5bfe77c598_5", "rdfs:label");
		
		fields_map.put("1b073c10-91fb-451e-b636-8c5bfe77c598_7", "?");
		fields_map.put("1b073c10-91fb-451e-b636-8c5bfe77c598_2", "?");
		fields_map.put("1b073c10-91fb-451e-b636-8c5bfe77c598_3", "?");
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
					
				if (code.equals("importance") && (rss == null || rss.resources.size() < 1))
					new_individual.addProperty("v-s:isActivityAccidental", new Resource(true, Type._Bool));
				
				
				if (rss == null)
					continue;
				
				if (rss.resources.size() < 1)
					continue;

				if (code.equals("1b073c10-91fb-451e-b636-8c5bfe77c598_2")) {
					new_individual.setUri("d:" + att.getTextValue());
				} else if (code.equals("1b073c10-91fb-451e-b636-8c5bfe77c598_7")) {
					if (!att.getTextValue().equals("Техническое место")) 
						return res;
				} else if (code.equals("1b073c10-91fb-451e-b636-8c5bfe77c598_3")) {
					String parent_id = att.getTextValue().substring(1).replace(".", "_");
					String query = String.format("select * from mapper where synchronizerId ='toro' and sapR3Id like '%%%s' limit 100", 
						parent_id);
					String ddsid =  ba.gedDdsidFromSynchronizationViaQuery(query);
					new_individual.addProperty("v-s:hasParentLink", new Resource("d:" + ddsid, Type._Uri));
				}
					
				
			}
		}
			
		new_individual.addProperty("rdf:type", to_class, Type._Uri);
		res.add(new_individual);
		return res;
	}

}
