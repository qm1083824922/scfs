options.initPage = function(){
	var isFormValid = true;
	var vl_index = 2;
	var $currentTr = $(".vo-create-table tbody tr:first");
	var $curAccountLines = [];

	var voucher = {}, 
		voucherLines = [{
				voucherLineSummary: "",
				accountLineId: "",
				debitOrCredit: "",
				amount: "",
				currencyType: "",
				projectId: "",
				custId: "",
				accountId: "",
				supplierId: "",
				userId: "",
				taxRate: "",
				innerBusiUnitId: ""
			},{
				voucherLineSummary: "",
				accountLineId: "",
				debitOrCredit: "",
				amount: "",
				currencyType: "",
				projectId: "",
				custId: "",
				accountId: "",
				supplierId: "",
				userId: "",
				taxRate: "",
				innerBusiUnitId: ""
			}];

	$(".js_datePicker").datetimepicker({
        format:'Y-m-d',
    });
	
	//帐套联动数据   科目 项目 账号
	$("#accountBookId").change(function(){
		var _self = $(this);
		var busiUnit = null;
		var accountBookId = _self.val();
		if (accountBookId && accountBookId != "") {
			GLOBAL.ajax("accountBook/line/rel/all/query", {accountBookId: _self.val()}, function(data){
				var $tr = $(".vo-create-table tbody").find("tr") ;
	            if (data) {
	                var items = data.items;
	                var options = "<option value=''>请选择</option>";
	                if (items && items.length > 0) {
	                    for (var k = 0; k < items.length; k++) {
	                        options += "<option value='" + items[k].accountLineId + "'>" + items[k].accountLineName + "</option>";
	                    }
	                    $curAccountLines = items;
	                    busiUnit = items[0].busiUnit;
	                }else {
	                	layer.msg("该帐套未分配科目，请重新选择");
		            	removeStar();
		            	return false;
	                }
	                $tr.find(".vo-subject").each(function(){
	                	$(this).empty().html(options);
	                });
	            }else {
	            	layer.msg("该帐套未分配科目，请重新选择");
	            	removeStar();
	            	return false;
	            }
			});
			if (busiUnit) {
				var projectParam = {
					key : "BUSI_UNIT_PROJECT" ,
					pId : busiUnit
				}
				var accountParam = {
					key : "SUBJECT_ACCOUNT" ,
					pId : busiUnit	
				}
				GLOBAL.ajax("common/selected/query", projectParam, function(data){
					var options = "<option value=''>请选择</option>";
		            if (data) {
		                var items = data.items;
		                if (items) {
		                    for (var k = 0; k < items.length; k++) {
		                        options += "<option value='" + items[k].code + "'>" + items[k].value + "</option>";
		                    }
		                }
		            }
		            $(".vo-detail-tab").find("#projectId").html(options);
				});
				
				GLOBAL.ajax("common/selected/query", accountParam, function(data){
					var options = "<option value=''>请选择</option>";
		            if (data) {
		                var items = data.items;
		                if (items) {
		                    for (var k = 0; k < items.length; k++) {
		                        options += "<option value='" + items[k].code + "'>" + items[k].value + "</option>";
		                    }
		                }
		            }
		            $(".vo-detail-tab").find("#accountId").html(options);
				});
			}
		}else {
			$(".vo-detail-tab").find("#projectId").empty();
			$(".vo-detail-tab").find("#accountId").empty();
			$(".vo-create-table tbody").find("tr").find(".vo-subject").each(function(){
				$(this).empty().change();
			});
		}
		$(".vo-create-table tbody").find("tr").each(function(){
			resetNeedItem($(this));
		});
		
	});
	
	$(".add").click(function(){
		var $tbody = $(this).closest("table").find("tbody");
		var $tr = $tbody.find("tr:first");
		var $newTr = $tr.clone(true);
		var newIdx = parseInt($tbody.find("tr").length) + 1;
		$tbody.find("tr:last").after($newTr);
		$newTr.find("td:first").html(newIdx);
		$newTr.find("td:last").html('<a href="javascript:" class="delete text-danger">删除</a>');
		
		var newSelectId = 'accountLineId' + newIdx;
		var newSelectName = newSelectId;
		var voSelectHtml = '<select class="form-control vo-field vo-subject" id="'+newSelectId+'" name="'+newSelectName+'">';
		var voSelectOptions = $tbody.find("tr:last .vo-subject").html();
		voSelectHtml += voSelectOptions;
		voSelectHtml += '</select>';
		$newTr.find("td:eq(3)").html(voSelectHtml);
		$newTr.find(".vo-subject").select2();
		
		var newCurrencyId = 'currencyType' + newIdx;
		var newCurrencyName = newCurrencyId;
		var voCurrencyHtml = '<select class="form-control vo-field vo-currency" id="'+newCurrencyId+'" name="'+newCurrencyName+'">';
		var voCurrencyOptions = $tbody.find("tr:last .vo-currency").html();
		voCurrencyHtml += voCurrencyOptions;
		voCurrencyHtml += '</select>';
		$newTr.find("td:eq(6)").html(voCurrencyHtml);
		$newTr.find(".vo-currency").select2();
		
		var $newFormCtrl = $newTr.find(".form-control");

		vl_index++;

		$newFormCtrl.each(function(){
			var name = $(this).attr("name");
			var newName = name + vl_index;
			if(!$(this).hasClass("vo-subject") && !$(this).hasClass("vo-currency")){
				$(this).attr("name", newName);
			}
			$(this).val("");
			if (!$(this).hasClass("form-control-readonly")) {
				$(this).rules("add", "required");
			}
		})
		
		
		
		
		var newData = {
				voucherLineSummary: "",
				accountLineId: "",
				debitOrCredit: "",
				amount: "",
				currencyType: "",
				projectId: "",
				custId: "",
				accountId: "",
				supplierId: "",
				userId: "",
				taxRate: "",
				innerBusiUnitId: ""
			}
		voucherLines.push(newData);
//		$('.js-select').selectpicker('val', '');
//		$('.js-select').selectpicker('refresh');
		$(".vo-detail-tab select").each(function(){
			var _self = $(this);
			_self.val('').find(".text-star").remove();
		});
	})
	$(".vo-create-table").on("click", ".delete", function(){
		var trLen = $(".vo-create-table").find("tbody tr").length;
		if(trLen < 3){
			layer.msg("至少保留两项");
			return;
		}
		var $tr = $(this).closest("tr");
		var idx = $tr.index();
		$tr.remove();
		$(".vo-create-table tbody tr").each(function(idx,item){
			$(this).find("td:first").text(idx + 1);
		})
		voucherLines.splice(idx, 1);
		sum();
		resetNeedItemText();
		removeStar();
	})
	$(".vo-create-table").on("input", ".vo-summary", function(){
		var $this = $(this);
		var idx = $this.closest("tr").index();
		voucherLines[idx].voucherLineSummary = $this.val();
		
	})
	$(".vo-create-table tbody").on("click", "tr", function(){
		$(this).find("[name='voItem']").prop("checked", true);
		$currentTr = $(this);
		var idx = $currentTr.index();
		var vlItem = voucherLines[idx];
		var $voDtlTab = $(".vo-detail-tab");

		var projectId = vlItem.projectId;
	    var custId = vlItem.custId;
	    var accountId = vlItem.accountId;
	    var supplierId = vlItem.supplierId;
	    var userId = vlItem.userId;
	    var taxRate = vlItem.taxRate;
	    var innerBusiUnitId = vlItem.innerBusiUnitId;

		$voDtlTab.find("#projectId").val(projectId);
        $voDtlTab.find("#custId").val(custId);
        $voDtlTab.find("#accountId").val(accountId);
        $voDtlTab.find("#supplierId").val(supplierId);
        $voDtlTab.find("#userId").val(userId);
        $voDtlTab.find("#taxRate").val(taxRate);
        $voDtlTab.find("#innerBusiUnitId").val(innerBusiUnitId);
        var accountLine_index = ($currentTr.find(".vo-subject").prop("selectedIndex")) - 1;
        renderNeedItem($curAccountLines[accountLine_index]);
	});
	$(".vo-create-table").on("change",".vo-subject",function(){
		var _self = $(this);
		var $currentTr = _self.closest("tr");
		$currentTr.find("[name='voItem']").prop("checked", true);
		var id = _self.val();
		var tr_index = $currentTr.index();
		var accountLine_index = _self.prop("selectedIndex") - 1;
		if(id != ""){
			voucherLines[tr_index].accountLineId = id;
			renderNeedItem($curAccountLines[accountLine_index]);
		}else {
			voucherLines[tr_index].accountLineId = "";
			removeStar();
		}
		resetNeedItem($currentTr);
		return false;
	})
	$(".vo-create-table").on("input", ".vo-debit", function(){
		var $credit = $(this).closest("tr").find(".vo-credit");
		var val = $(this).val();
		var $tr = $(this).closest("tr");
		var idx = $tr.index();
		var debitAmount = $tr.find(".vo-debit").val();
        var creditAmount = $tr.find(".vo-credit").val();
        voucherLines[idx].debitOrCredit = debitAmount ? 1 : 2;
        voucherLines[idx].amount = debitAmount ? debitAmount : creditAmount;
		if(val){
			$credit.rules("remove", "required");
			$credit.focus().blur();
			$(this).focus();
		}
		if($credit.val() != ""){
			$(this).val("");
			layer.msg("已输入贷方金额");
			return;
		}
		queryStandardCoinInfo($tr);
		if(!isNumber(val)){
			isFormValid = false;
		}else{
			isFormValid = true;
			sum();
		}
	})
	$(".vo-create-table").on("input", ".vo-credit", function(){
		var $debit = $(this).closest("tr").find(".vo-debit");
		var val = $(this).val();
		if(val){
			$debit.rules("remove", "required");
			$debit.focus().blur();
			$(this).focus();
		}
		var $tr = $(this).closest("tr");
		var idx = $tr.index();
		var debitAmount = $tr.find(".vo-debit").val();
        var creditAmount = $tr.find(".vo-credit").val();
        voucherLines[idx].debitOrCredit = debitAmount ? 1 : 2;
        voucherLines[idx].amount = debitAmount ? debitAmount : creditAmount;
		if($debit.val() != ""){
			$(this).val("");
			layer.msg("已输入借方金额");
			return;
		}
		queryStandardCoinInfo($tr);
		if(!isNumber(val)){
			isFormValid = false;
		}else{
			isFormValid = true;
			sum();
		}
	})
	$(".vo-create-table").on("change", ".vo-currency", function(){
		var val = $(this).val();
		var $tr = $(this).closest("tr");
		var idx = $tr.index();
		if( val != ""){
			voucherLines[idx].currencyType = val;
		}
		queryStandardCoinInfo($tr);
		sum();
	})
	$(".vo-detail-tab select").change(function(){
		var value = $(this).val();
		var name = $(this).attr("name");
		var idx = $currentTr.index();
		voucherLines[idx][name] = value;
	})
	function isNumber(number){
		var reg = /^(?:-?\d+|-?\d{1,3}(?:,\d{3})+)?(?:\.\d{1,2})?$/;
		if(!reg.test(number)){
			layer.msg("输入有误");
			return false;
		}
		return true;
	}
	function sum(){
		var debitSum = 0, creditSum = 0, standardDebitSum = 0, standardCreditSum = 0;
		$(".vo-create-table tbody tr").each(function(idx,item){
			var debit = $(this).find(".vo-debit").val();
			var credit = $(this).find(".vo-credit").val();
			var standardDebit = $(this).find(".vo-standard-debit").val();
			var standardCredit = $(this).find(".vo-standard-credit").val();
			if(debit){
				debitSum = debitSum + debit*1;
			}
			if(credit){
				creditSum = creditSum + credit*1;
			}
			if(standardDebit) {
				standardDebitSum = standardDebitSum + standardDebit*1;
			}
			if(standardCredit) {
				standardCreditSum = standardCreditSum + standardCredit*1;
			}
		})
		$("#debitSum").text(Number(debitSum.toFixed(2)).toThounds());
		$("#creditSum").text(Number(creditSum.toFixed(2)).toThounds());
		$("#standardDebitSum").text(Number(standardDebitSum.toFixed(2)).toThounds());
		$("#standardCreditSum").text(Number(standardCreditSum.toFixed(2)).toThounds());
	}
	function removeStar() {
		$(".vo-detail-tab .text-star").remove();
	}
	function resetNeedItem($tr) {
		var data = voucherLines[$tr.index()];
		data.projectId = "";
		data.userId = "";
		data.accountId = "";
		data.custId = "";
		data.supplierId = "";
		data.taxRate = "";
		data.innerBusiUnitId = "";
		resetNeedItemText();
	}
	function resetNeedItemText() {
		$("#projectId").val("");
		$("#userId").val("");
		$("#accountId").val("");
		$("#custId").val("");
		$("#supplierId").val("");
		$("#taxRate").val("");
		$("#innerBusiUnitId").val("");
	}
	
	function renderNeedItem(data) {
		if (typeof(data) != undefined && data != null) {
			var accountStr = data.needAccount == 1 ? "<em class='text-star'>*</em>账号: " : "账号: ";
			var custStr = data.needCust == 1 ? "<em class='text-star'>*</em>客户: " : "客户: ";
			var projectStr = data.needProject == 1 ? "<em class='text-star'>*</em>项目: " : "项目: ";
			var supplierStr = data.needSupplier == 1 ? "<em class='text-star'>*</em>供应商: " : "供应商: ";
			var taxRateStr = data.needTaxRate == 1 ? "<em class='text-star'>*</em>税率: " : "税率: ";
			var userStr = data.needUser == 1 ? "<em class='text-star'>*</em>人员: " : "人员: ";
			var innerBusiUnitStr = data.needInnerBusiUnit == 1 ? "<em class='text-star'>*</em>内部经营单位: " : "内部经营单位: ";
			$(".vo-detail-tab [name='accountLabel']").html(accountStr);
			$(".vo-detail-tab [name='custLabel']").html(custStr);
			$(".vo-detail-tab [name='projectLabel']").html(projectStr);
			$(".vo-detail-tab [name='supplierLabel']").html(supplierStr);
			$(".vo-detail-tab [name='taxRateLabel']").html(taxRateStr);
			$(".vo-detail-tab [name='userLabel']").html(userStr);
			$(".vo-detail-tab [name='innerBusiUnitLabel']").html(innerBusiUnitStr);
		}else {
			removeStar();
		}
	}
	
	function queryStandardCoinInfo($tr) {
		var debitAmount = $tr.find(".vo-debit").val();
        var creditAmount = $tr.find(".vo-credit").val();
        var currencyType = $tr.find(".vo-currency").val();
		var accountBookId = $("#accountBookId").val();
    	var voucherDate = $("#voucherDate").val();

		var params = {
			"accountBookId" : accountBookId,
			"voucherDate" : voucherDate,
			"debitAmount" : debitAmount,
			"creditAmount" : creditAmount,
			"currencyType" : currencyType
		};
		GLOBAL.ajax("voucher/queryStandardCoinInfo", params, function(data){
			if (data && data.items) {
				$tr.find(".vo-standard-debit").val(data.items.standardDebitAmount);
				$tr.find(".vo-standard-credit").val(data.items.standardCreditAmount);
				$tr.find(".vo-standard-coin-name").val(data.items.standardCoinName);
			}
		});
	}

	$("#voCreateForm").validate({
             rules: {
                 voucherSummary: {
                   required: true
                 },
                 accountBookId: {
                   required: true
                 },
                 voucherDate: {
                   required: true
                 },
                 voucherWord: {
                   required: true
                 },
                 attachmentNumber: {
                   required: true,
                   number: true
                 },


                 voucherLineSummary : {
                	 required: true
                 },

                 accountLineId: {
                   required: true
                 },
                 
                 creditAmount: {
                   required: true
                 },

                 debitAmount: {
                   required: true
                 },

                 currencyType: {
                   required: true
                 },


                 voucherLineSummary2: {
                	 required: true
                 },

                 accountLineId2: {
                   required: true
                 },
                 
                 creditAmount2: {
                   required: true
                 },

                 debitAmount2: {
                   required: true
                 },

                 currencyType2: {
                   required: true
                 },
             },
             submitHandler: function() {
                var data = {};
            	voucher.voucherSummary = $("#voucherSummary").val();
            	voucher.accountBookId = $("#accountBookId").val();
            	voucher.voucherDate = $("#voucherDate").val();
            	voucher.voucherWord = $("#voucherWord").val();
            	voucher.attachmentNumber = $("#attachmentNumber").val();
                
                data.voucher = voucher;
                data.voucherLines = voucherLines;
                console.log("提交数据：",data)

                GLOBAL.ajax("voucher/detail/add", JSON.stringify(data), function(e){
                  if(e.success){
                      layer.msg("保存成功！", {
                         icon: 1,
                         time: 1500
                     },function(){
                         var url = $("#save").data("url");
                         GLOBAL.go(url);
                     })
                  }
               }, true);
             },
			errorPlacement: function(error, element) { //错误信息位置设置方法
				//error.appendTo(); //这里的element是录入数据的对象
				error.remove();
			}
    });
	$("#accountBookId").change();
	$(".vo-create-table tbody tr:first").find("[name='voItem']").prop("checked", true);
}
GLOBAL.goBack();
