package com.afn.realstat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("prod")
public class DataConsistencyTests {

	private static final Logger log = LoggerFactory.getLogger("app");

	@Autowired
	private RealPropertyRepository rpRepo;

	@Autowired
	private PropertyTransactionRepository ptRepo;

	@Test
	public void checkForDuplicateApnNumbers() {

		long numRecs = rpRepo.count();
		log.info("RealProperty record count = " + numRecs);
		List<RealProperty> properties = rpRepo.findAll();

		HashMap<String, RealProperty> list = new HashMap<String, RealProperty>();
		List<RealProperty> dupApn = new ArrayList<RealProperty>();
		for (int i = 0; i < properties.size(); i++) {
			RealProperty prop = properties.get(i);
			String apn = prop.getApn();
			if (!list.containsKey(apn)) {
				list.put(apn, prop);
			} else {
				log.info("Duplicate APN Found:");
				dupApn.add(prop);
			}
			for (RealProperty dupProp : dupApn) {
				log.info("Duplicate APN" + dupProp);
			}
			assertEquals(0, dupApn.size());
		}
	}

	@Test
	public void checkMlsImportCompleteness() {

		int records = 0;
		File dir = new File(AppFiles.getDataDir());
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
			for (File file : directoryListing) {
				String fileName = file.getName();
				if (fileName.startsWith("MLSExport")) {
					int lineCount = RealStatUtil.countLines(dir + "\\" + fileName);
					System.out.println("Line count for file " + fileName + " = " + lineCount);
					records += lineCount - 1;
				}
			}
		} else {
			fail();
		}
		long ptCount = ptRepo.count();
		assertEquals(records, ptCount);
	}

}
