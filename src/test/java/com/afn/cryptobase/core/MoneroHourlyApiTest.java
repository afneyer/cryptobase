package com.afn.cryptobase.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
public class MoneroHourlyApiTest {

	@Autowired
	MoneroHourlyApi mhApi;
	
	@Autowired
	MoneroHourlyRepository mhRepo;
	
	@Test
	public void testApiGetEchangeRateData() {
		
		Long timestamp = MoneroBlock.toMoneroTimestamp(MoneroBlock.refBlockDateHour);
		mhApi.updateHourlyExchangeRate("USD", timestamp, 1L);
		mhApi.updateHourlyExchangeRate("BTC", timestamp, 1L);

	}
	
	@Test
	public void testUpdateHourlyExchangeRatesForDb() {
		mhApi.updateHourlyExchangeRatesForDb();
	}
	
	@Test
	public void testUpdateHourlyEchangeRates() {
		
		Long timestamp = MoneroBlock.toMoneroTimestamp(LocalDateTime.parse("2017-09-30T15:00:00"));
		MoneroHourly mh = mhRepo.findByStartTimestamp(timestamp);
		assertNotNull(mh);
		
		Double eBTC = mh.getExchangeBTC();
		Double eUSD = mh.getExchangeUSD();
		mh.setExchangeBTC(0.0);
		mh.setExchangeUSD(0.0);
		mhApi.updateHourlyExchangeRates(mh);
		
		assertEquals(eBTC, mh.getExchangeBTC());
		assertEquals(eUSD, mh.getExchangeUSD());
		
	}
}
