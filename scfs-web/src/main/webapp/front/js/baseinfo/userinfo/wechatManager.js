window.operateEvents = {
    'click .divide': function(e, value, row, index){
    	var id = row.id;
 	    layer.confirm('确定要解绑吗？', {
 	        btn: ['确定', '取消'] //按钮
 	    }, function() {
 	    	GLOBAL.ajax("/user/wechat/unbind", {
 	    		ids: id,
 	    		userId : options.param.id
 	    		}, function(){
 	    		layer.msg("绑定成功", {  icon: 1 }) ; 
 	    	    options.param.id=options.param.cid;
 	    	    GLOBAL.go("html/baseinfo/userinfo/wechatEdit.html", options.param);
 	    	})
 	    }, function() {});
    }
};
function operateFormatter(value, row, index) {
	    return [
	            '<a class="divide" href="javascript:void(0)">',
	            '<span class="btn btn-primary btn-sm" data-permissionUrl="/user/wechat/unbind" >解绑</span>',
	            '</a>  '
	        ].join('');
}/*数据表格*//*数据表格*/
var tabCols = [
	 {  
	     title: '序号',
	     field: 'columnsNumber',  
	     width: 10
     },{
	     title: '微信昵称',
	     field: 'nickname',
	     width: 92,
	     align: 'center'
	 }, {
        title: '真实姓名',
        field: 'realName',
        width: 92,
        align: 'center'
    },{
	     title: '性别',
	     field: 'sexName',
	     width: 85,
	     align: 'center'
	 }, {
	     title: '语言',
	     field: 'language',
	     width: 80,
	     align: 'center'
	 }, {
	     title: '城市',
	     field: 'city',
	     width: 100,
	     align: 'center'
	 }, {
	     title: '用户所在省',
	     field: 'province',
	     width: 100,
	     align: 'center'
	 }, {
	     title: '用户头像',
	     field: 'headimgurl',
	     width: 60,
	     align: 'center',
	     cellStyle: function(value, row, index){
	    	 return {
	    		 css: {
	    			 padding: 0
	    		 }
	    	 }
	     },
	     formatter : function(value , row , index) {
				return  "<a href="+value+" target='_blank'><img src="+value+" height='60' width='60'/></a>";
			}
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
	GLOBAL.initTable($('#js_dataTable'), tabCols, {userId: options.param.id},false, {
        onPostBody: function(){
            if($(".divide").length){
                GLOBAL.permissionBtn($(".divide .btn"));
            }
        }
    }); 
  };

$(".tab-item").click(function(){
    var url = $(this).data("url");
    GLOBAL.go(url, options.param);
})
$("#newbuild").click(function(){
    var url = $(this).data("url");
    options.param.id=options.param.cid;
    GLOBAL.go(url, options.param);
}) 
GLOBAL.goBack();

$("#back").click(function(){
    var url = $(this).data("url");
    options.param.id=options.param.cid;
    GLOBAL.go(url, options.param);
}) 