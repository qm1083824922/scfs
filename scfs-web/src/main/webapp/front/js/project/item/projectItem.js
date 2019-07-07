
window.operateEvents = {
    'click .submit': function (e, value, row, index) {
    	var id = row.id;
	    if(id){
	    	layer.confirm('确定要提交吗？', {
		        btn: ['确定', '取消'] //按钮
		    }, function() {
		         GLOBAL.ajax("projectItem/submit", {id: id}, function(e) {
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
	    	GLOBAL.ajax("projectItem/delete", {id: id}, function(){
	    		layer.msg("删除成功", {
	                icon: 1
	            })
	            $("#btnSearch").click();
	    	})
	    }, function() {});
    },
    'click .edit': function(e, value, row, index){
    	options.param.id = row.id;
		options.param.cid = row.id;
		GLOBAL.go("html/project/item/projectItemEdit.html", options.param);
    },
    'click .scan': function(e, value, row, index){
		options.param.id = row.id;
		options.param.cid = row.id;
		GLOBAL.go("html/project/item/projectItemScan.html", options.param);
    },
    'click .lock': function(e, value, row, index){
    	var id = row.id;
 	    layer.confirm('确定要锁定吗？', {
 	        btn: ['确定', '取消'] //按钮
 	    }, function() {
 	    	GLOBAL.ajax("projectItem/lock", {id: id}, function(){
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
 	    	GLOBAL.ajax("projectItem/unlock", {id: id}, function(){
 	    		layer.msg("解锁成功", {
 	                icon: 1
 	            })
 	            $("#btnSearch").click();
 	    	})
 	    }, function() {});
    },
    'click .copy': function(e, value, row, index){
    	options.param.id = row.id;
		options.param.cid = row.id;
		options.param.isCopy = true;
		GLOBAL.go("html/project/item/newBuildProjectItem.html", options.param);
    },
};
/*数据表格*/
var tabCols = [
	 {  
	     title: '序号',
	     field: 'columnsNumber',  
	     width: 10
     },{
	     title: '编号',
	     field: 'itemNo',
	     width: 92,
	     align: 'center'
	 }, {
	     title: '经营单位',
	     field: 'businessUnitName',
	     width: 100,
	     align: 'center'
	 }, {
	     title: '项目',
	     field: 'projectName',
	     width: 120,
	     align: 'center'
	 },{
	     title: '业务类别',
	     field: 'bizType',
	     width: 80,
	     align: 'center'
	 },{
	     title: '结算类型',
	     field: 'isFundAccount',
	     width: 80,
	     align: 'center'
	 },{
	     title: '额度',
	     field: 'amount',
	     width: 100,
	     align: 'center'
	 }, {
	     title: '效期',
	     field: 'dateStr',
	     width: 150,
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
	     width: 100,
	     align: 'center',
	     events: operateEvents
	 }
];

options.initPage = function() {
	
	GLOBAL.initOptionsParam('id,cid,hip');
	var dataTable = GLOBAL.initTable($('#js_dataTable'), tabCols,null,null,{
		onLoadSuccess: function(data){
			if(data.options.totalAmount != null){ 
				$('#js_dataTable tbody').append("<tr><td style='text-align:left' colspan='" + tabCols.length + "'><b>合计：条款总金额："+data.options.totalAmount+"&nbsp;"+data.options.currency
						+ "</b></td></tr>");		
			}
		}
	});

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