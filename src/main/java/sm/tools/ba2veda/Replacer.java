package sm.tools.ba2veda;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import sm.tools.veda_client.Resource;

public class Replacer
{
	private List<Rule> replace_rules;
	private List<Rule> ignore_rules;

	Replacer()
	{
		replace_rules = new ArrayList<Rule>();
		ignore_rules = new ArrayList<Rule>();
	}

	public void set_ignore_rules(JSONArray jsna)
	{

		for (int idx = 0; idx < jsna.size(); idx++)
		{
			Rule new_rule = new Rule();
			JSONObject el = (JSONObject) jsna.get(idx);

			JSONArray if_equals = (JSONArray) el.get("if-equals");
			set_rule_part(if_equals, new_rule.if_equals);

			ignore_rules.add(new_rule);
		}
	}

	public void set_replace_rules(JSONArray jsna_replace)
	{

		for (int idx = 0; idx < jsna_replace.size(); idx++)
		{
			Rule new_rule = new Rule();
			JSONObject el = (JSONObject) jsna_replace.get(idx);

			JSONArray if_equals = (JSONArray) el.get("if-equals");
			set_rule_part(if_equals, new_rule.if_equals);

			JSONArray set = (JSONArray) el.get("set");
			set_rule_part(set, new_rule.set);

			replace_rules.add(new_rule);
		}
	}

	private void set_rule_part(JSONArray if_equals, HashMap<String, Resource> rule_part)
	{
		for (int idxA = 0; idxA < if_equals.size(); idxA++)
		{
			JSONObject elA = (JSONObject) if_equals.get(idxA);

			String field = null;
			String value = null;
			String type = null;

			if (elA.get("field") != null)
				field = elA.get("field").toString();

			if (field == null)
				field = "*";

			if (elA.get("value") != null)
				value = elA.get("value").toString();

			if (elA.get("type") != null)
				type = elA.get("type").toString();

			if (type == null)
				type = "Uri";

			rule_part.put(field, new Resource(value, type));
		}

	}

	public HashMap<String, Resource> get_replace(String field, String value, String type)
	{
		if (field == null)
			field = "*";

		for (Rule rule : replace_rules)
		{
			Resource if_equals = rule.if_equals.get(field);

			if (if_equals == null)
				if_equals = rule.if_equals.get("*");

			if (if_equals != null)
			{
				String if_data = if_equals.getData();
				// int if_type = if_equals.getType();

				if (if_data != null && if_data.equals(value))
				{
					// System.out.println("FOUND REPLACE TO: [" + field + "]='" + value + "' (" +
					// type + ") -> " + rule.set);
					return rule.set;
				}
			}
		}
		return null;
	}

	public boolean is_ignore(String field, String value, String type)
	{
		if (field == null)
			field = "*";

		for (Rule rule : ignore_rules)
		{
			Resource if_equals = rule.if_equals.get(field);

			if (if_equals == null)
				if_equals = rule.if_equals.get("*");

			if (if_equals != null)
			{
				String if_data = if_equals.getData();
				// int if_type = if_equals.getType();

				if (if_data != null && if_data.equals(value))
				{
					return true;
				}
			}
		}
		return false;
	}

}
