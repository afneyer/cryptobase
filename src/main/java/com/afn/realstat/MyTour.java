package com.afn.realstat;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.afn.realstat.ui.ShowPdfButton.PdfFileGetter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.AreaBreakType;
import com.itextpdf.layout.property.Property;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

/**
 * MyTour is a tour created for a specific user. It consists of a start address, end address
 * and a set of tour stops for a particular tour day.
 * 
 * Start and end addresses are initialized to the default for the user. Later the user can
 * adjust them by moving them on the map.
 * 
 * MyTour also provides the functionality to show the tour as a PDF-File for printing.
 * 
 * @author Andreas
 *
 */
public class MyTour implements PdfFileGetter {

	private Date tourDate;
	
	// all stops of the tour
	private ArrayList<MyTourStop> tourList;
	
	// user selected stops of the tour in the order selected
	// sorting of this list must never change
	private ArrayList<MyTourStop> selectedList;
	
	private Address startAddress;
	private Address endAddress;
	
	private String userName = "Kathleen Callahan";

	/**
	 * Sets up a tour for a specific date and creates all the stops included
	 * in the tour for a specific day. Each tour stop is linked to a TourListEntry 
	 * which is a database element that describes to the tour.
	 * 
	 * @param tourDate
	 * @param inStartAddress
	 * @param inEndAddress
	 */
	public MyTour(Date tourDate, Address inStartAddress, Address inEndAddress) {
		
		// get the default start and end location from the user
		Address defaultStartAddress = new Address("4395 Piedmont Ave", "Oakland", "94611");
		Address defaultEndAddress = new Address("342 Highland Ave", "Piedmont", "94611");
		
		this.tourDate = tourDate;
	
		// initialize start and end to the user default if they are not provided
		if (inStartAddress != null) {
			startAddress = inStartAddress;
		} else {
			startAddress = defaultStartAddress;
		}
		if (inEndAddress != null) {
			endAddress = inEndAddress;
		} else {
			endAddress = defaultEndAddress;
		}
		
		// set up all tour list stop based on the generic tour list in the data base
		TourListRepository tlRepo = TourListEntry.getRepo();
		
		List<TourListEntry> tleList = tlRepo.findByTourDate(tourDate);
		tourList = new ArrayList<MyTourStop>();
		
		for (TourListEntry tle : tleList) {
			MyTourStop myStop = new MyTourStop(this, tle);
			tourList.add(myStop);
		}

		selectedList = new ArrayList<MyTourStop>();
	}
	
	public MyTour(Date tourDate) {
		this(tourDate, null, null);
	}
	

	public Date getTourDate() {
		return tourDate;
	}

	public boolean contains(MyTourStop tourListEntry) {
		return tourList.contains(tourListEntry);
	}

	public boolean isSelected(MyTourStop tourListEntry) {
		return selectedList.contains(tourListEntry);
	}

	public List<MyTourStop> getTourList() {
		return tourList;
	}

	public List<Address> getSelectedAddresses() {
		List<Address> list = new ArrayList<Address>();

		for (MyTourStop mts : selectedList) {
			list.add(mts.getPropertyAdr());
		}
		return list;

	}

	public List<MyTourStop> getSelected() {
		return selectedList;
	}
	
	public List<MyTourStop> getRouted() {
		ArrayList<MyTourStop> routedList = new ArrayList<MyTourStop>(selectedList);
 		Collections.sort(routedList);
		return routedList;
	}

	public void setSequence(int[] sequence) {
		for (int i = 0; i < sequence.length; i++) {
			MyTourStop mts = selectedList.get(i);
			mts.setSequence(sequence[i] + 1);
		}
	}

	public void clearSequence() {
		for (MyTourStop mts : selectedList) {
			mts.setSequence(0);
		}
	}

	/*
	 * Uses iText to create a PDF-file
	 */
	public File getPdfFile() {

		File file = null;
		try {

			// Save the results and ensure that the document is properly closed:
			String filePath = AppFiles.getTempDir();
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd-hh-mm-ss.sss");
			format.format(new Date());
			String fileName = format.format(new Date()) + ".pdf";
			String dest = filePath + fileName;
			file = new File(filePath + fileName);

			// Initialize PDF writer
			PdfWriter writer = new PdfWriter(dest);

			// Initialize PDF document
			PdfDocument pdf = new PdfDocument(writer);

			// Initialize document
			Document document = new Document(pdf);

			addPdfTourList(document);

			document.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return file;
	}

	private void addPdfTourList(Document doc) {
		addPageHeader(doc);
		addTourList(doc);
	}

	private void addPageHeader(Document doc) {

		SimpleDateFormat format = new SimpleDateFormat("EEEE,  MMMM dd, yyyy");

		String dateStr = format.format(getTourDate());
		String title = "Tour for " + userName + " on " + dateStr;
		doc.add(new Paragraph(title).setBold().setFontSize(16));
		addHorizontalLine(doc);

	}

	private void addTourList(Document doc) {

		// review this code for breaks
		int pageSize = 12;
		
		addStartEndAddress(startAddress, doc, "Start");
		List<MyTourStop> routedList = getRouted();
		for (int i = 0; i < routedList.size(); i++) {

			MyTourStop mts = routedList.get(i);
			addMyTourStop(mts, doc);

			// if necessary create new page
			if ((i + 2) % pageSize == 0) {
				doc.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
				addHorizontalLine(doc);
			}

		}
		addStartEndAddress(endAddress, doc, "End");

	}

	private void addHorizontalLine(Document doc) {
		Paragraph p = new Paragraph("").setBorderTop(new SolidBorder(1)).setFontSize(10);
		doc.add(p);
	}

	private void addStartEndAddress( Address adr, Document doc, String startEnd ) {
		// add table for holding a tour list entry
		UnitValue[] unitArray = { new UnitValue(UnitValue.PERCENT, 12f), new UnitValue(UnitValue.PERCENT, 25f),
				new UnitValue(UnitValue.PERCENT, 25f), new UnitValue(UnitValue.PERCENT, 18f),
				new UnitValue(UnitValue.PERCENT, 20f) };

		Table table = new Table(unitArray);
		table.setMargin(0);
		table.setPadding(-10);
		table.setProperty(Property.BORDER, Border.NO_BORDER);

		Cell cell = null;
		// add the first row of cells

		// Cell 1
		cell = new Cell().setBold().setFontSize(11).setBorder(Border.NO_BORDER).setHeight(15);		
		cell.add(startEnd);
		table.addCell(cell);

		// Cell 2-3
		cell = new Cell(1, 2).setBold().setFontSize(11).setBorder(Border.NO_BORDER);
		cell.add(adr.getFullStreet());
		table.addCell(cell);

		// Cell 4
		cell = new Cell().setBold().setFontSize(11).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER)
				.setHeight(15);
		cell.add("");
		table.addCell(cell);

		// Cell 5
		cell = new Cell().setBold().setFontSize(11).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER)
				.setHeight(15);
		cell.add("");
		table.addCell(cell);

