
window.operateEvents = {
    'click .ensure': function (e, value, row, index) {
    	var id = row.id;
    	options.param.id = row.id;
	    if(id){
	    	var url = $(this).data("url");
			GLOBAL.go("html/invoice/invoiceEnsure/InvoiceEnsureEdit.html", options.param);
	    }
    }
     
};
function operateFormatter(value, row, index) {
    return [
            '<a class="ensure" href="javascript:void(0)">',
            '<i class="btn btn-primary btn-sm" data-permissionUrl="/ensureInvoiceItem/update" >确认</i>',
            '</a>  '
        ].join('');
}
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
	     title: '发票编号',
	     field: 'invoiceNo',
	     width: 85,
	     align: 'center'
	 }, {
	     title: '项目',
	     field: 'projectName',
	     width: 80,
	     align: 'center'
	 }, {
	     title: '经营单位',
	     field: 'businessUnitName',
	     width: 100,
	     align: 'center'
	 }, {
	     title: '客户',
	     field: 'customerName',
	     width: 100,
	     align: 'center'
	 }, {
	     title: '票据类型',
	     field: 'invoiceTypeName',
	     width: 100,
	     align: 'center'
	 },  {
	     title: '单据类型',
	     field: 'billTypeName',
	     width: 100,
	     align: 'center'
	 }, {
	     title: '发票金额',
	     field: 'applyAmount',
	     width: 100,
	     align: 'center'
	 }, {
	     title: '创建人',
	     field: 'creator',
	     width: 100,
	     align: 'center'
	 }, {
	     title: '创建时间',
	     field: 'createAt',
	     width: 100,
	     align: 'center'
	 }, {
	     title: '状态',
	     field: 'statusName',
	     width: 100,
	     align: 'center'
	 }, {
	     title: '操作',
	     field: 'opertaList1',
	     width: 100,
	     align: 'center',
	     events: operateEvents,
         formatter: operateFormatter
     }
];

options.initPage = function() {
	
	GLOBAL.initOptionsParam('id,cid,hip');
	GLOBAL.initTable($('#js_dataTable'), tabCols, null,false,{
        onPostBody: function(){
            if($(".ensure").length){
                GLOBAL.permissionBtn($(".ensure .btn"));
            }
        },
    	onLoadSuccess: function(data){
    		if(data.options.totalAmount != null){
    			$('#js_dataTable tbody').append("<tr><td style='text-align:left' colspan='" + tabCols.length + "'><b>合计：开票确认总金额："+data.options.totalAmount+"&nbsp;"+data.options.currency
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

function getCondition() {
	return "?businessUnit=" + $("#businessUnit").val() 
	+ "&projectId=" + $("#projectId").val()
	+ "&supplierId=" + $("#supplierId").val()
	+ "&applyNo=" + $("#applyNo").val()
	+ "&invoiceNo=" + $("#invoiceNo").val()
	+ "&invoiceType=" + $("#invoiceType").val()
	+ "&billType=" + $("#billType").val() 
} 

$("#btnExport").click(function() {
	var count_url = "/invoiceEnsureItem/excel/export/count";
	var data = $("#searchForm").serializeObject();
	GLOBAL.ajax(count_url, data, function(e) {
        if (e.success) {
        	var url = "invoiceService/excel/invoiceExcel.xls";
        	url = url + "?" + $("#searchForm").serialize();
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