package sm.tools.ba2veda;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import ru.mndsc.bigarch.helpers.LocaledValue;
import ru.mndsc.bigarchive.server.kernel.document.beans.XmlAttribute;
import ru.mndsc.bigarchive.server.kernel.document.beans.XmlDocument;
import ru.mndsc.objects.organization.Department;
import ru.mndsc.objects.organization.User;
import sm.tools.veda_client.Individual;
import sm.tools.veda_client.Resource;
import sm.tools.veda_client.Resources;
import sm.tools.veda_client.Type;
import sm.tools.veda_client.VedaConnection;
import sm.tools.veda_client.util;
import sm.tools.ba2veda.Translit;

public abstract class Ba2VedaTransform
{
	private static List<Ba2VedaTransform> pool = new ArrayList<Ba2VedaTransform>();
	private static HashMap<String, String> prepared_ids;
	public static Connection transfer_info_conn;
	public static Statement st = null;
	public static Replacer st_replacer;
	public static VedaConnection st_veda;
	public static BaSystem st_ba;
	public static HashMap<String, String> st_types_map;
	static int count_get;
	static int count_put;
	static boolean is_enable_store = true;
	protected static int assignedSubsystems = 0;
	protected static List<String> classRestrictions = new ArrayList<String>();

	public static String employee_prefix = "d:mondi_employee_";
	public static String appointment_prefix = "d:mondi_appointment_";
	public static String department_prefix = "mondi_department";
	public static String stand_prefix = "d:mondi_";
	public static boolean is_mondi = true;

	public static void config_restrictions(String[] restrictions)
	{
		classRestrictions.clear();
		for (int i = 0; i < restrictions.length; i++)
			classRestrictions.add(restrictions[i]);
	}

	public static void set_subsystems(int subsystems)
	{
		assignedSubsystems = subsystems;
	}

	public static long get_count_of_queue(String queue_name)
	{
		long res = 9999999999L;
		try
		{
			Individual queue_state = st_veda.getIndividual("srv:queue-state-" + queue_name);

			if (queue_state != null)
			{
				long cc = Long.parseLong(queue_state.getValue("srv:current_count"));
				long tc = Long.parseLong(queue_state.getValue("srv:total_count"));

				return tc - cc;
			}
			return res;
		} catch (Exception ex)
		{
			return -1;
		}
	}

	public static int putIndividual(int level, Individual indv, String ba_doc_id) throws Exception
	{
		if (is_enable_store == false)
			return 200;

		if (classRestrictions.size() > 0)
		{
			String rdfType = indv.getResources("rdf:type").resources.get(0).getData();
			if (!classRestrictions.contains(rdfType))
			{
				System.out.printf("[%s] rdf:type %s is not permitted, cotinue\n", indv.getUri(), rdfType);
				return 200;
			}
		}

		//Resources types = indv.getResources("rdf:type");

		boolean is_store = true;

		// boolean is_store = false;
		// if (types != null)
		// {
		// for (Resource rs : types.resources)
		// {
		// if (rs.getData().equals("v-s:ContractorProfileFile") || rs.getData().equals("v-s:File"))
		// {
		// is_store = true;
		// break;
		// }
		// }
		// }

		// ba_doc_id = null;
		if (is_store)
		{
			if (get_count_of_queue("fulltext_indexer0") > 1000 || get_count_of_queue("fanout_sql_np0") > 1000
					|| get_count_of_queue("scripts_main0") > 100)
			{
				System.out.println("Server overload, sleep 20s");
				Thread.currentThread().sleep(20000);
			}

			if (ba_doc_id != null)
			{
				List<Right> rights = st_ba.get_rights_of_doc(ba_doc_id);

				for (Right right : rights)
				{
					String veda_user_uri = null;

					String link = right.user_id;
					String new_link = null;

					HashMap<String, Resource> rpls = st_replacer.get_replace(null, link, null);

					if (rpls != null)
						veda_user_uri = rpls.get("*").getData();
					else
					{
						new_link = findAppointmentFromVeda(level, link);
						if (new_link != null)
							veda_user_uri = new_link;
						else
							System.out.println("ERR: fail prepare : " + link);
					}

					if (veda_user_uri != null && veda_user_uri.indexOf("dismissed") < 0)
					{
						Individual app = st_veda.getIndividual(veda_user_uri);

						if (app != null)
						{
							Resources occup = app.getResources("v-s:occupation");

							String to = null;

							if (occup != null && occup.resources.size() > 0)
								to = occup.resources.get(0).getData();

							if (to != null)
							{
								String right_uri = util.get_hashed_uri(indv.getUri().hashCode() + "_" + to.hashCode() + "_prm");

								if (veda.getIndividual(right_uri) == null)
								{
									Individual new_permission = new Individual();
									new_permission.setUri(right_uri);
									new_permission.addProperty("rdf:type", new Resource("v-s:PermissionStatement", Type._Uri));
									new_permission.addProperty("v-s:permissionSubject", new Resource(to, Type._Uri));
									new_permission.addProperty("v-s:permissionObject", new Resource(indv.getUri(), Type._Uri));

									if (right.isCreate == true)
										new_permission.addProperty("v-s:canCreate", new Resource("true", Type._Bool));
									if (right.isRead == true)
										new_permission.addProperty("v-s:canRead", new Resource("true", Type._Bool));
									if (right.isUpdate == true)
										new_permission.addProperty("v-s:canUpdate", new Resource("true", Type._Bool));
									if (right.isDelete == true)
										new_permission.addProperty("v-s:canDelete", new Resource("true", Type._Bool));

									st_veda.putIndividual(new_permission, true, assignedSubsystems);
									System.out.println("ADD RIGHT:" + new_permission.getUri() + ", TO:" + indv.getUri());
								}
							} else
							{
								System.out.println("ERR: fail add right, not found occupation in appointment : " + veda_user_uri);
							}
						} else
						{
							System.out.println("ERR: fail add right, not found appointment : " + veda_user_uri);
						}

					}
					Thread.currentThread().sleep(10);
				}
			}

			System.out.println("PUT INDIVIDUAL: " + indv.getUri());

			int res = -1;
			while (res == -1)
			{
				res = st_veda.putIndividual(indv, true, assignedSubsystems);
			}

			return res;
		} else
			return 200;
	}

