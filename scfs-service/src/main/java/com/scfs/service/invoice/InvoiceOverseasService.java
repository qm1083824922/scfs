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
import com.scfs.dao.invoice.InvoiceOverseasDao;
import com.scfs.domain.BaseResult;
import com.scfs.domain.CodeValue;
import com.scfs.domain.audit.entity.AuditNode;
import com.scfs.domain.base.entity.BaseAccount;
import com.scfs.domain.base.entity.BaseProject;
import com.scfs.domain.base.entity.BaseSubject;
import com.scfs.domain.common.dto.req.FileAttachSearchReqDto;
import com.scfs.domain.common.entity.AsyncExcel;
import com.scfs.domain.common.entity.FileAttach;
import com.scfs.domain.invoice.dto.req.InvoiceOverseasFeeReqDto;
import com.scfs.domain.invoice.dto.req.InvoiceOverseasPoReqDto;
import com.scfs.domain.invoice.dto.req.InvoiceOverseasSearchReqDto;
import com.scfs.domain.invoice.dto.resp.InvoiceOverseasFeeResDto;
import com.scfs.domain.invoice.dto.resp.InvoiceOverseasFileResDto;
import com.scfs.domain.invoice.dto.resp.InvoiceOverseasPoResDto;
import com.scfs.domain.invoice.dto.resp.InvoiceOverseasResDto;
import com.scfs.domain.invoice.entity.InvoiceOverseas;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.audit.InvoiceOverseasAuditService;
import com.scfs.service.base.auditFlow.AuditFlowService;
import com.scfs.service.common.AsyncExcelService;
import com.scfs.service.common.SequenceService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.FileUploadService;
import com.scfs.service.support.ServiceSupport;
import com.google.common.collect.Lists;

/**
 * <pre>
 * 	境外开票业务
 *  File: InvoiceOverseasService.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年03月28日				Administrator
 *
 * </pre>
 */
@Service
public class InvoiceOverseasService {
	@Autowired
	private InvoiceOverseasDao invoiceOverseasDao;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private SequenceService sequenceService;
	@Autowired
	private InvoiceOverseasPoService overseasPoService;
	@Autowired
	private InvoiceOverseasFeeService overseasFeeService;

	@Autowired
	private FileUploadService fileUploadService;
	@Autowired
	private AsyncExcelService asyncExcelService;
	@Autowired
	private InvoiceOverseasAuditService auditService;
	@Autowired
	private AuditFlowService auditFlowService;

