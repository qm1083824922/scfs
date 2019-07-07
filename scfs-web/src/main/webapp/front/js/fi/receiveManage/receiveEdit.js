/* 数据表格 */
var tabCols = [{
	field : 'checkItem',
	checkbox : true,
	width : '1%'
},{
	title : '序号',
	field : 'columnsNumber',
	width : '2%'
}, {
	title : '项目',
	field : 'projectName',
	width : '15%',
	align : 'center'
}, {
	title : '客户',
	field : 'custName',
	width : '15%',
	align : 'center'
}, {
	title : '经营单位',
	field : 'busiUnitName',
	width : '10%',
	align : 'center'
}, {
	title : '单据编号',
	field : 'billNo',
	width : '8%',
	align : 'center'
},{
	title : '单据日期',
	field : 'billDate',
	width : '8%',
	align : 'center'
},{
	title : '币种',
	field : 'currencyTypeName',
	width : '8%',
	align : 'center'
}, {
	title : '对账金额',
	field : 'amountCheck',
	width : '10%',
	align : 'center',
	formatter : function(value , row , index) {
		return "<input style='text-align:center;color:red' name=amountCheck value='" + value + "'/>";
	}
}, {
	title : '核销金额',
	field : 'writeOffAmount',
	width : '10%',
	align : 'center'
}];

options.initPage = function() {
	GLOBAL.initTable($('#js_dataTable'), tabCols , {recId: options.param.id});
	
	$("#js_dataTable").on("change" , "input[name=amountCheck]" ,  function() {
		var amount = $(this).val();
		if (isNaN(amount)) {
			layer.alert("请输入合法的数字");
			return false;
		}
		if (amount == 0) {
			layer.alert("对账金额不能为0");
			return false;
		}
	});
}

$("#delete").click(function() {
	var ids = [];
	$('#js_dataTable').find('tbody tr').each(function(i,v){
        var cb = $(v).find("input[name='btSelectItem']");
        if(cb.prop("checked")){
        	ids.push($(v).data("uniqueid"));
        }
    });
	if (ids.length < 1) {
		layer.alert("请选择要删除的数据");
		return false;
	}
	var data = {
		id : options.param.id,
		ids : ids
	}
	GLOBAL.ajax("recLine/batch/delete" , JSON.stringify(data) , function(e) {
		if (e.success) {
			layer.msg("删除成功");
			$('#js_dataTable').bootstrapTable('refresh', {query: {recId : options.param.id}});
		}else {
			layer.alert(e.msg);
		}
	} , true);
});

$("#update").click(function() {
	var recLines = [];
	$('#js_dataTable').find('tbody tr').each(function(i,v){
        var cb = $(v).find("input[name='btSelectItem']");
        var data = $(v).data();
        if(cb.prop("checked")){
        	var uniqueid = $(v).data("uniqueid");
        	var rowData = $('#js_dataTable1').bootstrapTable('getRowByUniqueId' , uniqueid);
            var recLine = {
            	amountCheck : $(v).find("[name='amountCheck']").val(),
            	id : uniqueid
            }
            recLines.push(recLine);
        }
    });
	if (recLines.length < 1) {
		layer.alert("请先选择要修改的数据");
		return false;
	}
	var receive = {
		id : options.param.id
	}
	var recDetail = {
		receive  :receive,
		recLines :recLines
	}
	GLOBAL.ajax("recLine/batch/update" , JSON.stringify(recDetail) , function(e) {
		if (e.success) {
			layer.msg("修改成功");
			//$('#js_dataTable1').bootstrapTable('refresh', {query: options.param});
		}else {
			layer.alert(e.msg);
		}
	} , true);
});

GLOBAL.goBack();