package com.afn.cryptobase.core;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.afn.Application;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("dev")
@WebAppConfiguration
public class MoneroHourlyTest {

	@Test
	public void testMoneroHourlyCreation() {

		// verify the MoneroHourly record gets created correctly
		LocalDateTime ldt = MoneroBlock.refBlockDateHour;
		MoneroHourly mh = new MoneroHourly(ldt);
		mh.saveOrUpdate();

		// retrieve the record again
		Long epochSeconds = MoneroHourly.toEpochSeconds(ldt);
		MoneroHourlyRepository repo = MoneroHourly.getRepoStatic();
		mh = repo.findByStartTimestamp(epochSeconds);

		// verify time stamps
		assertEquals(epochSeconds, mh.getStartTimestamp());
		assertEquals(new Long(epochSeconds + 3600), mh.getEndTimestamp());

		// verify dates and times
		assertEquals(MoneroHourly.toLocalDateTime(epochSeconds), mh.getStartDateTime());
		assertEquals(mh.getStartDateTime().plusHours(1), mh.getEndDateTime());

		// update the record
		Long difficulty = MoneroBlock.refBlockDifficulty + 1;
		mh.setDifficulty(difficulty);
		mh.saveOrUpdate();

		// retrieve the record again and verify field
		mh = repo.findByStartTimestamp(epochSeconds);
		assertEquals(difficulty, mh.getDifficulty());

	}

	@Test(expected = RuntimeException.class)
	public void testInvalidRecordCreation() {

		// verify the an invalid MoneroHourly record does not get created
		LocalDateTime ldt = MoneroBlock.refBlockDateHour;
		MoneroHourly mh = new MoneroHourly(ldt.plusSeconds(1572));
		mh.saveOrUpdate();

	}

}
