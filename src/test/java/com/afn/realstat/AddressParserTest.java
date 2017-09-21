package com.afn.realstat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AddressParserTest {

	@Test
	public void testCleanAddress() {

		AddressParser origAddr;
		String targetStr;

		// Test 1
		origAddr = new AddressParser("2140 Arrowhead street", "Oakland", "94611");
		targetStr = "2140||ARROWHEAD|||OAKLAND|94611";
		assertEquals(targetStr, origAddr.getCleanAddress());

		origAddr = new AddressParser("2140 Arrowhead", "", "OAKLAND", "94611");
		assertEquals(targetStr, origAddr.getCleanAddress());

		// Test 2
		// AddressParser='1 KELTON CT', Unit='7H', City='OAKLAND',Zip='94611'
		origAddr = new AddressParser("1 KELTON CT", "#7H", "OAKLAND", "94611");
		targetStr = "1||KELTON||7H|OAKLAND|94611";
		assertEquals(targetStr, origAddr.getCleanAddress());

		origAddr = new AddressParser("1 Kelton Ct. Apt. 7H", "Oakland", "94611");
		assertEquals(targetStr, origAddr.getCleanAddress());

		// Test 3
		// AddressParser='155 PEARL ST', Unit='304', City='OAKLAND',Zip='94611'
		origAddr = new AddressParser("155 PEARL ST", "304", "OAKLAND", "94611");
		targetStr = "155||PEARL||304|OAKLAND|94611";
		assertEquals(targetStr, origAddr.getCleanAddress());

		origAddr = new AddressParser("155 Pearl St Apt 304", "Oakland", "94611");
		assertEquals(targetStr, origAddr.getCleanAddress());

		// Test 4
		// AddressParser='2005 PLEASANT VALLEY', Unit='308',
		// City='OAKLAND',Zip='94611'
		origAddr = new AddressParser("2005 PLEASANT VALLEY", "308", "OAKLAND", "94611");
		targetStr = "2005||PLEASANT VALLEY||308|OAKLAND|94611";
		assertEquals(targetStr, origAddr.getCleanAddress());

		origAddr = new AddressParser("2005 Pleasant Valley Ave. #308", "OAKLAND", "94611");
		targetStr = "2005||PLEASANT VALLEY||308|OAKLAND|94611";
		assertEquals(targetStr, origAddr.getCleanAddress());

		// Test 5
		origAddr = new AddressParser("150 Pearl St Apt 315", "OAKLAND", "94611");
		targetStr = "150||PEARL||315|OAKLAND|94611";
		assertEquals(targetStr, origAddr.getCleanAddress());
		
		// Test 6
		origAddr = new AddressParser("150 Pearl St Apt 315", "OAKLAND", "94611-4321");
		targetStr = "150||PEARL||315|OAKLAND|94611";
		assertEquals(targetStr, origAddr.getCleanAddress());
		assertEquals("4321", origAddr.getZip4());
	}

	@Test
	public void testHasUnitInfo() {
		AddressParser adr;
		adr = new AddressParser("1 KELTON CT", "#7H", "OAKLAND", "94611");
		assertTrue(adr.hasUnitInfo());

		adr = new AddressParser("2140 Arrowhead street", "Oakland", "94611");
		assertFalse(adr.hasUnitInfo());
	}

	@Test
	public void testIsInSameBuilding() {

		AddressParser adr1;
		AddressParser adr2;

		adr1 = new AddressParser("1 KELTON CT", "#7H", "OAKLAND", "94611");
		adr2 = new AddressParser("1 KELTON CT", "8Q", "OAKLAND", "94611");
		assertTrue(adr1.isInSameBuilding(adr2));

		adr2 = new AddressParser("2140 Arrowhead street", "Oakland", "94611");
		assertFalse(adr1.isInSameBuilding(adr2));
	}

	@Test
	public void testSpecialCases() {
		AddressParser origAddr = new AddressParser("2005 Pleasant Valley #308", "OAKLAND", "94611");
		String targetStr = "2005||PLEASANT VALLEY||308|OAKLAND|94611";
		assertEquals(targetStr, origAddr.getCleanAddress());
		
		origAddr = new AddressParser("4938 Proctor Ave", "OAKLAND", "94618-2545");
		targetStr = "4938||PROCTOR|||OAKLAND|94618";
		assertEquals(targetStr, origAddr.getCleanAddress());
	}
	
	@Test 
	// Test street type Lane 
	public void testStreetTypeLn() {
			AddressParser origAddr = new AddressParser("85 NORMAN LN.", "Oakland", "94681");
			String targetStr = "85||NORMAN|||OAKLAND|94681";
			assertEquals(targetStr, origAddr.getCleanAddress());
			assertEquals("LN",origAddr.getStreetType());
	}

}
