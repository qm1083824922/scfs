package com.scfs.dao.po;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.scfs.domain.po.dto.req.PoTitleReqDto;
import com.scfs.domain.po.entity.PurchaseOrderLine;
import com.scfs.domain.po.entity.PurchaseOrderTitle;
import com.scfs.domain.po.entity.PurchaseReturnDtl;
import com.scfs.domain.po.model.PoLineModel;

@Repository
public interface PurchaseOrderLineDao {

	/***
	 * 根据采购单头ID查询采购单信息
	 * 
	 * @param id
	 * @param rowBounds
	 * @return
	 */
	List<PoLineModel> queryPoLineListByPoId(Integer id, RowBounds rowBounds);

	/***
	 * 根据采购单头ID查询采购单信息，不分页
	 * 
	 * @param id
	 * @return
	 */
	List<PoLineModel> queryPoLineListByPoId(Integer id);

	PoLineModel queryPoLineByPoLineId(Integer poLineId);

	int countPoLineListByPoId(Integer id);

	int countPoLineListByCon(PoTitleReqDto poTitleReqDto);

	int deleteAllPurchaseLineByPoId(PurchaseOrderLine purchaseOrderLine);

	/**
	 * 根据ID集合查询po行信息
	 * 
	 * @param ids
	 * @return
	 */
	List<PoLineModel> queryPoLinesByIds(@Param("ids") List<Integer> ids);

	int insert(PurchaseOrderLine purchaseOrderLine);

	PurchaseOrderLine queryPurchaseOrderLineById(Integer id);

	int updatePurchaseOrderLineById(PurchaseOrderLine purchaseOrderLine);

	/**
	 * 根据采购单行查询订单总额和数量
	 * 
	 * @param poId
	 * @return
	 */
	PurchaseOrderTitle queryTotalByPoId(@Param("poId") Integer poId);

	/***
	 * 获取收票相关采购单信息
	 * 
	 * @param reqDto
	 * @param rowBounds
	 * @return
	 */
	List<PoLineModel> queryPoLineListByCon(PoTitleReqDto reqDto, RowBounds rowBounds);

	List<PoLineModel> queryPoLineListByCon(PoTitleReqDto reqDto);

	/**
	 * 物理删除 TODO.
	 *
	 * @param id
	 * @return
	 */
	int deleteById(Integer id);

	/**
	 * 查询可收货明细数量
	 * 
	 * @param poId
	 * @return
	 */
	int queryReceiveCountByPoId(@Param("poId") Integer poId);

	/** 根据商品的ID和币种 **/
	List<PurchaseOrderLine> queryPurchaseOrderLineByCon(PoTitleReqDto poTitleReqDto);

	/** 查询采购退货单对应明细信息总数量 */
	BigDecimal queryReturnNumByPoId(@Param("poId") Integer poId);

	/** 查询采购退货单对应明细信息总金额 */
	BigDecimal queryReturnAmountByPoId(@Param("poId") Integer poId);

	/** 查询采购退货单明细 */
	List<PurchaseReturnDtl> queryBillInStoreListDivide(PoTitleReqDto poTitleReqDto, RowBounds rowBounds);

	List<PurchaseReturnDtl> queryBillInStoreListDivide(PoTitleReqDto poTitleReqDto);

	/** 更新采购退货单明细 */
	int updatePurchaseReturnLine(PurchaseReturnDtl purchaseReturnDtl);

	PurchaseReturnDtl queryAndLockReturnById(Integer id);

	/**
	 * 通过商品采购编号获取信息
	 * 
	 * @param poId
	 * @param purchaseSn
	 * @return
	 */
	public List<PurchaseOrderLine> queryLineBySkuNumber(PoTitleReqDto poTitleReqDto);

	/** 查询铺货业务尾款付款金额 **/
	BigDecimal queryRecPayAmount(Integer poId);

	/**
	 * 根据商品ID和供应商查询铺货商品
	 * 
	 * @param goodsId
	 * @return
	 */
	List<PoLineModel> queryDistributionLinesByGoodsIdAndSupplierId(PoTitleReqDto poTitleReqDto);

	/**
	 * 根据商品id 和附属编号查询采购单明细并且计算可用数量的总量
	 * 
	 * @param poTitleReqDto
	 * @return
	 */
	public PoLineModel queryLineByGoodsIdAndAppendNo(PoTitleReqDto poTitleReqDto);

	/**
	 * 根据采购单号和状态类型商品SKU 查询铺货类型的数据
	 * 
	 * @param poTitleReqDto
	 * @return
	 */
	List<PurchaseOrderLine> queryDistribePoLineAppendNoAndGoodID(PoTitleReqDto poTitleReqDto);

	/***
	 * 根据项目，客户和币种查询结算单类型并且可核销金额不为0的
	 * 
	 * @param poTitleReqDto
	 * @return
	 */
	List<PoLineModel> queryPoTitleResultByOrderType(PoTitleReqDto poTitleReqDto);

	/***
	 * 根据采购单明细ID查询明细和头信息的数据
	 * 
	 * @param id
	 * @return
	 */
	PoLineModel queryPurchaseOrderLineAndTitleById(Integer id);

	/**
	 * 根据明细的id查询头信息和明显信息
	 * 
	 * @param id
	 * @return
	 */
	PoLineModel queryTitleAndLineByLineID(Integer id);

	/**
	 * 查询采购明细 根据商品和付款单关联查询
	 * 
	 * @param reqDto
	 * @param rowBounds
	 * @return
	 */
	List<PoLineModel> queryLineResultAndPay(PoTitleReqDto reqDto, RowBounds rowBounds);

}
