package com.scfs.service.base.department;

import com.beust.jcommander.internal.Lists;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.OperateConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.base.entity.BaseDepartmentDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.domain.CodeValue;
import com.scfs.domain.base.dto.req.BaseDepartmentReqDto;
import com.scfs.domain.base.dto.resp.BaseDepartmentResDto;
import com.scfs.domain.base.entity.BaseDepartment;
import com.scfs.domain.result.PageResult;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BaseDepartMentService {

	@Autowired
	private BaseDepartmentDao baseDepartmentDao;

	@Autowired
	private CacheService cacheService;

	public PageResult<BaseDepartmentResDto> queryBaseUserList(BaseDepartmentReqDto baseDepartmentReqDto) {
		PageResult<BaseDepartmentResDto> result = new PageResult<BaseDepartmentResDto>();
		int offSet = PageUtil.getOffSet(baseDepartmentReqDto.getPage(), baseDepartmentReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, baseDepartmentReqDto.getPer_page());
		List<BaseDepartment> baseDepartmentList = baseDepartmentDao.queryDepartMentByCon(baseDepartmentReqDto,
				rowBounds);
		List<BaseDepartmentResDto> baseUserResDtoList = convertToResult(baseDepartmentList);
		result.setItems(baseUserResDtoList);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), baseDepartmentReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(baseDepartmentReqDto.getPage());
		result.setPer_page(baseDepartmentReqDto.getPer_page());
		return result;
	}

	public void delete(Integer id) {
		BaseDepartment department = baseDepartmentDao.selectById(id);
		List<BaseDepartment> baseDepartMentList = baseDepartmentDao.queryDepartMentByParentId(department.getId());
		if (CollectionUtils.isNotEmpty(baseDepartMentList)) {
			throw new BaseException(ExcMsgEnum.DEPARTMENT_DELETE_NODE_ERROR);
		} else {
			int i = baseDepartmentDao.deleteById(id);
			if (i < 0) {
				throw new BaseException(ExcMsgEnum.DEPARTMENT_DELETE_ERROR);
			}
		}

	}

	public void update(BaseDepartment baseDepartment) {
		BaseDepartment department = baseDepartmentDao.selectById(baseDepartment.getId());
		List<BaseDepartment> baseDepartments = baseDepartmentDao.queryByNumberAndName(baseDepartment);
		if (CollectionUtils.isEmpty(baseDepartments)) {
			if (baseDepartment.getParentId() != null) {
				if (baseDepartment.getParentId().intValue() != department.getParentId().intValue()) {
					BaseDepartment departmentParent = baseDepartmentDao.selectById(baseDepartment.getParentId()); //
					if (baseDepartment.getParentId() == baseDepartment.getId()) {
						throw new BaseException(ExcMsgEnum.DEPARTMENT_RELATIVE_TERROR);
					} else {
						judgeLevel(baseDepartment.getParentId(), baseDepartment.getId());
						if (department.getLevel() - departmentParent.getLevel() != 1) {
							changeLevel(department.getId(), departmentParent.getLevel() + 1);
							baseDepartment.setLevel(departmentParent.getLevel() + 1);
						}
					}
				}
			} else {
				throw new BaseException(ExcMsgEnum.DEPARTMENT_UPDATE_ERROR);
			}
			int i = baseDepartmentDao.updateById(baseDepartment);
			if (i < 0) {
				throw new BaseException(ExcMsgEnum.DEPARTMENT_UPDATE_ERROR);
			}
		} else {
			throw new BaseException(ExcMsgEnum.DEPARTMENT_EXIST_TERROR);
		}

	}

	public void judgeLevel(Integer id, Integer parentId) {
		BaseDepartment baseDepartMent = baseDepartmentDao.selectById(id);
		if (baseDepartMent.getParentId() == null)
			return;
		else {
			if (baseDepartMent.getParentId() == parentId) {
				throw new BaseException(ExcMsgEnum.DEPARTMENT_RELATIVE_ERROR);
			}
			judgeLevel(baseDepartMent.getParentId(), parentId);
		}

	}

	// 循环改变子节点的level
	public void changeLevel(Integer parentId, Integer level) {
		List<BaseDepartment> baseDepartMentList = baseDepartmentDao.queryDepartMentByParentId(parentId);
		if (CollectionUtils.isEmpty(baseDepartMentList))
			return;
		else {
			for (int i = 0; i < baseDepartMentList.size(); i++) {
				baseDepartMentList.get(i).setLevel(level + 1);
				baseDepartmentDao.updateById(baseDepartMentList.get(i));
				changeLevel(baseDepartMentList.get(i).getId(), level + 1);
			}
		}
	}

	public void add(BaseDepartment baseDepartment) {
		List<BaseDepartment> baseDepartments = baseDepartmentDao.queryByNumberAndName(baseDepartment);
		if (CollectionUtils.isNotEmpty(baseDepartments)) {
			throw new BaseException(ExcMsgEnum.DEPARTMENT_EXIST_TERROR);
		}
		BaseDepartment department = baseDepartmentDao.selectById(baseDepartment.getParentId());
		baseDepartment.setLevel(department.getLevel() + 1);
		baseDepartment.setCreateAt(new Date());
		baseDepartment.setCreator(ServiceSupport.getUser().getChineseName());
		int i = baseDepartmentDao.insert(baseDepartment);
		if (i < 0) {
			throw new BaseException(ExcMsgEnum.DEPARTMENT_ADD_ERROR);
		}
	}

	public BaseDepartmentResDto detail(Integer id) {
		BaseDepartment baseDepartment = baseDepartmentDao.selectById(id);
		BaseDepartmentResDto baseDepartmentResDto = convertToResDto(baseDepartment);
		return baseDepartmentResDto;
	}

	public BaseDepartment edit(Integer id) {
		BaseDepartment baseDepartment = baseDepartmentDao.selectById(id);
		baseDepartment.setParentName(cacheService.getBaseDepartmentById(baseDepartment.getParentId()).getNameNo());
		return baseDepartment;
	}

	private List<BaseDepartmentResDto> convertToResult(List<BaseDepartment> baseDepartmentList) {
		List<BaseDepartmentResDto> baseDepartmentResDtoList = new ArrayList<BaseDepartmentResDto>();
		if (CollectionUtils.isEmpty(baseDepartmentList)) {
			return baseDepartmentResDtoList;
		}
		for (BaseDepartment baseDepartment : baseDepartmentList) {
			BaseDepartmentResDto baseDepartmentResDto = convertToResDto(baseDepartment);
			baseDepartmentResDto.setOpertaList(getOperList());
			baseDepartmentResDtoList.add(baseDepartmentResDto);
		}
		return baseDepartmentResDtoList;
	}

	private BaseDepartmentResDto convertToResDto(BaseDepartment baseDepartment) {
		BaseDepartmentResDto baseDepartmentResDto = new BaseDepartmentResDto();
		baseDepartmentResDto.setCreateAt(baseDepartment.getCreateAt());
		baseDepartmentResDto.setCreator(baseDepartment.getCreator());
		baseDepartmentResDto.setName(baseDepartment.getName());
		baseDepartmentResDto.setNumber(baseDepartment.getNumber());
		baseDepartmentResDto.setId(baseDepartment.getId());
		BaseDepartment pd = cacheService.getBaseDepartmentById(baseDepartment.getParentId());
		if (pd != null) {
			baseDepartmentResDto.setParentName(pd.getNameNo());
		}
		return baseDepartmentResDto;
	}

	private List<CodeValue> getOperList() {
		// 1.根据状态得到操作列表
		List<String> operNameList = getOperListByState();
		// 2.将上述的操作列表通过权限过滤
		List<CodeValue> oprResult = ServiceSupport.getOperListByPermission(operNameList,
				BaseDepartmentResDto.Operate.operMap);
		return oprResult;
	}

	private List<String> getOperListByState() {

		List<String> opertaList = Lists.newArrayList(5);
		opertaList.add(OperateConsts.DETAIL);
		opertaList.add(OperateConsts.EDIT);
		opertaList.add(OperateConsts.DELETE);

		return opertaList;
	}

}
