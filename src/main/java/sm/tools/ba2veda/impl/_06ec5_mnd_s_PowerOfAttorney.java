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

public class _06ec5_mnd_s_PowerOfAttorney extends Ba2VedaTransform {
	public _06ec5_mnd_s_PowerOfAttorney(BaSystem _ba, VedaConnection _veda, Replacer replacer)  {
		super(_ba, _veda, replacer, "06ec5fdaa4c54e17bdebd34d96913dbc", "mnd-s:PowerOfAttorney");
	}
	
	public void inital_set() {
		fields_map.put("number_reg", "v-s:registrationNumber");
		fields_map.put("date_reg", "v-s:registrationDate");
		fields_map.put("date_from", "v-s:dateFrom");
		fields_map.put("date_to", "v-s:dateTo");
		fields_map.put("attachment", "v-s:attachment");
		fields_map.put("add_info", "v-s:hasComment");
		fields_map.put("comment", "v-s:reason");
		fields_map.put("compound_title", "rdfs:label");
		
		fields_map.put("replaceable_stuff_from", "?");
		fields_map.put("contractor_from", "?");
		fields_map.put("replaceable_stuff_to", "?");
		fields_map.put("contractor", "?");
		fields_map.put("responsible_add", "?");
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
		
		Individual grantor = null;
		Individual grantee = null;

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
				
				if (code.equals("replaceable_stuff_from")) {
					if (grantor == null) 
						grantor = new Individual();
					
					grantor.addProperty("v-s:correspondentPerson", rss);
				} else if (code.equals("contractor_from")) {
					if (grantor == null) 
						grantor = new Individual();
					
					grantor.addProperty("v-s:correspondentOrganization", rss);
				} else if (code.equals("replaceable_stuff_to")) {
					if (grantee == null) 
						grantee = new Individual();
					grantee.addProperty("v-s:correspondentPerson", rss);
				} else if (code.equals("contractor")) {
					if (grantee == null) 
						grantee = new Individual();
					grantee.addProperty("v-s:correspondentOrganization", rss);
				} else if (code.equals("responsible_add")) {
					if (grantee == null) 
						grantee = new Individual();
					grantee.addProperty("v-s:correspondentPersonDescription", rss);
				}
			}
		}
		
		if (grantor != null) {
			grantor.setUri(new_individual.getUri() + "_grantor");
			grantor.addProperty("rdf:type", new Resource("v-s:Correspondent", Type._Uri));
			grantor.addProperty("v-s:creator", new_individual.getResources("v-s:creator"));
			grantor.addProperty("v-s:created", new_individual.getResources("v-s:created"));
			grantor.addProperty("v-s:parent", new Resource(new_individual.getUri(), Type._Uri));
			new_individual.addProperty("v-s:grantor", new Resource(grantor.getUri(), Type._Uri));
			putIndividual(grantor, ba_id, true);
		}
		
		if (grantee != null) {
			grantee.setUri(new_individual.getUri() + "_grantee");
			grantee.addProperty("rdf:type", new Resource("v-s:Correspondent", Type._Uri));
			grantee.addProperty("v-s:creator", new_individual.getResources("v-s:creator"));
			grantee.addProperty("v-s:created", new_individual.getResources("v-s:created"));
			grantee.addProperty("v-s:parent", new Resource(new_individual.getUri(), Type._Uri));
			new_individual.addProperty("v-s:grantee", new Resource(grantee.getUri(), Type._Uri));
			putIndividual(grantee, ba_id, true);
		}
		
		res.add(new_individual);
		return res;
	}
}
