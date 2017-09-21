package com.afn.cryptobase.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.afn.realstat.AbstractEntityManager;

@Component
public class MoneroManager extends AbstractEntityManager<MoneroBlock> {

	public static final Logger log = LoggerFactory.getLogger("import");

	@Autowired
	MoneroBlockRepository repo;

	public MoneroManager(MoneroBlockRepository rpRepo) {
		repo = rpRepo;
	}
}
