package org.gost19.ba2onto;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Vector;

import javax.xml.datatype.XMLGregorianCalendar;

import magnetico.objects.organization.Department;
import magnetico.ws.document.DocumentTemplateType;
import magnetico.ws.organization.AttributeType;
import magnetico.ws.organization.EntityType;
import net.n3.nanoxml.IXMLElement;
import net.n3.nanoxml.IXMLParser;
import net.n3.nanoxml.IXMLReader;
import net.n3.nanoxml.StdXMLReader;
import net.n3.nanoxml.XMLParserFactory;

public class Fetcher
{

	private static String documentTypeId;
	private static String ticketId;
	private static String SEARCH_URL;
	// private static QName SEARCH_QNAME = new
	// QName("http://search.bigarchive.magnetosoft.ru/", "SearchService");
	private static String DOCUMENT_SERVICE_URL;
	private static String pathToDump;
	private static boolean fake = false;
	private static Properties properties = new Properties();
	// private static ArrayList<String> roles = new ArrayList<String>();
	// private static ArrayList<String> admins = new ArrayList<String>();
	private static Connection connection = null;
	private static String dbUser;
	private static String dbPassword;
	private static String dbUrl;
	private static String dbSuffix;
	private static HashMap<String, String> code_onto = new HashMap<String, String>();
	private static HashMap<String, String> old_code__new_code = new HashMap<String, String>();
	private static String exclude_codes = "$comment, $private, $authorId, $defaultRepresentation";
	private static HashMap<String, EntityType> userUri__userObj = new HashMap<String, EntityType>();
	private static OrganizationUtil organizationUtil;
	private static String user_docflow = "541e2793-abc4-437a-9c71-6a1ac0434acf";

	public static void main(String[] args) throws Exception
	{
		// exclude_code.put("$parentDocumentId", "Y");
		{
			code_onto.put("date_from", Predicate.swrc__startDate);
			code_onto.put("to", Predicate.swrc__endDate);
			code_onto.put("date_to", Predicate.swrc__endDate);
			code_onto.put("Дата окончания (планируемая)", Predicate.swrc__endDate);
			code_onto.put("from", Predicate.docs19__from);
			code_onto.put("От кого", Predicate.docs19__from);
			code_onto.put("Кому", Predicate.docs19__to);
			code_onto.put("name", Predicate.swrc__name);
			code_onto.put("Name", Predicate.swrc__name);
			code_onto.put("Название", Predicate.swrc__name);
			code_onto.put("Наименование", Predicate.swrc__name);
			code_onto.put("name", Predicate.swrc__name);
			code_onto.put("Полное название", Predicate.swrc__name);
			code_onto.put("Тема", Predicate.dc__subject);
			code_onto.put("Заголовок", Predicate.dc__title);
			code_onto.put("Тип", Predicate.dc__type);
			code_onto.put("Подразделение", Predicate.docs19__department);
			code_onto.put("Дата окончания", Predicate.swrc__endDate);
			code_onto.put("Дата начала", Predicate.swrc__startDate);
			code_onto.put("В копию", Predicate.docs19__carbon_copy);
			code_onto.put("Контрагент (название/ страна/ город)", Predicate.docs19__contractor);
			code_onto.put("Контрагент", Predicate.docs19__contractor);
			code_onto.put("Вложения", Predicate.docs19__FileDescription);
			code_onto.put("Вложение", Predicate.docs19__FileDescription);
			code_onto.put("file", Predicate.docs19__FileDescription);
			code_onto.put("Ключевые слова", Predicate.swrc__keywords);
			code_onto.put("Номер", Predicate.swrc__number);
			code_onto.put("Ссылка на документ", Predicate.docs19__link);
			code_onto.put("Содержание", Predicate.docs19__content);
			code_onto.put("Краткое содержание", Predicate.dc__description);
			code_onto.put("Комментарии", Predicate.swrc__note);
			code_onto.put("Комментарий", Predicate.swrc__note);

			/*
			 * [Связанные документы]:19 [Цех]:15 [Дата регистрации]:13
			 * [status]:13 [Разработчик]:12 [Объект ТОРО]:11 [Конструкторская
			 * заявка]:10 [Раздел]:10 [Регистрационный номер]:9 [Тип работ]:9
			 * [status_history]:9 [Подписывающий]:9 [Проект]:8 [Дата старта
			 * маршрута]:8 [Дата получения]:8 [Количество листов]:8 [Инв.№]:8
			 * [Обозначение]:8 [Инициатор]:7 [Лист]:6 [Шифр]:6 [Исполнитель]:5
			 * [Вид документа]:5 [Регистрационный номер 1]:5 [Код]:4
			 * [Ответственное лицо]:4 [Список рассылки]:4 [Сопровождающие
			 * документы]:3 [Руководитель проекта]:3 [Источник финансирования]:3
			 * [Конструкторский проект]:3 [Предмет договора]:3 [Дата
			 * заключения]:3 [Объект]:3
			 */
		}

		loadProperties();
		init_source();

		organizationUtil = new OrganizationUtil(properties.getProperty("organizationUrl"), properties.getProperty("organizationNameSpace"),
				properties.getProperty("organizationName"));

		fetchOrganization("organization.nt");
		fetchDocumentTypes("doc_types.nt");
		fetchDocuments("docs.nt");

	}

	static void init_source() throws Exception
	{
		System.out.print("connect to source database " + dbUrl + "...");
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		connection = DriverManager.getConnection("jdbc:mysql://" + dbUrl, dbUser, dbPassword);

		System.out.println("ok");
	}

	private static void addLinkToDocument(String DocUri, String DocId, String attUri, OutputStreamWriter out) throws Exception
	{
		String PersonUri = Predicate.zdb + DocId;

		writeTriplet(DocUri, attUri, PersonUri, false, out);
		String newNodeId = DocUri + attUri;

		writeTriplet(newNodeId, Predicate.rdf__type, Predicate.rdf__Statement, false, out);
		writeTriplet(newNodeId, Predicate.rdf__object, PersonUri, false, out);
		writeTriplet(newNodeId, Predicate.rdf__subject, DocUri, false, out);
		writeTriplet(newNodeId, Predicate.rdf__predicate, attUri, false, out);

		writeTriplet(newNodeId, Predicate.swrc__firstName, "репрезентатионвалуес", false, out);
	}

