package sm.tools.ba2veda.impl;

import sm.tools.ba2veda.Ba2VedaTransform;
import sm.tools.ba2veda.BaSystem;
import sm.tools.ba2veda.Replacer;
import sm.tools.veda_client.VedaConnection;

public class _5c2ce_mnd_s_Building extends Ba2VedaTransform {
	public _5c2ce_mnd_s_Building(BaSystem _ba, VedaConnection _veda, Replacer replacer) {
		super(_ba, _veda, replacer, "5c2ce098-66a1-471c-886f-addb170f98da", "mnd-s:Building");
	}

	public void inital_set() {
		fields_map.put("8a469b6d-df5e-4bea-81ae-8a6264c00270_7", "v-s:hasParentLink");
		fields_map.put("8a469b6d-df5e-4bea-81ae-8a6264c00270_3", "rdfs:label");
		fields_map.put("Шифр", "v-s:shortLabel");
	}
}
