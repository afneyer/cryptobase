package com.afn.realstat.ui;

import org.springframework.data.geo.Point;

import com.afn.realstat.Address;
import com.afn.realstat.util.GeoLocation;
import com.afn.realstat.util.Icon;
import com.afn.realstat.util.MapLocation;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapMarker;

@SuppressWarnings("serial")
public class TourStartEndMarker extends GoogleMapMarker {

	private TourMarkerType tourMarkerType;
	private Address address;
	private Icon icon;
	private TourMap tourMap;

	public enum TourMarkerType {
		start, end;
	}

	public TourStartEndMarker(TourMarkerType type, Address adr, TourMap tourMap) {
		super();

		this.address = adr;
		this.tourMarkerType = type;
		this.tourMap = tourMap;

		this.setPosition(GeoLocation.convertToLatLon(address.getLoc()));

		this.setAnimationEnabled(false);
		if (tourMarkerType == TourMarkerType.start) {
			icon = new Icon(Icon.startIcon);
			icon.setText("S");
			setCaption("Tour Start Marker: Move to desired location");
		}
		if (tourMarkerType == TourMarkerType.end) {
			icon = new Icon(Icon.endIcon);
			icon.setText("E");
			setCaption("Tour Start Marker: Move to desired location");
		}

		this.setDraggable(true);
	}

	@Override
	public String getIconUrl() {
		return icon.getIconUrl();
	}
	
	public Address getAddress() {
		LatLon latLon = this.getPosition();
		Point loc = GeoLocation.convertToPoint(latLon);
		MapLocation mapLoc = new MapLocation(loc);
		Address address = mapLoc.getAddress();
		return address;
	}

	public boolean isStartMarker() {
		return tourMarkerType == TourMarkerType.start;

	}

	public boolean isEndMarker() {
		return tourMarkerType == TourMarkerType.end;

	}
	
	public TourMap getTourMap() {
		return tourMap;
	}

	public MyTourView getTourView() {
		return tourMap.getTourView();
	}

}
