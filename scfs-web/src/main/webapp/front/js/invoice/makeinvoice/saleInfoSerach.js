 	window.operateEvents = {
		'click .edit' : function(e, value, row, index) {
			options.param.id = row.id;
			options.param.invoiceApplyId =options.param.cid ;
			GLOBAL.go("html/invoice/makeinvoice/saleInfoEdit.html");
		}
	};
/*数据表格*/
var tabCols = [
	 {
	    field: 'checkItem',
	    checkbox: true,
	 },{  
	     title: '序号',
	     field: 'columnsNumber',  
	     width: 10
     },{
	     title: '销售/退货单编号',
	     field: 'billNo',
	     width: 92,
	     align: 'center'
	 }, {
	     title: '单据日期',
	     field: 'billDate',
	     width: 85,
	     align: 'center'
	 }, {
	     title: '商品编号',
	     field: 'goodsNumber',
	     width: 80,
	     align: 'center'
	 }, {
	     title: '商品描述',
	     field: 'goodsName',
	     width: 100,
	     align: 'center'
	 }, {
	     title: '数量',
	     field: 'provideInvoiceNum',
	     width: 100,
	     align: 'center'
	 }, {
	     title: '金额',
	     field: 'provideInvoiceAmount',
	     width: 100,
	     align: 'center',
	     formatter: function(value,row,index){ 
	    	 return value.toThounds();
	     }
	 }, {
	     title: '未税金额',
	     field: 'exRateAmount',
	     width: 100,
	     align: 'center',
	     formatter: function(value,row,index){ 
	    	 return value.toThounds();
	     }
	 }, {
	     title: '税额',
	     field: 'rateAmount',
	     width: 100,
	     align: 'center',
	     formatter: function(value,row,index){ 
	    	 return value.toThounds();
	     }
	 },{
		 title: '操作',
	     field: 'opertaList',
	     width: 110,
	     align: 'center',
	     events: operateEvents
	}
];

options.initPage = function(){
	GLOBAL.initTable($('#js_dataTable'), tabCols, {id: options.param.id},null,{
		onLoadSuccess: function(data){
			if(data.options.feeTotalAmount != null){ 
				$('#js_dataTable tbody').append("<tr><td style='text-align:left' colspan='" + tabCols.length + "'><b>合计：销售总金额："+data.options.feeTotalAmount.toThounds()+"&nbsp;"+data.options.currency
						+",未税总金额:"+ data.options.exRateTotalAmount.toThounds()+data.options.currency+ ",总税额:"+ data.options.rateTotalAmount.toThounds()+data.options.currency+ "</b></td></tr>");
			}
		}
	}); 
  };

$("#back").click(function(){
    var url = $(this).data("url");
    options.param.id=options.param.cid;
    GLOBAL.go(url, options.param);
})
$("#batchEdit").click(function(){
    var url = $(this).data("url");
    options.param.id=options.param.cid;
    GLOBAL.go(url, options.param);
})
$("#Add").click(function(){
    var url = $(this).data("url");
    options.param.id=options.param.cid;
    GLOBAL.go(url, options.param);
})
 // 批量删除
$("#batchDelete").click(function() {
	
	var ids =  GLOBAL.selectIds($('#js_dataTable'));
	if (ids) {
		var url = $(this).data("url");
		var data = {
			ids : ids 
		}
		layer.confirm('确定要删除吗？', {
			btn : [ '确定', '取消' ]
		// 按钮
		}, function() { 
			GLOBAL.ajax(url, data, function(e) {
				if (e.success) { 
					layer.msg("删除成功！");
					$('#js_dataTable').bootstrapTable('refresh', {query: {id: options.param.id}});
				} else {
					layer.msg(e.msg);
				}
			});
		}, function() {
		});
	}
});