var tabCols = [ {
	title : '序号',
	field : 'columnsNumber',
	width : '4%'
},{
	title : '科目编号',
	field : 'accountLineNo',
	width : '10%',
	align : 'center'
},{
	title : '科目级别',
	field : 'accountLineLevelName',
	width : '10%',
	align : 'center'
},{
	title : '科目名称',
	field : 'accountLineName',
	width : '10%',
	align : 'center'
},{
	title : '科目分类',
	field : 'accountLineTypeName',
	width : '10%',
	align : 'center'
},{
	title : '借贷',
	field : 'debitOrCreditName',
	width : '5%',
	align : 'center'
},{
	title : '辅助项',
	field : 'needDec',
	width : '20%',
	align : 'center'
},{
	title : '创建人',
	field : 'creator',
	align : 'center',
	width : '3%'
},{
	title : '创建时间',
	field : 'createAt',
	align : 'center',
	width : '7%'
},{
	title : '状态',
	field : 'stateName',
	width : '5%',
	align : 'center'
},{
	title : '操作',
	field : 'opertaList',
	width : '10%',
	align : 'center',
} ];

options.initPage = function() {
	GLOBAL.initOptionsParam('id');
	var dataTable = GLOBAL.initTable($('#js_dataTable'), tabCols);

	//还原查询条件及过滤后的数据
	GLOBAL.restoreQuery($('#js_dataTable')); 

	$("#btnSearch").click(function() {
		var data = $("#searchForm").serializeObject();
		if(data){
			var url = $('#js_dataTable').data("url");
			GLOBAL.local(url, data);
		}
		GLOBAL.tableRefresh($('#js_dataTable'), data);
	});

	$("#btnNew").click(function() {
		var url = $(this).data("url");
		GLOBAL.go(url);
	});

	$('#js_dataTable').on("click", ".delete", function() {
		var id = $(this).closest("tr").data('uniqueid');

		if (id) {
			layer.confirm('确定要删除吗？', {
				btn : [ '确定', '取消' ]
			// 按钮
			}, function() {
				GLOBAL.ajax("accountLine/delete", {
					id : id
				}, function(e) {
					if (e.success) {
						layer.msg("删除成功！");
						$("#btnSearch").click();
					} else {
						layer.msg(e.msg);
					}
				})
			}, function() {
			});

		}
	});

	$('#js_dataTable').on("click", ".submit", function() {
		var id = $(this).closest("tr").data('uniqueid');
		if (id) {
			layer.confirm('确定要提交吗？', {
				btn : [ '确定', '取消' ]
			// 按钮
			}, function() {
				GLOBAL.ajax("accountLine/submit", {
					id : id
				}, function(e) {
					if (e.success) {
						layer.msg("提交成功！");
						$("#btnSearch").click();
					} else {
						layer.msg(e.msg);
					}
				});
			}, function() {
			});
		}
	});

	$('#js_dataTable').on(
			"click",
			".scan",
			function() {
				var data = $(this).closest("tr").data();
				var id = $(this).closest("tr").data('uniqueid');
				if (id) {
					options.param.id = id;
					GLOBAL.go("html/fi/accountLine/accountLineScan.html");
				}
			});

	$('#js_dataTable').on(
			"click",
			".edit",
			function() {
				var id = $(this).closest("tr").data('uniqueid');
				if (id) {
					var url = $(this).data("url");
					options.param.id = id;
					GLOBAL.go("html/fi/accountLine/accountLineEdit.html");
				}
			});
	
	$("#btnExcelExport").click(function() {
		var url = "accountLine/excel/accountLine.xls";
		url += "?accountLineNo=" + $("#accountLineNo").val()
			+  "&accountLineType=" + $("#accountLineType").val()
			+  "&accountLineLevel=" + $("#accountLineLevel").val()
			+  "&debitOrCredit=" + $("#debitOrCredit").val();
				
		location.href = GLOBAL.host + url;
	});
}
