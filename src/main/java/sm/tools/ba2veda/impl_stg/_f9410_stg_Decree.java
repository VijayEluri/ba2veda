package sm.tools.ba2veda.impl_stg;

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

public class _f9410_stg_Decree extends Ba2VedaTransform
{
	public _f9410_stg_Decree(BaSystem _ba, VedaConnection _veda, Replacer replacer)
	{
		super(_ba, _veda, replacer, "f941006b96064c4cace673b0d740b034", "stg:Decree");
	}

	public void inital_set()
	{
		fields_map.put("owner", "v-s:owner");
		fields_map.put("subject", "v-s:title");
		//fields_map.put("type", "stg:hasDecreeKind");
		fields_map.put("class", "?");
		fields_map.put("content", "?");
		fields_map.put("initiator", "v-s:initiator");
		fields_map.put("signer", "v-s:signedBy");
		fields_map.put("responsible_person", "v-s:responsible");
		fields_map.put("attachment", "v-s:attachment");
		fields_map.put("file", "v-s:attachment");
		fields_map.put("display_requisite", "rdfs:label");
		fields_map.put("add_info", "v-s:hasComment");
		fields_map.put("reg_note", "stg:hasDecreeRegistrationRecord");
		fields_map.put("link_document", "?");
		fields_map.put("status", "v-s:hasStatus");		
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

		new_individual.addProperty("rdf:type", to_class, Type._Uri);
		int link_count = 0;

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

				if (code.equals("link_document"))
				{
					String irf = att.getLinkValue();
					if (irf == null)
						continue;
					Pair<XmlDocument, Long> pair = ba.getActualDocument(irf);
					if (pair == null)
						continue;

					XmlDocument irf_doc = pair.getLeft();

					String inherit_rights_from = ba.get_first_value_of_field(irf_doc, "inherit_rights_from");
					String link_to = irf;

					if (inherit_rights_from != null)
						if (irf_doc.getTypeId().equals("ead1b2fa113c45a8b79d093e8ec14728")
								|| irf_doc.getTypeId().equals("15206d33eafa440c84c02c8d912bce7a")
								|| irf_doc.getTypeId().equals("ec6d76a99d814d0496d5d879a0056428")
								|| irf_doc.getTypeId().equals("a0e50600ebe9450e916469ee698e3faa")
								|| irf_doc.getTypeId().equals("71e3890b3c77441bad288964bf3c3d6a")
								|| irf_doc.getTypeId().equals("cab21bf8b68a4b87ac37a5b41adad8a8")
								|| irf_doc.getTypeId().equals("110fa1f351e24a2bbc187c872b114ea4")
								|| irf_doc.getTypeId().equals("d539b253cb6247a381fb51f4ee34b9d8")
								|| irf_doc.getTypeId().equals("a7b5b15a99704c9481f777fa941506c0")
								|| irf_doc.getTypeId().equals("67588724c4c54b25a2c84906613bd15a"))
							link_to = inherit_rights_from;

					Individual link = new Individual();
					link.setUri(new_individual.getUri() + "_link_" + link_count);
					link_count++;
					link.addProperty("rdf:type", new Resource("v-s:Link", Type._Uri));
					link.addProperty("v-s:from", new Resource(new_individual.getUri(), Type._Uri));
					link.addProperty("v-s:to", new Resource("d:" + link_to, Type._Uri));
					putIndividual(level, link, ba_id);
					new_individual.addProperty("v-s:hasLink", new Resource(link.getUri(), Type._Uri));
				} else if (code.equals("content"))
				{
					new_individual.addProperty("v-s:description", new Resource(rss.resources.get(0).getData().replaceAll("\t", " "), Type._String));
				} else if (code.equals("class"))
				{
					String ttt = rss.resources.get(0).getData();

					if (ttt.equals("d:59b93765df9f4d548e3409752b925770") || ttt.equals("d:6e4d20f71ccc43678b76b924e7436994"))
						new_individual.addProperty("v-s:isFixedTerm", new Resource(true, Type._Bool));
					else
						new_individual.addProperty("v-s:isFixedTerm", new Resource(false, Type._Bool));
				}
			}
		}

		new_individual.addProperty("stg:hasDecreeKind", "d:6e2c0d2bad144e04a5ddd8b6ba5d0535", Type._Uri);

		res.add(new_individual);
		return res;
	}

}