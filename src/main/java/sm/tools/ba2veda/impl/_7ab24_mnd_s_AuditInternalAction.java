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

public class _7ab24_mnd_s_AuditInternalAction extends Ba2VedaTransform
{
	public _7ab24_mnd_s_AuditInternalAction(BaSystem _ba, VedaConnection _veda, Replacer replacer)
	{
		super(_ba, _veda, replacer, "7ab24b50f0414824a6edd2e887bc3c44", "mnd-s:AuditInternalAction");
	}

	public void inital_set()
	{
		fields_map.put("process", "v-s:responsibleProcess");
		fields_map.put("department", "v-s:responsibleDepartment");
		fields_map.put("responsible", "v-s:responsible");
		fields_map.put("with_who", "v-s:responsibleDescription");
		fields_map.put("author_recommendation", "v-s:author");
		fields_map.put("risk", "mnd-s:auditInternalRiskSize");
		fields_map.put("sum", "v-s:possibleLossAmount");
		fields_map.put("root_cause", "v-s:reason");
		fields_map.put("risk_kind", "mnd-s:hasRiskKindCOSO");
		fields_map.put("implication", "v-s:consequence");
		fields_map.put("issue", "v-s:deviationDescription");
		fields_map.put("recommendation", "v-s:description");
		fields_map.put("event", "mnd-s:auditInternalActionManagementDecision");
		fields_map.put("date_from", "v-s:dateFromPlan");
		fields_map.put("date_to", "v-s:dateToPlan");
		fields_map.put("add_info", "mnd-s:hasAuditInternalReport");
		fields_map.put("status_ex", "v-s:hasStatus");
		fields_map.put("area", "mnd-s:hasAuditInternalArea");
		fields_map.put("date_fact", "v-s:dateFact");
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

		Resources auto_number = null;
		Resources parent_number = null;
		Resources parent_company = null;
		Resources parent_audit_name = null;
		Resources parent_name = null;

		List<XmlAttribute> atts = doc.getAttributes();
		for (XmlAttribute att : atts)
		{
			String code = att.getCode();

			if (code.equals("risk_kind"))
				code.length();
			
			String predicate = fields_map.get(code);
			if (code.equals("auto_number"))
				auto_number = ba_field_to_veda(level, att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri, true);
			if (predicate != null)
			{
				Resources rss = ba_field_to_veda(level, att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri, true);
				
				if (code.equals("status_ex")) {
					if (att.getRecordIdValue().equals("4363a85a-1b4f-4c0b-89be-fe1326b9bae9")) {
						new_individual.addProperty(predicate, new Resource("v-s:StatusExecution", Type._Uri));
						continue;
					} else if (att.getRecordIdValue().equals("8e1d546b-e70e-4f88-a4de-091fc7a247c3")) {
						new_individual.addProperty(predicate, new Resource("v-s:StatusExecuted", Type._Uri));
						continue;
					}
				}

				new_individual.addProperty(predicate, rss);
				
				
			}
		}

		if (parent_veda_doc_uri != null)
			new_individual.addProperty("v-s:parent", parent_veda_doc_uri, Type._Uri);

		XmlDocument parent_ba_doc = ba.getDocumentOnTimestamp(parent_ba_doc_id, 0);
		if (parent_ba_doc != null)
		{
			atts = parent_ba_doc.getAttributes();
			for (XmlAttribute att : atts)
			{
				String code = att.getCode();				
				
				if (code.equals("name"))
				{
					parent_name = ba_field_to_veda(level, att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri, true);
				} else if (code.equals("number"))
				{
					parent_number = ba_field_to_veda(level, att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri, true);
				} else if (code.equals("company"))
				{
					parent_company = ba_field_to_veda(level, att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri, true);
					new_individual.addProperty("mnd-s:hasAuditInternalObject", parent_company);
				} else if (code.equals("audit_name"))
				{
					parent_audit_name = ba_field_to_veda(level, att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri, true);
					new_individual.addProperty("v-s:theme", parent_audit_name);
				}
			}

			Object[] ff =
			{ auto_number, ".", parent_name };
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
				new_individual.addProperty("rdfs:label", rss);
		}

		Object[] ff =
		{ parent_number, ".", auto_number };
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
			new_individual.addProperty("v-s:registrationNumber", rss);

		res.add(new_individual);

		return res;
	}

}
