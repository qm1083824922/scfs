package com.scfs.dao.interceptor.dialect;

public abstract class Dialect {
	public abstract String getLimitString(String paramString, int paramInt1, int paramInt2);

	public abstract String getCountSql(String paramString);

	public static enum Type {
		MYSQL, ORACLE;

		private Type() {
		}
	}
}
