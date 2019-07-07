/* 数据表格 */
var tabCols = [ {
	title : '序号',
	field : 'columnsNumber',
	width : 10
}, {
	title : '水单编号',
	field : 'receiptNo',
	width : 92,
	align : 'center'
}, {
	title : '水单类型',
	field : 'receiptTypeName',
	width : 85,
	align : 'center'
}, {
	title : '银行流水号',
	field : 'bankReceiptNo',
	width : 92,
	align : 'center'
}, {
	title : '日期',
	field : 'receiptDate',
	width : 96,
	align : 'center'
}, {
	title : '项目',
	field : 'projectName',
	width : 180,
	align : 'center'
}, {
	title : '经营单位',
	field : 'busiUnit',
	width : 180,
	align : 'center'
}, {
	title : '收款账户',
	field : 'recAccountNo',
	width : 72,
	align : 'center'
}, {
	title : '客户',
	field : 'custName',
	width : 72,
	align : 'center'
}, {
	title : '币种',
	field : 'currencyTypeName',
	width : 70,
	align : 'center'
}, {
	title : '水单金额',
	field : 'receiptAmount',
	width : 70,
	align : 'center',
	formatter : function(value , row , index) {
		return value==null ? "": value.toThounds();
    }
}, {
	title : '核销金额',
	field : 'writeOffAmount',
	width : 76,
	align : 'center',
	formatter : function(value , row , index) {
		return value==null ? "": value.toThounds();
    }
}];

options.initPage = function() {
	GLOBAL.initTable($('#js_dataTable'), tabCols , {recId : options.param.id});
}

GLOBAL.goBack();