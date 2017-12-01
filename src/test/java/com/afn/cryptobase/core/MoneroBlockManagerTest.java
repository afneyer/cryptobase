package com.afn.cryptobase.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.afn.Application;
import com.afn.realstat.AppFiles;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("dev")
@WebAppConfiguration
public class MoneroBlockManagerTest {

	@Autowired
	MoneroBlockRepository mbRepo;
	
	@Autowired
	MoneroBlockManager mbMgr;

	@Test
	public void testNonceDistribution() {
	
		String fileName = "NonceDistribution.csv";
		File file = new File( AppFiles.getTestOutputDir() + "\\" + fileName);
		mbMgr.computeNonceDistribution(file);
		
	}
	
	@Test
	public void graphNonces() {
		Long firstBlock = 1000000L;
		Long lastBlock = mbRepo.findHeighestBlockNbr();
		List<Long> nonces = mbRepo.getNonces(firstBlock, lastBlock);
		
		NumberPattern np = new NumberPattern( nonces );
		
		BufferedImage im = np.drawPattern();

		String fileName = "MoneroBlockNoncePattern.png";
		File file = new File( AppFiles.getTestOutputDir() + "\\" + fileName);
		try {
			ImageIO.write(im, "png", file);
		} catch (IOException e) {
			fail();
		}
	}
	
	@Test
	public void noncesOverTime() {
		Long firstBlock = 1000000L;
		Long lastBlock = mbRepo.findHeighestBlockNbr();
		List<Long> nonces = mbRepo.getNonces(firstBlock, lastBlock);
		String fileName = "MoneroNonceByTime.csv";
		File file = new File( AppFiles.getTestOutputDir() + "\\" + fileName);
		try {
			FileWriter fw = new FileWriter(file);
			int index = 0;
			for (Long nonce : nonces) {
				Long limit = Math.round(Math.pow(2,22));
				if ( nonce < limit ) {
					String str = index + "," + nonce + "\n";
					index++;
					fw.write(str);
				}
			}
		} catch (IOException e) {
			fail();
		}
	}
	
	@Test
	public void graphNonceDistribution() {
		
		Long firstBlock = 1L;
		Long lastBlock = mbRepo.findHeighestBlockNbr();
		List<Long> nonces = mbRepo.getNonces(firstBlock, lastBlock);
		
		NumberPattern np = new NumberPattern( nonces );
		
		Map<Long,Long> map = np.computeNormalizedNonceDistribution();
		
		String fileName = "MoneroNonceDistribution.csv";
		File file = new File( AppFiles.getTestOutputDir() + "\\" + fileName);
		try {
			FileWriter fw = new FileWriter(file);
			for (Long key : map.keySet()) {
				String str = key + "," + map.get(key) + "\n";
				fw.write(str);
			}
		} catch (IOException e) {
			fail();
		}
	}
	
	@Test
	public void nonceBitStats() {
		Long firstBlock = 1L;
		Long lastBlock = mbRepo.findHeighestBlockNbr();
		List<Long> nonces = mbRepo.getNonces(firstBlock, lastBlock);
		
		NumberPattern np = new NumberPattern( nonces );
		String fileName = "MoneroNonceBitPattern.txt";	
		File file = new File( AppFiles.getTestOutputDir() + "\\" + fileName);
		
		np.printBitStats(np.computeBitStats(),file);
	}

}
