package org.gost19.ba2onto;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.datatype.XMLGregorianCalendar;

import magnetico.objects.organization.Department;
import magnetico.ws.document.DocumentTemplateType;
import magnetico.ws.document.DocumentTemplateType.Attributes;
import magnetico.ws.document.AttributeLinkType;
import magnetico.ws.document.TypeAttributeType;
import magnetico.ws.organization.AttributeType;
import magnetico.ws.organization.EntityType;

public class Fetcher
{

	private static String documentTypeId;
	private static String ticketId;
	private static String SEARCH_URL;
	// private static QName SEARCH_QNAME = new
	// QName("http://search.bigarchive.magnetosoft.ru/", "SearchService");
	private static String DOCUMENT_URL;
	private static String pathToDump;
	private static boolean fake = false;
	private static Properties properties = new Properties();
	// private static ArrayList<String> roles = new ArrayList<String>();
	private static ArrayList<String> admins = new ArrayList<String>();
	private static String dbUser;
	private static String dbPassword;
	private static String dbUrl;
	private static String dbSuffix;

	public static void main(String[] args)
	{

		loadProperties();

		if (args.length == 1)
		{

			if (args[0].equals("organization"))
			{
				fetchOrganization(args[0] + ".nt");
			}
			else if (args[0].equals("doc"))
			{
				// fetchAllDocuments(args[0] + ".nt");
			}
			else if (args[0].equals("document_types"))
			{
				fetchDocumentTypes(args[0] + ".nt");
			}
			else if (args[0].equals("att"))
			{
				// fetchAttachments();
			}
			else if (args[0].equals("auth"))
			{
				// fetchAuthorizationData(args[0] + ".nt");
			}

		}

	}

	/**
	 * Выгружает данные структуры документов в виде пользовательских онтологий
	 * (пример: onto/user-onto.n3)
	 */

