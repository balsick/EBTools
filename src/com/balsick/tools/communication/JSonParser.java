package com.balsick.tools.communication;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;



public abstract class JSonParser {
	
	public static final String KEYVALUESEPARATOR = ":";
	public static final String OBJECTOPENER = "{";
	public static final String OBJECTCLOSER = "}";
	public static final String ARRAYOPENER = "[";
	public static final String ARRAYCLOSER = "]";
	public static final String ARRAYSEPARATOR =",";
	public static final String OBJECTSEPARATOR = ",";
	public static final String VALUELIMITS = "\"";
	
	public static final String getJSon(Object obj) {
		if (obj == null)
			return null;
		String json = null;
		if (obj instanceof JSonifiable) {
			Map<String, Object> jsonMap = ((JSonifiable)obj).getJSonMap();
			jsonMap.put("type", ((JSonifiable)obj).getJSonType());
			return getJSon(jsonMap);
		}
		else if (obj instanceof Map<?, ?>) {
			json = OBJECTOPENER;
			Map<Object, Object> map = (Map<Object, Object>)obj;
			for (Object key : map.keySet()) {
				json += VALUELIMITS + key.toString() + VALUELIMITS;
				json += KEYVALUESEPARATOR;
				json += getJSon(map.get(key));
				json += OBJECTSEPARATOR;
			}
			json = json.substring(0, json.lastIndexOf(OBJECTSEPARATOR));
			json += OBJECTCLOSER;
		}
		else if (obj instanceof List) {
			json = ARRAYOPENER;
			List<?> list = (List<?>)obj;
			for (Object o : list) {
				json += getJSon(o);
				json += ARRAYSEPARATOR;
			}
			json = json.substring(0, json.lastIndexOf(ARRAYSEPARATOR));
			json += ARRAYCLOSER;
		}
		else if (obj instanceof String || obj instanceof Date) {
			return VALUELIMITS+obj.toString()+VALUELIMITS;
		}
		else if (obj instanceof Number) {
			return obj.toString();
		}
		else {
			System.out.println(obj.toString());
			Map<String, Object> map = new HashMap<>();
			String type;
			if (obj instanceof JSonifiable)
				type = ((JSonifiable)obj).getJSonType();
			else
				type = JSonDictionary.getType(obj);
			map.put("type", type);
			map.put("value", getJSon(obj));
			return getJSon(map);
		}
		
		return json;
	}
	
	public static final Object revertJSon(String json) {
		HashMap<String, Object> map = new HashMap<>();
		List<Object> list = new ArrayList<>();
		String type = null;
		int objectStart = json.indexOf(OBJECTOPENER);
		int arrayStart = json.indexOf(ARRAYOPENER);
		while (objectStart >= 0 || arrayStart >= 0) {
			if (arrayStart < 0)
				arrayStart = json.length();
			if (objectStart < 0)
				objectStart = json.length();
			int start = Math.min(arrayStart, objectStart);
			char opener = objectStart < arrayStart ? '{' : '[';
			char closer = objectStart < arrayStart ? '}' : ']';
			int count = 1;
			int end = -1;
			for (int i = Math.min(objectStart, arrayStart)+1; i < json.length(); i++) {
				if (json.charAt(i) == opener)
					count++;
				else if (json.charAt(i) == closer)
					count --;
				if (count == 0) {
					end = i;
					break;
				}
			}
			if (opener == '[' && start == 0) {
				opener = '{';
				start = objectStart;
			}
			
			String keyPartAndMore = json.substring(0, start);
			StringTokenizer tokenizer = new StringTokenizer(keyPartAndMore, OBJECTOPENER+OBJECTCLOSER+KEYVALUESEPARATOR+OBJECTSEPARATOR);
			String t = null;
			while (tokenizer.hasMoreTokens()) {
				t = tokenizer.nextToken();
				}
			String key = t;
			int keyIndex = -1;
			if (key != null) {
				keyIndex = json.indexOf(key);
			}
			Object value;
			value = revertJSon(json.substring(start+1, end));

			if (key != null) {
				key = key.replace("\"", "");
				map.put(key, value);
			}
			else
				list.add(value);
//			int jsonstart = key != null ? key.length()+3 : 0;
			if (json.length() == 0)
				break;
			try {
				json = (key != null ? json.substring(0, keyIndex) : "") /*+ (start >= 0 && jsonstart<start ? json.substring(jsonstart, start) : "")*/ + (end+2 < json.length()-1 ? json.substring(end+2) : "");
			}
			catch (Exception ex) {
				System.err.println(json);
				return null;
			}
			if (json.length() == 0)
				break;
			objectStart = json.indexOf(OBJECTOPENER);
			arrayStart = json.indexOf(ARRAYOPENER);
		}
		if (list.size() > 0)
			return list;
		String[] pairs = json.split("["+OBJECTSEPARATOR+"]");
		type = null;
		for (String pair : pairs) {
			if (pair.length() == 0)
				continue;
			int separatorIndex = pair.indexOf(KEYVALUESEPARATOR);
			String key = pair.substring(0, separatorIndex).trim();
			String value = pair.substring(separatorIndex +1, pair.length()).trim();
			if (key.contains("type")) {
				type = value.replace("\"", "").replace("}", "").replace("{", "");
			}
			else
				map.put(key.replace("\"", ""), value.replace("\"", ""));
		}
		Object object = null;
		if (type != null) {
			try {
				object = JSonDictionary.getClass(type).newInstance();
				((JSonifiable)object).revertFromJSon(map);
			} catch (InstantiationException e) {
				System.err.println("type = "+type+"\nc = "+JSonDictionary.getClass(type));
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				System.err.println("type = "+type+"\nc = "+JSonDictionary.getClass(type));
				e.printStackTrace();
			} catch (NullPointerException e) {
				System.err.println("type = "+type+"\nc = "+JSonDictionary.getClass(type));
				e.printStackTrace();
			}
		}
		else {
			object = map;
		}
		return object;
	}
}
