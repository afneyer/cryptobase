package com.afn.realstat;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.Query;

public interface RealPropertyRepository extends AbstractEntityRepository<RealProperty> {
	
	public static final Logger log = LoggerFactory.getLogger("app");

	List<RealProperty> findByLastNameStartsWithIgnoreCase(String lastName);

	List<RealProperty> findByApn(String apn);
	
	List<RealProperty> findByApnClean(String ApnClean);
	
    List<RealProperty> findByAddressClean(String cleanAddress);
    
    @Query(value = "select count(rp.id) from real_property rp "
    		+ "where propertyZip5 = ?1 and " +
		      "propertyCity = ?2 and landUse = ?3",
		      nativeQuery = true)
    long countByZipCityLandUseCloseYear( String zip, String city, String landUse);
    
}
