package sm.tools.ba2veda.impl_mnd;

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
		
		fields_map.put("status", "?");
	}
	
	@Override
	public List<Individual> transform(int level, XmlDocument doc, String ba_id, String parent_veda_doc_uri,
		String parent_ba_doc_id, String path) throws Exception {
		
		String uri = prepare_uri(ba_id);
		List<Individual> res = new ArrayList<Individual>();

		Individual new_individual = new Individual();
		new_individual.setUri(uri);

		set_basic_fields(level, new_individual, doc);
		
		new_individual.addProperty("rdf:type", new Resource(to_class, Type._Uri));
		new_individual.addProperty("v-s:parent", new Resource(parent_veda_doc_uri, Type._Uri));
		
		int member = 0;

		List<XmlAttribute> atts = doc.getAttributes();
		for (XmlAttribute att : atts) {
			String code = att.getCode();

			String predicate = fields_map.get(code);
			System.out.println("CODE: " + code);
			
			if (predicate != null) {
				Resources rss = ba_field_to_veda(level, att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri,
						true);
					
				if (predicate.equals("?") == false)
					new_individual.addProperty(predicate, rss);
				
				if (rss == null)
					continue;
				
				if (rss.resources.size() < 1)
					continue;
				
				if (code.equals("status")) {
					Individual s = new Individual();
					s.setUri(new_individual.getUri() + "_status");
					s.addProperty("rdf:type", new Resource("mnd-s:StatusManualForAFAction", Type._Uri));
					s.addProperty("v-s:backwardTarget", new Resource(new_individual.getUri(), Type._Uri));
					s.addProperty("v-s:backwardProperty", 
							new Resource("mnd-s:hasStatusManualForAFAction", Type._Uri));
					s.addProperty("v-s:omitBackwardTargetGroup", new Resource(true, Type._Bool));
					s.addProperty("v-s:created", new_individual.getResources("v-s:created"));
					s.addProperty("v-s:creator", new Resource("d:mondi_appointment_00058240_71024477", Type._Uri));
				
					switch (att.getRecordIdValue()) {
					case "e9ede869a25845d3a4d715af0839c344":
					case "13332f3607eb4787b4954ad3a1621ac8":
						s.addProperty("v-s:hasStatus", new Resource("v-s:StatusExecuted", Type._Uri));
						break;
					case "8b0258717c0b46dbaf95027b1e367f41":
						s.addProperty("v-s:hasStatus", new Resource("v-s:StatusRejected", Type._Uri));
						break;
					case "83bb30a9d8d943f99d0c2d0eab2f478c":
						s.addProperty("v-s:hasStatus", new Resource("v-s:StatusExecution", Type._Uri));
						break;
					default:
						s.addProperty("v-s:hasStatus", new Resource("v-s:StatusAgreement", Type._Uri));
						break;
					}
					
					new_individual.addProperty("mnd-s:hasStatusManualForAFAction", new Resource(s.getUri(), Type._Uri));
					putIndividual(level, s, ba_id);
				}
			}
		}
		
		res.add(new_individual);
		return res;
	}
}
