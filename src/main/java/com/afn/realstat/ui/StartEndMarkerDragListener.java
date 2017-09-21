package com.afn.realstat.ui;

import com.afn.realstat.Address;
import com.afn.realstat.MyTour;
import com.afn.realstat.util.GeoLocation;
import com.afn.realstat.util.MapLocation;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.events.MarkerDragListener;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapMarker;

/**
 * Listener that opens info window when a marker is clicked.
 */
public class StartEndMarkerDragListener implements MarkerDragListener {

	/**
	* 
	*/
	private static final long serialVersionUID = -6867432226234962395L;
	private final TourStartEndMarker marker;

	public StartEndMarkerDragListener(TourStartEndMarker marker) {
		this.marker = marker;
	}

	@Override
	public void markerDragged(GoogleMapMarker draggedMarker, LatLon oldPosition) {
		if (draggedMarker.equals(marker)) {
			LatLon newPosition = draggedMarker.getPosition();
			MapLocation loc = new MapLocation(GeoLocation.convertToPoint(newPosition));
			Address adr = loc.getAddress();
			MyTour myTour = marker.getTourView().getTour();
			if (marker.isStartMarker()) {
				myTour.setStartAddress( adr);
			}
			if (marker.isEndMarker()) {
				myTour.setEndAddress(adr);
			}
		}
		marker.getTourView().refresh();
		
	}

}
