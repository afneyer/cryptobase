package com.afn.realstat;

import java.util.function.Function;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.querydsl.core.types.Predicate;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("prod")
public class PropertyTransactionManagerTest {

	@Autowired
	PropertyTransactionManager pm;
	
	@Autowired
	PropertyTransactionRepository ptr;
	
	@Test
	public void testCleanAndLinkAll() {
		
		pm.cleanAndLinkAll();
		
	}
	
	@Test
	public void testCleanAndLinkPartialForPerformance() {
		int batchSize = 100;
		int maxBatches = 10;
		
		Function<PropertyTransaction, Boolean> cleanAndLink = pt -> {
			pm.cleanAndLink(pt);
			return true;
		};
		
		Predicate p = null;
		pm.performActionOnEntities(cleanAndLink, p, batchSize, maxBatches);
	}

}
