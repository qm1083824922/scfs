package com.scfs.common.consts;

/**
 * 
 * @author Administrator
 *
 */
public class BaseUrlConsts extends UrlConsts{

	/** 角色信息 */
	public final static String ADDROLE = "/role" + ADD;
	public final static String UPDATEROLE = "/role" + UPDATE;
	public final static String QUERYROLE = "/role" + QUERY;
	public final static String EDITROLE = "/role" + EDIT;
	public final static String DETAILROLE = "/role" + DETAIL;
	public final static String LOCKROLE = "/role" + LOCK;
	public final static String UNLOCKROLE = "/role" + UNLOCK;

	/** 权限信息 */
	public final static String QUERYPERMISSION = "/permission" + QUERY;
	public final static String ADDPERMISSION = "/permission" + ADD;
	public final static String UPDATEPERMISSION = "/permission" + UPDATE;
	public final static String DELETEPERMISSION = "/permission" + DELETE;
	public final static String INVALIDEPERMISSION = "/permission" + INVALID;
	public final static String EDITPERMISSION = "/permission" + EDIT;
	public final static String DETAILPERMISSION = "/permission" + DETAIL;
	public final static String SUBMITPERMISSION = "/permission" + SUBMIT;
	public final static String LOCKPERMISSION = "/permission" + LOCK;
	public final static String UNLOCKPERMISSION = "/permission" + UNLOCK;
	public final static String QUERYPERMISSIONROLE = "/permissionRole" + QUERY;
	public final static String IMPORT_PERMISSION = "/permission/excel" + IMPORT;// 导入
	public final static String PERMISSION_TEMPLATE_DOWNLOAD = "/template/baseinfo/authority/permission_template.xls";// 导入
	

	public final static String QUERYDIVIDPERMISSION = "/permission/divid" + QUERY;
	public final static String DIVIDPERMISSION = "/permission/divid";
	public final static String QUERYUNDIVIDPERMISSION = "/permission/unDivid" + QUERY;

	/** 权限组信息 */
	public final static String QUERYPERMISSIONGROUP = "/permissionGroup" + QUERY;
	public final static String QUERYPERMISSIONGROUPROLE = "/permissionGroup/role" + QUERY;
	public final static String QUERYPERMISSIONGROUPUSER = "/permissionGroup/user" + QUERY;
	public final static String QUERYPERMISSIONGROUPBYPER = "/permission/permissionGroup" + QUERY;

	public final static String INVALIDPERMISSIONGROUP = "/permissionGroup" + INVALID;
	public final static String ADDPERMISSIONGROUP = "/permissionGroup" + ADD;
	public final static String UPDATEPERMISSIONGROUP = "/permissionGroup" + UPDATE;
	public final static String EDITPERMISSIONGROUP = "/permissionGroup" + EDIT;
	public final static String DETAILPERMISSIONGROUP = "/permissionGroup" + DETAIL;
	public final static String LOCKPERMISSIONGROUP = "/permissionGroup" + LOCK;
	public final static String UNLOCKPERMISSIONGROUP = "/permissionGroup" + UNLOCK;

	public final static String QUERYDIVIDPERMISSIONGROUP = "/role/permissionGroup/divid" + QUERY;
	public final static String DIVIDPERMISSIONGROUP = "/role/permissionGroup/divid";
	public final static String QUERYUNDIVIDPERMISSIONGROUP = "/role/permissionGroup/unDivid" + QUERY;
	
