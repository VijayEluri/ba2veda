package sm.tools.ba2veda.impl_mnd;

import java.util.ArrayList;
import java.util.List;

import ru.mndsc.bigarchive.server.kernel.document.beans.XmlAttribute;
import ru.mndsc.bigarchive.server.kernel.document.beans.XmlDocument;
import ru.mndsc.objects.organization.Department;
import sm.tools.ba2veda.Ba2VedaTransform;
import sm.tools.ba2veda.BaSystem;
import sm.tools.ba2veda.Replacer;
import sm.tools.veda_client.Individual;
import sm.tools.veda_client.Resource;
import sm.tools.veda_client.Resources;
import sm.tools.veda_client.Type;
import sm.tools.veda_client.VedaConnection;

public class _b163ab_mnd_s_TechnicalDocument extends Ba2VedaTransform
{
	public _b163ab_mnd_s_TechnicalDocument(BaSystem _ba, VedaConnection _veda, Replacer replacer)
	{
		super(_ba, _veda, replacer, "b163abf24965406b8bbadc7cf6d7a435", "mnd-s:TechnicalDocument");
	}

	public void inital_set()
	{
		fields_map.put("Родительский комплект", "v-s:backwardTarget");
		fields_map.put("Цех", "mnd-s:technicalDocumentObject");
		fields_map.put("Объект ТОРО", "v-s:hasMaintainedObject");
		fields_map.put("Обозначение", "v-s:shortLabel");
		fields_map.put("Название", "v-s:title");
		fields_map.put("Тип работ", "v-s:hasBudgetCategory");
		fields_map.put("Дата получения", "v-s:registrationDate");
		fields_map.put("Комментарий", "v-s:hasComment");
		fields_map.put("Полное название", "rdfs:label");
		fields_map.put("Конструкторская заявка", "mnd-s:hasEngineeringRequest");
		
		
		fields_map.put("Раздел", "?");
		fields_map.put("Разработчик", "?");
		fields_map.put("attachment_doc", "?");
		fields_map.put("add_doc", "?");
	}

