package com.balsick.tools.communication;

import java.io.Serializable;

public class ColumnStructure implements Serializable {
	
	String name;
	String columnType;

	public ColumnStructure(String name, String type) {
		this.name = name;
		this.columnType = type;
	}
	
	public String getName() {
		return name;
	}
}
