package com.afn.realstat.ui;

import org.springframework.data.geo.Point;

import com.afn.realstat.Address;
import com.afn.realstat.MyTourStop;
import com.afn.realstat.util.GeoLocation;
import com.afn.realstat.util.Icon;
import com.afn.realstat.util.MapLocation;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapMarker;

@SuppressWarnings("serial")
public class TourMarker extends GoogleMapMarker {

	private MyTourStop myTourStop;
	private TourMap tourMap;

	private TourListMarkerClickListener clickListener;

	private Icon blankInIcon;
	private Icon blankOutIcon;

	public TourMarker(MyTourStop mts, TourMap map) {
		super();

		this.myTourStop = mts;

		this.tourMap = map;

		Point location = mts.getLocation();
		// TODO re-factor conversion from point to LatLon
		LatLon pos = new LatLon(location.getY(), location.getX());
		this.setPosition(pos);

		String cap = mts.mapMarkerCaption();
		this.setCaption(cap);

		this.setAnimationEnabled(false);
		this.blankInIcon = new Icon(Icon.markerButtonRed);
		this.blankOutIcon = new Icon(Icon.markerButtonGreen);
		this.setDraggable(false);

	}

	@Override
	public String getIconUrl() {

		Icon icon = null;

		if (isInTour()) {
			icon = blankInIcon;
			int seq = myTourStop.getSequence();
			String text = Integer.toString(seq);
			if (seq != 0) {
				icon = new Icon(blankInIcon);
				icon.setText(text);
			}
		} else {
			icon = blankOutIcon;
		}
		return icon.getIconUrl();
	}

	public boolean isInTour() {
		return myTourStop.isSelected();
	}

	public void toggleTour() {
		MyTourStop stop = getMyTourStop();
		TourListView listView = tourMap.getTourView().getListView();
		if (isInTour()) {
			stop.deselect();
			listView.getSelectionModel().deselect(stop);
		} else {
			stop.select();
			listView.getSelectionModel().select(getMyTourStop());
		}
		tourMap.refresh();
		listView.refresh();
	}

	public MyTourStop getMyTourStop() {
		return myTourStop;
	}

	public void setListener(TourListMarkerClickListener tourListMarkerClickListener) {
		clickListener = tourListMarkerClickListener;
	}

	public TourListMarkerClickListener getTourListMarkerClickListener() {
		return clickListener;
	}

	public Address getAddress() {
		LatLon latLon = this.getPosition();
		Point loc = GeoLocation.convertToPoint(latLon);
		MapLocation mapLoc = new MapLocation(loc);
		Address address = mapLoc.getAddress();
		return address;
	}

}
