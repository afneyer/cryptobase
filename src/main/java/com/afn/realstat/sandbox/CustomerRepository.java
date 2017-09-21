package com.afn.realstat.sandbox;

import java.util.List;

import com.afn.realstat.AbstractEntityRepository;

public interface CustomerRepository extends AbstractEntityRepository<Customer> {

	List<Customer> findByLastNameStartsWithIgnoreCase(String lastName);
	long count();
}
