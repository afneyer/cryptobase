package com.afn.realstat.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.Instant;
import org.slf4j.LoggerFactory;

import com.afn.realstat.Address;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.EncodedPolyline;
import com.google.maps.model.LatLng;

import ch.qos.logback.classic.Logger;

/**
 * MapLocation is initialized with an address. The getters return the
 * information about the location.
 * 
 * @author Andreas Neyer
 *
 *         Copyright 2017 afndev. All rights reserved.
 *
 */
public class MapDirection {

	@SuppressWarnings("unused")
	private static final Logger log = (Logger) LoggerFactory.getLogger("app");

	private Address start;
	private Address end;
	private List<Address> route;
	private GoogleMapApi mapApi = null;
	private int[] order = null;

	public MapDirection(Address start, Address end, List<Address> routeList) {
		this.mapApi = new GoogleMapApi();
		this.start = start;
		this.end = end;
		this.route = routeList;
	}

	public EncodedPolyline route(Date startTime) {

		Instant routeStart = new Instant(startTime);

		EncodedPolyline polyLine = null;

		LatLng startPoint = start.getLatLng();
		LatLng endPoint = end.getLatLng();

		int wpSize = route.size();
		ArrayList<LatLng> wayPoints = new ArrayList<LatLng>();
		LatLng wayPoint;
		for (int i = 0; i < wpSize; i++) {
			wayPoint = route.get(i).getLatLng();
			if (wayPoint != null) {
				wayPoints.add(wayPoint);
			} 
		}

		LatLng[] wayPnts = wayPoints.toArray(new LatLng[wayPoints.size()]);
		DirectionsApiRequest request = null;
		if (routeStart.isAfter(new Instant())) { 
			request = DirectionsApi.newRequest(mapApi.getContext()).origin(startPoint).destination(endPoint)
					.departureTime(routeStart).optimizeWaypoints(true).waypoints(wayPnts);
		} else {
			// verify that directions API exists otherwise throw exception
			request = DirectionsApi.newRequest(mapApi.getContext()).origin(startPoint).destination(endPoint)
					.optimizeWaypoints(true).waypoints(wayPnts);
		}
		DirectionsResult result = null;
		try {
			result = request.await();
		} catch (Exception e) {		
			throw new RuntimeException("Error in Google Directions API-Request",e);
		}

		DirectionsRoute[] directionsRouteList = result.routes;
		for (int i = 0; i < directionsRouteList.length; i++) {
			DirectionsRoute directionsRoute = directionsRouteList[i];
			order = directionsRoute.waypointOrder;
			polyLine = directionsRoute.overviewPolyline;	
		}
		
		return polyLine;
	}

	/*
	public int[] getWaypointSequence() {
		return order;
	}
	*/
	
	public int[] getAddressSequence() {
		int[] adrSeq = new int[order.length];
		for (int i=0; i<order.length; i++) {
			int addressIndex = order[i];
			adrSeq[addressIndex] = i;
		}	
		return adrSeq;
	}
}
