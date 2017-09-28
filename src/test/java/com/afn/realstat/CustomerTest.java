	package com.afn.realstat;



import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.afn.Application;
import com.afn.realstat.sandbox.Customer;
import com.afn.realstat.sandbox.CustomerRepository;
	
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("prod")
public class CustomerTest {
	
	@Autowired
	CustomerRepository repo;

	@Test
	public void testSave() {
		
		List<Customer> clist = repo.findByLastNameStartsWithIgnoreCase("Neyer");
		repo.delete(clist);	
		
				clist = repo.findByLastNameStartsWithIgnoreCase("Neyer");
		assertEquals(0, clist.size());
		
		Customer c = new Customer("Andreas", "Neyer");
		c.save();
		
		// should do an sqlQuery
		clist = repo.findByLastNameStartsWithIgnoreCase("Neyer");
		assertEquals(1, clist.size());
		
	}
	
}
