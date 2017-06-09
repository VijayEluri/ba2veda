package sm.tools.ba2veda.impl;

import java.util.ArrayList;
import java.util.List;

import ru.mndsc.bigarchive.server.kernel.document.beans.XmlDocument;
import sm.tools.ba2veda.Ba2VedaTransform;
import sm.tools.ba2veda.BaSystem;
import sm.tools.ba2veda.Replacer;
import sm.tools.veda_client.Individual;
import sm.tools.veda_client.VedaConnection;

public class _f063e_v_s_ClassifierOKVED extends Ba2VedaTransform
{
	public _f063e_v_s_ClassifierOKVED(BaSystem _ba, VedaConnection _veda, Replacer replacer)
	{
		super(_ba, _veda, replacer, "f063ec241db74fac9cc42437c785fdee", "v-s:ClassifierOKVED");
	}

	public void inital_set()
	{
	}

	@Override
	public List<Individual> transform(XmlDocument doc, String ba_id, String pareint_veda_uri, String parent_ba_doc_id, String path) throws Exception
	{
		String uri = prepare_uri(ba_id);

		List<Individual> res = new ArrayList<Individual>();

		Individual new_individual = new Individual();
		new_individual.setUri(uri);

		res.add(new_individual);

		return res;
	}
}
