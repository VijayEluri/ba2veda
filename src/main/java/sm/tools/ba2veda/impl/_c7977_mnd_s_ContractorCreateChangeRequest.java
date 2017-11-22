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

public class _c7977_mnd_s_ContractorCreateChangeRequest extends Ba2VedaTransform {
	public _c7977_mnd_s_ContractorCreateChangeRequest(BaSystem _ba, VedaConnection _veda, Replacer replacer)
	{
		super(_ba, _veda, replacer, "c797754594f4430ba9219aaccb7478ca", "mnd-s:ContractorCreateChangeRequest");
	}

	public void inital_set()
	{
		fields_map.put("currency", "v-s:hasCurrency");
		fields_map.put("payment_terms", "v-s:hasPaymentConditions");
		fields_map.put("incoterms", "v-s:hasDeliveryConditions");
		fields_map.put("attachment", "v-s:attachment ");
		fields_map.put("info", "v-s:mailbox");
		fields_map.put("comment_r", "v-s:description");
		fields_map.put("contractor", "v-s:hasContractor");
		fields_map.put("add_info", "v-s:hasComment");
		fields_map.put("name", "rdfs:label");
		
		fields_map.put("contractor_dossier", "?");
	}
	
	@Override
	public List<Individual> transform(XmlDocument doc, String ba_id, String parent_veda_doc_uri,
			String parent_ba_doc_id, String path) throws Exception {
		String uri = prepare_uri(ba_id);
		List<Individual> res = new ArrayList<Individual>();

		Individual new_individual = new Individual();
		new_individual.setUri(uri);

		set_basic_fields(new_individual, doc);

		new_individual.addProperty("rdf:type", to_class, Type._Uri);
		new_individual.addProperty("v-s:backwardProperty", new Resource("mnd-s:hasContractorCreateChangeRequest", Type._Uri));
		new_individual.addProperty("mnd-s:hasThemeOfContractorCreateChangeRequest", new Resource("d:a75ec9bc7-601f-584c-3064-8b3f63de7806", Type._Uri));
		
		
//		new_individual.addProperty("mnd-s:hasDecreeKind", new Resource("d:e5753b58168843e28ad73855c07b8cff", Type._Uri));
		
		Individual drtr = null;
		Individual comment = null;
		ArrayList<Object> comment_parts = new ArrayList<Object>();
		
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
				
				if (rss == null)
					continue;
				
				if (rss.resources.size() < 1)
					continue;
				
				if (code.equals("contractor_dossier")) {
					try {
						Individual cd = veda.getIndividual(rss.resources.get(0).getData());
						if (cd != null)
						    new_individual.addProperty("v-s:backwardTarget", cd.getResources("v-s:backwardTarget"));
						else
						    System.out.println("ERR! code=contractor_dossier, not found:" + rss.resources.get(0).getData());
					} catch (Exception e) {
						System.err.println(String.format("Bad uri %s", 
							rss.resources.get(0).getData()));
					}
				}
			}
		}
		
		
		res.add(new_individual);
		return res;
	}
}
