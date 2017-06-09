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

public class _a96be_v_s_Comment extends Ba2VedaTransform
{
	public _a96be_v_s_Comment(BaSystem _ba, VedaConnection _veda, Replacer replacer)
	{
		super(_ba, _veda, replacer, "a96be31d57d043539de3187ec9c320c5", "v-s:Comment");
	}

	public void inital_set()
	{
		fields_map.put("attachment", "v-s:attachment");
		fields_map.put("add_doc", "v-s:linkedObject");
		fields_map.put("comment", "rdfs:label");
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

		Resources kind_as_comment = null;
		Resources comment = null;

		List<XmlAttribute> atts = doc.getAttributes();
		for (XmlAttribute att : atts)
		{
			String code = att.getCode();

			if (code.equals("kind"))
			{
				String kind_id = att.getRecordIdValue();
				XmlDocument doc1 = ba.getActualDocument(kind_id).getLeft();
				if (doc1 != null)
				{
					List<XmlAttribute> atts1 = doc1.getAttributes();
					for (XmlAttribute att1 : atts1)
					{
						String code1 = att1.getCode();

						if (code1.equals("name"))
						{
							Resources rss = ba_field_to_veda(att1, uri, kind_id, doc1, path, ba_id, parent_veda_doc_uri, true);
							kind_as_comment = rss;
							break;
						}
					}
				}
			}

			String predicate = fields_map.get(code);
			if (predicate != null)
			{
				Resources rss = ba_field_to_veda(att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri, true);

				if (rss != null && rss.resources.size() > 0)
				{
					if (code.equals("add_doc"))
					{
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
								Resources rss1 = ba_field_to_veda(att, uri, inherit_rights_from, doc, path, parent_ba_doc_id, parent_veda_doc_uri,
										true);

								new_individual.addProperty(predicate, rss1);
							} else
							{
								new_individual.addProperty(predicate, rss);
							}
						}
					} else
					{
						if (code.equals("comment"))
							comment = rss;

						new_individual.addProperty(predicate, rss);
					}
				}
			}
		}

		if (kind_as_comment != null)
		{
			Object[] ff1 =
			{ kind_as_comment, ", ", comment };
			String[] langs_out1 =
			{ "EN", "RU" };
			Resources rss = rs_assemble(ff1, langs_out1);
			if (rss.resources.size() == 0)
			{
				String[] langs_out2 =
				{ "NONE" };
				rss = rs_assemble(ff1, langs_out2);
			}

			if (rss.resources.size() > 0)
				new_individual.addProperty("rdfs:label", rss);
		}

		if (parent_veda_doc_uri != null)
		{
			new_individual.addProperty("v-s:backwardProperty", new Resource("v-s:hasComment", Type._Uri));
			new_individual.addProperty("v-s:backwardTarget", new Resource(parent_veda_doc_uri, Type._Uri));
		}

		res.add(new_individual);
		return res;
	}
}
