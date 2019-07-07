package com.scfs.service.base.shiro;

import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Maps;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BaseUrlConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.OperateConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.base.entity.BasePermissionDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.domain.CodeValue;
import com.scfs.domain.base.dto.req.BasePermissionReqDto;
import com.scfs.domain.base.dto.resp.BasePermissionResDto;
import com.scfs.domain.base.entity.BasePermission;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.support.ExcelService;
import com.scfs.service.support.ServiceSupport;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/26.
 */
@Service
public class BasePermissionService {

	@Autowired
	private BasePermissionDao basePermissionDao;

	public PageResult<BasePermissionResDto> queryPermissions(BasePermissionReqDto basePermissionReqDto) {
		PageResult<BasePermissionResDto> result = new PageResult<BasePermissionResDto>();
		int offSet = PageUtil.getOffSet(basePermissionReqDto.getPage(), basePermissionReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, basePermissionReqDto.getPer_page());
		List<BasePermission> permissionList = basePermissionDao.queryPermissions(basePermissionReqDto, rowBounds);
		// 添加操作
		List<BasePermissionResDto> basePermissionResList = convertToResult(permissionList);
		result.setItems(basePermissionResList);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), basePermissionReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(basePermissionReqDto.getPage());
		result.setPer_page(basePermissionReqDto.getPer_page());
		return result;
	}

	public PageResult<BasePermissionResDto> queryAllPermission(BasePermissionReqDto basePermissionReqDto) {
		PageResult<BasePermissionResDto> result = new PageResult<BasePermissionResDto>();
		List<BasePermission> permissionList = basePermissionDao.queryAllPermission(basePermissionReqDto);
		if (!CollectionUtils.isEmpty(permissionList)) {
			List<BasePermissionResDto> permResList = Lists.newArrayList();
			for (BasePermission permission : permissionList) {
				permResList.add(permissionConvertToRes(permission));
			}
			result.setItems(permResList);
			return result;
		}
		return result;
	}

	private List<BasePermissionResDto> convertToResult(List<BasePermission> permissionList) {
		List<BasePermissionResDto> basePermissionList = Lists.newArrayList();
		if (CollectionUtils.isEmpty(permissionList)) {
			return basePermissionList;
		}
		for (BasePermission permission : permissionList) {
			BasePermissionResDto permissionRes = permissionConvertToRes(permission);
			// 操作集合
			List<CodeValue> operList = getOperList(permission.getState());
			permissionRes.setOpertaList(operList);
			basePermissionList.add(permissionRes);
		}
		return basePermissionList;
	}

	private List<CodeValue> getOperList(Integer state) {
		// 1.根据状态得到操作列表
		List<String> operNameList = getOperListByState(state);
		// 2.将上述的操作列表通过权限过滤
		List<CodeValue> oprResult = ServiceSupport.getOperListByPermission(operNameList,
				BasePermissionResDto.Operate.operMap);
		return oprResult;
	}

	/**
	 * 根据状态得到操作列表
	 *
	 * @param state
	 * @return
	 */
	private List<String> getOperListByState(Integer state) {
		if (state == null) {
			return null;
		}
		List<String> opertaList = Lists.newArrayList();
		// 状态过滤
		switch (state) {
		// 状态,1表示待提交，2表示已完成，3表示已锁定
		case BaseConsts.ONE:
			opertaList.add(OperateConsts.DETAIL);
			opertaList.add(OperateConsts.EDIT);
			opertaList.add(OperateConsts.SUBMIT);
			opertaList.add(OperateConsts.DELETE);
			break;
		case BaseConsts.TWO:
			opertaList.add(OperateConsts.EDIT);
			opertaList.add(OperateConsts.DETAIL);
			opertaList.add(OperateConsts.LOCK);
			break;
		case BaseConsts.THREE:
			opertaList.add(OperateConsts.DETAIL);
			opertaList.add(OperateConsts.UNLOCK);
			break;
		}
		return opertaList;
	}

	public Integer addPermission(BasePermission basePermission) {
		basePermission.setState(BaseConsts.ONE);// 默认状态1，待提交
		basePermission.setCreateAt(new Date());
		basePermission.setCreator(ServiceSupport.getUser().getChineseName());
		Integer result = basePermissionDao.insert(basePermission);
		if (result < 0) {
			throw new BaseException(ExcMsgEnum.PERMISSIONADD_EXCEPTION);
		}
		return basePermission.getId();
	}

	public void updatePermission(BasePermission basePermission) {
		basePermissionDao.update(basePermission);
	}

	public Result<BasePermissionResDto> queryPermissionById(int id) {
		Result<BasePermissionResDto> result = new Result<BasePermissionResDto>();
		BasePermission basePermission = basePermissionDao.queryPermissionById(id);
		BasePermissionResDto basePermissionResDto = permissionConvertToRes(basePermission);
		result.setItems(basePermissionResDto);
		return result;
	}

	/**
	 * 转换M
	 *
	 * @param permission
	 * @return
	 */
	private BasePermissionResDto permissionConvertToRes(BasePermission permission) {
		BasePermissionResDto permissionRes = new BasePermissionResDto();
		permissionRes.setId(permission.getId());
		permissionRes.setName(permission.getName());
		permissionRes.setMenuLevel(permission.getMenuLevel());
		permissionRes.setMenuLevelName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.PERMISSIONMENULEVEL, permission.getMenuLevel() + ""));
		permissionRes.setType(permission.getType());
		permissionRes
				.setTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.PERMISSIONTYPE, permission.getType() + ""));
		permissionRes
				.setState(ServiceSupport.getValueByBizCode(BizCodeConsts.PERMISSIONSTS, permission.getState() + ""));
		permissionRes.setUrl(permission.getUrl());
		permissionRes.setIsDelete(permission.getIsDelete());
		permissionRes.setParentId(permission.getParentId());
		permissionRes.setOrd(permission.getOrd());
		permissionRes.setCreateAt(permission.getCreateAt());
		permissionRes.setCreator(permission.getCreator());
		// 父级权限
		if (permission.getParentId() != null) {
			BasePermission basePermission = basePermissionDao.queryPermissionById(permission.getParentId());
			if (basePermission != null) {
				permissionRes.setParentName(basePermission.getName());
			}
		}
		return permissionRes;
	}

	public PageResult<BasePermissionResDto> queryDividPermissionListByGroupId(
			BasePermissionReqDto basePermissionReqDto) {
		PageResult<BasePermissionResDto> result = new PageResult<BasePermissionResDto>();
		if (basePermissionReqDto.getPermissionGroupId() == null) {
			result.setSuccess(false);
			result.setMsg("权限组ID不能为空");
			return result;
		}

		int offSet = PageUtil.getOffSet(basePermissionReqDto.getPage(), basePermissionReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, basePermissionReqDto.getPer_page());
		// 获取已经分配的权限列表
		int permissionGroupId = basePermissionReqDto.getPermissionGroupId();
		List<BasePermission> dividPermissionList = basePermissionDao.queryPermissionsByGroupId(permissionGroupId,
				rowBounds);
		if (!CollectionUtils.isEmpty(dividPermissionList)) {
			List<BasePermissionResDto> permResList = Lists.newArrayList();
			for (BasePermission permission : dividPermissionList) {
				BasePermissionResDto basePermissionResDto = permissionConvertToRes(permission);
				List<CodeValue> opertaList = Lists.newArrayList();
				if (permission.getIsDelete() == BaseConsts.ONE) {// 作废
					basePermissionResDto.setState(OperateConsts.INVALID);
				} else {
					basePermissionResDto.setState(OperateConsts.AVAILABLE);
					opertaList.add(new CodeValue(BaseUrlConsts.INVALIDEPERMISSION, OperateConsts.INVALID));
				}
				basePermissionResDto.setOpertaList(opertaList);
				permResList.add(basePermissionResDto);
			}
			result.setItems(permResList);
		}
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), basePermissionReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(basePermissionReqDto.getPage());
		result.setPer_page(basePermissionReqDto.getPer_page());
		return result;
	}

	public PageResult<BasePermissionResDto> queryUnDividPermissionListByGroupId(
			BasePermissionReqDto basePermissionReqDto) {
		PageResult<BasePermissionResDto> result = new PageResult<BasePermissionResDto>();
		try {
			if (basePermissionReqDto.getPermissionGroupId() == null) {
				result.setSuccess(false);
				result.setMsg("权限组ID不能为空");
				return result;
			}
			int offSet = PageUtil.getOffSet(basePermissionReqDto.getPage(), basePermissionReqDto.getPer_page());
			RowBounds rowBounds = new RowBounds(offSet, basePermissionReqDto.getPer_page());
			// 获取未分配的权限列表
			List<BasePermission> unDividPermissionList = basePermissionDao
					.queryPermissionsNotInGroupId(basePermissionReqDto, rowBounds);
			List<BasePermissionResDto> permResList = Lists.newArrayList();
			if (!CollectionUtils.isEmpty(unDividPermissionList)) {

				for (BasePermission permission : unDividPermissionList) {
					BasePermissionResDto basePermissionResDto = permissionConvertToRes(permission);
					List<CodeValue> opertaList = Lists.newArrayList();
					opertaList.add(new CodeValue(BaseUrlConsts.DIVIDPERMISSION, OperateConsts.DIVIDE));
					basePermissionResDto.setOpertaList(opertaList);
					permResList.add(basePermissionResDto);
				}
				result.setItems(permResList);
			} else {
				result.setItems(permResList);
			}
			int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), basePermissionReqDto.getPer_page());
			result.setLast_page(totalPage);
			result.setTotal(CountHelper.getTotalRow());
			result.setCurrent_page(basePermissionReqDto.getPage());
			result.setPer_page(basePermissionReqDto.getPer_page());
		} catch (Exception e) {
			throw e;
			// result.setSuccess(false);
		}
		return result;

	}

	public List<BasePermission> queryFirstPermission() {
		// TODO Auto-generated method stub
		return basePermissionDao.queryFisrtPermission();
	}

	/**
	 * 通过角色获取权限信息
	 * 
	 * @param basePermissionReqDto
	 * @return
	 */
	public PageResult<BasePermissionResDto> queryPermissionsByRoleId(BasePermissionReqDto basePermissionReqDto) {
		PageResult<BasePermissionResDto> result = new PageResult<BasePermissionResDto>();
		int offSet = PageUtil.getOffSet(basePermissionReqDto.getPage(), basePermissionReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, basePermissionReqDto.getPer_page());
		List<BasePermission> permissionList = basePermissionDao.queryPermissionByRoleId(basePermissionReqDto,
				rowBounds);
		// 添加操作
		List<BasePermissionResDto> basePermissionResList = convertToResult(permissionList);
		result.setItems(basePermissionResList);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), basePermissionReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(basePermissionReqDto.getPage());
		result.setPer_page(basePermissionReqDto.getPer_page());
		return result;
	}

	/**
	 * 权限导入Excel 业务逻辑处理
	 *
	 * @param importFile
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void importExcel(MultipartFile importFile) {
		List<BasePermissionResDto> permissionList = Lists.newArrayList();
		Map beans = Maps.newHashMap();
		beans.put("permissionList", permissionList);
		ExcelService.resolverExcel(importFile, "/excel/baseinfo/authority/base_permission.xml", beans);
		// 业务逻辑处理
		permissionList = (List<BasePermissionResDto>) beans.get("permissionList");
		if (CollectionUtils.isNotEmpty(permissionList)) {
			if (permissionList.size() > BaseConsts.IMPORT_EXCEL_ROWS_MAX) {
				throw new BaseException(ExcMsgEnum.IMPORT_EXCEL_ROWS_MAX);
			}
			boolean result = true;
			for (BasePermissionResDto permissionResDto : permissionList) {
				result = validatePermissionInfo(permissionResDto);
			}
			if (result) {
				for (BasePermissionResDto permissionResDto : permissionList) {
					String menuLevelName = permissionResDto.getMenuLevelName();
					String parentName = permissionResDto.getParentName();
					BasePermission basePermission = new BasePermission();
					basePermission.setState(BaseConsts.TWO);// 默认状态1，待提交
					basePermission.setCreateAt(new Date());
					basePermission.setCreator(ServiceSupport.getUser().getChineseName());
					basePermission.setName(permissionResDto.getName());
					basePermission.setUrl(permissionResDto.getUrl());
					basePermission.setOrd(permissionResDto.getOrd());
					Integer type = BaseConsts.ZERO;
					List<CodeValue> codeValueList = ServiceSupport.getCvListByBizCode(BizCodeConsts.PERMISSIONTYPE);
					for (CodeValue codeValue : codeValueList) {
						if (codeValue.getValue().equals(permissionResDto.getTypeName())) {// 获取权限code
							type = Integer.valueOf(codeValue.getCode());
							break;
						}
					}
					basePermission.setType(type);
					if (type.equals(BaseConsts.TWO)) {// 二级菜单
						if (menuLevelName != null && !"".equals(menuLevelName)) {
							Integer levelType = BaseConsts.ZERO;
							List<CodeValue> codeLevelList = ServiceSupport
									.getCvListByBizCode(BizCodeConsts.PERMISSIONMENULEVEL);
							for (CodeValue leveValue : codeLevelList) {
								if (leveValue.getValue().equals(menuLevelName)) {
									levelType = Integer.valueOf(leveValue.getCode());
									break;
								}
							}
							basePermission.setMenuLevel(levelType);
						}
						if (parentName != null && !"".equals(parentName)) {
							Integer parentId = BaseConsts.ZERO;
							List<BasePermission> parementList = queryFirstPermission();
							for (BasePermission parament : parementList) {
								if (parament.getName().equals(parentName)) {
									parentId = parament.getId();
								}
							}
							basePermission.setParentId(parentId);
						}
					}
					basePermissionDao.insert(basePermission);
				}
			}
		} else {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "导入权限信息不能为空");
		}
	}

	/**
	 * 校验并导入权限
	 * 
	 * @param baseGoodsResDto
	 * @return
	 */
	private boolean validatePermissionInfo(BasePermissionResDto permission) {
		boolean result = true;
		String name = permission.getName();
		String url = permission.getUrl();
		Integer ord = permission.getOrd();
		String typeName = permission.getTypeName();
		if (name == null || name.equals("")) {
			result = false;
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "权限名不能为空");
		}
		if (url == null || url.equals("")) {
			result = false;
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "URL不能为空");
		}
		if (ord == null) {
			result = false;
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "排序不能为空");
		}
		if (typeName == null || typeName.equals("")) {
			result = false;
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "权限类型不能为空");
		} else {
			boolean flag = false;
			Integer type = 0;
			List<CodeValue> codeValueList = ServiceSupport.getCvListByBizCode(BizCodeConsts.PERMISSIONTYPE);
			for (CodeValue codeValue : codeValueList) {
				if (codeValue.getValue().equals(typeName)) {
					type = Integer.valueOf(codeValue.getCode());
					flag = true;
					break;
				}
			}
			if (flag) {// 判断权限类型是否存在
				if (type.equals(BaseConsts.TWO)) {// 菜单权限
					boolean level = false;
					Integer levelType = 0;
					String menuLevelName = permission.getMenuLevelName();
					if (menuLevelName == null || menuLevelName.equals("")) {// 判断菜单级别是否为空
						throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "菜单级别不能为空");
					} else {
						List<CodeValue> codeLevelList = ServiceSupport
								.getCvListByBizCode(BizCodeConsts.PERMISSIONMENULEVEL);
						for (CodeValue leveValue : codeLevelList) {
							if (leveValue.getValue().equals(menuLevelName)) {
								levelType = Integer.valueOf(leveValue.getCode());
								level = true;
								break;
							}
						}
						if (level) { // 判断菜单级别是否存在
							if (levelType.equals(BaseConsts.TWO)) {// 二级菜单
								String parentName = permission.getParentName();
								if (parentName == null || parentName.equals("")) {// 判断所属一级菜单是否为空
									result = false;
									throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "所属一级菜单不能为空");
								} else {
									boolean parent = false;
									List<BasePermission> permissionList = queryFirstPermission();
									for (BasePermission basePermission : permissionList) {
										if (basePermission.getName().equals(parentName)) {
											parent = true;
										}
									}
									if (!parent) {
										result = false;
										throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "所属一级菜单不存在");
									}
								}
							}
						} else {
							result = false;
							throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "该菜单级别不存在");
						}
					}
				}
			} else {
				result = false;
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "该权限类型不存在");
			}
		}
		return result;
	}
}
