package com.afn.cryptobase.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MoneroHourlyApi {

	private static String apiEndpoint = "https://min-api.cryptocompare.com/data/histohour";
	private static String apiGetData = "?fsym=XMR&tsym=XXX&limit=ZZZ&aggregate=0&toTs=YYY";
	
	String sTemp = "https://min-api.cryptocompare.com/data/histohour?fsym=XMR&tsym=BTC&limit=60&aggregate=0&toTs=1452680400&extraParams=your_app_name";
	
	
	
	
	
	/*
	 * This function assumes that the hourly records are already created
	 */
	public static void updateHourlyExchangeRates() {
		
		// update recent ones
		MoneroHourlyRepository mhRepo = MoneroHourly.getRepoStatic();
		Long mh = mhRepo.findEarliestRecordTimestamp();
		Long numDays = 10L;
		while (mh != null) {
			updateHourlyExchangeRate("USD", new Long(mh-3600L), numDays );
			updateHourlyExchangeRate("BTC", new Long(mh-3600L), numDays );
		}
	}
	
	public static void updateHourlyExchangeRate( String currencySymbol, Long startTimestamp, Long numHours ) {

		String timestampString = Long.toString(startTimestamp);
		
		String getData = apiGetData.replace("XXX",currencySymbol);
		getData = getData.replace("YYY",timestampString);
		getData = getData.replace("ZZZ", new Long(numHours*24L).toString() );

		JSONObject jsn0 = getApiResponseAsJson(apiEndpoint+getData);
		
		JSONArray jsnHourlyList;
		try {
			jsnHourlyList = (JSONArray) jsn0.get("Data");
			Long previousTimestamp = 0L;
			
			for (int i = 0; i<jsnHourlyList.length()-1 ; i++) {
				JSONObject record = jsnHourlyList.getJSONObject(i);
				System.out.println(record);
				
				// get all the info from the record
				Long timestamp = new Long(record.getLong("time"));
				if (previousTimestamp == 0L) {
					previousTimestamp = timestamp - 3600L;
				}
				
				if (timestamp != previousTimestamp + 3600L) {
					System.out.println("Computing time stamp at " + MoneroBlock.toLocalDateTime(timestamp) + " as average");
				}
				previousTimestamp = timestamp;
				
				Double high = record.getDouble("high");
				Double low = record.getDouble("low");
				Double close = record.getDouble("close");
				Double open = record.getDouble("high");
				
				Double averageRate = (high+low+close+open)/4.0;
				
				MoneroHourlyRepository mhRepo = MoneroHourly.getRepoStatic();
				MoneroHourly mh = mhRepo.findByStartTimestamp(timestamp);
				setExchangeRate(mh,currencySymbol,averageRate);
				
				mh.saveOrUpdate();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
	}
	
	private static void setExchangeRate(MoneroHourly mh, String currencyCode, Double value) {
		switch (currencyCode) {
			case "USD":  mh.setExchangeUSD(value);
			break;
			case "BTC":  mh.setExchangeBTC(value);
			break;
			default: throw new RuntimeException("Currency " + currencyCode + "is not implemented!");
		}

	}

	private static JSONObject getApiResponseAsJson(String ApiUrl) {

		JSONObject jsn = null;

		URL url;
		try {
			url = new URL(ApiUrl);
		} catch (MalformedURLException e) {
			throw new RuntimeException("MalformedURLException in MoneroBlockApi for URL " + ApiUrl, e);
		}

		HttpURLConnection con = null;
		int responseCode = 0;
		String responseMessage = new String();
		StringBuffer msgContent = new StringBuffer();
		try {
			con = (HttpURLConnection) url.openConnection();
			// By default it is GET request
			con.setRequestMethod("GET");

			responseCode = con.getResponseCode();
			responseMessage = con.getResponseMessage();

			BufferedReader inStream = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			msgContent = new StringBuffer();
			while ((inputLine = inStream.readLine()) != null) {
				msgContent.append(inputLine);
			}
			inStream.close();

		} catch (IOException e) {
			throw new RuntimeException( "Error in httpReponse for URL " + ApiUrl, e);
		}

		System.out.println("Sending get request : " + url);
		System.out.println("Response code : " + responseCode);
		System.out.println("Response message : " + responseMessage);
		System.out.println("Response content :" + msgContent);

		try {
			jsn = new JSONObject(msgContent.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return jsn;
	}


	
	
	
	
	
		
	
	
	

}