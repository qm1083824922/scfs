package com.scfs.common.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * 
 * @author Administrator
 *
 */
public class MsgPhpUrlBuilder {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String httpBuildQuery(Map<String, Object> params, String encoding) {
		if (isEmpty(encoding)) {
			encoding = "UTF-8";
		}
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			if (sb.length() > 0) {
				sb.append('&');
			}

			String name = entry.getKey();
			Object value = entry.getValue();

			if (value instanceof Map) {
				List<String> baseParam = new ArrayList<String>();
				baseParam.add(name);
				String str = buildUrlFromMap(baseParam, (Map) value, encoding);
				sb.append(str);

			} else if (value instanceof Collection) {
				List<String> baseParam = new ArrayList<String>();
				baseParam.add(name);
				String str = buildUrlFromCollection(baseParam, (Collection) value, encoding);
				sb.append(str);

			} else {
				sb.append(encodeParam(name));
				sb.append("=");
				sb.append(encodeParam(value));
			}

		}
		return sb.toString();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static String buildUrlFromMap(List<String> baseParam, Map<Object, Object> map, String encoding) {
		StringBuilder sb = new StringBuilder();
		String token;

		for (Map.Entry<Object, Object> entry : map.entrySet()) {

			if (sb.length() > 0) {
				sb.append('&');
			}

			String name = String.valueOf(entry.getKey());
			Object value = entry.getValue();
			if (value instanceof Map) {
				List<String> baseParam2 = new ArrayList<String>(baseParam);
				baseParam2.add(name);
				String str = buildUrlFromMap(baseParam2, (Map) value, encoding);
				sb.append(str);

			} else if (value instanceof List) {
				List<String> baseParam2 = new ArrayList<String>(baseParam);
				baseParam2.add(name);
				String str = buildUrlFromCollection(baseParam2, (List) value, encoding);
				sb.append(str);
			} else {
				token = getBaseParamString(baseParam) + "[" + name + "]=" + encodeParam(value);
				sb.append(token);
			}
		}

		return sb.toString();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static String buildUrlFromCollection(List<String> baseParam, Collection coll, String encoding) {
		StringBuilder sb = new StringBuilder();
		String token;
		if (!(coll instanceof List)) {
			coll = new ArrayList(coll);
		}
		List arrColl = (List) coll;

		for (int i = 0; i < arrColl.size(); i++) {

			if (sb.length() > 0) {
				sb.append('&');
			}

			Object value = (Object) arrColl.get(i);
			if (value instanceof Map) {
				List<String> baseParam2 = new ArrayList<String>(baseParam);
				baseParam2.add(String.valueOf(i));
				String str = buildUrlFromMap(baseParam2, (Map) value, encoding);
				sb.append(str);

			} else if (value instanceof List) {
				List<String> baseParam2 = new ArrayList<String>(baseParam);
				baseParam2.add(String.valueOf(i));
				String str = buildUrlFromCollection(baseParam2, (List) value, encoding);
				sb.append(str);
			} else {
				token = getBaseParamString(baseParam) + "[" + i + "]=" + encodeParam(value);
				sb.append(token);
			}
		}

		return sb.toString();
	}

	private static String getBaseParamString(List<String> baseParam) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < baseParam.size(); i++) {
			String s = baseParam.get(i);
			if (i == 0) {
				sb.append(s);
			} else {
				sb.append("[" + s + "]");
			}
		}
		return sb.toString();
	}

	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	@SuppressWarnings("deprecation")
	private static String encodeParam(Object param) {
		try {
			return URLEncoder.encode(String.valueOf(param), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return URLEncoder.encode(String.valueOf(param));
		}
	}
}
