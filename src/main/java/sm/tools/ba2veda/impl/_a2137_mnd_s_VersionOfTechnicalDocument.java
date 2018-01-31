package sm.tools.ba2veda.impl;

import java.util.ArrayList;
import java.util.List;

import ru.mndsc.bigarchive.server.kernel.document.beans.XmlAttribute;
import ru.mndsc.bigarchive.server.kernel.document.beans.XmlDocument;
import ru.mndsc.objects.organization.Department;
import sm.tools.ba2veda.Ba2VedaTransform;
import sm.tools.ba2veda.BaSystem;
import sm.tools.ba2veda.Pair;
import sm.tools.ba2veda.Replacer;
import sm.tools.veda_client.Individual;
import sm.tools.veda_client.Resource;
import sm.tools.veda_client.Resources;
import sm.tools.veda_client.Type;
import sm.tools.veda_client.VedaConnection;

public class _a2137_mnd_s_VersionOfTechnicalDocument extends Ba2VedaTransform
{
	public _a2137_mnd_s_VersionOfTechnicalDocument(BaSystem _ba, VedaConnection _veda, Replacer replacer)
	{
		super(_ba, _veda, replacer, "a2137a5a9c4d4c7999b94b3ccfb05378", "mnd-s:VersionOfTechnicalDocument");
	}

