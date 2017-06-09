package sm.tools.ba2veda.impl;

import sm.tools.ba2veda.Ba2VedaTransform;
import sm.tools.ba2veda.BaSystem;
import sm.tools.ba2veda.Replacer;
import sm.tools.veda_client.VedaConnection;

public class _99f08_v_s_LetterRegistrationRecordSender extends Ba2VedaTransform
{
	public _99f08_v_s_LetterRegistrationRecordSender(BaSystem _ba, VedaConnection _veda, Replacer replacer)
	{
		super(_ba, _veda, replacer, "99f08b9aa1284ec5a3ea09724370d94e", "v-s:LetterRegistrationRecordSender");
	}

	public void inital_set()
	{
		fields_map.put("name", "rdfs:label");
		fields_map.put("add_to_doc", "v-s:parent");
		fields_map.put("reg_number", "v-s:registrationNumber");
		fields_map.put("date_from", "v-s:registrationDate");
		fields_map.put("comment", "rdfs:comment");
	}

}
