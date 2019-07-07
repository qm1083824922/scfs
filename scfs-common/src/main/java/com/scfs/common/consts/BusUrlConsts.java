package com.scfs.common.consts;

/**
 * 相关业务模块URL
 * @author Administrator
 *
 */
public class BusUrlConsts extends UrlConsts {

	/** 采购订单 */
	public final static String ADD_PO_TITLE = "/po" + ADD;
	public final static String QUERY_PO_TITLE = "/po" + QUERY;
	public final static String UPDATE_PO_TITLE = "/po" + UPDATE;
	public final static String EDIT_PO_TITLE = "/po" + EDIT;
	public final static String DETAIL_PO_TITLE = "/po" + DETAIL;
	public final static String DELETE_PO_TITLE = "/po" + DELETE;
	public final static String SUBMIT_PO_TITLE = "/po" + SUBMIT;
	public final static String PRINT_PO_TITLE = "/po" + PRINT;
	public final static String RECEIVE_PO_TITLE = "/po" + RECEIVE;
	public final static String FLY_ORDER_PO = "/po" + FLY_ORDER;
	public final static String PRINT_PO_CONTRACT = "/poContract" + PRINT;
	public final static String PRINT_PO_INVOICE = "/poInvoice" + PRINT;
	public final static String PRINT_PO_PACK = "/poPack" + PRINT;
	public final static String DETAIL_PO_PACK = "/poPack" + DETAIL;
	public final static String QUERY_PO_PACK_LINE = "/poPack/line" + QUERY;
	public final static String DETAIL_PO_PRINT_TITLE = "/poPrint" + DETAIL;
	public final static String PRINT_PO_SEARCH_PRINT_LIST = "/poSearchPrint/list" + PRINT;
	public final static String PRINT_PO_INVOICE_PRINT_LIST = "/poInvoicePrint/list" + PRINT;
	public final static String QUERY_PO_TITLE_LIST = "/poTitle/list" + QUERY;
	public final static String QUERY_PO_BALANCE_OF_ACCOUNT_LIST = "/poBalanceOfAccount/list" + QUERY;//对账单打印业务
	public final static String QUERY_PO_BALANCE_OF_ACCOUNT_LINE_LIST = "/poBalanceOfAccountLine/list" + QUERY;
	public final static String QUERY_PO_DELIVER_GOODS_LIST = "/poDeliverGoods/list" + QUERY;//送货单打印业务
	public final static String QUERY_PO_DELIVER_GOODS_LINE_LIST = "/poDeliverGoodsLine/list" + QUERY;
	public final static String ADD_PO_PACK_PRINT = "/poPack/print" + ADD;//采购装箱打印信息
	public final static String QUERY_MERGE_PO_PACK_LINE = "/merge/poPack/line" + QUERY;
	public final static String QUERY_MERGE_SALE_PACK_LINE = "/merge/salePack/line" + QUERY;
	
	public final static String ADD_PO_RETURN_TITLE = "/poReturn" + ADD;
	public final static String UPDATE_PO_RETURN_TITLE = "/poReturn" + UPDATE;
	public final static String QUERY_PO_RETURN_TITLE = "/poReturn" + QUERY;
	public final static String EDIT_PO_RETURN_TITLE = "/poReturn" + EDIT;
	public final static String DELETE_PO_RETURN_TITLE = "/poReturn" + DELETE;
	public final static String DETAIL_PO_RETURN_TITLE = "/poReturn" + DETAIL;
	public final static String SUBMIT_PO_RETURN_TITLE = "/poReturn" + SUBMIT;
	public final static String PRINT_PO_RETURN_TITLE = "/poReturn" + PRINT;

	public final static String ADD_PO_RETURN_LINE = "/poReturn/line" + ADD;
	public final static String EDIT_PO_LINE_RETURN_TITLE = "/poReturn/line" + EDIT;
	public final static String UPDATE_PO_LINE_RETURN_TITLE = "/poReturn/line" + UPDATE;
	public final static String DELETE_PO_LINE_RETURN_TITLE = "/poReturn/line" + DELETE;

	public final static String QUERY_PO_RETURN_FILE = "/poReturn/file/query";
	public final static String UPLOAD_PO_RETURN_FILE = "/poReturn/file/upload";
	public final static String DELETE_PO_RETURN_FILE = "/poReturn/file/delete";
	public final static String DOWNLOAD_PO_RETURN_FILE = "/poReturn/file/download";
	public final static String DOWNLOAD_PO_RETURN_FILE_LIST = "/poReturn/fileList/download";

	public final static String QUERY_BILL_IN_STORE_LINE = "/poReturn/billInStoreLine" + QUERY;
	public final static String QUERY_BILL_IN_STORE_LINE_UNDIVIDE = "/poReturn/billInStoreLine/undivide" + QUERY;

	public final static String EXPORT_PO_RETURN_TITLE = "/poReturn/poReturn.xls";
	public final static String EXPORT_PO_RETURN_LINE = "/poReturn/line/poReturnLine.xls";
	public final static String EXPORT_PO_RETURN_COUNT = "/poReturn/excel/poReturn/count";
	public final static String EXPORT_PO_RETURN_LINE_COUNT = "/poReturn/excel/poReturnLine/count";

	/** 采购订单明细 */
	public final static String ADD_PO_LINE = "/po/line" + ADD;
	public final static String QUERY_PO_LINE = "/po/line" + QUERY;
	public final static String DELETE_PO_LINE = "/po/line" + DELETE;
	public final static String UPDATE_PO_LINE = "/po/line" + UPDATE;
	public final static String EDIT_PO_LINE = "/po/line" + EDIT;
	public final static String QUERY_PO_PRINT_LINE = "/poPrint/line" + QUERY;
	public final static String QUERY_PO_LINE_LIST = "/poLine/list" + QUERY;

	/** 采购导出EXCEL */
	public final static String EXPORT_PO = "/po/excel/poExport.xls";// 下载
	public final static String IMPORT_PO = "/po/excel" + IMPORT;// 导入
	public final static String IMPORT_PO_LINE = "/poLine/excel" + IMPORT;// 导入订单明细
	public final static String DOWNLOAD_PO_TEMPLATE = "/po/excel/poTemplate.xls";// 下载模板
	public final static String DOWNLOAD_PO_LINE_TEMPLATE = "/po/excel/poLineTemplate.xls";// 下载订单明细模板
	public final static String QUERY_PO_FILE = "/po/file/query";
	public final static String UPLOAD_PO_FILE = "/po/file/upload";
	public final static String DELETE_PO_FILE = "/po/file/delete";
	public final static String DOWNLOAD_PO_FILE = "/po/file/download";
	public final static String DOWNLOAD_PO_FILE_LIST = "/po/fileList/download";
	public final static String EXPORT_PO_ORDER = "/poOrder/excel/poOrder.xls";
	public final static String EXPORT_PO_ORDER_COUNT = "/poOrder/excel/poOrder/count";

	public final static String EXPORT_PO_LINE_ORDER = "/poLine/excel/export.xls";
	public final static String EXPORT_PO_LINE_ORDER_COUNT = "/poLine/excel/export/count";
	public final static String EXPORT_PO_ORDER_DTL = "/poOrderDtl/excel/poOrderDtl.xls";
	public final static String EXPORT_PO_ORDER_DTL_COUNT = "/poOrderDtl/excel/poOrderDtl/count";
	/** 铺货订单 */
	public final static String QUERY_DISTRIBUTION_ORDER_TITLE = "/distriOrder" + QUERY;
	public final static String DETAIL_DISTRIBUTION_ORDER_TITLE = "/distriOrder" + DETAIL;
	public final static String DETAIL_DISTRIBUTION_ORDER_LINE = "/distriOrder/line" + QUERY;

	public final static String EXPORT_DISTRIBUTION_ORDER = "/distriOrder/excel/distriOrder.xls";
	public final static String EXPORT_DISTRIBUTION_ORDER_COUNT = "/distriOrder/excel/distriOrder/count";
	public final static String EXPORT_DISTRIBUTION_ORDER_DTL = "/distriOrderDtl/excel/distriOrderDtl.xls";
	public final static String EXPORT_DISTRIBUTION_ORDER_DTL_COUNT = "/distriOrderDtl/excel/distriOrderDtl/count";

	/** 项目管理信息 */
	public final static String ADDPROJECT = "/project" + ADD;
	public final static String QUERYPROJECT = "/project" + QUERY;
	public final static String DETAILPROJECT = "/project" + DETAIL;
	public final static String EDITPROJECT = "/project" + EDIT;
	public final static String SUBMITPROJECT = "/project" + SUBMIT;
	public final static String DELETEPROJECT = "/project" + DELETE;
	public final static String LOCKPROJECT = "/project" + LOCK;
	public final static String UNLOCKPROJECT = "/project" + UNLOCK;
	public final static String UPDATEPROJECT = "/project" + UPDATE;
	public final static String COPYPROJECT = "/project" + COPY;
	public final static String EXPORT_PROJECT = "/project/excel/collect.xls";// 导出
	public final static String EXPORT_PROJECT_COUNT = "/project/excel/export/count";

	/** 项目分配供应商客户仓库 */
	public final static String QUERY_PROJECT_SUBJECTV = "/project/subjectv" + QUERY;
	public final static String QUERY_PROJECT_SUBJECTV_NOTASSIGNED = "/project/subjectv/notAssigned" + QUERY;
	public final static String DELETE_PROJECT_SUBJECTV = "/project/subjectv" + DELETE;
	public final static String DELETEALL_PROJECT_SUBJECTV = "/project/subjectv" + DELETE_ALL;
	public final static String DIVID_PROJECT_SUBJECTV = "/project/subjectv" + DIVID;
	public final static String DIVIDALL_PROJECT_SUBJECTV = "/project/subjectv" + DIVID_ALL;
	public final static String QUERY_PROJECT_SUBJECTC = "/project/subjectc" + QUERY;
	public final static String QUERY_PROJECT_SUBJECTC_NOTASSIGNED = "/project/subjectc/notAssigned" + QUERY;
	public final static String DELETE_PROJECT_SUBJECTC = "/project/subjectc" + DELETE;
	public final static String DELETEALL_PROJECT_SUBJECTC = "/project/subjectc" + DELETE_ALL;
	public final static String DIVID_PROJECT_SUBJECTC = "/project/subjectc" + DIVID;
	public final static String DIVIDALL_PROJECT_SUBJECTC = "/project/subjectc" + DIVID_ALL;
	public final static String QUERY_PROJECT_SUBJECTW = "/project/subjectw" + QUERY;
	public final static String QUERY_PROJECT_SUBJECTW_NOTASSIGNED = "/project/subjectw/notAssigned" + QUERY;
	public final static String DELETE_PROJECT_SUBJECTW = "/project/subjectw" + DELETE;
	public final static String DELETEALL_PROJECT_SUBJECTW = "/project/subjectw" + DELETE_ALL;
	public final static String DIVID_PROJECT_SUBJECTW = "/project/subjectw" + DIVID;
	public final static String DIVIDALL_PROJECT_SUBJECTW = "/project/subjectw" + DIVID_ALL;

	/** 项目分配商品 */
	public final static String QUERY_PROJECT_GOODS = "/project/goods" + QUERY;
	public final static String QUERY_DISTRIBUTE_PROJECT_GOODS = "/project/distributeGoods" + QUERY;
	public final static String QUERY_DISTRIBUTE_PROJECT_GOODS_NOTASSIGNED = "/project/distributeGoods/notAssigned"
			+ QUERY;
	public final static String QUERY_PROJECT_GOODS_NOTASSIGNED = "/project/goods/notAssigned" + QUERY;
	public final static String DELETE_PROJECT_GOODS = "/project/goods" + DELETE;
	public final static String DELETEALL_PROJECT_GOODS = "/project/goods" + DELETE_ALL;
	public final static String DIVID_PROJECT_GOODS = "/project/goods" + DIVID;
	public final static String DIVIDALL_PROJECT_GOODS = "/project/goods" + DIVID_ALL;

	/** 项目条款信息 */
	public final static String ADDPROJECTITEM = "/projectItem" + ADD;
	public final static String QUERYPROJECTITEM = "/projectItem" + QUERY;
	public final static String DETAILPROJECTITEM = "/projectItem" + DETAIL;
	public final static String EDITPROJECTITEM = "/projectItem" + EDIT;
	public final static String SUBMITPROJECTITEM = "/projectItem" + SUBMIT;
	public final static String DELETEPROJECTITEM = "/projectItem" + DELETE;
	public final static String LOCKPROJECTITEM = "/projectItem" + LOCK;
	public final static String UNLOCKPROJECTITEM = "/projectItem" + UNLOCK;
	public final static String UPDATEPROJECTITEM = "/projectItem" + UPDATE;
	public final static String UPLOADFILEITEM = "/projectItemFile" + UPLOAD;
	public final static String QUERYFILEITEM = "/projectItemFile" + QUERY;
	public final static String DELETEFILEITEM = "/projectItemFile" + DELETE;
	public final static String DOWNLOADFILEITEM = "/projectItemFile" + DOWNLOAD;
	public final static String DOWNLOADFILELIST = "/projectItemFileList" + DOWNLOAD;
	public final static String COPYPROJECTITEM = "/projectItem" + COPY;
	public final static String SELECTEDROJECTITEM = "/projectItem/selected";

	/** 项目事件 */
	public final static String QUERY_PROJECT_RISK = "/projectRisk" + QUERY;
	public final static String ADD_PROJECT_RISK = "/projectRisk" + ADD;
	public final static String EDIT_PROJECT_RISK = "/projectRisk" + EDIT;
	public final static String DETAIL_PROJECT_RISK = "/projectRisk" + DETAIL;
	public final static String UPDATE_PROJECT_RISK = "/projectRisk" + UPDATE;
	public final static String DELETE_PROJECT_RISK = "/projectRisk" + DELETE;
	public final static String SUBMIT_PROJECT_RISK = "/projectRisk" + SUBMIT;

