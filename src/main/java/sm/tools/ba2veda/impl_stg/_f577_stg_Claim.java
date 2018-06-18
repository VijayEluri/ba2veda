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

public class _f577_stg_Claim extends Ba2VedaTransform {
	public _f577_stg_Claim(BaSystem _ba, VedaConnection _veda, Replacer replacer) {
		super(_ba, _veda, replacer, "f577e7432bad4e8b9649ff1c023bb8aa", "stg:Claim");
	}

	public void inital_set() {
		fields_map.put("register_number", "?");
		fields_map.put("register_date", "?");
		fields_map.put("contractor", "?");
		fields_map.put("subject", "v-s:claimObjectDescription");
		fields_map.put("claim_type", "stg:claimRequirement");
		fields_map.put("claim_content", "?");
		fields_map.put("attachment", "?");
		fields_map.put("display_requisite", "rdfs:label");
		/*
		 * fields_map.put("smena", "?");veda-gowebserver,
		 * fields_map.put("currency_from", "?"); fields_map.put("executor", "?");
		 * fields_map.put("number_to", "?"); fields_map.put("date_to", "?");
		 * fields_map.put("other_sides", "?"); fields_map.put("department",
		 * "v-s:responsibleDepartment"); fields_map.put("department", "?");
		 * fields_map.put("invoice", "stg:invoiceForClaim");
		 * fields_map.put("prodect_kind", "v-s:hasClaimObject");
		 * fields_map.put("claim_class", "v-s:hasClaimCircumstance");
		 * fields_map.put("claim_class_description", "?");
		 * fields_map.put("object_claim_amount", "v-s:claimVolume");
		 * fields_map.put("edizm", "v-s:hasUnitOfMeasure");
		 * fields_map.put("claim_sum_from", "?"); fields_map.put("date_plan",
		 * "v-s:datePlan"); fields_map.put("resolution", "?");
		 * fields_map.put("claim_sum_to", "?"); fields_map.put("currency_to", "?");
		 * fields_map.put("stage", "v-s:hasStatus"); fields_map.put("comment", "?");
		 * fields_map.put("org_reason", "stg:organizationReasonOfClaim");
		 * fields_map.put("tech_reason", "stg:technicalReasonOfClaim");
		 * fields_map.put("final_resolution", "?");
		 * fields_map.put("object_claim_amount_to", "stg:claimVolumeCompensation");
		 * fields_map.put("edizm_1", "stg:hasUnitOfMeasure");
		 * fields_map.put("link_document", "?"); fields_map.put("contacts", "?");
		 */
		/*
		 * fields_map.put("cmt_number", "stg:registrationNumberCMT");
		 * fields_map.put("contacts", "v-s:contactInfo");
		 * fields_map.put("date_claim_from", "v-s:dateFrom");
		 * fields_map.put("date_claim_to", "v-s:dateTo");
		 * fields_map.put("Срок рассмотрения", "v-s:datePlan");
		 * fields_map.put("position_send", "v-s:hasDelivery");
		 * 
		 * fields_map.put("Содержание", "?");
		 * 
		 * fields_map.put("claim_request", "?"); fields_map.put("Требование претензии",
		 * "?");
		 * 
		 * fields_map.put("inherit_rights_from", "?"); // fields_map.put("name_short",
		 * "v-s:shortLabel");
		 * 
		 * fields_map.put("Сумма претензии (руб.)", "?");
		 * 
		 * fields_map.put("add_doc", "?"); fields_map.put("Связанный документ", "?");
		 * fields_map.put("Вложение", "?");
		 * 
		 * fields_map.put("contacts2", "?");
		 */
	}

	public Individual createRequirementSum(String uri) {
		Individual price = new Individual();
		price.setProperty("rdf:type", new Resource("v-s:Price", Type._Uri));
		price.setUri(uri + "_requirement_sum");

		return price;
	}

	public Individual createCompensationSum(String uri) {
		Individual price = new Individual();
		price.setProperty("rdf:type", new Resource("v-s:Price", Type._Uri));
		price.setUri(uri + "_compensation_sum");

		return price;
	}

