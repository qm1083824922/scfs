var tabCols = [ {
	field : 'checkItem',
	checkbox : true,
	width : '1%'
},{
	title : '序号',
	field : 'columnsNumber',
	width : '1%'
}, {
	title : '科目编号',
	field : 'accountLineNo',
	width : '8%',
	align : 'center'
}, {
	title : '科目级别',
	field : 'accountLineLevelName',
	width : '5%',
	align : 'center'
}, {
	title : '科目名称',
	field : 'accountLineName',
	width : '5%',
	align : 'center'
}, {
	title : '科目分类',
	field : 'accountLineTypeName',
	width : '5%',
	align : 'center'
}, {
	title : '借贷',
	field : 'debitOrCreditName',
	width : '5%',
	align : 'center'
}, {
	title : '辅助项',
	field : 'needDec',
	width : '15%',
	align : 'center'
}, {
	title : '分配人',
	field : 'creator',
	width : '5%',
	align : 'center'
}, {
	title : '分配时间',
	field : 'createAt',
	width : '15%',
	align : 'center'
}, {
	title : '作废人',
	field : 'deleter',
	width : '5%',
	align : 'center'
}, {
	title : '作废时间',
	field : 'deleteAt',
	width : '15%',
	align : 'center'
}, {
	title : '状态',
	field : 'stateName',
	width : '5%',
	align : 'center'
} ];

options.initPage = function() {
	GLOBAL.initTable($('#js_dataTable'), tabCols, {
		accountBookId : options.param.id
	});
}

$("#abLineDivide").click(function() {
	var url = $(this).data("url");
	GLOBAL.go(url);
});

// 批量作废
$("#multInvalid").click(function() {
	var ids = [];
	$("#js_dataTable tr").each(function() {
		var cb = $(this).find("input[name='btSelectItem']");
		if (cb.prop("checked")) {
			var id = $(this).data("uniqueid");
			ids.push(id);
		}
	});

	if (ids.length == 0) {
		layer.msg('请选择数据!');
		return false;
	}
	var url = $(this).data("url");
	var data = {
		ids : ids
	}
	layer.confirm('确定要作废吗？', {
		btn : [ '确定', '取消' ]
	// 按钮
	}, function() {
		GLOBAL.ajax(url, JSON.stringify(data), function(e) {
			if (e.success) {
				var data = {
					accountBookId : options.param.id
				}
				GLOBAL.tableRefresh($('#js_dataTable'), data);
				layer.msg("作废成功！");
			} else {
				layer.msg(e.msg);
			}
		}, true);
	}, function() {
	});
});

$("#back").click(function() {
	var url = $(this).data("url");
	GLOBAL.go(url);
});
