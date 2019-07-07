package com.scfs.service.invoice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.ListUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.invoice.InvoiceCollectFeeDao;
import com.scfs.domain.BaseResult;
import com.scfs.domain.fee.dto.req.QueryFeeReqDto;
import com.scfs.domain.fee.dto.resp.FeeQueryResDto;
import com.scfs.domain.fee.entity.Fee;
import com.scfs.domain.fee.entity.FeeQueryModel;
import com.scfs.domain.invoice.dto.req.InvoiceCollectFeeReqDto;
import com.scfs.domain.invoice.dto.resp.InvoiceCollectFeeResDto;
import com.scfs.domain.invoice.dto.resp.InvoiceCollectResDto;
import com.scfs.domain.invoice.entity.InvoiceCollect;
import com.scfs.domain.invoice.entity.InvoiceCollectFee;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.fee.impl.FeeServiceImpl;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 * 	收票费用相关业务
 *  File: InvoiceCollectService.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月28日			Administrator
 *
 * </pre>
 */
@Service
public class InvoiceCollectFeeService {
	@Autowired
	private InvoiceCollectFeeDao InvoiceCollectFeeDao;

	@Autowired
	private InvoiceCollectService invoiceCollectService;

	@Autowired
	private FeeServiceImpl feeService;// 费用信息

	@Autowired
	CacheService cacheService;

	/**
	 * 获取费用列表
	 * 
	 * @param queryRecPayFeeReqDto
	 * @return
	 */
	public PageResult<FeeQueryResDto> queryFeeByCond(QueryFeeReqDto queryRecPayFeeReqDto) {
		InvoiceCollectResDto conllect = invoiceCollectService.queryInvoiceCollectById(queryRecPayFeeReqDto.getId());

		queryRecPayFeeReqDto.setProjectId(conllect.getProjectId());
		queryRecPayFeeReqDto.setCustReceiver(conllect.getSupplierId());
		queryRecPayFeeReqDto.setState(BaseConsts.THREE);
		queryRecPayFeeReqDto.setIsPayAll(BaseConsts.TWO);
		queryRecPayFeeReqDto.setCurrencyType(BaseConsts.ONE);
		queryRecPayFeeReqDto.setAcceptInvoiceType(conllect.getInvoiceType());
		queryRecPayFeeReqDto.setAcceptInvoiceTaxRate(conllect.getInvoiceTaxRate());
		PageResult<FeeQueryResDto> model = feeService.queryFeeByCond(queryRecPayFeeReqDto);
		PageResult<FeeQueryResDto> result = new PageResult<FeeQueryResDto>();
		List<FeeQueryResDto> recFeeQueryResDtos = new ArrayList<FeeQueryResDto>();
		if (model.getItems() != null && model.getItems().size() > BaseConsts.ZERO) {// 费用税率与收票税率一致
			for (FeeQueryResDto fee : model.getItems()) {
				fee.setBlanceInvoiceAmount(DecimalUtil.subtract(fee.getPayAmount(), fee.getAcceptInvoiceAmount()));
				recFeeQueryResDtos.add(fee);
			}
		}
		result.setItems(recFeeQueryResDtos);
		return result;
	}

