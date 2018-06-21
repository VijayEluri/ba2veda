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

public class _6044c_v_s_Contractor extends Ba2VedaTransform
{
	public _6044c_v_s_Contractor(BaSystem _ba, VedaConnection _veda, Replacer replacer)
	{
		super(_ba, _veda, replacer, "6044c7bdf6cd4338a1428b13e4e2beda", "v-s:Contractor");
	}

	public void inital_set()
	{
		fields_map.put("c_code", "v-s:registrationNumber");
		fields_map.put("short_name_contractor", "v-s:shortLabel");
		fields_map.put("full_name_contractor", "rdfs:label");
		fields_map.put("low_adress", "v-s:legalAddress");
		fields_map.put("post_adress", "v-s:postalAddress");
		fields_map.put("inn", "v-s:taxId");
		fields_map.put("kpp", "v-s:taxRegistrationCause");
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

		new_individual.addProperty("rdf:type", to_class, Type._Uri);

		String org_uri = null;
		List<XmlAttribute> atts = doc.getAttributes();

		String kpp = null;
		String inn = null;

		for (XmlAttribute att : atts)
		{
			String code = att.getCode();

			if (code.equals("inn"))
			{
				inn = att.getTextValue();

				String[] res1 = prepare_inn(inn);

				inn = res1[0];

				if (inn == null)
				{
					System.out.println("WARN:empty inn:skip:ba_id=" + ba_id);
					//return res;
				}

				kpp = res1[1];

				org_uri = res1[2];

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
			}

		}

		if (org_uri != null)	
			new_individual.addProperty("v-s:linkedOrganization", new Resource(org_uri, Type._Uri));
		
		new_individual.addProperty("v-s:isCreditor", new Resource(true, Type._Bool));
		new_individual.addProperty("v-s:isDebitor", new Resource(true, Type._Bool));

		if (org_uri != null)
		{
			new_individual.addProperty("v-s:backwardTarget", new Resource(org_uri, Type._Uri));
		}
		new_individual.addProperty("v-s:backwardProperty", new Resource("v-s:hasContractor", Type._Uri));

		if (kpp != null && kpp.length() > 0)
			new_individual.setProperty("v-s:taxRegistrationCause", new Resource(kpp, Type._String));
		
		if (inn != null && inn.length() > 0)
			new_individual.setProperty("v-s:taxId", new Resource(inn, Type._String));

		res.add(new_individual);

		return res;
	}

}
