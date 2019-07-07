/*数据表格*/
var tabCols = [
	 {  
	     title: '序号',
	     field: 'columnsNumber',  
	     width: 10
     },{
	     title: '经营单位',
	     field: 'businessUnit',
	     width: 92,
	     align: 'center'
	 }, {
	     title: '已分配项目',
	     field: 'projectName',
	     width: 85,
	     align: 'center'
	 }, {
	     title: '分配人',
	     field: 'assigner',
	     width: 80,
	     align: 'center'
	 }, {
	     title: '分配时间',
	     field: 'assignAt',
	     width: 100,
	     align: 'center'
	 }, {
	     title: '作废人',
	     field: 'deleter',
	     width: 100,
	     align: 'center'
	 }, {
	     title: '作废时间',
	     field: 'deleteAt',
	     width: 100,
	     align: 'center'
	 }, {
	     title: '状态',
	     field: 'state',
	     width: 100,
	     align: 'center'
	 }
];


options.initPage = function(){
	GLOBAL.initTable($('#js_dataTable'), tabCols, {userId: options.param.id});
  };

$(".tab-item").click(function(){
    var url = $(this).data("url");
    GLOBAL.go(url, options.param);
})

GLOBAL.goBack();

$("#back").click(function(){
    var url = $(this).data("url");
    options.param.id=options.param.cid;
    GLOBAL.go(url, options.param);
}) 