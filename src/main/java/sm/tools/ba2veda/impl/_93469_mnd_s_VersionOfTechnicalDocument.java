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

public class _93469_mnd_s_VersionOfTechnicalDocument extends Ba2VedaTransform {
	public _93469_mnd_s_VersionOfTechnicalDocument(BaSystem _ba, VedaConnection _veda, Replacer replacer) {
		super(_ba, _veda, replacer, "93469768aeb244ff92d1620f3199af5d", "mnd-s:VersionOfTechnicalDocument");
	}
	
	public void inital_set() {
		fields_map.put("Цех", "mnd-s:technicalDocumentObject");
		fields_map.put("Объект ТОРО", "v-s:hasMaintainedObject");
		fields_map.put("Инв.№", "v-s:inventoryNumber");
		fields_map.put("Название", "v-s:title");
		fields_map.put("Раздел", "v-s:hasDocumentKind");
		fields_map.put("Дата получения", "v-s:registrationDate");
		fields_map.put("attachment_doc", "v-s:attachment");
		fields_map.put("Полное название", "rdfs:label");
		fields_map.put("Вложение", "v-s:attachment");
		fields_map.put("Вложения", "v-s:attachment");
		
		fields_map.put("Комментарий", "?");
		fields_map.put("Обозначение", "?");
		fields_map.put("Разработчик", "?");
		fields_map.put("add_doc", "?");
		fields_map.put("attachment_doc", "?");
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
		
		List<XmlAttribute> atts = doc.getAttributes();
		
		Resources shortLabel = null;
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
				
				if (code.equals("Обозначение"))
					shortLabel = rss;
				else if (code.equals("Разработчик")) {
					Individual dev = new Individual();
					dev.addProperty("v-s:parent", new Resource(new_individual.getUri(), Type._Uri));
					dev.addProperty("v-s:created", new_individual.getResources("v-s:created"));
					dev.addProperty("v-s:creator", new_individual.getResources("v-s:creator"));
					dev.setUri(new_individual.getUri() + "_developer");
					dev.addProperty("rdf:type", new Resource("v-s:Comment", Type._Uri));
					dev.addProperty("v-s:backwardTarget", new Resource(new_individual.getUri(), Type._Uri));
					dev.addProperty("v-s:backwardProperty", new Resource("v-s:hasComment", Type._Uri));
					dev.addProperty("rdfs:label", new Resource("Разработчик: " + att.getTextValue()));
					new_individual.addProperty("v-s:developer", new Resource(dev.getUri(), Type._Uri));
					putIndividual(level, dev, null);
				} else if (code.equals("Комментарий")) {
					Individual comment = new Individual();
					comment.addProperty("v-s:parent", new Resource(new_individual.getUri(), Type._Uri));
					comment.addProperty("v-s:created", new_individual.getResources("v-s:created"));
					comment.addProperty("v-s:creator", new_individual.getResources("v-s:creator"));
					comment.setUri(new_individual.getUri() + "_comment");
					comment.addProperty("rdf:type", new Resource("v-s:Comment", Type._Uri));
					comment.addProperty("v-s:backwardTarget", new Resource(new_individual.getUri(), Type._Uri));
					comment.addProperty("v-s:backwardProperty", new Resource("v-s:hasComment", Type._Uri));
					comment.addProperty("rdfs:label", rss);
					new_individual.addProperty("v-s:comment", new Resource(comment.getUri(), Type._Uri));
					putIndividual(level, comment, null);
				} else if (code.equals("add_doc")) {
//					Создать индивид класса v-s:Link. (ID сгенерировать), 
//					в котором v-s:from - id взятого по ссылке, v-s:to - id текущего документа.
					String from = "d:" + att.getLinkValue();
					String to = new_individual.getUri();
					Individual link = new Individual();
					link.addProperty("v-s:parent", new Resource(new_individual.getUri(), Type._Uri));
					link.setUri(new_individual.getUri());
					link.addProperty("v-s:created", new_individual.getResources("v-s:created"));
					link.addProperty("v-s:creator", new_individual.getResources("v-s:creator"));
					link.addProperty("rdf:type", new Resource("v-s:Link", Type._Uri));
					link.addProperty("v-s:from", new Resource(from, Type._Uri));
					link.addProperty("v-s:to", new Resource(to, Type._Uri));
					new_individual.addProperty("v-s:hasLink", new Resource(link.getUri(), Type._Uri));
					putIndividual(level, link, null);
				} else if (code.equals("attachment_doc")) {
					XmlDocument doc2 = ba.getActualDocument(att.getLinkValue()).getLeft();
					if (doc2 != null) {
						List<XmlAttribute> atts2 = doc2.getAttributes();
						for (XmlAttribute att2 : atts2) {
							if (att2.getCode().equals("attachment")) {
								Resources rss2 = ba_field_to_veda(level, att2, uri, ba_id, 
									doc2, path, parent_ba_doc_id, parent_veda_doc_uri,
									true);
								new_individual.addProperty("v-s:attachment", rss2);
							}
						}
					}
				}
			}
		}
		
		new_individual.addProperty("v-s:backwardProperty", new Resource("mnd-s:hasVersionOfTechnicalDocument", Type._Uri));
		new_individual.addProperty("v-s:canRead", new Resource(true, Type._Bool));
		new_individual.addProperty("v-s:registrationNumberAdd", new Resource("1", Type._String));		
		
		Individual td = new Individual();
		td.setUri(new_individual.getUri() + "_1");
		td.setProperty("rdf:type", new Resource("mnd-s:TechnicalDocument", Type._Uri));
		td.addProperty("v-s:parent", new Resource(new_individual.getUri(), Type._Uri));
		td.addProperty("v-s:created", new_individual.getResources("v-s:created"));
		td.addProperty("v-s:creator", new_individual.getResources("v-s:creator"));
		td.addProperty("mnd-s:technicalDocumentObject", new_individual.getResources("mnd-s:technicalDocumentObject"));
		td.addProperty("v-s:hasMaintainedObject", new_individual.getResources("v-s:hasMaintainedObject"));
		td.addProperty("v-s:title", new_individual.getResources("v-s:title"));
		td.addProperty("v-s:hasDocumentKind", new_individual.getResources("v-s:hasDocumentKind"));
		td.addProperty("v-s:registrationDate", new_individual.getResources("v-s:registrationDate"));
		td.addProperty("v-s:shortLabel", shortLabel);
		td.addProperty("v-s:hasLifecycleStage", new Resource("d:gigldog8r8la9vpe14h5skeuw4", Type._Uri));
		new_individual.addProperty("v-s:backwardTarget", new Resource(td.getUri(), Type._Uri));
		putIndividual(level, td, null);
		
		res.add(new_individual);
		return res;
	}
}
