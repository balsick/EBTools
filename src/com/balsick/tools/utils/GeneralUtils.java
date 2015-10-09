package com.balsick.tools.utils;

import java.util.StringTokenizer;

public abstract class GeneralUtils {
	
	public static String[] translate(String string, String key) {
		return traduce(string, key, "=", ";");
	}
	
	public static String[] traduce(String string, String key, String keyValueSeparator, String valuesSeparator) {
		String str = string;
		if (string.indexOf(key+keyValueSeparator) == 0) {
			str = str.substring((key+keyValueSeparator).length());
		} else if (string.indexOf(key+keyValueSeparator) > 0)
			return null;
		if (str.contains("{") && str.contains("}"))
			str = str.substring(str.indexOf('{')+1, str.indexOf('}'));
		StringTokenizer tokenizer = new StringTokenizer(str, valuesSeparator);
		String[] result = new String[tokenizer.countTokens()];
		int i = 0;
		while (tokenizer.hasMoreTokens()){
			result[i++] = tokenizer.nextToken();
		}
		return result;
	}
}
