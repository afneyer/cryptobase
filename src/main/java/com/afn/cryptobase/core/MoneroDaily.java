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
@Table(name = "monero_daily", uniqueConstraints = @UniqueConstraint(columnNames = { "startTime" }))
/* TODO clean
 * , indexes = {
 * 
 * @Index(name = "idx_blockDateTime", columnList = "startTimestamp") })
 */

public class MoneroDaily extends AbstractEntity {

	public static final Logger log = LoggerFactory.getLogger("app");
	private static MoneroDailyRepository repo;

	private LocalDateTime startTime; // day starts hour 00:00:00 which is the first hour of the day
	private Long difficulty;
	private Long targetDifficulty;
	private Long reward;
	private Double exchangeUSD; // [USD/XMR]
	private Double exchangeBTC; // [BTC/XMR]

	public MoneroDaily() {
	}

	public MoneroDaily(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}
	
	public LocalDateTime getEndTime() {
		return (startTime.plusDays(1));
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
		Example<MoneroDaily> e = Example.of(new MoneroDaily(this.startTime));
		return e;
	}

	@Override
	public void clean() {
	}

	@Override
	public boolean isValid() {
	
	    if (startTime.getHour() != 0) {
	    	return false;
	    }
		if (startTime.getMinute() != 0) {
			return false;
		}
		if (startTime.getSecond() != 0) {
			return false;
		}
		if (startTime.getNano() != 0) {
			return false;
		}

		return true;
	}
	
	@Override
	public String toString() {
		String s = super.toString();
		s += ", startTime = " + startTime;
		return s;
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

	public static Long toEpochSeconds(LocalDateTime ldt) {
		Long epochSeconds = ldt.atZone(ZoneId.of("UTC")).toEpochSecond();
		return epochSeconds;
	}

	public static LocalDateTime toLocalDateTime(Long epochSeconds) {
		LocalDateTime ldt = LocalDateTime.ofInstant(Instant.ofEpochSecond(epochSeconds), ZoneId.of("UTC"));
		return ldt;
	}
	
}