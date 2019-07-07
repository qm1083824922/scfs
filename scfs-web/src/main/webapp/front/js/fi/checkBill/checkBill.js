/*数据表格*/
var tabCols1 = [
     {
		field : 'checkItem',
		checkbox : true,
		width : '4%'
     },
	 {  
	     title: '序号',
	     field: 'columnsNumber',  
	     width: '4%'
     }, {
	     title: '项目',
	     field: 'projectName',
	     width: '15%',
	     align: 'center'
	 }, {
	     title: '客户',
	     field: 'custName',
	     width: '15%',
	     align: 'center'
	 }, {
	     title: '经营单位',
	     field: 'busiUnitName',
	     width: '15%',
	     align: 'center'
	 },  {
	     title: '单据类型',
	     field: 'billTypeName',
	     width: '8%',
	     align: 'center'
	 },{
	     title: '单据编号',
	     field: 'billNo',
	     width: '15%',
	     align: 'center'
	 }, {
	     title: '单据日期',
	     field: 'billDateStr',
	     width: '9%',
	     align: 'center'
	 },{
	     title: '应收日期',
	     field: 'recDate',
	     width: '9%',
	     align: 'center'
	 }, {
	     title: '币种',
	     field: 'currencyTypeName',
	     width: '5%',
	     align: 'center'
	 }, {
	     title: '对账金额',
	     field: 'amountUnChecked',
	     width: '10%',
	     align: 'center',
	     formatter : function(value , row , index) {
	    	 return "<input style='text-align:center;color:red;font-weight:bold;' name=amountUnChecked value='" + value + "'/>";
	     }
	 }
];

/*数据表格*/
var tabCols2 = [
	 {  
	     title: '序号',
	     field: 'columnsNumber',  
	     width: '1%'
     }, {
	     title: '项目',
	     field: 'projectName',
	     width: '15%',
	     align: 'center'
	 }, {
	     title: '客户',
	     field: 'custName',
	     width: '15%',
	     align: 'center'
	 }, {
	     title: '经营单位',
	     field: 'busiUnitName',
	     width: '15%',
	     align: 'center'
	 }, {
	     title: '币种',
	     field: 'currencyTypeName',
	     width: '6%',
	     align: 'center'
	 },{
	     title: '应收金额',
	     field: 'amountReceivable',
	     width: '10%',
	     align: 'center',
	     formatter : function(value , row , index) {
	    	 return value.toThounds();
	     }
	 },{
	     title: '已收金额',
	     field: 'amountReceived',
	     width: '8%',
	     align: 'center',
	     formatter : function(value , row , index) {
	    	 return value.toThounds();
	     }
	 },{
	     title: '对账日期',
	     field: 'checkDate',
	     width: '15%',
	     align: 'center'
	 },
	 {
	     title: '应收到期日期',
	     field: 'expireDate',
	     width: '15%',
	     align: 'center'
	 }
];
options.initPage = function(){
	var baseParam = {
		projectId : options.param.projectId,
		custId : options.param.custId,
		busiUnit : options.param.busiUnit,
		currencyType : options.param.currencyType
	}
	if (options.param.billNo.length > 0) {
		$("#billNo").val(options.param.billNo);
	}
	if (options.param.startBillDate.length > 0) {
		$("#js_form_datetimeStart").val(options.param.startBillDate);
	}
	if (options.param.endBillDate.length > 0) {
		$("#js_form_datetimeEnd").val(options.param.endBillDate);
	}
	var tableOptions1 = {
	         pagination: false,           		 //是否显示分页（*）
	         onPostBody: function(){
	 			formatTable1Cash();
	 		 }
	}
	GLOBAL.initTable($('#js_dataTable1'), tabCols1, options.param , null , tableOptions1);
	GLOBAL.initTable($('#js_dataTable2'), tabCols2, baseParam);
	
	$(".js_datePicker").datetimepicker({
        format:'Y-m-d',
    });
	
	$("#js_dataTable1").on("change" , "input[name=amountUnChecked]" ,  function() {
		var amount = $(this).val();
		if (isNaN(amount)) {
			layer.msg("请输入合法的数字");
			return false;
		}
		if (amount == 0) {
			layer.msg("对账金额不能为0");
			return false;
		}
		formatTable1Cash();
	});
	
	function formatTable1Cash() {
		var checkSum = 0 ;
		$("#js_dataTable1 tbody").find("tr").each(function() {
			var _self = $(this);
			var tempAmount = _self.find("input[name=amountUnChecked]").val();
			checkSum += (+tempAmount);
		});
		$("#checkSum").text(checkSum.toThounds());
	}
	
	function formatTable2Cash() {
		$("#js_dataTable2 tbody").find("tr").each(function() {
			var _self = $(this);
			var amountReceivable = _self.find("[name=amountReceivable]").text();
			var amountReceived = _self.find("[name=amountReceivable]").text();
			_self.find("[name=amountReceivable]").text((+amountReceivable).toThounds());
			_self.find("[name=amountReceived]").text((+amountReceived).toThounds());
		});
	}
}

