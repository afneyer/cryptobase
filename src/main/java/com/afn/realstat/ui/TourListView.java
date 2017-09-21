package com.afn.realstat.ui;

import com.afn.realstat.MyTourStop;
import com.vaadin.ui.Grid;

@SuppressWarnings("serial")
public class TourListView extends Grid<MyTourStop> {

	private MyTourView myTourView;

	public TourListView(MyTourView tourView) {
		this.myTourView = tourView;
	}

	/**
	 * Refreshes the list view
	 */
	public void refresh() {
		this.getDataProvider().refreshAll();
	}
	
	public MyTourView getTourView() {
		return myTourView;
	}

}