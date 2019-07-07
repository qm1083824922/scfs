window.operateEvents = {
    
    'click .scan': function(e, value, row, index){
		options.param.id = row.id;
		GLOBAL.go("html/project/projectPoolBaseInfo.html", options.param);
    } 
};/*数据表格*/
var tabCols = [
	 {  
	     title: '序号',
	     field: 'columnsNumber',  
	     width: 10
     },{
	     title: '项目',
	     field: 'projectname',
	     align: 'center'
	 }, {
	     title: '项目总额度',
	     field: 'projectAmount',
	     align: 'center'
	 }, {
	     title: '币种',
	     field: 'currencyTypeName',
	     align: 'center'
	 }, {
	     title: '资金占用',
	     field: 'usedFundAmount',
	     align: 'center'
	 }, {
	     title: '项目余额',
	     field: 'remainFundAmount',
	     align: 'center'
	 }, {
	     title: '资产总值',
	     field: 'usedAssetAmount',
	     align: 'center'
	 }, {
	     title: '项目额度(CNY)',
	     field: 'projectAmountCny',
	     align: 'center'
	 }, {
	     title: '资金占用(CNY)',
	     field: 'usedFundAmountCny',
	     align: 'center'
	 }, {
	     title: '项目余额(CNY)',
	     field: 'remainFundAmountCny',
	     align: 'center'
	 }, {
	     title: '资产总值(CNY)',
	     field: 'usedAssetAmountCny',
	     align: 'center'
	 }, {
	     title: '操作',
         field: 'opertaList',
         align: 'center'
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
		var url = $('#js_dataTable').data("url");
		GLOBAL.local(url, data);
	}
    GLOBAL.tableRefresh($('#js_dataTable'), data);
})    

$('#js_dataTable').on("click", ".scan", function() {
	var id = $(this).closest("tr").data('uniqueid');
	options.param.id = id;
	GLOBAL.go("html/project/projectPoolBaseInfo.html", options.param);
})