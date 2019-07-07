package com.scfs.common.consts;

/**
 * 异常列表
 * @author Administrator
 *
 */
public enum ExcMsgEnum {

    RESOLVER_EXCEL_ERROR("000001","解析EXCEL异常,第{}单元格数据有误"),
    ENTITY_NOT_EXSIT("000002","[{}]操作ID主键[{}]的实体不存在"),
    ENTITY_UPDATE_NOT_EXSIT("000003","更新实体不存在,id为{}"),
    IMPORT_EXCEL_ROWS_MAX("000004","导入条数不能超过999"),
    /**发票单**/
    INVOICE_ADD_EXCEPTION("6100001","新增发票异常"),
    INVOICE_UPDATE_EXCEPTION("6100003","更新发票异常"),
    INVOICE_SUBMIT_EXCEPTION("6100003","提交发票异常"),
    INVOICE_EXIST_EXCEPTION("6100002","该发票名称已存在"),
    INVOICE_LOCK_EXCEPTION("6100004","发票加锁失败 "),
    INVOICE_UNLOCK_EXCEPTION("6100005","发票解锁失败 "),
    INVOICE_DELETE_EXCEPTION("6100006","发票删除失败 "),
    INVOICE_SIMULATE_ADD_EXCEPTION("6100007","模拟插入发票失败 "),
    INVOICE_SIMULATE_SUBMIT_EXCEPTION("6100008","模拟发票提交失败 "),
    INVOICE_SIMULATE_ENSURE_EXCEPTION("6100009","发票信息确认失败 "),
    INVOICE_NUM__MAX_EXCEPTION("6100009","您所填写的开票数量超过最大数量"),
    INVOICE_AMOUNT_MAX_EXCEPTION("6100010","您所填写的开票金额超过最大额度"),
    INVOICE_DETAIL_EXCEPTION("6100011","发票明细为空,无法进行模拟"),
    /**商品单**/
    GOODS_ADD_EXCEPTION("1200001","新增商品异常"),
    GOODS_UPDATE_EXCEPTION("1200003","更新商品异常"),
    GOODS_EXIST_EXCEPTION("1200002","该商品名称已存在"),
    GOODS_LOCK_EXCEPTION("1200004","商品加锁失败 "),
    GOODS_UNLOCK_EXCEPTION("1200005","商品解锁失败 "),
    GOODS_DELETE_EXCEPTION("1200006","商品删除失败 "),
    GOODS_NUMBER_EXIST_EXCEPTION("1200007","该商品编号已存在"),
    /**用户角色**/
    USERSROLE_IDNOTEXIST_EXCEPTION("1300001","用户角色Id为空"), 
    USERSROLE_EXIST_EXCEPTION("13000012","您所选择的角色用户已拥有"),
    USERSROLE_EXISTINVALID_EXCEPTION("1300002","您所选择的角色中存在已作废的"), 
    USERSPROJECT_EXISTINVALID_EXCEPTION("13000013","您所选择的项目中存在已作废的"), 
    USERSPROJECT_DIVIDE_EXCEPTION("13000014","您所选择的用户存在已选择的项目"), 
    USERSDELETE_EXCEPTION("1300003","删除用户异常"), 
    USERSUNLOCK_EXCEPTION("1300004","删除解锁异常"), 
    USERSLOCK_EXCEPTION("1300005","删除加锁异常"), 
    USERSRESET_EXCEPTION("1300011","重置加锁异常"), 
    USERSSUBMIT_EXCEPTION("1300006","用户提交异常"), 
    PERMISSIONGROUP_EXCEPTION("1300007","您所选择的角色中存在已分配的"), 
    PERMISSIONGROUPD_EXCEPTION("1300008","您所选择的权限组中存在已分配的"), 
    PERMISSIONGROUPD_ROLE_EXCEPTION("1300009","您所选择的角色中存在已分配的权限组"), 
    PERMISSIONGROUPD_INVALID_EXCEPTION("1300010","您所选择的权限组中存在已作废数据"), 
    PERMISSIONADD_EXCEPTION("1300014","添加权限错误"), 
    ROLEADD_EXCEPTION("1300015","添加角色错误"),
    ERROR_GENERAL("000000","{}"),
    PROJECT_ITEM_EXCEPTION("1400001","条款新增异常"), 
    PROJECT_ITEM_UPDATE_EXCEPTION("1400001","条款更新异常"), 
    PROJECT_ITEM_SUBMIT_EXCEPTION("1400001","条款提交异常"), 
    PROJECT_ITEM_DELETE_EXCEPTION("1400001","条款删除异常"), 
    PROJECT_ITEM_LOCK_EXCEPTION("1400001","条款锁定异常"), 
    PROJECT_ITEM_UNLOCK_EXCEPTION("1400001","条款解锁异常"), 
    /***采购单*/
    PO_NOT_SUBMIT("600001","采购单已经提交，不能重复提交"),
    PO_NOT_DELETE("600002","采购单已经提交，不能删除"),
    PO_NOT_UPDATE("600003","采购单已经提交，不能更新"),
    PO_NOT_ADD_LINE("600004","采购单已经提交，不能添加订单明细"),
    PO_NOT_DELETE_LINE("600005","采购单已经提交，不能删除订单明细"),
    PO_NOT_UPDATE_LINE("600006","采购单已经提交，不能更新订单明细"),
    PO_ADD_EXCEPTION("600007","新增采购单异常"),
    PO_UPDATE_EXCEPTION("600008","更新采购单异常,id为{}"),
    PO_RETURN_UPDATE_EXCEPTION("600009","更新采购退货单异常,id为{}"),
    PO_RETURN_UPDATE_NUM_EXCEPTION("600010","采购退货数量不能大于理货数量,id为{}"),

