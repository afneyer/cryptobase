package com.afn.realstat;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.geo.Point;

import com.afn.realstat.framework.SpringApplicationContext;

public class MyTourStop implements Comparable<MyTourStop> {

	public static final Logger log = LoggerFactory.getLogger("app");
	public static final Class<MyTourStop> classType = MyTourStop.class;

	public static TourListRepository repo;

	private TourListEntry tle;
	private MyTour tour;
	private int sequence;

	public MyTourStop() {
	}

	public MyTourStop(MyTour tour, TourListEntry tle) {
		this.tle = tle;
		this.tour = tour;
	}

	public String htmlString() {
		String html = "";
		html += "<style>.ellipsis { text-overflow: ellipsis; }</style>";
		html += "<p style=\"text-align:left;\">";
		html += getStreet() + ", " + getCity();
		html += "<span style=\"float:right;\">";
		html += "<bold>";
		html += getPrice();
		html += "</bold>";
		html += "</span>";
		html += "<br>";
		html += getDescription();
		html += "<br>";
		html += getAgent();
		html += "<span style=\"float:right;\">";
		html += "MLS# ";
		html += getMlsNo();
		html += "</span>";
		html += "</p>";
		return html;
	}

	// todo remove
	public String htmlString02() {
		String html = "";
		html += "<p>";
		html += getStreet() + ", " + getCity() + ", ";
		html += "<bold>";
		html += getPrice();
		html += "</bold>";
		html += "</p>";
		html += "<p>";
		html += getDescription();
		html += "</p>";
		html += "<p>";
		html += getAgent();
		html += "</p>";
		return html;

	}

	public Date getTourDate() {
		return tle.getTourDate();
	}

	public void setTourDate(Date tourDate) {
		this.tle.setTourDate(tourDate);
	}

	public MyTour getTour() {
		return tour;
	}

	public Address getPropertyAdr() {
		return tle.getPropertyAdr();
	}

	public Point getLocation() {
		return getPropertyAdr().getLoc();
	}

	public String getCity() {
		return tle.getCity();
	}

	public String getZip() {
		return tle.getZip();
	}

	public String getStreet() {
		return tle.getStreet();
	}

	public String getCrossStreet() {
		return tle.getCrossStreet();
	}

	public String getBedBath() {
		return tle.getBedBath();
	}

	public String getPrice() {
		return tle.getPrice();
	}

	public String getTime() {
		return tle.getTime();
	}

	public String getDescription() {
		return tle.getDescription();
	}

	public String getAgent() {
		return tle.getAgent();
	}

	public String getOffice() {
		return tle.getOffice();
	}

	public String getPhone() {
		return tle.getPhone();
	}

	public String getMlsNo() {
		return tle.getMlsNo();
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
		// update the marker to include the sequence number
	}

	public String getStringSeq() {
		if (sequence == 0) {
			return "";
		} else {
			return Integer.toString(sequence);
		}
	}

	@Override
	public String toString() {
		String str = "MyTourListStop: " + getTourDate() + " - " + getPropertyAdr().toString();
		return str;
	}

	public TourListRepository getRepo() {
		if (repo == null) {
			repo = (TourListRepository) SpringApplicationContext.getBean("tourListRepository");
		}
		return repo;
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
		s += getAgent() + "  " + getOffice() + "\n";
		s += "MLS# " + getMlsNo();
		return s;

	}

	/*
	 * Tour stops are equal if the belong to the same tour and have the same
	 * sequence
	 */
	@Override
	public boolean equals(Object o) {

		// If the object is compared with itself then return true
		if (o == this) {
			return true;
		}

		/*
		 * Check if o is an instance of MyTourStop or not
		 * "null instanceof [type]" also returns false
		 */
		if (!(o instanceof MyTourStop)) {
			return false;
		}

		// Cast o to MyTour so that we can compare data members
		MyTourStop c = (MyTourStop) o;

		// Compare the data members and return accordingly
		boolean equals = this.tour.equals(c.tour) && this.tle.equals(c.tle);
		
		return equals;
	}

	@Override
	public int compareTo(MyTourStop mts) {
		return (this.sequence - mts.sequence);
	}

	public boolean isSelected() {
		return getTour().getSelected().contains(this);
	}

	public void deselect() {
		List<MyTourStop> selected = getTour().getSelected();
		if (selected.contains(this)) {
			selected.remove(this);
			sequence = 0;
		}
	}
	
	public void select() {
		List<MyTourStop> selected = getTour().getSelected();
		if (!selected.contains(this)) {
			selected.add(this);
		}
	}

}
