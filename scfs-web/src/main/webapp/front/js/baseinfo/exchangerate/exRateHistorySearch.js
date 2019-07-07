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
        align: 'center'
    }, {
        title: '原币种',
        field: 'currency',
        width: 85,
        align: 'center'
    }, {
        title: '目标币种',
        field: 'foreignCurrency',
        width: 85,
        align: 'center'
    }, {
        title: '现钞卖出价',
        field: 'cashSellingPrice',
        width: 80,
        align: 'center'
    }, {
        title: '现钞买入价',
        field: 'cashBuyingPrice',
        width: 80,
        align: 'center'
    }, {
        title: '电汇卖出价',
        field: 'draftSellingPrice',
        width: 80,
        align: 'center'
    }, {
        title: '电汇买入价',
        field: 'draftBuyingPrice',
        width: 80,
        align: 'center'
    }, {
        title: '发布时间',
        field: 'publishAt',
        width: 96,
        align: 'center'
    }, {
        title: '创建人',
        field: 'creator',
        width: 110,
        align: 'center'
    }, {
        title: '备份时间',
        field: 'backupAt',
        width: 72,
        align: 'center'
    }, {
        title: '备份人',
        field: 'backupPerson',
        width: 72,
        align: 'center'
    }
];


options.initPage = function(){
    GLOBAL.initTable($('#js_dataTable'), tabCols, null);
}

$("#btnSearch").click(function() {
    var data = $("#searchForm").serializeObject();
    GLOBAL.tableRefresh($('#js_dataTable'), data);
})
GLOBAL.goBack();


