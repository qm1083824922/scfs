window.operateEvents = {
	'click .scan' : function(e, value, row, index) {
		var id = $(this).closest("tr").data('uniqueid');
		if (id) {
			options.param.id = id;
			GLOBAL.go("html/fi/receiveManage/receiveScan.html");
		}
	},
	'click .edit' : function(e, value, row, index) {
		var id = $(this).closest("tr").data('uniqueid');
		if (id) {
			options.param.id = id;
			GLOBAL.go("html/fi/receiveManage/receiveEdit.html");
		}
	},
	'click .delete' : function(e, value, row, index) {
		var id = $(this).closest("tr").data('uniqueid');
		if (id) {
			layer.confirm('确定要删除吗？', {
				btn : [ '确定', '取消' ]
			// 按钮
			}, function() {
				GLOBAL.ajax("receive/delete", {
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
	}
};
/* 数据表格 */
var tabCols = [ {
	title : '序号',
	field : 'columnsNumber',
	width : '2%'
}, {
	title : '项目',
	field : 'projectName',
	width : '10%',
	align : 'center'
}, {
	title : '客户',
	field : 'custName',
	width : '10%',
	align : 'center'
}, {
	title : '经营单位',
	field : 'busiUnitName',
	width : '10%',
	align : 'center'
}, {
	title : '币种',
	field : 'currencyTypeName',
	width : '4%',
	align : 'center'
}, {
	title : '应收金额',
	field : 'amountReceivable',
	width : '6%',
	align : 'center'
}, {
	title : '已收金额',
	field : 'amountReceived',
	width : '6%',
	align : 'center'
}, {
	title : '未收金额',
	field : 'amountUnReceived',
	width : '6%',
	align : 'center'
},{
	title : '对账日期',
	field : 'checkDate',
	width : '8%',
	align : 'center'
},{
	title : '应收到期日',
	field : 'expireDate',
	width : '8%',
	align : 'center'
},{
	title : '创建人',
	field : 'creator',
	align : 'center',
	width : '5%'
}, {
	title : '创建时间',
	field : 'createAt',
	align : 'center',
	width : '8%'
},{
	title : '操作',
	field : 'opertaList',
	width : '15%',
	align : 'center',
	events : operateEvents
}];

options.initPage = function() {
	GLOBAL.initOptionsParam('id');
	var option = {
		onLoadSuccess : function(data) {
			if(data.options.totalStr != null){
				$('#js_dataTable tbody').append("<tr><td style='text-align:left;font-weight:bold' colspan='" + tabCols.length + "'>合计：" + data.options.totalStr + "</td></tr>");
			}
		}
	}
	var dataTable = GLOBAL.initTable($('#js_dataTable'), tabCols , null , null , option);
	//还原查询条件及过滤后的数据
	GLOBAL.restoreQuery($('#js_dataTable')); 

	$(".js_datePicker").datetimepicker({
        format:'Y-m-d',
    });
}

$("#btnSearch").click(function() {
	var data = $("#searchForm").serializeObject();
	if(data){
		var url = $('#js_dataTable').data("url");
		GLOBAL.local(url, data);
	}
	
	GLOBAL.tableRefresh($('#js_dataTable'), data);
});

function getCondition() {
	var condition ;
	if($("#projectId").val() != null){ 
	}
	return "?projectId=" + $("#projectId").val()
	+ "&custId=" + $("#custId").val()
	+ "&busiUnit=" + $("#busiUnit").val()
	+ "&currencyType=" + $("#currencyType").val()
	+ "&startCheckDate=" + $("input[name='startCheckDate']").val()
	+ "&endCheckDate=" + $("input[name='endCheckDate']").val()
	+ "&startExpireDate=" + $("input[name='startExpireDate']").val()
	+ "&endExpireDate=" + $("input[name='endExpireDate']").val()
	+ "&startBillDate=" + $("input[name='startCheckDate']").val()
	+ "&endBillDate=" + $("input[name='endCheckDate']").val()
	+ "&billNo=" + $("#billNo").val()
}

$("#btnExport").click(function() {
	var url = $(this).data("url");
	url = url + "?" + $("#searchForm").serialize();
	location.href = GLOBAL.host + url;
})
