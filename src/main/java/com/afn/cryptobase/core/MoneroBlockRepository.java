package com.afn.cryptobase.core;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.afn.realstat.AbstractEntityRepository;

@Repository
public interface MoneroBlockRepository extends AbstractEntityRepository<MoneroBlock> {

	public static final Logger log = LoggerFactory.getLogger("app");

	public MoneroBlock findByBlockNbr(Long refBlockNbr);

	@Query("SELECT min(blockNbr) FROM  MoneroBlock")
	Long findLowestBlockNbr();

	@Query("Select max(blockNbr) From MoneroBlock")
	Long findHeighestBlockNbr();

	public List<MoneroBlock> findAllByTimestampBetweenOrderByTimestamp(Long start, Long end);

	public default List<MoneroBlock> findBlocksByStartHour(LocalDateTime startHour) {

		Long startTimestamp = MoneroBlock.toMoneroTimestamp(startHour);
		Long endTimestamp = startTimestamp+3600L;
		List<MoneroBlock> list = this.findAllByTimestampBetweenOrderByTimestamp(startTimestamp - 1, endTimestamp);
		return list;

	}

}
