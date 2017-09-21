package com.afn.realstat;

import static org.junit.Assert.*;

import org.junit.Test;

public class AfnDateUtilTest {

	@Test
	public void testYearsBetween() {
		
		long years;
	
		years = AfnDateUtil.yearsBetween(AfnDateUtil.of(1900,1,1), AfnDateUtil.of(2000, 1, 1));
		assertEquals(100,years);
		
		years = AfnDateUtil.yearsBetween(AfnDateUtil.of(2016,1,1), AfnDateUtil.of(2017, 1, 1));
		assertEquals(1,years);
		
		years = AfnDateUtil.yearsBetween(AfnDateUtil.of(2016,1,1), AfnDateUtil.of(2017, 6, 1));
		assertEquals(1,years);
		
		years = AfnDateUtil.yearsBetween(AfnDateUtil.of(2016,1,1), AfnDateUtil.of(2017, 6, 30));
		assertEquals(1,years);
		
		years = AfnDateUtil.yearsBetween(AfnDateUtil.of(2016,1,1), AfnDateUtil.of(2017, 7, 1));
		assertEquals(1,years);
		
		years = AfnDateUtil.yearsBetween(AfnDateUtil.of(2016,1,1), AfnDateUtil.of(2017, 7, 4));
		assertEquals(1,years);
		
		years = AfnDateUtil.yearsBetween(AfnDateUtil.of(2016,1,1), AfnDateUtil.of(2017, 12, 31));
		assertEquals(1,years);
		
		years = AfnDateUtil.yearsBetween(AfnDateUtil.of(2016,1,1), AfnDateUtil.of(2018, 1, 1));
		assertEquals(2,years);
	
	}
}
