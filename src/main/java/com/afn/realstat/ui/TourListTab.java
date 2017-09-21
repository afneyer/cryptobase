package com.afn.realstat.ui;

import com.afn.realstat.AdReviewTourList;
import com.afn.realstat.ui.FileUploader.AfterUploadSucceeded;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class TourListTab {

	private HorizontalLayout tourPage;
	private TourListView tourListView = null;
	private MyTourView myTourView;
	private Component tourListSelector;
	private HorizontalLayout tourDisplayControl;

	private Button routeTourButton = null;
	private ShowPdfButton printTourButton = null;

	public TourListTab() {

		// initialize tourPage
		tourPage = new HorizontalLayout();
		tourPage.setMargin(false);
		tourPage.setSizeFull();

		VerticalLayout tourDisplay = new VerticalLayout();
		tourDisplay.setHeight(100, Unit.PERCENTAGE);
		tourDisplay.setWidth(460, Unit.PIXELS);
		tourDisplay.setMargin(false);

		tourPage.addComponent(tourDisplay);

		tourDisplayControl = new HorizontalLayout();
		tourDisplayControl.setMargin(false);
		tourDisplay.addComponent(tourDisplayControl);

		// initialize tourList view
		myTourView = new MyTourView();
		tourListView = myTourView.getListView();
		tourListView.setHeight(100, Unit.PERCENTAGE);
		tourListView.setWidth(460, Unit.PIXELS);

		HorizontalLayout startEndDisplay = new HorizontalLayout();

		TextArea start = myTourView.getStartTextArea();
		TextArea end = myTourView.getEndTextArea();

		startEndDisplay.addComponent(start);
		startEndDisplay.addComponent(end);
		tourDisplay.addComponent(startEndDisplay);

		tourDisplay.addComponent(tourListView);
		tourDisplay.setExpandRatio(tourListView, 1.0f);

		tourListSelector = myTourView.getTourSelector();
		tourDisplayControl.addComponent(tourListSelector);

		VerticalLayout tourControls = new VerticalLayout();
		tourControls.setMargin(false);
		tourDisplayControl.addComponent(tourControls);

		HorizontalLayout tourControlButtons = new HorizontalLayout();
		tourControlButtons.setMargin(false);
		tourControls.addComponent(tourControlButtons);

		HorizontalLayout tourOptions = new HorizontalLayout();
		tourOptions.setMargin(false);
		tourControls.addComponent(tourOptions);

		CheckBox showSelected = myTourView.getShowSelectedCheckBox();
		tourOptions.addComponent(showSelected);

		routeTourButton = myTourView.getRouteTourButton();
		tourControlButtons.addComponent(routeTourButton);

		printTourButton = myTourView.getPrintPdfButton();
		tourControlButtons.addComponent(printTourButton);

		Button importTourFile = getImportTourFileButton();

		tourControlButtons.addComponent(importTourFile);
		importTourFile.setDescription("testDescription");

		// initialize tourMapView
		TourMap map = myTourView.getMapView();
		map.setSizeFull();

		tourPage.addComponent(map);
		tourPage.setExpandRatio(map, 1.0f);
		// tourPage.setExpandRatio(tourDisplay, 1.0f);
		// initialize tourList view
	}

	private Button getImportTourFileButton() {
		@SuppressWarnings("serial")
		Button tourFile = new Button("Import", new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				importAdReviewPdf();
			}
		});
		return tourFile;
	}

	protected void importAdReviewPdf() {

		// create a window to upload a pdf-file
		Window popUp = new Window("Import AdReview");
		VerticalLayout subContent = new VerticalLayout();
		popUp.setContent(subContent);

		FileUploader receiver = new FileUploader();
		receiver.setAfterUploadSucceeded(new AfterUploadSucceeded() {
			@Override
			public void afterUploadSucceeded(FileUploader fileUploader) {
				AdReviewTourList adReview = new AdReviewTourList(fileUploader.getFile());
				adReview.createTourList();
				Notification.show("Upload Complete",
						"AdReview has been uploaded, please select the date for your tour.",
						Notification.Type.HUMANIZED_MESSAGE);
				popUp.close();
			}
		}

		);
		Upload upload = new Upload("Select AdReview Tour Pdf-File", receiver);
		upload.addSucceededListener(receiver);
		subContent.addComponent(upload);

		// Center it in the browser window
		popUp.center();

		tourPage.getUI().addWindow(popUp);

	}

	public Component getTourListPage() {
		return tourPage;
	}

}
