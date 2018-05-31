package sm.tools.ba2veda.impl_mnd;

import sm.tools.ba2veda.Ba2VedaTransform;
import sm.tools.ba2veda.BaSystem;
import sm.tools.ba2veda.Replacer;
import sm.tools.veda_client.VedaConnection;

public class _dabb6_mnd_s_MaterialGroup extends Ba2VedaTransform {
	public _dabb6_mnd_s_MaterialGroup(BaSystem _ba, VedaConnection _veda, Replacer replacer)  {
		super(_ba, _veda, replacer, "dabb67b8bc5d40f6b347025c6970025f", "mnd-s:MaterialGroup");
	}
	
	public void inital_set() {
		fields_map.put("group", "v-s:title");
		fields_map.put("title", "rdfs:label");
		fields_map.put("comment", "rdfs:comment");
		fields_map.put("controller", "v-s:controller");
		fields_map.put("responsible", "v-s:responsible");
	}
}
