package com.afn.realstat;

import org.springframework.beans.factory.annotation.Autowired;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.ParseDouble2;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;

import com.vaadin.spring.annotation.SpringComponent;

/**
 * @author Andreas
 *
 */
@SpringComponent
public class CrsImporter extends AbstractImporter<RealProperty> {

	/**
	 * 
	 */
	@Autowired
	RealPropertyRepository repository;
	// private static final Logger log =
	// LoggerFactory.getLogger(Application.class);

	public CrsImporter() {
	}

	/**
	 * @return
	 */
	protected CellProcessor[] getProcessors() {

		final CellProcessor[] processors = new CellProcessor[] {

				new Optional(), // Owner 1
				new Optional(), // Title
				new Optional(), // First Name
				new Optional(), // Middle
				new Optional(), // Last Name
				new Optional(), // Owner 2
				new Optional(), // Title2
				new Optional(), // First Name2
				new Optional(), // Middle2
				new Optional(), // Last Name2
				new Optional(), // Owner Address
				new Optional(), // Owner City
				new Optional(), // Owner State
				new Optional(), // Owner Zip
				new Optional(), // APN
				new Optional(), // Improvement Type
				new Optional(), // Land Use
				new NotNull(), // Property Address
				new NotNull(), // Property City
				new NotNull(), // Property State
				new NotNull(), // Property Zip
				new Optional(new ParseDouble()), // Total Square Footage
				new Optional(new ParseDate("MM/dd/yyyy")), // Last Sale Date
				new Optional(new ParseDouble()), // Last Sale Amount
				new Optional(), // Warranty Deed Book
				new Optional(), // Warranty Deed Page
				new Optional(), // Warranty Deed Document Number
				new Optional(new ParseDouble()), // Land Value
				new Optional(new ParseDouble()), // Improvement Value
				new Optional(new ParseDouble()), // Total Value
				new Optional(new ParseDate("yyyy")), // Year Built
				new Optional(), // Dimensions
				new Optional(), // Subdivision
				new Optional(new ParseDouble()), // Acreage
				new Optional(), // Block
				new Optional(), // Lot
				new Optional(new ParseDouble2()), // Lot Sq. Feet
				new Optional(), // Lot Dimensions
				new Optional(), // Census Tract
		};

		return processors;
	}

	/**
	 * Maps the header of the csv-file (i.e. list of fields) into the field
	 * names of the entity The default translator can be used if the entity are
	 * similar to the fields in the csv-file
	 * 
	 * @param header
	 * @return
	 */
	protected String[] mapHeader(String[] header) {

		// translate each element of the header to the corresponding bean field
		// name
		for (int i = 0; i < header.length; i++) {

			// special translations
			switch (header[i]) {
			case "Owner 1":
				header[i] = "owner1";
				break;
			default:
				header[i] = translateCsvFileFieldDefault(header[i]);
			}
		}
		return header;
	}

	@Override
	protected void preProcessEntity(RealProperty entity) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void saveOrUpdateEntity(RealProperty rp) {
		repository.saveOrUpdate(rp);

	}

	@Override
	protected void postProcessEntity(RealProperty entity) {
		// no post-processing needed
	}

	@Override
	protected RealProperty getNewEntity() {
		return new RealProperty();
	}
}
