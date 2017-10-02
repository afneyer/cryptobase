package com.afn.cryptobase.core;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.mockito.Matchers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;

import com.afn.realstat.AbstractEntity;
import com.afn.realstat.framework.SpringApplicationContext;

@Entity
@Table(name = "contract", uniqueConstraints = @UniqueConstraint(columnNames = { "uniqueName" }))


public class Contract extends AbstractEntity {

	public static final Logger log = LoggerFactory.getLogger("app");
	
	private static ContractRepository repo;

	private String uniqueName;
	private LocalDateTime startDateTime;
	private Long duration; 			// duration in seconds
	private Long hashRate;
	private String algorithm;
	private String currency;
	private String type; 			

	public Contract() {
	}
	
	public Contract(String uniqueName) {
		uniqueName = this.uniqueName;
	}

	public Contract(String uniqueName, LocalDateTime startDateTime, Long duration, Long hashRate, String algorithm, String currency, String type) {
		this.uniqueName = uniqueName;
		this.startDateTime = startDateTime;
		this.duration = duration;
		this.hashRate = hashRate;
		this.algorithm = algorithm;
		this.currency = currency;
		this.type = type;
	}

	public ContractRepository getRepo() {
		if (repo == null) {
			repo = (ContractRepository) SpringApplicationContext.getBean("moneroContractRepository");
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
		Example<Contract> e = Example.of(new Contract(this.uniqueName));
		return e;
	}

	@Override
	public void clean() {
	}

	@Override
	public boolean isValid() {
		
		List<String> algoList = Arrays.asList("CryptoNight");
		List<String> currencyList = Arrays.asList("XMR");
		List<String> typeList = Arrays.asList("Monero");

		if (uniqueName == null) return false;
		if (startDateTime == null) return false;
		if (duration == null);
		if (hashRate == null);
		if (!algoList.contains(algorithm)) return false;
		if (!currencyList.contains(currency)) return false;
		if (!typeList.contains(type)) return false;
		return true;
	}

	@Override
	public String toString() {
		String s = super.toString();
		s += ", uniqueName = " + uniqueName ;
		s += ", startDateTime " + startDateTime;
		return s;
	}

}