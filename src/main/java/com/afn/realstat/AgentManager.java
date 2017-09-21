package com.afn.realstat;

import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AgentManager extends AbstractEntityManager<Agent> {

	public static final Logger log = LoggerFactory.getLogger("log");

	@Autowired
	AgentRepository agtRepo;

	// move the superclass
	public AgentManager(AgentRepository agtRepo) {
		repo = agtRepo;
	}

	public void cleanAgentNames() {

		Function<Agent, Boolean> cleanAgentName = agt -> {
			agt.cleanAgentName();
			System.out.println("Cleaned agent: " + agt.getAgentName() );
			return true;	
		};
		

		// QAgent qAgt = QAgent.agent;
		// BooleanExpression predicate = qAgt.location.isNull().and(qAdr.mapLocCalls.eq(0));
		performActionOnEntities(cleanAgentName, null);
		
	}
}
