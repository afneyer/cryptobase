package com.afn.realstat;

import org.springframework.beans.factory.annotation.Autowired;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.prefs.CsvPreference;

import com.vaadin.spring.annotation.SpringComponent;

/**
 * @author Andreas
 *
 */
@SpringComponent
public class AgentImporter extends AbstractImporter<Agent> {

	/**
	 * 
	 */
 	@Autowired AgentRepository agentRep;
	// private static final Logger log = LoggerFactory.getLogger("import");

	public AgentImporter() {
		csvPref = CsvPreference.TAB_PREFERENCE;
	}
	

	/**
	 * @return
	 */
	protected CellProcessor[] getProcessors() {

		final CellProcessor[] processors = new CellProcessor[] {
				
				new NotNull(), // Agent Name
				new NotNull(), // User Code
				new Optional(), // Agent Phone
				new Optional(), // Office Name
				new Optional(), // Office Code
				new Optional(), // Office Phone
				new Optional(), // Type
				new Optional(), // Status
				new Optional(), // License
		};
		return processors;
	}

	/**
	 * Maps the header of the csv-file (i.e. list of fields) into the field names of the entity
	 * The default translator can be used if the entity are similar to the fields in the csv-file
	 * 
	 * @param header
	 * @return
	 */
	protected String[] mapHeader(String[] header) {

		// translate each element of the header to the corresponding bean field
		// name
		for (int i = 0; i < header.length; i++) {
			
			// special translations
			switch (header[i]) {
			case "Agent License":
				header[i] = "License";
				break;
			default:
				header[i] = translateCsvFileFieldDefault(header[i]);
			}
		}
		return header;
	}



	@Override
	protected void preProcessEntity(Agent a) {
		
		// translate bad data
		if (a.getAgentName().startsWith("(Ravi) Luthra, Virender")) a.setAgentName("Lutra, Virender Ravi");
		if (a.getAgentName().startsWith("- NO DRE# REQUIRED, ATTORNEY/LAW")) a.setAgentName("Attorney, Law");
		if (a.getAgentName().startsWith(". Robertson, H")) a.setAgentName("Robertson, H");
		if (a.getAgentName().startsWith("999999")) {
			a.setAgentName("Non-Member, Non-Member");
			a.setLicense("999999");
		}
		if (a.getAgentName().startsWith("xxx")) a.setAgentName("xxx");
		if (a.getAgentName().startsWith("xxx")) a.setAgentName("xxx");
		
		// clean entity
		a.clean();
	}


	@Override
	protected void saveOrUpdateEntity(Agent a) {
		agentRep.saveOrUpdate(a);
		// TODO Auto-generated method stub
		
	}


	@Override
	protected void postProcessEntity(Agent entity) {
		// no post-processing needed		
	}


	@Override
	protected Agent getNewEntity() {
		return new Agent();
	}
	
	
}
