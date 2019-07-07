package com.scfs.service.base.customer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.beust.jcommander.internal.Lists;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.OperateConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.dao.base.entity.CustomerFollowDao;
import com.scfs.domain.BaseResult;
import com.scfs.domain.CodeValue;
import com.scfs.domain.base.dto.req.CustomerMaintainReqDto;
import com.scfs.domain.base.dto.resp.CustomerFollowResDto;
import com.scfs.domain.base.dto.resp.CustomerMaintainResDto;
import com.scfs.domain.base.entity.CustomerFollow;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 * 客户跟进信息
 *  File: CustomerFollowService.java
 *  Description:
 *  TODO
 *  Date,                   Who,
 *  2017年07月27日         Administrator
 *
 * </pre>
 */
@Service
public class CustomerFollowService {
	@Autowired
	private CustomerFollowDao customerFollowDao;
	@Autowired
	private CustomerMaintainService customerMaintainService;

	/**
	 * 获取列表信息
	 * 
	 * @param customerFollow
	 * @return
	 */
	public PageResult<CustomerFollowResDto> queryCustomerFollowResultsByCon(CustomerFollow customerFollow) {
		PageResult<CustomerFollowResDto> result = new PageResult<CustomerFollowResDto>();
		List<CustomerFollow> customerList = customerFollowDao.queryResultsByCon(customerFollow);
		List<CustomerFollowResDto> customerResList = convertToResult(customerList);
		result.setItems(customerResList);
		return result;
	}

