package sm.tools.ba2veda.impl_mnd;

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

public class _7adb5_v_s_Delivery extends Ba2VedaTransform
{
	public _7adb5_v_s_Delivery(BaSystem _ba, VedaConnection _veda, Replacer replacer)
	{
		super(_ba, _veda, replacer, "7adb5b4a92f743659d90a2978e729cf5", "v-s:Delivery");
	}

	public void inital_set()
	{
		fields_map.put("name", "rdfs:label");
		fields_map.put("add_to_doc", "?");
		fields_map.put("date_send", "v-s:date");
		fields_map.put("type_send", "v-s:deliverBy");
		fields_map.put("comment", "rdfs:comment");
		fields_map.put("compound_title", "rdfs:label");
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

		Resources _add_to_doc = null;

		List<XmlAttribute> atts = doc.getAttributes();
		for (XmlAttribute att : atts)
		{
			String code = att.getCode();

			String predicate = fields_map.get(code);
			if (predicate != null)
			{
				Resources rss = null;
				rss = ba_field_to_veda(level, att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri, true);

				if (code.equals("add_to_doc"))
					_add_to_doc = rss;

				new_individual.addProperty(predicate, rss);
			}
		}

		if (_add_to_doc != null)
		{
			// add_to_doc -> v-s:parent 
			// add_to_doc -> v-s:backwardTarget
			// Константа v-s:hasDelivery -> v-s:backwardProperty			

			new_individual.addProperty("v-s:backwardProperty", "v-s:hasDelivery", Type._Uri);
			new_individual.addProperty("v-s:parent", _add_to_doc);
			new_individual.addProperty("v-s:backwardTarget", _add_to_doc);
		}

		res.add(new_individual);
		return res;
	}

}
