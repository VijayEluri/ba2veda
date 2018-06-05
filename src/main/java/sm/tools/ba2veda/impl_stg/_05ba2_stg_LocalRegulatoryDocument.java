package sm.tools.ba2veda.impl_stg;

import java.util.ArrayList;
import java.util.Date;
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

public class _05ba2_stg_LocalRegulatoryDocument extends Ba2VedaTransform
{
	public _05ba2_stg_LocalRegulatoryDocument(BaSystem _ba, VedaConnection _veda, Replacer replacer)
	{
		super(_ba, _veda, replacer, "05ba26785ce54fb6b63df1d2b01b4544", "stg:LocalRegulatoryDocument");
	}

	public void inital_set()
	{
		fields_map.put("document_type", "v-s:hasDocumentKind");
		fields_map.put("document_code", "v-s:shortLabel");
		fields_map.put("register_number", "v-s:registrationNumber");
		fields_map.put("registration_date", "?");
		fields_map.put("topic", "v-s:title");
		fields_map.put("marks", "rdfs:comment");
		fields_map.put("Signer", "?");
		fields_map.put("urgency", "?");
		fields_map.put("linked_documents", "?");
		fields_map.put("end_date", "?");
		fields_map.put("initiator", "?");
		fields_map.put("topic", "?");
		fields_map.put("attachment", "?");
		fields_map.put("made_mark", "?");

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

		new_individual.addProperty("rdf:type", new Resource(to_class, Type._Uri));
		//		new_individual.addProperty("v-s:hasDocumentKind", "d:653a839fec394a75bc060cd49404963f", Type._Uri);
		new_individual.addProperty("v-s:owner", "d:org_RU1121016110_1", Type._Uri);

		Individual version = new Individual();
		version.setUri(new_individual.getUri() + "_1");
		version.addProperty("rdf:type", "stg:VersionOfLocalRegulatoryDocument", Type._Uri);
		new_individual.addProperty("v-s:hasVersionOfLocalRegulatoryDocument", version.getUri(), Type._Uri);
		version.addProperty("v-s:registrationNumberAdd", "1", Type._Integer);
		version.addProperty("v-s:creator", new_individual.getResources("v-s:creator"));
		version.addProperty("v-s:created", new_individual.getResources("v-s:created"));
		version.addProperty("v-s:dateFrom", new_individual.getResources("v-s:created"));
		version.addProperty("v-s:backwardProperty", "v-s:hasVersionOfLocalRegulatoryDocument", Type._Uri);
		version.addProperty("v-s:backwardTarget", new_individual.getUri(), Type._Uri);
		version.addProperty("v-s:canRead", "true", Type._Bool);

		String number = null;
		Date end_date = null;
		Date registration_date = null;
		String urgency = null;

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
/*
				if (code.equals("made_mark"))
				{
					Individual comment = new Individual();
					comment.addProperty("rdf:type", new Resource("v-s:Comment", Type._Uri));
					comment.addProperty("v-s:parent", new Resource(new_individual.getUri(), Type._Uri));
					comment.addProperty("v-s:created", new_individual.getResources("v-s:created"));
					comment.addProperty("v-s:creator", new_individual.getResources("v-s:creator"));
					comment.setUri(new_individual.getUri() + "_comment");
					comment.addProperty("v-s:backwardTarget", new Resource(new_individual.getUri(), Type._Uri));
					comment.addProperty("v-s:backwardProperty", new Resource("v-s:hasComment", Type._Uri));
					comment.addProperty("rdfs:label", rss);
					new_individual.addProperty("v-s:hasComment", new Resource(comment.getUri(), Type._Uri));
					putIndividual(level, comment, null);
				} else */
				if (code.equals("end_date"))
				{
					end_date = att.getDateValue();
				} else if (code.equals("linked_documents"))
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
				} else if (code.equals("Signer"))
				{
					version.addProperty("v-s:signedBy", rss);
				} else if (code.equals("urgency"))
				{
					urgency = att.getRecordIdValue();
				} else if (code.equals("register_number"))
				{
					number = rss.resources.get(0).getData();
					new_individual.addProperty("v-s:registrationNumber", number, Type._String);
				} else if (code.equals("topic"))
				{
					version.addProperty("v-s:title", rss);
					new_individual.addProperty("v-s:title", rss);
				} else if (code.equals("initiator"))
				{
					version.addProperty("v-s:initiator", rss);
				} else if (code.equals("registration_date"))
				{
					registration_date = att.getDateValue();
					version.addProperty("v-s:registrationDate", rss);
					new_individual.addProperty("v-s:registrationDate", rss);
				} else if (code.equals("date_to"))
					version.addProperty("v-s:dateTo", rss);
				else if (code.equals("attachment"))
					version.addProperty("v-s:attachment", rss);
				else if (code.equals("description"))
					version.addProperty("v-s:comment", rss);
			}
		}

		if (registration_date == null)
		{
			version.addProperty("v-s:registrationDate", doc.getDateCreated());
			new_individual.addProperty("v-s:registrationDate", doc.getDateCreated());
		}

		if (urgency != null)
		{
			Date now = new Date();
			if (end_date != null && now.before(end_date)
					&& (urgency.equals("6e4d20f71ccc43678b76b924e7436994") || urgency.equals("59b93765df9f4d548e3409752b925770")))
			{
				new_individual.addProperty("v-s:valid", "false", Type._Bool);
				version.addProperty("v-s:valid", "false", Type._Bool);
			}

			if (end_date == null && (urgency.equals("6e4d20f71ccc43678b76b924e7436994") || urgency.equals("59b93765df9f4d548e3409752b925770")))
			{
				new_individual.addProperty("v-s:valid", "true", Type._Bool);
				version.addProperty("v-s:valid", "true", Type._Bool);
			}

			if (urgency.equals("01b2ab5487c6444b84b0c57d26aa53d7"))
			{
				new_individual.addProperty("v-s:valid", "true", Type._Bool);
				version.addProperty("v-s:valid", "true", Type._Bool);

				new_individual.addProperty("v-s:isFixedTerm", "false", Type._Bool);
				version.addProperty("v-s:isFixedTerm", "false", Type._Bool);
			}

			if (urgency.equals("6e4d20f71ccc43678b76b924e7436994") || urgency.equals("59b93765df9f4d548e3409752b925770"))
			{
				new_individual.addProperty("v-s:isFixedTerm", "true", Type._Bool);
				version.addProperty("v-s:isFixedTerm", "true", Type._Bool);
			}

		}

		//long sc0 = Ba2VedaTransform.get_count_of_queue("scripts_main0");
		//long sc1 = sc0;

		putIndividual(level, new_individual, ba_id);

		//while (sc1 <= sc0)
		//	sc1 = Ba2VedaTransform.get_count_of_queue("scripts_main0");

		Thread.currentThread().sleep(1000);

		Individual nnn = veda.getIndividual(new_individual.getUri());

		if (nnn != null)
		{
			String sss = nnn.getValue("v-s:registrationNumber");

			if (sss != null)
			{
				version.addProperty("v-s:registrationNumber", sss + ".1", Type._String);
			}
		}

		putIndividual(level, version, ba_id + "_1");

		res.add(new_individual);
		return res;
	}
}
