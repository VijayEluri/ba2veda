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

public class _c2dd5_stg_ClaimReport extends Ba2VedaTransform
{
	public _c2dd5_stg_ClaimReport(BaSystem _ba, VedaConnection _veda, Replacer replacer)
	{
		super(_ba, _veda, replacer, "c2dd5a87c4e34de2a4b1e4a5b7ddf85f", "stg:ClaimReport");
	}

	public void inital_set()
	{
		fields_map.put("stage", "v-s:hasStatus");
		fields_map.put("comment", "rdfs:comment");
		fields_map.put("resolution", "stg:hasClaimDecision");
		fields_map.put("attachment", "v-s:attachment");
		fields_map.put("add_doc", "?");
		fields_map.put("claim_sum_to", "?");

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

		new_individual.addProperty("v-s:backwardTarget", new Resource(parent_veda_doc_uri, Type._Uri));
		new_individual.addProperty("v-s:parent", new Resource(parent_veda_doc_uri, Type._Uri));
		new_individual.addProperty("v-s:backwardProperty", new Resource("stg:hasClaimReport"));

		XmlDocument parent = ba.getActualDocument(parent_ba_doc_id).getLeft();
		ArrayList<Object> label = new ArrayList<Object>();

		for (XmlAttribute att : parent.getAttributes())
		{
			if (att.getCode().equals("compound_title"))
				label.add(ba_field_to_veda(level, att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri, true));
		}

		if (label.size() > 0)
			label.add(",");
		label.add(new_individual.getResources("v-s:created"));

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

				if (rss.resources.size() < 1)
					continue;

				if (code.equals("add_doc"))
				{
					String irf = att.getLinkValue();
					if (irf == null)
						continue;
					String link_to = irf;

					Individual link = new Individual();
					link.addProperty("rdf:type", new Resource("v-s:Link", Type._Uri));
					link.setUri("d:link_" + ba_id + "_" + link_to);
					link.addProperty("v-s:from", new Resource(new_individual.getUri(), Type._Uri));
					link.addProperty("v-s:to", new Resource("d:" + link_to, Type._Uri));

					putIndividual(level, link, ba_id);

					new_individual.addProperty("v-s:hasLink", new Resource(link.getUri(), Type._Uri));
				} else if (code.equals("claim_sum_to"))
				{
					Individual price = new Individual();
					price.setUri(new_individual.getUri() + "_price");
					price.addProperty("rdf:type", new Resource("v-s:Price", Type._Uri));
					price.addProperty("v-s:parent", new Resource(new_individual.getUri(), Type._Uri));
					price.addProperty("v-s:creator", new_individual.getResources("v-s:creator"));
					price.addProperty("v-s:created", new_individual.getResources("v-s:created"));
					price.addProperty("v-s:sum", rss);
					price.addProperty("v-s:hasCurrency", new Resource("d:currency_rub", Type._Uri));
					//eomr.addProperty("v-s:hasPrice", new Resource(price.getUri(), Type._Uri));
					putIndividual(level, price, ba_id);
				}

			}
		}

		String[] langs_out1 =
		{ "EN", "RU" };
		String[] langs_out2 =
		{ "NONE" };

		Resources rss = rs_assemble(label.toArray(), langs_out1);
		if (rss.resources.size() == 0)
			rss = rs_assemble(label.toArray(), langs_out2);

		if (rss.resources.size() > 0)
			new_individual.addProperty("rdfs:label", rss);

		res.add(new_individual);
		return res;
	}
}
