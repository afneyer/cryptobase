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
@Table(name = "monero_daily", uniqueConstraints = @UniqueConstraint(columnNames = { "startTimestamp" }))
/*
 * , indexes = {
 * 
 * @Index(name = "idx_blockDateTime", columnList = "startTimestamp") })
 */

public class MoneroDaily extends AbstractEntity {

	public static final Logger log = LoggerFactory.getLogger("app");
	private static MoneroDailyRepository repo;

	private Long startTimestamp; // seconds POSIX time
	private Long firstHourly;
	private Long lastHourly;
	private Long difficulty;
	private Long targetDifficulty;
	private Long reward;
	private Double exchangeUSD; // [USD/XMR]
	private Double exchangeBTC; // [BTC/XMR]

	public MoneroDaily() {
	}

	public MoneroDaily(Long startTimestamp) {
		this.startTimestamp = startTimestamp;
	}

	public MoneroDaily(LocalDateTime startDateTime) {
		startTimestamp = MoneroDaily.toEpochSeconds(startDateTime);
	}

	public Long getStartTimestamp() {
		return startTimestamp;
	}
	
	public Long getEndTimestamp() {
		return (startTimestamp + 3600*24);
	}
	
	public LocalDateTime getStartDateTime() {
		LocalDateTime ldt = MoneroHourly.toLocalDateTime(startTimestamp);
		return ldt;
	}

	public LocalDateTime getEndDateTime() {
		LocalDateTime ldt = MoneroHourly.toLocalDateTime(getEndTimestamp());
		return ldt;
	}

	public Long getHashRate() {
		Long hashRate = difficulty / MoneroBlock.getBlockTime();
		return hashRate;
	}

	public MoneroDailyRepository getRepo() {
		if (repo == null) {
			repo = (MoneroDailyRepository) SpringApplicationContext.getBean("moneroDailyRepository");
		}
		return repo;
	}
	
	
	// TODO: this needs to be investigated
	public static MoneroDailyRepository getRepoStatic() {
		if (repo == null) {
			repo = (MoneroDailyRepository) SpringApplicationContext.getBean("moneroDailyRepository");
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
	public Example<MoneroDaily> getRefExample() {
		Example<MoneroDaily> e = Example.of(new MoneroDaily(this.startTimestamp));
		return e;
	}

	@Override
	public void clean() {
	}

	@Override
	public boolean isValid() {

		boolean isValid = false;

		// check whether start time stamp is a valid hour start time stamp
		if (startTimestamp % 3600*24 == 0) {
			isValid = true;
		}
		return isValid;
	}
	
	@Override
	public String toString() {
		String s = super.toString();
		s += ", startTimestamp = " + startTimestamp;
		s += ", startHour = " + getStartDay();
		return s;
	}

	private LocalDateTime getStartDay() {
		return MoneroDaily.toLocalDateTime(startTimestamp);
	}

	public Long getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(Long difficulty) {
		this.difficulty = difficulty;
	}

	public Long getTargetDifficulty() {
		return targetDifficulty;
	}

	public void setTargetDifficulty(Long targetDifficulty) {
		this.targetDifficulty = targetDifficulty;
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

	public Long getFirstHourly() {
		return firstHourly;
	}

	public void setFirstHourly(Long firstHourly) {
		this.firstHourly = firstHourly;
	}

	public Long getLastHourly() {
		return lastHourly;
	}

	public void setLastHourly(Long lastHourly) {
		this.lastHourly = lastHourly;
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