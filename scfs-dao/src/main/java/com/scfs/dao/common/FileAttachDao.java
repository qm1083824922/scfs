package com.scfs.dao.common;

import com.scfs.domain.common.dto.req.FileAttachSearchReqDto;
import com.scfs.domain.common.entity.FileAttach;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileAttachDao {

	int insert(FileAttach fileAttach);

	FileAttach queryById(Integer id);

	int updateById(FileAttach fileAttach);

	List<FileAttach> queryFileAttList(FileAttachSearchReqDto fileAttReqDto, RowBounds rowBounds);

	List<FileAttach> queryFileAtts(FileAttachSearchReqDto fileAttReqDto);

}