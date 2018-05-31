package sm.tools.ba2veda.impl_mnd;

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

public class _xxxxx_v_s_Link extends Ba2VedaTransform {
	public _xxxxx_v_s_Link(BaSystem _ba, VedaConnection _veda, Replacer replacer, String from, String to)
	{
		super(_ba, _veda, replacer, from, to);
	}
	
	public void inital_set() {
//		inherit_rights_from || Связанные документы || add_doc || link_to_doc
		fields_map.put("inherit_rights_from", "?");
		fields_map.put("Связанные документы", "?");
		fields_map.put("add_doc", "?");
		fields_map.put("link_to_doc", "?");
		fields_map.put("link", "?");
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
				
//				inherit_rights_from || Связанные документы || add_doc || link_to_doc
				if (code.equals("inherit_rights_from") || code.equals("Связанные документы") || 
						code.equals("add_doc") || code.equals("link_to_doc") || code.equals("link")) {
					String irf = att.getLinkValue();
//					XmlDocument irf_doc = ba.getActualDocument(irf).getLeft();
//					String inherit_rights_from = ba.get_first_value_of_field(irf_doc, "inherit_rights_from");
					String link_to = irf;
					Individual indiv = veda.getIndividual("d:" + irf);
					if (indiv == null)
						continue;
					String rdftype = indiv.getResources("rdf:type").resources.get(0).getData();
					if (rdftype.equals("mnd-s:ProjectCapex")) {
					
						Individual link = new Individual();
						link.setUri(new_individual.getUri() + "_" + link_to)	;
						link.addProperty("rdf:type", new Resource("v-s:Link", Type._Uri));
						link.addProperty("v-s:from", new Resource(new_individual.getUri(), Type._Uri));
						link.addProperty("v-s:to", new Resource("d:" + link_to, Type._Uri));
						link.addProperty("v-s:creator", new_individual.getResources("v-s:creator"));
						link.addProperty("v-s:created", new_individual.getResources("v-s:created"));
						putIndividual(level, link, ba_id);
					}
				} 
			}
		}
		
		return res;
	}
}
