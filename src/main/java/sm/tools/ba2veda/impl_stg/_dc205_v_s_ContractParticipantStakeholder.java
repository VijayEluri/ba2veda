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

public class _dc205_v_s_ContractParticipantStakeholder extends Ba2VedaTransform
{
	public _dc205_v_s_ContractParticipantStakeholder(BaSystem _ba, VedaConnection _veda, Replacer replacer)
	{
		super(_ba, _veda, replacer, "dc205f55fd8f435da8968e6cbbcd4149", "v-s:ContractParticipantStakeholder");
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
		List<Individual> res = new ArrayList<Individual>();

		String register_type = ba.get_first_value_of_field(doc, "register_type");
		String owner = ba.get_first_value_of_field(doc, "owner");
		String id_1c = ba.get_first_value_of_field(doc, "id_1c");

		if (register_type != null && id_1c != null)
			return res;

		Individual new_individual = new Individual();
		new_individual.setUri(uri);

		set_basic_fields(level, new_individual, doc);

		new_individual.addProperty("rdf:type", to_class, Type._Uri);
		//new_individual.addProperty("v-s:omitBackwardTarget", new Resource(false, Type._Bool));
		//new_individual.addProperty("v-s:backwardProperty", new Resource("mnd-s:hasDecreeRegistrationRecord", Type._Uri));

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

		new_individual.addProperty("v-s:hasRoleInContract", new Resource("d:dm44s17tayal8frdutpnb2matk", Type._Uri));

		new_individual.setProperty("v-s:hasContractor", new Resource("d:" + contractor, Type._Uri));

		new_individual.addProperty("v-s:backwardTarget", new Resource(uri0, Type._Uri));
		new_individual.addProperty("v-s:backwardProperty", new Resource("s:hasContractParticipantStakeholder", Type._Uri));

		res.add(new_individual);
		return res;
	}
}
