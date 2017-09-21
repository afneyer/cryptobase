package com.afn.cryptobase.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;

public class MoneroBlockApi {

	private static String ApiEndpoint = "http://moneroblocks.info/api/";
	private static String ApiGetBlockData = "get_block_header/";

	public static MoneroBlock getBlock(Long blkNbr) {

		String blkNbrStr = Long.toString(blkNbr);

		JSONObject jsn0 = getApiResponseAsJson(ApiEndpoint + ApiGetBlockData + blkNbrStr);
		JSONObject jsn1 = (JSONObject) jsn0.get("block_header");

		Long timestamp = jsn1.getLong("timestamp");

		MoneroBlock blk = new MoneroBlock(blkNbr, timestamp);

		blk.setDifficulty(jsn1.getLong("difficulty"));
		blk.setOrphanStatus(jsn1.getBoolean("orphan_status"));
		blk.setReward(jsn1.getLong("reward"));
		blk.setSize(jsn1.getLong("block_size"));
		blk.setStatus(jsn0.getString("status"));

		return blk;
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

		jsn = new JSONObject(msgContent.toString());

		return jsn;
	}

	public static void fillMoneroBlockDbRecent() {
		Long startIndex = getMostRecentBlockNbr();
		
		// get repository
		MoneroBlock refBlk = MoneroBlockApi.getBlock(MoneroBlock.refBlockNbr);
		MoneroBlockRepository repo = refBlk.getRepo();
		Long endIndex = repo.findHeighestBlockNbr();

		for (Long l = startIndex; l > endIndex; l--) {
			MoneroBlock blk = MoneroBlockApi.getBlock(l);
			blk.saveOrUpdate();
			System.out.println("Created or Updated Block " + l);
		}
	}

	public static Long getMostRecentBlockNbr() {
		Long blkNbr = 0L;
		JSONObject jsn = getApiResponseAsJson(ApiEndpoint + "get_stats");
		blkNbr = jsn.getLong("height");
		return blkNbr;
	}
	
	
	
	public static void fillMoneroHistorical() {
		
		Long refBlockNbr = MoneroBlock.refBlockNbr;
		Long startIndex = refBlockNbr;
	
		// get repository
		MoneroBlock refBlk = MoneroBlockApi.getBlock(MoneroBlock.refBlockNbr);
		MoneroBlockRepository repo = refBlk.getRepo();
		
		Long minBlkNbr = repo.findLowestBlockNbr();
		if (minBlkNbr != null) {
			startIndex = minBlkNbr-1;
		}
		
		Long endIndex = 0L;

		for (Long l = startIndex; l > endIndex; l--) {
			MoneroBlock blk = MoneroBlockApi.getBlock(l);
			blk.save();
			System.out.println("Created or Updated Block " + l);
		}
		
	}
	
	

}