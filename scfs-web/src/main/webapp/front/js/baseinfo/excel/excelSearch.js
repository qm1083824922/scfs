
window.operateEvents = {
		'click .download': function(e, value, row, index){
			var url = GLOBAL.host + "excelList/downLoad?fileId="+row.id;
			window.open(url); 
		}
};/*数据表格*/
var tabCols = [
	 {  
	     title: '序号',
	     field: 'columnsNumber',  
	     width: 10
     },{
	     title: '名称',
	     field: 'name',
	     width: 140,
	     align: 'center'
	 }, {
	     title: '类型',
	     field: 'poTypeName',
	     width: 80,
	     align: 'center'
	 }, {
	     title: '导出人',
	     field: 'creator',
	     width: 100,
	     align: 'center'
	 }, {
	     title: '导出时间',
	     field: 'createAt',
	     width: 100,
	     align: 'center'
	 },  {
	     title: '操作',
	     field: 'opertaList',
	     width: 60,
	     align: 'center',
	     events: operateEvents
	 }, {
        title: '导出条件',
        field: 'arg1',
        width: 80,
        align: 'center'
    }
];

options.initPage = function() {
	
	GLOBAL.initOptionsParam('id,cid,hip');
	GLOBAL.initTable($('#js_dataTable'), tabCols, null); 
	
	//还原查询条件及过滤后的数据
	GLOBAL.restoreQuery($('#js_dataTable'));
}

$("#btnSearch").click(function() {
	var data = $("#searchForm").serializeObject();
	if(data){
		console.log("商品查询参数：", data)
		var url = $('#js_dataTable').data("url");
		GLOBAL.local(url, data);
	}
    GLOBAL.tableRefresh($('#js_dataTable'), data);
}) 
    
$("#btnNew").click(function() {
    var url = $(this).data("url");
    GLOBAL.go(url, options.param);
}) 



