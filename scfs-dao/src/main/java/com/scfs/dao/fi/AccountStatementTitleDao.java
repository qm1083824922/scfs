package com.scfs.dao.fi;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.scfs.domain.fi.dto.req.AccountStatementSearchReqDto;
import com.scfs.domain.fi.entity.AccountStatementTitle;

@Repository
public interface AccountStatementTitleDao {

	int deleteById(Integer id);

	int insert(AccountStatementTitle record);

	AccountStatementTitle queryEntityById(Integer id);

	int updateById(AccountStatementTitle record);

	List<AccountStatementTitle> queryResultsByCon(AccountStatementSearchReqDto req, RowBounds rowBounds);

}