	/** 融资池信息 */
	public final static String QUERYPROJECTPOOLASSERTDTL = "/projectPoolAssertDtl" + QUERY;
	public final static String QUERYPROJECTPOOLFUNDDTL = "/projectPoolFundDtl" + QUERY;
	public final static String QUERYPROJECTPOOL = "/projectPool" + QUERY;
	public final static String ADDPROJECTPOOLASSERTDTL = "/projectPoolAssertDtl" + ADD;
	public final static String ADDPROJECTPOOLFUNDDTL = "/projectPoolFundDtl" + ADD;
	public final static String QUERYPROJECTPOOLBYID = "/projectPoolById" + QUERY;
	public final static String QUERYPROJECTPOOLBYPROJECTID = "/projectPoolByProjectId" + QUERY;
	public final static String EXPORT_FUND_REPORT = "/project/pool/excel/fund.xls";// 库存导出
	public final static String EXPORT_ASSERT_REPORT = "/project/pool/excel/assert.xls";// 库存导出

	/** 发票信息 */
	public final static String ADDINVOICESERVICE = "/invoiceService" + ADD;
	public final static String SUBMITINVOICESERVICE = "/invoiceService" + SUBMIT;
	public final static String EXPORT_INVOICE_EXCEL = "/invoiceService/excel/invoiceExcel.xls";
	public final static String EXPORT_INVOICE_APPLY_EXCEL = "/invoiceService/excel/invoiceApplyExcel.xls";
	public final static String EXPORT_INVOICE_INFO_EXCEL = "/invoiceInfoService/excel/invoiceInfoExcel.xls";
	public final static String DETAIL_INVOICE_INFO_VOUCHER = "/invoiceService/voucher" + DETAIL;// 发票凭证信息

	public final static String UPDATEINVOICESERVICE = "/invoiceService" + UPDATE;
	public final static String DELETEINVOICESERVICE = "/invoiceService" + DELETE;
	public final static String DETAILINVOICEITEM = "/invoiceService" + DETAIL;
	public final static String EDITINVOICEITEM = "/invoiceService" + EDIT;
	public final static String QUERYINVOICEITEM = "/invoiceServiceItem" + QUERY;
	public final static String QUERYINVOICEENSUREITEM = "/invoiceEnsureItem" + QUERY;
	public final static String EXPORT_INVOICE_ENSURE_COUNT = "/invoiceEnsureItem/excel/export/count";

	public final static String ENSUREINVOICEENSUREITEM = "/ensureInvoiceItem" + UPDATE;
	public final static String PRINT_INVOICE_APPLY = "/invoiceApply/print";
	public final static String IMPORT_INVOICE_APPLY = "/invoiceServiceItem/excel" + IMPORT;// 导入
	public final static String INVOICE_TEMPLATE_DOWNLOAD = "/template/invoice/invoice_ensure_template.xls";// 导入
	public final static String UPLOAD_INVOICE_APPLY = "/invoiceApplyFile" + UPLOAD;
	public final static String QUERY_FILE_INVOICE_APPLY = "/invoiceApplyFile" + QUERY;
	public final static String DELETE_FILE_INVOICE_APPLY = "/invoiceApplyFile" + DELETE;
	public final static String DOWNLOAD_INVOICE_APPLY = "/invoiceApplyFile" + DOWNLOAD;
	public final static String DOWNLOAD_LIST_INVOICE_APPLY = "/invoiceApplyFileList" + DOWNLOAD;
	public final static String EXPORT_INVOICE_APPLY_COUNT = "/invoiceApply/excel/export/count";

	/** 费用信息 */
	public final static String EDITFEE = "/invoiceFee" + EDIT;
	public final static String DELETEINVOICEFEE = "/invoiceFee" + DELETE;
	public final static String BATCHDELETEFEE = "/invoiceBatchFee" + DELETE;
	public final static String BATCHUPDATEFEE = "/invoiceBatchFee" + UPDATE;
	public final static String BATCHINSERTFEE = "/invoiceBatchFee" + ADD;
	public final static String QUERYINOVICEFEESELECT = "/invoiceFeeSelect" + QUERY;
	public final static String QUERYINOVICEFEENOTSELECT = "/invoiceFeeNotSelect" + QUERY;

	/** 销售信息 */
	public final static String QUERYSALENOTSELECT = "/invoiceSaleNotSelect" + QUERY;
	public final static String QUERYSALESELECT = "/invoiceSaleSelect" + QUERY;
	public final static String DELETESINGLESALE = "/invoiceSale" + DELETE;
	public final static String UPDATEINVOICESALE = "/invoiceSale" + UPDATE;
	public final static String INSERTINVOICESALE = "/invoiceBatchSale" + ADD;
	public final static String DELETEBATCHSALE = "/invoiceBatchSale" + DELETE;
	public final static String EDITSALE = "/invoiceSale" + EDIT;

	/** 发票模拟信息 */
	public final static String INSERTSIMULATION = "/invoiceSimulation" + ADD;
	public final static String QUERYSIMULATION = "/invoiceInfo" + QUERY;
	public final static String QUERYPRINTSIMULATION = "/invoiceInfo/print" + QUERY;
	public final static String DELETESIMULATEINFO = "/invoiceInfo" + DELETE;

	/** 收票管理 **/
	public final static String QUERY_INVOICE_COLLECT = "/invoiceCollect" + QUERY;
	public final static String ADD_INVOICE_COLLECT = "/invoiceCollect" + ADD;
	public final static String UPDATE_INVOICE_COLLECT = "/invoiceCollect" + UPDATE;
	public final static String DETAIL_INVOICE_COLLECT = "/invoiceCollect" + DETAIL;
	public final static String EDIT_INVOICE_COLLECT = "/invoiceCollect" + EDIT;
	public final static String DELETE_INVOICE_COLLECT = "/invoiceCollect" + DELETE;
	public final static String SUBMIT_INVOICE_COLLECT = "/invoiceCollect" + SUBMIT;
	public final static String EXPORT_INVOICE_COLLECT = "/collect/excel/collect.xls";// 导出
	public final static String QUERY_INVOICE_COLLECT_APPROVE = "/invoiceCollect/approve" + QUERY;
	public final static String SUBMIT_INVOICE_COLLECT_APPROVE = "/invoiceCollect/approve" + SUBMIT;
	public final static String EXPORT_INVOICE_COLLECT_APPROVE = "/collect/excel/collectApprove.xls";// 导出
	public final static String IMPORT_INVOICE_COLLECT_APPROVE = "/collect/excel" + IMPORT;// 导入
	public final static String PRINT_INVOICE_COLLECT = "/invoiceCollect/print";// 打印
	public final static String UPLOAD_INVOICE_COLLECT = "/invoiceCollectFile" + UPLOAD;
	public final static String QUERY_FILE_INVOICE_COLLECT = "/invoiceCollectFile" + QUERY;
	public final static String DELETE_FILE_INVOICE_COLLECT = "/invoiceCollectFile" + DELETE;
	public final static String DOWNLOAD_INVOICE_COLLECT = "/invoiceCollectFile" + DOWNLOAD;
	public final static String DOWNLOAD_LIST_INVOICE_COLLECT = "/invoiceCollectFileList" + DOWNLOAD;
	public final static String DETAIL_INVOICE_COLLECT_VOUCHER = "/invoiceCollect/voucher" + DETAIL;// 收票凭证浏览
	public final static String EXPORT_INVOICE_COLLECT_COUNT = "/collect/excel/export/count";
	public final static String IMPORT_INVOICE_COLLECT = "/invoiceCollect/excel" + IMPORT;// 导入
	public final static String INVOICE_COLLECT_TEMPLATE_DOWNLOAD = "/template/collect/collect_approve_template.xls";// 导入
	public final static String QUERY_INVOICE_COLLECT_APPROVE_DTL = "/invoiceCollect/approve/dtl" + QUERY;

	/** 收票费用 **/
	public final static String QUERY_INVOICE_FEE_COLLECT = "/invoiceCollectFee" + QUERY;
	public final static String ADD_INVOICE_FEE_COLLECT = "/invoiceCollectFee" + ADD;
	public final static String UPDATE_INVOICE_FEE_COLLECT = "/invoiceCollectFee" + UPDATE;
	public final static String DETAIL_INVOICE_FEE_COLLECT = "/invoiceCollectFee" + DETAIL;
	public final static String EDIT_INVOICE_FEE_COLLECT = "/invoiceCollectFee" + EDIT;
	public final static String DELETE_ALL_INVOICE_FEE_COLLECT = "/invoiceCollectFee" + DELETE_ALL;
	public final static String DIVID_INVOICE_FEE_COLLECT = "/invoiceCollectFee" + DIVID;

	/** 收票订单 **/
	public final static String QUERY_INVOICE_PO_COLLECT = "/invoiceCollectPo" + QUERY;
	public final static String ADD_INVOICE_PO_COLLECT = "/invoiceCollectPo" + ADD;
	public final static String UPDATE_INVOICE_PO_COLLECT = "/invoiceCollectPo" + UPDATE;
	public final static String DETAIL_INVOICE_PO_COLLECT = "/invoiceCollectPo" + DETAIL;
	public final static String EDIT_INVOICE_PO_COLLECT = "/invoiceCollectPo" + EDIT;
	public final static String DELETE_ALL_INVOICE_PO_COLLECT = "/invoiceCollectPo" + DELETE_ALL;
	public final static String DIVID_INVOICE_PO_COLLECT = "/invoiceCollectPo" + DIVID;

	/** 境外收票 **/
	public final static String QUERY_INVOICE_OVERSEAS = "/invoiceOverseas" + QUERY;
	public final static String ADD_INVOICE_OVERSEAS = "/invoiceOverseas" + ADD;
	public final static String UPDATE_INVOICE_OVERSEAS = "/invoiceOverseas" + UPDATE;
	public final static String DETAIL_INVOICE_OVERSEAS = "/invoiceOverseas" + DETAIL;
	public final static String EDIT_INVOICE_OVERSEAS = "/invoiceOverseas" + EDIT;
	public final static String DELETE_INVOICE_OVERSEAS = "/invoiceOverseas" + DELETE;
	public final static String SUBMIT_INVOICE_OVERSEAS = "/invoiceOverseas" + SUBMIT;
	public final static String EXPORT_INVOICE_OVERSEAS = "/overseas/excel/overseas.xls";// 导出
	public final static String EXPORT_INVOICE_OVERSEAS_COUNT = "/overseas/excel/export/count";
	public final static String PRINT_INVOICE_OVERSEAS = "/invoiceOverseas/print";// 打印
	public final static String QUERY_FILE_INVOICE_OVERSEAS = "/invoiceOverseasFile" + QUERY;// 文件上传
	public final static String UPLOAD_INVOICE_OVERSEAS = "/invoiceOverseasFile" + UPLOAD;
	public final static String DELETE_FILE_INVOICE_OVERSEAS = "/invoiceOverseasFile" + DELETE;
	public final static String DOWNLOAD_INVOICE_OVERSEAS = "/invoiceOverseasFile" + DOWNLOAD;
	public final static String DOWNLOAD_LIST_INVOICE_OVERSEAS = "/invoiceOverseasFileList" + DOWNLOAD;

	/** 境外费用 **/
	public final static String QUERY_INVOICE_FEE_OVERSEAS = "/invoiceOverseasFee" + QUERY;
	public final static String QUERY_ALL_INVOICE_FEE_OVERSEAS = "/invoiceOverseasFeeAll" + QUERY;
	public final static String ADD_INVOICE_FEE_OVERSEAS = "/invoiceOverseasFee" + ADD;
	public final static String UPDATE_INVOICE_FEE_OVERSEAS = "/invoiceOverseasFee" + UPDATE;
	public final static String DETAIL_INVOICE_FEE_OVERSEAS = "/invoiceOverseasFee" + DETAIL;
	public final static String EDIT_INVOICE_FEE_OVERSEAS = "/invoiceOverseasFee" + EDIT;
	public final static String DELETE_ALL_INVOICE_FEE_OVERSEAS = "/invoiceOverseasFee" + DELETE_ALL;
	public final static String DIVID_INVOICE_FEE_OVERSEAS = "/invoiceOverseasFee" + DIVID;

	/** 收票订单 **/
	public final static String QUERY_INVOICE_PO_OVERSEAS = "/invoiceOverseasPo" + QUERY;
	public final static String QUERY_ALL_INVOICE_PO_OVERSEAS = "/invoiceOverseasPoAll" + QUERY;
	public final static String ADD_INVOICE_PO_OVERSEAS = "/invoiceOverseasPo" + ADD;
	public final static String UPDATE_INVOICE_PO_OVERSEAS = "/invoiceOverseasPo" + UPDATE;
	public final static String DETAIL_INVOICE_PO_OVERSEAS = "/invoiceOverseasPo" + DETAIL;
	public final static String EDIT_INVOICE_PO_OVERSEAS = "/invoiceOverseasPo" + EDIT;
	public final static String DELETE_ALL_INVOICE_PO_OVERSEAS = "/invoiceOverseasPo" + DELETE_ALL;
	public final static String DIVID_INVOICE_PO_OVERSEAS = "/invoiceOverseasPo" + DIVID;

	/** 帐套 **/
	public final static String ADD_ACCOUNT_BOOK = "/accountBook" + ADD;
	public final static String DELETE_ACCOUNT_BOOK = "/accountBook" + DELETE;
	public final static String QUERY_ACCOUNT_BOOK = "/accountBook" + QUERY;
	public final static String UPDATE_ACCOUNT_BOOK = "/accountBook" + UPDATE;
	public final static String DETAIL_ACCOUNT_BOOK = "/accountBook" + DETAIL;
	public final static String EDIT_ACCOUNT_BOOK = "/accountBook" + EDIT;
	public final static String SUBMIT_ACCOUNT_BOOK = "/accountBook" + SUBMIT;
	public final static String DIVID_ACCOUNT_BOOK = "/accountBook" + DIVID;
	public final static String EXPORT_ACCOUNTBOOK = "/accountBook/excel/accountBook.xls";

