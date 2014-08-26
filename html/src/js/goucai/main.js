define(function (){

    var $el = $("#goucaiHall");
    if ($el.length == 0) return function(){};
    // ==========================================================
    var _$ = function(selector) {
        return $el.find(selector)
    };

    return function(){
        $list = _$(".gc-list li");
        var t = null;

        $list.hover(
            function(){
                var $this = $(this);
                t = setTimeout(function(){
                    $this.find(".go").animate({
                       top:5//同时向下移动4像素
                    },200)
                },200)
            },
            function(){
                clearTimeout(t);
                $(this).find(".go").animate({
                       top:178//同时向下移动7像素
                },200)
            }
        )
    }
});