	@Override
	public List<Individual> transform(int level, XmlDocument doc, String ba_id, String parent_veda_doc_uri,
		String parent_ba_doc_id, String path) throws Exception {
		
		String uri = prepare_uri(ba_id);
		List<Individual> res = new ArrayList<Individual>();

		Individual new_individual = new Individual();
		new_individual.setUri(uri);

		set_basic_fields(level, new_individual, doc);
		
		new_individual.addProperty("v-s:backwardProperty", new Resource("mnd-s:hasTechnicalDocument",
				Type._Uri));
		new_individual.addProperty("v-s:canRead", new Resource(true, Type._Bool));
		new_individual.addProperty("v-s:hasLifecycleStage", 
			new Resource("d:yzukiatavri0xticlw3xax2qeg", Type._Uri));
		new_individual.addProperty("v-s:valid", "true", Type._Bool);
		new_individual.addProperty("mnd-s:appliesTo", "d:org_RU1121003135", Type._Uri);
		new_individual.addProperty("mnd-s:isAccessLimited", "false", Type._Uri);
		new_individual.addProperty("v-s:owner", "d:mondi_department_50003626", Type._Uri);
		new_individual.addProperty("v-s:hasSector", "d:4775f24d50774505bc8279314557b19a", Type._Uri);
		new_individual.addProperty("v-s:hasDocumentKind", 
				new Resource("d:uqocbblmycyot69lvvv44m9c28", Type._Uri));
		int linksCount = 0;
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
				
				if (code.equals("Раздел")) {
					String recordId = att.getRecordIdValue();
					if (recordId != null) {
						XmlDocument recordDoc = ba.getActualDocument(recordId).getLeft();
						String recordId2 = null;
						if (recordDoc != null)
							recordId2 = ba.get_first_value_of_field(recordDoc, "Раздел");
						if (recordId2 == null)
							recordId2 = "";
						if (recordId.equals("69ca82170be24d41b32fc9033a2574f5") ||
							recordId2.equals("69ca82170be24d41b32fc9033a2574f5")) {
							new_individual.addProperty("mnd-s:hasMarkOfTechnicalDocuments", rss);
					//		new_individual.addProperty("v-s:hasDocumentKind", 
						//		new Resource("d:afc1a827f2ac47a9bd19b6db910dfc13", Type._Uri));
						} else if (recordId2.equals("4f391bc4b9434d619ea95396cd0faba7")) {
							new_individual.addProperty("mnd-s:hasSectionOfProjectDocumentation", rss);
//							new_individual.addProperty("v-s:hasDocumentKind", 
//								new Resource("d:kqyyu62f90hy89wh188664bxwl", Type._Uri));
						} else if (recordId.equals("7d67bd472db4481db0f5511f37107cae") || 
							recordId.equals("db6c04d678c849859295f65efce1de76")) {
							new_individual.addProperty("mnd-s:hasMarkOfTechnicalDocuments", 
								new Resource("d:6ba70b2261d4443e98d91452565d3b98", Type._Uri));
//							new_individual.addProperty("v-s:hasDocumentKind", 
//								new Resource("d:mqzlxqrejhhbod4ra42nq8cf", Type._Uri));
						} else if (recordId.equals("6ba70b2261d4443e98d91452565d3b98")) {
//							new_individual.addProperty("v-s:hasDocumentKind", 
//								new Resource("d:afc1a827f2ac47a9bd19b6db910dfc13", Type._Uri));
							new_individual.addProperty("mnd-s:hasMarkOfTechnicalDocuments", 
								new Resource("d:6ba70b2261d4443e98d91452565d3b98", Type._Uri));
						} else if (recordId.equals("584e7ef299b14bec89c516b311472ba5")) {
//							new_individual.addProperty("v-s:hasDocumentKind", 
//								new Resource("d:99d3887ae22d439c9fe77a10ff5a4b0d", Type._Uri));
							new_individual.addProperty("mnd-s:hasMarkOfTechnicalDocuments", 
								new Resource("d:6ba70b2261d4443e98d91452565d3b98", Type._Uri));
						} else if (recordId.equals("41b925655c8a44a8b34ab3b1894bebd0")) {
//							new_individual.addProperty("v-s:hasDocumentKind", 
//								new Resource("d:41b925655c8a44a8b34ab3b1894bebd0", Type._Uri));
							new_individual.addProperty("mnd-s:hasMarkOfTechnicalDocuments", 
								new Resource("d:zn8jlec6ma6x28fsgp6lyw49zo", Type._Uri));
						}
					}
				} else if (code.equals("Разработчик")) {
					String org = att.getOrganizationValue();
					Department dp = ba.getPacahon().getDepartmentByUid(org, "RU", "");
					
					Individual dev = new Individual();
					dev.setUri(new_individual.getUri() + "_correspondent");
					dev.addProperty("rdf:type", new Resource("v-s:Correspondent", Type._Uri));
					dev.addProperty("v-s:creator", new_individual.getResources("v-s:creator"));
					dev.addProperty("v-s:created", new_individual.getResources("v-s:created"));
					
					boolean is_mondi = true;
					boolean is_departnment = false;
				
					if (dp != null) {
						String iid = dp.getInternalId();
						if (iid.length() == 8 && iid.charAt(0) == '5')
							is_mondi = true;
						else
							is_mondi = false;

						is_departnment = true;
					}

					//Resources rss1 = ba_field_to_veda(level, xat1, null, org, d1, path, parent_ba_doc_id, parent_veda_doc_uri, true);

	
					if (is_mondi && is_departnment == false) {
						dev.addProperty("v-s:correspondentPerson", rss);
						dev.addProperty("v-s:correspondentOrganization", new Resource("d:org_RU1121003135", Type._Uri));
						putIndividual(level, dev, ba_id);
						new_individual.addProperty("v-s:developer", new Resource(dev.getUri(), Type._Uri));
					}
					if (is_mondi && is_departnment == true) {
						dev.addProperty("v-s:correspondentDepartment", rss);
						dev.addProperty("v-s:correspondentOrganization", new Resource("d:org_RU1121003135", Type._Uri));
						putIndividual(level, dev, ba_id);
						new_individual.addProperty("v-s:developer", new Resource(dev.getUri(), Type._Uri));
					}

					if (is_mondi == false && is_departnment == true) {
						dev.addProperty("v-s:correspondentOrganization", rss);
						putIndividual(level, dev, ba_id);
						new_individual.addProperty("v-s:developer", new Resource(dev.getUri(), Type._Uri));
					}
					
					
				} else if (code.equals("attachment_doc")) {
					String link = att.getLinkValue();
					if (link != null){
						XmlDocument linkDoc = ba.getActualDocument(link).getLeft();
						if (linkDoc != null) {
							List<XmlAttribute> linkAtts = linkDoc.getAttributes();
							for (XmlAttribute linkAtt : linkAtts) {
								String linkCode = linkAtt.getCode();
								if (linkCode.equals("attachment")) {
									Resources rssLink = ba_field_to_veda(level, linkAtt, null, null, linkDoc, 
										path, null, null,
										true);
									new_individual.addProperty("v-s:attachment", rssLink);
								}
							}
						}
					}
				} else if (code.equals("add_doc")) {
					String link = att.getLinkValue();
					if (link != null) {
						Individual i = st_veda.getIndividual("d:" + link);
						if (i != null) {
							if (i.getResources("rdf:type") != null) {
								if (i.getResources("rdf:type").equals("mnd-s:EngineeringRequest"))
									new_individual.addProperty("mnd-s:hasEngineeringRequest", new Resource("d:" + link, Type._Uri));
								else {
									linksCount++;
									Individual linkIndiv = new Individual();
									linkIndiv.setUri(String.format("%s_link%d", new_individual.getUri(), linksCount));
									linkIndiv.addProperty("rdf:type", new Resource("v-s:Link", Type._Uri));
									linkIndiv.addProperty("v-s:creator", new_individual.getResources("v-s:creator"));
									linkIndiv.addProperty("v-s:created", new_individual.getResources("v-s:created"));
									linkIndiv.addProperty("v-s:from", new Resource(new_individual.getUri(), Type._Uri));
									linkIndiv.addProperty("v-s:to", new Resource(i.getUri(), Type._Uri));
									new_individual.addProperty("v-s:hasLink", new Resource(linkIndiv.getUri(), Type._Uri));
									putIndividual(level, linkIndiv, ba_id);
								}
							}
						}
					} 
				}
			}
		}
		
		new_individual.addProperty("rdf:type", to_class, Type._Uri);
		res.add(new_individual);
		return res;
	}
}
