package sm.tools.ba2veda.impl;

import sm.tools.ba2veda.Ba2VedaTransform;
import sm.tools.ba2veda.BaSystem;
import sm.tools.ba2veda.Replacer;
import sm.tools.veda_client.VedaConnection;

public class _b4a29_mnd_s_ConatractDirection extends Ba2VedaTransform {
	public _b4a29_mnd_s_ConatractDirection(BaSystem _ba, VedaConnection _veda, Replacer replacer) {
		super(_ba, _veda, replacer, "b4a2927d05e54442ba78f5e8ec32bd9d", "mnd-s:ConatractDirection");
	}
	
	public void inital_set() {
		fields_map.put("name", "rdfs:label");
	}
}
