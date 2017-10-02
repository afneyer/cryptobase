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

	public MoneroDaily findByStartDayTime(LocalDateTime startDayTime);
	
	@Query("SELECT min(startDayTime) FROM  MoneroDaily")
	LocalDateTime findEarliestRecordDate();

	@Query("Select max(startDayTime) From MoneroDaily")
	LocalDateTime findLatestRecodDate();
	
	@Query("SELECT startDayTime from MoneroDaily where startDayTime >= ?1 and startDayTime < ?2")
	ArrayList<LocalDateTime> getDayList(LocalDateTime startDayTime, LocalDateTime endDayTime);
	
	@Query("select md from MoneroDaily md where mod(md.startDayTime,24*3600) != 0")
	ArrayList<MoneroDaily> getInvalidRecords();

}
