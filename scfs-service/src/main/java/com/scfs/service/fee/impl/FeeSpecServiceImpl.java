package com.scfs.service.fee.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.OperateConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.ListUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.fee.FeeSpecDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.domain.BaseResult;
import com.scfs.domain.CodeValue;
import com.scfs.domain.fee.dto.req.FeeSpecSearchReqDto;
import com.scfs.domain.fee.dto.resp.FeeSpecResDto;
import com.scfs.domain.fee.entity.FeeSpec;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.fee.FeeSpecService;
import com.scfs.service.support.ServiceSupport;
import com.google.common.collect.Lists;

/**
 * <pre>
 * 
 *  File: FeeSpecServiceImpl.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年10月17日			Administrator
 *
 * </pre>
 */
@Service
public class FeeSpecServiceImpl implements FeeSpecService {
	@Autowired
	FeeSpecDao feeSpecDao;

	public List<FeeSpec> queryAllFeeSpec(FeeSpecSearchReqDto req) {
		return feeSpecDao.queryAllFeeSpec(req);
	}

	public int createFeeSpec(FeeSpec feeSpec) {
		int id = feeSpecDao.insert(feeSpec);
		if (id <= 0) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "添加失败:" + JSONObject.toJSON(feeSpec));
		}
		return feeSpec.getId();
	}

	public BaseResult updateFeeSpecById(FeeSpec feeSpec) {
		BaseResult baseResult = new BaseResult();
		int result = feeSpecDao.updateById(feeSpec);
		if (result == BaseConsts.ZERO) {
			baseResult.setSuccess(false);
			baseResult.setMsg("更新信息失败，请重试");
		}
		return baseResult;
	}

	public Result<FeeSpecResDto> editFeeSpecById(FeeSpec feeSpec) {
		Result<FeeSpecResDto> result = new Result<FeeSpecResDto>();
		result.setItems(convertToFeeSpecResDto(feeSpecDao.queryEntityById(feeSpec.getId())));
		return result;
	}

	public BaseResult deleteFeeSpecById(FeeSpec feeSpec) {
		BaseResult baseResult = new BaseResult();
		feeSpecDao.deletById(feeSpec.getId());
		return baseResult;
	}

	@Override
	public PageResult<FeeSpecResDto> queryFeeSpecResultsByCon(FeeSpecSearchReqDto req) {
		PageResult<FeeSpecResDto> pageResult = new PageResult<FeeSpecResDto>();
		int offSet = PageUtil.getOffSet(req.getPage(), req.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, req.getPer_page());
		List<FeeSpecResDto> feeSpecList = convertToFeeSpecResDto(feeSpecDao.queryAllFeeSpec(req, rowBounds));
		pageResult.setItems(feeSpecList);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), req.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(req.getPage());
		pageResult.setPer_page(req.getPer_page());
		return pageResult;
	}

	public List<FeeSpecResDto> convertToFeeSpecResDto(List<FeeSpec> result) {
		List<FeeSpecResDto> feeSpecResDtos = new ArrayList<FeeSpecResDto>();
		if (ListUtil.isEmpty(result)) {
			return feeSpecResDtos;
		}
		for (FeeSpec feeSpec : result) {
			FeeSpecResDto feeSpecResDto = convertToFeeSpecResDto(feeSpec);
			List<CodeValue> operList = getOperList();
			feeSpecResDto.setOpertaList(operList);
			feeSpecResDtos.add(feeSpecResDto);
		}
		return feeSpecResDtos;
	}

	public FeeSpecResDto convertToFeeSpecResDto(FeeSpec model) {
		FeeSpecResDto result = new FeeSpecResDto();
		result.setId(model.getId());
		result.setFeeType(model.getFeeType());
		result.setFeeTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.FEE_SPEC_TYPE, model.getFeeType() + ""));
		result.setFeeSpecNo(model.getFeeSpecNo());
		result.setFeeSpecName(model.getFeeSpecName());
		result.setFeeOneName(model.getFeeOneName());
		result.setFeeOneNameValue(
				ServiceSupport.getValueByBizCode(BizCodeConsts.FEE_SPEC_ONE, model.getFeeOneName() + ""));
		result.setFeeTwoName(model.getFeeTwoName());
		result.setFeeTwoNameValue(
				ServiceSupport.getValueByBizCode(BizCodeConsts.FEE_SPEC_TWO, model.getFeeTwoName() + ""));
		result.setFinanceCode(model.getFinanceCode());
		result.setRemark(model.getRemark());
		return result;
	}

	/**
	 * 获取操作列表
	 *
	 * @param state
	 * @return
	 */
	private List<CodeValue> getOperList() {
		// 1.根据状态得到操作列表
		List<String> operNameList = getOperListByState();
		// 2.将上述的操作列表通过权限过滤
		List<CodeValue> oprResult = ServiceSupport.getOperListByPermission(operNameList, FeeSpecResDto.Operate.operMap);
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
		opertaList.add(OperateConsts.EDIT);
		opertaList.add(OperateConsts.DETAIL);
		opertaList.add(OperateConsts.DELETE);
		return opertaList;
	}
}
