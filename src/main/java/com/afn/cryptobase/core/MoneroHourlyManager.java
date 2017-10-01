package com.afn.cryptobase.core;

import java.util.Date;
import java.util.Iterator;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.afn.realstat.AbstractEntityManager;
import com.afn.realstat.AfnDateUtil;
import com.querydsl.core.types.Predicate;

@Component
public class MoneroHourlyManager extends AbstractEntityManager<MoneroHourly> {

	public static final Logger log = LoggerFactory.getLogger("app");

	@Autowired
	MoneroBlockRepository mbRepo;

	@Autowired
	MoneroHourlyRepository mhRepo;

	@Autowired
	MoneroBlockApi mbApi;
	
	@Autowired
	MoneroHourlyApi mbhApi;

	/*
	 * assumes all moneroBlocks are available
	 */
	public MoneroHourly createMoneroHourly(LocalDateTime startDateTime) {

		MoneroHourly mh = new MoneroHourly(startDateTime);

		// find all blocks for the hour
		List<MoneroBlock> blockList = mbRepo.findBlocksByStartHour(startDateTime);

		if (blockList.size() == 0) {
			mh.setFirstBlockNbr(0L);
			mh.setLastBlockNbr(0L);
			MoneroBlock mb = mbRepo.findMostRecentBlockBefore(MoneroBlock.toMoneroTimestamp(startDateTime));
			mh.setDifficulty(mb.getDifficulty());
			mh.setReward(0L);
		} else {

			mh.setFirstBlockNbr(blockList.get(0).getBlockNbr());
			mh.setLastBlockNbr(blockList.get(blockList.size() - 1).getBlockNbr());

			mh.setDifficulty(new Long(0));
			mh.setReward(new Long(0));

			for (MoneroBlock blk : blockList) {
				mh.setDifficulty(mh.getDifficulty() + blk.getDifficulty());
				mh.setReward(mh.getReward() + blk.getReward());
			}
			mh.setDifficulty(mh.getDifficulty() / blockList.size());
		}

		mbhApi.updateHourlyExchangeRate("USD", mh);
		mbhApi.updateHourlyExchangeRate("BTC", mh);
		
		return mh;
	}

	public void fillMoneroHourlyDb() {

		LocalDateTime current = LocalDateTime.now(Clock.systemUTC());

		// round down to the startTime latest complete hour
		LocalDateTime latestHour = MoneroBlock.roundDownToHour(current);
		latestHour = latestHour.minusSeconds(3600L);
		Long latestHourTimestamp = MoneroHourly.toEpochSeconds(latestHour);

		// TODO change repo lookup
		// MoneroHourlyRepository mhRepo = MoneroHourly.getRepoStatic();

		// if the MoneroBlock records don't yet exist for this hour, fill in the
		// recent data first
		Long blkNbr = mbRepo.findHeighestBlockNbr();
		LocalDateTime blkLdt = mbRepo.findByBlockNbr(blkNbr).getDateHour();
		if (blkLdt.isBefore(latestHour.plusSeconds(3600L))) {
			mbApi.fillMoneroBlockDbRecent();
		}

		Long batchSize = 1000L;
		LocalDateTime startHourLdt = MoneroBlock.firstBlockHour;
		Long startHour = MoneroHourly.toEpochSeconds(startHourLdt);

		while (startHour <= latestHourTimestamp) {

			Long endHour = startHour + batchSize * 3600L;
			endHour = Math.min(endHour, MoneroHourly.toEpochSeconds(latestHour));

			List<Long> list = mhRepo.getTimestampList(startHour, endHour);
			Long numHours = (endHour - startHour) / 3600L;

			Long timestamp = startHour;
			if (!new Long(numHours).equals(new Long(list.size()))) {

				while (timestamp < endHour) {
					MoneroHourly mh = mhRepo.findByStartTimestamp(timestamp);
					if (mh == null) {
						mh = createMoneroHourly(MoneroHourly.toLocalDateTime(timestamp));
						mh.saveOrUpdate();
						System.out
								.println("Created MoneroHourly record for " + MoneroHourly.toLocalDateTime(timestamp));
					}
					timestamp += 3600L;

				}

			}
			startHour += batchSize * 3600L;
		}

	}

	public Long countMissingRecords() {
		Long lowestTimestamp = MoneroHourly.toEpochSeconds(MoneroBlock.firstBlockHour);
		Long highestTimestamp = mhRepo.findLatestRecodTimestamp();
		Long numHourlyTarget = (highestTimestamp - lowestTimestamp) / 3600L + 1;

		Long numHourly = mhRepo.count();
		return numHourlyTarget - numHourly;
	}

	public boolean isComplete() {
		return countMissingRecords().equals(0L);
	}

	public void removeInvalidRecords() {
		List<MoneroHourly> list = mhRepo.getInvalidRecords();
		mhRepo.delete(list);
	}

	public void fillDates() {
		Predicate p = QMoneroHourly.moneroHourly.startDateTime.isNull();
		Iterable<MoneroHourly> iterable = mhRepo.findAll(p);
		
		Iterator<MoneroHourly> it = iterable.iterator();
		MoneroHourly mh = null;
		while ( it.hasNext() ) {
			mh = it.next();
			mh.computeDenormalizedFields(MoneroBlock.toLocalDateTime(mh.getStartTimestamp()));
			mh.saveOrUpdate();
			System.out.println("Setting startDateTime fields for : " + mh.getStartDateTime());
		}
		
	}
}
