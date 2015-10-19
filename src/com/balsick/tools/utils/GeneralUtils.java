package com.balsick.tools.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public abstract class GeneralUtils {
	
	public static List<String> translate(String string, String key) {
		return translate(string, key, "=", ";");
	}
	
	public static List<String> translate(String string, String key, String keyValueSeparator, String valuesSeparator) {
		String str = string;
		if (string.indexOf(key+keyValueSeparator) == 0) {
			str = str.substring((key+keyValueSeparator).length());
		} else if (string.indexOf(key+keyValueSeparator) > 0)
			return null;
		if (str.contains("{") && str.contains("}"))
			str = str.substring(str.indexOf('{')+1, str.indexOf('}'));
		StringTokenizer tokenizer = new StringTokenizer(str, valuesSeparator);
		List<String> result = new ArrayList<>();
		while (tokenizer.hasMoreTokens()){
			result.add(tokenizer.nextToken());
		}
		return result;
	}
}
