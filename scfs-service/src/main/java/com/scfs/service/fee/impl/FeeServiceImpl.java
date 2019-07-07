package com.scfs.service.fee.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.beust.jcommander.internal.Maps;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.OperateConsts;
import com.scfs.common.consts.SeqConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.ListUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.fee.FeeDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.domain.BaseResult;
import com.scfs.domain.CodeValue;
import com.scfs.domain.audit.entity.AuditNode;
import com.scfs.domain.base.entity.BaseProject;
import com.scfs.domain.base.entity.BaseSubject;
import com.scfs.domain.base.entity.BaseUser;
import com.scfs.domain.common.entity.AsyncExcel;
import com.scfs.domain.fee.dto.req.QueryFeeReqDto;
import com.scfs.domain.fee.dto.resp.FeeQueryResDto;
import com.scfs.domain.fee.dto.resp.FeeSumModel;
import com.scfs.domain.fee.entity.Fee;
import com.scfs.domain.fee.entity.FeeQueryModel;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.audit.FeeAuditService;
import com.scfs.service.base.auditFlow.AuditFlowService;
import com.scfs.service.common.AsyncExcelService;
import com.scfs.service.common.CommonParamValidate;
import com.scfs.service.common.SequenceService;
import com.scfs.service.project.ProjectPoolService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ExcelService;
import com.scfs.service.support.ServiceSupport;
import com.google.common.collect.Lists;

/**
 * <pre>
 * 
 *  File: FeeServiceImpl.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年10月13日			Administrator
 *
 * </pre>
 */
@Service
public class FeeServiceImpl {
	@Autowired
	FeeDao feeDao;
	@Autowired
	CacheService cacheService;
	@Autowired
	SequenceService sequenceService;
	@Autowired
	FeeAuditService feeAuditService;
	@Autowired
	AsyncExcelService asyncExcelService;
	@Autowired
	CommonParamValidate commonParamValidate;
	@Autowired
	ProjectPoolService projectPoolService;
	@Autowired
	AuditFlowService auditFlowService;

