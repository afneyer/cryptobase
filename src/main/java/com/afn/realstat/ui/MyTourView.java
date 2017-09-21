package com.afn.realstat.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.afn.realstat.Address;
import com.afn.realstat.MyTour;
import com.afn.realstat.MyTourStop;
import com.afn.realstat.TourListEntry;
import com.afn.realstat.TourListRepository;
import com.afn.realstat.util.GeoLocation;
import com.afn.realstat.util.MapDirection;
import com.google.maps.model.EncodedPolyline;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapPolyline;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.components.grid.MultiSelectionModel;
import com.vaadin.ui.renderers.HtmlRenderer;

public class MyTourView {

	private MyTour myTour;
	private TourListView tourListView = null;
	private TourMap tourMap;
	private GoogleMapPolyline tourPolyLine = null;

	private ShowPdfButton printTour;
	private Button routeTour;
	private TextArea startTextArea;
	private TextArea endTextArea;

	public MyTourView() {

		// create the tour
		myTour = new MyTour(new Date());
		
		// create the map view and add start and end marker
		tourMap = createMapView(this);

		tourListView = createListView();

	}

	public TourListView createListView() {

		tourListView = new TourListView(this);
		tourListView.addStyleName("h3rows");
		Grid.Column<MyTourStop, String> colPropertyInfo = tourListView.addColumn(MyTourStop::htmlString,
				new HtmlRenderer());
		colPropertyInfo.setWidth(350.0);
		colPropertyInfo.setCaption("Description");
		tourListView.addColumn(MyTourStop::getStringSeq).setCaption("Seq");

		tourListView.setSelectionMode(SelectionMode.MULTI);

		MultiSelectionModel<MyTourStop> selectionModel = (MultiSelectionModel<MyTourStop>) tourListView
				.getSelectionModel();
		selectionModel.addMultiSelectionListener(event -> {
			Set<MyTourStop> added = event.getAddedSelection();
			for (MyTourStop mts : added) {
				mts.select();
				getListView().select(mts);
			}

			Set<MyTourStop> removed = event.getRemovedSelection();
			for (MyTourStop mts : removed) {
				mts.deselect();
				getListView().deselect(mts);
			}
			getMapView().refresh();
		});

		tourListView.addItemClickListener(event -> Notification.show("Value: " + event.getItem()));
		return tourListView;
	}

	public TourListView getListView() {
		return tourListView;
	}

	public TourMap createMapView(MyTourView myTourView) {
		tourMap = new TourMap(myTourView);
		tourMap.setCenter(GeoLocation.convertToLatLon(myTour.getStartAddress().getLoc()));
		tourMap.setZoom(13);
		return tourMap;
	}

	public TourMap getMapView() {
		return tourMap;
	}



	public CheckBox getShowSelectedCheckBox() {
		String label = "Show Selected Only";
		CheckBox checkBox = new CheckBox(label);

		checkBox.addValueChangeListener(event -> {
			Boolean checked = event.getValue();

			if (myTour != null) {
				if (checked) {
					showSelected();
				} else {
					showAll();

				}
			}
		});
		return checkBox;
	}

	public void showAll() {

		// Save of the selection
		ArrayList<MyTourStop> selected = new ArrayList<MyTourStop>(myTour.getSelected());
		
		// Change out the data provider, this will clear the selection
		ListDataProvider<MyTourStop> dataProvider = DataProvider.ofCollection(myTour.getTourList());
		dataProvider.setSortOrder(MyTourStop::getPrice, SortDirection.ASCENDING);
		tourListView.setDataProvider(dataProvider);

		// Reselect all selected items base on the saved selection
		for (MyTourStop mts : selected) {
			tourListView.select(mts);
		}
		
		// re-add the markers
		tourMap.removeTourMarkers();
		tourMap.addMarkersForTour();

	}

	public void showSelected() {

		// Save of the selection
		ArrayList<MyTourStop> selected = new ArrayList<MyTourStop>(myTour.getSelected());
		
		// Change out the data provider
		ListDataProvider<MyTourStop> dataProvider = DataProvider.ofCollection(myTour.getSelected());
		dataProvider.setSortOrder(MyTourStop::getSequence, SortDirection.ASCENDING);
		tourListView.setDataProvider(dataProvider);

		// Reselect all selected items based on the saved selection
		for (MyTourStop mts : selected) {
			tourListView.select(mts);
		}
		
		tourMap.removeTourMarkers();
		tourMap.addMarkersForTourSelection();
		// hideExcludedMarkers();
	}

