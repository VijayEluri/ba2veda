package sm.tools.ba2veda.impl_mnd;

import sm.tools.ba2veda.Ba2VedaTransform;
import sm.tools.ba2veda.BaSystem;
import sm.tools.ba2veda.Replacer;
import sm.tools.veda_client.VedaConnection;

public class _c8a33_mnd_s_AuditInternalObject extends Ba2VedaTransform
{
	public _c8a33_mnd_s_AuditInternalObject(BaSystem _ba, VedaConnection _veda, Replacer replacer)
	{
		super(_ba, _veda, replacer, "c8a330abafae4da397d59cfa22bb3b71", "mnd-s:AuditInternalObject");
	}

	public void inital_set()
	{
		fields_map.put("name", "rdfs:label");
		fields_map.put("unit", "mnd-s:AuditInternalObjectBusinessUnit");
		fields_map.put("segment", "mnd-s:AuditInternalObjectBusinessSegment");
		fields_map.put("services", "mnd-s:AuditInternalObjectService");
		fields_map.put("audit_object", "rdfs:label");
		fields_map.put("comment", "rdfs:comment");
		fields_map.put("cty", "mnd-s:AuditInternalObjectCTY");
		fields_map.put("label", "v-s:shortLabel");
	}
}
