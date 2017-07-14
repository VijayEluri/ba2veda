package sm.tools.ba2veda;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import sm.tools.ba2veda.impl._027af_mnd_s_AccountingDoc;
import sm.tools.ba2veda.impl._0d31c_v_s_Comment;
import sm.tools.ba2veda.impl._13e63_mnd_s_AuditSafetyObservationKind;
import sm.tools.ba2veda.impl._15206_mnd_s_AdditionalAgreement;
import sm.tools.ba2veda.impl._15206_mnd_s_Contract;
import sm.tools.ba2veda.impl._19faf_mnd_s_AuditSafetyTheme;
import sm.tools.ba2veda.impl._2fba7_mnd_s_ContractRequest;
import sm.tools.ba2veda.impl._2fe65_mnd_s_RepresentativeCostsEvent;
import sm.tools.ba2veda.impl._524d6_mnd_s_AdditionalAgreement;
import sm.tools.ba2veda.impl._524d6_mnd_s_Contract;
import sm.tools.ba2veda.impl._52e5c_mnd_s_Claim;
import sm.tools.ba2veda.impl._579b1_mnd_s_ProjectCapex;
import sm.tools.ba2veda.impl._5c2ce_mnd_s_Building;
import sm.tools.ba2veda.impl._5d588_mnd_s_Decree;
import sm.tools.ba2veda.impl._5ee12_v_s_Contractor;
import sm.tools.ba2veda.impl._5fcfb_mnd_s_PurchaseOrder;
import sm.tools.ba2veda.impl._63a4a_mnd_s_AccountingDoc;
import sm.tools.ba2veda.impl._65875_mnd_s_Decree;
import sm.tools.ba2veda.impl._662_v_s_DeliveryMethod;
import sm.tools.ba2veda.impl._6b0ea_v_s_ContractorProfile;
import sm.tools.ba2veda.impl._6b0ea_v_s_Organization;
import sm.tools.ba2veda.impl._6f121_v_s_Level;
import sm.tools.ba2veda.impl._74eb8_mnd_s_AuditSafetyAction;
import sm.tools.ba2veda.impl._7a14e_mnd_s_AccountingRecord;
import sm.tools.ba2veda.impl._7ab24_mnd_s_AuditInternalAction;
import sm.tools.ba2veda.impl._7adb5_v_s_Delivery;
import sm.tools.ba2veda.impl._7be2d_mnd_s_ContractPassport;
import sm.tools.ba2veda.impl._86607_mnd_s_ContractorCategoryRequest;
import sm.tools.ba2veda.impl._9172d_v_s_Subsidiary;
import sm.tools.ba2veda.impl._92fdd_mnd_s_AdditionalAgreement;
import sm.tools.ba2veda.impl._92fdd_mnd_s_Contract;
import sm.tools.ba2veda.impl._99f08_v_s_LetterRegistrationRecordSender;
import sm.tools.ba2veda.impl._9fb73_mnd_s_AuditSafety;
import sm.tools.ba2veda.impl._a96be_v_s_Comment;
import sm.tools.ba2veda.impl._b243a_mnd_s_DismantlingProject;
import sm.tools.ba2veda.impl._b3999_mnd_s_OutgoingLetter;
import sm.tools.ba2veda.impl._bcb5e_mnd_s_AdditionalAgreement;
import sm.tools.ba2veda.impl._bcb5e_mnd_s_Contract;
import sm.tools.ba2veda.impl._bdc96_mnd_s_AuditInternal;
import sm.tools.ba2veda.impl._bfb8d_mnd_s_DecreeRegistrationRecord;
import sm.tools.ba2veda.impl._c6c99_mnd_s_ContractRequest;
import sm.tools.ba2veda.impl._c8a33_mnd_s_AuditInternalObject;
import sm.tools.ba2veda.impl._d0072_v_s_Correspodent;
import sm.tools.ba2veda.impl._d5c66_mnd_s_RepresentativeCostsForEvent;
import sm.tools.ba2veda.impl._d9378_mnd_s_IncomingLetter;
import sm.tools.ba2veda.impl._d9476_mnd_s_ContractRequest;
import sm.tools.ba2veda.impl._dabb6_v_s_MaterialGroup;
import sm.tools.ba2veda.impl._df75b_mnd_s_AuditSafety;
import sm.tools.ba2veda.impl._df968_mnd_s_Decree;
import sm.tools.ba2veda.impl._e2515_mnd_s_AuditInternalReport;
import sm.tools.ba2veda.impl._e85f6_mnd_s_Decree;
import sm.tools.ba2veda.impl._ead1b_mnd_s_AdditionalAgreement;
import sm.tools.ba2veda.impl._ead1b_mnd_s_Contract;
import sm.tools.ba2veda.impl._ec6d7_mnd_s_AdditionalAgreement;
import sm.tools.ba2veda.impl._ec6d7_mnd_s_Contract;
import sm.tools.ba2veda.impl._f063e_v_s_ClassifierOKVED;
import sm.tools.ba2veda.impl._fa740_mnd_s_ContractRequest;
import sm.tools.ba2veda.impl._faff0_v_s_ContractorProfileFile;
import sm.tools.ba2veda.impl._fc534_v_s_NormativeDocument;
import sm.tools.ba2veda.impl._fcb4a_mnd_s_Decree;
import sm.tools.ba2veda.impl._3369e_mnd_s_BusinessTrip;
import sm.tools.ba2veda.impl._3b538_mnd_s_Decree;
import sm.tools.ba2veda.impl._22701_mnd_s_ClaimReport;
import sm.tools.ba2veda.impl._22dc2_mnd_s_Decree;
import sm.tools.ba2veda.impl._7bd88_mnd_s_PassRequest;
import sm.tools.ba2veda.impl._050ce_mnd_s_Pass;
import sm.tools.ba2veda.impl._09969_mnd_s_Decree;
import sm.tools.ba2veda.impl._2bace_mnd_s_ActOfViolation;

