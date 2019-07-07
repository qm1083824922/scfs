
window.operateEvents = {
		'click .download': function(e, value, row, index){
			var url = GLOBAL.host + "projectItemFile/downLoad?fileId="+row.id;
			GLOBAL.preview({type: row.type, url: url});
	    },
    
};
function operateFormatter(value, row, index) {
    return [
            '<a class="download" href="javascript:void(0)">',
            '<span class="btn btn-primary btn-sm">下载</span>',
            '</a>  '
        ].join('');
}
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
	     title: '文件名称',
	     field: 'name',
	     width: 92,
	     align: 'center'
	 },  {
	     title: '文件类型',
	     field: 'type',
	     width: 80,
	     align: 'center'
	 },  {
	     title: '上传人',
	     field: 'creator',
	     width: 80,
	     align: 'center'
	 },  {
	     title: '上传时间',
	     field: 'createAt',
	     width: 80,
	     align: 'center'
	 }, {
	     title: '操作',
	     field: 'operta',
	     width: 100,
	     align: 'center',
	     events: operateEvents,
         formatter: operateFormatter
	 }
];

options.initPage = function() {
	
	var dataTable = GLOBAL.initTable($('#js_dataTable'), tabCols, {busId: options.param.cid});
}
 
$("#back").click(function() {
    var url = $(this).data("url");
    GLOBAL.go(url, options.param);
}) 

$("#upload").upload({
 	url: GLOBAL.host + "projectItemFile/upload",
 	param: {
 		busId: options.param.cid
 	},
	success: function(){
		GLOBAL.tableRefresh($('#js_dataTable'),{busId: options.param.cid});
    }
 });

$("#download").click(function(){
	var ids =  GLOBAL.selectIds($('#js_dataTable'));
	if(ids){
		var url = GLOBAL.host + "projectItemFileList/downLoad?ids="+ids;
		window.open(url); 
	}
 }); 
$(".tab-item").click(function(){
    var url = $(this).data("url");
    GLOBAL.go(url, options.param);
}) 
