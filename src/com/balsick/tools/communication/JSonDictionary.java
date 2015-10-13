package com.balsick.tools.communication;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class JSonDictionary {
	
	
	public static String getType(Object o) {
		if (o == null)
			return null;
		if (o instanceof String)
			return "string";
		if (o instanceof Date)
			return "date";
		if (o instanceof Map)
			return "map";
		if (o instanceof ClientServerDBResult)
			return "clientserverdbresult";
		if (o instanceof ClientServerDBResultRow)
			return "clientserverdbresultrow";
		if (o instanceof ColumnStructure)
			return "columnstructure";
		return "null";
	}
	
	public static Class getClass(String o) {
		switch (o) {
		case "string":
			return String.class;
		case "date":
			return Date.class;
		case "map":
			return HashMap.class;
		case "clientserverdbresult":
			return ClientServerDBResult.class;
		case "clientserverdbresultrow":
			return ClientServerDBResultRow.class;
		case "columnstructure":
			return ColumnStructure.class;
		default :
			return null;
		}
	}
	
}
