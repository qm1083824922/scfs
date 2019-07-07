package com.scfs.service.invoice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.beust.jcommander.internal.Maps;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.CacheKeyConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.OperateConsts;
import com.scfs.common.consts.SeqConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.ListUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.invoice.InvoiceCollectDao;
import com.scfs.domain.BaseResult;
import com.scfs.domain.CodeValue;
import com.scfs.domain.audit.entity.AuditNode;
import com.scfs.domain.base.entity.BaseProject;
import com.scfs.domain.base.entity.BaseSubject;
import com.scfs.domain.common.dto.req.FileAttachSearchReqDto;
import com.scfs.domain.common.entity.AsyncExcel;
import com.scfs.domain.common.entity.FileAttach;
import com.scfs.domain.invoice.dto.req.InvoiceCollectFeeReqDto;
import com.scfs.domain.invoice.dto.req.InvoiceCollectPoReqDto;
import com.scfs.domain.invoice.dto.req.InvoiceCollectSearchReqDto;
import com.scfs.domain.invoice.dto.resp.InvoiceCollectFeeResDto;
import com.scfs.domain.invoice.dto.resp.InvoiceCollectFileResDto;
import com.scfs.domain.invoice.dto.resp.InvoiceCollectPoResDto;
import com.scfs.domain.invoice.dto.resp.InvoiceCollectResDto;
import com.scfs.domain.invoice.entity.InvoiceCollect;
import com.scfs.domain.invoice.entity.InvoiceCollectApprove;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.audit.InvoiceCollectAuditService;
import com.scfs.service.base.auditFlow.AuditFlowService;
import com.scfs.service.bookkeeping.InvoiceCollectBookkeepingService;
import com.scfs.service.common.AsyncExcelService;
import com.scfs.service.common.SequenceService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ExcelService;
import com.scfs.service.support.FileUploadService;
import com.scfs.service.support.ServiceSupport;
import com.google.common.collect.Lists;

/**
 * <pre>
 * 	收票相关业务
 *  File: InvoiceCollectService.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月28日			Administrator
 *
 * </pre>
 */
@Service
public class InvoiceCollectService {
	@Autowired
	private InvoiceCollectDao invoiceCollectDao;
	@Autowired
	private InvoiceCollectFeeService invoiceCollectFeeService;
	@Autowired
	private InvoiceCollectPoService invoiceCollectPoService;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private SequenceService sequenceService;
	@Autowired
	private InvoiceCollectAuditService invoiceCollectAuditService;// 审核
	@Autowired
	private InvoiceCollectBookkeepingService invoiceCollectBookkeepingService;
	@Autowired
	private FileUploadService fileUploadService;
	@Autowired
	private AsyncExcelService asyncExcelService;
	@Autowired
	private InvoiceCollectApproveService invoiceCollectApproveService;
	@Autowired
	private AuditFlowService auditFlowService;

