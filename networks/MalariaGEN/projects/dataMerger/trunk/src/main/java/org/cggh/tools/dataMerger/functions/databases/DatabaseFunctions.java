package org.cggh.tools.dataMerger.functions.databases;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.sql.rowset.CachedRowSet;

public class DatabaseFunctions {

	
	public ArrayList<String> convertTablesAsCachedRowSetIntoTableNamesAsStringArrayList (CachedRowSet tablesAsCachedRowSet) {
		
		ArrayList<String> tableNamesAsStringArrayList = null;

		try {
			
			if (tablesAsCachedRowSet.next()) {
				
				tableNamesAsStringArrayList = new ArrayList<String>();
				
				tablesAsCachedRowSet.beforeFirst();
				
				while (tablesAsCachedRowSet.next()) {
					tableNamesAsStringArrayList.add(tablesAsCachedRowSet.getString("TABLE_NAME"));
				}
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

			
		return tableNamesAsStringArrayList;
		
	}
	
}
