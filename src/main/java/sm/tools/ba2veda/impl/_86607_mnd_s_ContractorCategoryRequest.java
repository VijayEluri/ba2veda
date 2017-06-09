package sm.tools.ba2veda.impl;

import java.util.ArrayList;
import java.util.HashMap;
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

public class _86607_mnd_s_ContractorCategoryRequest extends Ba2VedaTransform
{
	public _86607_mnd_s_ContractorCategoryRequest(BaSystem _ba, VedaConnection _veda, Replacer replacer)
	{
		super(_ba, _veda, replacer, "86607c9c7ec64c758e64b019e3a261a8", "mnd-s:ContractorCategoryRequest");
	}

	protected HashMap<String, String> inner2_fields_map;
	protected HashMap<String, String> inner3_fields_map;

	public void inital_set()
	{
		fields_map.put("activity", "v-s:description");
		fields_map.put("okved_code", "v-s:hasClassifierOKVED");

		inner2_fields_map = new HashMap<String, String>();
		inner2_fields_map.put("decision", "mnd-s:isOrganizationOk");
		inner2_fields_map.put("date", "v-s:date");

		inner3_fields_map = new HashMap<String, String>();
		inner3_fields_map.put("decision", "mnd-s:isContractorOkSecurityDepSummary");
		inner3_fields_map.put("date", "v-s:date");
	}

	@Override
	public List<Individual> transform(XmlDocument doc, String ba_id, String parent_veda_doc_uri, String parent_ba_doc_id, String path)
			throws Exception
	{
		String uri = prepare_uri(ba_id);

		List<Individual> res = new ArrayList<Individual>();

		Individual new_individual_1 = new Individual();
		new_individual_1.setUri(uri);

		List<XmlAttribute> atts = doc.getAttributes();
		for (XmlAttribute att : atts)
		{
			String code = att.getCode();
			String predicate = fields_map.get(code);
			if (predicate != null)
			{
				Resources rss = ba_field_to_veda(att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri, true);
				new_individual_1.addProperty(predicate, rss);
			}
		}
		set_basic_fields(new_individual_1, doc);

		if (parent_veda_doc_uri != null)
		{
			new_individual_1.addProperty("v-s:backwardProperty", new Resource("mnd-s:hasContractorCategoryRequest", Type._Uri));
			new_individual_1.addProperty("v-s:backwardTarget", new Resource(parent_veda_doc_uri, Type._Uri));
		}

		Individual new_individual_2 = new Individual();
		new_individual_2.setUri(uri + "_1");
		for (XmlAttribute att : atts)
		{
			String code = att.getCode();
			String predicate = inner2_fields_map.get(code);
			if (predicate != null)
			{
				Resources rss = ba_field_to_veda(att, new_individual_2.getUri(), null, doc, path, parent_ba_doc_id, parent_veda_doc_uri, true);
				new_individual_2.addProperty(predicate, rss);
			}
		}
		set_basic_fields(new_individual_2, doc);
		new_individual_2.setProperty("rdf:type", new Resource("mnd-s:ContractorCategoryDecision", Type._Uri));
		if (parent_veda_doc_uri != null)
		{
			new_individual_2.addProperty("v-s:backwardProperty", new Resource("mnd-s:hasContractorCategoryDecision", Type._Uri));
			new_individual_2.addProperty("v-s:backwardTarget", new Resource(uri, Type._Uri));
		}

		int res_2 = putIndividual(new_individual_2, ba_id, true);

		if (res_2 != 200)
		{
			System.out.println("ERR: veda_id=" + new_individual_2.getUri());

		} else
		{
			new_individual_1.addProperty("mnd-s:hasContractorCategoryDecision", new Resource(new_individual_2.getUri(), Type._Uri));

			res.add(new_individual_1);
		}

		Individual new_individual_3 = new Individual();
		new_individual_3.setUri(uri + "_2");
		for (XmlAttribute att : atts)
		{
			String code = att.getCode();
			String predicate = inner3_fields_map.get(code);
			if (predicate != null)
			{
				Resources rss = ba_field_to_veda(att, new_individual_3.getUri(), null, doc, path, parent_ba_doc_id, parent_veda_doc_uri, true);
				new_individual_3.addProperty(predicate, rss);
			}
		}
		set_basic_fields(new_individual_3, doc);
		new_individual_3.setProperty("rdf:type", new Resource("mnd-s:ContractorCategoryDecisionSecurity", Type._Uri));
		if (parent_veda_doc_uri != null)
		{
			new_individual_3.addProperty("v-s:backwardProperty", new Resource("mnd-s:hasContractorCategoryDecisionSecurity", Type._Uri));
			new_individual_3.addProperty("v-s:backwardTarget", new Resource(new_individual_2.getUri(), Type._Uri));
		}

		int res_3 = putIndividual(new_individual_3, ba_id, true);

		return res;
	}
}
