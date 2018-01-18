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

public class _74eb8_mnd_s_AuditSafetyAction extends Ba2VedaTransform
{
	public _74eb8_mnd_s_AuditSafetyAction(BaSystem _ba, VedaConnection _veda, Replacer replacer)
	{
		super(_ba, _veda, replacer, "74eb8e54882545f3a35c1acae761500e", "mnd-s:AuditSafetyAction");
	}

	public void inital_set()
	{
		fields_map.put("auto_num", "v-s:registrationNumber");
		fields_map.put("Вложение", "v-s:attachment");
		fields_map.put("Место", "v-s:placeDescription");
		fields_map.put("Наблюдение", "v-s:observationDescription");
		fields_map.put("characteristic", "v-s:isPositiveObservation");
		fields_map.put("kind", "mnd-s:hasAuditSafetyObservationKind");
		fields_map.put("Вид требования", "mnd-s:hasAuditSafetyTheme");
		fields_map.put("Основание", "mnd-s:basedOnNormativeDocument");
		fields_map.put("Основание текст", "v-s:rationale");
		fields_map.put("Подразделение", "v-s:responsibleDepartment");
		fields_map.put("responsible", "v-s:responsible");
		fields_map.put("contractor", "v-s:responsibleOrganization");
		fields_map.put("responsible_add", "mnd-s:responsibleFromContractor");
		// fields_map.put("add_to_doc", "v-s:parent");
		fields_map.put("risk", "v-s:hasPriorityLevel");
		fields_map.put("date_to", "v-s:datePlan");
		fields_map.put("date_fact", "v-s:dateFact");
		fields_map.put("Тема", "v-s:hasSector");
		fields_map.put("add_info", "v-s:hasComment");
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

		Resources description1 = null;
		Resources description2 = null;
		Resources registrationNumber = null;
		Resources responsibleDepartment = null;
		Resources datePlan = null;

		List<XmlAttribute> atts = doc.getAttributes();
		for (XmlAttribute att : atts)
		{
			String code = att.getCode();

			String predicate = fields_map.get(code);
			if (code.equals("Мероприятие для предотвращения"))
				description1 = ba_field_to_veda(level, att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri, true);
			else if (code.equals("Мероприятие незамедлительное"))
				description2 = ba_field_to_veda(level, att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri, true);
			if (code.equals("auto_num"))
				registrationNumber = ba_field_to_veda(level, att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri, true);
			else if (code.equals("Подразделение"))
				responsibleDepartment = ba_field_to_veda(level, att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri, true);
			else if (code.equals("date_to"))
				datePlan = ba_field_to_veda(level, att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri, true);

			if (code.equals("kind"))
				code.length();

			if (predicate != null)
			{
				Resources rss = ba_field_to_veda(level, att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri, true);

				new_individual.addProperty(predicate, rss);
			}
		}

		// Для предотвращения аналогичных нарушений в будущем необходимо:
		// Для устранения выявленного замечания необходимо:

		Object[] ff =
		{ description1, "\n ", description2 };
		String[] langs_out =
		{ "EN", "RU", "NONE" };
		Resources rss = rs_assemble(ff, langs_out);
		new_individual.addProperty("v-s:description", rss);

		Individual dep;
		Resources dep_label = null;
		if (responsibleDepartment != null && responsibleDepartment.resources.size() > 0)
		{
			dep = veda.getIndividual(responsibleDepartment.resources.get(0).getData());
			if (dep != null)
				dep_label = dep.getResources("rdfs:label");
			else
				System.out.println("ERR: DEPARTMENT NOT FOUND: " + responsibleDepartment.resources.get(0).getData());
		}
		Object[] ff1 =
		{ registrationNumber, ", ", dep_label, ", ", datePlan };
		String[] langs_out1 =
		{ "EN", "RU" };
		rss = rs_assemble(ff1, langs_out1);
		if (rss.resources.size() == 0)
		{
			String[] langs_out2 =
			{ "NONE" };
			rss = rs_assemble(ff1, langs_out2);
		}

		if (rss.resources.size() > 0)
			new_individual.addProperty("rdfs:label", rss);

		if (parent_veda_doc_uri != null)
		{
			new_individual.addProperty("v-s:parent", new Resource(parent_veda_doc_uri, Type._Uri));

			Individual parent_indv = veda.getIndividual(parent_veda_doc_uri);

			if (parent_indv != null)
			{
				Resources isSafetyIncident = parent_indv.getResources("mnd-s:isSafetyIncident");

				if (isSafetyIncident != null && isSafetyIncident.resources.size() > 0)
				{
					new_individual.addProperty("mnd-s:isSafetyIncident", isSafetyIncident);
				}
			}
		}

		res.add(new_individual);
		return res;
	}

}
