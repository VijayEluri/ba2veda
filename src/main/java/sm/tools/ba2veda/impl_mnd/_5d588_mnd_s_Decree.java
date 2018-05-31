package sm.tools.ba2veda.impl_mnd;

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
//		fields_map.put("status", "v-s:hasStatus");
		fields_map.put("add_info", "v-s:hasComment");
		
		fields_map.put("Связанные документы", "?");
		fields_map.put("nomenclature", "?");
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
		int link_count = 0;
		int ncomments = 1;
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
					case "4deabdd9909b4b178c977cbf079536d3":
						val = "d:506814f2866f4adc9f5effe3cbd75dab";
						break;
					case "1db30b0d81e34e43b0696ae39293ef7b":
						val = "d:e5e9d7334e7142b98ce82b53aa9f1b4e";
						break;
					case "0630eef1a5d741278a1eed024454e4c9":
						val = "d:8b527be6d03742caa2756f560a4e10ba";
						break;
					case "3ec030307fe84d24ab5fe100f93c5648":
						val = "d:93a91c7be4f84289b3a618ab4aaf9c99";
						break;
					case "86f1160b161d406baec0d17e1eb12444":
						val = "d:57fed459b3734e709cd2627b8b08f0a0";
						break;
					case "73dd3b95b8ea4f2bbdb6f228abbdfb7e":
						val = "d:d67d40855c4c492e93f1f8e979b32915";
						break;
					case "b1b0d700a6ac4e818f61b9ad283abf64":
						val = "d:cd74acdd370a4647851e588ab49e227f";
						break;
					case "3eaae80a7046418687538dd272c38667":
						val = "d:106cf5f7d76b4640a2bd771e05845c4b";
						break;
					case "32b9ce5766654912a33863891c0a6b3d":
						val = "d:d55ba7ababf846eda58e0940b4172ac8";
						break;
					case "0b82becdef5e45bcbe55b89e7958ae83":
						val = "d:61caa917c8584832ae8856ec9d982d41";
						break;
					case "2898698f69024cb58f0c863396684641":
						val = "d:897c15bc7cdf425c8106775ef31ecb17";
						break;
					case "3a398f5db31a44cf83ce29e759329c14":
						val = "d:4a293c55b37e492fad852401349b3072";
						break;
					case "78fd38fdba834d23a045fe246a09ac3e":
						val = "d:6b0f6cc7c96b49e4b5566316e75ab81e";
						break;
					case "72cce4f3d505486db70756a5b94751ab":
						val = "d:9c171b39761749dab3ca7891b67bf531";
						break;
					case "0d5f29aae4f84e7b8341545345c3f4f1:":
						val = "d:5f3338bf470448958167688853db0b4a";
						break;
					case "e066392a80d346aaba1c612422fb129e":
						val = "d:9664874293574b79af624f01e3c091cd";
						break;
					case "2efc30611f684202a39c420552da08f6":
						val = "d:55f99c4789ad4a59a0d15231c2b9951a";
						break;
					case "f1aa44bae7a048888d8db7360fc239f4":
						val = "d:b7a958be3d14437f98ab414c8ce88977";
						break;
					case "00d8673068554263bb7e7cb275ead27c":
						val = "d:e5753b58168843e28ad73855c07b8cff";
						break;
					case "3b9826766e634ec5a2130eae093e35ae":
						val = "d:6e7555857c18459c9025e1a3513c296f";
						break;
					case "6f23d1c9a14543d7980e2d3d83717826":
						val = "d:391362e32a2b48199ef4bebe9b7a611a";
						break;
					case "0dac7e2de8844afd8466ba3b5e4a4201":
						val = "d:c0d293bfced442d8864ba2aa450b8604";
						break;
					case "8944a3b393ea42b3a8ca2807186ccfc6":
						val = "d:337906d27e484923aa8b4400e6fdc7a3";
						break;
					case "db97a38adbdd4efcb407c16945217a28":
						val = "d:6f64ab3a995c4851865136e58e88f7ed";
						break;
					case "a30105c5e7a74d5da8e55e51bbb7b8c3":
						val = "d:ca1d0c4ca12e44ba8ad7e3471970e67c";
						break;
					case "6e536381e59e42bfa05e7f27578251ef":
						val = "d:c9dc77d387ff43ac8e88766aa66c7cae";
						break;
					case "0520dda1c2984321bc3a3a60b7aa80eb":
						val = "d:e5061f4736d3401899fd172f67c18f84";
						break;
					case "feb8e792cfba4b1d8a02f735599bd054":
						val = "d:ce99f0a34bbb4813ac88d3dfe7699a1a";
						break;
					case "5e9e17b0fbb04b87b410d1e463df9110":
						val = "d:57fed459b3734e709cd2627b8b08f0a0";
						break;
					case "347c92fe9506475aa97e030f9526db75":
						val = "d:8b527be6d03742caa2756f560a4e10ba";
						break;
					case "7e9cfc582fb548f6b34b5235f877b155":
						val = "d:b7a958be3d14437f98ab414c8ce88977";
						break;
					case "7c28dd34f9eb4e8882bf4f2741caf3b7":
						val = "d:d581c6a8c3224d3e801607af3357a4aa";
						break;
					case "581438cde8ae45838ebbeb4727421bea":
						val = "d:b7a958be3d14437f98ab414c8ce88977";
						break;
					case "acf53f41750e437b87a6a98a7fb43be7":
						val = "d:b7a958be3d14437f98ab414c8ce88977";
						break;
					case "47f81899ab8f40bca24929ee3f0d7a79":
						val = "d:b7a958be3d14437f98ab414c8ce88977";
						break;
					case "c4c25e71323d402c86b436932122b487":
						val = "d:d55ba7ababf846eda58e0940b4172ac8";
						break;
					case "c317dcc1ccae4486a7852a3a2eaf603b":
						val = "d:51e1860e2e214488bfa013d5e7ed6426";
						break;
					case "7d482ac3d16847dba60a34a22d49b425":
						val = "d:55f99c4789ad4a59a0d15231c2b9951a";
						break;
					case "c868d9a917dc4a53bc749eb1c0745f50":
						val = "d:897c15bc7cdf425c8106775ef31ecb17";
						break;
					case "5580e54fbf984e228c9777c8b0fc1d70":
						val = "d:5f3338bf470448958167688853db0b4a";
						break;
					case "df34458f1b934052b57214019f09cb6a":
						val = "d:40fec391a071473bbafda39e26839cd0";
						break;
					case "5f0ff42db0084de0b8e21482eb88e76e":
						val = "d:b79019ea82e24aaa94ec226ad10f7a46";
						break;
					case "dabace4dae5a492dac0690a00f1e78ac":
						val = "d:506814f2866f4adc9f5effe3cbd75dab";
						break;
					case "c785600fc6344b24bb098290a4f52b4c":
						val = "d:e5e9d7334e7142b98ce82b53aa9f1b4e";
						break;
					case "84f1b7fda1d44c8e93d8579d708b74b1":
						val = "d:93a91c7be4f84289b3a618ab4aaf9c99";
						break;
					case "5f892a97b53f421785cef014bf80392d":
						val = "d:d67d40855c4c492e93f1f8e979b32915";
						break;
					case "c7ea715773ce4b0dad5cc07eaaf0a817":
						val = "d:cd74acdd370a4647851e588ab49e227f";
						break;
					case "949bcc3830c547668c286be0de3e4ea6":
						val = "d:43b31005c8754e77ab2d472c907a81cd";
						break;	
					case "e5aaec1a02f74074b63bbe62a1dd3b7d":
						val = "d:4a293c55b37e492fad852401349b3072";
						break;
					case "9b11abd7b8dd4321921aa38313b572f8":
						val = "d:6b0f6cc7c96b49e4b5566316e75ab81e";
						break;
					case "28bce72b63054df1829d90cf22720d9b":
						val = "d:c0d293bfced442d8864ba2aa450b8604";
						break;
					case "f64a773954e54eb4b5d20778826990a5":
						val = "d:337906d27e484923aa8b4400e6fdc7a3";
						break;
					case "dab52f0713ac4f9593df8570688d01af":
						val = "d:6f64ab3a995c4851865136e58e88f7ed";
						break;
					case "21c1cbbba38245c493747977c79feb0f":
						val = "d:ca1d0c4ca12e44ba8ad7e3471970e67c";
						break;
					case "79dc5ae82fa9431fb1f441d0f044bf0a":
						val = "d:c9dc77d387ff43ac8e88766aa66c7cae";
						break;
					case "e82e7a479c90423aa604d9039c7d2716":
						val = "d:e5061f4736d3401899fd172f67c18f84";
						break;
					case "2643bdadfc6c4fe9bd4d45891253523c":
						val = "d:ce99f0a34bbb4813ac88d3dfe7699a1a";
						break;
					case "d7b852da009647ca8cf7699e2869581b":
						val = "d:b7a958be3d14437f98ab414c8ce88977";
						break;
					case "e5ddd1901fb14c9a993b21d9b2918d13":
						val = "d:e5753b58168843e28ad73855c07b8cff";
						break;
					case "6d94e3d6fbd24a68b2ccac9f6343e160":
						val = "d:391362e32a2b48199ef4bebe9b7a611a";
						break;
					case "1b87e563612d466c8b4a6508e1e59a13":
						val = "d:2283f9f186794145bd6c8e3229f7a009";
						break;
					case "49e7874916584388a7c3d879a6191e44":
						val = "d:9664874293574b79af624f01e3c091cd";
						break;
					case "f41fc69bd77246de82367f3e02c0da35":
						val = "d:586ab1e3ea554a92ab6ebb64d2df8c41";
						break;
					case "f0c32ee7a8a1428db774c59c7756d31f":
						val = "d:6e7555857c18459c9025e1a3513c296f";
						break;
					case "d459704e2bef42afa2fb00717d835782":
						val = "d:9c171b39761749dab3ca7891b67bf531";
						break;
					case "32b8b4f1528a4163ba1027cf49eec9fb":
						val = "d:61caa917c8584832ae8856ec9d982d41";
						break;
					case "8d2ec38f9acc4d24a389bc1d8ed077b9":
						val = "d:106cf5f7d76b4640a2bd771e05845c4b";
						break;
					case "11e09e5189df4d8ba926ff69da1f914c":
						val = "d:6cecc3c57826404abae33225db5a9c50";
						break;
					case "7976c325777f48b28c458b5a16579faa":
						val = "d:32e779b7fa50470480b6d12427749dc5";
						break;
					case "25966cae0c5a4d36a80568c5d6c6d753":
						val = "d:bd763385e5d749c38a59e26afa77e5d3";
						break;
					case "e2003eafca164adca9b75cf5fe2b4773":
						val = "d:b39182d2e89b4f0ca0d06a219d949681";
						break;
					case "4d1fc0f9abe147618e0dee35d6cddad4":
						val = "d:bd763385e5d749c38a59e26afa77e5d3";
						break;
					case "9b1604fa496847a890e09bffa87cc994":
						val = "d:b39182d2e89b4f0ca0d06a219d949681";
						break;
					case "3c061c2057a54a999777c4e1384ba871":
						val = "d:106cf5f7d76b4640a2bd771e05845c4b";
						break;
					case "fa51b2b765fb45bcadf6861845b6fd93":
						val = "d:61caa917c8584832ae8856ec9d982d41";
						break;
					case "ce48d3022bf4463bb1a33ba60d993298":
						val = "d:9c171b39761749dab3ca7891b67bf531";
						break;
					case "a71df7e35c4d4cbdae315f2cbf3fd9bd":
						val = "d:6e7555857c18459c9025e1a3513c296f";
						break;
					case "90c24c7c98484e698583be333054d1f6":
						val = "d:586ab1e3ea554a92ab6ebb64d2df8c41";
						break;
					case "871221c348534130ae0a9857b15cb459":
						val = "d:9664874293574b79af624f01e3c091cd";
						break;
					case "e08f7620fbf748ff9a022576b1b9fb88":
						val = "d:2283f9f186794145bd6c8e3229f7a009";
						break;
					case "6e9cf87e95af4171a51244c20a5bdcb3":
						val = "d:391362e32a2b48199ef4bebe9b7a611a";
						break;
					case "aa7e6d5a50e04d169f7a58f70c0a1eee":
						val = "d:e5753b58168843e28ad73855c07b8cff";
						break;
					case "9cb1ea7df37e40418aed99adfff703ba":
						val = "d:b7a958be3d14437f98ab414c8ce88977";
						break;
					case "e067ac4ca1304ad199b5702c81a74eb6":
						val = "d:ce99f0a34bbb4813ac88d3dfe7699a1a";
						break;
					case "7c67374b33f9480fb008aaf326360361":
						val = "d:e5061f4736d3401899fd172f67c18f84";
						break;
					case "09a666cb68f74af89f8980ec57db414a":
						val = "d:c9dc77d387ff43ac8e88766aa66c7cae";
						break;
					case "4de480d7adb7419bb6e9de0bfd6521c8":
						val = "d:ca1d0c4ca12e44ba8ad7e3471970e67c";
						break;
					case "e311eaaaa8cf4716bdf9d1ec66771890":
						val = "d:6f64ab3a995c4851865136e58e88f7ed";
						break;
					case "3b45e4ae179a4ac09186d07d8dce480c":
						val = "d:337906d27e484923aa8b4400e6fdc7a3";
						break;
					case "650b89c78a3a4dc18dac72603ef9b204":
						val = "d:c0d293bfced442d8864ba2aa450b8604";
						break;
					case "b48beed5a56d44a8be796a46d1c95256":
						val = "d:6b0f6cc7c96b49e4b5566316e75ab81e";
						break;
					case "f65bfe11190c479a83cabcb7f3a5d487":
						val = "d:4a293c55b37e492fad852401349b3072";
						break;
					case "130c2935aaae4ef28bea238fc2306da0":
						val = "d:43b31005c8754e77ab2d472c907a81cd";
						break;
					case "d0f8f8dd451745aea21740f83a426206":
						val = "d:57fed459b3734e709cd2627b8b08f0a0";
						break;
					case "dc48cc17f08646df9c79b2db35a3709f":
						val = "d:d67d40855c4c492e93f1f8e979b32915";
						break;
					case "8b91a03196ac4e3aa4075ccc0e93d880":
						val = "d:93a91c7be4f84289b3a618ab4aaf9c99";
						break;
					case "b86de682018244c3899e85f252f893fd":
						val = "d:e5e9d7334e7142b98ce82b53aa9f1b4e";
						break;
					case "8ac9eb42c95748558a018a14ae9b3393":
						val = "d:506814f2866f4adc9f5effe3cbd75dab";
						break;
					case "96958560ff5f416fb3fee8891a12ccfa":
						val = "d:b79019ea82e24aaa94ec226ad10f7a46";
						break;
					case "ff2f54b0103c47ecbde3b4e8d14f74f3":
						val = "d:40fec391a071473bbafda39e26839cd0";
						break;
					case "121d2fbba3b94b45b26a4b268b02d762":
						val = "d:5f3338bf470448958167688853db0b4a";
						break;
					case "f6345f4a90d74fd0b2cef62bd0b0dd65":
						val = "d:897c15bc7cdf425c8106775ef31ecb17";
						break;
					case "58d15413bfc54d7faae3365d7f83022a":
						val = "d:55f99c4789ad4a59a0d15231c2b9951a";
						break;
					case "12f36fd6a35b44df9a350e20d7e2e704":
						val = "d:51e1860e2e214488bfa013d5e7ed6426";
						break;
					case "e45cf94b36fe413fa1f118af060e810f":
						val = "d:51e1860e2e214488bfa013d5e7ed6426";
						break;
					case "0ceb6a5002f5495aae519a27d87f1fb9":
						val = "d:55f99c4789ad4a59a0d15231c2b9951a";
						break;
					case "1ba0b99d6bbe4fa5a10650a47aa25dd1":
						val = "d:897c15bc7cdf425c8106775ef31ecb17";
						break;
					case "148084018ae24f1db50a9910b4c7ac29":
						val = "d:5f3338bf470448958167688853db0b4a";
						break;
					case "397b29f442444656adb2d0fd08ba2969":
						val = "d:40fec391a071473bbafda39e26839cd0";
						break;
					case "9f952ab5a3de4335ba9e389a7b918f05":
						val = "d:b79019ea82e24aaa94ec226ad10f7a46";
						break;
					case "fe1605c67caf4adca5686870473d383f":
						val = "d:506814f2866f4adc9f5effe3cbd75dab";
						break;
					case "8ec26354b7da40ccb9570af3351a2ca8":
						val = "d:e5e9d7334e7142b98ce82b53aa9f1b4e";
						break;
					case "657f09e585a340a7bbb8a96d8e742d09":
						val = "d:93a91c7be4f84289b3a618ab4aaf9c99";
						break;
					case "04444ed5e3a041beb12a80f7f1b616f9":
						val = "d:d67d40855c4c492e93f1f8e979b32915";
						break;
					case "b9ce7518f6c64b99ada533b8f9abbae8":
						val = "d:57fed459b3734e709cd2627b8b08f0a0";
						break;
					case "cff522f23da54905837a8ae583b09186":
						val = "d:43b31005c8754e77ab2d472c907a81cd";
						break;
					case "cb9f1f46de884710b6090e5a706b7e62":
						val = "d:4a293c55b37e492fad852401349b3072";
						break;
					case "1fb19f30dc2749a187ae0c70c8f3d53f":
						val = "d:6b0f6cc7c96b49e4b5566316e75ab81e";
						break;
					case "77708c2d651f4895bfd8eca6cbbb6a10":
						val = "d:c0d293bfced442d8864ba2aa450b8604";
						break;
					case "cc6924c745f046e0b4a216d04f3c82d1":
						val = "d:337906d27e484923aa8b4400e6fdc7a3";
						break;
					case "2d2b0f102ad2427b905baf144e99142a":
						val = "d:6f64ab3a995c4851865136e58e88f7ed";
						break;
					case "bdc8b70795ab429887d990c2358c33dd":
						val = "d:ca1d0c4ca12e44ba8ad7e3471970e67c";
						break;
					case "22b6a2eea8924d419b69a331d1bf55bc":
						val = "d:c9dc77d387ff43ac8e88766aa66c7cae";
						break;
					case "089a265f94194d0480e0467c1dc97f0d":
						val = "d:e5061f4736d3401899fd172f67c18f84";
						break;
					case "d437c28e2a464265ae64adf562a90914":
						val = "d:ce99f0a34bbb4813ac88d3dfe7699a1a";
						break;
					case "474966bc87524fa6ac38ecf75b160c5f":
						val = "d:b7a958be3d14437f98ab414c8ce88977";
						break;
					case "17504cf4c2944111816d8b51528f804f":
						val = "d:e5753b58168843e28ad73855c07b8cff";
						break;
					case "0d4c94d08a044bd891ea7ed5ad918493":
						val = "d:391362e32a2b48199ef4bebe9b7a611a";
						break;
					case "68bbc7b692af4a64ac60a72dbfc01157":
						val = "d:2283f9f186794145bd6c8e3229f7a009";
						break;
					case "12ca31013efa4ccaaf3e917e4f3470a3":
						val = "d:9664874293574b79af624f01e3c091cd";
						break;
					case "e647d293f71f4170a6002cdf6109636b":
						val = "d:586ab1e3ea554a92ab6ebb64d2df8c41";
						break;
					case "94c92419480c4816b1205f7b82ee93f6":
						val = "d:6e7555857c18459c9025e1a3513c296f";
						break;
					case "56077830221143389d60809209332d95":
						val = "d:6e7555857c18459c9025e1a3513c296f";
						break;
					case "8af4248daffb404090c79483135657c8":
						val = "d:586ab1e3ea554a92ab6ebb64d2df8c41";
						break;
					case "8119d32a6e9f45ae9035ce7baeb1cb1f":
						val = "d:9664874293574b79af624f01e3c091cd";
						break;
					case "9462b4af24e54d8daaca75a43fbc9a95":
						val = "d:2283f9f186794145bd6c8e3229f7a009";
						break;
					case "855e3dbe1ffb4e068390bb67ea41cdfe":
						val = "d:391362e32a2b48199ef4bebe9b7a611a";
						break;
					case "886491ac7de344f2815d0805b9cf21af":
						val = "d:e5753b58168843e28ad73855c07b8cff";
						break;
					case "20ee9f6ebbf44eab87911aba18c684b7":
						val = "d:b7a958be3d14437f98ab414c8ce88977";
						break;
					default:
						val = "d:" + val;
						break;
					}
					
					new_individual.addProperty("mnd-s:hasDecreeKind", new Resource(val, Type._Uri));
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
					putIndividual(level, link, ba_id);
					new_individual.addProperty("v-s:hasLink", new Resource(link.getUri(), Type._Uri));
				}
			}
		}
			
		new_individual.addProperty("rdf:type", to_class, Type._Uri);
		res.add(new_individual);
		return res;
	}

}