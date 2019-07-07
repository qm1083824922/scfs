package com.scfs.dao.fi;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.scfs.domain.fi.dto.req.VlReceiptRelSearchReqDto;
import com.scfs.domain.fi.entity.VlReceiptRel;

@Repository
public interface VlReceiptRelDao {

	int deleteById(Integer id);

	int insert(VlReceiptRel record);

	VlReceiptRel queryEntityById(Integer id);

	int updateById(VlReceiptRel record);

	VlReceiptRel queryEntityByReceiptAndLineId(VlReceiptRelSearchReqDto req);

	List<VlReceiptRel> queryRecustsByCon(VlReceiptRelSearchReqDto req);

}