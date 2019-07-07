package com.scfs.service.export;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.beust.jcommander.internal.Lists;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.CacheKeyConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.OperateConsts;
import com.scfs.common.consts.SeqConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.export.CustomsApplyDao;
import com.scfs.dao.export.CustomsApplyLineDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.domain.CodeValue;
import com.scfs.domain.base.entity.BaseAddress;
import com.scfs.domain.base.entity.BaseProject;
import com.scfs.domain.base.entity.BaseSubject;
import com.scfs.domain.export.dto.req.CustomsApplySearchReqDto;
import com.scfs.domain.export.dto.resp.CustomsApplyResDto;
import com.scfs.domain.export.entity.CustomsApply;
import com.scfs.domain.export.entity.CustomsApplyLine;
import com.scfs.domain.export.entity.CustomsApplyLineSum;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.common.SequenceService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

/**
 * Created by Administrator on 2016年12月6日.
 */
@Service
public class CustomsApplyService {
	@Autowired
	private CustomsApplyDao customsApplyDao;
	@Autowired
	private CustomsApplyLineDao customsApplyLineDao;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private SequenceService sequenceService;
	@Autowired
	private CustomsApplyLineService customsApplyLineService;

	/**
	 * 查询报关申请
	 * 
	 * @param customsApplySearchReqDto
	 * @return
	 */
	public PageResult<CustomsApplyResDto> queryCustomsApplyResultsByCon(
			CustomsApplySearchReqDto customsApplySearchReqDto) {
		PageResult<CustomsApplyResDto> result = new PageResult<CustomsApplyResDto>();

		int offSet = PageUtil.getOffSet(customsApplySearchReqDto.getPage(), customsApplySearchReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, customsApplySearchReqDto.getPer_page());
		customsApplySearchReqDto.setUserId(ServiceSupport.getUser().getId());
		if (customsApplySearchReqDto.getNeedSum() != null && customsApplySearchReqDto.getNeedSum() == BaseConsts.ONE) {
			CustomsApply sum = customsApplyDao.sumAmountAndNumber(customsApplySearchReqDto);
			result.setTotalNum(sum.getCustomsNum());
			result.setTotalAmount(sum.getCustomsAmount());
		}
		List<CustomsApply> customsApplyList = customsApplyDao.queryResultsByCon(customsApplySearchReqDto, rowBounds);
		List<CustomsApplyResDto> customsApplyResDtoList = convertToResDto(customsApplyList);
		result.setItems(customsApplyResDtoList);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), customsApplySearchReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(customsApplySearchReqDto.getPage());
		result.setPer_page(customsApplySearchReqDto.getPer_page());

