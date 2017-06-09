package sm.tools.ba2veda;

import java.util.HashMap;

public class TransformCl
{
	String from_class;
	String to_class;
	HashMap<String, TransformRule> tr_rules;

	TransformCl()
	{
		tr_rules = new HashMap<String, TransformRule>();
	}

	public void add_rule(TransformRule tr)
	{
		tr_rules.put(from_class + tr.from_field, tr);
	}

	public HashMap<String, TransformRule> get_rules()
	{
		return tr_rules;
	}
}
