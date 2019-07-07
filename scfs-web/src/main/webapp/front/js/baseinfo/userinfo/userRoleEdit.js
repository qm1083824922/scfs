window.operateEvents = {
		
    'click .invalid': function(e, value, row, index){
    	var id = row.id;
 	    layer.confirm('确定要作废吗？', {
 	        btn: ['确定', '取消'] //按钮
 	    }, function() {
 	    	GLOBAL.ajax("user/role/delete", {
 	    		id: id,
 	    		userId : options.param.id},
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
          	     title: '已分配角色',
          	     field: 'name',
          	     width: 92,
          	     align: 'center'
          	 }, {
          	     title: '分配人',
          	     field: 'creator',
          	     width: 85,
          	     align: 'center'
          	 }, {
          	     title: '分配时间',
          	     field: 'createAt',
          	     width: 80,
          	     align: 'center'
          	 }, {
          	     title: '作废人',
          	     field: 'deleter',
          	     width: 80,
          	     align: 'center'
          	 }, {
          	     title: '作废时间',
          	     field: 'deleteAt',
          	     width: 80,
          	     align: 'center'
          	 }, {
          	     title: '状态',
          	     field: 'state',
          	     width: 100,
          	     align: 'center'
          	 }
];


options.initPage = function(){
	GLOBAL.initTable($('#js_dataTable'), tabCols, {userId: options.param.id});
  };

$("#back").click(function(){
    var url = $(this).data("url");
    options.param.id=options.param.cid;
    GLOBAL.go(url, options.param);
})
$("#newbuild").click(function(){
    var url = $(this).data("url");
    options.param.id=options.param.cid;
    GLOBAL.go(url, options.param);
})
 // 批量分配
$("#batchDelete").click(function() {
	
	var ids =  GLOBAL.selectIds($('#js_dataTable'));
	
	if (ids) {
		var url = $(this).data("url");
		var data = {
			ids : ids ,
			userId : options.param.id
		}
		layer.confirm('确定要作废吗？', {
			btn : [ '确定', '取消' ]
		// 按钮
		}, function() {
			console.log(88888,data);
			GLOBAL.ajax(url, data, function(e) {
				if (e.success) {
					var data = {
							userId : options.param.id
					} 
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