	/**
	 * 添加数据
	 * 
	 * @param customerFollow
	 * @return
	 */
	public Integer insertCustomerFollow(CustomerFollow customerFollow) {
		Integer state = customerFollow.getStage();
		Integer customerId = customerFollow.getId();
		CustomerFollow maxUpdateFollow = customerFollowDao.queryEntityByMaxUpdate(customerId);
		if (maxUpdateFollow == null || !maxUpdateFollow.getStage().equals(customerFollow.getStage())) {
			customerMaintainService.updateStage(customerId, state, false);
		}
		customerFollow.setCustomerId(customerId);
		customerFollow.setIsDelete(BaseConsts.ZERO);
		customerFollow.setCreator(ServiceSupport.getUser() == null ? null : ServiceSupport.getUser().getChineseName());
		customerFollow.setCreatorId(ServiceSupport.getUser() == null ? null : ServiceSupport.getUser().getId());
		customerFollow.setCreateAt(new Date());
		Integer result = customerFollowDao.insert(customerFollow);
		if (result < 1)
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "添加失败:" + JSONObject.toJSON(customerFollow));
		return customerFollow.getId();
	}

	/**
	 * 编辑/浏览
	 * 
	 * @param customerFollow
	 * @return
	 */
	public Result<CustomerFollowResDto> queryCustomerFollowById(CustomerFollow customerFollow) {
		Result<CustomerFollowResDto> result = new Result<CustomerFollowResDto>();
		CustomerFollow customer = customerFollowDao.queryEntityById(customerFollow.getId());
		result.setItems(convertToResDto(customer));
		return result;
	}

	/**
	 * 修改信息
	 * 
	 * @param customerFollow
	 * @return
	 */
	public BaseResult updateCustomerFollow(CustomerFollow customerFollow) {
		Integer state = customerFollow.getStage();
		Integer customerId = customerFollow.getCustomerId();
		BaseResult result = new BaseResult();
		CustomerFollow maxUpdateFollow = customerFollowDao.queryEntityByMaxUpdate(customerId);
		if (maxUpdateFollow == null || !maxUpdateFollow.getStage().equals(customerFollow.getStage())) {
			customerMaintainService.updateStage(customerId, state, false);
		}
		customerFollowDao.queryEntityById(customerFollow.getId());
		customerFollowDao.updateById(customerFollow);
		return result;
	}

	/**
	 * 删除数据
	 * 
	 * @param customerMaintain
	 * @return
	 */
	public BaseResult deleteCustomerFollow(CustomerFollow customerFollow) {
		boolean isOver = false; // 是否最后一条数据删除
		CustomerFollow follow = customerFollowDao.queryEntityById(customerFollow.getId());
		Integer customerId = follow.getCustomerId();
		Integer state = follow.getStage();
		if (state.equals(BaseConsts.THREE)) {
			state = BaseConsts.FOUR;
			customerMaintainService.updateStage(customerId, state, isOver);
		} else {
			CustomerFollow queryFollow = new CustomerFollow();
			queryFollow.setId(customerFollow.getId());
			queryFollow.setCustomerId(follow.getCustomerId());
			CustomerFollow maxUpdateFollow = customerFollowDao.queryEntityByNotin(queryFollow);// 获取最近一条数据
			if (maxUpdateFollow != null) {// 是否只有当前数据
				if (!maxUpdateFollow.getStage().equals(state)) {// 是否与最新数据相同
					customerMaintainService.updateStage(maxUpdateFollow.getCustomerId(), maxUpdateFollow.getStage(),
							isOver);
				}
			} else {
				state = BaseConsts.ONE;
				isOver = true;
				customerMaintainService.updateStage(customerId, state, isOver);
			}
		}
		BaseResult result = new BaseResult();
		customerFollow.setIsDelete(BaseConsts.ONE);
		customerFollow.setDeleteAt(new Date());
		customerFollowDao.updateById(customerFollow);
		return result;
	}

	public BaseResult deleteAllCustomerFollow(CustomerMaintainReqDto customerReqDto) {
		BaseResult result = new BaseResult();
		for (Integer id : customerReqDto.getIds()) {
			customerFollowDao.queryEntityById(id);
			CustomerFollow customerFollow = new CustomerFollow();
			customerFollow.setId(id);
			customerFollow.setIsDelete(BaseConsts.ONE);
			customerFollow.setDeleteAt(new Date());
			customerFollowDao.updateById(customerFollow);
		}
		return result;
	}

	private List<CustomerFollowResDto> convertToResult(List<CustomerFollow> followList) {
		List<CustomerFollowResDto> followResDtoList = new ArrayList<CustomerFollowResDto>();
		if (CollectionUtils.isEmpty(followList)) {
			return followResDtoList;
		}
		for (CustomerFollow customerFollow : followList) {
			CustomerFollowResDto restDto = convertToResDto(customerFollow);
			restDto.setOpertaList(getOperList());
			followResDtoList.add(restDto);
		}
		return followResDtoList;
	}

	public CustomerFollowResDto convertToResDto(CustomerFollow model) {
		CustomerFollowResDto result = new CustomerFollowResDto();
		result.setId(model.getId());
		result.setStage(model.getStage());
		result.setStageName(ServiceSupport.getValueByBizCode(BizCodeConsts.MATTER_STAGE, model.getStage() + ""));
		result.setCustomerId(model.getCustomerId());
		CustomerMaintainResDto mainRes = customerMaintainService.queryCustomerMaintainById(model.getCustomerId());
		result.setAbbreviation(mainRes.getAbbreviation());
		result.setChineseName(mainRes.getChineseName());
		result.setEnglishName(mainRes.getEnglishName());
		result.setFllow(result.getFllow());
		result.setFllowName(mainRes.getFllowName());
		result.setContent(model.getContent());
		result.setCreator(model.getCreator());
		result.setCreateAt(model.getCreateAt());
		result.setCreatorId(model.getCreatorId());
		result.setUpdateAt(model.getUpdateAt());
		result.setIsDelete(model.getIsDelete());
		return result;
	}

	private List<CodeValue> getOperList() {
		// 1.根据状态得到操作列表
		List<String> operNameList = getOperListByState();
		// 2.将上述的操作列表通过权限过滤
		List<CodeValue> oprResult = ServiceSupport.getOperListByPermission(operNameList,
				CustomerFollowResDto.Operate.operMap);
		return oprResult;
	}

	private List<String> getOperListByState() {
		List<String> opertaList = Lists.newArrayList(4);
		opertaList.add(OperateConsts.EDIT);
		opertaList.add(OperateConsts.DELETE);
		return opertaList;
	}
}
