package sm.tools.ba2veda.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

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

public class _7a775_mnd_s_ProjectCapexRequestChange extends Ba2VedaTransform {
	public _7a775_mnd_s_ProjectCapexRequestChange(BaSystem _ba, VedaConnection _veda, Replacer replacer) {
		super(_ba, _veda, replacer, "7a7756b711864a3a81e8319399df9ef3", "mnd-s:ProjectCapexRequestChange");
	}
	
	public void inital_set() {
		fields_map.put("number", "v-s:registrationNumber");
		fields_map.put("kind", "mnd-s:hasProjectCapexRequestChangeKind");
		fields_map.put("description", "v-s:description");
		fields_map.put("cause", "v-s:reason");
		fields_map.put("data_to", "v-s:dateTo");
		fields_map.put("attachment", "v-s:attachment");
		fields_map.put("compound_title", "rdfs:label");
		fields_map.put("nomenclature", "v-s:parent");
		fields_map.put("effect_to_value", "mnd-s:capexEffectToValue");
		fields_map.put("effect_to_budget", "mnd-s:capexEffectToBudget");
		fields_map.put("effect_to_time", "mnd-s:capexEffectToTime");
		fields_map.put("effect_to_safety", "mnd-s:capexEffectToSafety");
		fields_map.put("date_to", "v-s:dateTo");
		
		fields_map.put("inherit_rights_from", "?");
		fields_map.put("add_info", "v-s:hasComment");
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
		
		new_individual.addProperty("v-s:omitBackwardTargetGroup", new Resource(false, Type._Bool));
		new_individual.addProperty("v-s:backwardProperty", new Resource("mnd-s:hasProjectCapexRequestChange", Type._Uri));
		
		List<XmlAttribute> atts = doc.getAttributes();
		int ncomments = 1;
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
				
				if (code.equals("inherit_rights_from")) {
					String irf = att.getLinkValue();
					XmlDocument irf_doc = ba.getActualDocument(irf).getLeft();
					String inherit_rights_from = ba.get_first_value_of_field(irf_doc, "inherit_rights_from");
					
					if (inherit_rights_from == null)
					{
						new_individual.addProperty("v-s:backwardTarget", rss);
					} 
					else
					{
						if (irf_doc.getTypeId().equals("ead1b2fa113c45a8b79d093e8ec14728")
								|| irf_doc.getTypeId().equals("15206d33eafa440c84c02c8d912bce7a")
								|| irf_doc.getTypeId().equals("ec6d76a99d814d0496d5d879a0056428")
								|| irf_doc.getTypeId().equals("a0e50600ebe9450e916469ee698e3faa")
								|| irf_doc.getTypeId().equals("71e3890b3c77441bad288964bf3c3d6a")
								|| irf_doc.getTypeId().equals("cab21bf8b68a4b87ac37a5b41adad8a8")
								|| irf_doc.getTypeId().equals("110fa1f351e24a2bbc187c872b114ea4")
								|| irf_doc.getTypeId().equals("d539b253cb6247a381fb51f4ee34b9d8")
								|| irf_doc.getTypeId().equals("a7b5b15a99704c9481f777fa941506c0")
								|| irf_doc.getTypeId().equals("67588724c4c54b25a2c84906613bd15a"))
						{
							att.setLinkValue(inherit_rights_from);
							Resources rss1 = ba_field_to_veda(level, att, uri, inherit_rights_from, doc, path, parent_ba_doc_id, parent_veda_doc_uri,
									true);

							new_individual.addProperty("v-s:backwardTarget", rss1);
						}
						else
						{
							new_individual.addProperty("v-s:backwardTarget", rss);
						}
					}
				}
			}
		}
		
		res.add(new_individual);
		return res;
	}
}