	/**
	 * 新建收票
	 * 
	 * @param invoiceOverseas
	 * @return
	 */
	public int createInvoiceOverseas(InvoiceOverseas invoiceOverseas) {
		// 经营单位 根据项目ID获取经营单位ID
		if (invoiceOverseas.getBusinessUnit() == null) {
			BaseProject baseProject = cacheService.getProjectById(invoiceOverseas.getProjectId());// 项目
			if (baseProject != null) {
				invoiceOverseas.setBusinessUnit(baseProject.getBusinessUnitId());
			}
		}
		if (invoiceOverseas.getBillType() == BaseConsts.ONE) {
			invoiceOverseas.setFeeType(null);
		} else {
			invoiceOverseas.setIsMerge(null);
		}
		Date date = new Date();
		invoiceOverseas.setState(BaseConsts.ONE);
		invoiceOverseas.setCreateAt(date);
		invoiceOverseas.setCreator(ServiceSupport.getUser().getChineseName());
		invoiceOverseas.setCreatorId(ServiceSupport.getUser().getId());
		invoiceOverseas.setIsDelete(BaseConsts.ZERO);
		invoiceOverseas.setPrintNum(BaseConsts.ZERO);
		invoiceOverseas.setApplyNo(sequenceService.getNumDateByBusName(BaseConsts.OVERSEAS_APPLY_NO,
				SeqConsts.INVOICE_OVERSEAS_NO, BaseConsts.INT_13));
		int id = invoiceOverseasDao.insert(invoiceOverseas);
		if (id <= BaseConsts.ZERO) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "添加失败:" + JSONObject.toJSON(invoiceOverseas));
		}
		return invoiceOverseas.getId();
	}

	/**
	 * 更新收票信息
	 * 
	 * @param invoiceOverseas
	 * @return
	 */
	public BaseResult updateInvoiceOverseasById(InvoiceOverseas invoiceOverseas) {
		BaseResult baseResult = new BaseResult();
		int result = invoiceOverseasDao.updateById(invoiceOverseas);
		if (result == BaseConsts.ZERO) {
			baseResult.setSuccess(false);
			baseResult.setMsg("更新收票信息失败，请重试");
		}
		return baseResult;
	}

	/**
	 * 提交
	 * 
	 * @param invoiceOverseas
	 * @return
	 */
	public BaseResult sumitInvoiceOverseasById(InvoiceOverseas invoiceOverseas) {
		BaseResult baseResult = new BaseResult();
		InvoiceOverseas overseas = invoiceOverseasDao.queryEntityById(invoiceOverseas.getId());// 锁表
		if (DecimalUtil.gt(overseas.getInvoiceAmount(), BigDecimal.ZERO)) {
			AuditNode startAuditNode = auditFlowService.getStartAuditNode(BaseConsts.INT_19, null);
			if (null == startAuditNode) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请设置审核流");
			}
			invoiceOverseas.setState(startAuditNode.getAuditNodeState());
			invoiceOverseasDao.updateById(invoiceOverseas);
			auditService.startAudit(overseas, startAuditNode);
		} else {
			baseResult.setSuccess(false);
			baseResult.setMsg("开票金额必须大于0");
		}
		return baseResult;
	}

	public InvoiceOverseasResDto queryInvoiceCollectById(Integer id) {
		return convertToInvoiceOverseasResDto(invoiceOverseasDao.queryEntityById(id));
	}

	/**
	 * 编辑
	 * 
	 * @param invoiceCollect
	 * @return
	 */
	public Result<InvoiceOverseasResDto> editInvoiceCollectById(InvoiceOverseas invoiceOverseas) {
		Result<InvoiceOverseasResDto> result = new Result<InvoiceOverseasResDto>();
		InvoiceOverseasResDto resDto = convertToInvoiceOverseasResDto(
				invoiceOverseasDao.queryEntityById(invoiceOverseas.getId()));
		result.setItems(resDto);
		return result;
	}

	/**
	 * 删除
	 * 
	 * @param invoiceOverseas
	 * @return
	 */
	public BaseResult deleteInvoiceOverseasById(InvoiceOverseas invoiceOverseas) {
		BaseResult baseResult = new BaseResult();
		deleteRel(invoiceOverseas.getId());
		invoiceOverseasDao.queryEntityById(invoiceOverseas.getId());// 锁表
		invoiceOverseas.setIsDelete(BaseConsts.ONE);
		invoiceOverseasDao.updateById(invoiceOverseas);
		return baseResult;
	}

	public void deleteRel(int overseasId) {
		InvoiceOverseasPoReqDto poReqDto = new InvoiceOverseasPoReqDto();
		poReqDto.setOverseasId(overseasId);
		List<InvoiceOverseasPoResDto> poList = overseasPoService.queryInvoiceCollectPoResults(poReqDto);
		if (poList != null && poList.size() > BaseConsts.ZERO) {
			List<Integer> ids = new ArrayList<Integer>();
			for (InvoiceOverseasPoResDto overseasPoResDto : poList) {
				ids.add(overseasPoResDto.getId());
			}
			poReqDto.setIds(ids);
			overseasPoService.deleteInvoiceOverseasPo(poReqDto);
		}
		InvoiceOverseasFeeReqDto feeReqDto = new InvoiceOverseasFeeReqDto();
		feeReqDto.setOverseasId(overseasId);
		List<InvoiceOverseasFeeResDto> feeList = overseasFeeService.queryInvoiceCollectFeeResults(feeReqDto);
		if (feeList != null && feeList.size() > BaseConsts.ZERO) {
			List<Integer> ids = new ArrayList<Integer>();
			for (InvoiceOverseasFeeResDto overseasfeeResDto : feeList) {
				ids.add(overseasfeeResDto.getId());
			}
			feeReqDto.setIds(ids);
			overseasFeeService.deleteInvoiceOverseasFee(feeReqDto);
		}
	}

	/**
	 * 导出信息
	 * 
	 * @param searchreqDto
	 * @return
	 */
	public PageResult<InvoiceOverseasResDto> queryInvoiceOverseasResultsByEx(InvoiceOverseasSearchReqDto searchreqDto) {
		PageResult<InvoiceOverseasResDto> result = new PageResult<InvoiceOverseasResDto>();
		if (searchreqDto.getUserId() == null) {
			searchreqDto.setUserId(ServiceSupport.getUser().getId());
		}
		List<InvoiceOverseasResDto> invoiceCollectResDto = convertToInvoiceOverseasResDtos(
				invoiceOverseasDao.queryResultsByCon(searchreqDto));
		result.setItems(invoiceCollectResDto);
		return result;
	}

	/**
	 * 判断是否超出导出行数
	 * 
	 * @param searchreqDto
	 * @return
	 */
	public boolean isOverasyncMaxLine(InvoiceOverseasSearchReqDto searchreqDto) {
		searchreqDto.setUserId(ServiceSupport.getUser().getId());
		int count = invoiceOverseasDao.isOverasyncMaxLine(searchreqDto);
		//
		if (count >= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			// 后台导出
			AsyncExcel asyncExcel = new AsyncExcel();
			asyncExcel.setName("收票单据导出");
			asyncExcel.setClassName(this.getClass().getName());// 导出类名
			asyncExcel.setMethodName("asyncInvoiceOverseasExport");// 导出方法
			asyncExcel.setTemplatePath("/WEB-INF/excel/export/invoice/overseas/overseas_list.xls");// 导出模板路径
			asyncExcel.setPoType(BaseConsts.INT_19);
			asyncExcelService.addAsyncExcel(searchreqDto, asyncExcel);
			return true;
		}
		return false;
	}

	public Map<String, Object> asyncInvoiceOverseasExport(InvoiceOverseasSearchReqDto searchreqDto) {
		Map<String, Object> model = Maps.newHashMap();
		List<InvoiceOverseasResDto> overseasList = queryInvoiceOverseasResultsByEx(searchreqDto).getItems();
		model.put("overseasList", overseasList);
		return model;
	}

	/***
	 * 境外收票管理页面
	 * 
	 * @param searchreqDto
	 * @return
	 */
	public PageResult<InvoiceOverseasResDto> queryInvoiceOverseasResultsByCon(
			InvoiceOverseasSearchReqDto searchreqDto) {
		PageResult<InvoiceOverseasResDto> pageResult = new PageResult<InvoiceOverseasResDto>();
		int offSet = PageUtil.getOffSet(searchreqDto.getPage(), searchreqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, searchreqDto.getPer_page());
		searchreqDto.setUserId(ServiceSupport.getUser().getId());
		List<InvoiceOverseasResDto> invoiceOverseasResDto = convertToInvoiceOverseasResDtos(
				invoiceOverseasDao.queryResultsByCon(searchreqDto, rowBounds));

		if (searchreqDto.getNeedSum() != null && searchreqDto.getNeedSum() == BaseConsts.ONE) {// 是否需要合计，1表示是，2表示否
			List<InvoiceOverseas> sumResDto = invoiceOverseasDao.queryResultsByCon(searchreqDto);
			if (CollectionUtils.isNotEmpty(sumResDto)) {
				BigDecimal invoiceAmountSum = BigDecimal.ZERO;
				for (InvoiceOverseas invoiceOverseas : sumResDto) {
					if (invoiceOverseas != null) {
						invoiceAmountSum = DecimalUtil.add(invoiceAmountSum,
								ServiceSupport.amountNewToRMB(
										invoiceOverseas.getInvoiceAmount() == null ? DecimalUtil.ZERO
												: invoiceOverseas.getInvoiceAmount(),
										invoiceOverseas.getCurrnecyType(), new Date()));
					}
				}
				String totalStr = "收票金额  : " + DecimalUtil.toAmountString(DecimalUtil.formatScale2(invoiceAmountSum))
						+ " CNY";
				pageResult.setTotalStr(totalStr);
			}
		}

		pageResult.setItems(invoiceOverseasResDto);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), searchreqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(searchreqDto.getPage());
		pageResult.setPer_page(searchreqDto.getPer_page());
		return pageResult;
	}

	public List<InvoiceOverseasResDto> convertToInvoiceOverseasResDtos(List<InvoiceOverseas> result) {
		List<InvoiceOverseasResDto> invoiceOverseasResDto = new ArrayList<InvoiceOverseasResDto>();
		if (ListUtil.isEmpty(result)) {
			return invoiceOverseasResDto;
		}
		for (InvoiceOverseas invoiceOverseas : result) {
			InvoiceOverseasResDto collectResDto = convertToInvoiceOverseasResDto(invoiceOverseas);
			List<CodeValue> operList = getOperList(invoiceOverseas.getState());
			collectResDto.setOpertaList(operList);
			invoiceOverseasResDto.add(collectResDto);
		}
		return invoiceOverseasResDto;
	}

	public InvoiceOverseasResDto convertToInvoiceOverseasResDto(InvoiceOverseas module) {
		InvoiceOverseasResDto result = new InvoiceOverseasResDto();
		result.setId(module.getId());
		result.setApplyNo(module.getApplyNo());
		result.setBusinessUnit(module.getBusinessUnit());
		result.setBusinessUnitName(
				cacheService.showSubjectNameByIdAndKey(module.getBusinessUnit(), CacheKeyConsts.BUSI_UNIT));
		if (module.getBusinessUnit() != null) {
			BaseSubject baseSubject = cacheService.getBaseSubjectById(module.getBusinessUnit());
			if (baseSubject != null) {
				result.setBusinessChineseName(baseSubject.getChineseName());
				result.setEnglishName(baseSubject.getEnglishName());
				result.setAddress(baseSubject.getOfficeAddress());
				result.setRegPhone(baseSubject.getRegPhone());
			}
		}

		result.setProjectId(module.getProjectId());
		result.setProjectName(cacheService.getProjectNameById(module.getProjectId()));

		result.setCustomerId(module.getCustomerId());
		result.setCustomerName(cacheService.getSubjectNameByIdAndKey(module.getCustomerId(), CacheKeyConsts.BCS));
		if (module.getCustomerId() != null) {
			BaseSubject baseSubject = cacheService.getBaseSubjectById(module.getCustomerId());
			if (baseSubject != null) {
				result.setCustomerChineseName(baseSubject.getChineseName());
			}
		}
		result.setCurrnecyType(module.getCurrnecyType());
		result.setCurrnecyTypeName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE, module.getCurrnecyType() + ""));
		result.setPrintNum(module.getPrintNum());
		result.setInvoiceRemark(module.getInvoiceRemark());
		result.setBalanceStartDate(module.getBalanceStartDate());
		result.setBalanceEndDate(module.getBalanceEndDate());
		result.setBillType(module.getBillType());
		result.setBillTypeName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.DOCUMENT_TYPE, module.getBillType() + ""));
		result.setFeeType(module.getFeeType());
		result.setFeeTypeName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.BILL_DETAIL, module.getCurrnecyType() + ""));
		result.setIsMerge(module.getIsMerge());
		result.setInvoiceRemark(module.getInvoiceRemark());
		result.setRemark(module.getRemark());
		result.setState(module.getState());
		result.setStateName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.INVOKE_OVERSEAS_STATE, module.getState() + ""));
		result.setAccountId(module.getAccountId());
		if (module.getAccountId() != null) {
			BaseAccount baseAccount = cacheService.getAccountById(module.getAccountId());
			if (baseAccount != null) {
				result.setShowName(baseAccount.getShowValue());
				result.setSubjectName(baseAccount.getAccountor());
				result.setBankName(baseAccount.getBankName());
				result.setAccountNo(baseAccount.getAccountNo());
			}
		}
		if (module.getBillType() == BaseConsts.ONE) {
			InvoiceOverseasPoReqDto poReqDto = new InvoiceOverseasPoReqDto();
			poReqDto.setOverseasId(module.getId());
			List<InvoiceOverseasPoResDto> poList = overseasPoService.queryInvoiceCollectPoResults(poReqDto);
			BigDecimal sumNum = BigDecimal.ZERO;
			BigDecimal sumAmount = BigDecimal.ZERO;
			if (poList != null && poList.size() > BaseConsts.ZERO) {// 获取总数及总金额
				result.setRegPlace(poList.get(BaseConsts.ZERO).getRegPlace());
				for (int i = 0; i < poList.size(); i++) {
					sumNum = DecimalUtil.add(sumNum, poList.get(i).getInvoiceNum());
					sumAmount = DecimalUtil.add(sumAmount, poList.get(i).getInvoiceAmount());
				}
			}
			String billNo = "";
			List<InvoiceOverseasPoResDto> billList = overseasPoService.queryInvoiceCollectPoResultsByBill(poReqDto);// 获取销售单编号
			if (poList != null && billList.size() > BaseConsts.ZERO) {
				for (int i = 0; i < billList.size(); i++) {
					if (i == 0) {
						billNo = billNo + billList.get(i).getBillNo();
					} else {
						billNo = billNo + " | " + billList.get(i).getBillNo();
					}
				}
			}
			result.setBillNo(billNo);
			result.setSumNum(sumNum);
			result.setSumAmount(sumAmount);
		}
		result.setInvoiceAmount(module.getInvoiceAmount());
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
				InvoiceOverseasResDto.Operate.operMap);
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
		// 状态 1 待提交 2 已完成
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
	 * 文件相关操作
	 * 
	 * @param state
	 * @return
	 */
	public PageResult<InvoiceOverseasFileResDto> queryFileList(FileAttachSearchReqDto fileAttReqDto) {
		PageResult<InvoiceOverseasFileResDto> pageResult = new PageResult<InvoiceOverseasFileResDto>();
		fileAttReqDto.setBusType(BaseConsts.INT_26);
		List<FileAttach> fielAttach = fileUploadService.queryFileAttList(fileAttReqDto);
		List<InvoiceOverseasFileResDto> list = convertToFileResDto(fielAttach);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), fileAttReqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(fileAttReqDto.getPage());
		pageResult.setPer_page(fileAttReqDto.getPer_page());
		pageResult.setItems(list);
		return pageResult;
	}

	public List<InvoiceOverseasFileResDto> queryFileList(Integer collectId) {
		FileAttachSearchReqDto fileAttReqDto = new FileAttachSearchReqDto();
		fileAttReqDto.setBusId(collectId);
		fileAttReqDto.setBusType(BaseConsts.INT_26);
		List<FileAttach> fielAttach = fileUploadService.queryFileAttList(fileAttReqDto);
		List<InvoiceOverseasFileResDto> list = convertToFileResDto(fielAttach);
		return list;
	}

	private List<InvoiceOverseasFileResDto> convertToFileResDto(List<FileAttach> fileAttach) {
		List<InvoiceOverseasFileResDto> list = new LinkedList<InvoiceOverseasFileResDto>();
		if (ListUtil.isEmpty(fileAttach)) {
			return list;
		}
		for (FileAttach model : fileAttach) {
			InvoiceOverseasFileResDto result = new InvoiceOverseasFileResDto();
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
				InvoiceOverseasFileResDto.Operate.operMap);
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
		InvoiceOverseas invoiceOverseas = invoiceOverseasDao.queryEntityById(id);
		InvoiceOverseas Invoice = new InvoiceOverseas();
		Invoice.setId(id);
		Invoice.setPrintNum(invoiceOverseas.getPrintNum() + 1);
		invoiceOverseasDao.updateById(Invoice);
	}
}
