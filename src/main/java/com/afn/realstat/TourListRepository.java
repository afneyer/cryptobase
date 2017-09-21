package com.afn.realstat;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.Query;

public interface TourListRepository extends AbstractEntityRepository<TourListEntry> {

	public static final Logger log = LoggerFactory.getLogger("app");

	List<TourListEntry> findByTourDate(Date tourDate);

	// returns all transactions an agent participated in
	@Query(value = "select distinct tourDate from TourListEntry order by tourDate desc", nativeQuery = false)
		List<Date> findAllDisctintDatesNewestFirst();

}
