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
public class AgentImporterTest {
	 
	 @Autowired
	 private AgentImporter importer;

	@Test
	public void testImportFile() {
		File file = new File(AppFiles.getTestDataDir(),"MLSAllAgentList20.txt");
		importer.importFile(file);
	}
}
