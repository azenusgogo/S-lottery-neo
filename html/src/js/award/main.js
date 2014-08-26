define([
    "award/ssq",
    "award/dlt",
    "award/ssq_ball",
    "award/dlt_ball"
],function(ssq,ssq_ball,dlt,dlt_ball){

    return function(){
    	var $tab = $(".funtab a");
    	if($tab.length == 0) return false;
    	$tab.on("click",function(){
    		$(this).addClass("cur").siblings().removeClass("cur");
    		$(".t-box").eq($(this).index()).show().siblings(".t-box").hide();
    		return false;
    	});

        ssq();
        ssq_ball();
        dlt();
        dlt_ball();
    }
})