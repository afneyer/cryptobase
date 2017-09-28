package com.afn.realstat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.afn.Application;
import com.querydsl.core.types.Predicate;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("prod")
public class AgentLovTest {

	@Autowired
	AgentRepository agtRepo;

	@Test
	public void testCountQuery() {
		int size = size("Ca");
		System.out.println(size);
	}
	
	public int size(String filter) {
    	QAgent qa = QAgent.agent;
    	Predicate predicate = qa.agentName.startsWith(filter);

    	long count = agtRepo.count(predicate);
        return (int) count;
    };
}
