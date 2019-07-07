
window.operateEvents = {
    'click .submit': function (e, value, row, index) {
    	var id = row.id;
	    if(id){
	    	layer.confirm('确定要提交吗？', {
		        btn: ['确定', '取消'] //按钮
		    }, function() {
		         GLOBAL.ajax("user/submit", {id: id}, function(e) {
	                if (e.success) {
	                    layer.msg("提交成功！");
	                    $("#btnSearch").click();
	                } else {
	                    layer.msg(e.msg);
	                }
	            });
		    }, function() {});
	    }
    },
    'click .delete': function (e, value, row, index) {
        var id = row.id;
	    layer.confirm('确定要删除吗？', {
	        btn: ['确定', '取消'] //按钮
	    }, function() {
	    	GLOBAL.ajax("user/delete", {id: id}, function(){
	    		layer.msg("删除成功", {
	                icon: 1
	            })
	            $("#btnSearch").click();
	    	})
	    }, function() {});
    },
    'click .reset': function (e, value, row, index) {
        var id = row.id;
	    layer.confirm('确定要重置吗？', {
	        btn: ['确定', '取消'] //按钮
	    }, function() {
	    	GLOBAL.ajax("user/reset", {id: id}, function(){
	    		layer.msg("重置成功", {
	                icon: 1
	            })
	            $("#btnSearch").click();
	    	})
	    }, function() {});
    },
    'click .edit': function(e, value, row, index){
    	options.param.id = row.id;
		options.param.cid = row.id;
		GLOBAL.go("html/baseinfo/userinfo/userInfoEdit.html", options.param);
    },
    'click .scan': function(e, value, row, index){
		options.param.id = row.id;
		GLOBAL.go("html/baseinfo/userinfo/userInfoScan.html", options.param);
    },
    'click .lock': function(e, value, row, index){
    	var id = row.id;
 	    layer.confirm('确定要锁定吗？', {
 	        btn: ['确定', '取消'] //按钮
 	    }, function() {
 	    	GLOBAL.ajax("user/lock", {id: id}, function(){
 	    		layer.msg("锁定成功", {
 	                icon: 1
 	            })
 	            $("#btnSearch").click();
 	    	})
 	    }, function() {});
    },
    'click .unlock': function(e, value, row, index){
    	var id = row.id;
 	    layer.confirm('确定要解锁吗？', {
 	        btn: ['确定', '取消'] //按钮
 	    }, function() {
 	    	GLOBAL.ajax("user/unlock", {id: id}, function(){
 	    		layer.msg("解锁成功", {
 	                icon: 1
 	            })
 	            $("#btnSearch").click();
 	    	})
 	    }, function() {});
    }
};
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
	 },{
	     title: '角色',
	     field: 'roleName',
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
	 }, {
	     title: '操作',
	     field: 'opertaList',
	     width: 160,
	     align: 'center',
	     events: operateEvents
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
	var url = $('#js_dataTable').data("url");
	GLOBAL.local(url, data);
	
    GLOBAL.tableRefresh($('#js_dataTable'), data);
})    

$("#btnNew").click(function() {
    var url = $(this).data("url");
    GLOBAL.go(url, options.param);
})