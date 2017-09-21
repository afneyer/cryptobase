package com.afn.realstat;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RealStatUtilTest {


	@Test
	public void testCleanApn() {
		
		assertEquals("48D730422", RealStatUtil.cleanApn("48D-7304-22"));
		assertEquals("12987253", RealStatUtil.cleanApn("12-987-253"));
		assertEquals("48G7448133", RealStatUtil.cleanApn("48G-7448-13-3"));
		assertEquals("1081585", RealStatUtil.cleanApn("10081508500"));
		assertEquals("108155", RealStatUtil.cleanApn("10081500500"));
		assertEquals(null, RealStatUtil.cleanApn(null));
		assertEquals(null, RealStatUtil.cleanApn("NOT FOUND IN PUB"));
		assertEquals("48E732287", RealStatUtil.cleanApn("048E732208700"));
		assertEquals("48F73779", RealStatUtil.cleanApn("048F737700900"));
		
	}
	
	@Test
	public void testCleanLicense() {
		assertEquals("1969778", RealStatUtil.cleanLicense("r01969778"));
		assertEquals("969778", RealStatUtil.cleanLicense("r00969778"));
		assertEquals("1976000", RealStatUtil.cleanLicense("1976000"));
		assertEquals("197600", RealStatUtil.cleanLicense("RET00197600"));
		assertEquals(null,RealStatUtil.cleanLicense("    A        "));
	}
	
	@Test
	public void testCountLines() {
		
		String fileName = AppFiles.getDataDir() + "\\MLSExport - 94610_0-1000.csv";
		int lines = RealStatUtil.countLines(fileName);
		assertEquals(3087, lines);
	}
	
	@Test
	public void testConvertBuildingTypeToLandUse() {
		assertEquals("Single Family Residential",RealStatUtil.convertBuildingTypeToLandUse("DE"));
		assertEquals("Residential Townhouse",RealStatUtil.convertBuildingTypeToLandUse("TH"));
		assertEquals("Residential Condominium",RealStatUtil.convertBuildingTypeToLandUse("CO"));
		assertEquals(null,RealStatUtil.convertBuildingTypeToLandUse("DX"));
		assertEquals(null,RealStatUtil.convertBuildingTypeToLandUse(null));
	}
}
	


