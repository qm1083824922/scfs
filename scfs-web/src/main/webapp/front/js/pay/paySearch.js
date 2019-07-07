
window.operateEvents = {
    'click .submit': function (e, value, row, index) {
    	var id = row.id;
	    if(id){
	    	layer.confirm('确定要提交吗？', {
		        btn: ['确定', '取消'] //按钮
		    }, function() {
		         GLOBAL.ajax("payOrder/submit", {id: id}, function(e) {
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
	    	GLOBAL.ajax("payOrder/delete", {id: id}, function(){
	    		layer.msg("删除成功", {
	                icon: 1
	            })
	            $('#js_dataTable').bootstrapTable('refresh');
	    	})
	    }, function() {});
    },
    'click .edit': function(e, value, row, index){
		options.param.id = row.id;
		options.param.payType = row.payType;
		options.param.payNo = row.payNo;
		options.param.stateInt = row.stateInt;
		options.param.payWayType = row.payWayType;
		options.param.noneOrderFlag = row.noneOrderFlag;
		GLOBAL.go("html/pay/payBaseInfoEdit.html");
    },
    'click .scan': function(e, value, row, index){
		options.param.id = row.id;
		options.param.payType = row.payType;
		options.param.payNo = row.payNo;
		options.param.stateInt = row.stateInt;
		options.param.payWayType = row.payWayType;
		options.param.noneOrderFlag = row.noneOrderFlag;
		GLOBAL.go("html/pay/payBaseInfoScan.html");
    },
    'click .check': function(e, value, row, index){
    	options.param.id = row.id;
		options.param.payType = row.payType;
		options.param.payNo = row.payNo;
		options.param.stateInt = row.stateInt;
		options.param.payWayType = row.payWayType;
		options.param.noneOrderFlag = row.noneOrderFlag;
		GLOBAL.go("html/pay/payBaseInfoEdit.html");
    },
    'click .print': function(e, value, row, index){
		if(row.printNum > 0){
			layer.confirm("【" + row.payNo + "】已经打印过 " + row.printNum + "次，是否继续打印？",function(index) {
				var id = row.id;
				window.open("html/pay/payPrint.html?id="+id);
				layer.close(index);
			});
		} else {
			var id = row.id;
			window.open("html/pay/payPrint.html?id="+id);
		}
    }
};

/*数据表格*/
var tabCols = [
	{  
	     title: '序号',
	     field: 'columnsNumber',  
	     width: 10
     }, {
	     title: '付款编号',
	     field: 'payNo',
	     width: 92,
	     align: 'center'
     }, {
	     title: '附属编号',
	     field: 'attachedNumbe',
	     width: 92,
	     align: 'center'
     }, {
	     title: '经营单位',
	     field: 'busiUnit',
	     width: 180,
	     align: 'center'
	 }, {
	     title: '项目',
	     field: 'projectName',
	     width: 180,
	     align: 'center'
	 }, {
	     title: '业务类别',
	     field: 'bizTypeName',
	     width: 80,
	     align: 'center'
	 }, {
	     title: '付款类型',
	     field: 'payTypeName',
	     width: 85,
	     align: 'center'
	 }, {
	     title: '付款方式',
	     field: 'payWayName',
	     width: 40,
	     align: 'center'
	 }, {
	     title: '支付类型',
	     field: 'payWayTypeName',
	     width: 40,
	     align: 'center'
	 }, {
	     title: '币种',
	     field: 'currnecyTypeName',
	     width: 30,
	     align: 'center'
	 }, {
	     title: '付款金额',
	     field: 'payAmount',
	     width: 40,
	     align: 'center',
	 	 formatter : function(value , row , index) {
			return value==null ? "": value.toThounds();
	     }
	 }, {
	     title: '汇率',
	     field: 'payRate',
	     width: 30,
	     align: 'center'
	 }, {
	     title: '实际付款币种',
	     field: 'realCurrencyTypeName',
	     width: 30,
	     align: 'center'
	 }, {
	     title: '实际付款金额',
	     field: 'realPayAmount',
	     width: 40,
	     align: 'center',
	     formatter : function(value , row , index) {
			return value==null ? "": value.toThounds();
	     }
	 },  {
	     title: '收款单位',
	     field: 'payeeName',
	     width: 76,
	     align: 'center'
	 }, {
	     title: '要求付款日期',
	     field: 'requestPayTime',
	     width: 86,
	     align: 'center'
	 }, {
	     title: '预计内部打款日期',
	     field: 'innerPayDate',
	     width: 60,
	     align: 'center'
	 },{
	     title: '水单日期',
	     field: 'memoTime',
	     width: 60,
	     align: 'center'
	 }, {
	     title: '确认日期',
	     field: 'confirmorAt',
	     width: 60,
	     align: 'center'
	 }, {
	     title: '确认人',
	     field: 'confirmor',
	     width: 72,
	     align: 'center'
	 }, {
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
	     title: '状态',
	     field: 'state',
	     width: 88,
	     align: 'center'
	 }, {
	     title: '合并付款编号',
	     field: 'mergePayNo',
	     width: 88,
	     align: 'center'
	 }, {
	     title: '是否无单据付款',
	     field: 'noneOrderFlagName',
	     width: 90,
	     align: 'center'
	 }, {
	     title: '可核销金额',
	     field: 'leftCheckAmount',
	     width: 90,
	     align: 'center',
	     formatter : function(value , row , index) {
	    	 return value==null ? "": value.toThounds();
		 }
	 }, {
	     title: '操作',
         field: 'opertaList',
         width: 160,
         align: 'center',
         events: operateEvents
	 }
];


options.initPage = function(){
	GLOBAL.initOptionsParam('id,cid,hip');
	var sumPayAmount = 0;
	var option = {
			sumPayAmount : 0,
			onLoadSuccess : function(data) {
				if(data.options.totalStr != null){
					$('#js_dataTable tbody').append("<tr><td style='text-align:left;font-weight:bold' colspan='" + tabCols.length + "'>合计：" + data.options.totalStr + "</td></tr>");
				}
			},
			onCheck: function(row , element){
				sumPayAmount = sumPayAmount.add(row.payAmount);
				$('#js_dataTable tbody').find("#sumPayAmountTr").remove();
				$('#js_dataTable tbody').append("<tr id='sumPayAmountTr'><td style='text-align:left;font-weight:bold' colspan='" + tabCols.length + "'>付款打印总金额：<span id='sumPayAmount'>" + sumPayAmount + "</span></td></tr>");
		    },
		    onUncheck: function(row) {
			    sumPayAmount = sumPayAmount.subtract(row.payAmount);
			    $('#js_dataTable tbody').find("#sumPayAmountTr").remove();
			    var selections = $('#js_dataTable').bootstrapTable('getSelections');
			    if (selections.length > 0) {
				    $('#js_dataTable tbody').append("<tr id='sumPayAmountTr'><td style='text-align:left;font-weight:bold' colspan='" + tabCols.length + "'>付款打印总金额：<span id='sumPayAmount'>" + sumPayAmount + "</span></td></tr>");
			    }
		    },
		    onCheckAll: function(rows) {
		    	sumPayAmount = 0;
		    	for (var index=0 ; index < rows.length; index++) {
		    		sumPayAmount = sumPayAmount.add(rows[index].payAmount);
		    	}
		    	$('#js_dataTable tbody').find("#sumPayAmountTr").remove();
				$('#js_dataTable tbody').append("<tr id='sumPayAmountTr'><td style='text-align:left;font-weight:bold' colspan='" + tabCols.length + "'>付款打印总金额：<span id='sumPayAmount'>" + sumPayAmount + "</span></td></tr>");
		    },
		    onUncheckAll: function(rows) {
		    	sumPayAmount = 0;
		    	$('#js_dataTable tbody').find("#sumPayAmountTr").remove();
		    }
		}
	GLOBAL.initTable($('#js_dataTable'), tabCols, null, null , option);
	//还原查询条件及过滤后的数据
	GLOBAL.restoreQuery($('#js_dataTable')); 
	
	$(".js_datePicker").datetimepicker({
        format:'Y-m-d',
    });
 };

 $("#btnSearch").click(function() {
	var data = $("#searchForm").serializeObject();
	if(data){
		var url = $('#js_dataTable').data("url");
		GLOBAL.local(url, data);
	}
	GLOBAL.tableRefresh($('#js_dataTable'), data);
})
 $("#btnRefresh").click(function() {
	var data = $("#searchForm").serializeObject();
	if(data){
		GLOBAL.ajax("payOrder/refresh", data,function(){
			layer.msg("刷新成功", {
                icon: 1
            })
		});
	} 
})

$("#btnNew").click(function() {
    var url = $(this).data("url");
    GLOBAL.go(url, options.param);
})

$("#btnBatchPrint").click(function() {
	var ids = [];
	$("#js_dataTable tr").each(function() {
		var cb = $(this).find("input[name='btSelectItem']");
		if (cb.prop("checked")) {
			var id = $(this).data("uniqueid");
			ids.push(id);
		}
	});
	if (ids.length == 0) {
		layer.msg("请先选择付款单");
		return false;
	}
	var stanItem = $('#js_dataTable').bootstrapTable('getRowByUniqueId', ids[0]);
	if (stanItem.stateInt == 0) {
		layer.msg("【" + stanItem.payNo + "】为待提交不能打印");
		return false;
	}
	var flag = true;
	for (var i=1; i<ids.length ; i++) {
		var item = $('#js_dataTable').bootstrapTable('getRowByUniqueId', ids[i]);
		if (stanItem.busiUnitId != item.busiUnitId || stanItem.projectId != item.projectId || stanItem.currnecyType != item.currnecyType 
					|| stanItem.payee != item.payee || stanItem.payer != item.payer || stanItem.payWay != item.payWay || stanItem.payAccountId != item.payAccountId
					|| stanItem.payType != item.payType || stanItem.state != item.state ) {
			flag = false;
			layer.msg("经营单位、项目、币种、收款单位、收款账号、付款单位、付款方式、付款类型、状态不一致无法批量打印");
			return flag;
		}
		if (item.stateInt == 0) {
			flag = false;
			layer.msg("【" + item.payNo + "】为待提交不能打印");
			return flag;
		}
	}
	if (flag) {
		var param = {
			ids : ids
		}
		var callback = function(e) {
			var items = e.items;
			if (items != null && items.payOrderResDto.length > 0) {
				var hint = "";
				for (var index=0; index<items.payOrderResDto.length; index++) {
					var item = items.payOrderResDto[index];
					if (item.printNum > 0) {
						hint = hint + "【" + item.payNo + "】已经打印过" + item.printNum + "次</br>";
					}
				}
				if (hint.length > 0) {
					layer.confirm(hint + "是否继续打印？",function(index) {
						GLOBAL.ajax("payOrder/preBatch/print", JSON.stringify(param) , function(e) {
							if (e.success) {
								window.open("html/pay/payBatchPrint.html?ids="+ ids.unique().join(','));
							} else {
								layer.msg(e.msg);
							}
						},true);
						layer.close(index);
					});
				}else {
					GLOBAL.ajax("payOrder/preBatch/print", JSON.stringify(param) , function(e) {
						if (e.success) {
							window.open("html/pay/payBatchPrint.html?ids="+ ids.unique().join(','));
						} else {
							layer.msg(e.msg);
						}
					},true);
				}
			}
		}
		GLOBAL.ajax("payOrder/batchPrint/query", JSON.stringify(param) , function(e) {
			if (e.success) {
				callback(e);
			} else {
				layer.msg(e.msg);
			}
		}, true);
	}
})

/*function getCondition() {
	return "?busiUnit=" + $("#busiUnit").val() 
	+ "&projectId=" + $("#projectId").val()
	+ "&payee=" + $("#payee").val()
	+ "&payNo=" + $("#payNo").val()
	+ "&attachedNumbe=" + $("#attachedNumbe").val()
	+ "&payType=" + $("#payType").val()
	+ "&payWay=" + $("#payWay").val()
	+ "&startRequestTime=" + $("#startRequestTime").val()
	+ "&endRequestTime=" + $("#endRequestTime").val()
	+ "&starConfirmorAt=" + $("#starConfirmorAt").val()
	+ "&endConfirmorAt=" + $("#endConfirmorAt").val()
	+ "&state=" + $("#state").val();
}
$("#btnExport").click(function() {
	var url = "/payOrder/excel/collectApprove.xls";
	url = url + getCondition();
	location.href = GLOBAL.host + url;
})*/

$("#btnExport").click(function() {
	var count_url = "/payOrder/excel/export/count";
	var data = $("#searchForm").serializeObject();
	GLOBAL.ajax(count_url, data, function(e) {
        if (e.success) {
        	var url = "payOrder/excel/collectApprove.xls";
        	url = url + "?" + $("#searchForm").serialize();
        	location.href = GLOBAL.host + url;
        } else {
            layer.msg(e.msg);
        }
    });
}) 