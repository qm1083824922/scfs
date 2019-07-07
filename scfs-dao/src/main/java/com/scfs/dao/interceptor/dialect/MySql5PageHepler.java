package com.scfs.dao.interceptor.dialect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MySql5PageHepler {
	private static final Logger logger = LoggerFactory.getLogger(MySql5PageHepler.class);

	public static String getCountString(String sql) {
		String querySelect = getLineSql(sql);
		int orderIndex = getLastOrderInsertPoint(querySelect);
		int fromIndex = getAfterFormInsertPoint(querySelect);
		String select = querySelect.substring(0, fromIndex);
		if ((select.toLowerCase().indexOf("select distinct") != -1)
				|| (querySelect.toLowerCase().indexOf("group by") != -1)) {
			return "select count(*) count from (" + querySelect.substring(0, orderIndex) + " ) t";
		}
		return "select count(*) count " + querySelect.substring(fromIndex, orderIndex);
	}

	private static int getLastOrderInsertPoint(String querySelect) {
		int orderIndex = querySelect.toLowerCase().lastIndexOf("order by");
		if (orderIndex == -1) {
			//logger.warn("分页查询[{}]没有进行排序!", new Object[] { querySelect });
			orderIndex = querySelect.length();
		}
		if (!isBracketCanPartnership(querySelect.substring(orderIndex, querySelect.length()))) {
			throw new RuntimeException("Sql语句'('与')'数量不匹配");
		}
		return orderIndex;
	}

	public static String getLimitString(String sql, int offset, int limit) {
		String querySelect = getLineSql(sql);
		return querySelect + " limit " + offset + ", " + limit;
	}

	private static String getLineSql(String sql) {
		return sql.replaceAll("[\r\n]", " ").replaceAll("\\s{2,}", " ");
	}

	private static int getAfterFormInsertPoint(String querySelect) {
		String regex = "\\s+FROM\\s+";
		Pattern pattern = Pattern.compile(regex, 2);
		Matcher matcher = pattern.matcher(querySelect);
		while (matcher.find()) {
			int fromStartIndex = matcher.start(0);
			String text = querySelect.substring(0, fromStartIndex);
			if (isBracketCanPartnership(text)) {
				return fromStartIndex;
			}
		}
		return 0;
	}

	private static boolean isBracketCanPartnership(String text) {
		if ((text == null) || (getIndexOfCount(text, '(') != getIndexOfCount(text, ')'))) {
			return false;
		}
		return true;
	}

	private static int getIndexOfCount(String text, char ch) {
		int count = 0;
		for (int i = 0; i < text.length(); i++) {
			count = text.charAt(i) == ch ? count + 1 : count;
		}
		return count;
	}
}