	/**
	 * 添加数据
	 * 
	 * @param feeReqDto
	 * @return
	 */
	public BaseResult createInvoiceCollectFee(InvoiceCollectFeeReqDto feeReqDto) {
		BaseResult baseResult = new BaseResult();
		Integer collectId = feeReqDto.getCollectId();
		InvoiceCollectResDto conllect = invoiceCollectService.queryInvoiceCollectById(collectId);
		BigDecimal taxRateValue = new BigDecimal(conllect.getInvoiceTaxRateValue()); // 获取开票税率
		BigDecimal sum = conllect.getInvoiceAmount();

		List<InvoiceCollectFee> colRel = feeReqDto.getColRel();
		for (InvoiceCollectFee invoiceCollectFee : colRel) {
			FeeQueryModel oldEntity = feeService.queryEntityById(invoiceCollectFee.getFeeId()).getItems();
			BigDecimal blance = DecimalUtil.subtract(oldEntity.getPayAmount(), oldEntity.getAcceptInvoiceAmount());
			BigDecimal inRateAmount = invoiceCollectFee.getInRateAmount(); // 含税金额
			if (DecimalUtil.gt(inRateAmount, blance)) {
				baseResult.setMsg("余额不足！");
				return baseResult;
			}
		}
		Date date = new Date();
		for (InvoiceCollectFee invoiceCollectFee : colRel) {
			BigDecimal inRateAmount = invoiceCollectFee.getInRateAmount(); // 含税金额

			BigDecimal exAlgorithm = DecimalUtil.add(taxRateValue, new BigDecimal(BaseConsts.ONE));
			BigDecimal rateAlgorithm = DecimalUtil.divide(taxRateValue,
					DecimalUtil.add(taxRateValue, new BigDecimal(BaseConsts.ONE)));
			BigDecimal exRateAmount = DecimalUtil.divide(inRateAmount, exAlgorithm);// 未税金额=费用含税金额/(1+收票税率)
			BigDecimal rateAmount = DecimalUtil.multiply(inRateAmount, rateAlgorithm);// 税额
																						// =费用含税金额/(1+收票税率)*收票税率
			invoiceCollectFee.setInRateAmount(inRateAmount); // 含税金额
			invoiceCollectFee.setExRateAmount(exRateAmount.setScale(BaseConsts.TWO, BigDecimal.ROUND_HALF_UP));// 未税金额
			invoiceCollectFee.setRateAmount(rateAmount.setScale(BaseConsts.TWO, BigDecimal.ROUND_HALF_UP));// 税额
			invoiceCollectFee.setTaxRate(taxRateValue); // 税率
			invoiceCollectFee.setCollectId(collectId); // 收票id
			invoiceCollectFee.setCreateAt(date);
			invoiceCollectFee.setCreator(ServiceSupport.getUser().getChineseName());
			invoiceCollectFee.setCreatorId(ServiceSupport.getUser().getId());
			invoiceCollectFee.setIsDelete(BaseConsts.ZERO);
			InvoiceCollectFeeDao.insert(invoiceCollectFee);
			sum = DecimalUtil.add(sum, inRateAmount);

			FeeQueryModel oldEntity = feeService.queryEntityById(invoiceCollectFee.getFeeId()).getItems();
			Fee upFee = new Fee();
			upFee.setId(oldEntity.getId());
			upFee.setAcceptInvoiceAmount(DecimalUtil.add(oldEntity.getAcceptInvoiceAmount(), inRateAmount)
					.setScale(BaseConsts.TWO, BigDecimal.ROUND_HALF_UP));
			feeService.updateFeeById(upFee);// 修改费用收票金额
		}
		InvoiceCollect upEntity = new InvoiceCollect();
		upEntity.setId(collectId);
		upEntity.setInvoiceAmount(sum);
		invoiceCollectService.updateInvoiceCollectById(upEntity);// 修改收票金额
		return baseResult;
	}

	/**
	 * 修改数据
	 * 
	 * @param feeReqDto
	 * @return
	 */
	public BaseResult updateInvoiceCollectFeeById(InvoiceCollectFeeReqDto feeReqDto) {
		BaseResult baseResult = new BaseResult();
		List<InvoiceCollectFee> colRel = feeReqDto.getColRel();
		for (InvoiceCollectFee invoiceCollectFee : colRel) {
			InvoiceCollectFee collectFee = InvoiceCollectFeeDao.queryEntityById(invoiceCollectFee.getId());
			FeeQueryModel oldEntity = feeService.queryEntityById(collectFee.getFeeId()).getItems();
			BigDecimal diffAmount = DecimalUtil.subtract(invoiceCollectFee.getInRateAmount(),
					collectFee.getInRateAmount());// 差额
			BigDecimal sum = DecimalUtil.add(diffAmount, oldEntity.getAcceptInvoiceAmount());// 加上以前
			if (DecimalUtil.gt(sum, oldEntity.getPayAmount())) {// 是否大于总额
				baseResult.setMsg("余额不足！");
				return baseResult;
			}
		}
		for (InvoiceCollectFee invoiceCollectFee : colRel) {
			BigDecimal inRateAmount = invoiceCollectFee.getInRateAmount(); // 含税金额
			InvoiceCollectFee collectFee = InvoiceCollectFeeDao.queryEntityById(invoiceCollectFee.getId());

			Integer collectId = collectFee.getCollectId();// 获取开票基本信息税率
			InvoiceCollectResDto conllect = invoiceCollectService.queryInvoiceCollectById(collectId);
			BigDecimal taxRateValue = new BigDecimal(conllect.getInvoiceTaxRateValue()); // 税率

			BigDecimal exAlgorithm = DecimalUtil.add(taxRateValue, new BigDecimal(BaseConsts.ONE));
			BigDecimal rateAlgorithm = DecimalUtil.divide(taxRateValue,
					DecimalUtil.add(taxRateValue, new BigDecimal(BaseConsts.ONE)));
			BigDecimal exRateAmount = DecimalUtil.divide(inRateAmount, exAlgorithm);// 未税金额=费用含税金额/(1+收票税率)
			BigDecimal rateAmount = DecimalUtil.multiply(inRateAmount, rateAlgorithm);// 税额
																						// =费用含税金额/(1+收票税率)*收票税率
			invoiceCollectFee.setInRateAmount(inRateAmount); // 含税金额
			invoiceCollectFee.setExRateAmount(exRateAmount.setScale(BaseConsts.TWO, BigDecimal.ROUND_HALF_UP));// 未税金额
			invoiceCollectFee.setRateAmount(rateAmount.setScale(BaseConsts.TWO, BigDecimal.ROUND_HALF_UP));// 税额
			invoiceCollectFee.setTaxRate(taxRateValue); // 税率
			InvoiceCollectFeeDao.updateById(invoiceCollectFee);

			BigDecimal diffAmount = DecimalUtil.subtract(inRateAmount, collectFee.getInRateAmount());
			InvoiceCollect upEntity = new InvoiceCollect();
			upEntity.setId(collectId);
			upEntity.setInvoiceAmount(DecimalUtil.add(diffAmount, conllect.getInvoiceAmount()).setScale(BaseConsts.TWO,
					BigDecimal.ROUND_HALF_UP));
			invoiceCollectService.updateInvoiceCollectById(upEntity);// 修改收票金额

			FeeQueryModel oldEntity = feeService.queryEntityById(collectFee.getFeeId()).getItems();
			Fee upFee = new Fee();
			upFee.setId(oldEntity.getId());
			upFee.setAcceptInvoiceAmount(DecimalUtil.add(diffAmount, oldEntity.getAcceptInvoiceAmount())
					.setScale(BaseConsts.TWO, BigDecimal.ROUND_HALF_UP));
			feeService.updateFeeById(upFee);// 修改费用收票金额
		}
		return baseResult;
	}

