package sm.tools.ba2veda.impl;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.groovy.runtime.metaclass.NewInstanceMetaMethod;

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

public class _23784_mnd_s_ItObject extends Ba2VedaTransform {
	public _23784_mnd_s_ItObject(BaSystem _ba, VedaConnection _veda, Replacer replacer)  {
		super(_ba, _veda, replacer, "23784302672a40a7b6e67e60bb5f2e5c", "mnd-s:ItObject");
	}
	
	private Resources mapping(String id) {
		Resources rss = new Resources();
		switch (id) {
		case "81fe0083e61e4f3fa1548be6912f6d85":
			rss.add("d:dooulb9brlk2wvpwijhw3l2oe0", Type._Uri);
			rss.add("d:81fe0083e61e4f3fa1548be6912f6d85", Type._Uri);
			break;
		case "e400a93bd5c5422c84007f9165dc1a2d":
			rss.add("d:ag1m4e2vtb59za9hiefer30cgc", Type._Uri);
			rss.add("d:a5mlitf5qistm1gynxvph127t13", Type._Uri);
			break;
		case "5f1e88625a1f45539ee41754109ecdf3":
			rss.add("d:ag1m4e2vtb59za9hiefer30cgc", Type._Uri);
			rss.add("d:a082mtokfoglyv8ycjhar4rnh9x", Type._Uri);
			break;
		case "0ab5b343e33f4f56abff07195a3817c2":
			rss.add("d:ag1m4e2vtb59za9hiefer30cgc", Type._Uri);
			rss.add("d:a082mtokfoglyv8ycjhar4rnh9x", Type._Uri);
			break;
		case "b16d3875605e443081aa4e3f626d465a":
			rss.add("d:ag1m4e2vtb59za9hiefer30cgc", Type._Uri);
			rss.add("d:a5mlitf5qistm1gynxvph127t13", Type._Uri);
			break;
		case "9ed0bfbf73474960a1222cebb693d822":
			rss.add("d:ag1m4e2vtb59za9hiefer30cgc", Type._Uri);
			rss.add("d:xsp6p90iwk1ipyjmy6gkxmbbp5", Type._Uri);
			break;
		case "662cdd348bdf488c85acafc310569cfc":
			rss.add("d:dooulb9brlk2wvpwijhw3l2oe0", Type._Uri);
			rss.add("d:662cdd348bdf488c85acafc310569cfc", Type._Uri);
			break;
		case "6ce7d81ed2ef48b0801a9dfa97f732d4":
			rss.add("d:dooulb9brlk2wvpwijhw3l2oe0", Type._Uri);
			rss.add("d:6ce7d81ed2ef48b0801a9dfa97f732d4", Type._Uri);
			break;
		case "31b6f9004a64403db1cd732e79b0d4c4":
			rss.add("d:vn65fuyucxdjho4ssxs36dfcvo", Type._Uri);
			rss.add("d:31b6f9004a64403db1cd732e79b0d4c4", Type._Uri);
			break;
		case "e7d19d1e248046c0be32ae82f2564466":
			rss.add("d:dooulb9brlk2wvpwijhw3l2oe0", Type._Uri);
			rss.add("d:e6cc12436a8443a7828d9f475b19fbab", Type._Uri);
			break;
		case "bc97beae34f94f76b85392634c229402":
			rss.add("d:dooulb9brlk2wvpwijhw3l2oe0", Type._Uri);
			rss.add("d:bc97beae34f94f76b85392634c229402", Type._Uri);
			break;
		case "3ef7f493e97f465fae78e9f129c15458":
			rss.add("d:ag1m4e2vtb59za9hiefer30cgc", Type._Uri);
			rss.add("d:d7cw3yc5exq6cgcwzyoffpqeos", Type._Uri);
			break;
		case "a06bd3d39db04f4c8250008511d67c63":
			rss.add("d:dooulb9brlk2wvpwijhw3l2oe0", Type._Uri);
			rss.add("d:a06bd3d39db04f4c8250008511d67c63", Type._Uri);
			break;
		case "0e5b4a3806dc40fcbe01333c5ad8aafa":
			rss.add("d:dooulb9brlk2wvpwijhw3l2oe0", Type._Uri);
			rss.add("d:0e5b4a3806dc40fcbe01333c5ad8aafa", Type._Uri);
			break;
		case "cbef4f79099342258ee2c3a5c139d2cb":
			rss.add("d:dooulb9brlk2wvpwijhw3l2oe0", Type._Uri);
			rss.add("d:cbef4f79099342258ee2c3a5c139d2cb", Type._Uri);
			break;
		case "9c4b11850510469c976e62c9f3e59fe8":
			rss.add("d:dooulb9brlk2wvpwijhw3l2oe0", Type._Uri);
			rss.add("d:cbef4f79099342258ee2c3a5c139d2cb", Type._Uri);
			break;
		case "625304f8cac041f2acfed16ee04b3c03":
			rss.add("d:dooulb9brlk2wvpwijhw3l2oe0", Type._Uri);
			rss.add("d:625304f8cac041f2acfed16ee04b3c03", Type._Uri);
			break;
		case "5c37e242d8cc497c85c97ce2180ecd8d":
			rss.add("d:dooulb9brlk2wvpwijhw3l2oe0", Type._Uri);
			rss.add("d:5c37e242d8cc497c85c97ce2180ecd8d", Type._Uri);
			break;
		case "09ab980f144e4720a8be23b66ccffa5c":
			rss.add("d:dooulb9brlk2wvpwijhw3l2oe0", Type._Uri);
			rss.add("d:09ab980f144e4720a8be23b66ccffa5c", Type._Uri);
			break;
		case "70e62f2ead394cfb82461e3b0a903048":
			rss.add("d:dooulb9brlk2wvpwijhw3l2oe0", Type._Uri);
			rss.add("d:70e62f2ead394cfb82461e3b0a903048", Type._Uri);
			break;
		case "be4cb0a6f6bc4cc8bbe2ce7d1ba44c75":
			rss.add("d:dooulb9brlk2wvpwijhw3l2oe0", Type._Uri);
			rss.add("d:be4cb0a6f6bc4cc8bbe2ce7d1ba44c75", Type._Uri);
			break;
		case "d0ace20a670f421d968c72379800d347":
			rss.add("d:lb2s2gu1ftbr792adcgq51y6b6", Type._Uri);
			rss.add("d:a9yp0rm7lkth09cahl7kl7cqa08", Type._Uri);
			break;
		case "5558ff7bcecc40bea1253691a7f0f57e":
			rss.add("d:ag1m4e2vtb59za9hiefer30cgc", Type._Uri);
			rss.add("d:d7cw3yc5exq6cgcwzyoffpqeos", Type._Uri);
			break;
		case "35f218d025de4a749b72a0fb0b845fb6":
			rss.add("d:ag1m4e2vtb59za9hiefer30cgc", Type._Uri);
			rss.add("d:d7cw3yc5exq6cgcwzyoffpqeos", Type._Uri);
			break;
		case "f0c7e2b48fc2435989d36c4530b79d04":
			rss.add("d:ag1m4e2vtb59za9hiefer30cgc", Type._Uri);
			rss.add("d:d7cw3yc5exq6cgcwzyoffpqeos", Type._Uri);
			break;
		case "608964a0ad39441c8d18a93bf0ebc86f":
			rss.add("d:ag1m4e2vtb59za9hiefer30cgc", Type._Uri);
			rss.add("d:d7cw3yc5exq6cgcwzyoffpqeos", Type._Uri);
			break;
		case "1158eee0df29457daa03a67a4e3c91e8":
			rss.add("d:ag1m4e2vtb59za9hiefer30cgc", Type._Uri);
			rss.add("d:a5mlitf5qistm1gynxvph127t13", Type._Uri);
			break;
		case "0313621f6892455fac54ed1c8b9589ae":
			rss.add("d:ag1m4e2vtb59za9hiefer30cgc", Type._Uri);
			rss.add("d:a5mlitf5qistm1gynxvph127t13", Type._Uri);
			break;
		case "b482787aa76f4f7b8d6aa37c292a41f6":
			rss.add("d:vn65fuyucxdjho4ssxs36dfcvo", Type._Uri);
			rss.add("d:b482787aa76f4f7b8d6aa37c292a41f6", Type._Uri);
			break;
		case "c05d6664ec5d49eb9700ad518091e545":
			rss.add("d:vn65fuyucxdjho4ssxs36dfcvo", Type._Uri);
			rss.add("d:c05d6664ec5d49eb9700ad518091e545", Type._Uri);
			break;
		case "61315f150bd247629c258c3d05f281a7":
			rss.add("d:vn65fuyucxdjho4ssxs36dfcvo", Type._Uri);
			rss.add("d:61315f150bd247629c258c3d05f281a7", Type._Uri);
			break;
		case "820a163af2404caf81e76f8b1cb120ba":
			rss.add("d:vn65fuyucxdjho4ssxs36dfcvo", Type._Uri);
			rss.add("d:820a163af2404caf81e76f8b1cb120ba", Type._Uri);
			break;
		case "02014214c8704d1ba0c25ea292a5b767":
			rss.add("d:ag1m4e2vtb59za9hiefer30cgc", Type._Uri);
			rss.add("d:d7cw3yc5exq6cgcwzyoffpqeos", Type._Uri);
			break;
		case "a944bf9051c441e6a2818a85c5fb3854":
			rss.add("d:dooulb9brlk2wvpwijhw3l2oe0", Type._Uri);
			rss.add("d:a944bf9051c441e6a2818a85c5fb3854", Type._Uri);
			break;
		case "95bd09bdc37f4b68a257b690a0c900ba":
			rss.add("d:dooulb9brlk2wvpwijhw3l2oe0", Type._Uri);
			rss.add("d:95bd09bdc37f4b68a257b690a0c900ba", Type._Uri);
			break;
		case "b699e2c5ea83449fa21f6702ede871bb":
			rss.add("d:dooulb9brlk2wvpwijhw3l2oe0", Type._Uri);
			rss.add("d:b699e2c5ea83449fa21f6702ede871bb", Type._Uri);
			break;
		case "ddb1acf683e548e1bb1cf08bff41d569":
			rss.add("d:dooulb9brlk2wvpwijhw3l2oe0", Type._Uri);
			rss.add("d:ddb1acf683e548e1bb1cf08bff41d569", Type._Uri);
			break;
		case "c8468efa0d954d1292472b189fe5d0d8":
			rss.add("d:ag1m4e2vtb59za9hiefer30cgc", Type._Uri);
			rss.add("d:d7cw3yc5exq6cgcwzyoffpqeos", Type._Uri);
			break;
		case "6b0d53aa4e674ab78191570ca4b5d742":
			rss.add("d:dooulb9brlk2wvpwijhw3l2oe0", Type._Uri);
			rss.add("d:6b0d53aa4e674ab78191570ca4b5d742", Type._Uri);
			break;
		case "fe7c9a7aadd4432abf50e467185a9505":
			rss.add("d:dooulb9brlk2wvpwijhw3l2oe0", Type._Uri);
			rss.add("d:fe7c9a7aadd4432abf50e467185a9505", Type._Uri);
			break;
		case "9f0efbf978c1412f9a612ac6afaa638a":
			rss.add("d:ag1m4e2vtb59za9hiefer30cgc", Type._Uri);
			rss.add("d:a5mlitf5qistm1gynxvph127t13", Type._Uri);
			break;
		case "743e3949ffc2487e8b3fb10f4ddb5930":
			rss.add("d:ag1m4e2vtb59za9hiefer30cgc", Type._Uri);
			rss.add("d:d7cw3yc5exq6cgcwzyoffpqeos", Type._Uri);
			break;
		case "6e12a53d7135440790487162e0f529c3":
			rss.add("d:ag1m4e2vtb59za9hiefer30cgc", Type._Uri);
			rss.add("d:d7cw3yc5exq6cgcwzyoffpqeos", Type._Uri);
			break;
		case "32fdffcdb0844cecb1cb577daae5dce3":
			rss.add("d:ag1m4e2vtb59za9hiefer30cgc", Type._Uri);
			rss.add("d:a5mlitf5qistm1gynxvph127t13", Type._Uri);
			break;
			
		case "19cd3b702502403db251fab6f5207494":
			rss.add("d:lb2s2gu1ftbr792adcgq51y6b6", Type._Uri);
			rss.add("d:p49ghdtk4fb9677lthkqhvyd91", Type._Uri);
			break;
		case "f0ae8e26f1904e2ab28ddcac12711b2d":
			rss.add("d:vn65fuyucxdjho4ssxs36dfcvo", Type._Uri);
			rss.add("d:b482787aa76f4f7b8d6aa37c292a41f6", Type._Uri);
			break;
		}
		
		return rss;
	}

