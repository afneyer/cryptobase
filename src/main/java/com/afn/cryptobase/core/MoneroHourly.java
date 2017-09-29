package com.afn.cryptobase.core;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;

import com.afn.realstat.AbstractEntity;
import com.afn.realstat.framework.SpringApplicationContext;

@Entity
@Table(name = "monero_hourly", uniqueConstraints = @UniqueConstraint(columnNames = { "startTimestamp" }))
/*
 * , indexes = {
 * 
 * @Index(name = "idx_blockDateTime", columnList = "startTimestamp") })
 */

public class MoneroHourly extends AbstractEntity {

	public static final Logger log = LoggerFactory.getLogger("app");
	private static MoneroHourlyRepository repo;

	private Long startTimestamp; // seconds POSIX time
	private Long firstBlockNbr;
	private Long lastBlockNbr;
	private Long difficulty;
	private Long reward;
	private Double exchangeUSD; // [USD/XMR]
	private Double exchangeBTC; // [BTC/XMR]

	public MoneroHourly() {
	}

	public MoneroHourly(Long startTimestamp) {
		this.startTimestamp = startTimestamp;
	}

	public MoneroHourly(LocalDateTime startDateTime) {
		startTimestamp = MoneroHourly.toEpochSeconds(startDateTime);
	}

	public Long getStartTimestamp() {
		return startTimestamp;
	}
	
	public Long getEndTimestamp() {
		return (startTimestamp + 3600);
	}
	
	public LocalDateTime getStartDateTime() {
		LocalDateTime ldt = MoneroHourly.toLocalDateTime(startTimestamp);
		return ldt;
	}

	public LocalDateTime getEndDateTime() {
		LocalDateTime ldt = getStartDateTime().plusSeconds(3600);
		return ldt;
	}

	public Long getHashRate() {
		Long hashRate = difficulty / MoneroBlock.getBlockTime();
		return hashRate;
	}

	public MoneroHourlyRepository getRepo() {
		if (repo == null) {
			repo = (MoneroHourlyRepository) SpringApplicationContext.getBean("moneroHourlyRepository");
		}
		return repo;
	}
	
	public static MoneroHourlyRepository getRepoStatic() {
		if (repo == null) {
			repo = (MoneroHourlyRepository) SpringApplicationContext.getBean("moneroHourlyRepository");
		}
		return repo;
	}

	@Override
	public void save() {
		if (isValid()) {
			getRepo().save(this);
		} else {
			throw new RuntimeException( "Invalid Entity: " + this.toString() );
		}
	}

	@Override
	public void saveOrUpdate() {
		if (isValid()) {
			getRepo().saveOrUpdate(this);
		} else {
			throw new RuntimeException( "Invalid Entity: " + this.toString() );
		}
	}

	@Override
	public Example<MoneroHourly> getRefExample() {
		Example<MoneroHourly> e = Example.of(new MoneroHourly(this.startTimestamp));
		return e;
	}

	@Override
	public void clean() {
	}

	@Override
	public boolean isValid() {

		boolean isValid = false;

		// check whether start time stamp is a valid hour start time stamp
		if (startTimestamp % 3600 == 0) {
			isValid = true;
		}
		return isValid;
	}
	
	@Override
	public String toString() {
		String s = super.toString();
		s += ", startTimestamp = " + startTimestamp;
		s += ", startHour = " + getStartHour();
		return s;
	}

	private LocalDateTime getStartHour() {
		// TODO Auto-generated method stub
		return MoneroHourly.toLocalDateTime(startTimestamp);
	}

	public Long getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(Long difficulty) {
		this.difficulty = difficulty;
	}

	public Long getReward() {
		return reward;
	}

	public void setReward(Long reward) {
		this.reward = reward;
	}

	public Double getExchangeUSD() {
		return exchangeUSD;
	}

	public void setExchangeUSD(Double exchangeUSD) {
		this.exchangeUSD = exchangeUSD;
	}

	public Double getExchangeBTC() {
		return exchangeBTC;
	}

	public void setExchangeBTC(Double exchangeBTC) {
		this.exchangeBTC = exchangeBTC;
	}

	public Long getFirstBlockNbr() {
		return firstBlockNbr;
	}

	public void setFirstBlockNbr(Long firstBlockNbr) {
		this.firstBlockNbr = firstBlockNbr;
	}

	public Long getLastBlockNbr() {
		return lastBlockNbr;
	}

	public void setLastBlockNbr(Long lastBlockNbr) {
		this.lastBlockNbr = lastBlockNbr;
	}

	public static Long toEpochSeconds(LocalDateTime ldt) {
		Long epochSeconds = ldt.atZone(ZoneId.of("UTC")).toEpochSecond();
		return epochSeconds;
	}

	public static LocalDateTime toLocalDateTime(Long epochSeconds) {
		LocalDateTime ldt = LocalDateTime.ofInstant(Instant.ofEpochSecond(epochSeconds), ZoneId.of("UTC"));
		return ldt;
	}
	


}