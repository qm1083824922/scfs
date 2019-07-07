package com.scfs.common.consts;

/**
 * 缓存业务KEY常量类
 * @author Administrator
 *
 */
public class CacheKeyConsts {
    public final static String CACHE_PREFIX = "scfs:";
	
    /**客户*/
    public final static String CUSTOMER = "CUSTOMER";

    public final static String ACCOUNT = "ACCOUNT";//账号

    public final static String BUSI_UNIT = "BUSI_UNIT"; //经营单位

    public final static String WAREHOUSE = "WAREHOUSE"; //仓库

    public final static String SUPPLIER = "SUPPLIER"; //供应商

    public final static String SUPPLIER_PROJECT = "SUPPLIER_PROJECT"; //供应商 关联的项目

    public final static String SUBJECT = "SUBJECT"; //主题

    public final static String PROJECT = "PROJECT"; //项目

    public final static String GOODS = "GOODS"; //商品

    public final static String ADDRESS = "ADDRESS"; //地址

    public final static String BCS = "BCS"; //经营单位  客户  供应商

    public final static String PROJECT_CS = "PROJECT_CS"; //项目下的供应商  客户

    public final static String BIZ_CONSTANTS = "BIZ_CONSTANTS"; //常量类

    public final static String BIZ_CONSTANTS_PARENT = "BIZ_CONSTANTS_PARENT"; //常量类上级常量，父子关系

    public final static String DEPARTMENT = "DEPARTMENT"; //部门

    public final static String DISTRIBUTION_GOODS = "DISTRIBUTION_GOODS"; //铺货商品

    public final static String BUSI_UNIT_PROJECT = "BUSI_UNIT_PROJECT"; //经营单位的项目

    public final static String PROJECT_BUSI_UNIT = "PROJECT_BUSI_UNIT"; //项目下的经营单位

    public final static String PROJECT_SUBJECT = "PROJECT_SUBJECT"; //项目--主题

    public final static String PROJECT_CUSTOMER = "PROJECT_CUSTOMER"; //项目下的客户

    public final static String CUSTOMER_PROJECT = "CUSTOMER_PROJECT"; //客户下的项目

    public final static String PROJECT_ALL_CUSTOMER = "PROJECT_ALL_CUSTOMER"; //用户项目下所有的客户

    public final static String PROJECT_WAREHOUSE = "PROJECT_WAREHOUSE"; //项目下的仓库

    public final static String PROJECT_VIRTUAL_WAREHOUSE = "PROJECT_VIRTUAL_WAREHOUSE"; //项目下的虚拟仓库

    public final static String PROJECT_SUPPLIER = "PROJECT_SUPPLIER"; //项目下的供应商

    public final static String PROJECT_GOODS = "PROJECT_GOODS"; //项目下的商品

    public final static String PROJECT_BCS = "PROJECT_BCS"; //项目下的经营单位  客户  供应商

    public final static String SUBJECT_ACCOUNT = "SUBJECT_ACCOUNT"; //主题（经营单位、仓库、供应商、客户）下的账号

    public final static String SUBJECT_ADDRESS = "SUBJECT_ADDRESS"; //主题（经营单位、仓库、供应商、客户）下的地址

    public final static String SUBJECT_INVICE = "SUBJECT_INVICE"; //主题（经营单位、仓库、供应商、客户）下的开票信息

    public final static String ACCOUNTBOOK = "ACCOUNT_BOOK"; //帐套

    public final static String PAY_FEE_SPEC = "PAY_FEE_SPEC"; //应付费用 2

    public final static String PAY_FEE_MANAGE = "PAY_FEE_MANAGE"; //管理费用 2

    public final static String PAY_FEE_ARTIFICIAL = "PAY_FEE_ARTIFICIAL"; //人工费用 3

    public final static String REC_FEE_SPEC = "REC_FEE_SPEC"; //应收费用 1

    public final static String REC_PAY_FEE_SPEC = "REC_PAY_FEE_SPEC"; //应付 应收  3

    public final static String ACCOUNTLINE = "ACCOUNT_LINE"; //科目

    public final static String ACCOUNTLINE_LAST = "ACCOUNT_LINE_LAST"; //末级科目

    public final static String USER = "USER"; //用户

    public final static String USERNAME_ID = "USERNAME_ID"; //用户昵称--用户ID map

    public final static String ADMIN_ROLE = "ADMIN_ROLE"; //所有管理员用户

    public final static String USER_PROJECT = "USER_PROJECT"; //用户下的项目

    public final static String USER_ROLES = "USER_ROLES"; //用户--角色缓存

    public final static String ROLES = "ROLES"; //角色缓存

    public final static String ROLE_PERMISSION_GROUP = "ROLE_PERMISSION_GROUP"; //角色--权限组缓存

    public final static String PERMISSION_GROUP = "PERMISSION_GROUP"; //权限组缓存

    public final static String PERMISSION_GROUP_PERMISSION = "PERMISSION_GROUP_PERMISSION"; //权限组--权限缓存

    public final static String PERMISSIONS = "PERMISSIONS"; // 权限列表缓存hel

    public final static String USER_PERMISSIONS = "USER_PERMISSIONS"; // 用户--权限列表缓存

    public final static String PROJECT_ITEM = "PROJECT_ITEM"; // 项目--条款列表缓存

    public final static String DEPARTMENT_USER = "DEPARTMENT_USER"; // 部门--用户列表缓存

    public final static String DEPARTMENT_PROJECT = "DEPARTMENT_PROJECT"; // 部门--项目列表缓存

    public final static String DEPARTMENT_USER_PROJECT = "DEPARTMENT_USER_PROJECT"; // 部门--项目列表缓存,部门下的所有项目，在过滤这些项目是分配给当前登录用户

    public final static String PROJECT_USER = "PROJECT_USER"; //项目下的用户

    public final static String NATIOIN_DICT_REGION = "NATIOIN_DICT_REGION"; //国家

    public final static String DICT_REGION_MAP = "DICT_REGION_MAP"; //地址上下级映射 国家--省列表，省--市列表

    public final static String DICT_REGION = "DICT_REGION"; //地址id对应名称

    public final static String MAX_DATE_TIME = "_MAX_DATE_TIME"; //最后更新时间

    public final static String DEDUCTION_TYPE = "DEDUCTION_TYPE"; //抵扣类型

}
