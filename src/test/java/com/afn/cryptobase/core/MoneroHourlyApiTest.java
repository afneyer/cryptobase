package com.afn.cryptobase.core;

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
	
	@Test
	public void testApiGetEchangeRateData() {
		
		Long timestamp = MoneroBlock.toMoneroTimestamp(MoneroBlock.refBlockDateHour);
		MoneroHourlyApi.updateHourlyExchangeRate("USD", timestamp, 1L);
		MoneroHourlyApi.updateHourlyExchangeRate("BTC", timestamp, 1L);

	}
	
	@Test
	public void testApiUpdateExchangeRates() {
		mhApi.updateHourlyExchangeRates();
	}
	
}
