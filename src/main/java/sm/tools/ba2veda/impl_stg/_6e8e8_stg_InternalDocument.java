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

public class _6e8e8_stg_InternalDocument extends Ba2VedaTransform
{
	public _6e8e8_stg_InternalDocument(BaSystem _ba, VedaConnection _veda, Replacer replacer)
	{
		super(_ba, _veda, replacer, "6e8e888f2dfa459287e1f369e16ee4b6", "stg:InternalDocument");
	}

	public void inital_set()
	{
		fields_map.put("kind", "v-s:hasDocumentKind");
		fields_map.put("addressee_copy", "v-s:copyTo");
		fields_map.put("subject", "v-s:theme");
		fields_map.put("content", "v-s:content");
		fields_map.put("attachment", "v-s:attachment");
		fields_map.put("add_info", "v-s:hasComment");
		fields_map.put("link_document", "?");
		fields_map.put("comment", "?");
		fields_map.put("sender", "?");
		fields_map.put("addressee", "?");
		
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
		Resources from_comment = null;
		Resources to_comment = null;

		ArrayList<Object> comment_parts = new ArrayList<Object>();

		new_individual.addProperty("rdf:type", to_class, Type._Uri);

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
					String link_to = irf;

					Individual link = new Individual();
					link.addProperty("rdf:type", new Resource("v-s:Link", Type._Uri));
					link.setUri(ba_id + "_" + link_to);
					link.addProperty("v-s:from", new Resource(new_individual.getUri(), Type._Uri));
					link.addProperty("v-s:to", new Resource("d:" + link_to, Type._Uri));
					putIndividual(level, link, ba_id);
					new_individual.addProperty("v-s:hasLink", new Resource(link.getUri(), Type._Uri));
				}
/*				else if (code.equals("add_info"))
				{
					Individual comment = new Individual();
					comment.setUri(new_individual.getUri() + "_comment");
					comment.addProperty("rdf:type", "v-s:Comment", Type._Uri);
					comment.addProperty("rdfs:label", rss);
					comment.addProperty("v-s:backwardTarget", new_individual.getUri(), Type._Uri);
					comment.addProperty("v-s:backwardProperty", "v-s:hasComment", Type._Uri);
					comment.addProperty("v-s:created", new_individual.getResources("v-s:created"));
					comment.addProperty("v-s:creator", new_individual.getResources("v-s:creator"));
					putIndividual(level, comment, ba_id);
					new_individual.addProperty("v-s:hasComment", comment.getUri(), Type._Uri);
				} */
				 else if (code.equals("sender"))
				{
					Individual ii = veda.getIndividual(rss.resources.get(0).getData());
					if (ii != null)
					{
						from_comment = ii.getResources("rdfs:label");
						new_individual.addProperty("v-s:initiator", ii.getUri(), Type._Uri);
					}
				} else if (code.equals("addressee"))
				{
					Individual ii = veda.getIndividual(rss.resources.get(0).getData());
					if (ii != null)
					{
						to_comment = ii.getResources("rdfs:label");
						new_individual.addProperty("v-s:responsibleDepartment", ii.getUri(), Type._Uri);
					}

				}

			}
		}

		if (from_comment != null)
		{
			comment_parts.add("От кого");
			comment_parts.add(from_comment);
			comment_parts.add("\n");
		}
		if (to_comment != null)
		{
			comment_parts.add("Кому");
			comment_parts.add(to_comment);
		}

		if (comment_parts.size() > 0)
		{
			String[] langs_out1 =
			{ "EN", "RU" };
			String[] langs_out2 =
			{ "NONE" };

			Resources rss = rs_assemble(comment_parts.toArray(), langs_out1);
			if (rss.resources.size() == 0)
				rss = rs_assemble(comment_parts.toArray(), langs_out2);

			new_individual.addProperty("rdfs:comment", rss);
		}

		res.add(new_individual);
		return res;
	}
}