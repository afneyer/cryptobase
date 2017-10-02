package com.afn.cryptobase.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.afn.Application;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("dev")
@WebAppConfiguration
public class MoneroDailyManagerTest {

	@Autowired
	MoneroDailyRepository mdRepo;
	
	@Autowired
	MoneroDailyManager mdMgr;

	@Test
	public void testMoneroDailyCreation() {

		// verify the MoneroHourly record gets created correctly
		LocalDateTime ldt = MoneroBlock.refBlockDateDay;
		MoneroDaily md = new MoneroDaily(ldt);
		md.saveOrUpdate();

		// retrieve the record again
		md = mdRepo.findByStartDayTime(ldt);

		// verify date and times
		assertEquals(ldt, md.getStartDayTime());
		assertEquals(ldt.plusDays(1), md.getEndDayTime());

		// update the record
		Long difficulty = MoneroBlock.refBlockDifficulty + 1L;
		md.setDifficulty(difficulty);
		md.saveOrUpdate();

		// retrieve the record again and verify field
		md = mdRepo.findByStartDayTime(ldt);
		assertEquals(difficulty, md.getDifficulty());

	}

	@Test(expected = RuntimeException.class)
	public void testInvalidRecordCreation() {
		
		// verify the an invalid MoneroHourly record does not get created
		LocalDateTime ldt = MoneroBlock.refBlockDateHour;
		MoneroHourly mh = new MoneroHourly(ldt.plusSeconds(1572));
		mh.saveOrUpdate();

	}
	
	@Test
	public void fillMoneroDailyDb() {
		// new MoneroHourlyManager().fillMoneroHourlyDb();
		mdMgr.fillMoneroDailyDb();
	}
	
	@Test
	public void testMissingRecords() {
		assertEquals(new Long(0), mdMgr.countMissingRecords() );
	}
	
	@Test
	public void testRemoveInvalidRecords() {
		mdMgr.removeInvalidRecords();
		assertTrue( mdMgr.isComplete() );
	}
	
}
