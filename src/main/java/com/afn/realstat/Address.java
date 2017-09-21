package com.afn.realstat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.geo.Point;

import com.afn.realstat.framework.SpringApplicationContext;
import com.afn.realstat.util.GoogleMapApi;
import com.afn.realstat.util.MapLocation;
import com.asprise.ocr.util.StringUtils;
import com.google.maps.model.LatLng;

@Entity
@Table(name = "address", indexes = { @Index(name = "idx_streetNbr", columnList = "streetNbr"),
		@Index(name = "idx_streetName", columnList = "streetName") })
public class Address extends AbstractEntity {

	public static final Logger log = LoggerFactory.getLogger("app");
	private static AddressRepository repo;

	private String streetNbr;
	private String streetPreDir;
	private String streetName;
	private String streetType;
	private String streetPostDir;
	private String unitNbr;
	private String unitName;
	private String city;
	private String state;
	private String zip;
	private String zip4;
	private String county;
	private String country;
	@Column(columnDefinition = "BLOB")
	/* @Lob(type = LobType.BLOB) */
	private Point location;
	private Integer mapLocCalls = new Integer(0);

	public Address() {
	}

	public Address(String inStreet, String inUnit, String inCity, String inZip) {
		AddressParser adrPrsr = new AddressParser(inStreet, inUnit, inCity, inZip);
		setFields(adrPrsr);
	}

	public Address(String inStreetUnit, String inCity, String inZip) {
		this(inStreetUnit, null, inCity, inZip);
	}
	
	public Address(String inStreetUnit, String inCity, String inZip, Point inLocation) {
		location = inLocation;
		AddressParser adrPrsr = new AddressParser(inStreetUnit, null, inCity, inZip);
		setFields(adrPrsr);
	}
	
	private void setFields(AddressParser prsdAdr) {
		setStreetNbr(prsdAdr.getStreetNbr());
		setStreetPreDir(prsdAdr.getStreetPreDir());
		setStreetName(prsdAdr.getStreetName());
		setStreetType(prsdAdr.getStreetType());
		setStreetPostDir(prsdAdr.getStreetPostDir());
		setUnitNbr(prsdAdr.getSubUnitNumber());
		setUnitName(prsdAdr.getSubUnitName());
		setCity(prsdAdr.getCity());
		setState(prsdAdr.getState());
		setZip(prsdAdr.getZip());
		setZip4(prsdAdr.getZip4());
		setCounty(prsdAdr.getCounty());
		setCountry(prsdAdr.getCountry());
		setMapLocationFields();
	}

	public boolean setMapLocationFields() {
		if (location == null) {
			if (GoogleMapApi.apiLimitReached()) {
				log.warn("Google Maps daily api-limit reached! Limit = " + GoogleMapApi.apiLimit());
			} else {
				MapLocation mapLoc = new MapLocation(this.toString());
				setMapLocCalls(getMapLocCalls() + 1);

				location = mapLoc.getLocation();
				
				state = mapLoc.getState();
				zip = mapLoc.getZip();
				zip4 = mapLoc.getZip4();
				county = mapLoc.getCounty();
				if (county != null) {
					county = county.toUpperCase();
				}

				// Overwrite and correct zip and city from the map
				String mapZip = mapLoc.getZip();
				if (mapZip != null) {
					if (zip != null && !zip.equals(mapZip) && zip == null) {
						log.warn("Corrected address:" + this + "  Cur Zip:" + zip + "  New Zip:" + mapZip);
						zip = mapZip;
					}
				}

				if (city != null) {
					String mapCity = mapLoc.getCity();
					if (mapCity != null) {
						mapCity = mapCity.toUpperCase();
						if (!city.equals(mapCity)) {
							log.warn("Corrected address:" + this + "  Cur City:" + city + " New City: " + mapCity);
							city = mapCity;
						}
					}
				}
			}
			if (location == null) {
				return false;
			} else {
				return true;
			}
		}
		return true;

	}

