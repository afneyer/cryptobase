package com.afn.realstat;

import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.afn.Application;
import com.afn.realstat.sandbox.Customer;
import com.afn.realstat.sandbox.CustomerManager;
import com.afn.realstat.sandbox.CustomerRepository;
import com.querydsl.core.types.Predicate;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("dev")
public class AbstractEntityManagerTest {
	
	@Autowired
	CustomerRepository cRepo;
	
	@Autowired
	CustomerManager cMgr;
	
	@Before
	public void setUp() {
		cRepo.deleteAll();
		cRepo.flush();
		createCustomers(1000);
	}
	
	private void createCustomers(int numCust) {
		for (int i=0; i<numCust; i++) {
			String fn = "Andreas" + String.format("%04d", i);
			String ln = "Neyer" + String.format("%04d", i);
			Customer c = new Customer(fn, ln);
			cRepo.save(c);
		}
		
	}

	@Test
	public void testBatchUpdate() {
		int batchSize = 100;
		int maxBatches = 100;
		
		Function<Customer, Boolean> updateCustomer = c -> {
			c.setFirstName(c.getFirstName() + "_processed");
			System.out.println("Processed Customer" + c);
			return true;
		};
		
		Predicate p = null;
		cMgr.performActionOnEntities(updateCustomer, p, batchSize, maxBatches);
		// TODO test count of processed
	}

}
