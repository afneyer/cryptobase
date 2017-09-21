package com.afn.realstat;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdReviewTourList {

	public static final Logger log = LoggerFactory.getLogger("app");

	private Map<Date, List<Address>> tours = new HashMap<Date, List<Address>>();
	private TourListRepository tlRepo;
	private File pdfFile;

	public AdReviewTourList(File file) {
		this.tlRepo = TourListEntry.getRepo();
		this.pdfFile = file;
	}

	public void createTourList() {
		String text = getText();
		createTourListBasedOnFields(text);
	}

	public List<Address> getAdresses(Date date) {
		List<Address> adrList = tours.get(date);
		return adrList;
	}

	public List<Date> getDates() {
		List<Date> dates = new ArrayList<Date>(tours.keySet());
		Collections.sort(dates);
		return dates;

	}

	public void createTourListBasedOnFields(String text) {

		// Cleanup text

		// Remove all new line markers. They seem to be less reliable than the
		// field markers "|"
		text = text.replaceAll("\\n", "|");
		text = text.replaceAll("\\r", "|");

		// Ensure that the @-sign is in a separate field
		text = text.replaceAll(" @", "|@");
		text = text.replaceAll("@ ", "@|");

		System.out.println(text);
		// Split Ad Review into fields
		List<String> allFields = Arrays.asList(text.split("\\|"));

		// System.out.println("Text in PDF: " + text);
		// String[] lines = text.split(System.lineSeparator());
		HashMap<Date, List<String>> tourDays = splitByDates(allFields);

		
		for (Date tourDate : tourDays.keySet()) {

			List<String> tourDayFields = tourDays.get(tourDate);
			
			List<List<String>> tourElements = new ArrayList<List<String>>();
			tourElements.addAll(splitIntoTourStops(tourDayFields));
			int numStops = tourElements.size();

			// process all tourElements
			for (List<String> tourElement : tourElements) {

				// process tourElements
				TourListEntry te = parseTourElement(tourElement);
				te.setTourDate(tourDate);
				te.save();

			}
			int tourListEntryCount = TourListEntry.getRepo().findByTourDate(tourDate).size();
			if (tourListEntryCount != numStops) {
				log.error("AdReviewParsing for date " + tourDate + " is missing " + (numStops - tourListEntryCount)
						+ "properties. New city codes?");
			}
		}

	}

	private HashMap<Date, List<String>> splitByDates(List<String> allFields) {

		HashMap<Date, List<String>> tourDays = new HashMap<Date, List<String>>();

		ArrayList<String> tourDay = null;
		Date date = null;
		Date previousDate = null;
		for (String field : allFields) {
			field = field.trim();
			date = getTourDate(field);
			if (date != null) {
				// found a new date
				if (! date.equals(previousDate)) {
					previousDate = date;
					tourDay = new ArrayList<String>();
					tourDays.put(date, tourDay);
				}
			} else {
				// all non-date fields get added to the list
				if (tourDay != null) {
					tourDay.add(field);
				}
			}
		}
		return tourDays;

	}

	private TourListEntry parseTourElement(List<String> tourElement) {

		int cityIndex = 0;
		int priceIndex = getPriceIndex(tourElement);
		int streetIndex = 1;
		int crossStreetIndex = getCrossStreetIndex(tourElement);
		int timeIndex = getTimeIndex(tourElement);
		int descriptionIndex = getDescriptionIndex(tourElement);
		int zipIndex = getZipIndex(tourElement);
		int mlsTagIndex = getMlsTagIndex(tourElement);
		int agentIndex = getZipIndex(tourElement) + 1;
		int officeIndex = getOfficeIndex(tourElement);
		
		String bedBath = getBedBath(tourElement);

		TourListEntry tourListEntry = new TourListEntry();
		tourListEntry.setCity(tourElement.get(cityIndex));
		tourListEntry.setZip(getFieldForIndex(tourElement, zipIndex));
		tourListEntry.setStreet(getFieldForIndex(tourElement, streetIndex));
		tourListEntry.setCrossStreet(getFieldForIndex(tourElement, crossStreetIndex));
		tourListEntry.setBedBath(bedBath);
		tourListEntry.setPrice(getFieldForIndex(tourElement, priceIndex));
		tourListEntry.setDescription(getFieldForIndex(tourElement,descriptionIndex));
		tourListEntry.setTime(getFieldForIndex(tourElement,timeIndex));
		tourListEntry.setAgent(getFieldForIndex(tourElement,agentIndex));

		// office
		String office = tourElement.get(officeIndex);
		office = cleanOffice(office);
		tourListEntry.setOffice(office);

		// phone number
		int phoneIndex = getPhoneIndex(tourElement);
		String phone = tourElement.get(phoneIndex);
		phone = cleanPhone(phone);
		tourListEntry.setPhone(phone);

		String mlsNo = null;
		if (mlsTagIndex + 1 >= tourElement.size()) {
			mlsNo = null;
		} else {
			mlsNo = tourElement.get(mlsTagIndex + 1);
			mlsNo = mlsNo.replaceAll("[^\\d]", "");
			Long mlsNoLong;
			try {
				mlsNoLong = new Long(mlsNo);
				if (mlsNoLong > 10000000) {
					mlsNo = mlsNo.trim();
					tourListEntry.setMlsNo(mlsNo);
				} else {
					mlsNo = null;
				}
			} catch (NumberFormatException e) {
				mlsNo = null;
			}

		}

		Address propertyAddress = new Address(tourListEntry.getStreet(), tourListEntry.getCity(),
				tourListEntry.getZip());
		
		if (! isValid(propertyAddress) ) {
			
			// try without city and let Google maps find the city
			propertyAddress = new Address(tourListEntry.getStreet(), null,
					tourListEntry.getZip());
		}
		
		
		propertyAddress.saveOrUpdate();
		tourListEntry.setPropertyAdr(propertyAddress);
		return tourListEntry;
	}

	public static boolean isValid(Address propertyAddress) {	
		
		String zip = propertyAddress.getZip();
		String zipMatch = "94[5678]..";
		if (zip == null) {
			return false;
		}
		if ( ! zip.matches(zipMatch) ) {
			return false;
		}
		return true;
		
	}

	private String cleanPhone(String phone) {
		phone = phone.replaceAll("[^\\d-]", "");
		return phone;
	}

	private String cleanOffice(String office) {
		office = office.replaceAll("[\\d-]", "");
		return office;
	}

	private String getFieldForIndex(List<String> tourElement, int index) {
		String str = "";
		if (index == 0) {
			return str;
		} else {
			return tourElement.get(index);
		}

	}

	private int getPriceIndex(List<String> tourElement) {

		for (int i = 0; i < tourElement.size(); i++) {
			String field = tourElement.get(i);
			if (field.contains("$")) {
				field = field.replaceAll(",", "");
				field = field.replaceAll("[$]", "");
				if (StringUtils.isNumeric(field)) {
					return i;
				}
			}
		}
		log.warn("Parsing Tour List Element : " + "Cannot find Price for " + printTourElement(tourElement));
		return 0;
	}

	private int getCrossStreetIndex(List<String> tourElement) {

		for (int i = 0; i < tourElement.size(); i++) {
			String field = tourElement.get(i);
			field.trim();
			if (field.equals("@")) {
				return i + 1;
			}
		}
		log.warn("Parsing Tour List Element : " + "Cannot find CrossStreet marker @ for "
				+ printTourElement(tourElement));
		return 0;
	}

	private int getTimeIndex(List<String> tourElement) {

		for (int i = getPriceIndex(tourElement); i < getZipIndex(tourElement); i++) {
			String field = tourElement.get(i);
			if (field.matches("^[0-9\\-:]{1,12}$")) {
				return i;
			}
		}
		log.warn("Parsing Tour List Element : " + "Cannot find time element " + printTourElement(tourElement));
		return 0;
	
	}
	private int getDescriptionIndex(List<String> tourElement) {

		int index = getPriceIndex(tourElement);
		int stringLen = 0;
		for (int i = getPriceIndex(tourElement); i < getZipIndex(tourElement); i++) {
			String field = tourElement.get(i);
			if (field.length() > stringLen) {
				stringLen = field.length();
				index = i;
			}
		}
		if (index == getPriceIndex(tourElement)) {
			log.warn("Parsing Tour List Element : " + "Cannot find description for " + printTourElement(tourElement));
			return 0;
		} else {
			return index;
		}
	}

	private int getZipIndex(List<String> tourElement) {

		for (int i = 0; i < tourElement.size(); i++) {
			String field = tourElement.get(i);
			field = field.trim();
			field = field.replaceAll("\\n", "");
			if (field.startsWith("94") && StringUtils.isNumeric(field)) {
				return i;
			}
		}

		log.warn("Parsing Tour List Element : " + "Cannot find zip for " + printTourElement(tourElement));
		return 0;
	}

	private int getMlsTagIndex(List<String> tourElement) {

		for (int i = 0; i < tourElement.size(); i++) {
			String field = tourElement.get(i);
			field = field.trim();
			if (field.startsWith("MLS#")) {
				if (i < tourElement.size()) {
					return i;
				}
			}
		}

		log.warn("Parsing Tour List Element : " + "Cannot find MLS# tag for " + printTourElement(tourElement));
		return 0;
	}

	private int getOfficeIndex(List<String> tourElement) {
		int index = getMlsTagIndex(tourElement) - 2;
		if ( index > 0 && index < tourElement.size() ) {
			return index;
		} else {
			log.warn("Parsing Tour List Element : " + "Cannot find Office " + printTourElement(tourElement));
			return 0;
		}
	}

	private int getPhoneIndex(List<String> tourElement) {
		int index = getMlsTagIndex(tourElement) - 1;
		if ( index > 0 && index < tourElement.size() ) {
			return index;
		} else {
			log.warn("Parsing Tour List Element : " + "Cannot find Phone " + printTourElement(tourElement));
			return 0;
		}
		
	}

	private String getBedBath(List<String> tourElement) {
		String bb = new String();
		int start = getCrossStreetIndex(tourElement);
		int end = getPriceIndex(tourElement);
		for (int i = start + 1; i < end; i++) {
			bb += tourElement.get(i);
		}
		return bb;
	}

	private String printTourElement(List<String> tourElement) {
		String printStr = "|";
		for (String e : tourElement) {
			printStr += e + "|";
		}
		return printStr;
	}

	private List<List<String>> splitIntoTourStops(List<String> tourDayFields) {

		List<List<String>> tourStops = new ArrayList<List<String>>();
		List<String> tourStop = null;
		for (String field : tourDayFields) {

			String city = getCity(field);
			if (city != null) {
				// found the beginning of a tour stop

				// add the previous tour stop
				if (tourStop != null) {
					tourStops.add(tourStop);
				}
				tourStop = new ArrayList<String>();
				tourStop.add(city);
			} else {
				if (tourStop != null) {
					tourStop.add(field);
				}
			}
		}

		// add the last tourStop
		if (tourStop != null) {
			tourStops.add(tourStop);
		}

		return tourStops;
	}

	private String getCity(String str) {
		String city = str.trim();
		switch (city) {
		case "PIED":
			return "Piedmont";
		case "OAK":
			return "Oakland";
		case "PLEAS":
			return "Pleasant Hill";
		case "ALA":
			return "Alameda";
		case "HAY":
			return "Hayward";
		case "KENS":
			return "Kensington";
		case "BERK":
			return "Berkeley";
		case "RICH":
			return "Richmond";
		case "E.C.":
			return "El Ceritto";
		case "ALB":
			return "Albany";
		case "EMERY":
			return "Emeryville";
		default:
			return null;
		}
	}

	private Date getTourDate(String field) {
		Date date = null;
		SimpleDateFormat formatter = new SimpleDateFormat("E, MMM dd, yyyy");
		try {
			date = formatter.parse(field);
			return date;
		} catch (ParseException e) {
		}
		return date;
	}

	public String getText() {

		String text;
		try {
			PDDocument doc = PDDocument.load(pdfFile);
			PDFTextStripper pdfStrip = new PDFTextStripper();
			pdfStrip.setArticleStart("|");
			pdfStrip.setArticleEnd("|");
			pdfStrip.setParagraphStart("|");
			pdfStrip.setParagraphEnd("|");
			pdfStrip.setWordSeparator("|");
			pdfStrip.setLineSeparator("|");
			pdfStrip.setSortByPosition(true);

			text = pdfStrip.getText(doc);

			System.out.println("|" + pdfStrip.getArticleStart() + "|");
			System.out.println("|" + pdfStrip.getArticleEnd() + "|");
			System.out.println("|" + pdfStrip.getParagraphStart() + "|");
			System.out.println("|" + pdfStrip.getParagraphEnd() + "|");
			System.out.println("|" + pdfStrip.getWordSeparator() + "|");

			// ensure that there is a line separation before the zip code
			// text = text.replaceAll("\\|94", "\n\\|94");
			// text = text.replaceAll("\n\n", "\n");

			text = ensureLineBreakAfterPrice(text);

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return text;
	}

	private String ensureLineBreakAfterPrice(String text) {

		String[] fields = text.split("\\|");
		ArrayList<String> list = new ArrayList<String>();
		for (String field : fields) {
			list.add(field);
			if (field.startsWith("$")) {
				String sep = "\n";
				list.add(sep);
			}
		}

		String resString = StringUtils.join(list, "|");

		return resString;
	}

	public List<TourListEntry> getTourList(Date tourDate) {
		List<TourListEntry> tleList = tlRepo.findByTourDate(tourDate);
		return tleList;
	}
}
