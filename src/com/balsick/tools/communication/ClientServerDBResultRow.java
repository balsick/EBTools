package com.balsick.tools.communication;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ClientServerDBResultRow implements Serializable, JSonifiable {
	
	private static final long serialVersionUID = 7314545913032404480L;
	HashMap<String, Object> values;
	
	public ClientServerDBResultRow() {
		
	}
	
	public void put(String column, Object value) {
		if (values == null)
			values = new HashMap<>();
		values.put(column, value);
	}

	public Object get(String column) {
		return values.get(column);
	}

	public Map<String, Object> getValues() {
		return values;
	}

	@Override
	public Map<String, Object> getJSonMap(){
		HashMap<String,Object> map = new HashMap<>();
		map.put("values", values);
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void revertFromJSon(Map<String, Object> map) {
		values = (HashMap<String, Object>) map.get("values");
	}
}
