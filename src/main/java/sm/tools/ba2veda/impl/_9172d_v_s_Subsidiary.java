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

public class _9172d_v_s_Subsidiary extends Ba2VedaTransform
{
	public _9172d_v_s_Subsidiary(BaSystem _ba, VedaConnection _veda, Replacer replacer)
	{
		super(_ba, _veda, replacer, "9172d7184cd1439e8911c299c7b66df9", "v-s:Subsidiary");
	}

	public void inital_set()
	{
		fields_map.put("name", "rdfs:label");
		fields_map.put("name_short", "v-s:shortLabel");
		fields_map.put("owner", "v-s:parent");
		fields_map.put("kpp", "v-s:taxRegistrationCause");
		fields_map.put("address_postal", "v-s:postalAddress");
		fields_map.put("comment_d", "rdfs:comment");
		fields_map.put("zip", "tmp:zip");
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
		Resources owner = null;

		List<XmlAttribute> atts = doc.getAttributes();
		for (XmlAttribute att : atts)
		{
			String code = att.getCode();

			String predicate = fields_map.get(code);
			if (predicate != null)
			{
				Resources rss = ba_field_to_veda(level, att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri, true);

				if (code.equals("owner"))
					owner = rss;

				new_individual.addProperty(predicate, rss);
			}
		}

		if (owner != null)
		{
			new_individual.addProperty("v-s:backwardTarget", owner);
			new_individual.addProperty("v-s:backwardProperty", new Resource("v-s:hasSubsidiary", Type._Uri));
		}

		res.add(new_individual);

		return res;
	}

}
