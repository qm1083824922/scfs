alter table `tb_purchase_order_title` add column fly_order_flag tinyint(1) DEFAULT 0 COMMENT '飞单标识 0-否 1-是';
alter table `tb_bill_in_store` add column fly_order_flag tinyint(1) DEFAULT 0 COMMENT '飞单标识 0-否 1-是';
alter table `tb_bill_out_store` add column fly_order_flag tinyint(1) DEFAULT 0 COMMENT '飞单标识 0-否 1-是';
alter table `tb_bill_delivery` add column fly_order_flag tinyint(1) DEFAULT 0 COMMENT '飞单标识 0-否 1-是';
alter table `tb_stl` add column fly_order_flag tinyint(1) DEFAULT 0 COMMENT '飞单标识 0-否 1-是';
alter table `tb_stl_history` add column fly_order_flag tinyint(1) DEFAULT 0 COMMENT '飞单标识 0-否 1-是';
alter table `tb_base_exchange_rate` modify column bank varchar(20) DEFAULT NULL COMMENT '银行 1-汇丰银行 2-中国银行';
alter table `tb_base_exchange_rate` modify column currency varchar(20) DEFAULT NULL COMMENT '币种,1-CNY 2-USD 3-UKD 4-EUR';

INSERT INTO `tb_biz_constant` (`biz_code`, `code`, `value`, `ord`, `is_delete`, `create_at`, `remark`, `p_biz_code_code`)
VALUES ('YES_NO', '0', '否', 2, 0, '2018-01-03 16:02:46', '1-是 0-否', NULL);
INSERT INTO `tb_biz_constant` (`biz_code`, `code`, `value`, `ord`, `is_delete`, `create_at`, `remark`, `p_biz_code_code`)
VALUES ('YES_NO', '1', '是', 1, 0, '2018-01-03 16:02:46', '1-是 0-否', NULL);

update tb_base_permission set is_delete = 1 where ord in (2200000, 1905000, 1112000, 1114000);

INSERT INTO `tb_biz_constant` (`biz_code`, `code`, `value`, `ord`, `is_delete`, `create_at`, `remark`, `p_biz_code_code`)
VALUES
	('ACCEPT_INVOICE_TAX_RATE', '0.16', '0.16', 7, 0, '2018-01-03 16:02:46', '收票税率,tb_fee[accept_invoice_tax_rate]', NULL),
	('BILL_RATE', '0.16', '0.16', 6, 0, '2018-01-03 16:02:46', '开票税率', NULL),
	('PROVIDE_INVOICE_TAX_RATE', '0.16', '0.16', 7, 0, '2018-01-03 16:02:46', '开票税率,tb_fee[provide_invoice_tax_rate]', NULL),
	('VOUCHER_LINE_TAX_RATE', '0.16', '0.16', 6, 0, '2018-01-03 16:02:46', '自动录入凭证税率[tb_fi_voucher_line[tax_rate]', NULL);

INSERT INTO `tb_biz_constant` (`biz_code`, `code`, `value`, `ord`, `is_delete`, `create_at`, `remark`, `p_biz_code_code`)
VALUES
	('GOODS_CHINA_RATE', '0.16', '0.16', 3, 0, '2018-01-03 16:02:46', '', NULL);

update tb_biz_constant set code = '0.05' where biz_code = 'ACCEPT_INVOICE_TAX_RATE' and value = '0.05';
update tb_biz_constant set code = '0.05' where biz_code = 'PROVIDE_INVOICE_TAX_RATE' and value = '0.05';
update tb_biz_constant set ord = '0' where biz_code = 'GOODS_CHINA_RATE' and value = '0';

alter table `tb_pay_order` add column none_order_flag tinyint(1) DEFAULT 0 COMMENT '是否无订单 0-否 1-是';

INSERT INTO `tb_biz_constant` (`biz_code`, `code`, `value`, `ord`, `is_delete`, `create_at`, `remark`, `p_biz_code_code`)
VALUES ('NONE_ORDER_FLAG', '0', '否', 2, 0, '2018-08-19 16:02:46', '1-是 0-否', NULL);
INSERT INTO `tb_biz_constant` (`biz_code`, `code`, `value`, `ord`, `is_delete`, `create_at`, `remark`, `p_biz_code_code`)
VALUES ('NONE_ORDER_FLAG', '1', '是', 1, 0, '2018-08-19 16:02:46', '1-是 0-否', NULL);

--2018-09-01
update tb_biz_constant set is_delete = 1 where biz_code = 'SETTLE_RATE_TYPE' and code = '0';
alter table `tb_pay_order` add column write_off_flag tinyint(1) DEFAULT 0 COMMENT '核销状态 0-未核销 1-已核销';

INSERT INTO `tb_biz_constant` (`biz_code`, `code`, `value`, `ord`, `is_delete`, `create_at`, `remark`, `p_biz_code_code`)
VALUES ('WRITE_OFF_FLAG', '0', '未核销', 1, 0, '2018-09-06 16:02:46', '1-已核销 0-未核销', NULL);
INSERT INTO `tb_biz_constant` (`biz_code`, `code`, `value`, `ord`, `is_delete`, `create_at`, `remark`, `p_biz_code_code`)
VALUES ('WRITE_OFF_FLAG', '1', '已核销', 2, 0, '2018-09-06 16:02:46', '1-已核销 0-未核销', NULL);

alter table `tb_pay_order` add column check_amount decimal(20,8) DEFAULT 0 COMMENT '核销金额';

alter table `tb_pay_po_relation` add column write_off_flag tinyint(1) DEFAULT 0 COMMENT '核销状态 0-未核销 1-已核销';
alter table `tb_pay_fee_relation` add column write_off_flag tinyint(1) DEFAULT 0 COMMENT '核销状态 0-未核销 1-已核销';

--2018-09-19
delete from `tb_biz_constant` where biz_code = 'SETTLE_RATE_TYPE' and code = 0;

