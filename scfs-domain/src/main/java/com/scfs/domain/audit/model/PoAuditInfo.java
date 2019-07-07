package com.scfs.domain.audit.model;

import com.scfs.domain.po.dto.resp.PoTitleRespDto;
import com.scfs.domain.po.model.PoLineModel;

import java.util.List;

/**
 * Created by Administrator on 2016/10/18. PO审核单信息
 */
public class PoAuditInfo {

	/** 审核单头信息 */
	private PoTitleRespDto poTitleRespDto;
	/** 审核单行列表信息 */
	private List<PoLineModel> poLineDetailList;

	public PoTitleRespDto getPoTitleRespDto() {
		return poTitleRespDto;
	}

	public void setPoTitleRespDto(PoTitleRespDto poTitleRespDto) {
		this.poTitleRespDto = poTitleRespDto;
	}

	public List<PoLineModel> getPoLineDetailList() {
		return poLineDetailList;
	}

	public void setPoLineDetailList(List<PoLineModel> poLineDetailList) {
		this.poLineDetailList = poLineDetailList;
	}
}
