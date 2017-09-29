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
public class MoneroHourlyManagerTest {

	@Autowired
	MoneroHourlyRepository mhRepo;
	
	@Autowired
	MoneroHourlyManager mhMgt;

	@Test
	public void testMoneroHourlyCreation() {

		// verify the MoneroHourly record gets created correctly
		LocalDateTime ldt = MoneroBlock.refBlockDateHour;
		MoneroHourly mh = new MoneroHourly(ldt);
		mh.saveOrUpdate();

		// retrieve the record again
		Long epochSeconds = MoneroHourly.toEpochSeconds(ldt);
		mh = mhRepo.findByStartTimestamp(epochSeconds);

		// verify time stamps
		assertEquals(epochSeconds, mh.getStartTimestamp());
		assertEquals(new Long(epochSeconds + 3600), mh.getEndTimestamp());

		// verify dates and times
		assertEquals(MoneroHourly.toLocalDateTime(epochSeconds), mh.getStartDateTime());
		assertEquals(mh.getStartDateTime().plusHours(1), mh.getEndDateTime());

		// update the record
		mh.setDifficulty(new Long(15));
		mh.saveOrUpdate();

		// retrieve the record again and verify field
		mh = mhRepo.findByStartTimestamp(epochSeconds);
		assertEquals(new Long(15), mh.getDifficulty());

	}

	@Test(expected = RuntimeException.class)
	public void testInvalidRecordCreation() {
		
		// verify the an invalid MoneroHourly record does not get created
		LocalDateTime ldt = MoneroBlock.refBlockDateHour;
		MoneroHourly mh = new MoneroHourly(ldt.plusSeconds(1572));
		mh.saveOrUpdate();

	}
	
	@Test
	public void fillMoneroHourlyDb() {
		// new MoneroHourlyManager().fillMoneroHourlyDb();
		mhMgt.fillMoneroHourlyDb();
	}
	
	@Test
	public void testMissingRecords() {
		assertEquals(new Long(0), mhMgt.countMissingRecords() );
	}
	
	@Test
	public void removeInvalidRecords() {
		mhMgt.removeInvalidRecords();
		assertTrue( mhMgt.isComplete() );
	}
}
