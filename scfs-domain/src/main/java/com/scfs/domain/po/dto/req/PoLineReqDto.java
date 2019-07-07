package com.scfs.domain.po.dto.req;

import com.scfs.domain.BaseReqDto;
import com.scfs.domain.po.entity.PurchaseOrderLine;

import java.util.List;

/**
 * Created by Administrator on 2016/10/27.
 */
public class PoLineReqDto extends BaseReqDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 新增采购单行 */
	List<PurchaseOrderLine> poLines;

	public List<PurchaseOrderLine> getPoLines() {
		return poLines;
	}

	public void setPoLines(List<PurchaseOrderLine> poLines) {
		this.poLines = poLines;
	}
}