$("#btnSearch").click(function() {
	var baseParam = {
		projectId : options.param.projectId,
		custId : options.param.custId,
		busiUnit : options.param.busiUnit
	}
	var data = $("#searchForm").serializeObject();
	$.extend(data , baseParam);
    $('#js_dataTable1').bootstrapTable('refresh', {query: data});
    $('#js_dataTable2').bootstrapTable('refresh', {query: baseParam});
});

$("#save").click(function() {
	var recLines = [];
	if ($("#checkDate").val().length < 1) {
		layer.msg("对账日期不能为空");
		return false;
	}
	if ($("#expireDate").val().length < 1) {
		layer.msg("对账日期不能为空");
		return false;
	}
	var receive = {
		projectId 	 : options.param.projectId,
		custId    	 : options.param.custId,
		busiUnit  	 : options.param.busiUnit,
		currencyType : options.param.currencyType,
		checkDate 	 : $("#checkDate").val(),
		expireDate   : $("#expireDate").val(),
		checkNote 	 : $("#checkNote").val()
	}
	var flag = true;
	var stanItem = {};
	$('#js_dataTable1').find('tbody tr').find("input[name='btSelectItem']:checked").each(function(i,v){
    	var $tr = $(v).closest("tr")
    	var uniqueid = $tr.data("uniqueid");
    	var row = $("#js_dataTable1").bootstrapTable("getRowByUniqueId",uniqueid);
    	if (i == 0) {
    		stanItem = row ;
    	} else {
    		if (row.billType != stanItem.billType) {
    			layer.msg("请选择相同的单据类型进行对账");
    			flag = false;
    			return false;
    		}else if (1 == row.billType ) {
    			if (row.feeType != stanItem.feeType) {
    				layer.msg("应收费用和应收应付费用应分开对账");
    				flag = false;
    				return false;
    			}
    		}
    	}
    	var amountCheck =$tr.find("[name='amountUnChecked']").val();
    	if (!(/^(-)?[0-9]*(\.[0-9]{1,2})?$/).test(amountCheck)) {
    		layer.msg("请输入精度不超过2位的数字");
    		flag = false;
			return false;
    	}
        var recLine = {
        	amountCheck : amountCheck,
        	voucherLineId : uniqueid
        }
        
        recLines.push(recLine);
    });
	if (!flag) {
		return false;
	}
	if (recLines.length < 1) {
		layer.msg("请先选择要对账的数据");
		return false;
	}
	var recDetail = {
		receive  :receive,
		recLines :recLines
	}
	
	GLOBAL.ajax("rec/detail/add" , JSON.stringify(recDetail) , function(e) {
		if (e.success) {
			layer.msg("对账成功！");
			GLOBAL.go("html/fi/checkBill/checkBillSearch.html");
		} else {
			layer.msg(e.msg);
		}
	} , true);
});

$("#merge").click(function() {
	var ids = GLOBAL.selectIds($('#js_dataTable1'));
	if (ids.length > 0) {
		var data = $("#js_dataTable2").bootstrapTable('getData');
		if (typeof(data) == 'undefined' || data.length == 0 ) {
			layer.alert("没有可以合并的应收");
			return false;
		}
		var stanItem = {};
		var flag = true;
		$('#js_dataTable1').find('tbody tr').find("input[name='btSelectItem']:checked").each(function(i,v){
	    	var $tr = $(v).closest("tr")
	    	var uniqueid = $tr.data("uniqueid");
	    	var row = $("#js_dataTable1").bootstrapTable("getRowByUniqueId",uniqueid);
	    	if (i == 0) {
	    		stanItem = row ;
	    	} else {
	    		if (row.billType != stanItem.billType) {
	    			layer.msg("请选择相同的单据类型进行合并");
	    			flag = false;
	    			return false;
	    		}else if (1 == row.billType ) {
	    			if (row.feeType != stanItem.feeType) {
	    				layer.msg("应收费用和应收应付费用无法合并");
	    				flag = false;
	    				return false;
	    			}
	    		}
	    	}
		});
		if (!flag) {
			return;
		}
		options.param.ids = ids;
		GLOBAL.go("html/fi/checkBill/checkBillMerge.html");
	}
});

GLOBAL.goBack();