	public Result<Integer> addFee(Fee fee) {
		switch (fee.getFeeType()) {
		case BaseConsts.ONE:
			if (fee.getProvideInvoiceType() == BaseConsts.TWO || fee.getProvideInvoiceType() == BaseConsts.THREE) {
				if (fee.getProvideInvoiceTaxRate() == null) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "开票税率不能为空");
				}
			} else {
				fee.setProvideInvoiceTaxRate(DecimalUtil.ZERO);
			}
			break;
		case BaseConsts.TWO:
			if (fee.getAcceptInvoiceType() == BaseConsts.TWO || fee.getAcceptInvoiceType() == BaseConsts.THREE) {
				if (fee.getAcceptInvoiceTaxRate() == null) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "收票税率不能为空");
				}
			} else {
				fee.setAcceptInvoiceTaxRate(DecimalUtil.ZERO);
			}
			break;
		case BaseConsts.THREE:
			if (fee.getProvideInvoiceType() == BaseConsts.TWO || fee.getProvideInvoiceType() == BaseConsts.THREE) {
				if (fee.getProvideInvoiceTaxRate() == null) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "开票税率不能为空");
				}
			} else {
				fee.setProvideInvoiceTaxRate(DecimalUtil.ZERO);
			}
			if (fee.getAcceptInvoiceType() == BaseConsts.TWO || fee.getAcceptInvoiceType() == BaseConsts.THREE) {
				if (fee.getAcceptInvoiceTaxRate() == null) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "收票税率不能为空");
				}
			} else {
				fee.setAcceptInvoiceTaxRate(DecimalUtil.ZERO);
			}
			if (fee.getCustReceiver().equals(fee.getCustPayer())) {
				throw new BaseException(ExcMsgEnum.PAYER_RECEIVER_EQUAL);
			}
			break;
		default:
			break;

		}
		BaseUser user = ServiceSupport.getUser();
		Result<Integer> br = new Result<Integer>();
		String feeNo = sequenceService.getNumDateByBusName(BaseConsts.FE_NO_PREFIX, SeqConsts.S_FEE_NO,
				BaseConsts.INT_13);
		fee.setFeeNo(feeNo);
		fee.setState(BaseConsts.ONE);
		fee.setCreator(user.getChineseName());
		fee.setCreatorId(user.getId());
		int result = feeDao.insert(fee);
		if (result <= 0) {
			br.setMsg("插入失败，请重试");
		}
		br.setItems(fee.getId());
		return br;
	}

	public BaseResult updateFeeById(Fee fee) {
		Fee entity = feeDao.queryEntityById(fee.getId());
		switch (entity.getFeeType()) {
		case BaseConsts.ONE:
			if (null != fee.getProvideInvoiceType() && fee.getProvideInvoiceType() == BaseConsts.ONE) {
				fee.setProvideInvoiceTaxRate(DecimalUtil.ZERO);
			}
			break;
		case BaseConsts.TWO:
			if (null != fee.getAcceptInvoiceType() && fee.getAcceptInvoiceType() == BaseConsts.ONE) {
				fee.setAcceptInvoiceTaxRate(DecimalUtil.ZERO);
			}
			break;
		case BaseConsts.THREE:
			if (null != fee.getProvideInvoiceType() && fee.getProvideInvoiceType() == BaseConsts.ONE) {
				fee.setProvideInvoiceTaxRate(DecimalUtil.ZERO);
			}
			if (null != fee.getAcceptInvoiceType() && fee.getAcceptInvoiceType() == BaseConsts.ONE) {
				fee.setAcceptInvoiceTaxRate(DecimalUtil.ZERO);
			}
			if (null != fee.getCustReceiver() && null != fee.getCustPayer()
					&& fee.getCustReceiver().equals(fee.getCustPayer())) {
				throw new BaseException(ExcMsgEnum.PAYER_RECEIVER_EQUAL);
			}
			break;
		default:
			break;
		}
		BaseResult br = new BaseResult();
		feeDao.updateById(fee);
		return br;
	}

	public BaseResult deleteFeeById(Fee fee) {
		Fee feeQueryModel = feeDao.queryEntityById(fee.getId());
		if (feeQueryModel.getState() != BaseConsts.ONE) {
			throw new BaseException(ExcMsgEnum.ALREADY_DELETE, feeQueryModel.getId());
		}
		if (feeQueryModel.getIsDelete() == BaseConsts.ONE) {
			throw new BaseException(ExcMsgEnum.FEE_DELETE_ERROR);
		}
		BaseUser user = ServiceSupport.getUser();
		BaseResult br = new BaseResult();
		fee.setDeleteAt(new Date());
		fee.setIsDelete(BaseConsts.ONE);
		fee.setDeleter(user.getChineseName());
		fee.setDeleterId(user.getId());
		feeDao.deleteById(fee);
		return br;
	}

	public BaseResult submitFeeById(Fee fee) throws Exception {
		BaseResult br = new BaseResult();
		Fee feeQueryModel = feeDao.queryEntityById(fee.getId());
		if (feeQueryModel.getState() != BaseConsts.ONE) { // 待财务专员审核
			throw new BaseException(ExcMsgEnum.FEE_SUBMIT_ERROR);
		}
		Integer poType = null;
		switch (feeQueryModel.getFeeType()) {
		case BaseConsts.ONE: // 应收
			poType = BaseConsts.FOUR;
			break;
		case BaseConsts.TWO: // 应付
			poType = BaseConsts.EIGHT;
			break;
		case BaseConsts.THREE: // 应收应付
			poType = BaseConsts.NINE;
			break;
		}
		AuditNode startAuditNode = auditFlowService.getStartAuditNode(poType, feeQueryModel.getProjectId());
		if (null == startAuditNode) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请设置审核流");
		}
		fee.setState(startAuditNode.getAuditNodeState());
		feeDao.submitById(fee);
		feeAuditService.startAudit(fee.getId(), startAuditNode);
		return br;
	}

	public Result<FeeQueryModel> queryEntityById(Integer id) {
		Result<FeeQueryModel> result = new Result<FeeQueryModel>();
		Fee fee = feeDao.queryEntityById(id);
		FeeQueryModel feeQueryModel = new FeeQueryModel();
		BeanUtils.copyProperties(fee, feeQueryModel);
		feeQueryModel.setPayFeeSpecName(cacheService.getFeeSpecNoNameById(feeQueryModel.getPayFeeSpec()));
		feeQueryModel.setRecFeeSpecName(cacheService.getFeeSpecNoNameById(feeQueryModel.getRecFeeSpec()));
		feeQueryModel.setProjectName(cacheService.getProjectNameById(feeQueryModel.getProjectId()));
		if (feeQueryModel.getDeductionType() != null) {
			feeQueryModel.setDeductionTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEDUCTION_TYPE,
					feeQueryModel.getDeductionType() + ""));
		}
		result.setItems(feeQueryModel);
		return result;
	}

	public Result<FeeQueryResDto> detailEntityById(Integer id) {
		Result<FeeQueryResDto> result = new Result<FeeQueryResDto>();
		Fee fee = feeDao.queryEntityById(id);
		result.setItems(convertToFeeQueryResDto(fee));
		return result;
	}

	public PageResult<FeeQueryResDto> queryFeeByCond(QueryFeeReqDto queryFeeReqDto) {
		PageResult<FeeQueryResDto> pageResult = new PageResult<FeeQueryResDto>();
		queryFeeReqDto.setUserId(ServiceSupport.getUser().getId());
		int offSet = PageUtil.getOffSet(queryFeeReqDto.getPage(), queryFeeReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, queryFeeReqDto.getPer_page());
		List<FeeQueryResDto> list = convertToListDtos(feeDao.queryFeeByCond(queryFeeReqDto, rowBounds));
		if (queryFeeReqDto.getNeedSum() != null && queryFeeReqDto.getNeedSum().equals(BaseConsts.ONE)) { // 需要合计
			BigDecimal recAmountSum = BigDecimal.ZERO;
			BigDecimal receivedAmountSum = BigDecimal.ZERO;
			BigDecimal provideInvoiceAmountSum = BigDecimal.ZERO;
			BigDecimal paidAmountSum = BigDecimal.ZERO;
			BigDecimal payAmountSum = BigDecimal.ZERO;
			BigDecimal acceptInvoiceAmountSum = BigDecimal.ZERO;
			List<FeeSumModel> feeSumModels = feeDao.queryFeeSumByCond(queryFeeReqDto);
			for (FeeSumModel model : feeSumModels) {
				recAmountSum = DecimalUtil.add(recAmountSum,
						ServiceSupport.amountNewToRMB(model.getRecAmountSum(), model.getCurrencyType(), new Date()));
				receivedAmountSum = DecimalUtil.add(receivedAmountSum, ServiceSupport
						.amountNewToRMB(model.getReceivedAmountSum(), model.getCurrencyType(), new Date()));
				provideInvoiceAmountSum = DecimalUtil.add(provideInvoiceAmountSum, ServiceSupport
						.amountNewToRMB(model.getProvideInvoiceAmountSum(), model.getCurrencyType(), new Date()));
				paidAmountSum = DecimalUtil.add(paidAmountSum,
						ServiceSupport.amountNewToRMB(model.getPaidAmountSum(), model.getCurrencyType(), new Date()));
				payAmountSum = DecimalUtil.add(payAmountSum,
						ServiceSupport.amountNewToRMB(model.getPayAmountSum(), model.getCurrencyType(), new Date()));
				acceptInvoiceAmountSum = DecimalUtil.add(acceptInvoiceAmountSum, ServiceSupport
						.amountNewToRMB(model.getAcceptInvoiceAmountSum(), model.getCurrencyType(), new Date()));
			}
			String totalStr = "";
			switch (queryFeeReqDto.getFeeType()) {
			case BaseConsts.ONE:
				totalStr = "应收费用 : " + DecimalUtil.formatScale2(recAmountSum) + " CNY   已收费用: "
						+ DecimalUtil.formatScale2(receivedAmountSum) + " CNY   已开票金额: "
						+ DecimalUtil.formatScale2(provideInvoiceAmountSum) + " CNY";
				break;
			case BaseConsts.TWO:
				totalStr = "应付费用 : " + DecimalUtil.formatScale2(payAmountSum) + " CNY   已付费用: "
						+ DecimalUtil.formatScale2(paidAmountSum) + " CNY   已收票金额: "
						+ DecimalUtil.formatScale2(acceptInvoiceAmountSum) + " CNY";
				break;
			case BaseConsts.THREE:
				totalStr = "应收费用 : " + DecimalUtil.formatScale2(recAmountSum) + " CNY   已收费用: "
						+ DecimalUtil.formatScale2(receivedAmountSum) + " CNY   已开票金额: "
						+ DecimalUtil.formatScale2(provideInvoiceAmountSum) + " CNY  应付费用 : "
						+ DecimalUtil.formatScale2(payAmountSum) + " CNY   已付费用: "
						+ DecimalUtil.formatScale2(paidAmountSum) + " CNY   已收票金额: "
						+ DecimalUtil.formatScale2(acceptInvoiceAmountSum) + " CNY";
				break;
			case BaseConsts.FOUR:
				totalStr = "应收抵扣费用 : " + DecimalUtil.formatScale2(recAmountSum) + " CNY   已收费用: "
						+ DecimalUtil.formatScale2(provideInvoiceAmountSum) + " CNY";
				break;
			case BaseConsts.FIVE:
				totalStr = "应付抵扣费用 : " + DecimalUtil.formatScale2(payAmountSum) + " CNY   已付费用: "
						+ DecimalUtil.formatScale2(acceptInvoiceAmountSum) + " CNY";
				break;
			default:
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "费用类型有误，请核查");
			}
			pageResult.setTotalStr(totalStr);
		}
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), queryFeeReqDto.getPer_page());
		pageResult.setItems(list);
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(queryFeeReqDto.getPage());
		pageResult.setPer_page(queryFeeReqDto.getPer_page());
		return pageResult;

	}

	public List<FeeQueryResDto> queryListByCon(QueryFeeReqDto queryFeeReqDto) {
		queryFeeReqDto.setUserId(ServiceSupport.getUser().getId());
		List<FeeQueryResDto> list = convertToListDtos(feeDao.queryFeeByCond(queryFeeReqDto));
		return list;
	}

	public List<FeeQueryResDto> convertToListDtos(List<Fee> result) {
		List<FeeQueryResDto> list = new ArrayList<FeeQueryResDto>();
		if (ListUtil.isEmpty(result)) {
			return list;
		}
		for (Fee fee : result) {
			FeeQueryResDto recFeeQueryResDto = convertToFeeQueryResDto(fee);
			List<CodeValue> operList = getOperList(fee.getState(), fee.getFeeType());
			recFeeQueryResDto.setOpertaList(operList);
			list.add(recFeeQueryResDto);
		}
		return list;
	}

	private FeeQueryResDto convertToFeeQueryResDto(Fee feeQueryResult) {
		FeeQueryResDto dto = new FeeQueryResDto();
		BeanUtils.copyProperties(feeQueryResult, dto);
		dto.setCurrencyTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
				feeQueryResult.getCurrencyType() + ""));
		dto.setCustPayerName(cacheService.getSubjectNoNameById(feeQueryResult.getCustPayer()));
		dto.setCustReceiverName(cacheService.getSubjectNoNameById(feeQueryResult.getCustReceiver()));
		dto.setPayAssistFeeSpecName(ServiceSupport.getValueByBizCode(BizCodeConsts.PAY_ASSIST_FEE_SPEC,
				feeQueryResult.getPayAssistFeeSpec() + ""));
		dto.setRecAssistFeeSpecName(ServiceSupport.getValueByBizCode(BizCodeConsts.REC_ASSIST_FEE_SPEC,
				feeQueryResult.getRecAssistFeeSpec() + ""));
		BaseProject baseProject = cacheService.getProjectById(feeQueryResult.getProjectId());
		if (baseProject != null) {
			BaseSubject busiUnit = cacheService.getBusiUnitById(baseProject.getBusinessUnitId());
			dto.setBusiUnitName(busiUnit.getChineseName());
			dto.setBusiUnitAddress(busiUnit.getOfficeAddress());
			dto.setBusiUnitNameNo(busiUnit.getSubjectNo() + "-" + busiUnit.getChineseName());
		}
		dto.setExpireDate(feeQueryResult.getExpireDate());
		if (feeQueryResult.getFeeType().equals(BaseConsts.ONE)) {
			dto.setPayFeeTypeName(
					ServiceSupport.getValueByBizCode(BizCodeConsts.REC_FEE_TYPE, feeQueryResult.getPayFeeType() + ""));
		} else if (feeQueryResult.getFeeType().equals(BaseConsts.TWO)) {
			dto.setPayFeeTypeName(
					ServiceSupport.getValueByBizCode(BizCodeConsts.PAY_FEE_TYPE, feeQueryResult.getPayFeeType() + ""));
		}
		dto.setProjectName(cacheService.getProjectNameById(feeQueryResult.getProjectId()));
		dto.setRecTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.REC_TYPE, feeQueryResult.getRecType() + ""));
		dto.setPayTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.PAY_TYPE, feeQueryResult.getPayType() + ""));
		dto.setProvideInvoiceTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.PROVIDE_INVOICE_TYPE,
				feeQueryResult.getProvideInvoiceType() + ""));
		if (feeQueryResult.getDeductionType() != null) {
			dto.setDeductionTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEDUCTION_TYPE,
					feeQueryResult.getDeductionType() + ""));
		}
		dto.setAcceptInvoiceType(feeQueryResult.getAcceptInvoiceType());
		dto.setAcceptInvoiceTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.ACCEPT_INVOICE_TYPE,
				feeQueryResult.getAcceptInvoiceType() + ""));
		dto.setState(feeQueryResult.getState());
		dto.setStateName(ServiceSupport.getValueByBizCode(BizCodeConsts.FEE_STATE, feeQueryResult.getState() + ""));
		dto.setFeeTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.FEE_TYPE, feeQueryResult.getFeeType() + ""));
		dto.setPayFeeSpecName(cacheService.getFeeSpecNoNameById(feeQueryResult.getPayFeeSpec()));
		dto.setRecFeeSpecName(cacheService.getFeeSpecNoNameById(feeQueryResult.getRecFeeSpec()));
		if (dto.getAcceptInvoiceTaxRate() != null) {
			dto.setAcceptInvoiceTaxRateStr(dto.getAcceptInvoiceTaxRate().toString());
		}
		if (dto.getProvideInvoiceTaxRate() != null) {
			dto.setProvideInvoiceTaxRateStr(dto.getProvideInvoiceTaxRate().toString());
		}
		dto.setSystemTime(new Date());
		dto.setBlanceFeeAmount(DecimalUtil.subtract(feeQueryResult.getPayAmount(), feeQueryResult.getPaidAmount()));
		return dto;
	}

	/**
	 * 根据状态得到操作列表
	 * 
	 * @param state
	 * @return
	 */
	private List<String> getOperListByState(Integer state) {
		List<String> opertaList = Lists.newArrayList();
		if (state == null) {
			return opertaList;
		}
		switch (state) {
		// 状态,1表示待提交，2表示待财务审核，3表示已完成
		case BaseConsts.ONE:
			opertaList.add(OperateConsts.DETAIL);
			opertaList.add(OperateConsts.SUBMIT);
			opertaList.add(OperateConsts.EDIT);
			opertaList.add(OperateConsts.DELETE);
			break;
		case BaseConsts.INT_25:
			opertaList.add(OperateConsts.DETAIL);
			break;
		case BaseConsts.INT_30:
			opertaList.add(OperateConsts.DETAIL);
			break;
		case BaseConsts.THREE:
			opertaList.add(OperateConsts.DETAIL);
			opertaList.add(OperateConsts.PRINT);
			break;
		}

		return opertaList;
	}

	/**
	 * 获取操作列表
	 * 
	 * @param state
	 * @return
	 */
	private List<CodeValue> getOperList(Integer state, int feeType) {
		if (ServiceSupport.getUser() == null) {
			return null;
		}
		// 1.根据状态得到操作列表
		List<String> operNameList = getOperListByState(state);
		List<CodeValue> oprResult = Lists.newArrayList();
		if (!CollectionUtils.isEmpty(operNameList)) {
			for (String operName : operNameList) {
				String url = FeeQueryResDto.Operate.operMap.get(operName).get(feeType);
				boolean isPermed = ServiceSupport.isAllowPerm(url);
				if (isPermed) {
					CodeValue codeValue = new CodeValue(url, operName);
					oprResult.add(codeValue);
				}
			}
		}
		return oprResult;
	}

	public void updatePrintNum(Integer id, Integer printType) {
		Fee fee = feeDao.queryEntityById(id);
		Fee feeupdate = new Fee();
		feeupdate.setId(id);
		switch (printType) {
		case BaseConsts.SIX:
			feeupdate.setRecPrintNum(fee.getRecPrintNum() + 1);
			break;
		case BaseConsts.SEVEN:
			feeupdate.setPayPrintNum(fee.getPayPrintNum() + 1);
			break;
		case BaseConsts.EIGHT:
			feeupdate.setPayRecPrintNum(fee.getPayRecPrintNum() + 1);
			break;
		}
		feeDao.updatePrintNum(feeupdate);
	}

	public boolean isExcelExportOverMaxLine(QueryFeeReqDto req) {
		req.setUserId(ServiceSupport.getUser().getId());
		int count = feeDao.queryCountByCond(req);
		if (count >= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			// 后台导出
			AsyncExcel asyncExcel = new AsyncExcel();
			asyncExcel.setName("费用单单据导出");
			asyncExcel.setClassName(this.getClass().getName());// 导出类名
			asyncExcel.setMethodName("asyncFeeExport");// 导出方法
			switch (req.getFeeType()) {
			case BaseConsts.ONE:
				asyncExcel.setTemplatePath("/WEB-INF/excel/export/fee/payFee_list.xls");// 导出模板路径
				break;
			case BaseConsts.TWO:
				asyncExcel.setTemplatePath("/WEB-INF/excel/export/fee/recFee_list.xls");// 导出模板路径
				break;
			case BaseConsts.THREE:
				asyncExcel.setTemplatePath("/WEB-INF/excel/export/fee/recPayFee_list.xls");// 导出模板路径
				break;
			case BaseConsts.FOUR:
				asyncExcel.setTemplatePath("/WEB-INF/excel/export/fee/recDeductionFee_list.xls");// 导出模板路径
				break;
			case BaseConsts.FIVE:
				asyncExcel.setTemplatePath("/WEB-INF/excel/export/fee/payDeductionFee_list.xls");// 导出模板路径
				break;
			default:
				break;
			}

			asyncExcel.setPoType(BaseConsts.NINE);
			asyncExcelService.addAsyncExcel(req, asyncExcel);
			return true;
		}
		return false;
	}

	public Map<String, Object> asyncFeeExport(QueryFeeReqDto req) {
		Map<String, Object> model = Maps.newHashMap();
		List<FeeQueryResDto> fees = queryAllResultsByCon(req);
		model.put("feeList", fees);
		return model;
	}

	public List<FeeQueryResDto> queryAllResultsByCon(QueryFeeReqDto req) {
		return convertToListDtos(feeDao.queryFeeByCond(req));
	}

	/**
	 * 应收费用导入Excel 业务逻辑处理
	 *
	 * @param importFile
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void importRecFeeExcel(MultipartFile importFile) {
		List<FeeQueryResDto> feeReceiveList = Lists.newArrayList();
		Map beans = Maps.newHashMap();
		beans.put("feeReceiveList", feeReceiveList);
		ExcelService.resolverExcel(importFile, "/excel/fee/fee_receive.xml", beans);
		// 业务逻辑处理
		feeReceiveList = (List<FeeQueryResDto>) beans.get("feeReceiveList");
		if (CollectionUtils.isNotEmpty(feeReceiveList)) {
			if (feeReceiveList.size() > BaseConsts.IMPORT_EXCEL_ROWS_MAX) {
				throw new BaseException(ExcMsgEnum.IMPORT_EXCEL_ROWS_MAX);
			}
			boolean result = true;
			for (FeeQueryResDto receive : feeReceiveList) {// 校验信息是否正确
				receive.setFeeType(BaseConsts.ONE);
				result = validateColletInfo(receive);
			}
			if (result) {
				for (FeeQueryResDto receive : feeReceiveList) {
					Fee fee = new Fee();
					fee.setFeeType(BaseConsts.ONE);
					fee.setProjectId(receive.getProjectId());
					fee.setCustPayer(receive.getCustPayer());
					fee.setRecFeeSpec(receive.getRecFeeSpec());
					fee.setRecType(receive.getRecType());
					fee.setRecDate(receive.getRecDate());
					fee.setCurrencyType(receive.getCurrencyType());
					fee.setRecAmount(receive.getRecAmount());
					fee.setProvideInvoiceType(receive.getProvideInvoiceType());
					fee.setProvideInvoiceTaxRate(receive.getProvideInvoiceTaxRate());
					fee.setRecNote(receive.getRecNote());
					addFee(fee);
				}
			}
		} else {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "导入应收费用信息不能为空");
		}
	}

	/**
	 * 应付费用导入Excel 业务逻辑处理
	 *
	 * @param importFile
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void importPayFeeExcel(MultipartFile importFile) {
		List<FeeQueryResDto> payFeeList = Lists.newArrayList();
		Map beans = Maps.newHashMap();
		beans.put("payFeeList", payFeeList);
		ExcelService.resolverExcel(importFile, "/excel/fee/payFee_receive.xml", beans);
		// 业务逻辑处理
		payFeeList = (List<FeeQueryResDto>) beans.get("payFeeList");
		if (CollectionUtils.isNotEmpty(payFeeList)) {
			if (payFeeList.size() > BaseConsts.IMPORT_EXCEL_ROWS_MAX) {
				throw new BaseException(ExcMsgEnum.IMPORT_EXCEL_ROWS_MAX);
			}
			boolean result = true;
			for (FeeQueryResDto receive : payFeeList) {// 校验信息是否正确
				receive.setFeeType(BaseConsts.TWO);
				result = validateColletInfo(receive);
			}
			if (result) {
				for (FeeQueryResDto receive : payFeeList) {
					Fee fee = new Fee();
					fee.setFeeType(BaseConsts.TWO);
					fee.setProjectId(receive.getProjectId());
					fee.setCustReceiver(receive.getCustReceiver());
					fee.setPayFeeSpec(receive.getPayFeeSpec());
					fee.setPayType(receive.getPayType());
					fee.setPayDate(receive.getPayDate());
					fee.setCurrencyType(receive.getCurrencyType());
					fee.setPayAmount(receive.getPayAmount());
					fee.setAcceptInvoiceType(receive.getAcceptInvoiceType());
					fee.setAcceptInvoiceTaxRate(receive.getAcceptInvoiceTaxRate());
					fee.setPayNote(receive.getPayNote());
					addFee(fee);
				}
			}
		} else {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "导入应付费用信息不能为空");
		}
	}

	/**
	 * 应收应付费用导入Excel 业务逻辑处理
	 *
	 * @param importFile
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void importRecPayFeeExcel(MultipartFile importFile) {
		List<FeeQueryResDto> recPayFeeList = Lists.newArrayList();
		Map beans = Maps.newHashMap();
		beans.put("recPayFeeList", recPayFeeList);
		ExcelService.resolverExcel(importFile, "/excel/fee/recPayFee_receive.xml", beans);
		// 业务逻辑处理
		recPayFeeList = (List<FeeQueryResDto>) beans.get("recPayFeeList");
		if (CollectionUtils.isNotEmpty(recPayFeeList)) {
			if (recPayFeeList.size() > BaseConsts.IMPORT_EXCEL_ROWS_MAX) {
				throw new BaseException(ExcMsgEnum.IMPORT_EXCEL_ROWS_MAX);
			}
			boolean result = true;
			for (FeeQueryResDto receive : recPayFeeList) {// 校验信息是否正确
				receive.setFeeType(BaseConsts.THREE);
				result = validateColletInfo(receive);
			}
			if (result) {
				for (FeeQueryResDto receive : recPayFeeList) {
					Fee fee = new Fee();
					fee.setFeeType(BaseConsts.THREE);
					fee.setProjectId(receive.getProjectId());
					fee.setCustPayer(receive.getCustPayer());
					fee.setRecFeeSpec(receive.getRecFeeSpec());
					fee.setRecType(receive.getRecType());
					fee.setRecDate(receive.getRecDate());
					fee.setCurrencyType(receive.getCurrencyType());
					fee.setRecAmount(receive.getRecAmount());
					fee.setProvideInvoiceType(receive.getProvideInvoiceType());
					fee.setProvideInvoiceTaxRate(receive.getProvideInvoiceTaxRate());
					fee.setRecNote(receive.getRecNote());

					fee.setCustReceiver(receive.getCustReceiver());
					fee.setPayFeeSpec(receive.getPayFeeSpec());
					fee.setPayType(receive.getPayType());
					fee.setPayDate(receive.getPayDate());
					fee.setCurrencyType(receive.getCurrencyType());
					fee.setPayAmount(receive.getPayAmount());
					fee.setAcceptInvoiceType(receive.getAcceptInvoiceType());
					fee.setAcceptInvoiceTaxRate(receive.getAcceptInvoiceTaxRate());
					fee.setPayNote(receive.getPayNote());
					addFee(fee);
				}
			}
		} else {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "导入应收应付费用信息不能为空");
		}
	}

	/**
	 * 应付费用导入Excel 业务逻辑处理
	 *
	 * @param importFile
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void importRecDeductionFeeExcel(MultipartFile importFile) {
		List<FeeQueryResDto> feeRecDelist = Lists.newArrayList();
		Map beans = Maps.newHashMap();
		beans.put("feeRecDelist", feeRecDelist);
		ExcelService.resolverExcel(importFile, "/excel/fee/fee_rec_deduction.xml", beans);
		// 业务逻辑处理
		feeRecDelist = (List<FeeQueryResDto>) beans.get("feeRecDelist");
		if (CollectionUtils.isNotEmpty(feeRecDelist)) {
			if (feeRecDelist.size() > BaseConsts.IMPORT_EXCEL_ROWS_MAX) {
				throw new BaseException(ExcMsgEnum.IMPORT_EXCEL_ROWS_MAX);
			}
			boolean result = true;
			for (FeeQueryResDto receive : feeRecDelist) {// 校验信息是否正确
				receive.setFeeType(BaseConsts.FOUR);
				result = validateColletInfo(receive);
			}
			if (result) {
				for (FeeQueryResDto receive : feeRecDelist) {
					Fee fee = new Fee();
					fee.setFeeType(BaseConsts.FOUR);
					fee.setProjectId(receive.getProjectId());
					fee.setCustPayer(receive.getCustPayer());
					fee.setDeductionType(receive.getDeductionType());
					fee.setRecFeeSpec(receive.getRecFeeSpec());
					fee.setRecType(receive.getRecType());
					fee.setRecDate(receive.getRecDate());
					fee.setCurrencyType(receive.getCurrencyType());
					fee.setRecAmount(receive.getRecAmount());
					fee.setRecNote(receive.getRecNote());
					addFee(fee);
				}
			}
		} else {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "导入应收抵扣信息不能为空");
		}
	}

	/**
	 * 应付费用导入Excel 业务逻辑处理
	 *
	 * @param importFile
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void importPayDeductionFeeExcel(MultipartFile importFile) {
		List<FeeQueryResDto> feePayDeList = Lists.newArrayList();
		Map beans = Maps.newHashMap();
		beans.put("feePayDeList", feePayDeList);
		ExcelService.resolverExcel(importFile, "/excel/fee/fee_pay_deduction.xml", beans);
		// 业务逻辑处理
		feePayDeList = (List<FeeQueryResDto>) beans.get("feePayDeList");
		if (CollectionUtils.isNotEmpty(feePayDeList)) {
			if (feePayDeList.size() > BaseConsts.IMPORT_EXCEL_ROWS_MAX) {
				throw new BaseException(ExcMsgEnum.IMPORT_EXCEL_ROWS_MAX);
			}
			boolean result = true;
			for (FeeQueryResDto receive : feePayDeList) {// 校验信息是否正确
				receive.setFeeType(BaseConsts.FIVE);
				result = validateColletInfo(receive);
			}
			if (result) {
				for (FeeQueryResDto receive : feePayDeList) {
					Fee fee = new Fee();
					fee.setFeeType(BaseConsts.FIVE);
					fee.setProjectId(receive.getProjectId());
					fee.setCustReceiver(receive.getCustReceiver());
					fee.setDeductionType(receive.getDeductionType());
					fee.setPayFeeSpec(receive.getPayFeeSpec());
					fee.setPayType(receive.getPayType());
					fee.setPayDate(receive.getPayDate());
					fee.setCurrencyType(receive.getCurrencyType());
					fee.setPayAmount(receive.getPayAmount());
					fee.setPayNote(receive.getPayNote());
					addFee(fee);
				}
			}
		} else {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "导入应付抵扣信息不能为空");
		}
	}

	/**
	 * 校验导入费用数据
	 * 
	 * @param baseGoodsResDto
	 * @return
	 */
	private boolean validateColletInfo(FeeQueryResDto receive) {
		boolean result = true;
		String projectName = receive.getProjectName();
		if (projectName == null || projectName.equals("")) {
			result = false;
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "项目不能为空");
		}
		String currencyTypeName = receive.getCurrencyTypeName();
		if (currencyTypeName == null || currencyTypeName.equals("")) {
			result = false;
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "币种不能为空");
		}
		String projectId = commonParamValidate.userProjectValidate(projectName);
		String currencyTypeId = commonParamValidate.cvListByBizCodeValidate("DEFAULT_CURRENCY_TYPE", currencyTypeName);
		switch (receive.getFeeType()) {
		case BaseConsts.ONE:// 应收
			this.feeType_1(result, receive, projectId, currencyTypeId, BaseConsts.ONE);
			break;
		case BaseConsts.TWO:// 应付
			this.feeType_2(result, receive, projectId, currencyTypeId, BaseConsts.TWO);
			break;
		case BaseConsts.THREE:// 应收应付
			String custPayerName = receive.getCustPayerName();
			if (custPayerName == null || custPayerName.equals("")) {
				result = false;
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "应收客户不能为空");
			}
			String recFeeSpecName = receive.getRecFeeSpecName();
			if (recFeeSpecName == null || recFeeSpecName.equals("")) {
				result = false;
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "应收费用科目不能为空");
			}
			String recTypeName = receive.getRecTypeName();
			if (recTypeName == null || recTypeName.equals("")) {
				result = false;
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "应收方式不能为空");
			}
			Date recDate = receive.getRecDate();
			if (recDate == null) {
				result = false;
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "应收日期不能为空");
			}
			BigDecimal recAmount = receive.getRecAmount();
			if (recAmount == null) {
				result = false;
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "应收金额不能为空");
			}
			String provideInvoiceTypeName = receive.getProvideInvoiceTypeName();
			if (provideInvoiceTypeName == null || provideInvoiceTypeName.equals("")) {
				result = false;
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "开票方式不能为空");
			}

			// 判断项目名
			if (projectId == null) {
				result = false;
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "项目名" + projectName + "不存在");
			}
			receive.setProjectId(Integer.parseInt(projectId));
			// 判断项目下的应收客户
			String cusId = commonParamValidate.projectBcsValidate(projectId, custPayerName);
			if (cusId == null) {
				result = false;
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "项目名" + projectName + "应收客户" + custPayerName + "不存在");
			}
			receive.setCustPayer(Integer.parseInt(cusId));
			// 费用科目相关
			String recFeeSpecId = commonParamValidate.feeValidate("REC_FEE_SPEC", recFeeSpecName);
			if (recFeeSpecId == null) {
				result = false;
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "应收费用科目" + recFeeSpecName + "不存在");
			}
			receive.setRecFeeSpec(Integer.parseInt(recFeeSpecId));
			// 应收方式
			String recType = commonParamValidate.cvListByBizCodeValidate("REC_TYPE", recTypeName);
			if (recType == null) {
				result = false;
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "应收方式" + recTypeName + "不存在");
			}
			receive.setRecType(Integer.parseInt(recType));
			// 币种
			if (currencyTypeId == null) {
				result = false;
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "币种" + currencyTypeName + "不存在");
			}
			receive.setCurrencyType(Integer.parseInt(currencyTypeId));
			// 开票方式
			String provideInvoiceId = commonParamValidate.cvListByBizCodeValidate("PROVIDE_INVOICE_TYPE",
					provideInvoiceTypeName);
			if (provideInvoiceId == null) {
				result = false;
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "开票方式" + provideInvoiceTypeName + "不存在");
			}
			receive.setProvideInvoiceType(Integer.parseInt(provideInvoiceId));

			// 税率
			String provideInvoiceTaxRateStr = receive.getProvideInvoiceTaxRateStr();
			String rateId = null;
			if (Integer.parseInt(provideInvoiceId) == BaseConsts.TWO
					|| Integer.parseInt(provideInvoiceId) == BaseConsts.THREE) {
				if (provideInvoiceTaxRateStr == null) {
					result = false;
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "开票税率不能为空");
				}
				// 税率
				rateId = commonParamValidate.cvListByBizCodeValidate("PROVIDE_INVOICE_TAX_RATE",
						provideInvoiceTaxRateStr);
				if (rateId == null) {
					result = false;
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "开票税率" + provideInvoiceTaxRateStr + "不存在");
				}
				receive.setProvideInvoiceTaxRate(new BigDecimal(provideInvoiceTaxRateStr));
			} else {
				receive.setProvideInvoiceTaxRate(DecimalUtil.ZERO);
			}
			// 判断备注长度
			String recNote = receive.getRecNote();
			if (recNote != null) {
				if (recNote.length() > BaseConsts.IMPORT_EXCEL_ROWS_MAX) {
					result = false;
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "备注不能大于1000字");
				}
			}

			String custReceiverName = receive.getCustReceiverName();
			if (custReceiverName == null || custReceiverName.equals("")) {
				result = false;
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "应付客户不能为空");
			}
			String payFeeSpecName = receive.getPayFeeSpecName();
			if (payFeeSpecName == null || payFeeSpecName.equals("")) {
				result = false;
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "应付费用科目不能为空");
			}
			String payTypeName = receive.getPayTypeName();
			if (payTypeName == null || payTypeName.equals("")) {
				result = false;
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "应付方式不能为空");
			}
			Date payDate = receive.getPayDate();
			if (payDate == null) {
				result = false;
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "应付日期不能为空");
			}
			BigDecimal payAmount = receive.getPayAmount();
			if (payAmount == null) {
				result = false;
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "应付金额不能为空");
			}
			String acceptInvoiceTypeName = receive.getAcceptInvoiceTypeName();
			if (acceptInvoiceTypeName == null || acceptInvoiceTypeName.equals("")) {
				result = false;
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "收票方式不能为空");
			}

			// 判断项目下的应付客户
			String custReceiver = commonParamValidate.projectBcsValidate(projectId, custReceiverName);
			if (custReceiver == null) {
				result = false;
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL,
						"项目名" + projectName + "应付客户" + custReceiverName + "不存在");
			}
			receive.setCustReceiver(Integer.parseInt(custReceiver));
			// 应付费用科目
			String payFeeSpecId = commonParamValidate.feeValidate("PAY_FEE_SPEC", payFeeSpecName);
			if (payFeeSpecId == null) {
				result = false;
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "应付费用科目" + payFeeSpecName + "不存在");
			}
			receive.setPayFeeSpec(Integer.parseInt(payFeeSpecId));
			// 应付方式
			String payType = commonParamValidate.cvListByBizCodeValidate("PAY_TYPE", payTypeName);
			if (payType == null) {
				result = false;
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "应付方式" + payTypeName + "不存在");
			}
			receive.setPayType(Integer.parseInt(payType));
			// 收票方式
			String acceptInvoiceType = commonParamValidate.cvListByBizCodeValidate("ACCEPT_INVOICE_TYPE",
					acceptInvoiceTypeName);
			if (acceptInvoiceType == null) {
				result = false;
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "收票方式" + acceptInvoiceTypeName + "不存在");
			}
			receive.setAcceptInvoiceType(Integer.parseInt(acceptInvoiceType));

			// 税率
			String acceptInvoiceTaxRateStr = receive.getAcceptInvoiceTaxRateStr();
			String acceptId = null;
			if (Integer.parseInt(acceptInvoiceType) == BaseConsts.TWO
					|| Integer.parseInt(acceptInvoiceType) == BaseConsts.THREE) {
				if (acceptInvoiceTaxRateStr == null) {
					result = false;
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "收票方式不是无票时，收票税率不能为空");
				}
				// 税率
				acceptId = commonParamValidate.cvListByBizCodeValidate("ACCEPT_INVOICE_TAX_RATE",
						acceptInvoiceTaxRateStr);
				if (acceptId == null) {
					result = false;
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "收票税率" + acceptInvoiceTaxRateStr + "不存在");
				}
				receive.setAcceptInvoiceTaxRate(new BigDecimal(acceptInvoiceTaxRateStr));
			} else {
				receive.setAcceptInvoiceTaxRate(DecimalUtil.ZERO);
			}

			// 判断备注长度
			String payNote = receive.getPayNote();
			if (payNote != null) {
				if (payNote.length() > BaseConsts.IMPORT_EXCEL_ROWS_MAX) {
					result = false;
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "备注不能大于1000字");
				}
			}
			if (receive.getCustReceiver().equals(receive.getCustPayer())) {
				result = false;
				throw new BaseException(ExcMsgEnum.PAYER_RECEIVER_EQUAL);
			}
			break;
		case BaseConsts.FOUR:// 应收抵扣
			this.feeType_1(result, receive, projectId, currencyTypeId, BaseConsts.FOUR);
			break;
		case BaseConsts.FIVE:// 应付抵扣
			this.feeType_2(result, receive, projectId, currencyTypeId, BaseConsts.FIVE);
			break;
		default:
			break;
		}
		return result;
	}

	/**
	 * 应收导入的数据校验
	 * 
	 * @param result
	 * @param receive
	 * @param projectId
	 * @param currencyTypeId
	 */
	private void feeType_1(boolean result, FeeQueryResDto receive, String projectId, String currencyTypeId,
			Integer feeType) {
		String projectName = receive.getProjectName();
		String currencyTypeName = receive.getCurrencyTypeName();
		String custPayerName = receive.getCustPayerName();
		if (StringUtils.isBlank(custPayerName)) {
			result = false;
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "应收客户不能为空");
		}
		String recFeeSpecName = receive.getRecFeeSpecName();
		if (recFeeSpecName == null || recFeeSpecName.equals("")) {
			result = false;
			if (feeType == BaseConsts.ONE) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "应收费用科目不能为空");
			} else if (feeType == BaseConsts.FOUR) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "应收抵扣科目不能为空");
			}
		}
		String recTypeName = receive.getRecTypeName();
		if (recTypeName == null || recTypeName.equals("")) {
			result = false;
			if (feeType == BaseConsts.ONE) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "应收方式不能为空");
			} else if (feeType == BaseConsts.FOUR) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "应收抵扣方式不能为空");
			}

		}
		Date recDate = receive.getRecDate();
		if (recDate == null) {
			result = false;
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "应收日期不能为空");
		}
		BigDecimal recAmount = receive.getRecAmount();
		if (recAmount == null) {
			result = false;
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "应收金额不能为空");
		}
		if (feeType == BaseConsts.ONE) {
			String provideInvoiceTypeName = receive.getProvideInvoiceTypeName();
			if (provideInvoiceTypeName == null || provideInvoiceTypeName.equals("")) {
				result = false;
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "开票方式不能为空");
			}
			// 开票方式
			String provideInvoiceId = commonParamValidate.cvListByBizCodeValidate("PROVIDE_INVOICE_TYPE",
					provideInvoiceTypeName);
			if (provideInvoiceId == null) {
				result = false;
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "开票方式" + provideInvoiceTypeName + "不存在");
			}
			receive.setProvideInvoiceType(Integer.parseInt(provideInvoiceId));

			// 税率
			String provideInvoiceTaxRateStr = receive.getProvideInvoiceTaxRateStr();
			String rateId = null;
			if (Integer.parseInt(provideInvoiceId) == BaseConsts.TWO
					|| Integer.parseInt(provideInvoiceId) == BaseConsts.THREE) {
				if (provideInvoiceTaxRateStr == null) {
					result = false;
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "开票税率不能为空");
				}
				// 税率
				rateId = commonParamValidate.cvListByBizCodeValidate("PROVIDE_INVOICE_TAX_RATE",
						provideInvoiceTaxRateStr);
				if (rateId == null) {
					result = false;
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "开票税率" + provideInvoiceTaxRateStr + "不存在");
				}
				receive.setProvideInvoiceTaxRate(new BigDecimal(provideInvoiceTaxRateStr));
			} else {
				receive.setProvideInvoiceTaxRate(DecimalUtil.ZERO);
			}
		}
		if (feeType == BaseConsts.FOUR) {
			String deductionTypeName = receive.getDeductionTypeName();
			if (deductionTypeName == null || deductionTypeName.equals("")) {
				result = false;
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "抵扣类型不能为空");
			}
			// 应收方式
			String deductionType = commonParamValidate.cvListByBizCodeValidate("DEDUCTION_TYPE", deductionTypeName);
			if (deductionType == null) {
				result = false;
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "抵扣类型" + deductionTypeName + "不存在");
			}
			receive.setDeductionType(Integer.parseInt(deductionType));
		}
		// 判断项目名
		if (projectId == null) {
			result = false;
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "项目名" + projectName + "不存在");
		}
		receive.setProjectId(Integer.parseInt(projectId));
		// 判断项目下的应收客户
		String cusId = commonParamValidate.projectBcsValidate(projectId, custPayerName);
		if (cusId == null) {
			result = false;
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "项目名" + projectName + "应收客户" + custPayerName + "不存在");
		}
		receive.setCustPayer(Integer.parseInt(cusId));
		// 费用科目相关
		String recFeeSpecId = commonParamValidate.feeValidate("REC_FEE_SPEC", recFeeSpecName);
		if (recFeeSpecId == null) {
			result = false;
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "应收费用科目" + recFeeSpecName + "不存在");
		}
		receive.setRecFeeSpec(Integer.parseInt(recFeeSpecId));
		// 应收方式
		String recType = commonParamValidate.cvListByBizCodeValidate("REC_TYPE", recTypeName);
		if (recType == null) {
			result = false;
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "应收方式" + recTypeName + "不存在");
		}
		receive.setRecType(Integer.parseInt(recType));
		// 币种
		if (currencyTypeId == null) {
			result = false;
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "币种" + currencyTypeName + "不存在");
		}
		receive.setCurrencyType(Integer.parseInt(currencyTypeId));
		// 判断备注长度
		String recNote = receive.getRecNote();
		if (recNote != null) {
			if (recNote.length() > BaseConsts.IMPORT_EXCEL_ROWS_MAX) {
				result = false;
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "备注不能大于1000字");
			}
		}
	}

	/**
	 * 应付类型的校验
	 * 
	 * @param result
	 * @param receive
	 * @param projectId
	 * @param currencyTypeId
	 * @param feeType
	 */
	private void feeType_2(boolean result, FeeQueryResDto receive, String projectId, String currencyTypeId,
			Integer feeType) {
		String projectName = receive.getProjectName();
		String currencyTypeName = receive.getCurrencyTypeName();
		String custReceiverName = receive.getCustReceiverName();
		if (StringUtils.isBlank(custReceiverName)) {
			result = false;
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "应付客户不能为空");
		}
		String payFeeSpecName = receive.getPayFeeSpecName();
		if (payFeeSpecName == null || payFeeSpecName.equals("")) {
			result = false;
			if (feeType == BaseConsts.TWO) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "应付费用科目不能为空");
			} else if (feeType == BaseConsts.FIVE) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "应付抵扣科目不能为空");
			}
		}
		String payTypeName = receive.getPayTypeName();
		if (payTypeName == null || payTypeName.equals("")) {
			result = false;
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "应付方式不能为空");
		}
		Date payDate = receive.getPayDate();
		if (payDate == null) {
			result = false;
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "应付日期不能为空");
		}
		BigDecimal payAmount = receive.getPayAmount();
		if (payAmount == null) {
			result = false;
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "应付金额不能为空");
		}
		if (feeType == BaseConsts.TWO) {
			String acceptInvoiceTypeName = receive.getAcceptInvoiceTypeName();
			if (acceptInvoiceTypeName == null || acceptInvoiceTypeName.equals("")) {
				result = false;
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "收票方式不能为空");
			}
			// 收票方式
			String acceptInvoiceType = commonParamValidate.cvListByBizCodeValidate("ACCEPT_INVOICE_TYPE",
					acceptInvoiceTypeName);
			if (acceptInvoiceType == null) {
				result = false;
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "收票方式" + acceptInvoiceTypeName + "不存在");
			}
			receive.setAcceptInvoiceType(Integer.parseInt(acceptInvoiceType));

			// 税率
			String acceptInvoiceTaxRateStr = receive.getAcceptInvoiceTaxRateStr();
			String acceptId = null;
			if (Integer.parseInt(acceptInvoiceType) == BaseConsts.TWO
					|| Integer.parseInt(acceptInvoiceType) == BaseConsts.THREE) {
				if (acceptInvoiceTaxRateStr == null) {
					result = false;
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "收票方式不是无票时，收票税率不能为空");
				}
				// 税率
				acceptId = commonParamValidate.cvListByBizCodeValidate("ACCEPT_INVOICE_TAX_RATE",
						acceptInvoiceTaxRateStr);
				if (acceptId == null) {
					result = false;
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "收票税率" + acceptInvoiceTaxRateStr + "不存在");
				}
				receive.setAcceptInvoiceTaxRate(new BigDecimal(acceptInvoiceTaxRateStr));
			} else {
				receive.setAcceptInvoiceTaxRate(DecimalUtil.ZERO);
			}
		}
		if (feeType == BaseConsts.FIVE) {
			String deductionTypeName = receive.getDeductionTypeName();
			if (deductionTypeName == null || deductionTypeName.equals("")) {
				result = false;
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "抵扣类型不能为空");
			}
			// 应收方式
			String deductionType = commonParamValidate.cvListByBizCodeValidate("DEDUCTION_TYPE", deductionTypeName);
			if (deductionType == null) {
				result = false;
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "抵扣类型" + deductionTypeName + "不存在");
			}
			receive.setDeductionType(Integer.parseInt(deductionType));
		}
		// 判断项目名
		if (projectId == null) {
			result = false;
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "项目名" + projectName + "不存在");
		}
		receive.setProjectId(Integer.parseInt(projectId));
		// 判断项目下的应付客户
		String custReceiver = commonParamValidate.projectBcsValidate(projectId, custReceiverName);
		if (custReceiver == null) {
			result = false;
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "项目名" + projectName + "应付客户" + custReceiverName + "不存在");
		}
		receive.setCustReceiver(Integer.parseInt(custReceiver));
		// 应付费用科目
		String payFeeSpecId = commonParamValidate.feeValidate("PAY_FEE_SPEC", payFeeSpecName);
		if (payFeeSpecId == null) {
			result = false;
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "应付费用科目" + payFeeSpecName + "不存在");
		}
		receive.setPayFeeSpec(Integer.parseInt(payFeeSpecId));
		// 应付方式
		String payType = commonParamValidate.cvListByBizCodeValidate("PAY_TYPE", payTypeName);
		if (payType == null) {
			result = false;
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "应付方式" + payTypeName + "不存在");
		}
		receive.setPayType(Integer.parseInt(payType));
		// 币种
		if (currencyTypeId == null) {
			result = false;
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "币种" + currencyTypeName + "不存在");
		}
		receive.setCurrencyType(Integer.parseInt(currencyTypeId));
		// 判断备注长度
		String payNote = receive.getPayNote();
		if (payNote != null) {
			if (payNote.length() > BaseConsts.IMPORT_EXCEL_ROWS_MAX) {
				result = false;
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "备注不能大于1000字");
			}
		}
	}

	/**
	 * 获取应收应付费用关系
	 * 
	 * @param queryFeeReqDto
	 * @return
	 */
	public PageResult<FeeQueryResDto> queryFeePayByRecCond(QueryFeeReqDto queryFeeReqDto) {
		PageResult<FeeQueryResDto> pageResult = new PageResult<FeeQueryResDto>();
		queryFeeReqDto.setUserId(ServiceSupport.getUser().getId());
		int offSet = PageUtil.getOffSet(queryFeeReqDto.getPage(), queryFeeReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, queryFeeReqDto.getPer_page());
		List<FeeQueryResDto> list = convertToListDtos(feeDao.queryFeePayByRecCond(queryFeeReqDto, rowBounds));
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), queryFeeReqDto.getPer_page());
		pageResult.setItems(list);
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(queryFeeReqDto.getPage());
		pageResult.setPer_page(queryFeeReqDto.getPer_page());
		return pageResult;
	}

	public PageResult<FeeQueryResDto> queryFeePayByNotRecCond(QueryFeeReqDto queryFeeReqDto) {
		PageResult<FeeQueryResDto> pageResult = new PageResult<FeeQueryResDto>();
		queryFeeReqDto.setUserId(ServiceSupport.getUser().getId());
		int offSet = PageUtil.getOffSet(queryFeeReqDto.getPage(), queryFeeReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, queryFeeReqDto.getPer_page());
		List<FeeQueryResDto> list = convertToListDtos(feeDao.queryFeePayByNotRecCond(queryFeeReqDto, rowBounds));
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), queryFeeReqDto.getPer_page());
		pageResult.setItems(list);
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(queryFeeReqDto.getPage());
		pageResult.setPer_page(queryFeeReqDto.getPer_page());
		return pageResult;
	}
}
