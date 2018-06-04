package sm.tools.ba2veda.impl_mnd;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jfree.chart.axis.DateTickMarkPosition;

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

public class _f9260_mnd_s_LocalRegulatoryDocument extends Ba2VedaTransform {
	public _f9260_mnd_s_LocalRegulatoryDocument(BaSystem _ba, VedaConnection _veda, Replacer replacer)  {
		super(_ba, _veda, replacer, "f9260b106c4642df81dbc481aaf11bee1", "mnd-s:LocalRegulatoryDocument");
	}
	
	public void inital_set()
	{
		fields_map.put("department", "mnd-s:appliesTo");
		
		fields_map.put("date_to", "?");
		fields_map.put("number", "?");
		fields_map.put("title", "?");
		fields_map.put("period_provision", "?");
		fields_map.put("scope", "?");
		fields_map.put("initiator", "?");
		fields_map.put("date_from", "?");
		fields_map.put("date_to", "?");
		fields_map.put("attachment", "?");
		fields_map.put("description", "?");
		fields_map.put("kpi", "?");
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public List<Individual> transform(int level, XmlDocument doc, String ba_id, String parent_veda_doc_uri,
		String parent_ba_doc_id, String path) throws Exception {
		
		String uri = prepare_uri(ba_id);
		List<Individual> res = new ArrayList<Individual>();

		Individual new_individual = new Individual();
		new_individual.setUri(uri);

		set_basic_fields(level, new_individual, doc);
		
		new_individual.addProperty("rdf:type", new Resource(to_class, Type._Uri));
		new_individual.addProperty("v-s:hasDocumentKind", "d:653a839fec394a75bc060cd49404963f", Type._Uri);
		new_individual.addProperty("mnd-s:isAccessLimited", "false", Type._Bool);
		new_individual.addProperty("v-s:owner", "d:mondi_department_50002528", Type._Uri);
		new_individual.addProperty("v-s:hasSector", "d:550f04867ba247c49b9b7590d82fd32b", Type._Uri);
		
		Individual version = new Individual();
		version.setUri(new_individual.getUri() + "_1");
		version.addProperty("rdf:type", "mnd-s:VersionOfLocalRegulatoryDocument", Type._Uri);
		new_individual.addProperty("v-s:hasVersionOfLocalRegulatoryDocument", version.getUri(), Type._Uri);
		version.addProperty("v-s:registrationNumberAdd", "1", Type._String);
		version.addProperty("v-s:creator", new_individual.getResources("v-s:creator"));
		version.addProperty("v-s:created", new_individual.getResources("v-s:created"));
		version.addProperty("v-s:signedBy", "d:mondi_appointment_00024065_71017297", Type._Uri);
		version.addProperty("v-s:backwardProperty", "v-s:hasVersionOfLocalRegulatoryDocument", Type._Uri);
		version.addProperty("v-s:backwardTarget", new_individual.getUri(), Type._Uri);
		version.addProperty("v-s:canRead", "true", Type._Bool);
		
		String number = null;
		Resources titleRss = null;
		Resources periodProvision = null, scope = null;
		
		int linkCount = 0;
		
		List<XmlAttribute> atts = doc.getAttributes();
		for (XmlAttribute att : atts) {
			String code = att.getCode();

			String predicate = fields_map.get(code);
			System.out.println("CODE: " + code);
			
			if (predicate != null) {
				Resources rss = ba_field_to_veda(level, att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri,
						true);
					
				if (predicate.equals("?") == false)
					new_individual.addProperty(predicate, rss);
				
				if (rss == null)
					continue;
				
				if (rss.resources.size() < 1)
					continue;
				
				
				if (code.equals("date_to")) {
					Date now = new Date();
					Date dateTo = att.getDateValue();
					if (dateTo == null) {
						new_individual.addProperty("v-s:valid", "false", Type._Bool);
						version.addProperty("v-s:valid", "false", Type._Bool);
						continue;
					}
					
					if (now.getDate() != dateTo.getDate() || now.getMonth() != dateTo.getMonth() ||
						now.getYear() != dateTo.getYear()) { 
						new_individual.addProperty("v-s:valid", "false", Type._Bool);
						version.addProperty("v-s:valid", "false", Type._Bool);
					} else {
						new_individual.addProperty("v-s:valid", "true", Type._Bool);
						version.addProperty("v-s:valid", "true", Type._Bool);
					}
				} else if (code.equals("number")) {
					number = rss.resources.get(0).getData();
					while (number.length() < 3)
						number = "0" + number;
					
					number = "17001" + number;
					new_individual.addProperty("v-s:registrationNumber",  number, Type._String);
					version.addProperty("v-s:registrationNumber",  number + ".1", Type._String);
				} else if (code.equals("title")) {
					titleRss = rss;
					
					
					ArrayList<Object> parts = new ArrayList<Object>();
					parts.add("Показатель премирования. KPI - ");
					parts.add(rss);
					//parts.add(titleRss);
					
					String[] langs_out1 = { "EN", "RU" };
					String[] langs_out2 = { "NONE" };
					
					Resources rss1 = rs_assemble(parts.toArray(), langs_out1);
					if (rss1.resources.size() == 0)
						rss1 = rs_assemble(parts.toArray(), langs_out2);
						
					if (rss.resources.size() > 0) {
						new_individual.addProperty("v-s:title", rss1);
						version.addProperty("v-s:title", rss1);
					}
					//new_individual.addProperty("v-s:title", rss);
					version.addProperty("v-s:title", rss);
				} else if (code.equals("period_provision"))
					periodProvision = rss;
				else if (code.equals("scope"))
					scope = rss;
				else if (code.equals("initiator"))
					version.addProperty("v-s:initiator", rss);
				else if (code.equals("date_from"))
					version.addProperty("v-s:dateFrom", rss);
				else if (code.equals("date_to"))
					version.addProperty("v-s:dateTo", rss);
				else if (code.equals("attachment"))
					version.addProperty("v-s:attachment", rss);
				else if (code.equals("description"))
					version.addProperty("v-s:comment", rss);
				else if (code.equals("kpi")) {
					linkCount++;
					Individual link = new Individual();
					link.setUri(new_individual.getUri()+ "_link" + linkCount);
					link.addProperty("rdf:type", "v-s:Link", Type._Uri);
					link.addProperty("v-s:from", new_individual.getUri(), Type._Uri);
					link.addProperty("v-s:to", rss);
					link.addProperty("v-s:creator", new_individual.getResources("v-s:creator"));
					link.addProperty("v-s:created", new_individual.getResources("v-s:created"));
					new_individual.addProperty("v-s:hasLink", link.getUri(), Type._Uri);
					putIndividual(level, link, ba_id);
				}
			}
		}
		
		if (number != null && titleRss != null) {
			ArrayList<Object> parts = new ArrayList<Object>();
			parts.add(number);
			parts.add(" ");
			parts.add(titleRss);
			String[] langs_out1 = { "EN", "RU" };
			String[] langs_out2 = { "NONE" };
			
			Resources rss = rs_assemble(parts.toArray(), langs_out1);
			if (rss.resources.size() == 0)
				rss = rs_assemble(parts.toArray(), langs_out2);
				
			if (rss.resources.size() > 0) {
				new_individual.addProperty("rdfs:label", rss);
				version.addProperty("rdfs:label", rss);
			}
		}
		
		if (periodProvision != null && scope != null) {
			Individual comment = new Individual();
			comment.setUri(new_individual.getUri() + "_comment");
			comment.addProperty("rdf:type", "v-s:Comment", Type._Uri);
			comment.addProperty("v-s:creator", new_individual.getResources("v-s:creator"));
			comment.addProperty("v-s:created", new_individual.getResources("v-s:created"));
			comment.addProperty("v-s:parent", new_individual.getUri(), Type._Uri);
			
			ArrayList<Object> parts = new ArrayList<Object>();
			parts.add("Сроки предоставления: ");
			parts.add(periodProvision);
			parts.add("\n");
			parts.add("Группа сотрудников: ");
			parts.add(scope);
			parts.add("\n");
			//parts.add(titleRss);
			String[] langs_out1 = { "EN", "RU" };
			String[] langs_out2 = { "NONE" };
			
			Resources rss = rs_assemble(parts.toArray(), langs_out1);
			if (rss.resources.size() == 0)
				rss = rs_assemble(parts.toArray(), langs_out2);
				
			if (rss.resources.size() > 0)
				comment.addProperty("rdfs:label", rss);
			
			new_individual.addProperty("v-s:hasComment", comment.getUri(), Type._Uri);
			putIndividual(level, comment, ba_id);
		}
		
		putIndividual(level, version, ba_id);
		res.add(new_individual);
		return res;
	}
}

