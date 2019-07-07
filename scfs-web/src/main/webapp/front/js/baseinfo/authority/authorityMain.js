
window.operateEvents = {
    'click .submit': function (e, value, row, index) {
    	var id = row.id;
	    if(id){
	    	layer.confirm('确定要提交吗？', {
		        btn: ['确定', '取消'] //按钮
		    }, function() {
		         GLOBAL.ajax("permission/submit", {id: id}, function(e) {
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
	    	GLOBAL.ajax("permission/delete", {id: id}, function(){
	    		layer.msg("删除成功", {
	                icon: 1
	            })
	            $("#btnSearch").click();
	    	})
	    }, function() {});
    },
    'click .edit': function(e, value, row, index){
		options.param.id = row.id;
		GLOBAL.go("html/baseinfo/authority/authorityEdit.html", options.param);
    },
    'click .scan': function(e, value, row, index){
		options.param.id = row.id;
		GLOBAL.go("html/baseinfo/authority/authorityScan.html", options.param);
    },
    'click .lock': function(e, value, row, index){
    	var id = row.id;
 	    layer.confirm('确定要锁定吗？', {
 	        btn: ['确定', '取消'] //按钮
 	    }, function() {
 	    	GLOBAL.ajax("permission/lock", {id: id}, function(){
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
 	    	GLOBAL.ajax("permission/unlock", {id: id}, function(){
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
	     title: '权限名',
	     field: 'name',
	     width: 92,
	     align: 'center'
	 }, {
	     title: 'URL',
	     field: 'url',
	     width: 85,
	     align: 'center'
	 }, {
	     title: '排序',
	     field: 'ord',
	     width: 80,
	     align: 'center'
	 }, {
	     title: '权限类型',
	     field: 'typeName',
	     width: 100,
	     align: 'center'
	 }, {
	     title: '菜单级别',
	     field: 'menuLevelName',
	     width: 100,
	     align: 'center'
	 }, {
	     title: '所属一级菜单',
	     field: 'parentName',
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
	     field: 'state',
	     width: 100,
	     align: 'center'
	 }, {
	     title: '操作',
	     field: 'opertaList',
	     width: 100,
	     align: 'center',
	     events: operateEvents
	 }
];


var options = {
    param: {
      id: 7,
      cid:7,
    },
    initPage: function(){
    	GLOBAL.initTable($('#js_dataTable'), tabCols, null);

    	//还原查询条件及过滤后的数据
		GLOBAL.restoreQuery($('#js_dataTable'));


    	 var options = "<option value=''>请选择</option>";
         GLOBAL.ajax("first/menu/query", null, function(data){
             for(var k = 0; k < data.length; k++){
                 options += '<option value="'+data[k].id+'">'+data[k].name+'</option>';
             }   
             $("#parentId").empty().append(options);
         })
    }
};

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

$("#btnImport").click(function() {
	   var url = $(this).data("url");
	   GLOBAL.go(url, options.param);
})