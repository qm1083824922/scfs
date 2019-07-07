window.operateEvents = {
    'click .submit': function (e, value, row, index) {
    	var id = $(this).closest("tr").data('uniqueid');
		if (id) {
			layer.confirm('确定要提交吗？', {
				btn : [ '确定', '取消' ]
			// 按钮
			}, function() {
				GLOBAL.ajax("voucher/submit", {
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
				GLOBAL.ajax("voucher/delete", {
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
			GLOBAL.go("html/fi/voucher/voucherEdit.html");
		}
    },
    'click .scan': function(e, value, row, index){
    	var id = $(this).closest("tr").data('uniqueid');
		if (id) {
			options.param.id = id;
			GLOBAL.go("html/fi/voucher/voucherScan.html");
		}
    },
    'click .print': function(e, value, row, index){
		if(row){
			var id = row.id;
			window.open("html/fi/voucher/voucherPrint.html?id="+id);
		}
    }
};
/*数据表格*/
var tabCols = [ {
	title : '序号',
	field : 'columnsNumber',
	width : '1%'
},{
	title : '凭证编号',
	field : 'voucherNo',
	width : '6%'
},{
	title : '凭证字',
	field : 'voucherWord',
	align : 'center',
	width : '3%'

},{
	title : '帐套',
	field : 'accountBookName',
	align : 'center',
	width : '9%'
},{
	title : '经营单位',
	field : 'busiUnitName',
	align : 'center',
	width : '10%'
},{
	title : '单据类型',
	field : 'billTypeName',
	align : 'center',
	width : '5%'
},{
	title : '单据编号',
	field : 'billNo',
	align : 'center',
	width : '8%'
},{
	title : '分录数量',
	field : 'voucherLineNumber',
	align : 'center',
	width : '2%'
},{
	title : '借方金额',
	field : 'debitAmount',
	align : 'center',
	width : '5%',
	formatter : function(value , row , index) {
   	 	return value.toThounds();
    }
},{
	title : '贷方金额',
	field : 'creditAmount',
	align : 'center',
	width : '5%',
	formatter : function(value , row , index) {
		return value.toThounds();
    }
}, {
	title : '摘要',
	field : 'voucherSummary',
	align : 'center',
	width : '15%'
},{
	title : '凭证日期',
	field : 'voucherDate',
	align : 'center',
	width : '5%'
},{
	title : '创建人',
	field : 'creator',
	align : 'center',
	width : '3%'
},{
	title : '创建时间',
	field : 'createAt',
	align : 'center',
	width : '8%'
},{
	title : '状态',
	field : 'stateName',
	align : 'center',
	width : '5%'
},{
	title : '操作',
	field : 'opertaList',
	align : 'center',
	width : '11%',
	events: operateEvents
} ];

options.initPage = function() {
	GLOBAL.initOptionsParam('id');
	var dataTable = GLOBAL.initTable($('#js_dataTable'), tabCols);
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

$("#btnNew").click(function() {
	var url = $(this).data("url");
	GLOBAL.go(url);
});

$("#btnExcelExport").click(function() {
    var count_url = "/voucher/voucherLineList/count";
    var data = $("#searchForm").serializeObject();
    GLOBAL.ajax(count_url, data, function(e) {
        if (e.success) {
        	var url = "voucher/voucherLineList.xls";
        	url = url + "?" + $("#searchForm").serialize();
        	location.href = GLOBAL.host + url;
        } else {
            layer.msg(e.msg);
        }
    });
});