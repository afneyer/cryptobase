package com.afn.cryptobase.core;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;

import com.afn.realstat.AbstractEntity;
import com.afn.realstat.framework.SpringApplicationContext;

@Entity
@Table(name = "monero_daily", uniqueConstraints = @UniqueConstraint(columnNames = { "startDayTime" }))
/*
 * TODO clean , indexes = {
 * 
 * @Index(name = "idx_blockDateTime", columnList = "startTimestamp") })
 */

public class MoneroDaily extends AbstractEntity {

	public static final Logger log = LoggerFactory.getLogger("app");
	private static MoneroDailyRepository repo;

	private LocalDateTime startDayTime; // day starts hour 00:00:00 which is the
										// first hour of the day
	private Long difficulty;
	private Long targetDifficulty;
	private Long reward;
	private Double exchangeUSD; // [USD/XMR]
	private Double exchangeBTC; // [BTC/XMR]

	public MoneroDaily() {
	}

	public MoneroDaily(LocalDateTime startTime) {
		this.startDayTime = startTime;
	}

	public LocalDateTime getStartDayTime() {
		return startDayTime;
	}

	public LocalDateTime getEndDayTime() {
		return (startDayTime.plusDays(1));
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
			throw new RuntimeException("Invalid Entity: " + this.toString());
		}
	}

	@Override
	public void saveOrUpdate() {
		if (isValid()) {
			getRepo().saveOrUpdate(this);
		} else {
			throw new RuntimeException("Invalid Entity: " + this.toString());
		}
	}

	@Override
	public Example<MoneroDaily> getRefExample() {
		Example<MoneroDaily> e = Example.of(new MoneroDaily(this.startDayTime));
		return e;
	}

	@Override
	public void clean() {
	}

	@Override
	public boolean isValid() {

		if (startDayTime.getHour() != 0) {
			return false;
		}
		if (startDayTime.getMinute() != 0) {
			return false;
		}
		if (startDayTime.getSecond() != 0) {
			return false;
		}
		if (startDayTime.getNano() != 0) {
			return false;
		}

		return true;
	}

	@Override
	public String toString() {
		String s = super.toString();
		s += ", startTime = " + startDayTime;
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
	
	public Double getRewardXmr() {
		return new Double(reward) / new Double("1e12");
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

	public boolean isIncomplete() {
		if (difficulty == null)
			return true;
		if (exchangeBTC == null)
			return true;
		if (exchangeUSD == null)
			return true;
		return false;
	}

}