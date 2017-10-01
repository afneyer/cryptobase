package com.afn.cryptobase.core;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.afn.realstat.AbstractEntityRepository;

@Repository
public interface MoneroDailyRepository extends AbstractEntityRepository<MoneroDaily> {

	public static final Logger log = LoggerFactory.getLogger("app");

	public MoneroDaily findByStartTime(LocalDateTime startTime);
	
	@Query("SELECT min(startTime) FROM  MoneroDaily")
	Long findEarliestRecordDate();

	@Query("Select max(startTime) From MoneroDaily")
	Long findLatestRecodDate();
	
	@Query("SELECT startTime from MoneroDaily where startTime >= ?1 and startTime < ?2")
	ArrayList<Long> getDayList(Long startTimestamp, Long endTimestamp);
	
	@Query("select md from MoneroDaily md where mod(md.startTime,24*3600) != 0")
	ArrayList<MoneroDaily> getInvalidRecords();

}
