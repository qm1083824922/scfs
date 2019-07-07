package com.scfs.service.base.user;

import java.util.ArrayList;
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
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.base.entity.BaseRoleDao;
import com.scfs.dao.base.entity.BaseUserRoleDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.domain.BaseResult;
import com.scfs.domain.CodeValue;
import com.scfs.domain.base.dto.req.BaseUserRoleReqDto;
import com.scfs.domain.base.dto.resp.BaseRoleResDto;
import com.scfs.domain.base.dto.resp.BaseUserRoleResDto;
import com.scfs.domain.base.entity.BaseRole;
import com.scfs.domain.base.entity.BaseUserRoles;
import com.scfs.domain.result.PageResult;
import com.scfs.service.support.ServiceSupport;

@Service
public class BaseUserRoleService {

	@Autowired
	private BaseUserRoleDao baseUserRoleDao;
	@Autowired
	private BaseRoleDao baseRoleDao;

	public PageResult<BaseUserRoleResDto> queryUserRoleAssignedToUser(BaseUserRoleReqDto baseReqDto) {
		PageResult<BaseUserRoleResDto> result = new PageResult<BaseUserRoleResDto>();
		int offSet = PageUtil.getOffSet(baseReqDto.getPage(), baseReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, baseReqDto.getPer_page());
		List<BaseRole> baseRoleList = baseRoleDao.queryDividRoleByUserId(baseReqDto.getUserId(), rowBounds);
		List<BaseUserRoleResDto> baseUserRoleResDtoList = convertToResult(baseRoleList);
		result.setItems(baseUserRoleResDtoList);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), baseReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(baseReqDto.getPage());
		result.setPer_page(baseReqDto.getPer_page());