	/**
	 * 删除数据
	 * 
	 * @param feeReqDto
	 * @return
	 */
	public BaseResult deleteInvoiceCollectFeeById(InvoiceCollectFeeReqDto feeReqDto) {
		BaseResult baseResult = new BaseResult();
		for (Integer id : feeReqDto.getIds()) {
			InvoiceCollectFee oldEntity = InvoiceCollectFeeDao.queryEntityById(id);
			InvoiceCollectResDto conllect = invoiceCollectService.queryInvoiceCollectById(oldEntity.getCollectId());
			InvoiceCollect upEntity = new InvoiceCollect();
			upEntity.setId(oldEntity.getCollectId());
			upEntity.setInvoiceAmount(DecimalUtil.subtract(conllect.getInvoiceAmount(), oldEntity.getInRateAmount())
					.setScale(BaseConsts.TWO, BigDecimal.ROUND_HALF_UP)); // 除掉含税金额
			invoiceCollectService.updateInvoiceCollectById(upEntity);// 修改收票金额

			FeeQueryModel oldFeeEntity = feeService.queryEntityById(oldEntity.getFeeId()).getItems();
			Fee upFee = new Fee();
			upFee.setId(oldFeeEntity.getId());
			upFee.setAcceptInvoiceAmount(
					DecimalUtil.subtract(oldFeeEntity.getAcceptInvoiceAmount(), oldEntity.getInRateAmount())
							.setScale(BaseConsts.TWO, BigDecimal.ROUND_HALF_UP));
			feeService.updateFeeById(upFee);// 修改费用收票金额

			InvoiceCollectFee invoiceCollectFee = new InvoiceCollectFee();
			invoiceCollectFee.setId(id);
			invoiceCollectFee.setIsDelete(BaseConsts.ONE);
			InvoiceCollectFeeDao.updateById(invoiceCollectFee);

		}
		return baseResult;
	}

	/**
	 * 编辑数据
	 * 
	 * @param invoiceCollectFee
	 * @return
	 */
	public Result<InvoiceCollectFeeResDto> editInvoiceCollectFeeById(InvoiceCollectFee invoiceCollectFee) {
		Result<InvoiceCollectFeeResDto> result = new Result<InvoiceCollectFeeResDto>();
		InvoiceCollectFeeResDto resDto = convertInvoiceCollectFeeResDto(
				InvoiceCollectFeeDao.queryInvoiceCollectFeeById(invoiceCollectFee.getId()));
		result.setItems(resDto);
		return result;

	}

