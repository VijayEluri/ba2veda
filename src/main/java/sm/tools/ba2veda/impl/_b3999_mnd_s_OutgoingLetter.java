package sm.tools.ba2veda.impl;

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

public class _b3999_mnd_s_OutgoingLetter extends Ba2VedaTransform
{
	public _b3999_mnd_s_OutgoingLetter(BaSystem _ba, VedaConnection _veda, Replacer replacer)
	{
		super(_ba, _veda, replacer, "b3999ac421644079a1971536f22d252d", "mnd-s:OutgoingLetter");
	}

	public void inital_set()
	{
		fields_map.put("reg_note", "v-s:hasLetterRegistrationRecordSender");
		fields_map.put("Исполнитель", "?");
		fields_map.put("Подписывающий", "?");
		fields_map.put("addresse_to", "?");
		fields_map.put("addresse", "?");
		fields_map.put("Вид документа", "v-s:hasDocumentKind");
		fields_map.put("Краткое содержание", "v-s:description");
		fields_map.put("position_send", "v-s:hasDelivery");
		fields_map.put("Вложения", "v-s:attachment");
		fields_map.put("comment", "rdfs:comment");
		fields_map.put("reply", "v-s:hasRelatedLetter");
		fields_map.put("inherit_rights_from", "?");
		fields_map.put("add_doc", "?");
		fields_map.put("date_reg", "?");
		fields_map.put("type_send", "?");
		fields_map.put("add_doc", "?");
	}