	/** 商品信息 */
	public final static String QUERYGOODS = "/goods" + QUERY;
	public final static String ADDGOODS = "/goods" + ADD;
	public final static String DELETEGOODS = "/goods" + DELETE;
	public final static String DETAILGOODS = "/goods" + DETAIL;
	public final static String EDITGOODS = "/goods" + EDIT;
	public final static String LOCKGOODS = "/goods" + LOCK;
	public final static String UNLOCKGOODS = "/goods" + UNLOCK;
	public final static String SUBMITGOODS = "/goods" + SUBMIT;
	public final static String UPDATEGOODS = "/goods" + UPDATE;
	public final static String IMPORT_GOODS = "/goods/excel" + IMPORT;// 导入
	public final static String DOWNLOAD_GOODS_TEMPLATE = "/goods/excel/goodsTemplate.xls";	// 下载导入模板
	
	
	/** 铺货商品信息 */
	public final static String QUERY_DISTRIBUTE_GOODS = "/distributeGoods" + QUERY;
	public final static String ADD_DISTRIBUTE_GOODS = "/distributeGoods" + ADD;
	public final static String DELETE_DISTRIBUTE_GOODS = "/distributeGoods" + DELETE;
	public final static String DETAIL_DISTRIBUTE_GOODS = "/distributeGoods" + DETAIL;
	public final static String EDIT_DISTRIBUTE_GOODS = "/distributeGoods" + EDIT;
	public final static String LOCK_DISTRIBUTE_GOODS = "/distributeGoods" + LOCK;
	public final static String UNLOCK_DISTRIBUTE_GOODS = "/distributeGoods" + UNLOCK;
	public final static String SUBMIT_DISTRIBUTE_GOODS = "/distributeGoods" + SUBMIT;
	public final static String UPDATE_DISTRIBUTE_GOODS = "/distributeGoods" + UPDATE;
	public final static String IMPORT_DISTRIBUTE_GOODS = "/distributeGoods/excel" + IMPORT;// 导入
	public final static String DOWNLOAD_DISTRIBUTE_GOODS_TEMPLATE = "/distributeGoods/excel/distributeTemplate.xls";	// 下载导入模板
	public final static String QUERY_DISTRIBUTE_GOODS_FILE = "/distributeGoods/file/query";
	public final static String UPLOAD_DISTRIBUTE_GOODS_FILE = "/distributeGoods/file/upload";
	public final static String DELETE_DISTRIBUTE_GOODS_FILE = "/distributeGoods/file/delete";
	public final static String DOWNLOAD_DISTRIBUTE_GOODS_FILE = "/distributeGoods/file/download";
	public final static String DOWNLOAD_DISTRIBUTE_GOODS_FILE_LIST = "/distributeGoods/fileList/download";
	public final static String QUERY_DISTRIBUTE_GOODS_GOODS = "/distributeGoods/goods" + QUERY;
	public final static String COPE_DISTRIBUTE_GOODS = "/distributeGoods/cope";
	public final static String EXCEL_DISTRIBUTE_GOODS = "/distributeGoods/excel/gistribution_goods_list.xls";
	
	/** 汇率信息 */
	public final static String QUERYEXCHANGERATE = "/exchangeRate" + QUERY;
	public final static String QUERYEXCHANGERATEHIS = "/exchangeRate/history" + QUERY;
	public final static String ADDEXCHANGERATE = "/exchangeRate" + ADD;
	public final static String UPDATEEXCHANGERATE = "/exchangeRate" + UPDATE;
	public final static String EDITEXCHANGERATE = "/exchangeRate" + EDIT;

	/** 用户信息 **/
	public final static String ADDUSER = "/user" + ADD;
	public final static String UPDATEUSER = "/user" + UPDATE;
	public final static String QUERYUSER = "/user" + QUERY;
	public final static String SUBMITUSER = "/user" + SUBMIT;
	public final static String LOCKUSER = "/user" + LOCK;
	public final static String RESETPSW = "/user" + RESET;
	public final static String UNLOCKUSER = "/user" + UNLOCK;
	public final static String DELETEUSER = "/user" + DELETE;
	public final static String DETAILUSER = "/user" + DETAIL;
	public final static String EDITUSER = "/user" + EDIT;
	public final static String QUERYUSERPROJECT = "/user/project" + QUERY;
	public final static String QUERYUSERPROJECTNOTASSIGNED = "/user/project/notAssigned" + QUERY;
	public final static String DELETEUSERPROJECT = "/user/project" + DELETE;
	public final static String DELETEALLUSERPROJECT = "/user/project" + DELETE_ALL;
	public final static String ADDUSERPROJECT = "/user/project" + ADD;
	public final static String PROJECTUSERDIVIDE = "/project/user" + DIVID;
	public final static String PROJECTUSERDELETE = "/project/user" + DELETE;
	
