
window.operateEvents = {
    'click .submit': function (e, value, row, index) {
    	var id = row.id;
	    if(id){
	    	layer.confirm('确定要提交吗？', {
		        btn: ['确定', '取消'] //按钮
		    }, function() {
		         GLOBAL.ajax("distributeGoods/submit", {id: id}, function(e) {
	                if (e.success) {
	                    layer.msg("提交成功！");
	                    $("#btnSearch").click();
	                } else {
	                    layer.msg(e.msg);
	                }
	            });
		    }, function() {});
	    }
    },
    'click .delete': function (e, value, row, index) {
        var id = row.id;
	    layer.confirm('确定要删除吗？', {
	        btn: ['确定', '取消'] //按钮
	    }, function() {
	    	GLOBAL.ajax("distributeGoods/delete", {id: id}, function(){
	    		layer.msg("删除成功", {
	                icon: 1
	            })
	            $("#btnSearch").click();
	    	})
	    }, function() {});
    },
    'click .edit': function(e, value, row, index){
    	options.param.id = row.id;
		options.param.cid = row.id;
		GLOBAL.go("html/baseinfo/distribute/distributeGoodsEdit.html", options.param);
    },
    'click .scan': function(e, value, row, index){
		options.param.id = row.id;
		GLOBAL.go("html/baseinfo/distribute/distributeGoodsScan.html", options.param);
    }, 'click .lock': function(e, value, row, index){
    	var id = row.id;
 	    layer.confirm('确定要锁定吗？', {
 	        btn: ['确定', '取消'] //按钮
 	    }, function() {
 	    	GLOBAL.ajax("distributeGoods/lock", {id: id}, function(){
 	    		layer.msg("锁定成功", {
 	                icon: 1
 	            })
 	            $("#btnSearch").click();
 	    	})
 	    }, function() {});
    },
    'click .unlock': function(e, value, row, index){
    	var id = row.id;
 	    layer.confirm('确定要解锁吗？', {
 	        btn: ['确定', '取消'] //按钮
 	    }, function() {
 	    	GLOBAL.ajax("distributeGoods/unlock", {id: id}, function(){
 	    		layer.msg("解锁成功", {
 	                icon: 1
 	            })
 	            $("#btnSearch").click();
 	    	})
 	    }, function() {});
    },
    'click .copy': function(e, value, row, index){
    	options.param.id = row.id;
		options.param.cid = row.id;
		options.param.isCopy = true;
		GLOBAL.go("html/baseinfo/distribute/distributeGoodsCopy.html", options.param);
    }
};/*数据表格*/
var tabCols = [
	 {  
	     title: '序号',
	     field: 'columnsNumber',  
	     width: 10
     },{
	     title: '编号',
	     field: 'number',
	     width: 72,
	     align: 'center'
	 }, {
	     title: '名称',
	     field: 'name',
	     width: 110,
	     align: 'center'
	 }, {
	     title: '事业部',
	     field: 'departmentName',
	     width: 100,
	     align: 'center'
	 }, {
	     title: '供应商',
	     field: 'supplierName',
	     width: 80,
	     align: 'center'
	 }, {
	     title: '质押比例',
	     field: 'pledge',
	     width: 80,
	     align: 'center'
	 }, {
	     title: '品牌',
	     field: 'brand',
	     width: 110,
	     align: 'center'
	 }, {
	     title: '型号',
	     field: 'type',
	     width: 80,
	     align: 'center'
	 }, {
	     title: '条码',
	     field: 'barCode',
	     width: 100,
	     align: 'center'
	 }, {
	     title: '规格',
	     field: 'specification',
	     width: 100,
	     align: 'center'
	 }, {
	     title: '税收分类',
	     field: 'taxClassification',
	     width: 100,
	     align: 'center'
	 }, {
	     title: '国内税率',
	     field: 'taxRate',
	     width: 100,
	     align: 'center'
	 },  {
	     title: '单位',
	     field: 'unit',
	     width: 100,
	     align: 'center'
	 }, {
	     title: '高',
	     field: 'volume',
	     width: 100,
	     align: 'center',
	     formatter : function(value , row , index) {
	    	 return (value==null ? "" : value) +"cm" ;
	     }
	 }, {
	     title: '长',
	     field: 'grow',
	     width: 100,
	     align: 'grow',
	     formatter : function(value , row , index) {
	    	 return (value==null ? "" : value) +"cm" ;
	     }
	 }, {
	     title: '宽',
	     field: 'broad',
	     width: 100,
	     align: 'center',
	     formatter : function(value , row , index) {
	    	 return (value==null ? "" : value) +"cm" ;
	     }
	 }, {
	     title: '毛重',
	     field: 'grossWeight',
	     width: 100,
	     align: 'center',
	     formatter : function(value , row , index) {
	    	 return (value==null ? "" : value) +"kg" ;
	     }
	 
	 }, {
	     title: '净重',
	     field: 'netWeight',
	     width: 100,
	     align: 'center',
	     formatter : function(value , row , index) {
	    	 return (value==null ? "" : value) +"kg" ;
	     }
	 }, {
	     title: '采购指导价',
	     field: 'purchasePriceValue',
	     width: 100,
	     align: 'center' 
	 }, {
	     title: '销售指导价',
	     field: 'salePriceValue',
	     width: 100,
	     align: 'center'
	 }, {
	     title: '创建人',
	     field: 'creator',
	     width: 100,
	     align: 'center'
	 }, {
	     title: '创建时间',
	     field: 'creatorAt',
	     width: 100,
	     align: 'center'
	 },{
	     title: '状态',
	     field: 'statusName',
	     width: 100,
	     align: 'center'
	 }, {
	     title: '操作',
	     field: 'opertaList',
	     width: 100,
	     align: 'center',
	     events: operateEvents
	 }
];

options.initPage = function() {
	
	GLOBAL.initOptionsParam('id,cid,hip');
	GLOBAL.initTable($('#js_dataTable'), tabCols, null); 
	
	//还原查询条件及过滤后的数据
	GLOBAL.restoreQuery($('#js_dataTable'));
}

$("#btnSearch").click(function() {
	var data = $("#searchForm").serializeObject();
	if(data){
		console.log("商品查询参数：", data)
		var url = $('#js_dataTable').data("url");
		GLOBAL.local(url, data);
	}
    GLOBAL.tableRefresh($('#js_dataTable'), data);
}) 
    
$("#btnNew").click(function() {
    var url = $(this).data("url");
    GLOBAL.go(url, options.param);
})

$("#btnImport").click(function() {
    var url = $(this).data("url");
    GLOBAL.go(url, options.param);
})


$("#btnExport").click(function() {
	var url = "distributeGoods/excel/gistribution_goods_list.xls";
	url = url + "?" + $("#searchForm").serialize();
	location.href = GLOBAL.host + url;
})



