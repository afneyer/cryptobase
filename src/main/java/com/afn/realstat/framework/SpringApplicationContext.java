package com.afn.realstat.framework;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringApplicationContext implements ApplicationContextAware {
	
	private static ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
	}

	public static Object getBean(String beanName) {
		return context.getBean(beanName);
	}
	
	  public static Connection getConnection() {
		  Connection connect = null;
		  DataSource ds = getDataSource();
		  try {
			connect = ds.getConnection();
		} catch (SQLException e) {
			throw new RuntimeException("AppContext: Cannot get connection from datasource 'dataSource'", e);
		}
		  return connect;
		  
	  }

	  public static DataSource getDataSource() {
		DataSource ds = (DataSource) context.getBean("dataSource");
		return ds;
	}
	
	

}
