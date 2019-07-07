package com.scfs.web.controller.project;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.domain.BaseResult;
import com.scfs.domain.base.dto.req.DistributionGoodsReqDto;
import com.scfs.domain.base.dto.resp.DistributionGoodsResDto;
import com.scfs.domain.project.dto.req.ProjectGoodsSearchReqDto;
import com.scfs.domain.project.dto.resp.GoodsResDto;
import com.scfs.domain.project.dto.resp.ProjectGoodsResDto;
import com.scfs.domain.project.entity.ProjectGoods;
import com.scfs.domain.result.PageResult;
import com.scfs.service.project.ProjectGoodsService;

@RestController
public class ProjectGoodsController {

	private final static Logger LOGGER = LoggerFactory.getLogger(ProjectGoodsController.class);

	@Autowired
	private ProjectGoodsService projectGoodsService;

	/**
	 * 查询项目关联的商品信息
	 * 
	 * @param projectReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_PROJECT_GOODS, method = RequestMethod.POST)
	public PageResult<ProjectGoodsResDto> queryProjectGoodsResultsByProjectId(ProjectGoodsSearchReqDto projectReqDto) {
		PageResult<ProjectGoodsResDto> result = new PageResult<ProjectGoodsResDto>();
		try {
			result = projectGoodsService.queryProjectGoodsResultsByProjectId(projectReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询信息异常[{}]", JSONObject.toJSON(projectReqDto), e);
			result.setSuccess(false);
			result.setMsg("查询异常，请稍后重试");
		}

		return result;
	}

	@RequestMapping(value = BusUrlConsts.QUERY_DISTRIBUTE_PROJECT_GOODS, method = RequestMethod.POST)
	public PageResult<DistributionGoodsResDto> queryDistributionListByProject(
			DistributionGoodsReqDto distributionGoods) {
		PageResult<DistributionGoodsResDto> result = new PageResult<DistributionGoodsResDto>();
		try {
			result = projectGoodsService.queryDistributionListByProject(distributionGoods);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询信息异常[{}]", JSONObject.toJSON(distributionGoods), e);
			result.setSuccess(false);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 查询融通质押项目未分配铺货商品信息列表
	 * 
	 * @param projectReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_DISTRIBUTE_PROJECT_GOODS_NOTASSIGNED, method = RequestMethod.POST)
	public PageResult<DistributionGoodsResDto> queryDistributionGoodsResultsByCon(
			DistributionGoodsReqDto distributionGoods) {
		PageResult<DistributionGoodsResDto> result = new PageResult<DistributionGoodsResDto>();
		try {
			result = projectGoodsService.queryDistributionGoodsResultsByCon(distributionGoods);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询信息异常[{}]", JSONObject.toJSON(distributionGoods), e);
			result.setSuccess(false);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 删除项目商品
	 * 
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DELETE_PROJECT_GOODS, method = RequestMethod.POST)
	public BaseResult deleteProjectGoods(ProjectGoods projectGoods) {
		BaseResult br = new BaseResult();
		try {
			projectGoodsService.deleteProjectGoodsById(projectGoods);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("删除信息异常[{}]", JSONObject.toJSON(projectGoods), e);
			br.setSuccess(false);
			br.setMsg("删除失败，请重试");
		}
		return br;
	}

	/**
	 * 批量删除项目商品
	 * 
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DELETEALL_PROJECT_GOODS, method = RequestMethod.POST)
	public BaseResult deleteAllProjectGoods(ProjectGoodsSearchReqDto projectReqDto) {
		BaseResult br = new BaseResult();
		try {
			projectGoodsService.deleteProjectGoodsByIds(projectReqDto.getIds());
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("删除信息异常[{}]", JSONObject.toJSON(projectReqDto), e);
			br.setSuccess(false);
			br.setMsg("删除失败，请重试");
		}
		return br;
	}

	/**
	 * 查询项目未分配商品列表
	 * 
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_PROJECT_GOODS_NOTASSIGNED, method = RequestMethod.POST)
	public PageResult<GoodsResDto> queryGoodsToProjectByCon(ProjectGoodsSearchReqDto projectReqDto) {
		PageResult<GoodsResDto> pageResult = new PageResult<GoodsResDto>();
		try {
			pageResult = projectGoodsService.queryGoodsToProjectByCon(projectReqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询异常[{}]", JSONObject.toJSON(projectReqDto), e);
			pageResult.setMsg("查询异常，请稍后重试");
			pageResult.setSuccess(false);
		}
		return pageResult;
	}

	/**
	 * 分配商品到项目
	 * 
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DIVID_PROJECT_GOODS, method = RequestMethod.POST)
	public BaseResult createProjectGoods(ProjectGoods projectGoods) {
		BaseResult br = new BaseResult();
		try {
			projectGoodsService.createProjectGoods(projectGoods);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("分配信息异常[{}]", JSONObject.toJSON(projectGoods), e);
			br.setSuccess(false);
			br.setMsg("分配失败，请重试");
		}
		return br;
	}

	/**
	 * 批量分配商品到项目
	 * 
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DIVIDALL_PROJECT_GOODS, method = RequestMethod.POST)
	public BaseResult createProjectGoods(ProjectGoodsSearchReqDto projectReqDto) {
		BaseResult br = new BaseResult();
		try {
			projectGoodsService.createProjectGoods(projectReqDto.getIds(), projectReqDto.getProjectId());
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("分配信息异常[{}]", JSONObject.toJSON(projectReqDto), e);
			br.setSuccess(false);
			br.setMsg("分配失败，请重试");
		}
		return br;
	}

}
