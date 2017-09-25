package com.afn;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.util.Arrays;
import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.afn.realstat.framework.SpringApplicationContext;
import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration
@ComponentScan(basePackages = {"com.afn.*"})
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {"com.afn.*"})
@EnableAutoConfiguration
class AppConfig {

	public static final Logger log = LoggerFactory.getLogger("app");

	@Autowired
	private Environment environment;

	/**
	 * Returns the data source based on the active profile - development data
	 * source if one of the active profiles is "dev" - production data source if
	 * one of the active profiles is "prod"
	 * 
	 * @return dataSource based on one of two profiles: "dev" or "prod"
	 */
	@Bean(name = "dataSource")
	public DataSource dataSource() {

		// get the active profile
		String[] activeProfiles = environment.getActiveProfiles();

		for (String profile : activeProfiles) {
			if (profile.equals("dev")) {
				return testDataSource();
			}
			if (profile.equals("prod")) {
				return prodDataSource();
			}
		}
		throw new RuntimeException("Cannot set data source based on the profile dev or prod. Active Profiles"
				+ Arrays.toString(activeProfiles));
	}

	public DataSource embeddedDataSource() {
		return new EmbeddedDatabaseBuilder().build();
	}

	public DataSource dbTest1DataSource() {
		DriverManagerDataSource ds = new DriverManagerDataSource();
		ds.setDriverClassName(com.mysql.jdbc.Driver.class.getName());
		ds.setUrl("jdbc:mysql://localhost:3306/crypto_test?SSL=false");
		ds.setUsername("root");
		ds.setPassword("admin");
		return ds;
	}

	public DataSource testDataSource() {
		DriverManagerDataSource ds = new DriverManagerDataSource();
		ds.setDriverClassName(com.mysql.jdbc.Driver.class.getName());
		ds.setUrl("jdbc:mysql://localhost:3306/crypto_test?useSSL=false");
		ds.setUsername("root");
		ds.setPassword("admin");
		return ds;
	}

	public DataSource prodDataSource() {
		ComboPooledDataSource cpds = new ComboPooledDataSource();
		try {
			cpds.setDriverClass(com.mysql.jdbc.Driver.class.getName());
		} catch (PropertyVetoException e) {
			throw new RuntimeException("Cannot set pooled data source!", e);
		}

		// load the JDBC-driver
		cpds.setJdbcUrl("jdbc:mysql://localhost:3306/crypto_test?useSSL=false");
		cpds.setUser("root");
		cpds.setPassword("admin");

		// the settings below are optional -- c3p0 can work with defaults
		cpds.setMinPoolSize(5);
		cpds.setAcquireIncrement(5);
		cpds.setMaxPoolSize(20);
		return cpds;
	}

	@Bean(name = "springApplicationContext")
	public SpringApplicationContext springApplicationContext() {
		return new SpringApplicationContext();
	}

	@Bean(name = "entityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		DataSource ds = dataSource();
		factory.setDataSource(ds);
		factory.setPackagesToScan(getClass().getPackage().getName());
		HibernateJpaVendorAdapter hjva = new HibernateJpaVendorAdapter();
		factory.setJpaVendorAdapter(hjva);
		// factory.setJpaDialect(new HibernateJpaDialect());
		factory.setJpaProperties(additionalProperties());

		// HibernatePersistenceProvider p = new HibernatePersistenceProvider();
		// factory.setPersistenceProvider( p );
		// factory.setLoadTimeWeaver(new ReflectiveLoadTimeWeaver());

		// factory.afterPropertiesSet(); // It will initialize
		// EntityManagerFactory object otherwise below will return null

		log.info("AFN: completed creation of the factory " + factory.toString());

		return factory;

	}

	Properties additionalProperties() {
		Properties properties = new Properties();
		// "create-drop"
		properties.setProperty("hibernate.hbm2ddl.auto", "update");
		String hibernateDialect = null;
		Class<? extends DataSource> dataSourceClass = dataSource().getClass();
		if (EmbeddedDatabase.class.isAssignableFrom(dataSourceClass)) {
			hibernateDialect = "org.hibernate.dialect.HSQLDialect";
			log.info("Using hibernate dialect: org.hibernate.dialect.HSQLDialect");
		} else {
			hibernateDialect = "org.hibernate.dialect.MySQL5Dialect";
			log.info("Using hibernate dialect: org.hibernate.dialect.MySQL5Dialect");
		}
		properties.setProperty("hibernate.dialect", hibernateDialect);
		properties.setProperty("hibernate.connection.isolation", String.valueOf(Connection.TRANSACTION_READ_COMMITTED));
		properties.setProperty("hibernate.show_sql", "true");
		properties.setProperty("hibernate.format_sql", "false");
		properties.setProperty("hibernate.jdbc.batch_size", "1000");

		// properties.setProperty("spring.jpa.hibernate.naming.strategy","org.hibernate.cfg.ImprovedNamingStrategy");
		// properties.setProperty("spring.jpa.hibernate.naming.physical-strategy","org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl");
		// properties.setProperty("hibernate.archive.autodetection", "class");
		return properties;
	}

	/*
	 * @Bean public PersistenceAnnotationBeanPostProcessor postProcessor() {
	 * return new PersistenceAnnotationBeanPostProcessor(); } // <bean class=
	 * "org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor"
	 * />
	 */

	// @Bean
	// PlatformTransactionManager transactionManager() {
	// @Autowired
	// EntityManagerFactory emf;
	// JpaTransactionManager txManager = new JpaTransactionManager( emf );
	// return txManager;
	// }

	/**
	 * PersistenceExceptionTranslationPostProcessor is a bean post processor
	 * which adds an advisor to any bean annotated with Repository so that any
	 * platform-specific exceptions are caught and then rethrown as one Spring's
	 * unchecked data access exceptions (i.e. a subclass of
	 * DataAccessException).
	 */
	/*
	 * @Bean public PersistenceExceptionTranslationPostProcessor
	 * exceptionTranslation() { return new
	 * PersistenceExceptionTranslationPostProcessor(); }
	 */

}