	private static void addPersonToDocument(String DocUri, String PersonId, String attUri, OutputStreamWriter out) throws Exception
	{
		String PersonUri = Predicate.zdb + "person_" + PersonId;

		writeTriplet(DocUri, attUri, PersonUri, false, out);

		String newNodeId = DocUri + attUri;

		writeTriplet(newNodeId, Predicate.rdf__type, Predicate.rdf__Statement, false, out);
		writeTriplet(newNodeId, Predicate.rdf__object, PersonUri, false, out);
		writeTriplet(newNodeId, Predicate.rdf__subject, DocUri, false, out);
		writeTriplet(newNodeId, Predicate.rdf__predicate, attUri, false, out);

		EntityType person = userUri__userObj.get(PersonUri);
		if (person == null)
		{
			person = organizationUtil.getUser(PersonId);
		}

		if (person != null)
		{
			for (AttributeType a : person.getAttributes().getAttributeList())
			{
				String name = a.getName();
				String value = a.getValue();

				if (name.equalsIgnoreCase("firstNameRu"))
				{
					if (!(PersonId.equals(user_docflow) && (value == null || value.length() == 0)))
						writeTriplet(newNodeId, Predicate.swrc__firstName, value, false, out);
				}
				else if (name.equalsIgnoreCase("surnameRu"))
				{
					writeTriplet(newNodeId, Predicate.swrc__lastName, value, false, out);
				}
			}
		}
		else
		{
			throw new Exception("user [" + PersonId + "] not found in organization");
		}
	}

	private static int walkOnDocuments(OutputStreamWriter out) throws Exception
	{
		int count_documents = 0;

		IXMLParser parser = XMLParserFactory.createDefaultXMLParser();

		writeTriplet(Predicate.f_zdb, Predicate.owl__imports, Predicate.docs19, false, out);
		writeTriplet(Predicate.f_zdb, Predicate.owl__imports, Predicate.f_swrc, false, out);
		writeTriplet(Predicate.f_zdb, Predicate.owl__imports, Predicate.gost19, false, out);
		writeTriplet(Predicate.f_zdb, Predicate.rdf__type, Predicate.owl__Ontology, false, out);

		String docsIdDataQuery = "select objectId FROM objects where isTemplate = 0 and timestamp is null";
		ResultSet docRecordsRs = connection.createStatement().executeQuery(docsIdDataQuery);

		while (docRecordsRs.next())
		{
			String docId = docRecordsRs.getString(1);

			String docDataQuery = "select distinct content FROM objects where objectId = '" + docId + "' order by timestamp asc";
			Statement st1 = connection.createStatement();
			ResultSet docRecordRs = st1.executeQuery(docDataQuery);

			while (docRecordRs.next())
			{
				String id = null;
				try
				{
					count_documents++;

					if (count_documents % 1000 == 0)
						System.out.println("count prepared documents = " + count_documents);

					String docXmlStr = docRecordRs.getString(1);

					IXMLReader reader = StdXMLReader.stringReader(docXmlStr);
					parser.setReader(reader);
					IXMLElement xmlDoc = (IXMLElement) parser.parse(true);
					reader.close();

					String authorId = get(xmlDoc, "authorId", null);
					String dateCreated = get(xmlDoc, "dateCreated", null);
					String dateLastModified = get(xmlDoc, "dateLastModified", null);
					String lastEditorId = get(xmlDoc, "lastEditorId", null);
					String objectType = get(xmlDoc, "objectType", null);
					String typeId = get(xmlDoc, "typeId", null);
					id = get(xmlDoc, "id", null);

					String newDocSubj = Predicate.zdb + id;

					addPersonToDocument(newDocSubj, authorId, Predicate.dc__creator, out);

					writeTriplet(newDocSubj, Predicate.rdf__type, Predicate.user_onto + typeId, false, out);

					Vector<IXMLElement> atts = null;
					atts = xmlDoc.getFirstChildNamed("xmlAttributes").getChildren();

					if (atts != null)
					{
						for (IXMLElement att_list_element : atts)
						{
							String type = get(att_list_element, "type", "");
							String att_code = get(att_list_element, "code", "");
							String onto_code = old_code__new_code.get(att_code);
							if (onto_code == null)
								continue;

							if (type.equals("ORGANIZATION"))
							{
								/*
								 * <xmlAttribute>
								 * 		<dateCreated>2010-02-02T09:40:08.862 +03:00</dateCreated>
								 * 		<description></description>
								 * 		<multiSelect>true</multiSelect>
								 * 		<name>Кому</name>
								 * 		<obligatory>true</obligatory>
								 * 		<organizationTag>user</organizationTag>
								 * 		<organizationValue>fb926d69-3a49-4842-a2e2-e592fd301073</organizationValue>
								 * 		<type>ORGANIZATION</type>
								 * 		<computationalConfirm>NONE</computationalConfirm>
								 * 		<computationalReadonly>false</computationalReadonly>
								 * 		<code>Кому</code> 
								 * 		<xmlAttributes/>
								 * </xmlAttribute>
								 */
								String organizationValue = get(att_list_element, "organizationValue", null);
								String organizationTag = get(att_list_element, "organizationTag", null);

								if (organizationValue != null)
								{
									if (organizationTag.indexOf("user") >= 0)
									{
										addPersonToDocument(newDocSubj, organizationValue, onto_code, out);

									}
								}
							}
							else if (type.equals("TEXT"))
							{

								String textValue = get(att_list_element, "textValue", null);

								if (textValue != null && textValue.length() > 0)
									writeTriplet(newDocSubj, onto_code, textValue, true, out);
							}
							else if (type.equals("LINK"))
							{
								String value = get(att_list_element, "linkValue", null);

								if (value != null && value.length() > 0)
								{
									addLinkToDocument(newDocSubj, value, onto_code, out);
								}

							}

						}
					}

				} catch (Exception ex)
				{
					ex.printStackTrace();
					System.out.println("skip document id=" + id + ", reson:" + ex.getMessage());
				}
			}
			docRecordRs.close();
			st1.close();
			out.flush();
		}

		return count_documents;
	}

	/**
	 * Выгружает данные документов в виде триплетов (пример: onto/zdb.n3)
	 */