	/** 科目 **/
	public final static String ADD_ACCOUNT_LINE = "/accountLine" + ADD;
	public final static String DELETE_ACCOUNT_LINE = "/accountLine" + DELETE;
	public final static String QUERY_ACCOUNT_LINE = "/accountLine" + QUERY;
	public final static String UPDATE_ACCOUNT_LINE = "/accountLine" + UPDATE;
	public final static String DETAIL_ACCOUNT_LINE = "/accountLine" + DETAIL;
	public final static String EDIT_ACCOUNT_LINE = "/accountLine" + EDIT;
	public final static String SUBMIT_ACCOUNT_LINE = "/accountLine" + SUBMIT;
	public final static String EXPORT_ACCOUNTLINE = "/accountLine/excel/accountLine.xls";

	/** 帐套科目 **/
	public final static String ADD_ACCOUNT_BOOK_LINE_REL = "/accountBook/line/rel" + ADD;
	public final static String ADD_ACCOUNT_BOOK_LINE_REL_BATCH = "/accountBook/line/rel/batch" + ADD;
	public final static String INVALID_ACCOUNT_BOOK_LINE_REL = "/accountBook/line/rel" + INVALID;
	public final static String INVALID_ACCOUNT_BOOK_LINE_REL_BATCH = "/accountBook/line/rel/batch" + INVALID;
	public final static String QUERY_ACCOUNT_BOOK_LINE_REL = "/accountBook/line/rel" + QUERY;
	public final static String QUERY_ALL_ACCOUNT_BOOK_LINE_REL = "/accountBook/line/rel/all" + QUERY;

	/** 凭证 **/
	public final static String ADD_VOUCHER_DETAIL = "/voucher/detail" + ADD;
	public final static String UPDATE_VOUCHER_DETAIL = "/voucher/detail" + UPDATE;
	public final static String DETAIL_VOUCHER_DETAIL = "/voucher/detail" + DETAIL;
	public final static String EDIT_VOUCHER_DETAIL = "/voucher/detail" + EDIT;
	public final static String SUBMIT_VOUCHER = "/voucher" + SUBMIT;
	public final static String PRINT_VOUCHER = "/voucher" + PRINT;
	public final static String DELETE_VOUCHER = "/voucher" + DELETE;
	public final static String QUERY_VOUCHER = "/voucher" + QUERY;
	public final static String QUERY_VOUCHER_LINE = "/voucherLine" + QUERY;
	public final static String QUERY_VOUCHER_LINE_CHECK = "/voucherLine/check" + QUERY; // 查询待对账分录
	public final static String QUERY_VOUCHER_LINE_CHECK_PAGE = "/voucherLine/check/page" + QUERY; // 查询待对账分录--分页
	public final static String QUERY_VOUCHER_LINE_MERGE = "/voucherLine/merge" + QUERY; // 查询待合并分录
	public final static String QUERY_VOUCHER_LINE_GROUP = "/voucherLine/group" + QUERY;
	public final static String QUERY_CHECK_BILL_INFO = "/checkBill/info" + QUERY;
	public final static String DETAIL_CHECK_BILL_INFO = "/checkBill/info" + DETAIL;
	public final static String EXPORT_VOUCHER_LINE_COUNT = "/voucher/voucherLineList/count";
	public final static String EXPORT_VOUCHER_LINE = "/voucher/voucherLineList.xls";
	public final static String QUERY_STANDARD_COIN_INFO = "/voucher/queryStandardCoinInfo";

	/** 应收 **/
	public final static String DELETE_RECEIVE = "/receive" + DELETE;
	public final static String QUERY_RECEIVE = "/receive" + QUERY;
	public final static String QUERY_UN_RECEIVE = "/unReceive" + QUERY;
	public final static String QUERY_UNFINISHED_RECEIVE = "/unFinished/Receive" + QUERY;
	public final static String RECEIVE_EXPORT = "/receive/receiveList.xls";

	/** 应收明细 **/
	public final static String UPDATE_REC_LINE_BATCH = "/recLine/batch" + UPDATE;
	public final static String QUERY_REC_LINE = "/recLine" + QUERY;
	public final static String EDIT_REC_LINE = "/recLine" + EDIT;
	public final static String DELETE_REC_LINE_BATCH = "/recLine/batch" + DELETE;

	/** 对帐操作相关 **/
	public final static String ADD_REC_DETAIL = "/rec/detail" + ADD;
	public final static String MERGE_REC_DETAIL = "/rec/detail" + MERGE;

	/** 对账单相关 **/
	public final static String ADD_ACCOUNT_STATEMENT_TITLE = "/accountStatementTitle" + ADD;
	public final static String EDIT_ACCOUNT_STATEMENT_TITLE = "/accountStatementTitle" + EDIT;
	public final static String DETAIL_ACCOUNT_STATEMENT_TITLE = "/accountStatementTitle" + DETAIL;
	public final static String SUBMIT_ACCOUNT_STATEMENT_TITLE = "/accountStatementTitle" + SUBMIT;
	public final static String DELETE_ACCOUNT_STATEMENT_TITLE = "/accountStatementTitle" + DELETE;
	public final static String UPDATE_ACCOUNT_STATEMENT_TITLE = "/accountStatementTitle" + UPDATE;
	public final static String QUERY_ACCOUNT_STATEMENT_TITLE = "/accountStatementTitle" + QUERY;

	/** 水单相关 **/
	public final static String ADD_BANK_RECEIPT = "/bankReceipt" + ADD;
	public final static String DELETE_BANK_RECEIPT = "/bankReceipt" + DELETE;
	public final static String QUERY_BANK_RECEIPT = "/bankReceipt" + QUERY;
	public final static String UPDATE_BANK_RECEIPT = "/bankReceipt" + UPDATE;
	public final static String DETAIL_BANK_RECEIPT = "/bankReceipt" + DETAIL;
	public final static String SUBMIT_BANK_RECEIPT = "/bankReceipt" + SUBMIT;
	public final static String OVER_BANK_RECEIPT = "/bankReceipt" + OVER;
	public final static String EDIT_BANK_RECEIPT = "/bankReceipt" + EDIT;
	public final static String CHECK_BANK_RECEIPT = "/bankReceipt" + CHECK;
	public final static String REJECT_BANK_RECEIPT = "/bankReceipt" + REJECT;
	public final static String EDIT_CHECK_BANK_RECEIPT = "/bankReceipt/check" + EDIT;
	public final static String EXPORT_BANK_RECEIPT = "/bankReceipt/excel/collect.xls";// 导出
	public final static String DETAIL_BANK_RECEIPT_VOUCHER = "/bankReceipt/voucher" + DETAIL;// 水单凭证信息
	public final static String EXPORT_BANK_RECEIPT_COUNT = "/bankReceipt/excel/export/count";
	public final static String UPLOAD_BANK_RECEIPT = "/bankReceiptFile" + UPLOAD;
	public final static String QUERY_FILE_BANK_RECEIPT = "/bankReceiptFile" + QUERY;
	public final static String DELETE_FILE_BANK_RECEIPT = "/bankReceiptFile" + DELETE;
	public final static String DOWNLOAD_BANK_RECEIPT = "/bankReceiptFile" + DOWNLOAD;
	public final static String DOWNLOAD_LIST_BANK_RECEIPT = "/bankReceiptFileList" + DOWNLOAD;

	/** 水单转预收 **/
	public final static String ADD_ADVANCE = "/advance" + ADD;
	public final static String QUERY_ADVANCE = "/advance" + QUERY;
	public final static String DELETE_ALL_ADVANCE = "/advance" + DELETE_ALL;

	/** 水单核销应收 **/
	public final static String ADD_RECRECEIPTREL = "/recReceiptRel" + ADD;
	public final static String UPDATE_RECRECEIPTREL = "/recReceiptRel" + UPDATE;
	public final static String QUERY_RECRECEIPTREL = "/recReceiptRel" + QUERY;
	public final static String DELETE_ALL_RECRECEIPTREL = "/recReceiptRel" + DELETE_ALL;
	public final static String EDIT_RECRECEIPTREL = "/recReceiptRel" + EDIT;
	public final static String DIVID_RECRECEIPTREL = "/recReceiptRel" + DIVID;
	
	/**水单核销应付**/
	public final static String QUERY_COPE_RECEIPTREL = "/copeReceiptRel" + QUERY;
	public final static String ADD_COPE_RECEIPTREL = "/copeReceiptRel" + ADD;
	public final static String DIVID_COPE_RECEIPTREL = "/copeReceiptRel" + DIVID;
	public final static String DELETE_COPE_RECEIPTREL = "/copeReceiptRel" + DELETE;
	
	/**水单转预付明细**/
	public final static String QUERY_PREPAID_RECEIPTREL = "/prepaidReceiptRel" + QUERY;
	public final static String EDIT_PREPAID_RECEIPTREL = "/prepaidReceiptRel" + EDIT;
	public final static String ADD_PREPAID_RECEIPTREL = "/prepaidReceiptRel" + ADD;
	public final static String DELETE_PREPAID_RECEIPTREL = "/prepaidReceiptRel" + DELETE;

	/** 水单核销出库单 供应商退款类型 **/
	public final static String QUERY_RECRECEIPTOUTREL = "/recReceiptOutRel" + QUERY;
	public final static String DIVID_RECRECEIPTOUTREL = "/recReceiptOutRel" + DIVID;
	public final static String ADD_RECRECEIPTOUTREL = "/recReceiptOutRel" + ADD;
	public final static String DELETE_ALL_RECRECEIPTOUTREL = "/recReceiptOutRel" + DELETE_ALL;

	/** 预收管理 **/
	public final static String QUERY_ADVANCE_MANAGER = "/advance/manager" + QUERY;
	public final static String ADD_ADVANCE_MANAGER = "/advance/manager" + ADD;
	public final static String EDIT_ADVANCE_MANAGER = "/advance/manager" + EDIT;

	/** 物流相关URL */
	public final static String QUERY_BILL_IN_STORE = "/billInStore" + QUERY;
	public final static String ADD_BILL_IN_STORE = "/billInStore" + ADD;
	public final static String UPDATE_BILL_IN_STORE = "/billInStore" + UPDATE;
	public final static String EDIT_BILL_IN_STORE = "/billInStore" + EDIT;
	public final static String DETAIL_BILL_IN_STORE = "/billInStore" + DETAIL;
	public final static String SUBMIT_BILL_IN_STORE = "/billInStore" + SUBMIT;
	public final static String DELETE_BILL_IN_STORE = "/billInStore" + DELETE;
	public final static String QUERY_PO_DTL_BILL_IN_STORE = "/billInStore/po/dtl" + QUERY;
	public final static String INVOKE_WMS_STOCK_RECEIVE = "/billInStore/invoke/wmsStockReceive";
	public final static String EXPORT_BILL_IN_STORE_COUNT = "/billInStore/excel/billInStore/count";
	public final static String EXPORT_BILL_IN_STORE = "/billInStore/excel/billInStore.xls";
	public final static String EXPORT_BILL_IN_STORE_DTL_COUNT = "/billInStore/excel/billInStoreDtl/count";
	public final static String EXPORT_BILL_IN_STORE_DTL = "/billInStore/excel/billInStoreDtl.xls";
	public final static String EXPORT_BILL_IN_STORE_TALLY_DTL_COUNT = "/billInStore/excel/billInStoreTallyDtl/count";
	public final static String EXPORT_BILL_IN_STORE_TALLY_DTL = "/billInStore/excel/billInStoreTallyDtl.xls";
	public final static String EXPORT_BILL_IN_STORE_TALLY_DTL_EXPORT = "/billInStore/excel/billInStoreTallyDtlExport.xls";
	public final static String PRINT_BILL_IN_STORE = "/billInStore" + PRINT;
	public final static String NOT_SHOW_AMOUNT_BILL_IN_STORE = "/billInStore/notShowAmount";
	public final static String UPLOAD_BILL_IN_STOR = "/billInStoreFile" + UPLOAD;
	public final static String QUERY_FILE_BILL_IN_STOR = "/billInStoreFile" + QUERY;
	public final static String DELETE_FILE_BILL_IN_STOR = "/billInStoreFile" + DELETE;
	public final static String DOWNLOAD_BILL_IN_STOR = "/billInStoreFile" + DOWNLOAD;
	public final static String DOWNLOAD_LIST_BILL_IN_STOR = "/billInStoreFileList" + DOWNLOAD;
	public final static String IMPORT_BILL_IN_STORE_TALLY_DTL = "/billInStore/excel/billInStoreTallyDtl" + IMPORT; // 入库单理货明细导入
	public final static String DETAIL_BILL_IN_STORE_VOUCHER = "/billInStore/voucher" + DETAIL;// 入库单凭证浏览
	public final static String REJECT_BILL_IN_STORE = "/billInStore/reject"; // 驳回

	public final static String QUERY_BILL_IN_STORE_DTL = "/billInStoreDtl" + QUERY;
	public final static String ADD_BILL_IN_STORE_DTL = "/billInStoreDtl" + ADD;
	public final static String UPDATE_BILL_IN_STORE_DTL = "/billInStoreDtl" + UPDATE;
	public final static String EDIT_BILL_IN_STORE_DTL = "/billInStoreDtl" + EDIT;
	public final static String DETAIL_BILL_IN_STORE_DTL = "/billInStoreDtl" + DETAIL;
	public final static String DELETE_BILL_IN_STORE_DTL = "/billInStoreDtl" + DELETE;
	public final static String BATCH_DELETE_BILL_IN_STORE_DTL = "/billInStoreDtl/batch" + DELETE;
	public final static String BATCH_DETAIL_BILL_IN_STORE_DTL = "/billInStoreDtl/batch" + DETAIL;
	public final static String QUERY_BILL_IN_STORE_DTL_FOR_TALLY = "/billInStoreDtl/tally" + QUERY;
	public final static String QUERY_ALL_BILL_IN_STORE_DTL = "/billInStoreDtl/all" + QUERY;

