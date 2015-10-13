package com.balsick.tools.communication;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ColumnStructure implements Serializable, JSonifiable {
	
	String name;
	String columnType;
	
	public ColumnStructure() {
		
	}

	public ColumnStructure(String name, String type) {
		this.name = name;
		this.columnType = type;
	}
	
	public String getName() {
		return name;
	}

	@Override
	public Map<String, Object> getJSonMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("name", name);
		map.put("columnType", columnType);
		return map;
	}

	@Override
	public void revertFromJSon(Map<String, Object> map) {
		name = (String) map.get("name");
		columnType = (String) map.get("columnType");
	}

	@Override
	public String getJSonType() {
		return "columnstructure";
	}
}
