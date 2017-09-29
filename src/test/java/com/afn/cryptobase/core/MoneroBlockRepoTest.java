package com.afn.cryptobase.core;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.afn.Application;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("dev")
@WebAppConfiguration
@Component
public class MoneroBlockRepoTest {

	@Autowired
	MoneroBlockRepository mbRepo;

	@Test 
	public void testFindMostRecentBlock() {
		
		Long timestamp = MoneroBlock.refBlockTimestamp;
		
		Long blkNbr = mbRepo.findMostRecentBlockNbrBefore(timestamp);
		assertEquals(new Long(MoneroBlock.refBlockNbr-1),blkNbr);
		
		blkNbr = mbRepo.findMostRecentBlockBefore(timestamp).getBlockNbr();
		assertEquals(new Long(MoneroBlock.refBlockNbr-1),blkNbr);
	
	}
			
}
