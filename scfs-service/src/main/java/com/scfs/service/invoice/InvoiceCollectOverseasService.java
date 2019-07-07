package com.scfs.service.invoice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.testng.collections.Lists;

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
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.invoice.InvoiceCollectOverseasDao;
import com.scfs.dao.invoice.InvoiceCollectOverseasFeeDao;
import com.scfs.dao.invoice.InvoiceCollectOverseasPoDao;
import com.scfs.domain.BaseResult;
import com.scfs.domain.CodeValue;
import com.scfs.domain.audit.entity.AuditNode;
import com.scfs.domain.base.entity.BaseProject;
import com.scfs.domain.common.entity.AsyncExcel;
import com.scfs.domain.invoice.dto.req.InvoiceCollectOverseasReqDto;
import com.scfs.domain.invoice.dto.resp.InvoiceCollectOverseasResDto;
import com.scfs.domain.invoice.entity.InvoiceCollectOverseas;
import com.scfs.domain.invoice.entity.InvoiceCollectOverseasFee;
import com.scfs.domain.invoice.entity.InvoiceCollectOverseasPo;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.audit.invoiceCollectOverseasAuditService;
import com.scfs.service.base.auditFlow.AuditFlowService;
import com.scfs.service.common.AsyncExcelService;
import com.scfs.service.common.SequenceService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ExcelService;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 * 境外收票的相关业务
 *  File: InvoiceCollectOverseasService.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年07月24日		Administrator
 *
 * </pre>
 */
@Service
public class InvoiceCollectOverseasService {

	@Autowired
	private InvoiceCollectOverseasDao invoiceCollectOverseasDao;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private SequenceService sequenceService;
	@Autowired
	private InvoiceCollectOverseasPoDao invoiceCollectOverseasPoDao;
	@Autowired
	private InvoiceCollectOverseasFeeDao invoiceCollectOverseasFeeDao;
	@Autowired
	private InvoiceCollectOverseasPoService invoiceCollectOverseasPoService;
	@Autowired
	private InvoiceCollectOverseasFeeService invoiceCollectOverseasFeeService;
	@Autowired
	private invoiceCollectOverseasAuditService invoiceCollectOverseasAuditService;
	@Autowired
	private AsyncExcelService asyncExcelService;
	@Autowired
	private AuditFlowService auditFlowService;

