package com.afn.cryptobase.core;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.afn.realstat.AbstractEntityRepository;

@Repository
public interface MoneroDailyRepository extends AbstractEntityRepository<MoneroDaily> {

	public static final Logger log = LoggerFactory.getLogger("app");

	public MoneroDaily findByStartTimestamp(Long epochSeconds);
	
	@Query("SELECT min(startTimestamp) FROM  MoneroDaily")
	Long findEarliestRecordTimestamp();

	@Query("Select max(startTimestamp) From MoneroDaily")
	Long findLatestRecodTimestamp();
	
	@Query("SELECT startTimestamp from MoneroHourly where startTimestamp >= ?1 and startTimestamp < ?2")
	ArrayList<Long> getDayList(Long startTimestamp, Long endTimestamp);
	
	@Query("select md from MoneroDaily md where mod(md.startTimestamp,24*3600) != 0")
	ArrayList<MoneroHourly> getInvalidRecords();

}
