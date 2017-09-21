package com.afn.realstat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)

// TODO: Re-factor these tests and the whole class
@ActiveProfiles("dev")
public class RealPropertyManagerTest {

	@Autowired
	RealPropertyManager rpmgr;
	
	@Autowired
	PropertyTransactionManager ptmgr;
	
	@Autowired
	PropertyTransactionRepository ptRepo;
	
	@Autowired
	RealPropertyRepository rpRepo;
	
	@Test
	public void testPropertyTransactionLinking() {
		String apnClean = "1311287";
		
		// find RealProperty
		List<RealProperty> rpList = rpmgr.findByApnClean(apnClean);
		RealProperty rp = null;
		if ( rpList.size() == 1 ) {
			rp = rpList.get(0);
		} 
		if (rp == null) {
			fail("no RealProperty found for apnClean=" + apnClean);
		}
		
		
		// find PropertyTransaction
		List<PropertyTransaction> ptList = ptRepo.findByApnClean(apnClean);
		PropertyTransaction pt = null;
		if (ptList.size() == 1 ) {
			pt = ptList.get(0);
		}
		if (pt == null) {
			fail("no PropertyTransaction found for apnClean=" + apnClean);
		}
		
		assertEquals( pt.getApnClean(), rp.getApnClean());
		
	}
	
	
	@Test
	public void testIteration() {		
		rpmgr.iterateAll();
	}
	
	@Test
	public void testCreateAndLinkAddress() {
		RealProperty rp = createTestProperty01();
		rpmgr.createAndLinkAddress(rp);
		Address adr = rp.getPropertyAdr();
		
		assertEquals("112", adr.getStreetNbr());
		assertEquals("INDIAN", adr.getStreetName());
		assertEquals("RD", adr.getStreetType());
		assertEquals("PIEDMONT", adr.getCity());
		assertEquals("94610", adr.getZip());
		
	}

	private RealProperty createTestProperty01() {
		RealProperty rp = new RealProperty("51-4790-4-11");
		rp.setPropertyAddress("112 Indian Rd");
		rp.setPropertyCity("Piedmont");
		rp.setPropertyZip("94611");
		rpRepo.saveOrUpdate(rp);
		return rp;
	}
	
	@Test
	public void testLinkToAddresses() {
		rpmgr.linkToAddresses();
	}
	
	@Test 
	public void testSpecialCases() {
		List<RealProperty> rpList = rpRepo.findByApn("48B-7141-62");
		if (rpList.size() != 1) {
			fail("Real property count for 48B-7241-62 is:" + rpList.size());
		} else {
			RealProperty rp = rpList.get(0);
			rpmgr.createAndLinkAddress(rp);
			rpRepo.saveOrUpdate(rp);
			assertNotNull(rp.getPropertyAdr());
		}
		
	}
	
	@Test
	public void testCleanAndFixAddresses() {
		rpmgr.cleanAndFixAddresses();
	}
	
	
	

}
