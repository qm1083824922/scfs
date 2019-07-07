package com.scfs.dao.base.entity;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.scfs.domain.base.dto.req.AuditFlowReqDto;
import com.scfs.domain.base.entity.AuditFlow;

public interface AuditFlowDao {
	int insert(AuditFlow auditFlow);

	AuditFlow queryEntityById(Integer id);

	AuditFlow queryLockEntityById(Integer id);

	int updateById(AuditFlow auditFlow);

	List<AuditFlow> queryResultsByCon(AuditFlowReqDto auditFlowReqDto, RowBounds rowBounds);

	AuditFlow queryAuditFlowByTypeAndNo(@Param("auditFlowType") Integer auditFlowType,
			@Param("auditFlowNo") String auditFlowNo);

	AuditFlow queryAuditFlowByNo(@Param("auditFlowNo") String auditFlowNo);

	AuditFlow queryAuditFlowByType(Integer auditFlowType);

	List<AuditFlow> queryAuditFlow4Pay();

	int queryCountByType(@Param("auditFlowType") Integer auditFlowType);
}