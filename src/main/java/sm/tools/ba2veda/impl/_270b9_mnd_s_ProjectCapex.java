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

public class _270b9_mnd_s_ProjectCapex extends Ba2VedaTransform{
	public _270b9_mnd_s_ProjectCapex(BaSystem _ba, VedaConnection _veda, Replacer replacer) {
		super(_ba, _veda, replacer, "270b97d0ab204c16973dc27a064f974e", "mnd-s:ProjectCapex");
	}
	
	public void inital_set() {
		fields_map.put("Инвестиционная заявка", "?");
		fields_map.put("Вложения", "?");
		fields_map.put("План управления проектом", "?");
		fields_map.put("Проектная документация", "?");
		fields_map.put("Приемочная документация", "?");
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
		
		List<XmlAttribute> atts = doc.getAttributes();
		Individual individual = null;
		ArrayList<Resources> attachment = new ArrayList<Resources>();
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
				
				if (code.equals("Инвестиционная заявка")) {
					if (att.getLinkValue() == null) 
						continue;
					individual = veda.getIndividual("d:" + att.getLinkValue());
					if (individual == null)
						continue;
				} else if (code.equals("Вложения") || code.equals("Проектная документация") || code.equals("Приемочная документация") ||
						code.equals("План управления проектом"))
					attachment.add(rss);
			}
		}
		
		if (individual != null && attachment.size() > 0) {
			for (int i = 0; i < attachment.size(); i++)
				individual.addProperty("v-s:attachment", attachment.get(i));
			putIndividual(individual, ba_id, true);
		}
		
//		res.add(new_individual);
		return res;
	}
}
