package com.afn.realstat;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.geo.Point;

import com.afn.realstat.framework.SpringApplicationContext;

@Entity
// TODO
@Table(name = "tourlist_entry", indexes = {
		@Index(name = "idx_tourDate", columnList = "tourDate")})



public class TourListEntry extends AbstractEntity {
	
	public static final Logger log = LoggerFactory.getLogger("app");
	public static final Class<TourListEntry> classType = TourListEntry.class;
	private static TourListRepository repo;

	@Basic(optional = false)
	private Date tourDate;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Address propertyAdr;
	
	private String city;
	private String zip;
	private String street;
	private String crossStreet;
	private String bedBath;
	private String price;
	private String time;
	private String description;
	private String agent;
	private String office;
	private String phone;
	private String mlsNo;
	
	private int sequence;
	
	public TourListEntry() {
	}

	public TourListEntry(Date tourDate, String[] entryElements) {
		this.tourDate = tourDate;		
	}

	public TourListEntry(Date tourDate, Address adr) {
		this.tourDate = tourDate;
		this.propertyAdr = adr;
	}

	@Override
	public Example<TourListEntry> getRefExample() {
		TourListEntry sample = new TourListEntry();
		sample.tourDate = tourDate;
		sample.propertyAdr = propertyAdr;
		Example<TourListEntry> e = Example.of(sample);
		return e;
	}

	@Override
	public boolean isValid() {
		return propertyAdr != null;
	}

	@Override
	public void clean() {
	}

	public String htmlString() {
		String html = "";
		html += "<p style=\"text-align:left;\">";
		html += street + ", " + city;
		html += "<span style=\"float:right;\">";
		html += "<bold>";
		html += price; 
		html += "</bold>";
		html += "</span>";
		html += "<br>";
		html += description;
		html += "<br>";
		html += agent;
		html += "<span style=\"float:right;\">";
		html += "MLS# ";
		html += mlsNo;
		html += "</span>";
		html += "</p>";
		return html;
	}
	
	// todo remove
	public String htmlString02() {
		String html = "";
		html += "<p>";
		html += street + ", " + city + ", ";
		html += "<bold>";
		html += price; 
		html += "</bold>";
		html += "</p>";
		html += "<p>";
		html += description;
		html += "</p>";
		html += "<p>";
		html += agent;
		html += "</p>";
		return html;
		
	}
	public Date getTourDate() {
		return tourDate;
	}

	public void setTourDate(Date tourDate) {
		this.tourDate = tourDate;
	}

	public Address getPropertyAdr() {
		return propertyAdr;
	}

	public void setPropertyAdr(Address propertyAdr) {
		this.propertyAdr = propertyAdr;
	}
	
	public Point getLocation() {
		return propertyAdr.getLoc();
	}

	public String getCity() {
		return nullToEmpty(city);
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZip() {
		return nullToEmpty(zip);
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getStreet() {
		return nullToEmpty(street);
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCrossStreet() {
		return nullToEmpty(crossStreet);
	}

	public void setCrossStreet(String crossStreet) {
		this.crossStreet = crossStreet;
	}

	public String getBedBath() {
		return nullToEmpty(bedBath);
	}

	public void setBedBath(String bedBath) {
		this.bedBath = bedBath;
	}

	public String getPrice() {
		return nullToEmpty(price);
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getDescription() {
		return nullToEmpty(description);
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAgent() {
		return nullToEmpty(agent);
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getOffice() {
		return nullToEmpty(office);
	}

	public void setOffice(String office) {
		this.office = office;
	}

	public String getPhone() {
		return nullToEmpty(phone);
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMlsNo() {
		return nullToEmpty(mlsNo);
	}

	public void setMlsNo(String mlsNo) {
		this.mlsNo = mlsNo;
	}
	
	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	
	public String getStringSeq() {
		if (sequence == 0) {
			return "";
		} else {
			return Integer.toString(sequence);
		}
	}

	private String nullToEmpty(String s) {
		if (s == null) {
			return "";
		} else {
			return s;
		}
	}
	
	@Override
	public String toString() {
		String str = "TourListEntry: " + tourDate + " - " + propertyAdr.toString();
		return str;
	}
	
	public static TourListRepository getRepo() {
		if (repo == null) {
			repo =  (TourListRepository) SpringApplicationContext.getBean("tourListRepository");
		}
		return repo;
	}

	@Override
	public void save() {
		getRepo().save(this);
		
	}

	@Override
	public void saveOrUpdate() {
		getRepo().save(this);
	}

	public String mapMarkerCaption() {
		
		int width = 50;
		
		String desc = WordUtils.wrap(getDescription(), width, "\n", false);
		String s = "";
		String sc = getStreet() + " @ " + getCrossStreet();
		String cz = getCity() + ", " + getZip() + "\n"; 
		s += sc + cz;
		s += getTime() + "\n";
		s += desc + "\n";
		s += getAgent() + "  " + getOffice() +"\n";
		s += "MLS# " + getMlsNo();
		return s;
		
	}
	

}
