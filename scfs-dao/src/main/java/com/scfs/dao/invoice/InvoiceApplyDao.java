package com.scfs.dao.invoice;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.scfs.domain.invoice.dto.req.InvoiceApplyManagerReqDto;
import com.scfs.domain.invoice.entity.InvoiceApplyManager;

public interface InvoiceApplyDao {
	/** 删除发票 */
	int deleteById(Integer id);

	/** 新增发票申请 */
	int insert(InvoiceApplyManager record);

	/** 更新发票 */
	int updateById(InvoiceApplyManager record);

	/** 更新状态 */
	int updateStatus(InvoiceApplyManager record);

	/** 通过Id查询发票 */
	InvoiceApplyManager queryEntityById(Integer id);

	/** 查询发票 */
	List<InvoiceApplyManager> queryInvoiceResultsByCon(InvoiceApplyManagerReqDto projectReqDto, RowBounds rowBounds);

	/** 查询发票 */
	List<InvoiceApplyManager> queryInvoiceResultsByCon(InvoiceApplyManagerReqDto projectReqDto);

	List<InvoiceApplyManager> querySumByCon(InvoiceApplyManagerReqDto projectReqDto);

	/** 查询发票 */
	InvoiceApplyManager queryEntityByApplyNo(String applyNo);

	int updatePrintNum(InvoiceApplyManager record);

	int isOverasyncMaxLine(InvoiceApplyManagerReqDto projectReqDto);
}