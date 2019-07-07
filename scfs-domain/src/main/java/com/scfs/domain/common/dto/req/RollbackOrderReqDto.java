package com.scfs.domain.common.dto.req;

import com.scfs.domain.BaseReqDto;

/**
 * Created by Administrator on 2016/12/24.
 */
public class RollbackOrderReqDto extends BaseReqDto{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String orderType;

    private String orderNo;

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
