package com.afn.realstat;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;

// @RunWith(SpringRunner.class)
// @SpringBootTest(classes = Application.class,
//        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("dev")
public class AbstractImporterTest {

	// @Autowired TODO
	// private RealPropertyRepository repository;

	// @Autowired TODO
	// private CrsImporter importer;

	@Test
	public void testTranslateCsvFileDefault() {

		assertEquals("apn", CrsImporter.translateCsvFileFieldDefault("APN"));
		assertEquals("owner1", CrsImporter.translateCsvFileFieldDefault("Owner 1"));
		assertEquals("firstName", CrsImporter.translateCsvFileFieldDefault("First Name"));
		assertEquals("firstName2", CrsImporter.translateCsvFileFieldDefault("First Name2"));
		assertEquals("totalSquareFootage", CrsImporter.translateCsvFileFieldDefault("Total Square Footage"));
		assertEquals("lotSqFeet", CrsImporter.translateCsvFileFieldDefault("Lot Sq. Feet"));
		assertEquals("listPriceSqft", CrsImporter.translateCsvFileFieldDefault("List $/Sqft"));
		assertEquals("occPercent", CrsImporter.translateCsvFileFieldDefault("Occ%"));
		assertEquals("pool", CrsImporter.translateCsvFileFieldDefault("Pool (Y/N)"));
		assertEquals("priceSqft", CrsImporter.translateCsvFileFieldDefault("Price/SqFt"));
		assertEquals("salePriceSqft", CrsImporter.translateCsvFileFieldDefault("Sale $/SqFt"));
		assertEquals("saleLastListPrice", CrsImporter.translateCsvFileFieldDefault("Sale/Last List $"));
		assertEquals("ticPercentOwnerOffered", CrsImporter.translateCsvFileFieldDefault("TIC % Owner Offered"));
		assertEquals("coListAgentLicenseId", CrsImporter.translateCsvFileFieldDefault("Co-List Agent - License ID"));
		assertEquals("coSellAgentLicenseId", CrsImporter.translateCsvFileFieldDefault("Co-Sell Agent - License ID"));
		assertEquals("listingAgentLicenseId", CrsImporter.translateCsvFileFieldDefault("Listing Agent - License ID"));
		assertEquals("sellingAgent1LicenseId", CrsImporter.translateCsvFileFieldDefault("Selling Agent 1 - License ID"));

	}

}
