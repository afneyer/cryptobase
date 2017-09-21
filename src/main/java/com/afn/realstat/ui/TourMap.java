package com.afn.realstat.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.springframework.data.geo.Point;

import com.afn.realstat.Address;
import com.afn.realstat.MyTour;
import com.afn.realstat.MyTourStop;
import com.afn.realstat.ui.TourStartEndMarker.TourMarkerType;
import com.afn.realstat.util.Icon;
import com.google.maps.model.EncodedPolyline;
import com.google.maps.model.LatLng;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapMarker;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapPolyline;

@SuppressWarnings("serial")
public class TourMap extends GoogleMap {

	private GoogleMapMarker dummyMarker;
	private TourStartEndMarker startMarker;
	private TourStartEndMarker endMarker;
	private MyTourView myTourView;

	private TourMap(String apiKey, String clientId, String language) {
		super(apiKey, clientId, language);
	}

	public TourMap(MyTourView tourView) {
		this(null, null, null);
		this.myTourView = tourView;
		addStartEndMarkers();
	}


	/**
	 * Adds start and end markers using the default addresses for the user
	 */
	public void addStartEndMarkers() {

		startMarker = new TourStartEndMarker(TourMarkerType.start, getTour().getStartAddress(), this);
		
		// TODO move to TourStartEndMarker
		addMarker(startMarker);
		addMarkerDragListener(new StartEndMarkerDragListener(startMarker));

		endMarker = new TourStartEndMarker(TourMarkerType.end, getTour().getEndAddress(), this);
		addMarker(endMarker);
		addMarkerDragListener(new StartEndMarkerDragListener(endMarker));

	}
	
	public List<TourMarker> getTourMarkers() {
		List<TourMarker> markerList = new ArrayList<TourMarker>();

		Iterator<GoogleMapMarker> markerIter = getMarkers().iterator();

		GoogleMapMarker m;
		while (markerIter.hasNext()) {
			m = markerIter.next();
			if (TourMarker.class.isInstance(m)) {
				markerList.add((TourMarker) m);
			}
		}

		return markerList;
	}

	public TourMarker getMarker(MyTourStop mts) {
		Iterator<TourMarker> markerIter = getTourMarkers().iterator();
		while (markerIter.hasNext()) {
			TourMarker tm = markerIter.next();
			if (mts.equals(tm.getMyTourStop())) {
				return tm;
			}
		}
		return null;
	}
	
	public void addMarkersForTour() {

		List<MyTourStop> mtsList = getTourView().getTour().getTourList();
		for (MyTourStop mts : mtsList) {
			addMarkerForTourListEntry(mts);
		}
		centerOnTourMarkers();

	}
	
	public void addMarkersForTourSelection() {
		List<MyTourStop> mtsList = getTourView().getTour().getSelected();
		for (MyTourStop mts : mtsList) {
			addMarkerForTourListEntry(mts);
		}
		centerOnTourMarkers();
	}

	public TourMarker addMarkerForTourListEntry(MyTourStop mts) {
		
		TourMarker mrkr = null;
		// all tour tourStops must have locations
		Point loc = mts.getLocation();

		if (loc != null) {
			mrkr = new TourMarker(mts, this);
			// TODO move to Marker Constructor
			addMarker(mrkr);
			addMarkerClickListener(new TourListMarkerClickListener(mrkr));

		}
		return mrkr;
	}

	/* TODO remove
	private void hideExcludedMarkers() {
		List<MyTourStop> mtsList = myTour.getTourList();
		for (MyTourStop mts : mtsList) {
			TourMarker tm = mts.getTourMarker();
			if (!tm.isInTour()) {
				map.removeMarker(mts.getTourMarker());
			}
		}
		map.centerOnTourMarkers();
	}
	*/
	
	
	
	public void addMarker(TourMarker marker) {
		super.addMarker(marker);
	}

