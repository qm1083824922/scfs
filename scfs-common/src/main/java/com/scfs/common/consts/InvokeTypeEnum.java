package com.scfs.common.consts;

/**
 * 接口枚举
 * @author Administrator
 *
 */
public enum InvokeTypeEnum {
	
	CMS_PAY_ORDER(7001, "[cms]上传付款信息接口"),
	CMS_PAY_ORDER_CONFIRM(7002, "[cms]付款确认接口"),
	WMS_STOCK_RECEIVE(8001, "[wms]入库单接口"),
    PMS_PAY_ORDER(9001, "[pms]请款单接口"),
	PMS_SUPPLIER(9002, "[pms]校验供应商接口"),
	PMS_SUPPLIER_UPLOAD(9003, "[pms]上传供应商信息接口"),
	PMS_PAY_ORDER_UPLOAD(9004, "[pms]上传付款信息接口"),
	PMS_PAY_STORE_IN(9005, "[pms]铺货入库单明细接口"),
	PMS_STORE_OUT(9006, "[pms]铺货出库单明细接口"),
	PMS_PAY_PURCHASE(9007, "[pms]铺货请款单接口"),
	PMS_PAY_PURCHASE_CONFIRM(9008, "[pms]铺货付款确认接口"),
	PMS_PAY_ORDER_CONFIRM(9009, "[pms]付款确认(应收保理)接口"),
	PMS_PAY_RETURN(9010, "[pms]退货单申请(融通质押)接口"),
	PMS_PURCHASE_ORDER_CONFIRM(9011, "[pms]退货订单确认回传(融通质押)接口"),
	PMS_PURCHASE_ORDER_SEND(9012, "[pms]退货订单完成发送(融通质押)接口");
    private Integer type;
    private String name;
    
    private InvokeTypeEnum(final Integer type, final String name) {
        this.type = type;
        this.name = name;
    }

    public String getNameByType(Integer type){
    	InvokeTypeEnum[] invokeTypeEnumList = InvokeTypeEnum.values();
        for (InvokeTypeEnum invokeTypeEnum : invokeTypeEnumList) {
            if(type.equals(invokeTypeEnum.type)){
                return invokeTypeEnum.name;
            }
        }
        return null;
    }

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


}

