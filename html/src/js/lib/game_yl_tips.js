define(function(){
    return function(){
        var tpl = '\
            <div class="ball-list-tips" style="display:none; z-index:1">\
                <div class="top1 ct"></div>\
                <div class="top2 ct"></div>\
                <div style="_zoom:1"><%=text%></div>\
            </div>\
        ';
        var $el1 = $(".game-yl-tip1,.game-yl-tip2");
        if ($el1.length) {
            $el1.hover(function(){
                var $this = $(this);
                var $el = $this.data("el");
                if (!$el){
                    var text = $this.hasClass('game-yl-tip2') ? "遗漏是指该号码自上次开出以来至本次未出现的期数<br>最大遗漏是指该号码在历史上的最大遗漏值"
                                                 : "遗漏是指该号码自上次开出以来至本次未出现的期数";
                    $this.append(_.template(tpl,{text:text}));
                    $el = $this.find(".ball-list-tips");
                    $el.css("top",$this.outerHeight() + 10);
                    $el.find(".top1,.top2").css("left",$this.outerWidth()/2 - 7)
                    $this.data("el",$el)
                }
                $el.show()
            },function(){
                $(this).data("el").hide()
            })
        }
    }
})