	public final static String ADD_BILL_IN_STORE_TALLY_DTL = "/billInStoreTallyDtl" + ADD;
	public final static String DELETE_BILL_IN_STORE_TALLY_DTL = "/billInStoreTallyDtl" + DELETE;
	public final static String BATCH_DELETE_BILL_IN_STORE_TALLY_DTL = "/billInStoreTallyDtl/batch" + DELETE;
	public final static String AUTO_TALLY_BILL_IN_STORE_TALLY_DTL = "/billInStoreTallyDtl/autoTally";

	public final static String QUERY_BILL_OUT_STORE = "/billOutStore" + QUERY;
	public final static String ADD_BILL_OUT_STORE = "/billOutStore" + ADD;
	public final static String BORROW_ADD_BILL_OUT_STORE = "/billOutStore/borrow" + ADD;
	public final static String RETURN_ADD_BILL_OUT_STORE = "/billOutStore/return" + ADD;
	public final static String UPDATE_BILL_OUT_STORE = "/billOutStore" + UPDATE;
	public final static String EDIT_BILL_OUT_STORE = "/billOutStore" + EDIT;
	public final static String DETAIL_BILL_OUT_STORE = "/billOutStore" + DETAIL;
	public final static String DETAIL_BILL_OUT_STORE_BYNO = "/billOutStore/detail/byNo";
	public final static String SUBMIT_BILL_OUT_STORE = "/billOutStore" + SUBMIT;
	public final static String DELETE_BILL_OUT_STORE = "/billOutStore" + DELETE;
	public final static String REJECT_BILL_OUT_STORE = "/billOutStore/reject";
	public final static String SEND_BILL_OUT_STORE = "/billOutStore/send";
	public final static String BATCH_SEND_BILL_OUT_STORE = "/billOutStore/batch/send";
	public final static String EXPORT_BILL_OUT_STORE_COUNT = "/billOutStore/excel/billOutStore/count";
	public final static String EXPORT_BILL_OUT_STORE = "/billOutStore/excel/billOutStore.xls";
	public final static String EXPORT_BILL_OUT_STORE_DTL_COUNT = "/billOutStore/excel/billOutStoreDtl/count";
	public final static String EXPORT_BILL_OUT_STORE_DTL = "/billOutStore/excel/billOutStoreDtl.xls";
	public final static String EXPORT_BILL_OUT_STORE_PICK_DTL_COUNT = "/billOutStore/excel/billOutStorePickDtl/count";
	public final static String EXPORT_BILL_OUT_STORE_PICK_DTL = "/billOutStore/excel/billOutStorePickDtl.xls";
	public final static String PRINT_BILL_OUT_STORE = "/billOutStore" + PRINT;
	public final static String NOT_SHOW_AMOUNT_BILL_OUT_STORE = "/billOutStore/notShowAmount";
	public final static String UPLOAD_BILL_OUT_STOR = "/billOutStoreFile" + UPLOAD;
	public final static String QUERY_FILE_BILL_OUT_STOR = "/billOutStoreFile" + QUERY;
	public final static String DELETE_FILE_BILL_OUT_STOR = "/billOutStoreFile" + DELETE;
	public final static String DOWNLOAD_BILL_OUT_STOR = "/billOutStoreFile" + DOWNLOAD;
	public final static String DOWNLOAD_LIST_BILL_OUT_STOR = "/billOutStoreFileList" + DOWNLOAD;
	public final static String PRINT_BILL_OUT_STORE_PICK = "/billOutStore/pick" + PRINT;
	public final static String DETAIL_BILL_OUT_STORE_VOUCHER = "/billOutStore/voucher" + DETAIL;// 出库单凭证浏览
	public final static String DETAIL_BILL_OUT_STORE_BATCH_PRINT= "/billOutStore/batch" + PRINT;//出库单打印头信息

	public final static String QUERY_BILL_OUT_STORE_DTL = "/billOutStoreDtl" + QUERY;
	public final static String ADD_BILL_OUT_STORE_DTL = "/billOutStoreDtl" + ADD;
	public final static String UPDATE_BILL_OUT_STORE_DTL = "/billOutStoreDtl" + UPDATE;
	public final static String EDIT_BILL_OUT_STORE_DTL = "/billOutStoreDtl" + EDIT;
	public final static String DETAIL_BILL_OUT_STORE_DTL = "/billOutStoreDtl" + DETAIL;
	public final static String BATCH_DELETE_BILL_OUT_STORE_DTL = "/billOutStoreDtl/batch" + DELETE;
	public final static String ADD_BILL_OUT_STORE_DTL_BY_STL = "/billOutStoreDtl/stl" + ADD;
	public final static String QUERY_BILL_OUT_STORE_DTL_FOR_PICK = "/billOutStoreDtl/pick" + QUERY;
	public final static String AVAILABLE_BILL_OUT_STORE_DTL = "/billOutStoreDtl/available" + QUERY;
	public final static String QUERY_ALL_BILL_OUT_STORE_DTL = "/billOutStoreDtl/all" + QUERY;
	public final static String BATCH_PRINT_BILL_OUT_STORE_DTL= "/billOutStoreDtl/batch" + PRINT;


	public final static String QUERY_ALL_BILL_OUT_STORE_PICK_DTL = "/billOutStorePickDtl/all" + QUERY;
	public final static String ADD_BILL_OUT_STORE_PICK_DTL = "/billOutStorePickDtl" + ADD;
	public final static String DELETE_BILL_OUT_STORE_PICK_DTL = "/billOutStorePickDtl" + DELETE;
	public final static String BATCH_DELETE_BILL_OUT_STORE_PICK_DTL = "/billOutStorePickDtl/batch" + DELETE;
	public final static String AUTO_PICK_BILL_OUT_STORE_PICK_DTL = "/billOutStorePickDtl/autoPick";

	public final static String QUERY_STL = "/stl" + QUERY;
	public final static String QUERY_STL_SUMMARY = "/stl/summary" + QUERY;
	public final static String QUERY_AVAILABLE_STL = "/stl/available" + QUERY;
	public final static String QUERY_GOODSID_AVAILABLE_STL = "/stl/available/goodsId" + QUERY;
	public final static String EXPORT_STL_COUNT = "/stl/excel/stl/count";
	public final static String EXPORT_STL = "/stl/excel/stl.xls";
	public final static String EXPORT_STL_SUMMARY_COUNT = "/stl/summary/excel/stlSummary/count";
	public final static String EXPORT_STL_SUMMARY = "/stl/summary/excel/stlSummary.xls";
	public final static String NOT_SHOW_AMOUNT_STL = "/stl/notShowAmount";
	public final static String EDIT_STL = "/stl" + EDIT;
	public final static String UPDATE_STL = "/stl" + UPDATE;
	public final static String SPLIT_STL = "/stl" + SPLIT;
	public final static String SPLIT_UPDATE_STL = "/stl/split/update";

	public final static String QUERY_STL_HISTORY = "/stlHistory" + QUERY;
	public final static String QUERY_STL_HISTORY_SUMMARY = "/stlHistory/summary" + QUERY;
	public final static String EXPORT_STL_HISTORY_COUNT = "/stlHistory/excel/stlHistory/count";
	public final static String EXPORT_STL_HISTORY = "/stlHistory/excel/stlHistory.xls";
	public final static String EXPORT_STL_HISTORY_SUMMARY_COUNT = "/stlHistory/summary/excel/stlHistorySummary/count";
	public final static String EXPORT_STL_HISTORY_SUMMARY = "/stlHistory/summary/excel/stlHistorySummary.xls";
	public final static String NOT_SHOW_AMOUNT_STL_HISTORY = "/stlHistory/notShowAmount";

	/** 销售相关URL */
	public final static String QUERY_BILL_DELIVERY = "/billDelivery" + QUERY;
	public final static String ADD_BILL_DELIVERY = "/billDelivery" + ADD;
	public final static String UPDATE_BILL_DELIVERY = "/billDelivery" + UPDATE;
	public final static String EDIT_BILL_DELIVERY = "/billDelivery" + EDIT;
	public final static String DETAIL_BILL_DELIVERY = "/billDelivery" + DETAIL;
	public final static String DELETE_BILL_DELIVERY = "/billDelivery" + DELETE;
	public final static String SUBMIT_BILL_DELIVERY = "/billDelivery" + SUBMIT;
	public final static String REJECT_BILL_DELIVERY = "/billDelivery/reject";
	public final static String EXPORT_BILL_DELIVERY_COUNT = "/billDelivery/excel/billDelivery/count";
	public final static String EXPORT_BILL_DELIVERY = "/billDelivery/excel/billDelivery.xls";
	public final static String EXPORT_BILL_DELIVERY_DTL_COUNT = "/billDelivery/excel/billDeliveryDtl/count";
	public final static String EXPORT_BILL_DELIVERY_DTL = "/billDelivery/excel/billDeliveryDtl.xls";
	public final static String PRINT_BILL_DELIVERY = "/billDelivery" + PRINT;
	public final static String PRINT_BILL_SCAN_DELIVERY = "/billDeliveryScan" + PRINT;// 销售合同打印
	public final static String NOT_SHOW_AMOUNT_BILL_DELIVERY = "/billDelivery/notShowAmount";
	public final static String UPLOAD_BILL_DELIVERY = "/billDeliveryFile" + UPLOAD;
	public final static String QUERY_FILE_BILL_DELIVERY = "/billDeliveryFile" + QUERY;
	public final static String DELETE_FILE_BILL_DELIVERY = "/billDeliveryFile" + DELETE;
	public final static String DOWNLOAD_BILL_DELIVERY = "/billDeliveryFile" + DOWNLOAD;
	public final static String DOWNLOAD_LIST_BILL_DELIVERY = "/billDeliveryFileList" + DOWNLOAD;
	public final static String IMPORT_BILL_DELIVERY = "/billDelivery/excel" + IMPORT; // 销售单导入
	public final static String DOWNLOAD_BILL_DELIVERY_TEMPLATE = "/billDelivery/excel/billDeliveryTemplate.xls"; // 下载销售单导入模板
	public final static String DETAIL_BILL_DELIVERY_PRINT = "/billDeliveryPrint" + DETAIL;
	public final static String BILL_DEL_LIST_SCAN_PRINT = "/billDeliveryListScanPrint/list" + QUERY;//销售单合同合并打印
	public final static String BILL_DEL_LIST_SCAN_LINE_PRINT = "/billDeListScanPrintLine/list" + QUERY;//销售单明细合同合并打印
	public final static String BILL_DEL_INV_LIST_SCAN_PRINT = "/billDelInvoiceListScanPrint/list" + QUERY;//销售单发票合并打印
	public final static String BILL_DEL_INV_LIST_SCAN_LINE_PRINT = "/billDeInvListScanPrintLine/list" + QUERY;//销售单发票明细合同合并打印

	public final static String QUERY_BILL_DELIVERY_DTL = "/billDeliveryDtl" + QUERY;
	public final static String ADD_BILL_DELIVERY_DTL = "/billDeliveryDtl" + ADD;
	public final static String DELETE_BILL_DELIVERY_DTL = "/billDeliveryDtl" + DELETE;
	public final static String ADD_BILL_DELIVERY_DTL_BY_STL = "/billDeliveryDtl/stl" + ADD;
	public final static String UPDATE_BILL_DELIVERY_DTL = "/billDeliveryDtl" + UPDATE;
	public final static String DETAIL_BILL_DELIVERY_DTL = "/billDeliveryDtl" + DETAIL;
	public final static String EDIT_BILL_DELIVERY_DTL = "/billDeliveryDtl" + EDIT;
	public final static String QUERY_ALL_BILL_DELIVERY_DTL = "/billDeliveryDtl/all" + QUERY;
	public final static String IMPORT_BILL_DELIVERY_DTL = "/billDeliveryDtl/excel" + IMPORT; // 销售单明细导入
	public final static String DOWNLOAD_BILL_DELIVERY_DTL_TEMPLATE = "/billDeliveryDtl/excel/billDeliveryDtlTemplate.xls"; // 下载销售单明细导入模板
	public final static String QUERY_ALL_BILL_DELIVERY_DTL_PRINT = "/billDeliveryDtlPrint/all" + QUERY;

