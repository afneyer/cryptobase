package com.afn.cryptobase.core;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.Query;

import com.afn.realstat.AbstractEntityRepository;

public interface ContractRevenueRepository extends AbstractEntityRepository<ContractRevenue> {

	public static final Logger log = LoggerFactory.getLogger("app");

	// TODO fix
	public ContractRevenue findByContractAndStartDateTime(Contract contract, LocalDateTime startDateTime);

	public List<ContractRevenue> findByContract(Contract contract);

	/*
	@Query("SELECT min(startDateTime) FROM  MoneroDaily")
	LocalDateTime findEarliestRecordDate();

	@Query("Select max(startDateTime) From MoneroDaily")
	LocalDateTime findLatestRecodDate();

	@Query("SELECT startDateTime from MoneroDaily where startDateTime >= ?1 and startDateTime < ?2")
	ArrayList<LocalDateTime> getDateList(LocalDateTime startDayTime, LocalDateTime endDateTime);
	*/
}
