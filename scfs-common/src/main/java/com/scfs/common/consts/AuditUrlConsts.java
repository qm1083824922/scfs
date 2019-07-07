package com.scfs.common.consts;

/**
 * 审核流程URL
 * @author Administrator
 *
 */
public class AuditUrlConsts extends UrlConsts {
	/** ===============采购单审核URL===================== */
	public final static String PO_INFO_AUDIT = "/po/info/audit";// 采购单审核信息查询
	public final static String PO_BUS_AUDIT = "/po/bus/audit";// 业务审核通过
	public final static String PO_FINANCE_AUDIT = "/po/finance/audit";// 财务审核通过
	public final static String PO_RISK_AUDIT = "/po/risk/audit";// 风控审核通过
	public final static String PO_UNPASS_AUDIT = "/po/unpass/audit";// 采购单审核不通过
	public final static String PO_AUDITFLOW_AUDIT_QUERY = "/po/auditflow/audit" + QUERY;// 采购单审核记录
	public final static String PO_DELIVER_AUDIT_QUERY = "/po/deliver/audit";// 采购单审核转交
	public final static String PO_SIGH_AUDIT_QUERY = "/po/sigh/audit";// 采购单审核加签

	/** ===============采购退货单审核URL===================== */
	public final static String PO_RETURN_INFO_AUDIT = "/poReturn/info/audit";// 采购退货单审核信息查询
	public final static String PO_RETURN_FINANCE_AUDIT = "/poReturn/finance/audit";// 采购退货单财务专员审核通过
	public final static String PO_RETURN_FINANCE2_AUDIT = "/poReturn/finance2/audit";// 采购退货单财务主管审核通过
	public final static String PO_RETURN_UNPASS_AUDIT = "/poReturn/unpass/audit";// 采购退货单审核不通过
	public final static String PO_RETURN_AUDITFLOW_AUDIT_QUERY = "/poReturn/auditflow/audit" + QUERY;// 采购退货单审核记录
	public final static String PO_RETURN_DELIVER_AUDIT_QUERY = "/poReturn/deliver/audit";// 采购退货单审核转交
	public final static String PO_RETURN_SIGH_AUDIT_QUERY = "/poReturn/sigh/audit";// 采购退货单审核加签

	/** ===============销售单审核URL===================== */
	public final static String BILL_DELIVERY_INFO_AUDIT = "/billDelivery/info/audit";// 销售单审核信息查询
	public final static String BILL_DELIVERY_BUS_AUDIT = "/billDelivery/bus/audit";// 业务审核通过
	public final static String BILL_DELIVERY_FINANCE_AUDIT = "/billDelivery/finance/audit";// 财务专员审核通过
	public final static String BILL_DELIVERY_FINANCE2_AUDIT = "/billDelivery/finance2/audit";// 财务主管审核通过
	public final static String BILL_DELIVERY_UNPASS_AUDIT = "/billDelivery/unpass/audit";// 销售单审核不通过
	public final static String BILL_DELIVERY_AUDITFLOW_AUDIT_QUERY = "/billDelivery/auditflow/audit" + QUERY;// 销售单审核记录
	public final static String BILL_DELIVERY_DELIVER_AUDIT_QUERY = "/billDelivery/deliver/audit";// 销售单审核转交
	public final static String BILL_DELIVERY_SIGH_AUDIT_QUERY = "/billDelivery/sigh/audit";// 销售单审核加签
	public final static String FI_BANK_RECEIPT_INFO_AUDIT = "/billDelivery/fiBankReceipt/audit" + QUERY;// 销售单水单信息查询
	public final static String FI_BANK_RECEIPTDATE_INFO_AUDIT = "/billDelivery/fiBankReceiptDate/audit" + QUERY;// 销售单水单日期列表
	public final static String BILL_DELIVERY_SERVICE_AMOUNT_QUERY = "/billDelivery/serviceAmount/audit" + QUERY;// 销售单服务费金额查询
	public final static String FI_BANK_RECEIPT_ADVANCE_AUDIT = "/billDelivery/fiBankReceipt/advance/audit" + QUERY;// 水单预收货款明显查询

