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

public class _da0e6_v_s_Comment extends Ba2VedaTransform {
	public _da0e6_v_s_Comment(BaSystem _ba, VedaConnection _veda, Replacer replacer)  {
		super(_ba, _veda, replacer, "da0e63b08cdc4aae81070b59be1d135c", "v-s:Comment");
	}
	
	public void inital_set() {
		/*
		 * 1. v-s:created, v-s:creator - совпадают с датой создания и автором документа в СЭД 
2. v-s:backwardTarget заполнить "d:" + ID найденного индивида 
3. v-s:backwardProperty заполнить строкой v-s:hasComment
4. v-s:attachment заполнить файлом из поля add_attachment найденного индивида
5. rdfs:comment собрать из нескольких полей по шаблону:
строка "Простой оборудования (ч)" +" " +  damage_hour (значение из поля в найд.док-те)
строка "Потеря продукции (т)" +" " +   damage_product (значение из поля в найд.док-те)
строка "Затраты (Запасные части, привлечение ремонтного персонала), (руб)" +" " +   damage_cost (значение из поля в найд.док-те)
		 */
		
//		fields_map.put("v-s:creator", "?");
		fields_map.put("add_attachment", "?");
		fields_map.put("attachment", "?");
		fields_map.put("damage_hour", "?");
		fields_map.put("damage_product", "?");
		fields_map.put("damage_cost", "?");
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
		List<XmlAttribute> atts = doc.getAttributes();
		Boolean need = false;
		

		
		new_individual.setUri("d:" + ba_id + "_comment");
		new_individual.addProperty("v-s:backwardTarget", new Resource("d:"+ba_id, Type._Uri));
		new_individual.addProperty("v-s:backwardProperty", new Resource("v-s:hasComment", Type._Uri));
		Resources dh, dp, dc;
		dh = dp = dc = null;
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
				
				need = true;
				if (code.equals("add_attachment") || code.equals("attachment"))
					new_individual.addProperty("v-s:attachment", rss);
				else if (code.equals("damage_hour"))
					dh = rss;
				else if (code.equals("damage_product")) {
					dp = rss;
				} else if (code.equals("damage_cost")) {
					dc = rss;
				}
			}
		}
		
		/*
		5. rdfs:comment собрать из нескольких полей по шаблону:
		строка "Простой оборудования (ч)" +" " +  damage_hour (значение из поля в найд.док-те)
		строка "Потеря продукции (т)" +" " +   damage_product (значение из поля в найд.док-те)
		строка "Затраты (Запасные части, привлечение ремонтного персонала), (руб)" +" " +   damage_cost (значение из поля в найд.док-те)
				 */
		ArrayList<Object> parts = new ArrayList<Object>();
		if (dh != null) {
			parts.add("Простой оборудования (ч) ");
			parts.add(dh);
		}
		
		if (dp != null) {
			if (parts.size() > 0)
				parts.add("\n");
			parts.add("Потеря продукции (т) ");
			parts.add(dp);
		}
		
		if (dc != null) {
			if (parts.size() > 0)
				parts.add("\n");
			parts.add("Затраты (Запасные части, привлечение ремонтного персонала), (руб) ");
			parts.add(dc);
		}
		
		String[] langs_out1 = { "EN", "RU" };
		String[] langs_out2 = { "NONE" };
		
		Resources rss = rs_assemble(parts.toArray(), langs_out1);
		if (rss.resources.size() == 0)
			rss = rs_assemble(parts.toArray(), langs_out2);
			
		if (rss.resources.size() > 0)
			new_individual.addProperty("rdfs:label", rss);
		
		if (need)
			res.add(new_individual);
		return res;
	}
}
