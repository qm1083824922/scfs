package com.scfs.service.base.goods;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Maps;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.CacheKeyConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.OperateConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.base.entity.BaseGoodsDao;
import com.scfs.dao.base.entity.DistributionGoodsDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.po.PurchaseOrderTitleDao;
import com.scfs.domain.BaseResult;
import com.scfs.domain.CodeValue;
import com.scfs.domain.audit.entity.AuditNode;
import com.scfs.domain.base.dto.req.DistributionGoodsReqDto;
import com.scfs.domain.base.dto.resp.DistributionGoodsResDto;
import com.scfs.domain.base.entity.BaseGoods;
import com.scfs.domain.base.entity.DistributionGoods;
import com.scfs.domain.common.dto.req.FileAttachSearchReqDto;
import com.scfs.domain.common.entity.FileAttach;
import com.scfs.domain.interf.entity.PMSSupplierBind;
import com.scfs.domain.pay.entity.PmsStoreOut;
import com.scfs.domain.po.dto.resp.PoFileAttachRespDto;
import com.scfs.domain.po.entity.PurchaseOrderTitle;
import com.scfs.domain.project.entity.ProjectItemFileAttach;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.audit.DistributionGoodsAuditService;
import com.scfs.service.base.auditFlow.AuditFlowService;
import com.scfs.service.common.CommonParamValidate;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ExcelService;
import com.scfs.service.support.FileUploadService;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 *  铺货商品
 *  File: DistributionGoodsService.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年05月02日				Administrator
 *
 * </pre>
 */
@Service
public class DistributionGoodsService {
	@Autowired
	private DistributionGoodsDao distributionGoodsDao;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private CommonParamValidate commonParamValidate;
	@Autowired
	private DistributionGoodsAuditService distributionGoodsAuditService;
	@Autowired
	private PurchaseOrderTitleDao purchaseOrderTitleDao;
	@Autowired
	private FileUploadService fileUploadService;
	@Autowired
	private BaseGoodsDao baseGoodsDao;
	@Autowired
	private AuditFlowService auditFlowService;

