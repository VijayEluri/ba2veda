package sm.tools.ba2veda.impl;

import java.util.ArrayList;
import java.util.List;

import ru.mndsc.bigarchive.server.kernel.document.beans.XmlAttribute;
import ru.mndsc.bigarchive.server.kernel.document.beans.XmlDocument;
import sm.tools.ba2veda.Ba2VedaTransform;
import sm.tools.ba2veda.BaSystem;
import sm.tools.ba2veda.Replacer;
import sm.tools.veda_client.Individual;
import sm.tools.veda_client.Resource;
import sm.tools.veda_client.Resources;
import sm.tools.veda_client.Type;
import sm.tools.veda_client.VedaConnection;

public class _57da5_mnd_s_ActFailureAction extends Ba2VedaTransform {
	public _57da5_mnd_s_ActFailureAction(BaSystem _ba, VedaConnection _veda, Replacer replacer)  {
		super(_ba, _veda, replacer, "57da55c98e4d46b29bd84cd3b6fbba59", "mnd-s:ActFailureAction");
	}
	
	public void inital_set() {
		fields_map.put("responsible", "v-s:responsible");
		fields_map.put("with_whom", "v-s:contributor");
		fields_map.put("controller", "v-s:controller");
		fields_map.put("event", "v-s:description");
		fields_map.put("date_from", "v-s:dateFromPlan");
		fields_map.put("date_to", "v-s:dateToPlan");
		fields_map.put("period", "v-s:hasMaintenanceKind");
		fields_map.put("place", "v-s:placeDescription");
		fields_map.put("department", "v-s:responsibleDepartment");
		fields_map.put("department_kind", "v-s:hasSector");
		fields_map.put("classifier_budget", "v-s:hasBudgetCategory");
		fields_map.put("sum", "v-s:sum");
		fields_map.put("date_fact", "v-s:dateFact");
		fields_map.put("add_info", "v-s:hasComment");
	}
}
