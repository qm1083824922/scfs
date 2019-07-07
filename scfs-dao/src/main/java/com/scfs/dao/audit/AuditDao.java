package com.scfs.dao.audit;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.scfs.domain.audit.dto.req.AuditReqDto;
import com.scfs.domain.audit.entity.Audit;

@Repository
public interface AuditDao {

	int insert(Audit auditor);

	Audit queryAuditById(Integer id);

	int updateAuditById(Audit auditor);

	List<Audit> queryAuditResultsByCon(AuditReqDto auditReqDto, RowBounds rowBounds);

	List<Audit> queryAuditWechatResultsByCon(AuditReqDto auditReqDto, RowBounds rowBounds);

	List<Audit> queryAuditFlows(AuditReqDto auditReqDto);

	List<Audit> queryAuditSighs(Integer pauditId);

	Audit queryAuditResultsNext(AuditReqDto auditReqDto);
}