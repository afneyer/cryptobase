package com.afn.realstat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class PersNameTest {

	@Test
	public void nameLibraryTest() {
		
		javatools.parsers.Name.PersonName n;// split by blank

		n = new javatools.parsers.Name.PersonName("Sergio Vieira de Mello");
		assertEquals("Sergio Vieira Mello", n.normalize());
		assertEquals("Mello", n.familyName());
		assertEquals("Sergio Vieira", n.givenNames());
		assertEquals("Sergio", n.givenName());
		
		n = new javatools.parsers.Name.PersonName("Andreas Neyer");
		assertEquals("Andreas Neyer", n.normalize());
		assertEquals("Neyer", n.familyName());
		assertEquals("Andreas", n.givenNames());
		assertEquals("Andreas", n.givenName());
		
		n = new javatools.parsers.Name.PersonName("Andreas F. Neyer");
		assertEquals("Andreas F. Neyer", n.normalize());
		assertEquals("Neyer", n.familyName());
		assertEquals("Andreas F.", n.givenNames());
		assertEquals("Andreas", n.givenName());
		
		n = new javatools.parsers.Name.PersonName("Andreas F Neyer");
		assertEquals("Andreas F Neyer", n.normalize());
		assertEquals("Neyer", n.familyName());
		assertEquals("Andreas F", n.givenNames());
		assertEquals("Andreas", n.givenName());
		
		n = new javatools.parsers.Name.PersonName("Andreas Felix Neyer");
		assertEquals("Andreas Felix Neyer", n.normalize());
		assertEquals("Neyer", n.familyName());
		assertEquals("Andreas Felix", n.givenNames());
		assertEquals("Andreas", n.givenName());	
	}
	
	@Test
	public void testConstructor() {

		PersName n;
		
		n = new PersName("- NO DRE# REQUIRED,");
		assertEquals("- No Dre# Required,",n.getNormalizedName());
		assertFalse(n.isValidName());
		
		
		n = new PersName("PERRYMAN, C.J.");
		assertEquals("C. J. Perryman",n.getNormalizedName());
		assertEquals("C",n.getFirstName());
		assertEquals("J", n.getMiddleName());
		assertEquals("J", n.getMiddleInitial());
		assertEquals("Perryman", n.getLastName());
		
		n = new PersName("MALONEY, NANCY A. A");
		assertEquals("Nancy A. A Maloney",n.getNormalizedName());
		assertEquals("Nancy",n.getFirstName());
		assertEquals("A", n.getMiddleName());
		assertEquals("A", n.getMiddleInitial());
		assertEquals("Maloney", n.getLastName());
		
		n = new PersName("O'MELAY, TATIANA");
		assertEquals("Tatiana O'Melay", n.getNormalizedName());
		assertEquals("Tatiana", n.getFirstName());
		assertEquals(null, n.getMiddleName());
		assertEquals(null, n.getMiddleInitial());
		assertEquals("O'Melay", n.getLastName());
		
		n = new PersName("HARPER-COTTON, FREDRIC");
		assertEquals("Fredric Harper-Cotton", n.getNormalizedName());
		assertEquals("Fredric", n.getFirstName());
		assertEquals(null, n.getMiddleName());
		assertEquals(null, n.getMiddleInitial());
		assertEquals("Harper-Cotton", n.getLastName());
		
		n = new PersName("Neyer, Andreas");
		assertEquals("Andreas Neyer", n.getNormalizedName());
		assertEquals("Andreas", n.getFirstName());
		assertEquals(null, n.getMiddleName());
		assertEquals(null, n.getMiddleInitial());
		assertEquals("Neyer", n.getLastName());

		n = new PersName("Andreas F. Neyer");
		assertEquals("Andreas F. Neyer", n.getNormalizedName());
		assertEquals("Andreas", n.getFirstName());
		assertEquals("F", n.getMiddleName());
		assertEquals("F", n.getMiddleInitial());
		assertEquals("Neyer", n.getLastName());


		n = new PersName("Andreas F. Neyer");
		assertEquals("Andreas F. Neyer", n.getNormalizedName());
		assertEquals("Andreas", n.getFirstName());
		assertEquals("F", n.getMiddleName());
		assertEquals("F", n.getMiddleInitial());
		assertEquals("Neyer", n.getLastName());

		n = new PersName("Neyer, Andreas Felix");
		assertEquals("Andreas Felix Neyer", n.getNormalizedName());
		assertEquals("Andreas", n.getFirstName());
		assertEquals("Felix", n.getMiddleName());
		assertEquals("F", n.getMiddleInitial());
		assertEquals("Neyer", n.getLastName());

		n = new PersName("Andreas Felix Neyer");
		assertEquals("Andreas Felix Neyer", n.getNormalizedName());
		assertEquals("Andreas", n.getFirstName());
		assertEquals("Felix", n.getMiddleName());
		assertEquals("F", n.getMiddleInitial());
		assertEquals("Neyer", n.getLastName());

		n = new PersName("NEYER, Andreas    F");
		assertEquals("Andreas F Neyer", n.getNormalizedName());
		assertEquals("Andreas", n.getFirstName());
		assertEquals("F", n.getMiddleName());
		assertEquals("F", n.getMiddleInitial());
		assertEquals("Neyer", n.getLastName());

		n = new PersName("   ANDreas f neyer    ");
		assertEquals("Andreas F Neyer", n.getNormalizedName());
		assertEquals("Andreas", n.getFirstName());
		assertEquals("F", n.getMiddleName());
		assertEquals("F", n.getMiddleInitial());
		assertEquals("Neyer", n.getLastName());

		n = new PersName(" NEYER, ANDREAS    FELIX");
		assertEquals("Andreas Felix Neyer", n.getNormalizedName());
		assertEquals("Andreas", n.getFirstName());
		assertEquals("Felix", n.getMiddleName());
		assertEquals("F", n.getMiddleInitial());
		assertEquals("Neyer", n.getLastName());

		n = new PersName("    Neyer, Andreas fELIX      ");
		assertEquals("Andreas Felix Neyer", n.getNormalizedName());
		assertEquals("Andreas", n.getFirstName());
		assertEquals("Felix", n.getMiddleName());
		assertEquals("F", n.getMiddleInitial());
		assertEquals("Neyer", n.getLastName());
		
		
		
	}

}
