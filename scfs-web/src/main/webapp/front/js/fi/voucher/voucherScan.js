
/*数据表格*/
options.initPage = function() {
	var tabCols = [{
	    title: '序号',
	    field: 'columnsNumber',
	    width: '5%'
	}, {
	    title: '流程节点',
	    field: "stateName",
	    width: '15%'
	},{
	    title: '处理人',
	    field: "dealName",
	    width: '15%'
	},{
	    title: '处理意见',
	    field: "suggestion",
	    width: '25%'
	},{
	    title: '开始时间',
	    field: "createTime",
	    width: '15%'
	},{
	    title: "处理时间",
	    field: "dealTime",
	    width: '15%'
	},{
	    title: "处理状态",
	    field: "auditStateName",
	    width: '10%'
	}];

	GLOBAL.ajax("voucher/detail/detail" , {voucherId : options.param.id} , function(e) {
		if (e.success) {
			var voucherDetail = e.items;
			if (voucherDetail) {
				//1渲染凭证
				var voucherForm = $("#voucherForm");
				var voucher = voucherDetail.voucher;
				for ( var i in voucher) {
					var value = voucher[i];
					voucherForm.find("#" + i).text(value);
				}
				//1渲染分录
				var voucherLines = voucherDetail.voucherLines;
				if (voucherLines != null && voucherLines.length > 0) {
					var html = [];
					var totalStandardDebitAmount = 0;
					var totalStandardCreditAmount = 0;
					for(var i = 0 ; i < voucherLines.length; i++) {
						var item = voucherLines[i];
						var assistDec = "";
						if (item.needProject == 1 && item.projectName) {
							assistDec += "项目: " + item.projectName + "</br>";
						}
						if (item.needSupplier == 1 && item.supplierName) {
							assistDec += "供应商: " + item.supplierName + "</br>";
						}
						if (item.needCust == 1 && item.custName) {
							assistDec += "客户: " + item.custName + "</br>";
						}
						if (item.needAccount == 1 && item.accountNo) {
							assistDec += "账号: " + item.accountNo + "</br>";
						}
						if (item.needUser == 1 && item.userName) {
							assistDec += "用户: " + item.userName + "</br>";
						}
						if (item.needTaxRate == 1 && item.taxRate != null) {
							assistDec += "税率: " + item.taxRate + "</br>";
						}
						html.push("<tr>");
						html.push("<td width='3%'>" + (i+1) + "</td>");
						html.push("<td width='15%'><span name='voucherLineSummary'>" + item.voucherLineSummary + "</span></td>");
						html.push("<td width='15%'><span name='accountLineName'>" + item.accountLineName + "</span></td>");
						html.push("<td width='25%'> " + assistDec + "</td>");
						html.push("<td width='8%'><span name='debitAmount'>" + (item.debitAmount==null?"":(item.debitAmount).toThounds()) + "</span></td>");
						html.push("<td width='8%'><span name='creditAmount'>" + (item.creditAmount==null?"":(item.creditAmount).toThounds()) + "</span></td>");
						html.push("<td width='5%'><span name='currencyTypeName'>" + item.currencyTypeName + "</span></td>");
						html.push("<td width='8%'><span name='standardDebitAmount'>" + (item.standardDebitAmount==null?"":(item.standardDebitAmount).toThounds()) + "</span></td>");
						html.push("<td width='8%'><span name='standardCreditAmount'>" + (item.standardCreditAmount==null?"":(item.standardCreditAmount).toThounds()) + "</span></td>");
						html.push("<td width='5%'><span name='standardCoinName'>" + item.standardCoinName + "</span></td>");
						html.push("</tr>");
						
						totalStandardDebitAmount = totalStandardDebitAmount + item.standardDebitAmount*1;
						totalStandardCreditAmount = totalStandardCreditAmount + item.standardCreditAmount*1;
					}
					var html1 = [];
					html1.push("<tfoot><tr>");
					html1.push("<td></td>");
					html1.push("<td></td>");
					html1.push("<td></td>");
					html1.push("<td class='text-right'><strong>合计</strong></td>");
					html1.push("<td>" + (voucher.debitAmount).toThounds() + "</td>");
					html1.push("<td>" + (voucher.creditAmount).toThounds() + "</td>");
					html1.push("<td></td>");
					html1.push("<td>" + (totalStandardDebitAmount).toThounds() + "</td>");
					html1.push("<td>" + (totalStandardCreditAmount).toThounds() + "</td>");
					html1.push("<td></td>");
					html1.push("</tr></tfoot>");
					console.log($('#js-lineTable').find("#js-lineTbody"));
					$('#js-lineTable').find("tbody").html(html.join(''));
					$('#js-lineTable').append(html1.join(''));
					
				}
				//3渲染辅助项
			}
			
		} else {
			layer.msg(e.msg);
		}
	});

	GLOBAL.initTable($('#js_audit_dataTable'), tabCols, {
	        voucherId: options.param.id
	    }, null, {
	        pagination: false
	    });
}
GLOBAL.goBack();