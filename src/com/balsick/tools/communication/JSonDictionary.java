package com.balsick.tools.communication;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class JSonDictionary {
	/*
	public static String getType(Object o) {
		if (o == null)
			return null;
		if (o instanceof String)
			return "string";
		if (o instanceof Date)
			return "date";
		if (o instanceof Map)
			return "map";
		if (o instanceof JSonifiable)
			return ((JSonifiable) o).getJSonType();
//		if (o instanceof ClientServerMessageResult)
//			return "clientservermessageresult";
//		if (o instanceof ClientServerDBResult)
//			return "clientserverdbresult";
//		if (o instanceof ClientServerDBResultRow)
//			return "clientserverdbresultrow";
//		if (o instanceof ColumnStructure)
//			return "columnstructure";
		return "null";
	}*/
	
	public static String getFullClassName(Object o) {
		return o.getClass().getName();
	}
	
//	public static String getType(Object o) {
//		if (o == null)
//			return null;
//		if (o instanceof String)
//			return "string";
//		if (o instanceof Date)
//			return "date";
//		if (o instanceof Map)
//			return "map";
//		if (o instanceof ClientServerMessageResult)
//			return "clientservermessageresult";
//		if (o instanceof ClientServerDBResult)
//			return "clientserverdbresult";
//		if (o instanceof ClientServerDBResultRow)
//			return "clientserverdbresultrow";
//		if (o instanceof ColumnStructure)
//			return "columnstructure";
//		return "null";
//	}
	
	/**
	 * 
	 * @param className fully-qualified Class Name: package.classname <br/>If nested class, use '$'.
	 * @return A new instance of the requested object, if classname was found.
	 */
	public static Object instantiateObject(String className) {
		try {
			Class<?> class_ = Class.forName(className);
			Constructor<?> constructor = class_.getConstructor();
			Object object = constructor.newInstance();
			return object;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}
	/*
	public static Class<?> getClass(String o) {
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
		case "clientservermessageresult":
			return ClientServerMessageResult.class;
		default :
			return null;
		}
	}*/
	
}
