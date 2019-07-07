/*数据表格*/
var tabCols = [
               {  
          	     title: '序号',
          	     field: 'columnsNumber',  
          	     width: 10
             },{
          	     title: '类型',
          	     field: 'typeName',
          	     align: 'center'
          	 },{
          	     title: '单据来源',
          	     field: 'billSourceName',
          	     align: 'center'
          	 }, {
          	     title: '单号',
          	     field: 'billNo',
          	     align: 'center'
          	 }, {
          	     title: '客户',
          	     field: 'customerName',
          	     align: 'center'
          	 }, {
          	     title: '供应商',
          	     field: 'supplierName',
          	     align: 'center'
          	 }, {
          	     title: '创建日期',
          	     field: 'createAt',
          	     align: 'center'
          	 }, {
          	     title: '记账日期',
          	     field: 'businessDate',
          	     align: 'center'
          	 }, {
          	     title: '单据金额',
          	     field: 'billAmountValue',
          	     align: 'center'
          	 }, {
          	     title: '单据币种',
          	     field: 'billCurrencyTypeName',
          	     align: 'center'
          	 }, {
        	     title: '资金金额',
        	     field: 'projectAmountValue',
        	     align: 'center'
        	 }, {
          	     title: '汇率',
          	     field: 'billCnyExchangeRate',
          	     align: 'center'
          	 }, {
          	     title: '金额(CNY)',
          	     field: 'cnyAmountValue',
          	     align: 'center'
          	 }, {
          	     title: '备注',
          	     field: 'remark',
          	     align: 'center'
          	 }
];
options.initPage = function(){
	GLOBAL.initTable($('#js_dataTable'), tabCols, {id: options.param.id});
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
function getCondition() {
	return "?id=" + options.param.id ;
}
$("#btnExport").click(function() {
    var url = $(this).data("url");
	url = url + getCondition();
	location.href = GLOBAL.host + url;
})