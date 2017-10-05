package com.afn.cryptobase.core;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;

import com.afn.realstat.AbstractEntity;
import com.afn.realstat.framework.SpringApplicationContext;

@Entity
@Table(name = "contract_revenue", uniqueConstraints = @UniqueConstraint(columnNames = { "startDateTime" }))

public class ContractRevenue extends AbstractEntity {

	public static final Logger log = LoggerFactory.getLogger("app");

	private static ContractRevenueRepository repo;

	@ManyToOne(fetch = FetchType.EAGER)
	private Contract contract;
	private LocalDateTime startDateTime;
	private Double revenue;
	private String revenueCurrency;

	public ContractRevenue() {
	}

	public ContractRevenue(Contract contract, LocalDateTime startDateTime) {
		this.contract = contract;
		this.startDateTime = startDateTime;
	}

	public ContractRevenue(Contract contract, LocalDateTime startDateTime, Double revenue,String revenueCurrrency) {
		this(contract, startDateTime);
		this.revenue = revenue;
		this.revenueCurrency = revenueCurrrency;
	}

	public ContractRevenueRepository getRepo() {
		if (repo == null) {
			repo = (ContractRevenueRepository) SpringApplicationContext.getBean("contractRevenueRepository");
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
	public Example<ContractRevenue> getRefExample() {
		Example<ContractRevenue> e = Example.of(new ContractRevenue(this.contract, this.startDateTime));
		return e;
	}

	@Override
	public void clean() {
	}

	@Override
	public boolean isValid() {

		List<String> currencyList = Arrays.asList("XMR");

		if (contract == null) return false;
		if (startDateTime == null) return false;
		if (startDateTime.isBefore(MoneroBlock.roundDownToDay(contract.getStartDateTime()))) return false;
		if (revenue == null) return false;
		if (revenue < 0.0) return false;
		
		if (!currencyList.contains(revenueCurrency)) return false;
		return true;
	}

	public static Logger getLog() {
		return log;
	}

	public Contract getContract() {
		return contract;
	}

	public LocalDateTime getStartDateTime() {
		return startDateTime;
	}

	public Double getRevenue() {
		return revenue;
	}

	public String getRevenueCurrency() {
		return revenueCurrency;
	}

	@Override
	public String toString() {
		String s = super.toString();
		s += contract.toString();
		s += ", startDateTime " + startDateTime;
		return s;
	}

}