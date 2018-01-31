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

public class _b163ab_v_s_Directory extends Ba2VedaTransform
{
	public _b163ab_v_s_Directory(BaSystem _ba, VedaConnection _veda, Replacer replacer)
	{
		super(_ba, _veda, replacer, "b163abf24965406b8bbadc7cf6d7a435", "v-s:Directory");
	}

	public void inital_set()
	{
		fields_map.put("Полное название", "rdfs:label");
		fields_map.put("Родительский комплект", "?");
	}

	@Override
	public List<Individual> transform(int level, XmlDocument doc, String ba_id, String parent_veda_doc_uri, String parent_ba_doc_id, String path)
			throws Exception
	{
		//1. индивид из этого поля (id_1) помещаем в поле v-s:parentUnit
		//2. обрабатываем (или создаем) индивид id_1 (аналогично по рекурсии) и в нем в поле v-s:childUnit помещаем текущий документ.

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
			String predicate = fields_map.get(code);
			if (predicate != null)
			{
				List<Individual> out_indvs = new ArrayList<Individual>();
				Resources rss = ba_field_to_veda(level, att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri, true, out_indvs);

				if (rss != null && rss.resources.size() > 0)
				{
					if (code.equals("Родительский комплект"))
					{
						for (Resource rr : rss.resources)
						{
							Individual parent = null;

							String parent_id = rr.getData();
							parent = veda.getIndividual(parent_id);

							new_individual.addProperty("v-s:parentUnit", rss);
							if (parent != null)
							{
								Resources childs = parent.getResources("v-s:childUnit");
								if (childs == null)
									childs = new Resources();

								childs.add(new Resource(uri, Type._Uri));
								parent.addProperty("v-s:childUnit", childs);

								//if (parent_is_new == false)
								veda.putIndividual(parent, true, 0);
							}
						}
					} else
					{
						new_individual.addProperty(predicate, rss);
					}
				}
			}
		}

		res.add(new_individual);
		return res;
	}
}
