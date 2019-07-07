window.operateEvents = {
    'click .submit': function (e, value, row, index) {
    	var id = $(this).closest("tr").data('uniqueid');
		if (id) {
			layer.confirm('确定要提交吗？', {
				btn : [ '确定', '取消' ]
			// 按钮
			}, function() {
				GLOBAL.ajax("fee/receive/submit", {
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
				GLOBAL.ajax("fee/rec/deduction/delete", {
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
			GLOBAL.go("html/fee/recDeductionFee/recDeductionFeeEdit.html");
		}
    },
    'click .scan': function(e, value, row, index){
    	var id = $(this).closest("tr").data('uniqueid');
		if (id) {
			options.param.id = id;
			options.param.feeNo = row.feeNo;
			options.param.state = row.state;
			GLOBAL.go("html/fee/recDeductionFee/recDeductionFeeScan.html");
		}
    },
};
/*数据表格*/
var tabCols = [ {
	title : '序号',
	field : 'columnsNumber',
	width : '1%'
},{
	title : '费用编号',
	field : 'feeNo',
	align : 'center',
	width : '8%'

},{
	title : '经营单位',
	field : 'busiUnitNameNo',
	align : 'center',
	width : '10%'

},{
	title : '项目',
	field : 'projectName',
	align : 'center',
	width : '10%'

},{
	title : '应收客户',
	field : 'custPayerName',
	align : 'center',
	width : '10%'

},{
	title : '应收费用科目',
	field : 'recFeeSpecName',
	align : 'center',
	width : '7%'

},{
	title : '应收日期',
	field : 'recDate',
	align : 'center',
	width : '6%'

},{
	title : '币种',
	field : 'currencyTypeName',
	align : 'center',
	width : '3%'

},{
	title : '应收抵扣金额',
	field : 'recAmount',
	align : 'center',
	width : '4%',
	formatter : function(value , row , index) {
		return value==null ? "": value.toThounds();
    }
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

$("#btnNew").click(function() {
	var url = $(this).data("url");
	GLOBAL.go(url);
});

$("#import").click(function() {
	var url = $(this).data("url");
	GLOBAL.go(url);
});

$("#btnExcelExport").click(function() {
	var data = $("#searchForm").serializeObject();
	GLOBAL.ajax("fee/excel/recDeductionCount", data, function(e) {
		 if (e.success) {
            var url = "fee/excel/recDeductionFee.xls";
            url = url + getCondition();
            location.href = GLOBAL.host + url;
        } else {
            layer.msg(e.msg);
        }
	});
});

function getCondition() {
	return "?busiUnit=" + $("#busiUnit").val()
	+  "&custPayer=" + $("#custPayer").val()
	+  "&recFeeSpec=" + $("#recFeeSpec").val()
	+  "&deductionType=" + $("#deductionType").val()
	+  "&feeNo=" + $("#feeNo").val()
	+  "&state=" + $("#state").val()
	+  "&startRecDate=" + $("#startRecDate").val()
	+  "&endRecDate=" + $("#endRecDate").val()
	+  "&projectId=" + $("#projectId").val()
	+  "&recType=" + $("#recType").val();
}
