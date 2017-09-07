package sm.tools.ba2veda.impl;

import sm.tools.ba2veda.Ba2VedaTransform;
import sm.tools.ba2veda.BaSystem;
import sm.tools.ba2veda.Replacer;
import sm.tools.veda_client.VedaConnection;

public class _24b39_mnd_s_RepairInformation extends Ba2VedaTransform{
	public _24b39_mnd_s_RepairInformation(BaSystem _ba, VedaConnection _veda, Replacer replacer)  {
		super(_ba, _veda, replacer, "d4b397afbbd54a4da56b5aca7d742a58", "mnd-s:RepairInformation");
	}
	
	public void inital_set() {
		fields_map.put("date_from", "v-s:registrationDate");
		fields_map.put("number_reg", "v-s:registrationNumber");
		fields_map.put("description", "v-s:description");
		fields_map.put("failure_act", "v-s:hasParentLink");
	}
}
