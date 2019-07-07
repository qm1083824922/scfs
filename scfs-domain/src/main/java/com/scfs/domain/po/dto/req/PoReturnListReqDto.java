package com.scfs.domain.po.dto.req;

import java.io.Serializable;
import java.util.List;

import com.scfs.domain.po.entity.PurchaseReturnDtl;

public class PoReturnListReqDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4903047092001400211L;
	/** 采购单编号 */
	private Integer poId;
	private List<PurchaseReturnDtl> purchaseReturnDtl;

	public Integer getPoId() {
		return poId;
	}

	public void setPoId(Integer poId) {
		this.poId = poId;
	}

	public List<PurchaseReturnDtl> getPurchaseReturnDtl() {
		return purchaseReturnDtl;
	}

	public void setPurchaseReturnDtl(List<PurchaseReturnDtl> purchaseReturnDtl) {
		this.purchaseReturnDtl = purchaseReturnDtl;
	}

}
