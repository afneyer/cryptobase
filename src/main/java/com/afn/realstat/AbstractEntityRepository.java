package com.afn.realstat;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface AbstractEntityRepository<T extends AbstractEntity>
		extends JpaRepository<T, Long>, QueryDslPredicateExecutor<T> {
	
	Page<T> findAll(Pageable pageable);

	public static final Logger log = LoggerFactory.getLogger("app");

	public default T saveOrUpdate(T newEntity) {

		newEntity.clean();
		List<T> existingEntities = getExistingEntities(newEntity);
		if (existingEntities.size() > 1) {
			log.error("Non-unique entries for table " + newEntity.getClass().getName() + newEntity.toString());
			return null;
		}

		if (existingEntities.size() == 1) {
			log.info("Found exisiting entity for " + newEntity.getClass().getName() + newEntity.toString());
			newEntity = updateEntity(existingEntities.get(0), newEntity);
			log.info("Updated exisiting entity for " + newEntity.getClass().getName() + newEntity.toString());
			return newEntity;
		} else {
			log.info("No exisiting entity for " + newEntity.getClass().getName() + newEntity.toString());
			save(newEntity);
			log.info("Created new entity for " + newEntity.getClass().getName() + newEntity.toString());
			return newEntity;
		}
	}

	// assumes the entity exists in the repository
	public default T updateEntity(T existingEntity, T newEntity) {
		if (existingEntity != null && existingEntity.getId() != null) {
			newEntity.setId(existingEntity.getId());
			save(newEntity);
			return newEntity;
		}
		return null;
	}

	public default List<T> getExistingEntities(T newEntity) {
		@SuppressWarnings("unchecked")
		Example<T> example = newEntity.getRefExample();
		List<T> list = this.findAll(example);
		return list;
	}

}