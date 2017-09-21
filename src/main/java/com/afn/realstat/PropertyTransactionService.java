package com.afn.realstat;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.afn.realstat.util.MapLocation;

@Component
@Transactional
public class PropertyTransactionService {

	public static final Logger log = LoggerFactory.getLogger("app");

	@Autowired
	private PropertyTransactionRepository ptRepo;

	@Autowired
	private AgentRepository agtRepo;

	public PropertyTransactionService() {
	}

	public List<MapLocation> getPropertyLocationsForAgent(String license) {
		List<Agent> agtList = agtRepo.findByLicense(license);
		List<MapLocation> resultList = new ArrayList<MapLocation>();
		if (agtList.size() > 0) {
			Agent agent = agtList.get(0);
			List<PropertyTransaction> ptList = ptRepo.findAllTransactionsByAgent(agent);
			int i = 0;
			for (PropertyTransaction pt : ptList) {
				String address = pt.getAddress();
				System.out.println(address);
				MapLocation loc = new MapLocation(address);
				resultList.add(loc);
				
				// TODO : remove debugging code
				i++;
				if (i==1) {
					return resultList;
				}
			}
		}
		return resultList;
	}
	
	// TODO find by Agent
	public List<RealProperty> getRealPropertiesForAgent(String license) {
		List<Agent> agtList = agtRepo.findByLicense(license);
		List<RealProperty> resultList = new ArrayList<RealProperty>();
		if (agtList.size() > 0) {
			Agent agent = agtList.get(0);
			List<PropertyTransaction> ptList = ptRepo.findAllTransactionsByAgent(agent);
			for (PropertyTransaction pt : ptList) {
				RealProperty rp = pt.getRealProperty();
				if (rp != null) {
					resultList.add(rp);
					System.out.println(rp);
				} else {
					log.warn("PropertyTransaction pt = " + pt + " does not link to a real property!");
				}
			}
		}
		return resultList;
	}
}
