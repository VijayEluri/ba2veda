package sm.tools.ba2veda.impl;

import sm.tools.ba2veda.Ba2VedaTransform;
import sm.tools.ba2veda.BaSystem;
import sm.tools.ba2veda.Replacer;
import sm.tools.veda_client.VedaConnection;

public class _bdc96_mnd_s_AuditInternal extends Ba2VedaTransform
{
	public _bdc96_mnd_s_AuditInternal(BaSystem _ba, VedaConnection _veda, Replacer replacer)
	{
		super(_ba, _veda, replacer, "bdc96094278b4c4f805d0212530d34d7", "mnd-s:AuditInternal");
	}

	public void inital_set()
	{
		fields_map.put("name", "rdfs:label");
		fields_map.put("number", "v-s:registrationNumber");
		fields_map.put("audit_name", "v-s:theme");
		fields_map.put("event_kind", "mnd-s:auditInternalUnplanned");
		fields_map.put("company", "mnd-s:hasAuditInternalObject");
		fields_map.put("auditor", "v-s:auditor");
		fields_map.put("date_from", "v-s:dateFromFact");
		fields_map.put("date_to", "v-s:dateToFact");
		fields_map.put("risk_size", "mnd-s:auditInternalRiskSize");
		fields_map.put("risk_quality", "mnd-s:auditInternalRiskQuality");
		fields_map.put("attachment", "v-s:attachment");
		fields_map.put("attachment_standards", "v-s:attachment");
		fields_map.put("action", "v-s:hasAction");
		fields_map.put("public_date", "v-s:published");
		fields_map.put("coordination_date", "mnd-s:auditInternalCoordinationDate");
		fields_map.put("comment", "rdfs:comment");
		fields_map.put("status_ex", "v-s:hasStatus");
		fields_map.put("add_doc", "v-s:hasLink");
	}

}
