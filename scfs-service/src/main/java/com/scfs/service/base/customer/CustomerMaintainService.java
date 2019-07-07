package com.scfs.service.base.customer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.beust.jcommander.internal.Lists;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.OperateConsts;
import com.scfs.common.consts.SeqConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.base.entity.CustomerMaintainDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.domain.BaseResult;
import com.scfs.domain.CodeValue;
import com.scfs.domain.base.dto.req.CustomerMaintainReqDto;
import com.scfs.domain.base.dto.resp.CustomerMaintainResDto;
import com.scfs.domain.base.entity.BaseSubject;
import com.scfs.domain.base.entity.CustomerMaintain;
import com.scfs.domain.base.subject.dto.req.AddSubjectDto;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.base.subject.BaseSubjectService;
import com.scfs.service.common.SequenceService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 * 客户维护信息
 *  File: CustomerMaintainService.java
 *  Description:
 *  TODO
 *  Date,                   Who,
 *  2017年07月26日         Administrator
 *
 * </pre>
 */
@Service
public class CustomerMaintainService {
	@Autowired
	private CustomerMaintainDao customerMaintainDao;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private SequenceService sequenceService;
	@Autowired
	private BaseSubjectService baseSubjectService;

	/**
	 * 获取列表信息
	 * 
	 * @param customerMaintainReqDto
	 * @return
	 */
	public PageResult<CustomerMaintainResDto> queryCustomerMaintainResultsByCon(CustomerMaintainReqDto reqDto) {
		if (!ServiceSupport.isAllowPerm(BusUrlConsts.QUERY_CUSTOMER_MAINTAIN_POWER)) {// 判断用户是否拥有权限
			reqDto.setUserId(ServiceSupport.getUser().getId());
		}
		PageResult<CustomerMaintainResDto> result = new PageResult<CustomerMaintainResDto>();
		int offSet = PageUtil.getOffSet(reqDto.getPage(), reqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, reqDto.getPer_page());
		List<CustomerMaintain> customerList = customerMaintainDao.queryResultsByCon(reqDto, rowBounds);
		List<CustomerMaintainResDto> customerResList = convertToResult(customerList);
		result.setItems(customerResList);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), reqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(reqDto.getPage());
		result.setPer_page(reqDto.getPer_page());
		return result;
	}

	/**
	 * 添加数据
	 * 
	 * @param customerMaintain
	 * @return
	 */
	public Integer insertCustomerMaintain(CustomerMaintain customerMaintain) {
		customerMaintain.setFllow(ServiceSupport.getUser().getId());
		customerMaintain.setCustomerNo(sequenceService.getNumDateByBusName(BaseConsts.CUSTOMER_MAINTAIN_NO,
				SeqConsts.S_CUSTOMERMAINTAIN_NO, BaseConsts.INT_13));
		customerMaintain.setStage(BaseConsts.ONE);
		customerMaintain.setIsDelete(BaseConsts.ZERO);
		customerMaintain
				.setCreator(ServiceSupport.getUser() == null ? null : ServiceSupport.getUser().getChineseName());
		customerMaintain.setCreatorId(ServiceSupport.getUser() == null ? null : ServiceSupport.getUser().getId());
		customerMaintain.setCreateAt(new Date());
		Integer result = customerMaintainDao.insert(customerMaintain);
		if (result < 1)
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "添加失败:" + JSONObject.toJSON(customerMaintain));
		return customerMaintain.getId();
	}

	/**
	 * 编辑/浏览
	 * 
	 * @param customerMaintain
	 * @return
	 */
	public Result<CustomerMaintainResDto> queryCustomerMaintainById(CustomerMaintain customerMaintain) {
		Result<CustomerMaintainResDto> result = new Result<CustomerMaintainResDto>();
		CustomerMaintain customer = customerMaintainDao.queryEntityById(customerMaintain.getId());
		result.setItems(convertToResDto(customer));
		return result;
	}

	public CustomerMaintainResDto queryCustomerMaintainById(Integer id) {
		CustomerMaintainResDto result = convertToResDto(customerMaintainDao.queryEntityById(id));
		return result;
	}

	/**
	 * 修改信息
	 * 
	 * @param customerMaintain
	 * @return
	 */
	public BaseResult updateCustomerMaintain(CustomerMaintain customerMaintain) {
		BaseResult result = new BaseResult();
		customerMaintainDao.queryEntityById(customerMaintain.getId());
		customerMaintainDao.updateById(customerMaintain);
		return result;
	}

	/**
	 * 修改阶段相关业务
	 * 
	 * @param id
	 * @param state
	 * @param isOver
	 * @return
	 */
	public BaseResult updateStage(Integer id, Integer state, boolean isOver) {
		CustomerMaintain customerMaintain = new CustomerMaintain();
		customerMaintain.setId(id);
		BaseResult result = new BaseResult();
		CustomerMaintain maintain = customerMaintainDao.queryEntityById(customerMaintain.getId());
		Integer customerId = maintain.getSubjectId();
		Integer customerType = maintain.getCustomerType();
		customerMaintain.setSubjectId(customerId);
		if (isOver) {// 是否删除最后一条操作
			if (customerId != null) {
				BaseSubject vo = new BaseSubject();
				vo.setId(maintain.getSubjectId());
				baseSubjectService.deleteSubject(vo);
				customerMaintain.setSubjectId(null);
			}
		} else {
			if (state == BaseConsts.THREE) {// 合作阶段
				if (customerId == null) {
					AddSubjectDto addSubjectDto = new AddSubjectDto();
					Integer subjectType = BaseConsts.EIGHT;
					if (customerType == BaseConsts.TWO) {
						subjectType = BaseConsts.FOUR; // 供应商
					} else if (customerType == BaseConsts.THREE) {
						subjectType = BaseConsts.ONE;// 经营单位
					}
					addSubjectDto.setSubjectType(subjectType);
					addSubjectDto.setAbbreviation(maintain.getAbbreviation());
					addSubjectDto.setChineseName(maintain.getChineseName());
					addSubjectDto.setEnglishName(maintain.getEnglishName());
					addSubjectDto.setRegPlace(maintain.getRegNo());
					addSubjectDto.setRegNo(maintain.getRegNo());
					addSubjectDto.setRegPhone(maintain.getRegPhone());
					addSubjectDto.setOfficeAddress(maintain.getOfficeAddress());
					Integer subjectId = baseSubjectService.addBaseSubject(addSubjectDto);
					BaseSubject vo = new BaseSubject();
					vo.setId(subjectId);
					baseSubjectService.submitSubject(vo);
					customerMaintain.setSubjectId(subjectId);
				}
			} else if (state == BaseConsts.FOUR) {// 取消阶段
				if (customerId != null) {
					BaseSubject vo = new BaseSubject();
					vo.setId(maintain.getSubjectId());
					baseSubjectService.deleteSubject(vo);
					customerMaintain.setSubjectId(null);
				}
			}
		}
		customerMaintain.setStage(state);
		customerMaintainDao.updateStateById(customerMaintain);
		return result;
	}

	/**
	 * 删除数据
	 * 
	 * @param customerMaintain
	 * @return
	 */
	public BaseResult deleteCustomerMaintain(CustomerMaintain customerMaintain) {
		customerMaintainDao.queryEntityById(customerMaintain.getId());
		BaseResult result = new BaseResult();
		customerMaintain.setIsDelete(BaseConsts.ONE);
		customerMaintain.setDeleteAt(new Date());
		customerMaintainDao.updateById(customerMaintain);
		return result;
	}

	private List<CustomerMaintainResDto> convertToResult(List<CustomerMaintain> customerList) {
		List<CustomerMaintainResDto> customerResDtoList = new ArrayList<CustomerMaintainResDto>();
		if (CollectionUtils.isEmpty(customerList)) {
			return customerResDtoList;
		}
		for (CustomerMaintain customerMaintain : customerList) {
			CustomerMaintainResDto restDto = convertToResDto(customerMaintain);
			restDto.setOpertaList(getOperList());
			customerResDtoList.add(restDto);
		}
		return customerResDtoList;
	}

	public CustomerMaintainResDto convertToResDto(CustomerMaintain model) {
		CustomerMaintainResDto result = new CustomerMaintainResDto();
		result.setId(model.getId());
		result.setSubjectId(model.getSubjectId());
		result.setCustomerNo(model.getCustomerNo());
		result.setCustomerType(model.getCustomerType());
		result.setCustomerTypeName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.CUSTOMER_TYPE, model.getCustomerType() + ""));
		result.setSourceChannel(model.getSourceChannel());
		result.setSourceChannelName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.SOURCE_CHANNEL, model.getSourceChannel() + ""));
		result.setAbbreviation(model.getAbbreviation());
		result.setChineseName(model.getChineseName());
		result.setEnglishName(model.getEnglishName());
		result.setRegNo(model.getRegNo());
		result.setRegPhone(model.getRegPhone());
		result.setRegPlace(model.getRegPlace());
		result.setOfficeAddress(model.getOfficeAddress());
		result.setGuardian(model.getGuardian());
		result.setGuardianName(cacheService.getUserChineseNameByid(model.getGuardian()));
		result.setFllow(model.getFllow());
		if (model.getFllow() != null) {
			result.setFllowName(cacheService.getUserChineseNameByid(model.getFllow()));
		}
		result.setContacts(model.getContacts());
		result.setContactsNumber(model.getContactsNumber());
		result.setContactsOtherNumber(model.getContactsOtherNumber());
		result.setStage(model.getStage());
		result.setStageName(ServiceSupport.getValueByBizCode(BizCodeConsts.MATTER_STAGE, model.getStage() + ""));
		result.setRemark(model.getRemark());
		result.setCreator(model.getCreator());
		result.setCreateAt(model.getCreateAt());
		result.setCreatorId(model.getCreatorId());
		result.setDeleteAt(model.getDeleteAt());
		result.setUpdateAt(model.getUpdateAt());
		result.setIsDelete(model.getIsDelete());
		return result;
	}

	private List<CodeValue> getOperList() {
		// 1.根据状态得到操作列表
		List<String> operNameList = getOperListByState();
		// 2.将上述的操作列表通过权限过滤
		List<CodeValue> oprResult = ServiceSupport.getOperListByPermission(operNameList,
				CustomerMaintainResDto.Operate.operMap);
		return oprResult;
	}

	private List<String> getOperListByState() {
		List<String> opertaList = Lists.newArrayList(4);
		opertaList.add(OperateConsts.DETAIL);
		opertaList.add(OperateConsts.EDIT);
		opertaList.add(OperateConsts.DIVIDE);
		opertaList.add(OperateConsts.FOLLOW);
		return opertaList;
	}
}
