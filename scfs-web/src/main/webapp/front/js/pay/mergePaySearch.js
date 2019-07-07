window.operateEvents = {
    'click .submit': function (e, value, row, index) {
    	var id = row.id;
	    if(id){
	    	layer.confirm('确定要提交吗？', {
		        btn: ['确定', '取消'] //按钮
		    }, function() {
		         GLOBAL.ajax("mergePayOrder/submit", {id: id}, function(e) {
	                if (e.success) {
	                    layer.msg("提交成功！");
	                    $("#btnSearch").click();
	                } else {
	                    layer.msg(e.msg);
	                }
	            });
		    }, function() {});
	    }
    },
    'click .delete': function (e, value, row, index) {
        var id = row.id;
	    layer.confirm('确定要删除吗？', {
	        btn: ['确定', '取消'] //按钮
	    }, function() {
	    	GLOBAL.ajax("mergePayOrder/delete", {id: id}, function(){
	    		layer.msg("删除成功", {
	                icon: 1
	            })
	            $('#js_dataTable').bootstrapTable('refresh');
	    	})
	    }, function() {});
    },
    'click .edit': function(e, value, row, index){
		options.param.id = row.id;
		GLOBAL.go("html/pay/mergePay/mergePayEdit.html");
    },
    'click .scan': function(e, value, row, index){
		options.param.id = row.id;
		GLOBAL.go("html/pay/mergePay/mergePayScan.html");
    },
    'click .print': function(e, value, row, index){
    	var id = row.id;
    	var prePrint = function () {
    		GLOBAL.ajax("mergePayOrder/print", {id : id} , function(e) {
				if (e.success) {
					window.open("html/pay/mergePay/mergePayPrint.html?id="+id);
				} else {
					layer.msg(e.msg);
				}
			},false);
    	}
		if(row.printNum > 0){
			layer.confirm("【" + row.mergePayNo + "】已经打印过 " + row.printNum + "次，是否继续打印？",function(index) {
				prePrint();
				layer.close(index);
			});
		} else {
			prePrint();
		}
    }
};

/*数据表格*/
var tabCols = [
	 {  
	     title: '序号',
	     field: 'columnsNumber',  
	     width: 10
     }, {
	     title: '合并付款编号',
	     field: 'mergePayNo',
	     width: 92,
	     align: 'center'
     }, {
	     title: '经营单位',
	     field: 'busiUnit',
	     width: 180,
	     align: 'center'
	 }, {
	     title: '项目',
	     field: 'projectName',
	     width: 180,
	     align: 'center'
	 }, {
	     title: '业务类别',
	     field: 'bizTypeName',
	     width: 180,
	     align: 'center'
	 }, {
	     title: '付款类型',
	     field: 'payTypeName',
	     width: 85,
	     align: 'center'
	 }, {
	     title: '付款方式',
	     field: 'payWayName',
	     width: 40,
	     align: 'center'
	 }, {
	     title: '币种',
	     field: 'currencyTypeName',
	     width: 30,
	     align: 'center'
	 }, {
	     title: '付款金额',
	     field: 'payAmount',
	     width: 40,
	     align: 'center',
	 	 formatter : function(value , row , index) {
			return value==null ? "": value.toThounds();
	     }
	 }, {
	     title: '收款单位',
	     field: 'payeeName',
	     width: 76,
	     align: 'center'
	 }, {
	     title: '要求付款日期',
	     field: 'requestPayTime',
	     width: 86,
	     align: 'center'
	 }, {
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
	     title: '状态',
	     field: 'state',
	     width: 88,
	     align: 'center'
	 }, {
	     title: '操作',
         field: 'opertaList',
         width: 160,
         align: 'center',
         events: operateEvents
	 }
];

options.initPage = function(){
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
	
	$(".js_datePicker").datetimepicker({
        format:'Y-m-d',
    });
	
 };

 $("#btnSearch").click(function() {
	var data = $("#searchForm").serializeObject();
	if(data){
		var url = $('#js_dataTable').data("url");
		GLOBAL.local(url, data);
	}
	console.log(data);
	GLOBAL.tableRefresh($('#js_dataTable'), data);
})

$("#btnNew").click(function() {
    var url = $(this).data("url");
    var data = $("#searchForm").serializeObject();
    $.extend(options.param, data);
    GLOBAL.go(url, options.param);
})

$("#btnExport").click(function() {
	var count_url = "/payOrder/excel/export/count";
	var data = $("#searchForm").serializeObject();
	GLOBAL.ajax(count_url, data, function(e) {
        if (e.success) {
        	var url = "payOrder/excel/collectApprove.xls";
        	url = url + "?" + $("#searchForm").serialize();
        	location.href = GLOBAL.host + url;
        } else {
            layer.msg(e.msg);
        }
    });
})
