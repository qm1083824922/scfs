package com.scfs.service.fee;

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
import com.google.common.collect.Lists;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.consts.CacheKeyConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.OperateConsts;
import com.scfs.common.consts.SeqConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.Check;
import com.scfs.common.utils.Coder;
import com.scfs.common.utils.DateFormatUtils;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.ListUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.base.entity.BaseProjectDao;
import com.scfs.dao.fee.FeeManageResDao;
import com.scfs.dao.fee.FeeShareDao;
import com.scfs.dao.fee.FeeSpecDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.report.ProfitReportMonthDao;
import com.scfs.domain.BaseResult;
import com.scfs.domain.CodeValue;
import com.scfs.domain.base.entity.BaseRole;
import com.scfs.domain.base.entity.BaseUserProject;
import com.scfs.domain.common.dto.req.FileAttachSearchReqDto;
import com.scfs.domain.common.entity.AsyncExcel;
import com.scfs.domain.common.entity.FileAttach;
import com.scfs.domain.fee.dto.req.FeeManageSearchReqDto;
import com.scfs.domain.fee.dto.req.FeeShareReqDto;
import com.scfs.domain.fee.dto.resp.FeeManageResDto;
import com.scfs.domain.fee.entity.FeeManage;
import com.scfs.domain.fee.entity.FeeShare;
import com.scfs.domain.fee.entity.FeeSpec;
import com.scfs.domain.invoice.dto.resp.FeeManageFileResDto;
import com.scfs.domain.report.entity.MounthProfitReport;
import com.scfs.domain.report.req.ProfitReportReqMonthDto;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.common.AsyncExcelService;
import com.scfs.service.common.CommonParamValidate;
import com.scfs.service.common.SequenceService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ExcelService;
import com.scfs.service.support.FileUploadService;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 * 	管理费用和人工费用Service
 *  File: FeeManageService.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年04月12日				Administrator
 *
 * </pre>
 */
@Service
public class FeeManageService {
	@Autowired
	private FeeManageResDao feeManageResDao;
	@Autowired
	private FeeSpecDao feeSpecDao;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private AsyncExcelService asyncExcelService;
	@Autowired
	CommonParamValidate commonParamValidate;
	@Autowired
	SequenceService sequenceService;
	@Autowired
	private FileUploadService fileUploadService;
	@Autowired
	private FeeShareDao feeShareDao;
	@Autowired
	private ProfitReportMonthDao profitReportMonthDao;
	@Autowired
	private BaseProjectDao baseProjectDao;

