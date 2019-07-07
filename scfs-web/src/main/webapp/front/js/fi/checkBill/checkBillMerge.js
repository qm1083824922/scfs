/*数据表格*/
var tabCols1 = [
	 {  
	     title: '序号',
	     field: 'columnsNumber',  
	     width: '10%'
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
	     title: '单据日期',
	     field: 'billDateStr',
	     width: '15%',
	     align: 'center'
	 }, {
	    title: '单据类型',
	    field: 'billTypeName',
	    width: '15%',
	    align: 'center'
	 },{
	     title: '币种',
	     field: 'currencyTypeName',
	     width: '10%',
	     align: 'center'
	 }, {
	     title: '对账金额',
	     field: 'amountUnChecked',
	     width: '18%',
	     align: 'center',
	     formatter : function(value , row , index) {
	    	 return "<input style='text-align:center;color:red' name='amountUnChecked' value='" + value + "'/>";
	     }
	 }
];
var tabCols2 = [ {
	field : 'checkItem',
	checkbox : true,
	width : '1%'
}, {
	title : '序号',
	field : 'columnsNumber',
	width : '10%'
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
	width : '15%',
	align : 'center'
}, {
    title: '币种',
    field: 'currencyTypeName',
    width: '5%',
    align: 'center'
},{
	title : '应收金额',
	field : 'amountReceivable',
	width : '15%',
	align : 'center',
	formatter : function(value , row , index) {
   	 	return value.toThounds();
    }
},{
	title : '对账日期',
	field : 'checkDate',
	width : '24%',
	align : 'center'
} ];

options.initPage = function() {
	var baseParam = {
		projectId : options.param.projectId,
		custId : options.param.custId,
		busiUnit : options.param.busiUnit,
		currencyType : options.param.currencyType
	}
	var tableOptions1 = {
	         pagination: false,           //是否显示分页（*）
	         onPostBody: function(){
	 			formatTable1Cash();
	 		 }
	}
	GLOBAL.initTable($('#js_dataTable1'), tabCols1, {ids : options.param.ids} , null , tableOptions1);
	GLOBAL.initTable($('#js_dataTable2'), tabCols2, baseParam);
	
	$("#js_dataTable1").on("change" , "input[name=amountUnChecked]" ,  function() {
		var amount = $(this).val();
		if (!(/^(-)?[0-9]*(\.[0-9]{1,2})?$/).test(amount)) {
			layer.msg("请输入精度不超过2位的数字");
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
}
$("#save").click(function() {
	var recId = GLOBAL.selectId($('#js_dataTable2'));
	if (recId) {
		var recLines = [];
		var flag = true;
		$('#js_dataTable1').find('tbody tr').each(function(i,v){
	    	var uniqueid = $(v).data("uniqueid");
	    	var amountCheck = $(v).find("[name='amountUnChecked']").val();
	    	if (!(/^(-)?[0-9]*(\.[0-9]{1,2})?$/).test(amountCheck)) {
				layer.msg("请输入精度不超过2位的数字");
				flag = false;
				return false;
			}
	        var recLine = {
	        	amountCheck : $(v).find("[name='amountUnChecked']").val(),
	        	voucherLineId : uniqueid
	        }
	        recLines.push(recLine);
	    });
		if (!flag) {
			return ;
		}
		if (recId) {
			var receive = {
				id : recId
			}
			var recDetail = {
				receive  : receive,
				recLines : recLines
			}
			GLOBAL.ajax("rec/detail/merge" , JSON.stringify(recDetail) , function(e) {
				if (e.success) {
					layer.msg("合并成功！");
					GLOBAL.go("html/fi/checkBill/checkBillSearch.html");
				} else {
					layer.msg(e.msg);
				}
			} , true);
		}
	}
	
});
GLOBAL.goBack();