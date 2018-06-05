package sm.tools.ba2veda.impl_stg;

import sm.tools.ba2veda.Ba2VedaTransform;
import sm.tools.ba2veda.BaSystem;
import sm.tools.ba2veda.Replacer;
import sm.tools.veda_client.VedaConnection;

public class _07958_stg_PositionDictionary extends Ba2VedaTransform
{
	public _07958_stg_PositionDictionary(BaSystem _ba, VedaConnection _veda, Replacer replacer)
	{
		super(_ba, _veda, replacer, "07958ee706dc4025b4eec7aca0c9b040", "stg:PositionDictionary");
	}

	public void inital_set()
	{
		fields_map.put("profession_value", "rdfs:label");
	}

}
