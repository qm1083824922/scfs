
window.operateEvents = {
    'click .submit': function (e, value, row, index) {
    	var id = row.id;
	    if(id){
	    	layer.confirm('确定要提交吗？', {
		        btn: ['确定', '取消'] //按钮
		    }, function() {
		         GLOBAL.ajax("project/submit", {id: id}, function(e) {
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
	    	GLOBAL.ajax("project/delete", {id: id}, function(){
	    		layer.msg("删除成功", {
	                icon: 1
	            })
	            $("#btnSearch").click();
	    	})
	    }, function() {});
    },
    'click .lock': function (e, value, row, index) {
        var id = row.id;
	    layer.confirm('确定要锁定吗？', {
	        btn: ['确定', '取消'] //按钮
	    }, function() {
	    	GLOBAL.ajax("project/lock", {id: id}, function(){
	    		layer.msg("锁定成功", {
	                icon: 1
	            })
	            $("#btnSearch").click();
	    	})
	    }, function() {});
    },
    'click .unlock': function (e, value, row, index) {
        var id = row.id;
	    layer.confirm('确定要解锁吗？', {
	        btn: ['确定', '取消'] //按钮
	    }, function() {
	    	GLOBAL.ajax("project/unlock", {id: id}, function(){
	    		layer.msg("解锁成功", {
	                icon: 1
	            })
	            $("#btnSearch").click();
	    	})
	    }, function() {});
    },
    'click .edit': function(e, value, row, index){
		options.param.id = row.id;
		options.param.cid = row.id;
		options.param.bizType = row.bizType;
		GLOBAL.go("html/project/projectBaseInfoEdit.html", options.param);
    },
    'click .scan': function(e, value, row, index){
		options.param.id = row.id;
		options.param.cid = row.id;
		options.param.bizType = row.bizType;
		GLOBAL.go("html/project/projectBaseInfoScan.html", options.param);
    },
    'click .copy': function(e, value, row, index){
		options.param.id = row.id;
		options.param.cid = row.id;
		options.param.bizType = row.bizType;
		options.param.isCopy = true;
		GLOBAL.go("html/project/projectCreate.html", options.param);
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
	     field: 'projectNo',
	     width: 80,
	     align: 'center'
	 },  {
	     title: '简称',
	     field: 'projectName',
	     width: 100,
	     align: 'center'
	 }, {
	     title: '全称',
	     field: 'fullName',
	     width: 120,
	     align: 'center'
	 }, {
	     title: '经营单位',
	     field: 'businessUnitName',
	     width: 120,
	     align: 'center'
	 }, {
	     title: '业务类别',
	     field: 'bizTypeName',
	     width: 80,
	     align: 'center'
	 }, {
	     title: '法务主管',
	     field: 'lawName',
	     width: 80,
	     align: 'center'
	 }, {
	     title: '业务专员',
	     field: 'bizSpecialName',
	     width: 80,
	     align: 'center'
	 }, {
	     title: '商务主管',
	     field: 'businessManagerName',
	     width: 80,
	     align: 'center'
	 }, {
	     title: '财务专员',
	     field: 'financeSpecialName',
	     width: 80,
	     align: 'center'
	 }, {
	     title: '财务主管',
	     field: 'financeManagerName',
	     width: 80,
	     align: 'center'
	 }, {
	     title: '风控主管',
	     field: 'riskManagerName',
	     width: 80,
	     align: 'center'
	 }, {
	     title: '所属部门',
	     field: 'departmentName',
	     width: 80,
	     align: 'center'
	 },  {
	     title: '额度',
	     field: 'totalAmountValue',
	     width: 80,
	     align: 'center'
	 },{
	     title: '创建人',
	     field: 'creator',
	     width: 88,
	     align: 'center'
	 },  {
	     title: '创建时间',
	     field: 'createAt',
	     width: 100,
	     align: 'center'
	 }, {
	     title: '状态',
	     field: 'statusName',
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
	$("button[type='reset']").click(function(){
		$("input[type='hidden']").val("");
	})
	GLOBAL.initOptionsParam('id,cid');
	GLOBAL.initTable($('#js_dataTable'), tabCols, null);
	//还原查询条件及过滤后的数据
	GLOBAL.restoreQuery($('#js_dataTable'));
 };
 
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

$("#btnExport").click(function() {
	var count_url = "/project/excel/export/count";
	var data = $("#searchForm").serializeObject();
	GLOBAL.ajax(count_url, data, function(e) {
        if (e.success) {
        	var url = "project/excel/collect.xls";
        	url = url + "?" + $("#searchForm").serialize();
        	location.href = GLOBAL.host + url;
        } else {
            layer.msg(e.msg);
        }
    });
}) 