	static
	{
		prepared_ids = new HashMap<String, String>();
		st_types_map = new HashMap<String, String>();

		try
		{
			Class.forName("org.h2.Driver").newInstance();
			transfer_info_conn = DriverManager.getConnection("jdbc:h2:./transfer_info", "sa", "");
			st = transfer_info_conn.createStatement();
			st.execute(
					"CREATE TABLE IF NOT EXISTS PREPARED_IDS(src_id VARCHAR(255), dest_id VARCHAR(255), timestamp BIGINT(20), dest_type VARCHAR(255))");

			st.execute("CREATE INDEX IF NOT EXISTS t1b ON PREPARED_IDS(src_id)");
			st.execute("CREATE INDEX IF NOT EXISTS t2b ON PREPARED_IDS(dest_id)");
		} catch (Exception ex)
		{
			System.out.println(ex);
		}
	}

	protected String from_class;
	protected String to_class;
	protected static VedaConnection veda;
	protected static BaSystem ba;
	protected HashMap<String, String> fields_map;
	protected HashMap<String, Resource> default_values_map;
	protected HashMap<String, String> types_map;
	private static Replacer replacer;

	public static String calculateCRC(String input)
	{
		byte bytes[] = input.getBytes();
		Checksum checksum = new CRC32();
		checksum.update(bytes, 0, bytes.length);
		long checksumValue = checksum.getValue();
		return Long.toHexString(checksumValue);
	}

	public static Ba2VedaTransform get_transformer(String from, String to)
	{
		for (Ba2VedaTransform el : pool)
		{
			if (el.from_class.equals(from) && el.to_class.equals(to))
				return el;
		}

		return null;
	}

	protected Ba2VedaTransform(BaSystem _ba, VedaConnection _veda, Replacer _replacer, String _from_class, String _to_class)
	{
		from_class = _from_class;
		to_class = _to_class;

		st_types_map.put(from_class, to_class);

		veda = _veda;
		ba = _ba;
		replacer = _replacer;
		fields_map = new HashMap<String, String>();
		default_values_map = new HashMap<String, Resource>();
		inital_set();

		pool.add(this);

		types_map = new HashMap<String, String>();
	}

	public abstract void inital_set();

	public String get_vedaType_of_baType(String ba_type)
	{
		String res;
		res = types_map.get(ba_type);
		if (res == null)
			return res;
		else
			return res;
	}

	public List<Individual> transform(int level, XmlDocument doc, String ba_id, String parent_veda_id, String parent_ba_doc_id, String path)
			throws Exception
	{
		String uri = prepare_uri(ba_id);
		List<Individual> res = new ArrayList<Individual>();

		Individual new_individual = new Individual();
		new_individual.setUri(uri);

		set_basic_fields(0, new_individual, doc);

		new_individual.addProperty("rdf:type", to_class, Type._Uri);

		List<XmlAttribute> atts = doc.getAttributes();
		for (XmlAttribute att : atts)
		{
			String code = att.getCode();

			if (code.equals("action"))
				code.length();

			String predicate = fields_map.get(code);

			if (predicate != null)
			{
				Resources rss = ba_field_to_veda(level, att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_id, true);
				new_individual.addProperty(predicate, rss);
			} else
			{

			}
		}
		res.add(new_individual);
		return res;
	}

	public Resource r_assemble(List<Object> args, String lang)
	{
		int cc = 0;
		int cc_l = 0;

		String data = "";

		for (Object obj : args)
		{
			if (obj instanceof HashMap)
			{
				@SuppressWarnings("unchecked")
				HashMap<String, String> in_hm = (HashMap<String, String>) obj;

				if (in_hm.get(lang) != null)
				{
					data += in_hm.get(lang);
					cc++;
					cc_l++;
				} else if (lang.equals("NONE") == false)
				{
					data += in_hm.get("NONE");
					cc++;
				}

			} else if (obj instanceof java.lang.String)
			{
				String in_str = (String) obj;
				data += in_str;
			}
		}

		if (cc == 0 || cc_l == 0)
			return null;

		return new Resource(data, Type._String, lang);
	}

	public Resources rs_assemble(Object[] args, String[] langs_out)
	{
		Resources rss = new Resources();
		Resource rc;

		List<Object> arg_out = new ArrayList<Object>();
		for (Object obj : args)
		{
			if (obj instanceof sm.tools.veda_client.Resources)
			{
				Resources in_rss = (Resources) obj;

				arg_out.add(get_lang_str_as_map(in_rss));
			} else if (obj instanceof java.lang.String)
			{
				String in_str = (String) obj;
				arg_out.add(in_str);
			}
		}

		for (String lang : langs_out)
		{
			rc = r_assemble(arg_out, lang);
			if (rc != null)
				rss.add(rc);
		}
		return rss;
	}

