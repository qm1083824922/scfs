package com.scfs.service.base.shiro;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beust.jcommander.internal.Lists;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.OperateConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DateFormatUtils;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.base.entity.BaseRoleDao;
import com.scfs.dao.base.entity.BaseRolePermissionGroupDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.domain.BaseResult;
import com.scfs.domain.CodeValue;
import com.scfs.domain.base.dto.req.BasePermissionGroupReqDto;
import com.scfs.domain.base.dto.req.BaseRoleReqDto;
import com.scfs.domain.base.dto.resp.BaseRoleResDto;
import com.scfs.domain.base.entity.BaseRole;
import com.scfs.domain.base.entity.BaseRolePermissionGroup;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.support.ServiceSupport;

/**
 * Created by Administrator on 2016/9/26.
 */
@Service
public class BaseRoleService {

	@Autowired
	private BaseRoleDao baseRoleDao;
	@Autowired
	private BaseRolePermissionGroupDao baseRolePermissionGroupDao;

	public PageResult<BaseRoleResDto> queryBaseRoleList(BaseRoleReqDto baseRoleReqDto) {
		PageResult<BaseRoleResDto> result = new PageResult<BaseRoleResDto>();
		int offSet = PageUtil.getOffSet(baseRoleReqDto.getPage(), baseRoleReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, baseRoleReqDto.getPer_page());
		List<BaseRole> baseRoleList = baseRoleDao.queryBaseRoleList(baseRoleReqDto, rowBounds);
		// 添加操作
		List<BaseRoleResDto> baseRoleResList = convertToResult(baseRoleList);
		result.setItems(baseRoleResList);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), baseRoleReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(baseRoleReqDto.getPage());
		result.setPer_page(baseRoleReqDto.getPer_page());
		return result;
	}

	/** 根据权限组查询角色 **/
	public PageResult<BaseRoleResDto> queryRoleByPermissionGroup(BasePermissionGroupReqDto basePermissionReqDto) {
		PageResult<BaseRoleResDto> result = new PageResult<BaseRoleResDto>();
		int offSet = PageUtil.getOffSet(basePermissionReqDto.getPage(), basePermissionReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, basePermissionReqDto.getPer_page());
		List<BaseRole> baseRoleList = baseRoleDao.queryRoleByPermissionGroup(basePermissionReqDto, rowBounds);
		// 添加操作
		List<BaseRoleResDto> baseRoleResList = convertToResult(baseRoleList);
		result.setItems(baseRoleResList);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), basePermissionReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(basePermissionReqDto.getPage());
		result.setPer_page(basePermissionReqDto.getPer_page());
		return result;
	}

	public Result<BaseRoleResDto> queryBaseRoleById(int id) {
		Result<BaseRoleResDto> result = new Result<BaseRoleResDto>();
		BaseRole baseRole = baseRoleDao.queryBaseRoleById(id);
		result.setItems(roleConvertToResDto(baseRole));
		return result;
	}

	private List<BaseRoleResDto> convertToResult(List<BaseRole> baseRoleList) {
		List<BaseRoleResDto> baseRoleResList = Lists.newArrayList();
		if (CollectionUtils.isEmpty(baseRoleList)) {
			return baseRoleResList;
		}
		for (BaseRole baseRole : baseRoleList) {
			BaseRoleResDto baseRoleResDto = roleConvertToResDto(baseRole);
			List<CodeValue> opertaList = getOperList(baseRole.getState());
			baseRoleResDto.setOpertaList(opertaList);
			baseRoleResList.add(baseRoleResDto);
		}
		return baseRoleResList;
	}

	private BaseRoleResDto roleConvertToResDto(BaseRole baseRole) {
		BaseRoleResDto baseRoleResDto = new BaseRoleResDto();
		if (baseRole == null) {
			return baseRoleResDto;
		}
		baseRoleResDto.setCreator(baseRole.getCreator());
		if (baseRole.getCreateAt() != null) {
			baseRoleResDto
					.setCreateDate(DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, baseRole.getCreateAt()));
		}
		baseRoleResDto.setDeleter(baseRole.getDeleter());
		if (baseRole.getDeleteAt() != null) {
			baseRoleResDto
					.setDeleteDate(DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, baseRole.getDeleteAt()));
		}
		if (baseRole.getState() == 3) {
			baseRoleResDto.setLocker(baseRole.getLocker());
			if (baseRole.getLockAt() != null) {
				baseRoleResDto
						.setLockAt(DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, baseRole.getDeleteAt()));
			}
		}
		baseRoleResDto.setId(baseRole.getId());
		baseRoleResDto.setName(baseRole.getName());
		baseRoleResDto.setState(baseRole.getState());
		baseRoleResDto.setStateName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.ROLESTS, Integer.toString(baseRole.getState())));
		return baseRoleResDto;
	}

	private List<CodeValue> getOperList(Integer state) {
		// 1.根据状态得到操作列表
		List<String> operNameList = getOperListByState(state);
		// 2.将上述的操作列表通过权限过滤
		List<CodeValue> oprResult = ServiceSupport.getOperListByPermission(operNameList,
				BaseRoleResDto.Operate.operMap);
		return oprResult;
	}

	private List<String> getOperListByState(Integer state) {
		if (state == null) {
			return null;
		}
		List<String> opertaList = Lists.newArrayList();
		switch (state) {
		// 状态,1表示待提交，2表示已完成，3表示已锁定
		case BaseConsts.TWO:
			opertaList.add(OperateConsts.EDIT);
			opertaList.add(OperateConsts.DETAIL);
			opertaList.add(OperateConsts.LOCK);
			break;
		case BaseConsts.THREE:
			opertaList.add(OperateConsts.UNLOCK);
			break;
		}
		return opertaList;
	}

	public Integer addBaseRole(BaseRole baseRole) {
		baseRole.setCreateAt(new Date());
		baseRole.setCreator(ServiceSupport.getUser().getChineseName());
		baseRole.setState(BaseConsts.TWO);// 一创建，就是完成状态
		Integer result = baseRoleDao.insert(baseRole);
		if (result < 0) {
			throw new BaseException(ExcMsgEnum.ROLEADD_EXCEPTION);
		}
		return baseRole.getId();
	}

	public void updateBaseRole(BaseRole baseRole) {
		baseRoleDao.update(baseRole);
	}

	public List<BaseRole> queryRoleListByUserName(String userName) {
		return baseRoleDao.queryBaseRoleListByUserName(userName);
	}

	public BaseResult dividePermissionGroup(BaseRoleReqDto baseRoleReqDto) {
		BaseResult result = new BaseResult();
		if (baseRoleReqDto == null || baseRoleReqDto.getRoleId() == null) {
			result.setMsg("角色ID不能为空");
			result.setSuccess(false);
			return result;
		}
		if (!CollectionUtils.isEmpty(baseRoleReqDto.getIds())) {
			for (Integer permissionGroupId : baseRoleReqDto.getIds()) {
				BaseRolePermissionGroup baseRolePermissionGroup = new BaseRolePermissionGroup();
				baseRolePermissionGroup.setPermissionGroupId(permissionGroupId);
				baseRolePermissionGroup.setRoleId(baseRoleReqDto.getRoleId());
				baseRolePermissionGroup.setCreator(ServiceSupport.getUser().getChineseName());
				baseRolePermissionGroup.setCreateAt(new Date());
				baseRolePermissionGroup.setIsDelete(BaseConsts.ZERO);
				BaseRolePermissionGroup permissionRelation = baseRolePermissionGroupDao
						.queryRolePermissionGroup(baseRolePermissionGroup);
				if (permissionRelation != null) {
					throw new BaseException(ExcMsgEnum.PERMISSIONGROUPD_ROLE_EXCEPTION);
				} else {
					baseRolePermissionGroupDao.insert(baseRolePermissionGroup);
				}
			}
		} else {
			result.setMsg("角色ID不能为空");
			result.setSuccess(false);
			return result;
		}
		return result;
	}

	public BaseResult invalidPermissionGroupRelation(BaseRoleReqDto baseRoleReqDto) {
		BaseResult result = new BaseResult();
		if (!CollectionUtils.isEmpty(baseRoleReqDto.getIds())) {
			for (Integer pGroupRoleId : baseRoleReqDto.getIds()) {
				BaseRolePermissionGroup peor = baseRolePermissionGroupDao.queryRolePermissionGroupById(pGroupRoleId);
				if (peor.getIsDelete() != null && peor.getIsDelete() == 1) {
					throw new BaseException(ExcMsgEnum.PERMISSIONGROUPD_INVALID_EXCEPTION);
				}
				BaseRolePermissionGroup baseRolePermissionGroup = new BaseRolePermissionGroup();
				baseRolePermissionGroup.setId(pGroupRoleId);
				baseRolePermissionGroup.setIsDelete(BaseConsts.ONE);// 作废
				baseRolePermissionGroup.setDeleter(ServiceSupport.getUser().getChineseName());
				baseRolePermissionGroup.setDeleteAt(new Date());
				baseRolePermissionGroupDao.invalidRolePermissionGroup(baseRolePermissionGroup);
			}
		}
		return result;
	}
}
