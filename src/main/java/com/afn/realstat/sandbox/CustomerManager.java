package com.afn.realstat.sandbox;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.afn.realstat.AbstractEntityManager;
import com.afn.realstat.AddressRepository;

@Component
public class CustomerManager extends AbstractEntityManager<Customer> {

	public static final Logger log = LoggerFactory.getLogger("app");

	@Autowired
	CustomerRepository cRepo;
	
	@Autowired
	AddressRepository adrRepo;

	@PersistenceContext
    EntityManager em;
	
	@PersistenceUnit
	EntityManagerFactory emf;

	public CustomerManager(CustomerRepository cRepo) {
		// repo = cRepo;
	}
}
