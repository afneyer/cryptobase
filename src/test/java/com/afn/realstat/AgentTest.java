	package com.afn.realstat;



import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
	
// @RunWith(SpringRunner.class)
// @SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("dev")
public class AgentTest {

	@Test
	public void testToString() {
		Agent agt = new Agent("R01969778", "NEYER, ANDREAS");
		assertEquals("NEYER, ANDREAS",agt.getAgentName());
		assertEquals("Andreas", agt.getFirstName());
		assertEquals(null, agt.getMiddleName());
		assertEquals(null, agt.getMiddleInitial(), null);
		assertEquals("Neyer", agt.getLastName());
		assertEquals("1969778",agt.getLicense());
		
		agt = new Agent("00792768", "cALLAHAN, kathleen mArie");
		assertEquals( "cALLAHAN, kathleen mArie",agt.getAgentName());
		assertEquals( "Kathleen", agt.getFirstName());
		assertEquals( "Marie",agt.getMiddleName());
		assertEquals("M", agt.getMiddleInitial());
		assertEquals("Callahan",agt.getLastName());
		assertEquals("792768",agt.getLicense());
	}
	
	@Test 
	public void testHasValidLicense() {
		
		Agent agt;
		
		// valid agent licenses
		agt = new Agent("r01969778",null);
		assertTrue(agt.hasValidLicense());
		agt = new Agent("1969778",null);
		assertTrue(agt.hasValidLicense());
		agt = new Agent("888",null);
		assertTrue(agt.hasValidLicense());
		
		// invalid agent licenses
		agt = new Agent(null,null);
		assertFalse(agt.hasValidLicense());
		agt = new Agent("",null);
		assertFalse(agt.hasValidLicense());
		agt = new Agent("ABCD",null);
		assertFalse(agt.hasValidLicense());
		
		
	}
	
	@Test 
	public void testHasValidName() {
		
		Agent agt;
		
		// valid agent names
		agt = new Agent("1969778","Andreas Neyer");
		assertTrue(agt.hasValidName());
		agt = new Agent("1969778","Andreas","Neyer");
		assertTrue(agt.hasValidName());
		agt = new Agent("1969778","Andreas F. Neyer");
		assertTrue(agt.hasValidName());
		
		// invalid agent names
		agt = new Agent("1969778","");
		assertFalse(agt.hasValidName());
		agt = new Agent("1969778","         ");
		assertFalse(agt.hasValidName());
		agt = new Agent("1969778","      ","       ");
		assertFalse(agt.hasValidName());
		agt = new Agent("1969778",null);
		assertFalse(agt.hasValidName());
		agt = new Agent("1969778",null,null);
		assertFalse(agt.hasValidName());
	}
	
	@Test
	public void testCleanAgentName() {
		
		Agent agt;
		agt = new Agent("1969778","NEYER, ANDREAS F");
		agt.cleanAgentName();
		assertEquals("Neyer, Andreas F", agt.getAgentName());
		
		agt = new Agent("1969778","  NEYER,  ANDREAS    F");
		agt.cleanAgentName();
		assertEquals("Neyer, Andreas F", agt.getAgentName());
		
		agt = new Agent("1969778","  nEYER,  aNDREAS    F");
		agt.cleanAgentName();
		assertEquals("Neyer, Andreas F", agt.getAgentName());
		
		agt = new Agent("1969778","O'nEYER,  aNDREAS    F");
		agt.cleanAgentName();
		assertEquals("O'Neyer, Andreas F", agt.getAgentName());
		
	
	}
}
