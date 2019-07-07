package com.scfs.service.finance;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.testng.collections.Lists;

import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.CacheKeyConsts;
import com.scfs.common.consts.OperateConsts;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.ListUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.fee.FeeDao;
import com.scfs.dao.fi.VoucherLineDao;
import com.scfs.dao.finance.CopeManageDao;
import com.scfs.dao.finance.CopeManageDtlDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.domain.CodeValue;
import com.scfs.domain.fee.entity.Fee;
import com.scfs.domain.fi.entity.VoucherLine;
import com.scfs.domain.finance.cope.dto.req.CopeManageReqDto;
import com.scfs.domain.finance.cope.dto.resq.CopeManageResDto;
import com.scfs.domain.finance.cope.entity.CopeManage;
import com.scfs.domain.finance.cope.entity.CopeManageDtl;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 *  应付管理
 *  File: CopeManageService.java
 *  Description:
 *  TODO
 *  Date,                   Who,
 *  2017年10月31日         Administrator
 *
 * </pre>
 */
@Service
public class CopeManageService {
	@Autowired
	private CopeManageDao copeManageDao;
	@Autowired
	private CopeManageDtlDao copeManageDtlDao;
	@Autowired
	private FeeDao feeDao;
	@Autowired
	private VoucherLineDao voucherLineDao;// 凭证明细
	@Autowired
	private CacheService cacheService;

	/**
	 * 
	 * 应付费用审核通过添加相关数据
	 * 
	 * @param feeId
	 */
	public void saveCopeManage(Integer feeId) {
		Fee fee = feeDao.queryEntityById(feeId);
		if (fee.getFeeType().equals(BaseConsts.TWO)) {// 应付费用类型
			List<VoucherLine> voucherLines = voucherLineDao.queryLineResultsByFeeId(feeId);
			if (!CollectionUtils.isEmpty(voucherLines)) {// CR为2202
				for (VoucherLine voucherLine : voucherLines) {
					Date currDate = new Date();
					BigDecimal billAmount = fee.getPayAmount();// 单据金额
					BigDecimal copeAmount = fee.getPayAmount();// 应付金额(核销金额)
					BigDecimal paidAmount = fee.getPaidAmount();// 已付金额
					BigDecimal unpaidAmount = DecimalUtil.subtract(copeAmount, paidAmount);// 未付金额
					Integer projectId = voucherLine.getProjectId();
					Integer busiUnitId = voucherLine.getBusiUnit();
					Integer custId = voucherLine.getCustId();
					Integer currnecyType = voucherLine.getCurrencyType();
					String billNo = voucherLine.getBillNo();

					CopeManageReqDto reqDto = new CopeManageReqDto();
					reqDto.setBillNumber(billNo);
					Integer copeId = null;
					List<CopeManageDtl> copeManageDtlList = copeManageDtlDao.queryResultsByCon(reqDto);
					if (CollectionUtils.isEmpty(copeManageDtlList)) {// 不存在，添加应付管理
						CopeManage model = new CopeManage();
						model.setCreateAt(currDate);
						model.setProjectId(projectId);
						model.setBusiUnitId(busiUnitId);
						model.setCustomerId(custId);
						model.setCurrnecyType(currnecyType);
						model.setCreatorId(ServiceSupport.getUser() == null ? null : ServiceSupport.getUser().getId());
						model.setCopeAmount(copeAmount);
						model.setPaidAmount(paidAmount);
						model.setUnpaidAmount(unpaidAmount);
						copeManageDao.insert(model);
						copeId = model.getId();

						CopeManageDtl copeManageDtl = new CopeManageDtl();
						copeManageDtl.setCopeId(copeId);
						copeManageDtl.setBillId(feeId);
						copeManageDtl.setVoucherLineId(voucherLine.getId());
						copeManageDtl.setProjectId(projectId);
						copeManageDtl.setBusiUnitId(busiUnitId);
						copeManageDtl.setCustomerId(custId);
						copeManageDtl.setCopeDtlType(BaseConsts.ONE);
						copeManageDtl.setCurrnecyType(currnecyType);
						copeManageDtl.setBillNumber(billNo);
						copeManageDtl.setBillDate(voucherLine.getBillDate());
						copeManageDtl.setBillAmount(billAmount);
						copeManageDtl.setCopeAmount(copeAmount);
						copeManageDtl.setPaidAmount(paidAmount);
						copeManageDtl.setUnpaidAmount(unpaidAmount);
						copeManageDtl.setCreateAt(currDate);
						copeManageDtl.setCreatorId(
								ServiceSupport.getUser() == null ? null : ServiceSupport.getUser().getId());
						copeManageDtlDao.insert(copeManageDtl);
					} else {
						CopeManageDtl model = copeManageDtlList.get(BaseConsts.ZERO);
						CopeManageDtl upCopeDtl = new CopeManageDtl();
						upCopeDtl.setId(copeId);
						upCopeDtl.setCopeAmount(DecimalUtil.add(model.getCopeAmount(), copeAmount));
						upCopeDtl.setPaidAmount(DecimalUtil.add(model.getPaidAmount(), paidAmount));
						upCopeDtl.setUnpaidAmount(DecimalUtil.add(model.getUnpaidAmount(), unpaidAmount));

						copeId = model.getCopeId();
						CopeManage upCopeManage = new CopeManage();
						upCopeManage.setId(copeId);
						upCopeManage.setCopeAmount(DecimalUtil.add(model.getCopeAmount(), copeAmount));
						upCopeManage.setPaidAmount(DecimalUtil.add(model.getPaidAmount(), paidAmount));
						upCopeManage.setUnpaidAmount(DecimalUtil.add(model.getUnpaidAmount(), unpaidAmount));
						copeManageDao.updateById(upCopeManage);
					}
				}
			}
		}
	}