	/** 销售退货相关URL */
	public final static String QUERY_BILL_RETURN = "/billReturn" + QUERY;
	public final static String ADD_BILL_RETURN = "/billReturn" + ADD;
	public final static String UPDATE_BILL_RETURN = "/billReturn" + UPDATE;
	public final static String EDIT_BILL_RETURN = "/billReturn" + EDIT;
	public final static String DETAIL_BILL_RETURN = "/billReturn" + DETAIL;
	public final static String DELETE_BILL_RETURN = "/billReturn" + DELETE;
	public final static String SUBMIT_BILL_RETURN = "/billReturn" + SUBMIT;
	public final static String REJECT_BILL_RETURN = "/billReturn/reject";
	public final static String EXPORT_BILL_RETURN_COUNT = "/billReturn/excel/billReturn/count";
	public final static String EXPORT_BILL_RETURN = "/billReturn/excel/billReturn.xls";
	public final static String EXPORT_BILL_RETURN_DTL_COUNT = "/billReturn/excel/billReturnDtl/count";
	public final static String EXPORT_BILL_RETURN_DTL = "/billReturn/excel/billReturnDtl.xls";
	public final static String PRINT_BILL_RETURN = "/billReturn" + PRINT;
	public final static String PRINT_BILL_SCAN_RETURN = "/billReturnScan" + PRINT;// 销售退货合同打印
	public final static String DETAIL_BILL_RETURN_PRINT = "/billReturnPrint/detail";
	public final static String UPLOAD_BILL_RETURN = "/billReturnFile" + UPLOAD;
	public final static String QUERY_FILE_BILL_RETURN = "/billReturnFile" + QUERY;
	public final static String DELETE_FILE_BILL_RETURN = "/billReturnFile" + DELETE;
	public final static String DOWNLOAD_BILL_RETURN = "/billReturnFile" + DOWNLOAD;
	public final static String DOWNLOAD_LIST_BILL_RETURN = "/billReturnFileList" + DOWNLOAD;
	public final static String IMPORT_BILL_RETURN = "/billReturn/excel" + IMPORT; // 销售退货单导入
	public final static String DOWNLOAD_BILL_RETURN_TEMPLATE = "/billReturn/excel/billReturnTemplate.xls"; // 下载销售退货单导入模板
	public final static String QUERY_BILL_RETURN_DTL_BY_BILL_OUT_STORE = "/billReturnDtl/billOutStore" + QUERY;

	public final static String QUERY_BILL_RETURN_DTL = "/billReturnDtl" + QUERY;
	public final static String DELETE_BILL_RETURN_DTL = "/billReturnDtl" + DELETE;
	public final static String ADD_BILL_RETURN_DTL_BY_BILL_OUT_STORE = "/billReturnDtl/billOutStore" + ADD;
	public final static String UPDATE_BILL_RETURN_DTL = "/billReturnDtl" + UPDATE;
	public final static String DETAIL_BILL_RETURN_DTL = "/billReturnDtl" + DETAIL;
	public final static String EDIT_BILL_RETURN_DTL = "/billReturnDtl" + EDIT;
	public final static String QUERY_ALL_BILL_RETURN_DTL = "/billReturnDtl/all" + QUERY;
	public final static String IMPORT_BILL_RETURN_DTL = "/billReturnDtl/excel" + IMPORT; // 销售退货单明细导入
	public final static String DOWNLOAD_BILL_RETURN_DTL_TEMPLATE = "/billReturnDtl/excel/billReturnDtlTemplate.xls"; // 下载销售退货单明细导入模板

	/** 付款模块 **/
	public final static String QUERY_PAY_ORDER = "/payOrder" + QUERY;
	public final static String ADD_PAY_ORDER = "/payOrder" + ADD;
	public final static String EDIT_PAY_ORDER = "/payOrder" + EDIT;
	public final static String DETAIL_PAY_ORDER = "/payOrder" + DETAIL;
	public final static String UPDATE_PAY_ORDER = "/payOrder" + UPDATE;
	public final static String DELETE_PAY_ORDER = "/payOrder" + DELETE;
	public final static String SUBMIT_PAY_ORDER = "/payOrder" + SUBMIT;
	public final static String OVER_PAY_ORDER = "/payOrder" + OVER;
	public final static String BATCH_OVER_PAY_ORDER = "/payOrder/batch" + OVER;
	public final static String QUERY_PAY_ORDER_WRITE = "/payOrder/write" + QUERY;
	public final static String EXPORT_PAY_ORDER_ENSURE = "/payOrder/write/payEnsure.xls";
	public final static String IMPORT_PAY_ENSURE = "/payEnsure" + IMPORT;
	public final static String REJECT_PAY_ORDER = "/payOrder" + REJECT;// 确认付款新增驳回功能
	public final static String BATCH_SUBMIT_PAY_ORDER = "/payOrder/batch" + SUBMIT;// 批量提交付款
	public final static String REFRESH_PAY_ORDER = "/payOrder/refresh";// 批量提交付款
	public final static String CONFIG_SUBMIT_PAY_ORDER = "/payOrder/config" + SUBMIT;
	public final static String REJECT_UPDATE_PAY_ORDER = "/payOrder/reject" + UPDATE;
	public final static String QUERY_OVER_PAY_ORDER = "/payOrder/over" + QUERY;
	public final static String UPDATE_MEMO_PAY_ORDER = "/payOrder/memo" + UPDATE;
	public final static String CHECK_PAY_ORDER = "/payOrder/check";	// 核销

	public final static String QUERY_PAY_ORDER_OPEN = "/payOrder/open" + QUERY;
	public final static String OVER_PAY_ORDER_OPEN = "/payOrder/open" + OVER;
	public final static String BATCH_OVER_PAY_ORDER_OPEN = "/payOrder/batch/open" + OVER;
	public final static String PRINT_PAY_ORDER_OPEN = "/payOrder/print";// 打印
	public final static String UPLOAD_PAY_ORDER = "/payOrderFile" + UPLOAD;
	public final static String QUERY_FILE_PAY_ORDER = "/payOrderFile" + QUERY;
	public final static String DELETE_FILE_PAY_ORDER = "/payOrderFile" + DELETE;
	public final static String DOWNLOAD_PAY_ORDER = "/payOrderFile" + DOWNLOAD;
	public final static String DOWNLOAD_LIST_PAY_ORDER = "/payOrderFileList" + DOWNLOAD;
	public final static String EXPORT_PAY_ORDER = "/payOrder/excel/collectApprove.xls";// 导出
	public final static String QUERY_DEFAULT_REAL_PAY_AMOUNT = "/payOrder/defaultRealPayAmount" + QUERY;// 计算默认实际付款金额
	public final static String QUERY_BATCH_CONFIRM_PAY_ORDER = "/payOrder/batchConfirm" + QUERY;// 计算默认实际付款金额
	public final static String PRE_BATCH_PRINT_PAY_ORDER = "/payOrder/preBatch" + PRINT;// 批量预打印
	public final static String QUERY_BATCH_PRINT_PAY_ORDER = "/payOrder/batchPrint" + QUERY;// 查询批量打印付款单
	public final static String DETAIL_PAY_ORDER_VOUCHER = "/payOrder/voucher" + DETAIL;
	public final static String EXPORT_PAY_ORDER_COUNT = "/payOrder/excel/export/count";

	public final static String QUERY_PAY_PORELA = "/payPoRela" + QUERY;
	public final static String DIVID_PAY_PORELA = "/payPoRela" + DIVID;
	public final static String ADD_PAY_PORELA = "/payPoRela" + ADD;
	public final static String EDIT_PAY_PORELA = "/payPoRela" + EDIT;
	public final static String UPDATE_PAY_PORELA = "/payPoRela" + UPDATE;
	public final static String DELETE_ALL_PAY_PORELA = "/payPoRela" + DELETE_ALL;

	public final static String QUERY_PAY_FEERELA = "/payFeeRela" + QUERY;
	public final static String DIVID_PAY_FEERELA = "/payFeeRela" + DIVID;
	public final static String ADD_PAY_FEERELA = "/payFeeRela" + ADD;
	public final static String EDIT_PAY_FEERELA = "/payFeeRela" + EDIT;
	public final static String UPDATE_PAY_FEERELA = "/payFeeRela" + UPDATE;
	public final static String DELETE_ALL_PAY_FEERELA = "/payFeeRela" + DELETE_ALL;

	public final static String QUERY_PAY_DEDUCTION_FEERELA = "/payDeductionFeeRela" + QUERY;
	public final static String DIVID_PAY_DEDUCTION_FEERELA = "/payDeductionFeeRela" + DIVID;
	public final static String ADD_PAY_DEDUCTION_FEERELA = "/payDeductionFeeRela" + ADD;
	public final static String EDIT_PAY_DEDUCTION_FEERELA = "/payDeductionFeeRela" + EDIT;
	public final static String UPDATE_PAY_DEDUCTION_FEERELA = "/payDeductionFeeRela" + UPDATE;
	public final static String DELETE_ALL_PAY_DEDUCTION_FEERELA = "/payDeductionFeeRela" + DELETE_ALL;

	public final static String QUERY_PAY_ADVANRELA = "/payAdvanRela" + QUERY;
	public final static String DIVID_PAY_ADVANRELA = "/payAdvanRela" + DIVID;
	public final static String ADD_PAY_ADVANRELA = "/payAdvanRela" + ADD;
	public final static String EDIT_PAY_ADVANRELA = "/payAdvanRela" + EDIT;
	public final static String UPDATE_PAY_ADVANRELA = "/payAdvanRela" + UPDATE;
	public final static String DELETE_ALL_PAY_ADVANRELA = "/payAdvanRela" + DELETE_ALL;

	public final static String QUERY_PAY_KEY_WORD = "/payKeyWord" + QUERY;
	public final static String ADD_PAY_KEY_WORD = "/payKeyWord" + ADD;
	public final static String EDIT_PAY_KEY_WORD = "/payKeyWord" + EDIT;
	public final static String DETAIL_PAY_KEY_WORD = "/payKeyWord" + DETAIL;
	public final static String UPDATE_PAY_KEY_WORD = "/payKeyWord" + UPDATE;
	public final static String DELETE_PAY_KEY_WORD = "/payKeyWord" + DELETE;

	public final static String QUERY_PAY_REFUNDRELA = "/payRefundRela" + QUERY;
	public final static String DIVID_PAY_REFUNDRELA = "/payRefundRela" + DIVID;
	public final static String DELETE_PAY_REFUNDRELA = "/payRefundRela" + DELETE;
	public final static String ADD_PAY_REFUNDRELA = "/payRefundRela" + ADD;
	public final static String EDIT_PAY_REFUNDRELA = "/payRefundRela" + EDIT;
	public final static String UPDATE_PAY_REFUNDRELA = "/payRefundRela" + UPDATE;

	/** 合并付款 **/
	public final static String QUERY_MERGE_PAY_ORDER = "/mergePayOrder" + QUERY;
	public final static String ADD_MERGE_PAY_ORDER = "/mergePayOrder" + ADD;
	public final static String EDIT_MERGE_PAY_ORDER = "/mergePayOrder" + EDIT;
	public final static String DETAIL_MERGE_PAY_ORDER = "/mergePayOrder" + DETAIL;
	public final static String UPDATE_MERGE_PAY_ORDER = "/mergePayOrder" + UPDATE;
	public final static String DELETE_MERGE_PAY_ORDER = "/mergePayOrder" + DELETE;
	public final static String SUBMIT_MERGE_PAY_ORDER = "/mergePayOrder" + SUBMIT;
	public final static String BATCH_SUBMIT_MERGE_PAY_ORDER = "/mergePayOrder/batch" + SUBMIT;
	public final static String CONFIG_SUBMIT_MERGE_PAY_ORDER = "/mergePayOrder/config" + SUBMIT;
	public final static String REJECT_UPDATE_MERGE_PAY_ORDER = "/mergePayOrder/reject" + UPDATE;
	public final static String DIVIDE_MERGE_PAY_ORDER_ALL = "/mergePayOrder/all" + DIVID;
	public final static String DIVIDE_MERGE_PAY_ORDER = "/mergePayOrder" + DIVID;
	public final static String QUERY_MERGE_PAY_ORDER_PRINT = "/mergePayOrder/print" + QUERY;
	public final static String PRINT_MERGE_PAY_ORDER = "/mergePayOrder" + PRINT;// 合并付款预打印

	public final static String DETAIL_MERGE_PAY_ORDER_REL = "/mergePayOrderRel" + DETAIL;
	public final static String EDIT_MERGE_PAY_ORDER_REL = "/mergePayOrderRel" + EDIT;
	public final static String DELETE_MERGE_PAY_ORDER_REL = "/mergePayOrderRel" + DELETE;
	public final static String ADD_MERGE_PAY_ORDER_REL = "/mergePayOrderRel" + ADD;

	/** 费用模块 **/
	public final static String ADDRECFEE = "/fee/receive" + ADD;
	public final static String ADDPAYFEE = "/fee/pay" + ADD;
	public final static String ADDRECPAYFEE = "/fee/receive/pay" + ADD;

	public final static String DETAILRECFEE = "/fee/receive" + DETAIL;
	public final static String DETAILPAYFEE = "/fee/pay" + DETAIL;
	public final static String DETAILRECPAYFEE = "/fee/receive/pay" + DETAIL;

	public final static String EDITRECFEE = "/fee/receive" + EDIT;
	public final static String EDITPAYFEE = "/fee/pay" + EDIT;
	public final static String EDITRECPAYFEE = "/fee/receive/pay" + EDIT;

	public final static String UPDATERECFEE = "/fee/receive" + UPDATE;
	public final static String UPDATEPAYFEE = "/fee/pay" + UPDATE;
	public final static String UPDATERECPAYFEE = "/fee/receive/pay" + UPDATE;

	public final static String DELETERECFEE = "/fee/receive" + DELETE;
	public final static String DELETEPAYFEE = "/fee/pay" + DELETE;
	public final static String DELETERECPAYFEE = "/fee/receive/pay" + DELETE;

	public final static String SUBMITRECFEE = "/fee/receive" + SUBMIT;
	public final static String SUBMITPAYFEE = "/fee/pay" + SUBMIT;
	public final static String SUBMITRECPAYFEE = "/fee/receive/pay" + SUBMIT;

	public final static String PRINTRECFEE = "/fee/receive" + PRINT;
	public final static String PRINTPAYFEE = "/fee/pay" + PRINT;
	public final static String PRINTRECPAYFEE = "/fee/receive/pay" + PRINT;

	public final static String QUERYRECFEE = "/fee/receive" + QUERY;
	public final static String QUERYPAYFEE = "/fee/pay" + QUERY;
	public final static String QUERYRECPAYFEE = "/fee/receive/pay" + QUERY;

	public final static String DETAILFEEBYNO = "/fee/detail/byNo";

