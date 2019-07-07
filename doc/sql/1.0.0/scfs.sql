/*
SQLyog 企业版 - MySQL GUI v8.14 
MySQL - 5.7.22-log : Database - scfs
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`scfs` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `scfs`;

/*Table structure for table `b_currency_rate` */

DROP TABLE IF EXISTS `b_currency_rate`;

CREATE TABLE `b_currency_rate` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `name` varchar(150) DEFAULT NULL COMMENT '名称',
  `currency` varchar(20) DEFAULT NULL COMMENT '币种',
  `cny_rate` decimal(20,10) DEFAULT NULL COMMENT 'CNY汇率',
  `hkd_rate` decimal(20,10) DEFAULT NULL COMMENT 'HKD汇率',
  `create_user` varchar(48) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(48) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `status` tinyint(4) DEFAULT '1' COMMENT '1开启  2封存',
  `the_month_cd` varchar(8) DEFAULT NULL COMMENT '月份',
  `usd_rate` decimal(20,10) DEFAULT NULL COMMENT 'USD汇率',
  PRIMARY KEY (`id`),
  KEY `idx_b_currency_rate_currency_the_month_cd` (`currency`,`the_month_cd`)
) ENGINE=InnoDB AUTO_INCREMENT=4801 DEFAULT CHARSET=utf8 COMMENT='汇率管理表|2016-11-30';

/*Table structure for table `dict_region` */

DROP TABLE IF EXISTS `dict_region`;