		table.startNewRow();

		// add second row of cells
		table.startNewRow();

		cell = new Cell().setBold().setFontSize(9).setBorder(Border.NO_BORDER).setHeight(13);
		cell.add(adr.getCity());
		table.addCell(cell);

		cell = new Cell(1, 4).setFontSize(9).setBorder(Border.NO_BORDER).setHeight(13);
		cell.add("");
		table.addCell(cell);

		// add third row of cells
		table.startNewRow();

		cell = new Cell().setFontSize(10).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1)).setHeight(14);
		cell.add(adr.getZip());
		table.addCell(cell);

		cell = new Cell().setFontSize(10).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1)).setHeight(14);
		cell.add("");
		table.addCell(cell);

		cell = new Cell().setFontSize(10).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1)).setHeight(14);
		cell.add("");
		table.addCell(cell);

		cell = new Cell().setFontSize(10).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1)).setHeight(14);
		cell.add("");
		table.addCell(cell);

		cell = new Cell().setFontSize(10).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1))
				.setTextAlignment(TextAlignment.RIGHT).setHeight(14);
		cell.add("");
		table.addCell(cell);

		doc.add(table);
	}
	
	private void addMyTourStop(MyTourStop mts, Document doc) {

		// add table for holding a tour list entry
		UnitValue[] unitArray = { new UnitValue(UnitValue.PERCENT, 12f), new UnitValue(UnitValue.PERCENT, 25f),
				new UnitValue(UnitValue.PERCENT, 25f), new UnitValue(UnitValue.PERCENT, 18f),
				new UnitValue(UnitValue.PERCENT, 20f) };

		Table table = new Table(unitArray);
		table.setMargin(0);
		table.setPadding(-10);
		table.setProperty(Property.BORDER, Border.NO_BORDER);

		Cell cell = null;
		// add the first row of cells

		// Cell 1
		cell = new Cell().setBold().setFontSize(11).setBorder(Border.NO_BORDER).setHeight(15);
		cell.add(new Integer(mts.getSequence()).toString());
		table.addCell(cell);

		// Cell 2-3
		cell = new Cell(1, 2).setBold().setFontSize(11).setBorder(Border.NO_BORDER);
		cell.add(mts.getStreet() + " @ " + mts.getCrossStreet());
		table.addCell(cell);

		// Cell 4
		cell = new Cell().setBold().setFontSize(11).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER)
				.setHeight(15);
		cell.add(mts.getBedBath());
		table.addCell(cell);

		// Cell 5
		cell = new Cell().setBold().setFontSize(11).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER)
				.setHeight(15);
		cell.add(mts.getPrice());
		table.addCell(cell);

		table.startNewRow();

		// add second row of cells
		table.startNewRow();

		cell = new Cell().setBold().setFontSize(9).setBorder(Border.NO_BORDER).setHeight(13);
		cell.add(mts.getCity());
		table.addCell(cell);

		cell = new Cell(1, 4).setFontSize(9).setBorder(Border.NO_BORDER).setHeight(13);
		cell.add(mts.getDescription());
		table.addCell(cell);

		// add third row of cells
		table.startNewRow();

		cell = new Cell().setFontSize(10).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1)).setHeight(14);
		cell.add(mts.getZip());
		table.addCell(cell);

		cell = new Cell().setFontSize(10).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1)).setHeight(14);
		cell.add(mts.getAgent());
		table.addCell(cell);

		cell = new Cell().setFontSize(10).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1)).setHeight(14);
		cell.add(mts.getOffice());
		table.addCell(cell);

		cell = new Cell().setFontSize(10).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1)).setHeight(14);
		cell.add(mts.getPhone());
		table.addCell(cell);

		cell = new Cell().setFontSize(10).setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(1))
				.setTextAlignment(TextAlignment.RIGHT).setHeight(14);
		cell.add("MLS# " + mts.getMlsNo());
		table.addCell(cell);

		doc.add(table);
	}

	public Address getStartAddress() {
		return startAddress;
	}
	
	public Address getEndAddress() {
		return endAddress;
	}

	public void setStartAddress(Address adr) {
		startAddress  = adr;	
	}

	public void setEndAddress(Address adr) {
		endAddress = adr;
	}

}
