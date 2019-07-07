
/*数据表格*/
var tabCols = [
	 {  
	     title: '序号',
	     field: 'columnsNumber',  
	     width: 10
     },{
	     title: '权限组名',
	     field: 'name',
	     width: 92,
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
	     field: 'stateName',
	     width: 85,
	     align: 'center'
	 } 
];


options.initPage = function() {	
	GLOBAL.initTable($('#js_dataTable'), tabCols, {id: options.param.id}); 
} 

$("#back").click(function() {
	var url = $(this).data("url");
	GLOBAL.go(url);
});