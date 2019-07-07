
/*数据表格*/
var tabCols = [
	 {  
	     title: '序号',
	     field: 'columnsNumber',  
	     width: 10
     },{
	     title: '工号',
	     field: 'employeeNumber',
	     width: 80,
	     align: 'center'
	 }, {
	     title: '用户名',
	     field: 'userName',
	     width: 75,
	     align: 'center'
	 }, {
	     title: '中文名',
	     field: 'chineseName',
	     width: 80,
	     align: 'center'
	 }, {
	     title: '英文名',
	     field: 'englishName',
	     width: 100,
	     align: 'center'
	 }, {
	     title: '手机',
	     field: 'mobilePhone',
	     width: 100,
	     align: 'center'
	 }, {
	     title: '邮箱',
	     field: 'email',
	     width: 80,
	     align: 'center'
	 }, {
	     title: '用户类别',
	     field: 'userPropertyValue',
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
	     field: 'status',
	     width: 100,
	     align: 'center'
	 } 
];
 options.initPage = function(){
	GLOBAL.initTable($('#js_dataTable'), tabCols, {permissionGroupId: options.param.id});
  };

$("#back").click(function(){
    var url = $(this).data("url");
    options.param.id=options.param.cid;
    GLOBAL.go(url, options.param);
}) 
 