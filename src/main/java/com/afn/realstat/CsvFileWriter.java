package com.afn.realstat;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.afn.util.QueryResultTable;

/**
 * The CsvFileWriter generate comma-delimited files for the use in Excel. It
 * takes care of all the internal exception process and throws run-time
 * exceptions for both IOExceptions and SQLExceptions.
 * 
 * The CsvFileWriter provides two ways of writing files:
 * 
 * 1) A header and file are supplied in the constructor, followed by appending
 * the lines and finally closing the file writer.
 * 
 * 2) An SQL-Result set is provided. In this mode the CsvFileWriter will take
 * care of the extraction of the header and the complete writing of the file.
 * 
 * @author Andreas Neyer
 *
 *         Copyright 2017 afndev. All rights reserved.
 *
 */
public class CsvFileWriter {

	private FileWriter fileWriter;
	private String[] header;
	private long row;
	private long numCols;

	private static String LINE_SEP = "\r\n";
	private static String FIELD_SEP = ",";

	public static final Logger importLog = LoggerFactory.getLogger("import");

	/**
	 * Constructor
	 * 
	 * @param fileName:
	 *            full file name as String
	 * @param header:
	 *            full header with comma-separated fields as String
	 */
	public CsvFileWriter(File file, String[] header) {
		this.header = header;
		try {
			fileWriter = new FileWriter(file.getAbsolutePath());

			// Write the CSV file header
			String head = convertToCsv(header);
			System.out.println(head);
			fileWriter.append(head);
			fileWriter.append(LINE_SEP);

		} catch (Exception e) {
			importLog.error("Exception:",e);
			importLog.error("Error in CsvFileWriter see exception: File=", file.getName() + "Header = " + this.header);
			e.printStackTrace();
			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e1) {
				System.out.println("Error while flushing/closing CsvFileWriter !!!");
				e1.printStackTrace();
			}
		}
		row = 1;
		numCols = header.length;
	}

	public static String convertToCsv(String[] row) {
		if (row != null) {
			String line = quoteString(row[0]);
			for (int i = 1; i < row.length; i++) {
				line += CsvFileWriter.FIELD_SEP;
				line += quoteString(row[i]);
			}
			;
			return line;
		} else {
			return null;
		}
	}

	/**
	 * @param s:
	 *            appends the line with fields already comma-separated as string
	 *            to the file
	 */
	public void appendLine(String s) {
		if (s.split(FIELD_SEP).length == numCols) {
			try {
				fileWriter.append(s);
				fileWriter.append(LINE_SEP);
			} catch (IOException e) {
				importLog.error("Exception in CsvFileWriter see exception: Line=", row + "Content = " + s);
				close();
			}
		} else {
			importLog.error("Number of columns " + s.split(FIELD_SEP).length + " is different from header " + numCols
					+ " : Line=" + row + "Content = " + s);
		}
	}

	/**
	 * Flushes and closes the FileWriter used by CsvFileWriter
	 */
	public void close() {
		try {
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			System.out.println("Error while flushing/closing fileWriter !!!");
			e.printStackTrace();
		}
	}

	/**
	 * Writes the result of a query into a comma-separated file for the use in
	 * Excel
	 * 
	 * @param dataSource
	 * @param queryString
	 * @param fileName
	 */
	// TOOD refactor fileName to File
	public static void writeQueryResult(DataSource dataSource, String queryString, File file) {

		System.out.println("Writing query result " + file.getName());

		QueryResultTable qrt = new QueryResultTable(dataSource, queryString);
		writeQueryTable(qrt, file);

	}

	public static void writeQueryTable(QueryResultTable qrt, File file) {

		CsvFileWriter cfw = null;
		String[] head = qrt.getHeader(); 
		// System.out.println(head);

		cfw = new CsvFileWriter(file, head);

		for (int i = 0; i < qrt.getRowCount(); i++) {
			
			String row = convertToCsv(qrt.getRow(i));
			System.out.println(row);
			cfw.appendLine(row);
		}
		cfw.close();
	}

	/**
	 * Returns a string surrounded by double-quotes in case the string contains
	 * the field separator. This way the file can be consumed by Excel.
	 * 
	 * @param s
	 * @return
	 */
	public static String quoteString(String s) {
		if (s != null && s.contains(FIELD_SEP)) {
			String quote = "\"";
			s = quote + s + quote;
		}
		return s;
	}
}
