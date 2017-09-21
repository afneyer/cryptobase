package com.afn.realstat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
class TransactionManagerConfig {

	public static final Logger log = LoggerFactory.getLogger("app");

	// TODO remove
	/*
	@Autowired
	EntityManagerFactory emf;
	*/
	
	/*
	@Autowired
	private DataSource afn1DataSource;
	*/
	
	/*
	@Bean(name = "transactionManager")
	public PlatformTransactionManager transactionManager() {
		JpaTransactionManager tm = new JpaTransactionManager();
		tm.setEntityManagerFactory(emf);
		tm.setDataSource(afn1DataSource);
		return tm;
	}
	*/

}
