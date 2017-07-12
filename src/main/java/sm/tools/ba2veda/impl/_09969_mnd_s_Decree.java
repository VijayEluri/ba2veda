package sm.tools.ba2veda.impl;

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

public class _09969_mnd_s_Decree extends Ba2VedaTransform {
	public  _09969_mnd_s_Decree(BaSystem _ba, VedaConnection _veda, Replacer replacer) {
		super(_ba, _veda, replacer, "09969d03e3ff4fa38331d4eb8a5089ee", "mnd-s:Decree");
	}
	
	public void inital_set() {
		fields_map.put("Вложение", "v-s:attachment");
		fields_map.put("Заголовок", "v-s:title");
		
		fields_map.put("Регистрационный номер", "?");
		fields_map.put("Дата регистрации", "?");
		
		fields_map.put("Дата создания проекта", "?");
		fields_map.put("Дата документа", "?");
		fields_map.put("Тип документа", "?");
		fields_map.put("Инициатор", "?");
		fields_map.put("Подписывающий", "?");
		fields_map.put("Контроль исполнения возложен на", "?");
		
		fields_map.put("Вид документа", "?");
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
		
		Individual drtr = null;
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
				
				if (code.equals("Регистрационный номер")) {
					String data = rss.resources.get(0).getData();
					if (data .equals(""))
						continue;
					if (drtr == null)
						drtr = new Individual();
					
					
					drtr.addProperty("v-s:registrationNumber", rss);
				} else if (code.equals("Дата регистрации")) {
					String data = rss.resources.get(0).getData();
					if (data.equals(""))
						continue;
					if (drtr == null)
						drtr = new Individual();
					
				
					drtr.addProperty("v-s:registrationDate", rss);
				} else if (code.equals("Дата создания проекта") || 
					code.equals("Дата документа") ||
					code.equals("Тип документа") ||
					code.equals("Инициатор") || 
					code.equals("Подписывающий") ||
					code.equals("Контроль исполнения возложен на")) {
					if (comment_parts.size() > 0) {
						comment_parts.add("\n");
					}
					
					String elem = code + ": " + rss.resources.get(0).getData();
					Resources elem_resource = new Resources();
					elem_resource.add(new Resource(elem, Type._String));
					comment_parts.add(elem_resource);					
				} else if (code.equals("Вид документа")) {
					String doc_type = att.getTextValue();
					if (doc_type.equals("Приказы по основной деятельности"))
						new_individual.addProperty("mnd-s:hasDecreeKind", 
							new Resource("d:b7a958be3d14437f98ab414c8ce88977", Type._Uri));
					else if (doc_type.equals("Распоряжения по основной деятельности"))
						new_individual.addProperty("mnd-s:hasDecreeKind", 
							new Resource("d:2283f9f186794145bd6c8e3229f7a009", Type._Uri));
				}
			}
		}
		
		
		if (drtr != null) {
			drtr.setUri(new_individual.getUri() + "_registration_record");
			drtr.addProperty("v-s:author", new Resource("d:rimert_DocRegistrator_SLPK", Type._Uri));
			drtr.addProperty("v-s:created", new_individual.getResources("v-s:created"));
			drtr.addProperty("rdf:type", new Resource("mnd-s:DecreeRegistrationRecord", Type._Uri));
			new_individual.addProperty("mnd-s:hasDecreeRegistrationRecord", new Resource(drtr.getUri(), Type._Uri));
			putIndividual(drtr, ba_id, true);
		}
		
		String[] langs_out1 = { "EN", "RU" };
		String[] langs_out2 = { "NONE" };
		
		Resources rss = rs_assemble(comment_parts.toArray(), langs_out1);
		if (rss.resources.size() == 0)
			rss = rs_assemble(comment_parts.toArray(), langs_out2);
			
		if (rss.resources.size() > 0) {
			Individual comment = new Individual();
			comment.setUri(new_individual.getUri() + "_comment");
			comment.addProperty("rdf:type", new Resource("v-s:Comment", Type._Uri));
			comment.addProperty("v-s:creator", new_individual.getResources("v-s:creator"));
			comment.addProperty("v-s:created", new_individual.getResources("v-s:created"));
			comment.addProperty("rdfs:label", rss);
			new_individual.addProperty("v-s:hasComment", new Resource(comment.getUri(), Type._Uri));
			putIndividual(comment, ba_id, true);
		}
		
		res.add(new_individual);
		return res;
	}
}

