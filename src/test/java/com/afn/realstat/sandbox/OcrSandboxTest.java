package com.afn.realstat.sandbox;

import org.junit.Test;

import com.afn.realstat.AppFiles;
import com.asprise.ocr.Ocr;


public class OcrSandboxTest {

	private String testDataDir = AppFiles.getTestDataDir();
	@Test
	public void initialOcrSandboxTest() {

		Ocr.setUp(); // one time setup
		Ocr ocr = new Ocr(); // create a new OCR engine
		ocr.startEngine("eng", Ocr.SPEED_FASTEST); // English
		String fs = testDataDir + "\\17-03-25_Tour.pdf";
		String s = ocr.recognize(fs, -1, 100, 100, 400, 200,
				   Ocr.RECOGNIZE_TYPE_TEXT, Ocr.OUTPUT_FORMAT_PLAINTEXT);
		// String s = ocr.recognize(new File[] {file}, Ocr.RECOGNIZE_TYPE_ALL, Ocr.OUTPUT_FORMAT_PLAINTEXT);
		System.out.println("Result: " + s);
		ocr.stopEngine();
	}

}
