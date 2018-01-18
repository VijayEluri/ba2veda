package sm.tools.ba2veda.impl;

import java.util.ArrayList;
import java.util.List;

import ru.mndsc.bigarchive.server.kernel.document.beans.XmlAttribute;
import ru.mndsc.bigarchive.server.kernel.document.beans.XmlDocument;
import sm.tools.ba2veda.Ba2VedaTransform;
import sm.tools.ba2veda.BaSystem;
import sm.tools.ba2veda.Replacer;
import sm.tools.veda_client.Individual;
import sm.tools.veda_client.Resources;
import sm.tools.veda_client.Type;
import sm.tools.veda_client.VedaConnection;

public class _e2515_mnd_s_AuditInternalReport extends Ba2VedaTransform
{
	public _e2515_mnd_s_AuditInternalReport(BaSystem _ba, VedaConnection _veda, Replacer replacer)
	{
		super(_ba, _veda, replacer, "e2515eb343924e0497fe9e79e66a36da", "mnd-s:AuditInternalReport");
	}

	public void inital_set()
	{
		fields_map.put("responsible", "v-s:responsible");
		fields_map.put("date", "v-s:dateToPlan");
		fields_map.put("attachment", "v-s:attachment");
		fields_map.put("comment", "rdfs:comment");
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

		Resources parent_name = null;

		List<XmlAttribute> atts = doc.getAttributes();
		for (XmlAttribute att : atts)
		{
			String code = att.getCode();

			String predicate = fields_map.get(code);

			if (predicate != null)
			{
				Resources rss = ba_field_to_veda(level, att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri, true);
				new_individual.addProperty(predicate, rss);
			}
		}

		if (parent_veda_doc_uri != null)
			new_individual.addProperty("v-s:parent", parent_veda_doc_uri, Type._Uri);

		XmlDocument parent_ba_doc = ba.getDocumentOnTimestamp(parent_ba_doc_id, 0);
		if (parent_ba_doc != null)
		{
			atts = parent_ba_doc.getAttributes();
			for (XmlAttribute att : atts)
			{
				String code = att.getCode();

				if (code.equals("name"))
				{
					parent_name = ba_field_to_veda(level, att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri, true);
				}
			}

			Resources date_created = new_individual.getResources("v-s:created");

			Object[] ff =
			{ date_created, ", ", parent_name };
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
				new_individual.addProperty("rdfs:label", rss);

		}

		res.add(new_individual);
		return res;
	}

}
