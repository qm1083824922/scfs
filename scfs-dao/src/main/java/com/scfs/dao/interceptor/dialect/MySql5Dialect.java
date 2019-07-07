package com.scfs.dao.interceptor.dialect;

public class MySql5Dialect extends Dialect {
	public String getLimitString(String sql, int offset, int limit) {
		return MySql5PageHepler.getLimitString(sql, offset, limit);
	}

	public String getCountSql(String sql) {
		return MySql5PageHepler.getCountString(sql);
	}
}