	public final static String BILL_OUT_STORE_INFO_AUDIT = "/billOutStore/info/audit";// 出库单审核信息查询
	public final static String BILL_OUT_STORE_BUS_AUDIT = "/billOutStore/bus/audit";// 业务审核通过
	public final static String BILL_OUT_STORE_FINANCE_AUDIT = "/billOutStore/finance/audit";// 财务专员审核通过
	public final static String BILL_OUT_STORE_FINANCE2_AUDIT = "/billOutStore/finance2/audit";// 财务主管审核通过
	public final static String BILL_OUT_STORE_UNPASS_AUDIT = "/billOutStore/unpass/audit";// 出库单审核不通过
	public final static String BILL_OUT_STORE_AUDITFLOW_AUDIT_QUERY = "/billOutStore/auditflow/audit" + QUERY;// 出库单审核记录
	public final static String BILL_OUT_STORE_DELIVER_AUDIT_QUERY = "/billOutStore/deliver/audit";// 出库单审核转交
	public final static String BILL_OUT_STORE_SIGH_AUDIT_QUERY = "/billOutStore/sigh/audit";// 出库单审核加签

	/** ===============销售退货单审核URL===================== */
	public final static String BILL_RETURN_INFO_AUDIT = "/billReturn/info/audit";// 销售退货单审核信息查询
	public final static String BILL_RETURN_FINANCE_AUDIT = "/billReturn/finance/audit";// 财务专员审核通过
	public final static String BILL_RETURN_FINANCE2_AUDIT = "/billReturn/finance2/audit";// 财务主管审核通过
	public final static String BILL_RETURN_UNPASS_AUDIT = "/billReturn/unpass/audit";// 销售退货单审核不通过
	public final static String BILL_RETURN_AUDITFLOW_AUDIT_QUERY = "/billReturn/auditflow/audit" + QUERY;// 销售退货单审核记录
	public final static String BILL_RETURN_DELIVER_AUDIT_QUERY = "/billReturn/deliver/audit";// 销售退货单审核转交
	public final static String BILL_RETURN_SIGH_AUDIT_QUERY = "/billReturn/sigh/audit";// 销售退货单审核加签

	/** ===============条款审核URL===================== */
	public final static String PROJECT_ITEM_AUDIT = "/projectItem/info/audit";// 費用单审核信息查询
	public final static String PROJECT_ITEM_PASS_AUDIT = "/projectItem/finance/pass/audit";// 财务专员审核通过
	public final static String PROJECT_ITEM_FINANCE2_AUDIT = "/projectItem/finance2/pass/audit";// 财务主管审核通过
	public final static String PROJECT_ITEM_BUS_PASS_AUDIT = "/projectItem/bus/audit";// 业务审核通过
	public final static String PROJECT_ITEM_RISK_AUDIT = "/projectItem/risk/audit";// 风控审核通过
	public final static String PROJECT_ITEM_DEPT_MANAGE_AUDIT = "/projectItem/deptManage/audit";// 部门主管审核通过
	public final static String PROJECT_ITEM_UNPASS_AUDIT = "/projectItem/unpass/audit";// 条款单审核不通过
	public final static String PROJECT_ITEM_AUDITFLOW_AUDIT_QUERY = "/projectItem/auditflow/audit" + QUERY;// 条款单审核记录
	public final static String PROJECT_ITEM_DELIVER_AUDIT_QUERY = "/projectItem/deliver/audit";// 条款单审核转交
	public final static String PROJECT_ITEM_SIGH_AUDIT_QUERY = "/projectItem/sigh/audit";// 条款单审核加签
	/** ===============临时额度申请审核URL===================== */
	public final static String PROJECT_POOL_ADJUST_AUDIT = "/projectPoolAdjust/info/audit";// 临时额度申请审核信息查询
	public final static String PROJECT_POOL_ADJUST_BUS_PASS_AUDIT = "/projectPoolAdjust/bus/audit";// 业务审核通过
	public final static String PROJECT_POOL_ADJUST_FINANCE2_AUDIT = "/projectPoolAdjust/finance2/pass/audit";// 财务主管审核通过
	public final static String PROJECT_POOL_ADJUST_RISK_AUDIT = "/projectPoolAdjust/risk/audit";// 风控审核通过
	public final static String PROJECT_POOL_ADJUST_DEPT_MANAGE_AUDIT = "/projectPoolAdjust/deptManage/audit";// 部门主管审核通过
	public final static String PROJECT_POOL_ADJUST_UNPASS_AUDIT = "/projectPoolAdjust/unpass/audit";// 临时额度申请单审核不通过
	public final static String PROJECT_POOL_ADJUST_AUDITFLOW_AUDIT_QUERY = "/projectPoolAdjust/auditflow/audit" + QUERY;// 临时额度申请单审核记录
	public final static String PROJECT_POOL_ADJUST_DELIVER_AUDIT_QUERY = "/projectPoolAdjust/deliver/audit";// 临时额度申请单审核转交
	public final static String PROJECT_POOL_ADJUST_SIGH_AUDIT_QUERY = "/projectPoolAdjust/sigh/audit";// 临时额度申请单审核加签
	/** ===============費用单审核URL===================== */
	public final static String FEE_INFO_AUDIT = "/fee/info/audit";// 費用单审核信息查询
	public final static String FEE_FINANCE_PASS_AUDIT = "/fee/finance/pass/audit";// 财务专员审核通过
	public final static String FEE_FINANCE2_PASS_AUDIT = "/fee/finance2/pass/audit";// 财务主管审核通过
	public final static String FEE_UNPASS_AUDIT = "/fee/unpass/audit";// 費用单审核不通过
	public final static String FEE_AUDITFLOW_AUDIT_QUERY = "/fee/auditflow/audit" + QUERY;// 条款单审核记录
	public final static String FEE_DELIVER_AUDIT_QUERY = "/fee/deliver/audit";// 费用单审核转交
	public final static String FEE_SIGH_AUDIT_QUERY = "/fee/sigh/audit";// 费用单审核加签

