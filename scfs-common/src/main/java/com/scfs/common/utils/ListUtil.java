package com.scfs.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ListUtil {

	@SuppressWarnings("rawtypes")
	public static boolean isEmpty(List list) {
		if (list == null)
			return true;
		return list.size() == 0;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List mapToList(Map map) {
		List list = new ArrayList();
		Iterator it_map = map.keySet().iterator();
		while (it_map.hasNext()) {
			String key = (String) it_map.next();
			Object obj = map.get(key);
			list.add(obj);
		}
		return list;
	}

	/**
	 * 取A和B两个列表的交集List
	 * 
	 * @param listA
	 * @param listB
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List mixedList(List listA, List listB) {
		List mixedList = new ArrayList();
		if (!isEmpty(listA) && !isEmpty(listB)) {
			for (int i = 0; i < listA.size(); i++) {
				Object objA = listA.get(i);
				if (listB.contains(objA)) {
					mixedList.add(objA);
				}
			}
		}
		return mixedList;
	}

	/**
	 * 取在A中而不在B中的数据
	 * 
	 * @param listA
	 * @param listB
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List notContainList(List listA, List listB) {
		List NotContainList = new ArrayList();
		if (!isEmpty(listA)) {
			if (isEmpty(listB))
				return listA;
			for (int i = 0; i < listA.size(); i++) {
				Object objA = listA.get(i);
				if (!listB.contains(objA)) {
					NotContainList.add(objA);
				}
			}
		}
		return NotContainList;
	}

	/**
	 * 取A和B两个列表的并集List
	 * 
	 * @param listA
	 * @param listB
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List mergeList(List listA, List listB) {
		List res = new ArrayList();
		// 将 两个列表做合并
		if (listA == null && listB == null)
			return res;
		if (ListUtil.isEmpty(listA)) {
			return listB;
		} else {
			if (ListUtil.isEmpty(listB)) {
				return listA;
			} else {
				for (int i = 0; i < listB.size(); i++) {
					Object obj = listB.get(i);
					if (listA.contains(obj))
						continue;
					listA.add(obj);
				}
				return listA;
			}
		}
	}

	/**
	 * 将str根据seperator 拆分组织成 列表
	 * 
	 * @param seperator
	 * @param str
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List strToList(String seperator, String str) {
		List resList = new ArrayList();
		if (StringUtils.isEmpty(str)) {
			return resList;
		}
		String[] strArray = str.split(seperator);
		if (strArray.length == 0)
			return resList;
		for (int i = 0; i < resList.size(); i++) {
			String strCell = strArray[i];
			if (!resList.contains(strCell))
				resList.add(strCell);
		}
		return resList;
	}

	/**
	 * 根据List<Map> 中个Map, 的key建值组装新的List<Stirng> add by xiaof 111020
	 * 
	 * @param mapList
	 * @param key
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List<String> getStrValues(List<Map> mapList, String key) {
		List<String> strList = new ArrayList<String>();

		if (isEmpty(mapList) || key == null || key.equals("")) {
			return strList;
		}

		for (Map map : mapList) {
			String strValue = (String) map.get(key);
			strList.add(strValue);
		}

		return strList;
	}

	/**
	 * 获取第一个对象,无返回null
	 */
	@SuppressWarnings("rawtypes")
	public static Object getFrist(List o) {

		if (isEmpty(o)) {
			return null;
		}

		return o.get(0);
	}

	@SuppressWarnings("rawtypes")
	public static List safe(List list) {
		if (list == null)
			return new ArrayList();
		return list;
	}

	/**
	 * 判断是否存在交叉 add xioaf 2012-06-08
	 */
	public static boolean isExistMix(List<String> AList, List<String> ZList) {
		if (ListUtil.isEmpty(AList) || ListUtil.isEmpty(ZList)) {
			return false;
		}
		for (String aString : AList) {
			if (ZList.contains(aString)) {
				return true;
			}
		}
		return false;
	}
}