    /********销售********/
	STL_NOT_ENOUGH("700001", "当前库存不足"),
	BILL_DELIVERY_DTL_NOT_ADD("700002", "销售单明细未添加"),
	BILL_DELIVERY_DTL_NUM_NOT_EQUAL("700003", "销售单头跟明细应发总数量不相等"),
    /********物流********/
	BILL_IN_STORE_DTL_NOT_ADD("810001", "入库单明细未添加"),
	BILL_IN_STORE_TALLY_DTL_NOT_ADD("810002", "入库单理货明细未添加"),
	RECEIVE_NUM_NOT_EQUAL_TALLY_NUM("810003", "入库单收货数量与理货数量不相等"),
	TOTAL_RECEIVE_NUM_NOT_EQUAL("810004", "入库单头跟明细收货总数量不相等"),
	TOTAL_TALLY_NUM_NOT_EQUAL("810005", "入库单头跟明细理货总数量不相等"),
	AVAILABLE_TALLY_NUM_NOT_ENOUGH("810006", "可理货数量不足"),
	BILL_IN_STORE_ADD_ERROR("810007", "新增入库单失败"),
	BILL_IN_STORE_UPDATE_ERROR("810008", "更新入库单失败，请检查单据状态"),
	RECEIVE_NUM_EXCEED("810009", "收货数量超出采购订单可入库数量，请检查收货明细"),
	BILL_IN_STORE_CURRENCY_DIFF_ERROR("810010", "所选采购订单币种不相同，请重新选择"),
	BILL_IN_STORE_CURRENCY_TYPE_DIFF_ERROR("810011", "所选采购订单币种与入库单币种不相同，请重新选择"),
	BILL_IN_STORE_SUBMIT_ERROR("810012", "提交失败，请检查单据状态"),
	BILL_IN_STORE_DELETE_ERROR("810013", "删除失败，请检查单据状态"),
	BILL_IN_STORE_ADD_STATUS_ERROR("810014", "新增收货明细失败，请检查单据状态"),
	BILL_IN_STORE_UPDATE_STATUS_ERROR("810015", "更新收货明细失败，请检查单据状态"),
	BILL_IN_STORE_DELETE_STATUS_ERROR("810016", "删除收货明细失败，请检查单据状态"),
	WAREHOUSE_NOT_EXIST("810017", "仓库不存在"),
	SUPPLIER_NOT_EXIST("810018", "供应商不存在"),
	GOODS_NOT_EXIST("810019", "商品不存在"),
	PO_NOT_EXIST("810020", "采购单不存在"),
	CURRENCY_NOT_EXIST("810021", "币种不存在"),
	BILL_IN_STORE_TALLY_ADD_STATUS_ERROR("810022", "新增理货明细失败，请检查单据状态"),
	BILL_IN_STORE_TALLY_DELETE_STATUS_ERROR("810023", "删除理货明细失败，请检查单据状态"),
	BILL_IN_STORE_AUTO_TALLY_STATUS_ERROR("810024", "整单自动理货失败，请检查单据状态"),
	BILL_IN_STORE_WMS_STATUS_ERROR("810025", "调用wms接口失败，单据已收货"),
	BILL_IN_STORE_WMS_HAS_INVOKE("810026", "已调用wms接口"),
	BILL_IN_STORE_CURRENCY_TYPE_NOT_EXIST("810027", "采购订单币种不能为空"),
	BILL_OUT_STORE_DTL_NOT_ADD("820001", "出库单明细未添加"),
	BILL_OUT_STORE_TALLY_DTL_NOT_ADD("820002", "出库单拣货明细未添加"),
	SEND_NUM_NOT_EQUAL_PICK_NUM("820003", "出库单发货总数量与拣货总数量不相等"),
	TOTAL_SEND_NUM_NOT_EQUAL("820004", "出库单头跟明细收货总数量不相等"),
	TOTAL_PICK_NUM_NOT_EQUAL("820005", "出库单头跟明细理货总数量不相等"),
	AVAILABLE_PICK_NUM_NOT_ENOUGH("820006", "可拣货数量不足"),
	AUTO_PICK_NUM_NOT_ENOUGH("820007", "自动补拣失败，库存不足"),
	RECEIVE_WAREHOUSE_NOT_EXIST("820008", "接收方仓库不存在"),
	ONWAY_WAREHOUSE_NOT_EXIST("820009", "在途仓不存在"),
	BILL_OUT_STORE_SUBMIT_ERROR("820010", "提交失败，请检查单据状态"),
	BILL_OUT_STORE_DELETE_ERROR("820011", "删除失败，请检查单据状态"),
	BILL_OUT_STORE_SEND_ERROR("820012", "送货失败，请检查单据状态"),
	BILL_OUT_STORE_REJECT_ERROR("820013", "驳回失败，请检查单据状态"),
	BILL_OUT_STORE_ADD_ERROR("820014", "新增出库单失败"),
	BILL_OUT_STORE_UPDATE_ERROR("820015", "更新出库单失败，请检查单据状态"),
	BILL_OUT_STORE_ADD_STATUS_ERROR("820016", "新增发货明细失败，请检查单据状态"),
	BILL_OUT_STORE_UPDATE_STATUS_ERROR("820017", "更新发货明细失败，请检查单据状态"),
	BILL_OUT_STORE_DELETE_STATUS_ERROR("820018", "删除发货明细失败，请检查单据状态"),
	BILL_OUT_STORE_PICK_ADD_STATUS_ERROR("820019", "新增拣货明细失败，请检查单据状态"),
	BILL_OUT_STORE_PICK_DELETE_STATUS_ERROR("820020", "删除拣货明细失败，请检查单据状态"),
	BILL_OUT_STORE_AUTO_PICK_STATUS_ERROR("820021", "整单自动补拣失败，请检查单据状态"),
	BILL_OUT_STORE_CURRENCY_DIFF_ERROR("820022", "所选库存币种不相同，请重新选择"),
	BILL_OUT_STORE_CURRENCY_TYPE_DIFF_ERROR("820023", "所选库存币种与出库单币种不相同，请重新选择"),
	BILL_OUT_STORE_CURRENCY_TYPE_NOT_EXIST("820024", "库存币种不能为空"),