	public final static String EXPORTRECFEE = "/fee/excel/recFee.xls";
	public final static String EXPORTPAYFEE = "/fee/excel/payFee.xls";
	public final static String EXPORTRECPAYFEE = "/fee/excel/recPayFee.xls";
	public final static String EXPORTPAYFEECOUNT = "/fee/excel/payFeeCount";
	public final static String EXPORTRECFEECOUNT = "/fee/excel/recFeeCount";
	public final static String EXPORTRECPAYFEECOUNT = "/fee/excel/recPayFeeCount";
	public final static String DETAIL_FEE_VOUCHER = "/fee/voucher" + DETAIL;// 费用凭证浏览

	public final static String QUERYFEESPEC = "/fee/spec" + QUERY;
	public final static String ADDFEESPEC = "/fee/spec" + ADD;
	public final static String UPDATEFEESPEC = "/fee/spec" + UPDATE;
	public final static String EDITFEESPEC = "/fee/spec" + EDIT;
	public final static String DETAILFEESPEC = "/fee/spec" + DETAIL;
	public final static String DELETEFEESPEC = "/fee/spec" + DELETE;
	//应收应付关系
	public final static String QUERY_FEE_SPEC_PAY = "/fee/spec/pay" + QUERY;
	public final static String DIVID_FEE_SPEC_PAY = "/fee/spec/pay" + DIVID;
	public final static String ADD_FEE_SPEC_PAY = "/fee/spec/pay" + ADD;
	public final static String DELETE_ALL_FEE_SPEC_PAY = "/fee/spec/pay" + DELETE_ALL;
	
	// 费用管理
	public final static String QUERY_FEE_MANAGE = "/fee/manage" + QUERY;
	public final static String QUERY_FEE_MANAGE_POWER = "/fee/manage/power" + QUERY;
	public final static String ADD_FEE_MANAGE = "/fee/manage" + ADD;
	public final static String UPDATE_FEE_MANAGE = "/fee/manage" + UPDATE;
	public final static String DETAIL_FEE_MANAGE = "/fee/manage" + DETAIL;
	public final static String EDIT_FEE_MANAGE = "/fee/manage" + EDIT;
	public final static String DELETE_FEE_MANAGE = "/fee/manage" + DELETE;
	public final static String SUBMIT_FEE_MANAGE = "/fee/manage" + SUBMIT;
	public final static String SUBMIT_FEE_MANAGE_ALL = "/fee/manage" + SUBMIT_ALL;
	public final static String EXPORT_FEE_MANAGE = "/fee/excel/feeManage.xls";// 导出
	public final static String EXPORT_FEE_MANAGE_COUNT = "/feeManage/excel/export/count";
	public final static String UPLOAD_FEE_MANAGE = "/feeManageFile" + UPLOAD; // 附件
	public final static String QUERY_FILE_FEE_MANAGE = "/feeManageFile" + QUERY;
	public final static String DELETE_FILE_FEE_MANAGE = "/feeManageFile" + DELETE;
	public final static String DOWNLOAD_FEE_MANAGE = "/feeManageFile" + DOWNLOAD;
	public final static String DOWNLOAD_LIST_FEE_MANAGE = "/feeManageFileList" + DOWNLOAD;
	public final static String ADD_MANAMGE_FEE_ARTIFICIAL = "/fee/manage/addShare";//管理费批量手动分摊

	// 费用分摊
	public final static String QUERY_FEE_SHARE = "/fee/share" + QUERY;
	public final static String ADD_FEE_SHARE = "/fee/share" + ADD;
	public final static String DELETE_FEE_SHARE = "/fee/share" + DELETE;

	// 人工费用
	public final static String QUERY_FEE_ARTIFICIAL = "/fee/artificial" + QUERY;
	public final static String ADD_FEE_ARTIFICIAL = "/fee/artificial" + ADD;
	public final static String UPDATE_FEE_ARTIFICIAL = "/fee/artificial" + UPDATE;
	public final static String DETAIL_FEE_ARTIFICIAL = "/fee/artificial" + DETAIL;
	public final static String EDIT_FEE_ARTIFICIAL = "/fee/artificial" + EDIT;
	public final static String DELETE_FEE_ARTIFICIAL = "/fee/artificial" + DELETE;
	public final static String SUBMIT_FEE_ARTIFICIAL = "/fee/artificial" + SUBMIT;
	public final static String SUBMIT_FEE_ARTIFICIAL_ALL = "/fee/artificial" + SUBMIT_ALL;
	public final static String EXPORT_FEE_ARTIFICIAL = "/fee/excel/artificial.xls";// 导出
	public final static String EXPORT_FEE_ARTIFICIAL_COUNT = "/artificial/excel/export/count";
	public final static String QUERY_MANUAL_FEE_MANAGE_POWER = "/fee/manual/manage/power" + QUERY;
    public final static String ADD_SHARE_FEE_ARTIFICIAL = "/fee/artificial/addShare";//人工费批量手动分摊
    
    
	// 费用导入相关
	public final static String EXPORT_FEE_RECEIVE = "/fee/receive/feeReceiveExport.xls";// 应收下载
	public final static String IMPORT_FEE_RECEIVE = "/fee/receive" + IMPORT;// 应收导入
	public final static String EXPORT_PAY_FEE_RECEIVE = "/fee/pay/feePayExport.xls";// 应付下载
	public final static String IMPORT_PAY_FEE_RECEIVE = "/fee/pay" + IMPORT;// 应付导入
	public final static String EXPORT_REC_PAY_FEE_RECEIVE = "/fee/receive/pay/feePayExport.xls";// 应收应付下载
	public final static String IMPORT_REC_PAY_FEE_RECEIVE = "/fee/receive/pay" + IMPORT;// 应收应付应付导入Fee
	public final static String EXPORT_FEE_SPEC = "/fee/spec/feeSpecExport.xls";// 费用科目下载
	public final static String IMPORT_FEE_SPEC = "/fee/spec" + IMPORT;// 费用科目导入
	public final static String EXPORT_FEE_FILE_MANAGE = "/fee/manage/feeManageExport.xls";// 费用管理下载
	public final static String IMPORT_FEE_MANAGE = "/fee/manage" + IMPORT;// 费用管理导入
	public final static String EXPORT_FEE_FILE_ARTIFICIAL = "/fee/artificial/artificialExport.xls";// 人工费用下载
	public final static String IMPORT_FEE_ARTIFICIAL = "/fee/artificial" + IMPORT;// 人工费用导入
	

	/** 出口报关 **/
	public final static String QUERY_CUSTOMS_APPLY = "/customsApply" + QUERY;
	public final static String ADD_CUSTOMS_APPLY = "/customsApply" + ADD;
	public final static String UPDATE_CUSTOMS_APPLY = "/customsApply" + UPDATE;
	public final static String EDIT_CUSTOMS_APPLY = "/customsApply" + EDIT;
	public final static String DETAIL_CUSTOMS_APPLY = "/customsApply" + DETAIL;
	public final static String DELETE_CUSTOMS_APPLY = "/customsApply" + DELETE;
	public final static String SUBMIT_CUSTOMS_APPLY = "/customsApply" + SUBMIT;
	public final static String PRINT_CUSTOMS_APPLY = "/customsApply" + PRINT;

	public final static String QUERY_CUSTOMS_APPLY_LINE = "/customsApplyLine" + QUERY;
	public final static String EDIT_CUSTOMS_APPLY_LINE = "/customsApplyLine" + EDIT;
	public final static String ADD_CUSTOMS_APPLY_LINE = "/customsApplyLine" + ADD;
	public final static String UPDATE_CUSTOMS_APPLY_LINE = "/customsApplyLine" + UPDATE;
	public final static String DELETE_CUSTOMS_APPLY_LINE = "/customsApplyLine" + DELETE;

	/** 出口退税 **/
	public final static String QUERY_REFUND_APPLY = "/refundApply" + QUERY;
	public final static String ADD_REFUND_APPLY = "/refundApply" + ADD;
	public final static String DELETE_REFUND_APPLY = "/refundApply" + DELETE;
	public final static String EDIT_REFUND_APPLY = "/refundApply" + EDIT;
	public final static String UPDATE_REFUND_APPLY = "/refundApply" + UPDATE;
	public final static String DETAIL_REFUND_APPLY = "/refundApply" + DETAIL;
	public final static String SUBMIT_REFUND_APPLY = "/refundApply" + SUBMIT;
	public final static String EXPORT_REFUND_APPLY = "/refundApply/excel/collect.xls";// 导出
	public final static String EXPORT_REFUND_APPLY_COUNT = "/refundApply/excel/export/count";
	public final static String PRINT_REFUND_APPLY = "/refundApply/print";// 打印

	public final static String QUERY_REFUND_APPLY_LINE = "/refundApplyLine" + QUERY;
	public final static String ADD_REFUND_APPLY_LINE = "/refundApplyLine" + ADD;
	public final static String DELETE_ALL_REFUND_APPLY_LINE = "/refundApplyLine" + DELETE_ALL;
	public final static String DIVID_REFUND_APPLY_LINE = "/refundApplyLine" + DIVID;

	/** PMS请款接口 **/
	public final static String QUERY_PMS_PAY_ORDER_TITLE = "/pmsPayOrderTitle" + QUERY;
	public final static String DETAIL_PMS_PAY_ORDER_DTL = "/pmsPayOrderDtl" + DETAIL;
	public final static String DEAL_PMS_PAY_ORDER = "/pmsPayOrder/deal";

	/** PMS铺货分配搜索 **/
	public final static String QUERY_PMS_DISTRIBUTION_SEARCH = "/pmsDistributionSearch" + QUERY;
	public final static String DETAIL_PMS_DISTRIBUTION_SEARCH = "/pmsDistributionSearch" + DETAIL;
	public final static String RESET_PMS_DISTRIBUTION_SEARCH = "/pmsDistributionSearch" + RESET;
	public final static String DETAIL_FAILURE_PMS_DISTRIBUTION_SEARCH = "/pmsDistributionSearch/failure" + DETAIL;
	public final static String QUERY_PMS_DISTRIBUTION_SUCCESS_SEARCH = "/pmsDistributionSuccessSearch" + QUERY;

	/** PMSS **/
	public final static String QUERY_PMSS_SUPPLIER_BIND = "/pmssSupplierBind" + QUERY;
	public final static String EDIT_PMSS_SUPPLIER_BIND = "/pmssSupplierBind" + EDIT;
	public final static String DETAIL_PMSS_SUPPLIER_BIND = "/pmssSupplierBind" + DETAIL;
	public final static String DELETE_PMSS_SUPPLIER_BIND = "/pmssSupplierBind" + DELETE;
	public final static String UPDATE_PMSS_SUPPLIER_BIND = "/pmssSupplierBind" + UPDATE;
	public final static String SUBMIT_PMSS_SUPPLIER_BIND = "/pmssSupplierBind" + SUBMIT;
	public final static String BIND_PMSS_SUPPLIER_BIND = "/pmssSupplierBind" + BIND;
	public final static String ADD_PMSS_SUPPLIER_BIND = "/pmssSupplierBind" + ADD;
	public final static String UNBIND_PMSS_SUPPLIER_BIND = "/pmssSupplierBind" + UNBIND;
	public final static String UPDATE_PMSS_SUPPLIER_BIND_PRO = "/pmssSupplierBindPro" + UPDATE;
	/** wechat **/
	public final static String USER_WECHAT_QUERY = "/user/wechat/bind/" + QUERY;
	public final static String USER_WECHAT_UNBIND_QUERY = "/user/wechat/unbind/" + QUERY;
	public final static String USER_WECHAT_BIND = "/user/wechat" + UPDATE;
	public final static String USER_WECHAT_UNBIND = "/user/wechat" + UNBIND;

	/** 报表 */
	public final static String QUERY_BUSINESS_ANALYSIS = "/businessAnalysis" + QUERY; // 经营分析报表
	public final static String QUERY_SALE_REPORT = "/saleReport" + QUERY; // 销售报表
	public final static String QUERY_SALE_REPORT_SALE_DTL = "/saleReport/saleDtl" + QUERY; // 销售报表单据明细
	public final static String QUERY_SALE_REPORT_FEE_PROFIT_DTL = "/saleReport/feeProfitDtl" + QUERY; // 销售报表费用利润明细
	public final static String EXPORT_SALE_REPORT_COUNT = "/saleReport/excel/saleReport/count";
	public final static String EXPORT_SALE_REPORT = "/saleReport/excel/saleReport.xls"; // 销售报表导出
	public final static String QUERY_RECEIVE_REPORT = "/receive/report" + QUERY;
	public final static String QUERY_RECEIVE_DETAIL_REPORT = "/receive/detail/report" + QUERY;
	public final static String EXPORT_RECEIVE_REPORT = "/receive/report/excel/receiveReport.xls";
	public final static String QUERY_INVOICING_REPORT = "/invoicingReport" + QUERY;
	public final static String QUERY_INVOICING_DETAIL = "/invoicingReportDetail" + QUERY;
	public final static String EXPORT_INVOICING_REPORT = "/invoicing/report/invoicingReport.xls";

	public final static String QUERY_STOCK_REPORT = "/stock/report" + QUERY; // 库存报表
	public final static String QUERY_STOCK_GOODS_REPORT = "/stock/goods/report" + QUERY; // 库存商品
	public final static String EXPORT_STOCK_REPORT = "/stock/report/excel/stockReport.xls";// 库存导出

	public final static String QUERY_ORDER_PROFIT_REPORT = "/orderProfitReport" + QUERY; // 订单利润报表
	public final static String EXPORT_ORDER_PROFIT_REPORT_COUNT = "/orderProfitReport/excel/orderProfitReport/count";
	public final static String EXPORT_ORDER_PROFIT_REPORT = "/orderProfitReport/excel/orderProfitReport.xls"; // 订单利润报表导出
	public final static String QUERY_MOUNTH_PROFIT_REPORT = "/mounthProfitReport" + QUERY; // 订单月利润报表
	public final static String EXPORT_MOUNTH_PROFIT_REPORT_COUNT = "/mounthProfitReport/excel/mounthProfitReport/count";
	public final static String EXPORT_MOUNTH_PROFIT_REPORT = "/mounthProfitReport/excel/mounthProfitReport.xls"; // 订单月利润报表导出

