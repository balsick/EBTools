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
//		String strippedJson = json.substring(json.indexOf(OBJECTOPENER), json.lastIndexOf(OBJECTCLOSER));
		HashMap<String, Object> map = new HashMap<>();
		List<Object> list = new ArrayList<>();
		String type = null;
		int objectStart = json.indexOf(OBJECTOPENER);
		int arrayStart = json.indexOf(ARRAYOPENER);
		boolean isList = false;
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
				isList = true;
				opener = '{';
				start = objectStart;
			}
			
			String keyPartAndMore = json.substring(0, start);
			StringTokenizer tokenizer = new StringTokenizer(keyPartAndMore, OBJECTOPENER+OBJECTCLOSER+KEYVALUESEPARATOR+OBJECTSEPARATOR);
			String t = null;
			String s = null;
			while (tokenizer.hasMoreTokens())
				{
				s = t;
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
//				map.put(key, value);
			}
			else
				list.add(value);
//			if (start < 0 || end >= json.length()-1)
//				break;
			int jsonstart = key != null ? key.length()+3 : 0;
			if (json.length() == 0)
				break;
			try {
				json = (key != null ? json.substring(0, keyIndex) : "") /*+ (start >= 0 && jsonstart<start ? json.substring(jsonstart, start) : "")*/ + (end+2 < json.length()-1 ? json.substring(end+2) : "");
			}
			catch (Exception ex) {
				System.err.println(json);
				return null;
//				ex.printStackTrace();
			}
			if (json.length() == 0)
				break;
			objectStart = json.indexOf(OBJECTOPENER);
			arrayStart = json.indexOf(ARRAYOPENER);
		}
		if (list.size() > 0)
			return list;
//		if (json.contains(OBJECTOPENER) == false) {
		String[] pairs = json.split("["+OBJECTSEPARATOR+"]");
		type = null;
//			map = new HashMap<>();
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
			Class c = JSonDictionary.getClass(type);
			try {
				object = c.newInstance();
				((JSonifiable)object).revertFromJSon(map);
			} catch (InstantiationException e) {
				System.err.println("type = "+type+"\nc = "+c);
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				System.err.println("type = "+type+"\nc = "+c);
				e.printStackTrace();
			} catch (NullPointerException e) {
				System.err.println("type = "+type+"\nc = "+c);
				e.printStackTrace();
			}
		}
		else {
			object = map;
		}
		return object;
//		}
		/*
		HashMap<String, Object> map = new HashMap<>();
		String type = null;
		while (json.contains(OBJECTOPENER)) {
			int firstEndOfObject = json.indexOf(OBJECTCLOSER);
			String part1 = json.substring(0, firstEndOfObject);
			int lastStartOfObject = part1.lastIndexOf(OBJECTOPENER);
			int firstEndOfArray = json.indexOf(ARRAYCLOSER);
			int lastStartOfArray = 0;
			String partA1;
			if (firstEndOfArray > 0)
				{
				partA1 = json.substring(0, firstEndOfArray);
				lastStartOfArray = partA1.lastIndexOf(ARRAYOPENER);
				}
			
			boolean inAnArray = false;
			List<Object> list = new ArrayList<>();
			if (lastStartOfObject > lastStartOfArray && firstEndOfObject < firstEndOfArray) {

				String arrayPart = json.substring(lastStartOfArray+1, firstEndOfArray);
				while (lastStartOfObject > lastStartOfArray && firstEndOfObject < firstEndOfArray) {
					inAnArray = true;
					lastStartOfObject = arrayPart.lastIndexOf(OBJECTOPENER);
					firstEndOfObject = arrayPart.indexOf(OBJECTCLOSER, lastStartOfObject);
					String objectPart = arrayPart.substring(lastStartOfObject+1, firstEndOfObject);
					Object value = revertJSon(objectPart);
					list.add(value);
					arrayPart = arrayPart.substring(0, lastStartOfObject);
				}
				json = json.substring(0, lastStartOfArray) + json.substring(firstEndOfArray+1, json.length());
			}
			else {
				String valuePart = json.substring(lastStartOfObject+1, firstEndOfObject);
				Object value = revertJSon(valuePart);
				json = json.substring(0, lastStartOfObject) + json.substring(firstEndOfObject+2, json.length());
			}
			
			String keyPartAndMore = part1.substring(0, part1.lastIndexOf(OBJECTOPENER));
			StringTokenizer tokenizer = new StringTokenizer(keyPartAndMore, OBJECTOPENER+OBJECTCLOSER+KEYVALUESEPARATOR+OBJECTSEPARATOR);
			String t = null;
			String s = null;
			while (tokenizer.hasMoreTokens())
				{
				s = t;
				t = tokenizer.nextToken();
				}
			String key = t;
			if (key.equals("[")) {
				key = s;
			}
			if (key.replace("\"", "").equals("type")) {
				
			}
			
			if (inAnArray)
				{
				json = json.substring(0, keyPartAndMore.lastIndexOf(key))+ json.substring(keyPartAndMore.lastIndexOf(key)+key.length()+2) ;
				map.put(key.replace("\"", ""), list);
				}
			else
				{
//				map.put()
				}
		}
		*/
//		return null;
	}
	
//	/**
//	 * 
//	 * @param obj The object must be created before the call to the method, so it can be recognized and filled.
//	 * @param json 
//	 * @return
//	 */
//	public static final <T extends JSonifiable> T revertJSon(Object obj, String json) {
//		if (obj instanceof JSonifiable) {
//			((JSonifiable)obj).revertFromJSon(json);
//		}
//		else if (obj instanceof Map<?, ?>) {
//			String js = json.substring(json.indexOf(OBJECTOPENER)+1, json.lastIndexOf(OBJECTCLOSER) );
//			String[] pairs = js.split("["+OBJECTSEPARATOR+"]");
//			for (String pair : pairs) {
//				int separatorIndex = js.indexOf(KEYVALUESEPARATOR);
//				String key = js.substring(0, separatorIndex);
//				String value = js.substring(separatorIndex +1, js.length());
//				 
//				
//			}
//		}
//		return null;
//	}
}
