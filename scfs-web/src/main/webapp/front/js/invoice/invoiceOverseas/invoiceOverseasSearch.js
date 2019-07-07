window.operateEvents = {
    'click .submit': function (e, value, row, index) {
    	var id = row.id;
	    if(id){
	    	layer.confirm('确定要提交吗？', {
		        btn: ['确定', '取消'] //按钮
		    }, function() {
		         GLOBAL.ajax("invoiceCollectOver/submit", {id: id}, function(e) {
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
	    	GLOBAL.ajax("invoiceCollectOver/delete", {id: id}, function(){
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
		GLOBAL.go("html/invoice/invoiceOverseas/invoiceOverseasEdit.html", options.param);
    },
    'click .scan': function(e, value, row, index){
		options.param.id = row.id;
		options.param.billType = row.billType;
		GLOBAL.go("html/invoice/invoiceOverseas/invoiceOverseasScan.html", options.param);
    },
    'click .approve': function(e, value, row, index){
		options.param.id = row.id;
		options.param.billType = row.billType;
		GLOBAL.go("html/invoice/invoiceOverseas/invoiceOverseasApproveEdit.html", options.param);
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
	 },  {
	     title: '发票号',
	     field: 'invoiceNo',
	     width: 80,
	     align: 'center',
	     formatter: function(value, row, index) {
		     	if(value){
		     		value = value.split(",");
			     	var str = ""; 
			     	if(value.length > 4){
			     		for(var i =0, len = 3; i < len;  i++){
				     		str += '<p style="white-space:nowrap">'+value[i]+'</p>';
				     	}
			     		str += '<p style="white-space:nowrap;position:relative;bottom:4px">'+"..."+'</p>';
			     		str += '<p style="white-space:nowrap">'+value[value.length-1]+'</p>';
			     	}
			     	else{
			     		for(var i =0, len = value.length; i < len;  i++){
				     		str += '<p style="white-space:nowrap">'+value[i]+'</p>';
				     	}
			     	}
			     	return str;
		     	}
		     	
		    }
	 },{
	     title: '经营单位',
	     field: 'businessUnitName',
	     width: 100,
	     align: 'center'
	 }, {
	     title: '项目',
	     field: 'projectName',
	     width: 100,
	     align: 'center'
	 }, {
	     title: '供应商',
	     field: 'supplierName',
	     width: 100,
	     align: 'center'
	 },{
	     title: '票据类型',
	     field: 'billTypeName',
	     width: 100,
	     align: 'center'
	 }, {
	     title: '币种',
	     field: 'currnecyTypeName',
	     width: 100,
	     align: 'center'
	 },{
	     title: '发票金额',
	     field: 'invoiceAmount',
	     width: 80,
	     align: 'center',
	 	formatter : function(value , row , index) {
			return value==null ? "": value.toThounds();
	    }
	 },{
	     title: '发票日期',
	     field: 'invoiceDate',
	     width: 80,
	     align: 'center'
	 },{
	     title: '认证日期',
	     field: 'approveDate',
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
// 查询
$("#btnSearch").click(function() {
	var data = $("#invoiceForm").serializeObject();
	if(data){
		var url = $('#js_dataTable').data("url");
		GLOBAL.local(url, data);
	}
    GLOBAL.tableRefresh($('#js_dataTable'), data);
})    

// 新建Invoice收票
$("#btnNew").click(function() {
    var url = $(this).data("url");
    GLOBAL.go(url, options.param);
})

$("#btnExport").click(function() {
	var count_url = "/invoiceCollectOver/excel/export/count";
	var data = $("#invoiceForm").serializeObject();
	GLOBAL.ajax(count_url, data, function(e) {
        if (e.success) {
        	var url = "invoiceCollectOver/excel/invoiceCollectOver.xls";
        	url = url + "?" + $("#invoiceForm").serialize();
        	location.href = GLOBAL.host + url;
        } else {
            layer.msg(e.msg);
        }
    });
}) 

$("#btnImport").click(function() {
	   var url = $(this).data("url");
	   GLOBAL.go(url, options.param);
})