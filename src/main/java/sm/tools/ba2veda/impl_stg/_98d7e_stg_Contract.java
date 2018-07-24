package sm.tools.ba2veda.impl_stg;

import java.util.ArrayList;
import java.util.List;

import ru.mndsc.bigarchive.server.kernel.document.beans.XmlDocument;
import sm.tools.ba2veda.BaSystem;
import sm.tools.ba2veda.Pair;
import sm.tools.ba2veda.Replacer;
import sm.tools.veda_client.Individual;
import sm.tools.veda_client.Resource;
import sm.tools.veda_client.Resources;
import sm.tools.veda_client.Type;
import sm.tools.veda_client.VedaConnection;

public class _98d7e_stg_Contract extends _xxxxx_stg_Contract
{
	public _98d7e_stg_Contract(BaSystem _ba, VedaConnection _veda, Replacer replacer)
	{
		super(_ba, _veda, replacer, "98d7e7a4bb3e4e7192595aa39db326d9", "stg:Contract");

		kpr1 = "0670456ffce8475e9879d03852da2752";
		kpr2 = "30d691b8dffa46a099f1f92a42c28cf6";
		kpr3 = "8f5d41ff3f63463096ebf8c72f1734b6";
		kpr4 = "2ec8cebaa2014998901d285801695cd1";

	}