	public void removeTour() {
		tourMap.removeTourMarkers();
		if (tourPolyLine != null) {
			tourMap.removePolyline(tourPolyLine);
			if (myTour != null) {
				myTour.clearSequence();
			}
		}
	}

	public void replaceTour(MyTour tour) {

		// update start and end locations from markers
		if (myTour != null) {
			removeTour();
		}
		myTour = tour;
		showAll();
	}

	public void refresh() {
		tourListView.getDataProvider().refreshAll();
		tourMap.refresh();
		ComponentContainer parent = (ComponentContainer) startTextArea.getParent();
		parent.removeComponent(startTextArea);
		parent.removeComponent(endTextArea);
		parent.addComponent(getStartTextArea());
		parent.addComponent(getEndTextArea());
	}

	public Button getRouteTourButton() {
		routeTour = new Button("Route", (ClickListener) event -> {
			if (tourPolyLine != null) {
				tourMap.removePolyline(tourPolyLine);
				myTour.clearSequence();
			}

			Address start = myTour.getStartAddress();
			Address end = myTour.getEndAddress();

			// move this into myTour;
			List<Address> routeList = myTour.getSelectedAddresses();
			MapDirection dir = new MapDirection(start, end, routeList);
			EncodedPolyline polyline = dir.route(myTour.getTourDate());
			int seq[] = dir.getAddressSequence();
			myTour.setSequence(seq);
			
			tourPolyLine = GeoLocation.convert(polyline);
			tourPolyLine.setStrokeColor("#C7320D");
			tourPolyLine.setStrokeWeight(4);
			tourMap.addPolyline(tourPolyLine);
			
			refresh();
		});

		routeTour.setDescription("Click to route tour");
		routeTour.setEnabled(false);
		return routeTour;
	}

	public Component getTourSelector() {

		// Create the selection component for tour dates
		TourListRepository tleRepo = TourListEntry.getRepo();
		List<Date> tlDates = tleRepo.findAllDisctintDatesNewestFirst();

		ComboBox<Date> select = new ComboBox<>("Select Date");
		select.setItems(tlDates);

		select.setItemCaptionGenerator(d -> {
			SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy");
			String ds = df.format(d);
			return ds;
		});

		// Add the dates
		select.setItems(tlDates);

		select.addSelectionListener(event -> {
			Date date = event.getValue();
			if (date != null) {

				myTour = new MyTour(date, getMapView().getStartAddress(), getMapView().getEndAddress());

				// replace the tour on the view with the new one
				replaceTour(myTour);

				// enable the printTour button
				printTour.setPdfFileGetter(myTour::getPdfFile);
				printTour.setEnabled(true);
				routeTour.setEnabled(true);

			}
		});
		return select;
	}

	public ShowPdfButton getPrintPdfButton() {
		printTour = new ShowPdfButton();
		printTour.setCaption("Print");
		printTour.setDescription("Click to print the selected tour");
		printTour.setEnabled(false);
		return printTour;
	}
	
	public TextArea getStartTextArea() {
		startTextArea = getStartEndTextArea();
		startTextArea.setCaption("Tour Start");
		startTextArea.setDescription("Move start marker on map to change the start address");
		startTextArea.setValue(getAdrText(getMapView().getStartAddress()));
		return startTextArea;
	}

	public TextArea getEndTextArea() {
		endTextArea = getStartEndTextArea();
		endTextArea.setCaption("Tour End");
		endTextArea.setDescription("Move end marker on map to change the end address");
		endTextArea.setValue(getAdrText(getMapView().getEndAddress()));
		return endTextArea;
	}
	
	private TextArea getStartEndTextArea() {
		TextArea textArea = new TextArea();
		textArea.setStyleName("startEndText");
		textArea.setReadOnly(true);
		textArea.setHeight(4.0f,Unit.EM);
		return textArea;
	}

	private String getAdrText(Address adr) {
		String text = adr.getFullStreet() + "\n";
		text += adr.getCity() + ", " + adr.getState() + " " + adr.getZip();
		return text;
	}

	public MyTour getTour() {
		return myTour;
	}
}
