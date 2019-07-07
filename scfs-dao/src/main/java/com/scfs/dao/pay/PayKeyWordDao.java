package com.scfs.dao.pay;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.scfs.domain.pay.dto.req.PayKeyWordReqDto;
import com.scfs.domain.pay.entity.PayKeyWord;

@Repository
public interface PayKeyWordDao {
	/**
	 * 添加数据
	 * 
	 * @param payKeyWord
	 * @return
	 */
	int insert(PayKeyWord payKeyWord);

	/**
	 * 查询详情
	 * 
	 * @param id
	 * @return
	 */
	PayKeyWord queryEntityById(Integer id);

	/***
	 * 修改
	 * 
	 * @param payKeyWord
	 * @return
	 */
	int updateById(PayKeyWord payKeyWord);

	/**
	 * 获取数据分页
	 * 
	 * @return
	 */
	List<PayKeyWord> queryResultsByCon(PayKeyWordReqDto payKeyWord, RowBounds rowBounds);
}
