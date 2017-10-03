package com.afn.cryptobase.core;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;

import com.afn.realstat.AbstractEntity;
import com.afn.realstat.framework.SpringApplicationContext;

@Entity
@Table(name = "contract", uniqueConstraints = @UniqueConstraint(columnNames = { "company","startDateTime" }))

public class Contract extends AbstractEntity {

	public static final Logger log = LoggerFactory.getLogger("app");

	private static ContractRepository repo;


	private String company;
	private LocalDateTime startDateTime;
	private Long duration; // duration in seconds
	private Long hashRate;
	private String algorithm;
	private String revenueCurrency;
	private String type;
	private Double price;
	private String priceCurrency;

	public Contract() {
	}

	public Contract(String company, LocalDateTime startDateTime) {
		this.company = company;
		this.startDateTime = startDateTime;
	}

	public Contract(String company, LocalDateTime startDateTime, Long duration, Long hashRate,
			String algorithm, String revenueCurrency, String type, Double price, String priceCurrency) {
		this.company = company;
		this.startDateTime = startDateTime;
		this.duration = duration;
		this.hashRate = hashRate;
		this.algorithm = algorithm;
		this.revenueCurrency = revenueCurrency;
		this.type = type;
		this.price = price;
		this.priceCurrency = priceCurrency;

	}

	public ContractRepository getRepo() {
		if (repo == null) {
			repo = (ContractRepository) SpringApplicationContext.getBean("contractRepository");
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
	public Example<Contract> getRefExample() {
		Example<Contract> e = Example.of(new Contract(this.company, this.startDateTime));
		return e;
	}

	@Override
	public void clean() {
	}

	@Override
	public boolean isValid() {

		List<String> companyList = Arrays.asList("Genesis");
		List<String> algoList = Arrays.asList("CryptoNight");
		List<String> currencyList = Arrays.asList("XMR", "USD");
		List<String> typeList = Arrays.asList("Monero");

		if (!companyList.contains(company))
			return false;
		if (startDateTime == null)
			return false;
		if (duration == null)
			;
		if (hashRate == null)
			;
		if (!algoList.contains(algorithm))
			return false;
		if (!currencyList.contains(revenueCurrency))
			return false;
		if (!typeList.contains(type))
			return false;
		if (!(price >= 0))
			return false;
		if (!currencyList.contains(priceCurrency))
			return false;
		return true;
	}

	@Override
	public String toString() {
		String s = super.toString();
		s += ", company = " + company;
		s += ", startDateTime " + startDateTime;
		return s;
	}

	public String getAlgorithm() {
		return algorithm;
	}

	public String getCompany() {
		return company;
	}

	public LocalDateTime getStartDateTime() {
		return startDateTime;
	}

	public Long getDuration() {
		return duration;
	}

	public Long getHashRate() {
		return hashRate;
	}

	public String getRevenueCurrency() {
		return revenueCurrency;
	}

	public String getType() {
		return type;
	}

	public Double getPrice() {
		return price;
	}

	public String getPriceCurrency() {
		return priceCurrency;
	}

}