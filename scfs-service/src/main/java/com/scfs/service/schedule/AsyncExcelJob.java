package com.scfs.service.schedule;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.beust.jcommander.internal.Lists;
import com.scfs.common.consts.BaseConsts;
import com.scfs.dao.common.AsyncExcelDao;
import com.scfs.dao.tx.IgnoreTransactionalMark;
import com.scfs.domain.common.entity.AsyncExcel;
import com.scfs.service.util.ApplicationContextHolder;
import com.scfs.service.util.XlsWorkbookUtil;

/**
 * Created by Administrator on 2017/2/15.
 */
@Service
public class AsyncExcelJob {

	private final static Logger LOGGER = LoggerFactory.getLogger(AsyncExcelJob.class);

	@Autowired
	private AsyncExcelDao asyncExcelDao;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@IgnoreTransactionalMark
	public void execute() {

		try {
			List<AsyncExcel> asyncExcelWithBLOBsList = asyncExcelDao.queryAsyncExcelLimit10();
			while (CollectionUtils.isNotEmpty(asyncExcelWithBLOBsList)) {
				LOGGER.info("导出excel执行条数：" + asyncExcelWithBLOBsList.size());
				AsyncExcel asyncExcel = new AsyncExcel();
				for (AsyncExcel asyncExcelWithBLOBs : asyncExcelWithBLOBsList) {
					asyncExcel.setId(asyncExcelWithBLOBs.getId());
					asyncExcel.setYn(BaseConsts.ONE);
					List<Object> args = Lists.newArrayList();
					if (asyncExcelWithBLOBs.getArgs() != null) {
						args.add(XlsWorkbookUtil.bytes2Obj(asyncExcelWithBLOBs.getArgs()));
					}
					try {
						Class cls = Class.forName(asyncExcelWithBLOBs.getClassName());
						Object obj = ApplicationContextHolder.getBean(cls);
						String methodName = asyncExcelWithBLOBs.getMethodName();
						Method[] methods = cls.getMethods();
						for (Method method : methods) {
							if (methodName.equalsIgnoreCase(method.getName())) {
								ReflectionUtils.makeAccessible(method);
								Object returnObj = method.invoke(obj, args.toArray());
								// 生成xls
								XlsWorkbookUtil.buildWorkbook((Map<String, Object>) returnObj, asyncExcelWithBLOBs);
								break;
							}
						}
						asyncExcel.setResult(BaseConsts.ZERO);
						asyncExcel.setExcelPath(asyncExcelWithBLOBs.getExcelPath());
					} catch (Exception e) {
						asyncExcel.setResult(BaseConsts.ONE);
						LOGGER.error("【{}】生成EXCEL记录异常", JSONObject.toJSON(asyncExcelWithBLOBs), e);
					} finally {
						asyncExcelDao.updateById(asyncExcel);
					}
				}
				asyncExcelWithBLOBsList = asyncExcelDao.queryAsyncExcelLimit10();
			}
		} catch (Exception e) {
			LOGGER.error("后台excel异步导出：", e);
		}
	}

}
