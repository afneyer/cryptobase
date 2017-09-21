package com.afn.realstat;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;

public interface PropertyTransactionRepository extends AbstractEntityRepository<PropertyTransaction> {

	// List<PropertyTransaction> findById(Long id);
	List<PropertyTransaction> findByApnClean(String apnClean);
	
	List<PropertyTransaction> findByCloseDateBetween(Date d1, Date d2);

	List<PropertyTransaction> findByRealPropertyAndCloseDateBefore(RealProperty rp, Date d, Sort sort);
	
	// returns all transactions an agent participated in
	@Query(value = "select pt from PropertyTransaction pt "
    		+ "where listingAgent_id = ?1 or listingAgent2_id = ?1 "
			+ "or sellingAgent_id = ?1 or sellingAgent2_id = ?1 "
    		+ "order by mlsNo asc",
		      nativeQuery = false)
	List<PropertyTransaction> findAllTransactionsByAgent( Agent agt );

}
