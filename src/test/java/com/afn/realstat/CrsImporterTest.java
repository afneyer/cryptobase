package com.afn.realstat;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("dev")
public class CrsImporterTest {
	 
	 @Autowired
	 private CrsImporter importer;

	 @Test
	 public void testTranslateCsvFileDefault() {
		
	    assertEquals(CrsImporter.translateCsvFileFieldDefault("APN"),"apn");
	    assertEquals(CrsImporter.translateCsvFileFieldDefault("Owner 1"),"owner1");
	    assertEquals(CrsImporter.translateCsvFileFieldDefault("First Name"),"firstName");
	    assertEquals(CrsImporter.translateCsvFileFieldDefault("First Name2"),"firstName2");
	    assertEquals(CrsImporter.translateCsvFileFieldDefault("Total Square Footage"),"totalSquareFootage");
	    assertEquals(CrsImporter.translateCsvFileFieldDefault("Lot Sq. Feet"),"lotSqFeet");

	}

	@Test
	public void testImportFile() {
		File file = new File(AppFiles.getDataDir(),"CRSPropertyExportTest20.csv");

		importer.importFile(file);
	}
}
