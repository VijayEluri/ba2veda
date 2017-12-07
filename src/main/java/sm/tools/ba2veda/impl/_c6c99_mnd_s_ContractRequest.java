package sm.tools.ba2veda.impl;

import java.util.ArrayList;
import java.util.List;

import ru.mndsc.bigarchive.server.kernel.document.beans.XmlAttribute;
import ru.mndsc.bigarchive.server.kernel.document.beans.XmlDocument;
import sm.tools.ba2veda.BaSystem;
import sm.tools.ba2veda.Replacer;
import sm.tools.veda_client.Individual;
import sm.tools.veda_client.Resource;
import sm.tools.veda_client.Resources;
import sm.tools.veda_client.Type;
import sm.tools.veda_client.VedaConnection;

public class _c6c99_mnd_s_ContractRequest extends _xxxxx_x_ContractRequest
{
	public _c6c99_mnd_s_ContractRequest(BaSystem _ba, VedaConnection _veda, Replacer replacer)
	{
		super(_ba, _veda, replacer, "c6c99dd9041642ed93aedc1060259610", "mnd-s:ContractRequest");
	}

	public void inital_set()
	{
		fields_map.put("number_reg", "v-s:registrationNumber");
		fields_map.put("theme", "v-s:theme");
		fields_map.put("kind", "v-s:hasBudgetCategory");
		fields_map.put("date_plan", "v-s:dateTo");
		fields_map.put("contractor", "v-s:supplierContractor");
		fields_map.put("rationale", "mnd-s:hasStrategyOfChoice");
		fields_map.put("comment", "v-s:rationale");
		fields_map.put("add_info_purchase_requirement", "v-s:hasComment");
		
		fields_map.put("costs", "?");
		fields_map.put("description_equipment", "?");
		fields_map.put("description_materials", "?");
		fields_map.put("description_services", "?");
		fields_map.put("inherit_rights_from", "?");
	}	
	
	@Override
	public List<Individual> transform(XmlDocument doc, String ba_id, String parent_veda_doc_uri,
			String parent_ba_doc_id, String path) throws Exception {
		String uri = prepare_uri(ba_id);
		if (ba_id.equals(""))
			uri = "d:f010c09f1e624b1e885fe581361c5230";
		List<Individual> res = new ArrayList<Individual>();

		Individual new_individual = new Individual();
		new_individual.setUri(uri);

		set_basic_fields(new_individual, doc);

		new_individual.addProperty("rdf:type", to_class, Type._Uri);
		new_individual.addProperty("v-s:initiator", new Resource("d:mondi_department_50003626", Type._Uri));
		new_individual.addProperty("v-s:responsibleDepartment", new Resource("d:mondi_department_50003579", Type._Uri));
		new_individual.addProperty("v-s:customer", new Resource("d:org_RU1121003135", Type._Uri));
		
		Individual rcd = null;
		
//		new_individual.addProperty("mnd-s:hasDecreeKind", new Resource("d:e5753b58168843e28ad73855c07b8cff", Type._Uri));
		
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
				
				if (rss == null)
					continue;
				
				if (rss.resources.size() < 1)
					continue;
				
				if (code.equals("costs")) {
					Individual price = new Individual();
					price.setUri(new_individual.getUri()+"_price");
					price.addProperty("rdf:type", new Resource("v-s:Price", Type._Uri));
					price.addProperty("v-s:hasCurrency", new Resource("d:currency_rub", Type._Uri));
					price.addProperty("v-s:sum", rss);
					veda.putIndividual(price, true, assignedSubsystems);
					new_individual.addProperty("v-s:expectedValueOfContract", new Resource(price.getUri(), Type._Uri));
				} else if (code.equals("description_equipment")) {
					if (rcd == null)
						rcd = new Individual();
					rcd.addProperty("mnd-s:contractRequestEquipmentDescription", rss);
				} else if (code.equals("description_materials")) {
					if (rcd == null)
						rcd = new Individual();
					rcd.addProperty("mnd-s:contractRequestMaterialsDescription", rss);
				} else if (code.equals("description_services")) {
					if (rcd == null)
						rcd = new Individual();
					rcd.addProperty("mnd-s:contractRequestServicesDescription", rss);
				} else if (code.equals("inherit_rights_from")) {
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
					
					
					new_individual.addProperty("v-s:hasProject", new Resource("d:" + link_to, Type._Uri));
				}
			}
		}
		
		if (rcd != null) {
			rcd.setUri(new_individual.getUri() + "_request_contract_detail");
			rcd.addProperty("rdf:type", new Resource("mnd-s:RequestContractDetail", Type._Uri));
			veda.putIndividual(rcd, true, assignedSubsystems);
			new_individual.addProperty("mnd-s:hasRequestContractDetail", new Resource(rcd.getUri(), Type._Uri));
		}
		
		ArrayList<Object> label_parts = new ArrayList<Object>();
		if (new_individual.getResources("v-s:registrationNumber") != null)
			label_parts.add(new_individual.getResources("v-s:registrationNumber"));
		if (new_individual.getResources("v-s:theme") != null) {
			if (label_parts.size() > 0)
				label_parts.add(" ‒ ");
			label_parts.add(new_individual.getResources("v-s:theme"));
		}
//		{v-s:registrationNumber} - {v-s:theme}
		String[] langs_out1 = { "EN", "RU" };
		String[] langs_out2 = { "NONE" };
		
		Resources rss = rs_assemble(label_parts.toArray(), langs_out1);
		if (rss.resources.size() == 0)
			rss = rs_assemble(label_parts.toArray(), langs_out2);
		new_individual.addProperty("rdfs:label", rss);
		
		if (new_individual.getResources("v-s:supplierContractor") != null) {
			if (new_individual.getResources("v-s:supplierContractor").resources.size() > 0) {
				String sc = new_individual.getResources("v-s:supplierContractor").resources.get(0).getData();
				Individual sci = veda.getIndividual(sc);
				if (sci != null)
					new_individual.addProperty("v-s:supplier", sci.getResources("v-s:linkedOrganization"));
			}
		}
		
		res.add(new_individual);
		return res;
	}
}
