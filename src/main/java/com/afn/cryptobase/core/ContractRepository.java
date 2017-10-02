package com.afn.cryptobase.core;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.Query;

import com.afn.realstat.AbstractEntityRepository;

public interface ContractRepository extends AbstractEntityRepository<Contract> {

	public static final Logger log = LoggerFactory.getLogger("app");

	public MoneroDaily findByStartDayTime(LocalDateTime startDayTime);

	@Query("SELECT min(startDateTime) FROM  MoneroDaily")
	LocalDateTime findEarliestRecordDate();

	@Query("Select max(startDateTime) From MoneroDaily")
	LocalDateTime findLatestRecodDate();

	@Query("SELECT startDateTime from MoneroDaily where startDateTime >= ?1 and startDateTime < ?2")
	ArrayList<LocalDateTime> getDateList(LocalDateTime startDayTime, LocalDateTime endDateTime);

}