	@Override
	public List<Individual> transform(XmlDocument doc, String ba_id, String parent_veda_doc_uri, String parent_ba_doc_id, String path)
			throws Exception
	{
		String uri = prepare_uri(ba_id);
		List<Individual> res = new ArrayList<Individual>();

		Individual new_individual = new Individual();
		new_individual.setUri(uri);
		set_basic_fields(new_individual, doc);

		Resources _addressee = null;
		Resources _addressee_to = null;
		Resources _sender = null;
		Resources _signer = null;
		Resources _type_send = null;
		//Resources _date_reg = null;
		Resources _reg_note = null;

		Resources _creator = new_individual.getResources("v-s:creator");
		Resources _created = new_individual.getResources("v-s:created");
		Resources _edited = new_individual.getResources("v-s:edited");

		Individual indv_recepient = new Individual();
		indv_recepient.setUri(uri + "_4");
		indv_recepient.addProperty("rdf:type", "mnd-s:Correspondent", Type._Uri);
		indv_recepient.addProperty("v-s:parent", uri, Type._Uri);
		indv_recepient.addProperty("v-s:creator", _creator);
		indv_recepient.addProperty("v-s:created", _created);
		indv_recepient.addProperty("v-s:edited", _edited);
		res.add(indv_recepient);
		new_individual.addProperty("v-s:recipient", indv_recepient.getUri(), Type._Uri);

		Resources sender_shl = null;
		Resources recepient_shl = null;

		new_individual.addProperty("rdf:type", to_class, Type._Uri);
		List<XmlAttribute> atts = doc.getAttributes();
		for (XmlAttribute att : atts)
		{
			String code = att.getCode();

			String predicate = fields_map.get(code);

			if (code.equals("reply"))
				code.length();

			if (predicate != null)
			{
				Resources rss = null;

				if (code.equals("add_doc"))
				{
					rss = ba_field_to_veda(att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri, false);

					if (rss != null)
					{
						int ii = 0;
						for (Resource rsc : rss.resources)
						{
							ii++;
							String link_id = rsc.getData();

							//if (veda.getIndividual(link_id) == null)
							//{
							String new_link_id = link_id + "_" + ii;
							Individual link = new Individual();
							link.setUri(new_link_id);
							link.addProperty("rdf:type", "v-s:Link", Type._Uri);
							link.addProperty("v-s:from", link_id, Type._Uri);
							link.addProperty("v-s:to", uri, Type._Uri);
							link.addProperty("v-s:creator", _creator);
							link.addProperty("v-s:created", _created);
							link.addProperty("v-s:edited", _edited);
							res.add(link);
							//}
							new_individual.addProperty("v-s:hasLink", new_link_id, Type._Uri);
						}
					}
				} else
				{
					if (code.equals("position_send"))
						code.length();

					if (code.equals("reply"))
						rss = ba_field_to_veda(att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri, false);
					else
						rss = ba_field_to_veda(att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri, true);

					if (code.equals("reg_note"))
						_reg_note = rss;

					if (code.equals("addresse"))
						_addressee = rss;
					else if (code.equals("addresse_to"))
						_addressee_to = rss;
					else if (code.equals("Исполнитель"))
						_sender = rss;
					else if (code.equals("Подписывающий"))
						_signer = rss;
					else if (code.equals("type_send"))
						_type_send = rss;
					//else if (code.equals("date_reg"))
					//	_date_reg = rss;
					else
						new_individual.addProperty(predicate, rss);
				}
			}
		}

		if (_sender != null)
		{
			Individual sender_wrap = new Individual();
			sender_wrap.setUri(uri + "_3");
			sender_wrap.addProperty("rdf:type", "mnd-s:Correspondent", Type._Uri);
			sender_wrap.addProperty("v-s:parent", uri, Type._Uri);
			sender_wrap.addProperty("v-s:creator", _creator);
			sender_wrap.addProperty("v-s:created", _created);
			sender_wrap.addProperty("v-s:edited", _edited);
			sender_wrap.addProperty("v-s:correspondentOrganization", "d:org_RU1121003135", Type._Uri);
			res.add(sender_wrap);

			new_individual.addProperty("v-s:sender", sender_wrap.getUri(), Type._Uri);

			//String _sender_uri = _sender.resources.get(0).getData();
			if (sender_wrap != null)
			{
				Resources correspondentOrganization = sender_wrap.getResources("v-s:correspondentOrganization");
				if (correspondentOrganization != null && correspondentOrganization.resources.size() > 0)
				{
					String correspondentOrganization_uri = correspondentOrganization.resources.get(0).getData();

					if (correspondentOrganization_uri != null)
					{
						Individual indv = veda.getIndividual(correspondentOrganization_uri);
						if (indv != null)
						{
							sender_shl = indv.getResources("v-s:shortLabel");
						}
					}
				}

				//Resources _types = indv_sender.getResources("rdf:type");

				//for (Resource rc : _types.resources)
				//{
				//	if (rc.getData().equals("v-s:ContractorProfile"))
				//		_sender = indv_sender.getResources("v-s:backwardTarget");
				//}
			}

			if (_signer != null)
				sender_wrap.addProperty("v-s:correspondentPerson", _signer);

			sender_wrap.addProperty("v-s:correspondentDepartment", _sender);
		}

		if (_addressee != null)
			indv_recepient.addProperty("v-s:correspondentPersonDescription", _addressee);

		if (_addressee_to != null)
		{
			String _addressee_to_uri = _addressee_to.resources.get(0).getData();
			Individual indv_sender = veda.getIndividual(_addressee_to_uri);
			if (indv_sender != null)
			{
				Resources rcs = indv_sender.getResources("v-s:backwardTarget");
				if (rcs != null && rcs.resources.size() > 0)
				{
					indv_recepient.addProperty("v-s:correspondentOrganization", rcs);

					Resources correspondentOrganization = indv_recepient.getResources("v-s:correspondentOrganization");
					if (correspondentOrganization != null && correspondentOrganization.resources.size() > 0)
					{
						String correspondentOrganization_uri = correspondentOrganization.resources.get(0).getData();

						if (correspondentOrganization_uri != null)
						{
							Individual indv = veda.getIndividual(correspondentOrganization_uri);
							if (indv != null)
							{
								recepient_shl = indv.getResources("v-s:shortLabel");
							}
						}
					}

				}
			}

		}

		if (_type_send != null)
			new_individual.addProperty("v-s:deliverBy", _type_send);

		{
			/*		
					На основе полей создать индивид v-s:Delivery.
					date_reg -> v-s:date
					type_send -> v-s:deliverBy 
					ID создаваемого Исх документа -> v-s:parent 
					ID создаваемого Исх документа ->v-s:backwardTarget
					Константа v-s:hasDelivery -> v-s:backwardProperty
					Автор документа -> v-s:creator
					Дата создания документа -> v-s:created
			*/
			//Individual indv_delivery = new Individual();
			//indv_delivery.setUri(uri + "_5");
			//indv_delivery.addProperty("rdf:type", "v-s:Delivery", Type._Uri);
			//indv_delivery.addProperty("v-s:parent", uri, Type._Uri);
			//indv_delivery.addProperty("v-s:creator", _creator);
			//indv_delivery.addProperty("v-s:created", _created);
			//indv_delivery.addProperty("v-s:edited", _edited);

			//if (_date_reg != null)
			//	indv_delivery.addProperty("v-s:date", _date_reg);

			//if (_type_send != null)
			//	indv_delivery.addProperty("v-s:deliverBy", _type_send);

			//indv_delivery.addProperty("v-s:backwardTarget", uri, Type._Uri);
			//indv_delivery.addProperty("v-s:hasDelivery", "v-s:backwardProperty", Type._Uri);

			//res.add(indv_delivery);
			//new_individual.addProperty("v-s:hasDelivery", indv_delivery.getUri(), Type._Uri);
		}

		{
			Individual reg_rec = null;
			Resources rrn = null;
			Resources rrd = null;
			if (_reg_note != null && _reg_note.resources.size() > 0)
			{
				reg_rec = veda.getIndividual(_reg_note.resources.get(0).getData());
				if (reg_rec != null)
				{
					rrn = reg_rec.getResources("v-s:registrationNumber");
					rrd = reg_rec.getResources("v-s:registrationDate");
				}
			}
			/*			
				Склеить строку из: 
				v-s:sender / v-s:correspondentOrganization / v-s:shortLabel +
				v-s:hasLetterRegistrationRecordSender / v-s:registrationNumber +
				v-s:hasLetterRegistrationRecordSender / v-s:registrationDate +
				v-s:recipient / v-s:correspondentOrganization / v-s:shortLabel 		
			*/
			Object[] ff =
			{ sender_shl, " (№", rrd, " ", rrn, ")", recepient_shl };
			String[] langs_out =
			{ "EN", "RU" };
			Resources rss = rs_assemble(ff, langs_out);
			if (rss.resources.size() == 0)
			{
				String[] langs_out2 =
				{ "NONE" };
				rss = rs_assemble(ff, langs_out2);
			}
			if (rss.resources.size() > 0)
				new_individual.addProperty("rdfs:label", rss);
		}

		res.add(new_individual);

		return res;
	}
}
