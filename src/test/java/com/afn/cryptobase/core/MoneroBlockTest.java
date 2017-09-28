package com.afn.cryptobase.core;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.afn.Application;
import com.querydsl.core.types.Predicate;

@RunWith(SpringRunner.class)
// @SpringBootTest(classes = Application.class, webEnvironment =
// SpringBootTest.WebEnvironment.NONE)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("dev")
@WebAppConfiguration
@Component
public class MoneroBlockTest {

	@Autowired
	MoneroBlockRepository repo;

	@Test 
	public void createMoneroHourly() {
		
		LocalDateTime startTime = MoneroBlock.refBlockDateHour;
		LocalDateTime endTime = startTime.plusSeconds(3600-1);
		MoneroHourly mh = new MoneroHourlyManager().createMoneroHourly(startTime);
		
		Long averageDifficulty = 31990172403L;
		assertEquals(averageDifficulty, mh.getDifficulty());
		
		Long totalReward = 177766510616923L;
		assertEquals(totalReward, mh.getReward());
		
		
		Long firstBlock = mh.getFirstBlockNbr();
		assertEquals(new Long(1398315),firstBlock);
		Long lastBlock = mh.getLastBlockNbr();
		assertEquals(new Long(1398315),firstBlock);
		
		Long numBlocks = lastBlock + 1 - firstBlock;
		assertEquals(new Long(27),numBlocks);
		
		for (Long l=firstBlock; l<=lastBlock; l++) {
			MoneroBlock blk = repo.findByBlockNbr(l);
			assertTrue( startTime.minusSeconds(1).isBefore(blk.getDateTime()));
			assertTrue( endTime.plusSeconds(1).isAfter(blk.getDateTime()));
		}
		
		mh.saveOrUpdate();
		
	}
	
	

	
}
