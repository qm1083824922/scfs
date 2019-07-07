package com.scfs.dao.common;

import com.scfs.domain.common.entity.MsgContent;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MsgContentDao {
	int insert(MsgContent msgContent);

	MsgContent queryMsgContentById(Integer id);

	List<MsgContent> querySendMsgList();

	int updateById(MsgContent msgContent);
}