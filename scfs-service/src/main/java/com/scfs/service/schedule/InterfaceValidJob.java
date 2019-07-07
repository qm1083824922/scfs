package com.scfs.service.schedule;

import com.scfs.dao.tx.IgnoreTransactionalMark;
import com.scfs.service.api.pms.PmsSyncPayService;
import com.scfs.service.common.InvokeLogService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Administrator on 2017/4/1.
 */
public class InterfaceValidJob {
	@Autowired
	private PmsSyncPayService pmsSyncPayService;
	@Autowired
	private InvokeLogService invokeLogService;

	@IgnoreTransactionalMark
	public void execute() {
		pmsSyncPayService.doPmsSyncPay(null);// 同步请款信息
		invokeLogService.invokePmsPay(null, false);
	}
}
