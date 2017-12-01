package com.afn.cryptobase.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.commons.math3.primes.Primes;
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
@ActiveProfiles("dev")
@WebAppConfiguration
public class PrimeNumberTest {

	@Autowired
	ContractRepository cRepo;

	@Test
	public void testPrimesLibrary() {

		// Primes.isPrime(int i);
		assertFalse(Primes.isPrime(0));
		assertFalse(Primes.isPrime(1));
		assertTrue(Primes.isPrime(2));
		assertTrue(Primes.isPrime(3));
		assertFalse(Primes.isPrime(4));
		assertTrue(Primes.isPrime(5));
		assertFalse(Primes.isPrime(6));
		assertTrue(Primes.isPrime(7));
		assertFalse(Primes.isPrime(8));
		assertFalse(Primes.isPrime(9));
		assertFalse(Primes.isPrime(10));

		assertEquals(Primes.nextPrime(10), 11);
		assertEquals(Primes.nextPrime(11), 11);
		assertEquals(Primes.nextPrime(12), 13);

		List<Integer> primes = Primes.primeFactors(450);

		assertEquals(primes.get(0), new Integer(2));
		assertEquals(primes.get(1), new Integer(3));
		assertEquals(primes.get(2), new Integer(3));
		assertEquals(primes.get(3), new Integer(5));
		assertEquals(primes.get(4), new Integer(5));

	}

}