	public final static String QUERY_PERFORMANCE_REPORT = "/performanceReport" + QUERY; // 绩效报表
	public final static String EXPORT_PERFORMANCE_REPORT_COUNT = "/performanceReport/excel/performanceReport/count";
	public final static String EXPORT_PERFORMANCE_REPORT = "/performanceReport/excel/performanceReport.xls"; // 绩效报表导出
	public final static String EXEC_REPORT_RECORD = "/reportRecord/exec"; // 绩效报表重新生成

	public final static String QUERY_FUND_REPORT = "/fund/report" + QUERY;
	public final static String QUERY_FUND_DTL_REPORT = "/fund/dtl/report" + QUERY;
	public final static String QUERY_FUNDCOST_DTL_REPORT = "/fundCost/dtl/report" + QUERY;
	public final static String QUERY_FUNDCOST_BILL = "/fundCost/bill" + QUERY;
	public final static String EXPORT_FUND_STATISTIC_REPORT = "/fundStatistic/report/excel/fundReport.xls";
	public final static String EXPORT_FUNDCOST_DTL = "/fundCost/dtl/excel/fundCostDtl.xls";
	public final static String EXPORT_FUNDCOST_BILL_REPORT = "/fundCost/bill/excel/fundCostBill.xls";

	/** 铺货报表 **/
	public final static String QUERY_GOODS_REPORT = "/goods/report" + QUERY;
	public final static String QUERY_GOODS_DETAIL = "/goods/report" + DETAIL;
	public final static String QUERY_GOODS_PMSIN_QUERY = "/goods/report/pmsin" + QUERY;
	public final static String QUERY_GOODS_PMSOUT_QUERY = "/goods/report/pmsout" + QUERY;
	public final static String QUERY_GOODS_PMSPL_QUERY = "/goods/report/pmspl" + QUERY;
	public final static String QUERY_GOODS_PMSRT_QUERY = "/goods/report/pmsrt" + QUERY;
	public final static String QUERY_GOODS_STL_QUERY = "/goods/report/stl" + QUERY;
	public final static String EXPORT_GOODS_REPORT = "/goods/report/goodsReport.xls";
	public final static String EXPORT_GOODS_PMSIN = "/goods/report/goodsPmsInReport.xls";
	public final static String EXPORT_GOODS_PMSOUT = "/goods/report/goodsPmsOutReport.xls";
	public final static String EXPORT_GOODS_PLEASE = "/goods/report/goodsPleaseReport.xls";
	public final static String EXPORT_GOODS_RETURN = "/goods/report/goodsReturnReport.xls";
	

	public final static String QUERY_DISTRIBUTION_BILLS = "/distributionBillsReport" + QUERY;// 铺货对账单
	public final static String PRINT_DISTRIBUTION_BILLS = "/distributionBillsReport" + PRINT;
	public final static String PRINT_DISTRIBUTION_POWER = "/distributionBillsReport/power" + QUERY;
	
	public final static String QUERY_BUDGET_DISTRIBUTION_BILLS = "/distributionBudgetBillsReport" + QUERY;// 铺货预算对账单
	public final static String PRINT_BUDGET_DISTRIBUTION_BILLS = "/distributionBudgetBillsReport" + PRINT;
	public final static String EXCEL_BUDGET_DISTRIBUTION_BILLS = "/distributionBudgetBillsReport/excel/distributionBudget.xls";
	
	/** 审核时效 **/
	public final static String QUERY_AUDIT_AGING_REPORT = "/auditAgingReport" + QUERY; // 审核时效报表
	public final static String EXPORT_AUDIT_AGING_REPORT_COUNT = "/auditAgingReport/excel/auditAgingReport/count";
	public final static String EXPORT_AUDIT_AGING_REPORT = "/auditAgingReport/excel/auditAgingReport.xls"; // 审核时效报表导出

	/** 刷新数据 **/
	public final static String REFRESH_SALE_PRICE = "/api/refreshSalePrice"; // 刷新销售价和利润单价
	public final static String REFRESH_POOL_ASSET_DTL = "/api/refreshPoolAssetDtl"; // 刷新资产
	public final static String REFRESH_PMS_PAY_ORDER = "/api/refreshPmsPayOrder"; // 刷新pms应收保理付款确认未生成的入库、销售、出库等数据
	public final static String REFRESH_FUND_USED = "/api/refreshFundUsed"; // 刷新水单和应收关系的资金占用
	public final static String REFRESH_PROJECT_POOL = "/api/refreshProjectPool"; // 刷新融资池
	public final static String REFRESH_RECEIPT_POOL = "/api/refreshReceiptPool"; // 刷新资金池
	public final static String REFRESH_PROJECT_NO = "/api/refreshProjectNo"; // 刷新项目编号


	/** 项目临时额度信息 */
	public final static String ADDPROJECTPOOLADJUST = "/projectPoolAdjust" + ADD;
	public final static String QUERYPROJECTPOOLADJUST = "/projectPoolAdjust" + QUERY;
	public final static String DETAILPROJECTPOOLADJUST = "/projectPoolAdjust" + DETAIL;
	public final static String EDITPROJECTPOOLADJUST = "/projectPoolAdjust" + EDIT;
	public final static String SUBMITPROJECTPOOLADJUST = "/projectPoolAdjust" + SUBMIT;
	public final static String DELETEPROJECTPOOLADJUST = "/projectPoolAdjust" + DELETE;
	public final static String LOCKPROJECTPOOLADJUST = "/projectPoolAdjust" + LOCK;
	public final static String UNLOCKPROJECTPOOLADJUST = "/projectPoolAdjust" + UNLOCK;
	public final static String UPDATEPROJECTPOOLADJUST = "/projectPoolAdjust" + UPDATE;
	public final static String UPLOADPROJECTPOOLADJUST = "/projectPoolAdjustFile" + UPLOAD;
	public final static String QUERYPROJECTPOOLADJUSTFILE = "/projectPoolAdjustFile" + QUERY;
	public final static String DELETEPROJECTPOOLADJUSTFILE = "/projectPoolAdjustFile" + DELETE;
	public final static String DOWNLOADPROJECTPOOLADJUST = "/projectPoolAdjustFile" + DOWNLOAD;
	public final static String DOWNLOADPROJECTPOOLADJUSTFILELIST = "/projectPoolAdjustFileList" + DOWNLOAD;

	public final static String ADD_REPORT_FILTER_PROJECT = "/reportFilterProject" + ADD;
	public final static String QUERY_REPORT_FILTER_PROJECT = "/reportFilterProject" + QUERY;

	/** 资金池 **/
	public final static String QUERY_FUND_POOL = "/fundPool" + QUERY;
	public final static String DETAIL_FUND_POOL = "/fundPool" + DETAIL;
	public final static String DETAIL_FUND_POOL_FUND = "/fundPoolFund" + DETAIL;
	public final static String DETAIL_FUND_POOL_DTL = "/fundPoolDtl" + DETAIL;
	public final static String DETAIL_FUND_POOL_ASSEST = "/fundPoolAssest" + DETAIL;
	public final static String EXPORT_FUND_POOL_DTL = "/fundPoolDtl/excel/fundPoolDtl.xls";
	public final static String EXPORT_FUND_POOL_DTL_COUNT= "/fundPoolDtl/excel/export/count";
	public final static String EXPORT_FUND_POOL_ASSEST = "/fundPoolAssest/excel/fundPoolAssest.xls";
	public final static String EXPORT_FUND_POOL_ASSEST_COUNT= "/fundPoolAssest/excel/export/count";
	public final static String EXPORT_FUND_POOL_FUND = "/fundPoolFund/excel/fundPoolFund.xls";
	public final static String EXPORT_FUND_POOL_FUND_COUNT= "/fundPoolFund/excel/export/count";

	/** 可视化相关信息 **/
	public final static String QUERY_MONTH = "/month" + QUERY; // 订单月利润报表
	public final static String QUERY_MONTH_TYPE = "/monthType" + QUERY; // 项目类别利润额利润报表
	public final static String QUERY_MONTH_PROJECT = "/monthProject" + QUERY; // 项目利润额利润报表
	public final static String QUERY_MONTH_DETAIL = "/monthProject" + DETAIL; // 项目利润额利润报表
	public final static String QUERY_MONTH_PIE = "/monthProjectPie" + DETAIL; // 项目利润额利润报表
	public final static String QUERY_GROUP_MONTH_PROJECT = "/monthGroupProject" + QUERY; // 项目月度指标

	/** 推送人信息管理 **/
	public final static String QUERY_SEND_MANAGE = "/sendManage" + QUERY; // 信息查询
	public final static String DETAIL_SEND_MANAGE = "/sendManage" + DETAIL; // 信息详情
	public final static String ADD_SEND_MANAGE = "/sendManage" + ADD; // 信息 添加
	public final static String EDIT_SEND_MANAGE = "/sendManage" + EDIT; // 信息编辑
	public final static String UPDATE_SEND_MANAGE = "/sendManage" + UPDATE; // 信息修改
	public final static String DELETE_SEND_MANAGE = "/sendManage" + DELETE; // 信息删除
	/** 推送人项目 **/
	public final static String QUERY_SEND_PROJECT = "/sendProject" + QUERY; // 信息查询
	public final static String QUERY_SEND_BASEPROJECT = "/sendBaseProject" + QUERY; // 信息查询
	public final static String ADD_SEND_PROJECT = "/sendProject" + ADD; // 信息 添加
	public final static String DELETE_ALL_SEND_PROJECT = "/sendProject" + DELETE_ALL; // 信息删除

	/** 铺货退货单信息 **/
	public final static String QUERY_DISTRIBUTION_RETURN_TITLE = "/distriReturn" + QUERY;
	public final static String DETAIL_DISTRIBUTION_RETURN_TITLE = "/distriReturn" + DETAIL;
	public final static String DETAIL_DISTRIBUTION_RETURN_LINE = "/distriReturn/line" + QUERY;
	public final static String SUBMIT_DISTRIBUTION_RETURN_TITLE = "/distriReturn" + SUBMIT;
	public final static String QUERY_DISTRIBUTION_RETURN_FILE = "/distriReturn/file/query";
	public final static String UPLOAD_DISTRIBUTION_RETURN_FILE = "/distriReturn/file/upload";
	public final static String DELETE_DISTRIBUTION_RETURN_FILE = "/distriReturn/file/delete";
	public final static String DOWNLOAD_DISTRIBUTION_RETURN_FILE = "/distriReturn/file/download";
	public final static String DOWNLOAD_DISTRIBUTION_RETURN_FILE_LIST = "/distriReturn/fileList/download";
	public final static String EXPORT_DISTRIBUTION_RETURN_TITLE = "/distriReturn/distriReturn.xls";
	public final static String EXPORT_DISTRIBUTION_RETURN_LINE = "/distriReturn/line/distriReturnLine.xls";
	public final static String EXPORT_DISTRIBUTION_RETURN_COUNT = "/distriReturn/excel/distriReturn/count";
	public final static String EXPORT_DISTRIBUTION_RETURN_LINE_COUNT = "/distriReturn/excel/distriReturnLine/count";

	/** 铺货结算单信息 **/
	public final static String QUERY_DISTRIBUTION_SETTLE_TITLE = "/distriSettle" + QUERY;
	public final static String DETAIL_DISTRIBUTION_SETTLE_TITLE = "/distriSettle" + DETAIL;
	public final static String DETAIL_DISTRIBUTION_SETTLE_LINE = "/distriSettle/line" + QUERY;
	public final static String QUERY_DISTRIBUTION_SETTLE_FILE = "/distriSettle/file/query";
	public final static String DOWNLOAD_DISTRIBUTION_SETTLE_FILE_LIST = "/distriSettle/fileList/download";
	public final static String UPLOAD_DISTRIBUTION_SETTLE_FILE = "/distriSettle/file/upload";
	public final static String EXPORT_DISTRIBUTION_SETTLE_TITLE = "/distriSettle/distriSettle.xls";
	public final static String EXPORT_DISTRIBUTION_SETTLE_LINE = "/distriSettle/line/distriSettleLine.xls";
	public final static String EXPORT_DISTRIBUTION_SETTLE_COUNT = "/distriSettle/excel/distriSettle/count";
	public final static String EXPORT_DISTRIBUTION_SETTLE_LINE_COUNT = "/distriSettle/excel/distriSettleLine/count";
	public final static String DOWNLOAD_DISTRIBUTION_SETTLE_FILE = "/distriSettle/file/download";

	/** 首页相关信息 **/
	public final static String QUERY_ENTRY_INFO = "/entryInfo" + QUERY; // 业务指标
	public final static String QUERY_CAPITAL_TURNOVER = "/capitalTurnover" + QUERY; // 资金周转率明细查询
	public final static String QUERY_POWAIT_ORDER = "/poWaitOrder" + QUERY; // PO待到货单
	public final static String QUERY_INSENATE_STL = "/inSenateStl" + QUERY; // 在仓库存
	public final static String QUERY_AVG_STLAGE = "/avgStlAge" + QUERY; // 平均库龄
	public final static String QUERY_OVER_STLAGE = "/overStlAge" + QUERY; // 超期
	public final static String QUERY_OVER_RECEIVE = "/overReceive" + QUERY; // 超期应收
	public final static String QUERY_RISK_STL = "/riskStl" + QUERY; // 动销滞销风险
	public final static String QUERY_ENTRY_POOL = "/entryPool" + QUERY; // 统计资金池
	public final static String QUERY_ENTRY_POOL_LIST = "/entryPoolList" + QUERY; // 统计资金池