	public HashMap<String, String> get_lang_str_as_map(Resources rss)
	{
		HashMap<String, String> res = new HashMap<String, String>();
		for (Resource el : rss.resources)
		{
			if (el.getData() == null)
				continue;

			if (el.getType() == Type._Datetime)
			{
				String tmp = el.getData();
				tmp = tmp.replace('T', ' ').replace('Z', ' ').substring(0, 10);
				res.put(el.getLang(), tmp);
			} else
				res.put(el.getLang(), el.getData());
		}

		return res;
	}

	public String get_OrgUri_of_inn(String inn) throws Exception
	{
		String docId;

		if (inn != null && inn.length() > 0)
		{
			inn = inn.trim();

			if (util.isNumeric(inn)/* && inn.length() == 10 || inn.length() == 12 */)
				docId = "d:org_RU" + inn;
			else
			{
				docId = "d:org_" + inn;

				if (inn.indexOf('/') >= 0 || inn.indexOf(' ') >= 0 || inn.indexOf('\\') >= 0 || inn.indexOf('+') >= 0 || inn.indexOf('"') >= 0
						|| inn.indexOf('\'') >= 0 || inn.indexOf('	') >= 0)
				{
					System.out.println("WARN:invalid inn:" + inn);

					docId = "d:org_" + inn.replace('/', '_').replace(' ', '_').replace('\\', '_').replace('+', '_').replace('"', '_')
							.replace('\'', '_').replace('	', '_');
				}
			}
		} else
		{
			return null;
		}

		Individual inn_indv = veda.getIndividual(docId);

		if (inn_indv != null)
			return docId;

		String[] res = veda.query("'rdf:type' == 'v-s:Organization' && 'v-s:taxId' == '" + inn + "'");
		if (res != null && res.length > 0)
		{
			return res[0];
		}

		return null;
	}

	public String prepare_uri(String docId)
	{
		HashMap<String, Resource> rules_of_id = replacer.get_replace("@", docId, null);
		if (rules_of_id != null)
			docId = rules_of_id.get("@").getData();

		docId = "d:" + docId;
		return docId;
	}

	public String prepare_link(String docId)
	{
		String res = null;
		HashMap<String, Resource> rpls = replacer.get_replace(null, docId, null);
		if (rpls != null)
			res = rpls.get("*").getData();

		return res;
	}

	public void set_basic_fields(int level, Individual new_individual, XmlDocument doc) throws Exception
	{
		if (to_class != null && to_class.length() > 0)
			new_individual.addProperty("rdf:type", to_class, Type._Uri);

		if (doc.getDateCreated() != null)
			new_individual.addProperty("v-s:created", doc.getDateCreated());

		if (doc.getDateLastModified() != null)
			new_individual.addProperty("v-s:edited", doc.getDateLastModified());

		String editor_ap;

		HashMap<String, Resource> rpls = replacer.get_replace(null, doc.getLastEditorId(), null);
		if (rpls != null)
			editor_ap = rpls.get("*").getData();
		else
			editor_ap = findAppointmentFromVeda(level, doc.getLastEditorId());

		if (editor_ap != null)
			new_individual.addProperty("v-s:lastEditor", editor_ap, Type._Uri);

		if (doc.isActive() == false)
			new_individual.addProperty("v-s:deleted", "true", Type._Bool);

		String author_ap;
		rpls = replacer.get_replace(null, doc.getAuthorId(), null);
		if (rpls != null)
			author_ap = rpls.get("*").getData();
		else
			author_ap = findAppointmentFromVeda(level, doc.getAuthorId());

		if (author_ap != null && author_ap.length() > 0)
			new_individual.addProperty("v-s:creator", author_ap, Type._Uri);

		for (String pp : default_values_map.keySet())
		{
			Resource rc = default_values_map.get(pp);
			new_individual.addProperty(pp, rc);
		}

		if (!doc.isActive())
			new_individual.addProperty("v-s:deleted", new Resource(true, Type._Bool));
	}

