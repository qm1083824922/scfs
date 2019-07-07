/*数据表格*/
var tabCols = [
                {  
          	     title: '序号',
          	     field: 'columnsNumber',  
          	     width: 10
               },{
          	     title: '角色',
          	     field: 'name',
          	     width: 92,
          	     align: 'center'
          	 }, {
          	     title: '创建人',
          	     field: 'creator',
          	     width: 85,
          	     align: 'center'
          	 }, {
          	     title: '创建时间',
          	     field: 'createDate',
          	     width: 80,
          	     align: 'center'
          	 },{
          	     title: '状态',
          	     field: 'stateName',
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