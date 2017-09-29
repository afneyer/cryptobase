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

import com.afn.Application;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("dev")
@WebAppConfiguration
public class MoneroBlockApiTest {

	@Autowired
	MoneroBlockApi mbApi;

	@Autowired
	MoneroBlockRepository mbRepo;

	@Test
	public void testApiGetBlockData() {
		MoneroBlock blk = mbApi.getBlock(MoneroBlock.refBlockNbr);

		assertEquals(blk.getBlockNbr(), MoneroBlock.refBlockNbr);
		assertEquals(blk.getTimestamp(), MoneroBlock.refBlockTimestamp);
		assertEquals(blk.getDateTime(), MoneroBlock.refBlockDateTime);
		assertEquals(blk.getDifficulty(), MoneroBlock.refBlockDifficulty);
		assertEquals(blk.getHashRate(), new Long(MoneroBlock.refBlockDifficulty / MoneroBlock.getBlockTime()));
		assertEquals(blk.getBlockDate(), MoneroBlock.refBlockDateTime.toLocalDate());
		assertEquals(blk.getReward(), MoneroBlock.refBlockReward);
	}

	@Test
	public void testFillMoneroDBRecent() {
		mbApi.fillMoneroBlockDbRecent();
		mbApi.fillMoneroBlockDbRecent();
		assertTrue( mbApi.isUpdated() );
	}

	@Test
	public void testFillMoneroDbHistorical() {
		mbApi.fillMoneroHistorical();
	}

	@Test
	public void testFillMoneroDb() {
		mbApi.fillMoneroDb();
	}

	@Test
	public void testBlockDbCompleteness() {
		boolean complete = mbApi.isComplete();
		Long missingBlocks = mbApi.countMissingBlocks(mbRepo.findHeighestBlockNbr());
		System.out.println("Missing Blocks = " + missingBlocks.toString());
		if (complete) {
			assertEquals(new Long(0), missingBlocks);
		} else {
			assertTrue(missingBlocks > 0);
		}

	}

	@Test
	// TODO fix this test, not clear why repository is not available
	public void testUpdateReferenceBlock() {

		MoneroBlock blk = mbApi.getBlock(MoneroBlock.refBlockNbr);
		blk.saveOrUpdate();
	}

	@Test
	public void testGetMostRecentBlockNbr() {

		// Verify block is more recent than reference block
		long blkNbr = mbApi.getMostRecentBlockNbr();
		assertTrue(blkNbr > MoneroBlock.refBlockNbr);
		System.out.println("Most Recent Block :" + blkNbr);

		// Get current time in UTC
		LocalDateTime nowUTC = LocalDateTime.now(ZoneId.of("UTC"));

		// Verify that block was created before the current time
		MoneroBlock blk = mbApi.getBlock(blkNbr);
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
		MoneroBlock blk = mbApi.getBlock(1L);
		assertTrue(blk.getDateTime().equals(MoneroBlock.firstBlockDateTime));
		assertTrue(blk.getDateHour().equals(MoneroBlock.firstBlockHour));
	}
}
