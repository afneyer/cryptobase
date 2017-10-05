package com.afn.cryptobase.core;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.assertj.core.util.Files;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.afn.realstat.AppFiles;

@Component
public class ContractFileParser {

	@Autowired
	ContractRepository cRepo;
	ContractRevenueRepository crRepo;

	public static String contractFileDir = AppFiles.getTestDataDir() + "\\contracts";

	public void updateContracts() {

		File dir = new File(contractFileDir);
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
			for (File file : directoryListing) {
				updateContract(file);
			}
		} else {
			throw new RuntimeException("Cannot retrieve contract directory files " + contractFileDir);
		}
	}

	public Contract updateContract(File file) {
		if (file.getName().startsWith("Genesis")) {
			System.out.println("Processing file = " + file.getPath() + "\\" + file.getName());
			return updateGenesisContract(file);
		}
		return null;
	}

	private Contract updateGenesisContract(File file) {

		String company = "Genesis";

		Contract contract = null;

		List<String> lines = Files.linesOf(file, Charset.defaultCharset());
		for (String line : lines) {
			if (line.startsWith(company)) {

				// process contract line
				String[] fields = line.split(",");
				String cmp = fields[0];
				assertEquals(company, cmp);

				
				LocalDateTime startDateTime = LocalDateTime.parse(fields[1]);
				Long duration = new Long(fields[2]);
				Long hashrate = new Long(fields[3]);
				String algorithm = fields[4];
				String revenueCurrency = fields[5];
				String type = fields[6];
				Double price = new Double(fields[7]);
				String priceCurrency = fields[8];

				// TODO
				// ensure consistency of file name
				
				contract = new Contract(company, startDateTime, duration, hashrate, algorithm, revenueCurrency, type,
						price, priceCurrency);
				contract.saveOrUpdate();


			} else {

				Double revenue;
				LocalDateTime revTime;
				String revenueCurrency;

				// process lines for the first 30 days while no pay-out
				if (line.contains("added to your account")) {
					String[] fields = line.split(" ");

					revenue = new Double(fields[4]);
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'+00:00'");
					revTime = LocalDateTime.parse(fields[10],formatter);
					
					// the times seem to be wrong e.g 2017-08-26T12:00:00+00:00 seems to really mean
					// 2017-09-25 otherwise there a double payouts on the day it switches from credit card payouts to regular payouts
					revTime = MoneroBlock.roundDownToDay(revTime);
					revTime = revTime.minusDays(1);
					
					
					revenueCurrency = fields[1];

				} else {
					String[] fields = line.split("\t");

					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.HH.mm.ss");
					String dateTimeStr = fields[0].replace(" ","");
					dateTimeStr += ".00.00.00";
					revTime = LocalDateTime.parse(dateTimeStr, formatter);

					revenueCurrency = fields[1].replaceAll(" ", "");
				
					String revStr = fields[2].replaceAll("[^\\d.]", "");
					revenue = new Double(revStr);
					ContractRevenue contractRev = new ContractRevenue(contract, revTime, revenue, revenueCurrency);
					contractRev.saveOrUpdate();
				}

				ContractRevenue contractRev = new ContractRevenue(contract, revTime, revenue, revenueCurrency);
				contractRev.saveOrUpdate();
			}

		}
		return contract;

	}

}