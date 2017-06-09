package sm.tools.ba2veda.impl;

import sm.tools.ba2veda.Ba2VedaTransform;
import sm.tools.ba2veda.BaSystem;
import sm.tools.ba2veda.Replacer;
import sm.tools.veda_client.VedaConnection;

public class _74eb8_mnd_s_AuditSafetyObservation extends Ba2VedaTransform
{
	public _74eb8_mnd_s_AuditSafetyObservation(BaSystem _ba, VedaConnection _veda, Replacer replacer)
	{
		super(_ba, _veda, replacer, "74eb8e54882545f3a35c1acae761500e", "mnd-s:AuditSafetyObservation");
	}

	public void inital_set()
	{
		fields_map.put("add_to_doc", "v-s:parent");
		fields_map.put("Вложение", "v-s:attachment");
		fields_map.put("Место", "v-s:placeDescription");
		fields_map.put("Наблюдение", "v-s:description");
		fields_map.put("characteristic", "v-s:isPositiveObservation");
		fields_map.put("kind", "mnd-s:hasAuditSafetyObservationKind");
		fields_map.put("Вид требования", "mnd-s:hasAuditSafetyTheme");
		fields_map.put("Основание", "mnd-s:basedOnNormativeDocument");
		fields_map.put("Основание текст", "v-s:rationale");
	}

}
