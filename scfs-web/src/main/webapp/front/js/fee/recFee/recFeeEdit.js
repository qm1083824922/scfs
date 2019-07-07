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
			receiveType : {
				required : true
			},
			recAmount : {
				required : true,
				number : true
			},
			provideInvoiceType : {
				required : true
			},
			currencyType : {
				required : true
			}
		},
		submitHandler : function() {
			var data = $("#form1").serializeObject();
			GLOBAL.ajax("fee/receive/update", data, function(e) {
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
}
$("#provideInvoiceType").change(function() {
	if ($(this).val() == 1 || $(this).val() == "") {
		$("#provideInvoiceTaxRateTr").hide();
	} else {
		$("#provideInvoiceTaxRateTr").show();
	}
});
GLOBAL.goBack();