	public void inital_set()
	{
		fields_map.put("Название", "v-s:title");
		fields_map.put("Марка", "v-s:hasDocumentKind");
		fields_map.put("Дата получения", "v-s:registrationDate");
		fields_map.put("Лист", "v-s:sheetsCount");
		fields_map.put("attachment_doc", "v-s:attachment");
		fields_map.put("Полное название", "rdfs:label");

		fields_map.put("Комплект", "?");
		fields_map.put("Комментарий", "?");
		fields_map.put("Обозначение", "?");
		fields_map.put("Разработчик", "?");
		fields_map.put("add_doc", "?");
		fields_map.put("attachment_doc", "?");
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

		new_individual.addProperty("rdf:type", to_class, Type._Uri);

		List<XmlAttribute> atts = doc.getAttributes();

		String f4 = null;
		String f6 = null;
		String f6f6 = null;

		Resources shortLabel = null;
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

				if (code.equals("Обозначение"))
					shortLabel = rss;
				else if (code.equals("Комплект"))
				{
					String kpl_id = att.getLinkValue();
					Pair<XmlDocument, Long> dd = ba.getActualDocument(kpl_id);
					Individual parent = veda.getIndividual("d:" + kpl_id);					
					if (parent != null)
					{
						Resources childs = parent.getResources("v-s:childUnit");
						if (childs == null)
							childs = new Resources();

						childs.add(new Resource(uri, Type._Uri));
						parent.addProperty("v-s:childUnit", childs);

						//if (parent_is_new == false)
						veda.putIndividual(parent, true, 0);
					}
					
					
					XmlDocument d1 = dd.getLeft();

					String f1 = ba.get_first_value_of_field(d1, "Цех");
					// Цех -> mnd-s:technicalDocumentObject
					if (f1 != null)
					{
						new_individual.addProperty("mnd-s:technicalDocumentObject", new Resource("d:" + f1, Type._Uri));
					}

					XmlAttribute xat1 = null;

					for (XmlAttribute ddsid_att : d1.getAttributes())
					{
						if (ddsid_att.getCode().equals("Объект ТОРО"))
						{

							String ddsid = ddsid_att.getRecordIdValue();
							Pair<XmlDocument, Long> pair = ba.getActualDocument(ddsid);
							if (pair == null)
								continue;

							XmlDocument ddsid_doc = pair.getLeft();

							List<XmlAttribute> ddsid_atts = ddsid_doc.getAttributes();

							for (XmlAttribute ddsid_att1 : ddsid_atts)
							{
								if (ddsid_att1.getCode().equals("1b073c10-91fb-451e-b636-8c5bfe77c598_2"))
								{
									new_individual.addProperty("v-s:hasMaintainedObject", new Resource("d:" + ddsid_att1.getTextValue(), Type._Uri));
									break;
								}
							}
						} else if (ddsid_att.getCode().equals("Разработчик"))
						{
							xat1 = ddsid_att;
						}
					}

					String f3 = ba.get_first_value_of_field(d1, "Инв.№");
					// Инв.№ -> v-s:inventoryNumber
					if (f3 != null)
					{
						new_individual.addProperty("v-s:inventoryNumber", new Resource(f3, Type._String));
					}

					f4 = ba.get_first_value_of_field(d1, "Тип работ");

					String f5 = ba.get_first_value_of_field(d1, "Разработчик");

					if (f5 != null)
					{
						Individual dev = new Individual();
						dev.setUri(new_individual.getUri() + "_res1");
						dev.addProperty("rdf:type", new Resource("v-s:Correspondent", Type._Uri));
						dev.addProperty("v-s:creator", new_individual.getResources("v-s:creator"));
						dev.addProperty("v-s:created", new_individual.getResources("v-s:created"));
						dev.addProperty("v-s:parent", new Resource(new_individual.getUri(), Type._Uri));

						Department dp = ba.getPacahon().getDepartmentByUid(f5, "RU", "");

						boolean is_mondi = true;
						boolean is_departnment = false;
						if (dp != null)
						{
							String iid = dp.getInternalId();
							if (iid.length() == 8 && iid.charAt(0) == '5')
								is_mondi = true;
							else
								is_mondi = false;

							is_departnment = true;
						}

						Resources rss1 = ba_field_to_veda(level, xat1, null, f5, d1, path, parent_ba_doc_id, parent_veda_doc_uri, true);

						if (rss1 != null)
						{
							if (is_mondi && is_departnment == false)
							{
								dev.addProperty("v-s:correspondentPerson", rss1);
								dev.addProperty("v-s:correspondentOrganization", new Resource("d:org_RU1121003135", Type._Uri));
							}
							if (is_mondi && is_departnment == true)
							{
								dev.addProperty("v-s:correspondentDepartment", rss1);
								dev.addProperty("v-s:correspondentOrganization", new Resource("d:org_RU1121003135", Type._Uri));
							}

							if (is_mondi == false && is_departnment == true)
							{
								dev.addProperty("v-s:correspondentOrganization", rss1);
							}

						}

						new_individual.addProperty("v-s:developer", new Resource(dev.getUri(), Type._Uri));
						putIndividual(level, dev, null);
					}

					f6 = ba.get_first_value_of_field(d1, "Раздел");

					Pair<XmlDocument, Long> dd1 = ba.getActualDocument(f6);
					if (dd1 != null)
					{
						XmlDocument d11 = dd1.getLeft();

						f6f6 = ba.get_first_value_of_field(d11, "Раздел");
					}

				} else if (code.equals("Разработчик"))
				{
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
				} else if (code.equals("Комментарий"))
				{
					Individual comment = new Individual();
					comment.addProperty("v-s:parent", new Resource(new_individual.getUri(), Type._Uri));
					comment.addProperty("v-s:created", new_individual.getResources("v-s:created"));
					comment.addProperty("v-s:creator", new_individual.getResources("v-s:creator"));
					comment.setUri(new_individual.getUri() + "_comment");
					comment.addProperty("rdf:type", new Resource("v-s:Comment", Type._Uri));
					comment.addProperty("v-s:backwardTarget", new Resource(new_individual.getUri(), Type._Uri));
					comment.addProperty("v-s:backwardProperty", new Resource("v-s:hasComment", Type._Uri));
					comment.addProperty("rdfs:label", rss);
					new_individual.addProperty("v-s:hasComment", new Resource(comment.getUri(), Type._Uri));
					putIndividual(level, comment, null);
				} else if (code.equals("add_doc"))
				{
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
				} else if (code.equals("attachment_doc"))
				{
					XmlDocument doc2 = ba.getActualDocument(att.getLinkValue()).getLeft();
					if (doc2 != null)
					{
						List<XmlAttribute> atts2 = doc2.getAttributes();
						for (XmlAttribute att2 : atts2)
						{
							if (att2.getCode().equals("attachment"))
							{
								Resources rss2 = ba_field_to_veda(level, att2, uri, ba_id, doc2, path, parent_ba_doc_id, parent_veda_doc_uri, true);
								new_individual.addProperty("v-s:attachment", rss2);
							}
						}
					}
				}
			}
		}

		new_individual.addProperty("v-s:backwardProperty", new Resource("mnd-s:hasVersionOfTechnicalDocument", Type._Uri));
		new_individual.addProperty("v-s:canRead", new Resource(true, Type._Bool));
		new_individual.addProperty("v-s:shortLabel", shortLabel);
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

		// Тип работ -> v-s:hasBudgetCategory
		if (f4 != null)
		{
			String ss = prepare_link(f4);

			td.addProperty("v-s:hasBudgetCategory", new Resource(ss, Type._Uri));
		}

		if (f6 != null)
		{
			//1) если у него поле "Раздел"."Раздел" = 69ca82170be24d41b32fc9033a2574f5(Рабочая документация), то помещаем его в поле mnd-s:hasMarkOfTechnicalDocuments этого индивида
			if (f6f6.equals("69ca82170be24d41b32fc9033a2574f5"))
				td.addProperty("mnd-s:hasMarkOfTechnicalDocuments", new Resource("d:" + f6, Type._Uri));

			// 1.1)"Раздел" = 69ca82170be24d41b32fc9033a2574f5(Рабочая документация), то тоже самое
			if (f6.equals("69ca82170be24d41b32fc9033a2574f5"))
				td.addProperty("mnd-s:hasMarkOfTechnicalDocuments", new Resource("d:" + f6, Type._Uri));

			//2) если у него поле "Раздел"."Раздел" = 4f391bc4b9434d619ea95396cd0faba7 (Проектная документация), то помещаем его в поле mnd-s:hasSectionOfProjectDocumentation этого индивида
			if (f6f6.equals("4f391bc4b9434d619ea95396cd0faba7"))
				td.addProperty("mnd-s:hasSectionOfProjectDocumentation", new Resource("d:" + f6f6, Type._Uri));

			//3) если у него поле "Раздел"."Раздел" = 7d67bd472db4481db0f5511f37107cae (Конструкторские док), 
			// то в поле mnd-s:hasDocumentKindStandard вписываем d:in6sbni64f41euzkalz7tcky 
			// и в поле mnd-s:hasDocumentKind вписываем d:mqzlxqrejhhbod4ra42nq8cf (Чертеж)
			if (f6f6.equals("7d67bd472db4481db0f5511f37107cae"))
			{
				td.addProperty("mnd-s:hasDocumentKindStandard", new Resource("d:in6sbni64f41euzkalz7tcky", Type._Uri));
				td.addProperty("mnd-s:hasDocumentKind", new Resource("d:mqzlxqrejhhbod4ra42nq8cf", Type._Uri));
			}

			//4) если само значение предиката =  db6c04d678c849859295f65efce1de76 (ПКД), то в 
			//		mnd-s:hasDocumentKind вписываем d:mqzlxqrejhhbod4ra42nq8cf (Чертеж)
			//		и в поле mnd-s:hasDocumentKindStandard вписываем d:in6sbni64f41euzkalz7tcky
			if (f6.equals("db6c04d678c849859295f65efce1de76"))
			{
				td.addProperty("mnd-s:hasDocumentKindStandard", new Resource("d:in6sbni64f41euzkalz7tcky", Type._Uri));
				td.addProperty("mnd-s:hasDocumentKind", new Resource("d:mqzlxqrejhhbod4ra42nq8cf", Type._Uri));
			}

			//		5) если само значение предиката =  6ba70b2261d4443e98d91452565d3b98 (Произвольный комплект), то в 
			//		mnd-s:hasDocumentKind вписываем d:afc1a827f2ac47a9bd19b6db910dfc13 (Комплект)
			//		и в поле mnd-s:hasDocumentKindStandard вписываем d:in6sbni64f41euzkalz7tcky
			if (f6.equals("6ba70b2261d4443e98d91452565d3b98") || ba_id.equals("69ca82170be24d41b32fc9033a2574f5"))
			{
				td.addProperty("mnd-s:hasDocumentKindStandard", new Resource("d:in6sbni64f41euzkalz7tcky", Type._Uri));
				td.addProperty("mnd-s:hasDocumentKind", new Resource("d:afc1a827f2ac47a9bd19b6db910dfc13", Type._Uri));
			}

			//		6) если само значение предиката =  584e7ef299b14bec89c516b311472ba5 (Составное изделение), то в 
			//		mnd-s:hasDocumentKind вписываем d:99d3887ae22d439c9fe77a10ff5a4b0d (Сборочный чертеж) 
			// и в поле mnd-s:hasDocumentKindStandard вписываем d:in6sbni64f41euzkalz7tcky
			if (f6.equals("584e7ef299b14bec89c516b311472ba5"))
			{
				td.addProperty("mnd-s:hasDocumentKindStandard", new Resource("d:in6sbni64f41euzkalz7tcky", Type._Uri));
				td.addProperty("mnd-s:hasDocumentKind", new Resource("d:99d3887ae22d439c9fe77a10ff5a4b0d", Type._Uri));
			}

			//		7) если само значение предиката = 41b925655c8a44a8b34ab3b1894bebd0 (Инженерн изыск), то в 
			//		mnd-s:hasDocumentKind вписываем d:zn8jlec6ma6x28fsgp6lyw49zo (Инженерные изыскания) 
			// и в поле mnd-s:hasDocumentKindStandard вписываем d:in6sbni64f41euzkalz7tcky
			if (f6.equals("41b925655c8a44a8b34ab3b1894bebd0"))
			{
				td.addProperty("mnd-s:hasDocumentKindStandard", new Resource("d:in6sbni64f41euzkalz7tcky", Type._Uri));
				td.addProperty("mnd-s:hasDocumentKind", new Resource("d:zn8jlec6ma6x28fsgp6lyw49zo", Type._Uri));
			}

		}

		new_individual.addProperty("v-s:backwardTarget", new Resource(td.getUri(), Type._Uri));
		putIndividual(level, td, null);

		res.add(new_individual);
		return res;
	}
}