		return result;
	}

	/**
	 * 查询报关申请详情
	 * 
	 * @param customsApply
	 * @return
	 */
	public Result<CustomsApplyResDto> detailCustomsApplyById(CustomsApply customsApply) {
		Result<CustomsApplyResDto> result = new Result<CustomsApplyResDto>();
		CustomsApply customsApplyRes = customsApplyDao.queryEntityById(customsApply.getId());
		CustomsApplyResDto customsApplyResDto = convertToResDto(customsApplyRes);
		result.setItems(customsApplyResDto);
		return result;
	}

	/**
	 * 通过id获取信息
	 * 
	 * @param id
	 * @return
	 */
	public CustomsApplyResDto queryCustomsApplyById(int id) {
		return convertToResDto(customsApplyDao.queryEntityById(id));
	}

	/**
	 * 新增报关申请
	 * 
	 * @param customsApply
	 * @return
	 */
	public CustomsApply addCustomsApply(CustomsApply customsApply) {
		customsApply.setApplyNo(sequenceService.getNumDateByBusName(BaseConsts.CUSTOMS_APPLY_NO,
				SeqConsts.CUSTOMS_APPLY_NO, BaseConsts.INT_13));
		customsApply.setCustomsNum(BigDecimal.ZERO);
		customsApply.setCustomsAmount(BigDecimal.ZERO);
		customsApply.setStatus(BaseConsts.ONE); // 待提交
		customsApply.setCreator(ServiceSupport.getUser().getChineseName());
		customsApply.setCreatorId(ServiceSupport.getUser().getId());
		int result = customsApplyDao.insert(customsApply);
		if (result <= 0) {
			throw new BaseException(ExcMsgEnum.CUSTOMS_APPLY_ADD_ERROR);
		}
		return customsApply;
	}

	/**
	 * 修改退税进度
	 * 
	 * @param customsApply
	 * @return
	 */
	public int updateCustomsApplyTaxById(CustomsApply customsApply) {
		return customsApplyDao.updateById(customsApply);
	}

	/**
	 * 更新报关申请
	 * 
	 * @param customsApply
	 * @return
	 */
	public void updateCustomsApplyById(CustomsApply customsApply) {
		CustomsApply customsApplyRes = customsApplyDao.queryAndLockEntityById(customsApply.getId());
		if (customsApplyRes.getStatus().equals(BaseConsts.ONE)
				&& customsApplyRes.getIsDelete().equals(BaseConsts.ZERO)) { // 1-待提交
			customsApplyDao.updateById(customsApply);
		} else {
			throw new BaseException(ExcMsgEnum.CUSTOMS_APPLY_UPDATE_ERROR);
		}
	}

	/**
	 * 删除报关申请
	 * 
	 * @param customsApply
	 * @return
	 */
	public void deleteCustomsApplyById(CustomsApply customsApply) {
		customsApply = customsApplyDao.queryAndLockEntityById(customsApply.getId());
		if (customsApply.getStatus().equals(BaseConsts.ONE) && customsApply.getIsDelete().equals(BaseConsts.ZERO)) { // 1-待提交
			customsApply.setDeleterId(ServiceSupport.getUser().getId());
			customsApply.setDeleter(ServiceSupport.getUser().getChineseName());
			customsApply.setDeleteAt(new Date());
			customsApply.setIsDelete(BaseConsts.ONE); // 已删除
			customsApplyDao.updateById(customsApply);

			// 更新出库单明细报关数量
			List<CustomsApplyLine> customsApplyLineList = customsApplyLineDao
					.queryResultsByCustomsApplyId(customsApply.getId());
			if (!CollectionUtils.isEmpty(customsApplyLineList)) {
				for (CustomsApplyLine customsApplyLine : customsApplyLineList) {
					customsApplyLineService.updateCustomsDeclareNum(customsApplyLine, customsApplyLine,
							BaseConsts.THREE);
				}
			}
		} else {
			throw new BaseException(ExcMsgEnum.CUSTOMS_APPLY_DELETE_ERROR);
		}
	}

	/**
	 * 提交报关申请
	 * 
	 * @param customsApply
	 * @return
	 */
	public void submitCustomsApplyById(CustomsApply customsApply) {
		customsApply = customsApplyDao.queryAndLockEntityById(customsApply.getId());
		if (customsApply.getStatus().equals(BaseConsts.ONE) && customsApply.getIsDelete().equals(BaseConsts.ZERO)) { // 1-待提交
			validateSubmit(customsApply); // 验证报关申请单

			customsApply.setStatus(BaseConsts.TWO); // 已完成
			customsApplyDao.updateById(customsApply);
		} else {
			throw new BaseException(ExcMsgEnum.CUSTOMS_APPLY_SUBMIT_ERROR);
		}
	}

	/**
	 * 验证报关申请单
	 * 
	 * @param customsApply
	 */
	private void validateSubmit(CustomsApply customsApply) {
		if (null != customsApply) {
			int dtlsCount = customsApplyLineDao.queryCountByCustomsApplyId(customsApply.getId());
			if (dtlsCount <= 0) {
				throw new BaseException(ExcMsgEnum.CUSTOMS_APPLY_DTL_NOT_ADD);
			}

			CustomsApplyLineSum customsApplyLineSum = customsApplyLineDao
					.querySumByCustomsApplyId(customsApply.getId());
			if (null != customsApplyLineSum) {
				if (DecimalUtil.ne(customsApply.getCustomsNum(), customsApplyLineSum.getCustomsNum())) {
					throw new BaseException(ExcMsgEnum.CUSTOMS_APPLY_DTL_NUM_NOT_EQUAL);
				}
			}
		}
	}

	private List<CustomsApplyResDto> convertToResDto(List<CustomsApply> customsApplyList) {
		List<CustomsApplyResDto> customsApplyResDtoList = new ArrayList<CustomsApplyResDto>(5);
		if (CollectionUtils.isEmpty(customsApplyList)) {
			return customsApplyResDtoList;
		}
		for (CustomsApply customsApply : customsApplyList) {
			CustomsApplyResDto customsApplyResDto = convertToResDto(customsApply);
			customsApplyResDto.setOpertaList(getOperList(customsApply.getStatus()));
			customsApplyResDtoList.add(customsApplyResDto);
		}
		return customsApplyResDtoList;
	}

	private CustomsApplyResDto convertToResDto(CustomsApply customsApply) {
		CustomsApplyResDto customsApplyResDto = new CustomsApplyResDto();
		if (null != customsApply) {
			BeanUtils.copyProperties(customsApply, customsApplyResDto);
			if (customsApply.getStatus() != null) {
				customsApplyResDto.setStatusName(ServiceSupport.getValueByBizCode(BizCodeConsts.CUSTOMS_APPLY_STATUS,
						Integer.toString(customsApply.getStatus())));
			}
			customsApplyResDto.setProjectName(cacheService.showProjectNameById(customsApply.getProjectId()));
			customsApplyResDto.setProxyCompanyName(
					cacheService.showSubjectNameByIdAndKey(customsApply.getProxyCompanyId(), CacheKeyConsts.CUSTOMER));
			customsApplyResDto.setCustomerName(
					cacheService.showSubjectNameByIdAndKey(customsApply.getCustomerId(), CacheKeyConsts.CUSTOMER));
			customsApplyResDto.setSystemTime(new Date());
			if (customsApply.getTaxRate() != null) {
				customsApplyResDto.setTaxRateValue(customsApply.getTaxRate().toString());
			}
			if (customsApply.getCustomsNum() != null) {
				customsApplyResDto.setTaxNumValue(customsApply.getCustomsNum().toString());
			}
			if (customsApply.getCustomsAmount() != null) {
				customsApplyResDto.setCustomsAmountValue(customsApply.getCustomsAmount().toString());
			}
			if (customsApply.getCustomsTaxAmount() != null) {
				customsApplyResDto.setCustomsTaxAmountValue(customsApply.getCustomsTaxAmount().toString());
			}
			BaseAddress baseAddress = cacheService.getAddressById(customsApply.getCustomerAddressId());
			if (null != baseAddress) {
				customsApplyResDto.setCustomerAddressName(baseAddress.getShowValue());
			}
			BaseProject baseProject = cacheService.getProjectById(customsApply.getProjectId());
			if (null != baseProject) {
				BaseSubject busiUnit = cacheService.getBusiUnitById(baseProject.getBusinessUnitId());
				if (null != busiUnit) {
					customsApplyResDto.setBusinessUnitNameValue(busiUnit.getChineseName());
					customsApplyResDto.setBusinessUnitAddress(busiUnit.getOfficeAddress());
				}
			}
		}
		return customsApplyResDto;
	}

	private List<CodeValue> getOperList(Integer state) {
		// 1.根据状态得到操作列表
		List<String> operNameList = getOperListByState(state);
		// 2.将上述的操作列表通过权限过滤
		List<CodeValue> oprResult = ServiceSupport.getOperListByPermission(operNameList,
				CustomsApplyResDto.Operate.operMap);
		return oprResult;
	}

	private List<String> getOperListByState(Integer state) {
		if (state == null) {
			return null;
		}
		List<String> opertaList = Lists.newArrayList(5);
		switch (state) {
		// 状态 1-待提交 2-已完成
		case BaseConsts.ONE:
			opertaList.add(OperateConsts.DETAIL);
			opertaList.add(OperateConsts.EDIT);
			opertaList.add(OperateConsts.SUBMIT);
			opertaList.add(OperateConsts.DELETE);
			break;
		case BaseConsts.TWO:
			opertaList.add(OperateConsts.DETAIL);
			opertaList.add(OperateConsts.PRINT);
			break;
		}
		return opertaList;
	}

	public void updatePrintNum(Integer id) {
		CustomsApply customsApply = customsApplyDao.queryEntityById(id);
		CustomsApply customs = new CustomsApply();
		customs.setId(id);
		customs.setPrintNum(customsApply.getPrintNum() + 1);
		customsApplyDao.updatePrintNum(customs);
	}
}
