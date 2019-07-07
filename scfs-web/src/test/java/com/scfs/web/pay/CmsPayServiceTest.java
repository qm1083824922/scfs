package com.scfs.web.pay;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.scfs.domain.pay.entity.PayOrder;
import com.scfs.service.pay.PayService;
import com.scfs.service.pay.UploadPayService;
import com.scfs.web.base.BaseJUnitTest;
import com.google.common.collect.Lists;

public class CmsPayServiceTest extends BaseJUnitTest{
	@Autowired
	private PayService payService;
	@Autowired
	private UploadPayService uploadPayService;
	
	@Test
	public void testUpload() {
		String[] payNoList = {"PY17070500059", "PY17070500058", "PY17070500057", "PY17070500056", "PY17070500055", "PY17070500054", "PY17070500053", "PY17070500052", "PY17070500051"};
		List<PayOrder> payOrderList = Lists.newArrayList();
		for (String payNo : payNoList) {
			PayOrder payOrder = payService.queryEntityByPayNo(payNo);
			payOrderList.add(payOrder);
		}
		uploadPayService.uploadPayOrder(payOrderList, false, null);
	}
}
