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

public class _15206_mnd_s_AdditionalAgreement extends _xxxxx_x_Contract
{
	public _15206_mnd_s_AdditionalAgreement(BaSystem _ba, VedaConnection _veda, Replacer replacer)
	{
		super(_ba, _veda, replacer, "15206d33eafa440c84c02c8d912bce7a", "mnd-s:AdditionalAgreement");

		kpr1 = "fbf562d8a6a04d72b1034f7f7e4d21de";
		kpr2 = "074d6f4add1b4aebaf1cf7bc0332eb3d";
	}

	public void inital_set()
	{
		fields_map.put("name", "rdfs:label");
		fields_map.put("owner", "v-s:supplier");
		fields_map.put("kind_pr", "v-s:hasDocumentKind");
		fields_map.put("subject_type", "v-s:hasContractScope");
		fields_map.put("number", "v-s:registrationNumber");
		fields_map.put("number_by_contractor", "v-s:registrationNumberIn");
		fields_map.put("date", "v-s:registrationDate");
		fields_map.put("contractor", "v-s:customerContractor");
		fields_map.put("theme", "v-s:theme");
		fields_map.put("theme_en", "v-s:theme");
		fields_map.put("contract_by_department", "v-s:initiator");
		fields_map.put("executant_department", "v-s:responsibleDepartment");
		fields_map.put("date_from", "v-s:dateFrom");
		fields_map.put("date_to", "v-s:dateTo");
		fields_map.put("type_contract", "v-s:hasObligationKind");
		fields_map.put("type_contract", "v-s:hasContractObject");
		fields_map.put("summ", "v-s:hasPrice");
		fields_map.put("currency", "v-s:hasPrice");
		fields_map.put("payment_terms", "v-s:hasPaymentConditions");
		fields_map.put("kind", "v-s:hasBudgetCategory");
		fields_map.put("direct", "mnd-s:hasContractDirection");
		fields_map.put("attachment", "v-s:attachment");
		fields_map.put("comment", "rdfs:comment");
		fields_map.put("inherit_rights_from", "mnd-s:hasContractRequest");
		fields_map.put("preparation_specialist_of_contract", "mnd-s:executorSpecialistOfContract");
		fields_map.put("support_specialist_of_contract", "mnd-s:supportSpecialistOfContract");
		fields_map.put("add_doc", "v-s:linkedObject");
		fields_map.put("add_info", "v-s:hasComment");
		fields_map.put("add_to_contract", "v-s:hasContract");
	}

	@Override
	public List<Individual> transform(int level, XmlDocument doc, String ba_id, String parent_veda_doc_uri, String parent_ba_doc_id, String path)
			throws Exception
	{
		fields_map.clear();
		inital_set();

		List<Individual> res = new ArrayList<Individual>();

		String inherit_rights_from = ba.get_first_value_of_field(doc, "inherit_rights_from");
		String kind_pr = ba.get_first_value_of_field(doc, "kind_pr");
		if (kind_pr != null && (kind_pr.equals(kpr1) || kind_pr.equals(kpr2)))
		{

			if (inherit_rights_from == null || inherit_rights_from.length() == 0)
			{
				Individual new_individual = new Individual();
				transform1(level, doc, new_individual, kind_pr, ba_id, parent_veda_doc_uri, parent_ba_doc_id, path);

				if (new_individual.getUri() != null)
				{
					new_individual.addProperty("v-s:customer", new Resource("d:org_RU1121003135", Type._Uri));

					Resources hasContract = new_individual.getResources("v-s:hasContract");

					if (hasContract != null && hasContract.resources.size() > 0)
					{
						new_individual.addProperty("v-s:backwardProperty", new Resource("mnd-s:hasAdditionalAgreement", Type._Uri));
						new_individual.addProperty("v-s:backwardTarget", hasContract);
					}

					Resources customerContractor_lnk = new_individual.getResources("v-s:customerContractor");
					if (customerContractor_lnk != null && customerContractor_lnk.resources.size() == 1)
					{
						Individual customerContractor = veda.getIndividual(customerContractor_lnk.resources.get(0).getData());
						if (customerContractor != null)
						{
							Resources linkedOrganization = customerContractor.getResources("v-s:linkedOrganization");
							if (linkedOrganization != null && linkedOrganization.resources.size() > 0)
							{
								new_individual.addProperty("v-s:customer", linkedOrganization);
							}
						} else
						{
							System.out.println("ERR! in doc [" + ba_id + "] not found supplierContractor_lnk=" + customerContractor_lnk);
						}
					}

					res.add(new_individual);
				}
			} else
			{
				Individual new_individual = veda.getIndividual("d:" + inherit_rights_from);

				if (new_individual != null)
				{
					fields_map.clear();
					fields_map.put("contractor", "v-s:customerContractor");
					fields_map.put("attachment", "v-s:scanAttachment");
					fields_map.put("origiral_source", "mnd-s:hasOriginalSource");
					transform1(level, doc, new_individual, kind_pr, ba_id, parent_veda_doc_uri, parent_ba_doc_id, path);
					if (new_individual.getUri() != null)
					{
						res.add(new_individual);
					}
				} else
				{
					System.out.println("ERR! NOT UPDATE [" + "d:" + inherit_rights_from + "], IS NOT EXIST");
				}
			}
		}
		return res;
	}

}
