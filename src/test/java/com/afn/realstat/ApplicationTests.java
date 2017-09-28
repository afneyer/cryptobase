package com.afn.realstat;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.afn.Application;
import com.afn.cryptobase.core.MoneroBlockApi;
import com.afn.realstat.sandbox.CustomerRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("dev")
@WebAppConfiguration
public class ApplicationTests {

    @Autowired
    private CustomerRepository repository;
    
    @Autowired
    MoneroBlockApi api;

    @Test
    // Sample test: TODO remove once a new test is written
    public void shouldFindTwoBauerCustomers() {
        then(this.repository.findByLastNameStartsWithIgnoreCase("Bauer")).hasSize(0);
    }
}