	/**
	 * 分页数据
	 * 
	 * @param distributionGoods
	 * @return
	 */
	public PageResult<DistributionGoodsResDto> queryDistributionGoodsList(DistributionGoodsReqDto distributionGoods) {
		PageResult<DistributionGoodsResDto> result = new PageResult<DistributionGoodsResDto>();
		int offSet = PageUtil.getOffSet(distributionGoods.getPage(), distributionGoods.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, distributionGoods.getPer_page());
		List<DistributionGoods> goodsList = distributionGoodsDao.queryDistributionGoodsList(distributionGoods,
				rowBounds);
		List<DistributionGoodsResDto> baseGoodsResList = convertToResult(goodsList);
		result.setItems(baseGoodsResList);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), distributionGoods.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(distributionGoods.getPage());
		result.setPer_page(distributionGoods.getPer_page());
		return result;
	}

	/**
	 * 获取所有数据
	 * 
	 * @param distributionGoods
	 * @return
	 */
	public List<DistributionGoodsResDto> queryDistributionGoodsListAll(DistributionGoodsReqDto distributionGoods) {
		List<DistributionGoodsResDto> result = new ArrayList<DistributionGoodsResDto>();
		result = convertToResult(distributionGoodsDao.queryDistributionGoodsList(distributionGoods));
		return result;
	}

	private List<DistributionGoodsResDto> convertToResult(List<DistributionGoods> goodsList) {
		List<DistributionGoodsResDto> baseGoodsList = new ArrayList<DistributionGoodsResDto>();
		if (CollectionUtils.isEmpty(goodsList)) {
			return baseGoodsList;
		}
		for (DistributionGoods goods : goodsList) {
			DistributionGoodsResDto goodsRes = convertToResDto(goods);
			// 操作集合
			List<CodeValue> operList = getOperList(goods.getStatus());
			goodsRes.setOpertaList(operList);
			baseGoodsList.add(goodsRes);
		}
		return baseGoodsList;
	}

	public DistributionGoodsResDto convertToResDto(DistributionGoods goods) {
		DistributionGoodsResDto goodsRes = new DistributionGoodsResDto();
		goodsRes.setId(goods.getId());
		goodsRes.setNumber(goods.getNumber());
		goodsRes.setName(goods.getName());
		goodsRes.setDepartmentId(goods.getDepartmentId());
		goodsRes.setDepartmentName(cacheService.getBaseDepartmentById(goods.getDepartmentId()).getNameNo());
		goodsRes.setSupplierId(goods.getSupplierId());
		goodsRes.setSupplierName(cacheService.getSubjectNcByIdAndKey(goods.getSupplierId(), CacheKeyConsts.CUSTOMER));
		goodsRes.setPledge(goods.getPledge());
		goodsRes.setType(goods.getType());
		goodsRes.setBarCode(goods.getBarCode());
		goodsRes.setSpecification(goods.getSpecification());
		goodsRes.setTaxClassification(goods.getTaxClassification());
		goodsRes.setTaxRate(goods.getTaxRate());
		goodsRes.setUnit(goods.getUnit());
		goodsRes.setVolume(goods.getVolume());
		goodsRes.setBroad(goods.getBroad());
		goodsRes.setGrow(goods.getGrow());
		goodsRes.setGrossWeight(goods.getGrossWeight());
		goodsRes.setNetWeight(goods.getNetWeight());
		goodsRes.setPurchasePrice(goods.getPurchasePrice());
		goodsRes.setPurCurrencyType(goods.getPurCurrencyType());
		goodsRes.setPurchasePriceValue(DecimalUtil.toAmountString(goods.getPurchasePrice()) + ServiceSupport
				.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE, goods.getPurCurrencyType() + ""));
		goodsRes.setSalePrice(goods.getSalePrice());
		goodsRes.setSaleCurrencyType(goods.getSaleCurrencyType());
		goodsRes.setSalePriceValue(DecimalUtil.toAmountString(goods.getSalePrice()) + ServiceSupport
				.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE, goods.getSaleCurrencyType() + ""));
		goodsRes.setSaleCurrencyTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
				goods.getSaleCurrencyType() + ""));
		goodsRes.setPurCurrencyTypeName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE, goods.getPurCurrencyType() + ""));
		goodsRes.setCreator(goods.getCreator());
		goodsRes.setCreatorAt(goods.getCreateAt());
		goodsRes.setDeleter(goods.getDeleter());
		goodsRes.setDeleteAt(goods.getDeleteAt());
		goodsRes.setBrand(goods.getBrand());
		if (goods.getStatus() != null) {
			goodsRes.setStatus(goods.getStatus());
			goodsRes.setStatusName(
					ServiceSupport.getValueByBizCode(BizCodeConsts.DISTRIBUTE_GOODS_STATUS, goods.getStatus() + ""));
		}
		goodsRes.setCareerId(goods.getCareerId());
		goodsRes.setCareerName(cacheService.getUserChineseNameByid(goods.getCareerId()));
		goodsRes.setPurchaseId(goods.getPurchaseId());
		goodsRes.setPurchaseName(cacheService.getUserChineseNameByid(goods.getPurchaseId()));
		goodsRes.setSupplyChainGroupId(goods.getSupplyChainGroupId());
		goodsRes.setSupplyChainGroupName(cacheService.getUserChineseNameByid(goods.getSupplyChainGroupId()));
		goodsRes.setSupplyChainServiceId(goods.getSupplyChainServiceId());
		goodsRes.setSupplyChainServiceName(cacheService.getUserChineseNameByid(goods.getSupplyChainServiceId()));
		goodsRes.setRiskId(goods.getRiskId());
		goodsRes.setRiskName(cacheService.getUserChineseNameByid(goods.getRiskId()));
		return goodsRes;
	}

	/**
	 * 项目下供应商未关联铺货商品信息
	 * 
	 * @param codeList
	 * @return
	 */
	public List<DistributionGoodsResDto> queryDistributionGoodsByProjectSupplier(
			DistributionGoodsReqDto distributionGoods, RowBounds rowBounds) {
		return convertToResult(distributionGoodsDao.queryDistributionGoodsList(distributionGoods, rowBounds));
	}

	/**
	 * 项目下供应商关联铺货商品信息
	 * 
	 * @param distributionGoods
	 * @param rowBounds
	 * @return
	 */
	public PageResult<DistributionGoodsResDto> queryDistributionGoodsListByProjectId(
			DistributionGoodsReqDto distributionGoods) {
		PageResult<DistributionGoodsResDto> result = new PageResult<DistributionGoodsResDto>();
		int offSet = PageUtil.getOffSet(distributionGoods.getPage(), distributionGoods.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, distributionGoods.getPer_page());
		List<DistributionGoods> goodsList = distributionGoodsDao.queryDistributionGoodsListByProject(distributionGoods,
				rowBounds);
		List<DistributionGoodsResDto> baseGoodsResList = convertToResult(goodsList);
		result.setItems(baseGoodsResList);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), distributionGoods.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(distributionGoods.getPage());
		result.setPer_page(distributionGoods.getPer_page());
		return result;
	}

	/**
	 * 添加商品
	 * 
	 * @param baseGoods
	 * @return
	 */
	public void addDistributionBaseGoods(DistributionGoodsReqDto distributionGoodsReqDto) {
		List<DistributionGoods> goodsList = distributionGoodsReqDto.getDisList();
		if (!CollectionUtils.isEmpty(goodsList)) {
			for (DistributionGoods distributionGoods : goodsList) {
				BaseGoods baseGoods = cacheService.getGoodsById(distributionGoods.getId());
				DistributionGoods baseGoo = new DistributionGoods();
				if (null == baseGoods) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "商品信息为空");
				}
				if (null == distributionGoods.getSupplierId()) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "供应商不能为空");
				}
				BigDecimal pledge = distributionGoods.getPledge() == null ? BigDecimal.ZERO
						: distributionGoods.getPledge();
				if (DecimalUtil.gt(pledge, BigDecimal.ONE)) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "质押比例不能大于1");
				}
				if (null == distributionGoods.getCareerId()) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "事业部主管不能为空");
				}
				if (null == distributionGoods.getPurchaseId()) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "采购专员不能为空");
				}
				if (null == distributionGoods.getSupplyChainGroupId()) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "供应链小组不能为空");
				}
				if (null == distributionGoods.getSupplyChainServiceId()) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "供应链服务部不能为空");
				}
				if (null == distributionGoods.getRiskId()) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "风控专员不能为空");
				}
				if (null == distributionGoods.getDepartmentId()) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "事业部不能为空");
				}
				baseGoo = convertToDisResult(baseGoo, baseGoods);
				baseGoo.setStatus(BaseConsts.ONE);
				baseGoo.setGoodType(BaseConsts.ONE);
				baseGoo.setCreator(ServiceSupport.getUser() == null ? null : ServiceSupport.getUser().getChineseName());
				baseGoo.setIsDelete(BaseConsts.ZERO);
				baseGoo.setCreateAt(new Date());
				baseGoo.setDepartmentId(distributionGoods.getDepartmentId());// 事业部
				baseGoo.setSupplierId(distributionGoods.getSupplierId());// 供应商ID
				baseGoo.setCareerId(distributionGoods.getCareerId());// 事业部审核用户节点
				baseGoo.setPurchaseId(distributionGoods.getPurchaseId());// 采购部审核用户节点
				baseGoo.setSupplyChainGroupId(distributionGoods.getSupplyChainGroupId());// 供应链小组审核用户节点
				baseGoo.setSupplyChainServiceId(distributionGoods.getSupplyChainServiceId());// 供应链服务部审核用户节点
				baseGoo.setRiskId(distributionGoods.getRiskId());// 风控审核用户节点
				baseGoo.setPledge(pledge);
				Integer result = distributionGoodsDao.insert(baseGoo);
				if (result < 1) {
					throw new BaseException(ExcMsgEnum.GOODS_ADD_EXCEPTION);
				}
			}
		}
	}

	/**
	 * 将baseGood的数据进行封装到DistributionGoods
	 * 
	 * @param baseGoods
	 * @param baseGoo
	 * @return
	 */
	private DistributionGoods convertToDisResult(DistributionGoods baseGoo, BaseGoods baseGoods) {
		baseGoo.setNumber(baseGoods.getNumber());// 编号
		baseGoo.setName(baseGoods.getName());// 名称
		baseGoo.setType(baseGoods.getType());// 型号
		baseGoo.setBarCode(baseGoods.getBarCode());// 条码
		baseGoo.setSpecification(baseGoods.getSpecification());// 规格
		baseGoo.setTaxClassification(baseGoods.getTaxClassification());// 税收分类
		baseGoo.setTaxRate(baseGoods.getTaxRate());// 国内税率
		baseGoo.setUnit(baseGoods.getUnit());// 单位
		baseGoo.setVolume(baseGoods.getVolume());// 体积
		baseGoo.setGrossWeight(baseGoods.getGrossWeight());// 毛重
		baseGoo.setNetWeight(baseGoods.getNetWeight());// 净重
		baseGoo.setPurchasePrice(baseGoods.getPurchasePrice());// 采购指导价
		baseGoo.setSalePrice(baseGoods.getSalePrice());// 销售指导价
		baseGoo.setSaleCurrencyType(baseGoods.getSaleCurrencyType());// 销售指导价币种
		baseGoo.setPurCurrencyType(baseGoods.getPurCurrencyType());// 采购指导价币种
		baseGoo.setBrand(baseGoods.getBrand());// 品牌
		baseGoo.setBroad(baseGoods.getBroad());
		baseGoo.setGrow(baseGoods.getGrow());
		return baseGoo;
	}

	/**
	 * 编辑/浏览
	 * 
	 * @param baseGoods
	 * @return
	 */
	public Result<DistributionGoodsResDto> queryDistributionBaseGoodsById(DistributionGoods baseGoods) {
		Result<DistributionGoodsResDto> result = new Result<DistributionGoodsResDto>();
		DistributionGoods goods = distributionGoodsDao.queryDistributionGoodsById(baseGoods.getId());
		result.setItems(convertToResDto(goods));
		return result;
	}

	public DistributionGoodsResDto queryDistributionById(Integer id) {
		DistributionGoods goods = distributionGoodsDao.queryDistributionGoodsById(id);
		return convertToResDto(goods);
	}

	/**
	 * 更新商品信息
	 * 
	 * @param baseGoods
	 * @return
	 */
	public BaseResult updateDistributionGoods(DistributionGoods baseGoods) {
		BaseResult result = new BaseResult();
		if (baseGoods.getId() != null) {
			distributionGoodsDao.update(baseGoods);
		} else {
			baseGoods.setStatus(BaseConsts.ONE);
			baseGoods.setGoodType(BaseConsts.ONE);
			baseGoods.setCreator(ServiceSupport.getUser() == null ? null : ServiceSupport.getUser().getChineseName());
			baseGoods.setIsDelete(BaseConsts.ZERO);
			baseGoods.setCreateAt(new Date());
			distributionGoodsDao.insert(baseGoods);
		}
		return result;
	}

	/**
	 * 提交商品
	 * 
	 * @param baseGoods
	 * @return
	 */
	public BaseResult submit(DistributionGoods baseGoods) {
		BaseResult result = new BaseResult();
		DistributionGoods goodResult = distributionGoodsDao.queryDistributionGoodsById(baseGoods.getId());
		if (goodResult.getStatus() == 1) {
			AuditNode startAuditNode = auditFlowService.getStartAuditNode(BaseConsts.INT_21, null);
			if (null == startAuditNode) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请设置审核流");
			}
			baseGoods.setStatus(startAuditNode.getAuditNodeState());
			int i = distributionGoodsDao.submit(baseGoods);
			if (i <= 0) {
				throw new BaseException(ExcMsgEnum.USERSSUBMIT_EXCEPTION);
			}
			distributionGoodsAuditService.startAudit(goodResult, startAuditNode);// 审核流程
		} else if (goodResult.getStatus() == BaseConsts.TWO) {
			result.setSuccess(false);
			result.setMsg("该商品已提交！");
		} else if (goodResult.getStatus() == BaseConsts.THREE) {
			result.setSuccess(false);
			result.setMsg("该商品已锁定！");
		}
		return result;
	}

	/**
	 * 已完成相关业务操作
	 * 
	 * @param id
	 */
	public void isOver(int id) {
		DistributionGoods goodResult = distributionGoodsDao.queryDistributionGoodsById(id);
		DistributionGoodsReqDto distributionGoods = new DistributionGoodsReqDto();
		distributionGoods.setDepartmentId(goodResult.getDepartmentId());
		distributionGoods.setNumber(goodResult.getNumber());
		distributionGoods.setSupplierId(goodResult.getSupplierId());
		distributionGoods.setStatus(BaseConsts.TWO);
		List<DistributionGoods> distribution = distributionGoodsDao.queryAllDistributionGoodsList(distributionGoods);
		if (distribution != null) {
			for (DistributionGoods goods : distribution) {
				DistributionGoods uppDistribute = new DistributionGoods();
				uppDistribute.setId(goods.getId());
				uppDistribute.setStatus(BaseConsts.THREE);
				distributionGoodsDao.update(uppDistribute);
			}
		}
	}

	/**
	 * 加锁
	 * 
	 * @param baseGoods
	 * @return
	 */
	public BaseResult lock(DistributionGoods baseGoods) {
		BaseResult result = new BaseResult();
		// 查询商品状态加锁
		baseGoods.setStatus(BaseConsts.THREE);
		int i = distributionGoodsDao.lock(baseGoods);
		if (i <= 0) {
			throw new BaseException(ExcMsgEnum.GOODS_LOCK_EXCEPTION);
		}

		return result;
	}

	/**
	 * 解锁
	 * 
	 * @param baseGoods
	 * @return
	 */
	public BaseResult unlock(DistributionGoods baseGoods) {
		BaseResult result = new BaseResult();
		baseGoods.setStatus(BaseConsts.TWO);
		int i = distributionGoodsDao.unlock(baseGoods);
		if (i < 0) {
			throw new BaseException(ExcMsgEnum.GOODS_UNLOCK_EXCEPTION);
		}
		return result;
	}

	/**
	 * 逻辑删除商品
	 * 
	 * @param baseGoods
	 * @return
	 */
	public BaseResult delete(DistributionGoods baseGoods) {
		BaseResult result = new BaseResult();
		baseGoods.setDeleteAt(new Date());
		baseGoods.setDeleter(ServiceSupport.getUser().getChineseName());
		baseGoods.setIsDelete(BaseConsts.ONE);
		int i = distributionGoodsDao.delete(baseGoods);
		if (i < 0) {
			throw new BaseException(ExcMsgEnum.GOODS_DELETE_EXCEPTION);
		}
		return result;
	}

	/**
	 * 导入Excel 业务逻辑处理
	 *
	 * @param importFile
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void importExcel(MultipartFile importFile) {
		List<DistributionGoodsResDto> goodsList = Lists.newArrayList();
		Map beans = Maps.newHashMap();
		beans.put("goodsList", goodsList);
		ExcelService.resolverExcel(importFile, "/excel/baseinfo/goods/distribute.xml", beans);
		// 业务逻辑处理
		goodsList = (List<DistributionGoodsResDto>) beans.get("goodsList");
		if (CollectionUtils.isNotEmpty(goodsList)) {
			if (goodsList.size() > BaseConsts.IMPORT_EXCEL_ROWS_MAX) {
				throw new BaseException(ExcMsgEnum.IMPORT_EXCEL_ROWS_MAX);
			}
			List<CodeValue> goodChinaRateList = ServiceSupport.getCvListByBizCode(BizCodeConsts.GOODS_CHINA_RATE);
			for (DistributionGoodsResDto baseGoodsResDto : goodsList) {
				DistributionGoods baseGoods = validateDistributionGoods(baseGoodsResDto, goodChinaRateList);
				distributionGoodsDao.insert(baseGoods);
			}
		} else {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "导入商品不能为空");
		}
	}

	/**
	 * 校验导入商品
	 * 
	 * @param baseGoodsResDto
	 * @return
	 */
	public DistributionGoods validateDistributionGoods(DistributionGoodsResDto baseGoodsResDto,
			List<CodeValue> goodChinaRateList) {
		DistributionGoods baseGoods = new DistributionGoods();
		String number = (null == baseGoodsResDto.getNumber() ? "" : baseGoodsResDto.getNumber().trim());
		if (StringUtils.isNotBlank(number)) {
			if (number.length() > 50) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "编号不能超过50个字符");
			}
			baseGoods.setNumber(number);
			int count = distributionGoodsDao.getExistCountByCon(baseGoods);
			if (count > 0) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "编号[" + number + "]已存在");
			}
		} else {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "编号不能为空");
		}
		baseGoods.setNumber(number);

		String name = (null == baseGoodsResDto.getName() ? "" : baseGoodsResDto.getName().trim());
		if (StringUtils.isNotBlank(name)) {
			if (name.length() > 100) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "名称不能超过100个字符");
			}
		} else {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "名称不能为空");
		}
		baseGoods.setName(name);

		String departmentName = (null == baseGoodsResDto.getDepartmentName() ? ""
				: baseGoodsResDto.getDepartmentName().trim());
		if (StringUtils.isNotBlank(departmentName)) {
			String departmentId = commonParamValidate.getDepartmentDao(departmentName);
			if (departmentId == null || departmentId.equals("")) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "部门" + departmentName + "不存在");
			}
			baseGoods.setDepartmentId(Integer.parseInt(departmentId));
		} else {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "部门不能为空");
		}

		String supplierName = (null == baseGoodsResDto.getSupplierName() ? ""
				: baseGoodsResDto.getSupplierName().trim());
		if (StringUtils.isNotBlank(supplierName)) {
			String supplierId = commonParamValidate.getAllCdByKeyValidate("SUPPLIER", supplierName);
			if (supplierId == null || supplierId.equals("")) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "供应商" + departmentName + "不存在");
			}
			baseGoods.setSupplierId(Integer.parseInt(supplierId));
		} else {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "供应商不能为空");
		}

		BigDecimal pledge = baseGoodsResDto.getPledge();
		if (DecimalUtil.gt(pledge, new BigDecimal(BaseConsts.ONE))) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "质押比例不能大于1");
		}
		baseGoods.setPledge(pledge);

		String type = (null == baseGoodsResDto.getType() ? "" : baseGoodsResDto.getType());
		if (StringUtils.isNotBlank(type)) {
			if (type.length() > 100) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "型号不能超过100个字符");
			}
		} else {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "型号不能为空");
		}
		baseGoods.setType(type);

		String barCode = (null == baseGoodsResDto.getBarCode() ? "" : baseGoodsResDto.getBarCode());
		if (StringUtils.isNotBlank(barCode)) {
			if (barCode.length() > 20) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "条码不能超过20个字符");
			}
		} else {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "条码不能为空");
		}
		baseGoods.setBarCode(barCode);

		String specification = (null == baseGoodsResDto.getSpecification() ? "" : baseGoodsResDto.getSpecification());
		if (StringUtils.isNotBlank(specification)) {
			if (specification.length() > 50) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "规格不能超过50个字符");
			}
		} else {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "规格不能为空");
		}
		baseGoods.setSpecification(specification);

		String taxClassification = (null == baseGoodsResDto.getTaxClassification() ? ""
				: baseGoodsResDto.getTaxClassification());
		if (StringUtils.isNotBlank(taxClassification)) {
			if (taxClassification.length() > 50) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "税收分类不能超过50个字符");
			}
		}
		baseGoods.setTaxClassification(taxClassification);

		BigDecimal taxRate = baseGoodsResDto.getTaxRate();
		if (null == taxRate) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "国内税率不能为空");
		}
		if (CollectionUtils.isNotEmpty(goodChinaRateList)) {
			boolean flag = false;
			for (CodeValue goodChinaRate : goodChinaRateList) {
				if (taxRate.compareTo(new BigDecimal(goodChinaRate.getValue().trim())) == BaseConsts.ZERO) {
					flag = true;
					break;
				}
			}
			if (flag == false) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "国内税率录入不正确");
			}
		} else {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "国内税率没有在字典中设置");
		}
		baseGoods.setTaxRate(taxRate.setScale(4, BigDecimal.ROUND_HALF_UP));

		String unit = (null == baseGoodsResDto.getUnit() ? "" : baseGoodsResDto.getUnit());
		if (StringUtils.isNotBlank(unit)) {
			if (unit.length() > 50) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "单位不能超过20个字符");
			}
		} else {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "单位不能为空");
		}
		baseGoods.setUnit(unit);

		BigDecimal volume = baseGoodsResDto.getVolume();
		if (null == volume) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "体积不能为空");
		}
		baseGoods.setVolume(volume.setScale(BaseConsts.FOUR, BigDecimal.ROUND_HALF_UP));

		BigDecimal grossWeight = baseGoodsResDto.getGrossWeight();
		if (null == grossWeight) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "毛重不能为空");
		}
		baseGoods.setGrossWeight(grossWeight.setScale(BaseConsts.FOUR, BigDecimal.ROUND_HALF_UP));

		BigDecimal netWeight = baseGoodsResDto.getNetWeight();
		if (null == netWeight) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "净重不能为空");
		}
		baseGoods.setNetWeight(netWeight.setScale(BaseConsts.FOUR, BigDecimal.ROUND_HALF_UP));

		BigDecimal purchasePrice = baseGoodsResDto.getPurchasePrice();
		if (null == purchasePrice) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "采购指导价不能为空");
		}
		baseGoods.setPurchasePrice(purchasePrice.setScale(BaseConsts.EIGHT, BigDecimal.ROUND_HALF_UP));

		BigDecimal salePrice = baseGoodsResDto.getSalePrice();
		if (null == salePrice) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "销售指导价不能为空");
		}
		baseGoods.setSalePrice(salePrice.setScale(BaseConsts.EIGHT, BigDecimal.ROUND_HALF_UP));

		String purCurrencyTypeName = baseGoodsResDto.getPurCurrencyTypeName();
		if (StringUtils.isBlank(purCurrencyTypeName)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "采购指导价币种不能为空");
		}
		String purCurrencyType = ServiceSupport.getCodeByBizValue(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
				purCurrencyTypeName.trim());
		if (StringUtils.isBlank(purCurrencyType)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "采购指导价币种【" + purCurrencyTypeName + "】不存在");
		}
		baseGoods.setPurCurrencyType(Integer.parseInt(purCurrencyType));

		String saleCurrencyTypeName = baseGoodsResDto.getSaleCurrencyTypeName();
		if (StringUtils.isBlank(saleCurrencyTypeName)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "销售指导价币种不能为空");
		}
		String saleCurrencyType = ServiceSupport.getCodeByBizValue(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
				saleCurrencyTypeName.trim());
		if (StringUtils.isBlank(saleCurrencyType)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "销售指导价币种【" + saleCurrencyTypeName + "】不存在");
		}
		baseGoods.setSaleCurrencyType(Integer.parseInt(saleCurrencyType));

		String careerName = baseGoodsResDto.getCareerName();// 审核相关
		if (StringUtils.isBlank(careerName)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "事业部主管不能为空");
		}
		String purchaseName = baseGoodsResDto.getPurchaseName();
		if (StringUtils.isBlank(purchaseName)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "采购部专员不能为空");
		}
		String supplyChainGroupName = baseGoodsResDto.getSupplyChainGroupName();
		if (StringUtils.isBlank(supplyChainGroupName)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "供应链小组不能为空");
		}
		String supplyChainServiceName = baseGoodsResDto.getSupplyChainServiceName();
		if (StringUtils.isBlank(supplyChainServiceName)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "供应链服务不能为空");
		}
		String riskName = baseGoodsResDto.getRiskName();
		if (StringUtils.isBlank(riskName)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "风控专员不能为空");
		}
		List<CodeValue> listCode = commonParamValidate.getAllUserList();// 获取所有用户信息
		String careerId = null;
		for (CodeValue code : listCode) {
			if (code.getValue().equals(careerName)) {
				careerId = code.getCode();
			}
		}
		if (StringUtils.isBlank(careerId)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "事业部主管【" + careerName + "】不存在");
		}
		baseGoods.setCareerId(Integer.parseInt(careerId));

		String purchaseId = null;
		for (CodeValue code : listCode) {
			if (code.getValue().equals(purchaseName)) {
				purchaseId = code.getCode();
			}
		}
		if (StringUtils.isBlank(purchaseId)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "采购部专员【" + purchaseName + "】不存在");
		}
		baseGoods.setPurchaseId(Integer.parseInt(purchaseId));

		String supplyChainGroupId = null;
		for (CodeValue code : listCode) {
			if (code.getValue().equals(supplyChainGroupName)) {
				supplyChainGroupId = code.getCode();
			}
		}
		if (StringUtils.isBlank(supplyChainGroupId)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "供应链小组【" + supplyChainGroupName + "】不存在");
		}
		baseGoods.setSupplyChainGroupId(Integer.parseInt(supplyChainGroupId));

		String supplyChainServiceId = null;
		for (CodeValue code : listCode) {
			if (code.getValue().equals(supplyChainServiceName)) {
				supplyChainServiceId = code.getCode();
			}
		}
		if (StringUtils.isBlank(supplyChainServiceId)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "供应链服务【" + supplyChainServiceName + "】不存在");
		}
		baseGoods.setSupplyChainServiceId(Integer.parseInt(supplyChainServiceId));

		String riskId = null;
		for (CodeValue code : listCode) {
			if (code.getValue().equals(riskName)) {
				riskId = code.getCode();
			}
		}
		if (StringUtils.isBlank(riskId)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "分控专员【" + riskName + "】不存在");
		}
		baseGoods.setRiskId(Integer.parseInt(riskId));

		baseGoods.setStatus(BaseConsts.ONE); // 1-待提交
		baseGoods.setCreator(ServiceSupport.getUser().getChineseName());
		return baseGoods;
	}

	private List<CodeValue> getOperList(Integer state) {
		// 1.根据状态得到操作列表
		List<String> operNameList = getOperListByState(state);
		// 2.将上述的操作列表通过权限过滤
		List<CodeValue> oprResult = ServiceSupport.getOperListByPermission(operNameList,
				DistributionGoodsResDto.Operate.operMap);
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
			opertaList.add(OperateConsts.DETAIL);
			opertaList.add(OperateConsts.LOCK);
			opertaList.add(OperateConsts.COPY);
			break;
		default:
			opertaList.add(OperateConsts.DETAIL);
			break;
		}
		return opertaList;
	}

	/**
	 * 根据编号（sku）和币种查询铺货商品信息
	 * 
	 * @return
	 */
	public List<DistributionGoods> queryDistributionGoodByNumber(PmsStoreOut storeOut, PMSSupplierBind supplierId) {
		List<DistributionGoods> goodsList = new ArrayList<DistributionGoods>();
		if (storeOut == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "铺货采购单明细查询数据为空");
		}
		if (supplierId == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "铺货采购单明细查询供应商为空");
		}
		if (supplierId.getSupplierId() == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "铺货采购单明细查询供应商id为空");
		}

		// 铺货商品是否存在
		List<PurchaseOrderTitle> list = purchaseOrderTitleDao.queryDistribePoByAppendNo(storeOut.getPurchase_sn());
		if (CollectionUtils.isEmpty(list)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL,
					"铺货商品无对应的purchase_sn【" + storeOut.getPurchase_sn() + "】的商品信息");
		}
		// 封装查询的对象的数据
		DistributionGoodsReqDto distributionGoods = new DistributionGoodsReqDto();
		// 根据SKU查询商品信息
		List<DistributionGoods> goodslist = distributionGoodsDao.queryResultGoodsBySku(storeOut.getSku());
		if (CollectionUtils.isEmpty(goodslist)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "铺货商品无对应的sku【" + storeOut.getSku() + "】的商品信息");
		}
		DistributionGoods goods = new DistributionGoods();
		for (DistributionGoods good : goodslist) {
			distributionGoods.setGoodId(good.getId());// 商品ID
			distributionGoods.setProjectId(supplierId.getProjectId());// 项目ID
			distributionGoods.setSupplierId(supplierId.getSupplierId());// 供应商ID
			goods = distributionGoodsDao.queryDistributionGoodByNumber(distributionGoods);
			if (goods != null) {
				goodsList.add(goods);
			}
		}
		return goodsList;
	}

	/**
	 * 查询附件列表
	 */
	public PageResult<PoFileAttachRespDto> queryFileList(FileAttachSearchReqDto fileAttReqDto) {
		PageResult<PoFileAttachRespDto> pageResult = new PageResult<PoFileAttachRespDto>();
		List<FileAttach> fielAttach = fileUploadService.queryFileAttList(fileAttReqDto);
		List<PoFileAttachRespDto> list = convertToResDto(fielAttach);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), fileAttReqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(fileAttReqDto.getPage());
		pageResult.setPer_page(fileAttReqDto.getPer_page());
		pageResult.setItems(list);
		return pageResult;
	}

	public List<PoFileAttachRespDto> queryAllFileList(FileAttachSearchReqDto fileAttReqDto) {
		fileAttReqDto.setBusType(BaseConsts.INT_35);
		List<FileAttach> fielAttach = fileUploadService.queryFileAttList(fileAttReqDto);
		List<PoFileAttachRespDto> list = convertToResDto(fielAttach);
		return list;
	}

	private List<PoFileAttachRespDto> convertToResDto(List<FileAttach> fileAttach) {
		List<PoFileAttachRespDto> list = new LinkedList<PoFileAttachRespDto>();
		for (int i = 0; i < fileAttach.size(); i++) {
			PoFileAttachRespDto poFileAttachRespDto = new PoFileAttachRespDto();
			poFileAttachRespDto.setId(fileAttach.get(i).getId());
			poFileAttachRespDto.setBusId(fileAttach.get(i).getBusId());
			poFileAttachRespDto.setBusTypeName(
					ServiceSupport.getValueByBizCode(BizCodeConsts.FILE_BUS_TYPE, fileAttach.get(i).getBusType() + ""));
			poFileAttachRespDto.setName(fileAttach.get(i).getName());
			poFileAttachRespDto.setType(fileAttach.get(i).getType());
			poFileAttachRespDto.setCreateAt(fileAttach.get(i).getCreateAt());
			poFileAttachRespDto.setCreator(fileAttach.get(i).getCreator());
			List<CodeValue> operList = getFileOperList();
			poFileAttachRespDto.setOpertaList(operList);
			list.add(poFileAttachRespDto);
		}
		return list;
	}

	/**
	 * 获取文件操作列表
	 *
	 * @return
	 */
	private List<CodeValue> getFileOperList() {
		// 1.根据状态得到操作列表
		List<String> operNameList = getFileOperListByState();
		// 2.将上述的操作列表通过权限过滤
		List<CodeValue> oprResult = ServiceSupport.getOperListByPermission(operNameList,
				ProjectItemFileAttach.Operate.operMap);
		return oprResult;
	}

	/**
	 * 获取文件操作列表
	 *
	 * @return
	 */
	private List<String> getFileOperListByState() {
		List<String> opertaList = Lists.newArrayList();
		opertaList.add(OperateConsts.DOWNLOAD);
		opertaList.add(OperateConsts.DELETE);
		return opertaList;
	}

	/**
	 * 查询商品状态为0 的普通商品信息列表
	 * 
	 * @param distributionGoodsReqDto
	 * @return
	 */
	public PageResult<DistributionGoodsResDto> getBaseGoodsList(DistributionGoodsReqDto distributionGoodsReqDto) {
		PageResult<DistributionGoodsResDto> pageResult = new PageResult<DistributionGoodsResDto>();
		int offSet = PageUtil.getOffSet(distributionGoodsReqDto.getPage(), distributionGoodsReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, distributionGoodsReqDto.getPer_page());
		BaseGoods baseGoods = this.convertToBaseGoods(distributionGoodsReqDto);
		;
		List<BaseGoods> goodsList = baseGoodsDao.getGoodsList(baseGoods, rowBounds);
		List<DistributionGoodsResDto> goodsResDtos = converToGoodsResult(goodsList);
		pageResult.setItems(goodsResDtos);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), distributionGoodsReqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(distributionGoodsReqDto.getPage());
		pageResult.setPer_page(distributionGoodsReqDto.getPer_page());
		return pageResult;
	}

	private BaseGoods convertToBaseGoods(DistributionGoodsReqDto distributionGoodsReqDto) {
		BaseGoods baseGoods = new BaseGoods();
		baseGoods.setNumber(distributionGoodsReqDto.getNumber());
		baseGoods.setName(distributionGoodsReqDto.getName());
		baseGoods.setType(distributionGoodsReqDto.getType());
		baseGoods.setSpecification(distributionGoodsReqDto.getSpecification());
		baseGoods.setBarCode(distributionGoodsReqDto.getBarCode());
		baseGoods.setStatus(distributionGoodsReqDto.getStatus());
		baseGoods.setGoodType(BaseConsts.ZERO);
		return baseGoods;
	}

	/**
	 * 将查询的商品信息进行封装
	 * 
	 * @param goodsList
	 * @return
	 */
	public List<DistributionGoodsResDto> converToGoodsResult(List<BaseGoods> goodsList) {
		List<DistributionGoodsResDto> goodsResDtos = new ArrayList<DistributionGoodsResDto>();
		if (CollectionUtils.isEmpty(goodsList)) {
			return goodsResDtos;
		} else {
			for (BaseGoods goods : goodsList) {
				DistributionGoodsResDto resDto = new DistributionGoodsResDto();
				resDto.setId(goods.getId());// 商品ID
				resDto.setNumber(goods.getNumber());// 商品编号
				resDto.setName(goods.getName());
				resDto.setType(goods.getType());
				resDto.setBarCode(goods.getBarCode());
				resDto.setSpecification(goods.getSpecification());
				resDto.setTaxClassification(goods.getTaxClassification());
				resDto.setTaxRate(goods.getTaxRate());
				resDto.setUnit(goods.getUnit());
				resDto.setVolume(goods.getVolume());
				resDto.setGrossWeight(goods.getGrossWeight());
				resDto.setNetWeight(goods.getNetWeight());
				resDto.setPurchasePriceValue(DecimalUtil.toAmountString(goods.getPurchasePrice()) + ServiceSupport
						.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE, goods.getPurCurrencyType() + ""));
				resDto.setSalePriceValue(DecimalUtil.toAmountString(goods.getSalePrice()) + ServiceSupport
						.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE, goods.getSaleCurrencyType() + ""));
				resDto.setCreator(goods.getCreator());
				resDto.setCreatorAt(goods.getCreateAt());
				resDto.setBrand(goods.getBrand());
				resDto.setBroad(goods.getBroad());
				resDto.setGrow(goods.getGrow());
				if (goods.getStatus() != null) {
					resDto.setStatus(goods.getStatus());
					resDto.setStatusName(
							ServiceSupport.getValueByBizCode(BizCodeConsts.GOODS_STATUS, goods.getStatus() + ""));
				}
				List<CodeValue> operList = getOperGoodsList(goods.getStatus());
				resDto.setOpertaList(operList);
				goodsResDtos.add(resDto);
			}
		}
		return goodsResDtos;
	}

	private List<CodeValue> getOperGoodsList(Integer state) {
		// 1.根据状态得到操作列表
		List<String> operNameList = getOperGoodsListByState();
		// 2.将上述的操作列表通过权限过滤
		List<CodeValue> oprResult = ServiceSupport.getOperListByPermission(operNameList,
				DistributionGoodsResDto.Operate.operMap);
		return oprResult;
	}

	/**
	 * 根据状态得到操作列表
	 *
	 * @param state
	 * @return
	 */
	private List<String> getOperGoodsListByState() {
		List<String> opertaList = Lists.newArrayList();
		// 状态过滤
		opertaList.add(OperateConsts.SAVE);
		return opertaList;
	}

}
