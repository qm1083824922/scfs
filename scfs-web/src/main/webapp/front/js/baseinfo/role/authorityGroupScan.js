
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
          	     title: '分配时间',
          	     field: 'createDate',
          	     width: 85,
          	     align: 'center'
          	 }, {
          	     title: '分配人',
          	     field: 'creator',
          	     width: 80,
          	     align: 'center'
          	 }, {
          	     title: '作废时间',
          	     field: 'deleteDate',
          	     width: 100,
          	     align: 'center'
          	 }, {
          	     title: '作废人',
          	     field: 'deleter',
          	     width: 80,
          	     align: 'center'
          	 }, {
          	     title: '状态',
          	     field: 'stateName',
          	     width: 100,
          	     align: 'center'
          	 }
];


options.initPage = function(){
	GLOBAL.initTable($('#js_dataTable'), tabCols, {roleId: options.param.id});
  };

$("#back").click(function(){
    var url = $(this).data("url");
    options.param.id=options.param.cid;
    GLOBAL.go(url, options.param);
})
