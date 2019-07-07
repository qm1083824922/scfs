window.operateEvents = {
    'click .submit': function (e, value, row, index) {
    	var id = row.id;
	    if(id){
	    	layer.confirm('确定要提交吗？', {
		        btn: ['确定', '取消'] //按钮
		    }, function() {
		         GLOBAL.ajax("baseSubject/submit", {id: id}, function(e) {
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
	    	GLOBAL.ajax("baseSubject/delete", {id: id}, function(){
	    		layer.msg("删除成功", {
	                icon: 1
	            })
	            //$("#btnSearch").click();
	            var subjectType = $(".subtype.active").data("subjecttype");
				GLOBAL.tableRefresh($('#js_dataTable'),{subjectType: subjectType});
	    	})
	    }, function() {});
    },
    'click .lock': function (e, value, row, index) {
    	var id = row.id;
    	layer.confirm('确定要锁定吗？', {
    		btn: ['确定', '取消'] //按钮
    	}, function() {
    		GLOBAL.ajax("baseSubject/lock", {id: id}, function(){
    			layer.msg("锁定成功", {
    				icon: 1
    			})
    			//$("#btnSearch").click();
    			$('#js_dataTable').bootstrapTable('refresh');
    		})
    	}, function() {});
    },
    'click .unlock': function (e, value, row, index) {
    	var id = row.id;
    	layer.confirm('确定要解锁吗？', {
    		btn: ['确定', '取消'] //按钮
    	}, function() {
    		GLOBAL.ajax("baseSubject/unlock", {id: id}, function(){
    			layer.msg("解锁成功", {
    				icon: 1
    			})
    			//$("#btnSearch").click();
    			$('#js_dataTable').bootstrapTable('refresh');
    		})
    	}, function() {});
    },
    'click .edit': function(e, value, row, index){
		options.param.id = row.id;
		options.param.cid = row.id;
		GLOBAL.go("html/subject/subjectBaseInfoEdit.html");
    },
    'click .scan': function(e, value, row, index){
		options.param.id = row.id;
		options.param.cid = row.id;
		options.param.subjectType = row.subjectType;
		GLOBAL.go("html/subject/subjectBaseInfoScan.html");
    }
};


/*数据表格*/
var tabCols = [
	{  
	     title: '序号',
	     field: 'columnsNumber',  
	     width: 10
     }, {
	     title: '编号',
	     field: 'subjectNo',
	     width: 92,
	     align: 'center'
	 }, {
	     title: '简称',
	     field: 'abbreviation',
	     width: 85,
	     align: 'center'
	 }, {
	     title: '中文全称',
	     field: 'chineseName',
	     width: 210,
	     align: 'center'
	 }, {
	     title: '英文全称',
	     field: 'englishName',
	     width: 210,
	     align: 'center'
	 }, {
	     title: '类型',
	     field: 'subjectTypeLabel',
	     width: 88,
	     align: 'center'
	 }, {
	     title: '创建人',
	     field: 'creator',
	     width: 88,
	     align: 'center'
	 }, {
	     title: '创建时间',
	     field: 'createAt',
	     width: 88,
	     align: 'center'
	 }, {
	     title: '状态',
	     field: 'stateLabel',
	     width: 88,
	     align: 'center'
	 }, {
	     title: '操作',
         field: 'opertaList',
         width: 160,
         align: 'center',
         events: operateEvents
	 }
];

options.initPage = function(){
	GLOBAL.initOptionsParam('id,cid');
	GLOBAL.initTable($('#js_dataTable'), tabCols, {subjectType: 1});

	//还原查询条件及过滤后的数据
	GLOBAL.restoreQuery($('#js_dataTable'));
 };

$("#btnSearch").click(function() {
	var data = $("#searchForm").serializeObject();
	var url = $('#js_dataTable').data("url");
	GLOBAL.local(url, data);

	var subjectType = $(".subtype.active").data("subjecttype");
	GLOBAL.tableRefresh($('#js_dataTable'), $.extend(data,{subjectType: subjectType}));
})
 

//四个新建
$("#custCreate").click(function() {
	var url = $(this).data("url");
	GLOBAL.go(url);
})

$("#supplierCreate").click(function() {
	var url = $(this).data("url");
	GLOBAL.go(url);
})

$("#warehouseCreate").click(function() {
	var url = $(this).data("url");
	GLOBAL.go(url);
})

$("#busiUnitCreate").click(function() {
	var url = $(this).data("url");
	GLOBAL.go(url);
})

$("#mutiSubjectCreate").click(function() {
	var url = $(this).data("url");
	GLOBAL.go(url);
})

$(".subtype").click(function(){
	$(this).siblings(".active").removeClass("active");
	$(this).addClass("active");
	var subjectType = $(this).data("subjecttype");
	$("#subtype").val(subjectType);
	GLOBAL.tableRefresh($('#js_dataTable'), {subjectType: subjectType});
})
