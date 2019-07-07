package com.scfs.service.project;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beust.jcommander.internal.Lists;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.OperateConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.base.entity.BaseProjectDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.project.ProjectGoodsDao;
import com.scfs.domain.CodeValue;
import com.scfs.domain.base.dto.req.DistributionGoodsReqDto;
import com.scfs.domain.base.dto.resp.DistributionGoodsResDto;
import com.scfs.domain.base.entity.BaseGoods;
import com.scfs.domain.base.entity.BaseProject;
import com.scfs.domain.project.dto.req.ProjectGoodsSearchReqDto;
import com.scfs.domain.project.dto.resp.GoodsResDto;
import com.scfs.domain.project.dto.resp.ProjectGoodsResDto;
import com.scfs.domain.project.entity.ProjectGoods;
import com.scfs.domain.result.PageResult;
import com.scfs.service.base.goods.DistributionGoodsService;
import com.scfs.service.common.CommonService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

@Service
public class ProjectGoodsService {

	@Autowired
	private ProjectGoodsDao projectGoodsDao;

	@Autowired
	private CacheService cacheService;

	@Autowired
	DistributionGoodsService distributionGoodsService;

	@Autowired
	CommonService commonService;

	@Autowired
	private BaseProjectDao baseProjectDao;

	public ProjectGoods loadAndLockEntityById(int id) {
		ProjectGoods obj = projectGoodsDao.loadAndLockEntityById(id);
		if (obj == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, projectGoodsDao.getClass(), id);
		}
		return obj;
	}

	public void deleteProjectGoodsById(ProjectGoods projectGoods) {
		ProjectGoods vo = loadAndLockEntityById(projectGoods.getId());
		if (vo.getStatus().compareTo(BaseConsts.ONE) != 0) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "状态错误,作废失败");
		}

		projectGoods.setStatus(BaseConsts.TWO);
		projectGoods.setIsDelete(BaseConsts.ONE);
		projectGoods.setDeleter(ServiceSupport.getUser().getChineseName());
		projectGoods.setDeleteAt(new Date());
		int result = projectGoodsDao.updateById(projectGoods);
		if (result <= 0) {
			throw new RuntimeException();
		}
	}

	public void deleteProjectGoodsByIds(List<Integer> ids) {
		for (Integer id : ids) {
			ProjectGoods projectGoods = new ProjectGoods();
			projectGoods.setId(id);
			deleteProjectGoodsById(projectGoods);
		}
	}

	public PageResult<GoodsResDto> queryGoodsToProjectByCon(ProjectGoodsSearchReqDto projectReqDto) {
		PageResult<GoodsResDto> result = new PageResult<GoodsResDto>();
		int offSet = PageUtil.getOffSet(projectReqDto.getPage(), projectReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, projectReqDto.getPer_page());

		List<BaseGoods> baseGoodsList = projectGoodsDao.queryGoodsToProjectByCon(projectReqDto, rowBounds);
		List<GoodsResDto> baseGoodsResDtoList = convertToGoodsResult(baseGoodsList);
		result.setItems(baseGoodsResDtoList);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), projectReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(projectReqDto.getPage());
		result.setPer_page(projectReqDto.getPer_page());
		return result;
	}

	private List<GoodsResDto> convertToGoodsResult(List<BaseGoods> baseGoodsList) {
		List<GoodsResDto> goodsResDtoList = new ArrayList<GoodsResDto>();
		if (CollectionUtils.isEmpty(baseGoodsList)) {
			return goodsResDtoList;
		}
		for (BaseGoods baseGoods1 : baseGoodsList) {
			GoodsResDto goodsResDto = new GoodsResDto();
			BaseGoods baseGoods = cacheService.getGoodsById(baseGoods1.getId());
			goodsResDto.setId(baseGoods.getId());
			goodsResDto.setNumber(baseGoods.getNumber());
			goodsResDto.setName(baseGoods.getName());
			goodsResDto.setType(baseGoods.getType());
			goodsResDto.setBarCode(baseGoods.getBarCode());
			goodsResDto.setSpecification(baseGoods.getSpecification());
			goodsResDto.setUnit(baseGoods.getUnit());
			goodsResDto.setVolume(baseGoods.getVolume());
			goodsResDto.setGrossWeight(baseGoods.getGrossWeight());
			goodsResDto.setNetWeight(baseGoods.getNetWeight());
			goodsResDto.setPurCurrencyTypeValue(
					DecimalUtil.toAmountString(baseGoods.getPurchasePrice()) + ServiceSupport.getValueByBizCode(
							BizCodeConsts.DEFAULT_CURRENCY_TYPE, baseGoods.getPurCurrencyType() + ""));
			goodsResDto.setSaleCurrencyTypeValue(DecimalUtil.toAmountString(baseGoods.getSalePrice()) + ServiceSupport
					.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE, baseGoods.getSaleCurrencyType() + ""));

			goodsResDto.setOpertaList(getGoodsOperList(baseGoods.getStatus()));
			goodsResDtoList.add(goodsResDto);
		}
		return goodsResDtoList;
	}

	private List<CodeValue> getGoodsOperList(Integer state) {
		// 1.根据状态得到操作列表
		List<String> operNameList = getGoodsOperListByState(state);
		// 2.将上述的操作列表通过权限过滤
		List<CodeValue> oprResult = ServiceSupport.getOperListByPermission(operNameList, GoodsResDto.Operate.operMap);
		return oprResult;
	}

	private List<String> getGoodsOperListByState(Integer state) {
		if (state == null) {
			return null;
		}
		List<String> opertaList = Lists.newArrayList(5);
		switch (state) {
		// 状态,1表示可用
		case BaseConsts.ONE:
			opertaList.add(OperateConsts.DIVIDE);
			break;
		}
		return opertaList;
	}

	public void createProjectGoods(List<Integer> goodsIds, Integer projectId) {
		for (Integer goodsId : goodsIds) {
			createProjectGoods(goodsId, projectId);
		}
	}

	public Integer createProjectGoods(Integer goodsId, Integer projectId) {
		ProjectGoods projectGoods = new ProjectGoods();
		projectGoods.setGoodsId(goodsId);
		projectGoods.setProjectId(projectId);
		Integer id = createProjectGoods(projectGoods);
		return id;
	}

	public Integer createProjectGoods(ProjectGoods projectGoods) {
		projectGoods.setStatus(BaseConsts.ONE);
		if (StringUtils.isEmpty(projectGoods.getCreator())) {
			projectGoods
					.setCreator(ServiceSupport.getUser() == null ? null : ServiceSupport.getUser().getChineseName());
		}
		projectGoods.setCreateAt(new Date());
		projectGoods.setIsDelete(BaseConsts.ZERO);
		Integer id = projectGoodsDao.insert(projectGoods);
		return id;
	}

	public ProjectGoods queryEntityByProjectAndGoods(Integer projectId, Integer goodsId) {
		return projectGoodsDao.queryEntityByProjectAndGoods(projectId, goodsId);
	}

	public PageResult<ProjectGoodsResDto> queryProjectGoodsResultsByProjectId(ProjectGoodsSearchReqDto projectReqDto) {

		PageResult<ProjectGoodsResDto> result = new PageResult<ProjectGoodsResDto>();

		int offSet = PageUtil.getOffSet(projectReqDto.getPage(), projectReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, projectReqDto.getPer_page());
		List<ProjectGoods> projectGoodsList = projectGoodsDao
				.queryProjectGoodsResultsByProjectId(projectReqDto.getProjectId(), rowBounds);

		List<ProjectGoodsResDto> projectGoodsResDtoList = convertToResult(projectGoodsList);
		result.setItems(projectGoodsResDtoList);

		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), projectReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(projectReqDto.getPage());
		result.setPer_page(projectReqDto.getPer_page());

		return result;
	}

	public PageResult<DistributionGoodsResDto> queryDistributionListByProject(
			DistributionGoodsReqDto distributionGoods) {
		PageResult<DistributionGoodsResDto> result = new PageResult<DistributionGoodsResDto>();
		result = distributionGoodsService.queryDistributionGoodsListByProjectId(distributionGoods);
		return result;
	}

	/**
	 * 查询融通质押项目未分配铺货商品信息列表
	 * 
	 * @param projectReqDto
	 * @return
	 */
	public PageResult<DistributionGoodsResDto> queryDistributionGoodsResultsByCon(
			DistributionGoodsReqDto distributionGoods) {
		PageResult<DistributionGoodsResDto> result = new PageResult<DistributionGoodsResDto>();
		int offSet = PageUtil.getOffSet(distributionGoods.getPage(), distributionGoods.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, distributionGoods.getPer_page());
		BaseProject baseProject = baseProjectDao.queryEntityById(distributionGoods.getProjectId());
		if (!baseProject.getBizType().equals(BaseConsts.SEVEN)) {
			List<CodeValue> codeList = commonService.getAllOwnCv("PROJECT_SUPPLIER",
					distributionGoods.getProjectId() + "");
			distributionGoods.setCodeList(codeList);
			distributionGoods.setDepartmentId(baseProject.getDepartmentId());
		}
		distributionGoods.setStatus(BaseConsts.TWO);
		distributionGoods.setProjectId(baseProject.getId());
		List<DistributionGoodsResDto> goodsResDtoList = distributionGoodsService
				.queryDistributionGoodsByProjectSupplier(distributionGoods, rowBounds);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), distributionGoods.getPer_page());
		result.setItems(goodsResDtoList);
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(distributionGoods.getPage());
		result.setPer_page(distributionGoods.getPer_page());
		return result;
	}

	private List<ProjectGoodsResDto> convertToResult(List<ProjectGoods> projectGoodsList) {
		List<ProjectGoodsResDto> projectGoodsResDtoList = new ArrayList<ProjectGoodsResDto>();
		if (CollectionUtils.isEmpty(projectGoodsList)) {
			return projectGoodsResDtoList;
		}
		for (ProjectGoods projectGoods : projectGoodsList) {
			ProjectGoodsResDto projectGoodsResDto = convertToResDto(projectGoods);
			projectGoodsResDto.setOpertaList(getOperList(projectGoods.getStatus()));
			projectGoodsResDtoList.add(projectGoodsResDto);
		}
		return projectGoodsResDtoList;
	}

	private List<CodeValue> getOperList(Integer state) {
		// 1.根据状态得到操作列表
		List<String> operNameList = getOperListByState(state);
		// 2.将上述的操作列表通过权限过滤
		List<CodeValue> oprResult = ServiceSupport.getOperListByPermission(operNameList,
				ProjectGoodsResDto.Operate.operMap);
		return oprResult;
	}

	private List<String> getOperListByState(Integer state) {
		if (state == null) {
			return null;
		}
		List<String> opertaList = Lists.newArrayList(5);
		switch (state) {
		// 状态,1表示可用 2表示作废
		case BaseConsts.ONE:
			opertaList.add(OperateConsts.INVALID);
			break;
		}
		return opertaList;
	}

	private ProjectGoodsResDto convertToResDto(ProjectGoods projectGoods) {
		ProjectGoodsResDto projectGoodsResDto = new ProjectGoodsResDto();
		projectGoodsResDto.setId(projectGoods.getId());
		BaseGoods baseGoods = cacheService.getGoodsById(projectGoods.getGoodsId());
		if (baseGoods != null) {
			projectGoodsResDto.setNumber(baseGoods.getNumber());
			projectGoodsResDto.setName(baseGoods.getName());
			projectGoodsResDto.setType(baseGoods.getType());
			projectGoodsResDto.setBarCode(baseGoods.getBarCode());
			projectGoodsResDto.setSpecification(baseGoods.getSpecification());
			projectGoodsResDto.setUnit(baseGoods.getUnit());
			projectGoodsResDto.setPurCurrencyTypeValue(
					DecimalUtil.toAmountString(baseGoods.getPurchasePrice()) + ServiceSupport.getValueByBizCode(
							BizCodeConsts.DEFAULT_CURRENCY_TYPE, baseGoods.getPurCurrencyType() + ""));
			projectGoodsResDto.setSaleCurrencyTypeValue(
					DecimalUtil.toAmountString(baseGoods.getSalePrice()) + ServiceSupport.getValueByBizCode(
							BizCodeConsts.DEFAULT_CURRENCY_TYPE, baseGoods.getSaleCurrencyType() + ""));
		}
		projectGoodsResDto.setCreator(projectGoods.getCreator());
		projectGoodsResDto.setCreateAt(projectGoods.getCreateAt());
		projectGoodsResDto.setDeleter(projectGoods.getDeleter());
		projectGoodsResDto.setDeleteAt(projectGoods.getDeleteAt());
		projectGoodsResDto.setStatus(ServiceSupport.getValueByBizCode(BizCodeConsts.PROJECT_GOODS_STATUS,
				Integer.toString(projectGoods.getStatus())));
		return projectGoodsResDto;
	}

}