import sm.tools.veda_client.VedaConnection;

public class Fetcher
{
	private static Properties properties = new Properties();
	private static String destination;
	static int count_get;
	static int count_put;
	private static JSONParser jp;

	public static boolean is_delta = false;
	public static String delta_properties_fname;
	public static Properties delta_properties = new Properties();
	static SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSS'Z'");

	private static void loadProperties()
	{
		try
		{
			properties.load(new FileInputStream("ba2veda.properties"));

			destination = properties.getProperty("destination", "http://127.0.0.1:8080");
			System.out.println(properties);
		} catch (IOException e)
		{
			writeDefaultProperties();
		}
	}

	private static void writeDefaultProperties()
	{
		System.out.println("Writing default properties.");

		properties.setProperty("dbUser", "ba");
		properties.setProperty("dbPassword", "123456");
		properties.setProperty("dbUrl", "localhost:3306");
		properties.setProperty("mongodb", "localhost:27017");
		try
		{
			properties.store(new FileOutputStream("ba2veda.properties"), null);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////

	public static void map_init() throws Exception
	{
		jp = new JSONParser();
		JSONObject transform_rules = (JSONObject) jp.parse(new FileReader("transform-rules.json"));

		Ba2VedaTransform.st_replacer = new Replacer();
		Ba2VedaTransform.st_replacer.set_replace_rules((JSONArray) transform_rules.get("replace"));
		Ba2VedaTransform.st_replacer.set_ignore_rules((JSONArray) transform_rules.get("ignore"));

		List<Ba2VedaTransform> trs = new ArrayList<Ba2VedaTransform>();

		trs.add(new _9172d_v_s_Subsidiary(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _f063e_v_s_ClassifierOKVED(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _6b0ea_v_s_ContractorProfile(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _5ee12_v_s_Contractor(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _faff0_v_s_ContractorProfileFile(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _86607_mnd_s_ContractorCategoryRequest(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _6b0ea_v_s_Organization(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _9fb73_mnd_s_AuditSafety(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _df75b_mnd_s_AuditSafety(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _74eb8_mnd_s_AuditSafetyAction(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _19faf_mnd_s_AuditSafetyTheme(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		// trs.add(new _74eb8_mnd_s_AuditSafetyObservation(Ba2VedaTransform.st_ba,
		// Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _13e63_mnd_s_AuditSafetyObservationKind(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _6f121_v_s_Level(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _fc534_v_s_NormativeDocument(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _a96be_v_s_Comment(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _7be2d_mnd_s_ContractPassport(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _5fcfb_mnd_s_PurchaseOrder(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _dabb6_v_s_MaterialGroup(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _bdc96_mnd_s_AuditInternal(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _7ab24_mnd_s_AuditInternalAction(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _c8a33_mnd_s_AuditInternalObject(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _e2515_mnd_s_AuditInternalReport(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _7a14e_mnd_s_AccountingRecord(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));

		trs.add(new _524d6_mnd_s_Contract(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _524d6_mnd_s_AdditionalAgreement(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _ead1b_mnd_s_Contract(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _ead1b_mnd_s_AdditionalAgreement(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _bcb5e_mnd_s_Contract(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _bcb5e_mnd_s_AdditionalAgreement(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _15206_mnd_s_Contract(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _15206_mnd_s_AdditionalAgreement(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _92fdd_mnd_s_Contract(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _92fdd_mnd_s_AdditionalAgreement(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _ec6d7_mnd_s_Contract(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _ec6d7_mnd_s_AdditionalAgreement(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));

		trs.add(new _579b1_mnd_s_ProjectCapex(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));

		trs.add(new _027af_mnd_s_AccountingDoc(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _63a4a_mnd_s_AccountingDoc(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));

		trs.add(new _0d31c_v_s_Comment(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));

		trs.add(new _d9476_mnd_s_ContractRequest(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _fa740_mnd_s_ContractRequest(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _c6c99_mnd_s_ContractRequest(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _2fba7_mnd_s_ContractRequest(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));

		trs.add(new _d9378_mnd_s_IncomingLetter(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _b3999_mnd_s_OutgoingLetter(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));

		trs.add(new _662_v_s_DeliveryMethod(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));

		trs.add(new _99f08_v_s_LetterRegistrationRecordSender(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _7adb5_v_s_Delivery(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _52e5c_mnd_s_Claim(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _3369e_mnd_s_BusinessTrip(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _5c2ce_mnd_s_Building(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		
		trs.add(new _22701_mnd_s_ClaimReport(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _7bd88_mnd_s_PassRequest(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _050ce_mnd_s_Pass(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		
		trs.add(new _2bace_mnd_s_ActOfViolation(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _451f2_mnd_s_RepresentativeCosts(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _d0072_v_s_Correspodent(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _2fe65_mnd_s_RepresentativeCostsEvent(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _d5c66_mnd_s_RepresentativeCostsForEvent(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		
		trs.add(new _b243a_mnd_s_DismantlingProject(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		
		trs.add(new _5d588_mnd_s_Decree(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _bfb8d_mnd_s_DecreeRegistrationRecord(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		
		trs.add(new _09969_mnd_s_Decree(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _e85f6_mnd_s_Decree(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _fcb4a_mnd_s_Decree(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _df968_mnd_s_Decree(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _22dc2_mnd_s_Decree(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _65875_mnd_s_Decree(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _3b538_mnd_s_Decree(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		
		for (Ba2VedaTransform tr : trs)
		{
			for (String key : Ba2VedaTransform.st_types_map.keySet())
			{
				tr.types_map.put(key, Ba2VedaTransform.st_types_map.get(key));
			}

		}
	}

	public static void prepare_documents_of_type(String from, String to, Date begin_time) throws Exception
	{
		String templateId = from;
		System.out.println("prepare_documents_of_type: " + templateId + "->" + to);
		// List<Pair<String, Long>> elements =
		// Ba2VedaTransform.st_ba.getBAObjOnTemplateId(templateId, begin_time);		
		long count = Ba2VedaTransform.st_ba.getCountBAObjOnTemplateId(templateId, begin_time);
		System.out.println("found records: " + count);
		ResultSet rs = Ba2VedaTransform.st_ba.getBAObjOnTemplateId(templateId, begin_time);

		long idx = 0;
		while (rs.next())
		{
			ResultCode rc = new ResultCode();
			String docId = rs.getString(2);
			long timestamp = (long) rs.getLong(3);
			Ba2VedaTransform.prepare_document(from, to, docId, "", idx, count, null, null, rc);

			store_timestamp_to_cfg(new Date(timestamp + 1));

			idx++;
		}
		rs.close();

	}

	public static void main(String[] args) throws Exception
	{
		loadProperties();
		Ba2VedaTransform.st_ba = new BaSystem();
		Ba2VedaTransform.st_veda = new VedaConnection(destination, "ImportDMSToVeda",
				"a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3");
		if (Ba2VedaTransform.st_veda.isOk() == false)
			return;

		map_init();

		// getAttachmentPort();
		// fetchOrganization();

		if (args.length > 0)
		{
			for (String arg : args)
			{
				if (arg.equals("-delta"))
				{
					is_delta = true;
					System.out.println("use delta");
				}
			}

			Date start_timestamp = new Date(Byte.MIN_VALUE);

			for (String arg : args)
			{
				if (!arg.equals("-delta"))
				{
					String[] ss = arg.split("/");

					if (ss.length == 2)
					{
						String from = ss[0].trim();
						String to = ss[1].trim();
						delta_properties_fname = from + "#" + to + ".cfg";

						if (is_delta)
						{
							try
							{
								delta_properties.load(new FileInputStream(delta_properties_fname));
								String s_time = delta_properties.getProperty("begin-time", sdf.format(new Date()));

								try
								{
									start_timestamp = sdf0.parse(s_time);
								} catch (Exception ex)
								{
									start_timestamp = sdf.parse(s_time);
								}

								System.out.println(delta_properties);
							} catch (IOException e)
							{
								store_timestamp_to_cfg(new Date());
							}

						}

						prepare_documents_of_type(from, to, start_timestamp);
					} else
						System.out.println("invalid argument: " + arg);

				}
			}
		}

		System.out.println("finish");
		System.exit(0);
	}

	private static void store_timestamp_to_cfg(Date timestamp)
	{
		delta_properties.setProperty("begin-time", sdf.format(timestamp));
		try
		{
			delta_properties.store(new FileOutputStream(delta_properties_fname), null);
		} catch (IOException e1)
		{
			e1.printStackTrace();
		}

	}
}