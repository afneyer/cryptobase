package com.afn.realstat.ui;

import com.vaadin.shared.annotations.DelegateToWidget;
import com.vaadin.tapio.googlemaps.client.GoogleMapState;

/*
 * Adds a refresh count to the GoogleMapState which is used to
 * force refresh on the map. For example, changing the icon of a marker
 * does not refresh that marker. Only adding and removing markers does.
 */
public class AfnGoogleMapState extends GoogleMapState {
	
	private static final long serialVersionUID = -5172382510907831673L;
	
	@DelegateToWidget
	public long refreshCount = 0;
	
}