	/**
	 * 查询境外收票的信息列表
	 * 
	 * @param reqDto
	 * @return
	 */
	public PageResult<InvoiceCollectOverseasResDto> queryInvoiceOverseasResultByCon(
			InvoiceCollectOverseasReqDto reqDto) {
		PageResult<InvoiceCollectOverseasResDto> result = new PageResult<InvoiceCollectOverseasResDto>();
		int offSet = PageUtil.getOffSet(reqDto.getPage(), reqDto.getPer_page());
		reqDto.setUserId(ServiceSupport.getUser().getId());
		RowBounds rowBounds = new RowBounds(offSet, reqDto.getPer_page());
		List<InvoiceCollectOverseasResDto> overseasResDtos = new ArrayList<InvoiceCollectOverseasResDto>();
		List<InvoiceCollectOverseas> collectOverseasList = invoiceCollectOverseasDao.queryInvoiceOverseasList(reqDto,
				rowBounds);
		if (reqDto.getNeedSum() != null && reqDto.getNeedSum() == BaseConsts.ONE) {// 1
																					// 表示需要合计2表示不需要
			List<InvoiceCollectOverseas> collectOverseas = invoiceCollectOverseasDao.sumInvoiceCollectOversrea(reqDto);
			if (!CollectionUtils.isEmpty(collectOverseas)) {
				BigDecimal countInvoiceAmount = BigDecimal.ZERO;
				for (InvoiceCollectOverseas invoiceCollectOverseas : collectOverseas) {
					BigDecimal rmbAmount = ServiceSupport.amountNewToRMB(invoiceCollectOverseas.getInvoiceAmount(),
							invoiceCollectOverseas.getCurrnecyType(), null);
					countInvoiceAmount = DecimalUtil.add(countInvoiceAmount, rmbAmount);
				}
				result.setTotalStr("合计：发票总金额：" + DecimalUtil.toAmountString(countInvoiceAmount) + " CNY &nbsp;");
				result.setTotalAmount(countInvoiceAmount);
				result.setExRateTotalAmount(countInvoiceAmount);
			}
		}
		overseasResDtos = convertToResult(collectOverseasList);
		result.setItems(overseasResDtos);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), reqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(reqDto.getPage());
		result.setPer_page(reqDto.getPer_page());
		return result;
	}

	/**
	 * 创建境外收票的数据
	 * 
	 * @param reqDto
	 * @return
	 */
	public int createInvoiceCollectOver(InvoiceCollectOverseasReqDto reqDto) {
		this.checkInvoiceCollect(reqDto);
		InvoiceCollectOverseas collectOverseas = new InvoiceCollectOverseas();
		collectOverseas.setState(BaseConsts.ONE);// 待提交状态
		collectOverseas.setApplyNo(sequenceService.getNumDateByBusName(BaseConsts.COLLECT_APPLY_NO,
				SeqConsts.S_COLLECT_APPLY_NO, BaseConsts.INT_13));// 申请编号
		BaseProject baseProject = cacheService.getProjectById(reqDto.getProjectId());// 项目
		if (baseProject == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "该项目对于的经营单位为空");
		}
		collectOverseas.setBusinessUnit(baseProject.getBusinessUnitId());
		// 封装新增的对象
		collectOverseas = createToInvocieCollect(reqDto, collectOverseas);
		collectOverseas.setIsDelete(BaseConsts.ZERO);
		collectOverseas.setCreateAt(new Date());
		collectOverseas.setCreator(ServiceSupport.getUser() == null ? null : ServiceSupport.getUser().getChineseName());
		collectOverseas.setCreatorId(ServiceSupport.getUser() == null ? null : ServiceSupport.getUser().getId());
		this.updateInvoiceCollectOverseasById(collectOverseas);
		return collectOverseas.getId();
	}

	/**
	 * 浏览境外开票业务的数据详情
	 * 
	 * @param id
	 * @return
	 */
	public Result<InvoiceCollectOverseasResDto> queryInvoiceCollectOverById(Integer id) {
		Result<InvoiceCollectOverseasResDto> result = new Result<InvoiceCollectOverseasResDto>();
		if (id == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "境外开票浏览数据传输有误");
		}
		InvoiceCollectOverseas collectOverseas = this.queryInvoiceCollectOverEntityById(id);
		InvoiceCollectOverseasResDto collectOverseasResDto = invoiceCollectOverseasToRes(collectOverseas);
		result.setItems(collectOverseasResDto);
		return result;
	}

	/**
	 * 封装当期Invoice收票更新的信息
	 * 
	 * @param reqDto
	 */
	public void updateInvoiceCollectOverById(InvoiceCollectOverseasReqDto reqDto) {
		if (reqDto == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "数据传输为空");
		}
		InvoiceCollectOverseas collectOverseas = this.queryInvoiceCollectOverEntityById(reqDto.getId());
		if (collectOverseas == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "该Invoice收票数据不存在");
		}
		collectOverseas.setInvoiceNo(reqDto.getInvoiceNo());// 发票号
		collectOverseas.setInvoiceDate(reqDto.getInvoiceDate());
		collectOverseas.setRemark(reqDto.getRemark());// 单据备注
		collectOverseas.setInvoiceRemark(reqDto.getInvoiceRemark());// 票据备注
		this.updateInvoiceCollectOverseasById(collectOverseas);
	}

	/**
	 * 更新收票信息
	 * 
	 * @param invoiceCollect
	 * @return
	 */
	public void updateInvoiceCollectOverseasById(InvoiceCollectOverseas collectOverseas) {
		int result = BaseConsts.ONE;
		if (collectOverseas.getId() != null) {
			result = invoiceCollectOverseasDao.updateById(collectOverseas);
		} else {
			result = invoiceCollectOverseasDao.insert(collectOverseas);
		}
		if (result == BaseConsts.ZERO) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "更新失败:" + JSONObject.toJSON(collectOverseas));
		}
	}

	/**
	 * 业务逻辑删除境外开票
	 * 
	 * @param id
	 */
	public void deleteInvoiceCollectOver(Integer id) {
		if (id == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "Invoice收票删除数据传输有误");
		}
		InvoiceCollectOverseas collectOverseas = this.queryInvoiceCollectOverEntityById(id);
		if (collectOverseas == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "Invoice收票数据为空");
		}
		if (collectOverseas.getBillType() == BaseConsts.ONE) {// 货物类型
			List<InvoiceCollectOverseasPo> collectOverseasPos = invoiceCollectOverseasPoDao
					.queryOverseasIdResult(collectOverseas.getId());
			if (!CollectionUtils.isEmpty(collectOverseasPos)) {
				for (InvoiceCollectOverseasPo invoiceCollectOverseasPo : collectOverseasPos) {
					invoiceCollectOverseasPoService.deleteInvoiceCollectPo(invoiceCollectOverseasPo.getId(), false);
				}
			}
		} else if (collectOverseas.getBillType() == BaseConsts.TWO) {// 费用类型
			List<InvoiceCollectOverseasFee> collectOverseasFees = invoiceCollectOverseasFeeDao
					.queryOverseasIdResult(collectOverseas.getId());
			if (!CollectionUtils.isEmpty(collectOverseasFees)) {
				for (InvoiceCollectOverseasFee invoiceCollectOverseasFee : collectOverseasFees) {
					invoiceCollectOverseasFeeService.deleteInvoiceFee(invoiceCollectOverseasFee.getId(), false);
				}
			}
		}
		collectOverseas.setIsDelete(BaseConsts.ONE);
		this.updateInvoiceCollectOverseasById(collectOverseas);
	}

	/**
	 * invoice收票数据的进行认证操作
	 * 
	 * @param reqDto
	 */
	public void approveInvoiceCollect(InvoiceCollectOverseasReqDto reqDto) {
		InvoiceCollectOverseas collectOverseas = queryInvoiceCollectOverEntityById(reqDto.getId());
		if (collectOverseas.getState() != BaseConsts.THREE) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "Invoice收票数据状态有误");
		}
		InvoiceCollectOverseas overseas = new InvoiceCollectOverseas();
		overseas.setId(collectOverseas.getId());
		overseas.setApproveRemark(reqDto.getApproveRemark());
		overseas.setApproveDate(reqDto.getApproveDate());
		overseas.setState(BaseConsts.FOUR);
		this.updateInvoiceCollectOverseasById(overseas);
	}

	/**
	 * 根据invoice收票的Id查询数据
	 * 
	 * @param id
	 * @return
	 */
	public InvoiceCollectOverseas queryInvoiceCollectOverEntityById(Integer id) {
		InvoiceCollectOverseas collectOverseas = invoiceCollectOverseasDao.queryEntityById(id);
		return collectOverseas;
	}

	/**
	 * 封装单笔Invoice收票数据
	 * 
	 * @param id
	 * @return
	 */
	public InvoiceCollectOverseasResDto queryInvoiceCollectOverEntityResDto(Integer id) {
		return invoiceCollectOverseasToRes(invoiceCollectOverseasDao.queryEntityById(id));
	}

	/**
	 * 封装新增实体对象
	 * 
	 * @param reqDto
	 * @param collectOverseas
	 * @return
	 */
	public InvoiceCollectOverseas createToInvocieCollect(InvoiceCollectOverseasReqDto reqDto,
			InvoiceCollectOverseas collectOverseas) {
		collectOverseas.setProjectId(reqDto.getProjectId());// 项目ID
		collectOverseas.setSupplierId(reqDto.getSupplierId());// 供应商
		collectOverseas.setBillType(reqDto.getBillType());// 单据类型(1-货物 2-费用)
		if (reqDto.getBillType().equals(BaseConsts.TWO)) {
			collectOverseas.setInvoiceNo(reqDto.getInvoiceNo());
			collectOverseas.setInvoiceDate(reqDto.getInvoiceDate());
		}
		collectOverseas.setAccountId(reqDto.getAccountId());// 收款账号ID
		collectOverseas.setInvoiceAmount(BigDecimal.ZERO);// 发票金额
		collectOverseas.setRemark(reqDto.getRemark());// 单据备注
		collectOverseas.setInvoiceRemark(reqDto.getInvoiceRemark());// 单据备注
		return collectOverseas;
	}

	/**
	 * 新增境外开票的时候数据校验
	 * 
	 * @param reqDto
	 */
	private void checkInvoiceCollect(InvoiceCollectOverseasReqDto reqDto) {
		if (reqDto == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "数据传输为空");
		}
		if (reqDto.getProjectId() == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "新增境外收票项目为空");
		}
		if (reqDto.getSupplierId() == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "新增境外收票供应商为空");
		}
		if (reqDto.getAccountId() == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "新增境外收票收款账号为空");
		}
		if (reqDto.getBillType() == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "新增境外收票单据类型为空");
		}
		if (reqDto.getBillType().equals(BaseConsts.TWO)) {
			if (StringUtils.isEmpty(reqDto.getInvoiceNo())) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "新增境外收票发票号为空");
			}
			if (reqDto.getInvoiceDate() == null) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "新增境外收票发票日期为空");
			}
		}
	}

	/**
	 * 将境外收票对象进行封装成返回对象
	 * 
	 * @param collectOverseasList
	 * @return
	 */
	private List<InvoiceCollectOverseasResDto> convertToResult(List<InvoiceCollectOverseas> collectOverseasList) {
		List<InvoiceCollectOverseasResDto> resDtos = Lists.newArrayList();
		if (CollectionUtils.isEmpty(collectOverseasList)) {
			return resDtos;
		}
		for (InvoiceCollectOverseas overseas : collectOverseasList) {
			InvoiceCollectOverseasResDto overseasResDto = invoiceCollectOverseasToRes(overseas);
			// 操作集合
			List<CodeValue> operList = getOperList(overseasResDto.getState());
			overseasResDto.setOpertaList(operList);
			resDtos.add(overseasResDto);
		}
		return resDtos;

	}

	private List<CodeValue> getOperList(Integer state) {
		// 1.根据状态得到操作列表
		List<String> operNameList = getOperListByState(state);
		// 2.将上述的操作列表通过权限过滤
		List<CodeValue> oprResult = ServiceSupport.getOperListByPermission(operNameList,
				InvoiceCollectOverseasResDto.Operate.operMap);
		return oprResult;
	}

	/**
	 * 根据状态得到操作列表
	 *
	 * @param state
	 * @return
	 */
	private List<String> getOperListByState(Integer state) {
		if (state == null) {
			return null;
		}
		List<String> opertaList = Lists.newArrayList();
		// 状态过滤
		switch (state) {
		// 状态,1表示待提交 操作浏览、编辑、提交、删除。
		case BaseConsts.ONE:
			opertaList.add(OperateConsts.EDIT);
			opertaList.add(OperateConsts.DETAIL);
			opertaList.add(OperateConsts.SUBMIT);
			opertaList.add(OperateConsts.DELETE);
			break;
		// 状态2 浏览操作
		case BaseConsts.INT_25:
			opertaList.add(OperateConsts.DETAIL);
			break;
		case BaseConsts.THREE:
			opertaList.add(OperateConsts.DETAIL);
			opertaList.add(OperateConsts.APPROVE);
			break;
		case BaseConsts.FOUR:
			opertaList.add(OperateConsts.DETAIL);
			break;
		}
		return opertaList;
	}

	/**
	 * 返回对象的封装
	 * 
	 * @param overseas
	 * @return
	 */
	public InvoiceCollectOverseasResDto invoiceCollectOverseasToRes(InvoiceCollectOverseas overseas) {
		if (overseas == null) {
			return null;
		}
		InvoiceCollectOverseasResDto overseasResDto = new InvoiceCollectOverseasResDto();
		overseasResDto.setId(overseas.getId());// 收票id
		overseasResDto.setApplyNo(overseas.getApplyNo());// 申请编号
		overseasResDto.setBusinessUnit(overseas.getBusinessUnit());// 经营单位
		overseasResDto.setBusinessUnitName(
				cacheService.showSubjectNameByIdAndKey(overseas.getBusinessUnit(), CacheKeyConsts.BUSI_UNIT));// 经营单位名称
		overseasResDto.setProjectId(overseas.getProjectId());// 项目ID
		overseasResDto.setProjectName(cacheService.showProjectNameById(overseas.getProjectId()));// 项目名称
		overseasResDto.setSupplierId(overseas.getSupplierId());// 供应商ID
		overseasResDto.setSupplierName(
				cacheService.showSubjectNameByIdAndKey(overseas.getSupplierId(), CacheKeyConsts.SUPPLIER));// 供应商名称
		if (overseas.getBillType().equals(BaseConsts.ONE)) {
			overseasResDto.setInvoiceNo(overseas.getOrderNo());// 发票号
		} else {
			overseasResDto.setInvoiceNo(overseas.getInvoiceNo());// 发票号
		}
		overseasResDto.setBillType(overseas.getBillType());// 单据类型
		overseasResDto.setBillTypeName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.DOCUMENT_TYPE, overseas.getBillType() + ""));// 票据类型
		overseasResDto.setAccountId(overseas.getAccountId());// 收款账号
		overseasResDto.setInvoiceAmount(DecimalUtil
				.formatScale2(overseas.getInvoiceAmount() == null ? BigDecimal.ZERO : overseas.getInvoiceAmount()));// 发票金额
		overseasResDto.setCurrnecyType(overseas.getCurrnecyType());// 币种
		overseasResDto.setCurrnecyTypeName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE, overseas.getCurrnecyType() + ""));// 币种名称
		overseasResDto.setInvoiceRemark(overseas.getInvoiceRemark());// 票据备注
		overseasResDto.setInvoiceDate(overseas.getInvoiceDate());
		overseasResDto.setState(overseas.getState());// 状态
		overseasResDto.setStateName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.CONLLECT_OVERSEAS_STA, overseas.getState() + ""));// 审核节点名称
		overseasResDto.setRemark(overseas.getRemark());// 备注
		overseasResDto.setApproveDate(overseas.getApproveDate());// 认证时间
		overseasResDto.setApproveRemark(overseas.getApproveRemark());
		overseasResDto.setCreateAt(overseas.getCreateAt());// 创建时间
		overseasResDto.setCreator(overseas.getCreator());// 创建人
		return overseasResDto;
	}

	/**
	 * invoice收票的提交
	 * 
	 * @return
	 */
	public BaseResult submit(InvoiceCollectOverseas overseas) {
		BaseResult baseResult = new BaseResult();
		overseas = this.queryInvoiceCollectOverEntityById(overseas.getId());

		AuditNode startAuditNode = auditFlowService.getStartAuditNode(BaseConsts.INT_26, null);
		if (null == startAuditNode) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请设置审核流");
		}
		overseas.setState(startAuditNode.getAuditNodeState()); // 待财务专员审核
		this.updateInvoiceCollectOverseasById(overseas);

		invoiceCollectOverseasAuditService.startAudit(overseas, startAuditNode);
		return baseResult;
	}

	/**
	 * 导出信息
	 * 
	 * @param searchreqDto
	 * @return
	 */
	public PageResult<InvoiceCollectOverseasResDto> queryInvoiceCollectResultsByEx(
			InvoiceCollectOverseasReqDto searchreqDto) {
		PageResult<InvoiceCollectOverseasResDto> result = new PageResult<InvoiceCollectOverseasResDto>();
		if (searchreqDto.getUserId() == null) {
			searchreqDto.setUserId(ServiceSupport.getUser().getId());
		}
		List<InvoiceCollectOverseasResDto> invoiceCollectResDto = convertToResult(
				invoiceCollectOverseasDao.queryInvoiceOverseasList(searchreqDto));
		result.setItems(invoiceCollectResDto);
		return result;
	}

	/**
	 * 判断是否超出导出行数
	 * 
	 * @param searchreqDto
	 * @return
	 */
	public boolean isOverasyncMaxLine(InvoiceCollectOverseasReqDto searchreqDto) {
		searchreqDto.setUserId(ServiceSupport.getUser().getId());
		int count = invoiceCollectOverseasDao.isOverasyncMaxLine(searchreqDto);
		if (count >= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			// 后台导出
			AsyncExcel asyncExcel = new AsyncExcel();
			asyncExcel.setName("收票单据导出");
			asyncExcel.setClassName(this.getClass().getName());// 导出类名
			asyncExcel.setMethodName("asyncInvoiceApplyExport");// 导出方法
			asyncExcel.setTemplatePath("/WEB-INF/excel/export/invoice/overseas/invoicecollectoverseas_list");// 导出模板路径
			asyncExcel.setPoType(BaseConsts.INT_38);
			asyncExcelService.addAsyncExcel(searchreqDto, asyncExcel);
			return true;
		}
		return false;
	}

	public Map<String, Object> asyncInvoiceApplyExport(InvoiceCollectOverseasReqDto searchreqDto) {
		Map<String, Object> model = Maps.newHashMap();
		List<InvoiceCollectOverseasResDto> invoiceCollectList = queryInvoiceCollectResultsByEx(searchreqDto).getItems();
		model.put("invoiceCollectList", invoiceCollectList);
		return model;
	}

	/**
	 * 收票票确认导入Excel 业务逻辑处理
	 *
	 * @param importFile
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void importExcel(MultipartFile importFile) {
		List<InvoiceCollectOverseasResDto> collectList = Lists.newArrayList();
		Map beans = Maps.newHashMap();
		beans.put("collectApproveList", collectList);
		ExcelService.resolverExcel(importFile, "/excel/invoice/invoiceCollect_approve.xml", beans);
		// 业务逻辑处理
		collectList = (List<InvoiceCollectOverseasResDto>) beans.get("collectApproveList");
		if (CollectionUtils.isNotEmpty(collectList)) {
			if (collectList.size() > BaseConsts.IMPORT_EXCEL_ROWS_MAX) {
				throw new BaseException(ExcMsgEnum.IMPORT_EXCEL_ROWS_MAX);
			}
			boolean result = true;
			for (InvoiceCollectOverseasResDto collect : collectList) {// 校验信息是否正确
				result = validateColletInfo(collect);
			}
			if (result) {
				for (InvoiceCollectOverseasResDto collect : collectList) {
					InvoiceCollectOverseas collectInfo = new InvoiceCollectOverseas();
					collectInfo.setApplyNo(collect.getApplyNo());
					InvoiceCollectOverseas invoiceCollect = invoiceCollectOverseasDao
							.queryInvoiceByApplyNo(collectInfo);
					collectInfo.setApproveDate(collect.getApproveDate());
					collectInfo.setApproveRemark(collect.getApproveRemark());
					collectInfo.setId(invoiceCollect.getId());
					collectInfo.setState(BaseConsts.FOUR);
					updateInvoiceCollectOverseasById(collectInfo);
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
	private boolean validateColletInfo(InvoiceCollectOverseasResDto collect) {
		boolean result = true;
		String number = collect.getApplyNo();
		if (number == null || number.equals("")) {
			result = false;
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "申请编号不能为空");
		}
		if (collect.getApproveDate() == null) {
			result = false;
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "认证日期不能为空");
		}
		if (collect.getApproveRemark() == null || collect.getApproveRemark().equals("")) {
			result = false;
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "认证备注不能为空");
		} else {
			InvoiceCollectOverseas collectInfo = new InvoiceCollectOverseas();
			collectInfo.setApplyNo(number);
			InvoiceCollectOverseas invoiceCollect = invoiceCollectOverseasDao.queryInvoiceByApplyNo(collectInfo);
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
}
