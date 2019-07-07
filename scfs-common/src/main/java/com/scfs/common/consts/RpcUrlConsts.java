package com.scfs.common.consts;

/**
 * 外部系统接口URL
 * @author Administrator
 *
 */
public class RpcUrlConsts {
	/************************* PMS接口 *****************************/
	// 同步请款单(应收保理)
	public static final String API_PMS_SYNC_PAY_ORDER = "/api/pms/syncPayOrder";
	// PMS付款确认(应收保理)
	public static final String API_PMS_PAY_ORDER_CONFIRM = "/api/pms/syncPayOrder/confirm";
	// 同步入库单明细(铺货)
	public static final String API_PMS_STORE_IN = "/api/pms/storeIn";
	// 同步出库单明细(铺货)
	public static final String API_PMS_STORE_OUT = "/api/pms/storeOut";
	// 同步请款单付款确认(铺货)
	public static final String API_PMS_PAY_PURCHASE_CONFIRM = "/api/pms/payPurchaseConfirm";
	// 同步请款单(代付款)
	public static final String API_PMS_SYNC_WAIT_PAY_ORDER = "/api/pms/syncWaitPayOrder";
	// PMS退货单申请
	public static final String API_PMS_RETURN = "/api/pms/return";
	//PMS 退货订单完成发送接口
	public static final String API_PMS_PURCHASE_SEND = "/api/pms/purchase/send";
	
	/************************* CMS接口 *****************************/
	//CMS付款确认接口
	public static final String API_CMS_PAY_ORDER_CONFIRM = "/api/cms/payOrder/confirm";
}
