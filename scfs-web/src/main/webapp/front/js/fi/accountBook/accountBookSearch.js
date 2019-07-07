window.operateEvents = {
    'click .submit': function (e, value, row, index) {
    	var id = $(this).closest("tr").data('uniqueid');
		if (id) {
			layer.confirm('确定要提交吗？', {
				btn : [ '确定', '取消' ]
			// 按钮
			}, function() {
				GLOBAL.ajax("accountBook/submit", {
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
    },
    'click .delete': function (e, value, row, index) {
    	var id = $(this).closest("tr").data('uniqueid');
		if (id) {
			layer.confirm('确定要删除吗？', {
				btn : [ '确定', '取消' ]
			// 按钮
			}, function() {
				GLOBAL.ajax("accountBook/delete", {
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
    },
    'click .edit': function(e, value, row, index){
    	var id = $(this).closest("tr").data('uniqueid');
		if (id) {
			options.param.id = id;
			GLOBAL.go("html/fi/accountBook/abBaseEdit.html");
		}
    },
    'click .scan': function(e, value, row, index){
    	var id = $(this).closest("tr").data('uniqueid');
		if (id) {
			options.param.id = id;
			GLOBAL.go("html/fi/accountBook/abBaseScan.html");
		}
    }
};
/*数据表格*/
var tabCols = [ {
	title : '序号',
	field : 'columnsNumber',
	width : '5%'
},{
	title : '经营单位',
	field : 'busiUnitName',
	align : 'center',
	width : '11%'
},{
	title : '帐套编号',
	field : 'accountBookNo',
	align : 'center',
	width : '11%'
},{
	title : '财务编号',
	field : 'fiNo',
	align : 'center',
	width : '11%'
},{
	title : '帐套名称',
	field : 'accountBookName',
	align : 'center',
	width : '11%'
},{
	title : '财务主管',
	field : 'auditor',
	align : 'center',
	width : '10%'
},{
	title : '本位币',
	field : 'standardCoinName',
	align : 'center',
	width : '5%'
},{
	title : '创建人',
	field : 'creator',
	align : 'center',
	width : '6%'
},{
	title : '创建时间',
	field : 'createAt',
	align : 'center',
	width : '10%'
},{
	title : '状态',
	field : 'stateName',
	align : 'center',
	width : '5%'
},{
	title : '操作',
	field : 'opertaList',
	align : 'center',
	width : '15%',
	events: operateEvents
} ];

options.initPage = function() {
	GLOBAL.initOptionsParam('id');
	
	var dataTable = GLOBAL.initTable($('#js_dataTable'), tabCols);
	//还原查询条件及过滤后的数据
	GLOBAL.restoreQuery($('#js_dataTable')); 
}

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

$("#btnExcelExport").click(function() {
	var url = "accountBook/excel/accountBook.xls";
	url += "?busiUnit=" + $("#busiUnit").val()
		+  "&fiNo=" + $("#fiNo").val()
		+  "&accountBookName=" + $("#accountBookName").val()
		+  "&state=" + $("#state").val();
			
	location.href = GLOBAL.host + url;
});
