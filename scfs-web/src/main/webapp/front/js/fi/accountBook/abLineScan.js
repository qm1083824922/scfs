/*数据表格*/
var tabCols = [ {
	title : '序号',
	field : 'columnsNumber',
	width : '5%'
}, {
	title : '科目编号',
	field : 'accountLineNo',
	width : 80,
	align : 'center'
}, {
	title : '科目级别',
	field : 'accountLineLevelName',
	width : 210,
	align : 'center'
}, {
	title : '科目名称',
	field : 'accountLineName',
	width : 100,
	align : 'center'
}, {
	title : '科目分类',
	field : 'accountLineTypeName',
	width : 70,
	align : 'center'
}, {
	title : '借贷',
	field : 'debitOrCreditName',
	width : 70,
	align : 'center'
}, {
	title : '辅助项',
	field : 'needDec',
	width : 70,
	align : 'center'
}, {
	title : '分配人',
	field : 'creator',
	width : 80,
	align : 'center'
}, {
	title : '分配时间',
	field : 'createAt',
	width : 80,
	align : 'center'
}, {
	title : '作废人',
	field : 'deleter',
	width : 80,
	align : 'center'
}, {
	title : '作废时间',
	field : 'deleteAt',
	width : 80,
	align : 'center'
}, {
	title : '状态',
	field : 'stateName',
	width : 80,
	align : 'center'
} ];

options.initPage = function() {
	GLOBAL.initTable($('#js_dataTable'), tabCols, {
		accountBookId : options.param.id
	});
}

$("#back").click(function() {
	var url = $(this).data("url");
	GLOBAL.go(url);
});
