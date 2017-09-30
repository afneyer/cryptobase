package com.afn.cryptobase.core;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.afn.realstat.AbstractEntityRepository;

@Repository
public interface MoneroHourlyRepository extends AbstractEntityRepository<MoneroHourly> {

	public static final Logger log = LoggerFactory.getLogger("app");

	public MoneroHourly findByStartTimestamp(Long epochSeconds);
	
	@Query("SELECT min(startTimestamp) FROM  MoneroHourly")
	Long findEarliestRecordTimestamp();

	@Query("Select max(startTimestamp) From MoneroHourly")
	Long findLatestRecodTimestamp();
	
	@Query("SELECT startTimestamp from MoneroHourly where startTimestamp >= ?1 and startTimestamp < ?2")
	ArrayList<Long> getHourList(Long startTimestamp, Long endTimestamp);
	
	@Query("select mh from MoneroHourly mh where mod(mh.startTimestamp,3600) != 0")
	ArrayList<MoneroHourly> getInvalidRecords();

	@Query("select min(startTimestamp) from MoneroHourly where exchangeUSD is null or exchangeBTC is null")
	public Long findEarliestIncompleteRecord();

}