	/**
	 * 添加管理费用信息
	 * 
	 * @param feeManage
	 * @return
	 */
	public int createFeeManage(FeeManage feeManage) {
		Date date = new Date();
		feeManage.setFeeManageNo(
				sequenceService.getNumDateByBusName(BaseConsts.FE_NO_PREFIX, SeqConsts.S_FEE_NO, BaseConsts.INT_13));
		feeManage.setState(BaseConsts.ONE);
		feeManage.setCreateAt(date);
		feeManage.setCreator(ServiceSupport.getUser() == null ? null : ServiceSupport.getUser().getChineseName());
		feeManage.setCreatorId(ServiceSupport.getUser() == null ? null : ServiceSupport.getUser().getId());
		try {
			byte[] inputData = feeManage.getAmount().getBytes();
			feeManage.setAmount(Coder.encryptBASE64(inputData));
			feeManage.setShareAmount(Coder.encryptBASE64("0.00".getBytes()));// 添加铺货金额
		} catch (Exception e) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "添加失败:" + JSONObject.toJSON(feeManage));
		}
		int id = feeManageResDao.insert(feeManage);
		if (id <= 0) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "添加失败:" + JSONObject.toJSON(feeManage));
		}
		return feeManage.getId();
	}

	/**
	 * 更新管理费用信息
	 * 
	 * @param feeManage
	 * @return
	 */
	public BaseResult updateFeeManageById(FeeManage feeManage) {
		BaseResult baseResult = new BaseResult();
		if (feeManage.getAmount() != null && !"".equals(feeManage.getAmount())) {
			try {
				byte[] inputData = feeManage.getAmount().getBytes();
				feeManage.setAmount(Coder.encryptBASE64(inputData));
			} catch (Exception e) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "更新失败:" + JSONObject.toJSON(feeManage));
			}
		}
		if (feeManage.getShareAmount() != null && !"".equals(feeManage.getShareAmount())) {
			try {
				byte[] inputData = feeManage.getShareAmount().getBytes();
				feeManage.setShareAmount(Coder.encryptBASE64(inputData));
			} catch (Exception e) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "更新失败:" + JSONObject.toJSON(feeManage));
			}
		}
		int result = feeManageResDao.update(feeManage);
		if (result == BaseConsts.ZERO) {
			baseResult.setSuccess(false);
			baseResult.setMsg("更新管理费用信息失败，请重试");
		}
		return baseResult;
	}

	/**
	 * 编辑
	 * 
	 * @param invoiceCollect
	 * @return
	 */
	public Result<FeeManageResDto> editFeeManageById(FeeManage feeManage) {
		Result<FeeManageResDto> result = new Result<FeeManageResDto>();
		FeeManageResDto resDto = convertToFeeManageResDto(feeManageResDao.queryEntityById(feeManage.getId()));
		result.setItems(resDto);
		return result;
	}

	public FeeManageResDto queryFeeManageById(Integer id) {
		FeeManageResDto resDto = convertToFeeManageResDto(feeManageResDao.queryEntityById(id));
		return resDto;
	}

	/**
	 * 提交信息
	 * 
	 * @param feeManage
	 * @return
	 */
	public BaseResult sumitFeeManageById(FeeManage feeManage) {
		BaseResult baseResult = new BaseResult();
		FeeManage result = feeManageResDao.queryEntityById(feeManage.getId());
		if (result.getState().equals(BaseConsts.ONE)) {
			feeManage.setState(BaseConsts.TWO);
			feeManageResDao.updateById(feeManage);
		}
		return baseResult;
	}

	/**
	 * 批量提交
	 * 
	 * @param feeManageReq
	 * @return
	 */
	public BaseResult sumitFeeManageByIds(FeeManageSearchReqDto feeManageReq) {
		BaseResult baseResult = new BaseResult();
		for (Integer id : feeManageReq.getIds()) {
			FeeManage feeManage = new FeeManage();
			feeManage.setId(id);
			sumitFeeManageById(feeManage);
		}
		return baseResult;
	}

	/**
	 * 删除管理费用信息
	 * 
	 * @param feeManage
	 * @return
	 */
	public BaseResult deleteFeeManageById(FeeManage feeManage) {
		BaseResult baseResult = new BaseResult();
		feeManageResDao.queryEntityById(feeManage.getId());// 锁表
		feeManage.setIsDelete(BaseConsts.ONE);
		feeManageResDao.updateById(feeManage);
		return baseResult;
	}

	/**
	 * 导出信息
	 * 
	 * @param searchreqDto
	 * @return
	 */
	public PageResult<FeeManageResDto> queryFeeManageResultsByEx(FeeManageSearchReqDto searchreqDto) {
		PageResult<FeeManageResDto> result = new PageResult<FeeManageResDto>();
		Integer userId = ServiceSupport.getUser().getId();
		boolean isRole = true; // 是否拥有超级管理员权限
		List<BaseRole> roles = cacheService.getRolesByUserId(userId);
		if (roles != null) {
			for (BaseRole baseRole : roles) {
				if (baseRole.getId() == BaseConsts.ONE) {
					isRole = false;
				}
			}
		}
		if (isRole) {
			if (ServiceSupport.isAllowPerm(BusUrlConsts.QUERY_FEE_MANAGE_POWER)) {// 判断用户是否拥有权限
				searchreqDto.setCreatorId(userId);
			} else {
				List<Integer> departmentId = new ArrayList<Integer>();
				departmentId.add(ServiceSupport.getUser().getDepartmentId());
				searchreqDto.setDepartmentId(departmentId);
			}
		}
		List<FeeManageResDto> feeManageResDto = convertToFeeManageResDtos(
				feeManageResDao.queryResultsByCon(searchreqDto));
		result.setItems(feeManageResDto);
		return result;
	}

	/**
	 * 判断是否超出导出行数
	 * 
	 * @param searchreqDto
	 * @return
	 */
	public boolean isFeeManageMaxLine(FeeManageSearchReqDto searchreqDto) {
		searchreqDto.setUserId(ServiceSupport.getUser().getId());
		// searchreqDto.setFeeType(BaseConsts.FOUR); 费用科目
		int count = feeManageResDao.isFeeManageMaxLine(searchreqDto);
		//
		if (count >= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			// 后台导出
			AsyncExcel asyncExcel = new AsyncExcel();
			asyncExcel.setName("费用管理单据导出");
			asyncExcel.setClassName(this.getClass().getName());// 导出类名
			asyncExcel.setMethodName("asyncInvoiceOverseasExport");// 导出方法
			asyncExcel.setTemplatePath("/WEB-INF/excel/export/fee/feeManage_list.xls");// 导出模板路径
			asyncExcel.setPoType(BaseConsts.INT_20);
			asyncExcelService.addAsyncExcel(searchreqDto, asyncExcel);
			return true;
		}
		return false;
	}

	public Map<String, Object> asyncFeeManageExport(FeeManageSearchReqDto searchreqDto) {
		Map<String, Object> model = Maps.newHashMap();
		List<FeeManageResDto> feeManageList = convertToFeeManageResDtos(
				feeManageResDao.queryResultsByCon(searchreqDto));
		model.put("feeManageList", feeManageList);
		return model;
	}

	/**
	 * 费用管理信息查询
	 * 
	 * @param searchreqDto
	 * @return
	 */
	public PageResult<FeeManageResDto> queryFeeManagesResultsByCon(FeeManageSearchReqDto searchreqDto) {
		PageResult<FeeManageResDto> pageResult = new PageResult<FeeManageResDto>();
		int offSet = PageUtil.getOffSet(searchreqDto.getPage(), searchreqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, searchreqDto.getPer_page());
		Integer userId = ServiceSupport.getUser().getId();

		boolean isRole = true; // 是否拥有超级管理员权限
		List<BaseRole> roles = cacheService.getRolesByUserId(userId);
		if (roles != null) {
			for (BaseRole baseRole : roles) {
				if (baseRole.getId() == BaseConsts.ONE) {
					isRole = false;
				}
			}
		}
		if (isRole) {
			if (ServiceSupport.isAllowPerm(BusUrlConsts.QUERY_FEE_MANAGE_POWER)) {// 判断用户是否拥有权限
				searchreqDto.setCreatorId(userId);
			} else {
				List<Integer> departmentId = new ArrayList<Integer>();
				departmentId.add(ServiceSupport.getUser().getDepartmentId());
				searchreqDto.setDepartmentId(departmentId);
			}
		}
		searchreqDto.setFeeType(BaseConsts.FOUR);
		List<FeeManageResDto> feeManageResDto = convertToFeeManageResDtos(
				feeManageResDao.queryResultsByCon(searchreqDto, rowBounds));

		if (searchreqDto.getNeedSum() != null && searchreqDto.getNeedSum() == BaseConsts.ONE) {// 是否需要合计，1表示是，2表示否
			List<FeeManage> sumResDto = feeManageResDao.queryResultsByCon(searchreqDto);
			if (CollectionUtils.isNotEmpty(sumResDto)) {
				BigDecimal amountSum = BigDecimal.ZERO;
				for (FeeManage feeManage : sumResDto) {
					if (feeManage != null) {
						String amoutString = "0";
						if (feeManage.getAmount() != null && !"".equals(feeManage.getAmount())) {
							try {
								byte[] output = Coder.decryptBASE64(feeManage.getAmount());
								amoutString = new String(output);
							} catch (Exception e) {
							}
						}
						amountSum = DecimalUtil.add(amountSum,
								ServiceSupport.amountNewToRMB(
										feeManage.getAmount() == null ? DecimalUtil.ZERO
												: new BigDecimal(new String(amoutString)),
										feeManage.getCurrnecyType(), new Date()));
					}
				}
				String totalStr = "金额  : " + DecimalUtil.toAmountString(DecimalUtil.formatScale2(amountSum)) + " CNY";
				pageResult.setTotalStr(totalStr);
			}
		}

		pageResult.setItems(feeManageResDto);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), searchreqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(searchreqDto.getPage());
		pageResult.setPer_page(searchreqDto.getPer_page());
		return pageResult;
	}

	/**
	 * 人工费用信息查询
	 * 
	 * @param searchreqDto
	 * @return
	 */
	public PageResult<FeeManageResDto> queryFeeArtificialResultsByCon(FeeManageSearchReqDto searchreqDto) {
		PageResult<FeeManageResDto> pageResult = new PageResult<FeeManageResDto>();
		int offSet = PageUtil.getOffSet(searchreqDto.getPage(), searchreqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, searchreqDto.getPer_page());
		Integer userId = ServiceSupport.getUser().getId();
		boolean isRole = true; // 是否拥有超级管理员权限
		List<BaseRole> roles = cacheService.getRolesByUserId(userId);
		if (roles != null) {
			for (BaseRole baseRole : roles) {
				if (baseRole.getId() == BaseConsts.ONE) {
					isRole = false;
				}
			}
		}
		if (ServiceSupport.isAllowPerm(BusUrlConsts.QUERY_MANUAL_FEE_MANAGE_POWER)) {
			isRole = false;
		}
		if (isRole) {
			List<Integer> departmentId = new ArrayList<Integer>();
			departmentId.add(ServiceSupport.getUser().getDepartmentId());
			searchreqDto.setDepartmentId(departmentId);
		}

		searchreqDto.setFeeType(BaseConsts.FIVE);
		List<FeeManageResDto> feeManageResDto = convertToFeeManageResDtos(
				feeManageResDao.queryResultsByCon(searchreqDto, rowBounds));

		if (searchreqDto.getNeedSum() != null && searchreqDto.getNeedSum() == BaseConsts.ONE) {// 是否需要合计，1表示是，2表示否
			List<FeeManage> sumResDto = feeManageResDao.queryResultsByCon(searchreqDto);
			if (CollectionUtils.isNotEmpty(sumResDto)) {
				BigDecimal amountSum = BigDecimal.ZERO;
				for (FeeManage feeManage : sumResDto) {
					if (feeManage != null) {
						String amoutString = "0";
						if (feeManage.getAmount() != null && !"".equals(feeManage.getAmount())) {
							try {
								byte[] output = Coder.decryptBASE64(feeManage.getAmount());
								amoutString = new String(output);
							} catch (Exception e) {
							}
						}
						amountSum = DecimalUtil.add(amountSum,
								ServiceSupport.amountNewToRMB(
										feeManage.getAmount() == null ? DecimalUtil.ZERO : new BigDecimal(amoutString),
										feeManage.getCurrnecyType(), new Date()));
					}
				}
				String totalStr = "金额  : " + DecimalUtil.toAmountString(DecimalUtil.formatScale2(amountSum)) + " CNY";
				pageResult.setTotalStr(totalStr);
			}
		}

		pageResult.setItems(feeManageResDto);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), searchreqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(searchreqDto.getPage());
		pageResult.setPer_page(searchreqDto.getPer_page());
		return pageResult;
	}

	public List<FeeManageResDto> convertToFeeManageResDtos(List<FeeManage> result) {
		List<FeeManageResDto> feeManageList = new ArrayList<FeeManageResDto>();
		if (ListUtil.isEmpty(result)) {
			return feeManageList;
		}
		for (FeeManage feeManage : result) {
			FeeManageResDto feeManageResDto = convertToFeeManageResDto(feeManage);
			List<CodeValue> operList = getOperList(feeManage.getState());
			feeManageResDto.setOpertaList(operList);
			feeManageList.add(feeManageResDto);
		}
		return feeManageList;
	}

	public FeeManageResDto convertToFeeManageResDto(FeeManage model) {
		FeeManageResDto result = new FeeManageResDto();
		result.setId(model.getId());
		result.setFeeManageNo(model.getFeeManageNo());
		result.setDepartmentId(model.getDepartmentId());
		result.setDepartmentName(cacheService.getBaseDepartmentById(model.getDepartmentId()).getNameNo());
		result.setUserId(model.getUserId());
		if (model.getUserId() != null) {
			result.setUserName(cacheService.getUserByid(model.getUserId()).getChineseName());
		}
		result.setProjectId(model.getProjectId());
		if (model.getProjectId() != null) {
			result.setProjectName(cacheService.showProjectNameById(model.getProjectId()));
		}
		result.setFeeSpecId(model.getFeeSpecId());
		FeeSpec feeSpec = feeSpecDao.queryEntityById(model.getFeeSpecId());
		if (feeSpec != null) {
			result.setFeeSpecName(feeSpec.getFeeSpecNo() + "-" + feeSpec.getFeeSpecName());
		}
		result.setCustId(model.getCustId());
		if (model.getCustId() != null) {
			result.setCustName(cacheService.getSubjectNameByIdAndKey(model.getCustId(), CacheKeyConsts.PROJECT_CS));
		}
		result.setRecType(model.getRecType());
		result.setRecTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.REC_TYPE, model.getRecType() + ""));
		result.setDate(model.getDate());
		result.setCurrnecyType(model.getCurrnecyType());
		result.setCurrnecyTypeName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE, model.getCurrnecyType() + ""));
		if (model.getAmount() == null || "".equals(model.getAmount())) {
			result.setAmount(BigDecimal.ZERO);
		} else {
			if (Check.isNumber(model.getAmount())) {
				result.setAmount(new BigDecimal(model.getAmount()));
			} else {
				String amountString = "0.00";
				try {// 解密
					byte[] output = Coder.decryptBASE64(model.getAmount());
					amountString = new String(output);
				} catch (Exception e) {
				}
				result.setAmount(new BigDecimal(new String(amountString)));
			}
		}

		if (model.getShareAmount() == null || "".equals(model.getShareAmount())) {
			result.setShareAmount(BigDecimal.ZERO);
		} else {
			if (Check.isNumber(model.getShareAmount())) {
				result.setAmount(new BigDecimal(model.getShareAmount()));
			} else {
				String amountString = "0.00";
				try {// 解密
					byte[] output = Coder.decryptBASE64(model.getShareAmount());
					amountString = new String(output);
				} catch (Exception e) {
				}
				result.setShareAmount(new BigDecimal(new String(amountString)));
			}
		}
		result.setBlanceAmount(DecimalUtil.subtract(result.getAmount(), result.getShareAmount()));
		result.setState(model.getState());
		result.setStateName(ServiceSupport.getValueByBizCode(BizCodeConsts.FEE_MANAGE_STATE, model.getState() + ""));
		result.setRemark(model.getRemark());
		result.setCreator(model.getCreator());
		result.setCreateAt(model.getCreateAt());
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
				FeeManageResDto.Operate.operMap);
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
			opertaList.add(OperateConsts.SHARE);
			break;
		}

		return opertaList;
	}

	/**
	 * 费用管理导入
	 * 
	 * @param importFile
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void importFeeManageExcel(MultipartFile importFile, Integer feeSpec) {
		List<FeeManageResDto> feeManageList = Lists.newArrayList();
		Map beans = Maps.newHashMap();
		beans.put("feeManageList", feeManageList);
		ExcelService.resolverExcel(importFile, "/excel/fee/fee_manage.xml", beans);
		// 业务逻辑处理
		feeManageList = (List<FeeManageResDto>) beans.get("feeManageList");
		if (CollectionUtils.isNotEmpty(feeManageList)) {
			if (feeManageList.size() > BaseConsts.IMPORT_EXCEL_ROWS_MAX) {
				throw new BaseException(ExcMsgEnum.IMPORT_EXCEL_ROWS_MAX);
			}
			boolean result = true;
			for (FeeManageResDto feeManage : feeManageList) {// 校验信息是否正确
				result = validateFeeManageInfo(feeManage, feeSpec);
			}
			if (result) {
				for (FeeManageResDto fee : feeManageList) {
					FeeManage manage = new FeeManage();
					manage.setDepartmentId(fee.getDepartmentId());
					manage.setProjectId(fee.getProjectId());
					manage.setUserId(fee.getUserId());
					manage.setCustId(fee.getCustId());
					manage.setFeeSpecId(fee.getFeeSpecId());
					manage.setRecType(fee.getRecType());
					manage.setDate(fee.getDate());
					manage.setCurrnecyType(fee.getCurrnecyType());
					manage.setAmount(fee.getAmount().toString());
					manage.setRemark(fee.getRemark());
					createFeeManage(manage);
				}
			}
		} else {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "导入费用管理信息不能为空");
		}
	}

	/**
	 * 校验信息
	 * 
	 * @param manage
	 * @return
	 */
	private boolean validateFeeManageInfo(FeeManageResDto manage, Integer feeSpec) {
		boolean result = true;
		// 判断必填字段是否为空
		String departmentName = manage.getDepartmentName();
		if (departmentName == null || departmentName.equals("")) {
			result = false;
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "部门不能为空");
		}
		String feeSpecName = manage.getFeeSpecName();
		if (feeSpecName == null || feeSpecName.equals("")) {
			result = false;
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "管理费用科目不能为空");
		}
		String recTypeName = manage.getRecTypeName();
		if (recTypeName == null || recTypeName.equals("")) {
			result = false;
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "应付方式不能为空");
		}
		Date date = manage.getDate();
		if (date == null) {
			result = false;
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "日期不能不能为空");
		}
		String currnecyTypeName = manage.getCurrnecyTypeName();
		if (currnecyTypeName == null || currnecyTypeName.equals("")) {
			result = false;
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "币种不能为空");
		}
		BigDecimal amount = manage.getAmount();
		if (amount == null) {
			result = false;
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "管理费用金额不能为空");
		}

		// 判断部门是否存在
		String departmentId = commonParamValidate.getDepartmentDao(departmentName.trim());
		if (departmentId == null || departmentId.equals("")) {
			result = false;
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "部门" + departmentName + "不存在");
		}
		manage.setDepartmentId(Integer.parseInt(departmentId));

		// 判断项目是否存在
		String projectName = manage.getProjectName();
		if (projectName != null && !"".equals(projectName)) {
			String projectId = commonParamValidate.getAllOwnCvValidate("DEPARTMENT_USER_PROJECT", departmentId,
					projectName.trim());
			if (projectId == null) {
				result = false;
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL,
						"部门:" + departmentName + "下项目:" + projectName + "不存在");
			}
			manage.setProjectId(Integer.parseInt(projectId));
		}

		// 判断用户
		String userName = manage.getUserName();
		if (userName != null && !"".equals(userName)) {
			String userId = commonParamValidate.getAllOwnCvValidate("DEPARTMENT_USER", departmentId, userName.trim());
			if (userId == null) {
				result = false;
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "部门:" + departmentName + "下人员:" + userName + "不存在");
			}
			manage.setUserId(Integer.parseInt(userId));
		}

		// 判断客户
		String custName = manage.getCustName();
		if (custName != null && !"".equals(custName)) {
			String custId = commonParamValidate.getAllOwnCvValidate("PROJECT_CS", departmentId, custName.trim());
			if (custId == null) {
				result = false;
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "部门:" + departmentName + "下客户:" + custName + "不存在");
			}
			manage.setCustId(Integer.parseInt(custId));
		}

		// 校验管理费用科目是否存在
		String feeSpecId = null;
		if (feeSpec.equals(BaseConsts.FOUR)) {
			feeSpecId = commonParamValidate.getAllCdByKeyValidate("PAY_FEE_MANAGE", feeSpecName.trim());
		} else {
			feeSpecId = commonParamValidate.getAllCdByKeyValidate("PAY_FEE_ARTIFICIAL", feeSpecName.trim());
		}
		if (feeSpecId == null) {
			result = false;
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "管理费用科目:" + feeSpecName + "不存在");
		}
		manage.setFeeSpecId(Integer.parseInt(feeSpecId));

		// 校验应付方式是否存在
		String recTypeId = commonParamValidate.getAllCdByKeyValidate("REC_TYPE", recTypeName);
		if (recTypeId == null) {
			result = false;
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "应付方式:" + recTypeName + "不存在");
		}
		manage.setRecType(Integer.parseInt(recTypeId));

		// 校验币种
		String currencyTypeId = commonParamValidate.cvListByBizCodeValidate("DEFAULT_CURRENCY_TYPE",
				currnecyTypeName.trim());
		if (currencyTypeId == null) {
			result = false;
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "币种:" + currnecyTypeName + "不存在");
		}
		manage.setCurrnecyType(Integer.parseInt(currencyTypeId));

		// 判断备注长度
		String remark = manage.getRemark();
		if (remark != null) {
			if (remark.length() > BaseConsts.INT_200) {
				result = false;
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "备注不能大于200字");
			}
		}
		return result;
	}

	/**
	 * 获取文件操作列表,附件相关
	 * 
	 * @param state
	 * @return
	 */
	public PageResult<FeeManageFileResDto> queryFileList(FileAttachSearchReqDto fileAttReqDto) {
		PageResult<FeeManageFileResDto> pageResult = new PageResult<FeeManageFileResDto>();
		fileAttReqDto.setBusType(BaseConsts.INT_30);
		List<FileAttach> fielAttach = fileUploadService.queryFileAttList(fileAttReqDto);
		List<FeeManageFileResDto> list = convertToFileResDto(fielAttach);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), fileAttReqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(fileAttReqDto.getPage());
		pageResult.setPer_page(fileAttReqDto.getPer_page());
		pageResult.setItems(list);
		return pageResult;
	}

	public List<FeeManageFileResDto> queryFileList(Integer feeManageId) {
		FileAttachSearchReqDto fileAttReqDto = new FileAttachSearchReqDto();
		fileAttReqDto.setBusId(feeManageId);
		fileAttReqDto.setBusType(BaseConsts.INT_30);
		List<FileAttach> fielAttach = fileUploadService.queryFileAttList(fileAttReqDto);
		List<FeeManageFileResDto> list = convertToFileResDto(fielAttach);
		return list;
	}

	private List<FeeManageFileResDto> convertToFileResDto(List<FileAttach> fileAttach) {
		List<FeeManageFileResDto> list = new LinkedList<FeeManageFileResDto>();
		if (ListUtil.isEmpty(fileAttach)) {
			return list;
		}
		for (FileAttach model : fileAttach) {
			FeeManageFileResDto result = new FeeManageFileResDto();
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
				FeeManageFileResDto.Operate.operMap);
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

	/**
	 * 通过主表ID查询(无人员有项目或者有人员有项目)明细总金额
	 * 
	 * @param id
	 * @return BigDecimal amountFeeManage2
	 */
	private BigDecimal queryResultsByProjectId(Integer id) {
		FeeShareReqDto feeShareReqDto = new FeeShareReqDto();
		feeShareReqDto.setManageId(id);
		List<FeeShare> feeShareList2 = feeShareDao.queryResultsByManageId(feeShareReqDto);
		BigDecimal amountFeeManage2 = BigDecimal.ZERO;
		if (feeShareList2 != null && feeShareList2.size() > 0) {
			BigDecimal amountFeeManage1 = BigDecimal.ZERO;
			for (FeeShare feeShares1 : feeShareList2) {
				// 无人员有项目或者有人员有项目
				if ((feeShares1.getShareUserId() == null && feeShares1.getShareProjectId() != null)
						|| (feeShares1.getShareUserId() != null && feeShares1.getShareProjectId() != null)) {
					if (feeShares1.getAmount() == null || "".equals(feeShares1.getAmount())) {
						amountFeeManage1 = BigDecimal.ZERO;
					} else {
						if (Check.isNumber(feeShares1.getAmount())) {
							amountFeeManage1 = new BigDecimal(feeShares1.getAmount());
						} else {
							String amountString = "0.00";
							try {// 解密
								byte[] output = Coder.decryptBASE64(feeShares1.getAmount());
								amountString = new String(output);
							} catch (Exception e) {
							}
							amountFeeManage1 = new BigDecimal(new String(amountString));
						}
					}
					amountFeeManage2 = amountFeeManage2.add(amountFeeManage1);
				}
			}
		}
		return amountFeeManage2;
	}

	/**
	 * 通过主表ID查询(无人员无项目)明细总金额
	 * 
	 * @param id
	 * @return BigDecimal amountFeeManage2
	 */
	private BigDecimal notAueryResultsByUserIdByProjectId(List<FeeShare> feeShareList2) {
		BigDecimal amountFeeManage2 = BigDecimal.ZERO;
		if (feeShareList2 != null && feeShareList2.size() > 0) {
			BigDecimal amountFeeManage1 = BigDecimal.ZERO;
			for (FeeShare feeShares1 : feeShareList2) {
				// 无人员无项目
				if (feeShares1.getShareUserId() == null && feeShares1.getShareProjectId() == null) {
					if (feeShares1.getAmount() == null || "".equals(feeShares1.getAmount())) {
						amountFeeManage1 = BigDecimal.ZERO;
					} else {
						if (Check.isNumber(feeShares1.getAmount())) {
							amountFeeManage1 = new BigDecimal(feeShares1.getAmount());
						} else {
							String amountString = "0.00";
							try {// 解密
								byte[] output = Coder.decryptBASE64(feeShares1.getAmount());
								amountString = new String(output);
							} catch (Exception e) {
							}
							amountFeeManage1 = new BigDecimal(new String(amountString));
						}
					}
					amountFeeManage2 = amountFeeManage2.add(amountFeeManage1);
				}
			}
		}
		return amountFeeManage2;
	}

	/**
	 * 通过主表ID查询(有人员无项目)明细总金额
	 * 
	 * @param id
	 * @return BigDecimal amountFeeManage2
	 */
	private BigDecimal queryResultsByUserId(Integer id) {
		FeeShareReqDto feeShareReqDto = new FeeShareReqDto();
		feeShareReqDto.setManageId(id);
		List<FeeShare> feeShareList2 = feeShareDao.queryResultsByManageId(feeShareReqDto);
		BigDecimal amountFeeManage2 = BigDecimal.ZERO;
		if (feeShareList2 != null && feeShareList2.size() > 0) {
			BigDecimal amountFeeManage1 = BigDecimal.ZERO;
			for (FeeShare feeShares1 : feeShareList2) {
				// 有人员无项目
				if (feeShares1.getShareUserId() != null && feeShares1.getShareProjectId() == null) {
					if (feeShares1.getAmount() == null || "".equals(feeShares1.getAmount())) {
						amountFeeManage1 = BigDecimal.ZERO;
					} else {
						if (Check.isNumber(feeShares1.getAmount())) {
							amountFeeManage1 = new BigDecimal(feeShares1.getAmount());
						} else {
							String amountString = "0.00";
							try {// 解密
								byte[] output = Coder.decryptBASE64(feeShares1.getAmount());
								amountString = new String(output);
							} catch (Exception e) {
							}
							amountFeeManage1 = new BigDecimal(new String(amountString));
						}
					}
					amountFeeManage2 = amountFeeManage2.add(amountFeeManage1);
				}
			}
		}
		return amountFeeManage2;
	}

	/**
	 * 通过主表ID查询(有人员无项目)明细总金额
	 * 
	 * @param id
	 * @return BigDecimal amountFeeManage2
	 */
	private BigDecimal queryResultsByUserId(List<FeeShare> feeShareList2) {
		BigDecimal amountFeeManage2 = BigDecimal.ZERO;
		if (feeShareList2 != null && feeShareList2.size() > 0) {
			BigDecimal amountFeeManage1 = BigDecimal.ZERO;
			for (FeeShare feeShares1 : feeShareList2) {
				// 有人员无项目
				if (feeShares1.getShareUserId() != null && feeShares1.getShareProjectId() == null) {
					if (feeShares1.getAmount() == null || "".equals(feeShares1.getAmount())) {
						amountFeeManage1 = BigDecimal.ZERO;
					} else {
						if (Check.isNumber(feeShares1.getAmount())) {
							amountFeeManage1 = new BigDecimal(feeShares1.getAmount());
						} else {
							String amountString = "0.00";
							try {// 解密
								byte[] output = Coder.decryptBASE64(feeShares1.getAmount());
								amountString = new String(output);
							} catch (Exception e) {
							}
							amountFeeManage1 = new BigDecimal(new String(amountString));
						}
					}
					amountFeeManage2 = amountFeeManage2.add(amountFeeManage1);
				}
			}
		}
		return amountFeeManage2;
	}

	/**
	 * 通过主表ID查询(无人员有项目或者有人员有项目)明细总金额
	 * 
	 * @param id
	 * @return BigDecimal amountFeeManage2
	 */
	private BigDecimal queryResultsByProjectId(List<FeeShare> feeShareList2) {
		BigDecimal amountFeeManage2 = BigDecimal.ZERO;
		if (feeShareList2 != null && feeShareList2.size() > 0) {
			BigDecimal amountFeeManage1 = BigDecimal.ZERO;
			for (FeeShare feeShares1 : feeShareList2) {
				// 无人员有项目或者有人员有项目
				if ((feeShares1.getShareUserId() == null && feeShares1.getShareProjectId() != null)
						|| (feeShares1.getShareUserId() != null && feeShares1.getShareProjectId() != null)) {
					if (feeShares1.getAmount() == null || "".equals(feeShares1.getAmount())) {
						amountFeeManage1 = BigDecimal.ZERO;
					} else {
						if (Check.isNumber(feeShares1.getAmount())) {
							amountFeeManage1 = new BigDecimal(feeShares1.getAmount());
						} else {
							String amountString = "0.00";
							try {// 解密
								byte[] output = Coder.decryptBASE64(feeShares1.getAmount());
								amountString = new String(output);
							} catch (Exception e) {
							}
							amountFeeManage1 = new BigDecimal(new String(amountString));
						}
					}
					amountFeeManage2 = amountFeeManage2.add(amountFeeManage1);
				}
			}
		}
		return amountFeeManage2;
	}

	/**
	 * 总金额解密
	 * 
	 * @param id
	 * @return BigDecimal amountFeeManage2
	 */
	private BigDecimal queryResultsByFeeManage(FeeManage feeManage) {
		// 主表总金额
		BigDecimal amount = BigDecimal.ZERO;
		if (feeManage.getAmount() == null || "".equals(feeManage.getAmount())) {
			amount = BigDecimal.ZERO;
		} else {
			if (Check.isNumber(feeManage.getAmount())) {
				amount = new BigDecimal(feeManage.getAmount());
			} else {
				String amountString = "0.00";
				try {// 解密
					byte[] output = Coder.decryptBASE64(feeManage.getAmount());
					amountString = new String(output);
				} catch (Exception e) {
				}
				amount = new BigDecimal(new String(amountString));
			}
		}
		return amount;
	}

	/**
	 * 可用金额解密
	 * 
	 * @param id
	 * @return BigDecimal amountFeeManage2
	 */
	private BigDecimal queryShareAmountByFeeManage(FeeManage feeManage) {
		// 可用金额
		BigDecimal amount = BigDecimal.ZERO;
		if (feeManage.getShareAmount() == null || "".equals(feeManage.getShareAmount())) {
			amount = BigDecimal.ZERO;
		} else {
			if (Check.isNumber(feeManage.getShareAmount())) {
				amount = new BigDecimal(feeManage.getShareAmount());
			} else {
				String amountString = "0.00";
				try {// 解密
					byte[] output = Coder.decryptBASE64(feeManage.getShareAmount());
					amountString = new String(output);
				} catch (Exception e) {
				}
				amount = new BigDecimal(new String(amountString));
			}
		}
		return amount;
	}

	// 管理费用分摊规则
	public void manageAmountShare(List<FeeManage> manageFeeList, Date shareDate) {
		for (FeeManage feeManage : manageFeeList) {
			// 主表总金额
			BigDecimal amount = queryResultsByFeeManage(feeManage);
			// 主表已分摊金额
			BigDecimal shareAmount1 = queryShareAmountByFeeManage(feeManage);
			BigDecimal shareAmo = DecimalUtil.subtract(amount, shareAmount1);
			if (amount.compareTo(shareAmount1) == 1) {
				// 头有人员无项目
				if (feeManage.getUserId() != null && feeManage.getProjectId() == null) {
					FeeShareReqDto feeShareReqDto = new FeeShareReqDto();
					feeShareReqDto.setManageId(feeManage.getId());
					List<FeeShare> feeShareList = feeShareDao.queryResultsByManageId(feeShareReqDto);
					if (feeShareList != null && feeShareList.size() > 0) {
						for (FeeShare feeShare : feeShareList) {
							if (feeShare.getShareUserId() != null && feeShare.getShareProjectId() == null) {
								List<BaseUserProject> baseUserProjectList = baseProjectDao
										.queryUserProjectAssignedToUser(feeShare.getShareUserId(),
												new RowBounds(0, 1000));
								// 费用分摊有人员且人员名下有项目：利润报表和绩效报表通过人员关联对应的项目按利润占比分摊，挂人工费用（自动分摊）
								if (baseUserProjectList != null && baseUserProjectList.size() > 0) {
									perManagePeport(feeManage, feeShare, feeShareList, amount, shareAmo, shareDate);
								}
							}
						}
					}
				}
				// 头表无人员无项目 费用分摊无人员无项目：利润报表和绩效报表按部门下面角色为业务专员平摊，然后按各自
				// 通过部门查询角色,角色下的业务专员查询项目
				if (feeManage.getUserId() == null && feeManage.getProjectId() == null
						&& feeManage.getDepartmentId() != null) {
					// 直接通过部门找项目利润
					ProfitReportReqMonthDto profitReportReqMonthDto = new ProfitReportReqMonthDto();
					profitReportReqMonthDto.setDepartmentId(feeManage.getDepartmentId());
					String preDate = DateFormatUtils.format(DateFormatUtils.YYYYMM, shareDate);
					profitReportReqMonthDto.setStartStatisticsDate(preDate);
					profitReportReqMonthDto.setEndStatisticsDate(preDate);
					List<MounthProfitReport> pounthProfitReport = profitReportMonthDao
							.mounthProfitReportByDepartmentId(profitReportReqMonthDto);
					if (pounthProfitReport != null && pounthProfitReport.size() > 0) {
						BigDecimal profitAmount = BigDecimal.ZERO;// 利润总金额
						BigDecimal profitRate = BigDecimal.ZERO;
						if (pounthProfitReport != null && pounthProfitReport.size() > 0) {
							for (MounthProfitReport mounthProfitReport : pounthProfitReport) {
								BigDecimal amount2 = BigDecimal.ZERO;
								if (mounthProfitReport.getProfitAmount() == null
										|| "".equals(mounthProfitReport.getProfitAmount())) {
									amount2 = BigDecimal.ZERO;
								} else {
									amount2 = new BigDecimal(mounthProfitReport.getProfitAmount().toString());
								}
								profitAmount = DecimalUtil.add(profitAmount, amount2);
							}
						}
						if (profitAmount.compareTo(BigDecimal.ZERO) == 1) {
							for (MounthProfitReport baseUserProject : pounthProfitReport) {
								// 利润占比
								profitRate = DecimalUtil.divide(baseUserProject.getProfitAmount(), profitAmount)
										.setScale(10, BigDecimal.ROUND_HALF_DOWN);
								if (profitRate.compareTo(BigDecimal.ZERO) == 1) {
									// 需要把无人员无项目，有人员无项目的数据删除
									FeeShareReqDto feeShareReqDto1 = new FeeShareReqDto();
									feeShareReqDto1.setManageId(feeManage.getId());
									List<FeeShare> feeShareList2 = feeShareDao.queryResultsByManageId(feeShareReqDto1);
									if (feeShareList2 != null && feeShareList2.size() > 0) {
										//BigDecimal feeShareAmount = queryResultsByProjectId(feeShareList2);// 无人员有项目或者有人员有项目,明细总金额
										//BigDecimal notFeeShareAmount = notAueryResultsByUserIdByProjectId(feeShareList2);// 无人员无项目,明细总金额
										for (FeeShare feeShares1 : feeShareList2) {
											if (feeShares1.getShareUserId() != null
													&& feeShares1.getShareProjectId() == null) {
												List<BaseUserProject> baseUserProjectLis = baseProjectDao
														.queryUserProjectAssignedToUser(feeShares1.getShareUserId(),
																new RowBounds(0, 1000));
												// 有人员且名下有项目
												if (baseUserProjectLis != null && baseUserProjectLis.size() > 0) {
													perSharePeport(feeShares1, feeManage, shareDate, amount);
												}
												// 有人员且名下无项目
												if (baseUserProjectLis != null && baseUserProjectLis.size() == 0) {
													/*
													 * //可用金额 = 主表总金额 - (无人员有项目
													 * + 无人员无项目)明细有项目总金额
													 * BigDecimal notShareAmount
													 * = DecimalUtil.subtract(
													 * amount,DecimalUtil.add(
													 * feeShareAmount,
													 * notFeeShareAmount));
													 * BigDecimal isShareAmount
													 * = DecimalUtil.subtract(
													 * amount,notShareAmount);
													 * 
													 * FeeShare feeShareIs =new
													 * FeeShare();
													 * feeShareIs.setIsDelete(1)
													 * ; feeShareIs.setId(
													 * feeShares1.getId());
													 * feeShareDao.updateById(
													 * feeShareIs);
													 * 
													 * //处理可分摊金额 FeeManage
													 * upFeeManage = new
													 * FeeManage();
													 * upFeeManage.setId(
													 * feeManage.getId());
													 * //金额加密 try { byte[]
													 * inputData =
													 * isShareAmount.toString().
													 * getBytes(); upFeeManage.
													 * setShareAmount(Coder.
													 * encryptBASE64(inputData))
													 * ; } catch (Exception e) {
													 * throw new
													 * BaseException(ExcMsgEnum.
													 * ERROR_GENERAL, "添加失败:" +
													 * JSONObject.toJSON(
													 * feeShareIs)); }
													 * updateFeeManageById(
													 * upFeeManage);
													 * 
													 * shareAmo =
													 * DecimalUtil.subtract(
													 * amount,isShareAmount);
													 */
												}
											}
										}
									}

									FeeShareReqDto feeShareReqDto2 = new FeeShareReqDto();
									feeShareReqDto2.setManageId(feeManage.getId());
									List<FeeShare> feeShareLists = feeShareDao.queryResultsByManageId(feeShareReqDto2);
									if (feeShareLists != null && feeShareLists.size() > 0) {
										BigDecimal feeShareAmount1 = queryResultsByUserId(feeShareList2);// 有人员无项目,明细总金额
										BigDecimal feeShareAmount2 = queryResultsByProjectId(feeShareList2);// 无人员有项目或者有人员有项目,明细总金额

										for (FeeShare feeShares1 : feeShareList2) {
											// 无人员无项目
											if (feeShares1.getShareUserId() == null
													&& feeShares1.getShareProjectId() == null) {
												BigDecimal notShareAmount = DecimalUtil.subtract(amount,
														DecimalUtil.add(feeShareAmount1, feeShareAmount2));
												BigDecimal isShareAmount = DecimalUtil.subtract(amount, notShareAmount);

												FeeShare feeShareIs = new FeeShare();
												feeShareIs.setIsDelete(1);
												feeShareIs.setId(feeShares1.getId());
												feeShareDao.updateById(feeShareIs);

												// 处理可分摊金额
												FeeManage upFeeManage = new FeeManage();
												upFeeManage.setId(feeManage.getId());
												// 金额加密
												try {
													byte[] inputData = isShareAmount.toString().getBytes();
													upFeeManage.setShareAmount(Coder.encryptBASE64(inputData));
												} catch (Exception e) {
													throw new BaseException(ExcMsgEnum.ERROR_GENERAL,
															"添加失败:" + JSONObject.toJSON(feeShareIs));
												}
												updateFeeManageById(upFeeManage);

												shareAmo = DecimalUtil.subtract(amount, isShareAmount);
											}
										}
									}

									BigDecimal feeShareAmount2 = queryResultsByProjectId(feeManage.getId());// 无人员有项目或者有人员有项目,明细总金额
									// 可分摊金额 = 主表总金额 - 明细有项目总金额
									BigDecimal notShareAmount2 = DecimalUtil.subtract(amount, feeShareAmount2)
											.setScale(8, BigDecimal.ROUND_HALF_DOWN);

									// 项目需要分摊的金额 = 利润占比 * 可分摊金额
									BigDecimal amamountProfitount = DecimalUtil.multiply(profitRate, shareAmo)
											.setScale(8, BigDecimal.ROUND_HALF_DOWN);
									Long num = notShareAmount2.longValue();
									Long num1 = amamountProfitount.longValue();
									// 检验是否有金额进行分摊
									if (notShareAmount2.compareTo(BigDecimal.ZERO) == 1
											&& (notShareAmount2.compareTo(amamountProfitount) == 1 || num - num1 > -1)
											&& (amamountProfitount.compareTo(BigDecimal.ZERO) == 1)) {
										FeeShare feeShares = new FeeShare();
										feeShares.setManageId(feeManage.getId());
										feeShares.setShareProjectId(baseUserProject.getProjectId());
										Date date = new Date();
										feeShares.setCreateAt(date);
										feeShares.setShareDate(shareDate);
										feeShares.setCreator(ServiceSupport.getUser() == null ? null
												: ServiceSupport.getUser().getChineseName());
										feeShares.setCreatorId(ServiceSupport.getUser() == null ? null
												: ServiceSupport.getUser().getId());
										if (amamountProfitount != null) {

											try {
												byte[] inputData = amamountProfitount.toString().getBytes();
												feeShares.setAmount(Coder.encryptBASE64(inputData));
											} catch (Exception e) {
												throw new BaseException(ExcMsgEnum.ERROR_GENERAL,
														"添加失败:" + JSONObject.toJSON(feeShares));
											}
											// BigDecimal amountFeeManage2 =
											// notAueryResultsByUserIdByProjectId(feeManage.getId());//明细总金额
											// 无人员无项目
											int result = feeShareDao.insert(feeShares);
											if (result <= 0) {
												throw new BaseException(ExcMsgEnum.ERROR_GENERAL,
														"添加失败:" + JSONObject.toJSON(feeShares));
											}
											FeeManage upFeeManage = new FeeManage();
											upFeeManage.setId(feeManage.getId());
											BigDecimal sum = DecimalUtil.subtract(amount,
													DecimalUtil.subtract(notShareAmount2, amamountProfitount)
															.setScale(2, BigDecimal.ROUND_HALF_DOWN));
											upFeeManage.setShareAmount(
													sum.setScale(2, BigDecimal.ROUND_HALF_DOWN).toString());
											updateFeeManageById(upFeeManage);
										}
									}
								}
							}
						}
					}

				}
				// 头表有项目 费用分摊有项目：利润报表和绩效报表按项目，挂人工费用
				if (feeManage.getProjectId() != null) {
					profitReportMounth(feeManage, amount, shareAmo, shareDate);
				}
			}
		}
		// 对有人员且无项目的数据做最后处理
		feeManageByShareAmount(manageFeeList);
	}

	/**
	 * 头表有人员且名下有项目
	 * 
	 * @param isSchedule
	 */
	public void perManagePeport(FeeManage feeManage, FeeShare feeShare, List<FeeShare> feeShareList, BigDecimal amount,
			BigDecimal shareAmo, Date shareDate) {
		List<BaseUserProject> baseUserProjectList = baseProjectDao
				.queryUserProjectAssignedToUser(feeShare.getShareUserId(), new RowBounds(0, 1000));
		// 费用分摊有人员且人员名下有项目：利润报表和绩效报表通过人员关联对应的项目按利润占比分摊，挂人工费用（自动分摊）
		if (baseUserProjectList != null && baseUserProjectList.size() > 0) {
			BigDecimal profitAmount = BigDecimal.ZERO;// 利润总金额
			BigDecimal profitRate = BigDecimal.ZERO;
			for (BaseUserProject baseUserProject : baseUserProjectList) {
				// 通过项目找利润
				ProfitReportReqMonthDto profitReportReqMonthDto = new ProfitReportReqMonthDto();
				profitReportReqMonthDto.setProjectId(baseUserProject.getProjectId());
				if (feeShare.getShareDate() != null) {
					shareDate = feeShare.getShareDate();
				}
				String preDate = DateFormatUtils.format(DateFormatUtils.YYYYMM, shareDate);
				profitReportReqMonthDto.setStartStatisticsDate(preDate);
				profitReportReqMonthDto.setEndStatisticsDate(preDate);
				List<MounthProfitReport> pounthProfitReport = profitReportMonthDao
						.queryResultsByCon(profitReportReqMonthDto);
				if (pounthProfitReport != null && pounthProfitReport.size() > 0) {
					BigDecimal amount2 = BigDecimal.ZERO;
					if (pounthProfitReport.get(0).getProfitAmount() == null
							|| "".equals(pounthProfitReport.get(0).getProfitAmount())) {
						amount2 = BigDecimal.ZERO;
					} else {
						amount2 = new BigDecimal(pounthProfitReport.get(0).getProfitAmount().toString());
					}
					profitAmount = DecimalUtil.add(profitAmount, amount2);
				}
			}
			if (profitAmount.compareTo(BigDecimal.ZERO) == 1) {
				for (BaseUserProject baseUserProject : baseUserProjectList) {
					// 通过项目找利润
					ProfitReportReqMonthDto profitReportReqMonthDto = new ProfitReportReqMonthDto();
					profitReportReqMonthDto.setProjectId(baseUserProject.getProjectId());
					if (feeShare.getShareDate() != null) {
						shareDate = feeShare.getShareDate();
					}
					String preDate = DateFormatUtils.format(DateFormatUtils.YYYYMM, shareDate);
					profitReportReqMonthDto.setStartStatisticsDate(preDate);
					profitReportReqMonthDto.setEndStatisticsDate(preDate);
					List<MounthProfitReport> pounthProfitReport = profitReportMonthDao
							.queryResultsByCon(profitReportReqMonthDto);
					if (pounthProfitReport != null && pounthProfitReport.size() > 0) {
						for (MounthProfitReport profitReport : pounthProfitReport) {
							if (profitReport.getProfitAmount() != null
									&& profitReport.getProfitAmount().compareTo(new BigDecimal(1)) == 1) {
								BigDecimal feeShareAmount1 = queryResultsByProjectId(feeShareList);// 无人员有项目或者有人员有项目,明细总金额
								BigDecimal notFeeShareAmount = queryResultsByUserId(feeShareList);// 有人员无项目,明细总金额
								// 可用金额 = 主表总金额 - (无人员有项目 + 无人员无项目)明细有项目总金额
								BigDecimal notShareAmount = DecimalUtil.subtract(amount,
										DecimalUtil.add(feeShareAmount1, notFeeShareAmount));

								BigDecimal isShareAmount = DecimalUtil.subtract(amount, notShareAmount);
								// 无人员无项目的数据先删除 再按照比例生成
								if (feeShare.getShareUserId() == null && feeShare.getShareProjectId() == null) {
									FeeShare feeShareIs = new FeeShare();
									feeShareIs.setIsDelete(1);
									feeShareIs.setId(feeShare.getId());
									feeShareDao.updateById(feeShareIs);

									// 处理可分摊金额
									FeeManage upFeeManage = new FeeManage();
									upFeeManage.setId(feeManage.getId());
									// 金额加密
									try {
										byte[] inputData = isShareAmount.toString().getBytes();
										upFeeManage.setShareAmount(Coder.encryptBASE64(inputData));
									} catch (Exception e) {
										throw new BaseException(ExcMsgEnum.ERROR_GENERAL,
												"添加失败:" + JSONObject.toJSON(feeShareIs));
									}
									updateFeeManageById(upFeeManage);

									shareAmo = DecimalUtil.subtract(amount, isShareAmount);
								}

								// 利润占比
								profitRate = DecimalUtil.divide(profitReport.getProfitAmount(), profitAmount)
										.setScale(10, BigDecimal.ROUND_HALF_DOWN);
								// 项目需要分摊的金额 = 利润占比 * 可分摊金额
								BigDecimal amountProfit = DecimalUtil.multiply(profitRate, shareAmo).setScale(8,
										BigDecimal.ROUND_HALF_DOWN);

								Long num = notShareAmount.longValue();
								Long num1 = amountProfit.longValue();
								// 检验是否有金额进行分摊
								if (notShareAmount.compareTo(BigDecimal.ZERO) == 1
										&& (notShareAmount.compareTo(amountProfit) == 1 || num - num1 > -1)) {
									FeeShare feeShares = new FeeShare();
									feeShares.setManageId(feeShare.getManageId());
									feeShares.setShareProjectId(profitReport.getProjectId());
									feeShares.setShareUserId(baseUserProject.getUserId());
									Date date = new Date();
									feeShares.setCreateAt(date);
									feeShares.setShareDate(shareDate);
									feeShares.setCreator(ServiceSupport.getUser() == null ? null
											: ServiceSupport.getUser().getChineseName());
									feeShares.setCreatorId(
											ServiceSupport.getUser() == null ? null : ServiceSupport.getUser().getId());
									// 金额加密
									try {
										byte[] inputData = amountProfit.toString().getBytes();
										feeShares.setAmount(Coder.encryptBASE64(inputData));
									} catch (Exception e) {
										throw new BaseException(ExcMsgEnum.ERROR_GENERAL,
												"添加失败:" + JSONObject.toJSON(feeShares));
									}
									int result = feeShareDao.insert(feeShares);
									if (result <= 0) {
										throw new BaseException(ExcMsgEnum.ERROR_GENERAL,
												"添加失败:" + JSONObject.toJSON(feeShares));
									}
									// 处理可分摊金额
									FeeManage upFeeManage = new FeeManage();
									upFeeManage.setId(feeManage.getId());
									BigDecimal sum = DecimalUtil
											.subtract(amount, DecimalUtil.subtract(notShareAmount, amountProfit))
											.setScale(2, BigDecimal.ROUND_HALF_DOWN);
									upFeeManage.setShareAmount(sum.toString());
									updateFeeManageById(upFeeManage);
								}
							}
						}
					}
				}
			}
		}
		// 明细费用分摊有人员且名下无项目：利润报表和绩效报表按部门，挂人工费用（取人员无项目的已分摊金额）（自动分摊）
		if (baseUserProjectList == null || baseUserProjectList.size() == 0) {
			// 需求检验
		}

	}

	// 统计分摊金额信息（人工费用/管理费用）
	public void dealPerShareReport(FeeManage feeManages) {
		List<FeeManage> feeManageList = new ArrayList<FeeManage>();
		List<FeeManage> manageFeeList = new ArrayList<FeeManage>();
		// 分摊日期
		Date shareDate = feeManages.getShareDate();
		if (feeManages.getIds() != null && feeManages.getIds().size() > 0) {
			// 人工费
			if (feeManages.getFeeType() == 5) {
				for (Integer id : feeManages.getIds()) {
					// 通过ID查询信息
					FeeManage feeManage = feeManageResDao.queryEntityById(id);
					feeManageList.add(feeManage);
				}
			}
			// 管理费
			if (feeManages.getFeeType() == 4) {
				for (Integer id : feeManages.getIds()) {
					// 通过ID查询信息
					FeeManage feeManage = feeManageResDao.queryEntityById(id);
					manageFeeList.add(feeManage);
				}
			}
		}
		if (manageFeeList != null && manageFeeList.size() > 0) {
			// 管理费用分摊规则
			manageAmountShare(manageFeeList, shareDate);
		}
		if (feeManageList != null && feeManageList.size() > 0) {
			for (FeeManage feeManage : feeManageList) {
				// 主表总金额
				BigDecimal amount = queryResultsByFeeManage(feeManage);
				// 主表可用金额
				BigDecimal shareAmount1 = queryShareAmountByFeeManage(feeManage);
				BigDecimal shareAmo = DecimalUtil.subtract(amount, shareAmount1);
				// 头有人员无项目
				if (feeManage.getUserId() != null && feeManage.getProjectId() == null) {
					FeeShareReqDto feeShareReqDto = new FeeShareReqDto();
					feeShareReqDto.setManageId(feeManage.getId());
					List<FeeShare> feeShareList = feeShareDao.queryResultsByManageId(feeShareReqDto);
					if (feeShareList != null && feeShareList.size() > 0) {
						for (FeeShare feeShare : feeShareList) {
							if (feeShare.getShareUserId() != null && feeShare.getShareProjectId() == null) {
								List<BaseUserProject> baseUserProjectList = baseProjectDao
										.queryUserProjectAssignedToUser(feeShare.getShareUserId(),
												new RowBounds(0, 1000));
								// 费用分摊有人员且人员名下有项目：利润报表和绩效报表通过人员关联对应的项目按利润占比分摊，挂人工费用（自动分摊）
								if (baseUserProjectList != null && baseUserProjectList.size() > 0) {
									perManagePeport(feeManage, feeShare, feeShareList, amount, shareAmo, shareDate);
								}
							}
						}
					}
				}
				// 头表无人员无项目 费用分摊无人员无项目：利润报表和绩效报表取可分摊金额按部门所有项目利润占比分摊，挂人工费用（自动分摊）
				if (feeManage.getUserId() == null && feeManage.getProjectId() == null
						&& feeManage.getDepartmentId() != null) {
					// 通过部门找项目利润
					ProfitReportReqMonthDto profitReportReqMonthDto = new ProfitReportReqMonthDto();
					profitReportReqMonthDto.setDepartmentId(feeManage.getDepartmentId());
					String preDate = DateFormatUtils.format(DateFormatUtils.YYYYMM, shareDate);
					profitReportReqMonthDto.setStartStatisticsDate(preDate);
					profitReportReqMonthDto.setEndStatisticsDate(preDate);
					List<MounthProfitReport> pounthProfitReport = profitReportMonthDao
							.mounthProfitReportByFeeDeptId(profitReportReqMonthDto);
					BigDecimal profitAmount = BigDecimal.ZERO;
					if (pounthProfitReport != null && pounthProfitReport.size() > 0) {
						for (MounthProfitReport baseUserProject : pounthProfitReport) {
							BigDecimal amount2 = BigDecimal.ZERO;
							if (baseUserProject.getProfitAmount() == null
									|| "".equals(baseUserProject.getProfitAmount())) {
								amount2 = BigDecimal.ZERO;
							} else {
								if (Check.isNumber(baseUserProject.getProfitAmount().toString())) {
									amount2 = new BigDecimal(baseUserProject.getProfitAmount().toString());
								} else {
									amount2 = (new BigDecimal(baseUserProject.getProfitAmount().toString()));
								}
							}
							profitAmount = DecimalUtil.add(profitAmount, amount2);
						}
					}
					if (profitAmount.compareTo(BigDecimal.ZERO) == 1) {
						BigDecimal profitRate = BigDecimal.ZERO;
						for (MounthProfitReport baseProject : pounthProfitReport) {
							// 利润占比
							profitRate = DecimalUtil.divide(baseProject.getProfitAmount(), profitAmount).setScale(10,
									BigDecimal.ROUND_HALF_DOWN);
							if (profitRate.compareTo(BigDecimal.ZERO) == 1) {
								// 需要把无人员无项目，有人员无项目的数据删除
								FeeShareReqDto feeShareReqDto1 = new FeeShareReqDto();
								feeShareReqDto1.setManageId(feeManage.getId());
								List<FeeShare> feeShareList2 = feeShareDao.queryResultsByManageId(feeShareReqDto1);
								if (feeShareList2 != null && feeShareList2.size() > 0) {
									for (FeeShare feeShares1 : feeShareList2) {

										if (feeShares1.getShareUserId() != null
												&& feeShares1.getShareProjectId() == null) {
											List<BaseUserProject> baseUserProjectList = baseProjectDao
													.queryUserProjectAssignedToUser(feeShares1.getShareUserId(),
															new RowBounds(0, 1000));
											// 有人员且名下有项目
											if (baseUserProjectList != null && baseUserProjectList.size() > 0) {
												perSharePeport(feeShares1, feeManage, shareDate, amount);
											}
											// 有人员且名下无项目
											if (baseUserProjectList != null && baseUserProjectList.size() == 0) {
												/*
												 * //可用金额 = 主表总金额 - (无人员有项目 +
												 * 无人员无项目)明细有项目总金额 BigDecimal
												 * notShareAmount =
												 * DecimalUtil.subtract(amount,
												 * DecimalUtil.add(
												 * feeShareAmount,
												 * notFeeShareAmount));
												 * 
												 * BigDecimal isShareAmount =
												 * DecimalUtil.subtract(amount,
												 * notShareAmount); FeeShare
												 * feeShareIs =new FeeShare();
												 * feeShareIs.setIsDelete(1);
												 * feeShareIs.setId(feeShares1.
												 * getId());
												 * feeShareDao.updateById(
												 * feeShareIs);
												 * 
												 * //处理可分摊金额 FeeManage
												 * upFeeManage = new
												 * FeeManage();
												 * upFeeManage.setId(feeManage.
												 * getId()); //金额加密 try { byte[]
												 * inputData =
												 * isShareAmount.toString().
												 * getBytes();
												 * upFeeManage.setShareAmount(
												 * Coder.encryptBASE64(inputData
												 * )); } catch (Exception e) {
												 * throw new
												 * BaseException(ExcMsgEnum.
												 * ERROR_GENERAL, "添加失败:" +
												 * JSONObject.toJSON(feeShareIs)
												 * ); } updateFeeManageById(
												 * upFeeManage);
												 * 
												 * shareAmo =
												 * DecimalUtil.subtract(amount,
												 * isShareAmount);
												 */
											}
										}
									}
								}

								FeeShareReqDto feeShareReqDto2 = new FeeShareReqDto();
								feeShareReqDto2.setManageId(feeManage.getId());
								List<FeeShare> feeShareLists = feeShareDao.queryResultsByManageId(feeShareReqDto2);
								if (feeShareLists != null && feeShareLists.size() > 0) {
									BigDecimal feeShareAmount1 = queryResultsByUserId(feeShareList2);// 有人员无项目,明细总金额
									BigDecimal feeShareAmount2 = queryResultsByProjectId(feeShareList2);// 无人员有项目或者有人员有项目,明细总金额
									// BigDecimal notFeeShareAmount =
									// notAueryResultsByUserIdByProjectId(feeShareList2);//无人员无项目,明细总金额
									for (FeeShare feeShares1 : feeShareList2) {
										// 无人员无项目
										if (feeShares1.getShareUserId() == null
												&& feeShares1.getShareProjectId() == null) {
											BigDecimal notShareAmount = DecimalUtil.subtract(amount,
													DecimalUtil.add(feeShareAmount1, feeShareAmount2));

											BigDecimal isShareAmount = DecimalUtil.subtract(amount, notShareAmount);

											FeeShare feeShareIs = new FeeShare();
											feeShareIs.setIsDelete(1);
											feeShareIs.setId(feeShares1.getId());
											feeShareDao.updateById(feeShareIs);

											// 处理可分摊金额
											FeeManage upFeeManage = new FeeManage();
											upFeeManage.setId(feeManage.getId());
											// 金额加密
											try {
												byte[] inputData = isShareAmount.toString().getBytes();
												upFeeManage.setShareAmount(Coder.encryptBASE64(inputData));
											} catch (Exception e) {
												throw new BaseException(ExcMsgEnum.ERROR_GENERAL,
														"添加失败:" + JSONObject.toJSON(feeShareIs));
											}
											updateFeeManageById(upFeeManage);

											shareAmo = DecimalUtil.subtract(amount, isShareAmount);
										}
									}
								}

								BigDecimal feeShareAmount2 = queryResultsByProjectId(feeManage.getId());// 无人员有项目或者有人员有项目,明细总金额
								// 可分摊金额 = 主表总金额 - 明细有项目总金额
								BigDecimal notShareAmount2 = DecimalUtil.subtract(amount, feeShareAmount2).setScale(8,
										BigDecimal.ROUND_HALF_DOWN);

								// 项目需要分摊的金额 = 利润占比 * 可分摊金额
								BigDecimal amamountProfitount = DecimalUtil.multiply(profitRate, shareAmo).setScale(8,
										BigDecimal.ROUND_HALF_DOWN);

								Long num = notShareAmount2.longValue();
								Long num1 = amamountProfitount.longValue();
								if (notShareAmount2.compareTo(BigDecimal.ZERO) == 1
										&& (notShareAmount2.compareTo(amamountProfitount) == 1 || num - num1 > -1)
										&& (amamountProfitount.compareTo(BigDecimal.ZERO) == 1)) {
									FeeShare feeShares = new FeeShare();
									feeShares.setManageId(feeManage.getId());
									feeShares.setShareProjectId(baseProject.getProjectId());
									Date date = new Date();
									feeShares.setCreateAt(date);
									feeShares.setShareDate(shareDate);
									feeShares.setCreator(ServiceSupport.getUser() == null ? null
											: ServiceSupport.getUser().getChineseName());
									feeShares.setCreatorId(
											ServiceSupport.getUser() == null ? null : ServiceSupport.getUser().getId());
									if (amamountProfitount != null) {

										try {
											byte[] inputData = amamountProfitount.toString().getBytes();
											feeShares.setAmount(Coder.encryptBASE64(inputData));
										} catch (Exception e) {
											throw new BaseException(ExcMsgEnum.ERROR_GENERAL,
													"添加失败:" + JSONObject.toJSON(feeShares));
										}
										// BigDecimal amountFeeManage2 =
										// notAueryResultsByUserIdByProjectId(feeManage.getId());//明细总金额
										// 无人员无项目
										int result = feeShareDao.insert(feeShares);
										if (result <= 0) {
											throw new BaseException(ExcMsgEnum.ERROR_GENERAL,
													"添加失败:" + JSONObject.toJSON(feeShares));
										}
										FeeManage upFeeManage = new FeeManage();
										upFeeManage.setId(feeManage.getId());
										BigDecimal sum = DecimalUtil
												.subtract(amount,
														DecimalUtil.subtract(notShareAmount2, amamountProfitount))
												.setScale(2, BigDecimal.ROUND_HALF_DOWN);
										upFeeManage.setShareAmount(sum.toString());
										updateFeeManageById(upFeeManage);
									}
								}
							}
						}
					}
				}
				// 头表有项目 费用分摊有项目：利润报表和绩效报表按项目，挂人工费用
				if (feeManage.getProjectId() != null) {
					profitReportMounth(feeManage, amount, shareAmo, shareDate);
				}
			}
			// 对有人员且无项目的数据做最后处理
			feeManageByShareAmount(feeManageList);
		}
	}

	/**
	 * 头表有项目 费用分摊有项目：利润报表和绩效报表按项目
	 * 
	 * @param feeManage
	 * @param amount
	 * @param shareAmo
	 */
	public void profitReportMounth(FeeManage feeManage, BigDecimal amount, BigDecimal shareAmo, Date shareDate) {
		// 通过项目找利润
		ProfitReportReqMonthDto profitReportReqMonthDto = new ProfitReportReqMonthDto();
		profitReportReqMonthDto.setProjectId(feeManage.getProjectId());
		String preDate = DateFormatUtils.format(DateFormatUtils.YYYYMM, shareDate);
		profitReportReqMonthDto.setStartStatisticsDate(preDate);
		profitReportReqMonthDto.setEndStatisticsDate(preDate);
		List<MounthProfitReport> pounthProfitReport = profitReportMonthDao.queryResultsByCon(profitReportReqMonthDto);
		if (pounthProfitReport != null && pounthProfitReport.size() > 0) {
			for (MounthProfitReport profitReport : pounthProfitReport) {
				if (profitReport.getProfitAmount() != null
						&& profitReport.getProfitAmount().compareTo(new BigDecimal(1)) == 1) {
					FeeShareReqDto feeShareReqDto = new FeeShareReqDto();
					feeShareReqDto.setManageId(feeManage.getId());
					List<FeeShare> feeShareList = feeShareDao.queryResultsByManageId(feeShareReqDto);
					if (feeShareList != null && feeShareList.size() > 0) {
						BigDecimal feeShareAmount1 = queryResultsByProjectId(feeShareList);// 无人员有项目或者有人员有项目,明细总金额
						BigDecimal notFeeShareAmount = notAueryResultsByUserIdByProjectId(feeShareList);// 无人员无项目,明细总金额
						// 可用金额 = 主表总金额 - (无人员有项目 + 无人员无项目)明细有项目总金额
						BigDecimal notShareAmount = DecimalUtil
								.subtract(amount, DecimalUtil.add(feeShareAmount1, notFeeShareAmount))
								.setScale(8, BigDecimal.ROUND_HALF_DOWN);
						for (FeeShare feeShare : feeShareList) {
							// 有人员无项目的数据先删除 再按照比例生成
							if (feeShare.getShareUserId() != null && feeShare.getShareProjectId() == null) {
								BigDecimal isShareAmount = DecimalUtil.subtract(amount, notShareAmount);

								FeeShare feeShareIs = new FeeShare();
								feeShareIs.setIsDelete(1);
								feeShareIs.setId(feeShare.getId());
								feeShareDao.updateById(feeShareIs);

								// 处理可分摊金额
								FeeManage upFeeManage = new FeeManage();
								upFeeManage.setId(feeManage.getId());
								// 金额加密
								try {
									byte[] inputData = isShareAmount.toString().getBytes();
									upFeeManage.setShareAmount(Coder.encryptBASE64(inputData));
								} catch (Exception e) {
									throw new BaseException(ExcMsgEnum.ERROR_GENERAL,
											"添加失败:" + JSONObject.toJSON(feeShareIs));
								}
								updateFeeManageById(upFeeManage);

								shareAmo = DecimalUtil.subtract(amount, isShareAmount);
							}
						}
					}
				}
				// 项目需要分摊的金额 = 利润占比 * 可分摊金额
				BigDecimal amountProfit = DecimalUtil.multiply(BigDecimal.ONE, shareAmo).setScale(8,
						BigDecimal.ROUND_HALF_DOWN);
				Long num = shareAmo.longValue();
				Long num1 = amountProfit.longValue();
				// 检验是否有金额进行分摊
				if (shareAmo.compareTo(BigDecimal.ZERO) == 1
						&& (shareAmo.compareTo(amountProfit) == 1 || num - num1 > -1)) {
					FeeShare feeShares = new FeeShare();
					feeShares.setManageId(feeManage.getId());
					feeShares.setShareProjectId(profitReport.getProjectId());
					Date date = new Date();
					feeShares.setCreateAt(date);
					feeShares.setShareDate(shareDate);
					feeShares.setCreator(
							ServiceSupport.getUser() == null ? null : ServiceSupport.getUser().getChineseName());
					feeShares.setCreatorId(ServiceSupport.getUser() == null ? null : ServiceSupport.getUser().getId());
					// 金额加密
					try {
						byte[] inputData = amountProfit.toString().getBytes();
						feeShares.setAmount(Coder.encryptBASE64(inputData));
					} catch (Exception e) {
						throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "添加失败:" + JSONObject.toJSON(feeShares));
					}
					int result = feeShareDao.insert(feeShares);
					if (result <= 0) {
						throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "添加失败:" + JSONObject.toJSON(feeShares));
					}
					// 处理可分摊金额
					FeeManage upFeeManage = new FeeManage();
					upFeeManage.setId(feeManage.getId());
					BigDecimal sum = DecimalUtil.subtract(amount, DecimalUtil.subtract(shareAmo, amountProfit))
							.setScale(2, BigDecimal.ROUND_HALF_DOWN);
					upFeeManage.setShareAmount(sum.toString());
					updateFeeManageById(upFeeManage);
				}
			}
		}
	}

	/**
	 * 分摊明细有人员且名下有项目
	 */
	public void perSharePeport(FeeShare feeShare, FeeManage feeManage, Date shareDate, BigDecimal amountManage) {

		// 费用分摊有人员且人员名下有项目：利润报表和绩效报表通过人员关联对应的项目按利润占比分摊，挂人工费用（自动分摊）
		// 明细总金额
		BigDecimal amount = BigDecimal.ZERO;
		if (feeShare.getAmount() == null || "".equals(feeShare.getAmount())) {
			amount = BigDecimal.ZERO;
		} else {
			if (Check.isNumber(feeShare.getAmount())) {
				amount = new BigDecimal(feeShare.getAmount());
			} else {
				String amountString = "0.00";
				try {// 解密
					byte[] output = Coder.decryptBASE64(feeShare.getAmount());
					amountString = new String(output);
				} catch (Exception e) {
				}
				amount = new BigDecimal(new String(amountString));
			}
		}

		BigDecimal profitAmount = BigDecimal.ZERO;// 利润总金额
		BigDecimal profitRate = BigDecimal.ZERO;
		if (feeShare.getShareUserId() != null) {
			// 通过项目找利润
			ProfitReportReqMonthDto profitReportReqMonthDto = new ProfitReportReqMonthDto();
			profitReportReqMonthDto.setUserId(feeShare.getShareUserId());
			if (feeShare.getShareDate() != null) {
				shareDate = feeShare.getShareDate();
			}
			String preDate = DateFormatUtils.format(DateFormatUtils.YYYYMM, shareDate);
			profitReportReqMonthDto.setStartStatisticsDate(preDate);
			profitReportReqMonthDto.setEndStatisticsDate(preDate);
			List<MounthProfitReport> pounthProfitReport = profitReportMonthDao
					.mounthProfitReportByUserId(profitReportReqMonthDto);
			if (pounthProfitReport != null && pounthProfitReport.size() > 0) {
				for (MounthProfitReport mounthProfitReport : pounthProfitReport) {
					BigDecimal amount2 = BigDecimal.ZERO;
					if (mounthProfitReport.getProfitAmount() == null
							|| "".equals(mounthProfitReport.getProfitAmount())) {
						amount2 = BigDecimal.ZERO;
					} else {
						amount2 = new BigDecimal(mounthProfitReport.getProfitAmount().toString());
					}
					profitAmount = DecimalUtil.add(profitAmount, amount2);
				}
			}
			if (profitAmount.compareTo(BigDecimal.ZERO) == 1) {
				for (MounthProfitReport profitReport : pounthProfitReport) {
					if (profitReport.getProfitAmount() != null
							&& profitReport.getProfitAmount().compareTo(new BigDecimal(1)) == 1) {
						// 有人员无项目的数据先删除 再按照比例生成
						if (feeShare.getShareUserId() != null && feeShare.getShareProjectId() == null) {
							FeeShare feeShareIs = new FeeShare();
							feeShareIs.setIsDelete(1);
							feeShareIs.setId(feeShare.getId());
							feeShareDao.updateById(feeShareIs);
						}

						// 利润占比
						profitRate = DecimalUtil.divide(profitReport.getProfitAmount(), profitAmount).setScale(10,
								BigDecimal.ROUND_HALF_DOWN);
						// 项目需要分摊的金额 = 利润占比 * 可分摊金额
						BigDecimal amountProfit = DecimalUtil.multiply(profitRate, amount).setScale(8,
								BigDecimal.ROUND_HALF_DOWN);

						BigDecimal feeShareAmount = queryResultsByProjectId(feeShare.getManageId());// 无人员有项目或者有人员有项目,明细总金额
						BigDecimal feeShareAmount1 = queryResultsByUserId(feeShare.getManageId());// 有人员无项目,明细总金额

						BigDecimal isShareAmount = DecimalUtil.add(feeShareAmount, feeShareAmount1);
						// 可用金额
						BigDecimal notShareAmount = DecimalUtil.subtract(amountManage, isShareAmount);

						Long num = notShareAmount.longValue();
						Long num1 = amountProfit.longValue();
						// 检验是否有金额进行分摊
						if (notShareAmount.compareTo(BigDecimal.ZERO) == 1
								&& (notShareAmount.compareTo(amountProfit) == 1 || num - num1 > -1)) {
							FeeShare feeShares = new FeeShare();
							feeShares.setManageId(feeShare.getManageId());
							feeShares.setShareProjectId(profitReport.getProjectId());
							feeShares.setShareUserId(feeShare.getShareUserId());
							Date date = new Date();
							feeShares.setCreateAt(date);
							feeShares.setShareDate(shareDate);
							feeShares.setCreator(ServiceSupport.getUser() == null ? null
									: ServiceSupport.getUser().getChineseName());
							feeShares.setCreatorId(
									ServiceSupport.getUser() == null ? null : ServiceSupport.getUser().getId());
							// 金额加密
							try {
								byte[] inputData = amountProfit.toString().getBytes();
								feeShares.setAmount(Coder.encryptBASE64(inputData));
							} catch (Exception e) {
								throw new BaseException(ExcMsgEnum.ERROR_GENERAL,
										"添加失败:" + JSONObject.toJSON(feeShares));
							}
							int result = feeShareDao.insert(feeShares);
							if (result <= 0) {
								throw new BaseException(ExcMsgEnum.ERROR_GENERAL,
										"添加失败:" + JSONObject.toJSON(feeShares));
							}

						}
					}
				}
			}
		}
	}

	/**
	 * 对有人员且无项目的数据做最后处理
	 */
	public void feeManageByShareAmount(List<FeeManage> feeManageList) {
		// 最后对明细有人员且无项目的数据做处理
		for (FeeManage feeManage : feeManageList) {
			FeeShareReqDto feeShareReqDto1 = new FeeShareReqDto();
			feeShareReqDto1.setManageId(feeManage.getId());
			List<FeeShare> feeShareList2 = feeShareDao.queryResultsByManageId(feeShareReqDto1);
			if (feeShareList2 != null && feeShareList2.size() > 0) {
				for (FeeShare feeShares1 : feeShareList2) {
					if (feeShares1.getShareUserId() != null && feeShares1.getShareProjectId() == null) {
						List<BaseUserProject> baseUserProjectList = baseProjectDao
								.queryUserProjectAssignedToUser(feeShares1.getShareUserId(), new RowBounds(0, 1000));

						ProfitReportReqMonthDto profitReportReqMonthDto = new ProfitReportReqMonthDto();
						profitReportReqMonthDto.setUserId(feeShares1.getShareUserId());
						String preDate = DateFormatUtils.format(DateFormatUtils.YYYYMM, feeShares1.getShareDate());
						profitReportReqMonthDto.setStartStatisticsDate(preDate);
						profitReportReqMonthDto.setEndStatisticsDate(preDate);
						List<MounthProfitReport> pounthProfitReport = profitReportMonthDao
								.mounthProfitReportByUserId(profitReportReqMonthDto);

						// 有人员且名下无项目
						if ((baseUserProjectList != null && baseUserProjectList.size() == 0)
								|| (pounthProfitReport != null && pounthProfitReport.size() == 0)) {

							BigDecimal feeShareAmount = queryResultsByProjectId(feeShareList2);// 无人员有项目或者有人员有项目,明细总金额
							BigDecimal notFeeShareAmount = notAueryResultsByUserIdByProjectId(feeShareList2);// 无人员无项目,明细总金额
							BigDecimal hareAmount = queryResultsByUserId(feeManage.getId());// 有人员无项目

							BigDecimal notShareAmount = DecimalUtil.add(hareAmount,
									DecimalUtil.add(feeShareAmount, notFeeShareAmount));

							FeeManage upFeeManage = new FeeManage();
							upFeeManage.setId(feeManage.getId());
							upFeeManage.setShareAmount(notShareAmount.toString());
							updateFeeManageById(upFeeManage);
						}
					}
				}
			}
		}
	}
}
