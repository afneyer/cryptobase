package com.afn.realstat;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import com.skovalenko.geocoder.address_parser.ParsedUsAddress;
import com.skovalenko.geocoder.address_parser.UnparsedAddress;
import com.skovalenko.geocoder.address_parser.us.UsAddressParser;

import ch.qos.logback.classic.Logger;

public class AddressParser {

	private static final Logger log = (Logger) LoggerFactory.getLogger("import");

	private static char FIELD_SEP = '|';

	private ParsedUsAddress parsedAddress;
	private String rawAddress;
	private String addressLine;
	private String street;
	private String unit;
	private String city;
	private String zip;
	private String zip4;

	final static Map<String, String> streetNameTranslator;
	static {
		Map<String, String> snt = new HashMap<String, String>();
		snt.put("PLEASANT", "PLEASANT VALLEY");
		streetNameTranslator = Collections.unmodifiableMap(snt);
	}
	
	final static Map<String, String> streetTypeTranslator;
	static {
		Map<String, String> snt = new HashMap<String, String>();
		snt.put("LA", "LN");
		streetTypeTranslator = Collections.unmodifiableMap(snt);
	}

	public AddressParser() {
	};

	// This constructor is primarily used by property_transaction since the unit
	// number is in a separate field in property_transaction
	public AddressParser(String inStreet, String inUnit, String inCity, String inZip) {

		street = inStreet;
		zip = inZip;
		city = inCity;
		unit = inUnit;
		
		addressLine = inStreet;
		if (unit != null && !unit.isEmpty()) {
			 addressLine += " #" + unit;
		}
		rawAddress = street + ", " + city + ", " + zip;

		separateZip(inZip);
		
		parse();
		
	}

	private void separateZip(String inZip) {
		if (inZip != null) {
			// if the zip-code has a 4-digit extension remove it because the parser
			// does not use it, store it directly in zip4;
			inZip = inZip.trim();
			inZip = StringUtils.remove(inZip, ' ');
			if (inZip.length() < 5) {
				log.warn("Invalid Zip Code: " + rawAddress);
				return;
			}
			if (inZip.length() == 5) {
				zip = inZip;
				zip4 = null;
				return;
			}
			String[] zipParts = zip.split("-");
			if (zipParts.length == 2) {
				zip = zipParts[0];
				zip4 = zipParts[1];
			}
		} else {
			zip = null;
		}

	}

	// This constructor is primarily used by realProperty because the unit
	// number is included in the streetUnit field
	public AddressParser(String streetUnit, String city, String zip) {
		this(streetUnit, null, city, zip);
	}

	private void parse() {
		UnparsedAddress upa = new UnparsedAddress(addressLine, city, zip);
		UsAddressParser parser = new UsAddressParser();
		parsedAddress = parser.parse(upa);
		fixSpecialCases();
	}

	private void fixSpecialCases() {
		String newStreetName = streetNameTranslator.get(this.parsedAddress.getStreetName());
		if (newStreetName != null) {
			parsedAddress.setStreetName(newStreetName);
		}
		
		String newStreetType = streetTypeTranslator.get(this.parsedAddress.getStreetType());
		if (newStreetType != null) {
			parsedAddress.setStreetType(newStreetType);
		}
	}
	
	public String getStreetNbr() {
	    return parsedAddress.getStreetNumber(); 
	}
	
	public String getStreetPreDir() {
	    return parsedAddress.getStreetPreDir(); 
	}
	
	public String getStreetName() {
	    return parsedAddress.getStreetName(); 
	}
	
	public String getStreetType() {
	    return parsedAddress.getStreetType(); 
	}
	
	public String getStreetPostDir() {
	    return parsedAddress.getStreetPostDir(); 
	}
	
	public String getSubUnitNumber() {
	    return parsedAddress.getSubUnitNumber(); 
	}
	
	public String getSubUnitName() {
	    return parsedAddress.getSubUnitName(); 
	}
	
	public String getCity() {
	    return parsedAddress.getCity(); 
	}
	
	public String getState() {
	    return parsedAddress.getState(); 
	}
	
	public String getZip() {
	    return parsedAddress.getZip(); 
	}
	
	public String getZip4() {
	    return zip4; 
	}
	
	public String getCounty() {
	    return parsedAddress.getCounty(); 
	}
	
	public String getCountry() {
	    return parsedAddress.getCountry(); 
	}

	public void setRawAddress(String rawAddress) {
		this.rawAddress = rawAddress;
	}

	

	/*
	 * cleanAddress contains no street type or unit numbers
	 */
	public String getCleanAddress() {
		StringBuffer sb = new StringBuffer();
		sb.append(parsedAddress.getStreetNumber());
		sb.append(FIELD_SEP);
		sb.append(parsedAddress.getStreetPreDir());
		sb.append(FIELD_SEP);
		sb.append(parsedAddress.getStreetName());
		sb.append(FIELD_SEP);
		sb.append(parsedAddress.getStreetPostDir());
		sb.append(FIELD_SEP);
		// sb.append(parsedAddress.getSubUnitName());
		sb.append(parsedAddress.getSubUnitNumber());
		sb.append(FIELD_SEP);
		sb.append(parsedAddress.getCity());
		sb.append(FIELD_SEP);
		sb.append(parsedAddress.getZip());
		if (parsedAddress.getZip() == null || parsedAddress.getZip().isEmpty()) {
			log.warn("Clean address zip code is empty or null");
		}
		return sb.toString();
	}

	public String toString() {
		return parsedAddress.toString();
	}

	public boolean hasUnitInfo() {
		String unitNbr = parsedAddress.getSubUnitNumber();
		if (unitNbr != null && !unitNbr.isEmpty()) {
			return true;
		}
		return false;
	}

	public boolean isInSameBuilding(AddressParser adr) {
		if (parsedAddress.getStreetName().equals(adr.parsedAddress.getStreetName())
				&& parsedAddress.getStreetNumber().equals(adr.parsedAddress.getStreetNumber())
				&& parsedAddress.getCity().equals(adr.parsedAddress.getCity())
				&& parsedAddress.getZip().equals(adr.parsedAddress.getZip())) {
			return true;
		}
		return false;
	}

	public String getRawAddress() {
		return rawAddress;
	}

}
