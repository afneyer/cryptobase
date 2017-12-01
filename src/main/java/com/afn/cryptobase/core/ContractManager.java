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
import com.afn.realstat.CsvFileWriter;

@Component
public class ContractManager {

	@Autowired
	ContractRepository cRepo;
	
	@Autowired
	ContractRevenueRepository crRepo;
	
	@Autowired
	MoneroDailyRepository mdRepo;

	

	public String[][] actualVersusTargetRevenue(Contract contract) {
		
		List<ContractRevenue> revItems = crRepo.findByContract(contract);
		
		int numRevItems = revItems.size();
		
		String[][] result = new String[3][numRevItems];
		
		for (int i=0;  i<numRevItems; i++ ) {
			result[0][i] = revItems.get(i).getStartDateTime().toString();
			result[1][i] = revItems.get(i).getRevenue().toString();
			
			// get MoneroDaily
			MoneroDaily md = mdRepo.findByStartDayTime(revItems.get(i).getStartDateTime());
			
			// target return = globalHashRate / contractHashRate * dailyReward;
			Double target = new Double(contract.getHashRate()) / new Double(md.getHashRate()) * new Double(md.getRewardXmr());
			result[2][i] = target.toString();
		}
		return result;
	}
	
	public void arrayToFile(String[][] array, File file) {
		
		String[] header = {"dateTime","actual","target"};
		int size = array[0].length;
		CsvFileWriter cfw = new CsvFileWriter(file, header );
		for (int i = 0; i<size; i++) {
			String str = array[0][i] + "," + array[1][i] + "," + array[2][i];
			cfw.appendLine(str);
		}
		cfw.close();
	}

	

}