package com.afn.cryptobase.core;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.afn.realstat.Application;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("dev")
@WebAppConfiguration
public class MoneroHourlyApiTest {

	@Autowired
	MoneroBlockRepository repo;

	@Test
	public void testApiGetEchangeRateData() {
		
		MoneroHourlyApi.updateHourlyExchangeRate("USD", 1501657200L, 3L);
		MoneroHourlyApi.updateHourlyExchangeRate("BTC", 1501657200L, 3L);

	}
	
}
