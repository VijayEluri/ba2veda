package sm.tools.ba2veda.impl_mnd;

import java.util.ArrayList;
import java.util.List;

import ru.mndsc.bigarchive.server.kernel.document.beans.XmlAttribute;
import ru.mndsc.bigarchive.server.kernel.document.beans.XmlDocument;
import sm.tools.ba2veda.Ba2VedaTransform;
import sm.tools.ba2veda.BaSystem;
import sm.tools.ba2veda.Pair;
import sm.tools.ba2veda.Replacer;
import sm.tools.veda_client.Individual;
import sm.tools.veda_client.Resource;
import sm.tools.veda_client.Resources;
import sm.tools.veda_client.Type;
import sm.tools.veda_client.VedaConnection;

public class _f025ba_mnd_s_IncomingLetter extends Ba2VedaTransform {
	public _f025ba_mnd_s_IncomingLetter(BaSystem _ba, VedaConnection _veda, Replacer replacer) {
		super(_ba, _veda, replacer, "f025badc4337433a843b78315f1452a3", "mnd-s:IncomingLetter");
	}
	
	public void inital_set() {
		fields_map.put("content", "v-s:description");
		fields_map.put("count_page", "v-s:sheetsCount");
		fields_map.put("attachment", "v-s:attachment");
		fields_map.put("comment", "rdfs:comment");
		fields_map.put("add_info", "v-s:hasComment");
		
		fields_map.put("number_reg", "?");
		fields_map.put("date_reg", "?");
		fields_map.put("addresse_from", "?");
		fields_map.put("classifier_delivery", "?");
		fields_map.put("addresse_to", "?");
		fields_map.put("add_doc", "?");
		fields_map.put("date_arrival","?");
	}
	
