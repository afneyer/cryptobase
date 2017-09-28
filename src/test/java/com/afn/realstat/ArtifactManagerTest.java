package com.afn.realstat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.afn.Application;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("dev")
public class ArtifactManagerTest {

	@Autowired
	ArtifactManager artfctMgr;
	
	@Autowired
	ArtifactRepository artfctRepo;
	
	@Before
	public void intialize() {
		artfctRepo.deleteAll();
	}

	@Test
	public void testUploadIcons() {
		artfctMgr.uploadIcons();
	}
}
