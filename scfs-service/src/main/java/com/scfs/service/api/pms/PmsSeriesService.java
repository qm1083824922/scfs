package com.scfs.service.api.pms;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scfs.dao.api.pms.PmsSeriesDao;
import com.scfs.domain.api.pms.entity.PmsSeries;

@Service
public class PmsSeriesService {

	@Autowired
	private PmsSeriesDao pmsSeriesDao;

	/** pms调用接口之后，添加pms业务流水信息 */
	public PmsSeries createPmsSeries(Integer type) {
		PmsSeries pmsSeries = new PmsSeries();
		Date date = new Date();
		pmsSeries.setCreateAt(date);
		pmsSeries.setInvokeTime(date);
		pmsSeries.setType(type);
		pmsSeries.setMessage("");
		pmsSeriesDao.insert(pmsSeries);
		return pmsSeries;
	}

	/** 查询业务流水信息 */
	public PmsSeries queryEntityById(Integer id) {
		return pmsSeriesDao.queryEntityById(id);
	}

	/** pms定时任务调度之后，对业务流水信息状态进行更新 */
	public int updateById(PmsSeries pmsSeries) {
		return pmsSeriesDao.updateById(pmsSeries);
	}
}
