package com.balsick.tools.communication;

public abstract class ClientServerResult implements JSonifiable {
	public final static String RESULTSUCCESS = "success";
	public final static String RESULTFAIL = "fail";
	public String resultType = null;
}
