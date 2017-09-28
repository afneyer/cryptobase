	package com.afn.realstat;



import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.assertj.core.util.DateUtil;
import org.junit.Before;
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
public class AppParamTest {
	
	@Autowired
	AppParamRepository apRepo;
	
	@Autowired 
	AppParamManager apMgr;
	
	@Before
	public void setUp() {
		apRepo.deleteAll();
		apMgr.initializeParameters();
	}
	
	

	@Test
	public void testParameterInitialization() {
		assertEquals("50", apMgr.getVal("maxCallsPerSecond", "MAP"));
		assertEquals("2000", apMgr.getVal("maxCallsPerDay", "MAP"));
		assertEquals("0", apMgr.getVal("callsToday","MAP"));
	    assertEquals("2017-03-01T10:00:00.000", apMgr.getVal("lastCall","MAP"));
	  
	    long paramCount1 = apRepo.count();
	    
	    // initialize a second time and count
	    apMgr.initializeParameters();
	    long paramCount2 = apRepo.count();
	    assertEquals(paramCount1, paramCount2);
	}
	
	@Test
	public void testChangingParameter() {
		Date d1 = AfnDateUtil.dateYesterday();
		String str1 = DateUtil.formatAsDatetimeWithMs(d1);
		apMgr.setVal("lastCall", "MAP", d1);
		Date d2 = apMgr.getDateVal("lastCall", "MAP");
		String str2 = DateUtil.formatAsDatetimeWithMs(d2);
		assertEquals(str1,str2);
		assertTrue(d1.equals(d2));
	}
	
}
