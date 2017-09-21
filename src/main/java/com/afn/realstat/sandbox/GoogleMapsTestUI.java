package com.afn.realstat.sandbox;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.events.MapClickListener;
import com.vaadin.tapio.googlemaps.client.events.MarkerClickListener;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapMarker;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * Google Maps UI for testing and demoing.
 */
@SpringUI(path = "/mapTest")
@Theme("realstatTheme")
@SuppressWarnings("serial")
@Widgetset("AppWidgetset")

@Push
public class GoogleMapsTestUI extends UI {

	private GoogleMap googleMap;

	private final String inIconUrl = "VAADIN/house-32Red.ico";
	private final String outIconUrl = "VAADIN/star32.png";
	private final String emptyIcon = "VAADIN/emptyIcon.jpg";

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = GoogleMapsTestUI.class, widgetset = "com.vaadin.tapio.googlemaps.demo.DemoWidgetset")
	public static class Servlet extends VaadinServlet {
	}

	@Override
	protected void init(VaadinRequest request) {
		final UI ui = this;
		ui.getPushConfiguration().setPushMode(PushMode.AUTOMATIC);
		CssLayout rootLayout = new CssLayout();
		rootLayout.setSizeFull();
		setContent(rootLayout);

		TabSheet tabs = new TabSheet();
		tabs.setSizeFull();
		rootLayout.addComponent(tabs);

		VerticalLayout mapContent = new VerticalLayout();
		mapContent.setSizeFull();
		tabs.addTab(mapContent, "The map map");
		tabs.addTab(new Label("An another tab"), "The other tab");

		googleMap = new GoogleMap(null, null, null);
		// uncomment to enable Chinese API.
		// googleMap.setApiUrl("maps.google.cn");
		googleMap.setCenter(new LatLon(37.824960, -122.238769));
		googleMap.setZoom(10);
		googleMap.setSizeFull();

		GoogleMapMarker testMarker = new GoogleMapMarker("TestMarker for Icon Changes",
				new LatLon(37.824960, -122.238769), false, null);
		testMarker.setAnimationEnabled(false);
		testMarker.setIconUrl(inIconUrl);
		googleMap.addMarker(testMarker);
		
		// GoogleMapMarker dummyMarker = new GoogleMapMarker("Dummy Marker", new LatLon(37.8302682,-122.247759), false, null);
		GoogleMapMarker dummyMarker = new GoogleMapMarker("Dummy Marker", new LatLon(0,0), false, null);
		dummyMarker.setIconUrl(emptyIcon);
		
		
		googleMap.setMinZoom(4);
		googleMap.setMaxZoom(16);

		mapContent.addComponent(googleMap);
		mapContent.setExpandRatio(googleMap, 1.0f);

		Panel console = new Panel();
		console.setHeight("100px");
		final CssLayout consoleLayout = new CssLayout();
		console.setContent(consoleLayout);
		mapContent.addComponent(console);

		HorizontalLayout buttonLayoutRow1 = new HorizontalLayout();
		buttonLayoutRow1.setHeight("26px");
		mapContent.addComponent(buttonLayoutRow1);

		googleMap.addMarkerClickListener(new MarkerClickListener() {
			
			@Override
			public void markerClicked(GoogleMapMarker clickedMarker) {
				Label consoleEntry = new Label(
						"Marker \"" + clickedMarker.getCaption() + "\" at (" + clickedMarker.getPosition().getLat()
								+ ", " + clickedMarker.getPosition().getLon() + ") clicked.");
				consoleLayout.addComponent(consoleEntry, 0);

				if (clickedMarker.getIconUrl().contentEquals(inIconUrl)) {
					clickedMarker.setIconUrl(outIconUrl);
				} else {
					clickedMarker.setIconUrl(inIconUrl);
				}
				
				// refresh function
				if (googleMap.hasMarker(dummyMarker)) {
					googleMap.removeMarker(dummyMarker);
				} else {
					googleMap.addMarker(dummyMarker);
				} 

			}
		});

		googleMap.addMapClickListener(new MapClickListener() {
			@Override
			public void mapClicked(LatLon position) {
				Label consoleEntry = new Label("Map click to (" + position.getLat() + ", " + position.getLon() + ")");
				consoleLayout.addComponent(consoleEntry, 0);
			}
		});

	}
}