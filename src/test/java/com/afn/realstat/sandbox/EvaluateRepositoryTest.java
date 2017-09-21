package com.afn.realstat.sandbox;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.afn.realstat.Application;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("dev")
public class EvaluateRepositoryTest {
	 
	 @Autowired
	 private EvaluateRepository testSample;

	@Test
	public void testJpaSetup() {
		testSample.saveOrUpdateEntity();
	}
}
