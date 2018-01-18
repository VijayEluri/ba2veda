package sm.tools.ba2veda.impl;

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

public abstract class _xxxxx_x_ContractRequest extends Ba2VedaTransform
{
	public _xxxxx_x_ContractRequest(BaSystem _ba, VedaConnection _veda, Replacer replacer, String from, String to)
	{
		super(_ba, _veda, replacer, from, to);
	}

	public Individual createPrice(String uri)
	{
		Individual price = new Individual();
		price.setProperty("rdf:type", new Resource("v-s:Price", Type._Uri));
		price.setUri(uri + "_price");

		return price;
	}

	public void transform1(int level, XmlDocument doc, Individual new_individual, String ba_id, String parent_veda_doc_uri, String parent_ba_doc_id, String path)
			throws Exception
	{
		String uri;

		if (new_individual.getUri() == null)
		{
			uri = prepare_uri(ba_id);
			new_individual.setUri(uri);
			set_basic_fields(level, new_individual, doc);

			new_individual.addProperty("rdf:type", to_class, Type._Uri);
		} else
		{
			uri = new_individual.getUri();
		}

		Individual inin1 = null;
		Individual inin2 = null;

		Resources author = new_individual.getResources("v-s:creator");
		Resources created = new_individual.getResources("v-s:created");

		Individual price = null;

		List<XmlAttribute> atts = doc.getAttributes();

		for (XmlAttribute att : atts)
		{
			String code = att.getCode();
			String predicate = fields_map.get(code);

			if (predicate != null)
			{
				Resources rss;

				rss = ba_field_to_veda(level, att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri, true);

				if (rss != null && rss.resources.size() > 0)
				{
					if (code.equals("add_to_contract"))
					{
						String add_to_contract_link = att.getLinkValue();
						XmlDocument add_to_contract = ba.getActualDocument(add_to_contract_link).getLeft();
						String inherit_rights_from = ba.get_first_value_of_field(add_to_contract, "inherit_rights_from");

						if (inherit_rights_from == null)
						{
							new_individual.addProperty(predicate, rss);
						} else
						{
							att.setLinkValue(inherit_rights_from);
							Resources rss1 = ba_field_to_veda(level, att, uri, inherit_rights_from, doc, path, parent_ba_doc_id, parent_veda_doc_uri, true);

							new_individual.addProperty(predicate, rss1);
						}

					} else if (code.equals("summ_2"))
					{
						if (price == null)
							price = createPrice(uri);
						price.setProperty("v-s:sum", rss);
					} else if (code.equals("currency"))
					{
						if (price == null)
							price = createPrice(uri);
						price.setProperty("v-s:hasCurrency", rss);
					} else if (code.equals("add_info_long_list"))
					{
						if (inin1 == null)
						{
							inin1 = new Individual();
							inin1.setUri(uri + "_" + code);
							inin1.setProperty("rdf:type", new Resource("v-s:Comment", Type._Uri));
							inin1.addProperty("v-s:creator", author);
							inin1.addProperty("v-s:created", created);
							inin1.addProperty("rdfs:label", rss);
						}
					} else if (code.equals("link"))
					{
						if (inin2 == null)
						{
							inin2 = new Individual();
							inin2.setUri(uri + "_" + code);
							inin2.setProperty("rdf:type", new Resource("v-s:Link", Type._Uri));
							inin2.addProperty("v-s:creator", author);
							inin2.addProperty("v-s:created", created);
							inin2.addProperty("v-s:from", rss);
							inin2.addProperty("v-s:to", new Resource(new_individual.getUri(), Type._Uri));
						}

					} else
						new_individual.addProperty(predicate, rss);
				}

			}
		}

		if (inin1 != null)
		{
			putIndividual(level, inin1, ba_id);
			new_individual.addProperty("v-s:hasComment", new Resource(inin1.getUri(), Type._Uri));
		}

		if (inin2 != null)
		{
			putIndividual(level, inin2, ba_id);
			new_individual.addProperty("v-s:hasLink", new Resource(inin2.getUri(), Type._Uri));
		}

		if (price != null)
		{
			Resources sum = price.getResources("v-s:sum");

			if (sum != null && sum.resources.size() > 0)
			{
				new_individual.addProperty("v-s:expectedValueOfContract", new Resource(price.getUri(), Type._Uri));
				price.addProperty("v-s:parent", new Resource(new_individual.getUri(), Type._Uri));
				putIndividual(level, price, ba_id);
			}
		}

	}

}
