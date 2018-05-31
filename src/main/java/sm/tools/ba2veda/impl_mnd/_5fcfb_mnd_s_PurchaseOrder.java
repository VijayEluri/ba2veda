package sm.tools.ba2veda.impl_mnd;

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

public class _5fcfb_mnd_s_PurchaseOrder extends Ba2VedaTransform
{
	public _5fcfb_mnd_s_PurchaseOrder(BaSystem _ba, VedaConnection _veda, Replacer replacer)
	{
		super(_ba, _veda, replacer, "5fcfb16441774c6584abb368d9032427", "mnd-s:PurchaseOrder");
	}

	public void inital_set()
	{
		fields_map.put("number", "v-s:registrationNumber");
		fields_map.put("date_from", "v-s:registrationDate");
		fields_map.put("name", "mnd-s:contractNumberForPurchaseOrder");
		fields_map.put("account_number", "mnd-s:contractorCodeForPurchaseOrder");
		fields_map.put("contractor", "v-s:hasContractor");
		fields_map.put("procurement_organization", "mnd-s:hasProcurementOrganization");
		fields_map.put("procurement_organization_str", "mnd-s:procurementOrganizationCodeForPurchaseOrder");
		fields_map.put("payment_terms", "mnd-s:paymentConditionsForPurchaseOrder");
		fields_map.put("completed", "mnd-s:completedForPurchaseOrder");
		fields_map.put("old_number", "v-s:registrationNumberIn");
		fields_map.put("inherit_rights_from", "v-s:hasContract");

		// default_values_map.put("v-s:backwardProperty", new Resource("mnd-s:hasPurchaseOrder",
		// Type._Uri));
		// default_values_map.put("v-s:omitBackwardTargetGroup", new Resource(true, Type._Bool));
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
		boolean is_deleted = false, is_completed = false;

		List<XmlAttribute> atts = doc.getAttributes();
		for (XmlAttribute att : atts)
		{
			String code = att.getCode();

			if (code.equals("completed"))
			{
				if (att.getTextValue().equals("X"))
					is_completed = true;
				continue;
			}
			if (code.equals("deleted"))
			{
				if (att.getTextValue().equals("X"))
					is_deleted = true;
				continue;
			}

			String predicate = fields_map.get(code);
			if (predicate != null)
			{
				Resources rss = ba_field_to_veda(level, att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri, true);

				if (code.equals("inherit_rights_from"))
				{
					String add_to_link = att.getLinkValue();

					if (add_to_link != null)
					{
						Pair<XmlDocument, Long> rr = ba.getActualDocument(add_to_link);

						if (rr != null)
						{
							XmlDocument add_to = rr.getLeft();
							String inherit_rights_from = ba.get_first_value_of_field(add_to, "inherit_rights_from");
							// Resources rss = ba_field_to_veda(level, att, uri, ba_id, doc, path,
							// parent_ba_doc_id, true);

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

									new_individual.addProperty(predicate, rss1);
								} else
								{
									new_individual.addProperty(predicate, rss);
								}
							}
						} else
						{
							System.out.println("WARN! in field inherit_rights_from[" + add_to_link + "] not found in BA, use as is");
							new_individual.addProperty(predicate, rss);							
						}
					} else
					{
						System.out.println("ERR! in field inherit_rights_from[" + add_to_link + "]");
					}
				} else
				{
					new_individual.addProperty(predicate, rss);
				}

			}
		}

		new_individual.addProperty("v-s:deletedInOrigin", new Resource(is_deleted, Type._Bool));
		new_individual.addProperty("mnd-s:completedForPurchaseOrder", new Resource(is_completed, Type._Bool));

		Resources regnum = new_individual.getResources("v-s:registrationNumber");

		if (regnum != null && regnum.resources.size() > 0)
			new_individual.addProperty("rdfs:label", regnum);

		res.add(new_individual);

		return res;
	}

}
