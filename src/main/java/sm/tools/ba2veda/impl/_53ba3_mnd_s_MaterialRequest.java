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

public class _53ba3_mnd_s_MaterialRequest extends Ba2VedaTransform {
	public _53ba3_mnd_s_MaterialRequest(BaSystem _ba, VedaConnection _veda, Replacer replacer)  {
		super(_ba, _veda, replacer, "53ba31a7606349059e4a8cc08128f358", "mnd-s:MaterialRequest");
	}
	
	public void inital_set() {
		fields_map.put("classifier_doc", "mnd-s:hasMaterialRequestKind");
		fields_map.put("reason", "v-s:reason");
		fields_map.put("department", "v-s:responsibleDepartment");
		fields_map.put("classifier_product", "mnd-s:hasMaterialGroup");
		fields_map.put("attachment", "v-s:attachment");
		fields_map.put("add_info", "v-s:hasComment");
		fields_map.put("compound_title", "rdfs:label");
		
		fields_map.put("inherit_rights_from", "?");
		
		
		fields_map.put("title", "?");
		fields_map.put("number_reg", "?");
		fields_map.put("unit_of_measure", "?");
		fields_map.put("cost", "?");
		fields_map.put("duration", "?");
		fields_map.put("object_toro", "?");
		fields_map.put("count", "?");
		
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
		
		new_individual.addProperty("mnd-s:hasDecreeKind", new Resource("d:9664874293574b79af624f01e3c091cd", Type._Uri));
		Individual eomr = null;
		
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
				
				
				if (code.equals("inherit_rights_from")) {
					String irf = att.getLinkValue();
					XmlDocument irf_doc = ba.getActualDocument(irf).getLeft();
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
				} else if (code.equals("object_toro")) {
					String ddsid =  att.getRecordIdValue();
					Pair<XmlDocument, Long> pair = ba.getActualDocument(ddsid);
					if (pair == null)
						continue;
					
					XmlDocument ddsid_doc = pair.getLeft();
					
//					String r3_id = ba.get_first_value_of_field(ddsid_doc, "R3_ID");
					List<XmlAttribute> ddsid_atts = ddsid_doc.getAttributes();
					for (XmlAttribute ddsid_att : ddsid_atts) {
						if (ddsid_att.getCode().equals("1b073c10-91fb-451e-b636-8c5bfe77c598_2")) {
							if (eomr == null)
								eomr = new Individual();
							eomr.addProperty("mnd-s:hasMaintainedObject", new Resource("d:" + ddsid_att.getTextValue(), Type._Uri));
							break;
						}
					} 
				} else if (code.equals("title")) {
					if (eomr == null)
						eomr = new Individual();
					eomr.addProperty("v-s:title", rss);
				} else if (code.equals("number_reg")) {
					if (eomr == null)
						eomr = new Individual();
					eomr.addProperty("mnd-s:materialGroupNumber", rss);
				} else if (code.equals("unit_of_measure")) {
					if (eomr == null)
						eomr = new Individual();
					eomr.addProperty("v-s:hasUnitOfMeasure", rss);
				} else if (code.equals("cost")) {
					if (eomr == null)
						eomr = new Individual();
					Individual price = new Individual();
					price.setUri(new_individual.getUri() + "_price");
					price.addProperty("rdf:type", new Resource("v-s:Price", Type._Uri));
					price.addProperty("v-s:parent", new Resource(new_individual.getUri(), Type._Uri));
					price.addProperty("v-s:creator", new_individual.getResources("v-s:creator"));
					price.addProperty("v-s:created", new_individual.getResources("v-s:created"));
					price.addProperty("v-s:sum", rss);
					eomr.addProperty("v-s:hasPrice", new Resource(price.getUri(), Type._Uri));
					putIndividual(price, ba_id, true);
				} else if (code.equals("duration")) {
					if (eomr == null)
						eomr = new Individual();
					eomr.addProperty("v-s:duration", rss);
				}
			}
		}
		
		if (eomr != null) {
			eomr.setUri(new_individual.getUri() + "_eomr");
			eomr.addProperty("rdf:type", 
				new Resource("mnd-s:ElementOfMaterialRequest", Type._Uri));
			eomr.addProperty("v-s:created", new_individual.getResources("v-s:created"));
			eomr.addProperty("v-s:creator", new_individual.getResources("v-s:creator"));
			eomr.addProperty("v-s:parent", new Resource(new_individual.getUri(), 
				Type._Uri));
			if (eomr.getResources("v-s:title") != null) {
				String[] langs_out1 = { "EN", "RU" };
				String[] langs_out2 = { "NONE" };
				
				Object[] parts = {eomr.getResources("v-s:title"), " - ", 
					eomr.getResources("v-s:created")};
				Resources rss = rs_assemble(parts, langs_out1);
				if (rss.resources.size() == 0)
					rss = rs_assemble(parts, langs_out2);
			}
			new_individual.addProperty("mnd-s:hasElementOfMaterialRequest",
				new Resource(eomr.getUri(), Type._Uri));
			putIndividual(eomr, ba_id, true);
		}
		
		res.add(new_individual);
		return res;
	}
}