		return result;
	}

	private List<BaseUserRoleResDto> convertToResult(List<BaseRole> baseUserRolesList) {
		List<BaseUserRoleResDto> baseUserRoleResDtoList = new ArrayList<BaseUserRoleResDto>();
		if (CollectionUtils.isEmpty(baseUserRolesList)) {
			return baseUserRoleResDtoList;
		}
		for (BaseRole baseUserRoles : baseUserRolesList) {
			BaseUserRoleResDto baseUserRoleResDto = convertToResDto(baseUserRoles);
			baseUserRoleResDto.setOpertaList(getOperList(baseUserRoles.getState()));
			baseUserRoleResDtoList.add(baseUserRoleResDto);
		}
		return baseUserRoleResDtoList;
	}

	private List<CodeValue> getOperList(Integer state) {
		// 1.根据状态得到操作列表
		List<String> operNameList = getOperListByState(state);
		// 2.将上述的操作列表通过权限过滤
		List<CodeValue> oprResult = ServiceSupport.getOperListByPermission(operNameList,
				BaseUserRoleResDto.Operate.operMap);
		return oprResult;
	}

	private List<String> getOperListByState(Integer state) {
		if (state == null) {
			return null;
		}
		List<String> opertaList = Lists.newArrayList(5);
		switch (state) {
		// 状态,1表示可用
		case BaseConsts.ONE:
			opertaList.add(OperateConsts.INVALID);
			break;
		}
		return opertaList;
	}

	private BaseUserRoleResDto convertToResDto(BaseRole baseRole) {
		BaseUserRoleResDto baseUserRoleResDto = new BaseUserRoleResDto();
		baseUserRoleResDto.setId(baseRole.getId());
		baseUserRoleResDto.setName(baseRole.getName());
		baseUserRoleResDto.setCreator(baseRole.getCreator());
		baseUserRoleResDto.setCreateAt(baseRole.getCreateAt());
		baseUserRoleResDto.setDeleteAt(baseRole.getDeleteAt());
		baseUserRoleResDto.setDeleter(baseRole.getDeleter());
		baseUserRoleResDto
				.setRoleStatusName(ServiceSupport.getValueByBizCode(BizCodeConsts.ROLESTS, baseRole.getState() + ""));
		if (baseRole.getState() != null) {
			baseUserRoleResDto.setState(
					ServiceSupport.getValueByBizCode(BizCodeConsts.USER_ROLE_STATE, baseRole.getState() + ""));
		}
		return baseUserRoleResDto;
	}

	public PageResult<BaseRoleResDto> queryRoleNotAssignedToUser(BaseUserRoleReqDto baseReqDto) {

		PageResult<BaseRoleResDto> result = new PageResult<BaseRoleResDto>();
		int offSet = PageUtil.getOffSet(baseReqDto.getPage(), baseReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, baseReqDto.getPer_page());
		List<BaseRole> baseRoleList = baseRoleDao.queryUnDividRoleByUserId(baseReqDto, rowBounds);
		List<BaseRoleResDto> baseRoleResDtoList = convertToRoleResult(baseRoleList);
		result.setItems(baseRoleResDtoList);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), baseReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(baseReqDto.getPage());
		result.setPer_page(baseReqDto.getPer_page());
		return result;
	}

	private List<BaseRoleResDto> convertToRoleResult(List<BaseRole> baseRoleList) {
		List<BaseRoleResDto> baseRoleResDtoList = new ArrayList<BaseRoleResDto>();
		if (CollectionUtils.isEmpty(baseRoleList)) {
			return baseRoleResDtoList;
		}
		for (BaseRole baseRole : baseRoleList) {
			BaseRoleResDto baseRoleResDto = new BaseRoleResDto();
			baseRoleResDto.setId(baseRole.getId());
			baseRoleResDto.setName(baseRole.getName());
			baseRoleResDto.setOpertaList(getRoleOperList(baseRole.getState()));
			baseRoleResDtoList.add(baseRoleResDto);
		}
		return baseRoleResDtoList;
	}

	private List<CodeValue> getRoleOperList(Integer state) {
		// 1.根据状态得到操作列表
		List<String> operNameList = getRoleOperListByState(state);
		// 2.将上述的操作列表通过权限过滤
		List<CodeValue> oprResult = ServiceSupport.getOperListByPermission(operNameList,
				BaseRoleResDto.Operate.operMap);
		return oprResult;
	}

	private List<String> getRoleOperListByState(Integer state) {
		if (state == null) {
			return null;
		}
		List<String> opertaList = Lists.newArrayList(5);
		switch (state) {
		// 状态,2表示已完成
		case BaseConsts.TWO:
			opertaList.add(OperateConsts.DIVIDE);
			break;
		}
		return opertaList;
	}

	public int deleteAllBaseUserRole(BaseUserRoleReqDto baseReqDto) {

		if (CollectionUtils.isEmpty(baseReqDto.getIds())) {
			throw new BaseException(ExcMsgEnum.USERSROLE_IDNOTEXIST_EXCEPTION);
		}
		List<BaseUserRoles> userRoleList = new ArrayList<BaseUserRoles>();
		for (int i = 0; i < baseReqDto.getIds().size(); i++) {
			BaseUserRoles baseUserRole = baseUserRoleDao.queryUserRoleById(baseReqDto.getIds().get(i));
			if (baseUserRole.getIsDelete() != null && baseUserRole.getIsDelete() == 1) {
				throw new BaseException(ExcMsgEnum.USERSROLE_EXISTINVALID_EXCEPTION);
			}
			BaseUserRoles userRoles = new BaseUserRoles();
			userRoles.setId(baseReqDto.getIds().get(i));
			userRoles.setDeleter(ServiceSupport.getUser().getChineseName());
			userRoles.setDeleteAt(new Date());
			userRoles.setStatus(BaseConsts.TWO);
			userRoles.setIsDelete(BaseConsts.ONE);
			userRoleList.add(userRoles);
		}
		return baseUserRoleDao.batchUpdateUserRole(userRoleList);
	}

	public BaseResult addBaseUserRole(BaseUserRoleReqDto baseReqDto) {
		BaseResult result = new BaseResult();
		BaseUserRoles baseUserRole = new BaseUserRoles();
		baseUserRole.setUserId(baseReqDto.getUserId());
		baseUserRole.setRoleId(baseReqDto.getId());
		BaseUserRoles con = baseUserRoleDao.queryUserRoleByCon(baseUserRole);
		if (baseReqDto.getUserId() == null) {
			result.setMsg("用户ID不能为空");
			return result;
		}
		if (con != null) {
			result.setMsg("用户已经拥有该角色！");
			return result;
		} else {
			baseUserRole.setCreator(ServiceSupport.getUser().getChineseName());
			baseUserRole.setCreateAt(new Date());
			baseUserRole.setStatus(BaseConsts.ONE);
			baseUserRoleDao.insertUserRole(baseUserRole);
			return result;
		}
	}

	public BaseResult addUserRoleList(BaseUserRoleReqDto baseReqDto) {
		BaseResult result = new BaseResult();
		if (baseReqDto.getUserId() <= 0) {
			result.setMsg("用户ID不能为空");
			return result;
		}
		if (CollectionUtils.isNotEmpty(baseReqDto.getIds())) {
			for (Integer id : baseReqDto.getIds()) {
				BaseUserRoles baseUserRole = new BaseUserRoles();
				baseUserRole.setUserId(baseReqDto.getUserId());
				baseUserRole.setRoleId(id);
				BaseUserRoles con = baseUserRoleDao.queryUserRoleByCon(baseUserRole);
				if (con == null) {
					baseUserRole.setCreator(ServiceSupport.getUser().getChineseName());
					baseUserRole.setCreateAt(new Date());
					baseUserRole.setStatus(BaseConsts.ONE);
					baseUserRoleDao.insertUserRole(baseUserRole);
				} else {
					throw new BaseException(ExcMsgEnum.USERSROLE_EXIST_EXCEPTION);
				}
			}
		}
		result.setMsg("用户分配角色成功");
		result.setSuccess(true);
		return result;
	}

}
