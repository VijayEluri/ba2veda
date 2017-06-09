package sm.tools.ba2veda.impl;

import sm.tools.ba2veda.Ba2VedaTransform;
import sm.tools.ba2veda.BaSystem;
import sm.tools.ba2veda.Replacer;
import sm.tools.veda_client.VedaConnection;

public class _662_v_s_DeliveryMethod extends Ba2VedaTransform
{
	public _662_v_s_DeliveryMethod(BaSystem _ba, VedaConnection _veda, Replacer replacer)
	{
		super(_ba, _veda, replacer, "662", "v-s:DeliveryMethod");
	}

	public void inital_set()
	{
		fields_map.put("667_3", "rdfs:label");
	}

}
