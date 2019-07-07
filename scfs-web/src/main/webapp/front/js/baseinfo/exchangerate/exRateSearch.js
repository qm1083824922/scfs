window.operateEvents = {
       'click .edit': function(e, value, row, index){
        var id = $(this).closest("tr").data('uniqueid');
        options.param.id = id;
        GLOBAL.go("html/baseinfo/exchangerate/exRateEdit.html");
    }
};



/*数据表格*/
var tabCols = [
   {
        title: '序号',
        field: 'columnsNumber',
        width: 10
    }, {
        title: '银行名称',
        field: 'bank',
        width: 100,
        align: 'center',            
    }, {
        title: '原币种',
        field: 'currency',
        width: 85,
        align: 'center',            
    }, {
        title: '目标币种',
        field: 'foreignCurrency',
        width: 85,
        align: 'center',            
    }, {
        title: '现钞卖出价',
        field: 'cashSellingPrice',
        width: 80,
        align: 'center',            
    }, {
        title: '现钞买入价',
        field: 'cashBuyingPrice',
        width: 80,
        align: 'center',            
    }, {
        title: '电汇卖出价',
        field: 'draftSellingPrice',
        width: 80,
        align: 'center',            
    }, {
        title: '电汇买入价',
        field: 'draftBuyingPrice',
        width: 80,
        align: 'center',            
    }, {
        title: '发布时间',
        field: 'publishAt',
        width: 96,
        align: 'center',            
    }, {
        title: '创建人',
        field: 'creator',
        width: 110,
        align: 'center',            
    }, {
        title: '创建时间',
        field: 'createAt',
        width: 72,
        align: 'center',
    }, {
        title: '操作',
        field: 'opertaList',
        width: 160,
        align: 'center',
        events: operateEvents
    }
];


options.initPage = function(){
    GLOBAL.initTable($('#js_dataTable'), tabCols, null, null, {
        heightLight: function(value, row, index) {
            if(row.isError.code == "2"){
                return "<span style='color:red'>" + value + "</span>";
            }else{
                return value;
            }
        }
    });

    //还原查询条件及过滤后的数据
    GLOBAL.restoreQuery($('#js_dataTable'));
}

$("#btnSearch").click(function() {
    var data = $("#searchForm").serializeObject();
    var url = $('#js_dataTable').data("url");
    GLOBAL.local(url, data);
    
    GLOBAL.tableRefresh($('#js_dataTable'), data);
})

$("#btnNew").click(function() {
    var url = $(this).data("url");
    GLOBAL.go(url, options.param);
})

$("#btnHistory").click(function() {
    var url = $(this).data("url");
    GLOBAL.go(url, options.param);
})

