package sm.tools.ba2veda.impl_mnd;

import java.util.ArrayList;
import java.util.List;

import ru.mndsc.bigarchive.server.kernel.document.beans.XmlDocument;
import sm.tools.ba2veda.BaSystem;
import sm.tools.ba2veda.Replacer;
import sm.tools.veda_client.Individual;
import sm.tools.veda_client.Resource;
import sm.tools.veda_client.Resources;
import sm.tools.veda_client.Type;
import sm.tools.veda_client.VedaConnection;

public class _fa740_mnd_s_ContractRequest extends _xxxxx_x_ContractRequest
{
	public _fa740_mnd_s_ContractRequest(BaSystem _ba, VedaConnection _veda, Replacer replacer)
	{
		super(_ba, _veda, replacer, "fa740b46aa414b81a73fb748668257d8", "mnd-s:ContractRequest");
	}

	public void inital_set()
	{
		fields_map.put("number_reg", "v-s:registrationNumber");
		fields_map.put("inherit_rights_from", "v-s:hasProject");
		fields_map.put("kind", "v-s:hasBudgetCategory");
		fields_map.put("date_plan", "v-s:dateTo");
		fields_map.put("theme", "v-s:theme");
		fields_map.put("description_equipment", "mnd-s:contractRequestEquipmentDescription");
		fields_map.put("description_materials", "mnd-s:contractRequestMaterialsDescription");
		fields_map.put("description_services", "mnd-s:contractRequestServicesDescription");
		fields_map.put("contractor", "v-s:supplier");
		fields_map.put("add_info_purchase_requirement", "v-s:hasComment");
		fields_map.put("add_info_long_list", "");

		default_values_map.put("v-s:responsibleDepartment", new Resource("d:mondi_department_50003579", Type._Uri));
		default_values_map.put("v-s:initiator", new Resource("d:mondi_department_50003626", Type._Uri));
		default_values_map.put("v-s:customer", new Resource("d:org_RU1121003135", Type._Uri));
	}

	@Override
	public List<Individual> transform(int level, XmlDocument doc, String ba_id, String parent_veda_doc_uri, String parent_ba_doc_id, String path)
			throws Exception
	{
		fields_map.clear();
		inital_set();

		List<Individual> res = new ArrayList<Individual>();

		Individual new_individual = new Individual();
		transform1(level, doc, new_individual, ba_id, parent_veda_doc_uri, parent_ba_doc_id, path);

		Resources regnum = new_individual.getResources("v-s:registrationNumber");
		Resources theme = new_individual.getResources("v-s:theme");

		Object[] ff =
		{ regnum, " ", theme, };
		String[] langs_out =
		{ "EN", "RU" };
		Resources rss = rs_assemble(ff, langs_out);
		if (rss.resources.size() == 0)
		{
			String[] langs_out2 =
			{ "NONE" };
			rss = rs_assemble(ff, langs_out2);
		}
		if (rss.resources.size() > 0)
			new_individual.addProperty("rdfs:label", rss);

		res.add(new_individual);

		return res;
	}

}
