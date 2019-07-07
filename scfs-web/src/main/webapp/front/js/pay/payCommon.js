function showTab(payType, state, noneOrderFlag) {
	if (payType == 1) {
		$(".nav>li.li-po").show();	
		$(".nav>li.li-fee").hide();
		$(".nav>li.li-refund").hide();
		$(".nav>li.li-advance").show();
		$(".nav>li.li-deduction-fee").show();
		$(".nav>li.li-attachment").show();
	}
	
	if (payType == 2) {
		$(".nav>li.li-po").hide();	
		$(".nav>li.li-fee").show();
		$(".nav>li.li-refund").hide();
		$(".nav>li.li-advance").show();
		$(".nav>li.li-deduction-fee").hide();
		$(".nav>li.li-attachment").show();
	}
	
	if (payType == 3 || payType == 4 || payType == 5) {
		$(".nav>li.li-po").hide();	
		$(".nav>li.li-fee").hide();
		$(".nav>li.li-refund").hide();
		$(".nav>li.li-advance").show();
		$(".nav>li.li-deduction-fee").hide();
		$(".nav>li.li-attachment").show();
	}
	
	if (payType == 6) {
		$(".nav>li.li-po").hide();	
		$(".nav>li.li-fee").hide();
		$(".nav>li.li-refund").show();
		$(".nav>li.li-advance").hide();
		$(".nav>li.li-deduction-fee").hide();
		$(".nav>li.li-attachment").show();
	}
	
	if (state == 6) {
		$(".nav>li.li-voucher").show();	
	} else {
		$(".nav>li.li-voucher").hide();	
	}
	if (noneOrderFlag == 1 && state != 6) {
    	$(".nav>li.li-po").hide();
    	$(".nav>li.li-fee").hide();
    	$(".nav>li.li-refund").hide();
    	//$(".nav>li.li-advance").hide();
    	$(".nav>li.li-deduction-fee").hide();
    }
}