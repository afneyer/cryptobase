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

	@Test
	public void testMoneroDailyCreation() {

		// verify the MoneroHourly record gets created correctly
		LocalDateTime ldt = MoneroBlock.refBlockDateDay;
		MoneroDaily mh = new MoneroDaily(MoneroBlock.refBlockHourHourDaystamp);
		mh.saveOrUpdate();

		// retrieve the record again
		Long epochSeconds = MoneroHourly.toEpochSeconds(ldt);
		MoneroDailyRepository repo = MoneroDaily.getRepoStatic();
		mh = repo.findByStartTimestamp(epochSeconds);

		// verify time stamps
		assertEquals(epochSeconds, mh.getStartTimestamp());
		assertEquals(new Long(epochSeconds + 3600*24), mh.getEndTimestamp());

		// verify dates and times
		assertEquals(MoneroHourly.toLocalDateTime(epochSeconds), mh.getStartDateTime());
		assertEquals(mh.getStartDateTime().plusHours(24), mh.getEndDateTime());

		// update the record
		mh.setDifficulty(MoneroBlock.refBlockDifficulty+1);
		mh.saveOrUpdate();

		// retrieve the record again and verify field
		mh = repo.findByStartTimestamp(epochSeconds);
		assertEquals(new Long(MoneroBlock.refBlockDifficulty+1), mh.getDifficulty());

	}

	@Test(expected = RuntimeException.class)
	public void testInvalidRecordCreation() {

		// verify the an invalid MoneroHourly record does not get created
		LocalDateTime ldt = MoneroBlock.refBlockDateHour;
		MoneroHourly mh = new MoneroHourly(ldt.plusSeconds(1572));
		mh.saveOrUpdate();

	}

}
