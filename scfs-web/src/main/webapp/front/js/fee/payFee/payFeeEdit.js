options.initPage = function() {
	GLOBAL.getEditData($("#form1"), options.param.id);
	
	$("#form1").validate({
		rules : {
			projectId : {
				required : true
			},
			custReceiver : {
				required : true
			},
			payFeeSpec : {
				required : true
			},
			payAssistFeeSpec : {
				required : true
			},
			payType : {
				required : true
			},
			payDate : {
				required : true
			},
			payAmount : {
				required : true,
				number : true
			},
			acceptInvoiceType : {
				required : true
			},
			currencyType : {
				required : true
			},
			payFeeType : {
				required : true
			}
		},
		submitHandler : function() {
			var data = $("#form1").serializeObject();
			GLOBAL.ajax("fee/pay/update", data, function(e) {
				if (e.success) {
					layer.msg("修改成功！");
				} else {
					layer.msg(e.msg);
				}
			});
		}
	});
	$(".js_datePicker").datetimepicker({
		format : 'Y-m-d'
	});
	
	$("#acceptInvoiceType").change();
}
$("#acceptInvoiceType").change(function() {
	if ($(this).val() == 1 || $(this).val() == "") {
		$("#acceptInvoiceTaxRateTr").hide();
	} else {
		$("#acceptInvoiceTaxRateTr").show();
	}
});
GLOBAL.goBack();