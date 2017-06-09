package sm.tools.ba2veda.impl;

import sm.tools.ba2veda.Ba2VedaTransform;
import sm.tools.ba2veda.BaSystem;
import sm.tools.ba2veda.Replacer;
import sm.tools.veda_client.VedaConnection;

public class _19faf_mnd_s_AuditSafetyTheme extends Ba2VedaTransform
{
	public _19faf_mnd_s_AuditSafetyTheme(BaSystem _ba, VedaConnection _veda, Replacer replacer)
	{
		super(_ba, _veda, replacer, "19faf6e8-8843-4426-a905-ce2b0a1a07e0", "mnd-s:AuditSafetyTheme");
	}

	public void inital_set()
	{
		fields_map.put("d53472e4-7838-47fe-8993-b8aa1d8c33aa_3", "rdfs:label");
		fields_map.put("name_short", "v-s:shortLabel");
	}

}
