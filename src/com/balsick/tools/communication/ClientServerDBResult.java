package com.balsick.tools.communication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientServerDBResult extends ClientServerResult implements Serializable {
	
	private static final long serialVersionUID = -1948831896882633271L;
	
	HashMap<Integer, ClientServerDBResultRow> rows;
	List<ColumnStructure> columns;
	
	{
		resultType = RESULTSUCCESS;
	}
	public ClientServerDBResult() {
	}
	
	public HashMap<Integer, ClientServerDBResultRow> getRows() {
		return rows;
	}

	public List<ColumnStructure> getColumns() {
		return columns;
	}

	public void addRow(ClientServerDBResultRow row) {
		if (rows == null)
			rows = new HashMap<>();
		rows.put(rows.keySet().size(), row);
	}
	
	public Object getValue(int row, String column) {
		try {
			return rows.get(row).get(column);
		} catch (NullPointerException ex) {
			return null;
		}
	}
	
	public void addColumn(ColumnStructure cs) {
		if (columns == null)
			columns = new ArrayList<>();
		columns.add(cs);
	}
	
	@Override
	public Map<String, Object> getJSonMap(){
		HashMap<String,Object> map = new HashMap<>();
		map.put("columns", columns);
		map.put("rows", rows);
		map.put("resulttype", resultType);
		return map;
	}
	
	@Override
	public void revertFromJSon(Map<String, Object> map) {
		this.rows = (HashMap<Integer, ClientServerDBResultRow>) map.get("rows");
		this.columns = (List<ColumnStructure>) map.get("columns");
		this.resultType = (String)map.get("resulttype");
	}

	@Override
	public String getJSonType() {
		return "clientserverdbresult";
	}
}