	private static String findAppointmentFromVeda(int level, String person_id) throws Exception
	{
		String res_ap = null;

		if (person_id == null)
			return null;

		//Individual unit1 = st_veda.getIndividual("d:" + person_id);

		//if (unit1 != null)
		//	return unit1.getUri();

		//		if (person_id.equals("47a03c80-5f04-425b-80d7-1562bd4a0a6c"))
		//			person_id.length();

		ArrayList<String> arg1 = new ArrayList<String>();
		arg1.add(person_id);
		List<User> uus = st_ba.pacahon.getUsersByUids(arg1, "RU", "getAppointmentFromVeda");

		if (uus.size() < 1)
			return null;

		User uu = uus.get(0);

		if (uu == null)
			return null;

		long pid = uu.getTabNomer();

		List<String> res = new ArrayList<String>();

		String str_pid = null;
		boolean repeat = true;

		if (pid > 0)
		{
			str_pid = pid + "";

			if (str_pid.length() < 8)
				str_pid = "00000000".substring(0, 8 - ("" + pid).length()) + pid;

			//System.out.println("'rdf:type'=='v-s:Appointment' && 'v-s:employee'=='" + employee_prefix + str_pid + "'");
			while (repeat)
			{
				repeat = false;
				try
				{
					String[] queryResult = st_veda.query("'rdf:type'=='v-s:Appointment' && 'v-s:employee'=='" + employee_prefix + str_pid + "'");
					res.addAll(new ArrayList<String>(Arrays.asList(queryResult)));
					repeat = false;
				} catch (Exception e)
				{
					System.err.println("Err doing appointment query, repeat later");
					repeat = true;
					Thread.sleep(20000);
				}
			}
		} else
		{
			// System.out.println("user " + person_id + ", not content tabnumber");
			if (is_mondi == false)
			{
			} else
			{
				return "d:" + person_id;
			}
		}

		while (repeat)
		{
			repeat = false;
			try
			{
				String[] queryResult = st_veda
						.query("'v-s:deleted' == true && 'rdf:type'=='v-s:Appointment' && 'v-s:employee'=='" + employee_prefix + str_pid + "'");
				res.addAll(new ArrayList<String>(Arrays.asList(queryResult)));
			} catch (Exception e)
			{
				System.err.println("Err doing appointment query, repeat later");
				repeat = true;
				Thread.sleep(20000);
			}
		}

		//		res.addAll(new ArrayList<String>(Arrays.asList(
		//				st_veda.query(("'v-s:deleted' == true && 'rdf:type'=='v-s:Appointment' && 'v-s:employee'=='"+employee_prefix+" + str_pid + "'")))));

		if (res.size() == 0)
		{
			// поиск пользователя по доменному имени
			String domain_name = uu.getDomainName();
			if (domain_name != null && domain_name.length() > 0)
			{
				domain_name = domain_name.replace("-", " +");

				res = new ArrayList<String>(Arrays.asList(st_veda.query("'v-s:login'=='" + domain_name + "'")));
				if (res.size() == 0)
				{
					res = new ArrayList<String>(Arrays.asList(st_veda.query("'v-s:deleted' == true && 'v-s:login'=='" + domain_name + "'")));
					if (res.size() == 0)
					{
						if (is_mondi == false)
						{
							return createUserToVeda(level, uu, null, null, uu.isActive());
						} else
							return createUserToVeda(level, uu, "dismissed", stand_prefix + "position_dismissed", false);
					}

				} else
				{
					String account_id = (String) res.get(0);

					Individual jsno_account = st_veda.getIndividual(account_id);

					if (jsno_account != null)
					{
						String owner = jsno_account.getValue("v-s:owner");

						if (owner != null)
						{
							Individual jsno_empl = st_veda.getIndividual(owner);

							if (jsno_empl != null)
							{
								String appnt = jsno_empl.getValue("v-s:hasAppointment");

								if (appnt != null)
									return appnt;
							}
						}
					}
				}

			}

		}

		if (res != null)
		{
			for (int i = 0; i < res.size(); i++)
			{
				String id_ap = (String) res.get(i);

				Individual unit = st_veda.getIndividual(id_ap);

				if (is_mondi == true)
				{
					if (unit != null)
					{
						Resources origin = unit.getResources("v-s:origin");
						if (origin != null && origin.resources.size() > 0)
						{
							if (origin.resources.get(0).getData().equals("SAP HR") == false)
							{
								continue;
							}
						} else
							continue;
					} else
						continue;
				}

				res_ap = id_ap;
				break;
			}
		}

		if (res_ap == null)
		{
			for (int i = 0; i < res.size(); i++)
			{
				String id_ap = (String) res.get(i);

				if (id_ap.indexOf("dismissed") > 0)
				{
					res_ap = id_ap;
					break;
				}
			}
		}

		if (res_ap == null)
			return createUserToVeda(level, uu, "dismissed", stand_prefix + "position_dismissed", false);

		return res_ap;
	}

