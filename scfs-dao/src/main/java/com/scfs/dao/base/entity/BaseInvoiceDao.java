package com.scfs.dao.base.entity;

import com.scfs.domain.base.entity.BaseInvoice;
import com.scfs.domain.base.subject.dto.req.AddInvoiceDto;
import com.scfs.domain.base.subject.dto.req.QueryInvoiceReqDto;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BaseInvoiceDao {
	int insertBaseInvoice(AddInvoiceDto addInvoiceDto);

	/**
	 * 根据主键查询
	 */
	BaseInvoice loadAndLockEntityById(@Param(value = "id") int id);

	BaseInvoice queryInvoiceById(Integer id);

	/**
	 * 查询开票信息
	 */
	List<BaseInvoice> queryInvoiceBySubjectId(QueryInvoiceReqDto queryInvoiceReqDto, RowBounds rowBounds);

	/**
	 * 查询所有主体的开票信息
	 * 
	 * @return
	 */
	List<BaseInvoice> queryAllInvoice(@Param("updateAt") String updateAt);

	/**
	 * 查询最后更新时间
	 * 
	 * @return
	 */
	Date queryLastUpdateAt();

	/**
	 * 修改开票信息
	 */
	int updateBaseInvoiceById(BaseInvoice baseInvoice);

	/**
	 * 作废信息
	 */
	int invalidInvoiceById(BaseInvoice baseInvoice);

}