	/** ===============凭证审核URL===================== */
	public final static String VOUCHER_INFO_AUDIT = "/voucher/info/audit";// 手工录用凭证审核信息查询
	public final static String VOUCHER_FINANCE_PASS_AUDIT = "/voucher/finance/pass/audit";// 手工录用凭证审核通过
	public final static String VOUCHER_UNPASS_AUDIT = "/voucher/unpass/audit";// 手工录用凭证审核不通过
	public final static String VOUCHER_AUDITFLOW_AUDIT_QUERY = "/voucher/auditflow/audit" + QUERY;// 条款单审核记录
	public final static String VOUCHER_DELIVER_AUDIT_QUERY = "/voucher/deliver/audit";// 凭证单审核转交
	public final static String VOUCHER_SIGH_AUDIT_QUERY = "/voucher/sigh/audit";// 凭证单审核加签

	/** ===============付款单审核URL===================== */
	public final static String PAY_ORDER_INFO_AUDIT = "/payOrder/info/audit";// 付款单审核信息查询
	public final static String PAY_ORDER_BUS_AUDIT = "/payOrder/bus/audit";// 业务审核通过
	public final static String PAY_ORDER_RISK_AUDIT = "/payOrder/risk/audit";// 风控主管审核通过
	public final static String PAY_ORDER_RISK_SPECIAL_AUDIT = "/payOrder/risk/special/audit";// 风控专员审核通过
	public final static String PAY_ORDER_FINANCE_AUDIT = "/payOrder/finance/audit";// 财务专员审核通过
	public final static String PAY_ORDER_FINANCE2_AUDIT = "/payOrder/finance2/audit";// 财务主管审核通过
	public final static String PAY_ORDER_DEPT_AUDIT = "/payOrder/dept/audit";// 部门主管审核通过
	public final static String PAY_ORDER_BOSS_AUDIT = "/payOrder/boss/audit";// 总经理审核通过
	public final static String PAY_ORDER_UNPASS_AUDIT = "/payOrder/unpass/audit";// 付款单审核不通过
	public final static String PAY_ORDER_AUDITFLOW_AUDIT_QUERY = "/payOrder/auditflow/audit" + QUERY;// 付款单审核记录
	public final static String PAY_ORDER_DELIVER_AUDIT_QUERY = "/payOrder/deliver/audit";// 付款单审核转交
	public final static String PAY_ORDER_SIGH_AUDIT_QUERY = "/payOrder/sigh/audit";// 付款单审核加签
	public final static String PAY_ORDER_LAW_AUDIT = "/payOrder/law/audit";// 法务主管审核通过
	public final static String PAY_ORDER_RISK_BUS_AUDIT = "/payOrder/risk/bus/audit";// 商务审核

