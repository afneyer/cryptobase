package com.afn.realstat.util;

import java.util.Date;

import org.slf4j.LoggerFactory;

import com.afn.realstat.AfnDateUtil;
import com.afn.realstat.AppParamManager;
import com.afn.realstat.framework.SpringApplicationContext;
import com.google.maps.GeoApiContext;

import ch.qos.logback.classic.Logger;

/**
 * GoogleMapApi takes care of the interaction with GoogleMaps such as account,
 * keys, etc.
 * 
 * @author Andreas Neyer
 *
 *         Copyright 2017 afndev. All rights reserved.
 *
 */
public class GoogleMapApi {

	@SuppressWarnings("unused")
	private static final Logger log = (Logger) LoggerFactory.getLogger("app");

	private final static String apiKey = "AIzaSyCSgBJHB0XMVHlGaMrTgL-YO2_pHhPtuKc";

	private static final AppParamManager apmMgr = (AppParamManager) SpringApplicationContext.getBean("appParamManager");

	private static Integer maxCallsPerDay = new Integer(apmMgr.getVal("maxCallsPerDay", "MAP"));
	private static Integer maxCallsPerSecond = new Integer(apmMgr.getVal("maxCallsPerSecond", "MAP"));
	private static Integer numCallsToday = null;
	private static Date lastCall = null;

	private GeoApiContext context = null;

	public GoogleMapApi() {

		context = new GeoApiContext().setApiKey(apiKey);
		context.setQueryRateLimit(maxCallsPerSecond);

	};

	public static void updateCallData() {

		// if last call is yesterday, set it today and reset the number of calls
		// made today to zero
		Date lastCallDate = apmMgr.getDateVal("lastCall", "MAP");
		if (lastCallDate == null) {
			// lastCallDate = AfnDateUtil.dateYesterday();
		}

		numCallsToday = new Integer(apmMgr.getVal("callsToday", "MAP"));

		Date today = AfnDateUtil.dateToday();
		if (lastCallDate.before(today)) {
			lastCall = today;
			numCallsToday = 0;
		}

		// update call parameters
		numCallsToday++;
		apmMgr.setVal("callsToday", "MAP", Integer.toString(numCallsToday));
		apmMgr.setVal("lastCall", "MAP", lastCall);
		System.out.println("numCallsToday = " + numCallsToday);
		System.out.println("lastCall = " + lastCall);
	}

	public static boolean apiLimitReached() {

		if (numCallsToday == null || lastCall == null) {
			numCallsToday = 0;
			lastCall = new Date();
		}

		if (numCallsToday >= maxCallsPerDay) {
			return true;
		}
		return false;
	}

	public static Integer apiLimit() {
		return maxCallsPerDay;
	}

	public GeoApiContext getContext() {
		return context;
	}

}
