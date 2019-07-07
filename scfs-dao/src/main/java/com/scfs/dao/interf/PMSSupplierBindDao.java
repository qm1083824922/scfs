package com.scfs.dao.interf;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import com.scfs.domain.interf.dto.PMSSupplierBindReqDto;
import com.scfs.domain.interf.entity.PMSSupplierBind;

public interface PMSSupplierBindDao {
	int deleteById(Integer id);

	int insert(PMSSupplierBind record);

	PMSSupplierBind selectById(Integer id);

	int submit(PMSSupplierBind record);

	int update(PMSSupplierBind record);

	List<PMSSupplierBind> queryByCondition(PMSSupplierBindReqDto pMSSupplierBindReqDto, RowBounds rowBounds);

	List<PMSSupplierBind> queryByCondition(PMSSupplierBindReqDto pMSSupplierBindReqDto);

	List<PMSSupplierBind> queryBySub(PMSSupplierBindReqDto pMSSupplierBindReqDto);

	List<PMSSupplierBind> queryByProjectStatus(PMSSupplierBindReqDto pMSSupplierBindReqDto);

	PMSSupplierBind queryEntityByProjectIdAndSupplierId(@Param("projectId") Integer projectId,
			@Param("supplierId") Integer supplierId);

	PMSSupplierBind queryEntityBySupplierNo(@Param("pmsSupplierNo") String pmsSupplierNo);

	/**
	 * 根据供应商编码和经营单位查询项目
	 * 
	 * @param pMSSupplierBindReqDto
	 * @return
	 */
	PMSSupplierBind queryBySupplierNoAndBusi(PMSSupplierBindReqDto pMSSupplierBindReqDto);
}