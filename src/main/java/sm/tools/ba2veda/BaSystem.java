package sm.tools.ba2veda;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;

import org.gost19.pacahon.ba_organization_driver.BaOrganizationDriver;

import com.bigarchive.filemanager.FileManagerEndpoint;
import com.bigarchive.filemanager.FileManagerService;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import ru.mndsc.bigarchive.server.kernel.document.XmlUtil;
import ru.mndsc.bigarchive.server.kernel.document.beans.XmlAttribute;
import ru.mndsc.bigarchive.server.kernel.document.beans.XmlDocument;

public class BaSystem
{
	private Properties properties;
	private FileManagerEndpoint filemanager = null;
	public BaOrganizationDriver pacahon;
	private Connection documents_db_connection = null;
	private Connection files_db_connection = null;
	private Connection synchronization_db_connection = null;
	private String dbUser;
	private String dbPassword;
	private String documents_dbUrl;
	private String files_dbUrl;
	private String synchronization_dbUrl;
	private String az_mongodb_host;
	private int az_mongodb_port;
	private DBCollection az_simple_coll;

	BaSystem() throws Exception
	{
		loadProperties();

		connect_to_mysql();
		getPacahon();
		connectToAuthorizationDB();
	}

	private void loadProperties()
	{
		try
		{
			properties = new Properties();
			properties.load(new FileInputStream("ba2veda.properties"));

			dbUser = properties.getProperty("dbUser", "ba");
			dbPassword = properties.getProperty("dbPassword", "123456");
			documents_dbUrl = properties.getProperty("documents_dbUrl", "localhost:3306/documents_db");
			files_dbUrl = properties.getProperty("files_dbUrl", "localhost:3306/fm_meta_db");
			synchronization_dbUrl = properties.getProperty("synchronization_dbUrl", "localhost:3306/synchronization_db");

			String az_db = properties.getProperty("authorization_db", "localhost:27017");
			String[] els = az_db.split(":");
			az_mongodb_host = els[0];
			az_mongodb_port = Integer.parseInt(els[1]);

			System.out.println(properties);
		} catch (IOException e)
		{
		}
	}

	public String[] get_org_map_record(String sed_id)
	{
		String[] res = null;

		String queryStr = "SELECT name, inn, new_sed_id FROM map_org_data WHERE sed_id = ?";
		try
		{
			PreparedStatement ps = documents_db_connection.prepareStatement(queryStr);
			ps.setString(1, sed_id);
			ResultSet rs = ps.executeQuery();
			if (rs.next())
			{
				res = new String[3];
				res[0] = rs.getString(1);
				res[1] = rs.getString(2);
				res[2] = rs.getString(3);
			}
			rs.close();
			ps.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}

		return res;
	}

	public FileInfo getFileInfo(String id)
	{
		FileInfo fi = null;
		String queryStr = "SELECT name, dateCreated, filePath, fileLength FROM filerecords WHERE id = ?";
		try
		{
			PreparedStatement ps = files_db_connection.prepareStatement(queryStr);
			ps.setString(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next())
			{
				fi = new FileInfo();
				fi.name = rs.getString(1);
				fi.date_create = rs.getDate(2);
				fi.file_path = rs.getString(3);
				fi.file_length = rs.getInt(4);
			}
			rs.close();
			ps.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}

		return fi;
	}

	public String get_first_value_of_field(XmlDocument doc, String field_name)
	{
		List<XmlAttribute> atts = doc.getAttributes();
		for (XmlAttribute att : atts)
		{
			String code = att.getCode();
			String type = att.getType();
			if (code.equals(field_name))
			{
				if (type.equals("LINK"))
				{
					return att.getLinkValue();

				} else if (type.equals("DICTIONARY"))
					return att.getRecordIdValue();
			}
		}
		return null;
	}

	public XmlDocument getDocumentOnTimestamp(String id, long timestamp) throws JAXBException
	{
		XmlDocument doc = null;
		String xml = null;
		// String queryStr = "SELECT content, recordid FROM objects WHERE
		// objectId = ? AND actual =
		// 1 AND timestamp <= ? ORDER BY timestamp DESC LIMIT 1";
		String queryStr = "SELECT content, recordid FROM objects WHERE objectId = ? AND actual = 1 AND isDraft = 0";
		try
		{
			PreparedStatement ps = documents_db_connection.prepareStatement(queryStr);
			ps.setString(1, id);
			// ps.setLong(2, timestamp);
			ResultSet rs = ps.executeQuery();
			if (rs.next())
			{
				xml = rs.getString(1);
				String recordid = rs.getString(2);
				recordid.length();
				doc = (XmlDocument) XmlUtil.unmarshall(xml);
			}
			rs.close();
			ps.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}

		return doc;
	}

	public Pair<XmlDocument, Long> getActualDocument(String id) throws JAXBException
	{
		Pair<XmlDocument, Long> res = null;

		XmlDocument doc = null;
		String xml = null;
		String queryStr = "SELECT content, recordid, timestamp FROM objects WHERE objectId = ? AND actual = 1 AND isDraft = 0";
		try
		{
			PreparedStatement ps = documents_db_connection.prepareStatement(queryStr);
			ps.setString(1, id);
			// ps.setLong(2, timestamp);
			ResultSet rs = ps.executeQuery();
			if (rs.next())
			{
				xml = rs.getString(1);
				String recordid = rs.getString(2);
				recordid.length();
				doc = (XmlDocument) XmlUtil.unmarshall(xml);
				res = new Pair<XmlDocument, Long>(doc, rs.getLong(3));
			}
			rs.close();
			ps.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}

		return res;
	}

