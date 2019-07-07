function  showTab(receiptType){
  if(receiptType == 1 || receiptType == 2 ||  receiptType == 3){
		$(".nav>li.one").show();
    	$(".nav>li.two").show();
    	$(".nav>li.three").hide();
    	$(".nav>li.four").hide();
    	$(".nav>li.five").hide();
  }	
  if(receiptType ==7  || receiptType == 8){
	$(".nav>li.one").hide();
  	$(".nav>li.two").show();
  	$(".nav>li.three").show();
  	$(".nav>li.four").hide();
  	$(".nav>li.five").hide();
}	
  if(receiptType ==5){
		$(".nav>li.one").hide();
	  	$(".nav>li.two").hide();
	  	$(".nav>li.three").hide();
	  	$(".nav>li.four").hide();
	  	$(".nav>li.five").hide();
	}	
  if(receiptType ==9 || receiptType ==10){
	  	$(".nav>li.one").hide();
	  	$(".nav>li.two").hide();
	  	$(".nav>li.three").hide();
	   	$(".nav>li.four").show();
	  	 $(".nav>li.five").show();
	}	
  if(receiptType==6){
		$(".nav>li.one").show();
	  	$(".nav>li.two").hide();
	  	$(".nav>li.three").hide();
	  	$(".nav>li.four").hide();
	  	$(".nav>li.five").hide();
	 }
}