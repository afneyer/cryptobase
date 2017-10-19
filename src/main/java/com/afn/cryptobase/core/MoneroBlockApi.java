package com.afn.cryptobase.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MoneroBlockApi {

	private static String ApiEndpoint = "http://moneroblocks.info/api/";
	private static String ApiGetBlockData = "get_block_header/";

	@Autowired
	private MoneroBlockRepository mbRepo;

	public MoneroBlock getBlock(Long blkNbr) {

		String blkNbrStr = Long.toString(blkNbr);

		JSONObject jsn0 = getApiResponseAsJson(ApiEndpoint + ApiGetBlockData + blkNbrStr);
		JSONObject jsn1;
		MoneroBlock blk = null;
		try {
			jsn1 = (JSONObject) jsn0.get("block_header");
			Long timestamp = jsn1.getLong("timestamp");

			blk = new MoneroBlock(blkNbr, timestamp);

			blk.setDifficulty(jsn1.getLong("difficulty"));
			blk.setOrphanStatus(jsn1.getBoolean("orphan_status"));
			blk.setReward(jsn1.getLong("reward"));
			blk.setSize(jsn1.getLong("block_size"));
			blk.setNonce(jsn1.getLong("nonce"));
			blk.setHash(jsn1.getString("hash"));
			blk.setStatus(jsn0.getString("status"));
		} catch (JSONException e) {
			throw new RuntimeException("Cannot find field in MoneroBlockApi " + jsn0.toString() + e);
		}

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

	public void fillMoneroBlockDbRecent() {
		Long startBlockNbr = getMostRecentBlockNbr();
		Long endBlockNbr = MoneroBlock.refBlockNbr;
		fillMoneroDb(startBlockNbr, endBlockNbr);
	}

	public void fillMoneroHistorical() {
		Long startBlockNbr = MoneroBlock.refBlockNbr;
		Long endBlockNbr = 1L;
		fillMoneroDb(startBlockNbr, endBlockNbr);
	}

	public void fillMoneroDb() {
		Long startBlockNbr = MoneroBlock.refBlockNbr;
		Long endBlockNbr = 1L;
		fillMoneroDb(startBlockNbr, endBlockNbr);
	}
	
	public void updateNonceAndHash() {
		Long startBlockNbr = mbRepo.getMostRecentBlockNbrWithoutNonce();
		Long endBlockNbr = 1L;
		for (Long l = startBlockNbr; l>= endBlockNbr; l-- ) {
			MoneroBlock blk = this.getBlock(l);
			blk.saveOrUpdate();
		}
	}

	public Long getMostRecentBlockNbr() {
		Long blkNbr = 0L;
		JSONObject jsn = getApiResponseAsJson(ApiEndpoint + "get_stats");
		try {
			blkNbr = jsn.getLong("height");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return blkNbr;
	}

	private void fillMoneroDb(Long startBlockNbr, Long endBlockNbr) {

		Long batchSize = 1000L;
		Long startBatch = (startBlockNbr / batchSize) + 1;
		Long endBatch = (endBlockNbr / batchSize) + 1;

		for (Long l = startBatch; l >= endBatch; l--) {
			Long startIndex = (l - 1L) * batchSize + 1L;
			Long endIndex = startIndex + batchSize - 1L;
			endIndex = Math.min(endIndex, startBlockNbr);

			ArrayList<Long> blkList = mbRepo.getBlockNumbers(startIndex, endIndex);
			
			// check whether the batch has missing records
			if (blkList.size() < endIndex - startIndex + 1) {
				
				for (Long k = startIndex; k <= endIndex; k++) {
					if (!blkList.contains(k)) {
						MoneroBlock blk = getBlock(k);
						blk.saveOrUpdate();
						System.out.println("Created Block " + k);
					}
				}
				
			}
		}

	}

	public Long countMissingBlocks(Long highBlockNbr) {
		Long count = mbRepo.count();
		Long high = mbRepo.findHeighestBlockNbr();
		return high - count;
	}
	
	public boolean isComplete() {
		return this.countMissingBlocks(mbRepo.findHeighestBlockNbr()).equals(0L);
	}
	
	public boolean isUpdated() {
		Long mostRecentBlockId = getMostRecentBlockNbr();
		Long mostRecentBlockDb= mbRepo.findHeighestBlockNbr();
		return mostRecentBlockId.equals(mostRecentBlockDb);
	}

}