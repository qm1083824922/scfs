package com.scfs.service.base.shiro;

import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BaseUrlConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.OperateConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DateFormatUtils;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.base.entity.BasePermissionGroupDao;
import com.scfs.dao.base.entity.BasePermissionRelationDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.domain.BaseResult;
import com.scfs.domain.CodeValue;
import com.scfs.domain.base.dto.req.BasePermissionGroupReqDto;
import com.scfs.domain.base.dto.req.BasePermissionReqDto;
import com.scfs.domain.base.dto.resp.BasePermissionGroupResDto;
import com.scfs.domain.base.entity.BasePermissionGroup;
import com.scfs.domain.base.entity.BasePermissionRelation;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.support.ServiceSupport;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/9/26.
 */
@Service
public class BasePermissionGroupService {

	@Autowired
	private BasePermissionGroupDao basePermissionGroupDao;
	@Autowired
	private BasePermissionRelationDao basePermissionRelationDao;

	public PageResult<BasePermissionGroupResDto> queryPermissionGroups(
			BasePermissionGroupReqDto basePermissionGroupReqDto) {
		PageResult<BasePermissionGroupResDto> result = new PageResult<BasePermissionGroupResDto>();
		int offSet = PageUtil.getOffSet(basePermissionGroupReqDto.getPage(), basePermissionGroupReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, basePermissionGroupReqDto.getPer_page());
		List<BasePermissionGroup> basePermissionGroups = basePermissionGroupDao
				.queryPermissionsList(basePermissionGroupReqDto, rowBounds);
		// 添加操作
		List<BasePermissionGroupResDto> permissionGroups = convertToResult(basePermissionGroups);
		result.setItems(permissionGroups);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), basePermissionGroupReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(basePermissionGroupReqDto.getPage());
		result.setPer_page(basePermissionGroupReqDto.getPer_page());
		return result;
	}

	public PageResult<BasePermissionGroupResDto> queryPermissionGroupByPer(BasePermissionReqDto basePermissionReqDto) {
		PageResult<BasePermissionGroupResDto> result = new PageResult<BasePermissionGroupResDto>();
		int offSet = PageUtil.getOffSet(basePermissionReqDto.getPage(), basePermissionReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, basePermissionReqDto.getPer_page());
		List<BasePermissionGroup> basePermissionGroups = basePermissionGroupDao
				.queryPermissionsListByPer(basePermissionReqDto, rowBounds);
		// 添加操作
		List<BasePermissionGroupResDto> permissionGroups = convertToResult(basePermissionGroups);
		result.setItems(permissionGroups);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), basePermissionReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(basePermissionReqDto.getPage());
		result.setPer_page(basePermissionReqDto.getPer_page());
		return result;
	}

	public Result<BasePermissionGroupResDto> queryPermissionGroupById(int id) {
		Result<BasePermissionGroupResDto> result = new Result<BasePermissionGroupResDto>();
		BasePermissionGroup basePermissionGroup = basePermissionGroupDao.queryAndLockById(id);
		BasePermissionGroupResDto basePermissionGroupResDto = permissionGroupConvertToRes(basePermissionGroup);
		result.setItems(basePermissionGroupResDto);
		return result;
	}

	private List<BasePermissionGroupResDto> convertToResult(List<BasePermissionGroup> basePermissionGroups) {
		List<BasePermissionGroupResDto> permissionGroupList = Lists.newArrayList();
		if (CollectionUtils.isEmpty(basePermissionGroups)) {
			return permissionGroupList;
		}
		for (BasePermissionGroup basePermissionGroup : basePermissionGroups) {
			BasePermissionGroupResDto permissionGroupResDto = permissionGroupConvertToRes(basePermissionGroup);
			// 操作集合
			List<CodeValue> operList = getOperList(basePermissionGroup.getState());
			permissionGroupResDto.setOpertaList(operList);
			permissionGroupList.add(permissionGroupResDto);
		}

		return permissionGroupList;
	}

	/**
	 * 获取操作列表
	 *
	 * @param state
	 * @return
	 */
	private List<CodeValue> getOperList(Integer state) {
		// 1.根据状态得到操作列表
		List<String> operNameList = getOperListByState(state);
		// 2.将上述的操作列表通过权限过滤
		List<CodeValue> oprResult = ServiceSupport.getOperListByPermission(operNameList,
				BasePermissionGroupResDto.Operate.operMap);
		return oprResult;
	}

	private BasePermissionGroupResDto permissionGroupConvertToRes(BasePermissionGroup basePermissionGroup) {
		if (basePermissionGroup == null) {
			return null;
		}
		BasePermissionGroupResDto basePermissionGroupResDto = new BasePermissionGroupResDto();
		basePermissionGroupResDto.setId(basePermissionGroup.getId());
		basePermissionGroupResDto.setName(basePermissionGroup.getName());
		basePermissionGroupResDto.setCreator(basePermissionGroup.getCreator());
		basePermissionGroupResDto.setCreateAt(basePermissionGroup.getCreateAt());
		basePermissionGroupResDto.setDeleter(basePermissionGroup.getDeleter());
		if (basePermissionGroup.getDeleteAt() != null) {
			basePermissionGroupResDto.setDeleteDate(
					DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, basePermissionGroup.getDeleteAt()));
		}
		basePermissionGroupResDto.setStateName(ServiceSupport.getValueByBizCode(BizCodeConsts.PERMISSIONGROUPSTS,
				Integer.toString(basePermissionGroup.getState())));
		basePermissionGroupResDto.setState(basePermissionGroup.getState());

		return basePermissionGroupResDto;
	}

	/**
	 * 根据状态得到操作列表
	 *
	 * @param state
	 * @return
	 */
	private List<String> getOperListByState(Integer state) {
		List<String> opertaList = Lists.newArrayList();
		if (state == null) {
			return opertaList;
		}
		switch (state) {
		// 状态,1表示待提交，2表示已完成，3表示已锁定
		case BaseConsts.ONE:
			opertaList.add(OperateConsts.DETAIL);
			opertaList.add(OperateConsts.EDIT);
			opertaList.add(OperateConsts.LOCK);
			break;
		case BaseConsts.TWO:
			opertaList.add(OperateConsts.UNLOCK);
			break;
		}

		return opertaList;
	}

	public Integer addPermissionGroup(BasePermissionGroup basePermissionGroup) {
		basePermissionGroup.setCreateAt(new Date());
		basePermissionGroup.setState(BaseConsts.ONE);
		basePermissionGroup.setCreator(ServiceSupport.getUser().getChineseName());
		Integer result = basePermissionGroupDao.insert(basePermissionGroup);
		if (result < 0) {
			throw new RuntimeException();
		}
		return basePermissionGroup.getId();
	}

	public void updatePermissionGroup(BasePermissionGroup basePermissionGroup) {
		basePermissionGroupDao.update(basePermissionGroup);
	}

	@Transactional
	public BaseResult dividePermission(BasePermissionGroupReqDto basePermissionGroupReqDto) {
		BaseResult result = new BaseResult();
		if (basePermissionGroupReqDto == null || basePermissionGroupReqDto.getPermissionGroupId() == null) {
			result.setMsg("权限组ID不能为空");
			return result;
		}
		if (!CollectionUtils.isEmpty(basePermissionGroupReqDto.getPermissionIds())) {
			for (Integer permissionId : basePermissionGroupReqDto.getPermissionIds()) {
				// 1.先锁定，2校验，3插入
				// PermissionGroupId
				basePermissionGroupDao.queryAndLockById(basePermissionGroupReqDto.getPermissionGroupId());// 1.锁定
				BasePermissionRelation basePermissionRelation = new BasePermissionRelation();
				basePermissionRelation.setPermissionGroupId(basePermissionGroupReqDto.getPermissionGroupId());
				basePermissionRelation.setPermissionId(permissionId);
				// 校验
				BasePermissionRelation basePer = basePermissionRelationDao
						.queryPermissionRelationByCon(basePermissionRelation);
				if (basePer != null) {
					throw new BaseException(ExcMsgEnum.PERMISSIONGROUPD_EXCEPTION);
				} else {
					basePermissionRelation.setCreator(ServiceSupport.getUser().getChineseName());
					basePermissionRelation.setCreateAt(new Date());
					basePermissionRelationDao.insert(basePermissionRelation);// 插入
				}
			}
		} else {
			result.setMsg("权限ID不能为空");
			return result;
		}
		return result;
	}

	@Transactional
	public BaseResult invalidPermissionRelation(BasePermissionGroupReqDto basePermissionGroupReqDto) {
		BaseResult result = new BaseResult();
		if (!CollectionUtils.isEmpty(basePermissionGroupReqDto.getIds())) {
			for (Integer pgRelationId : basePermissionGroupReqDto.getIds()) {
				BasePermissionRelation basePermissionRelation = new BasePermissionRelation();
				basePermissionRelation.setId(pgRelationId);
				basePermissionRelation.setIsDelete(BaseConsts.ONE);// 作废
				basePermissionRelation.setDeleter(ServiceSupport.getUser().getChineseName());
				basePermissionRelation.setDeleteAt(new Date());
				basePermissionRelationDao.invalidPermissionRelationById(basePermissionRelation);
			}
		}
		return result;
	}

	public PageResult<BasePermissionGroupResDto> queryDividPermissionGroupList(
			BasePermissionGroupReqDto basePermissionGroupReqDto) {
		PageResult<BasePermissionGroupResDto> result = new PageResult<BasePermissionGroupResDto>();
		if (basePermissionGroupReqDto.getRoleId() == null) {
			result.setSuccess(false);
			result.setMsg("角色ID不能为空");
			return result;
		}
		int offSet = PageUtil.getOffSet(basePermissionGroupReqDto.getPage(), basePermissionGroupReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, basePermissionGroupReqDto.getPer_page());
		// 获取已经分配的权限列表
		int roleId = basePermissionGroupReqDto.getRoleId();
		List<BasePermissionGroup> dividPermissionGroupList = basePermissionGroupDao.queryPermissionGroupByRoleId(roleId,
				rowBounds);
		if (!CollectionUtils.isEmpty(dividPermissionGroupList)) {
			List<BasePermissionGroupResDto> permResList = Lists.newArrayList();
			for (BasePermissionGroup permissionGroup : dividPermissionGroupList) {
				BasePermissionGroupResDto basePermissionGroupResDto = permissionGroupConvertToRes(permissionGroup);
				List<CodeValue> opertaList = Lists.newArrayList();
				if (permissionGroup.getIsDelete() == BaseConsts.ONE) {// 作废
					basePermissionGroupResDto.setStateName(OperateConsts.INVALID);
				} else {
					basePermissionGroupResDto.setStateName(OperateConsts.AVAILABLE);
					opertaList.add(new CodeValue(BaseUrlConsts.INVALIDEPERMISSION, OperateConsts.INVALID));
				}
				basePermissionGroupResDto.setOpertaList(opertaList);
				permResList.add(basePermissionGroupResDto);
			}
			result.setItems(permResList);
		}
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), basePermissionGroupReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(basePermissionGroupReqDto.getPage());
		result.setPer_page(basePermissionGroupReqDto.getPer_page());
		return result;
	}

	public PageResult<BasePermissionGroupResDto> queryUnDividPermissionGroupList(
			BasePermissionGroupReqDto basePermissionGroupReqDto) {
		PageResult<BasePermissionGroupResDto> result = new PageResult<BasePermissionGroupResDto>();
		if (basePermissionGroupReqDto.getRoleId() == null) {
			result.setSuccess(false);
			result.setMsg("角色ID不能为空");
			return result;
		}
		int offSet = PageUtil.getOffSet(basePermissionGroupReqDto.getPage(), basePermissionGroupReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, basePermissionGroupReqDto.getPer_page());
		// 获取未分配的权限列表
		List<BasePermissionGroup> unDividPermissionGroupList = basePermissionGroupDao
				.queryPermissionGroupNotInRoleId(basePermissionGroupReqDto, rowBounds);
		List<BasePermissionGroupResDto> permResList = Lists.newArrayList();
		if (!CollectionUtils.isEmpty(unDividPermissionGroupList)) {
			for (BasePermissionGroup permissionGroup : unDividPermissionGroupList) {
				BasePermissionGroupResDto basePermissionResDto = permissionGroupConvertToRes(permissionGroup);
				List<CodeValue> opertaList = Lists.newArrayList();
				opertaList.add(new CodeValue(BaseUrlConsts.DIVIDPERMISSION, OperateConsts.DIVIDE));
				basePermissionResDto.setOpertaList(opertaList);
				permResList.add(basePermissionResDto);
			}
			result.setItems(permResList);
		} else {
			result.setItems(permResList);
		}
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), basePermissionGroupReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(basePermissionGroupReqDto.getPage());
		result.setPer_page(basePermissionGroupReqDto.getPer_page());
		return result;
	}

}
