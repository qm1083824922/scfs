package com.scfs.dao.fi;

import com.scfs.domain.fi.dto.req.AccountBookSearchReqDto;
import com.scfs.domain.fi.entity.AccountBook;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AccountBookDao {
	int insert(AccountBook accountBook);

	int updateById(AccountBook accountBook);

	int deleteById(AccountBook accountBook);

	int submitById(AccountBook accountBook);

	AccountBook queryEntityById(@Param(value = "id") int id);

	List<AccountBook> queryEntityByBusiUnit(@Param(value = "busiUnit") int busiUnit);

	List<AccountBook> queryResultsByCond(AccountBookSearchReqDto req, RowBounds rowBounds);

	List<AccountBook> queryResultsByCond(AccountBookSearchReqDto req);

	List<AccountBook> queryAllAccountBook(@Param("updateAt") String updateAt);

	/**
	 * 查询最后更新时间
	 * 
	 * @return
	 */
	Date queryLastUpdateAt();

}
