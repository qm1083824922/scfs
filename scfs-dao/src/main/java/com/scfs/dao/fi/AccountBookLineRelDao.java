package com.scfs.dao.fi;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.scfs.domain.fi.dto.req.AcctBookLineRelSearchReqDto;
import com.scfs.domain.fi.entity.AccountBookLineRel;

@Repository
public interface AccountBookLineRelDao {
	int insert(AccountBookLineRel accountBookLineRel);

	int deleteById(AccountBookLineRel accountBookLineRel);

	List<AccountBookLineRel> queryResultsByBookId(AcctBookLineRelSearchReqDto reqDto, RowBounds rowBounds);

	List<AccountBookLineRel> queryResultsByBookId(AcctBookLineRelSearchReqDto reqDt0);

	int queryCountByCon(AcctBookLineRelSearchReqDto reqDto);

	AccountBookLineRel queryEntityById(Integer id);

}
