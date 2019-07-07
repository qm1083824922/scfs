window.operateEvents = {
	'click .scan': function(e, value, row, index){
		var data = $("#searchForm").serializeObject();
		if (data.statisticsDimension == 1) {
			if (row.projectId == null) {
				layer.msg("统计维度为项目，项目id不能为空");
				return false;
			}else {
				data.projectId = row.projectId;
			}
		}
		if (data.statisticsDimension == 2) {
			if (row.capitalAccountType == null) {
				layer.msg("统计维度为资金占用类型，资金占用类型不能为空");
				return false;
			}else{ 
				data.capitalAccountType = row.capitalAccountType;
				data.accountId = row.accountId;
				if(data.capitalAccountType == 1){
					data.projectId = row.projectId;
					data.departmentId=row.departmentId;
				}
				if(data.capitalAccountType==2){
					data.businessUnitId = row.businessUnitId;
				}
			}
		}
		if (data.statisticsDimension == 3) {
			if (row.projectId == null || row.capitalAccountType == null) {
				layer.msg("统计维度为项目和资金占用类型，项目id和资金占用类型不能为空");
				return false;
			}else {
				data.projectId = row.projectId; 
				data.capitalAccountType = row.capitalAccountType;
			}
		}
		data.currencyType = row.currencyType;
		data.startPeriod = row.startIssueDate;
		data.endPeriod = row.endIssueDate;
		var open_url = "projectId=" + (typeof(data.projectId)=='undefined'?'':data.projectId) + "&capitalAccountType=" + (typeof(data.capitalAccountType)=='undefined'?'':data.capitalAccountType)  + "&startDate=" + data.startDate + "&endDate=" + data.endDate
						+ "&departmentId=" + (typeof(data.departmentId)=='undefined'?'':data.departmentId) + "&currencyType=" + data.currencyType + "&startPeriod=" + data.startPeriod + "&endPeriod=" + data.endPeriod + "&periodType=" + data.periodType
						+ "&statisticsDimension=" + data.statisticsDimension+"&businessUnitId="+(typeof(data.businessUnitId)=='undefined'?'':data.businessUnitId)+"&accountId="+(typeof(data.accountId)=='undefined'?'':data.accountId);
		window.open("html/report/fund/fundDtlReport.html?" + open_url);
    },
    'click .scanCost': function(e, value, row, index) {
    	var data = $("#searchForm").serializeObject();
		if (data.statisticsDimension == 1) {
			if (row.projectId == null) {
				layer.msg("统计维度为项目，项目id不能为空");
				return false;
			}else {
				data.projectId = row.projectId; 
			}
		}
		if (data.statisticsDimension == 2) {
			if (row.capitalAccountType == null) {
				layer.msg("统计维度为资金占用类型，资金占用类型不能为空");
				return false;
			}else{ 
				data.capitalAccountType = row.capitalAccountType;
				data.accountId = row.accountId;
				if(data.capitalAccountType == 1){
					data.projectId = row.projectId;
				}
				if(data.capitalAccountType==2){
					data.businessUnitId = row.businessUnitId;
				}
			}
		}
		if (data.statisticsDimension == 3) {
			if (row.projectId == null || row.capitalAccountType == null) {
				layer.msg("统计维度为项目和资金占用类型，项目id和资金占用类型不能为空");
				return false;
			}else {
				data.projectId = row.projectId; 
				data.capitalAccountType = row.capitalAccountType;
			}
		}
		data.currencyType = row.currencyType;
		data.startPeriod = row.startIssueDate;
		data.endPeriod = row.endIssueDate;
		var open_url = "projectId=" + (typeof(data.projectId)=='undefined'?'':data.projectId) + "&capitalAccountType=" + (typeof(data.capitalAccountType)=='undefined'?'':data.capitalAccountType)  + "&startDate=" + data.startDate + "&endDate=" + data.endDate
			+ "&departmentId=" + (typeof(data.departmentId)=='undefined'?'':data.departmentId) + "&currencyType=" + data.currencyType + "&startPeriod=" + data.startPeriod + "&endPeriod=" + data.endPeriod + "&periodType=" + data.periodType+ "&statisticsDimension=" + data.statisticsDimension
			+"&businessUnitId="+(typeof(data.businessUnitId)=='undefined'?'':data.businessUnitId)+"&accountId="+(typeof(data.accountId)=='undefined'?'':data.accountId);;

		window.open("html/report/fund/fundCostDtl.html?" + open_url);
    }
};
/* 数据表格 */
var tabCols = [ {
	title : '序号',
	field : 'columnsNumber',
	width : '2%'
}, {
	title : '部门',
	field : 'departmentName',
	width : '10%',
	align : 'center'
},{
	title : '项目',
	field : 'projectName',
	width : '10%',
	align : 'center'
},{
	title : '期号',
	field : 'issue',
	width : '5%',
	align : 'center'
}, {
	title : '资金占用类型',
	field : 'capitalAccountTypeName',
	width : '10%',
	align : 'center'
}, {
	title : '币种',
	field : 'currencyTypeName',
	width : '4%',
	align : 'center'
}, {
	title : '期初余额',
	field : 'beginBalance',
	width : '6%',
	align : 'center',
	formatter : function(value , row , index) {
		return  value.toThounds() ;
	}
},{
	title : '本期付款',
	field : 'payAmount',
	width : '6%',
	align : 'center',
	formatter : function(value , row , index) {
		if (value != null) {
			return  value.toThounds() ;
		}
	}
}, {
	title : '本期收款',
	field : 'receiptAmount',
	width : '6%',
	align : 'center',
	formatter : function(value , row , index) {
		if (value != null) {
			return  value.toThounds();
		}
	}
}, {
	title : '余额',
	field : 'balance',
	width : '6%',
	align : 'center',
	formatter : function(value , row , index) {
		return  "<a href='javascript:void(0)'; class='scan'>" + value.toThounds() + "</a>";
	},
	events: operateEvents
},{
	title : '资金成本',
	field : 'fundCost',
	width : '8%',
	align : 'center',
	formatter : function(value , row , index) {
		return  "<a href='javascript:void(0)'; class='scanCost'>" + value.toThounds() + "</a>";
	},
	events: operateEvents
}];

