package sm.tools.ba2veda.impl;

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

public class _b243a_mnd_s_DismantlingProject extends Ba2VedaTransform {
	public  _b243a_mnd_s_DismantlingProject(BaSystem _ba, VedaConnection _veda, Replacer replacer) {
		super(_ba, _veda, replacer, "b243a9f31e01462ba5695d6506f3ea72", "mnd-s:DismantlingProject");
	}
	
	public void inital_set() {
		fields_map.put("Номер", "v-s:registrationNumber");
		fields_map.put("Цех", "v-s:stakeholder");
		fields_map.put("Наименование", "v-s:title");
		fields_map.put("Руководитель проекта", "v-s:projectManager");
		fields_map.put("Год CAPEX", "v-s:year");
		fields_map.put("Вложения", "mnd-s:attachmentTz");
		fields_map.put("name", "rdfs:label");
		
		fields_map.put("Комментарий", "?");
		
		fields_map.put("№ СПП-элемента", "?");
		fields_map.put("Дата открытия СПП-элемента", "?");
		
		fields_map.put("Статус ИП", "?");
	}
	
	@Override
	public List<Individual> transform(XmlDocument doc, String ba_id, String parent_veda_doc_uri,
			String parent_ba_doc_id, String path) throws Exception {
		String uri = prepare_uri(ba_id);
		List<Individual> res = new ArrayList<Individual>();

		Individual new_individual = new Individual();
		new_individual.setUri(uri);

		set_basic_fields(new_individual, doc);

		new_individual.addProperty("rdf:type", to_class, Type._Uri);
		
		Resources n_spp = null;
		Resources date_spp = null;
		
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
				
				if (code.equals("Комментарий")) {
					Individual comment = new Individual();
					comment.setUri(new_individual.getUri() + "_comment");
					comment.addProperty("rdf:type", new Resource("v-s:Comment", Type._Uri));
					comment.addProperty("v-s:creator", new_individual.getResources("v-s:creator"));
					comment.addProperty("v-s:created", new_individual.getResources("v-s:created"));
					comment.addProperty("rdfs:comment", rss);
					putIndividual(comment, ba_id, true);
					new_individual.addProperty("v-s:hasComment", new Resource(comment.getUri(), Type._Uri));
				} else if (code.equals("№ СПП-элемента"))
					n_spp = rss;
				else if (code.equals("Дата открытия СПП-элемента"))
					date_spp = rss;
				else if (code.equals("Статус ИП")) {
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
					link.setUri(new_individual.getUri() + "_link");
					link.addProperty("rdf:type", new Resource("v-s:Link", Type._Uri));
					link.addProperty("v-s:from", new Resource(new_individual.getUri(), Type._Uri));
					link.addProperty("v-s:to", new Resource("d:" + link_to, Type._Uri));
					putIndividual(link, ba_id, true);
					new_individual.addProperty("v-s:hasLink", new Resource(link.getUri(), Type._Uri));
				}
			}
		}
		
		
		if (date_spp != null ||  n_spp != null) {
			Individual spp_elem = new Individual();
			spp_elem.setUri(new_individual.getUri() + "spp_element");
			spp_elem.addProperty("rdf:type", new Resource("mnd-s:SppElement", Type._Uri));
			spp_elem.addProperty("v-s:creator", new_individual.getResources("v-s:creator"));
			spp_elem.addProperty("v-s:created", new_individual.getResources("v-s:created"));
			spp_elem.addProperty("v-s:parent", new Resource(new_individual.getUri(), Type._Uri));
			spp_elem.addProperty("mnd-s:sppNumber", n_spp);
			spp_elem.addProperty("mnd-s:sppDate", date_spp);
			putIndividual(spp_elem, ba_id, true);
			new_individual.addProperty("mnd-s:hasSppElement", new Resource(spp_elem.getUri(), Type._Uri));
		}

		res.add(new_individual);
		return res;
	}
}
