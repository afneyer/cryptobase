package com.afn.realstat.ui;

import java.util.List;
import java.util.function.Function;

import javax.servlet.annotation.WebServlet;

// import org.hsqldb.lib.Iterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;

import com.afn.realstat.Address;
import com.afn.realstat.AddressManager;
import com.afn.realstat.Agent;
import com.afn.realstat.AgentRepository;
import com.afn.realstat.PropertyTransactionService;
import com.afn.realstat.QAgent;
import com.afn.realstat.RealProperty;
import com.querydsl.core.types.dsl.StringPath;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.events.InfoWindowClosedListener;
import com.vaadin.tapio.googlemaps.client.events.MapClickListener;
import com.vaadin.tapio.googlemaps.client.events.MapMoveListener;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapInfoWindow;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapMarker;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Google Maps UI for displaying properties on maps
 */
@SpringUI(path = "/maps")
@Theme("realstatTheme")
@SuppressWarnings("serial")
@Widgetset("AppWidgetset")
@SpringComponent
@Push
public class GoogleMapUI extends UI {

	@Autowired
	private PropertyTransactionService ptService;

	@Autowired
	private AgentRepository agtRepo;

	@Autowired
	private AddressManager adrMgr;

	private AfnGoogleMap googleMap;

	protected CssLayout consoleLayout;
	protected MapClickListener dummyListener;

	@WebServlet(value = { "/*" }, asyncSupported = true)
	// @VaadinServletConfiguration(productionMode = false, ui =
	// GoogleMapsDemoUI.class, widgetset =
	// "com.vaadin.tapio.googlemaps.demo.DemoWidgetset")
	@VaadinServletConfiguration(productionMode = false, ui = GoogleMapUI.class)
	public static class Servlet extends VaadinServlet {
		/* TODO
		@Override
		protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			String url = req.getRequestURI();
			System.out.println(url);
			resp.getWriter().println("Hello, world");
		}
		*/

	}

	/*
	@Component
	public static class FileServlet extends DefaultServlet {
	
		@Override
		protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			String url = req.getRequestURI();
			System.out.println(url);
			resp.getWriter().println("Hello, world");
		}
		
	}
	*/
	 
	/*
	@Controller
    @RequestMapping("/temp")
    public class TempFileController {
		 @RequestMapping(method = RequestMethod.GET)
		   public String printHello(ModelMap model) {
		      model.addAttribute("message", "Hello Spring MVC Framework!");
		      return "hello";
		   }
	}
	*/
	
	
	@Autowired
	public GoogleMapUI(PropertyTransactionService ptService) {
		this.ptService = ptService;
	}

	public GoogleMapUI() {
	}

