package com.afn.cryptobase.core;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.afn.realstat.AbstractEntityManager;

@Component
public class MoneroHourlyManager extends AbstractEntityManager<MoneroHourly> {

	public static final Logger log = LoggerFactory.getLogger("app");
	
	@Autowired
	MoneroBlockRepository mbRepo;

	/*
	 * assumes all moneroBlocks are available
	 */
	public MoneroHourly createMoneroHourly(LocalDateTime startDateTime) {

		MoneroHourly mh = new MoneroHourly(startDateTime);

		// find all blocks for the hour
		List<MoneroBlock> blockList = mbRepo.findBlocksByStartHour(startDateTime);

		mh.setFirstBlockNbr(blockList.get(0).getBlockNbr());
		mh.setLastBlockNbr(blockList.get(blockList.size() - 1).getBlockNbr());

		mh.setDifficulty(new Long(0));
		mh.setReward(new Long(0));

		for (MoneroBlock blk : blockList) {
			mh.setDifficulty(mh.getDifficulty() + blk.getDifficulty());
			mh.setReward(mh.getReward() + blk.getReward());
		}
		mh.setDifficulty(mh.getDifficulty() / blockList.size());

		return mh;
	}

	public void fillMoneroHourlyDb() {

		LocalDateTime current = LocalDateTime.now();

		// round down to the startTime latest complete hour
		LocalDateTime latestHour = MoneroBlock.roundDownToHour(current);
		latestHour = latestHour.minusSeconds(3600L);

		MoneroHourlyRepository mhRepo = MoneroHourly.getRepoStatic();
		// LocalDateTime firstHour = MoneroBlock.toLocalDateTime(mhRepo.findEarliestRecordTimestamp());
	
		// LocalDateTime lastHour = MoneroBlock.toLocalDateTime(mhRepo.findLatestRecodTimestamp());
		LocalDateTime firstBlockHour = MoneroBlock.firstBlockHour;
		
		LocalDateTime ldt = latestHour;
		while (/*ldt.isAfter(firstHour) &&*/ ldt.isAfter(firstBlockHour)) {
			MoneroHourly mh = mhRepo.findByStartTimestamp(MoneroBlock.toMoneroTimestamp(ldt));
			if (mh == null) {
				mh = createMoneroHourly(ldt);
				mh.saveOrUpdate();
			}
			ldt= ldt.minusSeconds(3600);
			System.out.println("Created MoneroHourly record for " + ldt.toString());
		}

	}
}
