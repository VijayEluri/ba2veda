package sm.tools.ba2veda.impl;

import sm.tools.ba2veda.Ba2VedaTransform;
import sm.tools.ba2veda.BaSystem;
import sm.tools.ba2veda.Replacer;
import sm.tools.veda_client.VedaConnection;

public class _a5931_mnd_s_DescriptionOfChange extends Ba2VedaTransform {
	public _a5931_mnd_s_DescriptionOfChange(BaSystem _ba, VedaConnection _veda, Replacer replacer)  {
		super(_ba, _veda, replacer, "a593174e33ac4b8c8a0c5b0ee8216f75", "mnd-s:DescriptionOfChange");
	}
	
	public void inital_set() {
		fields_map.put("asis", "v-s:asis");
		fields_map.put("tobe", "v-s:tobe");
	}
}