	@Override
	public List<Individual> transform(int level, XmlDocument doc, String ba_id, String parent_veda_doc_uri,
			String parent_ba_doc_id, String path) throws Exception {
		String uri = prepare_uri(ba_id);
		List<Individual> res = new ArrayList<Individual>();

		Individual new_individual = new Individual();
		new_individual.setUri(uri);

		set_basic_fields(level, new_individual, doc);

		new_individual.addProperty("rdf:type", to_class, Type._Uri);
		
		Resources number_reg = null;
		Resources date_reg = null;
		Resources classifier_delivery = null;
		ArrayList<Resources> adresse_to = new ArrayList<Resources>();
		
		
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
				
				if (code.equals("number_reg"))
					number_reg = rss;
				else if (code.equals("date_reg"))
					date_reg = rss;
				else if (code.equals("addresse_from")) {
					Individual ind = new Individual();
					ind.setUri(new_individual.getUri() + "_sender");
					ind.addProperty("rdf:type", new Resource("mnd-s:Correspondent", Type._Uri));
					ind.addProperty("v-s:parent", new Resource(new_individual.getUri(), Type._Uri));
					ind.addProperty("v-s:creator", new_individual.getResources("v-s:creator"));
					ind.addProperty("v-s:created", new_individual.getResources("v-s:created"));
					ind.addProperty("v-s:correspondentOrganization", 
						new Resource("d:org_RU000000000000", Type._Uri));
					ind.addProperty("v-s:correspondentPersonDescription", rss);
					new_individual.addProperty("v-s:sender", new Resource(ind.getUri(), Type._Uri));
					putIndividual(level, ind, ba_id);
				} else if (code.equals("classifier_delivery"))
					classifier_delivery = rss;
				else if (code.equals("addresse_to")) {
					adresse_to.add(rss);
				} else if (code.equals("date_arrival")) {
					Individual ind = new Individual();
					ind.setUri(new_individual.getUri() + "_letter_registration_record_sender");
					ind.addProperty("rdf:type", new Resource("v-s:LetterRegistrationRecordSender", 
						Type._Uri));
					ind.addProperty("v-s:parent", new Resource(new_individual.getUri(), Type._Uri));
					ind.addProperty("v-s:creator", new_individual.getResources("v-s:creator"));
					ind.addProperty("v-s:created", new_individual.getResources("v-s:created"));
					ind.addProperty("v-s:registrationDate", rss);
					new_individual.addProperty("v-s:hasLetterRegistrationRecordSender", new Resource(ind.getUri(), Type._Uri));
					putIndividual(level, ind, ba_id);
				}else if (code.equals("add_doc")) {
					String irf = att.getLinkValue();
					if (irf == null)
						continue;
					Pair<XmlDocument, Long> pair = ba.getActualDocument(irf);
					if (pair == null)
						continue;
					
					XmlDocument irf_doc = pair.getLeft();
					
					String inherit_rights_from = ba.get_first_value_of_field(irf_doc, "inherit_rights_from");
					String link_to = irf;
					
					if (inherit_rights_from != null)
						if (irf_doc.getTypeId().equals("ead1b2fa113c45a8b79d093e8ec14728")
								|| irf_doc.getTypeId().equals("15206d33eafa440c84c02c8d912bce7a")
								|| irf_doc.getTypeId().equals("ec6d76a99d814d0496d5d879a0056428")
								|| irf_doc.getTypeId().equals("a0e50600ebe9450e916469ee698e3faa")
								|| irf_doc.getTypeId().equals("71e3890b3c77441bad288964bf3c3d6a")
								|| irf_doc.getTypeId().equals("cab21bf8b68a4b87ac37a5b41adad8a8")
								|| irf_doc.getTypeId().equals("110fa1f351e24a2bbc187c872b114ea4")
								|| irf_doc.getTypeId().equals("d539b253cb6247a381fb51f4ee34b9d8")
								|| irf_doc.getTypeId().equals("a7b5b15a99704c9481f777fa941506c0")
								|| irf_doc.getTypeId().equals("67588724c4c54b25a2c84906613bd15a"))
							link_to = inherit_rights_from;
					
					Individual link = new Individual();
					link.setUri(ba_id + "_" + link_to);
					link.addProperty("rdf:type", new Resource("v-s:Link", Type._Uri));
					link.addProperty("v-s:to", new Resource(new_individual.getUri(), Type._Uri));
					link.addProperty("v-s:from", new Resource("d:" + link_to, Type._Uri));
					putIndividual(level, link, ba_id);
					new_individual.addProperty("v-s:hasLink", new Resource(link.getUri(), Type._Uri));
				}
			}
		}
		
		
		if (number_reg != null || date_reg != null) {
			Individual lrbr = new Individual();
			lrbr.setUri(new_individual.getUri() + "_letter_registration_record_recipient");
			lrbr.addProperty("rdf:type", new Resource("v-s:LetterRegistrationRecordRecipient", Type._Uri));
			lrbr.addProperty("v-s:parent", new Resource(new_individual.getUri(), Type._Uri));
			lrbr.addProperty("v-s:creator", new_individual.getResources("v-s:creator"));
			lrbr.addProperty("v-s:created", new_individual.getResources("v-s:created"));
			lrbr.addProperty("v-s:registrationNumber", number_reg);
			lrbr.addProperty("v-s:registrationDate", date_reg);
			new_individual.addProperty("v-s:hasLetterRegistrationRecordRecipient", 
				new Resource(lrbr.getUri(), Type._Uri));
			putIndividual(level, lrbr, ba_id);
		}
		
		if (classifier_delivery != null) {
			Individual ind = new Individual();
			ind.setUri(new_individual.getUri() + "_delivery");
			ind.addProperty("rdf:type", new Resource("v-s:Delivery", Type._Uri));
			ind.addProperty("v-s:parent", new Resource(new_individual.getUri(), Type._Uri));
			ind.addProperty("v-s:backwardTarget", new Resource(new_individual.getUri(), Type._Uri));
			ind.addProperty("v-s:creator", new_individual.getResources("v-s:creator"));
			ind.addProperty("v-s:created", new_individual.getResources("v-s:created"));
			ind.addProperty("v-s:deliverBy", classifier_delivery);
			ind.addProperty("v-s:date", date_reg);
			new_individual.addProperty("v-s:hasDelivery",	new Resource(ind.getUri(), Type._Uri));
			putIndividual(level, ind, ba_id);
		}
		
		if (adresse_to.size() > 0) {
			Individual ind = new Individual();
			ind.setUri(new_individual.getUri() + "_recipient");
			ind.addProperty("rdf:type", new Resource("mnd-s:Correspondent", Type._Uri));
			ind.addProperty("v-s:parent", new Resource(new_individual.getUri(), Type._Uri));
			ind.addProperty("v-s:creator", new_individual.getResources("v-s:creator"));
			ind.addProperty("v-s:created", new_individual.getResources("v-s:created"));
			ind.addProperty("v-s:correspondentOrganization", 
				new Resource("d:org_RU1121003135", Type._Uri));
			
			for (int i = 0; i < adresse_to.size(); i++)
				ind.addProperty("v-s:correspondentPerson", adresse_to.get(i));
			new_individual.addProperty("v-s:recipient", new Resource(ind.getUri(), Type._Uri));
			putIndividual(level, ind, ba_id);
		}
		
		res.add(new_individual);
		return res;
	}
}