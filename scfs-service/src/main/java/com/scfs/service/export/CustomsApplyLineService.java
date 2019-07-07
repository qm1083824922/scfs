package com.scfs.service.export;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.export.CustomsApplyDao;
import com.scfs.dao.export.CustomsApplyLineDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.logistics.BillOutStoreDtlDao;
import com.scfs.domain.base.entity.BaseGoods;
import com.scfs.domain.export.dto.req.CustomsApplyLineSearchReqDto;
import com.scfs.domain.export.dto.req.CustomsApplyReqDto;
import com.scfs.domain.export.dto.resp.CustomsApplyLineResDto;
import com.scfs.domain.export.entity.CustomsApply;
import com.scfs.domain.export.entity.CustomsApplyLine;
import com.scfs.domain.export.entity.CustomsApplyLineSum;
import com.scfs.domain.logistics.entity.BillOutStoreDtl;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

/**
 * Created by Administrator on 2016年12月6日.
 */
@Service
public class CustomsApplyLineService {
	@Autowired
	private CustomsApplyLineDao customsApplyLineDao;
	@Autowired
	private CustomsApplyDao customsApplyDao;
	@Autowired
	private BillOutStoreDtlDao billOutStoreDtlDao;
	@Autowired
	private CacheService cacheService;

	/**
	 * 查询报关明细
	 * 
	 * @param customsApplyLineSearchReqDto
	 * @return
	 */
	public PageResult<CustomsApplyLineResDto> queryCustomsApplyLinesByCustomsApplyId(
			CustomsApplyLineSearchReqDto customsApplyLineSearchReqDto) {
		PageResult<CustomsApplyLineResDto> result = new PageResult<CustomsApplyLineResDto>();

		int offSet = PageUtil.getOffSet(customsApplyLineSearchReqDto.getPage(),
				customsApplyLineSearchReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, customsApplyLineSearchReqDto.getPer_page());
		List<CustomsApplyLine> customsApplyLineList = customsApplyLineDao
				.queryResultsByCustomsApplyId(customsApplyLineSearchReqDto.getCustomsApplyId(), rowBounds);
		List<CustomsApplyLineResDto> customsApplyLineResDtoList = convertToResDto(customsApplyLineList);
		result.setItems(customsApplyLineResDtoList);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), customsApplyLineSearchReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(customsApplyLineSearchReqDto.getPage());
		result.setPer_page(customsApplyLineSearchReqDto.getPer_page());