	/**
	 * Refreshes the markers on a GoogleMap
	 * 
	 * For lack of better option adds or removes a dummy marker which forces a
	 * refresh. Used to refresh the client after performing actions on a marker
	 * that don't refresh the client (e.g. change icon).
	 *
	 */
	public void refresh() {

		// if there is a dummy Marker, remove it
		if (dummyMarker != null) {
			removeMarker(dummyMarker);
		}
		/*
		 * Create a new dummy Marker and add it to the map. The dummy marker is
		 * a marker without icon and is excluded from function affecting markers
		 * of AfnGoogleMap.
		 * 
		 * The dummy marker has a new id and hence the set of marker changes the
		 * state and forces a refresh.
		 */
		dummyMarker = new GoogleMapMarker("Dummy Marker", new LatLon(0, 0), false, null);
		dummyMarker.setIconUrl(new Icon(Icon.emptyIcon).getIconUrl());
		addMarker(dummyMarker);

	}

	/**
	 * Set the scale (zoom) and the boundaries of the map so that all markers
	 * are shown on the map
	 * 
	 * @param map
	 */
	public void centerOnTourMarkers() {
	
		Collection<GoogleMapMarker> tourMarkers = Collections.unmodifiableCollection(getTourMarkers());
		
		Collection<GoogleMapMarker> gMarkers = new ArrayList<GoogleMapMarker>();
		gMarkers.add(startMarker);
		gMarkers.add(endMarker);
		gMarkers.addAll(tourMarkers);
		
		this.centerOnMarkers(gMarkers);
	}

	/**
	 * Centers map on markers excluding the dummy marker
	 */
	public void centerOnMarkers() {
		centerOnMarkers(this.getMarkers());
	}

	/**
	 * Centers map on a collection of markers, excludes dummy marker
	 */
	private void centerOnMarkers(Collection<GoogleMapMarker> markers) {

		double latMin = 90.0;
		double latMax = -90.0;
		double lonMin = 180.0;
		double lonMax = -180.0;
		Iterator<GoogleMapMarker> iter = markers.iterator();

		GoogleMapMarker m;
		while (iter.hasNext()) {
			m = iter.next();

			// Skip dummyMarker
			if (m == dummyMarker) {
				break;
			}

			double lat = m.getPosition().getLat();
			latMin = Math.min(latMin, lat);
			latMax = Math.max(latMax, lat);

			double lon = m.getPosition().getLon();
			lonMin = Math.min(lonMin, lon);
			lonMax = Math.max(lonMax, lon);

		}

		if (markers.size() != 0) {
			LatLon boundsNE = new LatLon(latMax, lonMax);
			LatLon boundsSW = new LatLon(latMin, lonMin);
			fitToBounds(boundsNE, boundsSW);
		}

	}

	/**
	 * Adds a polyline to a map
	 */
	public void addPolyline(EncodedPolyline polyline) {
		List<LatLng> latLngPoly = polyline.decodePath();
		List<LatLon> latLonPoly = new ArrayList<LatLon>();
		for (LatLng latLng : latLngPoly) {
			LatLon latLon = new LatLon(latLng.lat, latLng.lng);
			latLonPoly.add(latLon);
		}
		GoogleMapPolyline gmPoly = new GoogleMapPolyline(latLonPoly);
		this.addPolyline(gmPoly);
	}

	/**
	 * Removes all markers from the map
	 */
	public void removeAllMarkers() {
		// create a separate list to avoid concurrent access
		Collection<GoogleMapMarker> markers = new ArrayList<GoogleMapMarker>(this.getMarkers());
		for (GoogleMapMarker marker : markers) {
			removeMarker(marker);
		}
	}
	
	public void removeTourMarkers() {
		Collection<GoogleMapMarker> markers = new ArrayList<GoogleMapMarker>(this.getMarkers());
		for (GoogleMapMarker marker : markers) {
			if (marker instanceof TourMarker) {
				removeMarker(marker);
			}
		}
	}

	public Address getStartAddress() {
		return startMarker.getAddress();
	}
	
	public Address getEndAddress() {
		return endMarker.getAddress();
	}

	public TourStartEndMarker getStartMarker() {
		return startMarker;
	}

	public void setStartMarker(TourStartEndMarker startMarker) {
		this.startMarker = startMarker;
	}

	public TourStartEndMarker getEndMarker() {
		return endMarker;
	}

	public void setEndMarker(TourStartEndMarker endMarker) {
		this.endMarker = endMarker;
	}
	
	public MyTourView getTourView() {
		return myTourView;
	}
	
	public MyTour getTour() {
		return getTourView().getTour();
	}

}