	/** ===============合并付款单审核URL===================== */
	public final static String MERGE_PAY_ORDER_INFO_AUDIT = "/mergePayOrder/info/audit";// 付款单审核信息查询
	public final static String MERGE_PAY_ORDER_BUS_AUDIT = "/mergePayOrder/bus/audit";// 业务审核通过
	public final static String MERGE_PAY_ORDER_RISK_AUDIT = "/mergePayOrder/risk/audit";// 风控主管审核通过
	public final static String MERGE_PAY_ORDER_RISK_SPECIAL_AUDIT = "/mergePayOrder/risk/special/audit";// 风控专员审核通过
	public final static String MERGE_PAY_ORDER_FINANCE_AUDIT = "/mergePayOrder/finance/audit";// 财务专员审核通过
	public final static String MERGE_PAY_ORDER_FINANCE2_AUDIT = "/mergePayOrder/finance2/audit";// 财务主管审核通过
	public final static String MERGE_PAY_ORDER_DEPT_AUDIT = "/mergePayOrder/dept/audit";// 部门主管审核通过
	public final static String MERGE_PAY_ORDER_BOSS_AUDIT = "/mergePayOrder/boss/audit";// 总经理审核通过
	public final static String MERGE_PAY_ORDER_UNPASS_AUDIT = "/mergePayOrder/unpass/audit";// 付款单审核不通过
	public final static String MERGE_PAY_ORDER_AUDITFLOW_AUDIT_QUERY = "/mergePayOrder/auditflow/audit" + QUERY;// 付款单单审核记录
	public final static String MERGE_PAY_ORDER_DELIVER_AUDIT_QUERY = "/mergePayOrder/deliver/audit";// 付款单审核转交
	public final static String MERGE_PAY_ORDER_SIGH_AUDIT_QUERY = "/mergePayOrder/sigh/audit";// 付款单审核加签
	public final static String MERGE_PAY_ORDER_LAW_AUDIT = "/mergePayOrder/law/audit";// 付款单法务主管审核
	public final static String MERGE_PAY_ORDER_RISK_BUS_AUDIT = "/mergePayOrder/risk/bus/audit";// 商务审核

	/** ===============发票审核URL===================== */
	public final static String INVOICE_FINANCE_AUDIT = "/invoice/finance/audit";// 发票信息审核查询
	public final static String INVOICE_FINANCE_PASS_AUDIT = "/invoice/financePass/audit";// 发票财务专员审核通过
	public final static String INVOICE_FINANCE_PASS2_AUDIT = "/invoice/financePass2/audit";// 发票财务主管审核通过
	public final static String INVOICE_FINANCE_UNPASS_AUDIT = "/invoice/financeunPass/audit";// 发票审核不通过
	public final static String INVOICE_DELIVER_AUDIT_QUERY = "/invoice/deliver/audit";// 审核单转交
	public final static String INVOICE_SIGN_AUDIT_QUERY = "/invoice/sign/audit";// 审核单加签
	public final static String INVOICE_AUDITFLOW_AUDIT_QUERY = "/invoice/auditflow/audit" + QUERY;// 审核单加签

	/** ===============收票审核URL===================== */
	public final static String PAY_COLLECT_INFO_AUDIT = "/collect/info/audit";// 收票审核信息查询
	public final static String PAY_COLLECT_FINANCE_AUDIT = "/collect/finance/audit";// 财务审核通过
	public final static String PAY_COLLECT_FINANCE2_AUDIT = "/collect/finance2/audit";// 财务审核通过
	public final static String PAY_COLLECT_UNPASS_AUDIT = "/collect/unpass/audit";// 财务审核不通过
	public final static String PAY_COLLECT_AUDITFLOW_AUDIT_QUERY = "/collect/auditflow/audit" + QUERY;// 采购单单审核记录
	public final static String PAY_COLLECT_DELIVER_AUDIT_QUERY = "/collect/deliver/audit";// 采购单审核转交
	public final static String PAY_COLLECT_SIGH_AUDIT_QUERY = "/collect/sigh/audit";// 采购单审核加签

