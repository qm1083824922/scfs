window.operateEvents = {
	'click .scan': function(e, value, row, index){
		var searchType = $(this).attr("searchtype");
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
			if (row.custId == null) {
				layer.msg("统计维度为客户，项目id不能为空");
				return false;
			}else{
				data.custId = row.custId;
			}
		}
		if (data.statisticsDimension == 3) {
			if (row.projectId == null || row.custId == null) {
				layer.msg("统计维度为项目和客户，项目id和客户id不能为空");
				return false;
			}else {
				data.projectId = row.projectId;
				data.custId = row.custId;
			}
		}
		data.currencyType = row.currencyType;
		var open_url = "projectId=" + (typeof(data.projectId)=='undefined'?'':data.projectId) + "&custId=" + (typeof(data.custId)=='undefined'?'':data.custId) + "&startCheckDate=" + data.startCheckDate + "&endCheckDate=" + data.endCheckDate
						+ "&departmentId=" + (typeof(data.departmentId)=='undefined'?'':data.departmentId) + "&bizManagerId=" + (typeof(data.bizManagerId)=='undefined'?'':data.bizManagerId) 
						+ "&currencyType=" + data.currencyType + "&searchType=" + (typeof(searchType)=='undefined'?'':searchType);
		window.open("html/report/receive/receiveDetail.html?" + open_url);
    },
    'click .scanUnCheck' : function(e, value, row, index) {
    	var open_url = "projectId=" + (row.projectId==null?'':row.projectId) + "&custId=" + (row.custId==null?'':row.custId) 
		+ "&currencyType=" + row.currencyType ;
    	window.open("html/report/receive/checkDetail.html?" + open_url);
    }
    
};
/* 数据表格 */
var tabCols = [ {
	title : '序号',
	field : 'columnsNumber',
	width : '2%'
}, {
	title : '事业部',
	field : 'departmentName',
	width : '10%',
	align : 'center'
},{
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
	title : '业务员',
	field : 'bizManagerName',
	width : '10%',
	align : 'center'
}, {
	title : '币种',
	field : 'currencyTypeName',
	width : '4%',
	align : 'center'
}, {
	title : '未对账金额',
	field : 'unCheckAmount',
	width : '6%',
	align : 'center',
	formatter : function(value , row , index) {
		if (!value) {
			return 0.00 ;
		}else {
			return "<a href='javascript:void(0);' class='scanUnCheck' searchType='1'>" + value.toThounds() + "</a>";
		}
	},
	events: operateEvents
},{
	title : '应收金额',
	field : 'balance',
	width : '6%',
	align : 'center',
	formatter : function(value , row , index) {
		return "<a href='javascript:void(0);' class='scan' >" + value.toThounds() + "</a>";
	},
	events: operateEvents
}, {
	title : '超期应收金额',
	field : 'expireRecAmount',
	width : '6%',
	align : 'center',
	formatter : function(value , row , index) {
		if (!value) {
			return 0.00 ;
		}else {
			return "<a href='javascript:void(0);' class='scan' searchType='1'>" + value.toThounds() + "</a>";
		}
	},
	events: operateEvents
}, {
	title : '超期1-7天金额',
	field : 'expireAmount1',
	width : '6%',
	align : 'center',
	formatter : function(value , row , index) {
		if (!value) {
			return 0.00 ;
		} else {
			return "<a href='javascript:void(0);' class='scan' searchType='2'>" + value.toThounds() + "</a>";
		}
	},
	events: operateEvents
},{
	title : '超期8-15天金额',
	field : 'expireAmount2',
	width : '8%',
	align : 'center',
	formatter : function(value , row , index) {
		if (!value) {
			return 0.00 ;
		} else {
			return "<a href='javascript:void(0);' class='scan' searchType='3'>" + value.toThounds() + "</a>";
		}
	},
	events: operateEvents
},{
	title : '超期15天以上金额',
	field : 'expireAmount3',
	width : '8%',
	align : 'center',
	formatter : function(value , row , index) {
		if (!value) {
			return 0.00 ;
		} else {
			return "<a href='javascript:void(0);' class='scan' searchType='4'>" + value.toThounds() + "</a>";
		}
	},
	events: operateEvents
},{
	title : '临期0-7天金额',
	field : 'adventAmount1',
	align : 'center',
	width : '5%',
	formatter : function(value , row , index) {
		if (!value) {
			return 0.00 ;
		}else {
			return "<a href='javascript:void(0);' class='scan' searchType='5'>" + value.toThounds() + "</a>";
		}
	},
	events: operateEvents
}, {
	title : '临期8到15天金额',
	field : 'adventAmount2',
	align : 'center',
	width : '8%',
	formatter : function(value , row , index) {
		if (!value) {
			return 0.00 ;
		} else {
			return "<a href='javascript:void(0);' class='scan' searchType='6'>" + value.toThounds() + "</a>";
		}
	},
	events: operateEvents
},{
	title : '临期15天以上金额',
	field : 'adventAmount3',
	width : '8%',
	align : 'center',
	formatter : function(value , row , index) {
		if (!value) {
			return 0.00 ;
		} else {
			return "<a href='javascript:void(0);' class='scan' searchType='7'>" + value.toThounds() + "</a>";
		}
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
	var param = {
		statisticsDimension : 1
	}
	var dataTable = GLOBAL.initTable($('#js_dataTable'), tabCols , param , null , option);
	//还原查询条件及过滤后的数据
	//GLOBAL.restoreQuery($('#js_dataTable')); 

	$(".js_datePicker").datetimepicker({
        format:'Y-m-d',
    });
	
	$("#statisticsDimension").change(function(){
        var thisValue = $(this).val();
        if (thisValue == 1) {	//1-项目
        	$("#custId").val("");
			$("#custId").attr("disabled","disabled");
			$("#projectId").val("");
			$("#projectId").removeAttr("disabled");
			$("#bizManagerId").val("");
			$("#bizManagerId").removeAttr("disabled");
			$("#departmentName").val("");
			$("#departmentName").removeAttr("disabled","disabled");
			$("#departmentId").val("");
			$("#departmentId").removeAttr("disabled","disabled"); 
        } else if (thisValue == 2) {	//2-客户
        	$("#custId").val("");
			$("#custId").removeAttr("disabled");
			$("#projectId").val("");
			$("#projectId").attr("disabled","disabled");
			$("#bizManagerId").val("");
			$("#bizManagerId").attr("disabled","disabled");
			$("#departmentName").val("");
			$("#departmentName").attr("disabled","disabled");
			$("#departmentId").val("");
			$("#departmentId").attr("disabled","disabled");  
        } else {	//3-项目+客户
        	$("#custId").val("");
			$("#custId").removeAttr("disabled");
			$("#projectId").val("");
			$("#projectId").removeAttr("disabled");
			$("#bizManagerId").val("");
			$("#bizManagerId").removeAttr("disabled");
			$("#departmentName").val("");
			$("#departmentName").removeAttr("disabled");
			$("#departmentId").val("");
			$("#departmentId").removeAttr("disabled");
        }
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
$("#departmentName").change(function() {
	$("#bizManagerId").empty();
	GLOBAL.ajax("common/selected/query?key=DEPARTMENT_USER&pId=" + $("#departmentId").val(), null, function(data) {
        if (data) {
            var option = "<option value=''>请选择</option>";
            var items = data.items;
            if (items) {
                for (var k = 0; k < items.length; k++) {
                	option += '<option value="' + items[k].code + '">' + items[k].value + '</option>';
                }
                $("#bizManagerId").append(option);
            }
        }
    })
    $('select').select2({language: "zh-CN"});
});
$("#btnExport").click(function() {
    var url = $(this).data("url");
	url = url + "?" + $("#searchForm").serialize();
	location.href = GLOBAL.host + url;
});

$("#resetEmpty").click(function() {
	$("#departmentId").val("");
});
