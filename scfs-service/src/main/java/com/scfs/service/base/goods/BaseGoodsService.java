package com.scfs.service.base.goods;

import java.math.BigDecimal;
import java.util.Date;
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
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.OperateConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.base.entity.BaseGoodsDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.domain.CodeValue;
import com.scfs.domain.base.dto.req.BaseGoodsReqDto;
import com.scfs.domain.base.dto.resp.BaseGoodsResDto;
import com.scfs.domain.base.entity.BaseGoods;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.support.ExcelService;
import com.scfs.service.support.ServiceSupport;

@Service
public class BaseGoodsService {
	@Autowired
	private BaseGoodsDao baseGoodsDao;

	public PageResult<BaseGoodsResDto> getBaseGoodsList(BaseGoodsReqDto baseGoodsReqDto) {
		PageResult<BaseGoodsResDto> result = new PageResult<BaseGoodsResDto>();
		int offSet = PageUtil.getOffSet(baseGoodsReqDto.getPage(), baseGoodsReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, baseGoodsReqDto.getPer_page());
		//设置商品类型
		baseGoodsReqDto.setGoodType(BaseConsts.ZERO);
		//DTO转domain
		BaseGoods baseGoods = convertToBaseGoods(baseGoodsReqDto);
		List<BaseGoods> goodsList = baseGoodsDao.getGoodsList(baseGoods, rowBounds);
		List<BaseGoodsResDto> baseGoodsResList = convertToResult(goodsList);
		result.setItems(baseGoodsResList);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), baseGoodsReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(baseGoodsReqDto.getPage());
		result.setPer_page(baseGoodsReqDto.getPer_page());
		return result;
	}

	private List<BaseGoodsResDto> convertToResult(List<BaseGoods> goodsList) {
		List<BaseGoodsResDto> baseGoodsList = Lists.newArrayList();
		if (CollectionUtils.isEmpty(goodsList)) {
			return baseGoodsList;
		}
		for (BaseGoods goods : goodsList) {
			BaseGoodsResDto goodsRes = new BaseGoodsResDto();
			goodsRes.setId(goods.getId());
			goodsRes.setNumber(goods.getNumber());
			goodsRes.setName(goods.getName());
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
			//采购指导价
			goodsRes.setPurchasePriceValue(DecimalUtil.toAmountString(goods.getPurchasePrice()) + ServiceSupport
					.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE, goods.getPurCurrencyType() + ""));
			//销售指导价
			goodsRes.setSalePriceValue(DecimalUtil.toAmountString(goods.getSalePrice()) + ServiceSupport
					.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE, goods.getSaleCurrencyType() + ""));
			goodsRes.setCreator(goods.getCreator());
			goodsRes.setCreatorAt(goods.getCreateAt());
			goodsRes.setBrand(goods.getBrand());
			if (goods.getStatus() != null) {
				goodsRes.setStatus(goods.getStatus());
				goodsRes.setStatusName(
						ServiceSupport.getValueByBizCode(BizCodeConsts.GOODS_STATUS, goods.getStatus() + ""));
			}
			// 操作集合
			List<CodeValue> operList = getOperList(goods.getStatus());
			goodsRes.setOpertaList(operList);
			baseGoodsList.add(goodsRes);
		}
		return baseGoodsList;
	}

	private BaseGoods convertToBaseGoods(BaseGoodsReqDto baseGoodsReqDto) {
		BaseGoods baseGoods = new BaseGoods();
		baseGoods.setNumber(baseGoodsReqDto.getNumber());
		baseGoods.setName(baseGoodsReqDto.getName());
		baseGoods.setType(baseGoodsReqDto.getType());
		baseGoods.setSpecification(baseGoodsReqDto.getSpecification());
		baseGoods.setBarCode(baseGoodsReqDto.getBarCode());
		baseGoods.setStatus(baseGoodsReqDto.getStatus());
		baseGoods.setGoodType(baseGoodsReqDto.getGoodType());
		return baseGoods;
	}

	/**
	 * 添加商品
	 * 
	 * @param baseGoods
	 * @return
	 */
	public Integer addBaseGoods(BaseGoods baseGoods) {
		BaseGoods baseGoo = new BaseGoods();
		baseGoo.setNumber(baseGoods.getNumber());
		// 查询商品编号是否存在
		if (baseGoodsDao.getExistCountByCon(baseGoo) > 0) {
			throw new BaseException(ExcMsgEnum.GOODS_NUMBER_EXIST_EXCEPTION);
		}

		baseGoods.setStatus(BaseConsts.ONE);
		baseGoods.setCreator(ServiceSupport.getUser() == null ? null : ServiceSupport.getUser().getChineseName());
		baseGoods.setCreateAt(new Date());
		Integer result = baseGoodsDao.insert(baseGoods);
		if (result < 1)
			throw new BaseException(ExcMsgEnum.GOODS_ADD_EXCEPTION);
		return baseGoods.getId();
	}

	/**
	 * 编辑/浏览
	 * 
	 * @param baseGoods
	 * @return
	 */
	public Result<BaseGoods> queryBaseGoodsById(BaseGoods baseGoods) {
		Result<BaseGoods> result = new Result<BaseGoods>();
		BaseGoods goods = baseGoodsDao.queryBaseGoodsById(baseGoods.getId());
		goods.setSaleCurrencyTypeValue(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
				goods.getSaleCurrencyType() + ""));
		goods.setPurCurrencyTypeValue(
				ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE, goods.getPurCurrencyType() + ""));
		result.setItems(goods);
		return result;
	}

	/**
	 * 更新商品信息
	 * 
	 * @param baseGoods
	 * @return
	 */
	public Result<BaseGoods> updateGoods(BaseGoods baseGoods) {
		Result<BaseGoods> result = new Result<BaseGoods>();
		// 判断商品id是否存在
		if (baseGoods.getId() != null) {
			BaseGoods baseGoo = new BaseGoods();
			baseGoo.setId(baseGoods.getId());
			baseGoo.setNumber(baseGoods.getNumber());
			// 查询商品编号是否存在
			int count = baseGoodsDao.getExistCountByCon(baseGoo);
			if (count > 0) {
				throw new BaseException(ExcMsgEnum.GOODS_NUMBER_EXIST_EXCEPTION);
			}
			baseGoodsDao.update(baseGoods);
		} else {
			throw new BaseException(ExcMsgEnum.GOODS_UPDATE_EXCEPTION);
		}
		return result;
	}

	/**
	 * 提交商品
	 * 
	 * @param baseGoods
	 * @return
	 */
	public Result<BaseGoods> submit(BaseGoods baseGoods) {
		Result<BaseGoods> result = new Result<BaseGoods>();
		BaseGoods goodResult = baseGoodsDao.queryBaseGoodsById(baseGoods.getId());
		if (goodResult.getStatus() == 1) {
			baseGoods.setStatus(BaseConsts.TWO);
			int i = baseGoodsDao.submit(baseGoods);
			if (i <= 0) {
				throw new BaseException(ExcMsgEnum.USERSSUBMIT_EXCEPTION);
			}
		} else if (goodResult.getStatus() == 2) {
			result.setSuccess(false);
			result.setMsg("该商品已提交！");
		} else if (goodResult.getStatus() == 3) {
			result.setSuccess(false);
			result.setMsg("该商品已锁定！");
		}
		return result;
	}

	/**
	 * 加锁
	 * 
	 * @param baseGoods
	 * @return
	 */
	public Result<BaseGoods> lock(BaseGoods baseGoods) {
		Result<BaseGoods> result = new Result<BaseGoods>();
		// 查询商品状态加锁
		baseGoods.setStatus(BaseConsts.THREE);
		int i = baseGoodsDao.lock(baseGoods);
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
	public Result<BaseGoods> unlock(BaseGoods baseGoods) {
		Result<BaseGoods> result = new Result<BaseGoods>();
		baseGoods.setStatus(BaseConsts.TWO);
		int i = baseGoodsDao.unlock(baseGoods);
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
	public Result<BaseGoods> delete(BaseGoods baseGoods) {
		Result<BaseGoods> result = new Result<BaseGoods>();
		baseGoods.setDeleteAt(new Date());
		baseGoods.setDeleter(ServiceSupport.getUser().getChineseName());
		baseGoods.setIsDelete(BaseConsts.ONE);
		int i = baseGoodsDao.delete(baseGoods);
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
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void importExcel(MultipartFile importFile) {
		List<BaseGoodsResDto> goodsList = Lists.newArrayList();
		Map beans = Maps.newHashMap();
		beans.put("goodsList", goodsList);
		ExcelService.resolverExcel(importFile, "/excel/baseinfo/goods/goods.xml", beans);
		// 业务逻辑处理
		goodsList = (List<BaseGoodsResDto>) beans.get("goodsList");
		if (CollectionUtils.isNotEmpty(goodsList)) {
			if (goodsList.size() > BaseConsts.IMPORT_EXCEL_ROWS_MAX) {
				throw new BaseException(ExcMsgEnum.IMPORT_EXCEL_ROWS_MAX);
			}
			List<CodeValue> goodChinaRateList = ServiceSupport.getCvListByBizCode(BizCodeConsts.GOODS_CHINA_RATE);
			for (BaseGoodsResDto baseGoodsResDto : goodsList) {
				BaseGoods baseGoods = validateGoods(baseGoodsResDto, goodChinaRateList);
				baseGoodsDao.insert(baseGoods);
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
	public BaseGoods validateGoods(BaseGoodsResDto baseGoodsResDto, List<CodeValue> goodChinaRateList) {
		BaseGoods baseGoods = new BaseGoods();
		String number = (null == baseGoodsResDto.getNumber() ? "" : baseGoodsResDto.getNumber().trim());
		if (StringUtils.isNotBlank(number)) {
			if (number.length() > 50) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "编号不能超过50个字符");
			}
			baseGoods.setNumber(number);
			int count = baseGoodsDao.getExistCountByCon(baseGoods);
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

		String type = (null == baseGoodsResDto.getType() ? "" : baseGoodsResDto.getType());
		if (StringUtils.isNotBlank(type)) {
			if (type.length() > 500) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "型号不能超过500个字符");
			}
		} else {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "型号不能为空");
		}
		baseGoods.setType(type);

		String brand = (null == baseGoodsResDto.getBrand() ? "" : baseGoodsResDto.getBrand());
		if (StringUtils.isBlank(brand)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "品牌不能为空");
		}
		baseGoods.setBrand(brand);

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
				if (taxRate.compareTo(new BigDecimal(goodChinaRate.getValue().trim())) == 0) {
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

		BigDecimal broad = baseGoodsResDto.getBroad();
		if (null == broad) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "宽不能为空");
		}
		baseGoods.setBroad(broad.setScale(4, BigDecimal.ROUND_HALF_UP));

		BigDecimal grow = baseGoodsResDto.getGrow();
		if (null == grow) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "长不能为空");
		}
		baseGoods.setGrow(grow.setScale(4, BigDecimal.ROUND_HALF_UP));

		BigDecimal volume = baseGoodsResDto.getVolume();
		if (null == volume) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "高不能为空");
		}
		baseGoods.setVolume(volume.setScale(4, BigDecimal.ROUND_HALF_UP));

		BigDecimal grossWeight = baseGoodsResDto.getGrossWeight();
		if (null == grossWeight) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "毛重不能为空");
		}
		baseGoods.setGrossWeight(grossWeight.setScale(4, BigDecimal.ROUND_HALF_UP));

		BigDecimal netWeight = baseGoodsResDto.getNetWeight();
		if (null == netWeight) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "净重不能为空");
		}
		baseGoods.setNetWeight(netWeight.setScale(4, BigDecimal.ROUND_HALF_UP));

		BigDecimal purchasePrice = baseGoodsResDto.getPurchasePrice();
		if (null == purchasePrice) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "采购指导价不能为空");
		}
		baseGoods.setPurchasePrice(purchasePrice.setScale(8, BigDecimal.ROUND_HALF_UP));

		BigDecimal salePrice = baseGoodsResDto.getSalePrice();
		if (null == salePrice) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "销售指导价不能为空");
		}
		baseGoods.setSalePrice(salePrice.setScale(8, BigDecimal.ROUND_HALF_UP));

		String purCurrencyTypeName = (null == baseGoodsResDto.getPurCurrencyTypeName() ? ""
				: baseGoodsResDto.getPurCurrencyTypeName().trim());
		if (StringUtils.isBlank(purCurrencyTypeName)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "采购指导价币种不能为空");
		}
		String purCurrencyType = ServiceSupport.getCodeByBizValue(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
				purCurrencyTypeName);
		if (StringUtils.isBlank(purCurrencyType)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "采购指导价币种【" + purCurrencyTypeName + "】不存在");
		}
		baseGoods.setPurCurrencyType(Integer.parseInt(purCurrencyType));

		String saleCurrencyTypeName = (null == baseGoodsResDto.getSaleCurrencyTypeName() ? ""
				: baseGoodsResDto.getSaleCurrencyTypeName().trim());
		if (StringUtils.isBlank(saleCurrencyTypeName)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "销售指导价币种不能为空");
		}
		String saleCurrencyType = ServiceSupport.getCodeByBizValue(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
				saleCurrencyTypeName);
		if (StringUtils.isBlank(saleCurrencyType)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "销售指导价币种【" + saleCurrencyTypeName + "】不存在");
		}
		baseGoods.setSaleCurrencyType(Integer.parseInt(saleCurrencyType));

		baseGoods.setStatus(BaseConsts.ONE); // 1-待提交
		baseGoods.setCreator(ServiceSupport.getUser().getChineseName());
		return baseGoods;
	}

	private List<CodeValue> getOperList(Integer state) {
		// 1.根据状态得到操作列表
		List<String> operNameList = getOperListByState(state);
		// 2.将上述的操作列表通过权限过滤
		List<CodeValue> oprResult = ServiceSupport.getOperListByPermission(operNameList,
				BaseGoodsResDto.Operate.operMap);
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

}
