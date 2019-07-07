package com.scfs.dao.fi;

import java.util.List;
import org.apache.ibatis.session.RowBounds;
import com.scfs.domain.fi.dto.req.ReceiptOutRelReqDto;
import com.scfs.domain.fi.entity.ReceiptOutStoreRel;

public interface ReceiptOutStoreRelDao {
	int deleteByPrimaryKey(Integer id);

	int insert(ReceiptOutStoreRel record);

	int insertSelective(ReceiptOutStoreRel record);

	ReceiptOutStoreRel selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(ReceiptOutStoreRel record);

	int updateByPrimaryKey(ReceiptOutStoreRel record);

	/***
	 * 分页查询水单关联的出库数据
	 * 
	 * @param relReqDto
	 * @param rowBounds
	 * @return
	 */
	List<ReceiptOutStoreRel> queryResultsByCon(ReceiptOutRelReqDto relReqDto, RowBounds rowBounds);

	/**
	 * 业务删除关联数据
	 * 
	 * @param id
	 * @return
	 */
	int updateRecOutByCon(Integer id);

	List<ReceiptOutStoreRel> queryResultsByCon(ReceiptOutRelReqDto relReqDto);

}