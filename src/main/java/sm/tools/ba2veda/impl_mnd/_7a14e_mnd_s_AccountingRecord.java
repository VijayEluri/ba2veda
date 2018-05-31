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

public class _7a14e_mnd_s_AccountingRecord extends Ba2VedaTransform
{
	public _7a14e_mnd_s_AccountingRecord(BaSystem _ba, VedaConnection _veda, Replacer replacer)
	{
		super(_ba, _veda, replacer, "7a14e94295374273ad1f54273b695e01", "mnd-s:AccountingRecord");
	}

	public void inital_set()
	{
		fields_map.put("name_k", "rdfs:label");
		fields_map.put("comment_k", "rdfs:comment");
		fields_map.put("project_contract", "v-s:hasContract");
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
		Resources project_contract = null;

		List<XmlAttribute> atts = doc.getAttributes();
		for (XmlAttribute att : atts)
		{
			String code = att.getCode();

			String predicate = fields_map.get(code);
			if (predicate != null)
			{
				Resources rss = ba_field_to_veda(level, att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri, true);

				if (code.equals("project_contract"))
					project_contract = rss;

				new_individual.addProperty(predicate, rss);
			}
		}

		if (project_contract != null)
		{
			new_individual.addProperty("v-s:backwardTarget", project_contract);
			new_individual.addProperty("v-s:backwardProperty", new Resource("mnd-s:hasAccountingRecord", Type._Uri));
		}

		res.add(new_individual);

		return res;
	}

}
