package com.scfs.dao.fi;

import com.scfs.domain.fi.dto.req.AccountLineSearchReqDto;
import com.scfs.domain.fi.entity.AccountLine;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AccountLineDao {
	int insert(AccountLine accountLine);

	int updateById(AccountLine accountLine);

	int deleteById(AccountLine accountLine);

	int submitById(@Param(value = "id") int id, @Param(value = "state") int state);

	List<AccountLine> queryResultsByCond(AccountLineSearchReqDto reqDto, RowBounds rowBounds);

	List<AccountLine> queryResultsByCond(AccountLineSearchReqDto reqDto);

	List<AccountLine> queryAllAccountLine(@Param("updateAt") String updateAt);

	/**
	 * 查询最后更新时间
	 * 
	 * @return
	 */
	Date queryLastUpdateAt();

	AccountLine queryEntityById(@Param(value = "id") int id);

	AccountLine queryAccountLineByNoAndName(@Param("accountLineNo") String accountLineNo,
			@Param("accountLineName") String accountLineName);

}
