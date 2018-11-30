package sm.tools.ba2veda.impl_stg;

import java.util.ArrayList;
import java.util.List;

import ru.mndsc.bigarchive.server.kernel.document.beans.XmlAttribute;
import ru.mndsc.bigarchive.server.kernel.document.beans.XmlDocument;
import sm.tools.ba2veda.BaSystem;
import sm.tools.ba2veda.Pair;
import sm.tools.ba2veda.Replacer;
import sm.tools.veda_client.Individual;
import sm.tools.veda_client.Resource;
import sm.tools.veda_client.Resources;
import sm.tools.veda_client.Type;
import sm.tools.veda_client.VedaConnection;

public class _dc205_stg_Contract extends _xxxxx_stg_Contract
{
	public _dc205_stg_Contract(BaSystem _ba, VedaConnection _veda, Replacer replacer)
	{
		super(_ba, _veda, replacer, "dc205f55fd8f435da8968e6cbbcd4149", "stg:Contract");

		kpr1 = "";
		kpr2 = "";
		kpr3 = "";
		kpr4 = "";

	}

	public void inital_set()
	{
		fields_map.put("owner", "v-s:owner");
		fields_map.put("kind_pr", "v-s:hasDocumentKind");
		//fields_map.put("register_type", "?");
		fields_map.put("register_number", "v-s:registrationNumber");
		fields_map.put("contractor_number", "v-s:registrationNumberIn");
		fields_map.put("subject", "v-s:theme");
		fields_map.put("subject_type", "v-s:hasContractScope");
		fields_map.put("initiator", "stg:hasCFOStructure");
		fields_map.put("initiator_person", "v-s:initiator");
		fields_map.put("type_contract", "v-s:hasObligationKind");
		fields_map.put("contract_date", "v-s:registrationDate");
		fields_map.put("type_contract", "v-s:hasObligationKind");
		fields_map.put("payment_order", "v-s:hasPaymentForm");
		fields_map.put("payment_terms", "v-s:hasPaymentConditions");
		fields_map.put("payment_delay", "stg:paymentDelay");
		fields_map.put("delivery_delay", "stg:deliveryDelay");
		fields_map.put("day_kind", "v-s:hasPaymentDaysForm");
		fields_map.put("summ_max", "stg:hasContractScale");
		fields_map.put("summ", "v-s:hasPrice");
		fields_map.put("currency", "v-s:hasPrice");
		fields_map.put("direct", "stg:hasContractDirection");
		fields_map.put("budget_item", "stg:hasBudjetItem");
		fields_map.put("date_from", "v-s:dateFrom");
		fields_map.put("date_to", "v-s:dateTo");
		fields_map.put("comment", "?");
		fields_map.put("attachment", "v-s:attachment");

		fields_map.put("original_source", "?");
		fields_map.put("signed_document", "?");

		fields_map.put("support_specialist_of_contract", "v-s:supportSpecialistOfContract");

		fields_map.put("chief_preparation_specialist_of_contract", "stg:chiefOfSupportSpecialistOfContract");
		//		fields_map.put("add_info", "v-s:hasComment");
		fields_map.put("link_document", "?");
		fields_map.put("display_requisite", "rdfs:label");
		fields_map.put("budget_limit", "?");
		fields_map.put("original_source", "?");
		fields_map.put("", "");

		//		fields_map.put("contractor", "v-s:stakeholder");
		//		fields_map.put("executant_department", "v-s:responsibleDepartment");
		//		fields_map.put("kind", "v-s:hasBudgetCategory");
		//		fields_map.put("direct", "mnd-s:hasContractDirection");
		//		fields_map.put("comment", "rdfs:comment");
		//		fields_map.put("origiral_source", "v-s:hasRegistrationRecord");
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
		if (inherit_rights_from != null)
		{
			return new ArrayList<Individual>();
		}

		String register_number = ba.get_first_value_of_field(doc, "register_number");

		Individual new_individual = new Individual();
		//String kind_pr = ba.get_first_value_of_field(doc, "kind_pr");
		//		if (kind_pr != null && (kind_pr.equals(kpr1) || kind_pr.equals(kpr2) || kind_pr.equals(kpr3) || kind_pr.equals(kpr4)))
		{
			if (inherit_rights_from == null || inherit_rights_from.length() == 0)
			{
				transform1(level, doc, new_individual, "", ba_id, parent_veda_doc_uri, parent_ba_doc_id, path);

				if (new_individual.getUri() != null)
				{
					if (register_number == null || register_number.length() == 0)
					{
						new_individual.addProperty("v-s:registrationNumber", "NNNN", Type._String);

					}

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
			}
		}

		Resources signed_document = null, original_source = null;
		String uri = new_individual.getUri();

		List<XmlAttribute> atts = doc.getAttributes();
		for (XmlAttribute att : atts)
		{
			String code = att.getCode();

			String predicate = fields_map.get(code);
			System.out.println("CODE: " + code);

			if (predicate != null && (code.equals("signed_document") || code.equals("original_source")))
			{
				Resources rss = ba_field_to_veda(level, att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri, true);

				if (code.equals("signed_document"))
					signed_document = rss;
				else if (code.equals("original_source"))
					original_source = rss;
			}
		}

		new_individual.addProperty("v-s:hasDocumentKind", "d:2ec8cebaa2014998901d285801695cd1", Type._Uri);
		new_individual.addProperty("v-s:hasContractScope", "d:c13aa6916114490185a9e6a93fde85f7", Type._Uri);
		
		if (signed_document != null)
		{
			Individual regRecord = new Individual();
			regRecord.setUri(new_individual.getUri() + "_crr");
			regRecord.addProperty("rdf:type", "stg:ContractRegistrationRecord", Type._Uri);
			regRecord.addProperty("v-s:creator", new_individual.getResources("v-s:creator"));
			regRecord.addProperty("v-s:created", new_individual.getResources("v-s:created"));
			regRecord.addProperty("v-s:attachment", signed_document);

			if (original_source != null)
				regRecord.addProperty("stg:hasOriginalSource", original_source);
			//                                      regRecord.addProperty("v-s:supplierContractor", contractor);
			regRecord.addProperty("v-s:backwardTarget", new_individual.getUri(), Type._Uri);
			regRecord.addProperty("v-s:backwardProperty", "v-s:hasRegistrationRecord", Type._Uri);
			regRecord.addProperty("v-s:parent", new_individual.getUri(), Type._Uri);
			regRecord.addProperty("v-s:canRead", "true", Type._Bool);
			putIndividual(level, regRecord, ba_id);
			new_individual.addProperty("v-s:hasRegistrationRecord", regRecord.getUri(), Type._Uri);
		}

		return res;
	}

}
