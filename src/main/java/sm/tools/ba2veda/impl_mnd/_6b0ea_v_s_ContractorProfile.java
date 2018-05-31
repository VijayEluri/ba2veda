package sm.tools.ba2veda.impl_mnd;

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

public class _6b0ea_v_s_ContractorProfile extends Ba2VedaTransform
{
	public _6b0ea_v_s_ContractorProfile(BaSystem _ba, VedaConnection _veda, Replacer replacer)
	{
		super(_ba, _veda, replacer, "6b0ea5c4957640c3bab5ab49b01eb01c", "v-s:ContractorProfile");
	}

	public void inital_set()
	{
		fields_map.put("okved_code", "v-s:hasClassifierOKVED");
		fields_map.put("decision", "mnd-s:hasContractorCategoryRequest");
		fields_map.put("organization_properties", "mnd-s:attachOrganizationProperties");
		fields_map.put("certificate_registration_legal_persons_link", "mnd-s:attachCertificateRegistrationLegal");
		fields_map.put("excerpt_from_egrul_link", "mnd-s:attachExcerptFromEGRUL");
		fields_map.put("certificate_tax_registration_link", "mnd-s:attachCertificateTaxRegistration");
		fields_map.put("founding_document_link", "mnd-s:attachOtherDoc");
		fields_map.put("order_appointing_link", "mnd-s:attachAppointingOrder");
		fields_map.put("warrant", "mnd-s:attachWarrant");
		fields_map.put("signature_sample", "mnd-s:attachSignatureSample");
		fields_map.put("license_link", "mnd-s:attachLicense");
		fields_map.put("other_doc_link", "mnd-s:attachOtherDoc");
		fields_map.put("tax_report", "mnd-s:attachTaxReport");
		fields_map.put("account_balance", "mnd-s:attachAccountBalance");
		fields_map.put("tax_declaration", "mnd-s:attachTaxDeclaration");
		fields_map.put("doc_confirm_use_addresses", "mnd-s:attachDocConfirmUseAddresses");
		fields_map.put("insurance_payments", "mnd-s:attachInsurancePayments");
		fields_map.put("subcontract", "mnd-s:attachSubcontract");
	}

	@Override
	public List<Individual> transform(int level, XmlDocument doc, String ba_id, String parent_veda_doc_uri, String parent_ba_doc_id, String path)
			throws Exception
	{
		String uri = prepare_uri(ba_id);
		List<Individual> res = new ArrayList<Individual>();

		Individual new_individual = new Individual();
		new_individual.setUri(uri);

		set_basic_fields(level, new_individual, doc);

		new_individual.addProperty("rdf:type", to_class, Type._Uri);

		String org_uri = null;

		List<XmlAttribute> atts = doc.getAttributes();
		for (XmlAttribute att : atts)
		{
			String code = att.getCode();

			if (code.equals("inn"))
			{
				org_uri = get_OrgUri_of_inn(att.getTextValue());
			}

			String predicate = fields_map.get(code);

			if (code.equals("founding_document"))
			{
				code.length();
			}

			if (predicate != null)
			{
				Resources rss = ba_field_to_veda(level, att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri, true);
				new_individual.addProperty(predicate, rss);
			}
		}

		if (org_uri != null)
		{
			new_individual.addProperty("v-s:backwardTarget", new Resource(org_uri, Type._Uri));
			new_individual.addProperty("v-s:backwardProperty", new Resource("v-s:hasContractorProfile", Type._Uri));
			new_individual.addProperty("v-s:omitBackwardTargetGroup", new Resource("true", Type._Bool));

			Individual org_indv = veda.getIndividual(org_uri);

			if (org_indv != null)
			{
				Resources rslbl = org_indv.getResources("v-s:shortLabel");

				if (rslbl != null)
					new_individual.addProperty("rdfs:label", rslbl);
			} else
			{
				System.out.println("ORGANIZATION NOT FOUND IN VEDA: " + org_uri);
			}
		}

		Individual prev_indv = veda.getIndividual(new_individual.getUri());

		if (prev_indv != null)
		{
			Resources bt = prev_indv.getResources("v-s:backwardTarget");
			if (bt != null && bt.resources.size() > 0)
				return res;

		}

		res.add(new_individual);
		return res;
	}

}
