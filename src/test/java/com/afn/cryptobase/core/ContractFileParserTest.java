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

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("dev")
@WebAppConfiguration
public class ContractFileParserTest {

	@Autowired
	ContractRepository cRepo;
	
	@Autowired
	ContractRevenueRepository crRepo;

	@Autowired
	ContractFileParser cfParser;

	@Test
	public void testParseFile() {

		String company = "Genesis";
		LocalDateTime startDateTime = LocalDateTime.parse("2017-08-26T08:09:26");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'_'hhmmss");
		String fileName = ContractFileParser.contractFileDir + "\\" + company + "_" + startDateTime.format(formatter) + ".txt";
		
		File file = new File(fileName);
		Contract contract1 = cfParser.updateContract(file);
		contract1 = cRepo.findByCompanyAndStartDateTime(company, startDateTime);

		assertEquals(contract1.getCompany(),company);
		assertEquals(contract1.getStartDateTime(),startDateTime);
		assertEquals(contract1.getDuration(), new Long(365*24*3600));
		assertEquals(contract1.getHashRate(), new Long(200));
		assertEquals(contract1.getAlgorithm(),"CryptoNight");
		assertEquals(contract1.getRevenueCurrency(),"XMR");
		assertEquals(contract1.getType(),"Monero");
		assertEquals(contract1.getPrice(), new Double(166.60));
		assertEquals(contract1.getPriceCurrency(),"USD");
		
		// verify contract selected revenue records
		LocalDateTime ldt1 = LocalDateTime.parse("2017-09-16T00:00:00");
		ContractRevenue rev1 = crRepo.findByContractAndStartDateTime(contract1, ldt1);
		assertEquals(rev1.getRevenue(),new Double(0.00369792));
		assertEquals(rev1.getRevenueCurrency(),"XMR");
		
		ldt1 = LocalDateTime.parse("2017-09-26T00:00:00");
		rev1 = crRepo.findByContractAndStartDateTime(contract1, ldt1);
		assertEquals(rev1.getRevenue(),new Double(0.0037702));
		assertEquals(rev1.getRevenueCurrency(),"XMR");

	}

}
