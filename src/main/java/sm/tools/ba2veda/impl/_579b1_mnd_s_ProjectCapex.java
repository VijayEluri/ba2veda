package sm.tools.ba2veda.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.util.StringUtil;

import org.apache.commons.lang.StringUtils;

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

public class _579b1_mnd_s_ProjectCapex extends Ba2VedaTransform
{
	public _579b1_mnd_s_ProjectCapex(BaSystem _ba, VedaConnection _veda, Replacer replacer)
	{
		super(_ba, _veda, replacer, "579b135940eb4f81bf29c0aaef794422", "mnd-s:ProjectCapex");
	}

	public void inital_set()
	{
		fields_map.put("Номер", "v-s:registrationNumber");
		fields_map.put("Наименование", "v-s:title");
		fields_map.put("Цех", "v-s:stakeholder");
		fields_map.put("Заказчик", "v-s:owner");
		fields_map.put("Пункт CAPEX", "mnd-s:itemOfCapex");
		fields_map.put("Год CAPEX", "v-s:year");
		fields_map.put("project_group", "mnd-s:hasProjectGroup");
		fields_map.put("classifier_budget", "?");
		fields_map.put("Категория выгод", "?");
		fields_map.put("status_manual", "v-s:hasStatus");
		fields_map.put("IC (Сумма инвестиций), евро", "mnd-s:investmentSumEuro");
		fields_map.put("ic_rub", "mnd-s:investmentSumRub");
		fields_map.put("NPV, евро", "mnd-s:presentProfit");
		fields_map.put("IRR", "mnd-s:irr");
		fields_map.put("PBP, лет", "mnd-s:payBackPeriod");
		fields_map.put("Дата запуска оборудования по графику", "v-s:dateFromPlan");
		fields_map.put("date_fact", "v-s:dateFromFact");
		fields_map.put("Приемочная документация", "mnd-s:attachmentsForProjectCapexAcceptanceDocumentation");
		fields_map.put("Вложение", "v-s:attachment");
		fields_map.put("Сопровождающие документы", "mnd-s:attachmentsForProjectCapexOther");
		fields_map.put("add_info", "v-s:hasComment");
		fields_map.put("Дата ввода в осн.фонды", "mnd-s:commissioningDate");
		fields_map.put("Сумма ввода, руб.", "mnd-s:commissioningSumRub");
		fields_map.put("Сумма ввода, евр", "mnd-s:commissioningSumEuro");
		fields_map.put("area_folder", "mnd-s:folderNumber");
		fields_map.put("from_doc_object_card_number_reg", "mnd-s:idNumber");
		
		fields_map.put("№ идеи в ЛИЗ", "?");
		fields_map.put("Решение по документу", "?");
		
		fields_map.put("№ СПП-элемента", "?");
		fields_map.put("Дата открытия СПП-элемента", "?");
		fields_map.put("Руководитель проекта", "?");
		
		fields_map.put("№ осн.средства", "?");
		
		fields_map.put("Комментарий", "?");
		
		fields_map.put("add_contract", "?");
		
		fields_map.put("Связанные документы", "?");
		
		fields_map.put("Дата утверждения мероприятия", "?");
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
		Resources nliz = null;
		
		Individual spp_element = null;
//		Individual agd = null;
		
		List<XmlAttribute> atts = doc.getAttributes();
		
		ArrayList<Object> label_parts = new ArrayList<Object>();
		Resources regnmb = null;
		Resources project_stage = null;
		
		int ncomments = 1;
		for (XmlAttribute att : atts) {
			String code = att.getCode();

			String predicate = fields_map.get(code);
			System.out.println("CODE: " + code);
			
			if (predicate != null) {
				Resources rss = ba_field_to_veda(att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri,
						true);

				if (predicate.equals("?") == false) 
					new_individual.addProperty(predicate, rss);
				
				if (rss == null)
					continue;
				
				if (rss.resources.size() < 1)
					continue;
				
				if (code.equals("Номер"))
					regnmb = rss;
				if (code.equals("№ идеи в ЛИЗ")) {
					nliz = rss;		
				} else if (code.equals("Решение по документу")) {
					String stage  = att.getRecordIdValue();
					String of_stage = "";
					switch (stage) {
						case "6d4eae44c758435ca06b08383a5bc7a2":
							of_stage = "d:ProjectStage5";
							break;
						case "a7ead7fd-9768-4e07-bcb2-9ad531cd6773":
							of_stage = "d:ProjectStage2";
							break;
						case "325490e0-9c67-44a4-9e91-332081add9c7":
							of_stage = "d:ProjectStage2";
							break;
						case "9d39e4a7-7b93-47a9-9a17-78eb4218b874":
							of_stage = "d:ProjectStage5";
							break;
						default:
							of_stage = "d:" + stage;
							break;
					}
					
					new_individual.addProperty("v-s:hasProjectStage", new Resource(of_stage, Type._Uri));
					Individual ps = veda.getIndividual(of_stage);
					
					if (ps != null) {
						project_stage = ps.getResources("rdfs:label");
					}
					
					/*if (agd == null)
						agd = new Individual();
					
					if (stage == "6d4eae44c758435ca06b08383a5bc7a2")
						agd.addProperty("mnd-s:ProjectOpex", new Resource("mnd-s:hasProjectClass"));
					else 
						agd.addProperty("mnd-s:ProjectCapex", new Resource("mnd-s:hasProjectClass"));
					
					agd.addProperty("v-s:hasProjectStage", new Resource(of_stage, Type._Uri));*/
				} else if (code.equals("№ СПП-элемента")){
					if (spp_element == null)
						spp_element = new Individual();
					
					spp_element.addProperty("mnd-s:sppNumber", rss);
					
					String[] langs_out1 = { "EN", "RU" };
					String[] langs_out2 = { "NONE" };
					
					Object[] parts = { "СПП-элемент", " ", rss };
					Resources rss1 = rs_assemble(parts, langs_out1);
					if (rss1.resources.size() == 0)
						rss1 = rs_assemble(parts, langs_out2);
					 
					spp_element.addProperty("rdfs:label", rss1);
				} else if (code.equals("Дата открытия СПП-элемента")) {
					if (spp_element == null)
						spp_element = new Individual();
					
					spp_element.addProperty("mnd-s:sppDate", rss);
				} else if (code.equals("№ осн.средства")){
					String doc_str = att.getTextValue();
					String[] docs = doc_str.split(",");
					
					ArrayList<String> conditions = new ArrayList<String>();
					for (int i = 0; i < docs.length; i++)
						if (StringUtils.isNumeric(docs[i].trim())) {
							if (docs[i].trim().length() < 7) {
								System.out.println("TOO SHORT v-s:registrationNumber !!!! " + docs[i]);
								continue;
							}
							conditions.add(String.format("'v-s:registrationNumber'=='%s'", docs[i].trim()));
						}
					
					if (conditions.size() == 0)
						continue;
					
					String cond_str = StringUtils.join(conditions.toArray(), " || ");
					
					String query = String.format("'rdf:type'=='v-s:Asset' && (%s)", cond_str);
					String[] ids = veda.query(query);
					
					for (int i = 0; i < ids.length; i++) {
//						mnd-s:AssetForDismantlig
						Individual afd = new Individual();
						afd.setUri(String.format("%s_asset_for_dismantling_%d",new_individual.getUri(), i));
						afd.addProperty("rdf:type", new Resource("mnd-s:AssetForDismantlig", Type._Uri));
						afd.addProperty("v-s:hasAsset", new Resource(ids[i], Type._Uri));
						new_individual.addProperty("mnd-s:hasAssetForDismantling", new Resource(afd.getUri(), Type._Uri));
						putIndividual(afd, ba_id, true);
					}
				} else if (code.equals("Комментарий")) {
					Individual comment = new Individual();
					comment.setUri(String.format("%s_comment_%d", new_individual.getUri(), ncomments));
					ncomments += 1;
					comment.addProperty("rdf:type", new Resource("v-s:Comment", Type._Uri));
					comment.addProperty("v-s:creator", new_individual.getResources("v-s:creator"));
					comment.addProperty("v-s:created", new_individual.getResources("v-s:created"));
					comment.addProperty("rdfs:label", rss);
					new_individual.addProperty("v-s:hasComment", new Resource(comment.getUri(), Type._Uri));
					putIndividual(comment, ba_id, true);
				} else if (code.equals("Руководитель проекта")) {
					new_individual.addProperty("v-s:projectManager", rss);
					
				/*	if (agd == null)
						agd = new Individual();
					
					agd.addProperty("v-s:projectManager", rss);*/
				} else if (code.equals("add_contract")) {
					String irf = att.getLinkValue();
					XmlDocument irf_doc = ba.getActualDocument(irf).getLeft();
					String inherit_rights_from = ba.get_first_value_of_field(irf_doc, "inherit_rights_from");
					
					if (inherit_rights_from == null)
					{
						new_individual.addProperty("v-s:hasContract", new Resource("d:" + att.getLinkValue(), Type._Uri));
					} 
					else
					{
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
						{
							att.setLinkValue(inherit_rights_from);
							Resources rss1 = ba_field_to_veda(att, uri, inherit_rights_from, doc, path, parent_ba_doc_id, parent_veda_doc_uri,
									true);

							new_individual.addProperty("v-s:hasContract", rss1);
						}
						else
						{
							new_individual.addProperty("v-s:hasContract", new Resource(att.getLinkValue(), Type._Uri));
						}
					}
				} else if (code.equals("Связанные документы")) {
					String irf = att.getLinkValue();
					XmlDocument irf_doc = ba.getActualDocument(irf).getLeft();
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
					link.setUri(new_individual.getUri() + "_" + link_to)	;
					link.addProperty("rdf:type", new Resource("v-s:Link", Type._Uri));
					link.addProperty("v-s:from", new Resource(new_individual.getUri(), Type._Uri));
					link.addProperty("v-s:to", new Resource("d:" + link_to, Type._Uri));
					putIndividual(link, ba_id, true);
					new_individual.addProperty("v-s:hasLink", new Resource(link.getUri(), Type._Uri));
				} else if (code.equals("Дата утверждения мероприятия")) {
					/*if (agd == null)
						agd = new Individual();
					
					agd.addProperty("v-s:created", rss);*/
				} else if (code.equals("classifier_budget")) {
					String cb = att.getRecordIdValue();
					String cb_of = "";
					
					switch (cb) {
					case "6a925f5c-7295-461e-806f-83ad86a6ee41":
						cb_of = "d:a193b366b-aaeb-c1f2-b101-89c95b10a6c8";
						break;
					case "1dfe4525-5218-43eb-914b-096cabb93b76":
						cb_of = "d:1dfe4525-5218-43eb-914b-096cabb93b76";
						break;
					case "2c045c5877994668a467f3b17dd5b0eb":
						cb_of = "d:a486cc102-8ec8-3f09-b326-6fabeec30882";
						break;
					case "4ebdc5f4401441cbb9e9bfe9421bc30b":
						cb_of = "d:a58764c7b-9730-2baa-68a4-da7b9fa36625";
						break;
					case "4078f2cdf73a431f8f26178ddeb9af6f":
						cb_of = "d:a0bfac3cc-ecf3-7847-59b2-5793ebd73879";
						break;
					case "3ad2ce958f93414d97ecd9b5caaca303":
						cb_of = "d:a7b44810e-604e-bc45-3b25-0289f13c444b";
						break;
					default:
						cb_of = "d:" + cb;
						break;
					}
					
					new_individual.addProperty("v-s:hasBudgetCategory", new Resource(cb_of, Type._Uri));		
				} else if (code.equals("Категория выгод")) {
					String pk = att.getRecordIdValue();
					String pk_of = "";
					
					switch (pk) {
					case "734091505ed24e62b1be5a3a185719be":
						pk_of = "d:d8eda1caf4345bcce6bcd14854ea81402";
						break;
					case "d486ad2492d740548c2ef76444e938c0:":
						pk_of = "d:d8eda1caf4345bcce6bcd14854ea81402";
						break;
					case "864c0d02-4e53-4d9e-9dad-0ed4a4b2940f":
						pk_of = "d:d2a9c27d435465f2a90db2cbd0c85a06c";
						break;
					case "a58ac3ee-dc3a-4559-9d3c-11a72f9f0cca":
						pk_of = "d:af80243ca-a107-2da8-ba7d-5c45b6f46e3a";
						break;
					case "29f27eff-a16e-4276-9f51-1d33958c37b3":
						pk_of = "d:d5938a59c45c2adee3d40f1c2d11ab24b";
						break;
					case "ea5b8e4f-f21a-454c-a8ce-4224826d7173":
						pk_of = "d:d67d5c7bd9120158740eb730f206a2484";
						break;
					case "e8a6c0a9-82f4-41f5-babd-57df77fd7975":
						pk_of = "d:d2a9c27d435465f2a90db2cbd0c85a06c";
						break;
					case "2d87b3c5-b86a-40fb-8919-c50529268824":
						pk_of = "d:d33eb7f35b0216410cc404540c8e041a6";
						break;
					case "93fa9045-fe36-41fc-87f7-2466c69e5f6f":
						pk_of = "d:d5938a59c45c2adee3d40f1c2d11ab24b";
						break;
					}
					
					new_individual.addProperty("v-s:hasProjectKind", new Resource(pk_of, Type._Uri));
				}
			}
		}
		
		if (nliz != null) {
			Individual comment = new Individual();
			comment.setUri(new_individual.getUri() + "_comment_liz");
			comment.addProperty("rdf:type", new Resource("v-s:Comment", Type._Uri));
			
			String[] langs_out1 = { "EN", "RU" };
			String[] langs_out2 = { "NONE" };
			
			Object[] parts = { "№ идеи в ЛИЗ: ", " ", nliz };
			Resources rss = rs_assemble(parts, langs_out1);
			if (rss.resources.size() == 0)
				rss = rs_assemble(parts, langs_out2);
			
			comment.addProperty("rdfs:label", rss);
			comment.addProperty("v-s:creator", new_individual.getResources("v-s:creator"));
			comment.addProperty("v-s:created", new_individual.getResources("v-s:created"));
			new_individual.addProperty("v-s:hasComment", new Resource(comment.getUri(), Type._Uri));
			putIndividual(comment, ba_id, true);
		}
		
		if (regnmb != null || project_stage != null) {
			String[] langs_out1 = { "EN", "RU" };
			String[] langs_out2 = { "NONE" };
			
//			Object[] parts = { "№ идеи в ЛИЗ: ", " ", nliz };
			
			ArrayList<Object> parts = new ArrayList<Object>();
			
			parts.add("Инвестиционная заявка");
			
			if (regnmb != null) {
				parts.add(" ");
				parts.add(regnmb);
			}
			
			if (new_individual.getResources("v-s:title") != null) {
				parts.add(" ");
				parts.add(new_individual.getResources("v-s:title"));
			}
			
			if (project_stage != null) {
				parts.add(" ");
				parts.add(project_stage);
			}
			
			Resources rss = rs_assemble(parts.toArray(), langs_out1);
			if (rss.resources.size() == 0)
				rss = rs_assemble(parts.toArray(), langs_out2);
			
			new_individual.addProperty("rdfs:label", rss);
		}
		
		if (spp_element != null) {
			spp_element.setUri(new_individual.getUri() + "_spp_element");
			spp_element.addProperty("rdf:type", new Resource("mnd-s:SppElement", Type._Uri));
			spp_element.addProperty("v-s:creator", new_individual.getResources("v-s:creator"));
			spp_element.addProperty("v-s:created", new_individual.getResources("v-s:created"));
			spp_element.addProperty("v-s:parent", new Resource(new_individual.getUri(), Type._Uri));
			new_individual.addProperty("mnd-s:hasSppElement", new Resource(spp_element.getUri(), Type._Uri));
			putIndividual(spp_element, ba_id, true);
		}
		
		/*if (agd != null) {
			agd.setUri(new_individual.getUri() + "_approval_gate_decision");
			agd.addProperty("rdf:type", new Resource("mnd-s:ApprovalGateDecision", Type._Uri));
			agd.addProperty("v-s:creator", new_individual.getResources("v-s:creator"));
			new_individual.addProperty("mnd-s:hasApprovalGateDecision", new Resource(agd.getUri(), Type._Uri));
			putIndividual(agd, ba_id, true);
		}*/
		
		res.add(new_individual);
		return res;
	}
}