options.initPage = function() {
	GLOBAL.getEditData($("#form1"), options.param.id);
	$("#form1").validate({
		rules : {
			projectId : {
				required : true
			},
			custPayer : {
				required : true
			},
			recFeeSpec : {
				required : true
			},
			recAssistFeeSpec : {
				required : true
			},
			recType : {
				required : true
			},
			recDate : {
				required : true
			},
			recAmount : {
				required : true,
				number : true
			},
			provideInvoiceType : {
				required : true
			},
			receiveType : {
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
			payFeeType : {
				required : true
			},
			acceptInvoiceType : {
				required : true
			},
			currencyType : {
				required : true
			}
		},
		submitHandler : function() {
			var data = $("#form1").serializeObject();
			GLOBAL.ajax("fee/receive/pay/update", data, function(e) {
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
	
	$("#provideInvoiceType").change();
	
	$("#acceptInvoiceType").change();
}
$("#provideInvoiceType").change(function() {
	if ($(this).val() == 1 || $(this).val() == "") {
		$("#provideInvoiceTaxRateTr").hide();
	} else {
		$("#provideInvoiceTaxRateTr").show();
	}
});
$("#acceptInvoiceType").change(function() {
	if ($(this).val() == 1 || $(this).val() == "") {
		$("#acceptInvoiceTaxRateTr").hide();
	} else {
		$("#acceptInvoiceTaxRateTr").show();
	}
});
GLOBAL.goBack();