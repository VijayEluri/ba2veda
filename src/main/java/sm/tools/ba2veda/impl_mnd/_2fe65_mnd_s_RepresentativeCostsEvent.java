package sm.tools.ba2veda.impl_mnd;

import sm.tools.ba2veda.Ba2VedaTransform;
import sm.tools.ba2veda.BaSystem;
import sm.tools.ba2veda.Replacer;
import sm.tools.veda_client.VedaConnection;

public class _2fe65_mnd_s_RepresentativeCostsEvent extends Ba2VedaTransform {
	public _2fe65_mnd_s_RepresentativeCostsEvent(BaSystem _ba, VedaConnection _veda, Replacer replacer)  {
		super(_ba, _veda, replacer, "2fe65803dcb847f0ac0d93c9a42d7596", "mnd-s:RepresentativeCostsEvent");
	}
	
	public void inital_set() {
		fields_map.put("date", "v-s:dateTo");
		fields_map.put("event", "v-s:description");
		fields_map.put("responsible", "v-s:responsible");
	}
}
