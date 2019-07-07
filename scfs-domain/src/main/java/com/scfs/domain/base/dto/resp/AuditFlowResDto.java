package com.scfs.domain.base.dto.resp;

import java.util.List;
import java.util.Map;

import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.consts.OperateConsts;
import com.scfs.domain.CodeValue;
import com.scfs.domain.base.entity.AuditFlow;
import com.google.common.collect.Maps;

/**
 * Created by Administrator on 2017年7月22日.
 */
public class AuditFlowResDto extends AuditFlow {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7037969658761859741L;

	private String auditFlowTypeName;

	private String isFirstRiskName;

	private String isFirstLawName;

	private String auditFlowNodes;

	/** 操作集合 */
	private List<CodeValue> opertaList;

	public static class Operate {
		public static Map<String, String> operMap = Maps.newHashMap();
		static {
			operMap.put(OperateConsts.DETAIL, BusUrlConsts.DETAIL_BILL_IN_STORE);
			operMap.put(OperateConsts.EDIT, BusUrlConsts.EDIT_BILL_IN_STORE);
			operMap.put(OperateConsts.DELETE, BusUrlConsts.DELETE_BILL_IN_STORE);
		}
	}

	public String getAuditFlowTypeName() {
		return auditFlowTypeName;
	}

	public void setAuditFlowTypeName(String auditFlowTypeName) {
		this.auditFlowTypeName = auditFlowTypeName;
	}

	public List<CodeValue> getOpertaList() {
		return opertaList;
	}

	public void setOpertaList(List<CodeValue> opertaList) {
		this.opertaList = opertaList;
	}

	public String getAuditFlowNodes() {
		return auditFlowNodes;
	}

	public void setAuditFlowNodes(String auditFlowNodes) {
		this.auditFlowNodes = auditFlowNodes;
	}

	public String getIsFirstRiskName() {
		return isFirstRiskName;
	}

	public void setIsFirstRiskName(String isFirstRiskName) {
		this.isFirstRiskName = isFirstRiskName;
	}

	public String getIsFirstLawName() {
		return isFirstLawName;
	}

	public void setIsFirstLawName(String isFirstLawName) {
		this.isFirstLawName = isFirstLawName;
	}

}
