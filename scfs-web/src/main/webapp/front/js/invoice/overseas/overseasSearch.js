
window.operateEvents = {
    'click .submit': function (e, value, row, index) {
    	var id = row.id;
	    if(id){
	    	layer.confirm('确定要提交吗？', {
		        btn: ['确定', '取消'] //按钮
		    }, function() {
		         GLOBAL.ajax("invoiceOverseas/submit", {id: id}, function(e) {
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
	    	GLOBAL.ajax("invoiceOverseas/delete", {id: id}, function(){
	    		layer.msg("删除成功", {
	                icon: 1
	            })
	            $("#btnSearch").click();
	    	})
	    }, function() {});
    },
    'click .edit': function(e, value, row, index){
    	options.param.id = row.id;
    	options.param.billType = row.billType;
    	options.param.isMerge = row.isMerge;
		GLOBAL.go("html/invoice/overseas/overseasInfoEdit.html", options.param);
    },
    'click .scan': function(e, value, row, index){
		options.param.id = row.id;
		options.param.billType = row.billType;
		options.param.isMerge = row.isMerge;
		GLOBAL.go("html/invoice/overseas/overseasInfoScan.html", options.param);
    },
    'click .print': function(e, value, row, index){
    	if(row.printNum > 0){
			layer.confirm("【" + row.applyNo + "】已经打印过 " + row.printNum + "次，是否继续打印？",function(index) {
				var id = row.id;
				var type = row.billType;
				if(type == "2"){
					window.open("html/invoice/overseas/overseasPrint.html?id="+id);
				}else{
					window.open("html/invoice/overseas/overseasPoPrint.html?id="+id);
				}
				layer.close(index);
			});
		} else {
			var id = row.id;
			var type = row.billType;
			if(type == "2"){
				window.open("html/invoice/overseas/overseasPrint.html?id="+id);
			}else{
				window.open("html/invoice/overseas/overseasPoPrint.html?id="+id);
			}
		}
    }
    
};
/*数据表格*/
var tabCols = [
	 {  
	     title: '序号',
	     field: 'columnsNumber',  
	     width: 10
     },{
	     title: '申请编号',
	     field: 'applyNo',
	     width: 92,
	     align: 'center'
	 }, {
	     title: '项目',
	     field: 'projectName',
	     width: 100,
	     align: 'center'
	 }, {
	     title: '经营单位',
	     field: 'businessUnitName',
	     width: 100,
	     align: 'center'
	 },{
	     title: '客户',
	     field: 'customerName',
	     width: 100,
	     align: 'center'
	 },{
	     title: '单据类别',
	     field: 'billTypeName',
	     width: 100,
	     align: 'center'
	 }, {
	     title: '申请金额金额',
	     field: 'invoiceAmount',
	     width: 80,
	     align: 'center',
	 	formatter : function(value , row , index) {
			return value==null ? "": value.toThounds();
	    }
	 },{
	     title: '状态',
	     field: 'stateName',
	     width: 100,
	     align: 'center'
	  }, {
	     title: '操作',
	     field: 'opertaList',
	     width: 100,
	     align: 'center',
	     events: operateEvents
	 }
];

options.initPage = function() {
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
}

$("#btnSearch").click(function() {
	var data = $("#searchForm").serializeObject();
	if(data){
		var url = $('#js_dataTable').data("url");
		GLOBAL.local(url, data);
	}
    GLOBAL.tableRefresh($('#js_dataTable'), data);
})    

$("#btnNew").click(function() {
    var url = $(this).data("url");
    GLOBAL.go(url, options.param);
})


$("#btnExport").click(function() {
	var count_url = "/overseas/excel/export/count";
	var data = $("#searchForm").serializeObject();
	GLOBAL.ajax(count_url, data, function(e) {
        if (e.success) {
        	var url = "overseas/excel/overseas.xls";
        	url = url + "?" + $("#searchForm").serialize();
        	location.href = GLOBAL.host + url;
        } else {
            layer.msg(e.msg);
        }
    });
}) 