options.initPage = function() {
	var option = {
		onLoadSuccess : function(data) {
			if(data.options.totalStr != null){
				$('#js_dataTable tbody').append("<tr><td style='text-align:left;font-weight:bold' colspan='" + tabCols.length + "'>合计：" + data.options.totalStr + "</td></tr>");
			}
		}
	}
    $('.period_datePicker').datepicker({
        format: "yyyymm",
        minViewMode: 1,
        maxViewMode: 2,
        language: 'zh-CN',
        clearBtn: true
    });
	
	$(".js_datePicker").datetimepicker({
        format:'Y-m-d',
    });
	var datea =  new Date(); 
	$("#endDate").val(GLOBAL.setDefaultDate(0));
	$("#startDate").val(GLOBAL.setDefaultDate(1-datea.getDate()));
//	$("#startDate").val(GLOBAL.setBeginOfMonth());
//	$("#endDate").val(GLOBAL.setEndOfMonth());
	var param = {
		statisticsDimension : 1,
		startDate : $("#startDate").val(),
		endDate : $("#endDate").val()
	}
	var dataTable = GLOBAL.initTable($('#js_dataTable'), tabCols , param , null , option);
	//还原查询条件及过滤后的数据
	//GLOBAL.restoreQuery($('#js_dataTable')); 
	
	$("#statisticsDimension").change(function(){
        var thisValue = $(this).val();
        if (thisValue == 1) {	//1-项目
        	$("#capitalAccountType").val("");
			$("#capitalAccountType").attr("disabled","disabled");
			$("#projectId").val("");
			$("#projectId").removeAttr("disabled");
			$("#departmentName").val("");
			$("#departmentName").removeAttr("disabled","disabled");
			$("#departmentId").val("");
			$("#departmentId").removeAttr("disabled","disabled"); 
        } else if (thisValue == 2) {	//2-客户
        	$("#capitalAccountType").val("");
			$("#capitalAccountType").removeAttr("disabled");
			$("#projectId").val("");
			$("#projectId").attr("disabled","disabled");
			$("#departmentName").val("");
			$("#departmentName").attr("disabled","disabled");
			$("#departmentId").val("");
			$("#departmentId").attr("disabled","disabled");  
         } else {	//3-项目+客户
        	$("#capitalAccountType").val("");
			$("#capitalAccountType").removeAttr("disabled");
			$("#projectId").val("");
			$("#projectId").removeAttr("disabled");
			$("#departmentName").val("");
			$("#departmentName").removeAttr("disabled");
			$("#departmentId").val("");
			$("#departmentId").removeAttr("disabled"); 
        }
    });
}
$('input:radio[name="periodType"]').change(function(){
	
	var value = $("input[name='periodType']:checked").val();
	if (value == 1) {
		$("#startPeriod").attr("readonly", "readonly");
		$("#endPeriod").attr("readonly", "readonly");
		$("#startPeriod").val("");
		$("#endPeriod").val("");  
		$("#startDate").removeAttr("readonly");
		$("#endDate").removeAttr("readonly");
		$("#js_dataTable").bootstrapTable("removeAll");

	} else {
		$("#startPeriod").removeAttr("readonly");
		$("#endPeriod").removeAttr("readonly");
		$("#startPeriod").val("");
		$("#endPeriod").val(""); 
		$("#startDate").attr("readonly", "readonly");
		$("#endDate").attr("readonly", "readonly");
		$("#js_dataTable").bootstrapTable("removeAll");
	}
});
$("#btnSearch").click(function() {
	var data = $("#searchForm").serializeObject();
	if (! data.startDate || ! data.endDate) {
		layer.msg("日期区间不能为空");
		return;
	}
	if(GLOBAL.getIntervalDays(data.startDate, data.endDate) > 30) {
		layer.msg("日期跨度不能超过30天");
		return;
	}
	if ($("#departmentName").val().length == 0) {
		data.departmentId = '';
	}
	if(data){
		var url = $('#js_dataTable').data("url");
		GLOBAL.local(url, data);
	}
	
	GLOBAL.tableRefresh($('#js_dataTable'), data);
});

$("#btnExport").click(function() {
    var url = $(this).data("url");
	url = url + "?" + $("#searchForm").serialize();
	location.href = GLOBAL.host + url;
});

$("#resetEmpty").click(function() {
	$("#departmentId").val("");
});
