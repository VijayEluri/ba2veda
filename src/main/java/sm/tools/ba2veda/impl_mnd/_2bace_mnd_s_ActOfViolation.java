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

public class _2bace_mnd_s_ActOfViolation extends Ba2VedaTransform {
	public _2bace_mnd_s_ActOfViolation(BaSystem _ba, VedaConnection _veda, Replacer replacer) {
		super(_ba, _veda, replacer, "2bacea4503294b53a4ee8d45d401853a", "mnd-s:ActOfViolation");
	}

	public void inital_set() {
		fields_map.put("date_fact", "v-s:date");
		fields_map.put("number_reg", "v-s:registrationNumber");
		fields_map.put("responsible_add", "v-s:correspondentPersonDescription");
		fields_map.put("classifier_act", "mnd-s:hasActOfViolationKind");
		fields_map.put("area", "mnd-s:ActOfViolationKpp");
		fields_map.put("description", "v-s:description");
		fields_map.put("attachment", "v-s:attachment");
		fields_map.put("compund_title", "rdfs:label");
		
		fields_map.put("contractor", "?");
		fields_map.put("importance", "?");
	}
	
	@Override
	public List<Individual> transform(int level, XmlDocument doc, String ba_id, String parent_veda_doc_uri,
		String parent_ba_doc_id, String path) throws Exception {
		
		String uri = prepare_uri(ba_id);
		List<Individual> res = new ArrayList<Individual>();

		Individual new_individual = new Individual();
		new_individual.setUri(uri);

		set_basic_fields(level, new_individual, doc);
		
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
					
				if (code.equals("importance") && (rss == null || rss.resources.size() < 1))
					new_individual.addProperty("v-s:isActivityAccidental", new Resource(true, Type._Bool));
				
				
				if (rss == null)
					continue;

					
				if (rss.resources.size() < 1)
					continue;
				
				if (rss == null)
					continue;
				
				if (code.equals("contractor")) {
					if (att.getLinkValue() == null) 
						continue;
					Individual individual = veda.getIndividual("d:" + att.getLinkValue());
					if (individual == null)
						continue;
					Resources resources = individual.getResources("v-s:backwardTarget");
					if (resources == null)
						continue;
					new_individual.addProperty("v-s:correspondentOrganization", resources);
				} else if (code.equals("importance")) {
					int a = Integer.parseInt(att.getNumberValue());
					if (a == 0) 
						new_individual.addProperty("v-s:isActivityAccidental", new Resource(true, Type._Bool));
					else 
						new_individual.addProperty("v-s:isActivityAccidental", new Resource(false, Type._Bool));
				}
			}
		}
			
		new_individual.addProperty("rdf:type", to_class, Type._Uri);
		res.add(new_individual);
		return res;
	}

}