	/**
	 * 通过收票Id获取收票费用信息
	 * 
	 * @param collectId
	 * @return
	 */
	public List<InvoiceCollectFeeResDto> queryInvoiceCollectFeeByCollectId(int collectId) {
		InvoiceCollectFeeReqDto feeReqDto = new InvoiceCollectFeeReqDto();
		feeReqDto.setCollectId(collectId);
		return convertToInvoiceCollectFeeResDtos(InvoiceCollectFeeDao.queryResultsByCon(feeReqDto));
	}

	/**
	 * 获取费用信息
	 * 
	 * @param feeReqDto
	 * @return
	 */
	public PageResult<InvoiceCollectFeeResDto> queryInvoiceCollectFeeResultsByCon(InvoiceCollectFeeReqDto feeReqDto) {
		PageResult<InvoiceCollectFeeResDto> pageResult = new PageResult<InvoiceCollectFeeResDto>();
		int offSet = PageUtil.getOffSet(feeReqDto.getPage(), feeReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, feeReqDto.getPer_page());
		List<InvoiceCollectFeeResDto> payFeeRelationResDto = convertToInvoiceCollectFeeResDtos(
				InvoiceCollectFeeDao.queryResultsByCon(feeReqDto, rowBounds));
		pageResult.setItems(payFeeRelationResDto);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), feeReqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(feeReqDto.getPage());
		pageResult.setPer_page(feeReqDto.getPer_page());

		List<InvoiceCollectFeeResDto> sumFee = convertToInvoiceCollectFeeResDtos(
				InvoiceCollectFeeDao.queryResultsByCon(feeReqDto, rowBounds));
		BigDecimal inRateAmountSum = BigDecimal.ZERO;
		BigDecimal exRateAmountSum = BigDecimal.ZERO;
		BigDecimal rateAmountSum = BigDecimal.ZERO;
		if (CollectionUtils.isNotEmpty(sumFee)) {
			for (InvoiceCollectFeeResDto fee : sumFee) {
				inRateAmountSum = DecimalUtil.add(inRateAmountSum, fee.getInRateAmount());
				exRateAmountSum = DecimalUtil.add(exRateAmountSum, fee.getExRateAmount());
				rateAmountSum = DecimalUtil.add(rateAmountSum, fee.getRateAmount());
			}
		}
		String totalStr = "费用金额  : " + DecimalUtil.formatScale2(inRateAmountSum) + "  &nbsp;&nbsp;&nbsp;  费用未税金额: "
				+ DecimalUtil.formatScale2(exRateAmountSum) + "  &nbsp;&nbsp;&nbsp;  税额: "
				+ DecimalUtil.formatScale2(rateAmountSum);
		pageResult.setTotalStr(totalStr);
		return pageResult;
	}

	public List<InvoiceCollectFeeResDto> convertToInvoiceCollectFeeResDtos(List<InvoiceCollectFee> result) {
		List<InvoiceCollectFeeResDto> invoiceCollectFeeResDto = new ArrayList<InvoiceCollectFeeResDto>();
		if (ListUtil.isEmpty(result)) {
			return invoiceCollectFeeResDto;
		}
		for (InvoiceCollectFee model : result) {
			InvoiceCollectFeeResDto collectFeeResDto = convertInvoiceCollectFeeResDto(model);
			invoiceCollectFeeResDto.add(collectFeeResDto);
		}
		return invoiceCollectFeeResDto;
	}

	public InvoiceCollectFeeResDto convertInvoiceCollectFeeResDto(InvoiceCollectFee model) {
		InvoiceCollectFeeResDto result = new InvoiceCollectFeeResDto();
		result.setId(model.getId());
		result.setCollectId(model.getCollectId());
		result.setFeeId(model.getFeeId());
		result.setFeeNo(model.getFeeNo());
		result.setFeeType(model.getFeeType());
		result.setFeeTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.FEE_TYPE, model.getFeeType() + ""));
		result.setPayFeeSpec(model.getPayFeeSpec());
		result.setPayFeeSpecName(cacheService.getFeeSpecNoNameById(model.getPayFeeSpec()));
		result.setPayDate(model.getPayDate());
		result.setAcceptInvoiceTaxRate(model.getAcceptInvoiceTaxRate());
		result.setPayAmount(model.getPayAmount());
		result.setTaxRate(model.getTaxRate());
		result.setInRateAmount(model.getInRateAmount());
		result.setExRateAmount(model.getExRateAmount());
		result.setRateAmount(model.getRateAmount());
		result.setBlance(model.getBlance());
		return result;
	}
}
