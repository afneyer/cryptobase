package com.afn.realstat;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;

import com.afn.realstat.framework.SpringApplicationContext;

@Entity
@Table(name = "app_param", indexes = @Index(name ="idx_key_type", columnList = "paramKey,paramType") )
public class AppParam extends AbstractEntity {

	public static final Logger log = LoggerFactory.getLogger("app");
	public static AppParamRepository repo;

	private String paramKey;
	private String paramType;
	private String paramValue;
	private String paramDescription;

	public AppParam() {
	}

	public AppParam(String key, String type, String value, String description) {
		this.paramKey = key;
		this.paramType = type;
		this.paramValue = value;
		this.paramDescription = description;
	}

	public String toString() {
		String param = paramKey;
		param += " " + paramType;
		param += " " + paramValue;
		return param;

	}

	@Override
	public void clean() {
	}

	@Override
	public boolean isValid() {
		return true;
	}

	@Override
	public Example<AppParam> getRefExample() {
		AppParam param = new AppParam();
		param.paramKey = paramKey;
		param.paramType = paramType;
		Example<AppParam> e = Example.of(param);
		return e;
	}

	public String getParamKey() {
		return paramKey;
	}

	public void setParamKey(String key) {
		this.paramKey = key;
	}

	public String getParamType() {
		return paramType;
	}

	public void setParamType(String paramType) {
		this.paramType = paramType;
	}

	public String getParamValue() {
		return paramValue;
	}

	public void setParamValue(String value) {
		this.paramValue = value;
	}

	public String getParamDescription() {
		return paramDescription;
	}

	public void setParamDescription(String description) {
		this.paramDescription = description;
	}

	public AppParamRepository getRepo() {
		if (repo == null) {
			repo =  (AppParamRepository) SpringApplicationContext.getBean("appParamRepository");
		}
		return repo;
	}

	@Override
	public void save() {
		getRepo().save(this);
		
	}

	@Override
	public void saveOrUpdate() {
		getRepo().save(this);
	}
}
