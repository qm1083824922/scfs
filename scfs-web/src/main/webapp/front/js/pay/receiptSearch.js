window.operateEvents = {
	'click .submit' : function(e, value, row, index) {
		var id = row.id;
		if (id) {
			layer.confirm('确定要提交吗？', {
				btn : [ '确定', '取消' ]
			// 按钮
			}, function() {
				GLOBAL.ajax("bankReceipt/submit", {
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
	'click .over' : function(e, value, row, index) {
		var id = row.id;
		if (id) {
			layer.confirm('确定要核完吗？', {
				btn : [ '确定', '取消' ]
			// 按钮
			}, function() {
				GLOBAL.ajax("bankReceipt/over", {
					id : id
				}, function(e) {
					if (e.success) {
						layer.msg("操作成功！");
						$("#btnSearch").click();
					} else {
						layer.msg(e.msg);
					}
				});
			}, function() {
			});
		}
	},
	'click .delete' : function(e, value, row, index) {
		var id = row.id;
		layer.confirm('确定要删除吗？', {
			btn : [ '确定', '取消' ]
		// 按钮
		}, function() {
			GLOBAL.ajax("bankReceipt/delete", {
				id : id
			}, function() {
				layer.msg("删除成功", {
					icon : 1
				})
				$('#js_dataTable').bootstrapTable('refresh');
			})
		}, function() {
		});
	},
	
	'click .reject' : function(e, value, row, index) {
		var id = row.id;
		layer.confirm('确定要驳回吗？', {
			btn : [ '确定', '取消' ]
		// 按钮
		}, function() {
			GLOBAL.ajax("bankReceipt/reject", {
				id : id
			}, function() {
				layer.msg("驳回成功", {
					icon : 1
				})
				$('#js_dataTable').bootstrapTable('refresh');
			})
		}, function() {
		});
	},
	
	'click .edit' : function(e, value, row, index) {
		options.param.id = row.id;
		options.param.type = row.receiptType;
		GLOBAL.go("html/pay/receipt/receiptBaseInfoEdit.html");
		
	},
	'click .check' : function(e, value, row, index) {
		options.param.id = row.id;
		options.param.type = row.receiptType;
		options.param.way = row.receiptWay;  
		GLOBAL.go("html/pay/receipt/receiptCheck.html");
		
	},
	'click .scan' : function(e, value, row, index) {
		options.param.id = row.id;
		options.param.type = row.receiptType;
		options.param.state = row.state;
		options.param.receiptNo = row.receiptNo;
		GLOBAL.go("html/pay/receipt/receiptCheckScan.html");
	}
};

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
	field : 'busiUnitName',
	width : 180,
	align : 'center'
}, {
	title : '收款账户',
	field : 'recAccountNoName',
	width : 72,
	align : 'center'
}, {
	title : '客户',
	field : 'cusName',
	width : 72,
	align : 'center'
}, {
	title : '水单币种',
	field : 'actualCurrencyTypeName',
	width : 70,
	align : 'center'
}, {
	title : '水单金额',
	field : 'actualReceiptAmount',
	width : 70,
	align : 'center',
	formatter : function(value , row , index) {
		return value==null ? "": value.toThounds();
    }
}, {
	title : '手续费',
	field : 'actualDiffAmount',
	width : 110,
	align : 'center'
},{
	title : '核销金额',
	field : 'writeOffAmount',
	width : 76,
	align : 'center',
	formatter : function(value , row , index) {
		return value==null ? "": value.toThounds();
    }
}, {
	title : '核销币种',
	field : 'currencyTypeName',
	width : 70,
	align : 'center'
}, {
	title : '预收金额',
	field : 'actualPreRecAmount',
	width : 76,
	align : 'center',
	formatter : function(value , row , index) {
		return value==null ? "": value.toThounds();
    }
},{
	title : '已付金额',
	field : 'actualPaidAmount',
	width : 110,
	align : 'center',
	formatter : function(value , row , index) {
		return value==null ? "": value.toThounds();
    }
},{
    title: '核完人',
    field: 'writeOfftor',
    width: 80,
    align: 'center'
}, {
    title: '核完时间',
    field: 'writeOffAt',
    width: 80,
    align: 'center'
},{
    title: '创建人',
    field: 'creator',
    width: 80,
    align: 'center'
}, {
    title: '创建时间',
    field: 'createAt',
    width: 80,
    align: 'center'
}, {
	title : '状态',
	field : 'stateName',
	width : 88,
	align : 'center'
}, {
	title : '操作',
	field : 'opertaList',
	width : 160,
	align : 'center',
	events : operateEvents
} ];

options.initPage = function() {
	$(".js_datePicker").datetimepicker({format:"Y-m-d", timepicker:false});
	GLOBAL.initOptionsParam('id,cid,hip');
	var option = {
			onLoadSuccess : function(data) {
				if(data.options.totalStr != null){
					$('#js_dataTable tbody').append("<tr><td style='text-align:left;font-weight:bold' colspan='" + tabCols.length + "'>合计：" + data.options.totalStr + "</td></tr>");
				}
			}
		}
	GLOBAL.initTable($('#js_dataTable'), tabCols, null, null , option);
	//还原查询条件及过滤后的数据
	GLOBAL.restoreQuery($('#js_dataTable')); 
};

$("#btnSearch").click(function() {
	var receiptAmount = $('#receiptAmount');
	var a=/^[0-9]*(\.[0-9]{1,2})?$/;
	if(!a.test(receiptAmount.val())){
		layer.msg("请输入正确格式");
		receiptAmount.focus();
	}else{
		var data = $("#searchForm").serializeObject();
		if(data){
			var url = $('#js_dataTable').data("url");
			GLOBAL.local(url, data);
		}
		GLOBAL.tableRefresh($('#js_dataTable'), data);
	}
})

$("#btnNew").click(function() {
	var url = $(this).data("url");
	GLOBAL.go(url, options.param);
})

/*function getCondition() {
	return "?custId=" + $("#custId").val() 
	+ "&projectId=" + $("#projectId").val()
	+ "&busiUnit=" + $("#busiUnit").val()
	+ "&receiptNo=" + $("#receiptNo").val()
	+ "&bankReceiptNo=" + $("#bankReceiptNo").val()
	+ "&receiptType=" + $("#receiptType").val()
	+ "&startReceiptDate=" + $("#startReceiptDate").val()
	+ "&endReceiptDate=" + $("#endReceiptDate").val()
	+ "&state=" + $("#state").val()
	+ "&isOver=" + $("#isOver").val();
}
$("#btnExport").click(function() {
	var url = "/bankReceipt/excel/collect.xls";
	url = url + getCondition();
	location.href = GLOBAL.host + url;
})*/

$("#btnExport").click(function() {
	var count_url = "/bankReceipt/excel/export/count";
	var data = $("#searchForm").serializeObject();
	GLOBAL.ajax(count_url, data, function(e) {
        if (e.success) {
        	var url = "bankReceipt/excel/collect.xls";
        	url = url + "?" + $("#searchForm").serialize();
        	location.href = GLOBAL.host + url;
        } else {
            layer.msg(e.msg);
        }
    });
}) 