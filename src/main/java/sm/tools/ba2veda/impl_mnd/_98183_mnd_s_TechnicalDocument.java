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

public class _98183_mnd_s_TechnicalDocument extends Ba2VedaTransform
{
	public _98183_mnd_s_TechnicalDocument(BaSystem _ba, VedaConnection _veda, Replacer replacer)
	{
		super(_ba, _veda, replacer, "98183c7a52294f7598a3077cdd7c1119", "mnd-s:TechnicalDocument");
	}

	public void inital_set()
	{
		fields_map.put("Наименование", "?");
		fields_map.put("Содержание", "?");
		fields_map.put("Инв.№", "?");
		fields_map.put("Номер в журнале", "?");
		fields_map.put("Местонахождение бумажной копии", "?");
		fields_map.put("Комментарий", "?");

		fields_map.put("Объект", "mnd-s:technicalDocumentObject");
		fields_map.put("Справочник", "mnd-s:hasMarkOfTechnicalDocuments");
		//fields_map.put("Стадия", "v-s:LifecycleStage");
		fields_map.put("Разработчик", "?");
		fields_map.put("Шифр от разработчика", "v-s:shortLabel");
		fields_map.put("Прилагаемые документы", "v-s:attachment");
		fields_map.put("Сопровождающий документ", "v-s:attachment");
		fields_map.put("Чертежи", "v-s:attachment");

		//fields_map.put("Раздел", "v-s:hasClassifierMarkOfWorkingDrawingsSet");
		//fields_map.put("Проект", "?");
		//fields_map.put("Контракт", "?");
		//fields_map.put("Тип работ", "v-s:hasBudgetCategory");
		//fields_map.put("Дата получения", "v-s:registrationDate");
		//fields_map.put("Сопровождающие документы", "?");
		//fields_map.put("Конструкторская заявка", "mnd-s:hasEngineeringRequest");
		//fields_map.put("Связанные документы", "?");
		//fields_map.put("Комплект", "v-s:backwardTarget");
		//fields_map.put("Полное название", "rdfs:label");
		//fields_map.put("Раздел", "?");
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

		new_individual.addProperty("v-s:LifecycleStage", new Resource("d:yzukiatavri0xticlw3xax2qeg", Type._Uri));
		new_individual.addProperty("v-s:hasDocumentKind", new Resource("d:uqocbblmycyot69lvvv44m9c28", Type._Uri));
		new_individual.addProperty("v-s:hasSector", "d:698a3a5cda374a46894334f6329f7cc0", Type._Uri);
		new_individual.addProperty("v-s:hasSector", "d:4775f24d50774505bc8279314557b19a", Type._Uri);
		new_individual.addProperty("v-s:hasSector", "d:831daafd86d9489ebb458450a49998a2", Type._Uri);
		new_individual.addProperty("v-s:hasMaintainedObject", new Resource("d:IF00000000000000196343", Type._Uri));
		new_individual.addProperty("v-s:valid", "true", Type._Bool);
		new_individual.addProperty("mnd-s:appliesTo", "d:org_RU1121003135", Type._Uri);

		//new_individual.addProperty("v-s:backwardProperty", new Resource("mnd-s:hasTechnicalDocument", Type._Uri));
		//new_individual.addProperty("v-s:canRead", new Resource(true, Type._Bool));
		//new_individual.addProperty("mnd-s:isAccessLimited", "false", Type._Bool);
		//new_individual.addProperty("v-s:owner", "d:mondi_department_50003626", Type._Uri);

		int linksCount = 0;

		Resources nnn = null;
		Resources project = null;
		Resources inm = null;
		Resources cod = null;
		Resources sod = null;
		Resources nj = null;
		Resources mbc = null;
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
				} else if (code.equals("Номер в журнале"))
				{
					nj = rss;
				} else if (code.equals("Местонахождение бумажной копии"))
				{
					mbc = rss;
				} else if (code.equals("Содержание"))
				{
					sod = rss;
				} else if (code.equals("Инв.№"))
				{
					inm = rss;
				} else if (code.equals("Проект"))
				{
					String link1 = att.getRecordIdValue();
					if (link1 != null)
					{
						XmlDocument linkDoc = ba.getActualDocument(link1).getLeft();
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
				} else if (code.equals("Контракт"))
				{
					String link1 = att.getRecordIdValue();
					if (link1 != null)
					{
						XmlDocument linkDoc = ba.getActualDocument(link1).getLeft();
						if (linkDoc != null)
						{
							List<XmlAttribute> linkAtts = linkDoc.getAttributes();
							for (XmlAttribute linkAtt : linkAtts)
							{
								String linkCode = linkAtt.getCode();
								if (linkCode.equals("Наименование"))
								{
									Resources rssLink = ba_field_to_veda(level, linkAtt, null, null, linkDoc, path, null, null, true);
									//contract = rssLink;
								}
							}
						}
					}
				} else if (code.equals("Наименование"))
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
						new_individual.addProperty("v-s:hasClassifierMarkOfWorkingDrawingsSet", rss);
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

		//Наименование+Содержание+Инв. №+Номер в журнале+'Местонахождение бумажной копии:'+Местонахождение бумажной копии+Комментарий
		Object[] ff =
		{ nnn, " ", sod, " ", inm, " ", nj, " Местонахождение бумажной копии:", mbc, " ", comment };
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
		new_individual.addProperty("v-s:registrationNumber", "10300000", Type._String);
		res.add(new_individual);
		return res;
	}
}
