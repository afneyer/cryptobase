package com.afn.realstat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface AppParamRepository extends AbstractEntityRepository<AppParam> {

	public static final Logger log = LoggerFactory.getLogger("app");

	AppParam findOneByParamKeyAndParamType(String key, String type);

}