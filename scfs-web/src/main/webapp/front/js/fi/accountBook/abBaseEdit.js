options.initPage = function() {
	GLOBAL.getEditData($("#form1"), options.param.id);

	$("#back").click(function() {
		var url = $(this).data("url");
		GLOBAL.go(url);
	});

	$("#form1").validate({
		rules : {
			busiUnit : {
				required : true
			},
			fiNo : {
				required : true
			},
			accountBookName : {
				required : true
			},
			isHome : {
				required : true
			},
			auditorId : {
				required : true
			}
		},
		submitHandler : function() {
			var data = $("#form1").serializeObject();
			GLOBAL.ajax("accountBook/update", data, function(e) {
				if (e.success) {
					layer.msg("修改成功！");
				} else {
					layer.msg(e.msg);
				}
			});
		}
	});
}