	public void inital_set()
	{
		fields_map.put("owner", "v-s:owner");
		fields_map.put("kind_pr", "v-s:hasDocumentKind");
		//fields_map.put("register_type", "?");
		fields_map.put("number_auto", "v-s:registrationNumber");
		fields_map.put("contractor_number", "v-s:registrationNumberIn");
		fields_map.put("subject", "v-s:theme");
		fields_map.put("subject_type", "v-s:hasContractScope");
		fields_map.put("initiator_person", "v-s:initiator");
		fields_map.put("initiator", "stg:hasCFOStructure");
		fields_map.put("type_contract", "v-s:hasObligationKind");
		fields_map.put("payment_order", "v-s:hasPaymentForm");
		fields_map.put("summ", "v-s:hasPrice");
		fields_map.put("currency", "v-s:hasPrice");
		fields_map.put("summ_max", "stg:hasContractScale");
		fields_map.put("payment_terms", "v-s:hasPaymentConditions");
		fields_map.put("payment_delay", "stg:paymentDelay");
		fields_map.put("delivery_delay", "stg:deliveryDelay");
		fields_map.put("day_kind", "v-s:hasPaymentDaysForm");
		fields_map.put("direct", "stg:hasContractDirection");
		fields_map.put("budget_item", "stg:hasBudjetItem");
		fields_map.put("date_from", "v-s:dateFrom");
		fields_map.put("date_to", "v-s:dateTo");
		fields_map.put("support_specialist_of_contract", "v-s:supportSpecialistOfContract");
		fields_map.put("chief_preparation_specialist_of_contract", "stg:chiefOfSupportSpecialistOfContract");
		fields_map.put("attachment", "v-s:attachment");
		fields_map.put("add_info", "v-s:hasComment");
		fields_map.put("comment", "?");
		fields_map.put("link_document", "?");
		fields_map.put("name", "rdfs:label");
		fields_map.put("", "");
		fields_map.put("", "");
		fields_map.put("", "");

		//		fields_map.put("date", "v-s:registrationDate");
		//		fields_map.put("contractor", "v-s:stakeholder");
		//		fields_map.put("executant_department", "v-s:responsibleDepartment");
		//		fields_map.put("type_contract", "v-s:hasContractObject");
		//		fields_map.put("kind", "v-s:hasBudgetCategory");
		//		fields_map.put("direct", "mnd-s:hasContractDirection");
		//		fields_map.put("comment", "rdfs:comment");
		//		fields_map.put("origiral_source", "mnd-s:hasOriginalSource");
		//		fields_map.put("pass", "mnd-s:hasContractPassport");
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
		
		fields_map.clear();
		inital_set();

		List<Individual> res = new ArrayList<Individual>();

		String add_to_contract = ba.get_first_value_of_field(doc, "add_to_contract");
		if (add_to_contract != null)
		{
			return new ArrayList<Individual>();
		}

		String inherit_rights_from = ba.get_first_value_of_field(doc, "inherit_rights_from");
		String kind_pr = ba.get_first_value_of_field(doc, "kind_pr");
		if (kind_pr != null && (kind_pr.equals(kpr1) || kind_pr.equals(kpr2) || kind_pr.equals(kpr3) || kind_pr.equals(kpr4)))
		{
			String register_type = ba.get_first_value_of_field(doc, "register_type");
			String owner = ba.get_first_value_of_field(doc, "owner");

			if (inherit_rights_from == null || inherit_rights_from.length() == 0)
			{
				Individual new_individual = new Individual();
				transform1(level, doc, new_individual, kind_pr, ba_id, parent_veda_doc_uri, parent_ba_doc_id, path);

				Resources author = new_individual.getResources("v-s:creator");
				Resources created = new_individual.getResources("v-s:created");

				if (new_individual.getUri() != null)
				{
/*					if (register_type.equals("8cf061a51fe44ae5b70bf0ae6447d9a4"))
					{
						Individual inin1 = new Individual();
						inin1.setUri("d:" + ba_id + "_CPC");
						inin1.setProperty("rdf:type", new Resource("v-s:ContractParticipantCustomer", Type._Uri));
						inin1.addProperty("v-s:creator", author);
						inin1.addProperty("v-s:created", created);
						inin1.addProperty("v-s:parent", new_individual.getUri(), Type._Uri);
						inin1.addProperty("v-s:hasRoleInContract", "d:dsxyd1uxsxuui6f1t3s4ki3rdp", Type._Uri);

						if (owner.equals("53343a30-449b-4e71-9103-2fcd4bdaafd1"))
							inin1.addProperty("v-s:hasOrganization", "d:org_RU1121016110_1", Type._Uri);

						if (owner.equals("ecae5139-5aca-41dc-923d-c0aecc941424"))
							inin1.addProperty("v-s:hasOrganization", "d:org_RU1121016110_2", Type._Uri);

						new_individual.addProperty("v-s:hasContractParticipantCustomer", inin1.getUri(), Type._Uri);
					}

					if (register_type.equals("2c4fc8846cb24609bb4f9134d2833991"))
					{
						Individual inin1 = new Individual();
						inin1.setUri("d:" + ba_id + "_CPS");
						inin1.setProperty("rdf:type", new Resource("v-s:ContractParticipantSupplier", Type._Uri));
						inin1.addProperty("v-s:creator", author);
						inin1.addProperty("v-s:created", created);
						inin1.addProperty("v-s:parent", new_individual.getUri(), Type._Uri);
						inin1.addProperty("v-s:hasRoleInContract", "d:wws2oz313z6xpl1hd4u3laxzfg", Type._Uri);

						if (owner.equals("53343a30-449b-4e71-9103-2fcd4bdaafd1"))
							inin1.addProperty("v-s:hasOrganization", "d:org_RU1121016110_1", Type._Uri);

						if (owner.equals("ecae5139-5aca-41dc-923d-c0aecc941424"))
							inin1.addProperty("v-s:hasOrganization", "d:org_RU1121016110_2", Type._Uri);

						new_individual.addProperty("v-s:hasContractParticipantCustomer", inin1.getUri(), Type._Uri);
					}

					if (register_type.equals("8cf061a51fe44ae5b70bf0ae6447d9a4"))
					{
						Individual inin1 = new Individual();
						inin1.setUri("d:" + ba_id + "_CPS");
						inin1.setProperty("rdf:type", new Resource("v-s:ContractParticipantSupplier", Type._Uri));
						inin1.addProperty("v-s:creator", author);
						inin1.addProperty("v-s:created", created);
						inin1.addProperty("v-s:parent", new_individual.getUri(), Type._Uri);
						inin1.addProperty("v-s:hasRoleInContract", "d:dsxyd1uxsxuui6f1t3s4ki3rdp", Type._Uri);

						//get_OrgUri_of_inn

						if (owner.equals("53343a30-449b-4e71-9103-2fcd4bdaafd1"))
							inin1.addProperty("v-s:hasOrganization", "d:org_RU1121016110_1", Type._Uri);

						if (owner.equals("ecae5139-5aca-41dc-923d-c0aecc941424"))
							inin1.addProperty("v-s:hasOrganization", "d:org_RU1121016110_2", Type._Uri);

						new_individual.addProperty("v-s:hasContractParticipantSupplier", inin1.getUri(), Type._Uri);
					}

					if (register_type.equals("2c4fc8846cb24609bb4f9134d2833991"))
					{
						Individual inin1 = new Individual();
						inin1.setUri("d:" + ba_id + "_CPC");
						inin1.setProperty("rdf:type", new Resource("v-s:ContractParticipantCustomer", Type._Uri));
						inin1.addProperty("v-s:creator", author);
						inin1.addProperty("v-s:created", created);
						inin1.addProperty("v-s:parent", new_individual.getUri(), Type._Uri);
						inin1.addProperty("v-s:hasRoleInContract", "d:wws2oz313z6xpl1hd4u3laxzfg", Type._Uri);

						if (owner.equals("53343a30-449b-4e71-9103-2fcd4bdaafd1"))
							inin1.addProperty("v-s:hasOrganization", "d:org_RU1121016110_1", Type._Uri);

						if (owner.equals("ecae5139-5aca-41dc-923d-c0aecc941424"))
							inin1.addProperty("v-s:hasOrganization", "d:org_RU1121016110_2", Type._Uri);

						new_individual.addProperty("v-s:hasContractParticipantCustomer", inin1.getUri(), Type._Uri);
					}
*/
					//new_individual.addProperty("v-s:customer", new Resource("d:org_RU1121003135", Type._Uri));

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
							}
						} else
						{
							System.out.println("ERR! in doc [" + ba_id + "] not found supplierContractor_lnk=" + supplierContractor_lnk);
						}
					}

					res.add(new_individual);
				}
			} else
			{
				Individual new_individual = veda.getIndividual("d:" + inherit_rights_from);

				if (new_individual == null)
				{
					System.out.println("ERR! in doc [" + ba_id + "] not found inherit_rights_from=" + inherit_rights_from);
				} else
				{
					fields_map.clear();
					fields_map.put("contractor", "v-s:stakeholder");
					fields_map.put("attachment", "v-s:scanAttachment");
					fields_map.put("origiral_source", "mnd-s:hasOriginalSource");
					transform1(level, doc, new_individual, kind_pr, ba_id, parent_veda_doc_uri, parent_ba_doc_id, path);
					if (new_individual.getUri() != null)
					{
						res.add(new_individual);
					}
				}
			}
		}

		return res;
	}

}
