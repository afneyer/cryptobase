package com.afn.realstat;

import java.util.List;
import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.querydsl.core.types.dsl.BooleanExpression;

@Component
public class RealPropertyManager extends AbstractEntityManager<RealProperty> {

	public static final Logger log = LoggerFactory.getLogger("import");

	@Autowired
	RealPropertyRepository rpRepo;
	
	@Autowired
	AddressRepository adrRepo;

	@PersistenceContext
    EntityManager em;
	
	@PersistenceUnit
	EntityManagerFactory emf;

	public RealPropertyManager(RealPropertyRepository rpRepo) {
		repo = rpRepo;
	}

	private List<RealProperty> getAllEntitiesIterable(int offset, int max) {
		final TypedQuery<RealProperty> tq = em.createQuery("SELECT rp FROM RealProperty rp",
				RealProperty.class);
		tq.setFirstResult(offset);
		tq.setMaxResults(max);
		List<RealProperty> result = tq.getResultList();

		return result;
	}

	@Transactional
	public void iterateAll() {
		int offset = 0;
		int batchSize = 100;

		List<RealProperty> listEntity;
		listEntity = getAllEntitiesIterable(offset, batchSize);
		while (listEntity.size() > 0) {
			for (RealProperty entity : listEntity) {
				log.info("iterating over Entity: Offset = " + offset + " EntityId =" + entity.toString());
				entity.setApnClean(entity.getApn());
				log.info("set RealProperty.apnClean to: " + entity.getApnClean() );
				entity.setAddressClean();
				log.info("set RealProperty.addressClean to: " + entity.getAddressClean() );
				entity.setPropertyZip5();
			}
			offset += batchSize;
			listEntity = getAllEntitiesIterable(offset, batchSize);
		}
	}
	
	public List<RealProperty> findByApnClean( String apn ) {
		List<RealProperty> rp = rpRepo.findByApnClean(apn);
		return rp;
	}
	
	public void linkToAddresses() {		
		Function<RealProperty, Boolean> createAndLinkAddress = rp -> {
			return this.createAndLinkAddress(rp);
		};
		performActionOnEntities(createAndLinkAddress);
	}
	
	public Boolean createAndLinkAddress(RealProperty rp) {
		if (rp.getPropertyAdr() == null) {
			Address adr = new Address(rp.getPropertyAddress(), rp.getPropertyCity(), rp.getPropertyZip());
			adr = adrRepo.saveOrUpdate(adr);
			rp.setAddress(adr);
			System.out.println("Creating and linking address: " + adr);
		}
		return true;
	}
	
	public void cleanAndFixAddresses() {
		
		Function<RealProperty, Boolean> cleanAndFixAddress = rp -> {
			return this.cleanAndFixAddressUsingMapLocations(rp);
		};
		
		QRealProperty rpq = QRealProperty.realProperty;
		BooleanExpression predicate = rpq.propertyAdr.isNull();
		performActionOnEntities(cleanAndFixAddress, predicate);
	}
	
	public Boolean cleanAndFixAddressUsingMapLocations(RealProperty rp) {
		if (rp.getPropertyAdr() != null) {
			Address adr = rp.getPropertyAdr();
			adr.setMapLocationFields();
			System.out.println("Fixing address using MapLocations: " + adr);
			return true;
		}
		return false;
	}
}
