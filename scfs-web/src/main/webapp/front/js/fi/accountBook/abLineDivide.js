/*数据表格*/
var tabCols = [ {
	field : 'checkItem',
	checkbox : true,
	width : '5%'
},
{
	title : '序号',
	field : 'columnsNumber',
	width : '5'
}, {
	title : '科目编号',
	field : 'accountLineNo',
	width : '15%',
	align : 'center'
}, {
	title : '科目级别',
	field : 'accountLineLevelName',
	width : '10%',
	align : 'center'
}, {
	title : '科目名称',
	field : 'accountLineName',
	width : '15%',
	align : 'center'
}, {
	title : '科目分类',
	field : 'accountLineTypeName',
	width : '10%',
	align : 'center'
}, {
	title : '借贷',
	field : 'debitOrCreditName',
	width : '5%',
	align : 'center'
}, {
	title : '辅助项',
	field : 'needDec',
	width : '25%',
	align : 'center'
}, {
	title : '状态',
	field : 'stateName',
	width : '10%',
	align : 'center'
} ];

options.initPage = function() {
	var dataTable = GLOBAL.initTable($('#js_dataTable'), tabCols, {
		accountBookId : options.param.id
	});
	$("#btnSearch").click(function() {
		var data = $("#searchForm").serializeObject();
		$.extend(data, {
			accountBookId : options.param.id
		});
		GLOBAL.tableRefresh($('#js_dataTable'), data);
	});

	$("#back").click(function() {
		var url = $(this).data("url");
		GLOBAL.go(url);
	});

	$("#btn_div").on(
			"click",
			"#mult-divide",
			function() {
				var data = {};
				var lineIds = [];
				$("#js_dataTable tr").each(function() {
					var cb = $(this).find("input[name='btSelectItem']");
					if (cb.prop("checked")) {
						var id = $(this).data("uniqueid");
						lineIds.push(id);
					}
				});

				if (lineIds.length == 0) {
					layer.msg('请选择数据!');
					return false;
				}
				var data = {
					id : options.param.id,
					ids : lineIds
				}
				GLOBAL.ajax("accountBook/line/rel/batch/add", JSON
						.stringify(data), function(e) {
					if (e.success) {
						layer.msg("分配成功！");
						$("#btnSearch").click();
					} else {
						layer.msg(e.msg);
					}
				}, true);
			})
};