	private static String createUserToVeda(int level, User user, String in_position_tab_num, String in_position_id, boolean isActive) throws Exception
	{
		int res;

		long pid = user.getTabNomer();
		String person_id;
		String appointment_id;

		if ((user.getLogin() == null || user.getLogin().length() < 1) && user.getTabNomer() == 0)
		{
			System.out.println("WARN: user is not identified " + user.getAttributes());
		}

		if (pid > 0)
		{
			String str_pid = pid + "";

			if (str_pid.length() < 8)
				str_pid = "00000000".substring(0, 8 - ("" + pid).length()) + pid;

			person_id = "" + employee_prefix + str_pid;
			appointment_id = appointment_prefix + str_pid + "_" + in_position_tab_num;
		} else
		{
			person_id = "d:employee_" + user.getId();
			appointment_id = "d:appointment_" + user.getId();
		}

		String account_id = user.getId();

		if (is_mondi == false)
		{
			appointment_id = "d:" + account_id.replace("zdb:doc_", "");
			String login = user.getLogin();
			if (login != null)
				account_id = "d:account_" + Translit.cyr2lat(user.getLogin());
			else
				account_id = "d:account_" + user.getId();
		} else
		{
			account_id = "d:" + account_id.replace("zdb:doc_", "");
		}

		String FIOD_RU;
		String FIOD_EN;
		String position_id;

		if (in_position_id == null)
		{
			position_id = "d:position-" + user.getDepartmentId().toString().toLowerCase().hashCode()
					+ new StringBuilder().append(user.getPosition("Ru")).append("").toString().toLowerCase().hashCode();
		} else
		{
			position_id = in_position_id;
		}

		System.out.println("\t" + user.getName());

		// //////////////////////////////////////////////////////////////////////////////////////
		Individual iipp = new Individual();
		iipp.addProperty("rdf:type", new Resources().add("v-s:Person", Type._Uri));

		if (!isActive)
		{
			iipp.addProperty("v-s:deleted", new Resources().add("true", Type._Bool));
		}
		user.changeLocale("Ru");
		Resource rc1 = null;
		Resource rc2 = null;
		if (user.getName() != null)
			rc1 = new Resource(user.getName(), 2, "RU");
		user.changeLocale("En");
		if (user.getName() != null)
		{
			rc2 = new Resource(user.getName(), 2, "EN");
		}
		if ((rc1 != null) || (rc2 != null))
		{
			iipp.addProperty("rdfs:label", new Resources().add(rc1).add(rc2));
		}
		iipp.addProperty("v-s:hasAccount", new Resources().add(account_id, 1));
		iipp.addProperty("v-s:lastName", new Resources().add(user.getLastName("Ru"), "RU").add(user.getLastName("En"), "EN"));
		FIOD_RU = user.getLastName("Ru");
		FIOD_EN = user.getLastName("En");
		iipp.addProperty("v-s:firstName", new Resources().add(user.getFirstName("Ru"), "RU").add(user.getFirstName("En"), "EN"));

		if (user.getFirstName("Ru") != null && user.getFirstName("Ru").length() > 0)
			FIOD_RU += " " + user.getFirstName("Ru").charAt(0) + ".";

		if (user.getFirstName("En") != null && user.getFirstName("En").length() > 0)
			FIOD_EN += " " + user.getFirstName("En").charAt(0) + ".";

		iipp.addProperty("v-s:middleName", new Resources().add(user.getMiddleName("Ru"), "RU").add(user.getMiddleName("En"), "EN"));

		if (user.getMiddleName("Ru") != null && user.getMiddleName("Ru").length() > 0)
			FIOD_RU += user.getMiddleName("Ru").charAt(0) + ".";

		if (user.getMiddleName("En") != null && user.getMiddleName("En").length() > 0)
			FIOD_EN += user.getMiddleName("En").charAt(0) + ".";

		iipp.setUri(person_id);
		res = putIndividual(level, iipp, null);
		if (res != 200)
		{
			System.out.println("ERR:" + res + "\n" + iipp);
			return null;
		}

		// //////////////////////////////////////////////////////////////////////////////////////
		Individual pozt = st_veda.getIndividual(position_id);

		if (pozt == null)
		{
			Individual ii = new Individual();

			ii.addProperty("rdf:type", new Resources().add("v-s:Position", 1));

			Resources trss = new Resources();

			if (user.getPosition("Ru") != null)
			{
				trss.add(user.getPosition("Ru"), "RU");
				FIOD_RU += " " + user.getPosition("Ru");
			}

			if (user.getPosition("En") != null)
			{
				trss.add(user.getPosition("En"), "EN");
				FIOD_EN += " " + user.getPosition("En");
			}

			ii.addProperty("rdfs:label", trss);

			ii.setUri(position_id);
			res = putIndividual(level, ii, null);
			if (res != 200)
			{
				System.out.println("ERR:" + res + "\n" + ii);
				return null;
			}
		} else
		{
			System.out.println("INFO:found position " + position_id);
		}

		// //////////////////////////////////////////////////////////////////////////////////////
		Individual iiap = new Individual();

		iiap.addProperty("rdf:type", new Resources().add("v-s:Appointment", 1));
		if (!isActive)
		{
			iiap.addProperty("v-s:deleted", new Resources().add("true", 64));
		}

		Resources trss = new Resources();
		if (FIOD_RU != null)
			trss.add(FIOD_RU, "RU");

		if (FIOD_EN != null)
			trss.add(FIOD_EN, "EN");

		iiap.addProperty("rdfs:label", trss);
		iiap.addProperty("v-s:occupation", new Resources().add(position_id, 1));
		iiap.addProperty("v-s:employee", new Resources().add(person_id, 1));

		String veda_dp_uri = prepare_org_id(user.getDepartmentId(), "", level);

		if (veda_dp_uri != null)
		{
			iiap.addProperty("v-s:parentUnit", new Resources().add(veda_dp_uri, 1));
		}

		iiap.setUri(appointment_id);
		res = putIndividual(level, iiap, null);
		if (res != 200)
		{
			System.out.println("ERR:" + res + "\n" + iiap);
			return null;
		}
		// //////////////////////////////////////////////////////////////////////////////////////
		Individual acc = new Individual();

		acc.addProperty("rdf:type", new Resources().add("v-s:Account", 1));
		if (!isActive)
		{
			acc.addProperty("v-s:deleted", new Resources().add("true", 64));
		}
		acc.addProperty("v-s:login", new Resources().add(user.getLogin(), 2));
		acc.addProperty("v-s:mailbox", new Resources().add(user.getEmail(), 2));
		acc.addProperty("v-s:owner", new Resources().add(person_id, 1));

		acc.setUri(account_id);
		res = putIndividual(level, acc, null);
		if (res != 200)
		{
			System.out.println("ERR:" + res + "\n" + acc);
			return null;
		}

		return appointment_id;
	}

