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
import org.springframework.transaction.PlatformTransactionManager;

@Component
public class PropertyTransactionManager extends AbstractEntityManager<PropertyTransaction> {

	public static final Logger importLog = LoggerFactory.getLogger("import");

	@Autowired
	PropertyTransactionRepository ptRepo;

	@Autowired
	RealPropertyRepository rpRepo;

	@Autowired
	AgentRepository agtRepo;

	@PersistenceContext
	EntityManager em;

	@PersistenceUnit
	EntityManagerFactory emf;

	@Autowired
	PlatformTransactionManager ptm;

	public PropertyTransactionManager(PropertyTransactionRepository ptRepo) {
		repo = ptRepo;
	}

	public void cleanAndLinkAll() {
		
		Function<PropertyTransaction, Boolean> cleanAndLink = pt -> {
			return this.cleanAndLink(pt);
		};
		
		performActionOnEntities(cleanAndLink);
	}
	
	public List<PropertyTransaction> getAllPropertyTransactionsIterable(int offset, int max) {
		final TypedQuery<PropertyTransaction> tq = em.createQuery("SELECT pt FROM PropertyTransaction pt",
				PropertyTransaction.class);
		tq.setFirstResult(offset);
		tq.setMaxResults(max);
		List<PropertyTransaction> result = tq.getResultList();

		return result;
	}

	public void iterateAll() {
		int maxBatches = 1;
		int batchNbr = 0;
		int batchSize = 1000;

		List<PropertyTransaction> listPt;

		listPt = getAllPropertyTransactionsIterable(batchNbr*batchSize, batchSize);
		while (listPt.size() > 0 && batchNbr < maxBatches) {

			performEntityAction(listPt);

			batchNbr++;
			listPt = getAllPropertyTransactionsIterable(batchNbr*batchSize, batchSize);
		}
	}

	public void printTransactions () {
		System.out.println(this);
	}
	
	@Transactional
	public void performEntityAction(List<PropertyTransaction> entityList) {

		int i = 0;
		for (PropertyTransaction pt : entityList) {

			// DebugUtils.transactionRequired("PropertyTransactionManager.performEntityAction");
			importLog.info("index =" + i + " iterating over PT=" + pt.toString());
			cleanAndLink( pt );
			// System.out.println(pt);
			// ptRepo.save(pt);
			i++;
		}
		ptRepo.save(entityList);
		ptRepo.flush();

	}
	
	public Boolean cleanAndLink( PropertyTransaction pt) {
		linkPropertyTransactionToRealProperty(pt);
		linkPropertyTransactionToAgents(pt);
		pt.setZip5();
		return true;
	}

	public void linkPropertyTransactionToRealProperty(PropertyTransaction pt) {

		RealProperty rp = null;
		String apnClean = RealStatUtil.cleanApn(pt.getApn());
		List<RealProperty> list = rpRepo.findByApnClean(apnClean);

		if (list.size() == 1) {
			rp = list.get(0);
			pt.setRealProperty(rp);
			importLog.info("Property Transaction Linked By APN= " + pt.toString());
			return;
		}

		if (list.size() == 0) {
			// no corresponding property found
			importLog.info("No property found by APN=" + pt.getApn() + " apnClean =" + apnClean
					+ " for propertyTransaction = " + pt.toString());
			this.linkPropertyTransactionToRealPropertyByAddress(pt);
			return;
		}

		for (RealProperty rp1 : list) {
			importLog.info("Multiple properties by APN! pt = " + pt.toString() + "rp = " + rp1.toString());
			this.linkPropertyTransactionToRealPropertyByAddress(pt);
			return;
		}

	}

