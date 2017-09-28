package com.afn.realstat.sandbox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.afn.Application;
import com.afn.realstat.AbstractEntityRepository;
import com.afn.realstat.Agent;
import com.afn.realstat.AgentRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("dev")
public class TypedEntityTest {
	
	@Autowired 
	AgentRepository agtRepo;

	@Test
	public void testTypedEntityType() {
		
		
		TypedEntity<Agent> e = new TypedEntity<Agent>(Agent.class,agtRepo);
		
		Class<Agent> typedClass = e.getTypedClass();
		assertEquals(Agent.class, typedClass);
		
	}
	
	@Test
	public void testRepository() {
		
		TypedEntity<Agent> e = new TypedEntity<Agent>(Agent.class, agtRepo);
		AbstractEntityRepository<Agent> repo = e.getRepo();
		assertNotNull(repo);	
		
	}

	
	
	
}
