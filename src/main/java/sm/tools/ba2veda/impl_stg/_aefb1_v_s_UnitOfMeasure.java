package sm.tools.ba2veda.impl_stg;

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

public class _aefb1_v_s_UnitOfMeasure extends Ba2VedaTransform
{
	public _aefb1_v_s_UnitOfMeasure(BaSystem _ba, VedaConnection _veda, Replacer replacer)
	{
		super(_ba, _veda, replacer, "aefb1b2502104efa8ec8ba35ce5e0ba8", "v-s:UnitOfMeasure");
	}

	public void inital_set()
	{
		fields_map.put("value", "rdfs:label");
		fields_map.put("code", "v-s:shortLabel");
	}

	@Override
	public List<Individual> transform(int level, XmlDocument doc, String ba_id, String parent_veda_doc_uri, String parent_ba_doc_id, String path)
			throws Exception
	{
		employee_prefix = "d:employee_";
		appointment_prefix = "d:";
		stand_prefix = "d:";
		department_prefix = "department";
		is_mondi = false;

		String uri = prepare_uri(ba_id);
		List<Individual> res = new ArrayList<Individual>();

		Individual new_individual = new Individual();
		new_individual.setUri(uri);

		set_basic_fields(level, new_individual, doc);

		List<XmlAttribute> atts = doc.getAttributes();
		for (XmlAttribute att : atts)
		{
			String code = att.getCode();

			String predicate = fields_map.get(code);
			System.out.println("CODE: " + code);

			if (predicate != null)
			{
				Resources rss = ba_field_to_veda(level, att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri, true);

				if (predicate.equals("?") == false)
					new_individual.addProperty(predicate, rss);

				if (code.equals("indicator_active"))
				{
					String tt = att.getTextValue();
					if (tt.equals("X") || tt.equals("x"))
						new_individual.addProperty("v-s:valid", new Resource(true, Type._Bool));
					else
						new_individual.addProperty("v-s:valid", new Resource(false, Type._Bool));
				}
				if (code.equals("indicator_blocked"))
				{
					String tt = att.getTextValue();
					if (tt.equals("X") || tt.equals("x"))
						new_individual.addProperty("v-s:locked", new Resource(true, Type._Bool));
					else
						new_individual.addProperty("v-s:locked", new Resource(false, Type._Bool));
				}

			}
		}
		res.add(new_individual);
		return res;
	}

}
