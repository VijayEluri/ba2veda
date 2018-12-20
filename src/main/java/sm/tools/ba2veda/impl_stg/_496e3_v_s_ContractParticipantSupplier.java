package sm.tools.ba2veda.impl_stg;

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

/*
Смотрим индивид из поля contract Если у него register_type = 2c4fc8846cb24609bb4f9134d2833991 (Продажа)
1. Создаем индивид класса v-s:ContractParticipantSupplier
2 Дата и автор совпадают
3. в поле v-s:parent - текщий индивид
4. в поле v-s:backwardTarget - текщий индивид
5. поле v-s:backwardProperty - константа v-s:hasContractParticipantSupplier
6. в поле v-s:hasRoleInContract d:wws2oz313z6xpl1hd4u3laxzfg (Исполнитель)
7. в поле v-s:hasOrganization = d:org_RU1121016110_1

Смотрим индивид из поля contract Если у него register_type = 8cf061a51fe44ae5b70bf0ae6447d9a4 (Закупка)
1. Создаем индивид класса v-s:ContractParticipantSupplier
2. Дата и автор совпадают
3. в поле v-s:parent - текщий индивид
4. в поле v-s:backwardTarget - текщий индивид
5. поле v-s:backwardProperty - константа v-s:hasContractParticipantSupplier
6. в поле v-s:hasRoleInContract d:wws2oz313z6xpl1hd4u3laxzfg (Исполнитель)
7. из индивида в поле contractor берем поле inn - по нему выполняем в OF поиск по всем индивидам класса v-s:Organization - находим тот у которого поле v-s:taxId совпадает. Вписываем в поле v-s:hasOrganization
8 индивид из поля contractor помещаем в поле v-s:hasContractor + d: 
 */

public class _496e3_v_s_ContractParticipantSupplier extends Ba2VedaTransform
{
	public _496e3_v_s_ContractParticipantSupplier(BaSystem _ba, VedaConnection _veda, Replacer replacer)
	{
		super(_ba, _veda, replacer, "496e359316334e47b87af4576d36d27c", "v-s:ContractParticipantSupplier");
	}

	public void inital_set()
	{
		fields_map.put("register_type", "?");
		fields_map.put("contractor", "v-s:hasOrganization");
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

		String uri0 = prepare_uri(ba_id);
		String uri = uri0 + "_cps";
		String register_type = null;
		List<Individual> res = new ArrayList<Individual>();

		String contract = ba.get_first_value_of_field(doc, "contract");
		Pair<XmlDocument, Long> contract_doc = ba.getActualDocument(contract);

		String id_1c = null;
		if (contract_doc != null)
			register_type = ba.get_first_value_of_field(contract_doc.getLeft(), "register_type");
		else
			id_1c = ba.get_first_value_of_field(doc, "id_1c");

		if (register_type == null && id_1c == null)
			return res;

		Individual new_individual = new Individual();
		new_individual.setUri(uri);

		set_basic_fields(level, new_individual, doc);

		new_individual.addProperty("rdf:type", to_class, Type._Uri);
		//new_individual.addProperty("v-s:omitBackwardTarget", new Resource(false, Type._Bool));
		//new_individual.addProperty("v-s:backwardProperty", new Resource("mnd-s:hasDecreeRegistrationRecord", Type._Uri));

		String owner = ba.get_first_value_of_field(doc, "owner");
		String contractor = null;

		List<XmlAttribute> atts = doc.getAttributes();
		for (XmlAttribute att : atts)
		{
			String code = att.getCode();

			String predicate = fields_map.get(code);
			System.out.println("CODE: " + code);
			if (predicate != null)
			{
				Resources rss = ba_field_to_veda(level, att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri, true);

				if (code.equals("contractor"))
				{
					contractor = att.getRecordIdValue();
				}

				if (predicate.equals("?") == false)
					new_individual.addProperty(predicate, rss);

				if (rss == null)
					continue;

				if (rss.resources.size() < 1)
					continue;

			}
		}

		new_individual.addProperty("v-s:hasRoleInContract", new Resource("d:wws2oz313z6xpl1hd4u3laxzfg", Type._Uri));

		if (register_type != null && register_type.equals("2c4fc8846cb24609bb4f9134d2833991"))
		{
			if (owner == null || owner.equals("53343a30-449b-4e71-9103-2fcd4bdaafd1"))
				new_individual.setProperty("v-s:hasOrganization", new Resource("d:org_RU1121016110_1", Type._Uri));

			if (owner != null && owner.equals("ecae5139-5aca-41dc-923d-c0aecc941424"))
				new_individual.setProperty("v-s:hasOrganization", new Resource("d:org_RU1121016110_2", Type._Uri));

			if (owner != null && owner.equals("df0ffa73-0740-4c3d-ae98-71b061bfca47"))
				new_individual.setProperty("v-s:hasOrganization", new Resource("d:org_RU7726531163", Type._Uri));

		} else
		{
			// если register_type пусто, то смотрим поле id_1c. В нем текст такой структуры СПоставщиком-17380 или СПокупателем-2898.
			// если id_1c = СПоставщиком*, то действуем как в ветке  register_type = 8cf061a51fe44ae5b70bf0ae6447d9a4 (Закупка)
			// если id_1c = СПокупателем*, то действуем как в ветке  register_type = 2c4fc8846cb24609bb4f9134d2833991 (Продажа)

			if (id_1c != null && id_1c.indexOf("СПокупателем") == 0)
			{
				if (owner == null || owner.equals("53343a30-449b-4e71-9103-2fcd4bdaafd1"))
					new_individual.setProperty("v-s:hasOrganization", new Resource("d:org_RU1121016110_1", Type._Uri));

				if (owner != null && owner.equals("ecae5139-5aca-41dc-923d-c0aecc941424"))
					new_individual.setProperty("v-s:hasOrganization", new Resource("d:org_RU1121016110_2", Type._Uri));

				if (owner != null && owner.equals("df0ffa73-0740-4c3d-ae98-71b061bfca47"))
					new_individual.setProperty("v-s:hasOrganization", new Resource("d:org_RU7726531163", Type._Uri));
			} else
				new_individual.setProperty("v-s:hasContractor", new Resource("d:" + contractor, Type._Uri));
		}

		new_individual.addProperty("v-s:backwardTarget", new Resource(uri0, Type._Uri));
		new_individual.addProperty("v-s:backwardProperty", new Resource("v-s:hasContractParticipantSupplier", Type._Uri));

		res.add(new_individual);
		return res;
	}
}
