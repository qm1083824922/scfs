package com.scfs.service.pay;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.OperateConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.ListUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.pay.PayKeyWordDao;
import com.scfs.domain.BaseResult;
import com.scfs.domain.CodeValue;
import com.scfs.domain.pay.dto.req.PayKeyWordReqDto;
import com.scfs.domain.pay.dto.resq.PayKeyWordResDto;
import com.scfs.domain.pay.entity.PayKeyWord;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.support.ServiceSupport;
import com.google.common.collect.Lists;

/**
 * <pre>
 *
 *  File: PayKeyWordService.java
 *  Description:
 *  TODO
 *  Date,					Who,
 *  2016年12月30日			Administrator
 *
 * </pre>
 */
@Service
public class PayKeyWordService {
	@Autowired
	private PayKeyWordDao payKeyWordDao;

	/**
	 * 添加数据
	 * 
	 * @param payKeyWord
	 * @return
	 */
	public int createPayKeyWord(PayKeyWord payKeyWord) {
		payKeyWord.setIsDelete(BaseConsts.ZERO);
		payKeyWord.setCreateAt(new Date());
		payKeyWord.setCreator(ServiceSupport.getUser().getChineseName());
		payKeyWord.setCreatorId(ServiceSupport.getUser().getId());
		int id = payKeyWordDao.insert(payKeyWord);
		if (id <= 0) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "添加失败:" + JSONObject.toJSON(payKeyWord));
		}
		return payKeyWord.getId();
	}

	/**
	 * 编辑付款信息
	 *
	 * @param payKeyWord
	 * @return
	 */
	public Result<PayKeyWordResDto> editPayOrderById(PayKeyWord payKeyWord) {
		Result<PayKeyWordResDto> result = new Result<PayKeyWordResDto>();
		PayKeyWord payKeyWordResult = payKeyWordDao.queryEntityById(payKeyWord.getId());
		PayKeyWordResDto data = convertToPayPoWordResDto(payKeyWordResult);
		result.setItems(data);
		return result;
	}

	/**
	 * 更新关键词
	 *
	 * @param payKeyWord
	 * @return
	 */
	public BaseResult updatePayKeyWordById(PayKeyWord payKeyWord) {
		BaseResult baseResult = new BaseResult();
		int result = payKeyWordDao.updateById(payKeyWord);
		if (result == BaseConsts.ZERO) {
			baseResult.setSuccess(false);
			baseResult.setMsg("更新失败，请重试");
		}
		return baseResult;
	}

	/**
	 * 删除关键词
	 *
	 * @param payKeyWord
	 * @return
	 */
	public BaseResult deletePayKeyWordById(PayKeyWord payKeyWord) {
		BaseResult baseResult = new BaseResult();
		payKeyWord.setIsDelete(BaseConsts.ONE);
		int result = payKeyWordDao.updateById(payKeyWord);
		if (result == BaseConsts.ZERO) {
			baseResult.setSuccess(false);
			baseResult.setMsg("删除失败，请重试");
		}
		return baseResult;
	}

	/**
	 * 获取分页列表信息
	 * 
	 * @param payKeyWordReqDto
	 * @return
	 */
	public PageResult<PayKeyWordResDto> queryPayKeyWordResultsByCon(PayKeyWordReqDto payKeyWordReqDto) {
		PageResult<PayKeyWordResDto> pageResult = new PageResult<PayKeyWordResDto>();
		int offSet = PageUtil.getOffSet(payKeyWordReqDto.getPage(), payKeyWordReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, payKeyWordReqDto.getPer_page());
		List<PayKeyWordResDto> payPoRelationResDto = convertToPayKeyWordResDtos(
				payKeyWordDao.queryResultsByCon(payKeyWordReqDto, rowBounds));
		pageResult.setItems(payPoRelationResDto);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), payKeyWordReqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(payKeyWordReqDto.getPage());
		pageResult.setPer_page(payKeyWordReqDto.getPer_page());
		return pageResult;
	}

	public List<PayKeyWordResDto> convertToPayKeyWordResDtos(List<PayKeyWord> result) {
		List<PayKeyWordResDto> payKeyWordResDto = new ArrayList<PayKeyWordResDto>();
		if (ListUtil.isEmpty(result)) {
			return payKeyWordResDto;
		}
		for (PayKeyWord model : result) {
			PayKeyWordResDto keyWordResDto = convertToPayPoWordResDto(model);
			keyWordResDto.setOpertaList(getOperList());
			payKeyWordResDto.add(keyWordResDto);
		}
		return payKeyWordResDto;
	}

	public PayKeyWordResDto convertToPayPoWordResDto(PayKeyWord model) {
		PayKeyWordResDto result = new PayKeyWordResDto();
		result.setId(model.getId());
		result.setWord(model.getWord());
		result.setCreateAt(model.getCreateAt());
		result.setCreator(model.getCreator());
		return result;
	}

	/**
	 * 获取操作列表
	 *
	 * @param state
	 * @return
	 */
	private List<CodeValue> getOperList() {
		List<String> opertaList = Lists.newArrayList();
		opertaList.add(OperateConsts.DELETE);
		opertaList.add(OperateConsts.EDIT);
		opertaList.add(OperateConsts.DETAIL);
		// 2.将上述的操作列表通过权限过滤
		List<CodeValue> oprResult = ServiceSupport.getOperListByPermission(opertaList,
				PayKeyWordResDto.Operate.operMap);
		return oprResult;
	}
}