	BILL_DELIVERY_ADD_ERROR("830001", "新增销售单失败"),
	BILL_DELIVERY_UPDATE_ERROR("830002", "更新销售单失败，请检查单据状态"),
	BILL_DELIVERY_DELETE_ERROR("830003", "删除销售单失败，请检查单据状态"),
	BILL_DELIVERY_SUBMIT_STATUS_ERROR("830004", "提交销售单失败，请检查单据状态"),
	BILL_DELIVERY_REJECT_STATUS_ERROR("830005", "驳回失败，请检查单据状态"),
	BILL_DELIVERY_ADD_STATUS_ERROR("830006", "新增销售明细失败，请检查单据状态"),
	BILL_DELIVERY_UPDATE_STATUS_ERROR("830007", "更新销售明细失败，请检查单据状态"),
	BILL_DELIVERY_DELETE_STATUS_ERROR("830008", "删除销售明细失败，请检查单据状态"),

	STL_LOCK_ERROR("840001", "锁定出库库存失败"),
	STL_STOCK_RELEASE_ERROR("840002", "释放出库库存失败"),
	STL_STOCK_SUBTRACT_ERROR("840003", "扣减库存后库存为负数，请重新操作"),
	STL_SALE_LOCK_ERROR("840004", "锁定销售库存失败"),
	STL_SALE_RELEASE_ERROR("840005", "释放销售库存失败"),
	
