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
import sm.tools.veda_client.util;

public class _6044c_v_s_Organization extends Ba2VedaTransform
{
	public _6044c_v_s_Organization(BaSystem _ba, VedaConnection _veda, Replacer replacer)
	{
		super(_ba, _veda, replacer, "6044c7bdf6cd4338a1428b13e4e2beda", "v-s:Organization");
	}

	public void inital_set()
	{
		fields_map.put("c_code", "?");
		fields_map.put("short_name_contractor", "v-s:shortLabel");
		fields_map.put("full_name_contractor", "rdfs:label");
		fields_map.put("low_adress", "v-s:legalAddress");
		fields_map.put("post_adress", "v-s:postalAddress");
		fields_map.put("inn", "v-s:taxId");
		fields_map.put("kpp", "v-s:taxRegistrationCause");

		employee_prefix = "d:employee_";
		appointment_prefix = "d:";
		stand_prefix = "d:";
		department_prefix = "department";
		is_mondi = false;
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

		Resources c_code = null;

		String org_uri = null;
		List<XmlAttribute> atts = doc.getAttributes();
		for (XmlAttribute att : atts)
		{
			String code = att.getCode();

			if (code.equals("inn"))
			{
				org_uri = get_OrgUri_of_inn(att.getTextValue());

				if (org_uri != null)
				{
					//return res;
				} else
				{
					String inn = att.getTextValue();
					if (inn != null && inn.length() > 0)
					{
						inn = inn.trim();

						if (util.isNumeric(inn)/* && inn.length() == 10 || inn.length() == 12 */)
							org_uri = "d:org_RU" + inn;
						else
						{
							org_uri = "d:org_" + inn;

							if (inn.indexOf('/') >= 0 || inn.indexOf(' ') >= 0 || inn.indexOf('\\') >= 0 || inn.indexOf('+') >= 0
									|| inn.indexOf('"') >= 0 || inn.indexOf('\'') >= 0 || inn.indexOf('	') >= 0)
								System.out.println("WARN:invalid inn:" + inn);
						}
					} else
					{
						org_uri = null;
					}
				}
			}
			String predicate = fields_map.get(code);

			if (predicate != null)
			{
				Resources rss;

				rss = ba_field_to_veda(level, att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri, true);

				if (predicate.equals("?") == false)
					new_individual.addProperty(predicate, rss);

				if (rss.resources.size() < 1)
					continue;

				if (code.equals("c_code"))
				{
					c_code = rss;
				}
			}

		}

		if (c_code != null)
		{
			Individual contractor = new Individual();
			contractor.setUri(new_individual.getUri());
			contractor.addProperty("rdf:type", new Resource("v-s:Contractor", Type._Uri));
			contractor.addProperty("v-s:linkedOrganization", new Resource(org_uri, Type._Uri));
			contractor.addProperty("v-s:creator", new_individual.getResources("v-s:creator"));
			contractor.addProperty("v-s:created", new_individual.getResources("v-s:created"));
			contractor.addProperty("v-s:shortLabel", new_individual.getResources("v-s:shortLabel"));
			contractor.addProperty("rdfs:label", new_individual.getResources("rdfs:label"));
			contractor.addProperty("v-s:legalAddress", new_individual.getResources("v-s:legalAddress"));
			contractor.addProperty("v-s:taxId", new_individual.getResources("v-s:taxId"));

			contractor.addProperty("v-s:backwardTarget", new Resource(org_uri, Type._Uri));
			contractor.addProperty("v-s:backwardProperty", new Resource("v-s:hasContractor"));

			contractor.addProperty("v-s:registrationNumber", c_code);
			putIndividual(level, contractor, ba_id);
		}

		if (org_uri != null)
		{
			new_individual.setUri(org_uri);

			res.add(new_individual);
		}

		return res;
	}

}