	public Resources ba_field_to_veda(int level, XmlAttribute att, String veda_doc_id, String ba_doc_id, XmlDocument doc, String path,
			String parent_ba_id, String parent_veda_doc_uri, boolean is_deep) throws Exception
	{
		return ba_field_to_veda(level, att, veda_doc_id, ba_doc_id, doc, path, parent_ba_id, parent_veda_doc_uri, is_deep, true, null);
	}

	public Resources ba_field_to_veda_with_store_deep(int level, XmlAttribute att, String veda_doc_id, String ba_doc_id, XmlDocument doc, String path,
			String parent_ba_id, String parent_veda_doc_uri, boolean is_deep, boolean is_store_new_individuals) throws Exception
	{
		return ba_field_to_veda(level, att, veda_doc_id, ba_doc_id, doc, path, parent_ba_id, parent_veda_doc_uri, is_deep, is_store_new_individuals,
				null);
	}

	public Resources ba_field_to_veda(int level, XmlAttribute att, String veda_doc_id, String ba_doc_id, XmlDocument doc, String path,
			String parent_ba_id, String parent_veda_doc_uri, boolean is_deep, boolean is_store_new_individuals, List<Individual> out_indvs)
			throws Exception
	{
		Resources res = new Resources();

		String code = att.getCode();
		String type = att.getType();

		if (code.equals("decision"))
			code.length();

		if (type.equals("TEXT"))
		{
			String str = att.getTextValue();

			if (str != null && str.length() > 0)
			{
				str = str.trim();
				if (str.indexOf("@@") > 0)
				{
					LocaledValue lv;
					String ru = null;
					String en = null;
					try
					{
						lv = new LocaledValue(str);

						ru = lv.get("ru");
						en = lv.get("en");
					} catch (Exception ex)
					{
						lv = new LocaledValue(str);
					}

					if (ru != null && ru.length() > 0)
						res.add(ru, "RU");

					if (en != null && en.length() > 0)
						res.add(en, "EN");
				} else
					res.add(str, "NONE");
			}

		} else if (type.equals("DATE"))
		{
			Date date = att.getDateValue();
			if (date != null)
			{
				res.add(date);
			}
		} else if (type.equals("ORGANIZATION"))
		{
			String otype = att.getOrganizationTag();
			String link = att.getOrganizationValue();
			String new_link = null;

			new_link = prepare_org_id(link, code, level);

			if (new_link != null)
				res.add(new_link, Type._Uri);
			else
				System.out.println("ERR: fail prepare [" + otype + "]: " + link);

		} else if (type.equals("DICTIONARY") || type.equals("LINK"))
		{
			String link = null;
			String link_type = null;

			if (type.equals("DICTIONARY"))
			{
				link = att.getRecordIdValue();
				XmlDocument link_doc = ba.getDocumentOnTimestamp(link, new Date().getTime());
				if (link_doc != null)
					link_type = link_doc.getTypeId();
			} else
			{
				link = att.getLinkValue();

				XmlDocument link_doc = ba.getDocumentOnTimestamp(link, new Date().getTime());
				if (link_doc != null)
					link_type = link_doc.getTypeId();
			}

			if (link != null && link.length() > 2)
			{
				HashMap<String, Resource> rpls = replacer.get_replace(code, link, null);

				if (rpls != null)
				{
					res.add(rpls.get("*").getData(), rpls.get("*").getType());
				} else
				{
					String veda_type = get_vedaType_of_baType(link_type);

					if (veda_type != null)
					{
						ResultCode rc = new ResultCode();
						List<Individual> indvs = null;

						if (is_deep == true)
						{
							indvs = prepare_document(level + 1, link_type, veda_type, link, path + veda_doc_id, 0, 0, veda_doc_id, ba_doc_id, rc,
									true, is_store_new_individuals);
						}

						if (indvs != null && indvs.size() > 0)
						{
							for (Individual indv : indvs)
							{
								res.add(indv.getUri(), Type._Uri);
								if (out_indvs != null)
									out_indvs.add(indv);
							}
						} else
						{
							String uri = "d:" + link;
							Individual indv = veda.getIndividual(uri);

							if (indv != null)
								res.add(uri, Type._Uri);
							else
							{
								String uri1 = "d:d" + link;
								indv = veda.getIndividual(uri);

								if (indv != null)
									res.add(uri1, Type._Uri);
								else
									res.add(uri, Type._Uri);
							}
						}
					} else
					{
						String uri = "d:" + link;

						Individual indv = veda.getIndividual(uri);

						if (indv != null)
							res.add(uri, Type._Uri);
						else
						{
							String uri1 = "d:d" + link;
							indv = veda.getIndividual(uri1);

							if (indv != null)
								res.add(uri1, Type._Uri);
							else
								res.add(uri, Type._Uri);
						}

					}
				}
			}
		} else if (type.equals("NUMBER"))
		{
			String val = att.getNumberValue();
			if (val != null && val.length() > 0)
			{
				try
				{
					Integer.parseInt(val);
					res.add(val, Type._Integer);
				} catch (Exception ex)
				{
					try
					{
						Double.parseDouble(val);
						res.add(val, Type._Decimal);
					} catch (Exception ex1)
					{
						res.add(val, Type._String);
					}
				}
			}
		} else if (type.equals("FILE"))
		{
			String fileId = att.getFileValue();
			FileInfo fi = ba.getFileInfo(fileId);

			if (fi != null && doc.getDateCreated() != null)
			{
				// String path_to_files = util.date2_short_string(doc.getDateCreated()).replace('-',
				// '/');
				String file_uri = "d:" + fileId;

				// создадим описание к загруженному файлу
				Individual ff = new Individual();
				ff.addProperty("rdf:type", new Resources().add("v-s:File", Type._Uri));
				ff.addProperty("rdfs:label", new Resources().add(fi.name, Type._String));
				ff.addProperty("v-s:fileName", new Resources().add(fi.name, Type._String));
				ff.addProperty("v-s:fileSize", new Resources().add(fi.file_length + "", Type._String));
				ff.addProperty("v-s:fileUri", new Resources().add(fileId, Type._String));
				// util.serializeResources(sbf, "v-s:filePath", new Resources().add(path_to_files,
				// Type._String));
				ff.addProperty("v-s:filePath", new Resources().add(fi.file_path, Type._String));

				if (veda_doc_id != null)
					ff.addProperty("v-s:parent", new Resources().add(veda_doc_id, Type._Uri));

				String author_ap;
				HashMap<String, Resource> rpls = replacer.get_replace(null, doc.getAuthorId(), null);
				if (rpls != null)
					author_ap = rpls.get("*").getData();
				else
					author_ap = findAppointmentFromVeda(level, doc.getAuthorId());

				ff.addProperty("v-s:creator", new Resources().add(author_ap, Type._Uri));
				// util.serializeResources(sbf, "v-s:created", new
				// Resources().add(doc.getDateCreated()));
				ff.addProperty("v-s:created", new Resources().add(fi.date_create));

				ff.setUri(file_uri);

				if (is_store_new_individuals)
				{
					int rc = putIndividual(level, ff, ba_doc_id);
					if (rc != 200)
						return null;
				}

				String link = file_uri;
				if (link != null && link.length() > 3)
				{
					res.add(link, Type._Uri);
				}
			}

		}
		return res;
	}

