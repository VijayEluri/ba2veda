package sm.tools.ba2veda;

import java.util.HashMap;

import sm.tools.veda_client.Resource;

public class Rule
{
	HashMap<String, Resource> if_equals;
	HashMap<String, Resource> set;

	Rule()
	{
		if_equals = new HashMap<String, Resource>();
		set = new HashMap<String, Resource>();
	}
}
