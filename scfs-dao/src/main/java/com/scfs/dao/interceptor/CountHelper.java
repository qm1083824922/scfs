package com.scfs.dao.interceptor;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public final class CountHelper {
	private static Logger logger = LoggerFactory.getLogger(CountHelper.class);
	private static final ThreadLocal<Integer> totalRowLocals = new ThreadLocal<Integer>() {
		protected Integer initialValue() {
			return Integer.valueOf(0);
		}
	};

	public static void getCount(String sql, StatementHandler statementHandler, Configuration configuration,
			List<ParameterMapping> parameterMappings, Connection connection) throws Exception {
		Object parameterObject = statementHandler.getParameterHandler().getParameterObject();
		if (logger.isDebugEnabled()) {
			logger.debug("Total count sql [{}] parameters [{}] ", new Object[] { sql, parameterObject });
		}
		BoundSql countBoundSql = new BoundSql(configuration, sql, parameterMappings, parameterObject);
		MappedStatement ms = (MappedStatement) ReflectionUtil.getFieldValue(statementHandler, "mappedStatement");
		DefaultParameterHandler handler = new DefaultParameterHandler(ms, parameterObject, countBoundSql);
		ResourceTracker tracker = new ResourceTracker("Total Count");
		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			tracker.attach(ps);
			handler.setParameters(ps);
			ResultSet rs = ps.executeQuery();
			tracker.attach(rs);
			int count = 0;
			if (rs.next()) {
				count = rs.getInt(1);
			}
			if (logger.isDebugEnabled()) {
				logger.debug("Total count [{}], sql [{}]", new Object[] { Integer.valueOf(count), sql });
			}
			totalRowLocals.set(Integer.valueOf(count));
		} catch (Exception e) {
			totalRowLocals.set(Integer.valueOf(0));
			throw e;
		} finally {
			tracker.clear();
		}
	}

	public static int getTotalRow() {
		return ((Integer) totalRowLocals.get()).intValue();
	}

	public static void remove() {
		totalRowLocals.remove();
	}
}