	@Override
	protected void init(VaadinRequest request) {
		CssLayout rootLayout = new CssLayout();
		rootLayout.setSizeFull();
		setContent(rootLayout);

		TabSheet tabs = new TabSheet();
		tabs.setSizeFull();
		rootLayout.addComponent(tabs);

		VerticalLayout page = new VerticalLayout();
		page.setSizeFull();
		tabs.addTab(page, "Map");

		TourListTab tourListTab = new TourListTab();
		com.vaadin.ui.Component tourListComp = tourListTab.getTourListPage();
		tabs.addTab(tourListComp, "Tour");

		HorizontalLayout mapContent = new HorizontalLayout();
		mapContent.setHeight(100, Unit.PERCENTAGE);
		mapContent.setWidth(100, Unit.PERCENTAGE);
		page.addComponent(mapContent);

		googleMap = new AfnGoogleMap(null, null, null);

		googleMap.setCenter(new LatLon(37.83, -122.226));
		googleMap.setZoom(13);
		googleMap.setSizeFull();

		mapContent.addComponent(googleMap);
		mapContent.setExpandRatio(googleMap, 3.0f);
		page.setExpandRatio(mapContent, 4.0f);

		Panel console = new Panel();
		final CssLayout cnlt = new CssLayout();
		consoleLayout = cnlt;
		console.setContent(cnlt);

		page.addComponent(console);
		page.setExpandRatio(console, 1.0f);

		VerticalLayout buttonLayoutRow1 = new VerticalLayout();
		mapContent.addComponent(buttonLayoutRow1);
		mapContent.setExpandRatio(buttonLayoutRow1, 1.0f);

		googleMap.addMapMoveListener(new MapMoveListener() {
			@Override
			public void mapMoved(int zoomLevel, LatLon center, LatLon boundsNE, LatLon boundsSW) {
				Label consoleEntry = new Label("Map moved to (" + center.getLat() + ", " + center.getLon() + "), zoom "
						+ zoomLevel + ", boundsNE: (" + boundsNE.getLat() + ", " + boundsNE.getLon() + "), boundsSW: ("
						+ boundsSW.getLat() + ", " + boundsSW.getLon() + ")");
				cnlt.addComponent(consoleEntry, 0);
			}
		});

		googleMap.addMapClickListener(new MapClickListener() {
			@Override
			public void mapClicked(LatLon position) {
				Label consoleEntry = new Label("Map click to (" + position.getLat() + ", " + position.getLon() + ")");
				cnlt.addComponent(consoleEntry, 0);
			}
		});

		googleMap.addInfoWindowClosedListener(new InfoWindowClosedListener() {

			@Override
			public void infoWindowClosed(GoogleMapInfoWindow window) {
				Label consoleEntry = new Label("InfoWindow \"" + window.getContent() + "\" closed");
				cnlt.addComponent(consoleEntry, 0);
			}
		});

		Button showAgentDeals = new Button("Show transactions for agent", new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				StringPath strPath = QAgent.agent.agentName;

				Function<Agent, Boolean> addMarkers = e -> {
					addMarkersForAgentDeals(e);
					return true;
				};

				Lov<Agent> lov = new Lov<Agent>(Agent.class, agtRepo, strPath, addMarkers);
				Window popUp = lov.getPopUpWindowLov();
				UI.getCurrent().addWindow(popUp);

			}

		});
		buttonLayoutRow1.addComponent(showAgentDeals);
		showAgentDeals.setWidth(100, Unit.PERCENTAGE);

		Button clearMarkersButton = new Button("Remove all markers", new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent clickEvent) {
				googleMap.clearMarkers();
			}
		});
		buttonLayoutRow1.addComponent(clearMarkersButton);
		clearMarkersButton.setWidth(100, Unit.PERCENTAGE);
	}



	// Make this null safe TODO

	protected void addMarkersForAgentDeals(Agent agt) {
		List<RealProperty> rpList = ptService.getRealPropertiesForAgent(agt.getLicense());

		// TODO change to the following
		// List<RealProperty> rpList = ptService.getRealPropertiesForAgent(agt);
		for (RealProperty rp : rpList) {
			Address adr = rp.getPropertyAdr();
			GoogleMapMarker mrkr = addMarkerForAddress(adr);
			String text = "Testing Info Window";
			new GoogleMapInfoWindow(text, mrkr);
		}
		// }

		googleMap.centerOnMarkers();
		System.out.println("Done with Marking");

	}

	protected void addMarkersForAddresses(List<Address> list) {

		// TODO change to the following
		// List<RealProperty> rpList = ptService.getRealPropertiesForAgent(agt);
		for (Address adr : list) {
			addMarkerForAddress(adr);
		}
		// }

		googleMap.centerOnMarkers();
		System.out.println("Done with Marking");

	}

	private GoogleMapMarker addMarkerForAddress(Address adr) {
		Point loc = adrMgr.getLocation(adr);
		if (adr != null) {
			if (loc != null) {
				GoogleMapMarker mrkr = new GoogleMapMarker("testCaption", new LatLon(loc.getY(), loc.getX()), false);
				googleMap.addMarker(mrkr);
				googleMap.markAsDirty();
				return mrkr;
			}
		}
		return null;
	}

}