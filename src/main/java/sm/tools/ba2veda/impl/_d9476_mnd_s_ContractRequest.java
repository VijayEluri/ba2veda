package sm.tools.ba2veda.impl;

import java.util.ArrayList;
import java.util.List;

import ru.mndsc.bigarchive.server.kernel.document.beans.XmlAttribute;
import ru.mndsc.bigarchive.server.kernel.document.beans.XmlDocument;
import sm.tools.ba2veda.BaSystem;
import sm.tools.ba2veda.Replacer;
import sm.tools.veda_client.Individual;
import sm.tools.veda_client.Resource;
import sm.tools.veda_client.Resources;
import sm.tools.veda_client.Type;
import sm.tools.veda_client.VedaConnection;

public class _d9476_mnd_s_ContractRequest extends _xxxxx_x_ContractRequest
{
	public _d9476_mnd_s_ContractRequest(BaSystem _ba, VedaConnection _veda, Replacer replacer)
	{
		super(_ba, _veda, replacer, "d947636ac03f4a1f9bd6e5cfe3e084ba", "mnd-s:ContractRequest");
	}

	public void inital_set()
	{
		fields_map.put("number_reg", "v-s:registrationNumber");
		fields_map.put("contract_by_dapartment", "v-s:initiator");
		fields_map.put("content", "v-s:description");
		fields_map.put("theme", "v-s:theme");
		fields_map.put("date_to", "v-s:dateTo");
		fields_map.put("inherit_rights_from", "v-s:hasProject");
		fields_map.put("kind", "v-s:hasBudgetCategory");
		fields_map.put("attachment", "v-s:attachment");
		fields_map.put("link", "v-s:hasLink");
		fields_map.put("add_to_contract", "v-s:hasContract");
		fields_map.put("add_info", "v-s:hasComment");
		fields_map.put("summ_2", "v-s:expectedValueOfContract");
		fields_map.put("currency", "?");

		default_values_map.put("v-s:responsibleDepartment", new Resource("d:mondi_department_50001663", Type._Uri));
		//default_values_map.put("v-s:hasContractScope", new Resource("d:b70f5e19b9d14c97868585517a3e5979", Type._Uri));
		default_values_map.put("v-s:customer", new Resource("d:org_RU1121003135", Type._Uri));
	}

	@Override
	public List<Individual> transform(XmlDocument doc, String ba_id, String parent_veda_doc_uri, String parent_ba_doc_id, String path)
			throws Exception
	{
		fields_map.clear();
		inital_set();

		List<Individual> res = new ArrayList<Individual>();

		Individual new_individual = new Individual();
		transform1(doc, new_individual, ba_id, parent_veda_doc_uri, parent_ba_doc_id, path);

		String subject_type = "";

		List<XmlAttribute> atts = doc.getAttributes();
		for (XmlAttribute att : atts)
		{
			String code = att.getCode();

			if (code.equals("subject_type"))
			{
				subject_type = att.getRecordIdValue();
			}
		}

		//Если в поле subject_type=b52870cf821143018b854bc98bf1cf58, то в это поле пишем d:cc87e0778a864272b1b8d366f591da88 
		//		Иначе d:fbf562d8a6a04d72b1034f7f7e4d21de
		if (subject_type.equals("b52870cf821143018b854bc98bf1cf58"))
			new_individual.addProperty("v-s:hasDocumentKind", new Resource("d:cc87e0778a864272b1b8d366f591da88", Type._Uri));
		else
			new_individual.addProperty("v-s:hasDocumentKind", new Resource("d:fbf562d8a6a04d72b1034f7f7e4d21de", Type._Uri));

		//Если в поле subject_type=b52870cf821143018b854bc98bf1cf58, то в это поле пишем  d:f1ef19aec63145dcbc9cac8087682efd
		//		Иначе - то что есть в subject_type
		if (subject_type.equals("b52870cf821143018b854bc98bf1cf58"))
			new_individual.addProperty("v-s:hasContractScope", new Resource("d:f1ef19aec63145dcbc9cac8087682efd", Type._Uri));
		else
			new_individual.addProperty("v-s:hasContractScope", new Resource("d:" + subject_type, Type._Uri));

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
