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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MoneroHourlyApi {

	private static String apiEndpoint = "https://min-api.cryptocompare.com/data/histohour";
	private static String apiGetData = "?fsym=XMR&tsym=XXX&limit=ZZZ&aggregate=0&toTs=YYY";
	
	String sTemp = "https://min-api.cryptocompare.com/data/histohour?fsym=XMR&tsym=BTC&limit=60&aggregate=0&toTs=1452680400&extraParams=your_app_name";
	
	
	@Autowired
	MoneroHourlyRepository mhRepo;
	
	/*
	 * This function assumes that the hourly records are already created
	 */
	public void updateHourlyExchangeRates() {
		
		MoneroHourlyRepository mhRepo = MoneroHourly.getRepoStatic();
		Long startTimestamp = mhRepo.findEarliestRecordTimestamp();
		Long endTimestamp = mhRepo.findLatestRecodTimestamp();
		Long batchSizeDays = 10L;
		Long batchSizeSeconds = batchSizeDays*24*3600L;
		
		Long timestamp = startTimestamp;
		
		while (timestamp + batchSizeSeconds <= endTimestamp) {
			updateHourlyExchangeRate("USD", timestamp, batchSizeDays );
			updateHourlyExchangeRate("BTC", timestamp, batchSizeDays );
			timestamp += batchSizeSeconds;
		}
		
		Long remainingHours = endTimestamp - timestamp + 1L;
		updateHourlyExchangeRate("USD",timestamp, remainingHours);
		updateHourlyExchangeRate("BTC",timestamp, remainingHours);
	}
	
	public static void updateHourlyExchangeRate( String currencySymbol, Long startTimestamp, Long numDays ) {

		/*
		 * Note: The API returns hourly values up and including the startTimestamp and endTimestamp
		 *       For this reason the startTimestamp needs to be increased by the number of hours
		 */
		Long periodSeconds = numDays * 24L * 3600L;
		startTimestamp = startTimestamp + periodSeconds;
		
		String timestampString = Long.toString(startTimestamp);
		
		String getData = apiGetData.replace("XXX",currencySymbol);
		getData = getData.replace("YYY",timestampString);
		getData = getData.replace("ZZZ", new Long(numDays*24L).toString() );

		JSONObject jsn0 = getApiResponseAsJson(apiEndpoint+getData);
		JSONArray jsnHourlyList;
		try {
			jsnHourlyList = (JSONArray) jsn0.get("Data");
			
			for (int i = 0; i<jsnHourlyList.length()-1 ; i++) {
				JSONObject record = jsnHourlyList.getJSONObject(i);
				System.out.println(record);
				
				// get all the info from the record
				Long timestamp = new Long(record.getLong("time"));
				
				Double high = record.getDouble("high");
				Double low = record.getDouble("low");
				Double close = record.getDouble("close");
				Double open = record.getDouble("high");
				
				Double averageRate = (high+low+close+open)/4.0;
				
				MoneroHourlyRepository mhRepo = MoneroHourly.getRepoStatic();
				MoneroHourly mh = mhRepo.findByStartTimestamp(timestamp);
				setExchangeRate(mh,currencySymbol,averageRate);		
				mh.saveOrUpdate();
				
				timestamp += 3600L;
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
	}
	
	private static void setExchangeRate(MoneroHourly mh, String currencyCode, Double value) {
		switch (currencyCode) {
			case "USD":  
				mh.setExchangeUSD(value);
				break;
			case "BTC":  
				mh.setExchangeBTC(value);
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