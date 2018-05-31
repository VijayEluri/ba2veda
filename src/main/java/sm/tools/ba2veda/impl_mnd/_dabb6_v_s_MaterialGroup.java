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

public class _dabb6_v_s_MaterialGroup extends Ba2VedaTransform
{
	public _dabb6_v_s_MaterialGroup(BaSystem _ba, VedaConnection _veda, Replacer replacer)
	{
		super(_ba, _veda, replacer, "dabb67b8bc5d40f6b347025c6970025f", "v-s:MaterialGroup");
	}

	public void inital_set()
	{
		fields_map.put("comment", "v-s:description");
		fields_map.put("controller", "mnd-s:controllerMRP");
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

		List<XmlAttribute> atts = doc.getAttributes();
		for (XmlAttribute att : atts)
		{
			String code = att.getCode();

			if (code.equals("title"))
			{
				String ss = att.getTextValue();
				String aa_ar[] = ss.split("-");

				if (aa_ar.length == 2)
				{
					new_individual.addProperty("v-s:registrationNumber", new Resource(aa_ar[0].trim()));
					new_individual.addProperty("rdfs:label", new Resource(aa_ar[1].trim()));
				}
			} else
			{
				String predicate = fields_map.get(code);

				if (predicate != null)
				{
					Resources rss = ba_field_to_veda(level, att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri, true);
					new_individual.addProperty(predicate, rss);
				}
			}
		}

		res.add(new_individual);
		return res;
	}

}
