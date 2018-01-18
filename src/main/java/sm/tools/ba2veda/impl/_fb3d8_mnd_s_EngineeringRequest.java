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

public class _fb3d8_mnd_s_EngineeringRequest extends Ba2VedaTransform {
	public _fb3d8_mnd_s_EngineeringRequest(BaSystem _ba, VedaConnection _veda, Replacer replacer) {
		super(_ba, _veda, replacer, "fb3d8a9d4c42465397a18a27a9e58580", "mnd-s:EngineeringRequest");
	}
	
	public void inital_set() {
		fields_map.put("Номер", "v-s:registrationNumber");
		fields_map.put("Цех", "mnd-s:engineeringRequestBuilding");
		fields_map.put("Объект ТОРО", "v-s:hasMaintainedObject");
		fields_map.put("Ответственное лицо", "v-s:initiator");
		
		fields_map.put("Наименование", "v-s:theme");
		fields_map.put("Описание работ", "v-s:content");
		fields_map.put("Тип работ", "v-s:hasBudgetCategory");
		fields_map.put("Вложение", "v-s:attachment");
		fields_map.put("add_doc", "v-s:hasLink");
		fields_map.put("status_execution", "v-s:hasStatus");
		fields_map.put("date_from", "v-s:dateFrom");
		fields_map.put("date_to", "v-s:dateTo");
		fields_map.put("comment", "rdfs:comment");
		
		fields_map.put("Комментарий", "?");
		fields_map.put("Разработчик", "mnd-s:engineeringRequestDeveloper");
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
		
		List<XmlAttribute> atts = doc.getAttributes();
		for (XmlAttribute att : atts) {
			String code = att.getCode();

			String predicate = fields_map.get(code);
			System.out.println("CODE: " + code);
			if (predicate != null) {
				Resources rss = ba_field_to_veda(level, att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri,
						true);
				
				if (code.equals("Тип работ") && rss != null) {
					int a = 2 + 2;
					a  = 2 + a;
				}
				
				if (predicate.equals("?") == false) 
					new_individual.addProperty(predicate, rss);
				
				if (rss == null)
					continue;
				
				if (rss.resources.size() < 1)
					continue;
				
				if (code.equals("Комментарий")) {
					Individual comment = new Individual();
					comment.setUri(new_individual.getUri() + "_comment");
					comment.addProperty("rdf:type", new Resource("v-s:Comment"));
					comment.addProperty("v-s:created", new_individual.getResources("v-s:created"));
					comment.addProperty("v-s:creator", new_individual.getResources("v-s:creator"));
					comment.addProperty("rdfs:label", rss);
					new_individual.addProperty("v-s:hasComment", new Resource(comment.getUri(), Type._Uri));
					putIndividual(level, comment, null);
				}
			}
		}
		
		res.add(new_individual);
		return res;
	}
}