	@Override
	public void inital_set() {
		// TODO Auto-generated method stub
		fields_map.put("name", "v-s:title");
		fields_map.put("responsible", "v-s:responsible");
		fields_map.put("parent_unit", "v-s:hasParentLink");
		fields_map.put("date_from", "v-s:dateFrom");
		fields_map.put("date_to", "v-s:dateTo");
		fields_map.put("attachment", "v-s:attachment");
		fields_map.put("comment", "v-s:description");
		fields_map.put("compound_title", "rdfs:label");
		
		fields_map.put("use_db", "?");
		fields_map.put("use_po", "?");
		fields_map.put("use_to", "?");
		fields_map.put("access_for_uz", "?");
		fields_map.put("use_report", "?");
		fields_map.put("use_license", "?");
		fields_map.put("use_authentication", "?");
		fields_map.put("use_energy", "?");
		fields_map.put("add_doc", "?");
		fields_map.put("unit", "?");
		
		fields_map.put("system_requirements", "?");
		fields_map.put("account_number", "?");
		fields_map.put("license_count", "?");
		
		fields_map.put("theme", "?");	
	}
	
	int linkCount = 1;
	
	private Individual createLink(Individual indiv, Resources rss, String comment) {
		Individual link = new Individual();
		link.setUri(String.format("%s_link%d", indiv.getUri(), linkCount));
		link.addProperty("rdf:type", "v-s:Link", Type._Uri);
		link.addProperty("v-s:created", indiv.getResources("v-s:created"));
		link.addProperty("v-s:creator", indiv.getResources("v-s:creator"));
		link.addProperty("v-s:from", indiv.getUri(), Type._Uri);
		link.addProperty("v-s:to", rss);
		if (comment != null)
			link.addProperty("rdfs:comment", comment, Type._String);
		linkCount++;
		indiv.addProperty("v-s:hasLink", link.getUri(), Type._Uri);
		
		return link;
	}
	
