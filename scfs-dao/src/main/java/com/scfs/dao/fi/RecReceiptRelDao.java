package com.scfs.dao.fi;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.scfs.domain.common.dto.resp.RefreshFundUsedResDto;
import com.scfs.domain.fi.dto.req.RecReceiptRelSearchReqDto;
import com.scfs.domain.fi.entity.RecReceiptRel;
import com.scfs.domain.fi.entity.ReceiptRecSum;

@Repository
public interface RecReceiptRelDao {
	/**
	 * 添加数据
	 * 
	 * @param recReceiptRel
	 * @return
	 */
	int insert(RecReceiptRel recReceiptRel);

	/**
	 * 修改数据
	 * 
	 * @param recReceiptRel
	 * @return
	 */
	int updateById(RecReceiptRel recReceiptRel);

	/**
	 * 获取实体
	 * 
	 * @param id
	 * @return
	 */
	RecReceiptRel queryEntityById(int id);

	/**
	 * 获取详情
	 * 
	 * @param id
	 * @return
	 */
	RecReceiptRel queryDetailById(int id);

	/**
	 * 删除
	 * 
	 * @param id
	 * @return
	 */
	int deleteById(int id);

	/**
	 * 分页查询
	 * 
	 * @return
	 */
	List<RecReceiptRel> queryResultsByCon(RecReceiptRelSearchReqDto recReceiptRel, RowBounds rowBounds);

	/**
	 * 获取总数据
	 * 
	 * @param recReceiptRel
	 * @return
	 */
	List<RecReceiptRel> queryResultsByCon(RecReceiptRelSearchReqDto recReceiptRel);

	/**
	 * 获取水单核销总金额 TODO.
	 *
	 * @param receiptId
	 * @return
	 */
	BigDecimal querySumByReceiptId(Integer receiptId);

	/**
	 * 获取应收核销金额
	 */
	List<ReceiptRecSum> queryReceiptRecSumByReceiptId(Integer receiptId);

	/**
	 * 根据水单ID获取核销关系
	 * 
	 * @param ReceiptId
	 * @return
	 */
	List<RecReceiptRel> getRecReceiptRelByReceiptId(Integer receiptId);

	/**
	 * 获取需刷新资金占用数据
	 * 
	 * @return
	 */
	List<RefreshFundUsedResDto> getRefreshFundUsedList(@Param("isVirtualReceipt") Integer isVirtualReceipt);

	/**
	 * 根据应收ID获取核销关系
	 * 
	 * @param recId
	 * @return
	 */
	List<RecReceiptRel> getFifoRecReceiptRelByRecId(@Param("recId") Integer recId,
			@Param("isVirtualReceipt") Integer isVirtualReceipt);

	/**
	 * 根据水单ID获取资金占用总金额
	 * 
	 * @param receiptId
	 * @return
	 */
	BigDecimal queryFundUsedByReceiptId(Integer receiptId);

	/**
	 * 根据水单ID获取资金占用总金额
	 * 
	 * @param receiptNo
	 * @return
	 */
	BigDecimal queryFundUsedByReceiptNo(String receiptNo);

	/**
	 * 根据水单ID获取出库单付款金额
	 * 
	 * @param receiptId
	 * @return
	 */
	BigDecimal queryPayAmountByBankReceiptId(Integer receiptId);

	/**
	 * 根据水单ID获取出库单列表
	 * 
	 * @param receiptId
	 * @return
	 */
	List<String> queryBillOutStoreBillNosByBankReceiptNo(String bankReceiptNo);

	/**
	 * 查询水单下应收记录数量
	 */
	int queryCountByBankReceiptId(Integer receiptId);
}