		return result;
	}

	/**
	 * 根据ID查询报关明细
	 * 
	 * @param customsApplyLineSearchReqDto
	 * @return
	 */
	public Result<CustomsApplyLineResDto> queryCustomsApplyLineById(
			CustomsApplyLineSearchReqDto customsApplyLineSearchReqDto) {
		Result<CustomsApplyLineResDto> result = new Result<CustomsApplyLineResDto>();
		CustomsApplyLine customsApplyLine = customsApplyLineDao.queryEntityById(customsApplyLineSearchReqDto.getId());
		CustomsApplyLineResDto customsApplyLineResDto = convertToResDto(customsApplyLine);
		BillOutStoreDtl billOutStoreDtl = billOutStoreDtlDao.queryEntityById(customsApplyLine.getBillDtlId());
		customsApplyLineResDto.setCustomsNumMax(billOutStoreDtl.getSendNum()
				.subtract(billOutStoreDtl.getCustomsDeclareNum().subtract(customsApplyLine.getCustomsNum()))
				.toString());
		result.setItems(customsApplyLineResDto);
		return result;
	}

	/**
	 * 新增报关明细
	 * 
	 * @param customsApplyReqDto
	 */
	public void addCustomsApplyLines(CustomsApplyReqDto customsApplyReqDto) {
		CustomsApply customsApply = customsApplyDao.queryAndLockEntityById(customsApplyReqDto.getId());
		if (customsApply.getStatus().equals(BaseConsts.ONE) && customsApply.getIsDelete().equals(BaseConsts.ZERO)) { // 1-待提交
			BigDecimal taxRate = customsApply.getTaxRate();
			BigDecimal oldTaxRate = taxRate;
			List<CustomsApplyLine> customsApplyLineList = customsApplyReqDto.getCustomsApplyLineList();
			if (!CollectionUtils.isEmpty(customsApplyLineList)) {
				for (CustomsApplyLine customsApplyLine : customsApplyLineList) {
					customsApplyLine.setCustomsApplyId(customsApply.getId());
					customsApplyLine.setCreator(ServiceSupport.getUser().getChineseName());
					customsApplyLine.setCreatorId(ServiceSupport.getUser().getId());
					BigDecimal currTaxRate = customsApplyLine.getTaxRate();
					if (null == taxRate && null != currTaxRate) {
						taxRate = currTaxRate;
					} else if (null != taxRate && null != currTaxRate) {
						if (DecimalUtil.ne(taxRate, currTaxRate)) {
							if (null == oldTaxRate) {
								throw new BaseException(ExcMsgEnum.CUSTOMS_APPLY_DTL_TAX_RATE_DIFF_ERROR);
							} else {
								throw new BaseException(ExcMsgEnum.CUSTOMS_APPLY_DTL_TAX_RATE_TYPE_DIFF_ERROR);
							}
						}
					} else {
						throw new BaseException(ExcMsgEnum.CUSTOMS_APPLY_DTL_TAX_RATE_NOT_EXIST);
					}

					customsApplyLineDao.insert(customsApplyLine);
					// 更新出库单明细报关数量
					updateCustomsDeclareNum(customsApplyLine, customsApplyLine, BaseConsts.ONE);
				}

				// 更新报关申请的税率、报关数量、报关金额、报关税额
				customsApply.setTaxRate(taxRate);
				updateCustomsApplyInfo(customsApply);
			}
		} else {
			throw new BaseException(ExcMsgEnum.CUSTOMS_APPLY_LINE_ADD_STATUS_ERROR);
		}
	}

	/**
	 * 更新报关明细
	 * 
	 * @param customsApplyReqDto
	 * @return
	 */
	public void updateCustomsApplyLines(CustomsApplyReqDto customsApplyReqDto) {
		CustomsApply customsApply = customsApplyDao.queryAndLockEntityById(customsApplyReqDto.getId());
		if (customsApply.getStatus().equals(BaseConsts.ONE) && customsApply.getIsDelete().equals(BaseConsts.ZERO)) { // 1-待提交
			List<CustomsApplyLine> customsApplyLineList = customsApplyReqDto.getCustomsApplyLineList();
			if (!CollectionUtils.isEmpty(customsApplyLineList)) {
				for (CustomsApplyLine customsApplyLine : customsApplyLineList) {
					CustomsApplyLine currCustomsApplyLine = customsApplyLineDao
							.queryEntityById(customsApplyLine.getId());
					BillOutStoreDtl billOutStoreDtl = billOutStoreDtlDao
							.queryEntityById(currCustomsApplyLine.getBillDtlId());
					// 检查报关数量是否超过剩余报关数量
					validateCustomsNum(currCustomsApplyLine.getCustomsNum(), customsApplyLine.getCustomsNum(),
							billOutStoreDtl);

					customsApplyLineDao.updateById(customsApplyLine);
					// 更新出库单明细报关数量
					updateCustomsDeclareNum(currCustomsApplyLine, customsApplyLine, BaseConsts.TWO);
				}

				// 更新报关申请的税率、报关数量、报关金额、报关税额
				updateCustomsApplyInfo(customsApply);
			}
		} else {
			throw new BaseException(ExcMsgEnum.CUSTOMS_APPLY_LINE_UPDATE_STATUS_ERROR);
		}
	}

	/**
	 * 删除报关明细
	 * 
	 * @param billDeliveryDtlSearchReqDto
	 * @return
	 */
	public void deleteCustomsApplyLinesByIds(CustomsApplyLineSearchReqDto customsApplyLineSearchReqDto) {
		List<Integer> ids = customsApplyLineSearchReqDto.getIds();
		Integer customsApplyId = customsApplyLineSearchReqDto.getCustomsApplyId();
		if (!CollectionUtils.isEmpty(ids) && null != customsApplyId) {
			CustomsApply customsApply = customsApplyDao.queryAndLockEntityById(customsApplyId);
			if (customsApply.getStatus().equals(BaseConsts.ONE) && customsApply.getIsDelete().equals(BaseConsts.ZERO)) { // 1-待提交
				for (Integer id : ids) {
					CustomsApplyLine customsApplyLine = customsApplyLineDao.queryEntityById(id);
					customsApplyLineDao.deleteById(id);
					// 更新出库单明细报关数量
					updateCustomsDeclareNum(customsApplyLine, customsApplyLine, BaseConsts.THREE);
				}

				// 明细全部删除时，税率为设置为空
				int count = customsApplyLineDao.queryCountByCustomsApplyId(customsApply.getId());
				if (count == 0) {
					customsApply.setTaxRate(null);
				}
				// 更新报关申请的税率、报关数量、报关金额、报关税额
				updateCustomsApplyInfo(customsApply);
			} else {
				throw new BaseException(ExcMsgEnum.CUSTOMS_APPLY_LINE_DELETE_STATUS_ERROR);
			}
		}
	}

	/**
	 * 更新出库单明细报关数量
	 * 
	 * @param currCustomsApplyLine
	 *            当前报关明细
	 * @param newCustomsApplyLine
	 *            新报关明细
	 * @param operateFlag
	 *            1-新增 2-修改 3-删除
	 */
	public void updateCustomsDeclareNum(CustomsApplyLine currCustomsApplyLine, CustomsApplyLine newCustomsApplyLine,
			Integer operateFlag) {
		BillOutStoreDtl billOutStoreDtl = billOutStoreDtlDao.queryEntityById(currCustomsApplyLine.getBillDtlId());

		if (null != billOutStoreDtl) {
			BigDecimal customsDeclareNum = (null == billOutStoreDtl.getCustomsDeclareNum() ? BigDecimal.ZERO
					: billOutStoreDtl.getCustomsDeclareNum());
			BigDecimal sendNum = (null == billOutStoreDtl.getSendNum() ? BigDecimal.ZERO
					: billOutStoreDtl.getSendNum());

			BillOutStoreDtl billOutStoreDtl2 = new BillOutStoreDtl();
			billOutStoreDtl2.setId(billOutStoreDtl.getId());
			// 更新出库单明细报关数量
			if (operateFlag.equals(BaseConsts.ONE)) { // 1-新增
				billOutStoreDtl2
						.setCustomsDeclareNum(DecimalUtil.add(customsDeclareNum, newCustomsApplyLine.getCustomsNum()));
				if (DecimalUtil.gt(billOutStoreDtl2.getCustomsDeclareNum(), sendNum)) {
					throw new BaseException(ExcMsgEnum.CUSTOMS_APPLY_DTL_SEND_NUM_EXCEED);
				}
			}
			if (operateFlag.equals(BaseConsts.TWO)) { // 2-修改
				billOutStoreDtl2.setCustomsDeclareNum(
						DecimalUtil.add(DecimalUtil.subtract(customsDeclareNum, currCustomsApplyLine.getCustomsNum()),
								newCustomsApplyLine.getCustomsNum()));
				if (DecimalUtil.gt(billOutStoreDtl2.getCustomsDeclareNum(), sendNum)) {
					throw new BaseException(ExcMsgEnum.CUSTOMS_APPLY_DTL_SEND_NUM_EXCEED);
				}
			}
			if (operateFlag.equals(BaseConsts.THREE)) { // 3-删除
				billOutStoreDtl2.setCustomsDeclareNum(
						DecimalUtil.subtract(customsDeclareNum, newCustomsApplyLine.getCustomsNum()));
				if (DecimalUtil.gt(billOutStoreDtl2.getCustomsDeclareNum(), BigDecimal.ZERO)) {
					throw new BaseException(ExcMsgEnum.CUSTOMS_APPLY_DTL_SEND_NUM_EXCEED);
				}
			}
			billOutStoreDtlDao.updateById(billOutStoreDtl2);
		}
	}

	/**
	 * 更新报关申请的税率、报关数量、报关金额、报关税额
	 * 
	 * @param customsApply
	 */
	public void updateCustomsApplyInfo(CustomsApply customsApply) {
		CustomsApplyLineSum customsApplyLineSum = customsApplyLineDao.querySumByCustomsApplyId(customsApply.getId());
		if (null != customsApplyLineSum) {
			customsApply.setCustomsNum(null == customsApplyLineSum.getCustomsNum() ? BigDecimal.ZERO
					: customsApplyLineSum.getCustomsNum());
			customsApply.setCustomsAmount(null == customsApplyLineSum.getCustomsAmount() ? BigDecimal.ZERO
					: customsApplyLineSum.getCustomsAmount());
			if (null != customsApply.getTaxRate()) {
				customsApply
						.setCustomsTaxAmount(
								DecimalUtil
										.formatScale2(
												DecimalUtil.multiply(
														DecimalUtil.divide(customsApply.getCustomsAmount(),
																DecimalUtil.add(customsApply.getTaxRate(),
																		new BigDecimal(1))),
														customsApply.getTaxRate())));
			} else {
				customsApply.setCustomsTaxAmount(BigDecimal.ZERO);
			}
			customsApplyDao.updateCustomsApplyInfo(customsApply);
		}
	}

	/**
	 * 添加和修改报关明细时校验报关数量是否超过出库单已报关数量
	 */
	private void validateCustomsNum(BigDecimal oldCustomsNum, BigDecimal newCustomsNum,
			BillOutStoreDtl billOutStoreDtl) {
		BigDecimal sendNum = (null == billOutStoreDtl.getSendNum() ? BigDecimal.ZERO : billOutStoreDtl.getSendNum());
		oldCustomsNum = (null == oldCustomsNum ? BigDecimal.ZERO : oldCustomsNum);
		newCustomsNum = (null == newCustomsNum ? BigDecimal.ZERO : newCustomsNum);
		BigDecimal customsDeclareNum = (null == billOutStoreDtl.getCustomsDeclareNum() ? BigDecimal.ZERO
				: billOutStoreDtl.getCustomsDeclareNum());
		BigDecimal currCustomsDeclareNum = DecimalUtil.add(DecimalUtil.subtract(customsDeclareNum, oldCustomsNum),
				newCustomsNum);
		if (DecimalUtil.gt(currCustomsDeclareNum, sendNum)) {
			throw new BaseException(ExcMsgEnum.CUSTOMS_APPLY_DTL_SEND_NUM_EXCEED);
		}
	}

	private List<CustomsApplyLineResDto> convertToResDto(List<CustomsApplyLine> customsApplyLineList) {
		List<CustomsApplyLineResDto> customsApplyLineResDtoList = new ArrayList<CustomsApplyLineResDto>(5);
		if (CollectionUtils.isEmpty(customsApplyLineList)) {
			return customsApplyLineResDtoList;
		}
		for (CustomsApplyLine customsApplyLine : customsApplyLineList) {
			CustomsApplyLineResDto customsApplyLineResDto = convertToResDto(customsApplyLine);
			customsApplyLineResDtoList.add(customsApplyLineResDto);
		}
		return customsApplyLineResDtoList;
	}

	private CustomsApplyLineResDto convertToResDto(CustomsApplyLine customsApplyLine) {
		CustomsApplyLineResDto customsApplyLineResDto = new CustomsApplyLineResDto();

		if (null != customsApplyLine) {
			BeanUtils.copyProperties(customsApplyLine, customsApplyLineResDto);

			if (customsApplyLine.getGoodsId() != null) {
				BaseGoods baseGoods = cacheService.getGoodsById(customsApplyLine.getGoodsId());
				if (null != baseGoods) {
					customsApplyLineResDto.setGoodsName(baseGoods.getName());
					customsApplyLineResDto.setGoodsNumber(baseGoods.getNumber());
					customsApplyLineResDto.setGoodsType(baseGoods.getType());
					customsApplyLineResDto.setTaxRate(baseGoods.getTaxRate());
					customsApplyLineResDto.setUnit(baseGoods.getUnit());
				}
			}
		}
		return customsApplyLineResDto;
	}

}
