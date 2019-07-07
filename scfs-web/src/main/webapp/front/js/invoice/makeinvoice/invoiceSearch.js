
window.operateEvents = {
    'click .submit': function (e, value, row, index) {
    	var id = row.id;
	    if(id){
	    	layer.confirm('确定要提交吗？', {
		        btn: ['确定', '取消'] //按钮
		    }, function() {
		         GLOBAL.ajax("invoiceService/submit", {id: id}, function(e) {
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
	    	GLOBAL.ajax("invoiceService/delete", {id: id}, function(){
	    		layer.msg("删除成功", {
	                icon: 1
	            })
	            $("#btnSearch").click();
	    	})
	    }, function() {});
    },
    'click .edit': function(e, value, row, index){
    	options.param.id = row.id;
		options.param.cid = row.id;  
		options.param.billType =row.billType; 
		GLOBAL.go("html/invoice/makeinvoice/InvoiceEdit.html", options.param);	 
    },
    'click .scan': function(e, value, row, index){
		options.param.id = row.id;
		options.param.cid = row.id; 
		options.param.billType =row.billType; 
		options.param.status =row.status; 
		options.param.applyNo =row.applyNo; 
		GLOBAL.go("html/invoice/makeinvoice/invoiceScan.html", options.param); 
		
    },
    'click .print': function(e, value, row, index){
		if(row){
			var id = row.id;
			window.open("html/invoice/makeinvoice/invoiceApplyPrint.html?id="+id);
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
	     align: 'center'
	 }, {
	     title: '发票编号',
	     field: 'invoiceNo', 
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
	 }, {
	     title: '发票号',
	     field: 'invoiceCode',	     
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
	 }, {
	     title: '项目',
	     field: 'projectName',	     
	     align: 'center'
	 }, {
	     title: '经营单位',
	     field: 'businessUnitName',	     
	     align: 'center'
	 }, {
	     title: '客户',
	     field: 'customerName',	     
	     align: 'center'
	 },  {
	     title: '票据类型',
	     field: 'invoiceTypeName',	     
	     align: 'center'
	 }, {
	     title: '单据类别',
	     field: 'billTypeName',	     
	     align: 'center'
	 }, {
	     title: '申请金额',
	     field: 'applyAmount',	     
	     align: 'center'
	 }, {
	     title: '创建人',
	     field: 'creator',	     
	     align: 'center'
	 }, {
	     title: '创建时间',
	     field: 'createAt',	     
	     align: 'center'
	 },{
	     title: '状态',
	     field: 'statusName',	     
	     align: 'center'
	 }, {
	     title: '操作',
	     field: 'opertaList',	     
	     align: 'center',
	     events: operateEvents
	 }
];

var totalNum  = 0;
options.initPage = function() {
	
	GLOBAL.initOptionsParam('id,cid,hip');
	GLOBAL.initTable($('#js_dataTable'), tabCols, null,null,{
		onLoadSuccess: function(data){
			totalNum = data.total;
			if(data.options.totalAmount != null){ 
				$('#js_dataTable tbody').append("<tr><td style='text-align:left' colspan='" + tabCols.length + "'><b>合计：开票申请总金额："+data.options.totalAmount+"&nbsp;"+data.options.currency
						+ "</b></td></tr>");
			}
		}
	}); 
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
	var count_url = "invoiceApply/excel/export/count";
	var data = $("#searchForm").serializeObject();
	GLOBAL.ajax(count_url, data, function(e) {
        if (e.success) {
        	var url = "invoiceService/excel/invoiceApplyExcel.xls";
        	url = url + "?" + $("#searchForm").serialize();
        	location.href = GLOBAL.host + url;
        } else {
            layer.msg(e.msg);
        }
    });
}) 