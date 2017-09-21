package com.afn.realstat;

import org.springframework.beans.factory.annotation.Autowired;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ParseDate2;
import org.supercsv.cellprocessor.ParseDouble2;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.StrReplace;
import org.supercsv.cellprocessor.ift.CellProcessor;

import com.vaadin.spring.annotation.SpringComponent;

/**
 * @author Andreas
 * 
 *         TODO: importer misses over 400 records for no known reason
 *
 */

// TODO: importer misses over 400 records for no known reason
@SpringComponent
public class MlsImporter extends AbstractImporter<PropertyTransaction> {

	/**
	 * 
	 */
	@Autowired
	PropertyTransactionRepository ptRepo;

	@Autowired
	PropertyTransactionManager pm;

	// private static final Logger log = LoggerFactory.getLogger("import");

	/**
	 * 
	 */
	public MlsImporter() {
	}

	/**
	 * @return
	 */
	protected CellProcessor[] getProcessors() {

		final CellProcessor[] processors = new CellProcessor[] {

				new ParseInt(), // MLS No
				new Optional(), // Status
				new Optional(), // DOM
				new Optional(), // Address
				new Optional(), // Unit
				new Optional(), // City
				new Optional(), // Area
				new Optional(new ParseDouble2()), // LP
				new Optional(new ParseDouble2()), // SP
				new Optional(), // Building Type
				new Optional(new ParseDouble2()), // SqFt
				new Optional(new ParseInt()), // Bedrooms
				new Optional(new ParseInt()), // Baths
				new Optional(new ParseInt()), // Baths Partial
				new Optional(), // Gar
				new Optional(new ParseInt()), // GarSp
				new Optional(new ParseDate2("yyyy")), // YrBlt
				new Optional(new ParseDouble2()), // Acres
				new Optional(new ParseDouble2()), // Lot SqFt
				new Optional(new ParseDouble2()), // HOA Fee
				new Optional(), // HOA Fees Freq
				new Optional(new ParseDate("MM/dd/yyyy")), // Close Date
				new Optional(new ParseInt()), // Age
				new Optional(), // APN
				new Optional(), // Census Tract
				new Optional(), // City Transfer Tax
				new Optional(), // Class
				new Optional(), // Co List Agent BRE Num.
				new Optional(), // Co Sell Agent BRE Num.
				new Optional(), // Comp to Selling Office
				new Optional(), // Comp Type
				new Optional(), // County
				new Optional(new ParseInt()), // Cumulative DOMLS
				new Optional(new ParseInt()), // Days On MLS
				new Optional(new ParseDate("MM/dd/yyyy")), // General Date
				new Optional(), // Dist/Neighborhood/Subdiv
				new Optional(), // Dual Variable
				new Optional(new ParseInt()), // Fireplaces
				new Optional(), // Floor Unit is on
				new Optional(), // Foreclosure Status
				new Optional(), // HOA
				new Optional(), // How Sold
				new Optional(new ParseDate("MM/dd/yyyy")), // Input Date
				new Optional(new ParseDouble2()), // List $/SqFt
				new Optional(), // List Agent BRE Num.
				new Optional(new ParseDate("MM/dd/yyyy")), // List Date
				new Optional(), // Listing Broker BRE Number
				new Optional(), // Listing Type
				new Optional(new ParseInt()), // Number of offers
				new Optional(new ParseDate("MM/dd/yyyy")), // Off Market Date
				new Optional(new ParseDouble2()), // Original Price
				new Optional(new ParseDate("MM/dd/yyyy")), // Pending Date
				new Optional(), // Pending Litigation
				new Optional(new ParseDouble2()), // Occ%
				new Optional(), // Point of Sale Ordinance
				new Optional(), // Pool (Y/N)
				new Optional(new ParseDouble2()), // Previous Price
				new Optional(new ParseDouble2()), // Price
				new Optional(new ParseDate("MM/dd/yyyy")), // Price Date
				new Optional(new ParseDouble2()), // Price/SqFt
				new Optional(new ParseInt()), // Rooms Total
				new Optional(new ParseDouble2()), // Sale $/SqFt
				new Optional(), // Sale COOP
				new Optional(new ParseDouble2()), // Sale/Last List $
				new Optional(new ParseDouble2()), // Sale/Original $
				new Optional(), // Sale/Rent
				new Optional(), // Selling Broker BRE Number
				new Optional(), // Sold Agent BRE Num.
				new Optional(), // Tax ID
				new Optional(new ParseDouble2()), // TIC % Owner Offered
				new Optional(new StrReplace(".", "0", new ParseInt())), // Units
																		// in
																		// Complex
				new Optional(new ParseDate("MM/dd/yyyy")), // Update Date
				new Optional(), // Source
				new Optional(), // Special Information
				new Optional(), // State
				new Optional(new ParseDate("MM/dd/yyyy")), // Status Date
				new Optional(), // Stories
				new Optional(), // Zip
				new Optional(), // Zoning
				new Optional(), // Co-List Agent - Agent Name
				new Optional(), // Co-List Agent - License ID
				new Optional(), // Co-Sell Agent - Agent Name
				new Optional(), // Co-Sell Agent - License ID
				new Optional(), // Listing Agent - Agent Name
				new Optional(), // Listing Agent - License ID
				new Optional(), // Selling Agent 1 - Agent Name
				new Optional(), // Selling Agent 1 - License ID
		};

		return processors;
	}

	/**
	 * @param header
	 * @return
	 */
	protected String[] mapHeader(String[] header) {

		// translate each element of the header to the corresponding bean field
		// name
		for (int i = 0; i < header.length; i++) {
			// special translations
			switch (header[i]) {
			case "MLS No":
				header[i] = "mlsNo";
				break;
			case "LP":
				header[i] = "listPrice";
				break;
			case "SP":
				header[i] = "salesPrice";
				break;
			case "Co-List Agent - Agent Name":
				header[i] = "coListAgentName";
				break;
			case "Co-Sell Agent - Agent Name":
				header[i] = "coSellAgentName";
				break;
			case "Listing Agent - Agent Name":
				header[i] = "listingAgentName";
				break;
			case "Selling Agent 1 - Agent Name":
				header[i] = "sellingAgent1Name";
				break;
			case "Class":
				header[i] = "propClass";
				break;

			default:
				header[i] = translateCsvFileFieldDefault(header[i]);
			}
		}
		return header;
	}

	@Override
	protected void preProcessEntity(PropertyTransaction pt) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void saveOrUpdateEntity(PropertyTransaction pt) {
		ptRepo.saveOrUpdate(pt);
	}

	@Override
	protected void postProcessEntity(PropertyTransaction pt) {
		// pm.linkPropertyTransactionToAgents(pt);
		// pm.linkPropertyTransactionToRealProperty(pt);
	}

	@Override
	protected PropertyTransaction getNewEntity() {
		return new PropertyTransaction();
	}

}