	/** ===============退税审核URL===================== */
	public final static String REFUND_APPLY_INFO_AUDIT = "/refund/info/audit";// 退税审核信息查询
	public final static String REFUND_APPLY_FINANCE_AUDIT = "/refund/finance/audit";// 财务审核通过
	public final static String REFUND_APPLY_UNPASS_AUDIT = "/refund/unpass/audit";// 财务审核不通过
	public final static String REFUND_APPLY_AUDITFLOW_AUDIT_QUERY = "/refund/auditflow/audit" + QUERY;// 采购单单审核记录
	public final static String REFUND_APPLY_DELIVER_AUDIT_QUERY = "/refund/deliver/audit";// 采购单审核转交
	public final static String REFUND_APPLY_SIGH_AUDIT_QUERY = "/refund/sigh/audit";// 采购单审核加签

	/** ===============境外开票审核URL===================== */
	public final static String PAY_OVERSEAS_INFO_AUDIT = "/overseas/info/audit";// 境外开票审核信息查询
	public final static String PAY_OVERSEAS_FINANCE_AUDIT = "/overseas/finance/audit";// 境外开票财务专业审核通过
	public final static String PAY_OVERSEAS_FINANCE2_AUDIT = "/overseas/finance2/audit";// 境外开票财务主管审核通过
	public final static String PAY_OVERSEAS_UNPASS_AUDIT = "/overseas/unpass/audit";// 境外开票审核不通过
	public final static String PAY_OVERSEAS_AUDITFLOW_AUDIT_QUERY = "/overseas/auditflow/audit" + QUERY;// 境外开票审核记录
	public final static String PAY_OVERSEAS_DELIVER_AUDIT_QUERY = "/overseas/deliver/audit";// 境外开票转交
	public final static String PAY_OVERSEAS_SIGH_AUDIT_QUERY = "/overseas/sigh/audit";// 境外开票加签

	/** ===============铺货商品审核URL===================== */
	public final static String DISTRIBUTE_GOODS_INFO_AUDIT = "/distributeGoods/info/audit";// 铺货商品审核信息查询
	public final static String DISTRIBUTE_GOODS_CAREER_AUDIT = "/distributeGoods/career/audit";// 铺货商品事业部审核通过
	public final static String DISTRIBUTE_GOODS_PURCHASE_AUDIT = "/distributeGoods/purchase/audit";// 铺货商品采购审核通过
	public final static String DISTRIBUTE_GOODS_GROUP_AUDIT = "/distributeGoods/group/audit";// 铺货商品供应链小组审核通过
	public final static String DISTRIBUTE_GOODS_SERVICE_AUDIT = "/distributeGoods/service/audit";// 铺货商品供应链服务部审核通过
	public final static String DISTRIBUTE_GOODS_RISK_AUDIT = "/distributeGoods/risk/audit";// 铺货商品风控审核通过
	public final static String DISTRIBUTE_GOODS_UNPASS_AUDIT = "/distributeGoods/unpass/audit";// 铺货商品审核不通过
	public final static String DISTRIBUTE_GOODS_AUDITFLOW_AUDIT_QUERY = "/distributeGoods/auditflow/audit" + QUERY;// 铺货商品审核记录
	public final static String DISTRIBUTE_GOODS_DELIVER_AUDIT_QUERY = "/distributeGoods/deliver/audit";// 铺货商品审核转交
	public final static String DISTRIBUTE_GOODS_SIGH_AUDIT_QUERY = "/distributeGoods/sigh/audit";// 铺货商品审核加签

	/** ===============铺货退货单审核URL===================== */
	public final static String DISTRIBUTION_RETURN_INFO_AUDIT = "/distributionReturn/info/audit";// 铺货退货单审核信息查询
	public final static String DISTRIBUTION_RETURN_INFO_BIZ_AUDIT = "/distributionReturn/info/bizAudit";// 铺货退货单审核信息查询
	public final static String DISTRIBUTION_RETURN_BIZ_AUDIT = "/distributionReturn/biz/audit";// 铺货退货单商务审核通过
	public final static String DISTRIBUTION_RETURN_FINANCE_AUDIT = "/distributionReturn/finance/audit";// 铺货退货单财务专员审核通过
	public final static String DISTRIBUTION_RETURN_FINANCE2_AUDIT = "/distributionReturn/finance2/audit";// 铺货退货单财务主管审核通过
	public final static String DISTRIBUTION_RETURN_UNPASS_AUDIT = "/distributionReturn/unpass/audit";// 铺货退货单审核不通过
	public final static String DISTRIBUTION_RETURN_AUDITFLOW_AUDIT_QUERY = "/distributionReturn/auditflow/audit"
			+ QUERY;// 铺货退货单审核记录
	public final static String DISTRIBUTION_RETURN_DELIVER_AUDIT_QUERY = "/distributionReturn/deliver/audit";// 铺货退货单审核转交
	public final static String DISTRIBUTION_RETURN_SIGH_AUDIT_QUERY = "/distributionReturn/sigh/audit";// 铺货退货单审核加签

