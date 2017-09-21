package com.afn.realstat;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface AgentRepository extends AbstractEntityRepository<Agent> {

	public static final Logger log = LoggerFactory.getLogger("app");

	List<Agent> findByLicense(String license);

	List<Agent> findByFirstNameAndLastName(String firstName, String lastName);

	public default Agent saveOrUpdate(Agent a) {
		a.clean(); // TODO too many cleans are called
		String lic = a.getLicense();
		String fn = a.getFirstName();
		String ln = a.getLastName();

		if (a.hasValidLicense()) {
			List<Agent> agentsByLic = findByLicense(lic);

			if (agentsByLic.size() > 1) {
				log.error("Non-unique entries for agent license: Agent=" + a);
				return null;
			}

			// if only one is found, update it
			if (agentsByLic.size() == 1) {
				updateEntity(agentsByLic.get(0), a);
				return a;
			}
		}

		// the agent could not be found by license, use name to match
		if (a.hasValidName()) {

			List<Agent> agentsByName = findByFirstNameAndLastName(fn, ln);

			if (agentsByName.size() > 1) {
				// There a multiple agents with same name and we don't have a
				// valid license to distinguish them.
				// TODO maybe select by middle name;
				log.error("Non-unique entries for agent first and last: Agent=" + a);
				return null;
			}

			if (agentsByName.size() == 1) {
				// update it
				updateEntity(agentsByName.get(0), a);
				return a;
			} else {
				// create a new agent with name and without license
				save(a);
				return a;
			}
		}
		
		if (lic != null || fn != null || ln != null) {
			log.warn("Agent has invalid license and invalid name: Agent=" + a);
		}
		return null;

	}

}