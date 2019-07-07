 
/*数据表格*/
var tabCols = [
	  {  
	     title: '序号',
	     field: 'columnsNumber',  
	     width: 10
     },{
	     title: '费用编号',
	     field: 'feeNo',
	     width: 92,
	     align: 'center'
	 }, {
	     title: '费用类型',
	     field: 'feeTypeName',
	     width: 85,
	     align: 'center'
	 }, {
	     title: '费用日期',
	     field: 'feeDate',
	     width: 80,
	     align: 'center'
	 }, {
	     title: '费用金额',
	     field: 'provideInvoiceAmount',
	     width: 100,
	     align: 'center',
	     formatter: function(value,row,index){ 
	    	 return value.toThounds();
	     }
	 } , {
	     title: '未税金额',
	     field: 'exRateAmount',
	     width: 100,
	     align: 'center',
	     formatter: function(value,row,index){ 
	    	 return value.toThounds();
	     }
	 },{
	     title: '税额',
	     field: 'rateAmount',
	     width: 100,
	     align: 'center',
	     formatter: function(value,row,index){ 
	    	 return value.toThounds();
	     }
	 }
];

options.initPage = function(){  
	var status = options.param.status;
	if(status==5){
		$(".nav>li.state").show();	
	}
	GLOBAL.initTable($('#js_dataTable'), tabCols, {id: options.param.id}, null ,{
		onLoadSuccess: function(data){
			if(data.options.feeTotalAmount != null){ 
				$('#js_dataTable tbody').append("<tr><td style='text-align:left' colspan='" + tabCols.length + "'><b>合计：费用总金额："+data.options.feeTotalAmount.toThounds()+"&nbsp;"+data.options.currency
						+",未税总金额:"+ data.options.exRateTotalAmount.toThounds()+data.options.currency+ ",总税额:"+ data.options.rateTotalAmount.toThounds()+data.options.currency+ "</b></td></tr>");
			}
		}
	}); 
	//还原查询条件及过滤后的数据
//	GLOBAL.restoreQuery($('#js_dataTable'));
  };

$("#back").click(function(){
    var url = $(this).data("url");
    options.param.id=options.param.cid;
    GLOBAL.go(url, options.param);
}) 