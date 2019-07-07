
window.operateEvents = {
    'click .submit': function (e, value, row, index) {
    	var id = row.id;
	    if(id){
	    	layer.confirm('确定要提交吗？', {
		        btn: ['确定', '取消'] //按钮
		    }, function() {
		         GLOBAL.ajax("projectPoolAdjust/submit", {id: id}, function(e) {
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
	    	GLOBAL.ajax("projectPoolAdjust/delete", {id: id}, function(){
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
	    	GLOBAL.ajax("projectPoolAdjust/lock", {id: id}, function(){
	    		layer.msg("锁定成功", {
	                icon: 1
	            })
	            $("#btnSearch").click();
	    	})
	    }, function() {});
    },
    'click .edit': function(e, value, row, index){
		options.param.id = row.id;
		options.param.cid = row.id;
		GLOBAL.go("html/project/pool/projectPoolAdjustEdit.html", options.param);
    },
    'click .scan': function(e, value, row, index){
		options.param.id = row.id;
		options.param.cid = row.id;
		GLOBAL.go("html/project/pool/projectPoolAdjustScan.html", options.param);
    }
};

/*数据表格*/
var tabCols = [
	 {  
	     title: '序号',
	     field: 'columnsNumber',  
	     width: 10
     }, {
	     title: '调整编号',
	     field: 'adjustNo',
	     width: 100,
	     align: 'center'
	 },  {
	     title: '项目',
	     field: 'projectName',
	     width: 100,
	     align: 'center'
	 }, {
	     title: '当前总额度',
	     field: 'projectAmount',
	     width: 80,
	     align: 'center'
	 }, {
	     title: '当前可用额度',
	     field: 'remainFundAmount',
	     width: 80,
	     align: 'center'
	 }, {
	     title: '临时调整额度',
	     field: 'adjustAmount',
	     width: 80,
	     align: 'center'
	 }, {
	     title: '币种',
	     field: 'currencyTypeName',
	     width: 50,
	     align: 'center'
	 }, {
	     title: '效期',
	     field: 'validDateString',
	     width: 120,
	     align: 'center'
	 }, {
	     title: '申请人',
	     field: 'creator',
	     width: 80,
	     align: 'center'
	 }, {
	     title: '申请时间',
	     field: 'createAt',
	     width: 120,
	     align: 'center'
	 },{
	     title: '状态',
	     field: 'stateName',
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
	GLOBAL.initTable($('#js_dataTable'), tabCols, null);
	
	$(".js_datePicker").datetimepicker({
		format:'Y-m-d'
	})
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

