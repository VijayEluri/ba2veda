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

public class _d9378_mnd_s_IncomingLetter extends Ba2VedaTransform
{
	public _d9378_mnd_s_IncomingLetter(BaSystem _ba, VedaConnection _veda, Replacer replacer)
	{
		super(_ba, _veda, replacer, "d93786a694374a07ac935e04bb389c3e", "mnd-s:IncomingLetter");
	}

	public void inital_set()
	{
		fields_map.put("add_info", "v-s:hasComment");
		fields_map.put("name", "rdfs:label");
		fields_map.put("Вид документа", "v-s:hasDocumentKind");
		fields_map.put("Количество листов", "v-s:sheetsCount");
		fields_map.put("Срок исполнения", "v-s:dueDate");
		fields_map.put("Краткое содержание", "v-s:description");
		fields_map.put("Вложения", "v-s:attachment");
		fields_map.put("name", "rdfs:label");
		fields_map.put("Связанные документы", "v-s:hasLink");

		fields_map.put("Регистрационный номер", "?");
		fields_map.put("Исходящий номер", "?");
		fields_map.put("Дата регистрации", "?");
		fields_map.put("Исходящая дата", "?");
		fields_map.put("Адресат", "?");
		fields_map.put("Вид доставки", "?");
		fields_map.put("Служебные отметки", "?");
		fields_map.put("Ключевые слова", "?");
		fields_map.put("addresse_from", "?");
		fields_map.put("Подписавший", "?");
		fields_map.put("Корреспондент", "?");
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
		Resources _registrationNumber = null;
		Resources _registrationOutNumber = null;
		Resources _registrationDate = null;
		Resources _registrationOutDate = null;
		Resources _addressee = null;
		Resources _addressee_from = null;
		Resources _signer = null;
		Resources _type_delivery = null;
		Resources _c1 = null;
		Resources _c2 = null;
		Resources _correspondent = null;
		Resources _creator = new_individual.getResources("v-s:creator");
		Resources _created = new_individual.getResources("v-s:created");
		Resources _edited = new_individual.getResources("v-s:edited");

		Individual hasLetterRegistrationRecordRecipient = new Individual();
		hasLetterRegistrationRecordRecipient.setUri(uri + "_1");
		hasLetterRegistrationRecordRecipient.addProperty("rdf:type", "v-s:LetterRegistrationRecordRecipient", Type._Uri);
		hasLetterRegistrationRecordRecipient.addProperty("v-s:parent", uri, Type._Uri);
		hasLetterRegistrationRecordRecipient.addProperty("v-s:creator", _creator);
		hasLetterRegistrationRecordRecipient.addProperty("v-s:created", _created);
		hasLetterRegistrationRecordRecipient.addProperty("v-s:edited", _edited);
		res.add(hasLetterRegistrationRecordRecipient);

		new_individual.addProperty("v-s:hasLetterRegistrationRecordRecipient", hasLetterRegistrationRecordRecipient.getUri(), Type._Uri);

		Individual hasLetterRegistrationRecordSender = new Individual();
		hasLetterRegistrationRecordSender.setUri(uri + "_2");
		hasLetterRegistrationRecordSender.addProperty("rdf:type", "v-s:LetterRegistrationRecordSender", Type._Uri);
		hasLetterRegistrationRecordSender.addProperty("v-s:parent", uri, Type._Uri);
		hasLetterRegistrationRecordSender.addProperty("v-s:creator", _creator);
		hasLetterRegistrationRecordSender.addProperty("v-s:created", _created);
		hasLetterRegistrationRecordSender.addProperty("v-s:edited", _edited);
		res.add(hasLetterRegistrationRecordSender);

		new_individual.addProperty("v-s:hasLetterRegistrationRecordSender", hasLetterRegistrationRecordSender.getUri(), Type._Uri);

		Individual addressee = new Individual();
		addressee.setUri(uri + "_4");
		addressee.addProperty("rdf:type", "mnd-s:Correspondent", Type._Uri);
		addressee.addProperty("v-s:parent", uri, Type._Uri);
		addressee.addProperty("v-s:creator", _creator);
		addressee.addProperty("v-s:created", _created);
		addressee.addProperty("v-s:edited", _edited);
		res.add(addressee);

		new_individual.addProperty("v-s:recipient", addressee.getUri(), Type._Uri);

		Individual delivery = new Individual();
		delivery.setUri(uri + "_5");
		delivery.addProperty("rdf:type", "v-s:Delivery", Type._Uri);
		delivery.addProperty("v-s:parent", uri, Type._Uri);
		delivery.addProperty("v-s:creator", _creator);
		delivery.addProperty("v-s:created", _created);
		delivery.addProperty("v-s:edited", _edited);
		res.add(delivery);

		Resources sender_shl = null;
		Resources corr_sl = null;

		new_individual.addProperty("v-s:hasDelivery", delivery.getUri(), Type._Uri);

		new_individual.addProperty("rdf:type", to_class, Type._Uri);
		List<XmlAttribute> atts = doc.getAttributes();
		for (XmlAttribute att : atts)
		{
			String code = att.getCode();

			String predicate = fields_map.get(code);

			if (code.equals("Связанные документы"))
				code.length();

			if (predicate != null)
			{
				Resources rss = null;

				if (code.equals("Связанные документы"))
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
					rss = ba_field_to_veda(att, uri, ba_id, doc, path, parent_ba_doc_id, parent_veda_doc_uri, true);

					if (code.equals("Регистрационный номер"))
						_registrationNumber = rss;
					else if (code.equals("Исходящий номер"))
						_registrationOutNumber = rss;
					else if (code.equals("Дата регистрации"))
						_registrationDate = rss;
					else if (code.equals("Исходящая дата"))
						_registrationOutDate = rss;
					else if (code.equals("Адресат"))
						_addressee = rss;
					else if (code.equals("addresse_from"))
						_addressee_from = rss;
					else if (code.equals("Подписавший"))
						_signer = rss;
					else if (code.equals("Вид доставки"))
						_type_delivery = rss;
					else if (code.equals("Служебные отметки"))
						_c1 = rss;
					else if (code.equals("Ключевые слова"))
						_c2 = rss;
					else if (code.equals("Корреспондент"))
						_correspondent = rss;
					else
						new_individual.addProperty(predicate, rss);
				}
			}
		}

