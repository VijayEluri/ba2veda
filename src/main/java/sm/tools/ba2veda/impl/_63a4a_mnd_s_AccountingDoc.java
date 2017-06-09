package sm.tools.ba2veda.impl;

import java.util.ArrayList;
import java.util.List;

import ru.mndsc.bigarchive.server.kernel.document.beans.XmlAttribute;
import ru.mndsc.bigarchive.server.kernel.document.beans.XmlDocument;
import sm.tools.ba2veda.Ba2VedaTransform;
import sm.tools.ba2veda.BaSystem;
import sm.tools.ba2veda.Pair;
import sm.tools.ba2veda.Replacer;
import sm.tools.veda_client.Individual;
import sm.tools.veda_client.Resource;
import sm.tools.veda_client.Resources;
import sm.tools.veda_client.Type;
import sm.tools.veda_client.VedaConnection;
import sm.tools.veda_client.util;

public class _63a4a_mnd_s_AccountingDoc extends Ba2VedaTransform
{
	public _63a4a_mnd_s_AccountingDoc(BaSystem _ba, VedaConnection _veda, Replacer replacer)
	{
		super(_ba, _veda, replacer, "63a4aa872c0d49a6a857060b9632a17e", "mnd-s:AccountingDoc");
	}

	public void inital_set()
	{
		fields_map.put("name", "rdfs:label");
		fields_map.put("tknum", "mnd-s:transportationNumber");
		fields_map.put("comment_p", "rdfs:comment");
		fields_map.put("attachment_p", "v-s:attachment");
		fields_map.put("prepayment", "v-s:prepayment");
		fields_map.put("summ_p", "v-s:hasPrice");
		fields_map.put("number_p", "v-s:registrationNumberIn");
		fields_map.put("date_fact", "v-s:transactionDate");
		fields_map.put("date_add_p", "v-s:registrationDate");
		fields_map.put("number_add_p", "v-s:registrationNumber");
		fields_map.put("order", "mnd-s:hasPurchaseOrder");
		fields_map.put("contractor", "v-s:customerContractor");
		fields_map.put("kind_p", "v-s:hasDocumentKind");
		fields_map.put("inherit_rights_from", "v-s:hasContract");
		fields_map.put("link_to_doc", "v-s:backwardTarget");

		// default_values_map.put("v-s:backwardProperty", new Resource("v-s:hasAccountingDoc",
		// Type._Uri));

		default_values_map.put("v-s:supplier", new Resource("d:org_RU1121003135", Type._Uri));
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

		List<Resources> link_to_doc = new ArrayList<Resources>();
		List<String> link_to_doc_ba = new ArrayList<String>();

		List<XmlAttribute> atts = doc.getAttributes();

		Resources currency = null;
		List<Resources> contractor_list = new ArrayList<Resources>();

		String kind_p_0 = ba.get_first_value_of_field(doc, "kind_p");

		if (kind_p_0 != null && kind_p_0.equals("92a5bcfed9fa4cd2a062a1dc53ed2a1c") == false)
			return new ArrayList<Individual>();

		for (XmlAttribute att : atts)
		{
			String code = att.getCode();

			if (code.equals("currency"))
				currency = ba_field_to_veda(att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri, true);
			else if (code.equals("contractor"))
				contractor_list.add(ba_field_to_veda(att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri, true));
		}

		List<Individual> add_docs = new ArrayList<Individual>();

		for (XmlAttribute att : atts)
		{
			String code = att.getCode();

			String predicate = fields_map.get(code);

			if (predicate != null)
			{
				Resources rss = ba_field_to_veda(att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri, true);

				if (code.equals("link_to_doc"))
				{
					if (rss.resources.size() > 0)
					{
						link_to_doc.add(rss);
						link_to_doc_ba.add(att.getLinkValue());
					}
				} else
				{
					if (code.equals("prepayment") || code.equals("summ_p"))
					{
						if (rss.resources.size() > 0)
						{
							Individual vsprice = new Individual();
							vsprice.setUri(util.get_hashed_uri(uri + "_" + code));

							vsprice.addProperty("rdf:type", new Resource("v-s:Price", Type._Uri));
							vsprice.setProperty("v-s:sum", rss);
							vsprice.setProperty("v-s:hasCurrency", currency);
							vsprice.addProperty("v-s:parent", new Resource(uri, Type._Uri));

							add_docs.add(vsprice);

							new_individual.addProperty(predicate, new Resource(vsprice.getUri(), Type._Uri));
						}
					} else if (code.equals("inherit_rights_from"))
					{
						String add_to_link = att.getLinkValue();
						Pair<XmlDocument, Long> add_to_pair = ba.getActualDocument(add_to_link);

						if (add_to_pair != null)
						{
							XmlDocument add_to = add_to_pair.getLeft();

							String inherit_rights_from = ba.get_first_value_of_field(add_to, "inherit_rights_from");

							if (inherit_rights_from == null)
							{
								new_individual.addProperty(predicate, rss);
							} else
							{
								att.setLinkValue(inherit_rights_from);
								Resources rss1 = ba_field_to_veda(att, uri, inherit_rights_from, doc, path, parent_ba_doc_id, parent_veda_doc_uri,
										true);

								new_individual.addProperty(predicate, rss1);
							}
						}
					} else
						new_individual.addProperty(predicate, rss);
				}

			}
		}

		boolean is_create_v_s_link = true;

		if (link_to_doc.size() == 1)
		{
			// Заполнять, только если link_to_doc единичное в данном документе, и оно веден на
			// Первичный документ (027afafd4c444d96913422ee669e6d44 или
			// 63a4aa872c0d49a6a857060b9632a17e),
			// и в нем вид документа (поле kind_p) Счет фактура (81cd1dfaeac64a3fadd2f6f8fabfaccb)
			// или Комплект ТСД (dd0a88c458f94ba08b61ff632a5d66dd)

			// String lnk = link_to_doc.get(0).resources.get(0).getData();
			String lnk_ba = link_to_doc_ba.get(0);

			Pair<XmlDocument, Long> dres = ba.getActualDocument(lnk_ba);

			if (dres != null)
			{
				XmlDocument lnk_ba_doc = dres.getLeft();

				if (lnk_ba_doc.getTypeId().equals("027afafd4c444d96913422ee669e6d44")
						|| lnk_ba_doc.getTypeId().equals("63a4aa872c0d49a6a857060b9632a17e"))
				{
					String kind_p = ba.get_first_value_of_field(lnk_ba_doc, "kind_p");

					if (kind_p != null)
					{
						if (kind_p.equals("81cd1dfaeac64a3fadd2f6f8fabfaccb") || kind_p.equals("dd0a88c458f94ba08b61ff632a5d66dd")
								|| kind_p.equals("77d4cbd58cf34321a82e7281e8347542"))
						{
							is_create_v_s_link = false;
							new_individual.addProperty("v-s:backwardTarget", link_to_doc.get(0));
							new_individual.addProperty("v-s:backwardProperty", new Resource("v-s:hasAccountingDoc", Type._Uri));
						}
					}
				}
			}

		}

		if (is_create_v_s_link)
		{
			// В остальных случаях создавать индивид класса v-s:Link из каждой ссылки на другой
			// документ, в котором v-s:from - id текущего документа. v-s:to - id документа по ссылке

			for (Resources rr : link_to_doc)
			{
				for (Resource r1 : rr.resources)
				{
					Individual vslink = new Individual();
					vslink.setUri(util.get_hashed_uri(uri + "_" + r1.getData()));

					vslink.addProperty("rdf:type", new Resource("v-s:Link", Type._Uri));
					vslink.addProperty("v-s:from", new Resource(uri, Type._Uri));
					vslink.addProperty("v-s:to", new Resource(r1.getData(), Type._Uri));
					vslink.addProperty("v-s:created", new_individual.getResources("v-s:created"));
					vslink.addProperty("v-s:creator", new_individual.getResources("v-s:creator"));

					add_docs.add(vslink);
				}
			}
		}

		if (contractor_list != null)
		{
			for (Resources contractor : contractor_list)
			{
				for (Resource rr : contractor.resources)
				{
					Individual element = veda.getIndividual(rr.getData());

					if (element != null)
					{
						Resources linkedOrganization = element.getResources("v-s:linkedOrganization");

						if (linkedOrganization != null)
							new_individual.addProperty("v-s:customer", linkedOrganization);
					} else
					{
						System.out.println("ERR! supplierContractor not found :" + rr.getData());
					}
				}
			}
		}

		if (parent_veda_doc_uri != null)
			new_individual.addProperty("v-s:parent", parent_veda_doc_uri, Type._Uri);
		/*
		 * Object[] ff = { hasDocumentKind, ", ", registrationNumber, ", ", registrationNumberIn,
		 * ", ", registrationDate }; String[] langs_out = { "EN", "RU" }; Resources rss =
		 * rs_assemble(ff, langs_out); if (rss.resources.size() == 0) { String[] langs_out2 = {
		 * "NONE" }; rss = rs_assemble(ff, langs_out2); } if (rss.resources.size() > 0)
		 * new_individual.addProperty("rdfs:label", rss);
		 */
		res.add(new_individual);

		for (Individual add_doc : add_docs)
		{
			res.add(add_doc);
		}

		return res;
	}

}
