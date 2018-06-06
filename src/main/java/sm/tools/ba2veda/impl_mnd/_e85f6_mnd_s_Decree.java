package sm.tools.ba2veda.impl_mnd;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

public class _e85f6_mnd_s_Decree extends Ba2VedaTransform {
	
	public _e85f6_mnd_s_Decree(BaSystem _ba, VedaConnection _veda, Replacer replacer) {
		super(_ba, _veda, replacer, "e85f65d307954046826a68feffab9526", "mnd-s:Decree");
	}
	
	public void inital_set() {
		fields_map.put("Заголовок", "v-s:title");
		fields_map.put("Инициатор", "v-s:initiator");
		fields_map.put("Подписывающий", "v-s:signedBy");
		
		fields_map.put("Регистрационный номер 1", "?");
		fields_map.put("Регистрационный номер", "?");
		fields_map.put("Дата регистрации", "?");
		
		fields_map.put("Ключевые слова", "?");
		fields_map.put("Содержание", "v-s:description");
		fields_map.put("Вложения", "?");
		fields_map.put("file", "?");
		fields_map.put("Связанные документы", "?");
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
		
		new_individual.addProperty("mnd-s:hasDecreeKind", new Resource("d:e5753b58168843e28ad73855c07b8cff", Type._Uri));
		
		Individual drtr = null;
		Individual comment = null;
		ArrayList<Object> comment_parts = new ArrayList<Object>();
		
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
				
				if (code.equals("Регистрационный номер 1") ||
						code.equals("Регистрационный номер")) {
					String data = rss.resources.get(0).getData();
					if (data.equals(""))
						continue;
					if (drtr == null)
						drtr = new Individual();
					
					drtr.addProperty("v-s:registrationNumber", rss);
				} else if (code.equals("Дата регистрации")) {
					String data = rss.resources.get(0).getData();
					if (data.equals(""))
						continue;
					if (drtr == null)
						drtr = new Individual();
					
					drtr.addProperty("v-s:registrationDate", rss);
				} else if (code.equals("Ключевые слова")) {
					if (comment_parts.size() > 0) {
						comment_parts.add("\n");
					}
					
					String elem = code + ": " + rss.resources.get(0).getData();
					Resources elem_resource = new Resources();
					elem_resource.add(new Resource(elem, Type._String));
					comment_parts.add(elem_resource);									
				} else if (code.equals("file") || code.equals("Вложения")) {
					if (comment == null)
						comment = new Individual();
					comment.addProperty("v-s:attachment", rss);
				} else if (code.equals("Связанные документы")) {
					if (att.getLinkValue() == null) 
						continue;
					Individual individual = veda.getIndividual("d:" + att.getLinkValue());
					if (individual == null)
						continue;
					Resources resources = individual.getResources("v-s:backwardTarget");
					if (resources == null)
						continue;
					new_individual.addProperty("v-s:customer", resources);
				} else if (code.equals("inherit_rights_from")) {
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
					
					
					new_individual.addProperty("v-s:linkedObject", new Resource(link_to, Type._Uri));
				}
			}
		}
		
		
		if (drtr != null) {
			drtr.setUri(new_individual.getUri() + "_registration_record");
			drtr.addProperty("v-s:canRead", "true", Type._Bool);
			drtr.addProperty("v-s:author", new Resource("d:rimert_DocRegistrator_SLPK", Type._Uri));
			drtr.addProperty("v-s:created", new_individual.getResources("v-s:created"));
			drtr.addProperty("rdf:type", new Resource("mnd-s:DecreeRegistrationRecord", Type._Uri));
			new_individual.addProperty("mnd-s:hasDecreeRegistrationRecord", new Resource(drtr.getUri(), Type._Uri));
			drtr.addProperty("mnd-s:hasDecreeKind", new Resource("d:e5753b58168843e28ad73855c07b8cff", Type._Uri));
			drtr.addProperty("v-s:backwardProperty", "mnd-s:hasDecreeRegistrationRecord",
				Type._Uri);
			drtr.addProperty("v-s:backwardReplace", "mnd-s:hasDecreeKind", Type._Uri);
			drtr.addProperty("v-s:backwardTarget", new_individual.getUri(), Type._Uri);
			putIndividual(level, drtr, ba_id);
		}
		
		String[] langs_out1 = { "EN", "RU" };
		String[] langs_out2 = { "NONE" };
		
		Resources rss = rs_assemble(comment_parts.toArray(), langs_out1);
		if (rss.resources.size() == 0)
			rss = rs_assemble(comment_parts.toArray(), langs_out2);
			
		if (rss.resources.size() > 0 || comment != null) {
			if (comment == null)
				comment = new Individual();
			comment.setUri(new_individual.getUri() + "_comment");
			comment.addProperty("rdf:type", new Resource("v-s:Comment", Type._Uri));
			comment.addProperty("v-s:creator", new_individual.getResources("v-s:creator"));
			comment.addProperty("v-s:created", new_individual.getResources("v-s:created"));
			comment.addProperty("rdfs:label", rss);
			new_individual.addProperty("v-s:hasComment", new Resource(comment.getUri(), Type._Uri));
			putIndividual(level, comment, ba_id);
		}
		
		res.add(new_individual);
		return res;
	}
}
