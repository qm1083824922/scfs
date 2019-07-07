 /*数据表格*//*数据表格*/
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
	 } 
];
options.initPage = function(){
	GLOBAL.initTable($('#js_dataTable'), tabCols, {userId: options.param.id}); 
  };

$(".tab-item").click(function(){
    var url = $(this).data("url");
    GLOBAL.go(url, options.param);
}) 
GLOBAL.goBack();

$("#back").click(function(){
    var url = $(this).data("url");
    options.param.id=options.param.cid;
    GLOBAL.go(url, options.param);
}) 