	/** ===============业务目标审核URL===================== */
	public final static String PROFIT_TARGET_AUDIT = "/profitTarget/info/audit";// 业务指标审核信息查询
	public final static String PROFIT_TARGET_BUSI_AUDIT = "/profitTarget/bus/audit";// 业务指标财务专业审核通过
	public final static String PROFIT_TARGET_DEPT_MANAGE_AUDIT = "/profitTarget/deptManage/audit";// 部门主管审核通过
	public final static String PROFIT_TARGET_UNPASS_AUDIT = "/profitTarget/unpass/audit";// 业务指标审核不通过
	public final static String PROFIT_TARGET_AUDITFLOW_AUDIT_QUERY = "/profitTarget/auditflow/audit" + QUERY;// 业务指标审核记录
	public final static String PROFIT_TARGET_DELIVER_AUDIT_QUERY = "/profitTarget/deliver/audit";// 业务指标转交
	public final static String PROFIT_TARGET_SIGH_AUDIT_QUERY = "/profitTarget/sigh/audit";// 业务指标加签
	
	/**=================Invoice收票审核URL===============**/
	public final static String INVOICE_COLLECT_INFO_AUDIT = "/invoiceCollect/info/audit";// iNVOICE收票审核信息查询
	public final static String INVOICE_COLLECT_FINANCE_AUDIT = "/invoiceCollect/finance/audit";// 财务审核通过
	public final static String INVOICE_COLLECT_UNPASS_AUDIT = "/invoiceCollect/unpass/audit";// 财务专员审核不通过
	public final static String INVOICE_COLLECT_DELIVER_AUDIT_QUERY = "/invoiceCollect/deliver/audit";// invoice收票单审核转交
	public final static String INVOICE_COLLECT_SIGH_AUDIT_QUERY = "/invoiceCollect/sigh/audit";//Invoice收票单审核加签
	public final static String INVOICE_COLLECT_AUDITFLOW_AUDIT_QUERY = "/invoiceCollect/auditflow/audit" + QUERY;// invoice收票单审核记录
	
	/** ===============事项管理审核URL===================== */
	public final static String MATTER_MANAGE_AUDIT = "/matterManage/info/audit";// 事项管理审核信息查询
	public final static String MATTER_MANAGE_BUSI_AUDIT = "/matterManage/busi/audit";// 事项管理业务审核通过
	public final static String MATTER_MANAGE_BIZ_AUDIT = "/matterManage/biz/audit";// 事项管理商务审核通过
	public final static String MATTER_MANAGE_DEPT_MANAGE_AUDIT = "/matterManage/deptManage/audit";// 部门主管审核通过
	public final static String MATTER_MANAGE_JUSTICE_AUDIT = "/matterManage/justice/audit";// 事项管理法务审核通过
	public final static String MATTER_MANAGE_FINANCE_AUDIT = "/matterManage/finance/audit";// 事项管理财务主管审核通过
	public final static String MATTER_MANAGE_RISK_AUDIT = "/matterManage/risk/audit";// 事项管理分控审核通过
	public final static String MATTER_MANAGE_BOSS_AUDIT = "/matterManage/boss/audit";// 事项管理总经理审核通过
	public final static String MATTER_MANAGE_UNPASS_AUDIT = "/matterManage/unpass/audit";// 事项管理审核不通过
	public final static String MATTER_MANAGE_AUDITFLOW_AUDIT_QUERY = "/matterManage/auditflow/audit" + QUERY;// 事项管理审核记录
	public final static String MATTER_MANAGE_DELIVER_AUDIT_QUERY = "/matterManage/deliver/audit";// 事项管理转交
	public final static String MATTER_MANAGE_SIGH_AUDIT_QUERY = "/matterManage/sigh/audit";// 事项管理加签
}
