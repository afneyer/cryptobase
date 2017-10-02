package com.afn.cryptobase.core;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.afn.realstat.AbstractEntityManager;
import com.querydsl.core.types.Predicate;

@Component
public class MoneroDailyManager extends AbstractEntityManager<MoneroDaily> {

	public static final Logger log = LoggerFactory.getLogger("app");

	@Autowired
	MoneroDailyRepository mdRepo;
	
	@Autowired
	MoneroHourlyRepository mhRepo;
	
	@Autowired
	MoneroHourlyManager mhMgr;

	/*
	 * assumes all moneroBlocks are available
	 */
	public MoneroDaily createMoneroDaily(LocalDateTime startDateTime) {

		MoneroDaily md = new MoneroDaily(startDateTime);

		// find all MoneroHourly records for the given day
		List<MoneroHourly> hourlyList = mhRepo.getMoneroHourlyList(md.getStartDayTime(), md.getEndDayTime());
		int listSize = hourlyList.size();
		
		md.setDifficulty(0L);
		md.setExchangeBTC(0.0);
		md.setExchangeUSD(0.0);
		md.setReward(0L);
		
		for (int i = 0; i<listSize; i++ ) {
			MoneroHourly mh = hourlyList.get(i);
			md.setDifficulty(md.getDifficulty() + mh.getDifficulty() );
			md.setExchangeBTC(md.getExchangeBTC() + mh.getExchangeBTC() );
			md.setExchangeUSD(md.getExchangeUSD() + mh.getExchangeUSD() );
			md.setReward(md.getReward() + mh.getReward() );
		}
		
		// compute averages
		md.setDifficulty( md.getDifficulty() / listSize );
		md.setExchangeBTC( md.getExchangeBTC() / listSize );
		md.setExchangeUSD( md.getExchangeUSD() / listSize );
		
		return md;
	}

	public void fillMoneroDailyDb() {
		
		mhMgr.fillMoneroHourlyDb();
		
		LocalDateTime current = LocalDateTime.now(Clock.systemUTC());

		// round down to the startTime latest complete hour
		LocalDateTime latestDay = MoneroBlock.roundDownToDay(current).minusDays(1);
		LocalDateTime earliestDay = MoneroBlock.roundDownToDay(MoneroBlock.firstBlockDateTime);
		
		LocalDateTime ldt = latestDay;
		while ( ldt.isAfter(earliestDay.minusSeconds(1)) ) {
			MoneroDaily md = mdRepo.findByStartDayTime(ldt);
			if (md == null || md.isIncomplete() ) {
				md = this.createMoneroDaily(ldt);
				md.saveOrUpdate();
			}
			ldt = ldt.minusDays(1);
		}
	}

	/* 
	 * Work in Progress
	 * Make the iteration more efficient using database queries in batches
	 * TODO
	public List findMissingRecords(LocalDateTime startTime, LocalDateTime endTime) {
		ArrayList<MoneroHourly> list = new ArrayList<MoneroHourly>();
		Long missing = countMissingAndIncompleteRecords(startTime, endTime);
		if (missing != null) {
			list = getMissingAndIncompleteRecords(startTime, endTime);
		}
		return list;
	}
	
	public Long countMissingAndIncompleteRecords(LocalDateTime startTime, LocalDateTime endTime) {
		
		Long records = null;
		
		// missing
		Predicate p1 = QMoneroDaily.moneroDaily.startDayTime.after(startTime.minusSeconds(1)).and(QMoneroDaily.moneroDaily.endDayTime.before(endTime));
		Long actual = mdRepo.count(p1);
		Long target = ChronoUnit.DAYS.between(startTime, endTime) + 1;
		Long missing = target - actual;
		if (missing <0) {
			throw new RuntimeException("Extra invalid records between " + startTime + " and " + endTime);
		}
		
		// find the number of incomplete records
		Predicate p2 = QMoneroDaily.moneroDaily.difficulty.isNotNull();
		
		return records;
	}
	
	*/
	
	public Long countMissingRecords() {
		LocalDateTime latest = mdRepo.findLatestRecodDate();
		LocalDateTime earliest = mdRepo.findEarliestRecordDate();
		Long numDaysTarget = ChronoUnit.DAYS.between(earliest, latest) + 1;

		Long numDaysActual = mdRepo.count();
		return numDaysTarget - numDaysActual;
	}

	public boolean isComplete() {
		return countMissingRecords().equals(0L);
	}

	public void removeInvalidRecords() {
		List<MoneroDaily> list = mdRepo.getInvalidRecords();
		mdRepo.delete(list);
	}
	
}
