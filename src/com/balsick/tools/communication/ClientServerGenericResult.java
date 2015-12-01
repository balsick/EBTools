package com.balsick.tools.communication;

import java.util.HashMap;
import java.util.Map;

public class ClientServerGenericResult extends ClientServerResult {
	
	Object result;
	
	private ClientServerGenericResult(Object result) {
		this.result = result;
		resultType = RESULTSUCCESS;
	}
	
	public static ClientServerGenericResult createResult(Object result) throws IllegalArgumentException {
		if (result == null)
			throw new IllegalArgumentException();
		return new ClientServerGenericResult(result);
	}

	@Override
	public Map<String, Object> getJSonMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("result", result);
		map.put("error", new Boolean(resultType.equals(RESULTFAIL)));
		return map;
	}

	@Override
	public void revertFromJSon(Map<String, Object> map) {
		result = map.get("result");
		resultType = resultType.equals(RESULTFAIL) ? RESULTFAIL : RESULTSUCCESS;
	}

}
