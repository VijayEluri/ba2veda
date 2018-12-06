package sm.tools.ba2veda.impl_stg;

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

/*

создаем индивид класса stg:ContractRegistrationRecord
1. автор и дата совпадают с текущим документом
2. v-s:backwardProperty - константа v-s:hasRegistrationRecord
3. v-s:backwardTarget заполняем индивидом из поля inherit_rights_from (Это Проект К -> Контракт)
4. v-s:parent заполняем индивидом из поля inherit_rights_from (Это Проект К -> Контракт)
5. stg:hasOriginalSource заполняем значением из поля original_source
6. в поле v-s:attachment добавляем файлы из поля signed_document

 */

public class _dc205_stg_ContractRegistrationRecord extends Ba2VedaTransform
{
	public _dc205_stg_ContractRegistrationRecord(BaSystem _ba, VedaConnection _veda, Replacer replacer)
	{
		super(_ba, _veda, replacer, "dc205f55fd8f435da8968e6cbbcd4149", "stg:ContractRegistrationRecord");
	}

	public void inital_set()
	{
		fields_map.put("original_source", "stg:hasOriginalSource");
		fields_map.put("signed_document", "v-s:attachment");
		fields_map.put("inherit_rights_from", "?");
	}

	@Override
	public List<Individual> transform(int level, XmlDocument doc, String ba_id, String parent_veda_doc_uri, String parent_ba_doc_id, String path)
			throws Exception
	{
		employee_prefix = "d:employee_";
		appointment_prefix = "d:";
		stand_prefix = "d:";
		department_prefix = "department";
		is_mondi = false;

		List<Individual> res = new ArrayList<Individual>();

		String inherit_rights_from = ba.get_first_value_of_field(doc, "inherit_rights_from");
		//if (inherit_rights_from == null)
		//	return new ArrayList<Individual>();

		//		String uri0 = prepare_uri(ba_id);
		String uri0 = "d:" + inherit_rights_from;

		if (inherit_rights_from == null)
			return res;
		//			uri0 = prepare_uri(ba_id);

		String uri = uri0 + "_crr";

		Individual new_individual = new Individual();
		new_individual.setUri(uri);

		set_basic_fields(level, new_individual, doc);

		new_individual.addProperty("rdf:type", to_class, Type._Uri);

		//new_individual.addProperty("v-s:omitBackwardTarget", new Resource(false, Type._Bool));
		//new_individual.addProperty("v-s:backwardProperty", new Resource("mnd-s:hasDecreeRegistrationRecord", Type._Uri));

		List<XmlAttribute> atts = doc.getAttributes();
		for (XmlAttribute att : atts)
		{
			String code = att.getCode();

			String predicate = fields_map.get(code);
			System.out.println("CODE: " + code);
			if (predicate != null)
			{
				Resources rss = ba_field_to_veda(level, att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri, true);

				if (predicate.equals("?") == false)
					new_individual.addProperty(predicate, rss);

				if (rss == null)
					continue;

				if (rss.resources.size() < 1)
					continue;

			}
		}

		//		new_individual.addProperty("v-s:hasRoleInContract", new Resource("d:dsxyd1uxsxuui6f1t3s4ki3rdp", Type._Uri));

		fields_map.put("inherit_rights_from", "v-s:parent");

		new_individual.addProperty("v-s:parent", new Resource(uri0, Type._Uri));
		new_individual.addProperty("v-s:backwardTarget", new_individual.getResources("v-s:parent"));
		new_individual.addProperty("v-s:backwardProperty", new Resource("v-s:hasRegistrationRecord", Type._Uri));

		res.add(new_individual);
		return res;
	}
}