	private static String prepare_org_id(String link, String code, int level) throws Exception
	{
		String new_link = null;
		if (link != null && link.length() > 3)
		{
			boolean is_department = false;
			boolean is_user = false;
			boolean is_organization = true;

			User uu = null;
			try
			{
				uu = ba.getPacahon().getUserByUid(link, "RU", "");
			} catch (Exception ex)
			{
			}

			Department dp = null;

			if (uu != null)
			{
				is_user = true;
				is_department = false;
				is_organization = false;
			} else
			{
				try
				{
					dp = ba.pacahon.getDepartmentByUid(link, "RU", "");
				} catch (Exception ex)
				{
				}

				if (dp != null)
				{
					is_user = false;
					is_department = true;
					is_organization = false;
				}
			}

			if (is_organization)
			{
				HashMap<String, Resource> rpls = replacer.get_replace(code, link, null);

				if (rpls != null)
					new_link = rpls.get("*").getData();

			} else if (is_department)
			{
				new_link = null;
				HashMap<String, Resource> rpls = replacer.get_replace(code, link, null);

				if (rpls != null)
				{
					new_link = rpls.get("*").getData();

					if (new_link.length() < 3)
						new_link = null;
					// String[] pnl = new_link.split("_");
					// if (pnl.length > 1)
					// dp.setInternalId(pnl[1]);
					// else
					// dp.getId();
				} else
				{
					if (dp != null)
						new_link = findOrgUnitInVeda(dp);
				}

				if (new_link == null && dp != null)
					new_link = createDepartmentToVeda(level, dp);
			} else
			{
				new_link = null;

				HashMap<String, Resource> rpls = replacer.get_replace(code, link, null);

				if (rpls != null)
					new_link = rpls.get("*").getData();
				else
					new_link = findAppointmentFromVeda(level, link);
			}
		}
		return new_link;
	}

	private static String createDepartmentToVeda(int level, Department ba_department) throws Exception
	{
		int res = 0;

		if (ba_department == null)
			return null;

		String ba_parent_id = ba_department.getPreviousId();
		String veda_parent_id = null;

		if (ba_parent_id != null)
		{
			Department ba_parent = ba.pacahon.getDepartmentByUid(ba_parent_id, "ru", "");
			if (ba_parent != null)
			{
				veda_parent_id = findOrgUnitInVeda(ba_parent);

				if (veda_parent_id == null)
					veda_parent_id = createDepartmentToVeda(level, ba_parent);
			}
		}

		String prefix = "";
		String rdf_type = "v-s:Department";

		if (ba_department.type == Department._DEPARTMENT)
		{
			prefix = department_prefix;
			rdf_type = "v-s:Department";
		} else if (ba_department.type == Department._GROUP)
		{
			prefix = "group";
			rdf_type = "v-s:OrgGroup";
		} else if (ba_department.type == Department._ORGANIZATION)
		{
			prefix = "org";
			rdf_type = "v-s:Organization";
		}

		String individualId = "d:" + prefix + "_" + ba_department.getInternalId();

		if (individualId.equals("d:org_0"))
		{
			return "cfg:org_Veda";
		}

		Individual pr = new Individual();

		pr.addProperty("rdf:type", new Resources().add(rdf_type, 1));
		pr.addProperty("v-s:origin", new Resources().add("ba2veda", 2));
		pr.addProperty("rdfs:label", new Resources().add(ba_department.getName("ru"), "RU").add(ba_department.getName("en"), "EN"));
		pr.addProperty("v-s:parentUnit", new Resources().add(veda_parent_id, 1));

		// if (dep.getLeader()!=null && ws.positions.containsKey(dep.getLeader())) {
		// util.serializeResources(department, "v-s:hasChief", new
		// Resources().add(appointment_prefix +
		// ws.positionToEmployee.get(dep.getLeader()) + "_" + dep.getLeader(), 1));
		// }

		// Короткое наименование
		// Код должности руководителя
		pr.setUri(individualId);

		res = putIndividual(level, pr, null);
		if (res != 200)
		{
			System.out.print("ERR:" + res + "\n" + pr);
			return null;
		}
		return individualId;
	}

