package com.afn.cryptobase.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
	public void updateHourlyExchangeRatesForDb() {

		MoneroHourlyRepository mhRepo = MoneroHourly.getRepoStatic();
		Long timestamp = mhRepo.findEarliestIncompleteRecord();
		MoneroHourly mh = mhRepo.findByStartTimestamp(timestamp);

		while (timestamp != null) {
			
			updateHourlyExchangeRates(mh);
			mh.saveOrUpdate();
			
			timestamp = mhRepo.findEarliestIncompleteRecord();
			mh = mhRepo.findByStartTimestamp(timestamp);

		}
	}

	public void updateHourlyExchangeRate(String currencySymbol, Long startTimestamp, Long numHours) {

		MoneroHourlyRepository mhRepo = MoneroHourly.getRepoStatic();
		List<MoneroHourly> list = mhRepo.getMoneroHourlyList(startTimestamp, startTimestamp + numHours * 3600L);

		for (int l = 0; l < numHours; l++) {
			updateHourlyExchangeRate("USD", list);
			updateHourlyExchangeRate("BTC", list);
			list.get(l).saveOrUpdate();
		}

	}

	@Deprecated
	public static void updateHourlyExchangeRateOld(String currencySymbol, Long startTimestamp, Long numHours) {

		/*
		 * Note: The API returns hourly values up and including the
		 * startTimestamp and endTimestamp For this reason the startTimestamp
		 * needs to be increased by the number of hours
		 */
		Long periodSeconds = numHours * 3600L;
		startTimestamp = startTimestamp + periodSeconds;

		String timestampString = Long.toString(startTimestamp);

		String getData = apiGetData.replace("XXX", currencySymbol);
		getData = getData.replace("YYY", timestampString);
		getData = getData.replace("ZZZ", new Long(numHours).toString());

		JSONObject jsn0 = getApiResponseAsJson(apiEndpoint + getData);
		JSONArray jsnHourlyList;
		try {
			jsnHourlyList = (JSONArray) jsn0.get("Data");

			for (int i = 0; i < jsnHourlyList.length() - 1; i++) {
				JSONObject record = jsnHourlyList.getJSONObject(i);
				System.out.println(record);

				// get all the info from the record
				Long timestamp = new Long(record.getLong("time"));

				Double high = record.getDouble("high");
				Double low = record.getDouble("low");
				Double close = record.getDouble("close");
				Double open = record.getDouble("high");

				Double averageRate = (high + low + close + open) / 4.0;

				MoneroHourlyRepository mhRepo = MoneroHourly.getRepoStatic();
				MoneroHourly mh = mhRepo.findByStartTimestamp(timestamp);
				setExchangeRate(mh, currencySymbol, averageRate);
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
		default:
			throw new RuntimeException("Currency " + currencyCode + "is not implemented!");
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
			throw new RuntimeException("Error in httpReponse for URL " + ApiUrl, e);
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

	public void updateHourlyExchangeRates(MoneroHourly inOutMh) {
		updateHourlyExchangeRate("USD", inOutMh);
		updateHourlyExchangeRate("BTC", inOutMh);
	}

	public void updateHourlyExchangeRate(String currencySymbol, MoneroHourly inOutMh) {
		ArrayList<MoneroHourly> inOutList = new ArrayList<MoneroHourly>();
		inOutList.add(inOutMh);
		updateHourlyExchangeRate(currencySymbol, inOutList);
	}

	public void updateHourlyExchangeRate(String currencySymbol, List<MoneroHourly> mhList) {

		/*
		 * Note: The API returns hourly values up and including the
		 * startTimestamp and endTimestamp For this reason the startTimestamp
		 * needs to be increased by the number of hours
		 */
		Long periodSeconds = 1 * 3600L;
		Long startTimestamp = mhList.get(0).getStartTimestamp() + periodSeconds;

		String timestampString = startTimestamp.toString();

		String getData = apiGetData.replace("XXX", currencySymbol);
		getData = getData.replace("YYY", timestampString);
		getData = getData.replace("ZZZ", new Long(1L).toString());

		JSONObject jsn0 = getApiResponseAsJson(apiEndpoint + getData);
		JSONArray jsnHourlyList;
		try {
			jsnHourlyList = (JSONArray) jsn0.get("Data");

			for (int i = 0; i < jsnHourlyList.length() - 1; i++) {
				JSONObject record = jsnHourlyList.getJSONObject(i);
				System.out.println(record);

				// get all the info from the record
				Long timestamp = new Long(record.getLong("time"));

				Double high = record.getDouble("high");
				Double low = record.getDouble("low");
				Double close = record.getDouble("close");
				Double open = record.getDouble("high");

				Double averageRate = (high + low + close + open) / 4.0;

				setExchangeRate(mhList.get(i), currencySymbol, averageRate);

				timestamp += 3600L;

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}