
window.operateEvents = {
    'click .submit': function (e, value, row, index) {
	    if(row){
			var id = row.id;
	    	layer.confirm('确定要提交吗？', {
		        btn: ['确定', '取消'] //按钮
		    }, function() {
		         GLOBAL.ajax("po/submit", {id: id}, function(e) {
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
        if(row){
			var id = row.id;
		    layer.confirm('确定要删除吗？', {
		        btn: ['确定', '取消'] //按钮
		    }, function() {
		    	GLOBAL.ajax("po/delete", {id: id}, function(){
		    		layer.msg("删除成功", {
		                icon: 1
		            })
		            //$("#btnSearch").click();
		            $('#js_dataTable').bootstrapTable('refresh');
		    	})
		    }, function() {});
		}
    },
    'click .edit': function(e, value, row, index){
		if(row){
			var id = row.id;
			options.param.id = id;
			//options.param.cid = id;
			options.param.projectId = row.projectId;
			GLOBAL.go("html/purchase/poBaseInfoEdit.html");
		}
    },
    'click .scan': function(e, value, row, index){
		if(row){
			var id = row.id;
			options.param.id = id;
			options.param.stateId = row.stateId;
			GLOBAL.go("html/purchase/poBaseInfoScan.html");
		}
    },
    'click .print': function(e, value, row, index){
		if(row){
			var id = row.id;
			window.open("html/purchase/poPrint.html?id="+id);
		}
    },
    'click .receive': function(e, value, row, index){
	    if(row){
			var id = row.id;
	    	layer.confirm('确定要收货吗？', {
		        btn: ['确定', '取消'] //按钮
		    }, function() {
		         GLOBAL.ajax("po/receive", {id: id}, function(e) {
	                if (e.success) {
	                    layer.msg(e.successMsg, {time: 5000, closeBtn:[0,false]});
	                    $("#btnSearch").click();
	                } else {
	                    layer.msg(e.msg);
	                }
	            });
		    }, function() {});
	    }
    },
    'click .flyOrder': function(e, value, row, index){
	    if(row){
			var id = row.id;
	    	layer.confirm('确定要飞单吗？', {
		        btn: ['确定', '取消'] //按钮
		    }, function() {
		         GLOBAL.ajax("po/flyOrder", {id: id}, function(e) {
	                if (e.success) {
	                    layer.msg(e.successMsg, {time: 5000, closeBtn:[0,false]});
	                    $("#btnSearch").click();
	                } else {
	                    layer.msg(e.msg);
	                }
	            });
		    }, function() {});
	    }
    }
};

/*数据表格*/
var tabCols = [
    {
		field : 'checkItem',
		checkbox : true,
	},{  
	     title: '序号',
	     field: 'columnsNumber',  
     }, {
	     title: '订单编号',
	     field: 'orderNo',
	     align: 'center'
	 }, {
	     title: '订单附属编号',
	     field: 'appendNo',
	     align: 'center'
	 }, {
	     title: '经营单位',
	     field: 'businessUnitName',
	     align: 'center'
	 }, {
	     title: '项目',
	     field: 'projectName',
	     align: 'center'
	 }, {
	     title: '供应商',
	     field: 'supplierName',
	     align: 'center'
	 }, {
	     title: '仓库',
	     field: 'warehouseName',
	     align: 'center'
	 }, {
	     title: '客户',
	     field: 'customerName',
	     align: 'center'
	 }, {
	     title: '订单日期',
	     field: 'orderTime',
	     align: 'center'
	 }, {
	     title: '预计到货日期',
	     field: 'perdictTime',
	     align: 'center'
	 }, {
		title: '收货数量',
		field: 'arrivalNum',
		align: 'center',
		formatter : function(value , row , index) {
			return value==null ? "": value.toThounds();
		}
	}, {
		title: '订单数量',
		field: 'orderTotalNum',
		align: 'center',
		formatter : function(value , row , index) {
			return value==null ? "": value.toThounds();
		}
	}, {
		title: '订单金额',
		field: 'orderTotalAmount',
		align: 'right',
		formatter : function(value , row , index) {
			return value==null ? "": value.toThounds();
		}
	}, {
		title: '折扣金额',
		field: 'titleDiscountAmount',
		align: 'right',
		formatter : function(value , row , index) {
			return value==null ? "": value.toThounds();
		}
	}, {
		title: '抵扣金额',
		field: 'ductionMoney',
		align: 'right',
		formatter : function(value , row , index) {
			return value==null ? "": value.toThounds();
		}
	},{
		title: '收票数量',
		field: 'invoiceTotalNum',
		align: 'right',
		formatter : function(value , row , index) {
			return value==null ? "": value.toThounds();
		}
	},{
		title: '收票金额',
		field: 'invoiceTotalAmount',
		align: 'right',
		formatter : function(value , row , index) {
			return value==null ? "": value.toThounds();
		}
	},{
		title: '币种',
		field: 'currencyName',
		align: 'center'
	},{
		title: '是否已飞单',
		field: 'flyOrderFlagName',
		align: 'center'
	},{
		title: '创建人',
		field: 'createUser',
		align: 'center'
	},{
		title: '创建时间',
		field: 'createAt',
		align: 'center'
	}, {
	     title: '状态',
	     field: 'stateName',
	     align: 'center'
	 }, {
	     title: '操作',
         field: 'opertaList',
         align: 'center',
         events: operateEvents
	 }
];


options.initPage = function(){
	GLOBAL.initTable($('#js_dataTable'), tabCols, null, null, {
		onLoadSuccess: function(data){
			if(data.options.totalAmount != null){
				$('#js_dataTable tbody').append("<tr><td style='text-align:left' colspan='" + tabCols.length + "'><b>"+data.options.totalStr+"</b></td></tr>");
			}
		},
	onCheck: function (row,$element) {
        var data = $("#js_dataTable").bootstrapTable("getAllSelections");
		if(data.length == 0){
			layer.msg("请选择采购单");
			return false;
		}
	  for(var i=0 ; i<data.length ;i++){
		   var  checkType=true;
		  var index = $element.data('index');
		  if(row.orderTime  != data[i].orderTime){
			  layer.msg("当前行选择的订单日期不一致");
			  checkType=false;
		  }
		  if(row.projectId !=data[i].projectId){
			  layer.msg("当前行选择的项目不一致");
			  checkType=false;
		  }
		  if(row.supplierId  != data[i].supplierId){
			  layer.msg("当前行选择的供应商不一致");
			  checkType= false;
		  }
		  if(row.currencyId  != data[i].currencyId){
			  layer.msg("当前行选择的币种不一致");
			  checkType= false;
		  }
		  if(checkType == false){
			  $('#js_dataTable').bootstrapTable("uncheck", index);
             return  checkType;
		  }
	  }
	},
 onCheckAll : function(rows){
     var data = $("#js_dataTable").bootstrapTable("getAllSelections");
		if(data.length == 0){
			layer.msg("请选择采购单");
			return false;
		}
	 var row=data[0];// 取第一条数据
	 for(var i=0; i<data.length;  i++){
		  var  checkType=true;
		  if(row.orderTime  != data[i].orderTime){
			  layer.msg("当前行选择的订单日期不一致");
			  checkType=false;
		  }
		  if(row.projectId !=data[i].projectId){
			  layer.msg("当前行选择的项目不一致");
			  checkType=false;
		  }
		  if(row.supplierId  != data[i].supplierId){
			  layer.msg("当前行选择的供应商不一致");
			  checkType= false;
		  }
		  if(row.currencyId  != data[i].currencyId){
			  layer.msg("当前行选择的币种不一致");
			  checkType= false;
		  }
	 }
	if(checkType == false){
		  $('#js_dataTable').bootstrapTable("uncheckAll", rows);
	         return  checkType;
	}
 },
	});
	//还原查询条件及过滤后的数据
	GLOBAL.restoreQuery($('#js_dataTable'));
	$(".js_datePicker").datetimepicker({
        format:'Y-m-d',
    });
}

function getCondition() {
	return "?projectId=" + $("#js_project").val() 
	+ "&supplierId=" + $("#js_supplier").val()
	+ "&orderNo=" + $("#orderNo").val()
	+ "&billNo=" + $("#billNo").val()
	+ "&businessUnitId=" + $("#js_busiUnit").val()
	+ "&appendNo=" + $("#appendNo").val()
	+ "&state=" + $("#state").val()
	+ "&startOrderTime=" + $("#js_form_datetimeStart").val()
	+ "&endOrderTime=" + $("#js_form_datetimeEnd").val()
	+ "&startPerdictTime=" + $("#js_form_perdictTimeStart").val()
	+ "&endPerdictTime=" + $("#js_form_perdictTimeEnd").val()
	+ "&warehouseId=" + $("#js_warehouse").val()
	+ "&customerId=" + $("#js_customer").val();
}


$("#btnExport").click(function() {
    var count_url = "poOrder/excel/poOrder/count";
    var data = $("#searchForm").serializeObject();
    GLOBAL.ajax(count_url, data, function(e) {
        if (e.success) {
            var url = "poOrder/excel/poOrder.xls";
            url = url + getCondition();
            location.href = GLOBAL.host + url;
        } else {
            layer.msg(e.msg);
        }
    });


})

$("#btnExportDtl").click(function() {
    var count_url = "poOrderDtl/excel/poOrderDtl/count";
    var data = $("#searchForm").serializeObject();
    GLOBAL.ajax(count_url, data, function(e) {
        if (e.success) {
            var url = "poOrderDtl/excel/poOrderDtl.xls";
            url = url + getCondition();
            location.href = GLOBAL.host + url;
        } else {
            layer.msg(e.msg);
        }
    });
})

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


//合同列表打印
$("#searchPrint").click(function() {
	var ids=[];
	$("#js_dataTable tr").each(function() {
		var cb = $(this).find("input[name='btSelectItem']");
		if (cb.prop("checked")) {
			var id = $(this).data("uniqueid");
			ids.push(id);
		}
	});
	if (ids.length == 0) {
		layer.msg("请选择采购单");
		return false;
	}
	options.param.ids = ids;
	var url = $(this).data("url")+ "?ids=" + ids;
    window.open(url);  
});

//发票列表打印
$("#invoicePrint").click(function(){
	var ids=[];
	$("#js_dataTable tr").each(function() {
		var cb = $(this).find("input[name='btSelectItem']");
		if (cb.prop("checked")) {
			var id = $(this).data("uniqueid");
			ids.push(id);
		}
	});
	if (ids.length == 0) {
		layer.msg("请选择采购单");
		return false;
	}
	options.param.ids = ids;
	var url = $(this).data("url")+ "?ids=" + ids;
    window.open(url);  
});

//对账列表打印
$("#balanceOfAccountPrint").click(function(){
	var ids=[];
	$("#js_dataTable tr").each(function() {
		var cb = $(this).find("input[name='btSelectItem']");
		if (cb.prop("checked")) {
			var id = $(this).data("uniqueid");
			ids.push(id);
		}
	});
	if (ids.length == 0) {
		layer.msg("请选择采购单");
		return false;
	}
	options.param.ids = ids;
	var url = $(this).data("url")+ "?ids=" + ids;
    window.open(url);  
});

//送货列表打印
$("#deliverGoodsPrint").click(function(){
	var ids=[];
	$("#js_dataTable tr").each(function() {
		var cb = $(this).find("input[name='btSelectItem']");
		if (cb.prop("checked")) {
			var id = $(this).data("uniqueid");
			ids.push(id);
		}
	});
	if (ids.length == 0) {
		layer.msg("请选择采购单");
		return false;
	}
	options.param.ids = ids;
	var url = $(this).data("url")+ "?ids=" + ids;
    window.open(url);  
});


$("#mergePackPrint").click(function(){
	var ids=[];
	$("#js_dataTable tr").each(function() {
		var cb = $(this).find("input[name='btSelectItem']");
		if (cb.prop("checked")) {
			var id = $(this).data("uniqueid");
			ids.push(id);
		}
	});
	if (ids.length == 0) {
		layer.msg("请选择采购单");
		return false;
	}
	options.param.ids = ids;
	var url = $(this).data("url")+ "?ids=" + ids;
    window.open(url);  
});




