package com.afn.cryptobase.core;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.afn.realstat.AbstractEntityManager;
import com.afn.realstat.CsvFileWriter;

@Component
public class MoneroBlockManager extends AbstractEntityManager<MoneroBlock> {

	public static final Logger log = LoggerFactory.getLogger("import");

	@Autowired
	MoneroBlockRepository repo;

	public MoneroBlockManager(MoneroBlockRepository rpRepo) {
		repo = rpRepo;
	}
	
	public void computeNonceDistribution(File file) {
		
		int numPoints = 1000;
		Long[] distribution = new Long[numPoints];
		for (int i=0; i<numPoints; i++) {
			distribution[i] = new Long(0);
		}
		
		Long lastBlock = repo.findHeighestBlockNbr();
		
		Long highNonce = repo.findHighestNonce();
		long interval = highNonce / numPoints + 1;
		
		
		long batchSize = 5000;
		long index = 0;
		
		while (index <= lastBlock) {
			
			long lmax = Math.min(index+batchSize, lastBlock.longValue());
			
			List<Long> nonces = repo.getNonces(index, lmax);
			
			for (Long nonce : nonces) {
				int nonceIndex = (int) (nonce / interval);
				distribution[nonceIndex]++;
			}
	
			System.out.println("Processed " + index + "nonces");
			index += batchSize;
			
		}
		
		// Write to File		
		String[] header = {"range","numberOfNonces"};
		
		CsvFileWriter cfw = new CsvFileWriter(file, header );
		for (int i = 0; i<numPoints; i++) {
			String str = new Long(i) * interval + "," + distribution[i];
			cfw.appendLine(str);
		}
		cfw.close();

		
	}
}
