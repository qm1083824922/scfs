package com.scfs.service.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scfs.common.consts.BaseConsts;
import com.scfs.dao.common.BusinessMsgDao;
import com.scfs.domain.common.entity.BusinessMsg;

@Service
public class BusinessMsgService {
	@Autowired
	private BusinessMsgDao businessMsgDao;

	public void save(BusinessMsg businessMsg) {
		businessMsgDao.insert(businessMsg);
	}

	public void success(BusinessMsg businessMsg) {
		businessMsg.setFlag(BaseConsts.ZERO);
		businessMsgDao.updateById(businessMsg);
	}

	public void error(BusinessMsg businessMsg) {
		businessMsg.setFlag(BaseConsts.ONE);
		businessMsgDao.updateById(businessMsg);
	}

	public BusinessMsg queryEntityByCondition(String billNo, Integer billType, Integer invokeType) {
		return businessMsgDao.queryEntityByCondition(billNo, billType, invokeType);
	}

	public BusinessMsg queryEntityById(Integer id) {
		return businessMsgDao.queryEntityById(id);
	}

}
