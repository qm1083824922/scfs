
window.operateEvents = {
    'click .delete': function (e, value, row, index) {
        var id = row.id; 
	    layer.confirm('确定要删除吗？', {
	        btn: ['确定', '取消'] //按钮
	    }, function() {
	    	GLOBAL.ajax("projectItemFile/delete", {fileId: id}, function(){
	    		layer.msg("删除成功", {
	                icon: 1
	            })
	            GLOBAL.tableRefresh($('#js_dataTable'),{busId: options.param.cid});
	    	})
	    }, function() {});
    }, 
    'click .download': function(e, value, row, index){
		var url = GLOBAL.host + "projectItemFile/downLoad?fileId="+row.id;
  		GLOBAL.preview({type: row.type, url: url});
	}
    
};
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
	 },{
	     title: '操作',
	     field: 'opertaList',
	     width: 100,
	     align: 'center',
	     events: operateEvents
	 }
];

options.initPage = function() {
	
	var dataTable = GLOBAL.initTable($('#js_dataTable'), tabCols, {busId: options.param.cid});
}
 
$("#back").click(function() {
    var url = $(this).data("url");
    GLOBAL.go(url, options.param);
})

$("#add").click(function() {
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
