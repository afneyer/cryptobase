package com.afn.realstat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

	public static final Logger log = LoggerFactory.getLogger("app");
	
	@Autowired
	AppParamManager apMgr;

	public static void main(String[] args) {
		SpringApplication.run(Application.class);
		
		log.info("Starting main program of the application");
		System.out.println("Starting main program of the application");
	}
	
	@Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {

        	log.info("Running CommandLineRunner");
    		System.out.println("Running CommandLineRunner");

            log.info("Initializing application parameters");
            System.out.println("Initializing application parameters");
            apMgr.initializeParameters();
            
        };
    }

}
