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

public class _689c5_mnd_s_Addendum extends Ba2VedaTransform{
	public _689c5_mnd_s_Addendum(BaSystem _ba, VedaConnection _veda, Replacer replacer)  {
		super(_ba, _veda, replacer, "689c58f35b52400591ef5d98514a771b", "mnd-s:Addendum");
	}
	
	public void inital_set() {
		fields_map.put("title", "v-s:title");
		fields_map.put("attachment", "v-s:attachment");
		fields_map.put("date_from", "v-s:dateFrom");
		fields_map.put("date_to", "v-s:dateTo");
	
		fields_map.put("actual_to", "?");
		fields_map.put("inherit_rights_from", "?");
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
		new_individual.addProperty("v-s:backwardProperty", new Resource("v-s:hasAddendum", Type._Uri));

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
				
				if (code.equals("actual_to")) {
					if (att.getRecordIdValue().equals("67c37a15c1bc4bfaa92e16ff09c3a349"))
						new_individual.addProperty("v-s:valid", new Resource(true, Type._Bool));
					else 
						new_individual.addProperty("v-s:valid", new Resource(false, Type._Bool));
				} else if (code.equals("inherit_rights_from")) {
					String irf = att.getLinkValue();
					XmlDocument irf_doc = ba.getActualDocument(irf).getLeft();
					if (irf_doc == null)
						continue;
					String inherit_rights_from = ba.get_first_value_of_field(irf_doc, "inherit_rights_from");
					String link_to = irf;
					
					if (inherit_rights_from != null)
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
							link_to = inherit_rights_from;
					
					
					new_individual.addProperty("v-s:backwardTarget", new Resource(link_to, Type._Uri));
				}
			}
		}
		
		if (new_individual.getResources("v-s:valid") == null)
			new_individual.addProperty("v-s:valid", new Resource(false, Type._Bool));
		
		res.add(new_individual);
		return res;
	}
}
