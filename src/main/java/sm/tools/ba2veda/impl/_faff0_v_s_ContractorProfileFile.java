package sm.tools.ba2veda.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import ru.mndsc.bigarchive.server.kernel.document.beans.XmlAttribute;
import ru.mndsc.bigarchive.server.kernel.document.beans.XmlDocument;
import sm.tools.ba2veda.Ba2VedaTransform;
import sm.tools.ba2veda.BaSystem;
import sm.tools.ba2veda.FileInfo;
import sm.tools.ba2veda.Replacer;
import sm.tools.veda_client.Individual;
import sm.tools.veda_client.Resources;
import sm.tools.veda_client.Type;
import sm.tools.veda_client.VedaConnection;
import sm.tools.veda_client.util;

public class _faff0_v_s_ContractorProfileFile extends Ba2VedaTransform
{
	public _faff0_v_s_ContractorProfileFile(BaSystem _ba, VedaConnection _veda, Replacer replacer)
	{
		super(_ba, _veda, replacer, "faff0324d75143fabe3a23444d2323ec", "v-s:ContractorProfileFile");
	}

	public void inital_set()
	{
		fields_map.put("date_from", "v-s:dateFrom");
		fields_map.put("date_to", "v-s:dateTo");
		fields_map.put("comment_a", "rdfs:comment");
	}

	@Override
	public List<Individual> transform(XmlDocument doc, String ba_id, String parent_veda_doc_uri, String parent_ba_doc_id, String path)
			throws Exception
	{
		String uri = prepare_uri(ba_id);

		List<Individual> res = new ArrayList<Individual>();

		HashMap<String, Individual> individuals_2_uri = new HashMap<String, Individual>();
		Date date_created = doc.getDateCreated();
		List<XmlAttribute> atts = doc.getAttributes();
		for (XmlAttribute att : atts)
		{
			String code = att.getCode();
			String type = att.getType();
			if (code.equals("attachment"))
			{
				Individual new_individual = new Individual();

				set_basic_fields(new_individual, doc);

				// сделаем содержимое этого поля частью документа
				if (type.equals("FILE"))
				{
					String fileId = att.getFileValue();
					FileInfo fi = ba.getFileInfo(fileId);

					if (fi != null && date_created != null)
					{
						// String path_to_files = util.date2_short_string(date_created).replace('-',
						// '/');
						String file_uri = "d:" + fileId;

						// создадим описание к загруженному файлу
						// indv.addProperty("rdf:type", "v-s:File", Type._Uri);

						new_individual.addProperty("rdfs:label", fi.name, Type._String);
						new_individual.addProperty("v-s:fileName", fi.name, Type._String);
						new_individual.addProperty("v-s:fileSize", fi.file_length + "", Type._String);
						new_individual.addProperty("v-s:fileUri", fileId, Type._String);
						new_individual.addProperty("v-s:filePath", new Resources().add(fi.file_path, Type._String));
						new_individual.setProperty("v-s:created", new Resources().add(fi.date_create));

						if (parent_veda_doc_uri != null)
							new_individual.addProperty("v-s:parent", new Resources().add(parent_veda_doc_uri, Type._Uri));

						new_individual.setUri(file_uri);

						if (file_uri.equals("d:ddf65ed3-8531-46d5-9dd2-1d17f9cb026d"))
						{
							file_uri.length();
						}
					}
				}

				// Resource r_new_uri = rss.resources.get(0);
				// String new_uri = r_new_uri.toString();

				// Individual new_individual = new Individual();
				// new_individual.setUri(uri);

				new_individual.addProperty("rdf:type", to_class, Type._Uri);
				individuals_2_uri.put(new_individual.getUri(), new_individual);
			}
		}

		for (XmlAttribute att : atts)
		{
			String code = att.getCode();

			String predicate = fields_map.get(code);
			if (predicate != null)
			{
				Resources rss = ba_field_to_veda(att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri, true);

				for (String key : individuals_2_uri.keySet())
				{
					Individual new_individual = individuals_2_uri.get(key);
					new_individual.addProperty(predicate, rss);
				}
			}
		}

		for (String key : individuals_2_uri.keySet())
		{
			res.add(individuals_2_uri.get(key));
		}

		return res;
	}

}
