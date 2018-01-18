package sm.tools.ba2veda.impl;

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

public class _524d6_mnd_s_Contract extends _xxxxx_x_Contract
{
	public _524d6_mnd_s_Contract(BaSystem _ba, VedaConnection _veda, Replacer replacer)
	{
		super(_ba, _veda, replacer, "524d62cde5194103ae91b25acb5fee77", "mnd-s:Contract");

		kpr1 = "cc87e0778a864272b1b8d366f591da88";
		kpr2 = "fecef6b8058549cfb0203b55bd2425de";
	}

	public void inital_set()
	{
		fields_map.put("name", "rdfs:label");
		fields_map.put("owner", "v-s:customer");
		fields_map.put("kind_pr", "v-s:hasDocumentKind");
		fields_map.put("number", "v-s:registrationNumber");
		fields_map.put("number_by_contractor", "v-s:registrationNumberIn");
		fields_map.put("date", "v-s:registrationDate");
		fields_map.put("contractor", "v-s:supplierContractor");
		fields_map.put("theme", "v-s:theme");
		fields_map.put("theme_en", "v-s:theme");
		fields_map.put("summ", "v-s:hasPrice");
		fields_map.put("currency", "v-s:hasPrice");
		fields_map.put("comment", "rdfs:comment");
		fields_map.put("contract_by_department", "v-s:initiator");
		fields_map.put("executant_department", "v-s:responsibleDepartment");
		fields_map.put("date_from", "v-s:dateFrom");
		fields_map.put("date_to", "v-s:dateTo");
		fields_map.put("type_contract", "v-s:hasContractObject");
		fields_map.put("payment_terms", "v-s:hasPaymentConditions");
		fields_map.put("kind", "v-s:hasBudgetCategory");
		fields_map.put("direct", "mnd-s:hasContractDirection");
		fields_map.put("attachment", "v-s:attachment");
		fields_map.put("inherit_rights_from", "mnd-s:hasContractRequest");
		fields_map.put("subject_type", "v-s:hasContractScope");
		fields_map.put("preparation_specialist_of_contract", "mnd-s:executorSpecialistOfContract");
		fields_map.put("support_specialist_of_contract", "mnd-s:supportSpecialistOfContract");
		fields_map.put("name", "rdfs:label");
		fields_map.put("add_info", "v-s:hasComment");
		fields_map.put("add_doc", "v-s:linkedObject");
	}

	@Override
	public List<Individual> transform(int level, XmlDocument doc, String ba_id, String parent_veda_doc_uri, String parent_ba_doc_id, String path)
			throws Exception
	{
		List<Individual> res = new ArrayList<Individual>();

		Individual new_individual = new Individual();
		String kind_pr = ba.get_first_value_of_field(doc, "kind_pr");
		if (kind_pr != null && (kind_pr.equals(kpr1) || kind_pr.equals(kpr2)))
		{

			transform1(level, doc, new_individual, kind_pr, ba_id, parent_veda_doc_uri, parent_ba_doc_id, path);

			if (new_individual.getUri() != null)
			{
				if (new_individual.getUri() != null)
				{
					new_individual.addProperty("v-s:customer", new Resource("d:org_RU1121003135", Type._Uri));

					Resources supplierContractor_lnk = new_individual.getResources("v-s:supplierContractor");

					if (supplierContractor_lnk != null && supplierContractor_lnk.resources.size() == 1)
					{
						Individual supplierContractor = veda.getIndividual(supplierContractor_lnk.resources.get(0).getData());
						if (supplierContractor != null)
						{
							Resources linkedOrganization = supplierContractor.getResources("v-s:linkedOrganization");
							if (linkedOrganization != null && linkedOrganization.resources.size() > 0)
							{
								new_individual.addProperty("v-s:supplier", linkedOrganization);
							} else
							{
								System.out.println("ERR! in doc [" + ba_id + "] not found supplierContractor_lnk=" + supplierContractor_lnk);
							}
						}
					}

					res.add(new_individual);
				}

				res.add(new_individual);
			}
		}
		return res;
	}
}
