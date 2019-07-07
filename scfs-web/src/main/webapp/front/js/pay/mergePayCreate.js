/*数据表格*/
var tabCols = [
	{
		field : 'checkItem',
		checkbox : true,
		width : '1%'
	 },{  
	     title: '序号',
	     field: 'columnsNumber',  
	     width: '1%'
     }, {
	     title: '付款编号',
	     field: 'payNo',
	     width: '8%',
	     align: 'center'
     }, {
	     title: '附属编号',
	     field: 'attachedNumbe',
	     width: '8%',
	     align: 'center'
     }, {
	     title: '经营单位',
	     field: 'busiUnit',
	     width: '8%',
	     align: 'center'
	 }, {
	     title: '项目',
	     field: 'projectName',
	     width: '8%',
	     align: 'center'
	 }, {
	     title: '付款类型',
	     field: 'payTypeName',
	     width: '4%',
	     align: 'center'
	 }, {
	     title: '付款方式',
	     field: 'payWayName',
	     width: '4%',
	     align: 'center'
	 }, {
	     title: '支付类型',
	     field: 'payWayTypeName',
	     width: 40,
	     align: 'center'
	 }, {
	     title: '币种',
	     field: 'currnecyTypeName',
	     width: '4%',
	     align: 'center'
	 }, {
	     title: '付款金额',
	     field: 'payAmount',
	     width: '4%',
	     align: 'center',
	 	 formatter : function(value , row , index) {
			return value==null ? "": value.toThounds();
	     }
	 },  {
	     title: '收款单位',
	     field: 'payeeName',
	     width: '8%',
	     align: 'center'
	 },{
	     title: '收款账号',
	     field: 'accountNo',
	     width: '8%',
	     align: 'center'
	 }, {
	     title: '要求付款日期',
	     field: 'requestPayTime',
	     width: '8%',
	     align: 'center'
	 },{
	     title: '预计内部打款日期',
	     field: 'innerPayDate',
	     width: '8%',
	     align: 'center'
	 }, {
	     title: '备注',
	     field: 'remark',
	     width: '10%',
	     align: 'center'
	 }
];

options.initPage = function(){
	var param = options.param ;
	$("#searchForm").find("input, select").each(function() {
		var id = $(this).attr("id");
		if (param[id] != null && param[id] != '') {
			$(this).val(param[id]);
		}
	});
	var sumPayAmount = 0;
	var option = {
		onCheck: function(row , element){
			sumPayAmount = sumPayAmount.add(row.payAmount);
			$("#mergeForm").find("#payAmount").val(sumPayAmount);
	    },
	    onUncheck: function(row) {
	    	sumPayAmount = sumPayAmount.subtract(row.payAmount);
	    	$("#mergeForm").find("#payAmount").val(sumPayAmount);
	    },
	    onCheckAll: function(rows) {
	    	sumPayAmount = 0;
	    	for (var index=0 ; index < rows.length; index++) {
	    		sumPayAmount = sumPayAmount.add(rows[index].payAmount);
	    	}
	    	$("#mergeForm").find("#payAmount").val(sumPayAmount);
	    },
	    onUncheckAll: function(rows) {
	    	sumPayAmount = 0;
	    	$("#mergeForm").find("#payAmount").val(sumPayAmount);
	    },
	    pagination: false
	}
	GLOBAL.initTable($('#js_dataTable'), tabCols, param, null, option);
	$(".js_datePicker").datetimepicker({
        format:'Y-m-d',
    });
 };

 $("#btnSearch").click(function() {
	var data = $("#searchForm").serializeObject();
	if(data){
		var url = $('#js_dataTable').data("url");
	}
	console.log(data);
	GLOBAL.tableRefresh($('#js_dataTable'), data);
})

$("#mergeForm").validate({
	rules : {
		requestPayTime : {
			required : true
		},
		remark : {
			maxlength : 200
		}
	},
	submitHandler : function() {
		var ids = [];
		$("#js_dataTable tr").each(function() {
			var cb = $(this).find("input[name='btSelectItem']");
			if (cb.prop("checked")) {
				var id = $(this).data("uniqueid");
				ids.push(id);
			}
		});
		if (ids.length < 2) {
			layer.msg("请至少选择两个付款单");
			return false;
		}
		var stanItem = $('#js_dataTable').bootstrapTable('getRowByUniqueId', ids[0]);
		var flag = true;
		for (var i=1; i<ids.length ; i++) {
			var item = $('#js_dataTable').bootstrapTable('getRowByUniqueId', ids[i]);
			if (stanItem.busiUnitId != item.busiUnitId || stanItem.projectId != item.projectId || stanItem.currnecyType != item.currnecyType 
						|| stanItem.payee != item.payee || stanItem.payer != item.payer || stanItem.payWay != item.payWay || stanItem.payWayType != item.payWayType || stanItem.payAccountId != item.payAccountId
						|| stanItem.payType != item.payType || stanItem.state != item.state ) {
				flag = false;
				layer.msg("经营单位、项目、币种、收款单位、收款账号、付款单位、付款方式、支付类型、付款类型、状态不一致无法合并付款");
				return flag;
			}
		}
		if (flag) {
			var mergePayOrder = {
				busiUnit : stanItem.busiUnitId,
				projectId : stanItem.projectId,
				payType : stanItem.payType,
				currencyType : stanItem.currnecyType,
				payWay : stanItem.payWay,
				payee : stanItem.payee,
				payAccountId : stanItem.payAccountId,
				payer : stanItem.payer,
				remark : $("#remark").val(),
				requestPayTime : $("#requestPayTime").val()
			}
			var param = {
				mergePayOrder : mergePayOrder,
				ids : ids
			}
			GLOBAL.ajax("mergePayOrder/add", JSON.stringify(param) , function(e) {
				if (e.success) {
					options.param.id = e.items;
					GLOBAL.go("html/pay/mergePay/mergePayEdit.html");
				} else {
					layer.msg(e.msg);
				}
			},true,$("#save"));
		}
	}
});
GLOBAL.goBack();
