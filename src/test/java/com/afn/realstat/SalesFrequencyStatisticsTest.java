package com.afn.realstat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("Prod")
public class SalesFrequencyStatisticsTest {

	@Autowired
	SalesFrequencyStatistics sfs;

	@Test
	public void testTimeBetweenSales2016() {
		sfs.timeBetweenSales2016();
	}

	@Test
	public void testSalesByYearCityAndZip() {
		sfs.salesByYearCityAndZip();
	}
	
	@Test
	public void testSalesActivtyByYearCityZipBuildingType() {
		sfs.salesActivtyByYearCityZipBuildingType();
	}
	
	@Test
	public void testPropertiesOnMarketByCityZipBuildingType() {
		sfs.propertiesOnMarketByCityZipBuildingType();
	}
	
	@Test
	public void runAllStatistics() {
		sfs.salesByYearCityAndZip();
		sfs.doubleEndedTransactionsByAgent();
		sfs.checkPropertyLinking();
		sfs.checkAgentLinking();
		
	}

}
