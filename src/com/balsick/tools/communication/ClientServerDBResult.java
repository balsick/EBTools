package com.balsick.tools.communication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClientServerDBResult implements Serializable {
	
	private static final long serialVersionUID = -1948831896882633271L;
	
	HashMap<Integer, ClientServerDBResultRow> rows;
	List<ColumnStructure> columns;
	
	public String getJSon(){
		String json = null;
		
		return json;
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
}
