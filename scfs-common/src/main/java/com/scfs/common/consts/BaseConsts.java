
package com.scfs.common.consts;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.scfs.common.utils.DateFormatUtils;
import com.scfs.common.utils.DecimalUtil;

/**
 * 基础模块常量定义类
 * @author Administrator
 *
 */
public class BaseConsts {
	/** --------------------主体信息------------------------ **/

	/** 编号占位符 **/
	public final static String NO_PLACEHOLDER = "-"; // 经营单位
	public final static String GOODS_NO_PREFIX = "G"; // 商品编号前缀

	/** 连接词符号 **/
	public final static String CONJUNCTION_FLAG = "-";
	public final static String SEPARATOR_COMMA = ",";

	public final static String ZT_NO_PREFIX = "ZT"; // 帐套编号前缀
	public final static String FE_NO_PREFIX = "FE"; // 费用编号前缀
	/** 开发票前缀 **/
	public final static String VT_NO_PREFIX = "VT"; // 费用编号前缀
	/** 主体编号前缀 **/
	public final static String BUSIUNIT_NO_PREFIX = "L"; // 经营单位
	public final static String WAREHOUSE_NO_PREFIX = "W"; // 仓库
	public final static String CUSTOMER_NO_PREFIX = "C"; // 客户
	public final static String PROJECT_NO_PREFIX = "P"; // 项目

	/** 入库单编号前缀 **/
	public final static String PRE_BILL_IN_STORE = "IN";
	/** 出库单编号前缀 **/
	public final static String PRE_BILL_OUT_STORE = "OU";
	/** 销售单编号前缀 **/
	public final static String PRE_BILL_DELIVERY = "TH";
	/** 采购单前缀 */
	public final static String PO_NO_PRE = "PO";
	/** 结算条款前缀 */
	public final static String PRE_PROJECTITEM_NO = "JS";
	/** 条款编号前缀 */
	public final static String PRE_PROJITEM_NO = "TK";
	/** 水单编号前缀 */
	public final static String PRE_RECEIPT_NO = "BR";
	/** 付款编号前缀 */
	public final static String PAY_ORDER_NO = "PY";
	/** 合并付款编号前缀 */
    public final static String MERGE_PAY_ORDER_NO = "MP";
	/** 收票申请编号前缀 */
	public final static String COLLECT_APPLY_NO = "PV";
	/** 报关申请编号前缀 */
	public final static String CUSTOMS_APPLY_NO = "BG";
	/** 退税申请编号前缀 */
	public final static String REFUND_APPLY_NO = "TS";
	/** 项目临时额度前缀 */
    public final static String PRE_PROJECT_POOL_ADJUST_NO = "LS";
    /** 境外开票申请编号前缀 */
	public final static String OVERSEAS_APPLY_NO = "IV";
	/** 客户维护编号前缀 */
	public final static String CUSTOMER_MAINTAIN_NO = "CM";
	/** 事项管理编号前缀 */
	public final static String MATTER_MANAGE_NO = "MM";
	/** 审核流编号前缀 **/
	public final static String AUDIT_FLOW_NO = "AF";
	
	public final static String LOGOIN_USER_NAME = "USER_NAME"; // 登陆用户名称
	public final static String WEI_XIN = "WEI_XIN_"; // 微信
	public final static String CHINESE_NAME = "CHINESE_NAME"; // 登陆用户中文名
	public final static String LOGOIN_USER_TOKEN = "USER_TOKEN"; // 登陆用户token
	public final static String LOG_ERROR_COUNT = "LOG_ERROR_COUNT_"; // 登陆用户密码错误次数

	/** 主体类型 **/
	public final static int SUBJECT_TYPE_BUSI_UNIT = 1; // 经营单位
	public final static int SUBJECT_TYPE_WAREHOUSE = 2; // 仓库
	public final static int SUBJECT_TYPE_SUPPLIER = 4; // 供应商
	public final static int SUBJECT_TYPE_CUSTOMER = 8; // 客户
	
	public final static Date DEFAULT_DATE = DateFormatUtils.formatToDate(DateFormatUtils.YYYY_MM_DD_HH_MM_SS,"1970-01-01 00:00:00");