CREATE TABLE `dict_region` (
  `region_id` mediumint(6) NOT NULL COMMENT 'ID',
  `parent_id` mediumint(6) DEFAULT NULL COMMENT '上级ID',
  `region_name` varchar(300) DEFAULT NULL COMMENT '地名',
  `region_type` tinyint(1) DEFAULT NULL COMMENT '类型',
  PRIMARY KEY (`region_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='国省市县';

/*Table structure for table `tb_account_pool` */

DROP TABLE IF EXISTS `tb_account_pool`;

CREATE TABLE `tb_account_pool` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '资金池ID',
  `busi_unit` bigint(20) NOT NULL DEFAULT '0' COMMENT '经营单位ID',
  `account_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '账户id',
  `currency_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '币种',
  `available_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '可用资金',
  `account_balance_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '账户余额',
  `profit_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '利润',
  `creator_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建人ID',
  `creator` varchar(50) NOT NULL DEFAULT '' COMMENT '创建人',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='账户资金池';

/*Data for the table `tb_account_pool` */

/*Table structure for table `tb_account_pool_fund` */

DROP TABLE IF EXISTS `tb_account_pool_fund`;

CREATE TABLE `tb_account_pool_fund` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '资金池明细id',
  `pool_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '资金池头id',
  `account_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '账户id',
  `busi_unit` bigint(20) NOT NULL DEFAULT '0' COMMENT '经营单位id',
  `project_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '项目Id',
  `customer_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '客户id',
  `supplie_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '供应商id',
  `bill_no` varchar(50) NOT NULL DEFAULT '' COMMENT '单据编号',
  `bill_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '单据类型',
  `currency_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '单据币种',
  `bill_date` datetime NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '单据日期',
  `bill_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '单据金额',
  `bill_charge_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '手续费',
  `bill_third_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '单据id，如水单id,付款id',
  `remark` varchar(250) NOT NULL DEFAULT '' COMMENT '备注',
  `creator_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建人ID',
  `creator` varchar(255) NOT NULL DEFAULT '' COMMENT '创建人',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='资金明细信息';

/*Data for the table `tb_account_pool_fund` */

/*Table structure for table `tb_account_statement_title` */

DROP TABLE IF EXISTS `tb_account_statement_title`;

CREATE TABLE `tb_account_statement_title` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `project_id` bigint(20) NOT NULL COMMENT '项目id',
  `bill_no` varchar(50) NOT NULL COMMENT '单据编号',
  `bill_attach_no` varchar(50) NOT NULL COMMENT '单据附属编号',
  `cust_id` bigint(20) NOT NULL COMMENT '客户id',
  `ast_start_date` date NOT NULL COMMENT '结算开始日期',
  `ast_end_date` date NOT NULL COMMENT '结算结束日期',
  `currency_type` tinyint(4) NOT NULL COMMENT '币种 (项目上的币种)',
  `project_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '项目总额度',
  `total_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '总占用额度',
  `in_use_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '可用额度',
  `in_store_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '在库监管总额',
  `on_way_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '在途总额',
  `lend_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '借货总额',
  `state` tinyint(4) DEFAULT NULL COMMENT '状态 1:待提交 2:待财务审核 3:已完成',
  `note` varchar(200) NOT NULL COMMENT '备注',
  `creator_id` bigint(20) NOT NULL COMMENT '创建人',
  `creator` varchar(50) NOT NULL COMMENT '创建人id',
  `create_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='对账单头';

/*Data for the table `tb_account_statement_title` */

/*Table structure for table `tb_async_excel` */

DROP TABLE IF EXISTS `tb_async_excel`;

CREATE TABLE `tb_async_excel` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `class_name` varchar(500) DEFAULT NULL COMMENT '类全名称',
  `method_name` varchar(100) DEFAULT NULL COMMENT '方法名称',
  `args` blob COMMENT '参数',
  `template_path` varchar(200) DEFAULT NULL COMMENT 'excel导出模板路径',
  `excel_path` varchar(200) DEFAULT NULL COMMENT '生成的excel文件存放路径',
  `po_type` tinyint(4) DEFAULT NULL COMMENT '单据类型',
  `name` varchar(100) DEFAULT NULL COMMENT '文件存放路径',
  `create_at` datetime DEFAULT NULL COMMENT '创建时间',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `update_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `yn` tinyint(4) DEFAULT '0' COMMENT '是否执行,0表示未执行，1表示已执行',
  `result` tinyint(4) DEFAULT '0' COMMENT '执行结果,0表示成功，1表示失败',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `tb_async_excel` */

/*Table structure for table `tb_audit` */

DROP TABLE IF EXISTS `tb_audit`;

CREATE TABLE `tb_audit` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `po_id` int(11) DEFAULT NULL COMMENT '单据ID',
  `po_type` tinyint(4) DEFAULT NULL COMMENT '单据类型',
  `po_date` datetime DEFAULT NULL COMMENT '单据日期',
  `audit_type` tinyint(4) DEFAULT NULL COMMENT '审核类型：1正常，2转交，3加签',
  `po_no` varchar(50) DEFAULT NULL COMMENT '单据编号',
  `business_unit_id` int(11) DEFAULT NULL COMMENT '经营单位ID',
  `project_id` int(11) DEFAULT NULL COMMENT '项目ID',
  `supplier_id` int(11) DEFAULT NULL COMMENT '供应商ID',
  `customer_id` int(11) DEFAULT NULL COMMENT '客户ID',
  `currency_id` tinyint(4) DEFAULT NULL COMMENT '币种',
  `amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '金额',
  `auditor_id` int(11) DEFAULT NULL COMMENT '当前审核人ID',
  `auditor` varchar(50) DEFAULT NULL COMMENT '当前审核人',
  `auditor_pass_id` int(11) DEFAULT NULL COMMENT '审核通过人ID',
  `auditor_pass` varchar(50) DEFAULT NULL COMMENT '审核通过人',
  `auditor_pass_at` datetime DEFAULT NULL COMMENT '审核通过时间',
  `state` tinyint(4) DEFAULT NULL COMMENT '订单状态',
  `proposer_id` int(11) DEFAULT NULL COMMENT '申请人ID',
  `proposer` varchar(50) DEFAULT NULL COMMENT '申请人',
  `proposer_at` datetime DEFAULT NULL COMMENT '申请时间',
  `creator_id` int(11) DEFAULT NULL COMMENT '创建人ID',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_at` datetime DEFAULT NULL COMMENT '创建时间',
  `audit_state` tinyint(4) DEFAULT NULL COMMENT '审核状态',
  `suggestion` varchar(200) DEFAULT NULL COMMENT '审核意见',
  `is_delete` tinyint(4) DEFAULT NULL COMMENT '是否删除',
  `paudit_id` int(11) DEFAULT NULL COMMENT '转交或加签给当前审核人ID的原始审核数据的ID',
  `pauditor_id` int(11) DEFAULT NULL COMMENT '转交或加签给当前审核人ID的ID',
  `pauditor` varchar(50) DEFAULT NULL COMMENT '转交或加签给当前审核人的人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='审核表';

/*Data for the table `tb_audit` */

/*Table structure for table `tb_audit_flow` */

DROP TABLE IF EXISTS `tb_audit_flow`;

CREATE TABLE `tb_audit_flow` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `audit_flow_no` varchar(50) NOT NULL DEFAULT '' COMMENT '审核流编号',
  `audit_flow_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '审核流类型',
  `audit_flow_name` varchar(100) NOT NULL DEFAULT '' COMMENT '审核流名称',
  `law_audit` varchar(10) NOT NULL DEFAULT '' COMMENT '待法务审核',
  `biz_audit` varchar(10) NOT NULL DEFAULT '' COMMENT '待商务审核',
  `career_audit` varchar(10) NOT NULL DEFAULT '' COMMENT '待事业部审核',
  `purchase_audit` varchar(10) NOT NULL DEFAULT '' COMMENT '待采购审核',
  `supply_chain_group_audit` varchar(10) NOT NULL DEFAULT '' COMMENT '待供应链小组审核',
  `supply_chain_service_audit` varchar(10) NOT NULL DEFAULT '' COMMENT '待供应链服务部审核',
  `goods_risk_audit` varchar(10) NOT NULL DEFAULT '' COMMENT '待商品风控审核',
  `busi_audit` varchar(10) NOT NULL DEFAULT '' COMMENT '待业务审核',
  `finance_audit` varchar(10) NOT NULL DEFAULT '' COMMENT '待财务专员审核',
  `finance2_audit` varchar(10) NOT NULL DEFAULT '' COMMENT '待财务主管审核',
  `risk_special_audit` varchar(10) NOT NULL DEFAULT '' COMMENT '待风控专员审核',
  `risk_audit` varchar(10) NOT NULL DEFAULT '' COMMENT '待风控主管审核',
  `dept_manage_audit` varchar(10) NOT NULL DEFAULT '' COMMENT '待部门主管审核',
  `boss_audit` varchar(10) NOT NULL DEFAULT '' COMMENT '待总经理审核',
  `is_first_risk` tinyint(4) NOT NULL DEFAULT '0' COMMENT '风控是否首单 0-否 1-是',
  `is_first_law` tinyint(4) NOT NULL DEFAULT '0' COMMENT '法务是否首单 0-否 1-是',
  `is_delete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除标记 0 : 有效 1 : 删除',
  `create_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8 COMMENT='审核流表';

/*Table structure for table `tb_base_account` */

DROP TABLE IF EXISTS `tb_base_account`;

CREATE TABLE `tb_base_account` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `subject_id` int(11) NOT NULL COMMENT '关联tb_base_subject表主键id',
  `account_type` tinyint(4) NOT NULL COMMENT '1:收货款 2:收费用 3:付货款 4:付费用',
  `bank_code` varchar(30) NOT NULL COMMENT '银行代码',
  `bank_name` varchar(100) NOT NULL COMMENT '开户银行',
  `account_no` varchar(100) NOT NULL COMMENT '开户账号',
  `bank_simple` varchar(100) DEFAULT NULL COMMENT '银行简介',
  `bank_address` varchar(100) DEFAULT NULL COMMENT '银行地址',
  `phone_number` varchar(20) NOT NULL COMMENT '电话',
  `default_currency` tinyint(4) NOT NULL COMMENT '默认币种 1：人民币 2：美元',
  `state` tinyint(4) NOT NULL COMMENT '1: 可用 2: 作废',
  `creator` varchar(50) NOT NULL COMMENT '操作人',
  `deleter` varchar(50) DEFAULT NULL COMMENT '作废人',
  `create_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `delete_at` datetime DEFAULT NULL COMMENT '作废时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `accountor` varchar(100) DEFAULT NULL COMMENT '开户人',
  `iban` varchar(50) DEFAULT '' COMMENT 'IBAN',
  `capital_account_type` tinyint(4) DEFAULT NULL COMMENT '资金占用1:共享账号  2:独立核算',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='经营单位、仓库、供应商、客户账户信息';

/*Data for the table `tb_base_account` */

/*Table structure for table `tb_base_address` */

DROP TABLE IF EXISTS `tb_base_address`;

CREATE TABLE `tb_base_address` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `subject_id` int(11) NOT NULL COMMENT '关联tb_base_subject表主键id',
  `address_type` tinyint(4) NOT NULL COMMENT '1:固定地址 2:临时地址',
  `nation_id` int(11) DEFAULT NULL COMMENT '国家',
  `nation_name` varchar(50) DEFAULT NULL COMMENT '国家名称',
  `province_id` int(11) DEFAULT NULL COMMENT '省',
  `province_name` varchar(50) DEFAULT NULL COMMENT '省名称',
  `city_id` int(11) DEFAULT NULL COMMENT '市',
  `city_name` varchar(50) DEFAULT NULL COMMENT '市名称',
  `county_id` int(11) DEFAULT NULL COMMENT '县',
  `county_name` varchar(50) DEFAULT NULL COMMENT '县名称',
  `address_detail` varchar(200) NOT NULL COMMENT '地址',
  `contact_person` varchar(50) NOT NULL COMMENT '联系人',
  `mobile_phone` varchar(20) NOT NULL COMMENT '手机',
  `telephone` varchar(20) DEFAULT NULL COMMENT '电话',
  `fax` varchar(20) DEFAULT NULL COMMENT '传真',
  `note` varchar(200) DEFAULT NULL COMMENT '备注',
  `creator` varchar(50) NOT NULL COMMENT '操作人',
  `deleter` varchar(50) DEFAULT NULL COMMENT '作废人',
  `create_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `delete_at` datetime DEFAULT NULL COMMENT '作废时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `state` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='经营单位、仓库、供应商、客户地址信息';

/*Data for the table `tb_base_address` */

/*Table structure for table `tb_base_department` */

DROP TABLE IF EXISTS `tb_base_department`;

CREATE TABLE `tb_base_department` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(50) DEFAULT NULL COMMENT '名称',
  `number` varchar(50) DEFAULT NULL COMMENT '编号',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `creator_id` int(11) DEFAULT NULL COMMENT '创建人ID',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `remark` varchar(50) DEFAULT NULL COMMENT '备注',
  `is_delete` tinyint(4) DEFAULT '0' COMMENT '逻辑删除标记',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '上级部门ID',
  `level` tinyint(4) DEFAULT NULL COMMENT '级别',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='部门表';

/*Data for the table `tb_base_department` */

/*Table structure for table `tb_base_exchange_rate` */

DROP TABLE IF EXISTS `tb_base_exchange_rate`;

CREATE TABLE `tb_base_exchange_rate` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '汇率ID',
  `bank` varchar(20) DEFAULT NULL COMMENT '银行名称',
  `currency` varchar(20) DEFAULT NULL COMMENT '币种',
  `foreign_currency` varchar(20) DEFAULT NULL COMMENT '外币种',
  `cash_selling_price` decimal(20,8) DEFAULT NULL COMMENT '现钞卖出价',
  `cash_buying_price` decimal(20,8) DEFAULT NULL COMMENT '现钞买入价',
  `draft_selling_price` decimal(20,8) DEFAULT NULL COMMENT '电汇卖出价',
  `draft_buying_price` decimal(20,8) DEFAULT NULL COMMENT '电汇买入价',
  `publish_at` datetime DEFAULT NULL COMMENT '发布时间',
  `creator` varchar(100) DEFAULT NULL COMMENT '创建人',
  `backup_person` varchar(20) DEFAULT NULL COMMENT '备份人',
  `deleter` varchar(50) DEFAULT NULL COMMENT '作废人',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `backup_at` datetime DEFAULT NULL COMMENT '备份时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `delete_at` datetime DEFAULT NULL COMMENT '作废时间',
  `is_delete` tinyint(4) DEFAULT '0' COMMENT '删除标记',
  `is_error` tinyint(4) DEFAULT '0' COMMENT '异常标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3343 DEFAULT CHARSET=utf8 COMMENT='银行汇率表';

/*Table structure for table `tb_base_goods` */

DROP TABLE IF EXISTS `tb_base_goods`;

CREATE TABLE `tb_base_goods` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `number` varchar(50) DEFAULT NULL COMMENT '编号',
  `name` varchar(255) DEFAULT NULL COMMENT '名称',
  `TYPE` varchar(500) NOT NULL DEFAULT '' COMMENT '型号',
  `bar_code` varchar(20) DEFAULT NULL COMMENT '条码',
  `specification` varchar(50) DEFAULT NULL COMMENT '规格',
  `tax_classification` varchar(50) DEFAULT NULL COMMENT '税收分类',
  `tax_rate` decimal(8,4) DEFAULT NULL COMMENT '国内税率',
  `unit` varchar(20) DEFAULT NULL COMMENT '单位',
  `volume` decimal(20,8) DEFAULT '0.00000000' COMMENT '体积',
  `grow` decimal(20,8) DEFAULT '0.00000000' COMMENT '长',
  `broad` decimal(20,8) DEFAULT '0.00000000' COMMENT '宽',
  `gross_weight` decimal(20,8) DEFAULT '0.00000000' COMMENT '毛重',
  `net_weight` decimal(20,8) DEFAULT '0.00000000' COMMENT '净重',
  `purchase_price` decimal(20,8) DEFAULT '0.00000000' COMMENT '采购指导价',
  `sale_price` decimal(20,8) DEFAULT '0.00000000' COMMENT '销售指导价',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `deleter` varchar(50) DEFAULT NULL COMMENT '作废人',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `delete_at` datetime DEFAULT NULL COMMENT '作废时间',
  `is_delete` tinyint(4) DEFAULT '0' COMMENT '删除标记',
  `locker` varchar(50) DEFAULT NULL COMMENT '锁定人',
  `lock_at` date DEFAULT NULL COMMENT '锁定时间',
  `sale_currency_type` tinyint(4) DEFAULT NULL COMMENT '销售指导价币种',
  `pur_currency_type` tinyint(4) DEFAULT NULL COMMENT '采购指导价币种',
  `brand` varchar(30) DEFAULT NULL COMMENT '品牌',
  `good_type` tinyint(4) DEFAULT '0' COMMENT '商品类型，0 普通商品 1 铺货商品',
  `department_id` bigint(20) DEFAULT NULL COMMENT '事业部,部门id',
  `supplier_id` bigint(20) DEFAULT NULL COMMENT '供应商ID',
  `pledge` decimal(20,8) DEFAULT '0.00000000' COMMENT '质押比例',
  `career_id` bigint(20) DEFAULT NULL COMMENT '事业部审核用户节点',
  `purchase_id` bigint(20) DEFAULT NULL COMMENT '采购部审核用户节点',
  `supply_chain_group_id` bigint(20) DEFAULT NULL COMMENT '供应链小组审核用户节点',
  `supply_chain_service_id` bigint(20) DEFAULT NULL COMMENT '供应链服务部审核用户节点',
  `risk_id` bigint(20) DEFAULT NULL COMMENT '风控审核用户节点',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品表';

/*Data for the table `tb_base_goods` */

/*Table structure for table `tb_base_invoice` */

DROP TABLE IF EXISTS `tb_base_invoice`;

CREATE TABLE `tb_base_invoice` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `subject_id` int(11) NOT NULL COMMENT '关联tb_base_subject表主键id',
  `tax_payer` varchar(100) NOT NULL COMMENT '纳税人识别号',
  `bank_name` varchar(100) NOT NULL COMMENT '开户银行',
  `account_no` varchar(50) NOT NULL COMMENT '开户账号',
  `address` varchar(200) NOT NULL COMMENT '开票地址',
  `phone_number` varchar(20) NOT NULL COMMENT '开票电话',
  `state` tinyint(4) NOT NULL COMMENT '0:可用 1:作废',
  `creator` varchar(50) NOT NULL COMMENT '操作人',
  `deleter` varchar(50) DEFAULT NULL,
  `create_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `delete_at` datetime DEFAULT NULL COMMENT '作废时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='经营单位、仓库、供应商、客户开票信息';

/*Data for the table `tb_base_invoice` */

/*Table structure for table `tb_base_permission` */

DROP TABLE IF EXISTS `tb_base_permission`;

CREATE TABLE `tb_base_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT ' 权限ID',
  `type` tinyint(4) DEFAULT NULL COMMENT '权限类型',
  `name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '权限名称',
  `parent_id` bigint(20) DEFAULT NULL COMMENT ' 父权限ID',
  `url` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '权限URL',
  `menu_level` tinyint(4) DEFAULT NULL COMMENT '菜单级别',
  `ord` int(11) DEFAULT NULL COMMENT '菜单顺序',
  `state` tinyint(4) DEFAULT NULL COMMENT '状态',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT ' 创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `delete_at` datetime DEFAULT NULL COMMENT '作废时间',
  `creator` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '创建人',
  `deleter` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '作废人',
  `is_delete` tinyint(4) DEFAULT '0' COMMENT '逻辑删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1475 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='权限表';

/*Table structure for table `tb_base_permission_group` */

DROP TABLE IF EXISTS `tb_base_permission_group`;

CREATE TABLE `tb_base_permission_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '权限组名称',
  `state` tinyint(4) DEFAULT NULL COMMENT 'state',
  `creator` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '创建人',
  `deleter` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '作废人',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `delete_at` datetime DEFAULT NULL COMMENT '作废时间',
  `is_delete` tinyint(4) DEFAULT '0' COMMENT '逻辑删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=149 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='权限组表';

/*Table structure for table `tb_base_permission_relation` */

DROP TABLE IF EXISTS `tb_base_permission_relation`;

CREATE TABLE `tb_base_permission_relation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `permission_group_id` bigint(20) DEFAULT NULL COMMENT '权限组ID',
  `permission_id` bigint(20) DEFAULT NULL COMMENT '权限ID',
  `deleter` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '作废人',
  `creator` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '创建人',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `delete_at` datetime DEFAULT NULL COMMENT '作废时间',
  `is_delete` tinyint(4) DEFAULT '0' COMMENT '逻辑删除标记',
  PRIMARY KEY (`id`),
  KEY `IDX_ROLE_PERMISSION_UP` (`permission_id`,`permission_group_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3239 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='权限关联表';

/*Table structure for table `tb_base_project` */

DROP TABLE IF EXISTS `tb_base_project`;

CREATE TABLE `tb_base_project` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '项目ID',
  `project_no` varchar(50) DEFAULT NULL COMMENT '编号',
  `project_name` varchar(50) DEFAULT NULL COMMENT '项目名称（简称）',
  `full_name` varchar(100) DEFAULT NULL COMMENT '全称',
  `business_unit_id` bigint(20) DEFAULT NULL COMMENT '经营单位',
  `total_amount` decimal(20,4) DEFAULT NULL COMMENT '额度总额',
  `amount_unit` tinyint(4) DEFAULT NULL COMMENT '额度币种',
  `biz_type` varchar(20) DEFAULT NULL COMMENT '业务类别',
  `law_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '法务主管',
  `biz_special_id` bigint(20) DEFAULT NULL COMMENT '业务专员',
  `biz_manager_id` bigint(20) DEFAULT NULL COMMENT '业务主管',
  `business_manager_id` bigint(20) DEFAULT NULL COMMENT '商务主管',
  `finance_manager_id` bigint(20) DEFAULT NULL COMMENT '财务主管',
  `risk_special_id` bigint(20) DEFAULT NULL COMMENT '风控专员',
  `risk_manager_id` bigint(20) DEFAULT NULL COMMENT '风控主管',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `deleter` varchar(50) DEFAULT NULL COMMENT '作废人',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `delete_at` datetime DEFAULT NULL COMMENT '作废时间',
  `is_delete` tinyint(4) DEFAULT '0' COMMENT '删除标记',
  `industrial` tinyint(4) DEFAULT NULL COMMENT '行业',
  `project_no_type` char(4) DEFAULT NULL COMMENT '项目编号类型',
  `finance_special_id` bigint(20) DEFAULT NULL COMMENT '财务专员',
  `department_id` bigint(20) DEFAULT NULL COMMENT '部门id',
  `is_pledge` tinyint(4) DEFAULT NULL COMMENT '是否质押 0-否 1-是',
  `department_manager_id` bigint(20) DEFAULT NULL COMMENT '部门主管',
  `boss_id` binary(20) DEFAULT NULL COMMENT '总经理',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='项目表';

/*Data for the table `tb_base_project` */

/*Table structure for table `tb_base_role` */

DROP TABLE IF EXISTS `tb_base_role`;

CREATE TABLE `tb_base_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '角色名称',
  `state` tinyint(4) DEFAULT NULL COMMENT '状态',
  `creator` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '创建人',
  `deleter` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '作废人',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `delete_at` datetime DEFAULT NULL COMMENT '作废时间',
  `is_delete` tinyint(4) DEFAULT '0' COMMENT '逻辑删除标记',
  `locker` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '锁定人',
  `lock_at` datetime DEFAULT NULL COMMENT '锁定时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='角色表';

/*Table structure for table `tb_base_role_permission_group` */

DROP TABLE IF EXISTS `tb_base_role_permission_group`;

CREATE TABLE `tb_base_role_permission_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色ID',
  `permission_group_id` bigint(20) DEFAULT NULL COMMENT '权限组ID',
  `creator` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '创建人',
  `deleter` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '作废人',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `delete_at` datetime DEFAULT NULL COMMENT '作废时间',
  `is_delete` tinyint(4) DEFAULT NULL COMMENT '逻辑删除标记',
  PRIMARY KEY (`id`),
  KEY `IDX_ROLE_PERMISSION_UP` (`role_id`,`permission_group_id`)
) ENGINE=InnoDB AUTO_INCREMENT=812 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='角色权限组关联表';

/*Table structure for table `tb_base_subject` */

DROP TABLE IF EXISTS `tb_base_subject`;

CREATE TABLE `tb_base_subject` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `subject_type` tinyint(4) NOT NULL COMMENT '1:经营单位; 2:仓库; 4:供应商; 8:客户',
  `subject_no` varchar(8) NOT NULL COMMENT '主类型编号',
  `abbreviation` varchar(50) NOT NULL COMMENT '简称',
  `chinese_name` varchar(100) NOT NULL COMMENT '中文全称',
  `english_name` varchar(100) NOT NULL COMMENT '英文全称',
  `reg_place` varchar(200) DEFAULT NULL COMMENT '注册地',
  `reg_no` varchar(20) DEFAULT NULL COMMENT '注册号',
  `reg_phone` varchar(20) DEFAULT NULL COMMENT '注册电话',
  `office_address` varchar(200) DEFAULT NULL COMMENT '办公地址',
  `supplier_type` tinyint(4) DEFAULT NULL COMMENT '1:已有供应商 2:新建供应商',
  `oms_supplier_no` varchar(20) DEFAULT NULL COMMENT 'OMS供应商编号',
  `nation` tinyint(4) DEFAULT NULL,
  `warehouse_type` tinyint(4) DEFAULT NULL COMMENT '1:自营仓 2:客户仓 3:虚拟仓   当实体类型为仓库时不能为空',
  `warehouse_no` varchar(20) DEFAULT NULL COMMENT '当实体类型为仓库时不能为空',
  `cust_type` tinyint(4) DEFAULT NULL COMMENT '1 : 已有客户 2 : 新增客户  当实体类型为客户时不能为空',
  `pms_cust_no` varchar(20) DEFAULT NULL COMMENT '当客户类型为已有客户时不能为空',
  `state` tinyint(4) NOT NULL COMMENT '1: 待提交 2: 已完成 3: 已锁定',
  `creator` varchar(50) NOT NULL COMMENT '创建人',
  `deleter` varchar(50) DEFAULT NULL COMMENT '作废人',
  `locked_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '锁定人',
  `create_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `delete_at` datetime DEFAULT NULL COMMENT '作废时间',
  `lock_at` datetime DEFAULT NULL COMMENT '锁定时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_delete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0 : 有效 1 : 删除',
  `business_unit_code` varchar(50) DEFAULT NULL COMMENT '公司法人编号',
  `pms_supplier_code` varchar(50) NOT NULL DEFAULT '' COMMENT 'pms结算对象(供应商编码)',
  `invoice_quota_type` decimal(20,2) DEFAULT '0.00' COMMENT '开票限额 千;万;十万;百万;千万',
  `finance_manager_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '财务主管',
  `department_manager_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '部门主管',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='经营单位、仓库、供应商、客户基本信息';

/*Data for the table `tb_base_subject` */

/*Table structure for table `tb_base_user` */

DROP TABLE IF EXISTS `tb_base_user`;

CREATE TABLE `tb_base_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `employee_number` varchar(20) DEFAULT NULL COMMENT '工号',
  `user_name` varchar(50) DEFAULT NULL COMMENT '用户名',
  `password` varchar(150) DEFAULT NULL COMMENT '密码',
  `chinese_name` varchar(50) DEFAULT NULL COMMENT '中文名',
  `english_name` varchar(50) DEFAULT NULL COMMENT '英文名',
  `mobile_phone` varchar(20) DEFAULT NULL COMMENT '手机',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `deleter` varchar(50) DEFAULT NULL COMMENT '作废人',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `delete_at` datetime DEFAULT NULL COMMENT '作废时间',
  `is_delete` tinyint(4) DEFAULT '0' COMMENT '删除标记',
  `type` tinyint(4) DEFAULT NULL COMMENT '类型',
  `creatorId` bigint(20) DEFAULT NULL,
  `rtx_code` varchar(50) DEFAULT NULL COMMENT 'RTX号码',
  `user_property` tinyint(4) DEFAULT '0' COMMENT '用户类别',
  `department_id` bigint(20) DEFAULT NULL COMMENT '部门id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_name` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='用户表';

/*Table structure for table `tb_base_user_project` */

DROP TABLE IF EXISTS `tb_base_user_project`;

CREATE TABLE `tb_base_user_project` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `project_id` bigint(20) DEFAULT NULL COMMENT '项目ID',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `assigner` varchar(50) DEFAULT NULL COMMENT '分配人',
  `deleter` varchar(50) DEFAULT NULL COMMENT '作废人',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `assign_at` datetime DEFAULT NULL COMMENT '分配时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `delete_at` datetime DEFAULT NULL COMMENT '作废时间',
  `state` tinyint(4) DEFAULT NULL COMMENT '状态',
  `is_delete` tinyint(4) DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `idx_tb_base_user_project_user_project_id` (`user_id`,`project_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户项目表';

/*Data for the table `tb_base_user_project` */

/*Table structure for table `tb_base_user_roles` */

DROP TABLE IF EXISTS `tb_base_user_roles`;

CREATE TABLE `tb_base_user_roles` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色ID',
  `state` tinyint(4) DEFAULT NULL COMMENT '分配状态',
  `creator` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '创建人',
  `deleter` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '作废人',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `delete_at` datetime DEFAULT NULL COMMENT '作废时间',
  `is_delete` tinyint(4) unsigned zerofill DEFAULT '0000' COMMENT '逻辑删除标记',
  PRIMARY KEY (`id`),
  KEY `IDX_USER_ROLES_UR` (`user_id`,`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='用户角色关联表';

/*Table structure for table `tb_base_user_subject` */

DROP TABLE IF EXISTS `tb_base_user_subject`;

CREATE TABLE `tb_base_user_subject` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户仓库关系',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `subject_id` bigint(20) DEFAULT NULL COMMENT '主体',
  `subject_type` tinyint(4) DEFAULT NULL COMMENT '1:经营单位; 2:仓库; 4:供应商; 8:客户',
  `operater` tinyint(4) DEFAULT '0' COMMENT '仓管操作;0 否 1 是',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人',
  `assigner_id` bigint(20) DEFAULT NULL COMMENT '分配人',
  `deleter_id` bigint(20) DEFAULT NULL COMMENT '作废人',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `assign_at` datetime DEFAULT NULL COMMENT '分配时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `delete_at` datetime DEFAULT NULL COMMENT '作废时间',
  `state` tinyint(4) DEFAULT NULL COMMENT '状态',
  `is_delete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户仓库和基本信息关系表';

/*Data for the table `tb_base_user_subject` */

/*Table structure for table `tb_bill_delivery` */

DROP TABLE IF EXISTS `tb_bill_delivery`;

CREATE TABLE `tb_bill_delivery` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '提货单ID',
  `bill_no` varchar(20) DEFAULT NULL COMMENT '提货单编号',
  `bill_type` tinyint(4) DEFAULT NULL COMMENT '提货类型 1-销售提货 2-销售退货',
  `affiliate_no` varchar(200) DEFAULT NULL COMMENT '提货附属编号',
  `project_id` bigint(20) DEFAULT NULL COMMENT '项目ID,关联tb_base_subject[id]',
  `warehouse_id` bigint(20) DEFAULT NULL COMMENT '仓库ID,关联tb_base_subject[id]',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '客户ID,关联tb_base_subject[id]',
  `STATUS` tinyint(4) DEFAULT '1' COMMENT '状态 1-待提交 30-待财务主管审核 25-待财务专员审核 4-待发货 5-已发货',
  `delivery_date` date DEFAULT NULL COMMENT '提货单日期',
  `required_send_date` date DEFAULT NULL COMMENT '应发货日期',
  `required_send_num` decimal(20,8) DEFAULT '0.00000000' COMMENT '应发货数量',
  `required_send_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '应发货金额',
  `cost_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '成本金额',
  `po_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '订单金额',
  `customer_address_id` bigint(20) DEFAULT NULL COMMENT '客户地址ID',
  `transfer_mode` tinyint(4) DEFAULT NULL COMMENT '运输方式 1-自提 2- 市内运输 3-汽运 4-铁运 5-空运',
  `currency_type` tinyint(4) DEFAULT NULL COMMENT '币种 1.人民币 2.美元 3.港币',
  `exchange_rate` decimal(20,8) DEFAULT '0.00000000' COMMENT '汇率',
  `wms_status` tinyint(4) DEFAULT '0' COMMENT '调用WMS接口 0-未调用 1-已调用 ',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `print_num` int(10) DEFAULT '0' COMMENT '打印次数',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleter_id` bigint(20) DEFAULT NULL COMMENT '作废人ID',
  `deleter` varchar(50) DEFAULT NULL COMMENT '作废人',
  `is_delete` tinyint(4) DEFAULT '0' COMMENT '删除标记 0 : 有效 1 : 删除',
  `delete_at` datetime DEFAULT NULL COMMENT '删除时间',
  `sign_standard` tinyint(4) DEFAULT NULL COMMENT '签收标准 0-身份证 1-公章 2-身份证+公章',
  `certificate_id` varchar(50) DEFAULT NULL COMMENT '身份证号码',
  `certificate_name` varchar(50) DEFAULT NULL COMMENT '姓名',
  `official_seal` varchar(100) DEFAULT NULL COMMENT '公章名',
  `pay_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '付款金额',
  `return_time` datetime DEFAULT NULL COMMENT '回款时间',
  `whole_return_time` datetime DEFAULT NULL COMMENT '全部回款时间',
  `submit_time` datetime DEFAULT NULL COMMENT '提交时间',
  `submitter_id` bigint(20) DEFAULT NULL COMMENT '提交人ID',
  `submitter` varchar(50) DEFAULT NULL COMMENT '提交人',
  `is_change_price` tinyint(4) DEFAULT '0' COMMENT '是否改价 0-未改价 1-改价',
  `receive_project_id` int(11) DEFAULT NULL COMMENT '接收项目Id',
  `receive_warehouse_id` int(11) DEFAULT NULL COMMENT '接收项目下的接收仓库Id',
  `receive_supplier_id` int(11) DEFAULT NULL COMMENT '接收项目下的接收供应商Id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='提货单表';

/*Data for the table `tb_bill_delivery` */

/*Table structure for table `tb_bill_delivery_dtl` */

DROP TABLE IF EXISTS `tb_bill_delivery_dtl`;

CREATE TABLE `tb_bill_delivery_dtl` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '提货单明细ID',
  `bill_delivery_id` bigint(20) DEFAULT NULL COMMENT '提货单ID,关联tb_bill_delivery[id]',
  `goods_id` bigint(20) DEFAULT NULL COMMENT '商品ID,关联tb_base_goods[id]',
  `required_send_num` decimal(20,8) DEFAULT '0.00000000' COMMENT '应发货数量',
  `required_send_price` decimal(20,8) DEFAULT '0.00000000' COMMENT '应发货单价',
  `stl_id` bigint(20) DEFAULT NULL COMMENT '库存ID,关联tb_stl[id]',
  `batch_no` varchar(50) DEFAULT NULL COMMENT '批次',
  `goods_status` tinyint(4) DEFAULT '1' COMMENT '商品状态 1-常规 2-残次品',
  `cost_price` decimal(20,8) DEFAULT '0.00000000' COMMENT '成本单价',
  `po_price` decimal(20,8) DEFAULT '0.00000000' COMMENT '订单单价',
  `provide_invoice_num` decimal(20,8) DEFAULT '0.00000000' COMMENT '开票数量',
  `provide_invoice_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '开票金额',
  `accept_invoice_num` decimal(20,8) DEFAULT '0.00000000' COMMENT '收票数量',
  `accept_invoice_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '收票金额',
  `assign_stl_flag` tinyint(4) DEFAULT '0' COMMENT '是否指定库存 0-不指定 1-指定',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `pay_price` decimal(20,8) DEFAULT '0.00000000' COMMENT '付款单价',
  `pay_rate` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '付款汇率',
  `pay_real_currency` tinyint(4) NOT NULL DEFAULT '1' COMMENT '付款实际支付币种',
  `pay_time` datetime DEFAULT NULL COMMENT '付款时间',
  `profit_price` decimal(20,8) DEFAULT '0.00000000' COMMENT '利润(服务)单价',
  `sale_guide_price` decimal(20,8) DEFAULT '0.00000000' COMMENT '销售指导价',
  `bill_out_store_id` bigint(20) DEFAULT NULL COMMENT '关联出库单ID',
  `bill_out_store_dtl_id` bigint(20) DEFAULT NULL COMMENT '关联出库单明细ID',
  `bill_out_store_pick_dtl_id` bigint(20) DEFAULT NULL COMMENT '关联出库单拣货明细ID',
  `origin_send_price` decimal(20,8) DEFAULT '0.00000000' COMMENT '原发货单价(退货)',
  PRIMARY KEY (`id`),
  KEY `idx_tb_bill_delivery_dtl_bill_delivery_id` (`bill_delivery_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='提货单明细表';

/*Data for the table `tb_bill_delivery_dtl` */

/*Table structure for table `tb_bill_in_store` */

DROP TABLE IF EXISTS `tb_bill_in_store`;

CREATE TABLE `tb_bill_in_store` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `bill_no` varchar(20) DEFAULT NULL COMMENT '入库编号',
  `bill_type` tinyint(4) DEFAULT NULL COMMENT '入库类型 1-采购 2-调拨 3-销售退货',
  `affiliate_no` varchar(200) DEFAULT NULL COMMENT '入库附属编号',
  `project_id` bigint(20) DEFAULT NULL COMMENT '项目ID',
  `supplier_id` bigint(20) DEFAULT NULL COMMENT '供应商ID',
  `warehouse_id` bigint(20) DEFAULT NULL COMMENT '仓库ID',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '客户ID',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态 1-待提交 2-已收货',
  `receive_date` date DEFAULT NULL COMMENT '收货日期',
  `receive_num` decimal(20,8) DEFAULT '0.00000000' COMMENT '收货数量',
  `receive_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '收货金额',
  `tally_num` decimal(20,8) DEFAULT '0.00000000' COMMENT '理货数量',
  `tally_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '理货金额',
  `acceptor_id` bigint(20) DEFAULT NULL COMMENT '入库人ID',
  `acceptor` varchar(50) DEFAULT NULL COMMENT '入库人',
  `accept_time` datetime DEFAULT NULL COMMENT '入库时间',
  `bill_out_store_id` bigint(20) DEFAULT NULL COMMENT '关联出库单ID',
  `currency_type` tinyint(4) DEFAULT NULL COMMENT '币种 1.人民币 2.美元 3.港币',
  `exchange_rate` decimal(20,8) DEFAULT '0.00000000' COMMENT '汇率',
  `wms_status` tinyint(4) DEFAULT '0' COMMENT '调用WMS接口 0-未调用 1-已调用 ',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `print_num` int(10) DEFAULT '0' COMMENT '打印次数',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人ID ',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleter_id` bigint(20) DEFAULT NULL COMMENT '作废人ID',
  `deleter` varchar(50) DEFAULT NULL COMMENT '作废人',
  `is_delete` tinyint(4) DEFAULT '0' COMMENT '删除标记 0 : 有效 1 : 删除',
  `delete_at` datetime DEFAULT NULL COMMENT '删除时间',
  `pay_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '付款金额',
  `return_num` decimal(20,8) DEFAULT '0.00000000' COMMENT '退货数量',
  `bill_delivery_id` bigint(20) DEFAULT NULL COMMENT '关联销售退货单ID',
  PRIMARY KEY (`id`),
  KEY `idx_tb_bill_in_store_bill_no` (`bill_no`,`affiliate_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='入库单表';

/*Data for the table `tb_bill_in_store` */

/*Table structure for table `tb_bill_in_store_dtl` */

DROP TABLE IF EXISTS `tb_bill_in_store_dtl`;

CREATE TABLE `tb_bill_in_store_dtl` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `bill_in_store_id` bigint(20) DEFAULT NULL COMMENT '入库单ID',
  `po_dtl_id` bigint(20) DEFAULT NULL COMMENT 'PO订单明细ID',
  `po_id` bigint(20) DEFAULT NULL COMMENT 'PO订单ID',
  `bill_out_store_id` bigint(20) DEFAULT NULL COMMENT '关联出库单ID',
  `bill_out_store_dtl_id` bigint(20) DEFAULT NULL COMMENT '关联出库单明细ID',
  `goods_id` bigint(20) DEFAULT NULL COMMENT '商品ID',
  `receive_num` decimal(20,8) DEFAULT '0.00000000' COMMENT '收货数量',
  `tally_num` decimal(20,8) DEFAULT '0.00000000' COMMENT '理货数量',
  `receive_price` decimal(20,8) DEFAULT '0.00000000' COMMENT '收货单价',
  `po_price` decimal(20,8) DEFAULT '0.00000000' COMMENT '订单单价',
  `cost_price` decimal(20,8) DEFAULT '0.00000000' COMMENT '成本单价',
  `bill_in_store_dtl_id` bigint(20) DEFAULT NULL COMMENT '关联入库单明细ID',
  `accept_time` datetime DEFAULT NULL COMMENT '入库时间',
  `origin_accept_time` datetime DEFAULT NULL COMMENT '原入库时间',
  `batch_no` varchar(50) DEFAULT NULL COMMENT '批次',
  `receive_date` date DEFAULT NULL COMMENT '收货日期',
  `currency_type` tinyint(4) DEFAULT NULL COMMENT '币种 1.人民币 2.美元 3.港币',
  `exchange_rate` decimal(20,8) DEFAULT NULL COMMENT '汇率',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '客户ID',
  `supplier_id` bigint(20) DEFAULT NULL COMMENT '供应商ID',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `pay_price` decimal(20,8) DEFAULT '0.00000000' COMMENT '付款单价',
  `pay_rate` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '付款汇率',
  `pay_real_currency` tinyint(4) NOT NULL DEFAULT '1' COMMENT '付款实际支付币种',
  `pay_time` datetime DEFAULT NULL COMMENT '付款时间',
  `bill_delivery_id` bigint(20) DEFAULT NULL COMMENT '关联销售退货单ID',
  `bill_delivery_dtl_id` bigint(20) DEFAULT NULL COMMENT '关联销售退货单明细ID',
  PRIMARY KEY (`id`),
  KEY `idx_tb_bill_in_store_dtl_bill_in_store_id` (`bill_in_store_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='入库单明细表';

/*Data for the table `tb_bill_in_store_dtl` */

/*Table structure for table `tb_bill_in_store_tally_dtl` */

DROP TABLE IF EXISTS `tb_bill_in_store_tally_dtl`;

CREATE TABLE `tb_bill_in_store_tally_dtl` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `bill_in_store_id` bigint(20) DEFAULT NULL COMMENT '入库单ID',
  `bill_in_store_dtl_id` bigint(20) DEFAULT NULL COMMENT '入库单明细ID ',
  `po_dtl_id` bigint(20) DEFAULT NULL COMMENT 'PO订单明细ID',
  `po_id` bigint(20) DEFAULT NULL COMMENT 'PO订单ID',
  `bill_out_store_id` bigint(20) DEFAULT NULL COMMENT '关联出库单ID',
  `bill_out_store_dtl_id` bigint(20) DEFAULT NULL COMMENT '关联出库单拣货明细ID',
  `goods_id` bigint(20) DEFAULT NULL COMMENT '商品ID',
  `tally_num` decimal(20,8) DEFAULT '0.00000000' COMMENT '理货数量',
  `receive_price` decimal(20,8) DEFAULT '0.00000000' COMMENT '收货单价',
  `po_price` decimal(20,8) DEFAULT '0.00000000' COMMENT '订单单价',
  `cost_price` decimal(20,8) DEFAULT '0.00000000' COMMENT '成本单价',
  `batch_no` varchar(50) DEFAULT NULL COMMENT '批次',
  `goods_status` tinyint(4) DEFAULT '1' COMMENT '商品状态 1-常规 2-残次品',
  `accept_time` datetime DEFAULT NULL COMMENT '入库时间',
  `origin_accept_time` datetime DEFAULT NULL COMMENT '原入库时间',
  `receive_date` date DEFAULT NULL COMMENT '收货日期',
  `currency_type` tinyint(4) DEFAULT NULL COMMENT '币种 1.人民币 2.美元 3.港币',
  `exchange_rate` decimal(20,8) DEFAULT '0.00000000' COMMENT '汇率',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '客户ID',
  `supplier_id` bigint(20) DEFAULT NULL COMMENT '供应商ID',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `pay_price` decimal(20,8) DEFAULT '0.00000000' COMMENT '付款单价',
  `pay_rate` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '付款汇率',
  `pay_real_currency` tinyint(4) NOT NULL DEFAULT '1' COMMENT '付款实际支付币种',
  `pay_time` datetime DEFAULT NULL COMMENT '付款时间',
  `return_num` decimal(20,8) DEFAULT '0.00000000' COMMENT '采购退货数量',
  `bill_delivery_id` bigint(20) DEFAULT NULL COMMENT '关联销售退货单ID',
  `bill_delivery_dtl_id` bigint(20) DEFAULT NULL COMMENT '关联销售退货单明细ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='入库单理货明细表';

/*Data for the table `tb_bill_in_store_tally_dtl` */

/*Table structure for table `tb_bill_out_store` */

DROP TABLE IF EXISTS `tb_bill_out_store`;

CREATE TABLE `tb_bill_out_store` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '出库单ID',
  `bill_no` varchar(20) DEFAULT NULL COMMENT '出库编号',
  `bill_type` tinyint(4) DEFAULT NULL COMMENT '出库类型 1-销售 2-调拨 3-借货 4-还货 5-采购退货',
  `affiliate_no` varchar(200) DEFAULT NULL COMMENT '出库附属编号',
  `project_id` bigint(20) DEFAULT NULL COMMENT '项目ID',
  `warehouse_id` bigint(20) DEFAULT NULL COMMENT '仓库ID',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '客户ID',
  `receive_warehouse_id` bigint(20) DEFAULT NULL COMMENT '接收仓库ID',
  `STATUS` tinyint(4) DEFAULT '1' COMMENT '状态 1-待提交 30-待财务主管审核 25-待财务专员审核 4-待发货 5-已发货',
  `required_send_date` date DEFAULT NULL COMMENT '要求发货日期',
  `send_date` date DEFAULT NULL COMMENT '发货日期',
  `bill_delivery_id` bigint(20) DEFAULT NULL COMMENT '提货单ID',
  `send_num` decimal(20,8) DEFAULT '0.00000000' COMMENT '发货数量',
  `send_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '发货金额',
  `pickup_num` decimal(20,8) DEFAULT '0.00000000' COMMENT '拣货数量',
  `pickup_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '拣货金额',
  `cost_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '成本金额',
  `po_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '订单金额',
  `customer_address_id` bigint(20) DEFAULT NULL COMMENT '出库客户地址ID',
  `transfer_mode` tinyint(4) DEFAULT NULL COMMENT '运输方式 1-自提 2- 市内运输 3-汽运 4-铁运 5-空运',
  `deliver_id` bigint(20) DEFAULT NULL COMMENT '出库人ID',
  `deliverer` varchar(50) DEFAULT NULL COMMENT '出库人',
  `deliver_time` datetime DEFAULT NULL COMMENT '单据出库时间',
  `system_deliver_time` datetime DEFAULT NULL COMMENT '系统出库时间',
  `currency_type` tinyint(4) DEFAULT NULL COMMENT '币种 1.人民币 2.美元 3.港币',
  `exchange_rate` decimal(20,8) DEFAULT '0.00000000' COMMENT '汇率',
  `reason_type` tinyint(4) DEFAULT NULL COMMENT '原因 类型为调拨出库时为调拨原因',
  `wms_status` tinyint(4) DEFAULT '0' COMMENT '调用WMS接口 0-未调用 1-已调用 ',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `print_num` int(10) DEFAULT '0' COMMENT '打印次数',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleter_id` bigint(20) DEFAULT NULL COMMENT '作废人ID',
  `deleter` varchar(50) DEFAULT NULL COMMENT '作废人',
  `is_delete` tinyint(4) DEFAULT '0' COMMENT '删除标记 0 : 有效 1 : 删除',
  `delete_at` datetime DEFAULT NULL COMMENT '删除时间',
  `sign_standard` tinyint(4) DEFAULT NULL COMMENT '签收标准 0-身份证 1-公章 2-身份证+公章',
  `certificate_id` varchar(50) DEFAULT NULL COMMENT '身份证号码',
  `certificate_name` varchar(50) DEFAULT NULL COMMENT '姓名',
  `official_seal` varchar(100) DEFAULT NULL COMMENT '公章名',
  `pay_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '付款金额',
  `received_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '回款金额',
  `fund_back_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '资金归还金额',
  `return_num` decimal(20,8) DEFAULT '0.00000000' COMMENT '退货数量',
  `po_return_id` bigint(20) DEFAULT NULL COMMENT '关联采购退货单ID',
  PRIMARY KEY (`id`),
  KEY `idx_tb_bill_out_store_bill_delivery_id` (`bill_delivery_id`),
  KEY `idx_tb_bill_out_store_bill_no` (`bill_no`,`affiliate_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='出库单表';

/*Data for the table `tb_bill_out_store` */

/*Table structure for table `tb_bill_out_store_dtl` */

DROP TABLE IF EXISTS `tb_bill_out_store_dtl`;

CREATE TABLE `tb_bill_out_store_dtl` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '出库单明细ID',
  `bill_out_store_id` bigint(20) DEFAULT NULL COMMENT '出库单ID',
  `bill_delivery_dtl_id` bigint(20) DEFAULT NULL COMMENT '提货单明细ID',
  `goods_id` bigint(20) DEFAULT NULL COMMENT '商品ID',
  `send_num` decimal(20,8) DEFAULT '0.00000000' COMMENT '发货数量',
  `send_price` decimal(20,8) DEFAULT '0.00000000' COMMENT '发货单价',
  `pickup_num` decimal(20,8) DEFAULT '0.00000000' COMMENT '拣货数量',
  `stl_id` bigint(20) DEFAULT NULL COMMENT '库存ID',
  `batch_no` varchar(50) DEFAULT NULL COMMENT '批次',
  `goods_status` tinyint(4) DEFAULT '1' COMMENT '商品状态 1-常规 2-残次品',
  `cost_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '成本金额',
  `po_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '订单金额',
  `assign_stl_flag` tinyint(4) DEFAULT '0' COMMENT '是否指定库存 0-不指定 1-指定',
  `customs_declare_num` decimal(20,8) DEFAULT '0.00000000' COMMENT '已报关数量',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `pay_price` decimal(20,8) DEFAULT '0.00000000' COMMENT '付款单价',
  `pay_rate` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '付款汇率',
  `pay_real_currency` tinyint(4) NOT NULL DEFAULT '1' COMMENT '付款实际支付币种',
  `pay_time` datetime DEFAULT NULL COMMENT '付款时间',
  `po_return_id` bigint(20) DEFAULT NULL COMMENT '关联采购退货单ID',
  `po_return_dtl_id` bigint(20) DEFAULT NULL COMMENT '关联采购退货单明细ID',
  `fund_back_dtl_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '明细资金归还金额',
  PRIMARY KEY (`id`),
  KEY `idx_tb_bill_out_store_dtl_bill_out_store_id` (`bill_out_store_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='出库单明细表';

/*Data for the table `tb_bill_out_store_dtl` */

/*Table structure for table `tb_bill_out_store_pick_dtl` */

DROP TABLE IF EXISTS `tb_bill_out_store_pick_dtl`;

CREATE TABLE `tb_bill_out_store_pick_dtl` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '出库单ID',
  `bill_out_store_id` bigint(20) DEFAULT NULL COMMENT '出库单ID',
  `bill_out_store_dtl_id` bigint(20) DEFAULT NULL COMMENT '出库单明细ID',
  `po_id` bigint(20) DEFAULT NULL COMMENT 'PO订单ID',
  `po_dtl_id` bigint(20) DEFAULT NULL COMMENT 'PO订单明细ID',
  `goods_id` bigint(20) DEFAULT NULL COMMENT '商品ID',
  `pickup_num` decimal(20,8) DEFAULT '0.00000000' COMMENT '拣货数量',
  `send_price` decimal(20,8) DEFAULT '0.00000000' COMMENT '发货单价',
  `stl_id` bigint(20) DEFAULT NULL COMMENT '库存ID',
  `batch_no` varchar(50) DEFAULT NULL COMMENT '批次',
  `goods_status` tinyint(4) DEFAULT '1' COMMENT '商品状态 1-常规 2-残次品',
  `cost_price` decimal(20,8) DEFAULT '0.00000000' COMMENT '成本单价',
  `po_price` decimal(20,8) DEFAULT '0.00000000' COMMENT '订单单价',
  `accept_time` datetime DEFAULT NULL COMMENT '入库时间',
  `origin_accept_time` datetime DEFAULT NULL COMMENT '原入库时间',
  `receive_date` date DEFAULT NULL COMMENT '收货日期',
  `currency_type` tinyint(4) DEFAULT NULL COMMENT '币种 1.人民币 2.美元 3.港币',
  `exchange_rate` decimal(20,8) DEFAULT '0.00000000' COMMENT '汇率',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '客户ID',
  `supplier_id` bigint(20) DEFAULT NULL COMMENT '供应商ID',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `pay_price` decimal(20,8) DEFAULT '0.00000000' COMMENT '付款单价',
  `pay_rate` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '付款汇率',
  `pay_real_currency` tinyint(4) NOT NULL DEFAULT '1' COMMENT '付款实际支付币种',
  `pay_time` datetime DEFAULT NULL COMMENT '付款时间',
  `return_num` decimal(20,8) DEFAULT '0.00000000' COMMENT '销售退货数量',
  `po_return_id` bigint(20) DEFAULT NULL COMMENT '关联采购退货单ID',
  `po_return_dtl_id` bigint(20) DEFAULT NULL COMMENT '关联采购退货单明细ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='出库单拣货明细表';

/*Data for the table `tb_bill_out_store_pick_dtl` */

/*Table structure for table `tb_biz_constant` */

DROP TABLE IF EXISTS `tb_biz_constant`;

CREATE TABLE `tb_biz_constant` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `biz_code` varchar(50) DEFAULT NULL COMMENT '业务标示',
  `code` varchar(50) DEFAULT NULL COMMENT '枚举代码',
  `value` varchar(50) DEFAULT NULL COMMENT '中文含义',
  `ord` int(11) DEFAULT NULL COMMENT '排列顺序',
  `is_delete` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记 0 : 有效 1 : 删除',
  `create_at` datetime DEFAULT NULL COMMENT '创建时间',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `p_biz_code_code` varchar(50) DEFAULT NULL COMMENT 'biz_code和code组合所属上级',
  PRIMARY KEY (`id`),
  UNIQUE KEY `biz_code_code` (`biz_code`,`code`)
) ENGINE=InnoDB AUTO_INCREMENT=847 DEFAULT CHARSET=utf8 COMMENT='业务常量字典表';

/*Table structure for table `tb_business_msg` */

DROP TABLE IF EXISTS `tb_business_msg`;

CREATE TABLE `tb_business_msg` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `bill_type` tinyint(4) DEFAULT NULL COMMENT '单据类型',
  `bill_no` varchar(50) DEFAULT NULL COMMENT '单据号',
  `invoke_type` int(10) DEFAULT NULL COMMENT '接口类型(与tb_invoke_log的接口类型一致)',
  `flag` tinyint(4) DEFAULT '0' COMMENT '0-成功 1-失败',
  `msg` varchar(500) DEFAULT NULL COMMENT '返回消息',
  `try_num` tinyint(4) DEFAULT '0' COMMENT '调用次数',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='业务消息表';

/*Data for the table `tb_business_msg` */

/*Table structure for table `tb_capital_turnover` */

DROP TABLE IF EXISTS `tb_capital_turnover`;

CREATE TABLE `tb_capital_turnover` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '资金周转id',
  `department_id` bigint(20) DEFAULT NULL COMMENT '部门id',
  `project_id` bigint(20) DEFAULT NULL COMMENT '项目id',
  `sale_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '销售总额',
  `begin_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '期初金额',
  `end_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '期末金额',
  `turnover_rate` decimal(20,8) DEFAULT '0.00000000' COMMENT '资金周转率',
  `currency_type` tinyint(4) NOT NULL DEFAULT '1' COMMENT '币种',
  `issue` varchar(20) DEFAULT NULL COMMENT '期号',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='资金周转率信息表';

/*Data for the table `tb_capital_turnover` */

/*Table structure for table `tb_cope_manage` */

DROP TABLE IF EXISTS `tb_cope_manage`;

CREATE TABLE `tb_cope_manage` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `project_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '项目id',
  `customer_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '客户id',
  `busi_unit_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '经营单位',
  `currnecy_type` tinyint(4) NOT NULL DEFAULT '1' COMMENT '币种',
  `cope_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '应付金额',
  `paid_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '已付金额',
  `unpaid_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '未付金额',
  `create_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `creator_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建人id',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='应付管理信息';

/*Data for the table `tb_cope_manage` */

/*Table structure for table `tb_cope_manage_dtl` */

DROP TABLE IF EXISTS `tb_cope_manage_dtl`;

CREATE TABLE `tb_cope_manage_dtl` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `cope_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '应付管理id',
  `bill_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '单据id',
  `voucher_line_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '凭证明细id',
  `project_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '项目id',
  `customer_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '客户id',
  `busi_unit_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '经营单位',
  `cope_dtl_type` tinyint(20) NOT NULL DEFAULT '1' COMMENT '类型：1 应付费用',
  `currnecy_type` tinyint(4) NOT NULL DEFAULT '1' COMMENT '币种',
  `bill_number` varchar(50) NOT NULL DEFAULT '' COMMENT '单据编号',
  `bill_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '单据日期',
  `bill_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '单据金额',
  `cope_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '应付金额(核销金额)',
  `paid_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '已付金额',
  `unpaid_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '未付金额',
  `create_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `creator_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建人id',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='应付明细信息';

/*Data for the table `tb_cope_manage_dtl` */

/*Table structure for table `tb_customer_follow` */

DROP TABLE IF EXISTS `tb_customer_follow`;

CREATE TABLE `tb_customer_follow` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '客户id',
  `stage` tinyint(4) DEFAULT '1' COMMENT '所处阶段 1 意向阶段 2 合作阶段 3 已取消',
  `content` varchar(200) DEFAULT NULL COMMENT '跟进内容',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `creator_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建人id',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_delete` tinyint(4) DEFAULT NULL COMMENT '0 : 有效 1 : 删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='客户跟进信息';

/*Data for the table `tb_customer_follow` */

/*Table structure for table `tb_customer_maintain` */

DROP TABLE IF EXISTS `tb_customer_maintain`;

CREATE TABLE `tb_customer_maintain` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `subject_id` bigint(20) DEFAULT NULL COMMENT '基本信息id',
  `customer_no` varchar(13) DEFAULT NULL COMMENT '客户编号',
  `customer_type` tinyint(4) DEFAULT NULL COMMENT '客户类型 1 客户 2 供应商 3 经营单位',
  `source_channel` tinyint(4) DEFAULT NULL COMMENT '来源渠道,1 供应商系统申请 2 自主开发 3 采购事业部推荐',
  `abbreviation` varchar(50) DEFAULT NULL COMMENT '客户简称',
  `chinese_name` varchar(100) DEFAULT NULL COMMENT '中文全称',
  `english_name` varchar(100) DEFAULT NULL COMMENT '英文名称',
  `reg_place` varchar(200) DEFAULT NULL COMMENT '注册地',
  `reg_no` varchar(20) DEFAULT NULL COMMENT '注册号',
  `reg_phone` varchar(20) DEFAULT NULL COMMENT '注册电话',
  `office_address` varchar(500) DEFAULT NULL COMMENT '办公地址',
  `guardian` bigint(20) DEFAULT NULL COMMENT '维护人',
  `fllow` bigint(20) DEFAULT NULL COMMENT '跟进人',
  `contacts` varchar(50) DEFAULT NULL COMMENT '联系人',
  `contacts_number` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `contacts_other_number` varchar(50) DEFAULT NULL COMMENT '其他联系方式',
  `stage` tinyint(4) DEFAULT NULL COMMENT '所处阶段 1 意向阶段 2 合同签署 3 合作阶段 4 已关闭 ',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `creator` varchar(50) NOT NULL COMMENT '创建人',
  `creator_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建人id',
  `create_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `delete_at` datetime DEFAULT NULL COMMENT '作废时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_delete` tinyint(4) DEFAULT '0' COMMENT '0 : 有效 1 : 删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='客户维护信息表';

/*Data for the table `tb_customer_maintain` */

/*Table structure for table `tb_customs_apply` */

DROP TABLE IF EXISTS `tb_customs_apply`;

CREATE TABLE `tb_customs_apply` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `apply_no` varchar(20) DEFAULT NULL COMMENT '报关申请编号',
  `affiliate_no` varchar(20) DEFAULT NULL COMMENT '报关申请附属编号',
  `project_id` bigint(20) DEFAULT NULL COMMENT '项目ID',
  `proxy_company_id` bigint(20) DEFAULT NULL COMMENT '报关代理公司ID',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '客户ID',
  `customer_address_id` bigint(20) DEFAULT NULL COMMENT '收货地址ID',
  `customs_date` datetime DEFAULT NULL COMMENT '报关日期',
  `print_num` int(10) DEFAULT '0' COMMENT '打印次数',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态 1-待提交 2-已完成',
  `tax_rate` decimal(20,8) DEFAULT '0.00000000' COMMENT '税率',
  `customs_num` decimal(20,8) DEFAULT '0.00000000' COMMENT '报关数量',
  `customs_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '报关含税金额',
  `customs_tax_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '报关税额',
  `is_return_tax` tinyint(4) DEFAULT '0' COMMENT '是否退税 0-未退税 1-正在退税 2-已退税',
  `remark` varchar(500) DEFAULT NULL COMMENT '申请备注',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人ID ',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleter_id` bigint(20) DEFAULT NULL COMMENT '作废人ID',
  `deleter` varchar(50) DEFAULT NULL COMMENT '作废人',
  `is_delete` tinyint(4) DEFAULT '0' COMMENT '删除标记 0 : 有效 1 : 删除',
  `delete_at` datetime DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='报关申请表';

/*Data for the table `tb_customs_apply` */

/*Table structure for table `tb_customs_apply_line` */

DROP TABLE IF EXISTS `tb_customs_apply_line`;

CREATE TABLE `tb_customs_apply_line` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `customs_apply_id` bigint(20) DEFAULT NULL COMMENT '报关申请ID',
  `bill_id` bigint(20) DEFAULT NULL COMMENT '出库单ID',
  `bill_no` varchar(20) DEFAULT NULL COMMENT '出库单编号',
  `bill_affiliate_no` varchar(20) DEFAULT NULL COMMENT '出库单附属编号',
  `bill_dtl_id` bigint(20) DEFAULT NULL COMMENT '出库单明细ID',
  `goods_id` bigint(20) DEFAULT NULL COMMENT '商品ID',
  `customs_num` decimal(20,8) DEFAULT '0.00000000' COMMENT '报关数量',
  `customs_price` decimal(20,8) DEFAULT '0.00000000' COMMENT '报关含税单价',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人ID ',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='报关申请明细表';

/*Data for the table `tb_customs_apply_line` */

/*Table structure for table `tb_distribution_goods` */

DROP TABLE IF EXISTS `tb_distribution_goods`;

CREATE TABLE `tb_distribution_goods` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '铺货商品ID',
  `number` varchar(50) DEFAULT NULL COMMENT '编号',
  `name` varchar(255) DEFAULT NULL COMMENT '名称',
  `department_id` bigint(20) DEFAULT NULL COMMENT '事业部,部门id',
  `supplier_id` bigint(20) DEFAULT NULL COMMENT '供应商ID',
  `pledge` decimal(20,8) DEFAULT '0.00000000' COMMENT '质押比例',
  `type` varchar(100) DEFAULT NULL COMMENT '型号',
  `bar_code` varchar(20) DEFAULT NULL COMMENT '条码',
  `specification` varchar(50) DEFAULT NULL COMMENT '规格',
  `tax_classification` varchar(50) DEFAULT NULL COMMENT '税收分类',
  `tax_rate` decimal(20,2) DEFAULT '0.00' COMMENT '国内税率',
  `unit` varchar(20) DEFAULT NULL COMMENT '单位',
  `volume` decimal(20,8) DEFAULT '0.00000000' COMMENT '体积',
  `gross_weight` decimal(20,8) DEFAULT '0.00000000' COMMENT '毛重',
  `net_weight` decimal(20,8) DEFAULT '0.00000000' COMMENT '净重',
  `purchase_price` decimal(20,8) DEFAULT '0.00000000' COMMENT '采购指导价',
  `sale_price` decimal(20,8) DEFAULT '0.00000000' COMMENT '销售指导价',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `deleter` varchar(50) DEFAULT NULL COMMENT '作废人',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `delete_at` datetime DEFAULT NULL COMMENT '作废时间',
  `is_delete` tinyint(4) DEFAULT '0' COMMENT '删除标记',
  `locker` varchar(50) DEFAULT NULL COMMENT '锁定人',
  `lock_at` date DEFAULT NULL COMMENT '锁定时间',
  `sale_currency_type` tinyint(4) DEFAULT NULL COMMENT '销售指导价币种',
  `pur_currency_type` tinyint(4) DEFAULT NULL COMMENT '采购指导价币种',
  `brand` varchar(30) DEFAULT NULL COMMENT '品牌',
  `career_id` bigint(20) DEFAULT NULL COMMENT '事业部审核用户节点',
  `purchase_id` bigint(20) DEFAULT NULL COMMENT '采购部审核用户节点',
  `supply_chain_group_id` bigint(20) DEFAULT NULL COMMENT '供应链小组审核用户节点',
  `supply_chain_service_id` bigint(20) DEFAULT NULL COMMENT '供应链服务部审核用户节点',
  `risk_id` bigint(20) DEFAULT NULL COMMENT '风控审核用户节点',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='铺货商品';

/*Data for the table `tb_distribution_goods` */

/*Table structure for table `tb_fee` */

DROP TABLE IF EXISTS `tb_fee`;

CREATE TABLE `tb_fee` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `fee_no` varchar(20) NOT NULL COMMENT '提交之后自动生成',
  `currency_type` tinyint(4) NOT NULL COMMENT '1.人民币 2.美元',
  `fee_type` tinyint(4) NOT NULL COMMENT '1.应收费用 2.应付费用 3.应收应付费用',
  `deduction_type` tinyint(4) DEFAULT NULL COMMENT '抵扣类型 1.活动抵扣',
  `project_id` bigint(20) NOT NULL COMMENT '项目',
  `cust_payer` bigint(20) DEFAULT NULL COMMENT '应收客户',
  `rec_fee_spec` tinyint(4) DEFAULT NULL COMMENT '应收费用科目 取自费用科目表 ',
  `rec_assist_fee_spec` tinyint(4) DEFAULT NULL COMMENT '应收辅助科目 1.服务费',
  `rec_type` tinyint(4) DEFAULT NULL COMMENT '1.转账 2.票扣 3.账扣',
  `rec_date` date DEFAULT NULL COMMENT '应收日期',
  `received_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '应收辅助科目 1.服务费',
  `rec_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '应收金额',
  `provide_invoice_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '开票金额',
  `pay_rec_print_num` int(10) DEFAULT '0' COMMENT '应收应付费用打印次数',
  `rec_print_num` int(10) DEFAULT '0' COMMENT '应收费用打印次数',
  `pay_print_num` int(10) DEFAULT '0' COMMENT '应付费用打印次数',
  `provide_invoice_type` tinyint(4) DEFAULT NULL COMMENT '1.无票 2.增值税专票 3.增值税普票',
  `provide_invoice_tax_rate` decimal(20,2) DEFAULT NULL COMMENT '1.    0 2     0.6 3.    0.17',
  `rec_note` varchar(1000) DEFAULT NULL,
  `cust_receiver` bigint(20) DEFAULT NULL,
  `pay_fee_spec` bigint(20) DEFAULT NULL COMMENT '应付费用科目 取自费用科目表 ',
  `pay_assist_fee_spec` tinyint(4) DEFAULT NULL,
  `pay_type` tinyint(4) DEFAULT NULL COMMENT '1.TT 2.现金 3.票扣 4.账扣',
  `pay_date` date DEFAULT NULL COMMENT '应付日期',
  `paid_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '已付金额',
  `pay_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '应付金额',
  `accept_invoice_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '收票金额',
  `accept_invoice_type` tinyint(4) DEFAULT NULL COMMENT '1.无票 2.增值税专票 3.增值税普票',
  `accept_invoice_tax_rate` decimal(20,2) DEFAULT NULL COMMENT '1.0 2.0.6 3. 0.17',
  `state` tinyint(4) NOT NULL COMMENT '1.待提交 2.待财务审核 3.已完成',
  `pay_note` varchar(1000) DEFAULT NULL,
  `creator` varchar(50) NOT NULL,
  `create_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleter` varchar(50) DEFAULT NULL,
  `delete_at` datetime DEFAULT NULL,
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_delete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0 有效 1 删除',
  `deleter_id` bigint(20) DEFAULT NULL,
  `creator_id` int(11) DEFAULT NULL,
  `audit_at` date DEFAULT NULL,
  `auditor` varchar(50) DEFAULT NULL,
  `auditor_id` bigint(20) DEFAULT NULL,
  `pay_id` int(11) DEFAULT NULL COMMENT '付款确认，项目条款下的计算操作费为付款时，由实际付款金额乘以服务费率生成应收费用',
  `book_date` date DEFAULT NULL COMMENT '对账日期',
  `expire_date` date DEFAULT NULL COMMENT '到期日',
  `pay_fee_type` tinyint(4) DEFAULT NULL COMMENT '1.资金占用 2.代付',
  `fund_used` decimal(20,8) DEFAULT '0.00000000' COMMENT '资金占用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='费用表';

/*Data for the table `tb_fee` */

/*Table structure for table `tb_fee_manage` */

DROP TABLE IF EXISTS `tb_fee_manage`;

CREATE TABLE `tb_fee_manage` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '管理费用id,自增长',
  `fee_manage_no` varchar(20) DEFAULT NULL COMMENT '费用管理编号',
  `department_id` bigint(20) DEFAULT NULL COMMENT '部门id',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `project_id` bigint(20) DEFAULT NULL COMMENT '项目id',
  `cust_id` bigint(20) DEFAULT NULL COMMENT '客户id',
  `fee_spec_id` bigint(20) DEFAULT NULL COMMENT '费用科目id',
  `rec_type` tinyint(4) DEFAULT NULL COMMENT '应付方式',
  `date` datetime DEFAULT NULL COMMENT '日期',
  `currnecy_type` tinyint(11) DEFAULT NULL COMMENT '币种',
  `amount` varchar(50) DEFAULT NULL COMMENT '管理费用金额加密',
  `share_amount` varchar(50) DEFAULT NULL COMMENT '分摊金额',
  `state` tinyint(4) DEFAULT '0' COMMENT '状态',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_at` datetime DEFAULT NULL COMMENT '创建时间',
  `creator_id` int(11) DEFAULT NULL COMMENT '创建人ID',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `is_delete` tinyint(4) DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `tb_fee_manage` */

/*Table structure for table `tb_fee_rec_pay` */

DROP TABLE IF EXISTS `tb_fee_rec_pay`;

CREATE TABLE `tb_fee_rec_pay` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `rec_fee_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '应收费用id',
  `pay_fee_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '应付费用id',
  `is_delete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除标识 0 有效 1 删除',
  `creator_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建人id',
  `creator` varchar(50) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='应收应付费用关系表';

/*Data for the table `tb_fee_rec_pay` */

/*Table structure for table `tb_fee_share` */

DROP TABLE IF EXISTS `tb_fee_share`;

CREATE TABLE `tb_fee_share` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键，自增长',
  `manage_id` bigint(20) DEFAULT NULL COMMENT '费用管理id',
  `project_id` bigint(20) DEFAULT NULL COMMENT '项目id',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `cust_id` bigint(20) DEFAULT NULL COMMENT '客户id',
  `amount` varchar(50) DEFAULT '0.00' COMMENT '金额',
  `create_at` datetime DEFAULT NULL COMMENT '创建时间',
  `creator_id` int(11) DEFAULT NULL COMMENT '创建人ID',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `is_delete` tinyint(4) DEFAULT '0' COMMENT '是否删除',
  `share_date` datetime NOT NULL DEFAULT '1991-11-21 00:00:00' COMMENT '分摊日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='分摊表';

/*Data for the table `tb_fee_share` */

/*Table structure for table `tb_fee_spec` */

DROP TABLE IF EXISTS `tb_fee_spec`;

CREATE TABLE `tb_fee_spec` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `fee_spec_no` varchar(30) NOT NULL COMMENT '费用科目编号 ， 与关联财务',
  `fee_type` tinyint(4) NOT NULL COMMENT '1.应收费用 2.应付费用',
  `fee_spec_name` varchar(20) NOT NULL COMMENT '费用科目名称',
  `fee_one_name` tinyint(4) DEFAULT NULL COMMENT '管理一级名称',
  `fee_two_name` tinyint(4) DEFAULT NULL COMMENT '管理二级名称',
  `finance_code` varchar(50) DEFAULT NULL COMMENT '财务科目编码',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8 COMMENT='费用科目表';

/*Data for the table `tb_fee_spec` */

insert  into `tb_fee_spec`(`id`,`fee_spec_no`,`fee_type`,`fee_spec_name`,`fee_one_name`,`fee_two_name`,`finance_code`,`remark`) values (1,'XSCCF',2,'销售费用\\仓储费',10,13,'XSCCF',NULL),(2,'XSFWF',1,'销售费用\\服务费',12,21,'XSFWF',NULL),(3,'P1001',2,'销售费用\\操作费',12,21,'P1001',NULL),(4,'GLBXF',2,'管理费用\\保险费',12,27,'GLBXF',NULL),(5,'XSYF',1,'销售费用\\运费',7,11,'XSYF',''),(6,'XSYF',2,'销售费用\\运费',7,11,'XSYF',''),(7,'GLBXF',1,'管理费用\\保险费',12,27,'GLBXF',''),(8,'XSCCF',1,'销售费用\\仓储费',10,13,'XSCCF',''),(9,'GLZJF',4,'管理费用\\折旧费',15,54,'GLZJF',''),(10,'GLCLF',5,'管理费用\\差旅费',12,18,'GLCLF',''),(11,'GLBGFJTF',4,'管理费用\\办公费\\交通费',12,24,'GLBGFJTF',''),(12,'GLBGFZPF',4,'管理费用\\办公费\\招聘费',12,34,'GLBGFZPF',''),(13,'GLZXFTX',4,'管理费用\\装修费摊销',15,56,'GLZXFTX',''),(14,'GLXCGZ',5,'管理费用\\薪酬\\工资',13,43,'GLXCGZ',''),(15,'GLBGFRCBGF',4,'管理费用\\办公费\\日常办公费',12,17,'GLBGFRCBGF',''),(16,'GLWY',4,'管理费用\\物业费',12,31,'GLWY',''),(17,'GLWYSF',4,'管理费用\\物业费\\水费',12,31,'GLWYSF',''),(18,'GLWYDF',4,'管理费用\\物业费\\电费',12,31,'GLWYDF',''),(19,'GLFZ',4,'管理费用\\房租',11,42,'GLFZ',''),(20,'GLWYBJLHF',4,'管理费用\\物业费\\保洁绿化费',12,31,'GLWYBJLHF',''),(21,'GLWYGLF',4,'管理费用\\物业费\\物业管理费',12,31,'GLWYGLF',''),(22,'GLWYBAFWF',4,'管理费用\\物业费\\保安服务费',12,31,'GLWYBAFWF',''),(23,'GLNJPGSJF',4,'管理费用\\年检评估审计费',12,25,'GLNJPGSJF',''),(24,'GTFY',4,'公摊费用\\公摊杂费',12,15,'GTFY',''),(25,'GLXCZFGJJ',5,'管理费用\\薪酬\\住房公积金',13,44,'GLXCZFGJJ',''),(26,'GLXCBX',5,'管理费用\\薪酬\\保险',13,44,'GLXCBX','');

/*Table structure for table `tb_fi_account_book` */

DROP TABLE IF EXISTS `tb_fi_account_book`;

CREATE TABLE `tb_fi_account_book` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_book_name` varchar(30) NOT NULL COMMENT '帐套名称',
  `account_book_no` varchar(30) NOT NULL COMMENT '帐套编号',
  `fi_no` varchar(30) DEFAULT NULL COMMENT '财务编号 , 由财务录入',
  `busi_unit` bigint(20) NOT NULL COMMENT '经营单位id',
  `state` tinyint(4) NOT NULL COMMENT '1 ： 待提交 2 ： 已完成 3 ： 已锁定',
  `creator` varchar(50) NOT NULL COMMENT '创建人',
  `creator_id` int(11) NOT NULL COMMENT '创建人id',
  `create_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `locked_by` varchar(50) DEFAULT NULL COMMENT '锁定人',
  `lock_at` datetime DEFAULT NULL COMMENT '锁定时间',
  `locked_by_id` int(11) DEFAULT NULL COMMENT '锁定人id',
  `is_delete` tinyint(4) DEFAULT '0' COMMENT '是否删除',
  `deleter` varchar(50) DEFAULT NULL COMMENT '作废人',
  `deleter_id` int(11) DEFAULT NULL COMMENT '作废人id',
  `delete_at` datetime DEFAULT NULL COMMENT '作废时间',
  `is_home` tinyint(4) NOT NULL DEFAULT '1' COMMENT ' 0：国外,1：国内',
  `auditor_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '凭证审核人id(财务主管)',
  `auditor` varchar(50) NOT NULL COMMENT '审核人',
  `standard_coin` tinyint(4) NOT NULL COMMENT '本位币',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='账套表，与经营单位一对一关联';

/*Data for the table `tb_fi_account_book` */

/*Table structure for table `tb_fi_account_book_line_rel` */

DROP TABLE IF EXISTS `tb_fi_account_book_line_rel`;

CREATE TABLE `tb_fi_account_book_line_rel` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_book_id` bigint(20) NOT NULL COMMENT '帐套id',
  `account_line_id` bigint(20) NOT NULL COMMENT '科目id',
  `creator_id` int(11) NOT NULL COMMENT '分配人id',
  `creator` varchar(50) NOT NULL COMMENT '分配人',
  `create_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '分配时间',
  `state` tinyint(4) NOT NULL COMMENT '1:可用 2:作废',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleter` varchar(50) DEFAULT NULL COMMENT '作废人',
  `delete_at` datetime DEFAULT NULL COMMENT '作废时间',
  `deleter_id` int(11) DEFAULT NULL COMMENT '作废人id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='帐套科目关系表';

/*Data for the table `tb_fi_account_book_line_rel` */

/*Table structure for table `tb_fi_account_line` */

DROP TABLE IF EXISTS `tb_fi_account_line`;

CREATE TABLE `tb_fi_account_line` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '财务科目id',
  `account_line_no` varchar(10) NOT NULL COMMENT '科目编号 , 由页面输入',
  `account_line_name` varchar(100) NOT NULL COMMENT '科目名称',
  `account_line_type` tinyint(4) NOT NULL COMMENT '科目分类 : 表示为资产类型还是负债类型',
  `account_line_level` tinyint(4) NOT NULL COMMENT '1 : 一级,2:  二级,3:  三级',
  `debit_or_credit` tinyint(4) NOT NULL COMMENT '表示是借方还是贷方,1 : 借方,2 : 贷方',
  `need_project` tinyint(4) NOT NULL COMMENT '0 不需要项目辅助,1.需要项目辅助',
  `need_supplier` tinyint(4) NOT NULL COMMENT '1.需要供应商辅助,2.不需要供应商辅助',
  `need_cust` tinyint(4) NOT NULL COMMENT '1 需要客户辅助,2.不需要客户辅助',
  `need_tax_rate` tinyint(4) NOT NULL COMMENT '1 需要税率辅助,2.不需要项目辅助',
  `need_account` tinyint(4) NOT NULL COMMENT '0 不需要账号辅助,1.需要账号辅助',
  `need_inner_busi_unit` tinyint(4) NOT NULL COMMENT '0 不需要内部经营单位辅助     1.需要内部经营单位账号辅助',
  `need_user` tinyint(4) NOT NULL COMMENT '1:需要用户辅助 2:不需要用户辅助',
  `state` tinyint(4) NOT NULL COMMENT '1:待提交,2.已完成,3.已锁定',
  `creator_id` int(11) NOT NULL COMMENT '创建人id',
  `creator` varchar(50) NOT NULL COMMENT '创建人',
  `create_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `locked_by` varchar(50) DEFAULT NULL COMMENT '锁定人',
  `locked_by_id` int(11) DEFAULT NULL COMMENT '锁定人id',
  `lock_at` datetime DEFAULT NULL COMMENT '锁定时间',
  `is_delete` tinyint(4) DEFAULT '0' COMMENT '0 : 否,1:  是',
  `deleter` varchar(50) DEFAULT NULL COMMENT '作废人',
  `delete_at` datetime DEFAULT NULL COMMENT '作废时间',
  `deleter_id` int(11) DEFAULT NULL COMMENT '作废人',
  `accout_line_state` tinyint(4) DEFAULT '1' COMMENT '科目状态 0-老科目 1-新科目',
  `is_last` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否末级科目',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='财务科目表 ,';


/*Table structure for table `tb_fi_advance` */

DROP TABLE IF EXISTS `tb_fi_advance`;

CREATE TABLE `tb_fi_advance` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '预收id,自增长',
  `cust_id` bigint(20) NOT NULL COMMENT '客户',
  `busi_unit` bigint(20) NOT NULL COMMENT '经营单位',
  `project_id` bigint(20) NOT NULL COMMENT '项目',
  `pre_rec_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '预收余额',
  `pre_rec_sum` decimal(20,8) DEFAULT '0.00000000' COMMENT '预收总额',
  `write_off_sum` decimal(20,8) DEFAULT '0.00000000' COMMENT '已核销总额',
  `writing_off_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '待核销金额',
  `currency_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '币种',
  `create_at` datetime NOT NULL COMMENT '创建时间',
  `creator` varchar(50) NOT NULL COMMENT '创建人',
  `creator_id` int(11) NOT NULL COMMENT '创建人id',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `advance_type` tinyint(4) NOT NULL COMMENT '预收类型',
  `paid_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '已付金额',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='预收表';

/*Data for the table `tb_fi_advance` */

/*Table structure for table `tb_fi_advance_receipt_rel` */

DROP TABLE IF EXISTS `tb_fi_advance_receipt_rel`;

CREATE TABLE `tb_fi_advance_receipt_rel` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '预收水单关系id',
  `receipt_id` bigint(20) NOT NULL COMMENT '水单id',
  `pre_rec_id` bigint(20) DEFAULT NULL COMMENT '预收id',
  `exchange_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '转预收金额',
  `creator_id` int(11) NOT NULL COMMENT '创建人id',
  `creator` varchar(50) NOT NULL COMMENT '创建人',
  `create_at` datetime NOT NULL COMMENT '创建时间',
  `currency_type` tinyint(4) NOT NULL COMMENT '币种',
  `project_id` bigint(20) NOT NULL COMMENT '项目',
  `pre_rec_note` varchar(200) DEFAULT NULL COMMENT '预收备注',
  `advance_type` tinyint(4) DEFAULT NULL COMMENT '预收类型',
  `delete_priv_flag` tinyint(4) DEFAULT '0' COMMENT '删除权限标示，提货单审核生成的水单预收关系前台不能删除，0可以删除，1不可以删除',
  PRIMARY KEY (`id`),
  KEY `idx_tb_fi_advance_receipt_rel_receipt_id_pre_rec_id` (`receipt_id`,`pre_rec_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='预收关系表';

/*Data for the table `tb_fi_advance_receipt_rel` */

/*Table structure for table `tb_fi_bank_receipt` */

DROP TABLE IF EXISTS `tb_fi_bank_receipt`;

CREATE TABLE `tb_fi_bank_receipt` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '水单id，自增长',
  `pid` bigint(11) DEFAULT NULL COMMENT '来源水单',
  `cust_id` int(50) DEFAULT NULL COMMENT '客户录入',
  `busi_unit` bigint(20) DEFAULT NULL COMMENT '经营单位 关联tb_base_subject[id]',
  `project_id` int(50) DEFAULT NULL COMMENT '项目',
  `receipt_no` varchar(50) NOT NULL COMMENT '水单编号 系统生成的编号',
  `receipt_note` varchar(200) DEFAULT NULL COMMENT '水单备注',
  `receipt_date` datetime NOT NULL COMMENT '水单日期',
  `bank_receipt_no` varchar(50) DEFAULT NULL COMMENT '银行水单号',
  `bank_receipt_note` varchar(200) DEFAULT NULL COMMENT '银行水单备注',
  `rec_account_no` int(50) DEFAULT NULL COMMENT '收款账号',
  `currency_type` tinyint(4) NOT NULL COMMENT '币种',
  `receipt_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '水单金额',
  `receipt_type` tinyint(4) NOT NULL COMMENT '水单类型 1 回款 2 预收定金 3 预收货款 4融资  5内部',
  `pre_rec_id` bigint(20) DEFAULT NULL COMMENT '预收转水单时记录预收id',
  `write_off_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '核销金额',
  `diff_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '尾差（抹零金额）',
  `pre_rec_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '预收金额（水单金额-核销金额+尾差）',
  `paid_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '已付金额，关联tb_pay_advance_relation',
  `state` tinyint(4) NOT NULL COMMENT '状态 1 待提交 2 待核销 3 核完',
  `creator` varchar(50) NOT NULL COMMENT '创建人',
  `creator_id` int(11) NOT NULL COMMENT '创建人id',
  `create_at` datetime NOT NULL COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `summary` varchar(500) DEFAULT NULL COMMENT '摘要',
  `pay_unit` varchar(100) DEFAULT NULL COMMENT '付款单位',
  `is_delete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `receipt_way` tinyint(4) DEFAULT NULL COMMENT '收款方式',
  `open_type` int(11) DEFAULT '0' COMMENT '开立类型',
  `open_date` datetime DEFAULT NULL COMMENT '开立日期',
  `deleter` varchar(50) DEFAULT NULL COMMENT '作废人',
  `deleter_id` bigint(20) DEFAULT NULL COMMENT '作废人id',
  `delete_at` datetime DEFAULT NULL COMMENT '作废时间',
  `write_offtor_id` int(11) DEFAULT NULL COMMENT '核完人',
  `write_offat` datetime DEFAULT NULL COMMENT '核完时间',
  `actual_receipt_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '实际水单金额',
  `actual_write_off_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '实际核销金额',
  `actual_diff_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '实际尾差（抹零金额）',
  `actual_pre_rec_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '实际预收金额（水单金额-核销金额+尾差）',
  `actual_paid_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '实际已付金额，关联tb_pay_advance_relation',
  `actual_currency_type` tinyint(4) DEFAULT NULL COMMENT '实际币种',
  `actual_currency_rate` decimal(20,8) DEFAULT '0.00000000' COMMENT '币种转换比率(实际水单金额/应付水单金额)',
  PRIMARY KEY (`id`),
  KEY `idx_tb_fi_bank_receipt_receipt_no` (`receipt_no`,`project_id`,`busi_unit`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='客户向经营单位转账金额，每次核销都要生成对冲分录';

/*Data for the table `tb_fi_bank_receipt` */

/*Table structure for table `tb_fi_cope_receipt_rel` */

DROP TABLE IF EXISTS `tb_fi_cope_receipt_rel`;

CREATE TABLE `tb_fi_cope_receipt_rel` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '水单与应付关系表ID',
  `cope_dtl_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '应付ID',
  `receipt_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '水单ID',
  `project_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '项目id',
  `customer_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '客户id',
  `busi_unit_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '经营单位',
  `currnecy_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '币种',
  `write_off_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '核销金额',
  `is_delete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `creator` varchar(50) NOT NULL DEFAULT '' COMMENT '创建人',
  `creator_id` int(11) NOT NULL DEFAULT '0' COMMENT '创建人id',
  `create_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='水单与应付关系表';

/*Data for the table `tb_fi_cope_receipt_rel` */

/*Table structure for table `tb_fi_prepaid_receipt_rel` */

DROP TABLE IF EXISTS `tb_fi_prepaid_receipt_rel`;

CREATE TABLE `tb_fi_prepaid_receipt_rel` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '预付水单关系id',
  `receipt_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '水单ID',
  `project_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '项目id',
  `customer_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '客户id',
  `busi_unit_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '经营单位',
  `currnecy_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '币种',
  `prepaid_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '可转转预付金额',
  `actual_prepaid_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '实际转预付金额',
  `prepaid_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '预付类型',
  `is_delete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `creator` varchar(50) NOT NULL DEFAULT '' COMMENT '创建人',
  `creator_id` int(11) NOT NULL DEFAULT '0' COMMENT '创建人id',
  `create_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='预付水单关系表';

/*Data for the table `tb_fi_prepaid_receipt_rel` */

/*Table structure for table `tb_fi_rec` */

DROP TABLE IF EXISTS `tb_fi_rec`;

CREATE TABLE `tb_fi_rec` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cust_id` bigint(20) NOT NULL COMMENT '客户',
  `busi_unit` bigint(20) NOT NULL COMMENT '经营单位',
  `project_id` bigint(20) NOT NULL COMMENT '项目',
  `currency_type` tinyint(4) NOT NULL COMMENT '1:人民币   2:美元',
  `amount_receivable` decimal(20,8) DEFAULT '0.00000000' COMMENT '应收金额',
  `amount_received` decimal(20,8) DEFAULT '0.00000000' COMMENT '已收金额 ， 水单核销总额',
  `check_date` date NOT NULL COMMENT '对账日期',
  `expire_date` date NOT NULL COMMENT '还款期限日',
  `create_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `creator` varchar(50) NOT NULL COMMENT '创建人',
  `creator_id` int(11) NOT NULL COMMENT '创建人id',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `check_note` varchar(200) DEFAULT NULL COMMENT '对账备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='根据分录表对账生成，记录已对账金额（应收关系表的对账金额总和），在核销之前可以编辑对账明细';

/*Data for the table `tb_fi_rec` */

/*Table structure for table `tb_fi_rec_line` */

DROP TABLE IF EXISTS `tb_fi_rec_line`;

CREATE TABLE `tb_fi_rec_line` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `rec_id` bigint(20) NOT NULL COMMENT '应收id【关联应收表id】',
  `voucher_line_id` bigint(20) NOT NULL COMMENT '分录id,关联tb_voucher_line[id]',
  `amount_check` decimal(20,8) DEFAULT '0.00000000' COMMENT '对账金额',
  `creator` varchar(50) NOT NULL COMMENT '创建人',
  `creator_id` int(11) NOT NULL COMMENT '创建人Id',
  `create_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `currency_type` tinyint(4) NOT NULL COMMENT '币种',
  `write_off_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '核销规则，当核销金额不足时，凭证日期越早，优先级越高',
  `bill_type` tinyint(4) DEFAULT NULL COMMENT '单据类型 1:费用单 2:入库单 3:出库单 4:付款单 5:收票单 6:开票单 7:水单',
  `bill_no` varchar(50) DEFAULT NULL COMMENT '单据编号',
  `bill_date` datetime DEFAULT NULL COMMENT '单据日期',
  `fee_id` int(11) DEFAULT NULL COMMENT '费用单id',
  `out_store_id` int(11) DEFAULT NULL COMMENT '出库单id',
  PRIMARY KEY (`id`),
  KEY `idx_tb_fi_rec_line_rec_id_voucher_line_id` (`rec_id`,`voucher_line_id`),
  KEY `idx_tb_fi_rec_line_complex` (`bill_type`,`bill_no`,`bill_date`,`fee_id`,`out_store_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='与分录表多对一，与应收管理表多对一，记录实际对账金额，可能与分录表的金额不等，可以分多次部分对账';

/*Data for the table `tb_fi_rec_line` */

/*Table structure for table `tb_fi_rec_receipt_rel` */

DROP TABLE IF EXISTS `tb_fi_rec_receipt_rel`;

CREATE TABLE `tb_fi_rec_receipt_rel` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '水单与应收关系表id',
  `rec_id` bigint(20) NOT NULL COMMENT '应收id',
  `receipt_id` bigint(20) NOT NULL COMMENT '水单id',
  `write_off_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '核销金额',
  `creator` varchar(50) NOT NULL COMMENT '创建人',
  `creator_id` int(11) NOT NULL COMMENT '创建人id',
  `create_at` datetime NOT NULL COMMENT '创建时间',
  `currency_type` tinyint(4) NOT NULL COMMENT '币种',
  `is_delete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `fund_used` decimal(20,8) DEFAULT '0.00000000' COMMENT '资金占用',
  PRIMARY KEY (`id`),
  KEY `idx_tb_fi_rec_receipt_rel_rec_id_receipt_id` (`rec_id`,`receipt_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='客户可多次分部分核销账款';

/*Data for the table `tb_fi_rec_receipt_rel` */

/*Table structure for table `tb_fi_receipt_out_rel` */

DROP TABLE IF EXISTS `tb_fi_receipt_out_rel`;

CREATE TABLE `tb_fi_receipt_out_rel` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '水单和出库单的关系ID',
  `bill_out_id` bigint(20) DEFAULT NULL COMMENT '出库单的ID',
  `bank_receipt_id` bigint(20) DEFAULT NULL COMMENT '水单ID',
  `busi_unit` bigint(20) DEFAULT NULL COMMENT '经营单位',
  `project_id` bigint(20) DEFAULT NULL COMMENT '项目ID',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '客户id',
  `currency_type` bigint(20) DEFAULT NULL COMMENT '币种 1.人民币 2.美元 3.港币',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_delete` bigint(4) DEFAULT '0' COMMENT '删除标记 0 : 有效 1 : 删除',
  `received_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '回款金额',
  `write_off_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '核销金额{出库单发货金额}',
  `create_at` datetime NOT NULL COMMENT '创建时间',
  `bill_type` tinyint(4) DEFAULT NULL COMMENT '单据类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='出库单和水单关系表';

/*Data for the table `tb_fi_receipt_out_rel` */

/*Table structure for table `tb_fi_vl_receipt_rel` */

DROP TABLE IF EXISTS `tb_fi_vl_receipt_rel`;

CREATE TABLE `tb_fi_vl_receipt_rel` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `voucher_line_id` bigint(20) NOT NULL COMMENT '分录id',
  `receipt_id` bigint(20) NOT NULL COMMENT '水单id',
  `write_off_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '核销金额',
  `create_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `creator` varchar(50) NOT NULL COMMENT '创建人',
  `creator_id` bigint(20) NOT NULL COMMENT '创建人id',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_tb_fi_vl_receipt_rel_voucher_line_id_receipt_id` (`voucher_line_id`,`receipt_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='水单与分录关联表';

/*Data for the table `tb_fi_vl_receipt_rel` */

/*Table structure for table `tb_fi_voucher` */

DROP TABLE IF EXISTS `tb_fi_voucher`;

CREATE TABLE `tb_fi_voucher` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '凭证id',
  `account_book_id` bigint(20) NOT NULL COMMENT '帐套id',
  `busi_unit` bigint(20) NOT NULL COMMENT '经营单位id',
  `voucher_word` tinyint(4) NOT NULL COMMENT '凭证字',
  `voucher_summary` varchar(200) NOT NULL COMMENT '凭证摘要',
  `voucher_no` varchar(50) NOT NULL DEFAULT '' COMMENT '凭证编号',
  `debit_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '借方金额',
  `debit_currency_type` tinyint(4) DEFAULT NULL COMMENT '借方币种',
  `credit_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '贷方金额',
  `credit_currency_type` tinyint(4) DEFAULT NULL COMMENT '贷方币种',
  `state` tinyint(4) NOT NULL DEFAULT '1' COMMENT '1-待提交 25-待财务专员审核 3-已完成',
  `creator_id` int(11) NOT NULL COMMENT '创建人id',
  `creator` varchar(50) NOT NULL COMMENT '创建人',
  `create_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleter_id` int(11) DEFAULT NULL COMMENT '作废人id',
  `deleter` varchar(50) DEFAULT NULL COMMENT '作废人',
  `delete_at` datetime DEFAULT NULL COMMENT '作废时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `attachment_number` int(11) DEFAULT '0' COMMENT '附件数目',
  `is_delete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0:可用 1:已删除',
  `voucher_date` date NOT NULL,
  `bill_no` varchar(50) DEFAULT NULL COMMENT '单据编号',
  `bill_date` date DEFAULT NULL COMMENT '单据日期',
  `fee_id` bigint(20) DEFAULT NULL COMMENT '费用单据id',
  `in_store_id` bigint(20) DEFAULT NULL COMMENT '入库单据id',
  `out_store_id` bigint(20) DEFAULT NULL COMMENT '出库单据id',
  `receipt_id` bigint(20) DEFAULT NULL COMMENT '水单单据id',
  `accept_invoice_id` bigint(20) DEFAULT NULL COMMENT '收票单据id',
  `provide_invoice_id` bigint(20) DEFAULT NULL COMMENT '开票单据id',
  `pay_id` bigint(20) DEFAULT NULL COMMENT '付款单据id',
  `bill_type` tinyint(4) DEFAULT NULL COMMENT '单据类型',
  `voucher_line_number` int(2) DEFAULT '0' COMMENT '分录数量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='凭证表 需提交审批';

/*Data for the table `tb_fi_voucher` */

/*Table structure for table `tb_fi_voucher_line` */

DROP TABLE IF EXISTS `tb_fi_voucher_line`;

CREATE TABLE `tb_fi_voucher_line` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '分录id',
  `busi_unit` bigint(20) NOT NULL COMMENT '经营单位',
  `voucher_id` bigint(20) NOT NULL COMMENT '凭证id',
  `account_line_id` bigint(20) NOT NULL COMMENT '科目id',
  `debit_or_credit` tinyint(4) NOT NULL COMMENT '借贷',
  `currency_type` tinyint(4) NOT NULL COMMENT '币种',
  `amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '金额',
  `cny_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '人民币金额',
  `exchange_rate` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '汇率',
  `standard_coin` tinyint(4) DEFAULT NULL COMMENT '本币币种',
  `standard_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '本币金额',
  `standard_rate` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '原币转本币汇率',
  `project_id` bigint(20) DEFAULT NULL COMMENT '项目辅助',
  `supplier_id` bigint(20) DEFAULT NULL COMMENT '供应商辅助',
  `cust_id` bigint(20) DEFAULT NULL COMMENT '客户辅助',
  `account_id` bigint(20) DEFAULT NULL COMMENT '账户辅助',
  `tax_rate` decimal(20,4) DEFAULT NULL COMMENT '税率辅助',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户辅助',
  `inner_busi_unit_id` bigint(20) DEFAULT NULL,
  `voucher_line_summary` varchar(200) NOT NULL COMMENT '摘要',
  `creator_id` int(11) NOT NULL COMMENT '创建人id',
  `creator` varchar(50) NOT NULL COMMENT '创建人',
  `create_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `deleter` varchar(50) DEFAULT NULL COMMENT '作废人',
  `deleter_id` int(11) DEFAULT NULL COMMENT '作废人id',
  `delete_at` datetime DEFAULT NULL COMMENT '作废时间',
  `is_delete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `amount_checked` decimal(20,8) DEFAULT '0.00000000' COMMENT '已对账金额',
  `bill_no` varchar(50) DEFAULT NULL COMMENT '单据编号',
  `bill_date` datetime DEFAULT NULL COMMENT '单据日期',
  `fee_id` bigint(20) DEFAULT NULL COMMENT '费用单据id',
  `in_store_id` bigint(20) DEFAULT NULL COMMENT '入库单据id',
  `out_store_id` bigint(20) DEFAULT NULL COMMENT '出库单据id',
  `pay_id` bigint(20) DEFAULT NULL COMMENT '付款单据id',
  `accept_invoice_id` bigint(20) DEFAULT NULL COMMENT '收票单据id',
  `provide_invoice_id` bigint(20) DEFAULT NULL COMMENT '开票单据id',
  `bill_type` tinyint(4) DEFAULT NULL COMMENT '单据类型',
  `receipt_id` bigint(20) DEFAULT NULL COMMENT '水单单据id',
  `invoice_collect_approve_id` bigint(20) DEFAULT NULL COMMENT '收票认证ID',
  PRIMARY KEY (`id`),
  KEY `idx_tb_fi_voucher_line_complex` (`voucher_id`,`account_line_id`),
  KEY `idx_tb_fi_voucher_line_receipt` (`receipt_id`,`bill_no`,`bill_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='分录表';

/*Data for the table `tb_fi_voucher_line` */

/*Table structure for table `tb_file_attach` */

DROP TABLE IF EXISTS `tb_file_attach`;

CREATE TABLE `tb_file_attach` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `bus_id` int(11) DEFAULT NULL COMMENT '业务ID',
  `bus_type` int(11) DEFAULT NULL COMMENT '业务类型',
  `name` varchar(200) DEFAULT NULL,
  `type` varchar(50) DEFAULT NULL COMMENT '文件类型',
  `path` varchar(200) DEFAULT NULL COMMENT '文件路径',
  `create_at` datetime DEFAULT NULL COMMENT '创建时间',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `creator_id` int(11) DEFAULT NULL COMMENT '创建人ID',
  `deleter` varchar(50) DEFAULT NULL COMMENT '删除人',
  `deleter_id` int(11) DEFAULT NULL COMMENT '删除人ID',
  `is_delete` tinyint(4) DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='上传附件表';

/*Data for the table `tb_file_attach` */

/*Table structure for table `tb_invoice` */

DROP TABLE IF EXISTS `tb_invoice`;

CREATE TABLE `tb_invoice` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `invoice_no` varchar(20) DEFAULT NULL COMMENT '发票编号',
  `invoice_code` varchar(50) DEFAULT NULL COMMENT '发票号',
  `invoice_date` datetime DEFAULT NULL COMMENT '发票日期',
  `invoice_apply_no` varchar(20) DEFAULT NULL COMMENT '申请编号',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态 1-已完成 2-已红冲 3-已作废',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '客户ID',
  `base_invoice_id` bigint(20) DEFAULT NULL COMMENT '开票信息 关联tb_base_invoice',
  `rate_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '未税金额',
  `in_rate_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '含税金额',
  `ex_rate_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '税额',
  `discount_rate_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '折扣税额',
  `discount_in_rate_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '折扣含税金额',
  `discount_ex_rate_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '折扣未税金额',
  `invoice_remark` varchar(500) DEFAULT NULL COMMENT '票据备注',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人ID ',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `invoice_apply_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_tb_invoice_invoice_apply_id` (`invoice_apply_id`,`invoice_no`,`invoice_code`,`invoice_apply_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='发票表';

/*Data for the table `tb_invoice` */

/*Table structure for table `tb_invoice_apply` */

DROP TABLE IF EXISTS `tb_invoice_apply`;

CREATE TABLE `tb_invoice_apply` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `apply_no` varchar(20) DEFAULT NULL COMMENT '申请编号',
  `apply_type` tinyint(4) DEFAULT NULL COMMENT '申请类型 1-开票 2-收票',
  `project_id` bigint(20) DEFAULT NULL COMMENT '项目ID',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '类型 1 客户ID  2 供应商ID',
  `business_unit_id` bigint(20) DEFAULT NULL COMMENT '经营单位ID',
  `invoice_type` tinyint(4) DEFAULT NULL COMMENT '发票类型 1-增值税专用发票 2-增值税普通发票',
  `is_elec_invoice` tinyint(4) DEFAULT '0' COMMENT '是否电子普通发票 0-否 1-是',
  `base_invoice_id` bigint(20) DEFAULT NULL COMMENT '开票信息 关联tb_base_invoice',
  `STATUS` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态 1-待模拟 2-待提交 25-待财务专员审核 30-待财务主管审核 4-待确认 5-已完成',
  `bill_type` tinyint(4) DEFAULT NULL COMMENT '单据类别 1-货物 2-费用',
  `fee_type` tinyint(4) DEFAULT NULL COMMENT '费用类型 1-服务费 2-操作费 3-仓储费 4运费',
  `invoice_tax_rate` decimal(4,2) DEFAULT NULL COMMENT '1: 0   2: 0.06   3: 0.13   4: 0.17',
  `tax_cate_no` varchar(20) DEFAULT NULL COMMENT '税收分类编码',
  `is_goods_merge` tinyint(4) DEFAULT '1' COMMENT '同品合并 0-否 1-是',
  `is_display_discount` tinyint(4) DEFAULT '1' COMMENT '显示折扣 0-否 1-是',
  `discount` decimal(20,8) DEFAULT '0.00000000' COMMENT '固定折扣率',
  `print_num` int(10) DEFAULT '0' COMMENT '打印次数',
  `invoice_remark` varchar(500) DEFAULT NULL COMMENT '票据备注',
  `currency_type` tinyint(4) DEFAULT NULL COMMENT '币种 1.人民币 2.美元',
  `exchange_rate` decimal(20,8) DEFAULT '0.00000000' COMMENT '汇率',
  `remark` varchar(500) DEFAULT NULL COMMENT '单据备注',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人ID ',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleter_id` bigint(20) DEFAULT NULL COMMENT '作废人ID',
  `deleter` varchar(50) DEFAULT NULL COMMENT '作废人',
  `is_delete` tinyint(4) DEFAULT '0' COMMENT '删除标记 0 : 有效 1 : 删除',
  `delete_at` datetime DEFAULT NULL COMMENT '删除时间',
  `apply_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '申请金额',
  PRIMARY KEY (`id`),
  KEY `idx_tb_invoice_apply_no_project` (`apply_no`,`apply_type`,`project_id`,`customer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='发票申请表';

/*Data for the table `tb_invoice_apply` */

/*Table structure for table `tb_invoice_apply_fee` */

DROP TABLE IF EXISTS `tb_invoice_apply_fee`;

CREATE TABLE `tb_invoice_apply_fee` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `invoice_apply_id` bigint(20) DEFAULT NULL COMMENT '发票申请ID',
  `fee_id` bigint(20) DEFAULT NULL COMMENT '费用ID,关联fee表',
  `fee_no` varchar(20) DEFAULT NULL COMMENT '费用编号',
  `fee_type` tinyint(4) DEFAULT NULL COMMENT '费用类型 1.应收费用 2.应付费用  3.应收应付费用',
  `fee_date` date DEFAULT NULL COMMENT '费用日期 开票-应收日期 收票-应付日期',
  `provide_invoice_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '开票金额',
  `discount_in_rate_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '折扣含税金额',
  `discount` decimal(20,8) DEFAULT '0.00000000' COMMENT '折扣率',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人ID ',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='发票申请-费用表';

/*Data for the table `tb_invoice_apply_fee` */

/*Table structure for table `tb_invoice_apply_sale` */

DROP TABLE IF EXISTS `tb_invoice_apply_sale`;

CREATE TABLE `tb_invoice_apply_sale` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `invoice_apply_id` bigint(20) DEFAULT NULL COMMENT '发票申请ID',
  `bill_id` bigint(20) DEFAULT NULL COMMENT '单据ID, 提货单/退货单ID',
  `bill_no` varchar(20) DEFAULT NULL COMMENT '单据编号, 提货单/退货单编号',
  `bill_dtl_id` bigint(20) DEFAULT NULL COMMENT '单据明细ID, 提货单/退货单明细ID',
  `goods_id` bigint(20) DEFAULT NULL COMMENT '商品ID',
  `provide_invoice_num` decimal(20,8) DEFAULT '0.00000000' COMMENT '开票数量',
  `provide_invoice_price` decimal(20,8) DEFAULT '0.00000000' COMMENT '开票单价',
  `provide_invoice_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '开票金额',
  `discount_in_rate_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '折扣含税金额',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人ID ',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `bill_date` date DEFAULT NULL COMMENT '单据日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='发票申请-销售明细表';

/*Data for the table `tb_invoice_apply_sale` */

/*Table structure for table `tb_invoice_collect` */

DROP TABLE IF EXISTS `tb_invoice_collect`;

CREATE TABLE `tb_invoice_collect` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '收票id自增',
  `apply_no` varchar(50) DEFAULT NULL COMMENT '申请编号',
  `business_unit` bigint(11) DEFAULT NULL COMMENT '经营单位',
  `project_id` bigint(11) DEFAULT NULL COMMENT '项目id',
  `supplier_id` bigint(11) DEFAULT NULL COMMENT '供应商id',
  `invoice_type` tinyint(4) DEFAULT NULL COMMENT '票据类型 1-增值税专用发票 2-增值税普通发票',
  `bill_type` tinyint(4) DEFAULT NULL COMMENT '单据类别 1-货物 2-费用',
  `invoice_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '发票金额',
  `invoice_no` varchar(50) DEFAULT NULL COMMENT '发票号',
  `print_num` int(10) DEFAULT '0' COMMENT '打印次数',
  `invoice_date` datetime DEFAULT NULL COMMENT '发票日期',
  `approve_date` datetime DEFAULT NULL COMMENT '认证日期(废弃)，使用收票认证表认证日期',
  `approve_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '已认证金额',
  `un_approve_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '未认证金额',
  `invoice_tax_rate` decimal(20,2) DEFAULT NULL COMMENT '收票税率1: 0   2: 0.06   3: 0.13   4: 0.17',
  `invoice_remark` varchar(500) DEFAULT NULL COMMENT '票据备注',
  `state` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态 1-待提交 25-待财务专员审核 30-待财务主管审核 3-待认证 4-已完成',
  `remark` varchar(500) DEFAULT NULL COMMENT '单据备注',
  `creator_id` bigint(11) DEFAULT NULL COMMENT '创建人ID',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_at` datetime DEFAULT NULL COMMENT '创建时间',
  `is_delete` tinyint(4) DEFAULT '0' COMMENT '是否删除 0 否 1 是',
  PRIMARY KEY (`id`),
  KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='进项发票表';

/*Data for the table `tb_invoice_collect` */

/*Table structure for table `tb_invoice_collect_approve` */

DROP TABLE IF EXISTS `tb_invoice_collect_approve`;

CREATE TABLE `tb_invoice_collect_approve` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '收票认证id',
  `invoice_collect_id` bigint(20) NOT NULL COMMENT '收票id',
  `approve_date` datetime DEFAULT NULL COMMENT '认证日期',
  `approve_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '认证金额',
  `approve_remark` varchar(500) DEFAULT NULL COMMENT '认证备注',
  `approver_id` bigint(20) DEFAULT NULL COMMENT '认证人ID',
  `approver` varchar(50) DEFAULT NULL COMMENT '认证人',
  `approver_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '认证时间',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_delete` tinyint(4) DEFAULT '0' COMMENT '是否删除 0 否 1 是',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='收票认证表';

/*Data for the table `tb_invoice_collect_approve` */

/*Table structure for table `tb_invoice_collect_fee` */

DROP TABLE IF EXISTS `tb_invoice_collect_fee`;

CREATE TABLE `tb_invoice_collect_fee` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '收票费用id',
  `collect_id` bigint(11) DEFAULT NULL COMMENT '收票id',
  `fee_id` bigint(11) DEFAULT NULL COMMENT '费用id',
  `in_rate_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '费用含税金额',
  `ex_rate_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '费用未税金额',
  `tax_rate` decimal(20,2) DEFAULT '0.00' COMMENT '税率',
  `rate_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '税额',
  `remark` varchar(50) DEFAULT NULL COMMENT '备注',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_at` datetime DEFAULT NULL COMMENT '创建时间',
  `is_delete` tinyint(4) DEFAULT '0' COMMENT '是否删除 0 否 1 是',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='进项发票关联费用表';

/*Data for the table `tb_invoice_collect_fee` */

/*Table structure for table `tb_invoice_collect_overseas` */

DROP TABLE IF EXISTS `tb_invoice_collect_overseas`;

CREATE TABLE `tb_invoice_collect_overseas` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '境外收票id自增',
  `apply_no` varchar(50) DEFAULT NULL COMMENT '境外申请编号自动生成',
  `business_unit` bigint(20) DEFAULT NULL COMMENT '经营单位',
  `project_id` bigint(20) DEFAULT NULL COMMENT '项目id',
  `supplier_id` bigint(20) DEFAULT NULL COMMENT '供应商ID',
  `invoice_no` varchar(50) DEFAULT NULL COMMENT '发票号',
  `bill_type` tinyint(4) DEFAULT NULL COMMENT '单据类型(1-货物 2-费用)',
  `account_id` bigint(20) DEFAULT NULL COMMENT '收款账号ID',
  `invoice_num` decimal(20,8) DEFAULT '0.00000000' COMMENT '收票数量',
  `invoice_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '收票金额',
  `invoice_date` datetime DEFAULT NULL COMMENT '收票日期',
  `currnecy_type` tinyint(4) DEFAULT NULL COMMENT '币种',
  `state` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态 1-待提交 25-待财务专员审核 3-待认证 4-已完成',
  `approve_date` datetime DEFAULT NULL COMMENT '认证日期',
  `approve_remark` varchar(500) DEFAULT NULL COMMENT '认证备注',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `is_delete` tinyint(4) DEFAULT '0' COMMENT '是否删除0 否 1 是',
  `create_at` datetime DEFAULT NULL COMMENT '创建时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '单据备注',
  `invoice_remark` varchar(500) DEFAULT NULL COMMENT '票据备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='Invoice境外收票结构表';

/*Data for the table `tb_invoice_collect_overseas` */

/*Table structure for table `tb_invoice_collect_overseas_fee` */

DROP TABLE IF EXISTS `tb_invoice_collect_overseas_fee`;

CREATE TABLE `tb_invoice_collect_overseas_fee` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '收票费用ID自增长',
  `collect_overseas_id` bigint(20) DEFAULT NULL COMMENT '境外收票id',
  `fee_id` bigint(20) DEFAULT NULL COMMENT '费用ID',
  `currency_type` tinyint(4) DEFAULT NULL COMMENT '币种',
  `invoice_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '收票金额',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_at` datetime DEFAULT NULL COMMENT '创建时间',
  `is_delete` tinyint(4) DEFAULT '0' COMMENT '是否删除 0 否 1 是',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='境外收票费用关联结构表';

/*Data for the table `tb_invoice_collect_overseas_fee` */

/*Table structure for table `tb_invoice_collect_overseas_po` */

DROP TABLE IF EXISTS `tb_invoice_collect_overseas_po`;

CREATE TABLE `tb_invoice_collect_overseas_po` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '收票采购ID自增长',
  `collect_overseas_id` bigint(20) DEFAULT NULL COMMENT '境外收票id',
  `po_line_id` bigint(20) DEFAULT NULL COMMENT '采购单id',
  `invoice_amoun` decimal(20,8) DEFAULT '0.00000000' COMMENT '收票金额',
  `real_invoice_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '实际收票金额',
  `invoice_num` decimal(20,8) DEFAULT '0.00000000' COMMENT '收票数量',
  `rate` decimal(20,8) DEFAULT NULL COMMENT '转换汇率',
  `real_currnecy_type` tinyint(4) DEFAULT NULL COMMENT '实际收票币种',
  `currnecy_type` tinyint(4) DEFAULT NULL COMMENT '币种',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_at` datetime DEFAULT NULL COMMENT '创建时间',
  `is_delete` tinyint(4) DEFAULT '0' COMMENT '是否删除 0 否 1 是',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='境外收票采购关联结构表';

/*Data for the table `tb_invoice_collect_overseas_po` */

/*Table structure for table `tb_invoice_collect_po` */

DROP TABLE IF EXISTS `tb_invoice_collect_po`;

CREATE TABLE `tb_invoice_collect_po` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '收票采购信息',
  `collect_id` bigint(11) DEFAULT NULL COMMENT '收票id',
  `po_id` bigint(11) DEFAULT NULL COMMENT '采购id',
  `in_rate_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '含税金额',
  `ex_rate_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '未税金额',
  `rate_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '税额',
  `discount_in_rate_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '折扣含税金额',
  `discount_ex_rate_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '折扣未税金额',
  `discount_rate_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '折扣税额',
  `actual_invoice_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '实际发票金额',
  `tax_rate` decimal(20,2) DEFAULT '0.00' COMMENT '税率',
  `po_num` decimal(20,8) DEFAULT '0.00000000' COMMENT '录入数量',
  `remark` varchar(50) DEFAULT NULL COMMENT '备注',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_at` datetime DEFAULT NULL COMMENT '创建时间',
  `is_delete` tinyint(4) DEFAULT '0' COMMENT '是否删除 0 否 1 是',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='进项发票关联订单表';

/*Data for the table `tb_invoice_collect_po` */

/*Table structure for table `tb_invoice_dtl` */

DROP TABLE IF EXISTS `tb_invoice_dtl`;

CREATE TABLE `tb_invoice_dtl` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `invoice_id` bigint(20) DEFAULT NULL COMMENT '发票ID',
  `name` varchar(100) DEFAULT NULL COMMENT '名称',
  `type` varchar(100) DEFAULT NULL COMMENT '型号',
  `specification` varchar(50) DEFAULT NULL COMMENT '规格',
  `tax_cate_no` varchar(20) DEFAULT NULL COMMENT '税收分类编码',
  `currency_type` tinyint(4) DEFAULT NULL COMMENT '币种 1.人民币 2.美元',
  `exchange_rate` decimal(20,8) DEFAULT '0.00000000' COMMENT '汇率',
  `num` decimal(20,8) DEFAULT '0.00000000' COMMENT '数量',
  `price` decimal(20,8) DEFAULT '0.00000000' COMMENT '单价',
  `rate` decimal(20,8) DEFAULT '0.00000000' COMMENT '税率',
  `rate_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '税额',
  `in_rate_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '含税金额',
  `ex_rate_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '未税金额',
  `discount_rate_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '折扣税额',
  `discount_in_rate_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '折扣含税金额',
  `discount_ex_rate_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '折扣未税金额',
  `discount` decimal(20,8) DEFAULT '0.00000000' COMMENT '折扣率',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人ID ',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `unit` varchar(50) DEFAULT NULL COMMENT '单位',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='发票明细表';

/*Data for the table `tb_invoice_dtl` */

/*Table structure for table `tb_invoice_overseas` */

DROP TABLE IF EXISTS `tb_invoice_overseas`;

CREATE TABLE `tb_invoice_overseas` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '境外开票id自增',
  `apply_no` varchar(50) DEFAULT NULL COMMENT '申请编号自动生成',
  `business_unit` bigint(20) DEFAULT NULL COMMENT '经营单位',
  `project_id` bigint(20) DEFAULT NULL COMMENT '项目id',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '客户id',
  `currnecy_type` int(11) DEFAULT NULL COMMENT '币种',
  `account_id` int(11) DEFAULT NULL COMMENT '收款账户id',
  `invoice_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '申请金额',
  `print_num` int(11) DEFAULT NULL COMMENT '打印次数',
  `balance_start_date` datetime DEFAULT NULL COMMENT '结算开始日期',
  `balance_end_date` datetime DEFAULT NULL COMMENT '结算结束日期',
  `bill_type` tinyint(4) DEFAULT NULL COMMENT '单据类型',
  `fee_type` tinyint(4) DEFAULT NULL COMMENT '开票明细',
  `is_merge` tinyint(4) DEFAULT '0' COMMENT '同品合并  0 否 1 是',
  `invoice_remark` varchar(500) DEFAULT NULL COMMENT '票据备注',
  `remark` varchar(500) DEFAULT NULL COMMENT '单据备注',
  `state` tinyint(4) DEFAULT '1' COMMENT '状态 1 待提交 20 待财务专员审核 30 待财务主管审核 2 已完成',
  `creator_id` bigint(11) DEFAULT NULL COMMENT '创建人ID',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_at` datetime DEFAULT NULL COMMENT '创建时间',
  `is_delete` tinyint(4) DEFAULT '0' COMMENT '是否删除 0 否 1 是',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `tb_invoice_overseas` */

/*Table structure for table `tb_invoice_overseas_fee` */

DROP TABLE IF EXISTS `tb_invoice_overseas_fee`;

CREATE TABLE `tb_invoice_overseas_fee` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '费用id自增',
  `overseas_id` bigint(11) DEFAULT NULL COMMENT '境外开票id',
  `fee_id` bigint(11) DEFAULT NULL COMMENT '费用id',
  `invoice_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '开票金额',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_at` datetime DEFAULT NULL COMMENT '创建时间',
  `is_delete` tinyint(4) DEFAULT '0' COMMENT '是否删除 0 否 1 是',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `tb_invoice_overseas_fee` */

/*Table structure for table `tb_invoice_overseas_po` */

DROP TABLE IF EXISTS `tb_invoice_overseas_po`;

CREATE TABLE `tb_invoice_overseas_po` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '销售id自增长',
  `overseas_id` bigint(11) DEFAULT NULL COMMENT '境外收票id',
  `bill_delivery_id` bigint(11) DEFAULT NULL COMMENT '销售的id',
  `invoice_num` decimal(20,8) DEFAULT '0.00000000' COMMENT '开票数量',
  `invoice_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '开票金额',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_at` datetime DEFAULT NULL COMMENT '创建时间',
  `is_delete` tinyint(4) DEFAULT '0' COMMENT '是否删除 0 否 1 是',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `tb_invoice_overseas_po` */

/*Table structure for table `tb_invoicing_wechar` */

DROP TABLE IF EXISTS `tb_invoicing_wechar`;

CREATE TABLE `tb_invoicing_wechar` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `supplier_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '供应商id',
  `sku` varchar(50) NOT NULL DEFAULT '' COMMENT '商品sku',
  `send_time` varchar(20) NOT NULL DEFAULT '' COMMENT '发送时间',
  `salse_num` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '销售数量',
  `stockin_num` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '入库数量',
  `remain_send_num` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '可用库存数量',
  `daily_salse_num` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '昨日销售数量',
  `daily_stockin_num` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '昨日入库数量',
  `send_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '发送类型：0 入库 1 出口',
  `send_sate` tinyint(4) NOT NULL DEFAULT '0' COMMENT '发送状态：0 未发送 1 发送成功',
  `good_type` varchar(500) NOT NULL DEFAULT '' COMMENT '商品类型',
  `create_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='进销存微信推送';

/*Data for the table `tb_invoicing_wechar` */

/*Table structure for table `tb_invoke_log` */

DROP TABLE IF EXISTS `tb_invoke_log`;

CREATE TABLE `tb_invoke_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `invoke_type` int(10) DEFAULT NULL COMMENT '接口类型(例如8001-入库单接口，第一位为模块类型，后三位自定义)',
  `invoke_mode` tinyint(4) DEFAULT NULL COMMENT '调用模式 1-实时 2-异步',
  `module_type` tinyint(4) DEFAULT NULL COMMENT '模块类型 7-销售 8-物流',
  `bill_type` tinyint(4) DEFAULT NULL COMMENT '单据类型 1-采购订单 2-提货单 3-出库单 4-入库单 5-付款单 6-合并付款单',
  `bill_id` bigint(20) DEFAULT NULL COMMENT '单据ID',
  `bill_no` varchar(20) DEFAULT NULL COMMENT '单据编号',
  `bill_date` datetime DEFAULT NULL COMMENT '单据日期',
  `provider` tinyint(4) DEFAULT NULL COMMENT '提供方(1-scfs 2-wms 3-pms 4-cms)',
  `consumer` tinyint(4) DEFAULT NULL COMMENT '调用方(1-scfs 2-wms 3-pms 4-cms)',
  `is_success` tinyint(4) DEFAULT '0' COMMENT '调用返回结果 0-未调用 1-调用失败 2-调用成功',
  `return_msg` text COMMENT '调用返回消息',
  `try_num` tinyint(4) DEFAULT '0' COMMENT '调用重试次数',
  `exception_msg` text COMMENT '调用异常信息',
  `try_again_flag` tinyint(4) DEFAULT '0' COMMENT '是否重新调用 0-不重新调用 1-重新调用',
  `deal_flag` tinyint(4) DEFAULT '0' COMMENT '提供者为scfs时使用，0-未处理 1-处理失败 2-处理成功',
  `deal_num` tinyint(4) DEFAULT '0' COMMENT '处理重试次数',
  `deal_msg` text COMMENT '处理失败原因',
  `deal_again_flag` tinyint(4) DEFAULT '0' COMMENT '是否重新处理 0-不重新处理 1-重新处理',
  `content` text COMMENT '传输内容',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='同步接口日志表';

/*Data for the table `tb_invoke_log` */

/*Table structure for table `tb_matter_manage` */

DROP TABLE IF EXISTS `tb_matter_manage`;

CREATE TABLE `tb_matter_manage` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `matter_no` varchar(13) DEFAULT NULL COMMENT '事项编码,自动生成',
  `cust_main_id` bigint(20) DEFAULT NULL COMMENT '客户维护id',
  `matter_name` tinyint(4) DEFAULT NULL COMMENT '事项名称,1 项目导入表 2 项目监控',
  `matter_type` tinyint(4) DEFAULT NULL COMMENT '事项类型, 1 客户事项 2 项目事项',
  `project_id` bigint(20) DEFAULT NULL COMMENT '项目',
  `customer_name` varchar(100) DEFAULT NULL COMMENT '客户名称',
  `customer_abbreviate` varchar(50) DEFAULT NULL COMMENT '客户简称',
  `stage` tinyint(1) DEFAULT NULL COMMENT '所处阶段 1 意向阶段 2 合作阶段 3 已取消',
  `hk_company` varchar(150) DEFAULT NULL COMMENT '香港公司全称',
  `enterprise_bus` varchar(100) DEFAULT NULL COMMENT '企业主营业务',
  `matter_describe` varchar(200) DEFAULT NULL COMMENT '事项描述',
  `office_address` varchar(500) DEFAULT NULL COMMENT '办公地址',
  `reg_address` varchar(200) DEFAULT NULL COMMENT '注册地址',
  `contacts` varchar(50) DEFAULT NULL COMMENT '联系人',
  `contacts_number` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `dis_goods` varchar(100) DEFAULT NULL COMMENT '铺货商品描述',
  `biz_manager_id` bigint(20) DEFAULT NULL COMMENT '业务审核人',
  `business_manager_id` bigint(20) DEFAULT NULL COMMENT '商务审核人',
  `department_id` bigint(20) DEFAULT NULL COMMENT '部门主管审核人',
  `justice_id` bigint(20) DEFAULT NULL COMMENT '法务审核人',
  `finance_manager_id` bigint(20) DEFAULT NULL COMMENT '财务主管审核人',
  `risk_manager_id` bigint(20) DEFAULT NULL COMMENT '分控主管审核人',
  `state` tinyint(4) NOT NULL DEFAULT '0' COMMENT '状态 0待提交',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `creator` varchar(50) NOT NULL COMMENT '创建人',
  `creator_id` tinyint(4) DEFAULT NULL COMMENT '创建人id',
  `create_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `delete_at` datetime DEFAULT NULL COMMENT '作废时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_delete` tinyint(4) DEFAULT '0' COMMENT '0 : 有效 1 : 删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='事项信息表';

/*Data for the table `tb_matter_manage` */

/*Table structure for table `tb_matter_service` */

DROP TABLE IF EXISTS `tb_matter_service`;

CREATE TABLE `tb_matter_service` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '服务要求,主键id',
  `matter_id` bigint(20) DEFAULT NULL COMMENT '事项id',
  `service_type` tinyint(4) DEFAULT NULL COMMENT '服务类型',
  `service_type_remark` varchar(50) DEFAULT NULL COMMENT '服务类型备注',
  `service_explain` varchar(200) DEFAULT NULL COMMENT '业务模型说明(项目流程说明)',
  `account_period` tinyint(4) DEFAULT NULL COMMENT '合作账期类型',
  `account_period_remark` varchar(50) DEFAULT NULL COMMENT '合作账期类型备注',
  `matched_amount` varchar(50) DEFAULT NULL COMMENT '配套资金申请金额',
  `currnecy_type` tinyint(4) DEFAULT NULL COMMENT '币种',
  `currnecy_type_remark` varchar(50) DEFAULT NULL COMMENT '币种备注',
  `deposit_paid` tinyint(4) DEFAULT NULL COMMENT '收取待采定金',
  `deposit_amount` varchar(50) DEFAULT NULL COMMENT '待采金额',
  `service_rate_type` tinyint(4) DEFAULT NULL COMMENT '服务费率类型',
  `service_rate` varchar(50) DEFAULT NULL COMMENT '服务费率',
  `rec_fee_type` tinyint(4) DEFAULT NULL COMMENT '应收费用类型',
  `rec_fee_remark` varchar(50) DEFAULT NULL COMMENT '应收费用备注',
  `pay_fee_type` tinyint(4) DEFAULT NULL COMMENT '应付费用类型',
  `pay_fee_remark` varchar(50) DEFAULT NULL COMMENT '应付费用备注',
  `service_settlement_time` tinyint(4) DEFAULT NULL COMMENT '服务费结算时间',
  `service_settlement_remark` varchar(50) DEFAULT NULL COMMENT '服务费结算时间备注',
  `pay_way` tinyint(4) DEFAULT NULL COMMENT '付款方式',
  `lend_way` tinyint(4) DEFAULT NULL COMMENT '放贷方式',
  `lend_way_remark` varchar(50) DEFAULT NULL COMMENT '放贷方式备注',
  `scale` varchar(200) DEFAULT NULL COMMENT '预计年度垫资规模',
  `turnover_times` varchar(50) DEFAULT NULL COMMENT '预计资金周转次数（/年）',
  `return_rate` varchar(50) DEFAULT NULL COMMENT '预计资金回报率（/年）',
  `cost_expend_type` tinyint(4) DEFAULT NULL COMMENT '成本支出项',
  `biz_blance` varchar(50) DEFAULT NULL COMMENT '预计年度业务毛利',
  `blance` varchar(50) DEFAULT NULL COMMENT '预计年度业务净利',
  `sign_subject` varchar(100) DEFAULT NULL COMMENT '签约主体',
  `project_biz_manager` varchar(50) DEFAULT NULL COMMENT '项目导入业务人员',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `creator_id` tinyint(4) DEFAULT NULL COMMENT '创建人id',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='服务要求';

/*Data for the table `tb_matter_service` */

/*Table structure for table `tb_merge_pay_order` */

DROP TABLE IF EXISTS `tb_merge_pay_order`;

CREATE TABLE `tb_merge_pay_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `merge_pay_no` varchar(50) NOT NULL COMMENT '合并付款编号',
  `busi_unit` int(11) NOT NULL COMMENT '经营单位',
  `project_id` int(11) NOT NULL COMMENT '项目id',
  `pay_type` tinyint(4) DEFAULT NULL COMMENT '付款类型',
  `currency_type` tinyint(4) DEFAULT NULL COMMENT '币种',
  `pay_way` tinyint(4) DEFAULT NULL COMMENT '付款方式',
  `pay_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '付款金额',
  `request_pay_time` datetime DEFAULT NULL COMMENT '要求付款时间',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `creator_id` int(11) DEFAULT NULL COMMENT '创建人id',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_at` datetime DEFAULT NULL COMMENT '创建时间',
  `update_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `payee` int(11) DEFAULT NULL COMMENT '收款单位',
  `pay_account_id` int(11) DEFAULT NULL COMMENT '收款账号',
  `payer` int(11) DEFAULT NULL COMMENT '付款单位',
  `state` tinyint(4) DEFAULT NULL COMMENT '0 待提交 25待财务审核 30待财务主管审核 80待部门主管审核 90 待总经理审核 6已完成',
  `print_num` int(10) DEFAULT '0' COMMENT '打印次数',
  `union_print_identifier` varchar(6) DEFAULT NULL COMMENT '统一打印标示符',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `tb_merge_pay_order` */

/*Table structure for table `tb_merge_pay_order_rel` */

DROP TABLE IF EXISTS `tb_merge_pay_order_rel`;

CREATE TABLE `tb_merge_pay_order_rel` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `merge_pay_id` int(11) NOT NULL COMMENT '合并付款id',
  `pay_id` int(11) NOT NULL COMMENT '付款id',
  `pay_amount` decimal(20,2) NOT NULL COMMENT '付款金额',
  `creator` varchar(50) DEFAULT NULL,
  `creator_id` int(11) DEFAULT NULL,
  `create_at` datetime DEFAULT NULL COMMENT '创建时间',
  `update_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `tb_merge_pay_order_rel` */

/*Table structure for table `tb_monitor_log` */

DROP TABLE IF EXISTS `tb_monitor_log`;

CREATE TABLE `tb_monitor_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `url` varchar(200) DEFAULT NULL COMMENT '请求URL',
  `time` int(11) DEFAULT NULL COMMENT '请求时间，单位毫秒',
  `creator_id` int(11) DEFAULT NULL COMMENT '请求人ID',
  `creator` varchar(50) DEFAULT NULL COMMENT '请求人名称',
  `create_at` datetime DEFAULT NULL COMMENT '请求时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

/*Table structure for table `tb_msg_content` */

DROP TABLE IF EXISTS `tb_msg_content`;

CREATE TABLE `tb_msg_content` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '消息ID',
  `to_accounts` varchar(2000) DEFAULT NULL COMMENT '接收消息账号，多个账号用英文逗号隔开',
  `cc_accounts` varchar(2000) DEFAULT NULL COMMENT '抄送账号，多个账号用英文逗号隔开',
  `msg_title` varchar(500) DEFAULT NULL COMMENT '消息头',
  `msg_content` text COMMENT '消息内容',
  `msg_type` tinyint(4) DEFAULT NULL COMMENT '消息类型，1表示RTX,2表示email,3表示sms短信，4表示微信，5表示语音电话，',
  `is_send` tinyint(4) DEFAULT NULL COMMENT '1表示未发送，2表示发送成功，3表示发送失败',
  `send_count` tinyint(4) DEFAULT NULL COMMENT '发送次数，大于五次不在发送',
  `send_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `creator_id` int(11) DEFAULT NULL COMMENT '创建人ID',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `is_delete` tinyint(4) DEFAULT '0' COMMENT '逻辑删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='消息表';

/*Data for the table `tb_msg_content` */

/*Table structure for table `tb_pay_advance_relation` */

DROP TABLE IF EXISTS `tb_pay_advance_relation`;

CREATE TABLE `tb_pay_advance_relation` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '付款预收id',
  `pay_id` int(11) DEFAULT NULL COMMENT '付款ID(tb_pay_order[id])',
  `advance_id` bigint(20) DEFAULT NULL COMMENT '预收ID(tb_fi_advance[id])',
  `pay_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '金额',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `creator_id` int(11) DEFAULT NULL COMMENT '创建人ID',
  `create_at` datetime DEFAULT NULL COMMENT '创建时间',
  `is_delete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_tb_pay_advance_relation_pay_id` (`pay_id`,`advance_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='付款关联预收表';

/*Data for the table `tb_pay_advance_relation` */

/*Table structure for table `tb_pay_deduction_fee_relation` */

DROP TABLE IF EXISTS `tb_pay_deduction_fee_relation`;

CREATE TABLE `tb_pay_deduction_fee_relation` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `pay_id` int(11) DEFAULT NULL COMMENT '付款ID(tb_pay_order[id])',
  `fee_id` int(11) DEFAULT NULL COMMENT '费用ID',
  `pay_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '付款金额',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `creator_id` int(11) DEFAULT NULL COMMENT '创建人ID',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_delete` tinyint(4) DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_tb_pay_deduction_fee_relation_pay_id` (`pay_id`,`fee_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='付款与抵扣费用关联';

/*Data for the table `tb_pay_deduction_fee_relation` */

/*Table structure for table `tb_pay_fee_relation` */

DROP TABLE IF EXISTS `tb_pay_fee_relation`;

CREATE TABLE `tb_pay_fee_relation` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `pay_id` int(11) DEFAULT NULL COMMENT '付款ID(tb_pay_order[id])',
  `fee_id` int(11) DEFAULT NULL COMMENT '费用ID',
  `pay_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '付款金额',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `creator_id` int(11) DEFAULT NULL COMMENT '创建人ID',
  `create_at` datetime DEFAULT NULL COMMENT '创建日期',
  `is_delete` tinyint(4) DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_tb_pay_fee_relation_pay_id` (`pay_id`,`fee_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='付款关联费用表';

/*Data for the table `tb_pay_fee_relation` */

/*Table structure for table `tb_pay_keyword` */

DROP TABLE IF EXISTS `tb_pay_keyword`;

CREATE TABLE `tb_pay_keyword` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '关键字id',
  `word` varchar(50) DEFAULT NULL COMMENT '关键词',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `creator_id` bigint(11) DEFAULT NULL COMMENT '创建人id',
  `create_at` datetime DEFAULT NULL COMMENT '创建时间',
  `is_delete` tinyint(4) DEFAULT NULL COMMENT '是否删除',
  `deleter` varchar(50) DEFAULT NULL COMMENT '作废人',
  `deleter_id` bigint(20) DEFAULT NULL COMMENT '作废人id',
  `delete_at` datetime DEFAULT NULL COMMENT '作废时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `tb_pay_keyword` */

/*Table structure for table `tb_pay_order` */

DROP TABLE IF EXISTS `tb_pay_order`;

CREATE TABLE `tb_pay_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `pay_no` varchar(50) NOT NULL COMMENT '付款编号 自动生成',
  `pay_type` tinyint(4) DEFAULT NULL COMMENT '付款类型',
  `project_id` int(11) DEFAULT NULL COMMENT '项目ID',
  `busi_unit` int(11) DEFAULT NULL COMMENT '经营单位',
  `payer` int(11) DEFAULT NULL COMMENT '付款单位',
  `pay_way` tinyint(4) DEFAULT NULL COMMENT '付款方式',
  `pay_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '付款金额',
  `payee` int(11) DEFAULT NULL COMMENT '收款单位',
  `pay_account_id` int(11) DEFAULT NULL COMMENT '收款账号ID',
  `request_pay_time` datetime DEFAULT NULL COMMENT '要求付款日期',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `state` tinyint(4) DEFAULT '0' COMMENT '0 待提交 1待业务审核 2待财务审核 3待风控审核 4待确认 5待开立 6已完成',
  `create_at` datetime DEFAULT NULL COMMENT '创建日期',
  `creator_id` int(11) DEFAULT NULL COMMENT '创建人ID',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `is_delete` tinyint(4) DEFAULT '0' COMMENT '是否删除',
  `confirmor_at` datetime DEFAULT NULL COMMENT '确认时间',
  `confirmor_id` int(11) DEFAULT NULL COMMENT '确认人ID',
  `confirmor` varchar(50) DEFAULT NULL COMMENT '确认人',
  `blance` decimal(20,8) DEFAULT '0.00000000' COMMENT '收款余额',
  `po_blance` decimal(20,8) DEFAULT '0.00000000' COMMENT '付款订单总额',
  `fee_blance` decimal(20,8) DEFAULT '0.00000000' COMMENT '付款费用总额',
  `payment_account` int(11) DEFAULT NULL COMMENT '付款账号',
  `bank_charge` decimal(20,8) DEFAULT '0.00000000' COMMENT '银行手续费',
  `currnecy_type` int(11) DEFAULT NULL COMMENT '币种',
  `open_type` int(50) DEFAULT '0' COMMENT '开立类型',
  `advance_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '预收金额',
  `deduction_fee_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '抵扣费用金额',
  `attached_numbe` varchar(50) DEFAULT NULL COMMENT '附属编号',
  `inner_pay_date` date DEFAULT NULL COMMENT '预计内部打款日期',
  `real_pay_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '实际付款金额',
  `real_currency_type` tinyint(4) DEFAULT NULL COMMENT '实际付款币种',
  `pay_rate` decimal(20,8) DEFAULT NULL COMMENT '实际付款币种兑付款币种汇率',
  `print_num` int(10) DEFAULT '0' COMMENT '打印次数',
  `union_over_identifier` varchar(6) DEFAULT NULL COMMENT '批量确认标示符',
  `union_print_identifier` varchar(6) DEFAULT NULL COMMENT '统一打印标示符',
  `merge_pay_no` varchar(50) DEFAULT '' COMMENT '合并付款编号',
  `discount_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '折扣金额',
  `in_discount_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '折扣前金额',
  `pay_way_type` tinyint(4) DEFAULT '0' COMMENT '付款支付类型 0-全部 1-预付 2-尾款',
  `cms_payer` varchar(50) DEFAULT '' COMMENT 'cms付款人',
  `cms_rejecter` varchar(50) DEFAULT '' COMMENT 'cms驳回人',
  `reason` varchar(200) DEFAULT '' COMMENT '原因',
  `memo_time` datetime NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '水单日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='付款单';

/*Data for the table `tb_pay_order` */

/*Table structure for table `tb_pay_po_relation` */

DROP TABLE IF EXISTS `tb_pay_po_relation`;

CREATE TABLE `tb_pay_po_relation` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `pay_id` int(11) DEFAULT NULL COMMENT '付款ID(tb_pay_order[id])',
  `po_id` int(11) DEFAULT NULL COMMENT '采购单ID(tb_purchase_order_title[id])',
  `pay_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '付款金额',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `creator_id` int(11) DEFAULT NULL COMMENT '创建人ID',
  `create_at` datetime DEFAULT NULL COMMENT '创建时间',
  `is_delete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `po_line_id` int(11) DEFAULT NULL COMMENT '采购单ID(tb_purchase_order_title[id])',
  `discount_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '折扣金额',
  `in_discount_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '折扣前金额',
  `duction_money` decimal(20,8) DEFAULT '0.00000000' COMMENT '抵扣金额',
  PRIMARY KEY (`id`),
  KEY `idx_tb_pay_po_relation_pay_id` (`pay_id`,`po_id`,`po_line_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='付款与采购单关联';

/*Data for the table `tb_pay_po_relation` */

/*Table structure for table `tb_pay_pool` */

DROP TABLE IF EXISTS `tb_pay_pool`;

CREATE TABLE `tb_pay_pool` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `business_unit_id` bigint(20) DEFAULT NULL COMMENT '经营单位ID',
  `account_id` int(11) DEFAULT NULL COMMENT '账户ID',
  `project_id` bigint(20) DEFAULT NULL COMMENT '项目ID',
  `in_out` tinyint(4) DEFAULT NULL COMMENT '出入类型1、支出；2、收入',
  `type` tinyint(4) DEFAULT NULL COMMENT '类别：1、收内部款；2、付内部款；3、收客户款；4、付客户款；5、收融资款；6、付融资款',
  `bill_id` bigint(20) DEFAULT NULL COMMENT '单据ID',
  `bill_type` tinyint(4) DEFAULT NULL COMMENT '单据类别:1、付款；2、收款；3、凭证',
  `bill_no` varchar(20) DEFAULT NULL COMMENT '单据编号',
  `amount` decimal(40,8) DEFAULT '0.00000000' COMMENT '金额',
  `currency_type` tinyint(4) DEFAULT NULL COMMENT '币种',
  `deleter` varchar(50) DEFAULT NULL COMMENT '作废人',
  `delete_at` datetime DEFAULT NULL COMMENT '作废时间',
  `creater_id` bigint(20) DEFAULT NULL COMMENT '创建id',
  `creater_at` datetime DEFAULT NULL COMMENT '创建时间',
  `creater` varchar(50) DEFAULT NULL COMMENT '创建人',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `deleter_id` bigint(20) DEFAULT NULL COMMENT '作废人id',
  `is_delete` tinyint(2) DEFAULT '0' COMMENT '是否作废：0、不作废；1、作废',
  `update_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='资金池';

/*Data for the table `tb_pay_pool` */

/*Table structure for table `tb_pay_refund_relation` */

DROP TABLE IF EXISTS `tb_pay_refund_relation`;

CREATE TABLE `tb_pay_refund_relation` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '退款,付款，水单的关联id,自增长',
  `pay_id` int(11) DEFAULT NULL COMMENT '付款项目的ID',
  `refund_im_id` bigint(20) DEFAULT NULL COMMENT '退款信息表的ID',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `creator_id` int(11) DEFAULT NULL COMMENT '创建人ID',
  `create_at` datetime DEFAULT NULL COMMENT '创建时间',
  `receipt_id` bigint(20) NOT NULL COMMENT '水单的ID',
  `project_id` int(50) DEFAULT NULL COMMENT '项目ID',
  `busi_unit` bigint(20) DEFAULT NULL COMMENT '经营单位 关联tb_base_subject[id]',
  `cust_id` int(50) DEFAULT NULL COMMENT '客户录入',
  `receipt_at` datetime NOT NULL COMMENT '水单日期',
  `is_delete` int(4) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `pay_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '付款金额',
  `currency_type` tinyint(4) NOT NULL COMMENT '币种',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='付款,退款水单信息关联表';

/*Data for the table `tb_pay_refund_relation` */

/*Table structure for table `tb_performance_report` */

DROP TABLE IF EXISTS `tb_performance_report`;

CREATE TABLE `tb_performance_report` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `report_record_id` bigint(20) DEFAULT NULL COMMENT '报表记录表ID',
  `issue` varchar(20) DEFAULT NULL COMMENT '期号',
  `bill_id` bigint(20) DEFAULT NULL COMMENT '关联单据ID',
  `project_id` bigint(20) DEFAULT NULL COMMENT '项目ID',
  `department_id` bigint(20) DEFAULT NULL COMMENT '部门ID',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '客户ID',
  `biz_manager_id` bigint(20) DEFAULT NULL COMMENT '业务员ID',
  `warehouse_id` bigint(20) DEFAULT NULL COMMENT '仓库ID',
  `business_unit_id` bigint(20) DEFAULT NULL COMMENT '经营单位ID',
  `biz_type` tinyint(4) DEFAULT NULL COMMENT '业务类型',
  `bill_type` tinyint(4) DEFAULT NULL COMMENT '单据类型',
  `order_no` varchar(20) DEFAULT NULL COMMENT '订单编号',
  `bill_no` varchar(20) DEFAULT NULL COMMENT '单据编号',
  `product_no` varchar(200) DEFAULT NULL COMMENT '产品编号',
  `num` decimal(20,8) DEFAULT '0.00000000' COMMENT '数量',
  `place_date` date DEFAULT NULL COMMENT '下单日期',
  `statistics_date` date DEFAULT NULL COMMENT '统计日期',
  `sale_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '销售总价',
  `service_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '服务收入',
  `composite_tax_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '综合税金',
  `purchase_cost` decimal(20,8) DEFAULT '0.00000000' COMMENT '采购成本',
  `fund_cost` decimal(20,8) DEFAULT '0.00000000' COMMENT '资金成本',
  `warehouse_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '仓储物流费',
  `market_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '市场费用',
  `finance_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '财务费用',
  `manage_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '管理费用',
  `manual_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '人工费用',
  `profit_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '利润',
  `profit_rate` decimal(20,8) DEFAULT '0.00000000' COMMENT '利润率',
  `tax_rate` decimal(20,8) DEFAULT '0.00000000' COMMENT '税率',
  `currency_type` tinyint(4) DEFAULT NULL COMMENT '币种 1.人民币',
  `exchange_rate` decimal(20,8) DEFAULT '0.00000000' COMMENT '汇率',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `creator` varchar(100) DEFAULT NULL COMMENT '创建人',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='绩效报表';

/*Data for the table `tb_performance_report` */

/*Table structure for table `tb_pms_factor_pay_confirm` */

DROP TABLE IF EXISTS `tb_pms_factor_pay_confirm`;

CREATE TABLE `tb_pms_factor_pay_confirm` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `provider_sn` varchar(50) NOT NULL DEFAULT '' COMMENT '供应商编号',
  `bank_series_no` varchar(128) NOT NULL DEFAULT '' COMMENT '银行流水号',
  `confirm_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '确认日期',
  `corporation_code` varchar(8) NOT NULL DEFAULT '' COMMENT '付款法人代码',
  `corporation_name` varchar(128) NOT NULL DEFAULT '' COMMENT '付款法人名称',
  `new_pay_no` varchar(50) NOT NULL DEFAULT '' COMMENT '新付款编号',
  `pay_no` varchar(50) NOT NULL DEFAULT '' COMMENT '原付款编号',
  `currency_type` varchar(20) NOT NULL DEFAULT '' COMMENT '付款币种',
  `pay_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '付款金额',
  `real_currency_type` varchar(20) NOT NULL DEFAULT '' COMMENT '实际付款币种',
  `real_pay_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '实际付款金额',
  `create_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='PMS付款确认(应收保理)';

/*Data for the table `tb_pms_factor_pay_confirm` */

/*Table structure for table `tb_pms_out_po_rel` */

DROP TABLE IF EXISTS `tb_pms_out_po_rel`;

CREATE TABLE `tb_pms_out_po_rel` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '铺货出库采购关联id',
  `pms_out_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '铺货出库ID',
  `po_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '采购单ID',
  `po_line_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '采购单明细ID',
  `out_number` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '出库数量',
  `create_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `is_delete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除标识 0 否 1 是',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='铺货出库采购关联表';

/*Data for the table `tb_pms_out_po_rel` */

/*Table structure for table `tb_pms_pay` */

DROP TABLE IF EXISTS `tb_pms_pay`;

CREATE TABLE `tb_pms_pay` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `pms_series_id` bigint(20) NOT NULL COMMENT '订单头id',
  `pay_sn` varchar(50) NOT NULL COMMENT '请款单号',
  `provider_sn` varchar(50) NOT NULL COMMENT '供应商编号',
  `currency_type` varchar(10) NOT NULL COMMENT '币种',
  `pay_create_time` datetime DEFAULT NULL COMMENT '请款单创建日期',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态 0-待请款 1-驳回',
  `unique_number` varchar(255) DEFAULT NULL COMMENT '唯一流水号',
  `deduction_money` decimal(20,8) DEFAULT '0.00000000' COMMENT '抵扣金额',
  `flag` tinyint(4) DEFAULT '0' COMMENT '返回值 0-接收成功 1-接收失败',
  `msg` varchar(500) DEFAULT NULL COMMENT '返回消息',
  `deal_flag` tinyint(4) DEFAULT '1' COMMENT '处理结果 1-待处理 2-处理失败 3-处理成功',
  `deal_msg` varchar(500) DEFAULT NULL COMMENT '处理消息',
  `create_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='PMS同步请款单(待付款或驳回)';

/*Data for the table `tb_pms_pay` */

/*Table structure for table `tb_pms_pay_dtl` */

DROP TABLE IF EXISTS `tb_pms_pay_dtl`;

CREATE TABLE `tb_pms_pay_dtl` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `pms_pay_id` bigint(20) NOT NULL COMMENT 'PMS同步请款单(待付款或驳回)id',
  `purchase_sn` varchar(50) NOT NULL COMMENT '采购单号',
  `account_sn` varchar(50) NOT NULL DEFAULT '' COMMENT 'pms结算对象编码',
  `sku` varchar(50) NOT NULL COMMENT '商品sku',
  `pay_quantity` decimal(20,8) DEFAULT '0.00000000' COMMENT '数量',
  `deal_price` decimal(20,8) DEFAULT '0.00000000' COMMENT '价格',
  `create_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='PMS同步请款单明细(待付款或驳回)';

/*Data for the table `tb_pms_pay_dtl` */

/*Table structure for table `tb_pms_pay_order_dtl` */

DROP TABLE IF EXISTS `tb_pms_pay_order_dtl`;

CREATE TABLE `tb_pms_pay_order_dtl` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `pms_pay_order_title_id` bigint(20) NOT NULL COMMENT '订单头id',
  `po_no` varchar(50) NOT NULL COMMENT '采购订单编号',
  `goods_no` varchar(50) NOT NULL COMMENT '商品编号',
  `goods_name` varchar(255) NOT NULL COMMENT '商品名称',
  `in_qty` decimal(20,8) DEFAULT '0.00000000' COMMENT '入库数量',
  `in_price` decimal(20,8) DEFAULT '0.00000000' COMMENT '不含税成本单价',
  `message` varchar(200) DEFAULT NULL COMMENT '描述',
  `create_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='PMS同步请款明细';

/*Data for the table `tb_pms_pay_order_dtl` */

/*Table structure for table `tb_pms_pay_order_title` */

DROP TABLE IF EXISTS `tb_pms_pay_order_title`;

CREATE TABLE `tb_pms_pay_order_title` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `pay_no` varchar(50) NOT NULL COMMENT '付款编号',
  `corporation_code` varchar(8) NOT NULL COMMENT '付款法人代码',
  `corporation_name` varchar(128) NOT NULL COMMENT '付款法人名称',
  `vendor_no` varchar(50) NOT NULL COMMENT '供应商编号',
  `pay_date` date NOT NULL COMMENT '请款日期',
  `inner_pay_date` date NOT NULL COMMENT '预计内部打款日期',
  `pay_money` decimal(20,8) DEFAULT '0.00000000' COMMENT '付款总金额',
  `pay_currency` varchar(10) NOT NULL COMMENT '付款币种',
  `currency_type` tinyint(4) NOT NULL COMMENT '系统内部币种',
  `state` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态 1：待处理 2：已驳回 3：处理失败 4: 付款待确认 5:已完成',
  `message` varchar(200) DEFAULT NULL COMMENT '描述',
  `create_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `supplier_no` varchar(50) DEFAULT NULL COMMENT '系统内部供应商编码',
  `deduction_money` decimal(20,8) DEFAULT '0.00000000' COMMENT '抵扣金额',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='pms同步请款单数据';

/*Data for the table `tb_pms_pay_order_title` */

/*Table structure for table `tb_pms_pay_po_rel` */

DROP TABLE IF EXISTS `tb_pms_pay_po_rel`;

CREATE TABLE `tb_pms_pay_po_rel` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '请款单采购关联ID',
  `pms_pay_dtl_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '请款单明细ID',
  `po_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '铺货类型的采购ID',
  `po_line_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '铺货类型的采购明细ID',
  `py_po_line_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '采购类型的采购明细ID',
  `pay_quantity` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '请款数量',
  `create_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `is_delete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除标识 0 否 1 是',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='PMS请款单采购关联表';

/*Data for the table `tb_pms_pay_po_rel` */

/*Table structure for table `tb_pms_pledge_pay_confirm` */

DROP TABLE IF EXISTS `tb_pms_pledge_pay_confirm`;

CREATE TABLE `tb_pms_pledge_pay_confirm` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `purchase_sn` varchar(500) NOT NULL DEFAULT '' COMMENT '采购单号',
  `pay_sn` varchar(50) NOT NULL DEFAULT '' COMMENT '请款单号',
  `currency_type` varchar(20) NOT NULL DEFAULT '' COMMENT '请款币种',
  `real_currency_type` varchar(20) NOT NULL DEFAULT '' COMMENT '实付币种',
  `pay_price` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '请款金额',
  `currency_money` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '实付金额',
  `verify_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '确认时间',
  `create_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='PMS付款确认(融通质押)';

/*Data for the table `tb_pms_pledge_pay_confirm` */

/*Table structure for table `tb_pms_retun_po_rel` */

DROP TABLE IF EXISTS `tb_pms_retun_po_rel`;

CREATE TABLE `tb_pms_retun_po_rel` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '退货采购关联Id',
  `rt_po_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '铺货退货类型采购头ID',
  `rt_po_line_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '铺货退货类型采购明细ID',
  `po_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '代销订单采购头ID',
  `po_line_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '代销订单采购明细ID',
  `return_number` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '退货数量',
  `create_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='退货采购关联表';

/*Data for the table `tb_pms_retun_po_rel` */

/*Table structure for table `tb_pms_return` */

DROP TABLE IF EXISTS `tb_pms_return`;

CREATE TABLE `tb_pms_return` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `refund_order_sn` varchar(50) NOT NULL COMMENT 'PMS退货单号',
  `provider_sn` varchar(50) NOT NULL COMMENT '供应商编号',
  `submit_time` datetime DEFAULT NULL COMMENT '退货日期',
  `division_code` varchar(50) DEFAULT NULL COMMENT '事业部',
  `deal_flag` tinyint(4) DEFAULT '1' COMMENT '处理结果 1-待处理 2-处理失败 3-处理成功',
  `deal_msg` varchar(500) DEFAULT NULL COMMENT '处理消息',
  `create_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='PMS退货申请单';

/*Data for the table `tb_pms_return` */

/*Table structure for table `tb_pms_return_dtl` */

DROP TABLE IF EXISTS `tb_pms_return_dtl`;

CREATE TABLE `tb_pms_return_dtl` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `pms_return_id` bigint(20) NOT NULL COMMENT 'PMS退货申请单id',
  `sku` varchar(50) NOT NULL COMMENT '商品sku',
  `refund_quantity` decimal(20,8) DEFAULT '0.00000000' COMMENT '退货申请数量',
  `create_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='PMS退货申请明细表';

/*Data for the table `tb_pms_return_dtl` */

/*Table structure for table `tb_pms_series` */

DROP TABLE IF EXISTS `tb_pms_series`;

CREATE TABLE `tb_pms_series` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `type` tinyint(4) DEFAULT NULL COMMENT '接口类型 1-pms入库单接口 2-pms出库单接口 3-pms请款单接口',
  `invoke_time` datetime NOT NULL COMMENT '调用时间',
  `series_no` varchar(64) DEFAULT NULL COMMENT '流水号',
  `message` varchar(200) DEFAULT NULL COMMENT '描述',
  `create_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='PMS铺货业务流水表';

/*Data for the table `tb_pms_series` */

/*Table structure for table `tb_pms_store_in` */

DROP TABLE IF EXISTS `tb_pms_store_in`;

CREATE TABLE `tb_pms_store_in` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `pms_series_id` bigint(20) NOT NULL COMMENT '流水表id',
  `po_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '采购ID',
  `purchase_sn` varchar(50) NOT NULL COMMENT '采购单号',
  `provider_sn` varchar(50) NOT NULL COMMENT '供应商编号',
  `account_sn` varchar(50) NOT NULL DEFAULT '' COMMENT 'pms结算对象编码',
  `currency_type` varchar(10) NOT NULL COMMENT '币种',
  `sku` varchar(50) NOT NULL COMMENT '商品sku',
  `stockin_num` decimal(20,8) DEFAULT '0.00000000' COMMENT '入库数量',
  `purchase_price` decimal(20,8) DEFAULT '0.00000000' COMMENT '入库价格',
  `stockin_time` datetime DEFAULT NULL COMMENT '入库日期',
  `purchase_delivery_sn` varchar(100) DEFAULT NULL COMMENT '送货单号',
  `flag` tinyint(4) DEFAULT '0' COMMENT '返回值 0-接收成功 1-接收失败',
  `msg` varchar(500) DEFAULT NULL COMMENT '返回消息',
  `deal_flag` tinyint(4) DEFAULT '1' COMMENT '处理结果 1-待处理 2-处理失败 3-处理成功',
  `deal_msg` varchar(500) DEFAULT NULL COMMENT '处理消息',
  `create_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='PMS同步入库单明细';

/*Data for the table `tb_pms_store_in` */

/*Table structure for table `tb_pms_store_out` */

DROP TABLE IF EXISTS `tb_pms_store_out`;

CREATE TABLE `tb_pms_store_out` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `pms_series_id` bigint(20) NOT NULL COMMENT '流水表id',
  `purchase_sn` varchar(50) NOT NULL COMMENT '采购单号',
  `provider_sn` varchar(50) NOT NULL COMMENT '供应商编号',
  `account_sn` varchar(50) NOT NULL DEFAULT '' COMMENT 'pms结算对象编码',
  `currency_type` varchar(10) NOT NULL COMMENT '币种',
  `sku` varchar(50) NOT NULL COMMENT '商品sku',
  `wms_out_stockin` decimal(20,8) DEFAULT '0.00000000' COMMENT '销售数量',
  `purchase_price` decimal(20,8) DEFAULT '0.00000000' COMMENT '销售价格',
  `sales_date` datetime DEFAULT NULL COMMENT '销售日期',
  `sku_id` bigint(11) NOT NULL COMMENT '销售id',
  `flag` tinyint(4) DEFAULT '0' COMMENT '返回值 0-接收成功 1-接收失败',
  `msg` varchar(500) DEFAULT NULL COMMENT '返回消息',
  `deal_flag` tinyint(4) DEFAULT '1' COMMENT '处理结果 1-待处理 2-处理失败 3-处理成功',
  `deal_msg` varchar(500) DEFAULT NULL COMMENT '处理消息',
  `create_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='PMS同步出库单明细';

/*Data for the table `tb_pms_store_out` */

/*Table structure for table `tb_pms_supplier_bind` */

DROP TABLE IF EXISTS `tb_pms_supplier_bind`;

CREATE TABLE `tb_pms_supplier_bind` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `supplier_id` bigint(20) DEFAULT NULL COMMENT '供应商ID',
  `supplier_no` varchar(20) DEFAULT NULL COMMENT '供应商编号',
  `pms_supplier_no` varchar(20) DEFAULT NULL COMMENT 'PMS供应商编号',
  `STATUS` tinyint(4) DEFAULT NULL COMMENT '状态 0-待提交 1-已提交未绑定 2-已提交已绑定',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleter_id` bigint(20) DEFAULT NULL COMMENT '作废人ID',
  `deleter` varchar(50) DEFAULT NULL COMMENT '作废人',
  `is_delete` tinyint(4) DEFAULT '0' COMMENT '作废标记 0:有效 1:删除',
  `delete_at` datetime DEFAULT NULL COMMENT '作废时间',
  `project_id` bigint(20) DEFAULT NULL COMMENT '项目ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `tb_pms_supplier_bind` */

/*Table structure for table `tb_profit_report` */

DROP TABLE IF EXISTS `tb_profit_report`;

CREATE TABLE `tb_profit_report` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `report_record_id` bigint(20) DEFAULT NULL COMMENT '报表记录表ID',
  `issue` varchar(20) DEFAULT NULL COMMENT '期号',
  `bill_id` bigint(20) DEFAULT NULL COMMENT '关联单据ID',
  `project_id` bigint(20) DEFAULT NULL COMMENT '项目ID',
  `project_name` varchar(200) DEFAULT NULL COMMENT '项目名称',
  `department_id` bigint(20) DEFAULT NULL COMMENT '部门ID',
  `department_name` varchar(200) DEFAULT NULL COMMENT '部门名称',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '客户ID',
  `customer_name` varchar(200) DEFAULT NULL COMMENT '客户名称',
  `biz_manager_id` bigint(20) DEFAULT NULL COMMENT '业务员ID',
  `biz_manager_name` varchar(200) DEFAULT NULL COMMENT '业务员名称',
  `warehouse_id` bigint(20) DEFAULT NULL COMMENT '仓库ID',
  `warehouse_name` varchar(200) DEFAULT NULL COMMENT '仓库名称',
  `business_unit_id` bigint(20) DEFAULT NULL COMMENT '经营单位ID',
  `business_unit_name` varchar(200) DEFAULT NULL COMMENT '经营单位名称',
  `biz_type` tinyint(4) DEFAULT NULL COMMENT '业务类型 1-服务 2-贸易',
  `biz_type_name` varchar(20) DEFAULT NULL COMMENT '业务类型名称',
  `bill_type` tinyint(4) DEFAULT NULL COMMENT '单据类型 1-应收费用 2-应付费用 3-应收应付费用 4-销售单 5-资金成本',
  `bill_type_name` varchar(20) DEFAULT NULL COMMENT '单据类型名称',
  `order_no` varchar(20) DEFAULT NULL COMMENT '订单编号',
  `bill_no` varchar(20) DEFAULT NULL COMMENT '单据编号',
  `product_no` varchar(200) DEFAULT NULL COMMENT '产品编号',
  `num` decimal(20,8) DEFAULT '0.00000000' COMMENT '数量',
  `place_date` date DEFAULT NULL COMMENT '下单日期',
  `statistics_date` date DEFAULT NULL COMMENT '统计日期(实际业务日期)',
  `sale_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '销售总价',
  `service_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '服务收入',
  `composite_tax_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '综合税金',
  `purchase_cost` decimal(20,8) DEFAULT '0.00000000' COMMENT '采购成本',
  `fund_cost` decimal(20,8) DEFAULT '0.00000000' COMMENT '资金成本',
  `warehouse_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '仓储物流费',
  `market_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '市场费用',
  `finance_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '财务费用',
  `manage_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '管理费用',
  `manual_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '人工费用',
  `profit_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '利润',
  `profit_rate` decimal(20,8) DEFAULT '0.00000000' COMMENT '利润率',
  `tax_rate` decimal(20,8) DEFAULT '0.00000000' COMMENT '税率',
  `currency_type` tinyint(4) DEFAULT NULL COMMENT '币种 1.人民币 2.美元 3.港币 4.欧元',
  `currency_name` varchar(20) DEFAULT NULL COMMENT '币种名称',
  `exchange_rate` decimal(20,8) DEFAULT '0.00000000' COMMENT '汇率',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `creator` varchar(100) DEFAULT NULL COMMENT '创建人',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='利润报表';

/*Data for the table `tb_profit_report` */

/*Table structure for table `tb_profit_report_mounth` */

DROP TABLE IF EXISTS `tb_profit_report_mounth`;

CREATE TABLE `tb_profit_report_mounth` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键，id',
  `issue` varchar(20) DEFAULT NULL COMMENT '期号',
  `project_id` bigint(20) DEFAULT NULL COMMENT '项目ID',
  `biz_type` tinyint(4) DEFAULT NULL COMMENT '业务类型 1-服务 2-贸易',
  `sale_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '销售总价',
  `sale_blance_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '销售毛利润',
  `purchase_cost` decimal(20,8) DEFAULT '0.00000000' COMMENT '采购成本',
  `fund_cost` decimal(20,8) DEFAULT '0.00000000' COMMENT '资金成本',
  `warehouse_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '仓储物流费',
  `manage_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '管理费用',
  `profit_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '利润',
  `biz_manager_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '业务利润',
  `profit_rate` decimal(20,8) DEFAULT '0.00000000' COMMENT '利润率',
  `sale_blance_rate` decimal(20,8) DEFAULT '0.00000000' COMMENT '销售毛利润率',
  `biz_manager_rate` decimal(20,8) DEFAULT '0.00000000' COMMENT '业务利润率',
  `tax_rate` decimal(20,8) DEFAULT '0.00000000' COMMENT '税率',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `creator` varchar(100) DEFAULT NULL COMMENT '创建人',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='利润月报表';

/*Data for the table `tb_profit_report_mounth` */

/*Table structure for table `tb_profit_target` */

DROP TABLE IF EXISTS `tb_profit_target`;

CREATE TABLE `tb_profit_target` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键:id',
  `issue` varchar(20) DEFAULT NULL COMMENT '期号',
  `user_id` bigint(20) DEFAULT NULL COMMENT '业务员',
  `target_profit_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '利润目标值',
  `target_biz_manager` decimal(20,8) DEFAULT '0.00000000' COMMENT '业务利润目标值',
  `target_sale_blance` decimal(20,8) DEFAULT '0.00000000' COMMENT '销售毛利润目标值',
  `target_sale_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '经营收入目标值',
  `target_manage_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '管理费用目标值',
  `target_warehouse_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '经营费用目标值',
  `target_fund_cost` decimal(20,8) DEFAULT '0.00000000' COMMENT '资金成本目标值',
  `state` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0 待提交 1 待业务主管审核  2 待部门主管审核 3 已完成',
  `busi_id` bigint(20) DEFAULT NULL COMMENT '业务审核员',
  `dept_manage_id` bigint(20) DEFAULT NULL COMMENT '部门主管审核人',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `creator` varchar(100) DEFAULT NULL COMMENT '创建人',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='业务指标目标值信息';

/*Data for the table `tb_profit_target` */

/*Table structure for table `tb_project_goods` */

DROP TABLE IF EXISTS `tb_project_goods`;

CREATE TABLE `tb_project_goods` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `project_id` bigint(20) DEFAULT NULL COMMENT '项目ID,关联tb_base_project[id]',
  `goods_id` bigint(20) DEFAULT NULL COMMENT '商品ID',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `deleter` varchar(50) DEFAULT NULL COMMENT '作废人',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `delete_at` datetime DEFAULT NULL COMMENT '作废时间',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态',
  `is_delete` tinyint(4) DEFAULT NULL COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='项目商品关联表';

/*Data for the table `tb_project_goods` */

/*Table structure for table `tb_project_item` */

DROP TABLE IF EXISTS `tb_project_item`;

CREATE TABLE `tb_project_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `item_no` varchar(50) DEFAULT NULL COMMENT '编号',
  `project_id` bigint(20) DEFAULT NULL COMMENT '项目ID',
  `business_unit_id` bigint(20) DEFAULT NULL COMMENT '经营单位',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '主客户ID',
  `supplier_id` bigint(20) DEFAULT NULL COMMENT '供应商ID',
  `start_date` date DEFAULT NULL COMMENT '开始日期',
  `end_date` date DEFAULT NULL COMMENT '结束日期',
  `total_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '额度总额',
  `amount_currency` tinyint(4) DEFAULT NULL COMMENT '额度总额单位',
  `is_fund_account` tinyint(4) DEFAULT NULL COMMENT '是否资金占用结算',
  `fund_account_period` int(11) DEFAULT NULL COMMENT '资金使用帐期',
  `fund_month_rate` decimal(20,8) DEFAULT NULL COMMENT '资金月服务费率',
  `day_penal_rate` decimal(20,8) DEFAULT NULL COMMENT '日违约金费率',
  `penal_grace_period` int(11) DEFAULT NULL COMMENT '违约宽限期',
  `is_operate_account` tinyint(4) DEFAULT NULL COMMENT '是否操作费结算',
  `operate_fee_type` tinyint(20) DEFAULT NULL COMMENT '操作费分类',
  `operate_fee_rate` decimal(20,8) DEFAULT NULL COMMENT '操作服务费率',
  `client_check_week` tinyint(4) DEFAULT NULL COMMENT '客户对帐周期',
  `client_check_day` tinyint(4) DEFAULT NULL COMMENT '客户对帐日期',
  `supplier_check_week` tinyint(4) DEFAULT NULL COMMENT '供应商对帐周期',
  `supplier_check_day` tinyint(4) DEFAULT NULL COMMENT '供应商对帐日期',
  `pay_currency` tinyint(20) DEFAULT NULL COMMENT '付款币种',
  `receive_currency` tinyint(20) DEFAULT NULL COMMENT '收款币种',
  `pay_rate` decimal(20,8) DEFAULT NULL COMMENT '付款比例',
  `is_agency_export` tinyint(4) DEFAULT NULL COMMENT '是否代理出口',
  `agency_export_rate` decimal(20,8) DEFAULT NULL COMMENT '代理出口费率',
  `account_rate_type` tinyint(20) DEFAULT NULL COMMENT '结算汇率类型',
  `account_rate` decimal(20,4) DEFAULT '0.0000' COMMENT '结算汇率',
  `pay_cycle` tinyint(20) NOT NULL DEFAULT '0' COMMENT '付款周期',
  `paypal_account` varchar(200) DEFAULT NULL COMMENT 'PAYPAL帐户',
  `paypal_account_type` tinyint(20) DEFAULT NULL COMMENT 'PAYPAL帐户结算方式',
  `paypal_days` int(11) DEFAULT NULL COMMENT 'PAYPAL帐户结算方式：天数',
  `paypal_fixday_type` tinyint(20) DEFAULT NULL COMMENT 'PAYPAL帐户结算方式：固定日类型',
  `paypal_fixday` int(11) DEFAULT NULL COMMENT 'PAYPAL帐户结算方式：固定日',
  `paypal_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT 'PAYPAL帐户结算方式：金额',
  `paypal_amount_currency` tinyint(20) DEFAULT NULL COMMENT 'PAYPAL帐户结算方式：金额单位',
  `address` varchar(500) DEFAULT NULL COMMENT '我司联系地址',
  `contacts` varchar(50) DEFAULT NULL COMMENT '我司联系人',
  `phone` varchar(20) DEFAULT NULL COMMENT '我司联系电话',
  `fax` varchar(20) DEFAULT NULL COMMENT '我司联系传真',
  `email` varchar(50) DEFAULT NULL COMMENT '我司联系邮箱',
  `client_address` varchar(500) DEFAULT NULL COMMENT '客户联系地址',
  `client_contacts` varchar(50) DEFAULT NULL COMMENT '客户联系人',
  `client_phone` varchar(20) DEFAULT NULL COMMENT '客户联系电话',
  `client_fax` varchar(20) DEFAULT NULL COMMENT '客户联系传真',
  `client_email` varchar(50) DEFAULT NULL COMMENT '客户联系邮箱',
  `supplier_address` varchar(500) DEFAULT NULL COMMENT '供应商联系地址',
  `supplier_contacts` varchar(50) DEFAULT NULL COMMENT '供应商联系人',
  `supplier_phone` varchar(20) DEFAULT NULL COMMENT '供应商联系电话',
  `supplier_fax` varchar(20) DEFAULT NULL COMMENT '供应商联系传真',
  `supplier_email` varchar(50) DEFAULT NULL COMMENT '供应商联系邮箱',
  `remark` varchar(1000) DEFAULT NULL COMMENT '备注',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `deleter` varchar(50) DEFAULT NULL COMMENT '作废人',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `delete_at` datetime DEFAULT NULL COMMENT '作废时间',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态 1-待提交 20-待业务审核 30-待财务主管审核 40-待风控审核 80-待部门主管审核 5-待完成 6-已完成 7-已锁定',
  `is_delete` tinyint(4) DEFAULT '0' COMMENT '删除标记',
  `operate_rate` decimal(20,8) DEFAULT '0.00000000' COMMENT '操作服务费率',
  `project_check_week` tinyint(4) DEFAULT NULL COMMENT '项目对账周期',
  `project_check_date` tinyint(4) DEFAULT NULL COMMENT '项目对账日期',
  `agency_import_rate` decimal(20,8) DEFAULT '0.00000000' COMMENT '代理进口费率',
  `is_agency_import` tinyint(4) DEFAULT NULL COMMENT '是否代理进口',
  `file` tinyint(4) DEFAULT NULL COMMENT '文件上传标志',
  `fixed_points` tinyint(4) DEFAULT NULL COMMENT '是否固定点数',
  `spread_fixed_points` decimal(20,8) DEFAULT '0.00000000' COMMENT '价差固定点数',
  `business_type` varchar(20) DEFAULT NULL COMMENT '业务类型',
  `project_check_type` tinyint(4) DEFAULT NULL COMMENT '项目对账方式',
  `client_check_type` tinyint(4) DEFAULT NULL COMMENT '客户对账方式',
  `supplier_check_type` tinyint(4) DEFAULT NULL COMMENT '供应商对账方式',
  `bank_id` tinyint(4) DEFAULT NULL COMMENT '银行名称',
  `account_method` tinyint(4) DEFAULT NULL COMMENT '结算方式',
  `min_sale_day` int(11) NOT NULL DEFAULT '0' COMMENT '最低消费天数',
  `sign_standard` tinyint(4) DEFAULT NULL COMMENT '签收标准 0-身份证 1-公章 2-身份证+公章',
  `certificate_id` varchar(50) DEFAULT NULL COMMENT '身份证号码',
  `certificate_name` varchar(50) DEFAULT NULL COMMENT '姓名',
  `official_seal` varchar(100) DEFAULT NULL COMMENT '公章名',
  `single_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '单笔额度',
  `library_age` int(20) DEFAULT NULL COMMENT '库龄时间',
  `day_rules` tinyint(4) DEFAULT NULL COMMENT '天数规则 ',
  `settle_type` tinyint(4) DEFAULT NULL COMMENT '项目客户结算方式 1 赊销 2 款到放货',
  `paypal_calc_type` tinyint(4) DEFAULT NULL COMMENT '结算计费方式 1-日 2-分段',
  `pay_audit_type` varchar(50) NOT NULL DEFAULT '' COMMENT '付款审核方式',
  `first_pay_rate` decimal(20,8) DEFAULT '1.00000000' COMMENT '首付款比例',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='结算条款';

/*Data for the table `tb_project_item` */

/*Table structure for table `tb_project_item_segment` */

DROP TABLE IF EXISTS `tb_project_item_segment`;

CREATE TABLE `tb_project_item_segment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `project_item_id` bigint(20) DEFAULT NULL COMMENT '项目条款ID',
  `segment_day` int(11) DEFAULT NULL COMMENT '分段天数',
  `segment_fund_month_rate` decimal(20,8) DEFAULT NULL COMMENT '服务费率',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleter` varchar(50) DEFAULT NULL COMMENT '作废人',
  `delete_at` datetime DEFAULT NULL COMMENT '作废时间',
  `is_delete` tinyint(4) DEFAULT '0' COMMENT '删除标记 0: 有效 1: 删除',
  PRIMARY KEY (`id`),
  KEY `idx_tb_project_item_segment_project_item_id` (`project_item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='项目条款结算计费方式表';

/*Data for the table `tb_project_item_segment` */

/*Table structure for table `tb_project_no_seq` */

DROP TABLE IF EXISTS `tb_project_no_seq`;

CREATE TABLE `tb_project_no_seq` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `project_no_type` char(10) NOT NULL DEFAULT '' COMMENT '项目编号类型',
  `seq_val` bigint(20) NOT NULL DEFAULT '1' COMMENT '序列值',
  `create_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='项目编号规则表';

/*Data for the table `tb_project_no_seq` */

/*Table structure for table `tb_project_pool` */

DROP TABLE IF EXISTS `tb_project_pool`;

CREATE TABLE `tb_project_pool` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `project_id` bigint(20) DEFAULT NULL COMMENT '项目ID',
  `project_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '项目额度',
  `project_amount_cny` decimal(20,8) DEFAULT '0.00000000' COMMENT '项目额度(CNY)',
  `currency_type` tinyint(4) DEFAULT NULL COMMENT '币种',
  `used_fund_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '已使用资金额度',
  `used_fund_amount_cny` decimal(20,8) DEFAULT '0.00000000' COMMENT '已使用资金额度(CNY',
  `remain_fund_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '资金余额',
  `remain_fund_amount_cny` decimal(20,8) DEFAULT '0.00000000' COMMENT '资金余额(CNY)',
  `used_asset_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '已使用资产额度',
  `used_asset_amount_cny` decimal(20,8) DEFAULT '0.00000000' COMMENT '已使用资产额度(CNY)',
  `remain_asset_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '资产余额',
  `remain_asset_amount_cny` decimal(20,8) DEFAULT '0.00000000' COMMENT '资产余额(CNY)',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='融资池';

/*Data for the table `tb_project_pool` */

/*Table structure for table `tb_project_pool_adjust` */

DROP TABLE IF EXISTS `tb_project_pool_adjust`;

CREATE TABLE `tb_project_pool_adjust` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `adjust_no` varchar(20) DEFAULT NULL COMMENT '调整编码',
  `project_id` bigint(11) DEFAULT NULL COMMENT '项目id',
  `currency_type` tinyint(4) DEFAULT NULL COMMENT '币种',
  `adjust_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '临时调整额度',
  `create_id` bigint(11) DEFAULT NULL COMMENT '创建人id',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_at` datetime DEFAULT NULL COMMENT '创建时间',
  `update_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `start_valid_date` date NOT NULL COMMENT '有效期开始时间',
  `end_valid_date` date DEFAULT NULL COMMENT '有效期结束时间',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `state` tinyint(4) DEFAULT NULL COMMENT '状态 1待提交 20待业务审核 30待财务审核 40待风控审核 80待部门主管审核 2已锁定 3已完成',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `tb_project_pool_adjust` */

/*Table structure for table `tb_project_pool_asset_dtl` */

DROP TABLE IF EXISTS `tb_project_pool_asset_dtl`;

CREATE TABLE `tb_project_pool_asset_dtl` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `type` tinyint(4) DEFAULT NULL COMMENT '类型：入/出',
  `bill_no` varchar(20) DEFAULT NULL COMMENT '单据编号',
  `bill_source` tinyint(4) DEFAULT NULL COMMENT '单据来源 类型为资金:1-收款 2-付款    类型为资产:1-收货 2-发货',
  `project_id` bigint(20) DEFAULT NULL COMMENT '项目ID',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '客户ID',
  `supplier_id` bigint(20) DEFAULT NULL COMMENT '供应商ID',
  `business_date` date DEFAULT NULL COMMENT '记账日期',
  `bill_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '单据占用金额',
  `bill_currency_type` tinyint(4) DEFAULT NULL COMMENT '单据币种',
  `bill_project_exchange_rate` decimal(20,8) DEFAULT '0.00000000' COMMENT '单据币种对项目币种汇率',
  `bill_cny_exchange_rate` decimal(20,8) DEFAULT NULL COMMENT '单据币种对人民币的汇率',
  `project_amount` decimal(40,8) DEFAULT '0.00000000' COMMENT '项目占用金额',
  `project_currency_type` tinyint(4) DEFAULT NULL COMMENT '项目币种',
  `cny_amount` decimal(40,8) DEFAULT '0.00000000' COMMENT '人民币占用金额',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `creator` varchar(255) DEFAULT NULL COMMENT '创建人',
  `create_at` datetime DEFAULT NULL COMMENT '创建时间',
  `update_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `fund_redant` decimal(20,8) DEFAULT '0.00000000' COMMENT '冗余金额 如果类型为入，则为入冗余金额，如果为出，则为出冗余金额',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='资产池明细';

/*Data for the table `tb_project_pool_asset_dtl` */

/*Table structure for table `tb_project_pool_asset_inout` */

DROP TABLE IF EXISTS `tb_project_pool_asset_inout`;

CREATE TABLE `tb_project_pool_asset_inout` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `asset_in_id` bigint(20) DEFAULT NULL COMMENT '资产-入ID',
  `asset_out_id` bigint(20) DEFAULT NULL COMMENT '资产-出ID',
  `asset_redant` decimal(20,8) DEFAULT '0.00000000' COMMENT '冗余金额',
  `business_date` date DEFAULT NULL COMMENT '记账日期',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `tb_project_pool_asset_inout` */

/*Table structure for table `tb_project_pool_fund_dtl` */

DROP TABLE IF EXISTS `tb_project_pool_fund_dtl`;

CREATE TABLE `tb_project_pool_fund_dtl` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `type` tinyint(4) DEFAULT NULL COMMENT '类型：入/出',
  `bill_no` varchar(20) DEFAULT NULL COMMENT '单据编号',
  `bill_source` tinyint(4) DEFAULT NULL COMMENT '单据来源 类型为资金:1-收款 2-付款    类型为资产:1-收货 2-发货',
  `project_id` bigint(20) DEFAULT NULL COMMENT '项目ID',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '客户ID',
  `supplier_id` bigint(20) DEFAULT NULL COMMENT '供应商ID',
  `business_date` date DEFAULT NULL COMMENT '记账日期',
  `bill_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '单据占用金额',
  `bill_currency_type` tinyint(4) DEFAULT NULL COMMENT '单据币种',
  `bill_project_exchange_rate` decimal(20,8) DEFAULT '0.00000000' COMMENT '单据币种对项目币种汇率',
  `bill_cny_exchange_rate` decimal(20,8) DEFAULT NULL COMMENT '单据币种对人民币的汇率',
  `project_amount` decimal(40,8) DEFAULT '0.00000000' COMMENT '项目占用金额',
  `project_currency_type` tinyint(4) DEFAULT NULL COMMENT '项目币种',
  `cny_amount` decimal(40,8) DEFAULT '0.00000000' COMMENT '人民币占用金额',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `creator` varchar(255) DEFAULT NULL COMMENT '创建人',
  `create_at` datetime DEFAULT NULL COMMENT '创建时间',
  `update_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `fund_redant` decimal(20,8) DEFAULT '0.00000000' COMMENT '冗余金额 如果类型为入，则为入冗余金额，如果为出，则为出冗余金额',
  `fund_class` tinyint(4) DEFAULT NULL COMMENT '资产类别 1应收核销 2预收定金 3预收货款 ',
  `fee_id` int(11) DEFAULT NULL COMMENT '费用id',
  `out_store_id` int(11) DEFAULT NULL COMMENT '出库单id',
  `assist_bill_no` varchar(20) DEFAULT NULL COMMENT '辅助单据编号',
  `bill_type` tinyint(4) DEFAULT NULL COMMENT '单据类型 定义同 tb_fi_voucher_line[bill_type]',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='资金池明细';

/*Data for the table `tb_project_pool_fund_dtl` */

/*Table structure for table `tb_project_pool_fund_inout` */

DROP TABLE IF EXISTS `tb_project_pool_fund_inout`;

CREATE TABLE `tb_project_pool_fund_inout` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `fund_in_id` bigint(20) DEFAULT NULL COMMENT '资金-入ID',
  `fund_out_id` bigint(20) DEFAULT NULL COMMENT '资金-出ID',
  `fund_redant` decimal(20,8) DEFAULT '0.00000000' COMMENT '冗余金额',
  `business_date` date DEFAULT NULL COMMENT '记账日期',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='资金池明细关系';

/*Data for the table `tb_project_pool_fund_inout` */

/*Table structure for table `tb_project_risk` */

DROP TABLE IF EXISTS `tb_project_risk`;

CREATE TABLE `tb_project_risk` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `project_id` bigint(20) DEFAULT NULL,
  `risktype` varchar(3) DEFAULT NULL,
  `title` varchar(200) DEFAULT NULL,
  `remarks` varchar(3000) DEFAULT NULL,
  `status` tinyint(11) DEFAULT NULL,
  `creator` varchar(50) DEFAULT NULL,
  `deleter` varchar(50) DEFAULT NULL,
  `create_at` datetime DEFAULT NULL,
  `update_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `detele_at` datetime DEFAULT NULL,
  `is_delete` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='项目预警';

/*Data for the table `tb_project_risk` */

/*Table structure for table `tb_project_subject` */

DROP TABLE IF EXISTS `tb_project_subject`;

CREATE TABLE `tb_project_subject` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `project_id` bigint(20) DEFAULT NULL COMMENT '项目ID',
  `subject_id` bigint(20) DEFAULT NULL COMMENT '主体ID',
  `subject_type` tinyint(4) DEFAULT NULL COMMENT '主体类型',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `deleter` varchar(50) DEFAULT NULL COMMENT '作废人',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `delete_at` datetime DEFAULT NULL COMMENT '作废时间',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态',
  `is_delete` tinyint(4) DEFAULT NULL COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='项目与主体绑定表';

/*Data for the table `tb_project_subject` */

/*Table structure for table `tb_purchase_order_line` */

DROP TABLE IF EXISTS `tb_purchase_order_line`;

CREATE TABLE `tb_purchase_order_line` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `po_id` int(11) DEFAULT NULL COMMENT '采购单ID',
  `goods_id` int(11) DEFAULT NULL COMMENT '商品ID',
  `goods_num` decimal(20,8) DEFAULT '0.00000000' COMMENT '采购单-采购数量，采购退货单-退货数量，铺货单-收货数量',
  `amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '金额(数量*单价）',
  `discount_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '折扣金额',
  `goods_price` decimal(20,8) DEFAULT '0.00000000' COMMENT '商品单价',
  `discount_price` decimal(20,8) DEFAULT '0.00000000' COMMENT '商品折扣单价',
  `storage_num` decimal(20,8) DEFAULT '0.00000000' COMMENT '商品入库数量',
  `cost_price` decimal(20,8) DEFAULT '0.00000000' COMMENT '成本单价',
  `batch_num` varchar(50) DEFAULT NULL COMMENT '批次',
  `invoice_num` decimal(20,8) DEFAULT '0.00000000' COMMENT '收票数量',
  `invoice_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '收票金额',
  `create_at` datetime DEFAULT NULL COMMENT '创建时间',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `creator_id` int(11) DEFAULT NULL COMMENT '创建人ID',
  `is_delete` tinyint(4) DEFAULT '0' COMMENT '是否删除',
  `paid_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '已付款金额',
  `pay_time` date DEFAULT NULL COMMENT '付款确认时间',
  `pay_price` decimal(20,8) DEFAULT '0.00000000' COMMENT '付款单价',
  `pay_rate` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '付款汇率',
  `pay_real_currency` tinyint(4) NOT NULL DEFAULT '1' COMMENT '付款实际支付币种',
  `required_send_price` decimal(20,8) DEFAULT '0.00000000' COMMENT '应发货单价，保存pms请款单传过来的inPrice',
  `bill_in_store_id` bigint(20) DEFAULT NULL COMMENT '关联入库单ID',
  `bill_in_store_dtl_id` bigint(20) DEFAULT NULL COMMENT '关联入库单明细ID',
  `bill_in_store_tally_dtl_id` bigint(20) DEFAULT NULL COMMENT '关联入库单理货明细ID',
  `stl_id` bigint(20) DEFAULT NULL COMMENT '库存ID',
  `goods_status` tinyint(4) DEFAULT '1' COMMENT '商品状态 1-常规 2-残次品',
  `po_price` decimal(20,8) DEFAULT '0.00000000' COMMENT '采购单价',
  `origin_goods_price` decimal(20,8) DEFAULT '0.00000000' COMMENT '原采购单价(退货)',
  `send_num` decimal(20,8) DEFAULT '0.00000000' COMMENT '铺货发货数量',
  `send_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '铺货发货金额',
  `return_num` decimal(20,8) DEFAULT '0.00000000' COMMENT '铺货退货数量',
  `return_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '铺货退货金额',
  `pledge_proportion` decimal(20,8) DEFAULT '0.00000000' COMMENT '质押比例',
  `purchase_delivery_sn` varchar(100) DEFAULT NULL COMMENT '送货单号',
  `remain_send_num` decimal(20,8) DEFAULT '0.00000000' COMMENT '铺货可发货数量',
  `stockin_time` datetime DEFAULT NULL COMMENT '铺货入库时间',
  `distribute_id` int(11) DEFAULT NULL COMMENT '铺货id',
  `distribute_num` decimal(20,8) DEFAULT '0.00000000' COMMENT '铺货已请款数量',
  `wait_distribute_num` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '铺货待请款数量',
  `deduction_money` decimal(20,8) DEFAULT '0.00000000' COMMENT '抵扣金额',
  `distribute_line_id` int(11) DEFAULT NULL COMMENT '铺货明细id',
  `occupy_day` int(11) DEFAULT '0' COMMENT '资金占用天数',
  `occupy_service_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '资金占用服务费',
  `fund_month_rate` decimal(20,8) DEFAULT '0.00000000' COMMENT '日服务费率',
  `refund_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '退款金额',
  PRIMARY KEY (`id`),
  KEY `idx_tb_purchase_order_line_po_id` (`po_id`,`goods_id`,`stl_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='采购单行(明细)';

/*Data for the table `tb_purchase_order_line` */

/*Table structure for table `tb_purchase_order_title` */

DROP TABLE IF EXISTS `tb_purchase_order_title`;

CREATE TABLE `tb_purchase_order_title` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '采购单头ID',
  `sys_no` varchar(50) DEFAULT NULL COMMENT '系统编号',
  `append_no` varchar(50) DEFAULT NULL COMMENT '附属编号',
  `order_no` varchar(50) DEFAULT NULL COMMENT '订单编号',
  `business_unit_id` int(11) DEFAULT NULL COMMENT '经营单位ID',
  `supplier_id` int(11) DEFAULT NULL COMMENT '供应商ID',
  `project_id` int(11) DEFAULT NULL COMMENT '项目ID',
  `warehouse_id` int(11) DEFAULT NULL COMMENT '仓库ID',
  `customer_id` int(11) DEFAULT NULL COMMENT '客户ID',
  `ware_addr_id` int(11) DEFAULT NULL,
  `print_num` int(10) DEFAULT '0' COMMENT '打印次数',
  `account_id` int(11) DEFAULT NULL COMMENT '付款账号ID',
  `pay_way` int(11) DEFAULT NULL COMMENT '付款方式',
  `order_type` tinyint(4) DEFAULT NULL COMMENT '订单类型 0-采购 1-退货 2-铺货 3-铺货退货 4-铺货结算',
  `order_time` datetime DEFAULT NULL COMMENT '订单日期',
  `is_request_pay` tinyint(4) DEFAULT NULL,
  `perdict_time` datetime DEFAULT NULL COMMENT '预计到货日期',
  `request_pay_time` datetime DEFAULT NULL COMMENT '要求付款时间',
  `open_type` int(11) DEFAULT NULL COMMENT '开立类型',
  `c_bank_water` varchar(50) DEFAULT NULL COMMENT '客户银行流水',
  `arrival_num` decimal(20,8) DEFAULT '0.00000000' COMMENT '到货数量',
  `arrival_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '到货金额',
  `pay_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '付款金额',
  `invoice_total_num` decimal(20,8) DEFAULT '0.00000000' COMMENT '收票总数量',
  `invoice_total_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '收票总金额',
  `per_rec_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '预付金',
  `currency_id` tinyint(4) DEFAULT NULL COMMENT '币种',
  `order_total_num` decimal(20,8) DEFAULT '0.00000000' COMMENT '收票总金额',
  `order_total_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '订单总金额',
  `total_discount_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '折扣总金额',
  `creator_id` int(11) DEFAULT NULL COMMENT '创建人ID',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_at` datetime DEFAULT NULL COMMENT '创建时间',
  `deleter` varchar(50) DEFAULT NULL COMMENT '作废人',
  `delete_at` datetime DEFAULT NULL COMMENT '作废时间',
  `is_delete` tinyint(4) DEFAULT '0' COMMENT '是否删除',
  `state` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态 1-待提交 10-待商务审核 20-待业务审核 25-待财务专员审核 30-待财务主管审核 35-待风控专员审核 5-已完成 6-已关闭 7-审核不通过',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `sign_standard` tinyint(4) DEFAULT NULL COMMENT '签收标准 0-身份证 1-公章 2-身份证+公章',
  `certificate_id` varchar(50) DEFAULT NULL COMMENT '身份证号码',
  `certificate_name` varchar(50) DEFAULT NULL COMMENT '姓名',
  `official_seal` varchar(100) DEFAULT NULL COMMENT '公章名',
  `supplier_address_id` bigint(20) DEFAULT NULL COMMENT '供应商地址',
  `transfer_mode` tinyint(4) DEFAULT NULL COMMENT '运输方式 1-自提 2- 市内运输 3-汽运 4-铁运 5-空运',
  `take_delivery_num` decimal(20,8) DEFAULT '0.00000000' COMMENT '收货数量',
  `duction_money` decimal(20,8) DEFAULT '0.00000000' COMMENT '抵扣金额',
  `total_refund_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '总退款金额',
  PRIMARY KEY (`id`),
  KEY `idx_tb_purchase_order_title_bill_no` (`append_no`,`order_no`,`order_type`),
  KEY `idx_tb_purchase_order_title_bill_id` (`business_unit_id`,`supplier_id`,`project_id`,`warehouse_id`,`customer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='采购PO单头信息';

/*Data for the table `tb_purchase_order_title` */

/*Table structure for table `tb_purchase_pack_print` */

DROP TABLE IF EXISTS `tb_purchase_pack_print`;

CREATE TABLE `tb_purchase_pack_print` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `po_line_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '采购单明细id',
  `packages` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '包装件数',
  `net_weight` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '净重',
  `gross_weight` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '毛重',
  `volume` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '体积',
  `create_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `creator` varchar(50) NOT NULL DEFAULT '' COMMENT '创建人',
  `creator_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建人ID',
  `is_delete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '已付款金额',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='采购单装箱打印信息';

/*Data for the table `tb_purchase_pack_print` */

/*Table structure for table `tb_receipt_pool` */

DROP TABLE IF EXISTS `tb_receipt_pool`;

CREATE TABLE `tb_receipt_pool` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '资金池的主键ID',
  `count_fund_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '资金额度',
  `business_unit_id` bigint(20) DEFAULT NULL COMMENT '经营单位ID',
  `currency_type` tinyint(4) DEFAULT NULL COMMENT '币种',
  `used_fund_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '已使用资金额度',
  `remain_fund_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '资金余额',
  `remain_asset_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '资产余额',
  `advance_pay_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '预付款金额',
  `payment_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '付货款金额',
  `stl_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '库存金额',
  `rec_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '应收金额',
  `count_fund_amount_cny` decimal(20,8) DEFAULT '0.00000000' COMMENT '资金额度（CNY）',
  `used_fund_amount_cny` decimal(20,8) DEFAULT '0.00000000' COMMENT '已使用资金额度（CNY）',
  `remain_fund_amount_cny` decimal(20,8) DEFAULT '0.00000000' COMMENT '资金余额（CNY）',
  `remain_asset_amount_cny` decimal(20,8) DEFAULT '0.00000000' COMMENT '资产余额（CNY）',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `update_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_at` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='资金池结构表';

/*Data for the table `tb_receipt_pool` */

/*Table structure for table `tb_receipt_pool_assest_dtl` */

DROP TABLE IF EXISTS `tb_receipt_pool_assest_dtl`;

CREATE TABLE `tb_receipt_pool_assest_dtl` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '资金池资产明细ID',
  `type` tinyint(4) DEFAULT NULL COMMENT '类型：入/出',
  `bill_no` varchar(20) DEFAULT NULL COMMENT '单据编号',
  `bill_source` tinyint(4) DEFAULT NULL COMMENT '单据来源 类型为资金:1-收款 2-付款 类型为资产:1-收货 2-发货',
  `project_id` bigint(20) DEFAULT NULL COMMENT '项目ID',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '客户ID',
  `business_unit_id` bigint(20) DEFAULT NULL COMMENT '经营单位ID',
  `supplier_id` bigint(20) DEFAULT NULL COMMENT '''供应商ID',
  `business_date` date DEFAULT NULL COMMENT '记账日期',
  `create_at` datetime DEFAULT NULL COMMENT '创建时间',
  `bill_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '单据占用金额',
  `bill_currency_type` tinyint(4) DEFAULT NULL COMMENT '单据币种',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `creator` varchar(255) DEFAULT NULL COMMENT '创建人',
  `update_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `bill_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '单据ID',
  `bill_type` tinyint(4) DEFAULT NULL COMMENT '单据类型',
  `exchange_rate` decimal(20,8) DEFAULT '0.00000000' COMMENT '币种汇率',
  `amount_cny` decimal(20,8) DEFAULT '0.00000000' COMMENT '转化为人民币后金额',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='资金池资产明细表';

/*Data for the table `tb_receipt_pool_assest_dtl` */

/*Table structure for table `tb_receipt_pool_dtl` */

DROP TABLE IF EXISTS `tb_receipt_pool_dtl`;

CREATE TABLE `tb_receipt_pool_dtl` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '资金池明细ID',
  `receipt_id` bigint(20) DEFAULT NULL COMMENT '水单ID',
  `currency_type` tinyint(4) DEFAULT NULL COMMENT '币种',
  `business_unit_id` bigint(20) DEFAULT NULL COMMENT '经营单位',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '客户ID',
  `supplier_id` bigint(20) DEFAULT NULL COMMENT '供应商ID',
  `bill_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '单据金额',
  `diff_amount` decimal(20,2) NOT NULL DEFAULT '0.00' COMMENT '尾差金额',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_at` datetime DEFAULT NULL COMMENT '创建时间',
  `update_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '跟新时间',
  `project_id` bigint(20) DEFAULT NULL COMMENT '项目id',
  `exchange_rate` decimal(20,8) DEFAULT '0.00000000' COMMENT '币种汇率',
  `bill_amount_cny` decimal(20,8) DEFAULT '0.00000000' COMMENT '人民币转化的金额',
  `pay_id` bigint(20) DEFAULT NULL COMMENT '付款单 付费用ID',
  `business_date` date DEFAULT NULL COMMENT '记账日期',
  `bill_type` tinyint(4) DEFAULT NULL COMMENT '单据类型',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `bill_no` varchar(20) DEFAULT NULL COMMENT '单据编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='资金池明细';

/*Data for the table `tb_receipt_pool_dtl` */

/*Table structure for table `tb_receipt_pool_fund_dtl` */

DROP TABLE IF EXISTS `tb_receipt_pool_fund_dtl`;

CREATE TABLE `tb_receipt_pool_fund_dtl` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '资金池资金明细ID',
  `type` tinyint(4) DEFAULT NULL COMMENT '类型：入/出',
  `bill_no` varchar(20) DEFAULT NULL COMMENT '单据编号',
  `bill_source` tinyint(4) DEFAULT NULL COMMENT '单据来源 类型为资金:1-收款 2-付款 类型为资产:1-收货 2-发货',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '客户ID',
  `business_unit_id` bigint(20) DEFAULT NULL COMMENT '经营单位ID',
  `supplier_id` bigint(20) DEFAULT NULL COMMENT '供应商ID',
  `project_id` bigint(20) DEFAULT NULL COMMENT '项目ID',
  `business_date` date DEFAULT NULL COMMENT '记账日期',
  `create_at` datetime DEFAULT NULL COMMENT '创建时间',
  `bill_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '单据占用金额',
  `bill_currency_type` tinyint(4) DEFAULT NULL COMMENT '单据币种',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `creator` varchar(255) DEFAULT NULL COMMENT '创建人',
  `update_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `bill_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '单据ID',
  `bill_type` tinyint(4) DEFAULT NULL COMMENT '单据类型',
  `exchange_rate` decimal(20,8) DEFAULT '0.00000000' COMMENT '币种汇率',
  `bill_amount_cny` decimal(20,8) DEFAULT '0.00000000' COMMENT '转化为人民币后金额',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='资金池资金明细';

/*Data for the table `tb_receipt_pool_fund_dtl` */

/*Table structure for table `tb_refund_apply` */

DROP TABLE IF EXISTS `tb_refund_apply`;

CREATE TABLE `tb_refund_apply` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '出口退税申请id',
  `refund_apply_no` varchar(50) DEFAULT NULL COMMENT '退税申请编号',
  `refund_attach_no` varchar(50) DEFAULT NULL COMMENT '退税附属编号',
  `project_id` bigint(20) DEFAULT NULL COMMENT '项目id',
  `cust_id` bigint(20) DEFAULT NULL COMMENT '客户id',
  `refund_apply_num` decimal(20,8) DEFAULT '0.00000000' COMMENT '退税数量',
  `refund_apply_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '退税金额',
  `refund_apply_tax` decimal(20,8) DEFAULT '0.00000000' COMMENT '可退税额',
  `refund_apply_date` datetime DEFAULT NULL COMMENT '退税申请日期',
  `verify_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '核销金额',
  `verify_date` datetime DEFAULT NULL COMMENT '核销日期',
  `verify` varchar(50) DEFAULT NULL COMMENT '核销',
  `print_num` int(10) DEFAULT '0' COMMENT '打印次数',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `creator_at` datetime DEFAULT NULL COMMENT '创建时间',
  `update_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `deleter` varchar(50) DEFAULT NULL COMMENT '删除人',
  `deleter_id` bigint(20) DEFAULT NULL COMMENT '删除人id',
  `delete_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '删除时间',
  `is_delete` tinyint(4) DEFAULT '0' COMMENT '删除状态',
  `state` tinyint(4) NOT NULL DEFAULT '1' COMMENT '1-待提交 25-待财务专员审核 3-已完成',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='出口退税申请表';

/*Data for the table `tb_refund_apply` */

/*Table structure for table `tb_refund_apply_line` */

DROP TABLE IF EXISTS `tb_refund_apply_line`;

CREATE TABLE `tb_refund_apply_line` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '退税明细id',
  `refund_apply_id` bigint(20) DEFAULT NULL COMMENT '退税申请id',
  `customs_apply_id` bigint(20) DEFAULT NULL COMMENT '报关申请id',
  `apply_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '金额',
  `apply_num` decimal(20,8) DEFAULT '0.00000000' COMMENT '退税数量',
  `apply_tax` decimal(20,8) DEFAULT '0.00000000' COMMENT '可退税额',
  `tax_rate` decimal(20,2) DEFAULT '0.00' COMMENT '税率',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `create_at` datetime DEFAULT NULL COMMENT '创建时间',
  `update_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_delete` tinyint(4) DEFAULT '0' COMMENT '删除状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='记录退税申请下的报关明细，一条报关明细只能被关联一次';

/*Data for the table `tb_refund_apply_line` */

/*Table structure for table `tb_refund_information` */

DROP TABLE IF EXISTS `tb_refund_information`;

CREATE TABLE `tb_refund_information` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '退款信息ID，自增长',
  `cust_id` bigint(20) NOT NULL COMMENT '客户',
  `busi_unit` bigint(20) NOT NULL COMMENT '经营单位',
  `project_id` bigint(20) NOT NULL COMMENT '项目',
  `currency_type` tinyint(4) NOT NULL COMMENT '币种',
  `receipt_date` datetime NOT NULL COMMENT '水单日期',
  `refund_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '退款金额',
  `is_delete` int(4) NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='退款信息表';

/*Data for the table `tb_refund_information` */

/*Table structure for table `tb_report_fund` */

DROP TABLE IF EXISTS `tb_report_fund`;

CREATE TABLE `tb_report_fund` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `report_record_id` bigint(20) DEFAULT NULL COMMENT '报表记录表ID',
  `department_id` bigint(20) DEFAULT NULL COMMENT '部门ID',
  `department_name` varchar(200) DEFAULT NULL COMMENT '部门名称',
  `project_id` bigint(20) DEFAULT NULL COMMENT '项目ID',
  `project_name` varchar(200) DEFAULT NULL COMMENT '项目名称',
  `busi_unit` bigint(20) NOT NULL DEFAULT '0' COMMENT '经营单位ID',
  `account_id` bigint(20) DEFAULT NULL COMMENT '账号ID',
  `account_no` varchar(100) DEFAULT NULL COMMENT '开户账号',
  `currency_type` tinyint(4) DEFAULT NULL COMMENT '币种 1.人民币 2.美元 3.港币 4.欧元',
  `currency_name` varchar(20) DEFAULT NULL COMMENT '币种名称',
  `begin_balance` decimal(20,8) DEFAULT '0.00000000' COMMENT '期初余额',
  `pay_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '本期付款',
  `receipt_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '本期收款',
  `balance` decimal(20,8) DEFAULT '0.00000000' COMMENT '余额',
  `fund_cost` decimal(20,8) DEFAULT '0.00000000' COMMENT '资金成本',
  `issue` varchar(20) DEFAULT NULL COMMENT '期号',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `creator` varchar(100) DEFAULT NULL COMMENT '创建人',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='资金统计报表';

/*Data for the table `tb_report_fund` */

/*Table structure for table `tb_report_project` */

DROP TABLE IF EXISTS `tb_report_project`;

CREATE TABLE `tb_report_project` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `project_id` tinyint(4) DEFAULT NULL COMMENT '需过滤项目ID',
  `report_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '过滤类型 1-销售报表 2-应收报表 3-进销存报表 4-库存报表 5-利润报表 6-资金统计报表 7-绩效报表 8-月结利润报表 9-资金周转率 10-待到货PO单 11-在仓库库存 12-平均库龄 13-超期库存 14-超期应收 15-动销滞销风险 16-审核时效',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_tb_report_project_report_type` (`report_type`,`project_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='报表查询项目权限表';

/*Data for the table `tb_report_project` */

/*Table structure for table `tb_report_record` */

DROP TABLE IF EXISTS `tb_report_record`;

CREATE TABLE `tb_report_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `report_type` tinyint(4) DEFAULT NULL COMMENT '报表类型 1-绩效报表 2-利润报表 3-资金成本报表',
  `issue` varchar(20) DEFAULT NULL COMMENT '期号',
  `is_success` tinyint(4) DEFAULT '0' COMMENT '是否存储成功 0-未成功 1-成功',
  `msg` varchar(500) DEFAULT NULL COMMENT '执行失败时原因',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `creator` varchar(100) DEFAULT NULL COMMENT '创建人',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `idx_report_type_issue` (`report_type`,`issue`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='报表记录表';

/*Data for the table `tb_report_record` */

/*Table structure for table `tb_sales_daily_wechar` */

DROP TABLE IF EXISTS `tb_sales_daily_wechar`;

CREATE TABLE `tb_sales_daily_wechar` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `department_user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '部门负责人用户id',
  `daily_date` varchar(50) NOT NULL DEFAULT '' COMMENT '昨日数据日期',
  `daily_sales` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '日销售额',
  `month_sales` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '月销售额',
  `daily_pay` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '日付款额',
  `month_pay` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '月付款额',
  `daily_payment` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '日回款额',
  `month_payment` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '月回款额',
  `stl_num` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '库存数量',
  `stl_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '库存金额',
  `currnecy_type` tinyint(4) NOT NULL DEFAULT '1' COMMENT '币种',
  `send_state` tinyint(4) NOT NULL DEFAULT '0' COMMENT '发送状态0 未发送，1 发送成功',
  `send_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
  `create_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='销售日报微信推送';

/*Data for the table `tb_sales_daily_wechar` */

/*Table structure for table `tb_sender_manage` */

DROP TABLE IF EXISTS `tb_sender_manage`;

CREATE TABLE `tb_sender_manage` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键,id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `biz_send_type` tinyint(4) DEFAULT '0' COMMENT '发送业务类型;0 账期提醒',
  `creator` varchar(50) NOT NULL COMMENT '创建人',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `status` tinyint(4) DEFAULT '0' COMMENT '状态',
  `is_delete` tinyint(4) DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='推送人信息管理';

/*Data for the table `tb_sender_manage` */

/*Table structure for table `tb_sender_project` */

DROP TABLE IF EXISTS `tb_sender_project`;

CREATE TABLE `tb_sender_project` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `sender_id` bigint(20) DEFAULT NULL COMMENT '发送id',
  `project_id` bigint(20) DEFAULT NULL COMMENT '项目id',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `is_delete` tinyint(4) DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='推送人项目关系表';

/*Data for the table `tb_sender_project` */

/*Table structure for table `tb_seq_manage` */

DROP TABLE IF EXISTS `tb_seq_manage`;

CREATE TABLE `tb_seq_manage` (
  `seq_name` varchar(30) NOT NULL COMMENT '序列名称',
  `seq_val` bigint(20) NOT NULL COMMENT '序列值',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`seq_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='序列表';

/*Table structure for table `tb_seq_sync` */

DROP TABLE IF EXISTS `tb_seq_sync`;

CREATE TABLE `tb_seq_sync` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `last_max_id` bigint(20) DEFAULT '0' COMMENT '上一次更新记录的最大id',
  `table_name` varchar(100) DEFAULT NULL COMMENT '表名',
  `creator` varchar(100) DEFAULT NULL COMMENT '创建人',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='同步记录更新表';

/*Data for the table `tb_seq_sync` */

/*Table structure for table `tb_stl` */

DROP TABLE IF EXISTS `tb_stl`;

CREATE TABLE `tb_stl` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '库存ID',
  `bill_in_store_dtl_tally_id` bigint(20) DEFAULT NULL COMMENT '入库单理货明细表ID',
  `project_id` bigint(20) DEFAULT NULL COMMENT '项目ID',
  `supplier_id` bigint(20) DEFAULT NULL COMMENT '供应商ID',
  `warehouse_id` bigint(20) DEFAULT NULL COMMENT '仓库ID',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '客户ID',
  `bill_in_store_id` bigint(20) DEFAULT NULL COMMENT '入库单ID',
  `bill_in_store_dtl_id` bigint(20) DEFAULT NULL COMMENT '入库单明细ID ',
  `po_dtl_id` bigint(20) DEFAULT NULL COMMENT 'PO订单明细ID',
  `po_id` bigint(20) DEFAULT NULL COMMENT 'PO订单ID',
  `goods_id` bigint(20) DEFAULT NULL COMMENT '商品ID',
  `tally_num` decimal(20,8) DEFAULT '0.00000000' COMMENT '理货数量',
  `batch_no` varchar(50) DEFAULT NULL COMMENT '批次',
  `goods_status` tinyint(4) DEFAULT '1' COMMENT '状态 1-常规 2-残次品',
  `cost_price` decimal(20,8) DEFAULT '0.00000000' COMMENT '成本单价',
  `po_price` decimal(20,8) DEFAULT '0.00000000' COMMENT '订单单价',
  `in_store_num` decimal(20,8) DEFAULT '0.00000000' COMMENT '入库数量',
  `store_num` decimal(20,8) DEFAULT '0.00000000' COMMENT '库存数量',
  `lock_num` decimal(20,8) DEFAULT '0.00000000' COMMENT '出库锁定数量',
  `sale_lock_num` decimal(20,8) DEFAULT '0.00000000' COMMENT '销售锁定数量',
  `stl_id` bigint(20) DEFAULT NULL COMMENT '关联库存ID',
  `origin_accept_time` datetime DEFAULT NULL COMMENT '原入库时间',
  `accept_time` datetime DEFAULT NULL COMMENT '入库时间',
  `receive_date` date DEFAULT NULL COMMENT '收货日期',
  `currency_type` tinyint(4) DEFAULT NULL COMMENT '币种 1.人民币 2.美元',
  `exchange_rate` decimal(20,8) DEFAULT '0.00000000' COMMENT '汇率',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `bill_in_store_no` varchar(20) DEFAULT NULL COMMENT '入库单编号',
  `affiliate_no` varchar(200) DEFAULT NULL COMMENT '入库单附属编号',
  `order_no` varchar(20) DEFAULT NULL COMMENT '订单编号',
  `append_no` varchar(200) DEFAULT NULL COMMENT '订单附属编号',
  `pay_price` decimal(20,8) DEFAULT '0.00000000' COMMENT '付款单价',
  `pay_rate` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '付款汇率',
  `pay_real_currency` tinyint(4) NOT NULL DEFAULT '1' COMMENT '付款实际支付币种',
  `pay_time` datetime DEFAULT NULL COMMENT '付款时间',
  PRIMARY KEY (`id`),
  KEY `idx_tb_stl_bill_in_store` (`bill_in_store_dtl_tally_id`,`bill_in_store_id`,`bill_in_store_dtl_id`),
  KEY `idx_tb_stl_purchase_order` (`po_id`,`po_dtl_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='库存表';

/*Data for the table `tb_stl` */

/*Table structure for table `tb_stl_history` */

DROP TABLE IF EXISTS `tb_stl_history`;

CREATE TABLE `tb_stl_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '库存ID',
  `bill_in_store_dtl_tally_id` bigint(20) DEFAULT NULL COMMENT '入库单理货明细表ID',
  `project_id` bigint(20) DEFAULT NULL COMMENT '项目ID',
  `supplier_id` bigint(20) DEFAULT NULL COMMENT '供应商ID',
  `warehouse_id` bigint(20) DEFAULT NULL COMMENT '仓库ID',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '客户ID',
  `bill_in_store_id` bigint(20) DEFAULT NULL COMMENT '入库单ID',
  `bill_in_store_dtl_id` bigint(20) DEFAULT NULL COMMENT '入库单明细ID ',
  `po_dtl_id` bigint(20) DEFAULT NULL COMMENT 'PO订单明细ID',
  `po_id` bigint(20) DEFAULT NULL COMMENT 'PO订单ID',
  `goods_id` bigint(20) DEFAULT NULL COMMENT '商品ID',
  `tally_num` decimal(20,8) DEFAULT '0.00000000' COMMENT '理货数量',
  `batch_no` varchar(50) DEFAULT NULL COMMENT '批次',
  `goods_status` tinyint(4) DEFAULT '1' COMMENT '状态 1-常规 2-残次品',
  `cost_price` decimal(20,8) DEFAULT '0.00000000' COMMENT '成本单价',
  `po_price` decimal(20,8) DEFAULT '0.00000000' COMMENT '订单单价',
  `in_store_num` decimal(20,8) DEFAULT '0.00000000' COMMENT '入库数量',
  `store_num` decimal(20,8) DEFAULT '0.00000000' COMMENT '库存数量',
  `lock_num` decimal(20,8) DEFAULT '0.00000000' COMMENT '出库锁定数量',
  `sale_lock_num` decimal(20,8) DEFAULT '0.00000000' COMMENT '销售锁定数量',
  `stl_id` bigint(20) DEFAULT NULL COMMENT '关联库存ID',
  `origin_accept_time` datetime DEFAULT NULL COMMENT '原入库时间',
  `accept_time` datetime DEFAULT NULL COMMENT '入库时间',
  `receive_date` date DEFAULT NULL COMMENT '收货日期',
  `currency_type` tinyint(4) DEFAULT NULL COMMENT '币种 1.人民币 2.美元',
  `exchange_rate` decimal(20,8) DEFAULT '0.00000000' COMMENT '汇率',
  `copy_date` date DEFAULT NULL COMMENT '复制日期',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `his_create_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '历史库存创建时间',
  `bill_in_store_no` varchar(20) DEFAULT NULL COMMENT '入库单编号',
  `affiliate_no` varchar(200) DEFAULT NULL COMMENT '入库单附属编号',
  `order_no` varchar(20) DEFAULT NULL COMMENT '订单编号',
  `append_no` varchar(200) DEFAULT NULL COMMENT '订单附属编号',
  `pay_price` decimal(20,8) DEFAULT '0.00000000' COMMENT '付款单价',
  `pay_time` datetime DEFAULT NULL COMMENT '付款时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='库存表';

/*Data for the table `tb_stl_history` */

/*Table structure for table `tb_sync_data_timestamp` */

DROP TABLE IF EXISTS `tb_sync_data_timestamp`;

CREATE TABLE `tb_sync_data_timestamp` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `max_update_at` datetime NOT NULL COMMENT '同步最大更新时间',
  `business_type` tinyint(4) DEFAULT NULL COMMENT '业务类型 1-同步顺友BL数据',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='同步数据时间戳表';

/*Data for the table `tb_sync_data_timestamp` */

insert  into `tb_sync_data_timestamp`(`id`,`max_update_at`,`business_type`,`create_at`,`update_at`) values (1,'2018-04-08 04:59:59',1,'2016-11-26 19:13:54','2017-04-26 10:25:16');

/*Table structure for table `tb_sys_param` */

DROP TABLE IF EXISTS `tb_sys_param`;

CREATE TABLE `tb_sys_param` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `param_name` varchar(50) NOT NULL DEFAULT '' COMMENT '参数名称',
  `param_key` varchar(100) NOT NULL DEFAULT '' COMMENT '参数key',
  `param_value` varchar(50) NOT NULL DEFAULT '' COMMENT '参数value',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '参数状态 0-启用 1-禁用 默认0',
  `remark` varchar(255) DEFAULT '' COMMENT '备注',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_sys_param` (`param_name`,`param_key`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='系统参数表';

/*Table structure for table `tb_verification_advance` */

DROP TABLE IF EXISTS `tb_verification_advance`;

CREATE TABLE `tb_verification_advance` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT ' ID',
  `bill_delivery_id` bigint(20) DEFAULT NULL COMMENT '提货单ID,关联tb_bill_delivery[id]',
  `advance_id` bigint(20) DEFAULT NULL COMMENT '预收id',
  `receipt_id` bigint(20) DEFAULT NULL COMMENT '水单id',
  `amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '金额',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `creator_id` int(11) DEFAULT NULL COMMENT '创建人ID',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `remark` varchar(50) DEFAULT NULL COMMENT '备注',
  `is_delete` tinyint(4) DEFAULT '0' COMMENT '逻辑删除标记',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态 ',
  `advance_receipt_rel_id` bigint(20) DEFAULT NULL COMMENT '水单转预收时回写水单预收关联id',
  `advance_receipt_id` bigint(11) DEFAULT NULL COMMENT '预收转成的水单id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='核销预收表';

/*Data for the table `tb_verification_advance` */

/*Table structure for table `tb_wechat_user` */

DROP TABLE IF EXISTS `tb_wechat_user`;

CREATE TABLE `tb_wechat_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `openid` varchar(100) DEFAULT NULL COMMENT '用户的标识，对当前公众号唯一',
  `nickname` varchar(100) DEFAULT NULL COMMENT '用户的昵称',
  `sex` tinyint(1) DEFAULT NULL COMMENT '用户的性别，值为1时是男性，值为2时是女性，值为0时是未知',
  `language` varchar(50) DEFAULT NULL COMMENT '用户的语言，简体中文为zh_CN',
  `city` varchar(50) DEFAULT NULL COMMENT '用户所在城市',
  `province` varchar(50) DEFAULT NULL COMMENT '用户所在省份',
  `country` varchar(50) DEFAULT NULL COMMENT '用户所在国家',
  `headimgurl` varchar(200) DEFAULT NULL COMMENT '用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像URL将失效',
  `subscribe_time` datetime DEFAULT NULL COMMENT '用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间',
  `unionid` varchar(100) DEFAULT NULL COMMENT '只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。',
  `groupid` varchar(20) DEFAULT NULL COMMENT '用户所在的分组ID（兼容旧的用户分组接口）',
  `tagid_list` varchar(100) DEFAULT NULL COMMENT '用户被打上的标签ID列表',
  `subscribe` tinyint(1) DEFAULT NULL COMMENT '用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息。',
  `remark` varchar(200) DEFAULT NULL COMMENT '公众号运营者对粉丝的备注，公众号运营者可在微信公众平台用户管理界面对粉丝添加备注',
  `create_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `user_id` bigint(20) DEFAULT NULL COMMENT '当前系统用户ID',
  `real_name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  `bind_type` tinyint(1) NOT NULL DEFAULT '1' COMMENT '认证状态 1-未认证供应商 2-已认证供应商',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='微信用户表';

/*Data for the table `tb_wechat_user` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
