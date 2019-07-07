package com.scfs.service.fee;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.CacheKeyConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.Coder;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.ListUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.fee.FeeShareDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.domain.fee.dto.req.FeeShareReqDto;
import com.scfs.domain.fee.dto.resp.FeeManageResDto;
import com.scfs.domain.fee.dto.resp.FeeShareResDto;
import com.scfs.domain.fee.entity.FeeManage;
import com.scfs.domain.fee.entity.FeeShare;
import com.scfs.domain.result.PageResult;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 * 	管理费用分摊service
 *  File: FeeShareService.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年05月17日				Administrator
 *
 * </pre>
 */
@Service
public class FeeShareService {
	@Autowired
	private FeeShareDao feeShareDao;
	@Autowired
	private FeeManageService feeManageService;
	@Autowired
	private CacheService cacheService;

	/**
	 * 添加
	 * 
	 * @param feeShare
	 * @return
	 */
	public void createFeeShare(FeeShareReqDto feeShareReqDto) {
		List<FeeShare> feeShareList = feeShareReqDto.getFeeShares();
		if (CollectionUtils.isNotEmpty(feeShareList)) {
			FeeManageResDto feeManage = feeManageService.queryFeeManageById(feeShareReqDto.getManageId());
			BigDecimal sum = BigDecimal.ZERO;
			for (FeeShare feeShare : feeShareList) {
				BigDecimal amount = new BigDecimal(feeShare.getAmount());
				sum = DecimalUtil.add(sum, amount);
			}
			if (DecimalUtil.gt(sum, feeManage.getBlanceAmount())) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "可分摊金额不足");
			} else {
				for (FeeShare feeShare : feeShareList) {
					feeShare.setManageId(feeShareReqDto.getManageId());
					Date date = new Date();
					feeShare.setCreateAt(date);
					feeShare.setCreator(
							ServiceSupport.getUser() == null ? null : ServiceSupport.getUser().getChineseName());
					feeShare.setCreatorId(ServiceSupport.getUser() == null ? null : ServiceSupport.getUser().getId());
					try {
						byte[] inputData = feeShare.getAmount().getBytes();
						feeShare.setAmount(Coder.encryptBASE64(inputData));
					} catch (Exception e) {
						throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "添加失败:" + JSONObject.toJSON(feeShare));
					}
					int result = feeShareDao.insert(feeShare);
					if (result <= 0) {
						throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "添加失败:" + JSONObject.toJSON(feeShare));
					}
				}
				FeeManage upFeeManage = new FeeManage();
				upFeeManage.setId(feeManage.getId());
				sum = DecimalUtil.add(feeManage.getShareAmount(), sum);
				upFeeManage.setShareAmount(sum.toString());
				feeManageService.updateFeeManageById(upFeeManage);
			}
		}
	}

	/**
	 * 删除
	 * 
	 * @param feeShareReqDto
	 */
	public void deleteFeeShare(FeeShareReqDto feeShareReqDto) {
		FeeManageResDto feeManage = feeManageService.queryFeeManageById(feeShareReqDto.getManageId());
		BigDecimal sum = BigDecimal.ZERO;
		for (Integer id : feeShareReqDto.getIds()) {
			FeeShareResDto resDto = convertToFeeShareResDto(feeShareDao.queryEntityById(id));
			sum = DecimalUtil.add(sum, resDto.getAmount());
			FeeShare feeShare = new FeeShare();
			feeShare.setId(id);
			feeShare.setIsDelete(BaseConsts.ONE);
			feeShareDao.updateById(feeShare);
		}
		FeeManage upFeeManage = new FeeManage();
		upFeeManage.setId(feeManage.getId());
		sum = DecimalUtil.subtract(feeManage.getShareAmount(), sum);
		upFeeManage.setShareAmount(sum.toString());
		feeManageService.updateFeeManageById(upFeeManage);
	}

	/**
	 * 获取分页数据
	 * 
	 * @param id
	 * @param shareAmout
	 */
	public PageResult<FeeShareResDto> queryManageShareResultsByCon(FeeShareReqDto feeShareReqDto) {
		PageResult<FeeShareResDto> pageResult = new PageResult<FeeShareResDto>();
		int offSet = PageUtil.getOffSet(feeShareReqDto.getPage(), feeShareReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, feeShareReqDto.getPer_page());
		List<FeeShareResDto> feeManageResDto = convertToFeeShareResDtos(
				feeShareDao.queryResultsByCon(feeShareReqDto, rowBounds));
		if (CollectionUtils.isNotEmpty(feeManageResDto)) {
			BigDecimal amountSum = new BigDecimal(BaseConsts.ZERO);
			for (FeeShareResDto feeShareResDto : feeManageResDto) {
				if (feeShareResDto != null) {
					amountSum = DecimalUtil.add(amountSum, feeShareResDto.getAmount());
				}
			}
			String totalStr = "金额  : " + DecimalUtil.toAmountString(DecimalUtil.formatScale2(amountSum));
			pageResult.setTotalStr(totalStr);
		}
		pageResult.setItems(feeManageResDto);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), feeShareReqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(feeShareReqDto.getPage());
		pageResult.setPer_page(feeShareReqDto.getPer_page());
		return pageResult;
	}

	public List<FeeShareResDto> convertToFeeShareResDtos(List<FeeShare> result) {
		List<FeeShareResDto> feeShareList = new ArrayList<FeeShareResDto>();
		if (ListUtil.isEmpty(result)) {
			return feeShareList;
		}
		for (FeeShare seeShare : result) {
			FeeShareResDto feeShareResDto = convertToFeeShareResDto(seeShare);
			feeShareList.add(feeShareResDto);
		}
		return feeShareList;
	}

	public FeeShareResDto convertToFeeShareResDto(FeeShare model) {
		FeeShareResDto result = new FeeShareResDto();
		result.setId(model.getId());
		result.setManageId(model.getManageId());
		result.setShareProjectId(model.getShareProjectId());
		Date date = null;
		if (model.getShareDate() != null) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			try {
				date = format.parse("1991-11-21");
			} catch (ParseException e) {
			}
			if (model.getShareDate().getTime() != date.getTime()) {
				result.setShareDate(model.getShareDate());
			}
		}
		if (model.getShareProjectId() != null) {
			result.setShareProjectName(cacheService.showProjectNameById(model.getShareProjectId()));
		}
		result.setShareCustId(model.getShareCustId());
		if (model.getShareCustId() != null) {
			result.setShareCustName(
					cacheService.getSubjectNameByIdAndKey(model.getShareCustId(), CacheKeyConsts.PROJECT_CS));
		}
		result.setShareUserId(model.getShareUserId());
		if (model.getShareUserId() != null) {
			result.setShareUserName(cacheService.getUserByid(model.getShareUserId()).getChineseName());
		}
		if (model.getAmount() == null || "".equals(model.getAmount())) {
			result.setAmount(BigDecimal.ZERO);
		} else {
			String amountString = "0.00";
			try {// 解密
				byte[] output = Coder.decryptBASE64(model.getAmount());
				amountString = new String(output);
			} catch (Exception e) {
				e.printStackTrace();
			}
			result.setAmount(new BigDecimal(new String(amountString)).setScale(2, BigDecimal.ROUND_HALF_DOWN));
		}
		return result;
	}
}
