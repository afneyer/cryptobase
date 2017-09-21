package com.afn.realstat.sandbox;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.afn.realstat.Application;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("dev")
public class EvaluateEntityManagerTest {

	@Autowired
	private CustomerRepository cRepo;
	
	@Autowired
	private EvaluateEntityManager ee;
	
	 /**
     * Sets up the test fixture. 
     * (Called before every test case method.)
     */
    @Before
    public void setUp() {
    	cRepo.deleteAll();
		cRepo.flush();       
    }


	@Test
	public void testEmQuery() {
		
		long count;

		count = ee.emQuery();
		assertEquals(count,0);
		
		// create a few customers
		Customer c = new Customer("Andreas", "Neyer");
		count = ee.emQuery();
		assertEquals(0,count);
		
		cRepo.save(c);
		count = ee.emQuery();
		assertEquals(1,count);

		c = new Customer("Kathleen", "Callahan");
		cRepo.save(c);
		count = ee.emQuery();
		assertEquals(2,count);
		
	}

	
	@Test
	public void testCreateCustomersWithRepoOneByOne() {
		
		int numCust = 1000;
		ee.createCustomersWithRepoOneByOne(numCust);
		long count = ee.emQuery();
		assertEquals(numCust, count);
		
	}
	
	@Test
	public void testCreateCustomersWithSaveAfterWards() {

		int numCust = 1000;
		ee.createCustomersWithSaveAfterWards(numCust);
		long count = ee.emQuery();
		assertEquals(numCust, count);
		
	}
	
	@Test
	public void testCreateCustomersWithIntermediateSaves() {
		int numCust = 1000;
		int batchSize = 100;
		ee.createCustomersWithIntermediateSaves(numCust, batchSize, false);
		long count = ee.emQuery();
		assertEquals(numCust, count);
	}
	
	@Test
	public void testCreateCustomersWithEntityMangerTransaction() {

		int numCust = 1000;
		ee.createCustomersWithEntityManagerTransaction(numCust);
		long count = ee.emQuery();
		assertEquals(numCust, count);
		
	}
	
	/* TODO remove
	@Test
	public void createCustomersWithAnnotatedTransaction() {

		int numCust = 1000;
		ee.createCustomersWithAnnotatedTransaction(numCust);
		long count = ee.emQuery();
		assertEquals(numCust, count);
		
	}
	
	@Test
	public void createCustomersWithEntityManagerTransaction() {

		int numCust = 1000;
		ee.createCustomersWithPtm(numCust);
		long count = ee.emQuery();
		assertEquals(numCust, count);
		
	}
	*/
	
}
