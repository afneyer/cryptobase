package com.afn.realstat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Point;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("dev")
public class RealPropertyTest {

	@Autowired
	RealPropertyRepository rpRepo;

	// TODO: create the required property
	// TODO: fix lat / lon
	@Test
	public void testSpatial() {
 
		Point p1 = new Point(37.816977, -122.220519);
		p1.toString();
		// get 4395 Piedmont Ave #309
		/*
		List<RealProperty> rpList = rpRepo.findByApnClean("13111653");
		RealProperty rp = rpList.get(0);
		rp.setLocation(p1);
		rpRepo.save(rp);

		rpList = rpRepo.findByApnClean("13111653");
		assertEquals("Andreas", rp.getFirstName());
		assertEquals(37.816977, rp.getLocation().getX(), 0.0000001);
		assertEquals(-122.220519, rp.getLocation().getY(), 0.0000001);
		*/

	}

}
