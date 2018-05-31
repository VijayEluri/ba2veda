package sm.tools.ba2veda.impl_mnd;

import sm.tools.ba2veda.Ba2VedaTransform;
import sm.tools.ba2veda.BaSystem;
import sm.tools.ba2veda.Replacer;
import sm.tools.veda_client.VedaConnection;

public class _13e63_mnd_s_AuditSafetyObservationKind extends Ba2VedaTransform
{
	public _13e63_mnd_s_AuditSafetyObservationKind(BaSystem _ba, VedaConnection _veda, Replacer replacer)
	{
		super(_ba, _veda, replacer, "13e63952889c4177b0dbaca23bdc4d5c", "mnd-s:AuditSafetyObservationKind");
	}

	public void inital_set()
	{
		fields_map.put("name", "rdfs:label");
	}

}
