package com.afn.cryptobase.core;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.afn.Application;
import com.afn.realstat.AppFiles;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("dev")
@WebAppConfiguration
public class ContractManagerTest {

	@Autowired
	ContractRepository cRepo;
	
	@Autowired
	ContractRevenueRepository crRepo;

	@Autowired
	ContractFileParser cfParser;
	
	@Autowired
	ContractManager cMgr;

	@Test
	public void testContractActualVersusTarget() {
		
		String company = "Genesis";
		LocalDateTime startDateTime = LocalDateTime.parse("2017-08-26T08:09:26");
		
		String fileName = "Genesis_20170826_080926_actual_target.csv";
		File file = new File( AppFiles.getTestOutputDir() + "\\" + fileName);
		Contract contract = cRepo.findByCompanyAndStartDateTime(company, startDateTime);
		String[][] array = cMgr.actualVersusTargetRevenue(contract);
		cMgr.arrayToFile(array, file);
		
	}

}
