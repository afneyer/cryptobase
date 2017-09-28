package com.afn.realstat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.afn.Application;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("dev")
public class AgentRepositoryTest {

	@Autowired
	AgentRepository agtRepo;

	@Test
	@Transactional
	public void testSaveOrUpdate() {

		Agent origAgt;
		Agent savedAgt;
		Agent testAgt;
		List<Agent> list;
		String oc1 = "OfficeCode1";
		String oc2 = "OfficeCode2";
		String oc3 = "OfficeCode3";
		String on1 = "OfficeName1";
		String lic1 = "9000001";
		String lic2 = "9000002";
		String firstName1 = "Andreasxxx";
		String lastName1 = "Neyerxxx";
		
		String firstName2 = "Kathleenxxx";
		String lastName2 = "Callahanxxx";

		/* 
		 * Base case create and save a new agent
		 */
		origAgt = new Agent(lic1, firstName1, lastName1);
		origAgt.setOfficeCode(oc1);
		savedAgt = agtRepo.saveOrUpdate(origAgt);

		assertNotNull(savedAgt);
		list = agtRepo.findByLicense(lic1);
		testAgt = list.get(0);
		assertEquals(1, list.size());
		assertEquals(savedAgt.getId(), testAgt.getId());
		assertEquals(oc1, testAgt.getOfficeCode());

		/*
		 * Do the same agent again, the same entity is updated
		 */
		origAgt = new Agent(lic1, firstName1, lastName1);
		origAgt.setOfficeCode(oc2);
		origAgt.setOfficeName(on1);
		savedAgt = agtRepo.saveOrUpdate(origAgt);

		assertNotNull(savedAgt);
		list = agtRepo.findByLicense(lic1);
		assertEquals(1, list.size());
		testAgt = list.get(0);
		assertEquals(savedAgt.getId(), testAgt.getId());
		assertEquals(oc2, testAgt.getOfficeCode());
		assertEquals(on1, testAgt.getOfficeName());

		/*
		 * Do the same again without a license, should update the same entity
		 */
		origAgt = new Agent(null, firstName1, lastName1);
		origAgt.setOfficeCode(oc3);
		origAgt.setOfficeName(on1);
		savedAgt = agtRepo.saveOrUpdate(origAgt);

		assertNotNull(savedAgt);
		list = agtRepo.findByFirstNameAndLastName(firstName1, lastName1);
		assertEquals(1, list.size());
		testAgt = list.get(0);
		assertEquals(savedAgt.getId(), testAgt.getId());
		assertEquals(oc3, testAgt.getOfficeCode());
		assertEquals(on1, testAgt.getOfficeName());
		
		/*
		 * New name without license, should create new record
		 */
		origAgt = new Agent(null, firstName2, lastName2);
		origAgt.setOfficeCode(oc1);
		savedAgt = agtRepo.saveOrUpdate(origAgt);

		assertNotNull(savedAgt);
		list = agtRepo.findByFirstNameAndLastName(firstName2, lastName2);
		assertEquals(1, list.size());
		testAgt = list.get(0);
		assertEquals(savedAgt.getId(), testAgt.getId());
		assertEquals(oc1, testAgt.getOfficeCode());
		
		/*
		 * Use the same name again this time with license
		 * Should use the same record and update license and office code
		 */
		origAgt = new Agent(lic2, firstName2, lastName2);
		origAgt.setOfficeCode(oc2);
		savedAgt = agtRepo.saveOrUpdate(origAgt);

		assertNotNull(savedAgt);
		list = agtRepo.findByFirstNameAndLastName(firstName2, lastName2);
		assertEquals(1, list.size());
		testAgt = list.get(0);
		assertEquals(savedAgt.getId(), testAgt.getId());
		assertEquals(oc2, testAgt.getOfficeCode());
		assertEquals(lic2, testAgt.getLicense());

	}

}