	public final static String QUERYUSERROLE = "/user/role" + QUERY;
	public final static String QUERYUSERROLENOTASSIGNED = "/user/role/notAssigned" + QUERY;
	public final static String DELETEUSERROLE = "/user/role" + DELETE;
	public final static String DELETEALLUSERROLE = "/user/role" + DELETE_ALL;
	public final static String ADDUSERROLE = "/user/role" + ADD;
	public final static String ADDUSERROLEList = "/user/role/list" + ADD;
	public final static String QUERYUSERROLEBYID = "/userRole" + QUERY;
	public final static String QUERYPROJECTUSER = "/project/user" + QUERY;
	public final static String QUERYUNDIVIDEPROJECTUSER = "/project/user/undivide" + QUERY;
	
	
	public final static String QUERY_USER_SUBJECT = "/user/subject" + QUERY;
	public final static String QUERY_USER_SUBJECT_NOTASSIGNED = "/user/subject/notAssigned" + QUERY;
	public final static String DELETE_USER_SUBJECT = "/user/subject" + DELETE;
	public final static String DELETE_ALL_USER_SUBJECT = "/user/subject" + DELETE_ALL;
	public final static String ADD_USER_DIVID = "/user/subject" + DIVID;
	public final static String ADD_USER_DIVID_ALL = "/user/subject" + DIVID_ALL;
	
	public final static String QUERY_USER_SUPPLIER = "/user/supplier" + QUERY;
	public final static String QUERY_USER_SUPPLIER_NOTASSIGNED = "/user/supplier/notAssigned" + QUERY;
	public final static String DELETE_ALL_USER_SUPPLIER = "/user/supplier" + DELETE_ALL;
	public final static String ADD_USER_SUPPLIER_DIVID_ALL = "/user/supplier" + DIVID_ALL;

	public final static String QUERY_SUBJECT_USER = "/baseSubject/user" + QUERY;
	public final static String QUERY_SUBJECT_USER_NOTASSIGNED = "/baseSubject/user/notAssigned" + QUERY;
	public final static String DELETE_ALL_SUBJECT_USER = "/baseSubject/user" + DELETE_ALL;
	public final static String ADD_SUBJECT_DIVID_ALL = "/baseSubject/user" + DIVID_ALL;
	
	/** 客户主体信息 **/
	public final static String QUERYSUBJECTBYCOND = "/baseSubject" + QUERY;
	public final static String UPDATESUBJECT = "/baseSubject" + UPDATE;
	public final static String DELETESUBJECT = "/baseSubject" + DELETE;
	public final static String LOCKSUBJECT = "/baseSubject" + LOCK;
	public final static String UNLOCKSUBJECT = "/baseSubject" + UNLOCK;
	public final static String SUBMITSUBJECT = "/baseSubject" + SUBMIT;
	public final static String DETAILSUBJECT = "/baseSubject" + DETAIL;
	public final static String EDITSUBJECT = "/baseSubject" + EDIT;

	/** 客户主体新建 **/
	public final static String ADDBUSIUNIT = "/baseSubject/busiUnit" + ADD;
	public final static String ADDSUPPLIER = "/baseSubject/supplier" + ADD;
	public final static String ADDWAREHOUSE = "/baseSubject/warehouse" + ADD;
	public final static String ADDCUST = "/baseSubject/cust" + ADD;
	public final static String ADDMUTISUBJECT = "/mutiSubject" + ADD;

	/** 客户主体地址信息 **/
	public final static String ADDSUBJECTADDRESS = "/baseSubject/address" + ADD;
	public final static String QUERYADDRESSBYSUBJECTID = "/baseSubject/address" + QUERY;
	public final static String BATCHINVALIDSUBJECTADDRESS = "/baseSubject/address/batch" + INVALID;

	/** 客户主体账号信息 **/
	public final static String ADDSUBJECTACCOUNT = "/baseSubject/account" + ADD;
	public final static String QUERYACCOUNTBYSUBJECTID = "/baseSubject/account" + QUERY;
	public final static String EDITACCOUNTBYID = "/baseSubject/account" + EDIT;
	public final static String BATCHINVALIDSUBJECTACCOUNT = "/baseSubject/account/batch" + INVALID;

