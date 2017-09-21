package com.afn.realstat.sandbox;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author Andreas
 *
 */
@Component
public class EvaluateEntityManager {

	@PersistenceContext
	EntityManager em;

	@Autowired
	PlatformTransactionManager ptm;

	@Autowired
	CustomerRepository cRepo;

	@PersistenceUnit
	private EntityManagerFactory emf;

	public EvaluateEntityManager() {
	}

	/*
	 * @PersistenceUnit public void setEntityManagerFactory(EntityManagerFactory
	 * emf) { this.emf = emf; }
	 */

	public long emQuery() {
		return cRepo.count();
		
		/*
		TypedQuery<Customer> tq = em.createQuery("SELECT c FROM Customer c", Customer.class);
		List<Customer> result = tq.getResultList();
		count = cRepo.count();
		return count;
		*/
	}

	public void createCustomersWithRepoOneByOne(int count) {
		for (int i = 0; i < count; i++) {
			Customer c = new Customer("John", "Smith");
			cRepo.save(c);
		}
	}

	public void createCustomersWithSaveAfterWards(int count) {

		List<Customer> list = new ArrayList<Customer>();
		for (int i = 0; i < count; i++) {
			Customer c = new Customer("John", "Smith");
			list.add(c);
		}
		cRepo.save(list);
	}
	
	public void createCustomersWithIntermediateSaves(int count, int batchSize, boolean flush) {
		
		List<Customer> list = new ArrayList<Customer>();
		for (int i = 1; i <= count; i++) {
			Customer c = new Customer("John", "Smith");
			list.add(c);
			
			if ( i % batchSize == 0 ) {
				System.out.format("%s %5d%n", "Saving batch count =" ,i);
				cRepo.save(list);
				if (flush) {
					cRepo.flush();
				}
			}
		}
		cRepo.save(list);
	}

	public void createCustomersWithEntityManagerTransaction(int count) {

		EntityManager em1 = emf.createEntityManager();

		EntityTransaction et = em1.getTransaction();
		et.begin();
		for (int i = 0; i < count; i++) {
			Customer c = new Customer("John", "Smith");
			em1.merge(c);
		}
		et.commit();
		em1.close();
	}

	/*
	 * The following don't work
	 */
	/*
	@Transactional
	public void createCustomersWithAnnotatedTransaction(int count) {
		for (int i = 0; i < count; i++) {
			new Customer("John", "Smith");
		}
	}

	@Transactional
	public void createCustomersWithPtm(int count) {
		TransactionStatus ts = ptm.getTransaction(null);
		
				for (int i = 0; i < count; i++) {
					new Customer("John", "Smith");
					if (i == 500-1) {
						ptm.commit(ts);
					}
				}

	}
	*/
}
