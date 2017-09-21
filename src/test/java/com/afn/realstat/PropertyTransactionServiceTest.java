package com.afn.realstat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("dev")
public class PropertyTransactionServiceTest {
	
	@Autowired
	private PropertyTransactionService srvc;

	@Test
	// TODO: initial proper test data and check result
	public void getPropertyLocationsForAgent() {
		String license = "792768";
		srvc.getPropertyLocationsForAgent(license);
	}

}
