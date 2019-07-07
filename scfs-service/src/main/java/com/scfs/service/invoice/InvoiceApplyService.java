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

import com.beust.jcommander.internal.Lists;
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
import com.scfs.dao.base.entity.BaseInvoiceDao;
import com.scfs.dao.base.entity.BaseProjectDao;
import com.scfs.dao.fee.FeeDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.invoice.InvoiceApplyDao;
import com.scfs.dao.invoice.InvoiceFeeDao;
import com.scfs.dao.invoice.InvoiceSaleDao;
import com.scfs.dao.sale.BillDeliveryDtlDao;
import com.scfs.domain.BaseResult;
import com.scfs.domain.CodeValue;
import com.scfs.domain.audit.entity.AuditNode;
import com.scfs.domain.base.entity.BaseInvoice;
import com.scfs.domain.base.entity.BaseProject;
import com.scfs.domain.base.entity.BaseSubject;
import com.scfs.domain.common.dto.req.FileAttachSearchReqDto;
import com.scfs.domain.common.entity.AsyncExcel;
import com.scfs.domain.common.entity.FileAttach;
import com.scfs.domain.fee.entity.Fee;
import com.scfs.domain.invoice.dto.req.InvoiceApplyManagerReqDto;
import com.scfs.domain.invoice.dto.resp.InvoiceApplyFileResDto;
import com.scfs.domain.invoice.dto.resp.InvoiceApplyManagerResDto;
import com.scfs.domain.invoice.entity.InvoiceApplyManager;
import com.scfs.domain.invoice.entity.InvoiceFeeManager;
import com.scfs.domain.invoice.entity.InvoiceSaleManager;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.sale.entity.BillDeliveryDtl;
import com.scfs.service.audit.InvoiceAuditService;
import com.scfs.service.base.auditFlow.AuditFlowService;
import com.scfs.service.common.AsyncExcelService;
import com.scfs.service.common.SequenceService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.FileUploadService;
import com.scfs.service.support.ServiceSupport;

/**
 * 
 * @author Administrator
 *
 */

@Service
public class InvoiceApplyService {

	@Autowired
	private SequenceService sequenceService;
	@Autowired
	private InvoiceApplyDao invoiceApplyDao;
	@Autowired
	private InvoiceAuditService invoiceAuditService;
	@Autowired
	private InvoiceFeeDao invoiceFeeDao;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private BaseProjectDao baseProjectDao;
	@Autowired
	private InvoiceSaleDao invoiceSaleDao;
	@Autowired
	private FeeDao feeDao;
	@Autowired
	private BillDeliveryDtlDao billDeliveryDtlDao;
	@Autowired
	private FileUploadService fileUploadService;
	@Autowired
	private BaseInvoiceDao baseInvoiceDao;
	@Autowired
	private AsyncExcelService asyncExcelService;
	@Autowired
	private AuditFlowService auditFlowService;