		if (_registrationNumber != null)
			hasLetterRegistrationRecordRecipient.addProperty("v-s:registrationNumber", _registrationNumber);

		if (_registrationDate != null)
			hasLetterRegistrationRecordRecipient.addProperty("v-s:registrationDate", _registrationDate);

		if (_registrationOutNumber != null)
			hasLetterRegistrationRecordSender.addProperty("v-s:registrationNumber", _registrationOutNumber);

		if (_registrationOutDate != null)
			hasLetterRegistrationRecordSender.addProperty("v-s:registrationDate", _registrationOutDate);

		String _addressee_from_uri = null;

		if (_addressee_from != null && _addressee_from.resources.size() > 0)
		{
			Individual addressee_from = new Individual();
			addressee_from.setUri(uri + "_3");
			addressee_from.addProperty("rdf:type", "mnd-s:Correspondent", Type._Uri);
			addressee_from.addProperty("v-s:parent", uri, Type._Uri);
			addressee_from.addProperty("v-s:creator", _creator);
			addressee_from.addProperty("v-s:created", _created);
			addressee_from.addProperty("v-s:edited", _edited);
			res.add(addressee_from);

			new_individual.addProperty("v-s:sender", addressee_from.getUri(), Type._Uri);

			_addressee_from_uri = _addressee_from.resources.get(0).getData();
			Individual indv_addressee_from = veda.getIndividual(_addressee_from_uri);

			if (indv_addressee_from != null)
			{
				Resources _types = indv_addressee_from.getResources("rdf:type");
				sender_shl = indv_addressee_from.getResources("v-s:shortLabel");

				for (Resource rc : _types.resources)
				{
					if (rc.getData().equals("v-s:ContractorProfile"))
						_addressee_from = indv_addressee_from.getResources("v-s:backwardTarget");
				}
			}

			addressee_from.addProperty("v-s:correspondentOrganization", _addressee_from);

			if (_signer != null)
				addressee_from.addProperty("v-s:correspondentPersonDescription", _signer);
		}

		if (_addressee != null)
			addressee.addProperty("v-s:correspondentPerson", _addressee);

		addressee.addProperty("v-s:correspondentOrganization", "d:org_RU1121003135", Type._Uri);

		delivery.addProperty("v-s:date", _registrationDate);
		delivery.addProperty("v-s:deliverBy", _type_delivery);
		delivery.addProperty("v-s:backwardTarget", uri, Type._Uri);
		delivery.addProperty("v-s:hasDelivery", "v-s:backwardProperty", Type._Uri);

		if (_correspondent != null && _correspondent.resources.size() > 0)
		{
			String _id = _correspondent.resources.get(0).getData();
			String[] _idp = _id.split(":");
			String[] rec = ba.get_org_map_record(_idp[1]);

			if (rec == null || rec.length == 0)
				rec = ba.get_org_map_record(_idp[1].replaceAll("-", "_"));

			Individual corr = new Individual();
			corr.setUri(uri + "_0");
			corr.addProperty("rdf:type", "mnd-s:Correspondent", Type._Uri);
			corr.addProperty("v-s:creator", _creator);
			corr.addProperty("v-s:created", _created);
			corr.addProperty("v-s:edited", _edited);

			if (rec != null)
			{
				String name = rec[0];
				String inn = rec[1];
				String new_sed_id = rec[2];

				if (new_sed_id != null && new_sed_id.length() > 0)
					corr.addProperty("v-s:correspondentOrganization", "d:" + new_sed_id, Type._Uri);
				else if (inn != null)
					corr.addProperty("v-s:correspondentOrganization", get_OrgUri_of_inn(inn), Type._Uri);
				if (name != null)
					corr.addProperty("v-s:correspondentDepartmentDescription", name, Type._String);

				String snd_uri = corr.getValue("v-s:correspondentOrganization");
				if (snd_uri != null)
				{
					Individual indv_snd = veda.getIndividual(snd_uri);
					if (indv_snd != null)
						sender_shl = indv_snd.getResources("v-s:shortLabel");
				}

			}
			if (_signer != null)
				corr.addProperty("v-s:correspondentPersonDescription", _signer);

			corr.addProperty("v-s:parent", uri, Type._Uri);

			new_individual.addProperty("v-s:sender", corr.getUri(), Type._Uri);

			res.add(corr);
		}

		{
			Object[] ff =
			{ _c1, ", ", _c2 };
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
				new_individual.addProperty("rdfs:comment", rss);
		}

		Resources correspondentOrganization = addressee.getResources("v-s:correspondentOrganization");
		if (correspondentOrganization != null && correspondentOrganization.resources.size() > 0)
		{
			String correspondentOrganization_uri = correspondentOrganization.resources.get(0).getData();

			if (correspondentOrganization_uri != null)
			{
				Individual indv = veda.getIndividual(correspondentOrganization_uri);
				if (indv != null)
				{
					corr_sl = indv.getResources("v-s:shortLabel");
				}
			}
		}

		{
			Object[] ff =
			{ sender_shl, " (№", _registrationOutNumber, " ", _registrationOutDate, ") -> ", corr_sl, " (№", _registrationNumber, " ", _registrationDate, ")"};
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
