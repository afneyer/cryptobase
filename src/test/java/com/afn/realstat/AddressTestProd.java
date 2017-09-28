package com.afn.realstat;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.afn.Application;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("prod")
public class AddressTestProd {

	@Autowired
	AddressRepository adrRepo;
	
	@Test
	public void removeDuplicates() {
		// TODO write this test
		// List<Address> adrList = adrRepo.findAll();
	}

	@Test
	public void testSaveAndUpdateAddress() {

		Address addr01 = new Address("112 Indian Rd", "Piedmont", "94610");
		adrRepo.saveOrUpdate(addr01);

		Example<Address> e = Example.of(addr01);
		Address addr01a = adrRepo.findOne(e);

		assertEquals(addr01.getId(), addr01a.getId());

		// check that the address will be updated and no new address created
		long cntBefore = adrRepo.count();
		adrRepo.saveOrUpdate(addr01a);
		long cntAfter = adrRepo.count();
		assertEquals(cntBefore, cntAfter);

		assertEquals("112", addr01.getStreetNbr());
		assertEquals("", addr01.getStreetPreDir());
		assertEquals("INDIAN", addr01.getStreetName());
		assertEquals("RD", addr01.getStreetType());
		assertEquals("", addr01.getStreetPostDir());
		assertEquals("", addr01.getUnitName());
		assertEquals("", addr01.getUnitNbr());
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
	

	
	
}