	private static String findOrgUnitInVeda(Department ba_department) throws Exception
	{
		String veda_department_id = null;

		String pprefix = "";
		if (ba_department.type == Department._DEPARTMENT)
			pprefix = "department";
		else if (ba_department.type == Department._GROUP)
			pprefix = "group";
		else if (ba_department.type == Department._ORGANIZATION)
			pprefix = "org";

		Individual dp_veda;// = veda.getIndividual("d:" + pprefix + "_" + ba_department.getInternalId());
		//		if (dp_veda == null)
		dp_veda = veda.getIndividual(stand_prefix + pprefix + "_" + ba_department.getInternalId());

		if (dp_veda != null)
			veda_department_id = dp_veda.getUri();

		return veda_department_id;

	}

	public static List<Individual> prepare_document(int level, String from_ba_class, String to_veda_class, String docId, String path,
			long cur_id_count, long total_ids, String parent_veda_doc_id, String parent_ba_doc_id, ResultCode rc, boolean prepare_deleted,
			boolean is_store_new_individuals) throws Exception
	{
		System.out.println("prepare_document:" + from_ba_class + "->" + to_veda_class + ", ba_id=" + docId + ", prepare_deleted=" + prepare_deleted
				+ ", is_store_new_individuals=" + is_store_new_individuals);

		List<Individual> new_individuals = null;

		String ba_docId = docId;
		String veda_doc_id = prepared_ids.get(docId);
		if (veda_doc_id != null)
		{
			Individual indv = st_veda.getIndividual(veda_doc_id);
			if (indv != null)
			{
				new_individuals = new ArrayList<Individual>();

				new_individuals.add(indv);
			}
			return new_individuals;
		}

		if (path.indexOf(docId) > 0)
			return null;

		Pair<XmlDocument, Long> doc_ts = st_ba.getActualDocument(docId);
		if (doc_ts == null)
			return null;

		XmlDocument doc = doc_ts.getLeft();
		long timestamp = doc_ts.getRight();

		if (doc.isActive() == false && !prepare_deleted)
			return null;

		long prev_timestamp = -1;
		if (Fetcher.no_check_exists == false)
		{
			ResultSet sql_result;
			sql_result = st
					.executeQuery("SELECT dest_id, timestamp FROM PREPARED_IDS WHERE src_id ='" + docId + "' AND dest_type ='" + to_veda_class + "'");

			while (sql_result.next())
			{
				String info = sql_result.getString("dest_id");
				prev_timestamp = sql_result.getLong("timestamp");
				if (info != null && prev_timestamp == timestamp)
				{
					rc.code = ResultCode.AlreadyExists;

					Individual indv = new Individual();

					indv.setUri(info);
					if (new_individuals == null)
						new_individuals = new ArrayList<Individual>();

					new_individuals.add(indv);
				}
			}
			if (new_individuals != null)
				return new_individuals;

		}

		if (st_replacer.is_ignore("@", docId, null))
		{
			System.out.println("use ignore_rule: " + docId);
			return null;
		}

		Ba2VedaTransform tr = Ba2VedaTransform.get_transformer(from_ba_class, to_veda_class);

		if (tr == null)
			return null;

		new_individuals = tr.transform(level, doc, ba_docId, parent_veda_doc_id, parent_ba_doc_id, path);

		for (Individual new_individual : new_individuals)
		{
			if (Fetcher.no_check_exists == false)
				prepared_ids.put(docId, new_individual.getUri());

			if (is_store_new_individuals)
			{
				int res = putIndividual(level, new_individual, ba_docId);

				if (res != 200)
				{
					System.out.println("[" + count_get + "/" + count_put + "], [" + cur_id_count + "/" + total_ids + "/" + st_veda.count_put
							+ "] ERR: res=" + res + ", veda_type=" + to_veda_class + ", ba_id=" + ba_docId + ", veda_id=" + new_individual.getUri());

				} else
				{
					// count_put++;
					System.out.println("[" + count_get + "/" + count_put + "], [" + cur_id_count + "/" + total_ids + "/" + st_veda.count_put
							+ "] OK: veda_type=" + to_veda_class + ", ba_id=" + ba_docId + ", veda_id=" + new_individual.getUri());
					try
					{
						if (prev_timestamp == -1)
							st.execute("INSERT INTO PREPARED_IDS VALUES('" + ba_docId + "','" + new_individual.getUri() + "', " + timestamp + ", '"
									+ to_veda_class + "')");
						else
							st.execute("UPDATE PREPARED_IDS SET timestamp = " + timestamp + " WHERE src_id ='" + docId + "' ");

						transfer_info_conn.commit();
					} catch (SQLException ex)
					{
						if (ex.getSQLState().equals("23505") == false)
						{
							throw ex;
						}
					}
				}
			}
		}

		return new_individuals;
	}

}
