package com.balsick.tools.communication;

import java.util.Map;

public interface JSonifiable {
	
	public Map<String, Object> getJSonMap();
	public void revertFromJSon(Map<String, Object> map);

}