	private static void fetchDocuments(String name_file) throws Exception
	{
		int count_doc = 0;

		try
		{

			long fetchStart = System.nanoTime();

			OutputStreamWriter out = null;
			if (!fake)
			{
				FileOutputStream fw = new FileOutputStream(pathToDump + java.io.File.separatorChar + name_file);
				out = new OutputStreamWriter(fw, "UTF8");
			}

			count_doc = walkOnDocuments(out);

			if (!fake)
			{
				out.close();
			}

			System.out.println("TOTAL: Finished in " + ((System.nanoTime() - fetchStart) / 1000000000.0) + " s. for " + count_doc
					+ " docs.");

			System.out.println("TOTAL: Averall extracting speed  = " + count_doc / ((System.nanoTime() - fetchStart) / 1000000000.0)
					+ " docs/s");

		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * Выгружает данные структуры документов в виде пользовательских онтологий
	 * (пример: onto/user-onto.n3)
	 */

	private static void fetchDocumentTypes(String name_file) throws Exception
	{
		IXMLParser parser = XMLParserFactory.createDefaultXMLParser();

		HashMap<String, Integer> code_stat = new HashMap<String, Integer>();

		long fetchStart = System.nanoTime();

		OutputStreamWriter out = null;
		if (!fake)
		{
			FileOutputStream fw = new FileOutputStream(pathToDump + java.io.File.separatorChar + name_file);
			out = new OutputStreamWriter(fw, "UTF8");
		}

		writeTriplet(Predicate.f_user_onto, Predicate.owl__imports, Predicate.dc, false, out);
		writeTriplet(Predicate.f_user_onto, Predicate.owl__imports, Predicate.f_swrc, false, out);
		writeTriplet(Predicate.f_user_onto, Predicate.owl__imports, Predicate.gost19, false, out);
		writeTriplet(Predicate.f_user_onto, Predicate.owl__imports, Predicate.docs19, false, out);
		writeTriplet(Predicate.f_user_onto, Predicate.owl__imports, Predicate.user_onto, false, out);
		writeTriplet(Predicate.f_user_onto, Predicate.rdf__type, Predicate.owl__Ontology, false, out);

		String docsIdDataQuery = "select objectId FROM objects where isTemplate = 1 and timestamp is null";
		ResultSet docRecordsRs = connection.createStatement().executeQuery(docsIdDataQuery);

		while (docRecordsRs.next())
		{
			String docId = docRecordsRs.getString(1);

			String docDataQuery = "select distinct content FROM objects where objectId = '" + docId + "' order by timestamp asc";
			Statement st1 = connection.createStatement();
			ResultSet docRecordRs = st1.executeQuery(docDataQuery);

			while (docRecordRs.next())
			{
				String docXmlStr = docRecordRs.getString(1);

				IXMLReader reader = StdXMLReader.stringReader(docXmlStr);
				parser.setReader(reader);
				IXMLElement xmlDoc = (IXMLElement) parser.parse(true);
				reader.close();

				String authorId = get(xmlDoc, "authorId", null);
				String dateCreated = get(xmlDoc, "dateCreated", null);
				String lastModifiedTime = get(xmlDoc, "dateLastModified", null);
				String lastEditorId = get(xmlDoc, "lastEditorId", null);
				String objectType = get(xmlDoc, "objectType", null);
				String typeId = get(xmlDoc, "typeId", null);
				String id = get(xmlDoc, "id", null);
				String name = get(xmlDoc, "name", null);

				// XMLGregorianCalendar dateCreated =
				// documentTypeType.getDateCreated();
				// XMLGregorianCalendar lastModifiedTime =
				// documentTypeType.getLastModifiedTime();
				// String name = documentTypeType.getName();
				// String systemInformation =
				// documentTypeType.getSystemInformation();

				if (dateCreated != null && lastModifiedTime != null)
				{
					if (dateCreated.equals(lastModifiedTime))
					{
						System.out.println("dateCreated == lastModifiedTime");
					}
					else
					{
						System.out.println("dateCreated != lastModifiedTime");
					}
				}

				String activeStatusLabel = "";
				if (dateCreated == null)
					activeStatusLabel = "удален";

				String draftStatusLabel = "";
				// if (documentTypeType.isInDraftState() == true)
				// draftStatusLabel = "черновик";

				// System.out.println(String.format("\n%s:[%s] %s %s",
				// documentTypeType.getId(), name, activeStatusLabel,
				// draftStatusLabel));

				// String[] si_elements = null;

				// if (systemInformation != null)
				// si_elements = systemInformation.split(";");

				// <http://user-onto.org#internal_memo>
				// <http://www.w3.org/2000/01/rdf-schema#subClassOf>
				// <http://gost19.org/base#Document> .
				writeTriplet(Predicate.user_onto + id, Predicate.rdfs__subClassOf, Predicate.docs19__Document, false, out);

				// <http://user-onto.org#internal_memo>
				// <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>
				// <http://www.w3.org/2000/01/rdf-schema#Class> .
				writeTriplet(Predicate.user_onto + id, Predicate.rdf__type, Predicate.rdfs__Class, false, out);

				writeTriplet(Predicate.user_onto + id, Predicate.dc__creator, Predicate.zdb + "person_" + authorId, false, out);

				if (dateCreated != null)
					writeTriplet(Predicate.user_onto + id, Predicate.swrc__creationDate, dateCreated.toString(), true, out);

				if (lastModifiedTime != null)
					writeTriplet(Predicate.user_onto + id, Predicate.dc__date, lastModifiedTime.toString(), true, out);

				// <http://user-onto.org#internal_memo>
				// <http://www.w3.org/2000/01/rdf-schema#label>
				// "\u0441\u043B\u0443\u0436\u0435\u0431\u043D\u0430\u044F \u0437\u0430\u043F\u0438\u0441\u043A\u0430"@ru
				// .
				LangString lName = LangString.parse(name);

				if (lName.text_ru != null)
					writeTriplet(Predicate.user_onto + id, Predicate.rdfs__label, lName.text_ru, true, out, "ru");
				if (lName.text_en != null)
					writeTriplet(Predicate.user_onto + id, Predicate.rdfs__label, lName.text_en, true, out, "en");

				// /////////////////////////////////////////////////////////////////////////////////////////////
				Vector<IXMLElement> atts = null;

				atts = xmlDoc.getFirstChildNamed("xmlAttributes").getChildren();

				int ii = 0;
				if (atts != null)
				{
					for (IXMLElement att_list_element : atts)
					{
						ii++;

						String att_name = get(att_list_element, "name", "");

						if (exclude_codes.indexOf(att_name) >= 0)
						{
							System.out.println("\n	att_name=[" + att_name + "] is skipped");

							continue;
						}

						// <>
						// <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>
						// <http://www.w3.org/2002/07/owl#Restriction> .
						String restrictionId = "_:" + id + "_" + ii;

						System.out.println("\n");

						writeTriplet(restrictionId, Predicate.rdf__type, Predicate.owl__Restriction, false, out);

						writeTriplet(Predicate.user_onto + id, Predicate.rdfs__subClassOf, restrictionId, false, out);

						lName = LangString.parse(att_name);

						if (lName.text_ru != null)
							writeTriplet(restrictionId, Predicate.rdfs__label, lName.text_ru, true, out, "ru");
						if (lName.text_en != null)
							writeTriplet(restrictionId, Predicate.rdfs__label, lName.text_en, true, out, "en");

						String code = get(att_list_element, "code", null);

						System.out.println("	code=[" + code + "]");

						// подсчет частоты встречаемости code
						Integer count = code_stat.get(code);
						if (count != null)
							count = new Integer(count.intValue() + 1);
						else
						{
							count = new Integer(1);
						}

						// переименование code в onto
						String this_code_in_onto = renameCodeToOnto(code, id, lName.text_ru);

						writeTriplet(restrictionId, Predicate.owl__onProperty, this_code_in_onto, false, out);

						code_stat.put(code, count);

						String descr = get(att_list_element, "description", "");

						String multi_select_label = "";
						String obligatory_label = "";

						obligatory_label = get(att_list_element, "obligatory", "false");
						multi_select_label = get(att_list_element, "multiSelect", "false");

						if (multi_select_label.equals("true"))
						{
							multi_select_label = "[множественное]";
						}
						else
						{
							// ? <http://www.w3.org/2002/07/owl#maxCardinality>
							// "1"^^<http://www.w3.org/2001/XMLSchema#nonNegativeInteger>
							// .
							writeTriplet(restrictionId, Predicate.owl__maxCardinality, "1", true, out);
						}

						if (obligatory_label.equals("true"))
						{
							obligatory_label = "[обязательное]";

							// <> <http://www.w3.org/2002/07/owl#minCardinality>
							// "1"^^<http://www.w3.org/2001/XMLSchema#nonNegativeInteger>
							// .
							writeTriplet(restrictionId, Predicate.owl__minCardinality, "1", true, out);
						}
						else
						{
							// ?
						}

						// att_list_element.getDateCreated();

						String type = get(att_list_element, "type", "");

						String typeLabel = null;

						String obj_owl__allValuesFrom = null;

						if (type.equals("BOOLEAN"))
						{
							typeLabel = "boolean";
							obj_owl__allValuesFrom = Predicate.xsd__boolean;
						}
						else if (type.equals("DATE"))
						{
							typeLabel = "date";
							obj_owl__allValuesFrom = Predicate.xsd__date;
						}
						else if (type.equals("DATEINTERVAL"))
						{
							typeLabel = "date_interval";
							obj_owl__allValuesFrom = Predicate.docs19__dateInterval;
						}
						else if (type.equals("FILE"))
						{
							obj_owl__allValuesFrom = Predicate.docs19__FileDescription;
							typeLabel = "attachment";
						}
						else if (type.equals("LINK") || type.equals("DICTIONARY"))
						{
							if (type.equals("DICTIONARY"))
							{
								String dictionaryIdValue = get(att_list_element, "dictionaryIdValue", null);

								if (dictionaryIdValue != null)
								{
									System.out.println("dictionaryIdValue = " + dictionaryIdValue);
									obj_owl__allValuesFrom = Predicate.user_onto + dictionaryIdValue;
								}

							}
							else
								obj_owl__allValuesFrom = Predicate.docs19__Document;

							if (descr.indexOf("$composition") >= 0)
							{
								// нужно вынести в hasValue импортируемые поля
								// из ссылаемого документа
								String[] compzes = descr.split(";");

								for (String compz : compzes)
								{
									if (compz.indexOf("$isTable") >= 0)
									{
										String[] tmpa = compz.split("=");
										if (tmpa.length > 1)
										{
											String data = tmpa[1];

											obj_owl__allValuesFrom = Predicate.user_onto + id;
										}
									}
									if (compz.indexOf("$composition") >= 0)
									{
										String[] tmpa = compz.split("=");
										if (tmpa.length > 1)
										{
											String data = tmpa[1];
											if (data != null)
											{
												String[] importsFields = data.replace('|', ';').split(";");

												for (String field : importsFields)
												{
													String new_code = old_code__new_code.get(field);

													if (new_code == null)
														new_code = renameCodeToOnto(field, null, null);

													writeTriplet(restrictionId, Predicate.owl__hasValue, new_code, true, out);

													new_code += "";
												}
											}
										}

									}
								}

							}
							if (type.equals("LINK"))
								typeLabel = "document_link";
							else
								typeLabel = "dictionary";

						}
						else if (type.equals("NUMBER"))
						{
							typeLabel = "number";
							obj_owl__allValuesFrom = Predicate.xsd__integer;
						}
						else if (type.equals("ORGANIZATION"))
						{
							typeLabel = "organization";
							// obj_owl__allValuesFrom =
							// Predicate.swrc__Organization;

							// нужно определить что это за тип, person ?
							// department ? organization
							String organizationTag = get(att_list_element, "organizationTag", "");

							String[] organizationTags = organizationTag.split(";");

							for (String tag : organizationTags)
							{
								if (tag.equals("user"))
								{
									// для этого типа нужно добавить:

									// <><http://www.w3.org/2002/07/owl#hasValue><http://swrc.ontoware.org/ontology#lastName>
									writeTriplet(restrictionId, Predicate.owl__hasValue, Predicate.swrc__lastName, true, out);

									// <><http://www.w3.org/2002/07/owl#hasValue><http://swrc.ontoware.org/ontology#firstName>
									writeTriplet(restrictionId, Predicate.owl__hasValue, Predicate.swrc__firstName, true, out);

									if (organizationTags.length == 1)
										writeTriplet(restrictionId, Predicate.owl__allValuesFrom, Predicate.swrc__Person, true, out);
									else
										writeTriplet(restrictionId, Predicate.owl__someValuesFrom, Predicate.swrc__Person, true, out);

								}
								else if (tag.equals("department"))
								{
									// <><http://www.w3.org/2002/07/owl#hasValue><http://swrc.ontoware.org/ontology#name>
									writeTriplet(restrictionId, Predicate.owl__hasValue, Predicate.swrc__name, true, out);

									if (organizationTags.length == 1)
										writeTriplet(restrictionId, Predicate.owl__allValuesFrom, Predicate.swrc__Department, true, out);
									else
										writeTriplet(restrictionId, Predicate.owl__someValuesFrom, Predicate.swrc__Department, true, out);

								}
							}

						}
						else if (type.equals("TEXT"))
						{
							typeLabel = "text";
							obj_owl__allValuesFrom = Predicate.xsd__string;
						}

						if (obj_owl__allValuesFrom != null)
							writeTriplet(restrictionId, Predicate.owl__allValuesFrom, obj_owl__allValuesFrom, false, out);

						System.out.println("	att_name=[" + att_name + "]:" + typeLabel + " " + obligatory_label + " " + multi_select_label);
						System.out.println("	description=[" + descr + "]");

					}
				}

			}
			docRecordRs.close();
			st1.close();
			out.flush();
		}

		if (!fake)
		{
			out.close();
		}

		System.out.println("\ncode entry stat");
		for (int ii = 20; ii > 0; ii--)
		{
			for (Entry<String, Integer> ee : code_stat.entrySet())
			{
				if (ee.getValue() == ii)
					System.out.println("[" + ee.getKey() + "]:" + ee.getValue());
			}
		}

		System.out.println("\ncode -> onto");
		for (Entry<String, String> ee : old_code__new_code.entrySet())
		{
			System.out.println("[" + ee.getKey() + "]->[" + ee.getValue() + "]");
		}

	}

	private static String renameCodeToOnto(String code, String id, String alternativeName)
	{
		String this_code_in_onto = code_onto.get(code);

		if (this_code_in_onto == null)
		{
			if ((code.indexOf('1') >= 0 || code.indexOf('2') >= 0 || code.indexOf('3') >= 0 || code.indexOf('4') >= 0
					|| code.indexOf('5') >= 0 || code.indexOf('6') >= 0 || code.indexOf('7') >= 0 || code.indexOf('8') >= 0
					|| code.indexOf('9') >= 0 || code.indexOf('0') >= 0)
					&& alternativeName != null)
			{
				// если code содержит uid, то заменим code на
				// название из метки
				if (id.equals("0027562cbe0948e5965c3183eb23e42c") == false)
					this_code_in_onto = alternativeName;
			}

			this_code_in_onto = Predicate.user_onto
					+ Translit.toTranslit(code).replace(' ', '_').replace('\'', 'j').replace('№', 'N').replace(',', '_').replace('(', '_')
							.replace(')', '_').replace('.', '_');

		}

		old_code__new_code.put(code, this_code_in_onto);

		return this_code_in_onto;
	}

	private static String get(IXMLElement el, String Name, String def_val)
	{
		String res = null;

		IXMLElement dd = el.getFirstChildNamed(Name);

		if (dd != null)
			res = dd.getContent();

		if (res == null)
			return def_val;
		else
			return res;
	}

	/**
	 * Выгружает данные орг. структуры в виде триплетов
	 */
	private static void fetchOrganization(String name_file)
	{

		try
		{

			long start = System.currentTimeMillis();

			// prepareRoles();
			// populateAdmins();

			OutputStreamWriter out = null;

			FileOutputStream fw = new FileOutputStream(pathToDump + java.io.File.separatorChar + name_file);
			out = new OutputStreamWriter(fw, "UTF8");

			OutputStreamWriter out_auth_data = null;

			FileOutputStream fw1 = new FileOutputStream(pathToDump + java.io.File.separatorChar + "auth_data.nt");
			out_auth_data = new OutputStreamWriter(fw1, "UTF8");

			List<Department> deps = organizationUtil.getDepartments();

			ArrayList<String> excludeNode = new ArrayList<String>();
			excludeNode.add("1154685117926");
			excludeNode.add("1146725963873");
			excludeNode.add("1000");
			excludeNode.add("123456");

			// находим родителей для всех подразделений
			int buCounter = 0;
			Map<String, Department> departmentsOfExtIdMap = new HashMap<String, Department>();
			// HashMap<String, ArrayList<String>> childs = new HashMap<String,
			// ArrayList<String>>();
			HashMap<String, String> childToParent = new HashMap<String, String>();
			for (Department department : deps)
			{
				if (department.getExtId().length() == 1)
					continue;

				if (excludeNode.contains(department.getExtId()) == true)
					continue;

				List<Department> childDeps = organizationUtil.getDepartmentsByParentId(department.getExtId(), "Ru");

				// ArrayList<String> breed = new ArrayList<String>();
				for (Department child : childDeps)
				{
					// breed.add(child.getInternalId());
					childToParent.put(child.getExtId(), department.getExtId());
				}
				// childs.put(department.getId(), breed);

				departmentsOfExtIdMap.put(department.getExtId(), department);
				buCounter++;
			}

			// установим для каждого из подразделений его организацию
			for (Department department : deps)
			{

				String parent = childToParent.get(department.getExtId());

				if (parent == null)
					continue;

				String up_department = null;

				while (parent != null)
				{
					up_department = parent;

					parent = childToParent.get(parent);
				}

				department.setOrganizationId(up_department);
			}

			// выгружаем штатное расписание и сотрудников
			buCounter = 0;

			writeTriplet(Predicate.f_zdb, Predicate.owl__imports, Predicate.docs19, false, out);
			writeTriplet(Predicate.f_zdb, Predicate.owl__imports, Predicate.f_swrc, false, out);
			writeTriplet(Predicate.f_zdb, Predicate.owl__imports, Predicate.gost19, false, out);
			writeTriplet(Predicate.f_zdb, Predicate.rdf__type, Predicate.owl__Ontology, false, out);

			for (Department department : deps)
			{
				if (department.getExtId().length() == 1)
				{
					System.out.println("exclude node:" + department.getExtId());
					continue;
				}

				if (excludeNode.contains(department.getExtId()) == true)
				{
					System.out.println("exclude node:" + department.getExtId());
					continue;
				}

				String parent = childToParent.get(department.getExtId());
				boolean isOrganization = false;

				if (parent == null || parent.length() == 1)
					isOrganization = true;

				if (isOrganization == false)
				{
					// String subject = blankNodePrefix + buCounter++;
					writeTriplet(Predicate.zdb + "dep_" + department.getExtId(), Predicate.rdf__type, Predicate.swrc__Department, false,
							out);
					writeTriplet(Predicate.zdb + "doc_" + department.getId(), Predicate.rdf__type, Predicate.docs19__department_card,
							false, out);
					writeTriplet(Predicate.zdb + "doc_" + department.getId(), Predicate.swrc__name, department.getName(), true, out, "ru");
					writeTriplet(Predicate.zdb + "doc_" + department.getId(), Predicate.docs19__department, Predicate.zdb + "dep_"
							+ department.getExtId(), false, out);
					writeTriplet(Predicate.zdb + "doc_" + department.getId(), Predicate.swrc__organization, Predicate.zdb + "org_"
							+ department.getOrganizationId(), false, out);

					writeTriplet(Predicate.zdb + "doc_" + department.getId(), Predicate.gost19__externalIdentifer, department.getExtId(),
							true, out);

					if (parent != null && parent.length() > 1)
					{
						writeTriplet(Predicate.zdb + "doc_" + department.getId(), Predicate.docs19__parentDepartment, Predicate.zdb
								+ "dep_" + parent, false, out);
						write_add_info_of_attribute(Predicate.zdb + "doc_" + department.getId(), Predicate.docs19__parentDepartment,
								Predicate.zdb + "dep_" + parent, Predicate.swrc__name, departmentsOfExtIdMap.get(parent).getName(), out);
					}

				}
				else
				{
					writeTriplet(Predicate.zdb + "org_" + department.getExtId(), Predicate.rdf__type, Predicate.swrc__Organization, false,
							out);
					writeTriplet(Predicate.zdb + "doc_" + department.getId(), Predicate.rdf__type, Predicate.docs19__organization_card,
							false, out);
					writeTriplet(Predicate.zdb + "doc_" + department.getId(), Predicate.gost19__externalIdentifer, department.getExtId(),
							true, out);
					writeTriplet(Predicate.zdb + "doc_" + department.getId(), Predicate.swrc__name, department.getName(), true, out, "ru");
					writeTriplet(Predicate.zdb + "doc_" + department.getId(), Predicate.swrc__organization, Predicate.zdb + "org_"
							+ department.getExtId(), false, out);

				}

				// for (String child : childs.get(department.getId()))
				// {
				// writeTriplet(department.getId(), predicates.HAS_PART,
				// newToInternalIdMap.get(child), true, out);
				// }

				// break;
			}

			out.flush();

			long end = System.currentTimeMillis();
			System.out.println("Finished in " + ((end - start) / 1000) + " s. for " + deps.size() + " departments.");
			System.out.println("Querying speed  = " + deps.size() / ((end - start) / 1000) + " deps/s");

			// Выгружаем пользователей

			start = System.currentTimeMillis();

			List<EntityType> list = organizationUtil.getUsers();
			for (EntityType userEntity : list)
			{

				// String prefix = blankNodePrefix + personCount++;

				String userId = userEntity.getUid();

				userUri__userObj.put(Predicate.zdb + "person_" + userId, userEntity);

				writeTriplet(Predicate.zdb + "person_" + userId, Predicate.rdf__type, Predicate.swrc__Person, false, out);
				writeTriplet(Predicate.zdb + "doc_" + userId, Predicate.rdf__type, Predicate.docs19__employee_card, false, out);
				writeTriplet(Predicate.zdb + "doc_" + userId, Predicate.docs19__employee, Predicate.zdb + "person_" + userId, false, out);

				String domainName = null;
				String password = null;

				for (AttributeType a : userEntity.getAttributes().getAttributeList())
				{
					if (a.getName().equalsIgnoreCase("firstNameRu"))
					{
						writeTriplet(Predicate.zdb + "doc_" + userId, Predicate.swrc__firstName, a.getValue(), true, "ru", out);
					}
					else if (a.getName().equalsIgnoreCase("firstNameEn"))
					{
						writeTriplet(Predicate.zdb + "doc_" + userId, Predicate.swrc__firstName, a.getValue(), true, "en", out);
					}
					else if (a.getName().equalsIgnoreCase("secondnameRu"))
					{
						writeTriplet(Predicate.zdb + "doc_" + userId, Predicate.gost19__middlename, a.getValue(), true, "ru", out);
					}
					else if (a.getName().equalsIgnoreCase("secondnameEn"))
					{
						writeTriplet(Predicate.zdb + "doc_" + userId, Predicate.gost19__middlename, a.getValue(), true, "en", out);
					}
					else if (a.getName().equalsIgnoreCase("surnameRu"))
					{
						writeTriplet(Predicate.zdb + "doc_" + userId, Predicate.swrc__lastName, a.getValue(), true, "ru", out);
					}
					else if (a.getName().equalsIgnoreCase("surnameEn"))
					{
						writeTriplet(Predicate.zdb + "doc_" + userId, Predicate.swrc__lastName, a.getValue(), true, "en", out);
					}
					else if (a.getName().equals("domainName"))
					{
						domainName = a.getValue();
					}
					else if (a.getName().equals("password"))
					{
						password = a.getValue();
					}
					else if (a.getName().equals("email"))
					{
						String email = a.getValue();
						if (email != null && email.length() > 0)
							writeTriplet(Predicate.zdb + "doc_" + userId, Predicate.swrc__email, a.getValue(), true, out);
					}
					else if (a.getName().equalsIgnoreCase("id"))
					{
						// writeTriplet(p.zdb + "doc_" + userId,
						// "magnet-ontology#id", a.getValue(), true, out);
					}
					else if (a.getName().equalsIgnoreCase("pid"))
					{
						// writeTriplet(p.zdb + "doc_" + userId,
						// "magnet-ontology#pid", a.getValue(), true, out);
					}
					else if (a.getName().equalsIgnoreCase("pager"))
					{
						String value = a.getValue();
						if (value != null && value.length() > 0)
							writeTriplet(Predicate.zdb + "doc_" + userId, Predicate.docs19__pager, a.getValue(), true, out);
					}
					else if (a.getName().equalsIgnoreCase("phone"))
					{
						String value = a.getValue();
						if (value != null && value.length() > 0)
							writeTriplet(Predicate.zdb + "doc_" + userId, Predicate.swrc__phone, a.getValue(), true, out);
					}
					else if (a.getName().equalsIgnoreCase("offlineDateBegin"))
					{
						// writeTriplet(p.zdb + "doc_" + userId,
						// "magnet-ontology#offlineDateBegin", a.getValue(),
						// true, out);
					}
					else if (a.getName().equalsIgnoreCase("offlineDateEnd"))
					{
						// writeTriplet(p.zdb + "doc_" + userId,
						// "magnet-ontology#offlineDateEnd", a.getValue(), true,
						// out);
					}
					else if (a.getName().equalsIgnoreCase("departmentId"))
					{
						Department department = departmentsOfExtIdMap.get(a.getValue());

						if (department == null)
							System.out.println("dep is null for user (id = " + userId + ")");
						else
						{
							writeTriplet(Predicate.zdb + "doc_" + userId, Predicate.docs19__department,
									Predicate.zdb + "dep_" + department.getExtId(), false, out);
							write_add_info_of_attribute(Predicate.zdb + "doc_" + userId, Predicate.docs19__department, Predicate.zdb
									+ "dep_" + department.getExtId(), Predicate.swrc__name, department.getName(), out);
						}

					}
					else if (a.getName().equalsIgnoreCase("mobilePrivate"))
					{
						String value = a.getValue();
						if (value != null && value.length() > 0)
							writeTriplet(Predicate.zdb + "doc_" + userId, Predicate.swrc__phone, a.getValue(), true, out);
					}
					else if (a.getName().equalsIgnoreCase("phoneExt"))
					{
						String value = a.getValue();
						if (value != null && value.length() > 0)
							writeTriplet(Predicate.zdb + "doc_" + userId, Predicate.swrc__phone, a.getValue(), true, out);
					}
					else if (a.getName().equalsIgnoreCase("mobile"))
					{
						String value = a.getValue();
						if (value != null && value.length() > 0)
							writeTriplet(Predicate.zdb + "doc_" + userId, Predicate.swrc__phone, a.getValue(), true, out);
					}
					else if (a.getName().equalsIgnoreCase("active"))
					{
						// writeTriplet(userId, "magnet-ontology#isActive",
						// a.getValue(), true, out);
					}
					else if (a.getName().equalsIgnoreCase("employeeCategoryR3"))
					{
						// writeTriplet(userId,
						// "magnet-ontology#employeeCategoryR3", a.getValue(),
						// true, out);
					}
					else if (a.getName().equalsIgnoreCase("r3_ad"))
					{
						// writeTriplet(userId, "magnet-ontology#r3_ad",
						// a.getValue(), true, out);
					}
					else if (a.getName().equalsIgnoreCase("photo"))
					{
						// writeTriplet(userId,
						// "http://swrc.ontoware.org/ontology#photo",
						// a.getValue(), true, out);
					}
					else if (a.getName().equalsIgnoreCase("photoUID"))
					{
						// writeTriplet(userId, "magnet-ontology#photoUID",
						// a.getValue(), true, out);
					}
					else if (a.getName().equalsIgnoreCase("postRu"))
					{
						// writeTriplet(userId, "magnet-ontology#onPosition",
						// a.getValue(), true, "Ru", out);
					}
					else if (a.getName().equalsIgnoreCase("postEn"))
					{
						// writeTriplet(userId, "magnet-ontology#onPosition",
						// a.getValue(), true, "En", out);
					}
					else if (a.getName().equalsIgnoreCase("administrator"))
					{
						// if (a.getValue().equals("1"))
						// {
						// a.setValue("true");
						// }
						// else if (!(a.getValue().equals("true") ||
						// a.getValue().equals("false")))
						// {
						// a.setValue("false");
						// }
						// writeTriplet(userId, p.IS_ADMIN, a.getValue(), true,
						// out);
					}
					// else if (!writeRole(prefix, a, out))
					// {
					// writeTriplet(prefix, "magnet-ontology#unknown"
					// + a.getName(), a.getValue(), true, out);
					// }
				}

				if (domainName != null && password != null)
				{
					// обяьвим этого субьекта как аутентифицируемого и добавим
					// необходимые данные, выгружаем в отдельный файл
					writeTriplet(Predicate.zdb + "person_" + userId, Predicate.rdf__type, Predicate.auth__Authenticated, false,
							out_auth_data);
					writeTriplet(Predicate.zdb + "person_" + userId, Predicate.auth__login, domainName, true, out_auth_data);
					writeTriplet(Predicate.zdb + "person_" + userId, Predicate.auth__credential, password, true, out_auth_data);
				}

			}

			end = System.currentTimeMillis();
			System.out.println("Finished in " + ((end - start) / 1000) + " s. for " + list.size() + " persons.");
			/*
			 * System.out.println("Querying speed  = " + list.size() / ((end -
			 * start + 1) / 1000) + " persons/s");
			 */

			System.out.println("-----------------------------------------");

			out.close();
			out_auth_data.close();

		} catch (Exception e)
		{

			System.out.println("Error !");

			e.printStackTrace();

			printUsage();

		}
	}

	private static void loadProperties()
	{

		try
		{
			properties.load(new FileInputStream("ba2onto.properties"));

			documentTypeId = properties.getProperty("documentTypeId", "");
			ticketId = properties.getProperty("sessionTicketId", "");
			SEARCH_URL = properties.getProperty("searchUrl", "");
			DOCUMENT_SERVICE_URL = properties.getProperty("documentsUrl", "");
			fake = new Boolean(properties.getProperty("fake", "false"));
			pathToDump = properties.getProperty("pathToDump");
			dbUser = properties.getProperty("dbUser", "ba");
			dbPassword = properties.getProperty("dbPassword", "123456");
			dbUrl = properties.getProperty("dbUrl", "localhost:3306");
			dbSuffix = properties.getProperty("dbSuffix", "");
		} catch (IOException e)
		{
			writeDefaultProperties();
		}

	}

	private static void writeDefaultProperties()
	{

		System.out.println("Writing default properties.");

		properties.setProperty("documentTypeId", "fake-type-2mn3-6n3m");
		properties.setProperty("sessionTicketId", "fake-tiket-4abe-8c5f-a30d6c251165");
		properties.setProperty("searchUrl", "http://localhost:9874/ba-server/SearchServices?wsdl");
		properties.setProperty("documentsUrl", "http://localhost:9874/ba-server/DocumentServices?wsdl");
		properties.setProperty("fake", "false");
		properties.setProperty("pathToDump", "data");
		properties.setProperty("organizationName", "OrganizationEntityService");
		properties.setProperty("organizationNameSpace", "http://organization.magnet.magnetosoft.ru/");
		properties.setProperty("organizationUrl", "http://localhost:9874/organization/OrganizationEntitySvc?wsdl");
		properties.setProperty("dbUser", "ba");
		properties.setProperty("dbPassword", "123456");
		properties.setProperty("dbUrl", "localhost:3306");
		properties.setProperty("dbSuffix", "");

		try
		{
			properties.store(new FileOutputStream("ba2onto.properties"), null);
		} catch (IOException e)
		{
		}
	}

	private static void printUsage()
	{
		System.out
				.println("Usage  : java -cp ba2onto.jar org.gost19.ba2onto.Fetcher [ fetchType(directive|organization) [ docType [ pathToDump [ ticketId [ searchServicesWsdl [ searchQname [ docUrl [ fake(if exists => true) ] ] ] ] ] ]");
		System.out.println("Example: java -cp ba2onto.jar org.gost19.ba2onto.Fetcher " + documentTypeId + " " + pathToDump + " " + ticketId
				+ " " + SEARCH_URL + " " + DOCUMENT_SERVICE_URL);
	}

	/**
	 * Записывает триплет с заданным субъектом, предикатом и объектом в заданный
	 * BufferedWriter, с учетом локали
	 */
	private static void writeTriplet(String subject, String predicate, String object, boolean isObjectLiteral, String locale,
			OutputStreamWriter bw) throws IOException
	{

		if (locale != null && locale.length() > 0)
		{
			writeTriplet(subject, predicate, object + "@" + locale.toLowerCase(), isObjectLiteral, bw, null);
		}
		else
		{
			writeTriplet(subject, predicate, object, isObjectLiteral, bw, null);
		}
	}

	private static void writeTriplet(String subject, String predicate, String object, boolean isObjectLiteral, OutputStreamWriter bw,
			String lang) throws IOException
	{
		_writeTriplet(subject, predicate, object, isObjectLiteral, bw, lang);
	}

	private static void writeTriplet(String subject, String predicate, String object, boolean isObjectLiteral, OutputStreamWriter bw)
			throws IOException
	{
		_writeTriplet(subject, predicate, object, isObjectLiteral, bw, null);
	}

	/**
	 * Записывает триплет с заданным субъектом, предикатом и объектом в заданный
	 * BufferedWriter без учета локали
	 */
	private static void _writeTriplet(String subject, String predicate, String object, boolean isObjectLiteral, OutputStreamWriter bw,
			String lang) throws IOException
	{
		if (object != null && object.length() > 0)
		{
			StringBuilder builder = new StringBuilder();

			// ///////// SUBJECT

			if (subject.indexOf("_:") < 0)
				builder.append("<");

			builder.append(subject.trim());

			if (subject.indexOf("_:") < 0)
				builder.append(">");

			// ///////// PREDICATE

			builder.append(" <");
			builder.append(predicate.trim());
			builder.append("> ");

			// ///////// OBJECT

			if (isObjectLiteral)
			{
				builder.append("\"");
			}
			else
			{
				if (object.indexOf("_:") < 0)
					builder.append("<");
			}

			String obj = new String(object.trim().getBytes(), "UTF-8");

			builder.append(escape(obj));

			if (isObjectLiteral)
			{
				builder.append("\"");
				if (lang != null)
					builder.append("@" + lang);
				// builder.append("^^<" + p.xsd__string + ">");

			}
			else
			{
				if (object.indexOf("_:") < 0)
					builder.append(">");
			}

			builder.append(" .\n");

			if (fake)
			{
				System.out.println(builder.toString());
			}
			else
			{
				bw.write(builder.toString());
			}
		}
		else
		{
			System.out.println("skip fact <" + subject + "><" + predicate + "><>");
		}
	}

	public static String escape(String string)
	{

		if (string != null)
		{
			StringBuilder sb = new StringBuilder();

			char c = ' ';

			for (int i = 0; i < string.length(); i++)
			{

				c = string.charAt(i);

				if (c == '\n')
				{
					sb.append("\\n");
				}
				else if (c == '\r')
				{
					sb.append("\\r");
				}
				else if (c == '\\')
				{
					sb.append("\\\\");
				}
				else if (c == '"')
				{
					sb.append("\\\"");
				}
				else if (c == '\t')
				{
					sb.append("\\t");
				}
				else
				{
					sb.append(c);
				}
			}
			return sb.toString();
		}
		else
		{
			return "";
		}
	}

	private static void write_add_info_of_attribute(String subject, String predicate, String object, String addInfo_predicate,
			String addInfo_value, OutputStreamWriter bw) throws Exception
	{
		String addinfo_subject = subject + "_add_info_" + System.currentTimeMillis();

		writeTriplet(addinfo_subject, Predicate.rdf__type, Predicate.rdf__Statement, false, bw);
		writeTriplet(addinfo_subject, Predicate.rdf__subject, subject, false, bw);
		writeTriplet(addinfo_subject, Predicate.rdf__predicate, predicate, false, bw);
		writeTriplet(addinfo_subject, Predicate.rdf__object, object, false, bw);
		writeTriplet(addinfo_subject, addInfo_predicate, addInfo_value, true, bw);

	}

}
