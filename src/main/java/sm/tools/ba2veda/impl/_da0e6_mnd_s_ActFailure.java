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

public class _da0e6_mnd_s_ActFailure extends Ba2VedaTransform {
	public _da0e6_mnd_s_ActFailure(BaSystem _ba, VedaConnection _veda, Replacer replacer)  {
		super(_ba, _veda, replacer, "da0e63b08cdc4aae81070b59be1d135c", "mnd-s:ActFailure");
	}
	
	public void inital_set() {
		fields_map.put("number", "v-s:registrationNumber");
		fields_map.put("number_tips", "mnd-s:idTips");
		fields_map.put("name", "v-s:theme");
		fields_map.put("department_kind", "v-s:hasSector");
		fields_map.put("department", "v-s:responsibleDepartment");
		fields_map.put("manager", "v-s:actFailureManager");
		fields_map.put("date_from", "v-s:date");
		fields_map.put("status_manual", "v-s:hasStatus");
		fields_map.put("initiation_kind", "mnd-s:hasInitiationKind");
		fields_map.put("add_info", "v-s:hasComment");
		fields_map.put("name_full", "rdfs:label");
		fields_map.put("content", "v-s:observationDescription");
		fields_map.put("content2", "v-s:observationCircumstances");
		fields_map.put("content3", "v-s:effectsDescription");
		fields_map.put("content4", "v-s:observationInitiator");
		fields_map.put("toro_message", "mnd-s:toro_message");
		fields_map.put("order_number", "mnd-s:order_number");
		fields_map.put("date_fact", "v-s:registrationDate");
		fields_map.put("object_toro", "v-s:hasMaintainedObject");
		fields_map.put("equipment", "mnd-s:hasEquipmentForFailureAct");
		fields_map.put("strategy", "mnd-s:toStrategy");
		fields_map.put("last_repair", "mnd-s:lastRepairInfo");
		fields_map.put("information", "mnd-s:defectsMessage");
		fields_map.put("comment", "rdfs:comment");
		fields_map.put("disposal_time", "v-s:eliminatingTime");
		fields_map.put("downtime", "v-s:downtime");
		fields_map.put("count", "mnd-s:stoppedMachinesCount");
		fields_map.put("spare_part_costs", "mnd-s:sparePartsCosts");
		fields_map.put("services_costs", "mnd-s:servicesCosts");
		fields_map.put("unreleased_products_sum", "mnd-s:lossesUnreleasedProducts");
		fields_map.put("action", "v-s:hasAction");
		fields_map.put("title", "v-s:title");
		fields_map.put("reason", "v-s:reason");
		fields_map.put("opinion", "v-s:specialOpinion");
		
		fields_map.put("chief", "?");
		fields_map.put("member", "?");
	}
	@Override
	public List<Individual> transform(XmlDocument doc, String ba_id, String parent_veda_doc_uri,
		String parent_ba_doc_id, String path) throws Exception {
		
		String uri = prepare_uri(ba_id);
		List<Individual> res = new ArrayList<Individual>();

		Individual new_individual = new Individual();
		new_individual.setUri(uri);

		set_basic_fields(new_individual, doc);
		
		new_individual.addProperty("rdf:type", new Resource(to_class, Type._Uri));
		
		int member = 0;

		List<XmlAttribute> atts = doc.getAttributes();
		for (XmlAttribute att : atts) {
			String code = att.getCode();

			String predicate = fields_map.get(code);
			System.out.println("CODE: " + code);
			
			if (predicate != null) {
				Resources rss = ba_field_to_veda(att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri,
						true);
					
				if (predicate.equals("?") == false)
					new_individual.addProperty(predicate, rss);
				
				if (rss == null)
					continue;
				
				if (rss.resources.size() < 1)
					continue;
				
				if (code.equals("chief")) {
					Individual m = new Individual();
					
					m.setUri(String.format("%s_member_%d", new_individual.getUri(), + member));
					member++;
					m.addProperty("v-s:parent", new Resource(new_individual.getUri(), Type._Uri));
					m.addProperty("rdf:type", new Resource("v-s:MemberOfWorkGroup", Type._Uri));
					m.addProperty("v-s:created", new_individual.getResources("v-s:created"));
					m.addProperty("v-s:creator", new_individual.getResources("v-s:creator"));
					m.addProperty("v-s:member", rss);
					m.addProperty("v-s:occupation", new Resource("d:aaa2f4046-eb3f-3c3e-1b50-026cf6c3cfdf", Type._Uri));
					new_individual.addProperty("v-s:hasMemberOfWorkGroup", new Resource(m.getUri(), Type._Uri));
					putIndividual(m, ba_id, true);					
				} else if (code.equals("member")) {
					Individual m = new Individual();
					
					m.setUri(String.format("%s_member_%d", new_individual.getUri(), + member));
					member++;
					m.addProperty("v-s:parent", new Resource(new_individual.getUri(), Type._Uri));
					m.addProperty("rdf:type", new Resource("v-s:MemberOfWorkGroup", Type._Uri));
					m.addProperty("v-s:created", new_individual.getResources("v-s:created"));
					m.addProperty("v-s:creator", new_individual.getResources("v-s:creator"));
					m.addProperty("v-s:member", rss);
					new_individual.addProperty("v-s:hasMemberOfWorkGroup", new Resource(m.getUri(), Type._Uri));
					putIndividual(m, ba_id, true);
				}
			}
		}
		
		String[] langs_out1 = { "EN", "RU" };
		String[] langs_out2 = { "NONE" };
		
		ArrayList<Object> label_parts = new ArrayList<Object>();
		if (new_individual.getResources("v-s:registrationNumber") != null) {
			label_parts.add(new_individual.getResources("v-s:registrationNumber"));
			label_parts.add(" - ");
		}
		
		if (new_individual.getResources("v-s:theme") != null)
			label_parts.add(new_individual.getResources("v-s:theme"));

		Resources rss = rs_assemble(label_parts.toArray(), langs_out1);
		if (rss.resources.size() == 0)
			rss = rs_assemble(label_parts.toArray(), langs_out2);
			
		new_individual.addProperty("rdfs:label", rss);
		res.add(new_individual);
		
	/*	if (chief != null) {
			chief.setUri(new_individual.getUri() + "_chief");
			
			chief.addProperty("v-s:occupation", new Resource("d:aaa2f4046-eb3f-3c3e-1b50-026cf6c3cfdf", Type._Uri));
			chief.addProperty("v-s:parent", new Resource("ur"))
			
			new_individual.addProperty("rdf:type", new Resource("v-s:MemberOfWorkGroup", Type._Uri));
		}*/
		return res;
	}
}
