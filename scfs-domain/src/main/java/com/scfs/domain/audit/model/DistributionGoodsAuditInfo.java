package com.scfs.domain.audit.model;

import java.util.List;

import com.scfs.domain.base.dto.resp.DistributionGoodsResDto;
import com.scfs.domain.po.dto.resp.PoFileAttachRespDto;

/**
 * <pre>
 * 
 *  File: DistributionGoodsAuditInfo.java
 *  Description:铺货商品审核审核信息
 *  TODO
 *  Date,					Who,				
 *  2017年07月24日			Administrator
 *
 * </pre>
 */
public class DistributionGoodsAuditInfo {
	DistributionGoodsResDto distributionGoods;
	/** 收票附件信息 **/
	List<PoFileAttachRespDto> poFileAttachList;

	public DistributionGoodsResDto getDistributionGoods() {
		return distributionGoods;
	}

	public void setDistributionGoods(DistributionGoodsResDto distributionGoods) {
		this.distributionGoods = distributionGoods;
	}

	public List<PoFileAttachRespDto> getPoFileAttachList() {
		return poFileAttachList;
	}

	public void setPoFileAttachList(List<PoFileAttachRespDto> poFileAttachList) {
		this.poFileAttachList = poFileAttachList;
	}

}
