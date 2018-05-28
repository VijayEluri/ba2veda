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

public class _97da9_mnd_s_AccountingDoc extends Ba2VedaTransform {
	public _97da9_mnd_s_AccountingDoc(BaSystem _ba, VedaConnection _veda, Replacer replacer)  {
		super(_ba, _veda, replacer, "97da94f05bcf48c5a3387e44d47dba13", "mnd-s:AccountingDoc");
	}
	
	public void inital_set() {
		fields_map.put("kind_p", "v-s:hasDocumentKind");
		fields_map.put("number_add_p", "v-s:registrationNumberIn");
		fields_map.put("date_add_p", "v-s:registrationDate");
		fields_map.put("number_p", "v-s:registrationNumber");
		fields_map.put("attachment_p", "v-s:attachment");
		fields_map.put("comment_p", "rdfs:comment");
		
		fields_map.put("contractor", "?");
		fields_map.put("inherit_rights_from", "?");
		fields_map.put("summ_p", "?");
		fields_map.put("currency", "?");
		fields_map.put("link_to_doc", "?");
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
		new_individual.addProperty("v-s:customer", "d:org_RU1121021007", Type._Uri);
		Resources linkToDoc = null;
		boolean makeLink = false;
		
		
		Resources summ_p = null, currency = null;
		
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
				
				if (code.equals("contractor")) {
					new_individual.addProperty("v-s:supplierContractor", rss);
					String contractor = rss.resources.get(0).getData();
					
					if (contractor != null) {
						while (true) {
							try {
								Individual contractorIndiv = veda.getIndividual(contractor);
								if (contractorIndiv != null)
									new_individual.addProperty("v-s:supplier", 
										contractorIndiv.getResources("v-s:linkedOrganization"));
							} catch (Exception e) {
								System.err.println("Err getting contractor, retry later");
								e.printStackTrace();
								Thread.sleep(5000);
							}
							break;
						}
					}
				} else if (code.equals("inherit_rights_from")) {
					String link = att.getLinkValue();
					if (link == null)
						continue;
					Pair<XmlDocument, Long> pair = ba.getActualDocument(link);
					if (pair == null)
						continue;
					XmlDocument doc2 = pair.getLeft();
					if (doc2 == null)
						continue;
					
					String link2 = ba.get_first_value_of_field(doc2, "inherit_rights_from");
					if (link2 != null)
						new_individual.addProperty("v-s:hasContract", "d:" + link2, Type._Uri);
					else
						new_individual.addProperty("v-s:hasContract", "d:" + link, Type._Uri);
				} else if (code.equals("summ_p"))
					summ_p = rss;
				else if (code.equals("currency"))
					currency = rss;
				else if (code.equals("link_to_doc")) {
					String link = att.getLinkValue();
					linkToDoc = rss;
					if (link == null)
						continue;
					
					if (link.equals("027afafd4c444d96913422ee669e6d44") ||
							link.equals("63a4aa872c0d49a6a857060b9632a17e") ||
							link.equals("135fd4dbef534220b79f007d7af403f7") ||
							link.equals("97da94f05bcf48c5a3387e44d47dba13")) {
						Pair<XmlDocument, Long> pair = ba.getActualDocument(link);
						if (pair == null) {
							makeLink = true;
							continue;
						}
						
						XmlDocument doc2 = pair.getLeft();
						if (doc2 == null) {
							makeLink = true;
							continue;
						}
						
						String kind_p = ba.get_first_value_of_field(doc2, "kind_p");
						if (kind_p == null) {
							makeLink = true;
							continue;
						}
						
						if (kind_p.equals("81cd1dfaeac64a3fadd2f6f8fabfaccb") ||
								kind_p.equals("dd0a88c458f94ba08b61ff632a5d66dd") ||
								kind_p.equals("77d4cbd58cf34321a82e7281e8347542")) {
							new_individual.addProperty("v-s:backwardTarget", rss);
						} else 
							makeLink = true;
					} else 
						makeLink = true;
				}
				
			}
		}
		
		if (linkToDoc != null && makeLink) {
			Individual link = new Individual();
			link.setUri(new_individual.getUri() + "_link");
			link.addProperty("rdf:type", "v-s:Link", Type._Uri);
			link.addProperty("v-s:creator", new_individual.getResources("v-s:creator"));
			link.addProperty("v-s:created", new_individual.getResources("v-s:created"));
			link.addProperty("v-s:from", new_individual.getUri(), Type._Uri);
			link.addProperty("v-s:to", linkToDoc);
			putIndividual(level, link, ba_id);
			new_individual.addProperty("v-s:hasLink", link.getUri(), Type._Uri);
		}
		
		if (summ_p != null && currency != null) {
			Individual price = new Individual();
			price.setUri(new_individual.getUri() + "_price");
			price.addProperty("rdf:type","v-s:Price", Type._Uri);
			price.addProperty("v-s:creator", new_individual.getResources("v-s:creator"));
			price.addProperty("v-s:created", new_individual.getResources("v-s:created"));
			price.addProperty("v-s:parent", new_individual.getUri(), Type._Uri);
			price.addProperty("v-s:sum", summ_p);
			price.addProperty("v-s:hasCurrency", currency);
			putIndividual(level, price, ba_id);
			new_individual.addProperty("v-s:hasPrice", price.getUri(), Type._Uri);
		}
		
		if (new_individual.getResources("v-s:backwardTarget") != null)
			new_individual.addProperty("v-s:backwardProperty", "v-s:hasAccountingDoc", Type._Uri);
		
		res.add(new_individual);
		return res;
	}
}
