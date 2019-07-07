window.operateEvents = {
		
    'click .divide': function(e, value, row, index){
    	var id = row.id;
 	    layer.confirm('确定要分配吗？', {
 	        btn: ['确定', '取消'] //按钮
 	    }, function() {
 	    	GLOBAL.ajax("user/project/add", {
 	    		ids: id,
 	    		userId : options.param.id
 	    		}, function(){
 	    		layer.msg("分配成功", {  icon: 1 }) ; 
 	    	    options.param.id=options.param.cid;
 	    	    GLOBAL.go("html/baseinfo/userinfo/newBuildProject.html", options.param);
 	    	})
 	    }, function() {});
    }
};
function operateFormatter(value, row, index) {
	    return [
	            '<a class="divide" href="javascript:void(0)">',
	            '<span class="btn btn-primary btn-sm" data-permissionUrl="/user/project/add" >分配</span>',
	            '</a>  '
	        ].join('');
	}
/*数据表格*/
var tabCols = [
	 {
         field: 'checkItem',
         checkbox: true,
     },{  
	     title: '序号',
	     field: 'columnsNumber',  
	     width: 10
     },{
	     title: '经营单位',
	     field: 'businessUnit',
	     width: 92,
	     align: 'center'
	 }, {
	     title: '未分配项目',
	     field: 'projectName',
	     width: 85,
	     align: 'center'
	 },{
	     title: '操作',
	     field: 'operate',
	     width: 100,
	     align: 'center',
	     events: operateEvents,
         formatter: operateFormatter
	 }
];
options.initPage = function(){
	GLOBAL.initTable($('#js_dataTable'), tabCols, 
			{userId: options.param.id}, false, {
	             onPostBody: function(){
	                 if($(".divide").length){
	                     GLOBAL.permissionBtn($(".divide .btn"));
	                 }
	             }
	         });
 };
$("#back").click(function(){
    var url = $(this).data("url");
    options.param.id=options.param.cid;
    GLOBAL.go(url, options.param);
})
$("#btnSearch").click(function() {
	var data = {
			userId: options.param.id,
			projectNo:$('#projectNo').val(),
			businessUnit:$('#businessUnit').val(),
			projectName:$('#projectName').val()

	};
    GLOBAL.tableRefresh($('#js_dataTable'), data);
})   
// 批量分配
$("#dispatcher").click(function() {
	var ids =  GLOBAL.selectIds($('#js_dataTable'));
	

	if (ids) {
		var url = $(this).data("url");
		var data = {
			ids : ids ,
			userId : options.param.id
		}
		layer.confirm('确定要分配吗？', {
			btn : [ '确定', '取消' ]
		// 按钮
		}, function() { 
			GLOBAL.ajax(url, data, function(e) {
				if (e.success) {
					var data = {
							userId : options.param.id
					}
					$('#js_dataTable').bootstrapTable('refresh', {
						query : data
					});
					layer.msg("批量分配成功！");
					options.param.id=options.param.cid;
	 	    	    GLOBAL.go("html/baseinfo/userinfo/newBuildProject.html", options.param);
				} else {
					layer.msg(e.msg);
				}
			});
		}, function() {
		});
	}
});
