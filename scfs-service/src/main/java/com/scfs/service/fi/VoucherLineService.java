package com.scfs.service.fi;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Maps;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.CacheKeyConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.OperateConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DateFormatUtils;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.fee.FeeDao;
import com.scfs.dao.fi.AccountLineDao;
import com.scfs.dao.fi.VoucherLineDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.domain.CodeValue;
import com.scfs.domain.base.entity.BaseDepartment;
import com.scfs.domain.base.entity.BaseProject;
import com.scfs.domain.base.entity.BaseSubject;
import com.scfs.domain.common.entity.AsyncExcel;
import com.scfs.domain.fee.entity.Fee;
import com.scfs.domain.fi.dto.req.VoucherLineSearchReqDto;
import com.scfs.domain.fi.dto.req.VoucherSearchReqDto;
import com.scfs.domain.fi.dto.resp.StandardCoinResDto;
import com.scfs.domain.fi.dto.resp.VoucherLineModelResDto;
import com.scfs.domain.fi.dto.resp.VoucherLineResDto;
import com.scfs.domain.fi.entity.AccountBook;
import com.scfs.domain.fi.entity.AccountLine;
import com.scfs.domain.fi.entity.VoucherLine;
import com.scfs.domain.fi.entity.VoucherLineModel;
import com.scfs.domain.interf.entity.PMSSupplierBind;
import com.scfs.domain.result.PageResult;
import com.scfs.service.base.exchangeRate.BaseExchangeRateService;
import com.scfs.service.common.AsyncExcelService;
import com.scfs.service.interf.PMSSupplierBindService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.FiCacheService;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 * 
 *  File: VoucherLineService.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年10月25日			Administrator
 *
 * </pre>
 */
@Service
public class VoucherLineService {

	@Autowired
	private VoucherLineDao voucherLineDao;
	@Autowired
	private FiCacheService fiCacheService;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private AccountLineDao accountLineDao;
	@Autowired
	private AsyncExcelService asyncExcelService;
	@Autowired
	private PMSSupplierBindService pMSSupplierBindService;
	@Autowired
	private FeeDao feeDao;
	@Autowired
	private BaseExchangeRateService baseExchangeRateService;

