options.initPage = function() {
	var callback = function(reply) {
		var data = reply.items;

		for ( var i in data) {
			var value = data[i];
			$("#" + i).text(value);
		}
	}
	GLOBAL.ajax("accountBook/detail", options.param, callback);

	$("#back").click(function() {
		var url = $(this).data("url");
		GLOBAL.go(url, options.param);
	});
}