	int commentCount = 1;
	
	private Individual createComment(Individual indiv, Resources rss, String str) {
		Individual comment = new Individual();
		comment.setUri(String.format("%s_comment%d", indiv.getUri(), commentCount));
		comment.addProperty("rdf:type", "v-s:Comment", Type._Uri);
		comment.addProperty("v-s:created", indiv.getResources("v-s:created"));
		comment.addProperty("v-s:creator", indiv.getResources("v-s:creator"));
		comment.addProperty("rdfs:label", str + " " + rss.resources.get(0).getData(), Type._String);
		indiv.addProperty("v-s:hasComment", comment.getUri(), Type._Uri);
		
		commentCount++;
		return comment;
	}
	
	@Override
	public List<Individual> transform(int level, XmlDocument doc, String ba_id, String parent_veda_doc_uri,
		String parent_ba_doc_id, String path) throws Exception {
		
		String uri = prepare_uri(ba_id);
		List<Individual> res = new ArrayList<Individual>();

		Individual new_individual = new Individual();
		new_individual.setUri(uri);

		set_basic_fields(level, new_individual, doc);
		
		new_individual.addProperty("rdf:type", new Resource(to_class, Type._Uri));

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
					
				if (code.equals("importance") && (rss == null || rss.resources.size() < 1))
					new_individual.addProperty("v-s:isActivityAccidental", new Resource(true, Type._Bool));
				
				
				if (rss == null)
					continue;
				
				if (rss.resources.size() < 1)
					continue;
				
				if (code.equals("use_db"))
					putIndividual(level, createLink(new_individual, rss, "Источники данных"), ba_id);
				else if (code.equals("use_po"))
					putIndividual(level, createLink(new_individual, rss, "Программное обеспечение"), ba_id);
				else if (code.equals("use_to"))
					putIndividual(level, createLink(new_individual, rss, "Техническое обеспечение"), ba_id);
				else if (code.equals("access_for_uz"))
					putIndividual(level, createLink(new_individual, rss, "Учетные записи"), ba_id);
				else if (code.equals("use_report"))
					putIndividual(level, createLink(new_individual, rss, "Формирование отчетов"), ba_id);
				else if (code.equals("use_license"))
					putIndividual(level, createLink(new_individual, rss, "Лицензирование"), ba_id);
				else if (code.equals("use_authentication"))
					putIndividual(level, createLink(new_individual, rss, "Аутентификация"), ba_id);
				else if (code.equals("use_energy"))
					putIndividual(level, createLink(new_individual, rss, "Энергобезопасность"), ba_id);
				else if (code.equals("unit"))
					putIndividual(level, createLink(new_individual, rss, "Комплект поставки"), ba_id);
				else if (code.equals("add_doc"))
					putIndividual(level, createLink(new_individual, rss, null), ba_id);
				else if (code.equals("system_requirements"))
					putIndividual(level, createComment(new_individual, rss, "Системные требования:"), ba_id);
				else if (code.equals("account_number"))
					putIndividual(level, createComment(new_individual, rss, 
						"Номер основного средства:"), ba_id);
				else if (code.equals("license_count"))
					putIndividual(level, createComment(new_individual, rss, 
						"Количество лицензий:"), ba_id);
				else if (code.equals("theme")) {
					Resources rss1 = mapping(att.getRecordIdValue());
					if (rss1.resources.size() > 0) {
						new_individual.addProperty("mnd-s:hasItSector", rss1.resources.get(0).getData(), Type._Uri);
						new_individual.addProperty("mnd-s:hasItObjectType", rss1.resources.get(1).getData(), Type._Uri);
					}
				}
			}
				
		}
			
		new_individual.addProperty("rdf:type", to_class, Type._Uri);
		res.add(new_individual);
		return res;
	}
}
