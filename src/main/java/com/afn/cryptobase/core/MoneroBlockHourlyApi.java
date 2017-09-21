package com.afn.cryptobase.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;

public class MoneroBlockHourlyApi {

	private static String apiEndpoint = "https://min-api.cryptocompare.com/data/histohour";
	private static String apiGetData = "?fsym=XMR&tsym=XXX&limit=60&aggregate=0&toTs=YYY";

	String sTemp = "https://min-api.cryptocompare.com/data/histohour?fsym=XMR&tsym=BTC&limit=60&aggregate=0&toTs=1452680400&extraParams=your_app_name";

	public static void updateHourlyUSD(String currencySymbol, Long startTimestamp, Long numHours) {

		String timestampString = Long.toString(startTimestamp);

		String getData = apiGetData.replace("XXX", currencySymbol);
		getData = getData.replace("YYY", timestampString);

		JSONObject jsn0 = getApiResponseAsJson(apiEndpoint + getData);
		JSONObject jsn1 = (JSONObject) jsn0.get("block_header");

		@SuppressWarnings("unchecked")
		String jsnHourlyList = (String) jsn0.get("Data");

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
			throw new RuntimeException("Error in httpReponse for URL " + ApiUrl, e);
		}

		System.out.println("Sending get request : " + url);
		System.out.println("Response code : " + responseCode);
		System.out.println("Response message : " + responseMessage);
		System.out.println("Response content :" + msgContent);

		jsn = new JSONObject(msgContent.toString());

		return jsn;
	}

}