	public final static String MINUSONE="-1";
	public final static int ZERO = 0;
	public final static int ONE = 1;
	public final static int TWO = 2;
	public final static int THREE = 3;
	public final static int FOUR = 4;
	public final static int FIVE = 5;
	public final static int SIX = 6;
	public final static int SEVEN = 7;
	public final static int EIGHT = 8;
	public final static int NINE = 9;
	public final static int TEN = 10;
	public final static int INT_11 = 11;
	public final static int INT_12 = 12;
	public final static int INT_13 = 13;
	public final static int INT_14 = 14;
	public final static int INT_15 = 15;
	public final static int INT_16 = 16;
	public final static int INT_17 = 17;
	public final static int INT_18 = 18;
	public final static int INT_19 = 19;
	public final static int INT_20 = 20;
	public final static int INT_21 = 21;
	public final static int INT_22 = 22;
	public final static int INT_23 = 23;
	public final static int INT_24 = 24;
	public final static int INT_25 = 25;
	public final static int INT_26 = 26;
	public final static int INT_27 = 27;
	public final static int INT_28 = 28;
	public final static int INT_29 = 29;
	public final static int INT_30 = 30;
	public final static int INT_31 = 31;
	public final static int INT_32 = 32;
	public final static int INT_33 = 33;
	public final static int INT_34 = 34;
	public final static int INT_35 = 35;
	public final static int INT_36 = 36;
	public final static int INT_37 = 37;
	public final static int INT_38 = 38;
	public final static int INT_39 = 39;
	public final static int INT_40 = 40;
	public final static int INT_41 = 41;
	public final static int INT_42 = 42;
	public final static int INT_43 = 43;
	public final static int INT_44 = 44;
	public final static int INT_60 = 60;
	public final static int INT_79 = 79;
	public final static int INT_80 = 80;
	public final static int INT_90 = 90;
	public final static int INT_98 = 98;
	public final static int INT_99 = 99;
	public final static int INT_100 = 100;
	public final static int INT_200 = 200;
	public final static int INT_10000 = 10000;
	
	public final static Map<Integer, String> CURRENCY_UNIT_MAP = new HashMap<Integer, String>() {
		private static final long serialVersionUID = 6623275565137829292L;
		{
			put(1, "CNY");
			put(2, "USD");
			put(3, "HKD");
			put(4, "EUR");
		}
	};

	// excel导出最大行数,超过五千异步导出
	public final static int EXPORT_EXCEL_ROWS_MAX = 5000;
	// excel导入最大行数
	public final static int IMPORT_EXCEL_ROWS_MAX = 999;
	// 查询无权限时显示字符
	public final static String NO_PERMISSION_HIT = "***";
	// 空格
	public final static String STRING_BLANK_SPACE = " ";
	// 超级管理员角色名称
	public final static String SUPER_ROLE_NAME = "超级管理员";
	// 系统管理员角色名称
	public final static String SYSTEM_ROLE_NAME = "系统管理员";
	// 系统管理员角色名称
    public final static int SYSTEM_ROLE_ID = 1;
	// 抓取汇率异常，系统报警
	public static final String RATE_EXCEPT_MSG = "抓取{}的汇率异常，网址：{}，请手工输入处理";
	public static final String RATE_EXCEPT_MSG_SUBJECT = "抓取汇率异常，供应链金融系统报警";
	public static final String ATE_EXCEPT_ROLENAME = "系统管理员";

	// 成功失败标示
	public static final String FLAG_YES = "Y";
	public static final String FLAG_NO = "N";
	// 币种代码
	public static final String CURRENCY_CNY = "CNY";
	public static final String CURRENCY_USD = "USD";
	public static final String CURRENCY_HKD = "HKD";
	
	// 税率、科目固定
	public static final String DEFAULT_GOODS_TAX_RATE = "0.17";
	public static final String DEFAULT_PROVIDE_INVOICE_TAX_RATE = "0.06";
	public static final String DEFAULT_REC_FEE_SPEC_NO = "XSFWF";
	
	public final static BigDecimal getFeeRate() {
	    return DecimalUtil.divide(new BigDecimal("0.068"), new BigDecimal("360"));
	}
	
	public static final String FUND_PRODUCT_NO = "资金成本"; 
	public static Integer SYSTEM_USER_ID = 1;	//超级管理员id
	
	/*********************系统环境配置******************************/
	public static final String PROFILE_DEV = "dev";
	
}
