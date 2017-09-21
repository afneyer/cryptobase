package com.afn.realstat.ui;

import com.vaadin.tapio.googlemaps.client.events.MarkerClickListener;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapMarker;

/**
 * Listener that opens info window when a marker is clicked.
 */
public class TourListMarkerClickListener implements MarkerClickListener {

	private static final long serialVersionUID = 646386541641L;

	private final TourMarker marker;

	public TourListMarkerClickListener(TourMarker marker) {
		this.marker = marker;
		marker.setListener(this);
	}

	@Override
	public void markerClicked(GoogleMapMarker clickedMarker) {
		if (clickedMarker.equals(marker)) {
			// change the marker and add it to the selected list of MyTour
			marker.toggleTour();
		}
	}
}