	/**
	 * 查询发票信息
	 * 
	 * @param billDeliveryDtlSearchReqDto
	 * @return
	 * @throws Exception
	 */
	public PageResult<InvoiceApplyManagerResDto> queryInvoiceResultsByCon(
			InvoiceApplyManagerReqDto queryInvoiceReqDto) {

		PageResult<InvoiceApplyManagerResDto> pageResult = new PageResult<InvoiceApplyManagerResDto>();
		int offSet = PageUtil.getOffSet(queryInvoiceReqDto.getPage(), queryInvoiceReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, queryInvoiceReqDto.getPer_page());
		queryInvoiceReqDto.setUserId(ServiceSupport.getUser().getId());
		List<InvoiceApplyManager> result = invoiceApplyDao.queryInvoiceResultsByCon(queryInvoiceReqDto, rowBounds);
		List<InvoiceApplyManagerResDto> vos = new ArrayList<InvoiceApplyManagerResDto>();
		if (CollectionUtils.isNotEmpty(result)) {
			for (InvoiceApplyManager inv : result) {
				InvoiceApplyManagerResDto res = convertToResDto(inv);
				vos.add(res);
			}
			if (queryInvoiceReqDto.getNeedSum() != null && queryInvoiceReqDto.getNeedSum() == BaseConsts.ONE) {
				List<InvoiceApplyManager> sumList = invoiceApplyDao.querySumByCon(queryInvoiceReqDto);
				BigDecimal amountRmbTotal = BigDecimal.ZERO;
				for (InvoiceApplyManager invoice : sumList) {
					if (invoice.getBillType() == 1) {
						if (invoiceSaleDao.querySaleAmountByAppId(invoice.getId()) != null)
							amountRmbTotal = DecimalUtil.add(amountRmbTotal,
									invoiceSaleDao.querySaleAmountByAppId(invoice.getId()));
					} else {
						if (invoiceFeeDao.queryFeeAmountByAppId(invoice.getId()) != null)
							amountRmbTotal = DecimalUtil.add(amountRmbTotal,
									invoiceFeeDao.queryFeeAmountByAppId(invoice.getId()));
					}
				}
				pageResult.setTotalAmount(amountRmbTotal);
			}
		}
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), queryInvoiceReqDto.getPer_page());
		pageResult.setItems(vos);
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(queryInvoiceReqDto.getPage());
		pageResult.setPer_page(queryInvoiceReqDto.getPer_page());
		return pageResult;
	}

	public List<InvoiceApplyManagerResDto> queryInvoicePrintByCon(InvoiceApplyManagerReqDto queryInvoiceReqDto) {
		List<InvoiceApplyManager> result = invoiceApplyDao.queryInvoiceResultsByCon(queryInvoiceReqDto);
		List<InvoiceApplyManagerResDto> vos = new ArrayList<InvoiceApplyManagerResDto>();
		if (result != null) {
			for (InvoiceApplyManager inv : result) {
				InvoiceApplyManagerResDto res = convertToResDto(inv);
				vos.add(res);
			}
		}
		return vos;
	}

	private InvoiceApplyManagerResDto convertToResDto(InvoiceApplyManager invoice) {
		InvoiceApplyManagerResDto res = new InvoiceApplyManagerResDto();
		res.setId(invoice.getId());
		res.setApplyNo(invoice.getApplyNo());
		res.setProjectName(cacheService.getProjectNameById(invoice.getProjectId()));
		res.setCustomerName(cacheService.getSubjectNameByIdAndKey(invoice.getCustomerId(), CacheKeyConsts.BCS));
		res.setInvoiceTypeName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.INVOICE_TYPE, invoice.getInvoiceType() + ""));
		res.setBillTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.DOCUMENT_TYPE, invoice.getBillType() + ""));
		res.setFeeTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.BILL_DETAIL, invoice.getFeeType() + ""));
		res.setBusinessUnitName(
				cacheService.getSubjectNameByIdAndKey(invoice.getBusinessUnitId(), CacheKeyConsts.BUSI_UNIT));
		res.setStatus(invoice.getStatus());
		res.setStatusName(ServiceSupport.getValueByBizCode(BizCodeConsts.INVOICE_STATUS, invoice.getStatus() + ""));
		res.setInvoiceNo(invoice.getInvoiceNo());
		res.setInvoiceCode(invoice.getInvoiceCode());
		res.setBillType(invoice.getBillType());
		res.setCreateAt(invoice.getCreateAt());
		res.setCreator(invoice.getCreator());
		// 获取申请金额
		if (invoice.getBillType() == 1) {
			res.setApplyAmount(DecimalUtil.toAmountString(invoiceSaleDao.querySaleAmountByAppId(invoice.getId())));
		} else {
			res.setApplyAmount(DecimalUtil.toAmountString(invoiceFeeDao.queryFeeAmountByAppId(invoice.getId())));
		}
		// 获取操作权限
		res.setOpertaList(getOperList(invoice.getStatus()));

		return res;
	}

	/**
	 * 浏览单条信息
	 * 
	 * @param invoiceManager
	 * @return
	 */
	public InvoiceApplyManager detailInvoiceById(InvoiceApplyManager invoiceManager) {

		InvoiceApplyManager vo = invoiceApplyDao.queryEntityById(invoiceManager.getId());
		vo.setProjectName(cacheService.getProjectNameById(vo.getProjectId()));
		vo.setCustomerName(cacheService.getSubjectNameByIdAndKey(vo.getCustomerId(), CacheKeyConsts.CUSTOMER));
		vo.setCustomerNameAll(cacheService.getCustomerById(vo.getCustomerId()).getChineseName());
		vo.setInvoiceTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.INVOICE_TYPE, vo.getInvoiceType() + ""));
		vo.setBillTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.DOCUMENT_TYPE, vo.getBillType() + ""));
		vo.setFeeTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.BILL_DETAIL, vo.getFeeType() + ""));
		vo.setBusinessUnitName(cacheService.getSubjectNameByIdAndKey(vo.getBusinessUnitId(), CacheKeyConsts.BUSI_UNIT));
		vo.setSystemTime(new Date());
		BaseProject baseProject = cacheService.getProjectById(vo.getProjectId());
		if (null != baseProject) {
			BaseSubject busiUnit = cacheService.getBusiUnitById(baseProject.getBusinessUnitId());
			if (null != busiUnit) {
				vo.setBusinessUnitNameValue(busiUnit.getChineseName());
				vo.setBusinessUnitAddress(busiUnit.getOfficeAddress());
			}
		}
		if (vo.getBaseInvoiceId() != null) {
			BaseInvoice invoice = baseInvoiceDao.queryInvoiceById(vo.getBaseInvoiceId());
			vo.setPhoneNumber(invoice.getPhoneNumber());
			vo.setAccountNo(invoice.getAccountNo());
			vo.setBankName(invoice.getBankName());
			vo.setAddress(invoice.getAddress());
		}
		// 获取申请金额
		if (vo.getBillType() == 1) {
			vo.setApplyAmount(invoiceSaleDao.querySaleAmountByAppId(vo.getId()));
		} else {
			vo.setApplyAmount(invoiceFeeDao.queryFeeAmountByAppId(vo.getId()));
		}
		if (vo.getInvoiceTaxRate() != null) {
			BigDecimal fg = vo.getInvoiceTaxRate();
			String fg1 = fg.toString();
			vo.setInvoiceTaxRateValue(fg1);
		}
		if (vo.getInvoiceType() == 3) {
			vo.setIsElecInvoiceValue(
					ServiceSupport.getValueByBizCode(BizCodeConsts.PROMPT_STATUS, vo.getIsElecInvoice() + ""));
		}
		if (vo.getBillType() == 1) {
			vo.setIsGoodsMergeValue(
					ServiceSupport.getValueByBizCode(BizCodeConsts.PROMPT_STATUS, vo.getIsGoodsMerge() + ""));
			vo.setIsDisplayDiscountValue(
					ServiceSupport.getValueByBizCode(BizCodeConsts.PROMPT_STATUS, vo.getIsDisplayDiscount() + ""));
		}
		return vo;
	}

	/**
	 * 编辑信息
	 * 
	 * @param invoiceManager
	 * @return
	 */
	public InvoiceApplyManager editInvoiceById(InvoiceApplyManager invoiceManager) {
		InvoiceApplyManager result = invoiceApplyDao.queryEntityById(invoiceManager.getId());
		result.setProjectName(cacheService.getProjectNameById(result.getProjectId()));
		result.setCustomerName(cacheService.getSubjectNameByIdAndKey(result.getCustomerId(), CacheKeyConsts.CUSTOMER));
		if (result.getInvoiceTaxRate() != null) {
			BigDecimal fg = result.getInvoiceTaxRate();
			String fg1 = fg.toString();
			result.setInvoiceTaxRateValue(fg1);
		}
		return result;
	}

	/**
	 * 更新保存信息
	 * 
	 * @param invoice
	 * @return
	 */
	public int updateInvoiceById(InvoiceApplyManager invoice) {
		if (invoice.getInvoiceTaxRateValue() != null) {
			invoice.setInvoiceTaxRate(new BigDecimal(invoice.getInvoiceTaxRateValue()));
		}
		invoice.setApplyNo(
				sequenceService.getNumDateByBusName(BaseConsts.VT_NO_PREFIX, SeqConsts.INVOICE_NO, BaseConsts.INT_13));
		Integer id = invoiceApplyDao.updateById(invoice);
		if (id <= 0) {
			throw new BaseException(ExcMsgEnum.INVOICE_UPDATE_EXCEPTION);
		}
		return id;
	}

	/**
	 * 提交发票信息
	 * 
	 * @param invoice
	 * @return
	 */
	public BaseResult submitInvoiceInfo(Integer invoiceApplyId) {
		BaseResult result = new BaseResult();
		InvoiceApplyManager invoice = new InvoiceApplyManager();
		AuditNode startAuditNode = auditFlowService.getStartAuditNode(BaseConsts.TEN, null);
		if (null == startAuditNode) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请设置审核流");
		}

		invoice.setStatus(startAuditNode.getAuditNodeState());
		invoice.setId(invoiceApplyId);
		Integer id = invoiceApplyDao.updateStatus(invoice);
		if (id <= 0) {
			throw new BaseException(ExcMsgEnum.INVOICE_SUBMIT_EXCEPTION);
		} else {
			invoiceAuditService.startAudit(invoiceApplyId, startAuditNode);
		}
		return result;
	}

	/**
	 * 更新状态
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public void updateInvoiceApplyStatus(InvoiceApplyManager invoiceApplyManager) {
		InvoiceApplyManager vo = new InvoiceApplyManager();
		vo.setId(invoiceApplyManager.getId());
		vo.setStatus(invoiceApplyManager.getStatus());
		Integer status = invoiceApplyDao.updateStatus(invoiceApplyManager);
		if (status < 0) {
			throw new BaseException(ExcMsgEnum.INVOICE_SIMULATE_SUBMIT_EXCEPTION);
		}
	}

	/**
	 * 新增发票信息
	 * 
	 * @param invoice
	 * @return
	 */
	public int insertInvoice(InvoiceApplyManager invoice) {
		BaseProject project = baseProjectDao.queryEntityById(invoice.getProjectId());
		invoice.setBusinessUnitId(project.getBusinessUnitId());
		invoice.setApplyNo(
				sequenceService.getNumDateByBusName(BaseConsts.VT_NO_PREFIX, SeqConsts.INVOICE_NO, BaseConsts.INT_13));
		invoice.setCreator(ServiceSupport.getUser().getChineseName());
		invoice.setCreateAt(new Date());
		Integer id = invoiceApplyDao.insert(invoice);
		if (id <= 0) {
			throw new BaseException(ExcMsgEnum.INVOICE_ADD_EXCEPTION);
		}
		return invoice.getId();
	}

	/**
	 * 删除发票信息
	 * 
	 * @param projectPoolDtl
	 * @return
	 */
	public Integer deleteInvoice(InvoiceApplyManager invoice) {
		InvoiceApplyManager result = invoiceApplyDao.queryEntityById(invoice.getId());
		invoice.setDeleteAt(new Date());
		invoice.setIsDelete(BaseConsts.ONE);
		int result1 = invoiceApplyDao.updateById(invoice);
		if (result1 <= 0) {
			throw new BaseException(ExcMsgEnum.INVOICE_DELETE_EXCEPTION);
		} else {

			if (result.getBillType() == 1) {
				List<InvoiceSaleManager> vo = invoiceSaleDao.selectByInvoiceId(result.getId());
				// 删除销售单明细里面的信息
				for (int i = 0; i < vo.size(); i++) {
					BillDeliveryDtl dtl = new BillDeliveryDtl();
					BillDeliveryDtl dtl1 = billDeliveryDtlDao.queryAndLockEntityById(vo.get(i).getBillDtlId());
					dtl.setProvideInvoiceNum(dtl1.getProvideInvoiceNum().subtract(vo.get(i).getProvideInvoiceNum()));
					dtl.setProvideInvoiceAmount(
							dtl1.getProvideInvoiceAmount().subtract(vo.get(i).getProvideInvoiceAmount()));
					dtl.setId(dtl1.getId());
					billDeliveryDtlDao.updateById(dtl);
				}
				// 删除货物单据
				invoiceSaleDao.deleteByInvoiceAppId(invoice.getId());
			} else {
				InvoiceFeeManager inv = new InvoiceFeeManager();
				inv.setInvoiceApplyId(result.getId());
				List<InvoiceFeeManager> vo = invoiceFeeDao.selectByInvoiceId(invoice.getId());
				// 更新费用表里面的数据
				for (int i = 0; i < vo.size(); i++) {
					Fee feeEntity = new Fee();
					Fee fee = feeDao.queryEntityById(vo.get(i).getFeeId());
					feeEntity.setId(fee.getId());
					if (vo.get(i).getProvideInvoiceAmount() != null) {
						feeEntity.setProvideInvoiceAmount(
								fee.getProvideInvoiceAmount().subtract(vo.get(i).getProvideInvoiceAmount()));
					}
					feeDao.updateById(feeEntity);
				}
				// 删除费用单据
				invoiceFeeDao.deleteByCon(inv);

			}
		}
		return result1;
	}

	/**
	 * 根据状态获取操作列表
	 * 
	 * @param state
	 * @return
	 */
	private List<String> getOperListByState(Integer state) {
		if (state == null) {
			return null;
		}
		List<String> opertaList = Lists.newArrayList(5);
		opertaList.add(OperateConsts.DETAIL);
		switch (state) {
		// 状态, 1-待提交 2-待业务审核 25-待财务审核 4-待发货 5-已发货
		case BaseConsts.ONE:
			opertaList.add(OperateConsts.EDIT);
			opertaList.add(OperateConsts.DELETE);
			break;
		case BaseConsts.TWO:
			opertaList.add(OperateConsts.SUBMIT);
			break;
		case BaseConsts.INT_25:
			opertaList.add(OperateConsts.PRINT);
			break;
		case BaseConsts.FOUR:
			opertaList.add(OperateConsts.PRINT);
			break;
		case BaseConsts.FIVE:
			opertaList.add(OperateConsts.PRINT);
			break;
		}
		return opertaList;
	}

	private List<CodeValue> getOperList(Integer state) {
		// 1.根据状态得到操作列表
		List<String> operNameList = getOperListByState(state);
		// 2.将上述的操作列表通过权限过滤
		List<CodeValue> oprResult = ServiceSupport.getOperListByPermission(operNameList,
				InvoiceApplyManagerResDto.Operate.operMap);
		return oprResult;
	}

	/**
	 * 获取文件操作列表
	 * 
	 * @param state
	 * @return
	 */
	public PageResult<InvoiceApplyFileResDto> queryFileList(FileAttachSearchReqDto fileAttReqDto) {
		PageResult<InvoiceApplyFileResDto> pageResult = new PageResult<InvoiceApplyFileResDto>();
		fileAttReqDto.setBusType(BaseConsts.SIX);
		List<FileAttach> fielAttach = fileUploadService.queryFileAttList(fileAttReqDto);
		List<InvoiceApplyFileResDto> list = convertToFileResDto(fielAttach);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), fileAttReqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(fileAttReqDto.getPage());
		pageResult.setPer_page(fileAttReqDto.getPer_page());
		pageResult.setItems(list);
		return pageResult;
	}

	private List<InvoiceApplyFileResDto> convertToFileResDto(List<FileAttach> fileAttach) {
		List<InvoiceApplyFileResDto> list = new LinkedList<InvoiceApplyFileResDto>();
		if (ListUtil.isEmpty(fileAttach)) {
			return list;
		}
		for (FileAttach model : fileAttach) {
			InvoiceApplyFileResDto result = new InvoiceApplyFileResDto();
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
				InvoiceApplyFileResDto.Operate.operMap);
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
		InvoiceApplyManager invoiceApplyManager = invoiceApplyDao.queryEntityById(id);
		InvoiceApplyManager InvoiceApply = new InvoiceApplyManager();
		InvoiceApply.setId(id);
		InvoiceApply.setPrintNum(invoiceApplyManager.getPrintNum() + 1);
		invoiceApplyDao.updatePrintNum(InvoiceApply);
	}

	public boolean isOverasyncMaxLine(InvoiceApplyManagerReqDto queryInvoiceReqDto) {
		queryInvoiceReqDto.setUserId(ServiceSupport.getUser().getId());
		int count = invoiceApplyDao.isOverasyncMaxLine(queryInvoiceReqDto);
		if (count >= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			// 后台导出
			AsyncExcel asyncExcel = new AsyncExcel();
			asyncExcel.setName("发票申请单据导出");
			asyncExcel.setClassName(this.getClass().getName());// 导出类名
			asyncExcel.setMethodName("asyncInvoiceApplyExport");// 导出方法
			asyncExcel.setTemplatePath("/WEB-INF/excel/export/invoice/makeinvoice/makeinvoice_apply_dtl_list.xls");// 导出模板路径
			asyncExcel.setPoType(BaseConsts.SIX);
			asyncExcelService.addAsyncExcel(queryInvoiceReqDto, asyncExcel);
			return true;
		}
		return false;
	}

	public Map<String, Object> asyncInvoiceApplyExport(InvoiceApplyManagerReqDto queryInvoiceReqDto) {
		Map<String, Object> model = Maps.newHashMap();
		List<InvoiceApplyManagerResDto> vos = new ArrayList<InvoiceApplyManagerResDto>();
		List<InvoiceApplyManager> result = invoiceApplyDao.queryInvoiceResultsByCon(queryInvoiceReqDto);
		for (InvoiceApplyManager inv : result) {
			InvoiceApplyManagerResDto res = convertToResDto(inv);
			vos.add(res);
		}
		model.put("invoiceApplyList", vos);
		return model;
	}
}