	private static void fetchDocumentTypes(String name_file)
	{

		try
		{

			long fetchStart = System.nanoTime();

			OutputStreamWriter out = null;
			if (!fake)
			{
				FileOutputStream fw = new FileOutputStream(pathToDump + java.io.File.separatorChar + name_file);
				out = new OutputStreamWriter(fw, "UTF8");
			}

			List<DocumentTemplateType> types = DocumentUtil.getInstance().listDocumentTypes(DOCUMENT_URL, ticketId);

			System.out.println("Got " + types.size() + " docTypes");

			for (DocumentTemplateType documentTypeType : types)
			{
				
				String authorId = documentTypeType.getAuthorId();
				XMLGregorianCalendar dateCreated = documentTypeType.getDateCreated();
				String id = documentTypeType.getId();
				XMLGregorianCalendar lastModifiedTime = documentTypeType.getLastModifiedTime();
				String name = documentTypeType.getName();
				String systemInformation = documentTypeType.getSystemInformation();

				String activeStatusLabel = "";
				if (documentTypeType.isActive() == false)
					activeStatusLabel = "удален";
				
				String draftStatusLabel = "";
				if (documentTypeType.isInDraftState() == true)
					draftStatusLabel = "черновик";
				
				System.out.println(String.format("\n%s:[%s] %s %s", documentTypeType.getId(), name, activeStatusLabel, draftStatusLabel));				
				
				
				String[] si_elements = null;

				if (systemInformation != null)
					si_elements = systemInformation.split(";");

				List<magnetico.ws.document.AttributeType> att_list = null;

				try
				{
					DocumentTemplateType doc_template = DocumentUtil.getInstance().getDocumentTemplate(DOCUMENT_URL, id, ticketId);

					Attributes attributes = doc_template.getAttributes();

					att_list = attributes.getAttributes();
				} catch (Exception ex)
				{

				}
				
//				<http://user-onto.org#internal_memo> <http://www.w3.org/2000/01/rdf-schema#subClassOf> <http://gost19.org/base#Document> .				
				writeTriplet(Predicate.user_onto + id, Predicate.rdfs__subClassOf, Predicate.docs19__Document, false, out);
				
//				<http://user-onto.org#internal_memo> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/2000/01/rdf-schema#Class> .				
				writeTriplet(Predicate.user_onto + id, Predicate.rdf__type, Predicate.rdfs__Class, false, out);
				
				int ii = 0;
				
				if (att_list != null)
					for (magnetico.ws.document.AttributeType att_list_element : att_list)
					{
						ii++;
						
//						_:AX2dX3eaab3feX3aX12b3991c651X3aXX2dX79a6 <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/2002/07/owl#Restriction> .
						writeTriplet("_:" + id + "_" + ii, Predicate.rdf__type, Predicate.owl__Restriction, false, out);
						
						String att_name = att_list_element.getName();
						String sysinfo = att_list_element.getSystemInformation();
						String code = att_list_element.getCode();
						String descr = att_list_element.getDescription();
						
						String multi_select_label = "";
						String obligatory_label = "";
						
						if (att_list_element.isMultiSelect())
							multi_select_label = "[множественное]";
						
						if (att_list_element.isObligatory())
							obligatory_label = "[обязательное]";							
						
						att_list_element.getDateCreated();
						
						TypeAttributeType type = att_list_element.getType();
						
						String typeLabel = null;
						
						if (type == TypeAttributeType.BOOLEAN)
						{							
							typeLabel = "boolean";
						}
						else if (type == TypeAttributeType.DATE)
						{
							typeLabel = "date";							
						}
						else if (type == TypeAttributeType.DATEINTERVAL)
						{
							typeLabel = "date_interval";							
						}
						else if (type == TypeAttributeType.DICTIONARY)
						{
							typeLabel = "dictionary";							
						}
						else if (type == TypeAttributeType.FILE)
						{
							typeLabel = "attachment";							
						}
						else if (type == TypeAttributeType.LINK)
						{
							typeLabel = "document_link";							
						}
						else if (type == TypeAttributeType.NUMBER)
						{
							typeLabel = "number";							
						}
						else if (type == TypeAttributeType.ORGANIZATION)
						{
							typeLabel = "organization";							
						}
						else if (type == TypeAttributeType.TEXT)
						{
							typeLabel = "text";							
						}

						if (sysinfo != null)
						{
							System.out.println("sysinfo="+sysinfo);
						}

						System.out.println("\n	att_name=[" + att_name + "]:" + typeLabel + " " + obligatory_label + " " + multi_select_label);
						System.out.println("	code=[" + code + "]");
						System.out.println("	description=[" + descr + "]");
						att_list_element.isMultiSelect();
					}
				
			}

			if (!fake)
			{
				out.close();
			}

			System.out.println("TOTAL: Finished in " + ((System.nanoTime() - fetchStart) / 1000000000.0) + " s. for " + types.size()
					+ " docs.");

			System.out.println("TOTAL: Averall extracting speed  = " + types.size() / ((System.nanoTime() - fetchStart) / 1000000000.0)
					+ " docs/s");

		} catch (Exception ex)
		{
			ex.printStackTrace();
		}

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

			if (!fake)
			{
				FileOutputStream fw = new FileOutputStream(pathToDump + java.io.File.separatorChar + name_file);
				out = new OutputStreamWriter(fw, "UTF8");
			}

			OrganizationUtil organizationUtil = new OrganizationUtil(properties.getProperty("organizationUrl"),
					properties.getProperty("organizationNameSpace"), properties.getProperty("organizationName"));

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

				writeTriplet(Predicate.zdb + "person_" + userId, Predicate.rdf__type, Predicate.swrc__Person, false, out);
				writeTriplet(Predicate.zdb + "doc_" + userId, Predicate.rdf__type, Predicate.docs19__employee_card, false, out);
				writeTriplet(Predicate.zdb + "doc_" + userId, Predicate.docs19__employee, Predicate.zdb + "person_" + userId, false, out);

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
						//
						// writeTriplet(userId, p.LOGIN_NAME, a.getValue(),
						// true, out);
						//
						// if (admins.contains(a.getValue()))
						// {
						// writeTriplet(userId, "magnet-ontology#isAdmin",
						// "true", true, out);
						// }

					}
					else if (a.getName().equals("email"))
					{
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
						writeTriplet(Predicate.zdb + "doc_" + userId, Predicate.docs19__pager, a.getValue(), true, out);
					}
					else if (a.getName().equalsIgnoreCase("phone"))
					{
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
						writeTriplet(Predicate.zdb + "doc_" + userId, Predicate.swrc__phone, a.getValue(), true, out);
					}
					else if (a.getName().equalsIgnoreCase("phoneExt"))
					{
						writeTriplet(Predicate.zdb + "doc_" + userId, Predicate.swrc__phone, a.getValue(), true, out);
					}
					else if (a.getName().equalsIgnoreCase("mobile"))
					{
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
					// writeTriplet(prefix, "magnet-ontology#chunknown"
					// + a.getName(), a.getValue(), true, out);
					// }
				}
			}

			end = System.currentTimeMillis();
			System.out.println("Finished in " + ((end - start) / 1000) + " s. for " + list.size() + " persons.");
			/*
			 * System.out.println("Querying speed  = " + list.size() / ((end -
			 * start + 1) / 1000) + " persons/s");
			 */

			System.out.println("-----------------------------------------");

			if (!fake)
			{
				out.close();
			}

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
			DOCUMENT_URL = properties.getProperty("documentsUrl", "");
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
				+ " " + SEARCH_URL + " " + DOCUMENT_URL);
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
			
			if (subject.indexOf("_:") < 0)
				builder.append("<");
				
			builder.append(subject.trim());

			if (subject.indexOf("_:") < 0)
			builder.append(">");
			
			
			builder.append (" <");
			builder.append(predicate.trim());
			builder.append("> ");

			if (isObjectLiteral)
			{
				builder.append("\"");
			}
			else
			{
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
			System.out.println("Error! object = null. subject = " + subject + "; predicate = " + predicate);
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
