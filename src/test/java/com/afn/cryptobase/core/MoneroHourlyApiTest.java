package com.afn.cryptobase.core;

import org.junit.Test;
import org.junit.runner.RunWith;
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

	
	@Test
	public void testApiGetEchangeRateData() {
		
		MoneroHourlyApi.updateHourlyExchangeRate("USD", 1501657200L, 3L);
		MoneroHourlyApi.updateHourlyExchangeRate("BTC", 1501657200L, 3L);

	}
	
}
