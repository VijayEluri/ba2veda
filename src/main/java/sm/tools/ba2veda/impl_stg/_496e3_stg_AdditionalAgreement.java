package sm.tools.ba2veda.impl_stg;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

public class _496e3_stg_AdditionalAgreement extends _xxxxx_stg_Contract
{
	public _496e3_stg_AdditionalAgreement(BaSystem _ba, VedaConnection _veda, Replacer replacer)
	{
		super(_ba, _veda, replacer, "496e359316334e47b87af4576d36d27c", "stg:AdditionalAgreement");
	}

	public void inital_set()
	{
		fields_map.put("owner", "v-s:owner");
		fields_map.put("kind_pr", "?");
		fields_map.put("contract", "?");
		fields_map.put("add_agreement_contractor_number", "v-s:registrationNumberAdd");
		fields_map.put("number", "v-s:registrationNumber");
		fields_map.put("agreement_register_date", "v-s:registrationDate");
		fields_map.put("add_agreement_number", "v-s:registrationNumberIn");
		fields_map.put("subject", "v-s:theme");
		fields_map.put("subject_type", "v-s:hasContractScope");
		fields_map.put("initiator", "v-s:initiator");
		// fields_map.put("initiator", "stg:hasCFOStructure");
		// fields_map.put("contractor", "v-s:hasContractParticipantCustomer");
		fields_map.put("type_contract", "v-s:hasObligationKind");
		fields_map.put("payment_order", "v-s:hasPaymentForm");
		fields_map.put("summ", "?");
		fields_map.put("currency", "?");
		fields_map.put("summ_max", "stg:hasContractScale");
		fields_map.put("payment_terms", "v-s:hasPaymentConditions");
		fields_map.put("payment_delay", "stg:paymentDelay");
		fields_map.put("delivery_delay", "stg:deliveryDelay");
		fields_map.put("day_kind", "v-s:hasPaymentDaysForm");
		fields_map.put("direct", "stg:hasContractDirection");
		fields_map.put("budget_item", "stg:hasBudjetItem");
		fields_map.put("budget_limit", "?");
		fields_map.put("date_from", "v-s:dateFrom");
		fields_map.put("agreement_validity_period", "v-s:dateTo");
		fields_map.put("support_specialist_of_contract", "v-s:supportSpecialistOfContract");
		fields_map.put("chief_preparation_specialist_of_contract", "stg:chiefOfSupportSpecialistOfContract");
		// fields_map.put("add_agreement", "stg:hasAdditionalAgreement");
		fields_map.put("attachment", "v-s:attachment");
		fields_map.put("add_info", "v-s:hasComment");
		fields_map.put("comment", "?");
		fields_map.put("contract", "?");
		fields_map.put("link_document", "?");
		fields_map.put("display_requisite", "rdfs:label");
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

		String uri = prepare_uri(ba_id);
		List<Individual> res = new ArrayList<Individual>();

		Individual new_individual = new Individual();
		new_individual.setUri(uri);

		set_basic_fields(level, new_individual, doc);

		new_individual.addProperty("rdf:type", new Resource(to_class, Type._Uri));
		new_individual.addProperty("v-s:customer", "d:org_RU6674128343", Type._Uri);
		new_individual.addProperty("v-s:owner", "d:org_RU1121016110_1", Type._Uri);

		new_individual.addProperty("v-s:hasDocumentKind", "d:fbf562d8a6a04d72b1034f7f7e4d21de", Type._Uri);
		new_individual.addProperty("v-s:hasContractScope", "d:c13aa6916114490185a9e6a93fde85f7", Type._Uri);

		String kind_pr = "";
		String irf = null;

		Resources attachment = null, original_source = null, contractor = null;
		Resources summ = null, currency = null;
		Resources comment = null;

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

				if (code.equals("contract"))
				{
					new_individual.addProperty("v-s:backwardProperty", "stg:hasAdditionalAgreement", Type._Uri);
					String add_to_contract_link = att.getLinkValue();

					if (add_to_contract_link != null)
					{
						Pair<XmlDocument, Long> doc_ts = ba.getActualDocument(add_to_contract_link);

						if (doc_ts != null)
						{
							XmlDocument add_to_contract1 = doc_ts.getLeft();

							String inherit_rights_from1 = ba.get_first_value_of_field(add_to_contract1, "inherit_rights_from");

							if (inherit_rights_from1 == null)
							{
								new_individual.addProperty("v-s:backwardTarget", rss);
							} else
							{
								new_individual.addProperty("v-s:backwardTarget", "d:" + inherit_rights_from1, Type._Uri);
							}
						}
					}
				} else if (code.equals("kind_pr"))
				{
					// new_individual.addProperty("v-s:hasDocumentKind", new
					// Resource("d:fbf562d8a6a04d72b1034f7f7e4d21de", Type._Uri));
					new_individual.addProperty("v-s:hasDocumentKind", rss);
					kind_pr = att.getRecordIdValue();
				} else if (code.equals("inherit_rights_from"))
					irf = att.getLinkValue();
				else if (code.equals("attachment"))
					attachment = rss;
				else if (code.equals("original_source"))
					original_source = rss;
				else if (code.equals("contractor"))
				{
					new_individual.addProperty("v-s:supplierContractor", rss);
					while (true)
					{
						try
						{
							Individual indiv = veda.getIndividual("d:" + irf);
							if (indiv != null)
								new_individual.addProperty("v-s:supplier", indiv.getResources("v-s:linkedOrganization"));
						} catch (Exception e)
						{
							System.err.println("Err getting individual, retry later");
							e.printStackTrace();
							Thread.sleep(5000);
						}

						break;
					}

					contractor = rss;
				} else if (code.equals("summ"))
					summ = rss;
				else if (code.equals("currency"))
					currency = rss;
				else if (code.equals("budget_limit"))
					comment = rss;
				else if (code.equals("number"))
				{
					new_individual.addProperty("v-s:registrationNumber", rss);
					String data = rss.resources.get(0).getData();
					Pattern pattern = Pattern.compile("[0-9]{6}[.][0-9]{2}?");
					Pattern pattern2 = Pattern.compile("[0-9]{6}[.][0-9]{1}?");
					Matcher matcher = pattern.matcher(data);
					Matcher matcher2 = pattern2.matcher(data);
					String extract = null;
					if (matcher.find())
						extract = matcher.group();
					else if (matcher2.find())
						extract = matcher2.group();

					if (extract != null)
					{
						String[] parts = extract.split("[.]");
						new_individual.addProperty("v-s:registrationNumberAdd", parts[1], Type._String);
					}

				} else if (code.equals("link_document"))
				{
					String irf1 = att.getLinkValue();
					if (irf1 == null)
						continue;
					String link_to = irf1;

					Individual link = new Individual();
					link.addProperty("rdf:type", new Resource("v-s:Link", Type._Uri));
					link.setUri("d:link_" + ba_id + "_" + link_to);
					link.addProperty("v-s:from", new Resource(new_individual.getUri(), Type._Uri));
					link.addProperty("v-s:to", new Resource("d:" + link_to, Type._Uri));

					putIndividual(level, link, ba_id);

					new_individual.addProperty("v-s:hasLink", new Resource(link.getUri(), Type._Uri));

				}
			}
		}
		/*
		 * if ((kind_pr.equals("fbf562d8a6a04d72b1034f7f7e4d21de") ||
		 * kind_pr.equals("074d6f4add1b4aebaf1cf7bc0332eb3d")) && irf != null) {
		 * 
		 * while (true) { try { Individual indiv = veda.getIndividual("d:" + irf); if
		 * (indiv != null) { Individual regRecord = new Individual();
		 * regRecord.setUri(new_individual.getUri()); regRecord.addProperty("rdf:type",
		 * "stg:ContractRegistrationRecord", Type._Uri);
		 * regRecord.addProperty("v-s:creator", indiv.getResources("v-s:creator"));
		 * regRecord.addProperty("v-s:created", indiv.getResources("v-s:created"));
		 * regRecord.addProperty("v-s:attachment", attachment);
		 * regRecord.addProperty("stg:hasOriginalSource", original_source);
		 * regRecord.addProperty("v-s:backwardTarget", indiv.getUri(), Type._Uri);
		 * regRecord.addProperty("v-s:parent", indiv.getUri(), Type._Uri);
		 * regRecord.addProperty("v-s:backwardProperty", "v-s:hasRegistrationRecord",
		 * Type._Uri); regRecord.addProperty("v-s:canRead", "true", Type._Bool);
		 * putIndividual(level, regRecord, ba_id);
		 * //indiv.addProperty("v-s:scanAttachment", attachment);
		 * indiv.addProperty("stg:hasOriginalSource", original_source);
		 * indiv.addProperty("v-s:supplierContractor", contractor);
		 * indiv.addProperty("v-s:hasRegistrationRecord", regRecord.getUri(),
		 * Type._Uri); putIndividual(level, indiv, ba_id);
		 * 
		 * } } catch (Exception e) {
		 * System.err.println("Err getting individual, retry later");
		 * e.printStackTrace(); Thread.sleep(5000); }
		 * 
		 * break; } }
		 */
		if (currency != null && summ != null)
		{
			Individual price = new Individual();
			price.setUri(new_individual.getUri() + "_price");
			price.addProperty("rdf:type", "v-s:Price", Type._Uri);
			price.addProperty("v-s:creator", new_individual.getResources("v-s:creator"));
			price.addProperty("v-s:created", new_individual.getResources("v-s:created"));
			price.addProperty("v-s:sum", summ);
			price.addProperty("v-s:hasCurrency", currency);
			new_individual.addProperty("v-s:hasPrice", price.getUri(), Type._Uri);
			putIndividual(level, price, ba_id);
		}

		if (original_source != null || contractor != null || attachment != null)
		{
			Individual regRecord = new Individual();
			regRecord.setUri(new_individual.getUri() + "_registration_record");
			regRecord.addProperty("rdf:type", "stg:ContractRegistrationRecord", Type._Uri);
			regRecord.addProperty("v-s:creator", new_individual.getResources("v-s:creator"));
			regRecord.addProperty("v-s:created", new_individual.getResources("v-s:created"));
			regRecord.addProperty("v-s:attachment", attachment);
			regRecord.addProperty("stg:hasOriginalSource", original_source); //
			//regRecord.addProperty("v-s:supplierContractor", contractor);
			regRecord.addProperty("v-s:backwardTarget", new_individual.getUri(), Type._Uri);
			regRecord.addProperty("v-s:backwardProperty", "v-s:hasRegistrationRecord", Type._Uri);
			regRecord.addProperty("v-s:parent", new_individual.getUri(), Type._Uri);
			regRecord.addProperty("v-s:canRead", "true", Type._Bool);
			putIndividual(level, regRecord, ba_id);
			new_individual.addProperty("v-s:hasRegistrationRecord", regRecord.getUri(), Type._Uri);
		}

		if (comment != null && comment.resources.size() > 0)
		{
			Individual commentIndiv = new Individual();
			commentIndiv.setUri(new_individual.getUri() + "_comment");
			commentIndiv.addProperty("rdf:type", "v-s:Comment", Type._Uri);
			commentIndiv.addProperty("v-s:creator", new_individual.getResources("v-s:creator"));
			commentIndiv.addProperty("v-s:created", new_individual.getResources("v-s:created"));
			commentIndiv.addProperty("rdfs:label", comment);
			commentIndiv.addProperty("v-s:parent", new_individual.getUri(), Type._Uri);
			new_individual.addProperty("v-s:hasComment", commentIndiv.getUri(), Type._Uri);
			putIndividual(level, commentIndiv, ba_id);
		}

		res.add(new_individual);

		return res;
	}
}