	/** 业务目标值相关信息 **/
	public final static String QUERY_PROFIT_TARGET = "/profitTarget" + QUERY;
	public final static String DELETE_PROFIT_TARGET = "/profitTarget" + DELETE;
	public final static String DETAIL_PROFIT_TARGET = "/profitTarget" + DETAIL;
	public final static String ADD_PROFIT_TARGET = "/profitTarget" + ADD;
	public final static String EDIT_PROFIT_TARGET = "/profitTarget" + EDIT;
	public final static String UPDATE_PROFIT_TARGET = "/profitTarget" + UPDATE;
	public final static String SUBMIT_PROFIT_TARGET = "/profitTarget" + SUBMIT;
	public final static String QUERY_PROFIT_TARGET_POWER = "/profitTarget/power" + QUERY;

	/** 客户维护相关信息 **/
	public final static String QUERY_CUSTOMER_MAINTAIN = "/customerMaintain" + QUERY;
	public final static String DELETE_CUSTOMER_MAINTAIN = "/customerMaintain" + DELETE;
	public final static String DETAIL_CUSTOMER_MAINTAIN = "/customerMaintain" + DETAIL;
	public final static String ADD_CUSTOMER_MAINTAIN = "/customerMaintain" + ADD;
	public final static String EDIT_CUSTOMER_MAINTAIN = "/customerMaintain" + EDIT;
	public final static String UPDATE_CUSTOMER_MAINTAIN = "/customerMaintain" + UPDATE;
	public final static String FOLLOW_CUSTOMER_MAINTAIN = "/customerMaintain" + FOLLOW;
	public final static String DIVID_CUSTOMER_MAINTAIN = "/customerMaintain" + DIVID;
	public final static String QUERY_CUSTOMER_MAINTAIN_POWER = "/customerMaintain/power" + QUERY;
	/** 客户跟进 **/
	public final static String QUERY_CUSTOMER_FOLLOW = "/customerFollow" + QUERY;
	public final static String ADD_CUSTOMER_FOLLOW = "/customerFollow" + ADD;
	public final static String UPDATE_CUSTOMER_FOLLOW = "/customerFollow" + UPDATE;
	public final static String EDIT_CUSTOMER_FOLLOW = "/customerFollow" + EDIT;
	public final static String DELETE_CUSTOMER_FOLLOW = "/customerFollow" + DELETE;
	public final static String DELETE_ALL_CUSTOMER_FOLLOW = "/customerFollow" + DELETE_ALL;
	/** 事项管理 **/
	public final static String QUERY_MATTER_MANAGE = "/matterManage" + QUERY;
	public final static String DETAIL_MATTER_MANAGE = "/matterManage" + DETAIL;
	public final static String ADD_MATTER_MANAGE = "/matterManage" + ADD;
	public final static String UPDATE_MATTER_MANAGE = "/matterManage" + UPDATE;
	public final static String EDIT_MATTER_MANAGE = "/matterManage" + EDIT;
	public final static String DELETE_MATTER_MANAGE = "/matterManage" + DELETE;
	public final static String SUBMIT_MATTER_MANAGE = "/matterManage" + SUBMIT;
	public final static String EDIT_MATTER_SERVICE = "/matterService" + EDIT;
	public final static String UPDATE_MATTER_SERVICE = "/matterService" + UPDATE;
	public final static String QUERY_FILE_MATTER_MANAGE = "/matterManageFile" + QUERY;// 文件上传
	public final static String UPLOAD_MATTER_MANAGE = "/matterManageFile" + UPLOAD;
	public final static String DELETE_FILE_MATTER_MANAGE = "/matterManageFile" + DELETE;
	public final static String DOWNLOAD_MATTER_MANAGE = "/matterManageFile" + DOWNLOAD;
	public final static String DOWNLOAD_LIST_MATTER_MANAGE = "/matterManageFileList" + DOWNLOAD;
	public final static String QUERY_MATTER_MANAGE_POWER = "/matterManage/power" + QUERY;

	/** Invoice境外收票 **/
	public final static String QUERY_INVOICE_COLLECT_OVER = "/invoiceCollectOver" + QUERY;
	public final static String SUBMIT_INVOICE_COLLECT_OVER = "/invoiceCollectOver" + SUBMIT;
	public final static String DELETE_INVOICE_COLLECT_OVER = "/invoiceCollectOver" + DELETE;
	public final static String EDIT_INVOICE_COLLECT_OVER = "/invoiceCollectOver" + EDIT;
	public final static String DETAIL_INVOICE_COLLECT_OVER = "/invoiceCollectOver" + DETAIL;
	public final static String APPROVE_INVOICE_COLLECT_OVER = "/invoiceCollectOver/approve";
	public final static String ADD_INVOICE_COLLECT_OVER = "/invoiceCollectOver" + ADD;
	public final static String UPDATE_INVOICE_COLLECT_OVER = "/invoiceCollectOver" + UPDATE;
	public final static String EXPORT_INVOICE_COLLECT_OVER = "/invoiceCollectOver/excel/invoiceCollectOver.xls";// 导出
	public final static String EXPORT_INVOICE_COLLECT_OVER_COUNT = "/invoiceCollectOver/excel/export/count";
	public final static String IMPORT_INVOICE_COLLECT_OVER = "/invoiceCollectOver/excel" + IMPORT;// 导入

	/** Invoice境外收票关联采购单 **/
	public final static String QUERY_INVOICE_COLLECT_OVER_PO = "/invoiceCollectOverPo" + QUERY;
	public final static String DELETE_INVOICE_COLLECT_OVER_PO = "/invoiceCollectOverPo" + DELETE;
	public final static String DIVID_INVOICE_COLLECT_OVER_PO = "/invoiceCollectOverPo" + DIVID;
	public final static String ADD_INVOICE_COLLECT_OVER_PO = "/invoiceCollectOverPo" + ADD;
	public final static String EDIT_INVOICE_COLLECT_OVER_PO = "/invoiceCollectOverPo" + EDIT;
	public final static String UPDATE_INVOICE_COLLECT_OVER_PO = "/invoiceCollectOverPo" + UPDATE;
	public final static String QUERY_INVOICE_COLLECT_OVER_FILE = "/invoiceCollectOverFile" + QUERY;
	public final static String DELETE_INVOICE_COLLECT_OVER_FILE = "/invoiceCollectOverFile" + DELETE;
	public final static String DOWNLOAD_INVOICE_COLLECT_OVER_FILE = "/invoiceCollectOverFile" + DOWNLOAD;
	public final static String UPLOAD_INVOICE_COLLECT_OVER_FILE = "/invoiceCollectOverFile" + UPLOAD;
	public final static String DOWNLOAD_INVOICE_COLLECT_OVER_FILE_LIST = "/invoiceCollectOverFileList" + DOWNLOAD;

	/** Invoice境外收票关联费用单 **/
	public final static String QUERY_INVOICE_COLLECT_OVER_FEE = "/invoiceCollectOverFee" + QUERY;
	public final static String DIVID_INVOICE_COLLECT_OVER_FEE = "/invoiceCollectOverFee" + DIVID;
	public final static String ADD_INVOICE_COLLECT_OVER_FEE = "/invoiceCollectOverFee" + ADD;
	public final static String DELETE_INVOICE_COLLECT_OVER_FEE = "/invoiceCollectOverFee" + DELETE;
	public final static String EDIT_INVOICE_COLLECT_OVER_FEE = "/invoiceCollectOverFee" + EDIT;
	public final static String UPDATE_INVOICE_COLLECT_OVER_FEE = "/invoiceCollectOverFee" + UPDATE;

	/** 应收抵扣费用模块 **/
	public final static String QUERY_REC_DEDUCTION_FEE = "/fee/rec/deduction" + QUERY;
	public final static String ADD_REC_DEDUCTION_FEE = "/fee/rec/deduction" + ADD;
	public final static String DELETE_REC_DEDUCTION_FEE = "/fee/rec/deduction" + DELETE;
	public final static String UPDATE_REC_DEDUCTION_FEE = "/fee/rec/deduction" + UPDATE;
	public final static String EDIT_REC_DEDUCTION_FEE = "/fee/rec/deduction" + EDIT;
	public final static String DETAIL_REC_DEDUCTION_FEE = "/fee/rec/deduction" + DETAIL;
	public final static String SUBMIT_REC_DEDUCTION_FEE = "/fee/rec/deduction" + SUBMIT;
	public final static String EXCEL_REC_DEDUCTION_FEE = "/fee/excel/recDeductionFee.xls";
	public final static String EXCEL_REC_DEDUCTION_FEE_COUNT = "/fee/excel/recDeductionCount";
	public final static String EXPORT_REC_DEDUCTION_FEE = "/fee/rec/feeRecDeductionExport.xls";
	public final static String IMPORT_REC_DEDUCTION_FEE = "/fee/rec/feeRecDeductionExport" + IMPORT;

	/** 应付抵扣费用模块 **/
	public final static String QUERY_PAY_DEDUCTION_FEE = "/fee/pay/deduction" + QUERY;
	public final static String ADD_PAY_DEDUCTION_FEE = "/fee/pay/deduction" + ADD;
	public final static String DELETE_PAY_DEDUCTION_FEE = "/fee/pay/deduction" + DELETE;
	public final static String UPDATE_PAY_DEDUCTION_FEE = "/fee/pay/deduction" + UPDATE;
	public final static String EDIT_PAY_DEDUCTION_FEE = "/fee/pay/deduction" + EDIT;
	public final static String DETAIL_PAY_DEDUCTION_FEE = "/fee/pay/deduction" + DETAIL;
	public final static String SUBMIT_PAY_DEDUCTION_FEE = "/fee/pay/deduction" + SUBMIT;
	public final static String EXCEL_PAY_DEDUCTION_FEE = "/fee/excel/payDeductionFee.xls";
	public final static String EXCEL_PAY_DEDUCTION_FEE_COUNT = "/fee/excel/payDeductionCount";
	public final static String EXPORT_PAY_DEDUCTION_FEE = "/fee/pay/feePayDeductionExport.xls";
	public final static String IMPORT_PAY_DEDUCTION_FEE = "/fee/pay/feePayDeductionExport" + IMPORT;
	public final static String QUERY_TITLE_PAY_DEDUCTION_FEE = "/fee/pay/feePayDeductionTitle" + QUERY;

	/** 贷款客户 **/
	public final static String QUERY_SUPPLIER_AUTH = "/supplierAuth" + QUERY;
	public final static String DETAIL_SUPPLIER_AUTH = "/supplierAuth" + DETAIL;
	public final static String QUERY_LOAN_ORDER = "/loanOrder" + QUERY;
	public final static String DETAIL_LOAN_ORDER = "/loanOrder" + DETAIL;
	public final static String QUERY_REPAY = "/repay" + QUERY;
	public final static String DETAIL_REPAY = "/repay" + DETAIL;
	public final static String QUERY_REPAY_DETAIL = "/repayDetail" + QUERY;
	public final static String DETAIL_REPAY_DETAIL = "/repayDetail" + DETAIL;
	public final static String QUERY_ACCOUNT_STATEMENT = "/accountStatement" + QUERY;
	public final static String DETAIL_ACCOUNT_STATEMENT = "/accountStatement" + DETAIL;
	public final static String QUERY_ORDER_MESSAGE = "/orderMessage" + QUERY;
	public final static String DETAIL_ORDER_MESSAGE = "/orderMessage" + DETAIL;
	public final static String QUERY_REPAY_PLAN = "/repayPlan" + QUERY;
	public final static String DOWNLOAD_GSA = "/gsaFile" + DOWNLOAD;
	public final static String QUERY_FILE_SUPPLIER_AUTH = "/supplierAuthFile" + QUERY;
	public final static String QUERY_FILE_LOAN_ORDER = "/loanOrderFile" + QUERY;
	
	/** 审核流相关信息 **/
	public final static String QUERY_AUDIT_FLOW = "/auditFlow" + QUERY; 
	public final static String DELETE_AUDIT_FLOW = "/auditFlow" + DELETE; 
	public final static String DETAIL_AUDIT_FLOW = "/auditFlow" + DETAIL; 
	public final static String ADD_AUDIT_FLOW = "/auditFlow" + ADD; 
	public final static String EDIT_AUDIT_FLOW = "/auditFlow" + EDIT; 
	public final static String UPDATE_AUDIT_FLOW = "/auditFlow" + UPDATE; 
	public final static String PAY_AUDIT_FLOW = "/auditFlow/pay"; 
	
	/** 供应商销售信息 **/
	public final static String QUERY_PMSSTORE_OUT = "/pmsStoreOut" + QUERY;

	/** 小额贷款-日志查询 **/
	public final static String QUERY_TRACE_LOG = "/traceLog" + QUERY;
	public final static String DETAIL_TRACE_LOG = "/traceLog" + DETAIL;
	
	/** 资金池信息 **/
	public final static String QUERY_ACCOUNT_POOL = "/accountPool" + QUERY;
	public final static String DETAIL_ACCOUNT_POOL = "/accountPool" + DETAIL;
	public final static String DETAIL_ACCOUNT_POOL_FUND = "/accountPoolFund" + DETAIL;
	public final static String REFRESH_ACCOUNT_POOL = "/api/refreshAccountPool";
	
	/** 销售日报信息 **/
	public final static String QUERY_SALES_DAILY = "/salesDaily" + QUERY;
	
	/** 应付管理 **/
	public final static String QUERY_COPE_MANAGE = "/copeManage" + QUERY;
	public final static String DETAIL_COPE_MANAGE = "/copeManage" + DETAIL;
	public final static String QUERY_COPE_MANAGE_DTL = "/copeManageDtl" + QUERY;
	public final static String QUERY_COPE_MANAGE_MEMO = "/copeManageMemo" + QUERY;
	
}
