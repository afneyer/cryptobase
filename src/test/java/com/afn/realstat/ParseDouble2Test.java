package com.afn.realstat;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.supercsv.cellprocessor.ParseDouble2;

public class ParseDouble2Test {

	@Test
	public void testTranslateCsvFileDefault() {
		
		ParseDouble2 parser = new ParseDouble2();

		assertEquals("2.0", parser.cleanStrDouble2("2.0"));
		assertEquals(null, parser.cleanStrDouble2("MONTCL"));
		assertEquals("3", parser.cleanStrDouble2("*3"));
		assertEquals(null,parser.cleanStrDouble2("."));
		
	}

}
