package com.afn.realstat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.afn.Application;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("dev")
@WebAppConfiguration
public class AddressTest {

	@Autowired
	AddressRepository adrRepo;

	@Before
	public void initialize() {
		adrRepo.deleteAll();
	}
	
	@Test
	public void testSaveAndUpdateAddress() {

		Address addr01 = new Address("112 Indian Rd", "Piedmont", "94610");
		adrRepo.saveOrUpdate(addr01);

		Example<Address> e = addr01.getRefExample();
		Address addr01a = adrRepo.findOne(e);

		assertEquals(addr01.getId(), addr01a.getId());

		// check that the address will be updated and no new address created
		long cntBefore = adrRepo.count();
		adrRepo.saveOrUpdate(addr01a);
		long cntAfter = adrRepo.count();
		assertEquals(cntBefore, cntAfter);

		assertEquals("112", addr01.getStreetNbr());
		assertEquals(null, addr01.getStreetPreDir());
		assertEquals("INDIAN", addr01.getStreetName());
		assertEquals("RD", addr01.getStreetType());
		assertEquals(null, addr01.getStreetPostDir());
		assertEquals(null, addr01.getUnitName());
		assertEquals(null, addr01.getUnitNbr());
		assertEquals("PIEDMONT", addr01.getCity());
		assertEquals("CA", addr01.getState());
		assertEquals("94610", addr01.getZip());
		assertEquals("1201", addr01.getZip4());
		assertEquals("ALAMEDA COUNTY", addr01.getCounty());
		assertEquals("USA", addr01.getCountry());
		assertEquals("112 INDIAN RD", addr01.getFullStreet());

		assertEquals(-122.220519, addr01.getLocation().getX(), 0.000001);
		assertEquals(37.816977, addr01.getLocation().getY(), 0.000001);
	}
	
	@Test
	public void testSaveAndUpdateWithOutRepository() {

		Address addr01 = new Address("112 Indian Rd", "Piedmont", "94610");
		addr01.saveOrUpdate();

		Example<Address> e = addr01.getRefExample();
		Address addr01a = adrRepo.findOne(e);

		assertEquals(addr01.getId(), addr01a.getId());

		// check that the address will be updated and no new address created
		long cntBefore = adrRepo.count();
		addr01a.saveOrUpdate();
		long cntAfter = adrRepo.count();
		assertEquals(cntBefore, cntAfter);

		assertEquals("112", addr01.getStreetNbr());
		assertEquals(null, addr01.getStreetPreDir());
		assertEquals("INDIAN", addr01.getStreetName());
		assertEquals("RD", addr01.getStreetType());
		assertEquals(null, addr01.getStreetPostDir());
		assertEquals(null, addr01.getUnitName());
		assertEquals(null, addr01.getUnitNbr());
		assertEquals("PIEDMONT", addr01.getCity());
		assertEquals("CA", addr01.getState());
		assertEquals("94610", addr01.getZip());
		assertEquals("1201", addr01.getZip4());
		assertEquals("ALAMEDA COUNTY", addr01.getCounty());
		assertEquals("USA", addr01.getCountry());
		assertEquals("112 INDIAN RD", addr01.getFullStreet());

		assertEquals(-122.220519, addr01.getLocation().getX(), 0.000001);
		assertEquals(37.816977, addr01.getLocation().getY(), 0.000001);
	}

	@Test
	public void testWrongZip() {

		Address addr01 = new Address("112 Indian Rd", "Piedmont", "94610");
		adrRepo.saveOrUpdate(addr01);
	
		Address addr02 = new Address("112 Indian Rd", "Piedmont", "94611");
		adrRepo.saveOrUpdate(addr02);
		assertEquals("94610",addr02.getZip());
		assertEquals(addr01.getId(),addr02.getId());
		
	}
	
	@Test
	public void testWrongCity() {

		Address addr01 = new Address("112 Indian Rd", "Piedmont", "94610");
		adrRepo.saveOrUpdate(addr01);
	
		Address addr02 = new Address("112 Indian Rd", "Oakland", "94610");
		adrRepo.saveOrUpdate(addr02);
		assertEquals("PIEDMONT",addr02.getCity());
		assertEquals(addr01.getId(),addr02.getId());
		
	}
	
	@Test
	public void testSpecialCases() {
		
		Address addr = new Address("4928 Proctor Ave", "Oakland", "94618");
		System.out.println(addr);
	}

	@Test
	// test street type lane
	public void testStreetTypeLn() {
		
		Address addr = new Address("85 NORMAN LN.", "Oakland", "94681");
		System.out.println(addr);
		assertNotNull(addr.getLoc());
	}
	
	@Test
	// test no city
	public void testNoCity() {
		
		Address addr = new Address("6167 Bernhard Ave.", null, "94805");
		System.out.println(addr);
	}
	
	
	
	@Test
	public void testSelfSave() {
		Address adr = new Address("501 Hampton Rd.", "Oakland", "94611");
		adr.saveOrUpdate();
	}

	
	
}
