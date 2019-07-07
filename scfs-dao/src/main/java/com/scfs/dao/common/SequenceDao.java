package com.scfs.dao.common;

import com.scfs.domain.common.entity.SeqManage;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SequenceDao {
	SeqManage getSeqByName(@Param(value = "seqName") String seqName);

	int updateSeqValByName(SeqManage seqManage);
}
