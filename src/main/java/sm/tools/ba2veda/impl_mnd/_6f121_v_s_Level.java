package sm.tools.ba2veda.impl_mnd;

import sm.tools.ba2veda.Ba2VedaTransform;
import sm.tools.ba2veda.BaSystem;
import sm.tools.ba2veda.Replacer;
import sm.tools.veda_client.VedaConnection;

public class _6f121_v_s_Level extends Ba2VedaTransform
{
	public _6f121_v_s_Level(BaSystem _ba, VedaConnection _veda, Replacer replacer)
	{
		super(_ba, _veda, replacer, "6f121c4c4dc745e5a794c9b18c924ffa", "v-s:Level");
	}

	public void inital_set()
	{
		fields_map.put("name", "rdfs:label");
	}

}
