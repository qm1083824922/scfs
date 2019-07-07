window.operateEvents = {

    'click .divide': function(e, value, row, index){
    	var id = row.openid;
        options.param.id = id;
        GLOBAL.go("html/baseinfo/userinfo/wechatUser.html", options.param);
    }
};
function operateFormatter(value, row, index) {
	    return [
	            '<a class="divide" href="javascript:void(0)" >',
	            '<span class="btn btn-primary btn-sm" data-permissionUrl="/user/wechat/update" >绑定</span>',
	            '</a>  '
	        ].join('');
}/*数据表格*/
var tabCols = [
	 {
	     title: '序号',
	     field: 'columnsNumber',  
	     width: 10
     },{
	     title: '用户昵称',
	     field: 'nickname',
	     width: 92,
	     align: 'center'
	 }, {
        title: '真实姓名',
        field: 'realName',
        width: 92,
        align: 'center'
    }, {
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
	     width: 50,
	     align: 'center', 
	     cellStyle: {
	    	 padding:0
	     },
	     formatter : function(value , row , index) {
				return  "<a href="+value+" target='_blank'><img src="+value+" height='60' width='60'/></a>";
			}
	 } ,{
	     title: '操作',
	     field: 'operate',
	     width: 100,
	     align: 'center',
	     events: operateEvents,
         formatter: operateFormatter
	 }
];
options.initPage = function(){
	GLOBAL.initTable($('#js_dataTable'), tabCols, null ,false, {
        onPostBody: function(){
            if($(".divide").length){
                GLOBAL.permissionBtn($(".divide .btn"));
            }
        }
    });
 };

$("#btnSearch").click(function() {
	var data = {
			userName:$('#userName').val(),
        chineseName:$('#chineseName').val()
	};
    GLOBAL.tableRefresh($('#js_dataTable'), data);
})
