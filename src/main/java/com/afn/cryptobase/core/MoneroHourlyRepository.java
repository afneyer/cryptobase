package com.afn.cryptobase.core;

import java.util.ArrayList;
import java.util.List;

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
	
	// TODO may not be needed because of the next function
	@Query("SELECT startTimestamp from MoneroHourly where startTimestamp >= ?1 and startTimestamp < ?2")
	List<Long> getTimestampList(Long startTimestamp, Long endTimestamp);
	
	@Query("SELECT mh from MoneroHourly mh where mh.startTimestamp >= ?1 and mh.startTimestamp < ?2")
	List<MoneroHourly> getMoneroHourlyList(Long startTimestamp, Long endTimestamp);
	
	@Query("select mh from MoneroHourly mh where mod(mh.startTimestamp,3600) != 0")
	List<MoneroHourly> getInvalidRecords();

	@Query("select min(startTimestamp) from MoneroHourly where exchangeUSD is null or exchangeBTC is null")
	public Long findEarliestIncompleteRecord();

}
