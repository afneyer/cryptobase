package com.afn.realstat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.afn.Application;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("prod")
@WebAppConfiguration
public class AddressManagerTest {

	@Autowired
	AddressManager adrMgr;

	@Test
	public void testCleanAndFixAddresses() {
		adrMgr.cleanAndFixAddresses();
	}

}
