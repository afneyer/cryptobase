package com.afn.realstat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface ArtifactRepository extends AbstractEntityRepository<Artifact> {

	public static final Logger log = LoggerFactory.getLogger("app");

	// List<Agent> findByLicense(String license);



}