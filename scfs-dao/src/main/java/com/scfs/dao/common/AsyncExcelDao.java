package com.scfs.dao.common;

import com.scfs.domain.common.dto.req.AsyncExcelReqDto;
import com.scfs.domain.common.entity.AsyncExcel;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AsyncExcelDao {

	int insert(AsyncExcel asyncExcel);

	AsyncExcel queryAsyncExcelById(Integer id);

	List<AsyncExcel> queryAsyncExcelLimit10();

	List<AsyncExcel> queryAsyncExcelByCon(AsyncExcelReqDto asyncExcelReqDto, RowBounds rowBounds);

	int updateById(AsyncExcel asyncExcel);
}