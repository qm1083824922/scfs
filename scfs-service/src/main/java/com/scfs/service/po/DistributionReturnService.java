package com.scfs.service.po;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.po.PurchaseOrderLineDao;
import com.scfs.dao.po.PurchaseOrderTitleDao;
import com.scfs.domain.base.entity.BaseGoods;
import com.scfs.domain.po.dto.req.PoTitleReqDto;
import com.scfs.domain.po.dto.resp.PoTitleRespDto;
import com.scfs.domain.po.entity.PurchaseOrderTitle;
import com.scfs.domain.po.model.PoLineModel;
import com.scfs.domain.project.entity.ProjectItem;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.project.ProjectItemService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

/**
 * Created by Administrator on 2017年6月23日.
 */
@Service
public class DistributionReturnService extends PurchaseOrderService {
	@Autowired
	private PurchaseOrderTitleDao purchaseOrderTitleDao;
	@Autowired
	private PurchaseOrderLineDao purchaseOrderLineDao;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private ProjectItemService projectItemService;

	public Result<PoTitleRespDto> queryPurchaseOrderTitleById4BizAudit(Integer id) {
		Result<PoTitleRespDto> result = new Result<PoTitleRespDto>();
		PurchaseOrderTitle purchaseOrderTitle = purchaseOrderTitleDao.queryEntityById(id);
		PoTitleRespDto respDto = super.purchaseOrderTitleConvertToRes(purchaseOrderTitle);
		result.setItems(respDto);
		return result;
	}

	public PageResult<PoLineModel> queryPoLinesByPoTitleId4BizAudit(PoTitleReqDto purchaseOrderTitleReqDto) {
		PageResult<PoLineModel> result = new PageResult<PoLineModel>();
		int offSet = PageUtil.getOffSet(purchaseOrderTitleReqDto.getPage(), purchaseOrderTitleReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, purchaseOrderTitleReqDto.getPer_page());
		List<PoLineModel> poLineList = purchaseOrderLineDao.queryPoLineListByPoId(purchaseOrderTitleReqDto.getId(),
				rowBounds);
		PurchaseOrderTitle purchaseOrderTitle = purchaseOrderTitleDao.queryEntityById(purchaseOrderTitleReqDto.getId());
		processPoLineModels4BizAudit(purchaseOrderTitle, poLineList);

		result.setItems(poLineList);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), purchaseOrderTitleReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(purchaseOrderTitleReqDto.getPage());
		result.setPer_page(purchaseOrderTitleReqDto.getPer_page());
		return result;
	}

	private void processPoLineModels4BizAudit(PurchaseOrderTitle purchaseOrderTitle, List<PoLineModel> poLineList) {
		Date currDate = new Date();
		ProjectItem projectItem = projectItemService.getProjectItem(purchaseOrderTitle.getProjectId());
		if (CollectionUtils.isNotEmpty(poLineList)) {
			for (PoLineModel poLine : poLineList) {
				poLine.setCurrencyName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
						poLine.getCurrencyId() + ""));
				poLine.setMark(purchaseOrderTitle.getRemark());// 备注
				BaseGoods goods = cacheService.getGoodsById(poLine.getGoodsId());
				poLine.setGoodsNo(goods.getNumber());// 商品编号
				poLine.setGoodsName(goods.getName());
				poLine.setDiscountPrice(poLine.getDiscountPrice());// 商品折扣单价
				poLine.setBarSpecificationName(goods.getName() + goods.getSpecification());
				poLine.setGoodsNo(goods.getNumber());
				poLine.setGoodsBarCode(goods.getBarCode());
				poLine.setGoodsType(goods.getType());
				poLine.setSpecification(goods.getSpecification());
				// poLine.setGoodsAmount(poLine.getGoodsAmount() == null ?
				// DecimalUtil.ZERO :
				// DecimalUtil.formatScale2(poLine.getGoodsAmount()));
				poLine.setArrivalAmount(poLine.getArrivalAmount() == null ? DecimalUtil.ZERO
						: DecimalUtil.formatScale2(poLine.getArrivalAmount()));
				poLine.setDiscountAmount(poLine.getDiscountAmount() == null ? DecimalUtil.ZERO
						: DecimalUtil.formatScale2(poLine.getDiscountAmount()));
				poLine.setPaidAmount(poLine.getPaidAmount() == null ? DecimalUtil.ZERO
						: DecimalUtil.formatScale2(poLine.getPaidAmount()));
				poLine.setVolume(goods.getVolume());// 体积
				poLine.setGrossWeight(goods.getGrossWeight());// 毛重
				poLine.setNetWeight(goods.getNetWeight());// 净重
				poLine.setDeductionMoney(poLine.getDeductionMoney());// 抵扣后金额
				if (null != poLine.getGoodsStatus()) {
					poLine.setGoodsStatusName(ServiceSupport.getValueByBizCode(BizCodeConsts.BILL_IN_STORE_GOODS_STATUS,
							poLine.getGoodsStatus() + ""));

				}
				long occupyDays = projectItemService.getOccupyDays(purchaseOrderTitle.getProjectId(),
						poLine.getPayTime(), currDate);
				BigDecimal profitPrice = projectItemService.getProfitPrice(purchaseOrderTitle.getProjectId(),
						poLine.getPayPrice(), poLine.getPayTime(), currDate);
				poLine.setId(poLine.getId());
				poLine.setOccupyDay((int) occupyDays);
				poLine.setOccupyServiceAmount(
						DecimalUtil.formatScale2(DecimalUtil.multiply(profitPrice, poLine.getGoodsNum())));
				poLine.setRefundAmount(DecimalUtil.add(poLine.getGoodsAmount(), poLine.getOccupyServiceAmount()));
				poLine.setFundMonthRate(projectItem.getFundMonthRate());
			}
		}
	}

	public PurchaseOrderTitle queryEntityById(Integer poId) {
		return purchaseOrderTitleDao.queryEntityById(poId);
	}

}
