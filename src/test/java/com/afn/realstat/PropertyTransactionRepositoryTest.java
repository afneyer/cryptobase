package com.afn.realstat;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("dev")
public class PropertyTransactionRepositoryTest {

	@Autowired
	private PropertyTransactionRepository ptRepo;

	@Autowired
	private AgentRepository agtRepo;

	private Agent agt = new Agent("12345", "John Smith");

	@Before
	public void setUp() {

		agt = new Agent("12345", "John Smith");
		agtRepo.save(agt);

		// create test data
		PropertyTransaction pt = new PropertyTransaction(1001);
		pt.setListingAgent(agt);
		ptRepo.saveOrUpdate(pt);

		pt = new PropertyTransaction(1002);
		pt.setListingAgent(agt);
		ptRepo.saveOrUpdate(pt);

		pt = new PropertyTransaction(1003);
		pt.setListingAgent(agt);
		ptRepo.saveOrUpdate(pt);

		pt = new PropertyTransaction(1004);
		pt.setListingAgent(agt);
		ptRepo.saveOrUpdate(pt);

		pt = new PropertyTransaction(1005);
		ptRepo.saveOrUpdate(pt);

		pt = new PropertyTransaction(1006);
		ptRepo.saveOrUpdate(pt);

		pt = new PropertyTransaction(1007);
		pt.setSellingAgent(agt);
		pt.setListingAgent(agt);
		ptRepo.saveOrUpdate(pt);

	}

	@Test
	public void testFindallPropertyTransbyAgent() {
		List<PropertyTransaction> ptList = ptRepo.findAllTransactionsByAgent(agt);
		assertEquals(5, ptList.size());

	}
}