package com.scfs.service.common;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.testng.collections.Lists;

import com.scfs.common.consts.BaseConsts;
import com.scfs.dao.common.SysParamDao;
import com.scfs.domain.common.entity.SysParam;

/**
 * Created by Administrator on 2017年7月18日.
 */
@Service
public class SysParamService {
	@Autowired
	private SysParamDao sysParamDao;

	public String queryParamValueByParamKey(String paramKey) {
		SysParam sysParam = sysParamDao.queryEntityByParamKey(paramKey);
		if (null != sysParam) {
			return sysParam.getParamValue();
		} else {
			return StringUtils.EMPTY;
		}
	}

	public SysParam queryEntityByParamKey(String paramKey) {
		SysParam sysParam = sysParamDao.queryEntityByParamKey(paramKey);
		return sysParam;
	}

	public List<String> queryParamValueListByParamKey(String paramKey) {
		List<String> paramValueList = Lists.newArrayList();
		String paramValue = queryParamValueByParamKey(paramKey);
		if (StringUtils.isNotBlank(paramValue)) {
			String[] paramValueArray = StringUtils.split(paramValue, BaseConsts.SEPARATOR_COMMA);
			paramValueList = Arrays.asList(paramValueArray);
		}
		return paramValueList;
	}
}
