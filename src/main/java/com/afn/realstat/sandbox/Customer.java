package com.afn.realstat.sandbox;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.data.domain.Example;

import com.afn.realstat.AbstractEntity;
import com.afn.realstat.framework.SpringApplicationContext;

@Entity
@Table(name="customer")
public class Customer extends AbstractEntity {	

	private String firstName;

	private String lastName;
	
	private static CustomerRepository repo;
	
	public Customer() {
	}

	public Customer(String firstName, String lastName) {
		
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public String toString() {
		return String.format("Customer[id=%d, firstName='%s', lastName='%s']", this.getId(),
				firstName, lastName);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Example getRefExample() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clean() {
	}

	@Override
	public boolean isValid() {
		return true;
	}
	
	public CustomerRepository getRepo() {
		if (repo == null) {
			repo =  (CustomerRepository) SpringApplicationContext.getBean("customerRepository");
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
	
}
