/*数据表格*/
options.initPage = function() {
	var $currentTr = $("#js-lineTbody tr:first");
	var $curAccountLines = [];
	var $curAccountLine = {};
	
	$(".js_datePicker").datetimepicker({
        format:'Y-m-d',
    });
	
	GLOBAL.ajax("voucher/detail/edit" , {voucherId : options.param.id} , function(e) {
		if (e.success) {
			var voucherDetail = e.items;
			if (voucherDetail) {
				//1渲染凭证
				var voucherForm = $("#voucherForm");
				var voucher = voucherDetail.voucher;
				for ( var i in voucher) {
					var value = voucher[i];
					voucherForm.find("#" + i).val(value);
				}
				//1渲染分录
				var voucherLines = voucherDetail.voucherLines;
				if (voucherLines != null && voucherLines.length > 0) {
					var html = [];
					var totalStandardDebitAmount = 0;
					var totalStandardCreditAmount = 0;
					for(var i = 0 ; i < voucherLines.length; i++) {
						var item = voucherLines[i];
						html.push("<tr id=" + item.id + ">");
						html.push("<td>" + (i+1) + "</td>");
						if (item.accountLineNo == "660304") {	//损益科目
							html.push("<td><input disabled type='radio' name='voItem'/> </td>");
							html.push("<input type='hidden' name='accountLineNo' id='accountLineNo' value='" + item.accountLineNo + "'/>");
							html.push("<td><input readonly=true class='form-control vo-field' name='voucherLineSummary' id='voucherLineSummary' value='" + item.voucherLineSummary + "'/></td>");
							html.push("<td><input readonly=true class='form-control vo-field' name='accountLineName' value='" + item.accountLineName + "'/></td>");
							html.push("<td><input readonly=true class='form-control' style='text-align:right' name='debitAmount' id='debitAmount' value='" + (item.debitAmount==null?"":item.debitAmount) + "'/></td>");
							html.push("<td><input readonly=true class='form-control' style='text-align:right' name='creditAmount' id='creditAmount' value='" + (item.creditAmount==null?"":item.creditAmount) + "'/></td>");
							html.push("<td><input readonly=true class='form-control' name='currencyTypeName' id='currencyTypeName' value='" + item.currencyTypeName +  "'/></td>");
							html.push("<td><input style='width:100px' readonly=true class='form-control form-control-readonly' style='text-align:right' name='standardDebitAmount' id='standardDebitAmount' value='" + (item.standardDebitAmount==null?"":item.standardDebitAmount) + "'/></td>");
							html.push("<td><input style='width:100px' readonly=true class='form-control form-control-readonly' style='text-align:right' name='standardCreditAmount' id='standardCreditAmount' value='" + (item.standardCreditAmount==null?"":item.standardCreditAmount) + "'/></td>");
							html.push("<td><input style='width:100px' readonly=true class='form-control form-control-readonly' data-url='DEFAULT_CURRENCY_TYPE' name='standardCoinName' id='standardCoinName' value='" + item.standardCoinName +  "'/></td>");
							html.push("<td><a href='javascript:'></a></td>");
						} else {
							html.push("<td><input type='radio' name='voItem'/> </td>");
							html.push("<input type='hidden' name='accountLineNo' id='accountLineNo' value='" + item.accountLineNo + "'/>");
							html.push("<td><input class='form-control vo-field' name='voucherLineSummary' id='voucherLineSummary' value='" + item.voucherLineSummary + "'/></td>");
							html.push("<td><select class='form-control vo-field' name='accountLineId' ></select></td>");
							html.push("<td><input class='form-control' style='text-align:right' name='debitAmount' id='debitAmount' value='" + (item.debitAmount==null?"":item.debitAmount) + "'/></td>");
							html.push("<td><input class='form-control' style='text-align:right' name='creditAmount' id='creditAmount' value='" + (item.creditAmount==null?"":item.creditAmount) + "'/></td>");
							html.push("<td><select class='form-control vo-field js-select' data-url='DEFAULT_CURRENCY_TYPE' name='currencyType' id='currencyType' value='" + item.currencyType +  "'></select></td>");
							html.push("<td><input style='width:100px' readonly=true class='form-control form-control-readonly' style='text-align:right' name='standardDebitAmount' id='standardDebitAmount' value='" + (item.standardDebitAmount==null?"":item.standardDebitAmount) + "'/></td>");
							html.push("<td><input style='width:100px' readonly=true class='form-control form-control-readonly' style='text-align:right' name='standardCreditAmount' id='standardCreditAmount' value='" + (item.standardCreditAmount==null?"":item.standardCreditAmount) + "'/></td>");
							html.push("<td><input style='width:100px' readonly=true class='form-control form-control-readonly' data-url='DEFAULT_CURRENCY_TYPE' name='standardCoinName' id='standardCoinName' value='" + item.standardCoinName +  "'/></td>");
							html.push("<td><a href='javascript:' class='delete text-danger'>删除</a></td>");
						}
						html.push("</tr>");
						
						totalStandardDebitAmount = totalStandardDebitAmount + item.standardDebitAmount*1;
						totalStandardCreditAmount = totalStandardCreditAmount + item.standardCreditAmount*1;
					}
					var tfoot_html = [];
					tfoot_html.push("<tfoot><tr>");
					tfoot_html.push("<td><a href='javascript:' class='add'>添加</a></td>");
					tfoot_html.push("<td></td>");
					tfoot_html.push("<td></td>");
					tfoot_html.push("<td class='text-right'><strong>合计</strong></td>");
					tfoot_html.push("<td id='debitSum'>"  + (voucher.debitAmount).toThounds() + "</td>");
					tfoot_html.push("<td id='creditSum'>" + (voucher.creditAmount).toThounds() + "</td>");
					tfoot_html.push("<td></td>");
					tfoot_html.push("<td id='standardDebitSum'>"  + (totalStandardDebitAmount).toThounds() + "</td>");
					tfoot_html.push("<td id='standardCreditSum'>" + (totalStandardCreditAmount).toThounds() + "</td>");
					tfoot_html.push("<td></td>");
					tfoot_html.push("<td></td>");
					tfoot_html.push("</tr></tfoot>");
					console.log($('#js-lineTable').find("#js-lineTbody"));
					$('#js-lineTbody').html(html.join(''));
					$('#js-lineTable').append(tfoot_html.join(''));
					$("#js-lineTbody").on("click", "tr", function(){
						$(this).find("[name='voItem']").prop("checked", true);
						$currentTr = $(this);
						var data = $currentTr.data("voucherLine");
						if (data) {
							$(".vo-detail-tab select").each(function(){
								var id = $(this).attr("id");
								$(this).val(data[id]);
							});
						}
						var $select = $currentTr.find("select[name=accountLineId]");
						var accountLine_index = $select.prop("selectedIndex");
						renderNeedItem($curAccountLines[accountLine_index]);
						
						var accountLineNo = $currentTr.find("input[name=accountLineNo]").val();
						if (accountLineNo == "660304") {	//损益科目
							$("#assistTable select").attr("disabled", true);
						} else {
							$("#assistTable select").attr("disabled", false);
						}
					});
					$("#js-lineTable").on("change", "select[name=accountLineId]", function(){
						var _self = $(this);
						var accountLineId = _self.val();
						var accountLine_index = _self.prop("selectedIndex");
						
						$currentTr = _self.closest("tr");
						$currentTr.find("[name='voItem']").prop("checked", true);
						
						var data = $currentTr.data("voucherLine");
						if (data) {
							$(".vo-detail-tab select").each(function(){
								var id = $(this).attr("id");
								$(this).val(data[id]);
							});
						} 
						if(accountLineId != ""){
							$curAccountLine = $curAccountLines[accountLine_index];
							renderNeedItem($curAccountLine);
							$currentTr.data("voucherLine" , data);
						}else {
							removeStar();
						}
						data.accountLineId = accountLineId;
						resetNeedItem($currentTr);
						return false;
					});
					$("#js-lineTbody").on("change" , "input , select" , function(){
						var $tr = $(this).closest("tr");
						var data = $tr.data("voucherLine");
						var attr_name = $(this).attr("name");
						data[attr_name] = $(this).val();
						queryStandardCoinInfo($tr);
						sum();
					});
					$(".vo-detail-tab").on("change" , "select" , function(){
						var _self = $(this);
						var id = _self.attr("id");
						var curData = $currentTr.data("voucherLine");
						curData[id] = _self.val();
						$currentTr.data("voucherLine" , curData);
					});
					$(".add").click(function(){
						var $tbody = $(this).closest("table").find("tbody");
						var $tr = $tbody.find("tr:first");
						var $newTr = $tr.clone();
						var newIdx = parseInt($tbody.find("tr").length) + 1;
						$tbody.find("tr:last").after($newTr);
						$newTr.find("td:first").html(newIdx);
						var $newFormCtrl = $newTr.find(".form-control");
						$newFormCtrl.each(function(){
							$(this).val("");
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
						
						$tbody.find("tr:last").data("voucherLine" , newData);
					});
					$("#js-lineTable").on("click", ".delete", function(){
						var trLen = $(".vo-create-table").find("tbody tr").length;
						if (trLen == 2) {
							layer.msg("至少保留两项");
							return false;
						}
						var $tr = $(this).closest("tr");
						var idx = $tr.index();
						$tr.remove();
						$("#js-lineTable tbody tr").each(function(idx,item){
							$(this).find("td:first").text(idx + 1);
						});
						sum();
						resetNeedItemText();
						removeStar();
					});
					$("#js-lineTable").on("input", "input[name=debitAmount]", function(){
						var debitAmount = $(this).val();
						var $credit = $(this).closest("tr").find("#creditAmount");
						if (isNumber(debitAmount)) {
							var creditAmount = $credit.val();
							var $tr = $(this).closest("tr");
							var data = $tr.data("voucherLine");
							if(creditAmount != ""){
								$(this).val("");
								layer.msg("已输入贷方金额");
								return;
							}
							queryStandardCoinInfo($tr);

							data.debitOrCredit = 1;
							data.amount = debitAmount;
							sum();
							$tr.data("voucherLine" , data);
						}
					});
					$("#js-lineTable").on("input", "input[name=creditAmount]", function(){
						var creditAmount = $(this).val();
						var $debit = $(this).closest("tr").find("#debitAmount");
						if(isNumber(creditAmount)) {
							var debitAmount = $debit.val();
							var $tr = $(this).closest("tr");
							var data = $tr.data("voucherLine");
							if(debitAmount != ""){
								$(this).val("");
								layer.msg("已输入借方金额");
								return;
							}
							queryStandardCoinInfo($tr);

					        data.debitOrCredit = 2;
					        data.amount = creditAmount;
							sum();
							$tr.data("voucherLine" , data);
						}
					});
					$("#voucherForm").validate({
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
				                 }
				             },
				             submitHandler: function() {
				                var data = {};
				                var voucherLines = [];
				                var voucher = {};
				                voucher.id = $("#voucherDiv").find("#id").val();
				            	voucher.voucherSummary = $("#voucherSummary").val();
				            	voucher.accountBookId = $("#accountBookId").val();
				            	voucher.voucherDate = $("#voucherDate").val();
				            	voucher.voucherWord = $("#voucherWord").val();
				            	voucher.attachmentNumber = $("#attachmentNumber").val();
				                
				                data.voucher = voucher;
				                var result = true;
				                $("#js-lineTbody").find("tr").each(function(){
				                	var curData = $(this).data("voucherLine");
				                	if (curData.accountLineNo != "660304") {	//损益科目
				                		if (curData.debitAmount && curData.creditAmount) {
					                		layer.msg("贷方金额和借方金额不能同时录入");
					                		result = false;
					                		return result;
					                	}
					                	if (!curData.debitAmount && !curData.creditAmount) {
					                		layer.msg("贷方金额和借方金额不能同时为空");
					                		result = false;
					                		return result;
					                	}
					                	curData.accountLineId = $(this).find("select[name=accountLineId]").val();
					                	curData.currencyType = $(this).find("select[name=currencyType]").val();
					                	if (!curData.accountLineId) {
					                		layer.msg("科目不能为空");
					                		result = false;
					                		return result;
					                	}
					                	if (!curData.currencyType) {
					                		layer.msg("币种不能为空");
					                		result = false;
					                		return result;
					                	}
					                	voucherLines.push(curData);
				                	}
				                });
				                if (!result) {
				                	return false;
				                }
				                data.voucherLines = voucherLines;
				                console.log("提交数据：",data)
				                GLOBAL.ajax("voucher/detail/update", JSON.stringify(data), function(e){
				                  if(e.success){
				                      layer.msg("保存成功！", {
				                         icon: 1,
				                         time: 1500
				                     },function(){
				                         GLOBAL.go("html/fi/voucher/voucherEdit.html");
				                     })
				                  }
				               }, true);
				             }
				    });
					$('#js-lineTbody').find('tr').each(function(index , self) {
						$(self).data("voucherLine" , voucherLines[index]);
					});
					GLOBAL.ajax("common/selected/query?key=DEFAULT_CURRENCY_TYPE", null, function(data) {
	                    if (data) {
	                        var items = data.items;
	                        if (items && items.length > 0) {
	                        	var options = "";
	                            for (var k = 0; k < items.length; k++) {
	                                options += "<option value='" + items[k].code + "'>" + items[k].value + "</option>";
	                            }
	                            $("#js-lineTbody").find("select[name=currencyType]").each(function(){
	                            	var data = $(this).closest('tr').data("voucherLine");
	            					var attr_name = $(this).attr("name");
	            					$(this).append(options).val(data[attr_name]);
	                            });
	                        }
	                    }
	                });
					initSelectInfo();
					$currentTr = $("#js-lineTbody tr:first");
					$currentTr.click();
					$("#js-lineTbody").find("select[name=accountLineId]").each(function(){
						var _self = $(this);
						var $tr = _self.closest("tr");
						var voucherLine = $tr.data("voucherLine");
						_self.val(voucherLine.accountLineId);
					});
				}
			}
		} else {
			layer.msg(e.msg);
		}
	});
	//帐套联动数据   科目 项目 账号
	function initSelectInfo () {
		var busiUnit = null;
		GLOBAL.ajax("accountBook/line/rel/all/query", {accountBookId: $("#accountBookId").val()}, function(data){
			var $tr = $("#js-lineTbody").find("tr") ;
            if (data) {
                var items = data.items;
                var options = "";
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
                $tr.find("select[name=accountLineId]").each(function(){
                	$(this).empty().html(options);
                });
            }else {
            	layer.msg("查询帐套下的科目失败");
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
	}
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
		$("#js-lineTable tbody tr").each(function(idx,item){
			var debit = $(this).find("input[name=debitAmount]").val();
			var credit = $(this).find("input[name=creditAmount]").val();
			var standardDebit = $(this).find("input[name=standardDebitAmount]").val();
			var standardCredit = $(this).find("input[name=standardCreditAmount]").val();
			if(debit){
				debitSum = debitSum + parseFloat(debit);
			}
			if(credit){
				creditSum = creditSum + parseFloat(credit);
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
		var data = $tr.data("voucherLine");
		data.projectId = "";
		data.userId = "";
		data.accountId = "";
		data.custId = "";
		data.supplierId = "";
		data.taxRate = "";
		data.innerBusiUnitId = "";
		$tr.data("voucherLine" , data);
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
			var accountStr = data.needAccount == 1 ? "<em class='text-star'>*</em>账号: " : "账号";
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
		} else {
			removeStar();
		}
	}
	function queryStandardCoinInfo($tr) {
		var debitAmount = $tr.find("input[name=debitAmount]").val();
        var creditAmount = $tr.find("input[name=creditAmount]").val();
        var currencyType = $tr.find("select[name=currencyType]").val();
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
				$tr.find("input[name=standardDebitAmount]").val(data.items.standardDebitAmount);
				$tr.find("input[name=standardCreditAmount]").val(data.items.standardCreditAmount);
				$tr.find("input[name=standardCoinName]").val(data.items.standardCoinName);
			}
		});
	}
}
GLOBAL.goBack();