package com.scfs.domain.audit.model;

import java.util.List;

import com.scfs.domain.base.dto.resp.MatterManageFileResDto;
import com.scfs.domain.base.dto.resp.MatterManageResDto;
import com.scfs.domain.base.dto.resp.MatterServiceResDto;

/**
 * <pre>
 * 	
 *  File: MatterManageAuditService.java
 *  Description:事项管理审核业务
 *  TODO
 *  Date,					Who,				
 *  2017年08月05日				Administrator
 *
 * </pre>
 */
public class MatterManageAuditInfo {
	/** 事项管理详情 **/
	private MatterManageResDto matterManage;
	/** 服务要求 **/
	private MatterServiceResDto matterService;
	/** 事项管理附件信息 **/
	List<MatterManageFileResDto> matterManageFileList;

	public MatterManageResDto getMatterManage() {
		return matterManage;
	}

	public void setMatterManage(MatterManageResDto matterManage) {
		this.matterManage = matterManage;
	}

	public MatterServiceResDto getMatterService() {
		return matterService;
	}

	public void setMatterService(MatterServiceResDto matterService) {
		this.matterService = matterService;
	}

	public List<MatterManageFileResDto> getMatterManageFileList() {
		return matterManageFileList;
	}

	public void setMatterManageFileList(List<MatterManageFileResDto> matterManageFileList) {
		this.matterManageFileList = matterManageFileList;
	}

}
