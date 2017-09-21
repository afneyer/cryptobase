package com.afn.realstat.sandbox;

import org.springframework.stereotype.Component;

import com.afn.realstat.AbstractEntity;
import com.afn.realstat.AbstractEntityRepository;

@Component
public class TypedEntity<T extends AbstractEntity> {

	private Class<T> typedClass = null;

	AbstractEntityRepository<T> repo;
	
	public TypedEntity() {
	}

	public TypedEntity(Class<T> t, AbstractEntityRepository<T> repo) {
		typedClass = t;
		this.repo = repo;
	}

	public Class<T> getTypedClass() {
		return typedClass;
	}

	public AbstractEntityRepository<T> getRepo() {
		return repo;
	}

}
