package com.afn.cryptobase.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.afn.realstat.Application;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("dev")
@WebAppConfiguration
public class MoneroBlockApiTest {

	@Autowired
	MoneroBlockTest repo;

	@Test
	public void testApiGetBlockData() {
		MoneroBlock blk = MoneroBlockApi.getBlock(MoneroBlock.refBlockNbr);

		assertEquals(blk.getBlockNbr(), MoneroBlock.refBlockNbr);
		assertEquals(blk.getTimestamp(), MoneroBlock.refBlockTimestamp);
		assertEquals(blk.getDateTime().toString(), MoneroBlock.refBlockDateTime);
		assertEquals(blk.getDifficulty(), MoneroBlock.refBlockDifficulty);
		assertEquals(blk.getHashRate(), new Long(MoneroBlock.refBlockDifficulty / MoneroBlock.getBlockTime()));
		assertEquals(blk.getBlockDate(), MoneroBlock.refBlockDateTime.toLocalDate());
		assertEquals(blk.getReward(), MoneroBlock.refBlockReward);
	}

	@Test
	public void testFillMoneroDBRecent() {
		MoneroBlockApi.fillMoneroBlockDbRecent();
	}

	@Test
	public void testFillMoneroDbHistorical() {
		MoneroBlockApi.fillMoneroHistorical();
	}

	@Test
	public void testUpdateReferenceBlock() {
		MoneroBlock blk = MoneroBlockApi.getBlock(MoneroBlock.refBlockNbr);
		blk.saveOrUpdate();
	}

	@Test
	public void testGetMostRecentBlockNbr() {

		// Verify block is more recent than reference block
		long blkNbr = MoneroBlockApi.getMostRecentBlockNbr();
		assertTrue(blkNbr > MoneroBlock.refBlockNbr);
		System.out.println("Most Recent Block :" + blkNbr);

		// Get current time in UTC
		LocalDateTime nowUTC = LocalDateTime.now(ZoneId.of("UTC"));
		// Verify that block was created before the current time
		MoneroBlock blk = MoneroBlockApi.getBlock(blkNbr);
		LocalDateTime ldtBlk = blk.getDateTime();
		System.out.println("Most Recent Block Time : " + ldtBlk);
		assertTrue(nowUTC.isAfter(ldtBlk));

		// Verify the block was created within blockTimeTolerance * blockTime
		// creation target
		long blockTimeTolerance = 10;
		LocalDateTime nowMinus3BlockTimes = nowUTC.minusSeconds(MoneroBlock.getBlockTime() * blockTimeTolerance);
		assertTrue(nowMinus3BlockTimes.isBefore(ldtBlk));

	}

	@Test
	public void testFirstBlockTimes() {
		MoneroBlock blk = MoneroBlockApi.getBlock(1L);
		assertTrue(blk.getDateTime().equals(MoneroBlock.firstBlockDateTime));
		assertTrue(blk.getDateHour().equals(MoneroBlock.firstBlockHour));
	}
}
