package sm.tools.ba2veda.impl_mnd;

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

public abstract class _xxxxx_x_Contract extends Ba2VedaTransform
{
	public String kpr1 = null;
	public String kpr2 = null;

	public _xxxxx_x_Contract(BaSystem _ba, VedaConnection _veda, Replacer replacer, String from, String to)
	{
		super(_ba, _veda, replacer, from, to);
		fields_map.put("number_auto_2", "v-s:registrationNumberAdd");
	}

	public Individual createPrice(String uri)
	{
		Individual price = new Individual();
		price.setProperty("rdf:type", new Resource("v-s:Price", Type._Uri));
		price.setUri(uri + "_price");

		return price;
	}

	public void transform1(int level, XmlDocument doc, Individual new_individual, String kind_pr, String ba_id, String parent_veda_doc_uri,
			String parent_ba_doc_id, String path) throws Exception
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

		Individual price = null;

		Resources author = new_individual.getResources("v-s:creator");
		Resources created = new_individual.getResources("v-s:created");

		List<XmlAttribute> atts = doc.getAttributes();

		if (kind_pr == null)
		{
			System.out.println("ERR! kind_pr is null, doc_id=" + ba_id);
		}

		Individual inin1 = null;

		if (kind_pr != null && (kind_pr.equals(kpr1) || kind_pr.equals(kpr2)))
		{
			for (XmlAttribute att : atts)
			{
				String code = att.getCode();

				if (code.equals("number_auto_2"))
					code.length();

				String predicate = fields_map.get(code);

				if (code.equals("number_auto_2"))
					predicate = "v-s:registrationNumberAdd";

				if (predicate != null)
				{
					Resources rss;

					if (code.equals("add_doc"))
					{
						rss = ba_field_to_veda(level, att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri, false);
					} else
					{
						rss = ba_field_to_veda(level, att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri, true);
					}

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
								Resources rss1 = ba_field_to_veda(level, att, uri, inherit_rights_from, doc, path, parent_ba_doc_id, parent_veda_doc_uri,
										true);

								new_individual.addProperty(predicate, rss1);
							}

						} else if (code.equals("kind_pr"))
						{
							kind_pr = att.getRecordIdValue();
							new_individual.addProperty(predicate, rss);
						} else if (code.equals("add_doc"))
						{
							if (inin1 == null)
							{
								inin1 = new Individual();
								inin1.setUri(uri + "_" + code);
								inin1.setProperty("rdf:type", new Resource("v-s:Comment", Type._Uri));
								inin1.addProperty("v-s:creator", author);
								inin1.addProperty("v-s:created", created);
							}

							String add_to_link = att.getLinkValue();
							XmlDocument add_to = ba.getActualDocument(add_to_link).getLeft();
							String inherit_rights_from = ba.get_first_value_of_field(add_to, "inherit_rights_from");

							if (inherit_rights_from == null)
							{
								new_individual.addProperty(predicate, rss);
							} else
							{
								if (add_to.getTypeId().equals("ead1b2fa113c45a8b79d093e8ec14728")
										|| add_to.getTypeId().equals("15206d33eafa440c84c02c8d912bce7a")
										|| add_to.getTypeId().equals("ec6d76a99d814d0496d5d879a0056428")
										|| add_to.getTypeId().equals("a0e50600ebe9450e916469ee698e3faa")
										|| add_to.getTypeId().equals("71e3890b3c77441bad288964bf3c3d6a")
										|| add_to.getTypeId().equals("cab21bf8b68a4b87ac37a5b41adad8a8")
										|| add_to.getTypeId().equals("110fa1f351e24a2bbc187c872b114ea4")
										|| add_to.getTypeId().equals("d539b253cb6247a381fb51f4ee34b9d8")
										|| add_to.getTypeId().equals("a7b5b15a99704c9481f777fa941506c0")
										|| add_to.getTypeId().equals("67588724c4c54b25a2c84906613bd15a"))
								{

									att.setLinkValue(inherit_rights_from);
									Resources rss1 = ba_field_to_veda(level, att, uri, inherit_rights_from, doc, path, parent_ba_doc_id, parent_veda_doc_uri,
											true);

									inin1.addProperty(predicate, rss1);
								} else
								{
									inin1.addProperty(predicate, rss);
								}
							}
						} else if (code.equals("theme"))
						{
							new_individual.addProperty(predicate, att.getTextValue(), "RU");
						} else if (code.equals("theme_en"))
						{
							new_individual.addProperty(predicate, att.getTextValue(), "EN");
						} else if (code.equals("summ"))
						{
							if (price == null)
								price = createPrice(uri);
							price.setProperty("v-s:sum", rss);
						} else if (code.equals("currency"))
						{
							if (price == null)
								price = createPrice(uri);
							price.setProperty("v-s:hasCurrency", rss);
						} else if (code.equals("comment"))
						{
							Individual inin2 = new Individual();
							inin2.setUri(uri + "_" + code);
							inin2.setProperty("rdf:type", new Resource("v-s:Comment", Type._Uri));
							inin2.setProperty("rdfs:label", rss);
							inin2.addProperty("v-s:creator", author);
							inin2.addProperty("v-s:created", created);
							putIndividual(level, inin2, ba_id);
							new_individual.addProperty("v-s:hasComment", new Resource(inin2.getUri(), Type._Uri));
						} else if (code.equals("date_to"))
						{
							if (rss == null || rss.resources.size() == 0)
								new_individual.addProperty(predicate, new Resource("2030-12-01T00:00:00Z", Type._Datetime));
							else
								new_individual.addProperty(predicate, rss);
						} else
						{
							new_individual.addProperty(predicate, rss);
						}
					}

					if (code.equals("type_contract"))
					{
						String prev_id = att.getRecordIdValue();
						String f_id = "_" + prev_id;
						att.setRecordIdValue(f_id);
						Resources rss1 = ba_field_to_veda(level, att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri, false);
						String fc_id = rss1.resources.get(0).getData();
						if (fc_id.equals("d:" + f_id))
							new_individual.addProperty("v-s:hasObligationKind", new Resource("d:" + prev_id, Type._Uri));
						else
							new_individual.addProperty("v-s:hasObligationKind", rss1);
					}

				}
			}

			if (inin1 != null)
			{
				putIndividual(level, inin1, ba_id);
				new_individual.addProperty("v-s:hasComment", new Resource(inin1.getUri(), Type._Uri));
			}

			if (price != null)
			{
				new_individual.addProperty("v-s:hasPrice", new Resource(price.getUri(), Type._Uri));
				price.addProperty("v-s:parent", new Resource(new_individual.getUri(), Type._Uri));
				putIndividual(level, price, ba_id);
			}

		} else
			new_individual.setUri(null);
	}

}
