package com.afn.realstat;

import static org.junit.Assert.assertNotNull;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import javatools.parsers.Name.PersonName;

public class PersName {

	private static final Logger log = (Logger) LoggerFactory.getLogger("app");

	private String rawName;
	private String normalizedName;
	private String firstName;
	private String middleName;
	private String middleInitial;
	private String lastName;

	public PersName() {
	};

	// The raw name is cleaned up by removing multiple blanks, trimming front
	// and back and capitalizing it.
	public PersName(String rawName) {
		this.rawName = rawName;
		normalizedName = rawName;
		if (normalizedName != null) {
			normalizedName = normalizedName.replaceAll("\\.", ". ");
			normalizedName = StringUtils.trim(normalizedName).replaceAll(" +", " ");
			normalizedName = WordUtils.capitalizeFully((normalizedName).toLowerCase(), ' ', '-', '\'');
			normalizedName = normalizeName(normalizedName);
			PersonName pn = new PersonName(normalizedName);
			lastName = pn.familyName();
			extractFirstMiddle(pn.givenNames());
		}

	}

	private String normalizeName(String raw) {
		
		assertNotNull(raw);
		String[] nameParts = raw.split(",");

		switch (nameParts.length) {
		case 1:
			return raw;
		case 2:
			String lastName = StringUtils.trim(nameParts[0]);
			String firstMiddle = StringUtils.trim(nameParts[1]);
			firstMiddle = firstMiddle + " " + lastName;
			return firstMiddle;
		default:
			log.warn("Name contains multiple commas, may not be able to parse it!" + rawName);
			return raw;
		}
	}

	private void extractFirstMiddle(String firstMiddle) {

		// don't do anything if firstMiddle is null
		if (firstMiddle == null) {
			log.warn("Cannot parse first or middle name of: " + rawName);
			return;
		}

		// split by blank
		firstMiddle = firstMiddle.replaceAll("\\.", " ");
		firstMiddle = firstMiddle.replaceAll("  ", " ");
		firstMiddle = StringUtils.trim(firstMiddle);
		String[] nameParts2 = firstMiddle.split(" ");

		switch (nameParts2.length) {
		case 1:
			firstName = StringUtils.trim(nameParts2[0]);
			middleName = null;
			middleInitial = null;
			break;
		// for 3 given names ignore the last element for now TODO: review
		case 2:
		case 3:
			firstName = StringUtils.trim(nameParts2[0]);
			middleName = StringUtils.trim(nameParts2[1]);
			cleanMiddleName();
			extractMiddleInitial();
			break;
		default:
			log.warn("Cannot parse first or middle name of: " + rawName);
			break;
		}
	}

	// this is not needed anymore TODO
	@SuppressWarnings("unused")
	private void extractFirstMiddleLast() {

		// split by comma
		String[] nameParts = rawName.split(",");
		// TODO process non-comma formats
		if (nameParts.length == 2) {

			lastName = WordUtils.capitalizeFully(StringUtils.trim(nameParts[0]).toLowerCase());

			// split by blank
			String firstMiddle = nameParts[1];
			firstMiddle = firstMiddle.replaceAll("  ", " ");
			firstMiddle = StringUtils.trim(firstMiddle);
			String[] nameParts2 = firstMiddle.split(" ");

			switch (nameParts2.length) {
			case 1:
				firstName = StringUtils.trim(nameParts2[0]);
				middleName = null;
				middleInitial = null;
				break;
			case 2:
				firstName = StringUtils.trim(nameParts2[0]);
				middleName = StringUtils.trim(nameParts2[1]);
				cleanMiddleName();
				extractMiddleInitial();
				break;
			default:
				log.warn("Unparseable Agent Name " + rawName);
				break;
			}
		}

	}

	private void cleanMiddleName() {
		middleName = middleName.replaceAll("[.]", "");

	}

	private void extractMiddleInitial() {

		if (middleName == null || middleName.isEmpty()) {
			middleInitial = null;
			return;
		}
		if (middleName.length() >= 1) {
			middleInitial = middleName.substring(0, 1);
			return;
		}
	}

	public Boolean isValidName() {
		return (firstName != null && lastName != null);
	}

	public String getRawName() {
		return rawName;
	}

	public void setRawName(String rawName) {
		this.rawName = rawName;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getNormalizedName() {
		return normalizedName;
	}

	public void setNormalizedName(String normalizedName) {
		this.normalizedName = normalizedName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getMiddleInitial() {
		return middleInitial;
	}

	public void setMiddleInitial(String middleInitial) {
		this.middleInitial = middleInitial;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}
