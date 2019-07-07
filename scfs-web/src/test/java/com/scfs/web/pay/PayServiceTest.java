package com.scfs.web.pay;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.scfs.common.consts.BaseConsts;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.dao.fee.FeeDao;
import com.scfs.dao.pay.PayFeeRelationDao;
import com.scfs.dao.pay.PayOrderDao;
import com.scfs.domain.fee.entity.Fee;
import com.scfs.domain.pay.dto.req.PayFeeRelationReqDto;
import com.scfs.domain.pay.dto.req.PayOrderSearchReqDto;
import com.scfs.domain.pay.dto.resq.PayOrderResDto;
import com.scfs.domain.pay.entity.PayFeeRelationModel;
import com.scfs.domain.pay.entity.PayOrder;
import com.scfs.domain.result.PageResult;
import com.scfs.service.pay.PayService;
import com.scfs.web.base.BaseJUnitTest;

public class PayServiceTest extends BaseJUnitTest {
	@Autowired
	PayService payService;
	@Autowired
	private PayOrderDao payOrderDao;
	@Autowired
	private PayFeeRelationDao payFeeRelationDao;
	@Autowired
	private FeeDao feeDao;
	@Test
	public void queryPay() {
		PayOrderSearchReqDto paySerch = new PayOrderSearchReqDto();
		paySerch.setState(0);
		PageResult<PayOrderResDto> pageResult = payService.queryPayOrderResultsByCon(paySerch);
		System.out.println(pageResult.getTotal());
	}

	@Test
	public void addPay() {
		PayOrder payOrder = new PayOrder();
		payOrder.setProjectId(1);
		payOrder.setPayWay(1);
		payOrder.setPayee(2);
		payService.createPayOrder(payOrder);
	}

	@Test
	public void upPay() {
		PayOrder payOrder = new PayOrder();
		payOrder.setId(4);
		payOrder.setProjectId(2);
		payService.updatePayOrderById(payOrder);
	}

	@Test
	public void removePay() {
		PayOrder payOrder = new PayOrder();
		payOrder.setId(4);
		payService.deletePayOrderById(payOrder);
	}
	
	@Test
	public void updateFundUsed(PayOrder entity ){ 
		PayOrderSearchReqDto payOrderSearchReqDto = new PayOrderSearchReqDto();
		payOrderSearchReqDto.setState(BaseConsts.SIX);
		List<PayOrder>  ds = payOrderDao.queryResultsByCon(payOrderSearchReqDto);
		for(PayOrder p:ds){
			 updateFundUsedd(p);
		}
		
	}
	
	private void updateFundUsedd(PayOrder entity ){ 
		PayFeeRelationReqDto payFeeRelationReqDto = new PayFeeRelationReqDto();
		payFeeRelationReqDto.setPayId(entity.getId());
		payFeeRelationReqDto.setPayFeeType(BaseConsts.ONE);
		List<PayFeeRelationModel> list = payFeeRelationDao.queryResultsByCon(payFeeRelationReqDto);
		if(CollectionUtils.isNotEmpty(list)){
			BigDecimal minusNumber = new BigDecimal(BaseConsts.ZERO);
			BigDecimal positiveNumber = new BigDecimal(BaseConsts.ZERO);
			for(PayFeeRelationModel fee:list){
				if(DecimalUtil.ge(fee.getPayAmount(), BigDecimal.ZERO))
					positiveNumber = DecimalUtil.add(fee.getPayAmount(), positiveNumber); 
				else
					minusNumber = DecimalUtil.add(fee.getPayAmount(), minusNumber); 		
			}
			BigDecimal plage = DecimalUtil.divide(minusNumber, positiveNumber).setScale(BaseConsts.TWO,BigDecimal.ROUND_FLOOR);
			for(PayFeeRelationModel payFeeRelationModel:list){
				Fee fee =  feeDao.queryEntityById(payFeeRelationModel.getFeeId());
				if(DecimalUtil.ge(payFeeRelationModel.getPayAmount(), BigDecimal.ZERO)){
					Fee newFee = new Fee();
					newFee.setId(payFeeRelationModel.getFeeId());
					newFee.setFundUsed(DecimalUtil.add(fee.getFundUsed() == null?BigDecimal.ZERO:fee.getFundUsed() , payFeeRelationModel.getPayAmount().subtract(DecimalUtil.multiply(payFeeRelationModel.getPayAmount(), plage))));  
					feeDao.updateById(newFee);
				}
			}
		}
	}
}
