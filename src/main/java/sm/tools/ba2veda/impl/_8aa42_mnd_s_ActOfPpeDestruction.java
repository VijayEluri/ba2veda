package sm.tools.ba2veda.impl;

import sm.tools.ba2veda.Ba2VedaTransform;
import sm.tools.ba2veda.BaSystem;
import sm.tools.ba2veda.Replacer;
import sm.tools.veda_client.VedaConnection;

public class _8aa42_mnd_s_ActOfPpeDestruction extends Ba2VedaTransform{
	public _8aa42_mnd_s_ActOfPpeDestruction(BaSystem _ba, VedaConnection _veda, Replacer replacer)  {
		super(_ba, _veda, replacer, "8aa42683d92643799bc88aa36a835d8b", "mnd-s:ActOfPpeDestruction");
	}
	
	public void inital_set() {
		fields_map.put("number_reg", "v-s:registrationNumber");
		fields_map.put("date_reg", "v-s:registrationDate");
		fields_map.put("department", "v-s:owner");
		fields_map.put("responsible", "v-s:responsible");
		fields_map.put("resource", "v-s:title");
		fields_map.put("date_from", "v-s:dateFromPlan");
		fields_map.put("chief", "v-s:chiefOfWorkGroup");
		fields_map.put("member", "v-s:member");
		fields_map.put("condition", "v-s:productionCondition");
		fields_map.put("desctiption", "rdfs:comment");
		fields_map.put("deviation", "v-s:deviationDescription");
		fields_map.put("classifier_decision", "mnd-s:hasPpeDestructionDecisionKind");
		fields_map.put("attachment", "v-s:attachment");
	}
}
