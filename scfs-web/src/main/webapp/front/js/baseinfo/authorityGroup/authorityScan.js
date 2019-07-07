
/*数据表格*/
var tabCols = [
	  {  
	     title: '序号',
	     field: 'columnsNumber',  
	     width: 10
     },{
	     title: '权限名',
	     field: 'name',
	     width: 92,
	     align: 'center'
	 }, {
	     title: 'URL',
	     field: 'url',
	     width: 85,
	     align: 'center'
	 },{
	     title: '权限类型',
	     field: 'typeName',
	     width: 92,
	     align: 'center'
	 }, {
	     title: '菜单级别',
	     field: 'menuLevelName',
	     width: 85,
	     align: 'center'
	 },{
	     title: '所属一级菜单',
	     field: 'parentName',
	     width: 85,
	     align: 'center'
	 } ,{
	     title: '状态',
	     field: 'state',
	     width: 85,
	     align: 'center'
	 }
];

options.initPage = function() {
	GLOBAL.initTable($('#js_dataTable'), tabCols, {permissionGroupId: options.param.id});
}   
 
$("#back").click(function() {
    var url = $(this).data("url");
    GLOBAL.go(url, options.param);
}) 