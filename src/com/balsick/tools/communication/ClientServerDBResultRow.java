package com.balsick.tools.communication;

import java.io.Serializable;
import java.util.HashMap;

public class ClientServerDBResultRow implements Serializable {
	
	private static final long serialVersionUID = 7314545913032404480L;
	HashMap<String, Object> values;
	
	public void put(String column, Object value) {
		if (values == null)
			values = new HashMap<>();
		values.put(column, value);
	}

	public Object get(String column) {
		return values.get(column);
	}

	public HashMap<String, Object> getValues() {
		return values;
	}
}
