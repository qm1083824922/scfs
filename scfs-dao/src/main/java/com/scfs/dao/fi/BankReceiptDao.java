package com.scfs.dao.fi;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.scfs.domain.fi.dto.req.BankReceiptSearchReqDto;
import com.scfs.domain.fi.entity.AdvanceReceiptRel;
import com.scfs.domain.fi.entity.BankReceipt;

@Repository
public interface BankReceiptDao {
	/**
	 * 添加操作
	 * 
	 * @param bankReceipt
	 * @return
	 */
	int insert(BankReceipt bankReceipt);

	/**
	 * 修改操作
	 * 
	 * @param bankReceipt
	 * @return
	 */
	int updateById(BankReceipt bankReceipt);

	/**
	 * 查询分页
	 * 
	 * @return
	 */
	List<BankReceipt> queryResultsByCon(BankReceiptSearchReqDto bankReceiptSearchReqDto, RowBounds rowBounds);

	/**
	 * 查询不分页
	 * 
	 * @return
	 */
	List<BankReceipt> queryResultsByCon(BankReceiptSearchReqDto bankReceiptSearchReqDto);

	/**
	 * 查询刷新水单
	 * 
	 * @return
	 */
	List<BankReceipt> queryRefreshResults();

	/**
	 * 获取详情
	 * 
	 * @return
	 */
	BankReceipt queryEntityById(Integer id);

	/**
	 * 获取到期的承兑汇票付款单
	 */
	List<BankReceipt> queryExpireResults();

	/**
	 * 查询自动核销付款单信息
	 * 
	 * @param requestPayTime
	 * @return
	 */
	List<BankReceipt> queryRefreshReceiveInfo(@Param("requestPayTime") String requestPayTime);

	/**
	 * 查询自动核销费用信息
	 * 
	 * @param expireDate
	 * @return
	 */
	List<BankReceipt> queryRefreshFeeInfo(@Param(value = "expireDate") String expireDate);

	/**
	 * 统计
	 * 
	 * @return
	 */
	List<BankReceipt> sumBankReceipt(BankReceiptSearchReqDto bankReceiptSearchReqDto);

	/**
	 * 查询销售单审核的待核销水单
	 * 
	 * @param bankReceiptSearchReqDto
	 * @return
	 */
	List<BankReceipt> queryResultsByCon4BillDelivery(BankReceiptSearchReqDto bankReceiptSearchReqDto);

	/**
	 * 销售单财务主管审核时查询指定水单
	 * 
	 * @param bankReceiptSearchReqDto
	 * @return
	 */
	BankReceipt queryResultsById4BillDeliveryAudit(BankReceiptSearchReqDto bankReceiptSearchReqDto);

	List<BankReceipt> queryRootByConPayAdvanceRel(BankReceiptSearchReqDto bankReceiptSearchReqDto, RowBounds rowBounds);

	List<BankReceipt> queryRootByPayAdvanceRel(BankReceiptSearchReqDto bankReceiptSearchReqDto);

	List<BankReceipt> queryRootByConPayAdvanceRel(BankReceiptSearchReqDto bankReceiptSearchReqDto);

	List<AdvanceReceiptRel> querySubAmountByRoot(Integer id);

	/**
	 * 获取总数
	 * 
	 * @param bankReceiptSearchReqDto
	 * @return
	 */
	public int isOverasyncMaxLine(BankReceiptSearchReqDto bankReceiptSearchReqDto);

	/**
	 * 查询满足退款信息的水单数据
	 * 
	 * @param bankReceipt
	 * @return
	 */
	List<BankReceipt> queryRefundResult(BankReceiptSearchReqDto bankReceiptSearchReqDto, RowBounds rowBounds);

	/**
	 * 查询需刷新融资池的水单
	 * 
	 * @return
	 */
	List<BankReceipt> queryRefreshProjectPoolResults();

	/**
	 * 查询所有的预收货款且带待核销
	 * 
	 * @param bankReceiptSearchReqDto
	 * @return
	 */
	List<BankReceipt> queryBankReceptTypeThreeAllResult(BankReceiptSearchReqDto bankReceiptSearchReqDto);

	/**
	 * 根据水单编号查询水单
	 * 
	 * @return
	 */
	BankReceipt queryEntityByBankReceiptNo(String receiptNo);

	/**
	 * 查询需要刷新资金池的水单数据
	 * 
	 * @return
	 */
	List<BankReceipt> queryRefreshReceiptPoolResults();
}