	public String getFullStreet() {
		StringBuffer sb = new StringBuffer();
		sb.append(nullToEmpty(streetNbr));
		sb.append(" ");
		sb.append(nullToEmpty(streetPreDir));
		sb.append(" ");
		sb.append(nullToEmpty(streetName));
		sb.append(" ");
		sb.append(nullToEmpty(streetPostDir));
		sb.append(" ");
		sb.append(nullToEmpty(streetType));
		sb.append(" ");
		sb.append(nullToEmpty(unitName));
		sb.append(nullToEmpty(unitNbr));
		String fs = sb.toString();
		fs = fs.trim();
		fs = fs.replaceAll(" +", " ");
		return fs;
	}

	public String toString() {
		String adrStr = getFullStreet();
		adrStr += ", " + nullToEmpty(city);
		adrStr += ", " + nullToEmpty(state);
		adrStr += " " + nullToEmpty(zip);
		adrStr += nullToEmpty(zip4);
		return adrStr;
	}

	@Override
	public void clean() {
	}

	@Override
	public boolean isValid() {
		return true;
	}

	public String getStreetNbr() {
		return streetNbr;
	}

	public void setStreetNbr(String streetNbr) {
		this.streetNbr = streetNbr;
	}

	public String getStreetPreDir() {
		return streetPreDir;
	}

	public void setStreetPreDir(String streetPreDir) {
		this.streetPreDir = emptyToNull(streetPreDir);
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getStreetType() {
		return streetType;
	}

	public void setStreetType(String streetType) {
		this.streetType = streetType;
	}

	public String getStreetPostDir() {
		return streetPostDir;
	}

	public void setStreetPostDir(String streetPostDir) {
		this.streetPostDir = emptyToNull(streetPostDir);
	}

	public String getUnitNbr() {
		return unitNbr;
	}

	public void setUnitNbr(String unitNbr) {
		this.unitNbr = emptyToNull(unitNbr);
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = emptyToNull(unitName);
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getZip4() {
		return zip4;
	}

	public void setZip4(String zip4) {
		this.zip4 = zip4;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Point getLocation() {
		return location;
	}

	public Point getLoc() {
		if (location == null) {
			setMapLocationFields();
			saveOrUpdate();
		}
		return location;
	}

	public Integer getMapLocCalls() {
		return mapLocCalls;
	}

	public void setMapLocCalls(Integer i) {
		mapLocCalls = i;
	}

	@Override
	public Example<Address> getRefExample() {
		Address adr = new Address();
		adr.mapLocCalls = null;
		adr.streetNbr = streetNbr;
		adr.streetName = streetName;
		adr.streetPreDir = streetPreDir;
		adr.streetPostDir = streetPostDir;
		adr.unitNbr = unitNbr;
		adr.unitName = unitName;
		adr.zip = zip;
		adr.city = city;

		System.out.println("Original Address=" + getDetails());
		System.out.println("Address Example =" + getDetails());

		ExampleMatcher matcher = ExampleMatcher.matching()
		// .withIgnorePaths("lastname")
		// .withIncludeNullValues()
		// .withStringMatcherEnding()
		;
		Example<Address> e = Example.of(adr, matcher);
		return e;
	}

	public String getDetails() {
		String str = "|";
		str += printStringField(streetNbr) + "|";
		str += printStringField(streetName) + "|";
		str += printStringField(streetPreDir) + "|";
		str += printStringField(streetPostDir) + "|";
		str += printStringField(unitNbr) + "|";
		str += printStringField(unitName) + "|";
		str += printStringField(zip) + "|";
		str += printStringField(city) + "|";
		return str;
	}

	private String printStringField(String str) {
		if (str == null)
			return "null";
		if (str == "")
			return "empty";
		return str;
	}

	public static AddressRepository getRepo() {
		if (repo == null) {
			repo = (AddressRepository) SpringApplicationContext.getBean("addressRepository");
		}
		return repo;
	}

	@Override
	public void save() {
		getRepo().save(this);

	}

	@Override
	public void saveOrUpdate() {
		getRepo().saveOrUpdate(this);
	}

	String emptyToNull(String s) {
		if (StringUtils.isEmpty(s)) {
			s = null;
		}
		return s;
	}

	String nullToEmpty(String s) {
		if (s == null) {
			s = "";
		}
		return s;
	}

	public LatLng getLatLng() {
		Point loc = getLoc();
		if (loc != null) {
			return new LatLng(getLoc().getY(), getLoc().getX());
		} else {
			return null;
		}
	}
}