	/** 客户主体开票信息 **/
	public final static String ADDSUBJECTINVOICE = "/baseSubject/invoice" + ADD;
	public final static String QUERYINVOICEBYSUBJECTID = "/baseSubject/invoice" + QUERY;
	public final static String QUERYINVOICEBYID = "/baseSubject/invoiceById" + QUERY;
	public final static String BATCHINVALIDSUBJECTINVOICE = "/baseSubject/invoice/batch" + INVALID;
	public final static String QUERYREGIONSBYID = "/region/id" + QUERY;

	/** 审核信息 **/
	public final static String QUERYAUDITENTRY1 = "/audit/entry1" + QUERY;
	public final static String QUERY_AUDIT_ENTRY1_WECHAT = "/audit/entry1/wechat" + QUERY;//模糊查询
	public final static String QUERYAUDITAUDITOR = "/audit/auditor" + QUERY;
	public final static String QUERYAUDITENTRY2 = "/audit/entry2" + QUERY;
	public final static String QUERYAUDITPROPOSER = "/audit/proposer" + QUERY;
	public final static String QUERYAUDITBYID = "/audit/id" + QUERY;
	public final static String QUERYAUDITBYNEXT = "/audit/next" + QUERY;
	public final static String AUDIT_BATCH = "/audit/batch" ;//批量审核
	public final static String QUERY_AUDIT_PROPOSER_ALL = "/audit/proposer/query/all";	//查询所有申请单据的权限

	/** 基础通用URL **/
	public final static String QUERYCOMMONSELECTED = "/common/selected" + QUERY;
	public final static String PREMCOMMONSELECTED = "/common/perm" + QUERY;
	public final static String DETAILCOMMONGOODS = "/common/goods" + DETAIL;
	public final static String QUEYRCOMMONGOODS = "/common/project/goods" + QUERY;
	public final static String QUEYRCOMMONMENU = "/common/menus" + QUERY;
	public final static String REFRESHCOMMONCACHE = "/common/cache/refresh";
	public final static String UPDATEPRINTNUM = "/common/updatePrintNum";
	public final static String BATCHUPDATEPRINTNUM = "/common/batchUpdatePrintNum";
	public final static String DEPARTMENT_TREE = "/common/department/tree";
	public final static String DEPARTMENT_USER_TREE = "/common/department/user";
	public final static String COMMON_ROLLBACK = "/common/rollback";
	public final static String QUERY_COMMON_GSA_SELECTED = "/common/gsa/selected" + QUERY;
	public final static String GSA_REFRESHCOMMONCACHE = "/common/cache/gsa/refresh";


	public final static String LOGIN = "/login";
	public final static String CAPTCHA = "/captcha";
	public final static String LOGIN_INNER = "/inner/login";
	public final static String API_UPDATE_INNER = "/api/inner/update";
	public final static String LOGIN_INNER_REDIRECT = "/inner/redirect";
	public final static String LOGOUT = "/logout";
	public final static String UPDATEPWD = "/pwd/update";
	public final static String QUERYFIRSTMENU = "/first/menu" + QUERY;
    public final static String FILE_VIEW = "/file/view" ;//文件下载
    public final static String FILE_UPLOAD = "/file/upload" ;//文件下载
	/** 接口日志调用 **/
	public final static String QUERYINVOKELOG = "/invokeLog" + QUERY;
	public final static String REINVOKEINVOKELOG = "/invokeLog/reInvoke";
	public final static String REDEALINVOKELOG = "/invokeLog/reDeal";
	/** 部门 **/
	public final static String QUERYDEPARTMENT = "/department" + QUERY ;
	public final static String ADDDEPARTMENT = "/department" + ADD;
	public final static String DELETEDEPARTMENT = "/department" + DELETE;
	public final static String EDITDEPARTMENT = "/department" + EDIT; 
	public final static String DETAILDEPARTMENT = "/department" + DETAIL; 
	public final static String UPDATEDEPARTMENT = "/department" + UPDATE; 

	/** excel **/
	public final static String QUERYEXCELLIST = "/excelList" + QUERY ;

	public final static String DOWNLOADEXCELLIST = "/excelList" + DOWNLOAD ;

}