	RECEIPT_ADVANCE("100001","核完转预存业务出错"),
	RECEIPT_RECEIPT_UPDATE("100002","更新水单信息异常,id为{}"),
	
	PAY_ORDER_UPDATE_EXCEPTION("50001","更新付款单异常,id为{}"),

    /************记账**********/
    WRITE_OFF_AMOUNT_NOT_ZERO("900001" , "已核销金额必须为0"),
    CHECK_AMOUNT_EXCCED("900002" , "对账金额[{}]不能超过待对账金额[{}],请重新输入"),
    REC_AND_LINE_NOT_MATCH("900003" , "应收id和应收明细id不匹配"),
    ASSIST_ITEM_NOT_EXIST("900004" , "[{}]科目辅助项[{}]不能为空"),
    DEBIT_CREDIT_NOT_EQUAL("900005" , "录入凭证出错，借[{}]贷[{}]不相等"),
    RED_VOUCHER_STATE_ERROR("900006" , "凭证状态有误无法红冲，请检查"),
    VOUCHER_ALREADY_DELETE("900007" , "凭证已经删除,无法红冲"),
    VOUCHER_AUDIT_STATE_ERROR("900008" , "审核单据状态有误，请检查"),
    STANDARD_DEBIT_CREDIT_NOT_EQUAL("900009" , "录入凭证出错，本币[{}]借[{}]贷[{}]不相等"),
    
    /************帐套*************/
    REL_ALREADY_EXIST("110001" , "科目不能重复分配"),
    ALREADY_INVALID("110002" , "id[{}]已经作废"),
    ALREADY_SUBMIT("110000" , "id[{}]已经提交"),
    ALREADY_DELETE("110003" , "id[{}]已经删除"),
    DELETE_ERROR("110004" , "只有待提交状态才能删除，请检查！"),
    SUBMIT_ERROR("110004" , "只有待提交状态才能提交，请检查！"),
    ACCOUNT_LINE_NOT_LAST_LEVEL("110005" , "编号[{}]不是末级科目"),
    ACCOUNT_BOOK_INVALID("110006" , "经营单位[{}]下没有可用的帐套"),
    ACCOUNT_BOOK_NOT_IN_USE("110007" , "帐套[{}]不可用"),
    
    /************费用*************/
    PAYER_RECEIVER_EQUAL("120001" , "应收客户和应付客户不能相同"),
    FEE_DELETE_ERROR("120002" , "只有待提交状态才能删除，请检查！"),
    FEE_SUBMIT_ERROR("120003" , "只有待提交状态才能提交，请检查！"),
	
