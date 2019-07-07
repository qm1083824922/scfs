package com.scfs.domain.export.dto.req;

import com.scfs.domain.BaseReqDto;

/**
 * Created by Administrator on 2016年12月6日.
 */
public class CustomsApplyLineSearchReqDto extends BaseReqDto{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6492642347000248341L;
	
    /**
     * 报关申请ID
     */ 
    private Integer customsApplyId;

	public Integer getCustomsApplyId() {
		return customsApplyId;
	}

	public void setCustomsApplyId(Integer customsApplyId) {
		this.customsApplyId = customsApplyId;
	}
    
    

}

