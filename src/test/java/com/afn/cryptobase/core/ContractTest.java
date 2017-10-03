package com.afn.cryptobase.core;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

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
public class ContractTest {
	
	@Autowired
	ContractRepository cRepo;

	@Test
	public void testContractCreation() {

		String company = "Genesis";
		LocalDateTime startDateTime = LocalDateTime.parse("2017-08-26T08:09:26");
		Contract contract1 = new Contract(company, startDateTime, 365*24*3600L, 200L,
			"CryptoNight", "XMR", "Monero", 166.60, "USD"); 
		
		contract1.saveOrUpdate();
		
		Contract contract2 = cRepo.findByCompanyAndStartDateTime(company,startDateTime);
		
		assertEquals(contract1,contract2);

	}

	@Test(expected = RuntimeException.class)
	public void testInvalidRecordCreation() {
		
		//TODO fix

		// verify the an invalid MoneroHourly record does not get created
		LocalDateTime ldt = MoneroBlock.refBlockDateHour;
		MoneroHourly mh = new MoneroHourly(ldt.plusSeconds(5572));
		mh.saveOrUpdate();

	}

}
