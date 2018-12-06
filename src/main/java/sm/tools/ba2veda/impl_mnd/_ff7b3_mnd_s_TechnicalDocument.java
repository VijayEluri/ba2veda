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

public class _ff7b3_mnd_s_TechnicalDocument extends Ba2VedaTransform
{
	public _ff7b3_mnd_s_TechnicalDocument(BaSystem _ba, VedaConnection _veda, Replacer replacer)
	{
		super(_ba, _veda, replacer, "ff7b326d38b74b639c23ad198b8bd8b1", "mnd-s:TechnicalDocument");
	}

	public void inital_set()
	{
		fields_map.put("Цех", "mnd-s:technicalDocumentObject");
		fields_map.put("Обозначение", "v-s:shortLabel");
		fields_map.put("Раздел", "v-s:hasClassifierMarkOfWorkingDrawingsSet");
		fields_map.put("Проект", "?");
		fields_map.put("Тип работ", "v-s:hasBudgetCategory");
		fields_map.put("Дата получения", "v-s:registrationDate");
		fields_map.put("Разработчик", "?");
		fields_map.put("Сопровождающие документы", "?");
		fields_map.put("Конструкторская заявка", "mnd-s:hasEngineeringRequest");
		fields_map.put("Связанные документы", "?");
		// // //

		fields_map.put("Родительский комплект", "v-s:backwardTarget");
		fields_map.put("Инв.№", "?");
		fields_map.put("Название", "?");
		fields_map.put("Комментарий", "?");

		//fields_map.put("Комментарий", "v-s:hasComment");
		fields_map.put("Полное название", "rdfs:label");

		fields_map.put("Раздел", "?");
	}

