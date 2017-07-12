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

public class _5d588_mnd_s_Decree extends Ba2VedaTransform {
	public _5d588_mnd_s_Decree(BaSystem _ba, VedaConnection _veda, Replacer replacer) {
		super(_ba, _veda, replacer, "5d58831674654beba275600783490941", "mnd-s:Decree");
	}

	public void inital_set() {
		fields_map.put("to_department", "v-s:responsibleDepartment");
		fields_map.put("Заголовок", "v-s:title");
		fields_map.put("Инициатор", "v-s:initiator");
		fields_map.put("Подписывающий", "v-s:signedBy");
		fields_map.put("file", "v-s:attachment");
		fields_map.put("cause", "v-s:reason");
		fields_map.put("description", "v-s:description");
		fields_map.put("comment", "v-s:hasComment");
		fields_map.put("reg_note", "mnd-s:hasDecreeRegistrationRecord");
		fields_map.put("name", "rdfs:label");
		fields_map.put("recipient", "v-s:copyTo");
		
		fields_map.put("add_info", "?");
		fields_map.put("Связанные документы", "?");
		fields_map.put("nomenclature", "?");
	}
	
	@Override
	public List<Individual> transform(XmlDocument doc, String ba_id, String parent_veda_doc_uri,
		String parent_ba_doc_id, String path) throws Exception {
		
		String uri = prepare_uri(ba_id);
		List<Individual> res = new ArrayList<Individual>();

		Individual new_individual = new Individual();
		new_individual.setUri(uri);

		set_basic_fields(new_individual, doc);
		
		new_individual.addProperty("rdf:type", to_class, Type._Uri);
		int link_count = 0;
		List<XmlAttribute> atts = doc.getAttributes();
		for (XmlAttribute att : atts) {
			String code = att.getCode();

			String predicate = fields_map.get(code);
			System.out.println("CODE: " + code);
			if (predicate != null) {
				Resources rss = ba_field_to_veda(att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri,
						true);
					
				if (predicate.equals("?") == false)
					new_individual.addProperty(predicate, rss);
					
				if (code.equals("importance") && (rss == null || rss.resources.size() < 1))
					new_individual.addProperty("v-s:isActivityAccidental", new Resource(true, Type._Bool));
				
				
				if (rss == null)
					continue;

					
				if (rss.resources.size() < 1)
					continue;
				if (code.equals("nomenclature")) {
					String val = att.getRecordIdValue();
					switch (val) {
					case "b7a958be3d14437f98ab414c8ce88977":
						val = "d:b7a958be3d14437f98ab414c8ce88977";
						break;
					case "e5753b58168843e28ad73855c07b8cff":
						val = "d:e5753b58168843e28ad73855c07b8cff";
						break;
					case "391362e32a2b48199ef4bebe9b7a611a":
						val = "d:391362e32a2b48199ef4bebe9b7a611a";
						break;
					case "2283f9f186794145bd6c8e3229f7a009":
						val = "d:2283f9f186794145bd6c8e3229f7a009";
						break;
					case "9664874293574b79af624f01e3c091cd":
						val = "d:9664874293574b79af624f01e3c091cd";
						break;
					case "586ab1e3ea554a92ab6ebb64d2df8c41":
						val = "d:586ab1e3ea554a92ab6ebb64d2df8c41";
						break;
					case "4df228cb437747c89db2ed9e00fb4abc":
						val = "d:4df228cb437747c89db2ed9e00fb4abc";
						break;
					case "6e7555857c18459c9025e1a3513c296f":
						val = "d:6e7555857c18459c9025e1a3513c296f";
						break;
					case "bd763385e5d749c38a59e26afa77e5d3":
						val = "d:bd763385e5d749c38a59e26afa77e5d3";
						break;
					case "b39182d2e89b4f0ca0d06a219d949681":
						val = "d:b39182d2e89b4f0ca0d06a219d949681";
						break;
					case "32e779b7fa50470480b6d12427749dc5":
						val = "d:32e779b7fa50470480b6d12427749dc5";
						break;
					case "6cecc3c57826404abae33225db5a9c50":
						val = "d:6cecc3c57826404abae33225db5a9c50";
						break;
					case "ce99f0a34bbb4813ac88d3dfe7699a1a":
						val = "d:ce99f0a34bbb4813ac88d3dfe7699a1a";
						break;
					case "e5061f4736d3401899fd172f67c18f84":
						val = "d:e5061f4736d3401899fd172f67c18f84";
						break;
					case "c9dc77d387ff43ac8e88766aa66c7cae":
						val = "d:c9dc77d387ff43ac8e88766aa66c7cae";
						break;
					case "ca1d0c4ca12e44ba8ad7e3471970e67c":
						val = "d:ca1d0c4ca12e44ba8ad7e3471970e67c";
						break;
					case "6f64ab3a995c4851865136e58e88f7ed":
						val = "d:6f64ab3a995c4851865136e58e88f7ed";
						break;
					case "337906d27e484923aa8b4400e6fdc7a3":
						val = "d:337906d27e484923aa8b4400e6fdc7a3";
						break;
					case "c0d293bfced442d8864ba2aa450b8604":
						val = "d:c0d293bfced442d8864ba2aa450b8604";
						break;
					case "6b0f6cc7c96b49e4b5566316e75ab81e":
						val = "d:6b0f6cc7c96b49e4b5566316e75ab81e";
						break;
					case "4a293c55b37e492fad852401349b3072":
						val = "d:4a293c55b37e492fad852401349b3072";
						break;
					case "43b31005c8754e77ab2d472c907a81cd":
						val = "d:43b31005c8754e77ab2d472c907a81cd";
						break;
					case "cd74acdd370a4647851e588ab49e227f":
						val = "d:cd74acdd370a4647851e588ab49e227f";
						break;
					case "d67d40855c4c492e93f1f8e979b32915":
						val = "d:d67d40855c4c492e93f1f8e979b32915";
						break;
					case "93a91c7be4f84289b3a618ab4aaf9c99":
						val = "d:93a91c7be4f84289b3a618ab4aaf9c99";
						break;
					case "e5e9d7334e7142b98ce82b53aa9f1b4e":
						val = "d:e5e9d7334e7142b98ce82b53aa9f1b4e";
						break;
					case "506814f2866f4adc9f5effe3cbd75dab":
						val = "d:506814f2866f4adc9f5effe3cbd75dab";
						break;
					case "b79019ea82e24aaa94ec226ad10f7a46":
						val = "d:b79019ea82e24aaa94ec226ad10f7a46";
						break;
					case "5f3338bf470448958167688853db0b4a":
						val = "d:5f3338bf470448958167688853db0b4a";
						break;
					case "40fec391a071473bbafda39e26839cd0":
						val = "d:40fec391a071473bbafda39e26839cd0";
						break;
					case "897c15bc7cdf425c8106775ef31ecb17":
						val = "d:897c15bc7cdf425c8106775ef31ecb17";
						break;
					case "55f99c4789ad4a59a0d15231c2b9951a":
						val = "d:55f99c4789ad4a59a0d15231c2b9951a";
						break;
					case "51e1860e2e214488bfa013d5e7ed6426":
						val = "d:51e1860e2e214488bfa013d5e7ed6426";
						break;
					case "9c171b39761749dab3ca7891b67bf531":
						val = "d:9c171b39761749dab3ca7891b67bf531";
						break;
					case "d581c6a8c3224d3e801607af3357a4aa":
						val = "d:d581c6a8c3224d3e801607af3357a4aa";
						break;
					case "8b527be6d03742caa2756f560a4e10ba":
						val = "d:8b527be6d03742caa2756f560a4e10ba";
						break;
					case "57fed459b3734e709cd2627b8b08f0a0":
						val = "d:57fed459b3734e709cd2627b8b08f0a0";
						break;
					case "ed97548ed45047bcaf5de9ce05de205c":
						val = "d:ed97548ed45047bcaf5de9ce05de205c";
						break;
					case "61caa917c8584832ae8856ec9d982d41":
						val = "d:61caa917c8584832ae8856ec9d982d41";
						break;
					case "106cf5f7d76b4640a2bd771e05845c4b":
						val = "d:106cf5f7d76b4640a2bd771e05845c4b";
						break;
					case "d55ba7ababf846eda58e0940b4172ac8":
						val = "d:d55ba7ababf846eda58e0940b4172ac8";
						break;
					case "22fdcdee537a496785cbcf00747413fa":
						val = "d:b79019ea82e24aaa94ec226ad10f7a46";
						break;
					case "1e8a855407fb40d291572b3cdca2fa69":
						val = "d:40fec391a071473bbafda39e26839cd0";
						break;
					case "d883ae3de49448a391ea1bdedf70f6ce":
						val = "d:93a91c7be4f84289b3a618ab4aaf9c99";
						break;
					case "d45dfac9d94e4afdbdf4b9b857a1e6b7":
						val = "d:93a91c7be4f84289b3a618ab4aaf9c99";
						break;
					case "3027c61514d547309844fe89f6ba926b":
						val = "d:8b527be6d03742caa2756f560a4e10ba";
						break;
					case "a91fbe20c7484c4493ec4399bc82e6d9":
						val = "d:e5e9d7334e7142b98ce82b53aa9f1b4e";
						break;
					case "9bf4fce283ba407782f51ed3eb147eb4":
						val = "d:ed97548ed45047bcaf5de9ce05de205c";
						break;
					case "7ec843ff3ca14a718796fb6690942161":
						val = "d:506814f2866f4adc9f5effe3cbd75dab";
						break;
					case "ff290bb03958429f9523a1c5d48731d5":
						val = "d:d67d40855c4c492e93f1f8e979b32915";
						break;	
					case "337680c078f74a37b6b8517ff4022b90":
						val = "d:d581c6a8c3224d3e801607af3357a4aa";
						break;
					case "75164e9a01334599a22c15d268bfa26b":
						val = "d:cd74acdd370a4647851e588ab49e227f";
						break;
					case "97f63eb486774f2a91eace6ab02796c1":
						val = "d:106cf5f7d76b4640a2bd771e05845c4b";
						break;
					case "65be742ee07c41e4818556de69e64601":
						val = "d:d55ba7ababf846eda58e0940b4172ac8";
						break;
					case "cbdf12c7718743878c3a2bc0fb8c3fb4":
						val = "d:61caa917c8584832ae8856ec9d982d41";
						break;
					case "9a988aa44eab44fc86356f658144f35b":
						val = "d:897c15bc7cdf425c8106775ef31ecb17";
						break;
					case "211a2f22f04348adae83d6ae083bdef9":
						val = "d:4a293c55b37e492fad852401349b3072";
						break;
					case "8b686bea05f34835bb4935a4e3118123":
						val = "d:6b0f6cc7c96b49e4b5566316e75ab81e";
						break;
					case "939f082f4a1547178f6a4f8bcf4afff3":
						val = "d:2283f9f186794145bd6c8e3229f7a009";
						break;
					case "f81145e6103c41cab408dd8b32713fad":
						val = "d:9c171b39761749dab3ca7891b67bf531";
						break;
					case "c1f6f26bb98040fc97924b7cf66cabd6":
						val = "d:5f3338bf470448958167688853db0b4a";
						break;
					case "1f66b0f8fd394392bd620bd2a1b91687":
						val = "d:586ab1e3ea554a92ab6ebb64d2df8c41";
						break;
					case "3740b429d02344e3bce22f9d26286bfa":
						val = "d:9664874293574b79af624f01e3c091cd";
						break;
					case "2e7826bf4e5f4615ba7858b5cb3fb80f":
						val = "d:55f99c4789ad4a59a0d15231c2b9951a";
						break;
					case "1d95483f4291489cb18ddcfdbc4c6916":
						val = "d:c0d293bfced442d8864ba2aa450b8604";
						break;
					case "0199980c36ac48c89077a9c9c2881ac6":
						val = "d:337906d27e484923aa8b4400e6fdc7a3";
						break;
					case "4d96b846a3ef4e928a8eb708390c2bdd":
						val = "d:6f64ab3a995c4851865136e58e88f7ed";
						break;
					case "b482e6a6330a43608d3ff8e5554369d7":
						val = "d:ca1d0c4ca12e44ba8ad7e3471970e67c";
						break;
					case "e9958d2bf6a9455d8f954554aedeb415":
						val = "d:c9dc77d387ff43ac8e88766aa66c7cae";
						break;
					case "998f8d75923948258c1ab9e7b13637a1":
						val = "d:e5061f4736d3401899fd172f67c18f84";
						break;
					case "34d1b2a7b3e54d05adf8115b46bd9dce":
						val = "d:ce99f0a34bbb4813ac88d3dfe7699a1a";
						break;
					case "1c22aa85b55a4e64acba45b61df729aa":
						val = "d:b7a958be3d14437f98ab414c8ce88977";
						break;
					case "c4bb1067f2bf424eaa8e8c6ee4d20c37":
						val = "d:e5753b58168843e28ad73855c07b8cff";
						break;
					case "41fcb13619dd4560b1849b3db3dad475":
						val = "d:6e7555857c18459c9025e1a3513c296f";
						break;
					case "11a3e6535f8c4d818c59717ca5e911be":
						val = "d:391362e32a2b48199ef4bebe9b7a611a";
						break;
					case "0dc78db4add8498085b9d68d1444a6ba":
						val = "d:ed97548ed45047bcaf5de9ce05de205c";
						break;
					case "3676ab469fd9430f84e9474ed9d845a0":
						val = "d:d581c6a8c3224d3e801607af3357a4aa";
						break;
					case "4e5320641a0b4408a2baea33a083f512":
						val = "d:40fec391a071473bbafda39e26839cd0";
						break;
					case "6a8689bff0c941879c88c22d16d83807":
						val = "d:b79019ea82e24aaa94ec226ad10f7a46";
						break;
					case "e87384809b2744afbe789bf2492dd51a":
						val = "d:2283f9f186794145bd6c8e3229f7a009";
						break;
					case "76fea26122ba4e5fae34e6a8ffeaca2e":
						val = "d:586ab1e3ea554a92ab6ebb64d2df8c41";
						break;
						
					}
				} else if (code.equals("add_info")) {
					Individual comment = new Individual();
					comment.setUri(new_individual.getUri() + "_comment");
					comment.addProperty("rdf:type", new Resource("v-s:Comment", Type._Uri));
					comment.addProperty("v-s:creator", new_individual.getResources("v-s:creator"));
					comment.addProperty("v-s:created", new_individual.getResources("v-s:created"));
					comment.addProperty("v-s:attachment", rss);
					putIndividual(comment, ba_id, true);
					new_individual.addProperty("v-s:hasComment", new Resource(comment.getUri(), Type._Uri));
				} else if (code.equals("Связанные документы")) {
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
					putIndividual(link, ba_id, true);
					new_individual.addProperty("v-s:hasLink", new Resource(link.getUri(), Type._Uri));
				}
			}
		}
			
		new_individual.addProperty("rdf:type", to_class, Type._Uri);
		res.add(new_individual);
		return res;
	}

}