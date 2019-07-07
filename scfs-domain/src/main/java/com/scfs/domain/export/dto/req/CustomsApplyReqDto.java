package com.scfs.domain.export.dto.req;

import java.util.List;

import com.scfs.domain.export.entity.CustomsApply;
import com.scfs.domain.export.entity.CustomsApplyLine;

/**
 * Created by Administrator on 2016年12月6日.
 */
public class CustomsApplyReqDto extends CustomsApply{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4099775332261371183L;

	/**
	 * 报关明细
	 */
	private List<CustomsApplyLine> customsApplyLineList;

	public List<CustomsApplyLine> getCustomsApplyLineList() {
		return customsApplyLineList;
	}

	public void setCustomsApplyLineList(List<CustomsApplyLine> customsApplyLineList) {
		this.customsApplyLineList = customsApplyLineList;
	}

}

