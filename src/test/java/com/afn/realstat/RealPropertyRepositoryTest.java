package com.afn.realstat;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("dev")
public class RealPropertyRepositoryTest {

	@Autowired
	private RealPropertyRepository rpRepo;

	@Test
	public void testSaveOrUpdateNew() {

		// test creation of new object
		String apn1 = "apn-01";
		RealProperty rp = new RealProperty(apn1);
		String fn = "new";
		rp.setFirstName(fn);
		rpRepo.saveOrUpdate(rp);
		List<RealProperty> list = rpRepo.findByApn(apn1);
		assertEquals(1, list.size());
		assertEquals(apn1, list.get(0).getApn());
		assertEquals(fn, list.get(0).getFirstName());

		// test updating of object
		rp = new RealProperty(apn1);
		fn = "updated";
		rp.setFirstName(fn);
		rpRepo.saveOrUpdate(rp);
		list = rpRepo.findByApn(apn1);
		assertEquals(1, list.size());
		assertEquals(apn1, list.get(0).getApn());
		assertEquals(fn, list.get(0).getFirstName());
	}

	@Test
	public void testcountByZipCityLandUseCloseYear() {
		long count = rpRepo.countByZipCityLandUseCloseYear("94611", "PIEDMONT", "Single Family Residential");
		System.out.println("Resulting count = " + count);
	}

}
