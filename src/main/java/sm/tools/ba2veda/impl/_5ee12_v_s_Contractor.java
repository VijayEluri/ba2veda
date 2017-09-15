package sm.tools.ba2veda.impl;

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

public class _5ee12_v_s_Contractor extends Ba2VedaTransform
{
	public _5ee12_v_s_Contractor(BaSystem _ba, VedaConnection _veda, Replacer replacer)
	{
		super(_ba, _veda, replacer, "5ee12a7388384cd6b3ce2c2de4a86376", "v-s:Contractor");
	}

	public void inital_set()
	{
		fields_map.put("name", "rdfs:label");
		fields_map.put("code", "v-s:shortLabel");
		fields_map.put("number", "v-s:registrationNumber");
		fields_map.put("inn", "v-s:taxId");
		fields_map.put("owner", "mnd-s:forOrganization");
	}

	@Override
	public List<Individual> transform(XmlDocument doc, String ba_id, String parent_veda_doc_uri, String parent_ba_doc_id, String path)
			throws Exception
	{
		String uri = prepare_uri(ba_id);
		List<Individual> res = new ArrayList<Individual>();

		Individual origin = veda.getIndividual(uri);

		Individual new_individual = new Individual();
		new_individual.setUri(uri);

		set_basic_fields(new_individual, doc);

		new_individual.addProperty("rdf:type", to_class, Type._Uri);

		String org_uri = null;
		Resources city = null;
		Resources address = null;
		Resources zip = null;
		boolean is_blocked = false, is_deleted = false, is_creditor = false, is_debitor = false;
		String inn = null;
		String number = null;

		List<XmlAttribute> atts = doc.getAttributes();
		for (XmlAttribute att : atts)
		{
			String code = att.getCode();

			if (code.equals("blocked"))
			{
				if (att.getTextValue().equals("X"))
					is_blocked = true;
				continue;
			}
			if (code.equals("deleted"))
			{
				if (att.getTextValue().equals("X"))
					is_deleted = true;
				continue;
			}
			if (code.equals("creditor"))
			{
				if (att.getTextValue().equals("X"))
					is_creditor = true;
				continue;
			}
			if (code.equals("debitor"))
			{
				if (att.getTextValue().equals("X"))
					is_debitor = true;
				continue;
			}

			if (code.equals("number"))
			{
				number = att.getTextValue();
			}

			if (code.equals("inn"))
			{
				inn = att.getTextValue();

				org_uri = get_OrgUri_of_inn(inn);
			}

			String predicate = fields_map.get(code);
			if (predicate != null)
			{
				Resources rss = ba_field_to_veda(att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri, true);
				new_individual.addProperty(predicate, rss);
			} else
			{
				if (code.equals("city"))
				{
					city = ba_field_to_veda(att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri, true);
				} else if (code.equals("address"))
				{
					address = ba_field_to_veda(att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri, true);
				} else if (code.equals("zip"))
				{
					zip = ba_field_to_veda(att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri, true);
				}
			}

		}

		new_individual.addProperty("v-s:lockedInOrigin", new Resource(is_blocked, Type._Bool));
		new_individual.addProperty("v-s:deletedInOrigin", new Resource(is_deleted, Type._Bool));
		new_individual.addProperty("v-s:isCreditor", new Resource(is_creditor, Type._Bool));
		new_individual.addProperty("v-s:isDebitor", new Resource(is_debitor, Type._Bool));
		new_individual.addProperty("v-s:origin", new Resource("SAP", Type._String));

		if (origin != null)
		{
			Resources locked = origin.getResources("v-s:locked");
			Resources hasContractorLock = origin.getResources("mnd-s:hasContractorLock");

			if (locked != null)
				new_individual.addProperty("v-s:locked", locked);

			if (hasContractorLock != null)
				new_individual.addProperty("mnd-s:hasContractorLock", hasContractorLock);
		}

		Object[] ff =
		{ city, ", ", address, ", ", zip };
		String[] langs_out =
		{ "EN", "RU" };
		Resources rss = rs_assemble(ff, langs_out);
		if (rss.resources.size() == 0)
		{
			String[] langs_out2 =
			{ "NONE" };
			rss = rs_assemble(ff, langs_out2);
		}
		if (rss.resources.size() > 0)
			new_individual.addProperty("v-s:legalAddress", rss);

		Individual linkedOrganization = null;
		if (org_uri != null)
		{
			linkedOrganization = veda.getIndividual(org_uri);
			new_individual.addProperty("v-s:linkedOrganization", new Resource(org_uri, Type._Uri));

			new_individual.addProperty("v-s:backwardTarget", new Resource(org_uri, Type._Uri));
			new_individual.addProperty("v-s:backwardProperty", new Resource("v-s:hasContractor", Type._Uri));
		}

		if (number != null && number.length() > 2 && (number.charAt(0) == 'Ф' || number.charAt(0) == 'П'))
		{
			System.out.println("КОНТРАГЕНТ с number[" + number + "] для [" + ba_id + "] НЕ ПРОШЕЛ ФИЛЬТР");
			return res;
		}

		if (parent_veda_doc_uri == null && inn != null && inn.length() > 3 && (inn.charAt(0) == 'Ф' || inn.charAt(0) == 'П'))
		{
			System.out.println("КОНТРАГЕНТ с инн[" + inn + "] для [" + ba_id + "] НЕ ПРОШЕЛ ФИЛЬТР");
			return res;
		}

		if (linkedOrganization != null)
		{
			Resources hasContractor = linkedOrganization.getResources("v-s:hasContractor");

			if (hasContractor == null)
			{
				hasContractor = new Resources();
			}

			boolean bb = false;
			for (Resource el : hasContractor.resources)
			{
				if (el.getData().equals(new_individual.getUri()))
				{
					bb = true;
					break;
				}
			}

			if (bb == false)
			{
				hasContractor.add(new_individual.getUri(), Type._Uri);

				linkedOrganization.setProperty("v-s:hasContractor", hasContractor);

				res.add(linkedOrganization);
			}
		}

		if (parent_veda_doc_uri == null && (org_uri == null || org_uri.length() < 3))
		{
			System.out.println("КОНТРАГЕНТ с инн[" + inn + "] для [" + ba_id + "] НЕ НАЙДЕН");
			res.add(new_individual);
		} else
			res.add(new_individual);
		
		return res;
	}

}
