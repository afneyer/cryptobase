package com.afn.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * QueryResultTable consists of a header and table of data as strings Data in the
 * table can be accessed by index e.g. get(rowIndex, columnIndex). The first row
 * or column is 1.
 * 
 * @author Andreas Neyer
 *
 *         Copyright 2017 afndev. All rights reserved.
 *
 */
public class QueryResultTable {

	public static final Logger log = LoggerFactory.getLogger("app");

	private ArrayList<String> header;
	private ArrayList<ArrayList<String>> rows;

	public QueryResultTable(DataSource ds, String query) {

		Connection conn = null;
		try {
			conn = ds.getConnection();
			PreparedStatement ps = conn.prepareStatement(query);

			ResultSet rs = ps.executeQuery();

			ResultSetMetaData metadata;
			int columnCount;
			// try { TODO remove
			metadata = rs.getMetaData();
			columnCount = metadata.getColumnCount();
			header = new ArrayList<String>(columnCount);
			header.add(metadata.getColumnLabel(1));
			for (int i = 2; i <= columnCount; i++) {
				header.add(metadata.getColumnName(i));
			}

			rows = new ArrayList<ArrayList<String>>(1);
			while (rs.next()) {
				ArrayList<String> row = new ArrayList<String>(columnCount);
				row.add(rs.getString(1));
				for (int i = 2; i <= columnCount; i++) {
					row.add(rs.getString(i));
				}
				System.out.println(row);
				rows.add(row);
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			}

		}

	}

	public String get(int i, int j) {
		return rows.get(i).get(j);
	}

	public void set(int ri, int cj, String s) {

		if (cj >= getColumnCount()) {
			String msg = "ColumnIndex for a QueryResultTable must be smaller than the header size: Add header label(s) first!";
			msg += "columnIndex = " + cj + " header size = " + getColumnCount();
			log.error(msg);
			throw new RuntimeException(msg);
		}


		if (ri >= getRowCount()) {
			String msg = "RowIndex for a QueryResultTable must be smaller than row count: Use addRow instead!";
			msg += "rowIndex = " + ri + " row count = " + getRowCount();
			log.error(msg);
			throw new RuntimeException(msg);
		} 
		
		rows.get(ri).set(cj, s);
	}
	
	
	/**
	 * @param col : must include header label at index 0
	 */
	public void addColumn(String[] col) {
		if (col.length != getRowCount()+1) {
			String msg = "Column must have same number of elements as other colums (including a header at index=0): ";
			msg += "Col element = " + col.length + " other columns = " + getRowCount();
			log.error(msg);
			throw new RuntimeException(msg);
		}
		header.add(col[0]);
		for (int i=1; i<= this.getRowCount(); i++) {
			rows.get(i-1).add(col[i]);
		}
	}
	
	public void addRow(String[] newRow) {
		if (newRow.length != getColumnCount()) {
			String msg = "Row must have same number of elements as other rows: ";
			msg += "Row element = " + newRow.length + " other rows = " + getColumnCount();
			log.error(msg);
			throw new RuntimeException(msg);
		} 
		ArrayList<String> row = new ArrayList<String>(getColumnCount());
		for (int i=0; i<getColumnCount(); i++) {
			row.add(i, newRow[i]);			
		}
		rows.add(row);
	}

	public int getRowCount() {
		return rows.size();

	}

	public int getColumnCount() {
		return header.size();
	}

	public String getHeaderElement(int i) {
		return header.get(i);
	}
	
	public String[] getHeader() {
		String[] head = new String[header.size()];
		head = header.toArray(head);
		return head;
	}
	
	public String[] getRow(int i) {
		String[] row = new String[rows.get(i).size()];
		row = rows.get(i).toArray(row);
		return row;
	}

	public void addQueryResultTable(QueryResultTable queryResultTable) {
		int colCount = getColumnCount();
		if (queryResultTable.getColumnCount() != colCount) {
			String msg = "Rows of added QueryResultTable must have number of columns as this table: ";
			msg += "Existing table column count = " + colCount + ";   New table column count = " + queryResultTable.getColumnCount();
			log.error(msg);
			throw new RuntimeException(msg);
		} 
		
		// Use existing header and add rows and columns
		int rowCount = queryResultTable.getRowCount();
		for (int i=0; i<rowCount; i++) {
			rows.add( new ArrayList<String>(queryResultTable.rows.get(i)) );
		}
	}
}