	public Integer createVoucherLine(VoucherLine voucherLine) {
		if (null == voucherLine.getAccountLineId()) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "科目不能为空");
		}
		AccountLine accountLine = accountLineDao.queryEntityById(voucherLine.getAccountLineId());
		if (accountLine == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, accountLineDao, voucherLine.getAccountLineId());
		}
		if (accountLine.getIsLast() == BaseConsts.ZERO) {
			throw new BaseException(ExcMsgEnum.ACCOUNT_LINE_NOT_LAST_LEVEL, accountLine.getAccountLineNo());
		}
		/**
		 * 1 : 需要辅助 0 : 不需要辅助
		 */
		if (!accountLine.getAccountLineNo().equals("660304")) { // 非损益科目需要校验辅助项
			if (voucherLine.getProjectId() != null) { // 服务水单内部项目不录入
				if (BaseConsts.ONE == accountLine.getNeedProject() && voucherLine.getProjectId() == null) {
					throw new BaseException(ExcMsgEnum.ASSIST_ITEM_NOT_EXIST,
							accountLine.getAccountLineNo() + "-" + accountLine.getAccountLineName(), "项目");
				}
			}
			if (BaseConsts.ONE == accountLine.getNeedAccount() && voucherLine.getAccountId() == null) {
				throw new BaseException(ExcMsgEnum.ASSIST_ITEM_NOT_EXIST,
						accountLine.getAccountLineNo() + "-" + accountLine.getAccountLineName(), "账号");
			}
			if (BaseConsts.ONE == accountLine.getNeedCust() && voucherLine.getCustId() == null) {
				throw new BaseException(ExcMsgEnum.ASSIST_ITEM_NOT_EXIST,
						accountLine.getAccountLineNo() + "-" + accountLine.getAccountLineName(), "客户");
			}
			if (BaseConsts.ONE == accountLine.getNeedSupplier() && voucherLine.getSupplierId() == null) {
				throw new BaseException(ExcMsgEnum.ASSIST_ITEM_NOT_EXIST,
						accountLine.getAccountLineNo() + "-" + accountLine.getAccountLineName(), "供应商");
			}
			if (BaseConsts.ONE == accountLine.getNeedUser() && voucherLine.getUserId() == null) {
				throw new BaseException(ExcMsgEnum.ASSIST_ITEM_NOT_EXIST,
						accountLine.getAccountLineNo() + "-" + accountLine.getAccountLineName(), "人员");
			}
			if (BaseConsts.ONE == accountLine.getNeedTaxRate() && voucherLine.getTaxRate() == null) {
				throw new BaseException(ExcMsgEnum.ASSIST_ITEM_NOT_EXIST,
						accountLine.getAccountLineNo() + "-" + accountLine.getAccountLineName(), "税率");
			}
			if (BaseConsts.ONE == accountLine.getNeedInnerBusiUnit() && voucherLine.getInnerBusiUnitId() == null) {
				throw new BaseException(ExcMsgEnum.ASSIST_ITEM_NOT_EXIST,
						accountLine.getAccountLineNo() + "-" + accountLine.getAccountLineName(), "内部经营单位");
			}
		}
		if (StringUtils.isEmpty(voucherLine.getVoucherLineSummary())) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "分录摘要不能为空");
		}
		voucherLine.setCreator(ServiceSupport.getUser() == null ? BaseConsts.SYSTEM_ROLE_NAME
				: ServiceSupport.getUser().getChineseName());
		voucherLine.setCreatorId(
				ServiceSupport.getUser() == null ? BaseConsts.SYSTEM_ROLE_ID : ServiceSupport.getUser().getId());
		voucherLineDao.insert(voucherLine);
		return voucherLine.getId();
	}

	public void updateVoucherLine(VoucherLine voucherLine) {
		AccountLine accountLine = accountLineDao.queryEntityById(voucherLine.getAccountLineId());
		if (accountLine == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, AccountLineDao.class, voucherLine.getAccountLineId());
		}
		if (accountLine.getIsLast() == BaseConsts.ZERO) {
			throw new BaseException(ExcMsgEnum.ACCOUNT_LINE_NOT_LAST_LEVEL, accountLine.getAccountLineNo());
		}
		/**
		 * 1 : 需要辅助 0 : 不需要辅助
		 */
		if (voucherLine.getProjectId() == null) {
			if (BaseConsts.ONE == accountLine.getNeedProject()) {
				throw new BaseException(ExcMsgEnum.ASSIST_ITEM_NOT_EXIST,
						accountLine.getAccountLineNo() + "-" + accountLine.getAccountLineName(), "项目");
			}
			voucherLine.setProjectId(-1);
		}
		if (voucherLine.getAccountId() == null) {
			if (BaseConsts.ONE == accountLine.getNeedAccount()) {
				throw new BaseException(ExcMsgEnum.ASSIST_ITEM_NOT_EXIST,
						accountLine.getAccountLineNo() + "-" + accountLine.getAccountLineName(), "账号");
			}
			voucherLine.setAccountId(-1);
		}
		if (voucherLine.getCustId() == null) {
			if (BaseConsts.ONE == accountLine.getNeedCust()) {
				throw new BaseException(ExcMsgEnum.ASSIST_ITEM_NOT_EXIST,
						accountLine.getAccountLineNo() + "-" + accountLine.getAccountLineName(), "客户");
			}
			voucherLine.setCustId(-1);
		}
		if (voucherLine.getSupplierId() == null) {
			if (BaseConsts.ONE == accountLine.getNeedSupplier()) {
				throw new BaseException(ExcMsgEnum.ASSIST_ITEM_NOT_EXIST,
						accountLine.getAccountLineNo() + "-" + accountLine.getAccountLineName(), "供应商");
			}
			voucherLine.setSupplierId(-1);
		}
		if (voucherLine.getUserId() == null) {
			if (BaseConsts.ONE == accountLine.getNeedUser()) {
				throw new BaseException(ExcMsgEnum.ASSIST_ITEM_NOT_EXIST,
						accountLine.getAccountLineNo() + "-" + accountLine.getAccountLineName(), "人员");
			}
			voucherLine.setUserId(-1);
		}
		if (voucherLine.getTaxRate() == null) {
			if (BaseConsts.ONE == accountLine.getNeedTaxRate()) {
				throw new BaseException(ExcMsgEnum.ASSIST_ITEM_NOT_EXIST,
						accountLine.getAccountLineNo() + "-" + accountLine.getAccountLineName(), "税率");
			}
			voucherLine.setTaxRate(DecimalUtil.ZERO);
		}
		if (voucherLine.getInnerBusiUnitId() == null) {
			if (BaseConsts.ONE == accountLine.getNeedInnerBusiUnit()) {
				throw new BaseException(ExcMsgEnum.ASSIST_ITEM_NOT_EXIST,
						accountLine.getAccountLineNo() + "-" + accountLine.getAccountLineName(), "内部经营单位");
			}
			voucherLine.setInnerBusiUnitId(-1);
		}
		if (StringUtils.isEmpty(voucherLine.getVoucherLineSummary())) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "分录摘要不能为空");
		}
		voucherLineDao.updateById(voucherLine);
	}

	// 对账管理(对账查询页面)，项目，客户，供应商，单据编号，单据日期为查询条件，项目，客户，供应商,币种组合键作为聚合条件查询对账总额
	public PageResult<VoucherLineResDto> queryGroupResultsByCon(VoucherLineSearchReqDto req) {
		req.setUserId(ServiceSupport.getUser().getId());
		req.setDebitOrCredit(BaseConsts.ONE);
		req.setStatisticsDimension(BaseConsts.THREE);
		PageResult<VoucherLineResDto> pageResult = new PageResult<VoucherLineResDto>();
		int offSet = PageUtil.getOffSet(req.getPage(), req.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, req.getPer_page());
		List<VoucherLine> voucherLines = voucherLineDao.queryGroupResultsByCon(req, rowBounds);
		List<VoucherLineResDto> resDtos = new ArrayList<VoucherLineResDto>();
		for (VoucherLine voucherLine : voucherLines) {
			VoucherLineResDto voucherResDto = convertToLineRes(voucherLine);
			voucherResDto.setOpertaList(getOperList(voucherResDto));
			resDtos.add(voucherResDto);
		}
		if (req.getNeedSum() != null && req.getNeedSum().equals(BaseConsts.ONE)) {
			List<VoucherLine> list = voucherLineDao.queryTotalGroupResultsByCon(req);
			BigDecimal amountSum = BigDecimal.ZERO;
			BigDecimal amountCheckedSum = BigDecimal.ZERO;
			BigDecimal amountUnCheckedSum = BigDecimal.ZERO;
			for (VoucherLine item : list) {
				amountSum = DecimalUtil.add(amountSum,
						ServiceSupport.amountNewToRMB(item.getAmount(), item.getCurrencyType(), new Date()));
				amountCheckedSum = DecimalUtil.add(amountCheckedSum,
						ServiceSupport.amountNewToRMB(item.getAmountChecked(), item.getCurrencyType(), new Date()));
			}
			amountUnCheckedSum = DecimalUtil.subtract(amountSum, amountCheckedSum);
			String totalStr = "对账总金额: " + DecimalUtil.toAmountString(amountSum) + " CNY   已对账金额: "
					+ DecimalUtil.toAmountString(amountCheckedSum) + " CNY   未对账金额: "
					+ DecimalUtil.toAmountString(amountUnCheckedSum) + " CNY";
			pageResult.setTotalStr(totalStr);
		}
		pageResult.setItems(resDtos);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), req.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(req.getPage());
		pageResult.setPer_page(req.getPer_page());
		return pageResult;
	}

	// 查询待对账分录
	public PageResult<VoucherLineModelResDto> queryLineCheckPageByCon(VoucherLineSearchReqDto req) {
		req.setUserId(ServiceSupport.getUser().getId());
		req.setDebitOrCredit(BaseConsts.ONE);
		req.setSearchType(BaseConsts.FOUR);
		PageResult<VoucherLineModelResDto> pageResult = new PageResult<VoucherLineModelResDto>();
		int offSet = PageUtil.getOffSet(req.getPage(), req.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, req.getPer_page());
		List<VoucherLine> voucherLines = voucherLineDao.queryLineResultsByCon(req, rowBounds);
		List<VoucherLineModelResDto> modelResDtos = new ArrayList<VoucherLineModelResDto>();
		for (VoucherLine voucherLine : voucherLines) {
			VoucherLineModelResDto voucherResDto = convertToLineModelRes(voucherLine);
			modelResDtos.add(voucherResDto);
		}
		pageResult.setItems(modelResDtos);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), req.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(req.getPage());
		pageResult.setPer_page(req.getPer_page());
		return pageResult;
	}

	// 查询待对账分录,不分页
	public List<VoucherLineModelResDto> queryLineCheckResultsByCon(VoucherLineSearchReqDto req) {
		req.setUserId(ServiceSupport.getUser().getId());
		req.setDebitOrCredit(BaseConsts.ONE);
		req.setSearchType(BaseConsts.FOUR);
		List<VoucherLine> voucherLines = voucherLineDao.queryLineResultsByCon(req);
		List<VoucherLineModelResDto> modelResDtos = new ArrayList<VoucherLineModelResDto>();
		for (VoucherLine voucherLine : voucherLines) {
			VoucherLineModelResDto voucherResDto = convertToLineModelRes(voucherLine);
			modelResDtos.add(voucherResDto);
		}
		return modelResDtos;
	}

	// 根据项目，客户，供应商，单据编号，单据日期查询分录列表
	public PageResult<VoucherLineModelResDto> queryLineResultsByCon(VoucherLineSearchReqDto req) {
		req.setUserId(ServiceSupport.getUser().getId());
		PageResult<VoucherLineModelResDto> pageResult = new PageResult<VoucherLineModelResDto>();
		int offSet = PageUtil.getOffSet(req.getPage(), req.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, req.getPer_page());
		List<VoucherLine> voucherLines = voucherLineDao.queryLineResultsByCon(req, rowBounds);
		List<VoucherLineModelResDto> modelResDtos = new ArrayList<VoucherLineModelResDto>();
		for (VoucherLine voucherLine : voucherLines) {
			VoucherLineModelResDto voucherResDto = convertToLineModelRes(voucherLine);
			modelResDtos.add(voucherResDto);
		}
		pageResult.setItems(modelResDtos);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), req.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(req.getPage());
		pageResult.setPer_page(req.getPer_page());
		return pageResult;
	}

	public List<VoucherLineModelResDto> queryResultsByVoucherId(Integer voucherId) {
		List<VoucherLineModelResDto> resDtos = new ArrayList<VoucherLineModelResDto>();
		List<VoucherLine> voucherLines = voucherLineDao.queryResultsByVoucherId(voucherId);
		for (VoucherLine voucherLine : voucherLines) {
			VoucherLineModelResDto voucherResDto = convertToLineModelRes(voucherLine);
			resDtos.add(voucherResDto);
		}
		return resDtos;
	}

	public VoucherLine queryEntityById(Integer id) {
		VoucherLine voucherLine = voucherLineDao.queryEntityById(id);
		return voucherLine;
	}

	public VoucherLineModelResDto queryEntityDtoById(Integer id) {
		VoucherLineModelResDto voucherLine = convertToLineModelRes(voucherLineDao.queryEntityById(id));
		return voucherLine;
	}

	public List<VoucherLineModelResDto> queryEntityByIds(VoucherLineSearchReqDto voucherLineSearchReqDto) {
		List<VoucherLine> voucherLines = voucherLineDao.queryEntityByIds(voucherLineSearchReqDto);
		List<VoucherLineModelResDto> modelResDtos = new ArrayList<VoucherLineModelResDto>();
		for (VoucherLine voucherLine : voucherLines) {
			VoucherLineModelResDto voucherResDto = convertToLineModelRes(voucherLine);
			modelResDtos.add(voucherResDto);
		}
		return modelResDtos;
	}

	private VoucherLineModelResDto convertToLineModelRes(VoucherLine line) {
		VoucherLineModelResDto resDto = new VoucherLineModelResDto();
		BeanUtils.copyProperties(line, resDto);
		resDto.setAccountLineName(fiCacheService.getAlNameById(line.getAccountLineId()));
		if (null != line.getCurrencyType()) {
			resDto.setCurrencyTypeName(
					ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE, line.getCurrencyType() + ""));
		}
		resDto.setCustName(cacheService.getSubjectNcByIdAndKey(line.getCustId(), CacheKeyConsts.CUSTOMER));
		resDto.setDebitOrCreditName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.DEBIT_OR_CREDIT, line.getDebitOrCredit() + ""));
		resDto.setProjectName(cacheService.getProjectNameById(line.getProjectId()));
		resDto.setSupplierName(cacheService.getSubjectNcByIdAndKey(line.getSupplierId(), CacheKeyConsts.SUPPLIER));
		resDto.setAccountNo(cacheService.getAccountNoById(line.getAccountId()));
		resDto.setUserName(cacheService.getUserChineseNameByid(line.getUserId()));
		resDto.setInnerBusiUnitName(
				cacheService.getSubjectNcByIdAndKey(line.getInnerBusiUnitId(), CacheKeyConsts.BUSI_UNIT));
		resDto.setBusiUnitName(cacheService.getSubjectNcByIdAndKey(line.getBusiUnit(), CacheKeyConsts.BUSI_UNIT));
		resDto.setAmountUnChecked(DecimalUtil.subtract(line.getAmount(), line.getAmountChecked()));
		AccountLine accountLine = cacheService.getAccountLineById(line.getAccountLineId());
		if (null == accountLine) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, AccountLine.class, line.getAccountLineId());
		}
		resDto.setNeedAccount(accountLine.getNeedAccount());
		resDto.setNeedCust(accountLine.getNeedCust());
		resDto.setNeedProject(accountLine.getNeedProject());
		resDto.setNeedSupplier(accountLine.getNeedSupplier());
		resDto.setNeedTaxRate(accountLine.getNeedTaxRate());
		resDto.setNeedUser(accountLine.getNeedUser());
		resDto.setNeedInnerBusiUnit(accountLine.getNeedInnerBusiUnit());
		resDto.setAccountLineNo(accountLine.getAccountLineNo());
		if (null != resDto.getBillType()) {
			resDto.setBillTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.BILL_TYPE, line.getBillType() + ""));
			if (resDto.getBillType().equals(BaseConsts.ONE)) {
				Fee fee = feeDao.queryEntityById(resDto.getFeeId());
				resDto.setFeeType(fee.getFeeType());
				resDto.setBillTypeName(resDto.getBillTypeName() + "("
						+ ServiceSupport.getValueByBizCode(BizCodeConsts.FEE_TYPE, fee.getFeeType() + "") + ")");
				resDto.setRecDate(fee.getRecDate());
			}
		}
		if (line.getBillDate() != null) {
			resDto.setBillDateStr(DateFormatUtils.format(line.getBillDate(), DateFormatUtils.YYYY_MM_DD));
		}
		if (BaseConsts.ONE == line.getDebitOrCredit()) { // 1是借方 2是贷方
			resDto.setDebitAmount(DecimalUtil.eq(line.getAmount(), BigDecimal.ZERO) ? null : line.getAmount());
			resDto.setStandardDebitAmount(line.getStandardAmount());
		} else if (BaseConsts.TWO == line.getDebitOrCredit()) {
			resDto.setCreditAmount(DecimalUtil.eq(line.getAmount(), BigDecimal.ZERO) ? null : line.getAmount());
			resDto.setStandardCreditAmount(line.getStandardAmount());
		}
		if (line.getStandardCoin() != null) {
			resDto.setStandardCoinName(
					ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE, line.getStandardCoin() + ""));
		} else {
			resDto.setStandardCoinName("");
		}

		return resDto;
	}

	private VoucherLineResDto convertToLineRes(VoucherLine line) {
		VoucherLineResDto resDto = new VoucherLineResDto();
		resDto.setProjectId(line.getProjectId());
		resDto.setCustId(line.getCustId());
		resDto.setBusiUnit(line.getBusiUnit());
		resDto.setCurrencyType(line.getCurrencyType());
		resDto.setCustName(cacheService.getSubjectNcByIdAndKey(line.getCustId(), CacheKeyConsts.CUSTOMER));
		resDto.setProjectName(cacheService.getProjectNameById(line.getProjectId()));
		resDto.setBusiUnitName(cacheService.getSubjectNcByIdAndKey(line.getBusiUnit(), CacheKeyConsts.BUSI_UNIT));
		resDto.setCurrencyTypeName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE, line.getCurrencyType() + ""));
		resDto.setAmount(line.getAmount());
		resDto.setAmountChecked(line.getAmountChecked());
		resDto.setAmountUnChecked(DecimalUtil.subtract(line.getAmount(), line.getAmountChecked()));
		String aCreateDate = DateFormatUtils.format(line.getMinCreateAt(), DateFormatUtils.YYYY_MM_DD);
		String eCreateDate = DateFormatUtils.format(line.getMaxCreateAt(), DateFormatUtils.YYYY_MM_DD);
		resDto.setCreateDateRange(aCreateDate + "~" + eCreateDate);
		return resDto;
	}

	/**
	 * 获取操作列表，对账和浏览
	 * 
	 * @param state
	 * @return
	 */
	private List<CodeValue> getOperList(VoucherLineResDto voucherLineResDto) {
		List<String> operNameList = new ArrayList<String>();
		List<CodeValue> oprResult = new ArrayList<CodeValue>();
		if (DecimalUtil.eq(voucherLineResDto.getAmount(), voucherLineResDto.getAmountChecked())) {
			return oprResult;
		}
		operNameList.add(OperateConsts.CHECK_BILL);
		operNameList.add(OperateConsts.DETAIL);
		// 将上述的操作列表通过权限过滤
		oprResult = ServiceSupport.getOperListByPermission(operNameList, VoucherLineResDto.Operate.operMap);
		return oprResult;
	}

	public List<VoucherLineModelResDto> queryVoucherLineModelList(VoucherSearchReqDto voucherSearchReqDto) {
		if (null == voucherSearchReqDto.getUserId()) {
			voucherSearchReqDto.setUserId(ServiceSupport.getUser().getId());
		}
		List<VoucherLineModel> voucherLineModelList = voucherLineDao.queryLineResultsByVoucherCon(voucherSearchReqDto);
		List<VoucherLineModelResDto> voucherLineModelResDtoList = convertToExtResDto(voucherLineModelList);
		return voucherLineModelResDtoList;
	}

	public List<VoucherLineModelResDto> convertToExtResDto(List<VoucherLineModel> voucherLineExtlist) {
		List<VoucherLineModelResDto> voucherLineModelResDtoList = Lists.newArrayList();
		if (CollectionUtils.isEmpty(voucherLineExtlist)) {
			return voucherLineModelResDtoList;
		}
		for (VoucherLineModel voucherLineExt : voucherLineExtlist) {
			VoucherLineModelResDto voucherLineModelResDto = convertToExtResDto(voucherLineExt);
			voucherLineModelResDtoList.add(voucherLineModelResDto);
		}
		return voucherLineModelResDtoList;
	}

	public VoucherLineModelResDto convertToExtResDto(VoucherLineModel voucherLineModel) {
		VoucherLineModelResDto resDto = new VoucherLineModelResDto();
		BeanUtils.copyProperties(voucherLineModel, resDto);
		resDto.setAccountLineName(fiCacheService.getAlNameById(voucherLineModel.getAccountLineId()));
		if (null != voucherLineModel.getCurrencyType()) {
			resDto.setCurrencyTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
					voucherLineModel.getCurrencyType() + ""));
		}
		resDto.setCustName(cacheService.getSubjectNcByIdAndKey(voucherLineModel.getCustId(), CacheKeyConsts.CUSTOMER));
		resDto.setDebitOrCreditName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEBIT_OR_CREDIT,
				voucherLineModel.getDebitOrCredit() + ""));
		resDto.setProjectName(cacheService.getProjectNameById(voucherLineModel.getProjectId()));
		PMSSupplierBind pMSSupplierBind = pMSSupplierBindService
				.queryEntityByProjectIdAndSupplierId(voucherLineModel.getProjectId(), voucherLineModel.getSupplierId());
		if (null != pMSSupplierBind) { // 若供应商已绑定PMS供应商，取PMS供应商编码
			resDto.setSupplierNo(pMSSupplierBind.getPmsSupplierNo());
			resDto.setSupplierName(pMSSupplierBind.getPmsSupplierNo());
		} else {
			BaseSubject baseSubject = cacheService.getSubjectById(voucherLineModel.getSupplierId(),
					CacheKeyConsts.SUPPLIER);
			if (null != baseSubject) {
				resDto.setSupplierNo(baseSubject.getSubjectNo());
			}
			resDto.setSupplierName(
					cacheService.getSubjectNcByIdAndKey(voucherLineModel.getSupplierId(), CacheKeyConsts.SUPPLIER));
		}
		resDto.setAccountNo(cacheService.getAccountNoById(voucherLineModel.getAccountId()));
		resDto.setUserName(cacheService.getUserChineseNameByid(voucherLineModel.getUserId()));
		resDto.setInnerBusiUnitName(
				cacheService.getSubjectNcByIdAndKey(voucherLineModel.getInnerBusiUnitId(), CacheKeyConsts.BUSI_UNIT));
		resDto.setBusiUnitName(
				cacheService.getSubjectNcByIdAndKey(voucherLineModel.getBusiUnit(), CacheKeyConsts.BUSI_UNIT));
		resDto.setAmountUnChecked(
				DecimalUtil.subtract(voucherLineModel.getAmount(), voucherLineModel.getAmountChecked()));
		AccountLine accountLine = accountLineDao.queryEntityById(voucherLineModel.getAccountLineId());
		resDto.setNeedAccount(accountLine.getNeedAccount());
		resDto.setNeedCust(accountLine.getNeedCust());
		resDto.setNeedProject(accountLine.getNeedProject());
		resDto.setNeedSupplier(accountLine.getNeedSupplier());
		resDto.setNeedTaxRate(accountLine.getNeedTaxRate());
		resDto.setNeedUser(accountLine.getNeedUser());
		resDto.setNeedInnerBusiUnit(accountLine.getNeedInnerBusiUnit());
		resDto.setAccountLineNo(accountLine.getAccountLineNo());
		resDto.setBillTypeName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.BILL_TYPE, voucherLineModel.getBillType() + ""));
		if (voucherLineModel.getBillDate() != null) {
			resDto.setBillDateStr(DateFormatUtils.format(voucherLineModel.getBillDate(), DateFormatUtils.YYYY_MM_DD));
		}
		if (BaseConsts.ONE == voucherLineModel.getDebitOrCredit()) { // 1是借方
																		// 2是贷方
			resDto.setDebitAmount(
					null == voucherLineModel.getAmount() ? BigDecimal.ZERO : voucherLineModel.getAmount());
			resDto.setStandardDebitAmount(null == voucherLineModel.getStandardAmount() ? BigDecimal.ZERO
					: voucherLineModel.getStandardAmount());
			resDto.setCreditAmount(BigDecimal.ZERO);
			resDto.setStandardCreditAmount(BigDecimal.ZERO);
		} else if (BaseConsts.TWO == voucherLineModel.getDebitOrCredit()) {
			resDto.setDebitAmount(BigDecimal.ZERO);
			resDto.setStandardDebitAmount(BigDecimal.ZERO);
			resDto.setCreditAmount(
					null == voucherLineModel.getAmount() ? BigDecimal.ZERO : voucherLineModel.getAmount());
			resDto.setStandardCreditAmount(null == voucherLineModel.getStandardAmount() ? BigDecimal.ZERO
					: voucherLineModel.getStandardAmount());
		}

		String assistDec = "";
		if (resDto.getNeedProject() == BaseConsts.ONE && !StringUtils.isEmpty(resDto.getProjectName())) {
			assistDec += "项目: " + resDto.getProjectName() + ";";
		}
		if (resDto.getNeedSupplier() == BaseConsts.ONE && !StringUtils.isEmpty(resDto.getSupplierName())) {
			assistDec += "供应商: " + resDto.getSupplierName() + ";";
		}
		if (resDto.getNeedCust() == BaseConsts.ONE && !StringUtils.isEmpty(resDto.getCustName())) {
			assistDec += "客户: " + resDto.getCustName() + ";";
		}
		if (resDto.getNeedAccount() == BaseConsts.ONE && !StringUtils.isEmpty(resDto.getAccountNo())) {
			assistDec += "账号: " + resDto.getAccountNo() + ";";
		}
		if (resDto.getNeedUser() == BaseConsts.ONE && !StringUtils.isEmpty(resDto.getUserName())) {
			assistDec += "用户: " + resDto.getUserName() + ";";
		}
		if (resDto.getNeedTaxRate() == BaseConsts.ONE && null != resDto.getTaxRate()) {
			assistDec += "税率: " + resDto.getTaxRate() + ";";
		}
		resDto.setAssistDec(assistDec);

		/** 账套信息 **/
		AccountBook accountBook = fiCacheService.getAbById(voucherLineModel.getAccountBookId());
		resDto.setAccountBookName(fiCacheService.getAbNameById(accountBook));
		if (accountBook.getStandardCoin() != null) {
			resDto.setStandardCoinName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
					accountBook.getStandardCoin() + ""));
		}
		BaseProject baseProject = cacheService.getProjectById(voucherLineModel.getProjectId());
		if (null != baseProject && null != baseProject.getDepartmentId()) {
			BaseDepartment baseDepartment = cacheService.getBaseDepartmentById(baseProject.getDepartmentId());
			if (null != baseDepartment) {
				resDto.setDepartmentName(baseDepartment.getName());
			}
		}
		return resDto;
	}

	public boolean isOverVoucherLineMaxLine(VoucherSearchReqDto voucherSearchReqDto) {
		voucherSearchReqDto.setUserId(ServiceSupport.getUser().getId());
		int count = voucherLineDao.queryLineCountByVoucherCon(voucherSearchReqDto);
		if (count >= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			// 后台导出
			AsyncExcel asyncExcel = new AsyncExcel();
			asyncExcel.setName("凭证明细导出");
			asyncExcel.setClassName(this.getClass().getName());// 导出类名
			asyncExcel.setMethodName("asyncVoucherLineExport");// 导出方法
			asyncExcel.setTemplatePath("/WEB-INF/excel/export/fi/voucher_line_list.xls");// 导出模板路径
			asyncExcel.setPoType(BaseConsts.INT_21);
			asyncExcelService.addAsyncExcel(voucherSearchReqDto, asyncExcel);
			return true;
		}
		return false;
	}

	public Map<String, Object> asyncVoucherLineExport(VoucherSearchReqDto voucherSearchReqDto) {
		Map<String, Object> model = Maps.newHashMap();
		List<VoucherLineModelResDto> voucherLineModelResDtoList = queryVoucherLineModelList(voucherSearchReqDto);
		model.put("voucherLineList", voucherLineModelResDtoList);
		return model;
	}

	public StandardCoinResDto queryStandardCoinInfo(VoucherLineSearchReqDto voucherLineSearchReqDto) {
		StandardCoinResDto standardCoinResDto = new StandardCoinResDto();

		Integer accountBookId = voucherLineSearchReqDto.getAccountBookId();
		Date voucherDate = voucherLineSearchReqDto.getVoucherDate();
		BigDecimal debitAmount = voucherLineSearchReqDto.getDebitAmount();
		BigDecimal creditAmount = voucherLineSearchReqDto.getCreditAmount();
		Integer currencyType = voucherLineSearchReqDto.getCurrencyType();
		if (null == accountBookId) {
			return standardCoinResDto;
		}
		if (null == voucherDate) {
			return standardCoinResDto;
		}
		if (null == currencyType) {
			return standardCoinResDto;
		}
		AccountBook accountBook = cacheService.getAccountBookById(accountBookId);
		if (accountBook.getId() == null || accountBook.getIsDelete().equals(BaseConsts.ONE)
				|| !accountBook.getState().equals(BaseConsts.TWO)) {
			throw new BaseException(ExcMsgEnum.ACCOUNT_BOOK_NOT_IN_USE, accountBookId);
		}
		Integer standardCoin = accountBook.getStandardCoin();
		String standardCoinName = ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
				Integer.toString(standardCoin));
		
		BigDecimal currencyRate = baseExchangeRateService.convertCurrency(String.valueOf(BaseConsts.TWO),
				String.valueOf(currencyType), String.valueOf(standardCoin), voucherDate)
				.setScale(8, BigDecimal.ROUND_HALF_UP);
		if (null != debitAmount) {
			BigDecimal standardDebitAmount = DecimalUtil.formatScale2(DecimalUtil.multiply(debitAmount, currencyRate));
			standardCoinResDto.setStandardDebitAmount(standardDebitAmount);
		}
		if (null != creditAmount) {
			BigDecimal standardCreditAmount = DecimalUtil
					.formatScale2(DecimalUtil.multiply(creditAmount, currencyRate));
			standardCoinResDto.setStandardCreditAmount(standardCreditAmount);
		}
		standardCoinResDto.setStandardCoin(standardCoin);
		standardCoinResDto.setStandardCoinName(standardCoinName);
		standardCoinResDto.setStandardRate(currencyRate);

		return standardCoinResDto;
	}
}
