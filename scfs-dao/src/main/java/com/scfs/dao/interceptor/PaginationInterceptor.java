package com.scfs.dao.interceptor;

import java.sql.Connection;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scfs.dao.interceptor.dialect.Dialect;

@Intercepts({ @org.apache.ibatis.plugin.Signature(type = StatementHandler.class, method = "prepare", args = {
		Connection.class }) })
public class PaginationInterceptor implements Interceptor {
	private static final Logger logger = LoggerFactory.getLogger(PaginationInterceptor.class);
	private Dialect dialect;

	public void setDialect(Dialect dialect) {
		this.dialect = dialect;
	}

	private String beautifySql(String sql) {
		return sql.replaceAll("[\r\n]", " ").replaceAll("\\s{2,}", " ");
	}

	public Object intercept(Invocation invocation) throws Throwable {
		StatementHandler statementHandler = getStatementHandler(invocation);
		MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
		BoundSql boundSql = (BoundSql) metaObject.getValue("boundSql");
		String sql = boundSql.getSql();
		if ((StringUtils.startsWithIgnoreCase(sql.trim(), "UPDATE"))
				|| (StringUtils.startsWithIgnoreCase(sql.trim(), "INSERT"))
				|| (StringUtils.startsWithIgnoreCase(sql.trim(), "DELETE"))) {
			logger.info("SQL:{} -> Parameters:{}",
					new Object[] { beautifySql(sql), statementHandler.getParameterHandler().getParameterObject() });
		}
		RowBounds rowBounds = (RowBounds) metaObject.getValue("rowBounds");
		if ((rowBounds == null) || (rowBounds == RowBounds.DEFAULT)) {
			return invocation.proceed();
		}
		Connection connection = (Connection) invocation.getArgs()[0];
		String countSql = this.dialect.getCountSql(sql);
		Configuration configuration = (Configuration) metaObject.getValue("configuration");
		CountHelper.getCount(countSql, statementHandler, configuration, boundSql.getParameterMappings(), connection);
		metaObject.setValue("boundSql.sql",
				this.dialect.getLimitString(sql, rowBounds.getOffset(), rowBounds.getLimit()));
		metaObject.setValue("rowBounds.offset", Integer.valueOf(0));
		metaObject.setValue("rowBounds.limit", Integer.valueOf(Integer.MAX_VALUE));
		if (logger.isDebugEnabled()) {
			logger.debug("生成分页SQL:{}", new Object[] { statementHandler.getBoundSql().getSql() });
		}
		return invocation.proceed();
	}

	protected StatementHandler getStatementHandler(Invocation invocation) {
		StatementHandler statement = (StatementHandler) invocation.getTarget();
		if ((statement instanceof RoutingStatementHandler)) {
			statement = (StatementHandler) ReflectionUtil.getFieldValue(statement, "delegate");
		}
		return statement;
	}

	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	public void setProperties(Properties properties) {
	}
}
