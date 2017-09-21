package com.afn.realstat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)

@ActiveProfiles("prod")
public class AdReviewTourListTest {

	@Autowired
	AddressRepository adrRepo;

	@Autowired
	TourListRepository tlRepo;

	private final String testDataDir = AppFiles.getTestDataDir();

	@Before
	public void initialize() {
		
		tlRepo.deleteAll();
	}

	@Test
	// TODO: further testing needed, still some wrong fields
	public void testCreateTourList01() {
		
		String tourFile = "17-03-25_Tour";
		createTour( tourFile );

		Date tourDate = AfnDateUtil.of(2017, 3, 27);
		List<TourListEntry> tourList = tlRepo.findByTourDate(tourDate);
		assertEquals(23, tourList.size());
		
		for (TourListEntry tle : tourList) {
			if (tle.getStreet() == "5728 MORAGA AVE.") {
				assertEquals("Oakland", tle.getCity());
			}
		}

		tourDate = AfnDateUtil.of(2017, 3, 28);
		tourList = tlRepo.findByTourDate(tourDate);
		assertEquals(2, tourList.size());

		tourDate = AfnDateUtil.of(2017, 3, 29);
		tourList = tlRepo.findByTourDate(tourDate);
		assertEquals(1, tourList.size());

		tourDate = AfnDateUtil.of(2017, 3, 30);
		tourList = tlRepo.findByTourDate(tourDate);
		assertEquals(15, tourList.size());

	}
	
	@Test
	public void verifyTestCreateTourList02() {
		String baseName =  "17-03-25_Tour";
		createTour(baseName);
	}
	
	public AdReviewTourList createTour( String fileBaseName ) {

		File file = new File(testDataDir, fileBaseName + ".pdf");
		AdReviewTourList atl = new AdReviewTourList(file);

		// write out the text file for debugging
		writeText(atl.getText(), file);

		// create the tour list entries
		atl.createTourList();
		return atl;

	}


	@Test
	public void testGetDates() {
		String baseName =  "17-03-25_Tour";
		
		AdReviewTourList atl = createTour(baseName);
		List<Date> dates = atl.getDates();
		assertEquals(4, dates.size());
		for (Date d : dates) {
			System.out.println(d);
		}
	}

	@Test
	public void testGetAddressesForDate() {
		AdReviewTourList adReviewList = createTour("17-03-25_Tour");
		List<Date> dates = adReviewList.getDates();
		List<Address> adr = null;
		adr = adReviewList.getAdresses(dates.get(0));
		assertEquals(23, adr.size());
		adr = adReviewList.getAdresses(dates.get(1));
		assertEquals(2, adr.size());
		adr = adReviewList.getAdresses(dates.get(2));
		assertEquals(1, adr.size());
		adr = adReviewList.getAdresses(dates.get(3));
		assertEquals(15, adr.size());
	}

	@Test
	public void testPDFTestStripper() {

		File file = new File(testDataDir, "17-03-25_Tour.pdf");
		PDDocument doc;
		String text = null;
		try {
			doc = PDDocument.load(file);
			text = new PDFTextStripper().getText(doc);
			System.out.println(text);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void checkTourListByDate() {
		Date date = AfnDateUtil.of(2017, 3, 27);
		List<TourListEntry> tleList = tlRepo.findByTourDate(date);
		for (TourListEntry tle : tleList) {
			System.out.println(tle);
		}
	}

	@Test
	public void testFindAllDistinctDates() {
		List<Date> dateList = tlRepo.findAllDisctintDatesNewestFirst();
		for (Date d : dateList) {
			System.out.println(d);
		}
	}

	@Test
	public void importTourPdfs() {

		File dir = new File(AppFiles.getUploadDir());
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
			for (File file : directoryListing) {
				String fileName = file.getName();
				System.out.println("Importing file = " + fileName);
				AdReviewTourList atl = new AdReviewTourList(file);
				atl.createTourList();
			}
		} else {
			fail();
		}
	}

	

	public void writeText(String text, File file) {
		String fileName = FilenameUtils.getBaseName(file.getName());
		fileName += ".txt";
		File txtFile = new File(AppFiles.getTestOutputDir(), fileName);
		PrintWriter printWriter;
		try {
			printWriter = new PrintWriter(txtFile);
			printWriter.print(text);
			printWriter.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@Test
	public void testIsValid() {
		
		Address adr01 = new Address("6167 Bernhard Ave.","Berkeley","94805");
		// Google map finds some address in NY because the city is wrong
		assertFalse( AdReviewTourList.isValid(adr01));
		
		Address adr02 = new Address("6167 Bernhard Ave.", null,"94805");
		assertTrue( AdReviewTourList.isValid(adr02));
		
	}

}
