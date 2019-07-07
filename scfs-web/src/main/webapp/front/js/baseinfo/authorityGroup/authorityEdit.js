
window.operateEvents = {
 
    'click .invalid': function (e, value, row, index) {
        var id = row.id;
	    layer.confirm('确定要作废吗？', {
	        btn: ['确定', '取消'] //按钮
	    }, function() {
	    	GLOBAL.ajax("permission/invalid", {
	    		ids : id,
	    		permissionGroupId : options.param.id}, 
 	    		function(){
	    		layer.msg("作废成功！");
				$('#js_dataTable').bootstrapTable('refresh', {query: {id: options.param.id}});
	    	})
	    }, function() {});
    } 
};
function operateFormatter(value, row, index) {
    return [
            '<a class="invalid" href="javascript:void(0)">',
            '<span class="btn btn-warning btn-sm ml10">作废</span>',
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
	     title: '权限名',
	     field: 'name',
	     width: 92,
	     align: 'center'
	 }, {
	     title: 'URL',
	     field: 'url',
	     width: 85,
	     align: 'center'
	 },{
	     title: '权限类型',
	     field: 'typeName',
	     width: 92,
	     align: 'center'
	 }, {
	     title: '菜单级别',
	     field: 'menuLevelName',
	     width: 85,
	     align: 'center'
	 },{
	     title: '所属一级菜单',
	     field: 'parentName',
	     width: 85,
	     align: 'center'
	 } ,{
	     title: '状态',
	     field: 'state',
	     width: 85,
	     align: 'center'
	 }
];

options.initPage = function() {
	GLOBAL.initTable($('#js_dataTable'), tabCols, {permissionGroupId: options.param.id});
}   

$("#btnNew").click(function() {
    var url = $(this).data("url");
    GLOBAL.go(url, options.param);
})

$("#newbuild").click(function() {
    var url = $(this).data("url");
    GLOBAL.go(url, options.param);
})
$("#back").click(function() {
    var url = $(this).data("url");
    GLOBAL.go(url, options.param);
})
 // 批量分配
$("#batchDelete").click(function() {
	var ids =  GLOBAL.selectIds($('#js_dataTable'));
	if (ids) {
		var url = $(this).data("url");
		var data = {
			ids : ids 
		}
		layer.confirm('确定要作废吗？', {
			btn : [ '确定', '取消' ]
		// 按钮
		}, function() {
			console.log(88888,data);
			GLOBAL.ajax(url, data, function(e) {
				if (e.success) {
					var data = {
							permissionGroupId : options.param.id
					}
					$('#js_dataTable').bootstrapTable('refresh', {
						query : data
					});
					layer.msg("作废成功！");
					$('#js_dataTable').bootstrapTable('refresh', {query: {id: options.param.id}});
				} else {
					layer.msg(e.msg);
				}
			});
		}, function() {
		});
	}
});  