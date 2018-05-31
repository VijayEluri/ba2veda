package sm.tools.ba2veda.impl_mnd;

import sm.tools.ba2veda.Ba2VedaTransform;
import sm.tools.ba2veda.BaSystem;
import sm.tools.ba2veda.Replacer;
import sm.tools.veda_client.VedaConnection;

public class _fc534_v_s_NormativeDocument extends Ba2VedaTransform
{
	public _fc534_v_s_NormativeDocument(BaSystem _ba, VedaConnection _veda, Replacer replacer)
	{
		super(_ba, _veda, replacer, "fc534b64efdb4dbe93f28c3a6f53240c", "v-s:NormativeDocument");
	}

	public void inital_set()
	{
		fields_map.put("Название", "rdfs:label");
		fields_map.put("Шифр", "v-s:shortLabel");
		fields_map.put("Вложение", "v-s:attachment");
	}

}
