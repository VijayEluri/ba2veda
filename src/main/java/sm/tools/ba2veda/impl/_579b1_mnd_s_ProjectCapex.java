package sm.tools.ba2veda.impl;

import java.util.ArrayList;
import java.util.List;

import ru.mndsc.bigarchive.server.kernel.document.beans.XmlAttribute;
import ru.mndsc.bigarchive.server.kernel.document.beans.XmlDocument;
import sm.tools.ba2veda.Ba2VedaTransform;
import sm.tools.ba2veda.BaSystem;
import sm.tools.ba2veda.Replacer;
import sm.tools.veda_client.Individual;
import sm.tools.veda_client.Resource;
import sm.tools.veda_client.Resources;
import sm.tools.veda_client.Type;
import sm.tools.veda_client.VedaConnection;

public class _579b1_mnd_s_ProjectCapex extends Ba2VedaTransform
{
	public _579b1_mnd_s_ProjectCapex(BaSystem _ba, VedaConnection _veda, Replacer replacer)
	{
		super(_ba, _veda, replacer, "579b135940eb4f81bf29c0aaef794422", "mnd-s:ProjectCapex");
	}

	public void inital_set()
	{
		fields_map.put("name", "rdfs:label");
		fields_map.put("Наименование", "v-s:title");
		fields_map.put("status_manual", "v-s:hasStatus");
		fields_map.put("Цех", "v-s:stakeholder");
		fields_map.put("Руководитель проекта", "v-s:projectManager");
		fields_map.put("Заказчик", "v-s:owner");
		fields_map.put("Номер", "v-s:registrationNumber");
		fields_map.put("№ СПП-элемента", "mnd-s:coordinationWithOtherAction");
	}

	@Override
	public List<Individual> transform(XmlDocument doc, String ba_id, String parent_veda_doc_uri, String parent_ba_doc_id, String path)
			throws Exception
	{
		String uri = prepare_uri(ba_id);
		List<Individual> res = new ArrayList<Individual>();

		Individual new_individual = new Individual();
		new_individual.setUri(uri);

		set_basic_fields(new_individual, doc);

		new_individual.addProperty("rdf:type", to_class, Type._Uri);

		Resources registrationNumber = null;
		Resources title = null;

		List<XmlAttribute> atts = doc.getAttributes();
		for (XmlAttribute att : atts)
		{
			String code = att.getCode();

			String predicate = fields_map.get(code);
			if (predicate != null)
			{
				Resources rss = ba_field_to_veda(att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri, true);

				if (rss != null && rss.resources.size() > 0)
				{
					if (code.equals("Номер"))
						registrationNumber = rss;

					if (code.equals("Наименование"))
						title = rss;

					new_individual.addProperty(predicate, rss);
				}
			}
		}

		if (registrationNumber != null)
		{
			Object[] ff1 =
			{ registrationNumber, ", ", title };
			String[] langs_out1 =
			{ "EN", "RU" };
			Resources rss = rs_assemble(ff1, langs_out1);
			if (rss.resources.size() == 0)
			{
				String[] langs_out2 =
				{ "NONE" };
				rss = rs_assemble(ff1, langs_out2);
			}

			if (rss.resources.size() > 0)
				new_individual.addProperty("rdfs:label", rss);
		}

		res.add(new_individual);
		return res;
	}

}