	/**
	 * 获取列表数据
	 * 
	 * @param reqDto
	 * @return
	 */
	public PageResult<CopeManageResDto> queryCopeManageResults(CopeManageReqDto reqDto) {
		PageResult<CopeManageResDto> result = new PageResult<CopeManageResDto>();
		int offSet = PageUtil.getOffSet(reqDto.getPage(), reqDto.getPer_page());
		reqDto.setUserId(ServiceSupport.getUser().getId());
		RowBounds rowBounds = new RowBounds(offSet, reqDto.getPer_page());
		List<CopeManageResDto> copeManageResDtos = convertToResDtos(copeManageDao.queryResultsByCon(reqDto, rowBounds));
		if (reqDto.getNeedSum() != null && reqDto.getNeedSum() == BaseConsts.ONE) {// 1
			List<CopeManage> copeManages = copeManageDao.queryResultsByCon(reqDto);
			if (!CollectionUtils.isEmpty(copeManages)) {
				BigDecimal copeAmount = BigDecimal.ZERO;
				BigDecimal paidAmount = BigDecimal.ZERO;
				BigDecimal unpaidAmount = BigDecimal.ZERO;
				for (CopeManage copeManage : copeManages) {
					BigDecimal rmbCopeAmount = ServiceSupport.amountNewToRMB(copeManage.getCopeAmount(),
							copeManage.getCurrnecyType(), null);
					copeAmount = DecimalUtil.add(copeAmount, rmbCopeAmount);

					BigDecimal rmbPaidAmount = ServiceSupport.amountNewToRMB(copeManage.getPaidAmount(),
							copeManage.getCurrnecyType(), null);
					paidAmount = DecimalUtil.add(paidAmount, rmbPaidAmount);

					BigDecimal rmbUnpaidAmount = ServiceSupport.amountNewToRMB(copeManage.getUnpaidAmount(),
							copeManage.getCurrnecyType(), null);
					unpaidAmount = DecimalUtil.add(unpaidAmount, rmbUnpaidAmount);
				}
				String totalStr = "应付金额：" + DecimalUtil.toAmountString(copeAmount) + " CNY &nbsp;&nbsp;已付金额："
						+ DecimalUtil.toAmountString(paidAmount) + " CNY &nbsp;&nbsp;未付金额："
						+ DecimalUtil.toAmountString(unpaidAmount) + " CNY &nbsp;";
				result.setTotalStr(totalStr);
			}
		}
		result.setItems(copeManageResDtos);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), reqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(reqDto.getPage());
		result.setPer_page(reqDto.getPer_page());
		return result;
	}

	/**
	 * 获取浏览详情
	 * 
	 * @param copeManage
	 * @return
	 */
	public Result<CopeManageResDto> detailCopeManage(CopeManage copeManage) {
		Result<CopeManageResDto> result = new Result<CopeManageResDto>();
		CopeManageResDto model = convertResDto(copeManageDao.queryEntityById(copeManage.getId()));
		result.setItems(model);
		return result;
	}

	public List<CopeManageResDto> convertToResDtos(List<CopeManage> result) {
		List<CopeManageResDto> resDtos = new ArrayList<CopeManageResDto>();
		if (ListUtil.isEmpty(result)) {
			return resDtos;
		}
		for (CopeManage model : result) {
			CopeManageResDto resDto = convertResDto(model);
			// 操作集合
			List<CodeValue> operList = getOperList();
			resDto.setOpertaList(operList);
			resDtos.add(resDto);
		}
		return resDtos;
	}

	private List<CodeValue> getOperList() {
		// 1.根据状态得到操作列表
		List<String> operNameList = getOperListByState();
		// 2.将上述的操作列表通过权限过滤
		List<CodeValue> oprResult = ServiceSupport.getOperListByPermission(operNameList,
				CopeManageResDto.Operate.operMap);
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
		opertaList.add(OperateConsts.DETAIL);
		return opertaList;
	}

	public CopeManageResDto convertResDto(CopeManage model) {
		CopeManageResDto result = new CopeManageResDto();
		result.setId(model.getId());
		result.setProjectId(model.getProjectId());
		result.setProjectName(cacheService.showProjectNameById(model.getProjectId()));
		result.setCustomerId(model.getCustomerId());
		result.setCustomerName(cacheService.showSubjectNameByIdAndKey(result.getCustomerId(), CacheKeyConsts.CUSTOMER));
		result.setBusiUnitId(model.getBusiUnitId());
		result.setBusiUnitName(cacheService.showSubjectNameByIdAndKey(model.getBusiUnitId(), CacheKeyConsts.BUSI_UNIT));
		result.setCurrnecyType(model.getCurrnecyType());
		result.setCurrnecyTypeName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE, model.getCurrnecyType() + ""));
		result.setCopeAmount(model.getCopeAmount());
		result.setPaidAmount(model.getPaidAmount());
		result.setUnpaidAmount(model.getUnpaidAmount());
		result.setCreateAt(model.getCreateAt());
		result.setCreatorId(model.getCreatorId());
		result.setUpdateAt(model.getUpdateAt());
		return result;
	}
}