	@Override
	public List<Individual> transform(int level, XmlDocument doc, String ba_id, String parent_veda_doc_uri,
			String parent_ba_doc_id, String path) throws Exception {
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

		new_individual.addProperty("rdf:type", to_class, Type._Uri);

		ArrayList<Resources> contacts = new ArrayList<Resources>();
		ArrayList<Resources> department = new ArrayList<Resources>();

		ArrayList<Resources> attachments = new ArrayList<Resources>();

		Individual requirement_sum = null;
		Individual compensation_sum = null;

		Resources organization2 = null;
		Resources currency_from = null;

		Individual has_letter_registration_record_recipient = null;
		Individual has_letter_registration_record_sender = null;

		Individual comment = null;
		List<XmlAttribute> atts = doc.getAttributes();
		for (XmlAttribute att : atts) {
			String code = att.getCode();

			String predicate = fields_map.get(code);
			System.out.println("CODE: " + code);
			if (predicate != null) {
				Resources rss = ba_field_to_veda(level, att, uri, ba_id, doc, path, parent_ba_doc_id,
						parent_veda_doc_uri, true);

				if (predicate.equals("?") == false)
					new_individual.addProperty(predicate, rss);

				if (rss.resources.size() < 1)
					continue;

				if (code.equals("claim_content"))
					new_individual.addProperty("v-s:claimCircumstanceDescription", rss);
				else if (code.equals("Содержание"))
					new_individual.addProperty("v-s:claimCircumstanceDescription", rss);
				else if (code.equals("claim_class_description"))
					new_individual.addProperty("v-s:claimCircumstanceDescription", rss);
				else if (code.equals("claim_request")) {
					new_individual.addProperty("v-s:claimRequirement", new Resource(att.getRecordNameValue()));
				} else if (code.equals("Требование претензии")) {
					new_individual.addProperty("v-s:claimRequirement", new Resource(att.getRecordNameValue()));
				} else if (code.equals("claim_sum_from")) {
					if (requirement_sum == null)
						requirement_sum = createRequirementSum(uri);

					requirement_sum.addProperty("v-s:sum", rss);
				} else if (code.equals("Сумма претензии (руб.)")) {
					if (requirement_sum == null)
						requirement_sum = createRequirementSum(uri);

					requirement_sum.addProperty("v-s:sum", rss);
				} else if (code.equals("claim_sum_to")) {
					if (compensation_sum == null)
						compensation_sum = createCompensationSum(uri);

					compensation_sum.addProperty("v-s:sum", rss);
				} else if (code.equals("currency_from")) {
					currency_from = rss;
				} else if (code.equals("currency_to")) {
					if (requirement_sum == null)
						requirement_sum = createRequirementSum(uri);
					if (compensation_sum == null)
						compensation_sum = createCompensationSum(uri);

					requirement_sum.addProperty("v-s:hasCurrency", rss);
					compensation_sum.addProperty("v-s:hasCurrency", rss);
				} else if (code.equals("comment")) {
					if (comment == null)
						comment = new Individual();

					comment.addProperty("rdfs:label", new Resource(att.getTextValue()));
				} else if (code.equals("attachment")) {

					attachments.add(rss);
				} else if (code.equals("link_document")) {
					String irf = att.getLinkValue();
					if (irf == null)
						continue;
					String link_to = irf;

					Individual link = new Individual();
					link.addProperty("rdf:type", new Resource("v-s:Link", Type._Uri));
					link.setUri("d:link_" + ba_id + "_" + link_to);
					link.addProperty("v-s:from", new Resource(new_individual.getUri(), Type._Uri));
					link.addProperty("v-s:to", new Resource("d:" + link_to, Type._Uri));

					putIndividual(level, link, ba_id);

					new_individual.addProperty("v-s:hasLink", new Resource(link.getUri(), Type._Uri));

				} else if (code.equals("number_to")) {
					if (rss.resources.size() < 1)
						continue;
					if (has_letter_registration_record_recipient == null) {
						has_letter_registration_record_recipient = new Individual();
						has_letter_registration_record_recipient.setUri(uri + "_letter_registration_record_recipient");
						has_letter_registration_record_recipient.addProperty("rdf:type",
								new Resource("v-s:LetterRegistrationRecordSender", Type._Uri));
					}

					has_letter_registration_record_recipient.addProperty("v-s:registrationNumber", rss);
				} else if (code.equals("date_to")) {
					if (rss.resources.size() < 1)
						continue;
					if (has_letter_registration_record_recipient == null) {
						has_letter_registration_record_recipient = new Individual();
						has_letter_registration_record_recipient.setUri(uri + "_letter_registration_record_recipient");
						has_letter_registration_record_recipient.addProperty("rdf:type",
								new Resource("v-s:LetterRegistrationRecordSender", Type._Uri));
					}

					has_letter_registration_record_recipient.addProperty("v-s:registrationDate", rss);
				} else if (code.equals("contractor")) {
					organization2 = rss;
				} else if (code.equals("department")) {
					department.add(rss);
				} else if (code.equals("final_resolution")) {
					new_individual.addProperty("stg:hasClaimDecision", rss);
				} else if (code.equals("contacts")) {
					contacts.add(rss);
				} else if (code.equals("register_number")) {
					if (rss.resources.size() < 1)
						continue;
					if (has_letter_registration_record_sender == null) {
						has_letter_registration_record_sender = new Individual();
						has_letter_registration_record_sender.setUri(uri + "_letter_registration_record_sender");
						has_letter_registration_record_sender.addProperty("rdf:type",
								new Resource("v-s:LetterRegistrationRecordSender", Type._Uri));
					}

					has_letter_registration_record_sender.addProperty("v-s:registrationNumber", rss);
				} else if (code.equals("register_date")) {
					if (rss.resources.size() < 1)
						continue;
					if (has_letter_registration_record_sender == null) {
						has_letter_registration_record_sender = new Individual();
						has_letter_registration_record_sender.setUri(uri + "_letter_registration_record_sender");
						has_letter_registration_record_sender.addProperty("rdf:type",
								new Resource("v-s:LetterRegistrationRecordSender", Type._Uri));
					}

					has_letter_registration_record_sender.addProperty("v-s:registrationDate", rss);
				} else if (code.equals("resolution")) {
					new_individual.addProperty("stg:hasClaimReport", rss);
				} else if (code.equals("other_sides")) {
					new_individual.addProperty("v-s:stakeholder", rss);
				} else if (code.equals("smena")) {
					Individual comment1 = new Individual();
					comment1.setUri(new_individual.getUri() + "_comment");
					comment1.addProperty("rdf:type", "v-s:Comment", Type._Uri);
					comment1.addProperty("rdfs:label", rss);
					comment1.addProperty("v-s:backwardTarget", new_individual.getUri(), Type._Uri);
					comment1.addProperty("v-s:backwardProperty", "v-s:hasComment", Type._Uri);
					comment1.addProperty("v-s:created", new_individual.getResources("v-s:created"));
					comment1.addProperty("v-s:creator", new_individual.getResources("v-s:creator"));
					putIndividual(level, comment1, ba_id);
					new_individual.addProperty("v-s:hasComment", comment1.getUri(), Type._Uri);
				}

				if (rss.resources.size() > 0) {
					if (code.equals("inherit_rights_from")) {
						String irf = att.getLinkValue();
						XmlDocument irf_doc = ba.getActualDocument(irf).getLeft();
						String inherit_rights_from = ba.get_first_value_of_field(irf_doc, "inherit_rights_from");

						if (inherit_rights_from == null) {
							new_individual.addProperty("v-s:hasContract", rss);
						} else {
							if (irf_doc.getTypeId().equals("ead1b2fa113c45a8b79d093e8ec14728")
									|| irf_doc.getTypeId().equals("15206d33eafa440c84c02c8d912bce7a")
									|| irf_doc.getTypeId().equals("ec6d76a99d814d0496d5d879a0056428")
									|| irf_doc.getTypeId().equals("a0e50600ebe9450e916469ee698e3faa")
									|| irf_doc.getTypeId().equals("71e3890b3c77441bad288964bf3c3d6a")
									|| irf_doc.getTypeId().equals("cab21bf8b68a4b87ac37a5b41adad8a8")
									|| irf_doc.getTypeId().equals("110fa1f351e24a2bbc187c872b114ea4")
									|| irf_doc.getTypeId().equals("d539b253cb6247a381fb51f4ee34b9d8")
									|| irf_doc.getTypeId().equals("a7b5b15a99704c9481f777fa941506c0")
									|| irf_doc.getTypeId().equals("67588724c4c54b25a2c84906613bd15a")) {
								att.setLinkValue(inherit_rights_from);
								Resources rss1 = ba_field_to_veda(level, att, uri, inherit_rights_from, doc, path,
										parent_ba_doc_id, parent_veda_doc_uri, true);

								new_individual.addProperty("v-s:hasContract", rss1);
							} else {
								new_individual.addProperty("v-s:hasContract", rss);
							}
						}
					}
				}
			}
		}

		Individual correspondent_organization = new Individual();
		correspondent_organization.setUri(uri + "_correspondent_organization_sender");

		correspondent_organization.addProperty("v-s:correspondentOrganization",
				new Resource("d:org_RU1121016110_1", Type._Uri));

		correspondent_organization.addProperty("rdf:type", new Resource("v-s:Correspondent", Type._Uri));
		correspondent_organization.addProperty("v-s:parent", new Resource(new_individual.getUri(), Type._Uri));
		correspondent_organization.addProperty("v-s:creator", new_individual.getResources("v-s:creator"));
		correspondent_organization.addProperty("v-s:created", new_individual.getResources("v-s:created"));

		new_individual.addProperty("v-s:sender", new Resource(correspondent_organization.getUri(), Type._Uri));
		putIndividual(level, correspondent_organization, ba_id);

		if (organization2 != null) {
			String indv_uri = organization2.resources.get(0).getData();

			Individual indv = veda.getIndividual(indv_uri);

			String inn = indv.getValue("v-s:taxId");

			Individual correspondent_organization2 = new Individual();
			correspondent_organization2.setUri(uri + "_correspondent_organization_recepient");
			correspondent_organization2.addProperty("rdf:type", new Resource("v-s:Correspondent", Type._Uri));
			correspondent_organization2.addProperty("v-s:parent", new Resource(new_individual.getUri(), Type._Uri));
			correspondent_organization2.addProperty("v-s:creator", new_individual.getResources("v-s:creator"));
			correspondent_organization2.addProperty("v-s:created", new_individual.getResources("v-s:created"));

			new_individual.addProperty("v-s:recipient", new Resource(correspondent_organization2.getUri(), Type._Uri));

			if (inn != null) {
				String org_uri = get_OrgUri_of_inn(inn);

				// Individual org = veda.getIndividual(org_uri);

				// if (org != null) {

				correspondent_organization2.addProperty("v-s:correspondentOrganization",
						new Resource(org_uri, Type._Uri));

			} else {
				correspondent_organization2.addProperty("v-s:hasContractor", new Resource(indv_uri, Type._Uri));

			}

			putIndividual(level, correspondent_organization2, ba_id);
		}

		if (requirement_sum != null)

		{
			new_individual.addProperty("v-s:requirementSum", new Resource(requirement_sum.getUri(), Type._Uri));
			requirement_sum.addProperty("v-s:hasCurrency", currency_from);
			requirement_sum.addProperty("v-s:parent", new Resource(new_individual.getUri(), Type._Uri));
			requirement_sum.addProperty("v-s:creator", new_individual.getResources("v-s:creator"));
			requirement_sum.addProperty("v-s:created", new_individual.getResources("v-s:created"));
			putIndividual(level, requirement_sum, ba_id);
		}

		if (compensation_sum != null) {
			new_individual.addProperty("v-s:compensationSum", new Resource(compensation_sum.getUri(), Type._Uri));
			compensation_sum.addProperty("v-s:parent", new Resource(new_individual.getUri(), Type._Uri));
			compensation_sum.addProperty("v-s:creator", new_individual.getResources("v-s:creator"));
			compensation_sum.addProperty("v-s:created", new_individual.getResources("v-s:created"));
			putIndividual(level, compensation_sum, ba_id);
		}

		if (has_letter_registration_record_recipient != null) {
			has_letter_registration_record_recipient.addProperty("v-s:parent",
					new Resource(new_individual.getUri(), Type._Uri));
			has_letter_registration_record_recipient.addProperty("v-s:creator",
					new_individual.getResources("v-s:creator"));
			has_letter_registration_record_recipient.addProperty("v-s:created",
					new_individual.getResources("v-s:created"));
			new_individual.addProperty("v-s:hasLetterRegistrationRecordRecipient",
					new Resource(has_letter_registration_record_recipient.getUri(), Type._Uri));
			putIndividual(level, has_letter_registration_record_recipient, ba_id);
		}

		if (has_letter_registration_record_sender != null) {
			has_letter_registration_record_sender.addProperty("v-s:parent",
					new Resource(new_individual.getUri(), Type._Uri));
			has_letter_registration_record_sender.addProperty("v-s:creator",
					new_individual.getResources("v-s:creator"));
			has_letter_registration_record_sender.addProperty("v-s:created",
					new_individual.getResources("v-s:created"));
			new_individual.addProperty("v-s:hasLetterRegistrationRecordSender",
					new Resource(has_letter_registration_record_sender.getUri(), Type._Uri));
			putIndividual(level, has_letter_registration_record_sender, ba_id);
		}

		if (comment != null) {
			comment.setUri(uri + "_comment");
			comment.addProperty("rdf:type", new Resource("v-s:Comment", Type._Uri));
			comment.addProperty("v-s:creator", new_individual.getResources("v-s:creator"));
			comment.addProperty("v-s:created", new_individual.getResources("v-s:created"));
			putIndividual(level, comment, ba_id);
			new_individual.addProperty("v-s:hasComment", new Resource(comment.getUri(), Type._Uri));
		}

		if (attachments.size() > 0) {
			Individual crep1 = new Individual();
			crep1.setUri(new_individual.getUri() + "_clreport");
			crep1.addProperty("rdf:type", "stg:ClaimReport", Type._Uri);
			crep1.addProperty("v-s:backwardTarget", new_individual.getUri(), Type._Uri);
			crep1.addProperty("v-s:backwardProperty", "v-s:hasComment", Type._Uri);
			crep1.addProperty("v-s:created", new_individual.getResources("v-s:created"));
			crep1.addProperty("v-s:creator", new_individual.getResources("v-s:creator"));

			for (int idx = 0; idx < attachments.size(); idx++) {
				crep1.addProperty("v-s:attachment", attachments.get(idx));
			}

			putIndividual(level, crep1, ba_id);
			new_individual.addProperty("stg:hasClaimReport", crep1.getUri(), Type._Uri);

		}

		res.add(new_individual);
		return res;
	}
}
