package com.scfs.service.base.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.base.entity.BaseSubjectDao;
import com.scfs.dao.base.entity.BaseUserSubjectDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.domain.BaseResult;
import com.scfs.domain.base.dto.req.BaseUserSubjectReqDto;
import com.scfs.domain.base.dto.resp.BaseUserSubjectResDto;
import com.scfs.domain.base.entity.BaseSubject;
import com.scfs.domain.base.entity.BaseUserSubject;
import com.scfs.domain.result.PageResult;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 *  用户基础信息关系service
 *  File: BaseUserSubejctService.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年05月31日				Administrator
 *
 * </pre>
 */
@Service
public class BaseUserSubjectService {

	@Autowired
	private BaseUserSubjectDao baseUserSubjectDao;
	@Autowired
	private BaseSubjectDao baseSubjectDao;
	@Autowired
	private CacheService cacheService;

	/**
	 * 获取信息
	 * 
	 * @param baseReqDto
	 * @return
	 */
	public PageResult<BaseUserSubjectResDto> queryUserSubjectByCon(BaseUserSubjectReqDto baseReqDto) {
		PageResult<BaseUserSubjectResDto> result = new PageResult<BaseUserSubjectResDto>();
		int offSet = PageUtil.getOffSet(baseReqDto.getPage(), baseReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, baseReqDto.getPer_page());
		List<BaseUserSubject> baseSubjectList = baseUserSubjectDao.queryUserSubjectByCon(baseReqDto, rowBounds);
		List<BaseUserSubjectResDto> baseUserSubjectResDtoList = convertToResult(baseSubjectList);
		result.setItems(baseUserSubjectResDtoList);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), baseReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(baseReqDto.getPage());
		result.setPer_page(baseReqDto.getPer_page());
		return result;
	}

	private List<BaseUserSubjectResDto> convertToResult(List<BaseUserSubject> modelList) {
		List<BaseUserSubjectResDto> baseUserSubjectList = new ArrayList<BaseUserSubjectResDto>();
		if (CollectionUtils.isEmpty(modelList)) {
			return baseUserSubjectList;
		}
		for (BaseUserSubject baseUserSubject : modelList) {
			BaseUserSubjectResDto baseUserSubjectResDto = convertToResDto(baseUserSubject);
			baseUserSubjectList.add(baseUserSubjectResDto);
		}
		return baseUserSubjectList;
	}

	private BaseUserSubjectResDto convertToResDto(BaseUserSubject model) {
		BaseSubject baseSubject = cacheService.getBaseSubjectById(model.getSubjectId());
		BaseUserSubjectResDto result = new BaseUserSubjectResDto();
		result.setId(model.getId());
		result.setSubjectId(model.getSubjectId());
		result.setSubjectName(baseSubject.getNoName());
		result.setOperater(model.getOperater());
		if (null != model.getOperater()) {
			result.setOperaterStr(
					ServiceSupport.getValueByBizCode(BizCodeConsts.WAREHOUSE_OPERATE, model.getOperater() + ""));
		}
		result.setCreatorId(model.getCreatorId());
		result.setCreator(cacheService.getUserByid(model.getCreatorId()).getChineseName());
		result.setCreateAt(model.getCreateAt());
		result.setAssignerId(model.getAssignerId());
		result.setAssigner(cacheService.getUserByid(model.getAssignerId()).getChineseName());
		result.setAssignAt(model.getAssignAt());
		result.setDeleterId(model.getDeleterId());
		if (model.getState() != null) {
			result.setStateName(ServiceSupport.getValueByBizCode(BizCodeConsts.USER_ROLE_STATE, model.getState() + ""));
		}
		return result;
	}

	/**
	 * 未分配仓库信息
	 * 
	 * @param querySubjectReqDto
	 * @return
	 */
	public PageResult<BaseSubject> querySubjectByCond(BaseUserSubjectReqDto querySubjectReqDto) {
		PageResult<BaseSubject> pageResult = new PageResult<BaseSubject>();
		int offSet = PageUtil.getOffSet(querySubjectReqDto.getPage(), querySubjectReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, querySubjectReqDto.getPer_page());
		List<BaseSubject> querySubjectResDtos = baseSubjectDao.queryUnDividSubjectByUserId(querySubjectReqDto,
				rowBounds);
		for (BaseSubject baseSubject : querySubjectResDtos) {
			baseSubject.setChineseName(baseSubject.getNoName());
		}
		pageResult.setItems(querySubjectResDtos);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), querySubjectReqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(querySubjectReqDto.getPage());
		pageResult.setPer_page(querySubjectReqDto.getPer_page());
		return pageResult;
	}

	/**
	 * 批量删除
	 * 
	 * @param baseReqDto
	 * @return
	 */
	public BaseResult deleteAllBaseUserSubject(BaseUserSubjectReqDto baseReqDto) {
		if (CollectionUtils.isEmpty(baseReqDto.getIds())) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "用户仓库ID不能为空");
		}
		for (Integer id : baseReqDto.getIds()) {
			baseUserSubjectDao.queryUserSubjectById(id);

			BaseUserSubject baseUserSubject = new BaseUserSubject();
			baseUserSubject.setId(id);
			baseUserSubject.setDeleter(ServiceSupport.getUser().getChineseName());
			baseUserSubject.setDeleteAt(new Date());
			baseUserSubject.setState(BaseConsts.TWO);
			baseUserSubject.setIsDelete(BaseConsts.ONE);
			baseUserSubjectDao.updateUserSubject(baseUserSubject);
		}
		return new BaseResult();
	}

	/**
	 * 分配仓库
	 * 
	 * @param baseReqDto
	 * @return
	 */
	public BaseResult addBaseUserSubject(BaseUserSubject baseUserSubject) {
		BaseResult result = new BaseResult();
		if (baseUserSubject.getUserId() == null) {
			result.setMsg("用户ID不能为空");
			return result;
		}
		baseUserSubject.setCreatorId(ServiceSupport.getUser().getId());
		baseUserSubject.setCreateAt(new Date());
		baseUserSubject.setAssignerId(ServiceSupport.getUser().getId());
		baseUserSubject.setAssignAt(new Date());
		baseUserSubject.setState(BaseConsts.ONE);
		baseUserSubject.setSubjectType(baseUserSubject.getSubjectType());
		baseUserSubjectDao.insertUserSubject(baseUserSubject);
		result.setMsg("用户分配仓库成功");
		result.setSuccess(true);
		return result;
	}

	/**
	 * 分配仓库
	 * 
	 * @param baseReqDto
	 * @return
	 */
	public BaseResult addBaseUserSubjectList(BaseUserSubjectReqDto baseReqDto) {
		BaseResult result = new BaseResult();
		if (CollectionUtils.isNotEmpty(baseReqDto.getUserSubjectList())) {
			for (BaseUserSubject model : baseReqDto.getUserSubjectList()) {
				BaseUserSubject subject = new BaseUserSubject();
				subject.setUserId(baseReqDto.getUserId());
				subject.setSubjectId(model.getSubjectId());
				subject.setCreatorId(ServiceSupport.getUser().getId());
				subject.setCreateAt(new Date());
				subject.setAssignerId(ServiceSupport.getUser().getId());
				subject.setAssignAt(new Date());
				subject.setState(BaseConsts.ONE);
				subject.setOperater(model.getOperater());
				subject.setSubjectType(baseReqDto.getSubjectType());
				baseUserSubjectDao.insertUserSubject(subject);
			}
		}
		result.setMsg("用户分配仓库成功");
		result.setSuccess(true);
		return result;
	}

	/**
	 * 批量删除
	 * 
	 * @param baseReqDto
	 * @return
	 */
	public BaseResult deleteAllBaseUserSubjectBySubjectId(BaseUserSubjectReqDto baseReqDto) {
		if (CollectionUtils.isEmpty(baseReqDto.getIds())) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "用户主体ID不能为空");
		}
		for (Integer id : baseReqDto.getIds()) {
			BaseUserSubject baseUserSubject = new BaseUserSubject();
			baseUserSubject.setUserId(id);
			baseUserSubject.setSubjectId(baseReqDto.getSubjectId());
			baseUserSubject.setDeleter(ServiceSupport.getUser().getChineseName());
			baseUserSubject.setDeleteAt(new Date());
			baseUserSubject.setState(BaseConsts.TWO);
			baseUserSubject.setIsDelete(BaseConsts.ONE);
			baseUserSubjectDao.deleteUserSubject(baseUserSubject);
		}
		return new BaseResult();
	}

	/**
	 * 主体分配用户
	 * 
	 * @param baseReqDto
	 * @return
	 */
	public BaseResult addBaseUserSubjectListBySubjectId(BaseUserSubjectReqDto baseReqDto) {
		BaseResult result = new BaseResult();
		if (CollectionUtils.isNotEmpty(baseReqDto.getUserSubjectList())) {
			for (BaseUserSubject model : baseReqDto.getUserSubjectList()) {
				BaseUserSubject baseUserSubject = new BaseUserSubject();
				BaseSubject baseSubject = baseSubjectDao.queryEntityById(baseReqDto.getSubjectId());
				baseUserSubject.setUserId(model.getUserId());
				baseUserSubject.setSubjectId(baseReqDto.getSubjectId());
				baseUserSubject.setCreatorId(ServiceSupport.getUser().getId());
				baseUserSubject.setCreateAt(new Date());
				baseUserSubject.setAssignerId(ServiceSupport.getUser().getId());
				baseUserSubject.setAssignAt(new Date());
				baseUserSubject.setState(BaseConsts.ONE);
				baseUserSubject.setOperater(model.getOperater());
				baseUserSubject.setSubjectType(baseSubject.getSubjectType());
				baseUserSubjectDao.insertUserSubject(baseUserSubject);
			}
		}
		result.setMsg("主体分配用户成功");
		result.setSuccess(true);
		return result;
	}
}
