package com.scfs.dao.po;

import com.scfs.domain.po.dto.req.DistributeAccountReqDto;
import com.scfs.domain.po.dto.req.PoTitleReqDto;
import com.scfs.domain.po.entity.PurchaseOrderLineTaxGroupSum;
import com.scfs.domain.po.entity.PurchaseOrderTitle;
import com.scfs.domain.po.entity.PurchaseReturnDtl;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PurchaseOrderTitleDao {

	List<PurchaseOrderTitle> queryPurchaseOrderTitleList(PoTitleReqDto poTitleReqDto, RowBounds rowBounds);

	List<PurchaseOrderTitle> queryPurchaseOrderTitleList(PoTitleReqDto poTitleReqDto);

	int countPurchaseOrderTitle(PoTitleReqDto poTitleReqDto);

	List<PurchaseOrderTitle> queryPurchaseOrderTitleListNoUser(PoTitleReqDto poTitleReqDto);

	List<PurchaseOrderTitle> sumPoTitle(PoTitleReqDto poTitleReqDto);

	int insert(PurchaseOrderTitle purchaseOrderTitle);

	PurchaseOrderTitle queryAndLockById(Integer id);

	int updatePurchaseOrderTitleById(PurchaseOrderTitle purchaseOrderTitle);

	PurchaseOrderTitle queryEntityById(Integer id);

	int updatePrintNum(PurchaseOrderTitle purchaseOrderTitle);

	/** 物理删除 */
	int deleteById(Integer id);

	/** 查询入库单理货明细 */
	List<PurchaseReturnDtl> queryBillInStoreListUndivide(PoTitleReqDto poTitleReqDto, RowBounds rowBounds);

	List<PurchaseReturnDtl> queryBillInStoreListUndivide(PoTitleReqDto poTitleReqDto);

	PurchaseOrderTitle queryEntityByParam(PoTitleReqDto poTitleReqDto);

	List<PurchaseOrderTitle> queryFinishedPoByAppendNo(String appendNo);

	List<PurchaseOrderTitle> queryDistribePoByAppendNo(String appendNo);

	/**
	 * 仅月底冲销使用
	 */
	public List<PurchaseOrderTitle> queryDistributeOrderGroupByProjectId(
			DistributeAccountReqDto distributeAccountReqDto);

	/**
	 * 仅月底冲销使用
	 */
	public List<PurchaseOrderLineTaxGroupSum> queryTaxGroupSumByProjectId(
			DistributeAccountReqDto distributeAccountReqDto);

	/**
	 * 仅月底冲销使用
	 */
	public BigDecimal queryUnDistributeAmountByProjectId(DistributeAccountReqDto distributeAccountReqDto);

	/**
	 * 根据退货单号查询铺货退货类型 并且为已完成状态的数据
	 * 
	 * @param appendNo
	 * @return
	 */
	PurchaseOrderTitle queryDistribeReturnPoAppend(String appendNo);

	/**
	 * Po待付款金额信息查询
	 * 
	 * @param poTitleReqDto
	 * @return
	 */
	List<PurchaseOrderTitle> queryPuOrderTitleListGroupByNo(PoTitleReqDto poTitleReqDto, RowBounds rowBounds);

	List<PurchaseOrderTitle> queryPuOrderTitleListGroupByNo(PoTitleReqDto poTitleReqDto);

	/**
	 * Po待付款金额汇总
	 * 
	 * @param poTitleReqDto
	 * @return
	 */
	BigDecimal queryPoBlance(PoTitleReqDto poTitleReqDto);

	/**
	 * 根据付款单ID查询采购单和付款单的关联数据
	 * 
	 */
	List<PurchaseOrderTitle> queryPoRelationTitleResult(Integer payId);
}
