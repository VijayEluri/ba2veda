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

public class _34567_v_s_RegistrationRecord extends Ba2VedaTransform
{
	public _34567_v_s_RegistrationRecord(BaSystem _ba, VedaConnection _veda, Replacer replacer)
	{
		super(_ba, _veda, replacer, "3456788cda534e52b63be863ecf90185", "v-s:RegistrationRecord");
	}

	public void inital_set()
	{
		fields_map.put("add_to_doc", "v-s:parent");
		fields_map.put("reg_number", "v-s:registrationNumber");
		fields_map.put("reg_date", "v-s:registrationDate");
		fields_map.put("comment", "rdfs:comment");
		fields_map.put("attachment", "v-s:attachment");
		fields_map.put("name", "rdfs:label");
		fields_map.put("nomenclature", "?");
		fields_map.put("", "");

		employee_prefix = "d:employee_";
		appointment_prefix = "d:";
		stand_prefix = "d:";
		department_prefix = "department";
		is_mondi = false;
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
		//new_individual.addProperty("v-s:omitBackwardTarget", new Resource(false, Type._Bool));
		//new_individual.addProperty("v-s:backwardProperty", new Resource("mnd-s:hasDecreeRegistrationRecord", Type._Uri));

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

				if (rss == null)
					continue;

				if (rss.resources.size() < 1)
					continue;

				if (code.equals("nomenclature"))
				{
					String val = att.getRecordIdValue();

					new_individual.addProperty("stg:hasDecreeKind", new Resource(val, Type._Uri));
				} else if (code.equals("add_to_doc"))
				{
					String val = att.getRecordIdValue();
					new_individual.addProperty("v-s:backwardTarget", new Resource(val, Type._Uri));
					new_individual.addProperty("v-s:backwardProperty", new Resource("stg:hasDecreeRegistrationRecord", Type._Uri));
				}
			}
		}

		res.add(new_individual);
		return res;
	}
}