	// @Transactional
	public void linkPropertyTransactionToRealPropertyByAddress(PropertyTransaction pt) {

		RealProperty rp = null;
		AddressParser adrClean = new AddressParser(pt.getAddress(), pt.getUnit(), pt.getCity(), pt.getZip());
		String adrCleanStr = adrClean.getCleanAddress();
		List<RealProperty> list = rpRepo.findByAddressClean(adrCleanStr);

		if (list.size() == 1) {
			rp = list.get(0);
			pt.setRealProperty(rp);
			importLog.info("Property Transaction Linked By AddressParser= " + pt.toString());
			return;
		}

		if (list.size() == 0) {
			// no corresponding property found
			importLog.warn("No property found by adress raw=" + adrClean.getRawAddress() + "  clean="
					+ adrClean.getCleanAddress() + "propTrans=" + pt.toString());
			return;
		}

		for (RealProperty rp1 : list) {
			importLog.warn("Multiple properties by Adress! pt = " + pt.toString() + "rp = " + rp1.toString());
			return;
		}

	}

	// @Transactional
	public void linkPropertyTransactionToAgents(PropertyTransaction pt) {

		// Link Listing Agent
		{
			Function<PropertyTransaction, String> licenseGetter = pt1 -> {
				return pt1.getListingAgentLicenseId();
			};
			Function<PropertyTransaction, String> agentNameGetter = pt1 -> {
				return pt1.getListingAgentName();
			};
			BiFunction<PropertyTransaction, Agent, String> agentSetter = (pt1, agt) -> {
				pt.setListingAgent(agt);
				return null;
			};
			linkPropertyTransactionToAgent(pt, licenseGetter, agentNameGetter, agentSetter);
		}

		// Link Selling Agent
		{
			Function<PropertyTransaction, String> licenseGetter = pt1 -> {
				return pt1.getSellingAgent1LicenseId();
			};
			Function<PropertyTransaction, String> agentNameGetter = pt1 -> {
				return pt1.getSellingAgent1Name();
			};
			BiFunction<PropertyTransaction, Agent, String> agentSetter = (pt1, agt) -> {
				pt.setSellingAgent(agt);
				return null;
			};
			linkPropertyTransactionToAgent(pt, licenseGetter, agentNameGetter, agentSetter);
		}
		// Link Listing Agent1
		{
			Function<PropertyTransaction, String> licenseGetter = pt1 -> {
				return pt1.getCoListAgentLicenseId();
			};
			Function<PropertyTransaction, String> agentNameGetter = pt1 -> {
				return pt1.getCoListAgentName();
			};
			BiFunction<PropertyTransaction, Agent, String> agentSetter = (pt1, agt) -> {
				pt.setListingAgent2(agt);
				return null;
			};
			linkPropertyTransactionToAgent(pt, licenseGetter, agentNameGetter, agentSetter);
		}
		// Link Selling Agent1
		{
			Function<PropertyTransaction, String> licenseGetter = pt1 -> {
				return pt1.getCoSellAgentLicenseId();
			};
			Function<PropertyTransaction, String> agentNameGetter = pt1 -> {
				return pt1.getCoSellAgentName();
			};
			BiFunction<PropertyTransaction, Agent, String> agentSetter = (pt1, agt) -> {
				pt.setSellingAgent2(agt);
				return null;
			};
			linkPropertyTransactionToAgent(pt, licenseGetter, agentNameGetter, agentSetter);
		}
	}

	// @Transactional
	public void linkPropertyTransactionToAgent(PropertyTransaction pt, Function<PropertyTransaction, String> licGetter,
			Function<PropertyTransaction, String> agentNameGetter,
			BiFunction<PropertyTransaction, Agent, String> agentSetter) {
		Agent agt = null;
		String lic = licGetter.apply(pt);
		String name = agentNameGetter.apply(pt);

		// only try to link if name or license is not null
		if (lic != null || name != null) {

			// correct data issues (should really go into the importer) TODO
			String outOfAreaLic = "9999915";
			String outOfAreaName = "Outofareafn Outofarealn";
			String lcName = name.toLowerCase();
			if (lcName.startsWith("out of") || lcName.startsWith("non member") || lcName.startsWith("_ nonmember")
					|| lcName.startsWith("non-member")) {
				lic = outOfAreaLic;
				name = outOfAreaName;
			}

			// find or create agent
			agt = new Agent(lic, name);
			agt = agtRepo.saveOrUpdate(agt);

			if (agt != null) {
				agentSetter.apply(pt, agt);
				importLog.info("Property Transaction Linked by LicenseId= " + pt.toString() + "  Using: ",
						licGetter.toString());
			}

		}
	}

}
