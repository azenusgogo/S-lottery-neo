define(['lib/goTop'],function(goTop){

	var $el = $("#help_center");
    if ($el.length == 0) return function(){};
    // ==========================================================
    var _$ = function(selector) {
        return $el.find(selector)
    };

    return function(){
    	var $mTop = _$(".help_menu .arr");
    	var $cTab = _$(".hright .tab span");
    	var $cont = _$(".cont");
    	var $li = _$(".help_menu li");
    	var $w  = window;
    	var nowUrl = $w.location.pathname;

        // if(nowUrl != "/help/"){ //非帮助中心首页时判断当前目录

        //     if(nowUrl == "/help/problem/all.html"){
        //         _$(".self").addClass("cur");
        //     }else{
        // 		$li.each(function(){
        // 			var $this = $(this);
        // 			var $par = $this.parent();
        // 			var link = $this.find("a").attr("href");

        // 			if(nowUrl == link){
        // 				$this.addClass("cur");
        // 				$par.prev().addClass("cur");
        // 				$par.slideDown();
        // 				return;
        // 			}
        // 		});
        //     }
        // }

    	$mTop.on("click",function(){ //左侧菜单展开效果
    		var $this = $(this);
            if($this.hasClass("cur")){
                $this.removeClass("cur");
                $this.next().slideUp();
                return;
            }
    		$mTop.removeClass("cur");
    		$this.addClass("cur");
    		$this.next().slideDown().siblings("ul").slideUp();
    	});

    	$cTab.on("click",function(){ //内容区域Tab切换
    		var index = $(this).index();

    		$(this).addClass("cur").siblings().removeClass("cur");
    		$cont.hide();
    		$cont.eq(index).show();
    	});
    	 goTop.call($w,{offsetHeight:350});
    }
});