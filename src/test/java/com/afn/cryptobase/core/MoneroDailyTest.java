package com.afn.cryptobase.core;

import static org.junit.Assert.assertEquals;

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
public class MoneroDailyTest {
	
	@Autowired
	MoneroDailyRepository mdRepo;

	@Test
	public void testMoneroDailyCreation() {

		// verify the MoneroHourly record gets created correctly
		LocalDateTime ldt = MoneroBlock.refBlockDateDay;
		MoneroDaily md = new MoneroDaily(ldt);
		md.saveOrUpdate();

		// retrieve the record again
		md = mdRepo.findByStartTime(ldt);

		// verify time stamps
		assertEquals(MoneroBlock.refBlockDateDay, md.getStartTime());
		assertEquals(MoneroBlock.refBlockDateDay.plusDays(1), md.getEndTime());

		// update the record
		md.setDifficulty(MoneroBlock.refBlockDifficulty+1);
		md.saveOrUpdate();

		// retrieve the record again and verify field
		md = mdRepo.findByStartTime(MoneroBlock.refBlockDateDay);
		assertEquals(new Long(MoneroBlock.refBlockDifficulty+1), md.getDifficulty());

	}

	@Test(expected = RuntimeException.class)
	public void testInvalidRecordCreation() {

		// verify the an invalid MoneroHourly record does not get created
		LocalDateTime ldt = MoneroBlock.refBlockDateHour;
		MoneroHourly mh = new MoneroHourly(ldt.plusSeconds(5572));
		mh.saveOrUpdate();

	}

}
