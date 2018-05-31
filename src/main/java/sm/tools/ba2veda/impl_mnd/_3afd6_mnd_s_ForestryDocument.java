package sm.tools.ba2veda.impl_mnd;

import sm.tools.ba2veda.Ba2VedaTransform;
import sm.tools.ba2veda.BaSystem;
import sm.tools.ba2veda.Replacer;
import sm.tools.veda_client.VedaConnection;

public class _3afd6_mnd_s_ForestryDocument extends Ba2VedaTransform {
	public _3afd6_mnd_s_ForestryDocument(BaSystem _ba, VedaConnection _veda, Replacer replacer) {
		super(_ba, _veda, replacer, "3afd6d3af04a4c6a8d83f998f129409a", "mnd-s:ForestryDocument");
	}
	
	public void inital_set() {
		fields_map.put("kind", "v-s:hasDocumentKind");
		fields_map.put("department", "v-s:initiator");
		fields_map.put("date", "v-s:registrationDate");
		fields_map.put("attachment", "v-s:attachment");
		fields_map.put("comment", "v-s:rdfs:comment");
		fields_map.put("add_doc", "v-s:hasLink");
		fields_map.put("name", "rdfs:label");
		fields_map.put("number", "v-s:registrationNumber");
	}
}
