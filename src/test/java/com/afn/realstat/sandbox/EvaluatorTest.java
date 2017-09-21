package com.afn.realstat.sandbox;

import org.junit.Test;

public class EvaluatorTest {
	
	@Test
	public void regexMatching() {
		String s = "94708";
		boolean test = s.matches("94[5678]..");
		
		s="10:30-12:30";
		test = s.matches("^[0-9\\-:]{1,12}$");
		System.out.println(test);
		
		s="10abe-12:30";
		test = s.matches("^[0-9\\-:]{1,12}$");
		System.out.println(test);
	}

}