	public ResultSet getBAObjOnTemplateId(String templateId, Date begin_time)
	{
//		String queryStr = "SELECT recordId, objectId, timestamp FROM objects WHERE isDraft = 0 AND templateId = ? AND timestamp > ? AND actual = 1";	
		String id = "328cff7c89e644b188679a50206db986";
		String queryStr = "SELECT recordId, objectId, timestamp FROM objects WHERE isDraft = 0 AND templateId = ? AND timestamp > ? AND actual = 1 AND objectId = '"+ id + "'";
		try
		{
			PreparedStatement ps = documents_db_connection.prepareStatement(queryStr);
			ps.setString(1, templateId);
			long tt = begin_time.getTime();
			ps.setLong(2, tt);
			ResultSet rs = ps.executeQuery();

			//ps.close();
			return rs;
			// while (rs.next())
			// {
			// String recordId = rs.getString(1);
			// String objectId = rs.getString(2);
			// recordId.length();
			// res.add(new Pair<String, Long>(objectId, rs.getLong(3)));
			// }
			// rs.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	public Long getCountBAObjOnTemplateId(String templateId, Date begin_time)
	{
		Long res = null;
//		String queryStr = "SELECT COUNT(*) FROM objects WHERE isDraft = 0 AND templateId = ? AND timestamp > ? AND actual = 1";
		String id = "328cff7c89e644b188679a50206db986";
		String queryStr = "SELECT COUNT(*) FROM objects WHERE isDraft = 0 AND templateId = ? AND timestamp > ? AND actual = 1 AND objectId = '"+ id + "'";

		try
		{
			PreparedStatement ps = documents_db_connection.prepareStatement(queryStr);
			ps.setString(1, templateId);
			long tt = begin_time.getTime();
			ps.setLong(2, tt);
			ResultSet rs = ps.executeQuery();
			if (rs.next())
			{
				res = rs.getLong(1);
			}
			rs.close();
			ps.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}

		return res;
	}

	public void connect_to_mysql() throws Exception
	{
		Class.forName("com.mysql.jdbc.Driver").newInstance();

		System.out.print("connect to source database " + documents_dbUrl + "...");
		documents_db_connection = DriverManager.getConnection("jdbc:mysql://" + documents_dbUrl, dbUser, dbPassword);
		System.out.print("connect to source database " + files_dbUrl + "...");
		files_db_connection = DriverManager.getConnection("jdbc:mysql://" + files_dbUrl, dbUser, dbPassword);
		synchronization_db_connection = DriverManager.getConnection("jdbc:mysql://" + synchronization_dbUrl, dbUser, dbPassword);

		System.out.println("ok");
	}

	public BaOrganizationDriver getPacahon() throws Exception
	{
		if (pacahon == null)
		{
			String endpoint = properties.getProperty("pacahon", "tcp://127.0.0.1:5560");
			pacahon = new BaOrganizationDriver(endpoint);
		}
		return pacahon;
	}

	public FileManagerEndpoint getAttachmentPort()
	{
		if (filemanager == null)
		{
			try
			{
				URL url = new URL("file:wsdl/filemanager.wsdl");
				QName qname = new QName("http://filemanager.zdms_component.mndsc.ru/", "FileManagerService");

				filemanager = new FileManagerService(url, qname).getFileManagerEndpointPort();
			} catch (Throwable e)
			{
				throw new IllegalStateException("Ошибка инициализации соединения с сервисом атачментов", e);
			}

		}

		return filemanager;
	}

	private void connectToAuthorizationDB() throws Exception
	{
		MongoClient az_mongoClient = new MongoClient(az_mongodb_host, az_mongodb_port);
		DB az_pacahon_db = az_mongoClient.getDB("az1");
		if (az_mongodb_host != null)
		{
			az_mongoClient = new MongoClient(az_mongodb_host, az_mongodb_port);
			az_pacahon_db = az_mongoClient.getDB("az1");
			az_simple_coll = az_pacahon_db.getCollection("simple");
		}

		// az_mongoClient.close();
	}

	public Right get_right(String ba_user_id, String ba_doc_id)
	{
		Right res = new Right();

		return res;
	}

	public List<Right> get_rights_of_doc(String ba_doc_id)
	{
		List<Right> res = new ArrayList<Right>();

		BasicDBObject query = new BasicDBObject("mo/at/acl#eId", ba_doc_id);

		DBCursor cursor = az_simple_coll.find(query);

		Iterator<DBObject> it = cursor.iterator();

		while (it.hasNext())
		{
			DBObject el = it.next();
			String rt = el.get("mo/at/acl#rt") + "";
			String tgSsE = el.get("mo/at/acl#tgSsE") + "";

			Right rr = new Right();

			for (int idx = 0; idx < rt.length(); idx++)
			{
				char ch = rt.charAt(idx);
				if (ch == 'c')
					rr.isCreate = true;
				if (ch == 'r')
					rr.isRead = true;
				if (ch == 'u')
					rr.isUpdate = true;
				if (ch == 'd')
					rr.isDelete = true;
			}

			rr.doc_id = ba_doc_id;
			rr.user_id = tgSsE;

			res.add(rr);
		}

		return res;
	}

	public String gedDdsidFromSynchronizationViaQuery(String query) {
		try
		{
			PreparedStatement ps = synchronization_db_connection.prepareStatement(query);
			System.out.println("QUERY: " + query);
			ResultSet rs = ps.executeQuery();
			if (rs.next())
			{
				return rs.getString("ddsid");
			}
			rs.close();
			ps.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return "";
	}
}
