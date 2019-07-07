package com.scfs.service.base.send;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.CacheKeyConsts;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.base.entity.SenderProjectDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.domain.BaseResult;
import com.scfs.domain.base.dto.req.SenderProjectReqDto;
import com.scfs.domain.base.dto.resp.BaseProjectResDto;
import com.scfs.domain.base.dto.resp.SenderProjectResDto;
import com.scfs.domain.base.entity.BaseProject;
import com.scfs.domain.base.entity.SenderProject;
import com.scfs.domain.result.PageResult;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 *
 *  File: SenderProjectService.java
 *  Description:
 *  TODO
 *  Date,                   Who,
 *  2017年07月19日         Administrator
 *
 * </pre>
 */
@Service
public class SenderProjectService {
	@Autowired
	private SenderProjectDao senderProjectDao;
	@Autowired
	private CacheService cacheService;

	/**
	 * 获取没关联项目
	 * 
	 * @param senderProjectReqDto
	 * @return
	 */
	public PageResult<BaseProjectResDto> queryBaseProject(SenderProjectReqDto senderProjectReqDto) {
		PageResult<BaseProjectResDto> result = new PageResult<BaseProjectResDto>();
		int offSet = PageUtil.getOffSet(senderProjectReqDto.getPage(), senderProjectReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, senderProjectReqDto.getPer_page());
		List<BaseProject> baseProjectList = senderProjectDao.queryBaseProject(senderProjectReqDto, rowBounds);
		List<BaseProjectResDto> baseProjectResDtoList = convertToProjectResult(baseProjectList);
		result.setItems(baseProjectResDtoList);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), senderProjectReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(senderProjectReqDto.getPage());
		result.setPer_page(senderProjectReqDto.getPer_page());
		return result;
	}

	private List<BaseProjectResDto> convertToProjectResult(List<BaseProject> baseProjectList) {
		List<BaseProjectResDto> baseProjectResDtoList = new ArrayList<BaseProjectResDto>();
		if (CollectionUtils.isEmpty(baseProjectList)) {
			return baseProjectResDtoList;
		}
		for (BaseProject baseProject : baseProjectList) {
			BaseProjectResDto baseProjectResDto = new BaseProjectResDto();
			baseProjectResDto.setId(baseProject.getId());
			baseProjectResDto.setBusinessUnit(
					cacheService.getSubjectNameByIdAndKey(baseProject.getBusinessUnitId(), CacheKeyConsts.BUSI_UNIT));
			baseProjectResDto.setProjectName(cacheService.showProjectNameById(baseProject.getId()));
			baseProjectResDtoList.add(baseProjectResDto);
		}
		return baseProjectResDtoList;
	}

	/**
	 * 获取列表
	 * 
	 * @param senderProjectReqDto
	 * @return
	 */
	public PageResult<SenderProjectResDto> querySenderProjectResultsByCon(SenderProjectReqDto senderProjectReqDto) {
		PageResult<SenderProjectResDto> result = new PageResult<SenderProjectResDto>();
		int offSet = PageUtil.getOffSet(senderProjectReqDto.getPage(), senderProjectReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, senderProjectReqDto.getPer_page());
		List<SenderProject> sendersList = senderProjectDao.queryResultsByCon(senderProjectReqDto, rowBounds);
		List<SenderProjectResDto> sendersResList = convertToResult(sendersList);
		result.setItems(sendersResList);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), senderProjectReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(senderProjectReqDto.getPage());
		result.setPer_page(senderProjectReqDto.getPer_page());
		return result;
	}

	/**
	 * 添加
	 * 
	 * @param senderProjectReqDto
	 * @return
	 */
	public BaseResult createSenderProject(SenderProjectReqDto reqDto) {
		BaseResult baseResult = new BaseResult();
		for (Integer projectId : reqDto.getIds()) {
			SenderProject sender = new SenderProject();
			sender.setProjectId(projectId);
			sender.setSenderId(reqDto.getId());
			sender.setIsDelete(BaseConsts.ZERO);
			sender.setCreator(ServiceSupport.getUser() == null ? null : ServiceSupport.getUser().getChineseName());
			sender.setCreateAt(new Date());
			senderProjectDao.insert(sender);
		}
		return baseResult;
	}

	/**
	 * 删除
	 * 
	 * @param reqDto
	 * @return
	 */
	public BaseResult deleteSenderProject(SenderProjectReqDto reqDto) {
		BaseResult baseResult = new BaseResult();
		for (Integer id : reqDto.getIds()) {
			senderProjectDao.deleteById(id);
		}
		return baseResult;
	}

	private List<SenderProjectResDto> convertToResult(List<SenderProject> sendersList) {
		List<SenderProjectResDto> senderManageResDtoList = new ArrayList<SenderProjectResDto>();
		if (CollectionUtils.isEmpty(sendersList)) {
			return senderManageResDtoList;
		}
		for (SenderProject senderProject : sendersList) {
			SenderProjectResDto restDto = convertToResDto(senderProject);
			senderManageResDtoList.add(restDto);
		}
		return senderManageResDtoList;
	}

	public SenderProjectResDto convertToResDto(SenderProject model) {
		SenderProjectResDto result = new SenderProjectResDto();
		result.setId(model.getId());
		result.setSenderId(model.getSenderId());
		result.setProjectId(model.getProjectId());
		result.setProjectName(cacheService.showProjectNameById(model.getProjectId()));
		result.setCreator(model.getCreator());
		result.setCreateAt(model.getCreateAt());
		result.setUpdateAt(model.getUpdateAt());
		result.setIsDelete(model.getIsDelete());
		return result;
	}

}
