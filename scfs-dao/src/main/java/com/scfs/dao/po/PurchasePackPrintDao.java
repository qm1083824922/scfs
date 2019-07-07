package com.scfs.dao.po;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.scfs.domain.po.dto.resp.PurchasePackPrintResDto;
import com.scfs.domain.po.entity.PurchasePackPrint;

@Repository
public interface PurchasePackPrintDao {

	int insert(PurchasePackPrint purchasePackPrint);

	void update(PurchasePackPrint purchasePackPrint);

	PurchasePackPrint queryEntityById(Integer id);

	/**
	 * 通过采购单ids获取
	 * 
	 * @param ids
	 * @return
	 */
	List<PurchasePackPrintResDto> queryPoLineListByPoIds(@Param("ids") List<Integer> ids);

	/**
	 * 通过销售单ids获取
	 * 
	 * @param ids
	 * @return
	 */
	List<PurchasePackPrintResDto> queryPoLineListBySaleIds(@Param("ids") List<Integer> ids);
}
