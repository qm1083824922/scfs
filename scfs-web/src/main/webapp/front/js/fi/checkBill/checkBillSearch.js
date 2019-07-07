window.operateEvents = {
    'click .checkBill': function (e, value, row, index) {
    	var data = $("#searchForm").serializeObject();
    	data.projectId = row.projectId;
    	data.custId = row.custId;
    	data.busiUnit = row.busiUnit;
    	data.currencyType = row.currencyType;
    	options.param = data ;
    	GLOBAL.go("html/fi/checkBill/checkBill.html");
    },
    'click .scan': function (e, value, row, index) {
    	var data = $("#searchForm").serializeObject();
    	data.projectId = row.projectId;
    	data.custId = row.custId;
    	data.busiUnit = row.busiUnit;
    	data.currencyType = row.currencyType;
    	options.param = data ;
    	GLOBAL.go("html/fi/checkBill/checkBillDetail.html");
    }
};

/*数据表格*/
var tabCols = [
	 {  
	     title: '序号',
	     field: 'columnsNumber',  
	     width: '2%'
     }, {
	     title: '项目',
	     field: 'projectName',
	     width: '15%',
	     align: 'center'
	 }, {
	     title: '客户',
	     field: 'custName',
	     width: '13%',
	     align: 'center'
	 }, {
	     title: '经营单位',
	     field: 'busiUnitName',
	     width: '13%',
	     align: 'center'
	 }, {
	     title: '币种',
	     field: 'currencyTypeName',
	     width: '5%',
	     align: 'center'
	 },{
	     title: '对账总金额',
	     field: 'amount',
	     width: '8%',
	     align: 'center',
	     formatter : function(value , row , index) {
	    	 return value.toThounds();
	     }
	 }, {
	     title: '已对账金额',
	     field: 'amountChecked',
	     width: '8%',
	     align: 'center',
	     formatter : function(value , row , index) {
	    	 return value.toThounds();
	     }
	 }, {
	     title: '未对账金额',
	     field: 'amountUnChecked',
	     width: '8%',
	     align: 'center',
	     formatter : function(value , row , index) {
	    	 return value.toThounds();
	     }
	 }, {
	     title: '对账日期区间',
	     field: 'createDateRange',
	     width: '15%',
	     align: 'center'
	 },{
	     title: '操作',
         field: 'opertaList',
         width: '10%',
         align: 'center',
         events: operateEvents
	 }
];

options.initPage = function(){
	
	GLOBAL.initOptionsParam("projectId" , "custId" , "busiUnit" , "currencyType" , "billNo" , "startBillDate" , "endBillDate");
	var option = {
		onLoadSuccess : function(data) {
			if(data.options.totalStr != null){
				$('#js_dataTable tbody').append("<tr><td style='text-align:left;font-weight:bold' colspan='" + tabCols.length + "'>合计：" + data.options.totalStr + "</td></tr>");
			}
		}
	}
	GLOBAL.initTable($('#js_dataTable'), tabCols, null , null , option);
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

