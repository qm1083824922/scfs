window.operateEvents = {
		 'click .divide': function(e, value, row, index){
		    	var id = row.id;
		 	    layer.confirm('确定要分配吗？', {
		 	        btn: ['确定', '取消'] //按钮
		 	    }, function() {
		 	    	GLOBAL.ajax("permission/divid", {
		 	    		permissionIds: id,
		 	    		permissionGroupId : options.param.id
		 	    		}, function(){
		 	    		layer.msg("分配成功", {  icon: 1 }) ; 
		 	    	    options.param.id=options.param.cid;
		 	    	    GLOBAL.go("html/baseinfo/authorityGroup/distributeAuthority.html", options.param);
		 	    	})
		 	    }, function() {});
		    }
};
function operateFormatter(value, row, index) {
    return [
            '<a class="divide" href="javascript:void(0)">',
            '<span class="btn btn-primary btn-sm" data-permissionUrl="/permission/divid">分配</span>',
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
	 }, {
	     title: '权限类型',
	     field: 'typeName',
	     width: 100,
	     align: 'center'
	 }, {
	     title: '菜单级别',
	     field: 'menuLevelName',
	     width: 100,
	     align: 'center'
	 }, {
	     title: '所属一级菜单',
	     field: 'parentName',
	     width: 100,
	     align: 'center'
	 },  {
	     title: '操作',
	     field: 'operta',
	     width: 100,
	     align: 'center',
	     events: operateEvents,
         formatter: operateFormatter
	 }
];

options.initPage = function(){
	GLOBAL.initTable($('#js_dataTable'), tabCols, {permissionGroupId: options.param.id},false, {
        onPostBody: function(){
            if($(".divide").length){
                GLOBAL.permissionBtn($(".divide .btn"));
            }
        }
    });
	var option = "<option value=''>请选择</option>";
    GLOBAL.ajax("first/menu/query", null, function(data){
        for(var k = 0; k < data.length; k++){
            option += '<option value="'+data[k].id+'">'+data[k].name+'</option>';
        }   
        $("#parentId").empty().append(option);
    })
  };

$("#btnSearch").click(function() {
	var data ={
			permissionGroupId: options.param.id,
			name:$('#name').val(),
			url:$('#url').val(),
			type:$('#type').val(),
			menuLevel:$('#menuLevel').val(),
			parentId:$('#parentId').val()
	};
    GLOBAL.tableRefresh($('#js_dataTable'), data);
})    

$("#back").click(function() {
    var url = $(this).data("url");
    GLOBAL.go(url, options.param);
})
// 批量分配
$("#dispatcher").click(function() {
	
	var ids =  GLOBAL.selectIds($('#js_dataTable'));
	if (ids) {
		var url = $(this).data("url");
		var data = {
				permissionIds : ids ,
			permissionGroupId : options.param.id
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
	 	    	    GLOBAL.go("html/baseinfo/authorityGroup/distributeAuthority.html", options.param);
				} else {
					layer.msg(e.msg);
				}
			});
		}, function() {
		});
	}
	
});