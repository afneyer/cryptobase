package com.afn.realstat;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.afn.realstat.framework.SpringApplicationContext;
import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration
@ComponentScan
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.afn.realstat")
@EnableAutoConfiguration
@Profile("XXX")
class ProdContext {
	
	public static final Logger log = LoggerFactory.getLogger("app");
	
	/*
	@Bean
	public DataSource afn1DataSource() {
		DriverManagerDataSource datasource = new DriverManagerDataSource();
        datasource.setDriverClassName(com.mysql.jdbc.Driver.class.getName());
        datasource.setUrl("jdbc:mysql://localhost:3306/test?useSSL=false");
        datasource.setUsername("root");
        datasource.setPassword("admin");
        return datasource;
	}
	*/
	
	@Bean
	public DataSource pooledDataSource() {
		System.out.println("Running Production Profile");
		ComboPooledDataSource cpds = new ComboPooledDataSource();
		try {
			cpds.setDriverClass( com.mysql.jdbc.Driver.class.getName() );
		} catch (PropertyVetoException e) {
			throw new RuntimeException("Cannot set pooled data source!",e);
		} 
		
		//load the JDBC-driver
		cpds.setJdbcUrl( "jdbc:mysql://localhost:3306/test?useSSL=false" );
		cpds.setUser("root");
		cpds.setPassword("admin");

		// the settings below are optional -- c3p0 can work with defaults
		cpds.setMinPoolSize(5);
		cpds.setAcquireIncrement(5);
		cpds.setMaxPoolSize(20);
		return cpds;
	}
	
    @Bean( name  = "springApplicationContext" )
    public SpringApplicationContext springApplicationContext() {
    	return new SpringApplicationContext();
    }
	
    @Bean( name = "entityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		
		LocalContainerEntityManagerFactoryBean factory = 
				new LocalContainerEntityManagerFactoryBean();
		factory.setDataSource(pooledDataSource());
		factory.setPackagesToScan(getClass().getPackage().getName());
	    HibernateJpaVendorAdapter hjva = new HibernateJpaVendorAdapter();
		factory.setJpaVendorAdapter(hjva);
	    // factory.setJpaDialect(new HibernateJpaDialect());
	    factory.setJpaProperties(additionalProperties());
	   
	    // HibernatePersistenceProvider p = new HibernatePersistenceProvider();
	    // factory.setPersistenceProvider( p );
	    // factory.setLoadTimeWeaver(new ReflectiveLoadTimeWeaver());
	     
		// factory.afterPropertiesSet(); // It will initialize EntityManagerFactory object otherwise below will return null
		
		log.info( "AFN: completed creation of the factory " + factory.toString() );
				
		return factory;
		
	}
	
	Properties additionalProperties() {
        Properties properties = new Properties();
//      "create-drop"
        properties.setProperty("hibernate.hbm2ddl.auto","update" );
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        properties.setProperty("hibernate.connection.isolation", String.valueOf(Connection.TRANSACTION_READ_COMMITTED));
        properties.setProperty("hibernate.show_sql", "false" );
        properties.setProperty("hibernate.format_sql", "false" );
        properties.setProperty("hibernate.jdbc.batch_size","1000");
        
        // properties.setProperty("spring.jpa.hibernate.naming.strategy","org.hibernate.cfg.ImprovedNamingStrategy");
        // properties.setProperty("spring.jpa.hibernate.naming.physical-strategy","org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl");
        // properties.setProperty("hibernate.archive.autodetection", "class");
        return properties;
     }
	

	/*
	@Bean
	public PersistenceAnnotationBeanPostProcessor postProcessor() {
		return new PersistenceAnnotationBeanPostProcessor();
	}
	// <bean class="org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor" />
	*/
	
	

//	@Bean
//	PlatformTransactionManager transactionManager() {
//		@Autowired 
//		EntityManagerFactory emf;
//		JpaTransactionManager txManager = new JpaTransactionManager( emf );
//		return txManager;
//	}
	
	
	/**
	   * PersistenceExceptionTranslationPostProcessor is a bean post processor
	   * which adds an advisor to any bean annotated with Repository so that any
	   * platform-specific exceptions are caught and then rethrown as one
	   * Spring's unchecked data access exceptions (i.e. a subclass of 
	   * DataAccessException).
	   */
	  /* @Bean
	  public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
	    return new PersistenceExceptionTranslationPostProcessor();
	  }*/
	  
	
}