	@Override
	public List<Individual> transform(int level, XmlDocument doc, String ba_id, String parent_veda_doc_uri, String parent_ba_doc_id, String path)
			throws Exception
	{

		String uri = prepare_uri(ba_id);
		List<Individual> res = new ArrayList<Individual>();

		Individual new_individual = new Individual();
		new_individual.setUri(uri);

		set_basic_fields(level, new_individual, doc);

		new_individual.addProperty("v-s:backwardProperty", new Resource("mnd-s:hasTechnicalDocument", Type._Uri));
		new_individual.addProperty("v-s:canRead", new Resource(true, Type._Bool));
		new_individual.addProperty("v-s:hasLifecycleStage", new Resource("d:yzukiatavri0xticlw3xax2qeg", Type._Uri));
		new_individual.addProperty("v-s:valid", "true", Type._Bool);
		new_individual.addProperty("mnd-s:appliesTo", "d:org_RU1121003135", Type._Uri);
		new_individual.addProperty("mnd-s:isAccessLimited", "false", Type._Bool);
		new_individual.addProperty("v-s:owner", "d:mondi_department_50003626", Type._Uri);
		new_individual.addProperty("v-s:hasSector", "d:4775f24d50774505bc8279314557b19a", Type._Uri);
		new_individual.addProperty("v-s:hasDocumentKind", new Resource("d:uqocbblmycyot69lvvv44m9c28", Type._Uri));
		new_individual.addProperty("v-s:hasMaintainedObject", new Resource("d:IF00000000000000196343", Type._Uri));

		int linksCount = 0;

		Resources nnn = null;
		Resources project = null;
		Resources inm = null;
		Resources cod = null;
		Resources comment = null;

		List<XmlAttribute> atts = doc.getAttributes();
		for (XmlAttribute att : atts)
		{
			String code = att.getCode();

			String predicate = fields_map.get(code);
			System.out.println("CODE: " + code);
			if (predicate != null)
			{
				Resources rss = ba_field_to_veda(level, att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri, true);

				if (predicate.equals("?") == false)
					new_individual.addProperty(predicate, rss);

				if (rss == null)
					continue;

				if (rss.resources.size() < 1)
					continue;

				if (code.equals("Комментарий"))
				{
					comment = rss;
				} else if (code.equals("Инв.№"))
				{
					inm = rss;
				} else if (code.equals("Проект"))
				{
					String link = att.getRecordIdValue();
					if (link != null)
					{
						XmlDocument linkDoc = ba.getActualDocument(link).getLeft();
						if (linkDoc != null)
						{
							List<XmlAttribute> linkAtts = linkDoc.getAttributes();
							for (XmlAttribute linkAtt : linkAtts)
							{
								String linkCode = linkAtt.getCode();
								if (linkCode.equals("Наименование"))
								{
									Resources rssLink = ba_field_to_veda(level, linkAtt, null, null, linkDoc, path, null, null, true);
									project = rssLink;
								} else if (linkCode.equals("Код"))
								{
									Resources rssLink = ba_field_to_veda(level, linkAtt, null, null, linkDoc, path, null, null, true);
									cod = rssLink;
								}
							}
						}
					}
				} else if (code.equals("Название"))
				{
					nnn = rss;
				} else if (code.equals("Раздел"))
				{
					String recordId = att.getRecordIdValue();
					if (recordId != null)
					{
						XmlDocument recordDoc = ba.getActualDocument(recordId).getLeft();
						String recordId2 = null;
						if (recordDoc != null)
							recordId2 = ba.get_first_value_of_field(recordDoc, "Раздел");
						if (recordId2 == null)
							recordId2 = "";
						if (recordId.equals("69ca82170be24d41b32fc9033a2574f5") || recordId2.equals("69ca82170be24d41b32fc9033a2574f5"))
						{
							new_individual.addProperty("mnd-s:hasMarkOfTechnicalDocuments", rss);
							//		new_individual.addProperty("v-s:hasDocumentKind", 
							//		new Resource("d:afc1a827f2ac47a9bd19b6db910dfc13", Type._Uri));
						} else if (recordId2.equals("4f391bc4b9434d619ea95396cd0faba7"))
						{
							new_individual.addProperty("mnd-s:hasSectionOfProjectDocumentation", rss);
							//								new_individual.addProperty("v-s:hasDocumentKind", 
							//									new Resource("d:kqyyu62f90hy89wh188664bxwl", Type._Uri));
						} else if (recordId.equals("7d67bd472db4481db0f5511f37107cae") || recordId.equals("db6c04d678c849859295f65efce1de76"))
						{
							new_individual.addProperty("mnd-s:hasMarkOfTechnicalDocuments",
									new Resource("d:6ba70b2261d4443e98d91452565d3b98", Type._Uri));
							//								new_individual.addProperty("v-s:hasDocumentKind", 
							//									new Resource("d:mqzlxqrejhhbod4ra42nq8cf", Type._Uri));
						} else if (recordId.equals("6ba70b2261d4443e98d91452565d3b98"))
						{
							//								new_individual.addProperty("v-s:hasDocumentKind", 
							//									new Resource("d:afc1a827f2ac47a9bd19b6db910dfc13", Type._Uri));
							new_individual.addProperty("mnd-s:hasMarkOfTechnicalDocuments",
									new Resource("d:6ba70b2261d4443e98d91452565d3b98", Type._Uri));
						} else if (recordId.equals("584e7ef299b14bec89c516b311472ba5"))
						{
							//								new_individual.addProperty("v-s:hasDocumentKind", 
							//									new Resource("d:99d3887ae22d439c9fe77a10ff5a4b0d", Type._Uri));
							new_individual.addProperty("mnd-s:hasMarkOfTechnicalDocuments",
									new Resource("d:6ba70b2261d4443e98d91452565d3b98", Type._Uri));
						} else if (recordId.equals("41b925655c8a44a8b34ab3b1894bebd0"))
						{
							//								new_individual.addProperty("v-s:hasDocumentKind", 
							//									new Resource("d:41b925655c8a44a8b34ab3b1894bebd0", Type._Uri));
							new_individual.addProperty("mnd-s:hasMarkOfTechnicalDocuments", new Resource("d:zn8jlec6ma6x28fsgp6lyw49zo", Type._Uri));
						}
					}
				} else if (code.equals("Разработчик"))
				{
					Resources code1 = null;
					Resources otvl1 = null;
					Resources rss1 = ba_field_to_veda(level, att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri, true);

					String link = att.getRecordIdValue();
					if (link != null)
					{
						XmlDocument linkDoc = ba.getActualDocument(link).getLeft();
						if (linkDoc != null)
						{

							List<XmlAttribute> linkAtts = linkDoc.getAttributes();
							for (XmlAttribute linkAtt : linkAtts)
							{
								String linkCode = linkAtt.getCode();
								if (linkCode.equals("Код"))
								{
									Resources rssLink = ba_field_to_veda(level, linkAtt, null, null, linkDoc, path, null, null, true);
									code1 = rssLink;
								} else if (linkCode.equals("Ответственное лицо"))
								{
									Resources rssLink = ba_field_to_veda(level, linkAtt, null, null, linkDoc, path, null, null, true);
									otvl1 = rssLink;
								}

							}
						}
					}

					Individual dev = new Individual();
					dev.setUri(new_individual.getUri() + "_correspondent");
					dev.addProperty("rdf:type", new Resource("mnd-s:Correspondent", Type._Uri));
					dev.addProperty("v-s:creator", new_individual.getResources("v-s:creator"));
					dev.addProperty("v-s:created", new_individual.getResources("v-s:created"));

					if (otvl1 != null)
						dev.addProperty("v-s:correspondentPersonDescription", otvl1);

					if (rss1 != null)
						dev.addProperty("v-s:correspondentOrganization", rss1);

					if (code1 != null)
						dev.addProperty("v-s:correspondentDepartmentDescription", code1);

					putIndividual(level, dev, ba_id);
					new_individual.addProperty("v-s:developer", new Resource(dev.getUri(), Type._Uri));

				} else if (code.equals("Сопровождающие документы"))
				{
					String link = att.getLinkValue();
					if (link != null)
					{
						XmlDocument linkDoc = ba.getActualDocument(link).getLeft();
						if (linkDoc != null)
						{
							List<XmlAttribute> linkAtts = linkDoc.getAttributes();
							for (XmlAttribute linkAtt : linkAtts)
							{
								String linkCode = linkAtt.getCode();
								if (linkCode.equals("attachment"))
								{
									Resources rssLink = ba_field_to_veda(level, linkAtt, null, null, linkDoc, path, null, null, true);
									new_individual.addProperty("v-s:attachment", rssLink);
								}
							}
						}
					}
				} else if (code.equals("Связанные документы"))
				{
					String link = att.getLinkValue();
					if (link != null)
					{
						Individual i = st_veda.getIndividual("d:" + link);
						if (i != null)
						{
							if (i.getResources("rdf:type") != null)
							{
								if (i.getResources("rdf:type").equals("mnd-s:EngineeringRequest"))
									new_individual.addProperty("mnd-s:hasEngineeringRequest", new Resource("d:" + link, Type._Uri));
								else
								{
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

		Object[] ff =
		{ nnn, " ", inm, " ", comment, " ", cod, " ", project };
		String[] langs_out =
		{ "EN", "RU" };
		Resources rss = rs_assemble(ff, langs_out);
		if (rss.resources.size() == 0)
		{
			String[] langs_out2 =
			{ "NONE" };
			rss = rs_assemble(ff, langs_out2);
		}
		if (rss.resources.size() > 0)
			new_individual.addProperty("v-s:title", rss);

		new_individual.addProperty("rdf:type", to_class, Type._Uri);
		res.add(new_individual);
		return res;
	}
}
