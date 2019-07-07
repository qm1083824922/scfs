
window.operateEvents = {
    'click .submit': function (e, value, row, index) {
    	var id = row.id;
	    if(id){
	    	layer.confirm('确定要提交吗？', {
		        btn: ['确定', '取消'] //按钮
		    }, function() {
		         GLOBAL.ajax("fee/artificial/submit", {id: id}, function(e) {
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
	    	GLOBAL.ajax("fee/artificial/delete", {id: id}, function(){
	    		layer.msg("删除成功", {
	                icon: 1
	            })
	            $("#btnSearch").click();
	    	})
	    }, function() {});
    },
    'click .edit': function(e, value, row, index){
    	options.param.id = row.id;
		GLOBAL.go("html/fee/artificialFee/artificialFeeEdit.html", options.param);
    },
    'click .scan': function(e, value, row, index){
		options.param.id = row.id;
		options.param.state = row.state;
		GLOBAL.go("html/fee/artificialFee/artificialFeeScan.html", options.param);
    },
    'click .share': function(e, value, row, index){
		options.param.id = row.id;
		GLOBAL.go("html/fee/artificialFee/artificialFeeShare.html", options.param);
    }
    
};
/*数据表格*/
var tabCols = [
	 {
		field : 'checkItem',
		checkbox : true,
		width: '2%'
	},{  
	     title: '序号',
	     field: 'columnsNumber',  
	     width: 10
     },{
	     title: '费用管理编号',
	     field: 'feeManageNo',
	     width: 100,
	     align: 'center'
	 },{
	     title: '部门',
	     field: 'departmentName',
	     width: 100,
	     align: 'center'
	 }, {
	     title: '项目',
	     field: 'projectName',
	     width: 100,
	     align: 'center'
	 }, {
	     title: '人员',
	     field: 'userName',
	     width: 80,
	     align: 'center'
	 }, {
	     title: '客户',
	     field: 'custName',
	     width: 100,
	     align: 'center'
	 }, {
	     title: '管理费用科目',
	     field: 'feeSpecName',
	     width: 100,
	     align: 'center'
	 },{
	     title: '应付方式',
	     field: 'recTypeName',
	     width: 80,
	     align: 'center'
	 },{
	     title: '日期',
	     field: 'date',
	     width: 80,
	     align: 'center'
	 }, {
	     title: '金额',
	     field: 'amount',
	     width: 80,
	     align: 'center',
	 	formatter : function(value , row , index) {
			return value==null ? "": value.toThounds();
	    }
	 },{
	     title: '币种',
	     field: 'currnecyTypeName',
	     width: 80,
	     align: 'center'
	 },{
	     title: '创建人',
	     field: 'creator',
	     width: 80,
	     align: 'center'
	 },{
	     title: '创建时间',
	     field: 'createAt',
	     width: 80,
	     align: 'center'
	 },{
	     title: '状态',
	     field: 'stateName',
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
	$(".js_datePicker").datetimepicker({
        format:'Y-m-d',
    });
	
	
	$("button[type='reset']").click(function(){
		$("input[type='hidden']").val("");
	})
	
	GLOBAL.initOptionsParam('id,cid,hip');
	var option = {
			onLoadSuccess : function(data) {
				if(data.options.totalStr != null){
					$('#js_dataTable tbody').append("<tr><td style='text-align:left;font-weight:bold' colspan='" + tabCols.length + "'>合计：" + data.options.totalStr + "</td></tr>");
				}
			}
		}
	GLOBAL.initTable($('#js_dataTable'), tabCols, null, null , option);
	//还原查询条件及过滤后的数据
	GLOBAL.restoreQuery($('#js_dataTable')); 
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
$("#import").click(function() {
    var url = $(this).data("url");
    GLOBAL.go(url, options.param);
})


$("#btnExport").click(function() {
	var count_url = "/artificial/excel/export/count";
	var data = $("#searchForm").serializeObject();
	GLOBAL.ajax(count_url, data, function(e) {
        if (e.success) {
        	var url = "fee/excel/artificial.xls";
        	url = url + "?" + $("#searchForm").serialize();
        	location.href = GLOBAL.host + url;
        } else {
            layer.msg(e.msg);
        }
    });
}) 


$("#multSubmit").click(function() {
	var ids = GLOBAL.selectIds($('#js_dataTable'));
	if(ids){
		layer.confirm('确定要批量提交吗？', {
			btn : [ '确定', '取消' ]
		//按钮
		}, function() {
			GLOBAL.ajax("fee/manage/submitAll", {
				ids : ids
			}, function(e) {
				if (e.success) {
					layer.msg("提交成功！");
                    $("#btnSearch").click();
				} else {
					layer.msg(e.msg);
				}
			}, false);
		});
	}
})

//合并人工费用自动分摊
$("#artFeeConformityShare").click(function(){
	var ids=[];
	$("#js_dataTable tr").each(function() {
		var cb = $(this).find("input[name='btSelectItem']");
		if (cb.prop("checked")) {
			var id = $(this).data("uniqueid");
			ids.push(id);
		}
	});
	if (ids.length == 0) {
		layer.msg("请选择单据");
		return false;
	}
	options.param.ids = ids;
	var url = $(this).data("url")+ "?ids=" + ids;
	GLOBAL.go(url,options.param);  
});

