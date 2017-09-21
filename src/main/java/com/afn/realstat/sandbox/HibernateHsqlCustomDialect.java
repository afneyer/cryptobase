package com.afn.realstat.sandbox;

import java.sql.Types;

import org.hibernate.dialect.HSQLDialect;

public class HibernateHsqlCustomDialect extends HSQLDialect {

	public HibernateHsqlCustomDialect() {
		super();
		this.registerColumnType(Types.BLOB, "blob");
	}

}
