package sm.tools.ba2veda.impl_mnd;

import java.util.ArrayList;
import java.util.List;

import ru.mndsc.bigarchive.server.kernel.document.beans.XmlAttribute;
import ru.mndsc.bigarchive.server.kernel.document.beans.XmlDocument;
import sm.tools.ba2veda.Ba2VedaTransform;
import sm.tools.ba2veda.BaSystem;
import sm.tools.ba2veda.Replacer;
import sm.tools.veda_client.Individual;
import sm.tools.veda_client.Resources;
import sm.tools.veda_client.Type;
import sm.tools.veda_client.VedaConnection;
import sm.tools.veda_client.util;

public class _6b0ea_v_s_Organization extends Ba2VedaTransform
{
	public _6b0ea_v_s_Organization(BaSystem _ba, VedaConnection _veda, Replacer replacer)
	{
		super(_ba, _veda, replacer, "6b0ea5c4957640c3bab5ab49b01eb01c", "v-s:Organization");
	}

	public void inital_set()
	{
		fields_map.put("name", "rdfs:label");
		fields_map.put("name_short", "v-s:shortLabel");
		fields_map.put("inn", "v-s:taxId");
		fields_map.put("address_legal", "v-s:legalAddress");
		fields_map.put("address_postal", "v-s:postalAddress");
		fields_map.put("kpp", "v-s:taxRegistrationCause");
		fields_map.put("contractor", "v-s:hasContractor");
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

				if (org_uri != null)
				{
					return res;
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

				if (code.equals("contractor"))
					rss = ba_field_to_veda(level, att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri, false);
				else
					rss = ba_field_to_veda(level, att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri, true);

				new_individual.addProperty(predicate, rss);
			}

		}

		if (org_uri != null)
		{
			new_individual.setUri(org_uri);

			res.add(new_individual);
		}

		return res;
	}

}