	/**
	 * 新建收票
	 * 
	 * @param invoiceCollect
	 * @return
	 */
	public int createInvoiceCollect(InvoiceCollect invoiceCollect) {
		// 经营单位 根据项目ID获取经营单位ID
		if (invoiceCollect.getProjectId() != null) {
			BaseProject baseProject = cacheService.getProjectById(invoiceCollect.getProjectId());// 项目
			if (baseProject != null) {
				invoiceCollect.setBusinessUnit(baseProject.getBusinessUnitId());
			}
		}
		Date date = new Date();
		invoiceCollect.setState(BaseConsts.ONE);
		invoiceCollect.setInvoiceAmount(BigDecimal.ZERO);
		invoiceCollect.setCreateAt(date);
		invoiceCollect.setCreator(ServiceSupport.getUser().getChineseName());
		invoiceCollect.setCreatorId(ServiceSupport.getUser().getId());
		invoiceCollect.setIsDelete(BaseConsts.ZERO);
		invoiceCollect.setApplyNo(sequenceService.getNumDateByBusName(BaseConsts.COLLECT_APPLY_NO,
				SeqConsts.S_COLLECT_APPLY_NO, BaseConsts.INT_13));
		int id = invoiceCollectDao.insert(invoiceCollect);
		if (id <= BaseConsts.ZERO) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "添加失败:" + JSONObject.toJSON(invoiceCollect));
		}
		return invoiceCollect.getId();
	}

	/**
	 * 更新收票信息
	 * 
	 * @param invoiceCollect
	 * @return
	 */
	public BaseResult updateInvoiceCollectById(InvoiceCollect invoiceCollect) {
		BaseResult baseResult = new BaseResult();
		int result = invoiceCollectDao.updateById(invoiceCollect);
		if (result == BaseConsts.ZERO) {
			baseResult.setSuccess(false);
			baseResult.setMsg("更新收票信息失败，请重试");
		}
		return baseResult;
	}

	/**
	 * 提交
	 * 
	 * @param invoiceCollect
	 * @return
	 */
	public BaseResult sumitInvoiceCollectById(InvoiceCollect invoiceCollect) {
		BaseResult baseResult = new BaseResult();
		InvoiceCollect collect = invoiceCollectDao.queryEntityById(invoiceCollect.getId());// 锁表
		AuditNode startAuditNode = auditFlowService.getStartAuditNode(BaseConsts.INT_11, null);
		if (null == startAuditNode) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请设置审核流");
		}
		invoiceCollect.setState(startAuditNode.getAuditNodeState());

		invoiceCollectDao.updateById(invoiceCollect);
		invoiceCollectAuditService.startAudit(collect, startAuditNode);// 添加财务审核
		return baseResult;
	}

	/**
	 * 删除
	 * 
	 * @param invoiceCollect
	 * @return
	 */
	public BaseResult deleteInvoiceCollectById(InvoiceCollect invoiceCollect) {
		BaseResult baseResult = new BaseResult();
		invoiceCollectDao.queryEntityById(invoiceCollect.getId());// 锁表
		deleteRel(invoiceCollect.getId());
		invoiceCollect.setIsDelete(BaseConsts.ONE);
		invoiceCollectDao.updateById(invoiceCollect);
		return baseResult;
	}

	public void deleteRel(int collectId) {
		// 获取关联所有费用数据并删除
		List<InvoiceCollectFeeResDto> feeList = invoiceCollectFeeService.queryInvoiceCollectFeeByCollectId(collectId);
		if (feeList != null && feeList.size() > BaseConsts.ZERO) {
			List<Integer> ids = new ArrayList<Integer>();
			for (InvoiceCollectFeeResDto collectFee : feeList) {
				ids.add(collectFee.getId());
			}
			InvoiceCollectFeeReqDto feeReqDto = new InvoiceCollectFeeReqDto();
			feeReqDto.setIds(ids);
			invoiceCollectFeeService.deleteInvoiceCollectFeeById(feeReqDto);
		}
		// 删除关联所有采购单数据
		List<InvoiceCollectPoResDto> poList = invoiceCollectPoService.queryInvoiceCollectPoByCollectId(collectId);
		if (poList != null && poList.size() > BaseConsts.ZERO) {
			List<Integer> ids = new ArrayList<Integer>();
			for (InvoiceCollectPoResDto collectPo : poList) {
				ids.add(collectPo.getId());
			}
			InvoiceCollectPoReqDto poReqDto = new InvoiceCollectPoReqDto();
			;
			poReqDto.setIds(ids);
			invoiceCollectPoService.deleteInvoiceCollectPoByIds(poReqDto);
		}

	}

	/**
	 * 编辑
	 * 
	 * @param invoiceCollect
	 * @return
	 */
	public Result<InvoiceCollectResDto> editInvoiceCollectById(InvoiceCollect invoiceCollect) {
		Result<InvoiceCollectResDto> result = new Result<InvoiceCollectResDto>();
		InvoiceCollectResDto resDto = convertToToInvoiceCollectResDto(
				invoiceCollectDao.queryEntityById(invoiceCollect.getId()));
		result.setItems(resDto);
		return result;
	}

	/**
	 * 通过id获取数据
	 * 
	 * @param collectId
	 * @return
	 */
	public InvoiceCollectResDto queryInvoiceCollectById(int collectId) {
		return convertToToInvoiceCollectResDto(invoiceCollectDao.queryEntityById(collectId));
	}

	/**
	 * 收票认证信息
	 * 
	 * @param searchreqDto
	 * @return
	 */
	public PageResult<InvoiceCollectResDto> queryInvoiceCollectApproved(InvoiceCollectSearchReqDto searchreqDto) {
		PageResult<InvoiceCollectResDto> result = new PageResult<InvoiceCollectResDto>();
		searchreqDto.setState(BaseConsts.THREE);
		searchreqDto.setUserId(ServiceSupport.getUser().getId());
		List<InvoiceCollectResDto> invoiceCollectResDto = convertToInvoiceCollectResDtos(
				invoiceCollectDao.queryResultsByCon(searchreqDto));
		if (searchreqDto.getNeedSum() != null && searchreqDto.getNeedSum() == BaseConsts.ONE) {// 是否需要合计，1表示是，2表示否
			List<InvoiceCollect> sumResDto = invoiceCollectDao.sumInvoiceCollect(searchreqDto);
			if (CollectionUtils.isNotEmpty(sumResDto)) {
				BigDecimal invoiceAmountSum = BigDecimal.ZERO;
				for (InvoiceCollect invoiceCollect : sumResDto) {
					if (invoiceCollect != null) {
						invoiceAmountSum = DecimalUtil.add(invoiceAmountSum, invoiceCollect.getInvoiceAmount() == null
								? DecimalUtil.ZERO : invoiceCollect.getInvoiceAmount());
					}
				}
				String totalStr = "发票金额  : " + DecimalUtil.toAmountString(DecimalUtil.formatScale2(invoiceAmountSum))
						+ " CNY";
				result.setTotalStr(totalStr);
			}
		}
		result.setItems(invoiceCollectResDto);
		return result;
	}

	/**
	 * 收票认证
	 * 
	 * @param invoiceCollect
	 * @return
	 */
	public BaseResult sumitInvoiceCollectApproved(InvoiceCollect invoiceCollect) {
		BaseResult baseResult = new BaseResult();
		InvoiceCollect invoiceCollect2 = invoiceCollectDao.queryEntityById(invoiceCollect.getId());// 锁表
		BigDecimal currApproveAmount = null == invoiceCollect.getCurrApproveAmount() ? BigDecimal.ZERO
				: invoiceCollect.getCurrApproveAmount();
		BigDecimal approveAmount = null == invoiceCollect2.getApproveAmount() ? BigDecimal.ZERO
				: invoiceCollect2.getApproveAmount();
		BigDecimal invoiceAmount = null == invoiceCollect2.getInvoiceAmount() ? BigDecimal.ZERO
				: invoiceCollect2.getInvoiceAmount();
		approveAmount = DecimalUtil.add(currApproveAmount, approveAmount);
		if (DecimalUtil.le(approveAmount, invoiceAmount)) {
			if (DecimalUtil.eq(approveAmount, invoiceAmount)) {
				invoiceCollect.setState(BaseConsts.FOUR);
			}
			invoiceCollect.setApproveAmount(approveAmount);
			invoiceCollect.setUnApproveAmount(DecimalUtil.subtract(invoiceAmount, approveAmount));
			invoiceCollectDao.updateById(invoiceCollect);

			InvoiceCollectApprove invoiceCollectApprove = new InvoiceCollectApprove();
			invoiceCollectApprove.setInvoiceCollectId(invoiceCollect.getId());
			invoiceCollectApprove.setApproveAmount(currApproveAmount);
			invoiceCollectApprove.setApproveDate(invoiceCollect.getCurrApproveDate());
			invoiceCollectApprove.setApproveRemark(invoiceCollect.getCurrApproveRemark());
			invoiceCollectApproveService.createInvoiceCollectApprove(invoiceCollectApprove);

			invoiceCollectBookkeepingService.collectBookkeeping(invoiceCollect.getId(), invoiceCollectApprove.getId()); // 记账
		} else {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "认证后已认证金额将超出发票金额，请重新操作！");
		}
		return baseResult;
	}

	/**
	 * 收票票确认导入Excel 业务逻辑处理
	 *
	 * @param importFile
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void importExcel(MultipartFile importFile) {
		List<InvoiceCollectResDto> collectList = Lists.newArrayList();
		Map beans = Maps.newHashMap();
		beans.put("collectApproveList", collectList);
		ExcelService.resolverExcel(importFile, "/excel/invoice/collect_approve.xml", beans);
		// 业务逻辑处理
		collectList = (List<InvoiceCollectResDto>) beans.get("collectApproveList");
		if (CollectionUtils.isNotEmpty(collectList)) {
			if (collectList.size() > BaseConsts.IMPORT_EXCEL_ROWS_MAX) {
				throw new BaseException(ExcMsgEnum.IMPORT_EXCEL_ROWS_MAX);
			}
			boolean result = true;
			for (InvoiceCollectResDto collect : collectList) {// 校验信息是否正确
				result = validateColletInfo(collect);
			}
			if (result) {
				for (InvoiceCollectResDto collect : collectList) {
					InvoiceCollect collectInfo = new InvoiceCollect();
					collectInfo.setApplyNo(collect.getApplyNo());
					InvoiceCollect invoiceCollect = invoiceCollectDao.queryEntityByParam(collectInfo);
					collectInfo.setCurrApproveDate(collect.getCurrApproveDate());
					collectInfo.setCurrApproveRemark(collect.getCurrApproveRemark());
					collectInfo.setCurrApproveAmount(collect.getCurrApproveAmount());
					collectInfo.setId(invoiceCollect.getId());
					sumitInvoiceCollectApproved(collectInfo);
				}
			}
		} else {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "导入收票信息不能为空");
		}
	}

	/**
	 * 校验并更新导入发票
	 * 
	 * @param baseGoodsResDto
	 * @return
	 */
	private boolean validateColletInfo(InvoiceCollectResDto collect) {
		boolean result = true;
		String number = collect.getApplyNo();
		if (number == null || number.equals("")) {
			result = false;
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "申请编号不能为空");
		}
		if (collect.getCurrApproveDate() == null) {
			result = false;
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "认证日期不能为空");
		}
		if (collect.getCurrApproveAmount() == null) {
			result = false;
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "认证金额不能为空");
		}
		if (collect.getCurrApproveRemark() == null || collect.getCurrApproveRemark().equals("")) {
			result = false;
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "认证备注不能为空");
		} else {
			InvoiceCollect collectInfo = new InvoiceCollect();
			collectInfo.setApplyNo(number);
			InvoiceCollect invoiceCollect = invoiceCollectDao.queryEntityByParam(collectInfo);
			if (invoiceCollect != null) {
				String[] invoices = invoiceCollect.getInvoiceNo().split(",");
				boolean flag = false;
				for (String inv : invoices) {
					if (invoiceCollect.getInvoiceNo().equals(inv)) {
						flag = true;
						break;
					}
				}
				if (flag) {
					if (invoiceCollect.getState() != BaseConsts.THREE) {
						result = false;
						throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "收票已认证");
					}
				} else {
					result = false;
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "该收票号不存在");
				}
			} else {
				result = false;
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "该信息 不存在");
			}

		}
		return result;
	}

	/**
	 * 导出信息
	 * 
	 * @param searchreqDto
	 * @return
	 */
	public PageResult<InvoiceCollectResDto> queryInvoiceCollectResultsByEx(InvoiceCollectSearchReqDto searchreqDto) {
		PageResult<InvoiceCollectResDto> result = new PageResult<InvoiceCollectResDto>();
		if (searchreqDto.getUserId() == null) {
			searchreqDto.setUserId(ServiceSupport.getUser().getId());
		}
		List<InvoiceCollectResDto> invoiceCollectResDto = convertToInvoiceCollectResDtos(
				invoiceCollectDao.queryResultsByCon(searchreqDto));
		result.setItems(invoiceCollectResDto);
		return result;
	}

	/**
	 * 判断是否超出导出行数
	 * 
	 * @param searchreqDto
	 * @return
	 */
	public boolean isOverasyncMaxLine(InvoiceCollectSearchReqDto searchreqDto) {
		searchreqDto.setUserId(ServiceSupport.getUser().getId());
		int count = invoiceCollectDao.isOverasyncMaxLine(searchreqDto);
		//
		if (count >= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			// 后台导出
			AsyncExcel asyncExcel = new AsyncExcel();
			asyncExcel.setName("收票单据导出");
			asyncExcel.setClassName(this.getClass().getName());// 导出类名
			asyncExcel.setMethodName("asyncInvoiceApplyExport");// 导出方法
			asyncExcel.setTemplatePath("/WEB-INF/excel/export/invoice/collect/collect_list.xls");// 导出模板路径
			asyncExcel.setPoType(BaseConsts.SEVEN);
			asyncExcelService.addAsyncExcel(searchreqDto, asyncExcel);
			return true;
		}
		return false;
	}

	public Map<String, Object> asyncInvoiceApplyExport(InvoiceCollectSearchReqDto searchreqDto) {
		Map<String, Object> model = Maps.newHashMap();
		List<InvoiceCollectResDto> collectList = queryInvoiceCollectResultsByEx(searchreqDto).getItems();
		model.put("collectList", collectList);
		return model;
	}

	/**
	 * 收票管理页面
	 * 
	 * @param searchreqDto
	 * @return
	 */
	public PageResult<InvoiceCollectResDto> queryInvoiceCollectResultsByCon(InvoiceCollectSearchReqDto searchreqDto) {
		PageResult<InvoiceCollectResDto> pageResult = new PageResult<InvoiceCollectResDto>();
		int offSet = PageUtil.getOffSet(searchreqDto.getPage(), searchreqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, searchreqDto.getPer_page());
		searchreqDto.setUserId(ServiceSupport.getUser().getId());
		List<InvoiceCollectResDto> invoiceCollectResDto = convertToInvoiceCollectResDtos(
				invoiceCollectDao.queryResultsByCon(searchreqDto, rowBounds));

		if (searchreqDto.getNeedSum() != null && searchreqDto.getNeedSum() == BaseConsts.ONE) {// 是否需要合计，1表示是，2表示否
			List<InvoiceCollect> sumResDto = invoiceCollectDao.sumInvoiceCollect(searchreqDto);
			if (CollectionUtils.isNotEmpty(sumResDto)) {
				BigDecimal invoiceAmountSum = BigDecimal.ZERO;
				for (InvoiceCollect invoiceCollect : sumResDto) {
					if (invoiceCollect != null) {
						invoiceAmountSum = DecimalUtil.add(invoiceAmountSum, invoiceCollect.getInvoiceAmount() == null
								? DecimalUtil.ZERO : invoiceCollect.getInvoiceAmount());
					}
				}
				String totalStr = "发票金额  : " + DecimalUtil.toAmountString(DecimalUtil.formatScale2(invoiceAmountSum))
						+ " CNY";
				pageResult.setTotalStr(totalStr);
			}
		}

		pageResult.setItems(invoiceCollectResDto);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), searchreqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(searchreqDto.getPage());
		pageResult.setPer_page(searchreqDto.getPer_page());
		return pageResult;
	}

	public List<InvoiceCollectResDto> convertToInvoiceCollectResDtos(List<InvoiceCollect> result) {
		List<InvoiceCollectResDto> invoiceCollectResDto = new ArrayList<InvoiceCollectResDto>();
		if (ListUtil.isEmpty(result)) {
			return invoiceCollectResDto;
		}
		for (InvoiceCollect invoiceCollect : result) {
			InvoiceCollectResDto collectResDto = convertToToInvoiceCollectResDto(invoiceCollect);
			List<CodeValue> operList = getOperList(invoiceCollect.getState());
			collectResDto.setOpertaList(operList);
			invoiceCollectResDto.add(collectResDto);
		}
		return invoiceCollectResDto;
	}

	public InvoiceCollectResDto convertToToInvoiceCollectResDto(InvoiceCollect module) {
		InvoiceCollectResDto result = new InvoiceCollectResDto();
		result.setId(module.getId());
		result.setApplyNo(module.getApplyNo());
		// 经营单位
		result.setBusinessUnit(module.getBusinessUnit());
		result.setBusinessUnitName(
				cacheService.showSubjectNameByIdAndKey(module.getBusinessUnit(), CacheKeyConsts.BUSI_UNIT));
		result.setSystemTime(new Date());
		BaseProject baseProject = cacheService.getProjectById(module.getProjectId());
		if (null != baseProject) {
			BaseSubject busiUnit = cacheService.getBusiUnitById(baseProject.getBusinessUnitId());
			if (null != busiUnit) {
				result.setBusinessUnitNameValue(busiUnit.getChineseName());
				result.setBusinessUnitAddress(busiUnit.getOfficeAddress());
			}
		}

		// 项目
		result.setProjectId(module.getProjectId());
		result.setProjectName(cacheService.getProjectNameById(module.getProjectId()));
		// 供应商
		result.setSupplierId(module.getSupplierId());
		result.setSupplierName(cacheService.getSubjectNcByIdAndKey(module.getSupplierId(), CacheKeyConsts.CUSTOMER));
		// 票据类型
		result.setInvoiceType(module.getInvoiceType());
		result.setInvoiceTypeName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.INVOICE_TYPE, module.getInvoiceType() + ""));
		// 单据类型
		result.setBillType(module.getBillType());
		result.setBillTypeName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.DOCUMENT_TYPE, module.getBillType() + ""));
		result.setInvoiceAmount(module.getInvoiceAmount());
		result.setInvoiceNo(module.getInvoiceNo());
		result.setInvoiceDate(module.getInvoiceDate());
		result.setApproveAmount(module.getApproveAmount());
		result.setUnApproveAmount(module.getUnApproveAmount());
		// 税率
		result.setInvoiceTaxRate(module.getInvoiceTaxRate());
		result.setInvoiceTaxRateValue(ServiceSupport.getValueByBizCode(BizCodeConsts.ACCEPT_INVOICE_TAX_RATE,
				module.getInvoiceTaxRate() + ""));
		result.setInvoiceRemark(module.getInvoiceRemark());
		result.setState(module.getState());
		result.setStateName(ServiceSupport.getValueByBizCode(BizCodeConsts.CONLLECT_STATE, module.getState() + ""));
		result.setRemark(module.getRemark());
		result.setApproveDate(module.getApproveDate());
		result.setCreator(module.getCreator());
		result.setCreateAt(module.getCreateAt());
		return result;
	}

	/**
	 * 获取操作列表
	 * 
	 * @param state
	 * @return
	 */
	private List<CodeValue> getOperList(Integer state) {
		// 1.根据状态得到操作列表
		List<String> operNameList = getOperListByState(state);
		// 2.将上述的操作列表通过权限过滤
		List<CodeValue> oprResult = ServiceSupport.getOperListByPermission(operNameList,
				InvoiceCollectResDto.Operate.operMap);
		return oprResult;
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
		// 状态 1 待提交 25 待财务审核 3 待认证 4 已完成
		case BaseConsts.ONE:
			opertaList.add(OperateConsts.EDIT);
			opertaList.add(OperateConsts.DELETE);
			opertaList.add(OperateConsts.SUBMIT);
			opertaList.add(OperateConsts.DETAIL);
			break;
		default:
			opertaList.add(OperateConsts.DETAIL);
			opertaList.add(OperateConsts.PRINT);
			break;
		}

		return opertaList;
	}

	/**
	 * 获取文件操作列表
	 * 
	 * @param state
	 * @return
	 */
	public PageResult<InvoiceCollectFileResDto> queryFileList(FileAttachSearchReqDto fileAttReqDto) {
		PageResult<InvoiceCollectFileResDto> pageResult = new PageResult<InvoiceCollectFileResDto>();
		fileAttReqDto.setBusType(BaseConsts.SEVEN);
		List<FileAttach> fielAttach = fileUploadService.queryFileAttList(fileAttReqDto);
		List<InvoiceCollectFileResDto> list = convertToFileResDto(fielAttach);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), fileAttReqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(fileAttReqDto.getPage());
		pageResult.setPer_page(fileAttReqDto.getPer_page());
		pageResult.setItems(list);
		return pageResult;
	}

	public List<InvoiceCollectFileResDto> queryFileList(Integer collectId) {
		FileAttachSearchReqDto fileAttReqDto = new FileAttachSearchReqDto();
		fileAttReqDto.setBusId(collectId);
		fileAttReqDto.setBusType(BaseConsts.SEVEN);
		List<FileAttach> fielAttach = fileUploadService.queryFileAttList(fileAttReqDto);
		List<InvoiceCollectFileResDto> list = convertToFileResDto(fielAttach);
		return list;
	}

	private List<InvoiceCollectFileResDto> convertToFileResDto(List<FileAttach> fileAttach) {
		List<InvoiceCollectFileResDto> list = new LinkedList<InvoiceCollectFileResDto>();
		if (ListUtil.isEmpty(fileAttach)) {
			return list;
		}
		for (FileAttach model : fileAttach) {
			InvoiceCollectFileResDto result = new InvoiceCollectFileResDto();
			result.setId(model.getId());
			result.setBusId(model.getBusId());
			result.setBusTypeName(
					ServiceSupport.getValueByBizCode(BizCodeConsts.FILE_BUS_TYPE, model.getBusType() + ""));
			result.setName(model.getName());
			result.setType(model.getType());
			result.setCreateAt(model.getCreateAt());
			result.setCreator(model.getCreator());
			List<CodeValue> operList = getOperList();
			result.setOpertaList(operList);
			list.add(result);
		}
		return list;
	}

	private List<CodeValue> getOperList() {
		// 1.根据状态得到操作列表
		List<String> operNameList = getOperListByState();
		// 2.将上述的操作列表通过权限过滤
		List<CodeValue> oprResult = ServiceSupport.getOperListByPermission(operNameList,
				InvoiceCollectFileResDto.Operate.operMap);
		return oprResult;
	}

	/**
	 * 根据状态得到操作列表
	 * 
	 * @param state
	 * @return
	 */
	private List<String> getOperListByState() {
		List<String> opertaList = Lists.newArrayList();
		opertaList.add(OperateConsts.DOWNLOAD);
		opertaList.add(OperateConsts.DELETE);
		return opertaList;
	}

	public void updatePrintNum(Integer id) {
		InvoiceCollect invoiceCollect = invoiceCollectDao.queryEntityById(id);
		InvoiceCollect Invoice = new InvoiceCollect();
		Invoice.setId(id);
		Invoice.setPrintNum(invoiceCollect.getPrintNum() + 1);
		invoiceCollectDao.updatePrintNum(Invoice);
	}
}
