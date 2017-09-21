package com.afn.realstat;

import static org.junit.Assert.fail;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("prod")
public class ImportAllTest {

	@Autowired
	private MlsImporter mlsImporter;

	@Autowired
	private CrsImporter crsImporter;

	@Autowired
	private AgentImporter agentImporter;

	@Autowired
	private AgentVolumeImporter agvImporter;

	private static String dataDir = AppFiles.getDataDir();
	@Test
	public void importCrsFiles() {
		File file;
		file = new File(dataDir, "CRSPropertyExport - 94610_0-2000.csv");
		crsImporter.importFile(file);
		file = new File(dataDir, "CRSPropertyExport - 94610_2001-100000.csv");
		crsImporter.importFile(file);
		file = new File(dataDir, "CRSPropertyExport - 94611_0-1500.csv");
		crsImporter.importFile(file);
		file = new File(dataDir, "CRSPropertyExport - 94611_1501-2500.csv");
		crsImporter.importFile(file);
		file = new File(dataDir, "CRSPropertyExport - 94611_2501-100000.csv");
		crsImporter.importFile(file);
		file = new File(dataDir, "CRSPropertyExport - 94618_0-2500.csv");
		crsImporter.importFile(file);
		file = new File(dataDir, "CRSPropertyExport - 94618_2501-100000.csv");
		crsImporter.importFile(file);
	}

	@Test
	public void importAgentFiles() {
		File file;
		file = new File(dataDir, "MLSAllAgentList.txt");
		agentImporter.importFile(file);
	}

	public void importAgentVolumes() {
		File file;
		file = new File(dataDir,
				"Paragon-Volume-Ranking---Agent-Within-MLS-2012-01-12-2017-01-12.csv");
		agvImporter.importFile(file);
	}

	@Test
	public void importMlsFiles() {
		
		File dir = new File(dataDir);
		  File[] directoryListing = dir.listFiles();
		  if (directoryListing != null) {
		    for (File file : directoryListing) {
		        String fileName = file.getName();
		        if (fileName.startsWith("MLSExport")) {
		        	System.out.println("Importing file = " + fileName);
		        	mlsImporter.importFile(file);
		        }
		    }
		  } else {
		    fail();
		  }
	}
}
