options.initPage = function(){
	
};

$("#js-back").click(function(){
    var url = $(this).data("url");
    GLOBAL.go(url, options.param);
});

$("#download").click(function(){
	var url = $(this).data("url");
	location.href = GLOBAL.host + url;
});

$("#upload").upload({
 	url: GLOBAL.host + "/goods/excel/import",
 	fileType: "xls, xlsx",
 	isMultiple: false,
	success: function(){
		
    }
}); 