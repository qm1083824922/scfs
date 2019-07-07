/* 数据表格 */
var tabCols = [ {
	title : '序号',
	field : 'columnsNumber',
	width : '2%'
}, {
	title : '项目',
	field : 'projectName',
	width : '15%',
	align : 'center'
}, {
	title : '客户',
	field : 'custName',
	width : '15%',
	align : 'center'
}, {
	title : '经营单位',
	field : 'busiUnitName',
	width : '10%',
	align : 'center'
}, {
	title : '单据编号',
	field : 'billNo',
	width : '8%',
	align : 'center'
},{
	title : '单据日期',
	field : 'billDate',
	width : '8%',
	align : 'center'
},{
	title : '币种',
	field : 'currencyTypeName',
	width : '8%',
	align : 'center'
}, {
	title : '已对账金额',
	field : 'amountCheck',
	width : '10%',
	align : 'center'
}, {
	title : '核销金额',
	field : 'writeOffAmount',
	width : '10%',
	align : 'center'
}];

options.initPage = function() {
	GLOBAL.initTable($('#js_dataTable'), tabCols , {recId : options.param.id});
}

GLOBAL.goBack();