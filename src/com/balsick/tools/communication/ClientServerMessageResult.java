package com.balsick.tools.communication;

import java.util.HashMap;
import java.util.Map;

public class ClientServerMessageResult extends ClientServerResult {
	String message = null;
	
	public String getMessage() {
		return message;
	}

	public ClientServerMessageResult() {
		
	}
	
	public ClientServerMessageResult(String result, String message) {
		this.resultType = result;
		this.message = message;
	}

	@Override
	public Map<String, Object> getJSonMap() {
		HashMap<String, Object> map = new HashMap<>();
		map.put("message", message);
		map.put("resulttype", resultType);
		return map;
	}

	@Override
	public void revertFromJSon(Map<String, Object> map) {
		this.message = (String)map.get("message");
		this.resultType = (String)map.get("resulttype");
	}
}
