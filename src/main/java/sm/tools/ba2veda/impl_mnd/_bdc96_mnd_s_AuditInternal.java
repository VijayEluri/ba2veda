package sm.tools.ba2veda.impl_mnd;

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

public class _bdc96_mnd_s_AuditInternal extends Ba2VedaTransform
{
	public _bdc96_mnd_s_AuditInternal(BaSystem _ba, VedaConnection _veda, Replacer replacer)
	{
		super(_ba, _veda, replacer, "bdc96094278b4c4f805d0212530d34d7", "mnd-s:AuditInternal");
	}

	public void inital_set()
	{
		fields_map.put("name", "rdfs:label");
		fields_map.put("number", "v-s:registrationNumber");
		fields_map.put("audit_name", "v-s:theme");
		
		fields_map.put("company", "mnd-s:hasAuditInternalObject");
		fields_map.put("auditor", "v-s:auditor");
		fields_map.put("date_from", "v-s:dateFromFact");
		fields_map.put("date_to", "v-s:dateToFact");
		fields_map.put("risk_size", "mnd-s:auditInternalRiskSize");
		fields_map.put("risk_quality", "mnd-s:auditInternalRiskQuality");
		fields_map.put("attachment", "v-s:attachment");
		fields_map.put("attachment_standards", "v-s:attachment");
		fields_map.put("action", "v-s:hasAction");
		fields_map.put("public_date", "v-s:published");
		fields_map.put("coordination_date", "mnd-s:auditInternalCoordinationDate");
		fields_map.put("comment", "rdfs:comment");
		fields_map.put("status_ex", "v-s:hasStatus");
		fields_map.put("add_doc", "v-s:hasLink");
		
		fields_map.put("event_kind", "?");
	}

	@Override
	public List<Individual> transform(int level, XmlDocument doc, String ba_id, String parent_veda_doc_uri,
			String parent_ba_doc_id, String path) throws Exception {
		String uri = prepare_uri(ba_id);
		List<Individual> res = new ArrayList<Individual>();

		Individual new_individual = new Individual();
		new_individual.setUri(uri);

		set_basic_fields(level, new_individual, doc);

		new_individual.addProperty("rdf:type", to_class, Type._Uri);
		
		new_individual.addProperty("mnd-s:hasDecreeKind", new Resource("d:e5753b58168843e28ad73855c07b8cff", Type._Uri));
		
		Individual drtr = null;
		Individual comment = null;
		ArrayList<Object> comment_parts = new ArrayList<Object>();
		
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
				
				if (code.equals("event_kind")) {
					String val = att.getRecordIdValue();
					switch (val) {
					case "662b7b2a611446f88fa6f7353bdf47ca":
						new_individual.addProperty("mnd-s:auditInternalPlanned", new Resource(true, Type._Bool));
						break;
					case "b4519ba4667b4f26b8f8a9ec03e07a78":
						new_individual.addProperty("mnd-s:auditInternalPlanned", new Resource(false, Type._Bool));
						break;
					}
				}
			}
		}
		
	
		res.add(new_individual);
		return res;
	}
}
