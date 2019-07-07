
window.operateEvents = {
    'click .delete': function (e, value, row, index) {
        var id = row.id;
	    layer.confirm('确定要删除吗？', {
	        btn: ['确定', '取消'] //按钮
	    }, function() {
	    	GLOBAL.ajax("sendManage/delete", {id: id}, function(){
	    		layer.msg("删除成功", {
	                icon: 1
	            })
	            $("#btnSearch").click();
	    	})
	    }, function() {});
    },
    'click .edit': function(e, value, row, index){
    	options.param.id = row.id;
		GLOBAL.go("html/baseinfo/send/sendManageEdit.html", options.param);
    },
    'click .scan': function(e, value, row, index){
		options.param.id = row.id;
		GLOBAL.go("html/baseinfo/send/sendManageScan.html", options.param);
    }
};
/*数据表格*/
var tabCols = [
	 {  
	     title: '序号',
	     field: 'columnsNumber',  
	     width: 10
     }/*,{
	     title: '部门',
	     field: 'departmentName',
	     width: 100,
	     align: 'center'
	 },{
	     title: '项目',
	     field: 'projectName',
	     width: 100,
	     align: 'center'
	 }*/, {
	     title: '用户名',
	     field: 'userName',
	     width: 100,
	     align: 'center'
	 }, {
	     title: '业务类型',
	     field: 'bizSendTypeName',
	     width: 100,
	     align: 'center'
	 },{
	     title: '创建人',
	     field: 'creator',
	     width: 80,
	     align: 'center'
	  }, {
		 title: '创建时间',
		 field: 'createAt',
		 width: 80,
		 align: 'center'
	  }, {
	     title: '操作',
	     field: 'opertaList',
	     width: 100,
	     align: 'center',
	     events: operateEvents
	 }
];

options.initPage = function() {
	$("button[type='reset']").click(function(){
		$("input[type='hidden']").val("");
	})
	GLOBAL.initOptionsParam('id,cid,hip');
	GLOBAL.initTable($('#js_dataTable'), tabCols, null, null , null);
}

$("#btnSearch").click(function() {
	var data = $("#searchForm").serializeObject();
	if(data){
		var url = $('#js_dataTable').data("url");
		GLOBAL.local(url, data);
	}
    GLOBAL.tableRefresh($('#js_dataTable'), data);
})    

$("#btnNew").click(function() {
    var url = $(this).data("url");
    GLOBAL.go(url, options.param);
})

