define(function(){
	return function(){
		var $holdspace = $("#holdspace");
 	    var $shreshold = $("#tbl");
        var _shreshold = 395;
        var $w = $(window);
        var _run = function(){
        	var me=arguments.callee;
        	var _scrollTop = $w.scrollTop();
      	   if(_scrollTop > _shreshold){
      		   if(!me.setCssed){
      			   $shreshold[0].style.cssText = "zoom:1;z-index:1;display:block;top:0;position:fixed;_position: absolute;_left:50%;_margin-left:-494px;_top:expression(0+((e=document.documentElement.scrollTop)?e:document.body.scrollTop)+'px'););";//expression(0+((e=document.documentElement.scrollLeft)?e:document.body.scrollLeft)
        		   //$holdspace.show();
      			   $("#matchBascket").css("margin-top","41px")
        		   me.setCssed = 1;
      		   }
      	   }else{	        		    
      		   $shreshold[0].style.cssText = "poistion:relative;"
      		   //$holdspace.hide();
      			 $("#matchBascket").css("margin-top","0px")
      		   me.setCssed = 0;
      	   }
      	   
        };
        $(window).on("scroll",function(){	
        	_run();
        });
	}
	
})