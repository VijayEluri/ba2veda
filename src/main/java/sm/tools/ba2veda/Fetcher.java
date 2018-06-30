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

import sm.tools.ba2veda.impl_mnd.*;
import sm.tools.ba2veda.impl_mnd._252bf_mnd_s_LocalRegulatoryDocument;
import sm.tools.ba2veda.impl_mnd._d6e6d_mnd_s_VersionOfLocalRegulatoryDocument;
import sm.tools.ba2veda.impl_stg.*;
import sm.tools.veda_client.VedaConnection;

public class Fetcher
{
	private static Properties properties = new Properties();
	private static String destination;
	static int count_get;
	static int count_put;
	private static JSONParser jp;

	public static boolean is_delta = false;
	public static boolean no_check_exists = false;
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
		//		trs.add(new _dabb6_v_s_MaterialGroup(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
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

		trs.add(new _dabb6_mnd_s_MaterialGroup(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));

		trs.add(new _53ba3_mnd_s_MaterialRequest(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));

		trs.add(new _270b9_mnd_s_ProjectCapex(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));

		trs.add(new _d1191_v_s_Link(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _0d138_v_s_Link(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _38851_v_s_Link(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _c6c99_v_s_Link(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _fb3d8_v_s_Link(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _7a775_mnd_s_ProjectCapexRequestChange(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));

		trs.add(new _3afd6_mnd_s_ForestryDocument(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _b4a29_mnd_s_ConatractDirection(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));

		trs.add(new _f025ba_mnd_s_IncomingLetter(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _d1191_mnd_s_InternalDocument(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _0d138_mnd_s_InternalDocument(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));

		trs.add(new _2806b_mnd_s_EnergyResourceRequest(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));

		trs.add(new _8aa42_mnd_s_PpeDestructionRequest(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));

		//		trs.add(new _73d7c_mnd_s_Equipmentlocation(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _73d7c_mnd_s_EquipmentPiece(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));

		trs.add(new _1d28e_v_s_Cost(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _24b39_mnd_s_RepairInformation(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _e5312_mnd_s_ActFailureReason(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		//trs.add(new _57da5_mnd_s_ActFailureAction(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		//	trs.add(new _da0e6_mnd_s_ActFailure(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));

		trs.add(new _cdaa9_mnd_s_PowerOfAttorney(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _252bf_mnd_s_LocalRegulatoryDocument(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _d6e6d_mnd_s_VersionOfLocalRegulatoryDocument(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _a5931_mnd_s_DescriptionOfChange(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _689c5_mnd_s_Addendum(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));

		trs.add(new _38851_mnd_s_InternalAccountingDocument(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));

		trs.add(new _c7977_mnd_s_ContractorCreateChangeRequest(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _da0e6_v_s_Comment(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _bcc80_mnd_s_AuditSafetyRating(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));

		trs.add(new _fb3d8_mnd_s_EngineeringRequest(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _93469_mnd_s_VersionOfTechnicalDocument(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));

		trs.add(new _a2137_mnd_s_TechnicalDocument(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _66591_mnd_s_VersionOfTechnicalDocument(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));

		trs.add(new _b163ab_mnd_s_TechnicalDocument(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));

		trs.add(new _23784_mnd_s_ItObject(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));

		trs.add(new _4748a_mnd_s_ItRequest(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));

		trs.add(new _71e38_mnd_s_Contract(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _71e38_mnd_s_AdditionalAgreement(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _d539b_mnd_s_Contract(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _d539b_mnd_s_AdditionalAgreement(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _110fa_mnd_s_Contract(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _110fa_mnd_s_AdditionalAgreement(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _a7b5b_mnd_s_Contract(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _a7b5b_mnd_s_AdditionalAgreement(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));

		trs.add(new _135fd_mnd_s_AccountingDoc(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _97da9_mnd_s_AccountingDoc(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));

		trs.add(new _f9260_mnd_s_LocalRegulatoryDocument(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));

		// STG
		trs.add(new _6e8e8_stg_InternalDocument(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _f7f8d_v_s_Comment(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _e1c2e_stg_Decree(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _71e44_stg_DecreeRegistrationRecord(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _2215e_stg_Decree(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _095bd_stg_DecreeRegistrationRecord(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _f9410_stg_Decree(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));

		trs.add(new _34567_stg_DecreeRegistrationRecord(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer)); //?

		trs.add(new _a2a74_v_s_IncomingLetter(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _fc31e_v_s_OutgoingLetter(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _65703_v_s_Delivery(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _6044c_v_s_Contractor(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _6044c_v_s_Organization(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));

		// action
		trs.add(new _05ba2_stg_LocalRegulatoryDocument(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _40612_stg_LocalRegulatoryDocument(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _07958_stg_PositionDictionary(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _8b937_stg_Claim(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _c2dd5_stg_ClaimReport(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		
		trs.add(new _aefb1_v_s_UnitOfMeasure(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _f577_stg_Claim(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _f5fe8_v_s_Comment(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		
		trs.add(new _98d7e_stg_Contract(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		trs.add(new _f16b2_stg_BudjetItem(Ba2VedaTransform.st_ba, Ba2VedaTransform.st_veda, Ba2VedaTransform.st_replacer));
		
		
		for (Ba2VedaTransform tr : trs)
		{
			for (String key : Ba2VedaTransform.st_types_map.keySet())
			{
				tr.types_map.put(key, Ba2VedaTransform.st_types_map.get(key));
			}

		}

	}

	public static void prepare_documents_of_type(int level, String from, String to, Date begin_time, String ba_id, boolean is_store_new_individuals)
			throws Exception
	{
		String templateId = from;
		System.out.println("prepare_documents_of_type: " + templateId + "->" + to);
		// List<Pair<String, Long>> elements =
		// Ba2VedaTransform.st_ba.getBAObjOnTemplateId(templateId, begin_time);		
		long count = Ba2VedaTransform.st_ba.getCountBAObjOnTemplateId(templateId, begin_time, ba_id);
		System.out.println("found records: " + count);
		ResultSet rs = Ba2VedaTransform.st_ba.getBAObjOnTemplateId(templateId, begin_time, ba_id);

		long idx = 0;
		while (rs.next())
		{
			ResultCode rc = new ResultCode();
			String docId = rs.getString(2);
			long timestamp = (long) rs.getLong(3);
			Ba2VedaTransform.prepare_document(level, from, to, docId, "", idx, count, null, null, rc, false, is_store_new_individuals);

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
		int assignedSubsystem = 0;
		Ba2VedaTransform.set_subsystems(0);
		if (args.length > 0)
		{
			for (String arg : args)
			{
				if (arg.equals("-no_check_exists"))
				{
					no_check_exists = true;
					System.out.println("no check exists");
				} else if (arg.equals("-delta"))
				{
					is_delta = true;
					System.out.println("use delta");
				} else if (arg.indexOf("-subsystem") >= 0)
				{
					assignedSubsystem = Integer.parseInt(arg.replace("-subsystem", ""));
					Ba2VedaTransform.set_subsystems(assignedSubsystem);
				} else if (arg.indexOf("-restrictions:") >= 0)
				{
					Ba2VedaTransform.config_restrictions(arg.replace("-restrictions:", "").split("/"));
				}
			}

			Date start_timestamp = new Date(Byte.MIN_VALUE);

			for (String arg : args)
			{
				if (!arg.equals("-delta") && !arg.equals("-no_check_exists") && (arg.indexOf("-subsystem") < 0)
						&& (arg.indexOf("-restrictions:") < 0))
				{
					String[] ss = arg.split("/");

					if (ss.length >= 2)
					{
						String from = ss[0].trim();
						String to = ss[1].trim();
						String ba_id = null;
						if (ss.length == 3)
							ba_id = ss[2].trim();
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

						prepare_documents_of_type(0, from, to, start_timestamp, ba_id, true);
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