    /************报关申请*************/
	CUSTOMS_APPLY_ADD_ERROR("2100001", "新增报关申请失败"),
	CUSTOMS_APPLY_UPDATE_ERROR("2100002", "更新报关申请失败，单据已提交"),
	CUSTOMS_APPLY_DELETE_ERROR("2100003", "删除报关申请失败，单据已提交"),
	CUSTOMS_APPLY_SUBMIT_ERROR("2100004", "提交报关申请失败，单据已提交"),
	CUSTOMS_APPLY_LINE_ADD_STATUS_ERROR("2100005", "新增报关申请明细失败，单据已提交"),
	CUSTOMS_APPLY_LINE_UPDATE_STATUS_ERROR("2100006", "更新报关申请明细失败，单据已提交"),
	CUSTOMS_APPLY_LINE_DELETE_STATUS_ERROR("2100007", "删除报关申请明细失败，单据已提交"),
	CUSTOMS_APPLY_DTL_NOT_ADD("2100008", "报关申请未添加报关明细"),
	CUSTOMS_APPLY_DTL_NUM_NOT_EQUAL("2100009", "报关申请跟明细报关总数量不相等"),
	CUSTOMS_APPLY_DTL_TAX_RATE_DIFF_ERROR("2100010", "所选出库单明细税率不相同，请重新选择"),
	CUSTOMS_APPLY_DTL_TAX_RATE_TYPE_DIFF_ERROR("2100011", "所选出库单明细税率与报关申请税率不相同，请重新选择"),
	CUSTOMS_APPLY_DTL_TAX_RATE_NOT_EXIST("2100012", "商品税率不能为空"),
	CUSTOMS_APPLY_DTL_SEND_NUM_EXCEED("2100013", "报关数量超出剩余报关数量，请检查报关明细"),
	/************PMSS*************/
	PMSS_SUPPLIER_UPDATE_ERROR("2200001", "PMS接口更新异常"),
	PMSS_SUPPLIER_DELETE_ERROR("2200002", "PMS接口删除异常"),
	PMSS_SUPPLIER_ADD_ERROR("2200003", "PMS接口新增异常"),
	PMSS_SUPPLIER_ADD_NOT_ERROR("2200004", "PMS接口不能绑定"),
	PMSS_SUPPLIER_ADD_EXIST_ERROR("2200005", "PMS接口绑定关系已存在"),
	PMSS_SUPPLIER_SUBMIT_ERROR("2200006", "PMS接口提交异常"),
	PMSS_SUPPLIER_BIND_ERROR("2200007", "PMS接口无法进行绑定"),
	PMSS_SUPPLIER_UNBIND_ERROR("2200008", "PMS接口无法进行解绑"),
	PMSS_SUPPLIER_BIND_DATABASE_ERROR("2200009", "PMS接口绑定异常"),
	/************部门*************/
	DEPARTMENT_UPDATE_ERROR("2300001", "部门更新异常"),
	DEPARTMENT_DELETE_ERROR("2300002", "部门删除异常"),
	DEPARTMENT_ADD_ERROR("2300003", "部门新增异常"), 
	DEPARTMENT_DETAIL_ERROR("2300004", "部门浏览异常"),
	DEPARTMENT_DELETE_NODE_ERROR("2300006", "该部门下存在部门，无法删除！"),
	DEPARTMENT_RELATIVE_ERROR("2300005", "不能选择当前部门的下级部门为上级部门"),
	DEPARTMENT_RELATIVE_TERROR("2300007", "不能选择当前部门为上级部门"),
	DEPARTMENT_PARENTID_TERROR("2300008", "上级部门不能为空"),
	DEPARTMENT_EXIST_TERROR("2300009", "该部门名称或编号已存在"),
	/************微信*************/
	WECHAT_UPDATE_ERROR("2400001", "绑定异常");

    private String code;
    private String msg;
    private ExcMsgEnum(final String code, final String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getMsgByCode(String code){
        ExcMsgEnum[] excMsgEnArr = ExcMsgEnum.values();
        for(ExcMsgEnum msgEnum:excMsgEnArr){
            if(code.equals(msgEnum.code)){
                return msgEnum.msg;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
