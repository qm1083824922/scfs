package com.scfs.dao.api.pms;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.scfs.domain.api.pms.entity.PmsPayDtl;
import com.scfs.domain.api.pms.entity.PmsPaySum;

@Repository
public interface PmsPayDtlDao {
	/**
	 * 添加数据
	 * 
	 * @param pmsPayDtl
	 * @return
	 */
	int insert(PmsPayDtl pmsPayDtl);

	/**
	 * 修改数据
	 * 
	 * @param pmsPayDtl
	 * @return
	 */
	int updateById(PmsPayDtl pmsPayDtl);

	/**
	 * 删除数据
	 * 
	 * @param pmsPayDtl
	 * @return
	 */
	int deleteById(PmsPayDtl pmsPayDtl);

	/**
	 * 获取详情
	 * 
	 * @param id
	 * @return
	 */
	PmsPayDtl queryEntityById(Integer id);

	/**
	 * 获取详情
	 * 
	 * @param id
	 * @return
	 */
	List<PmsPayDtl> queryPmsPayDtl(PmsPayDtl pmsPayDtl);

	/**
	 * 明细合计
	 * 
	 * @param pmsPayId
	 * @return
	 */
	PmsPaySum queryPmsPaySum(Integer pmsPayId);
}
