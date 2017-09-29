package com.afn.cryptobase.core;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;

import com.afn.realstat.AbstractEntity;
import com.afn.realstat.AfnDateUtil;
import com.afn.realstat.framework.SpringApplicationContext;

@Entity
@Table(name = "monero_block", uniqueConstraints = @UniqueConstraint(columnNames = { "blockNbr" }), indexes = {
		@Index(name = "idx_blockDateTime", columnList = "blockDateTime"),
		@Index(name = "idx_blockTimestamp", columnList = "timestamp")})

public class MoneroBlock extends AbstractEntity {

	public static final Logger log = LoggerFactory.getLogger("app");
	private static MoneroBlockRepository repo;

	public static Long refBlockNbr = 1398339L;
	public static Long refBlockTimestamp = 1505324950L;
	public static Long refBlockMoneySupply = 15078240L;
	public static LocalDateTime refBlockDateTime = LocalDateTime.parse("2017-09-13T17:49:10");
	public static LocalDateTime refBlockDateHour = LocalDateTime.parse("2017-09-13T17:00:00");
	public static Long refBlockDifficulty = 31969675874L;
	public static Long refBlockReward = 6722898519276L;
	public static final LocalDateTime firstBlockDateTime = LocalDateTime.parse("2014-04-18T10:49:53");
	public static final LocalDateTime firstBlockHour = LocalDateTime.parse("2014-04-18T10:00:00");

	private static Long blockTime = 120L; // Target block creation time in seconds

	private Long blockNbr;
	private Long size;
	private Long difficulty;
	private boolean orphanStatus;
	private Long reward; // Block reward in mXmr
	private Long timestamp; // time stamp in seconds since ?
	private String status;
	private Double exchangeUSD; // [USD/XMR]
	private Date blockDateTime; // block date / time

	public MoneroBlock() {
	}

	public MoneroBlock(Long blockNbr) {
		this.blockNbr = blockNbr;
	}

	public MoneroBlock(Long blockNbr, Long blockTimestamp) {
		this.timestamp = blockTimestamp;
		this.blockNbr = blockNbr;

		// Compute the date
		setDateTime();
	}

	private void setDateTime() {
		LocalDateTime ldt = refBlockDateTime;
		Long timestampDiffSecs = getTimestamp() - refBlockTimestamp;
		ldt = ldt.plusSeconds(timestampDiffSecs);
		this.blockDateTime = AfnDateUtil.asDate(ldt);
	}

	public static LocalDateTime firstBlockDateTime() {
		return LocalDateTime.parse("2014-04-18T10:49:53");
	}
	
	
	public Long getHashRate() {
		Long hashRate = difficulty / blockTime;
		return hashRate;
	}

	public LocalDateTime getDateTime() {
		LocalDateTime ldt = refBlockDateTime;
		Long timestampDiffSecs = getTimestamp() - refBlockTimestamp;
		ldt = ldt.plusSeconds(timestampDiffSecs);
		return ldt;
	}
	
	public LocalDateTime getDateHour() {
		return MoneroBlock.roundDownToHour(getDateTime());
	}

	public MoneroBlockRepository getRepo() {
		if (repo == null) {
			repo = (MoneroBlockRepository) SpringApplicationContext.getBean("moneroBlockRepository");
		}
		return repo;
	}


	@Override
	public void save() {
		getRepo().save(this);

	}

	@Override
	public void saveOrUpdate() {
		getRepo().saveOrUpdate(this);
	}

	@Override
	public Example<MoneroBlock> getRefExample() {
		Example<MoneroBlock> e = Example.of(new MoneroBlock(this.getBlockNbr()));
		return e;
	}

	@Override
	public void clean() {
	}

	@Override
	public boolean isValid() {
		return true;
	}

	public static Long getBlockTime() {
		return blockTime;
	}

	public Long getBlockNbr() {
		return blockNbr;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public Long getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(Long difficulty) {
		this.difficulty = difficulty;
	}

	public boolean isOrphanStatus() {
		return orphanStatus;
	}

	public void setOrphanStatus(boolean orphanStatus) {
		this.orphanStatus = orphanStatus;
	}

	public Long getReward() {
		return reward;
	}

	public void setReward(Long reward) {
		this.reward = reward;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Double getExchangeUSD() {
		return exchangeUSD;
	}

	public void setExchangeUSD(Double exchangeUSD) {
		this.exchangeUSD = exchangeUSD;
	}

	public LocalDate getBlockDate() {
		LocalDate ld = AfnDateUtil.asLocalDate(blockDateTime);
		return ld;
	}

	public static Long toMoneroTimestamp(LocalDateTime ldt) {
		Long dateTimeEpoch = MoneroHourly.toEpochSeconds(ldt);
		// Long firstBlockEpoch = MoneroHourly.toEpochSeconds(MoneroBlock.firstBlockDateTime);
		// return dateTimeEpoch - firstBlockEpoch;
		return dateTimeEpoch;
	}
	
	public static LocalDateTime roundDownToHour (LocalDateTime ldt) {
		ldt = ldt.minusSeconds(ldt.getSecond());
		ldt = ldt.minusMinutes(ldt.getMinute());
		ldt = ldt.minusNanos(ldt.getNano());
		return ldt;
	}

	public static LocalDateTime toLocalDateTime(Long timestamp) {
		LocalDateTime ldt = MoneroHourly.toLocalDateTime(timestamp);
		return ldt;
	}


}