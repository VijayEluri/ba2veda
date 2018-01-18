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

public class _df75b_mnd_s_AuditSafety extends Ba2VedaTransform
{
	public _df75b_mnd_s_AuditSafety(BaSystem _ba, VedaConnection _veda, Replacer replacer)
	{
		super(_ba, _veda, replacer, "df75b123521d4ab99f881b33a5470f81", "mnd-s:AuditSafety");
	}

	public void inital_set()
	{
		fields_map.put("8", "v-s:registrationNumber");
		fields_map.put("4", "v-s:checkedDepartment");
		fields_map.put("contractor", "v-s:checkedOrganization");
		fields_map.put("1", "v-s:dateFact");
		fields_map.put("6", "v-s:auditor");
		fields_map.put("Список рассылки", "v-s:copyTo");
		fields_map.put("Замечание аудита", "v-s:hasAction");
		fields_map.put("action", "v-s:hasAction");
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

		Resources registrationNumber = null;
		Resources checkedDepartment = null;
		Resources dateFact = null;
		Resources checkedOrganization = null;

		List<XmlAttribute> atts = doc.getAttributes();
		for (XmlAttribute att : atts)
		{
			String code = att.getCode();

			String predicate = fields_map.get(code);
			if (code.equals("8"))
				registrationNumber = ba_field_to_veda(level, att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri, true);
			else if (code.equals("4"))
				checkedDepartment = ba_field_to_veda(level, att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri, true);
			else if (code.equals("1"))
				dateFact = ba_field_to_veda(level, att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri, true);
			else if (code.equals("contractor"))
				checkedOrganization = ba_field_to_veda(level, att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri, true);

			if (predicate != null)
			{
				Resources rss = ba_field_to_veda(level, att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri, true);

				new_individual.addProperty(predicate, rss);
			}
		}

		Individual dep;
		Resources dep_label = null;
		if (checkedDepartment != null && checkedDepartment.resources.size() > 0)
		{
			dep = veda.getIndividual(checkedDepartment.resources.get(0).getData());
			if (dep != null)
				dep_label = dep.getResources("rdfs:label");
			else
				System.out.println("ERR: DEPARTMENT NOT FOUND: " + checkedDepartment.resources.get(0).getData());
			dep_label = dep.getResources("rdfs:label");

			if (dep.getUri().indexOf("d:org_") >= 0)
				new_individual.addProperty("v-s:checkedOrganization", checkedDepartment);
		}

		// if (checkedOrganization != null && checkedOrganization.resources.size() > 0)
		// new_individual.addProperty("v-s:checkedOrganization", checkedOrganization);

		Object[] ff =
		{ registrationNumber, ", ", dep_label, ", ", dateFact };
		String[] langs_out =
		{ "EN", "RU" };
		Resources rss = rs_assemble(ff, langs_out);
		new_individual.addProperty("rdfs:label", rss);

		new_individual.addProperty("mnd-s:isSafetyIncident", new Resource(true, Type._Bool));

		res.add(new_individual);
		return res;
	}

}
