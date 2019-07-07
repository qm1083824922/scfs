package com.scfs.service.base.subject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.base.entity.BaseAddressDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.domain.base.entity.BaseAddress;
import com.scfs.domain.base.subject.dto.req.AddAddressDto;
import com.scfs.domain.base.subject.dto.req.QueryAddressReqDto;
import com.scfs.domain.base.subject.dto.resp.QueryAddressResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 * 
 *  File: BaseAddressServiceImpl.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年9月27日				Administrator
 *
 * </pre>
 */
@Service
public class BaseAddressService {
	@Autowired
	private CacheService cacheService;

	@Autowired
	private BaseAddressDao baseAddressDao;

	public BaseAddress loadAndLockEntityById(int id) {
		BaseAddress obj = baseAddressDao.loadAndLockEntityById(id);
		if (obj == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, baseAddressDao.getClass(), id);
		}
		return obj;
	}

	/**
	 * 新增地址
	 */
	public void addBaseAddress(AddAddressDto addAddressDto) {
		addAddressDto.setCreator(ServiceSupport.getUser().getChineseName());
		addAddressDto.setState(BaseConsts.ONE);
		int result = baseAddressDao.insertBaseAddress(addAddressDto);
		if (result <= 0) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "添加失败:" + JSONObject.toJSON(addAddressDto));
		}

	}

	public PageResult<QueryAddressResDto> queryAddressBySubjectId(QueryAddressReqDto queryAddressReqDto) {
		PageResult<QueryAddressResDto> pageResult = new PageResult<QueryAddressResDto>();
		int offSet = PageUtil.getOffSet(queryAddressReqDto.getPage(), queryAddressReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, queryAddressReqDto.getPer_page());
		List<QueryAddressResDto> queryAddressResDtos = convertToResult(
				baseAddressDao.queryAddressBySubjectId(queryAddressReqDto, rowBounds));
		pageResult.setItems(queryAddressResDtos);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), queryAddressReqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(queryAddressReqDto.getPage());
		pageResult.setPer_page(queryAddressReqDto.getPer_page());
		return pageResult;
	}

	private List<QueryAddressResDto> convertToResult(List<BaseAddress> baseAddresss) {
		List<QueryAddressResDto> queryAddressResDtos = new ArrayList<QueryAddressResDto>();
		if (CollectionUtils.isEmpty(baseAddresss)) {
			return queryAddressResDtos;
		}
		for (BaseAddress baseAddress : baseAddresss) {
			// 操作集合
			QueryAddressResDto queryAddressResDto = new QueryAddressResDto();
			queryAddressResDto.setNationId(baseAddress.getNationId());
			queryAddressResDto.setNationName(cacheService.getDictNameById(baseAddress.getNationId()));
			queryAddressResDto.setProvinceId(baseAddress.getProvinceId());
			queryAddressResDto.setProvinceName(cacheService.getDictNameById(baseAddress.getProvinceId()));
			queryAddressResDto.setCityId(baseAddress.getCityId());
			queryAddressResDto.setCityName(cacheService.getDictNameById(baseAddress.getCityId()));
			queryAddressResDto.setContactPerson(baseAddress.getContactPerson());
			queryAddressResDto.setCountyId(baseAddress.getCountyId());
			queryAddressResDto.setCountyName(cacheService.getDictNameById(baseAddress.getCountyId()));
			queryAddressResDto.setAddressDetail(baseAddress.getAddressDetail());
			queryAddressResDto.setFax(baseAddress.getFax());
			queryAddressResDto.setMobilePhone(baseAddress.getMobilePhone());
			queryAddressResDto.setCreatAt(baseAddress.getCreateAt());
			queryAddressResDto.setCreator(baseAddress.getCreator());
			queryAddressResDto.setTelephone(baseAddress.getTelephone());
			queryAddressResDto.setNote(baseAddress.getNote());
			queryAddressResDto.setSubjectId(baseAddress.getSubjectId());
			queryAddressResDto.setId(baseAddress.getId());

			queryAddressResDto.setAddressType(baseAddress.getAddressType());
			queryAddressResDto.setAddressTypeName(
					ServiceSupport.getValueByBizCode(BizCodeConsts.ADDRESS_TYPE, baseAddress.getAddressType() + ""));
			queryAddressResDto.setState(baseAddress.getState());
			queryAddressResDto.setStateLabel(
					ServiceSupport.getValueByBizCode(BizCodeConsts.ADDRESS_STATE_TYPE, baseAddress.getState() + ""));

			queryAddressResDtos.add(queryAddressResDto);
		}
		return queryAddressResDtos;
	}

	public void updateBaseAddress(BaseAddress baseAddress) {
		int result = baseAddressDao.updateBaseAddressById(baseAddress);
		if (result <= 0) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "更新失败:" + JSONObject.toJSON(baseAddress));
		}
	}

	public void invalidBaseAddressByIds(List<Integer> ids) {
		for (Integer id : ids) {
			invalidBaseAddressById(id);
		}
	}

	public void invalidBaseAddressById(Integer id) {
		BaseAddress baseAddress = loadAndLockEntityById(id);
		baseAddress.setDeleteAt(new Date());
		baseAddress.setDeleter(ServiceSupport.getUser().getChineseName());
		baseAddress.setState(BaseConsts.TWO);
		int result = baseAddressDao.invalidAddressById(baseAddress);
		if (result <= 0) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "作废失败:" + JSONObject.toJSON(baseAddress));
		}
	}

}
