package com.afn.realstat;

import java.io.File;

public class AppFiles {

	private static String userDir = System.getProperty("user.dir");

	public static String getTempDir() {
		return createDir(userDir + "\\temp\\");
	}

	public static String getIconDir() {
		return validateDir(userDir + "\\src\\main\\resources\\VAADIN");
	}

	public static String getUploadDir() {
		return createDir(userDir + "\\uploads");
	}
	
	public static String getLogDir() {
		return createDir(userDir + "\\logs");
	}
	
	public static String getReportsDir() {
		return createDir(userDir + "\\reports");
	}
	
	public static String getDownloadDir() {
		return createDir(userDir + "\\downloads");
	}
	
	public static String getTestDataDir() {
		return validateDir(userDir + "\\testdata");
	}

	public static String getTestOutputDir() {
		return createDir(getLogDir() +"\\testOutput");
	}
	
	public static String getDataDir() {
		return validateDir(userDir + "data");
	}
	
	private static String validateDir(String filePath) {
		File dir = new File(filePath);
		if (!dir.exists()) {
			throw new RuntimeException("Error: Directory (" + filePath + "does not exists!");
		}
		return filePath;
	}

	private static String createDir(String filePath) {

		File dir = new File(filePath);
		if (!dir.exists()) {
			dir.mkdir();
		} else {
			if (!dir.isDirectory()) {
				dir.mkdir();
			}
		}
		return filePath;
	}
}
