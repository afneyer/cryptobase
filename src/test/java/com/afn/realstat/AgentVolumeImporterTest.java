package com.afn.realstat;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.afn.Application;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("dev")
public class AgentVolumeImporterTest {
	 
	 @Autowired
	 private AgentVolumeImporter importer;

	@Test
	public void testImportFile() {
		File file = new File(AppFiles.getTestDataDir(),"AgentVolumeExportTest20.csv");
		importer.importFile(file);
	}
}
