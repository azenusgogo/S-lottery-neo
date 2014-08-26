define([
    "text!ucenter/jc_table.tpl",
    "text!ucenter/jc_page.tpl",
    "lib/jquery.pagination"

],function (jc_table, jc_page){
    var $el = $("#jczq_order_detail");
    if ($el.length == 0) return function(){};

    // ==========================================================
    var _$ = function(selector) {
        return $el.find(selector)
    };

    return function(){
        var $table = _$(".match-detail");
        var $pagelist = _$(".tic-detail");
        var $page = _$(".u-page2");
        var orderId = $("#betOrderId").val();
        var pageSize = $("#betPageSize").val();
        var tpl_table = _.template(jc_table);
        var tpl_page = _.template(jc_page);

        _$(".tic-detail").on({//彩票标识码说明
            mouseenter:function(){
                var $this = $(this);
                var top = $this.offset().top+25;
                var left = $this.offset().left-50;
                $this.next().css({
                    top:top,
                    left:left,
                    display:"block"
                });
            },
            mouseleave:function(){
                $(this).next().hide();
            }
        },".cp_sign");

        var _render = function(index){

            if(_render.out == 1){
                _render.out = 2;
                return;
            } 

            $.post("/ajax/login/order/detail/sport.html",{"userOrderId":orderId,"pageNo":index+1,"pageSize":pageSize},function(data){
                if(data.retcode == 0){
                    data.nowPage = index;
                    var rolltop = 0,ret = data.result;
                    ret.nowPage = index;

                    $pagelist.html(tpl_page(ret));

                    if(_render.first){
                        rolltop = $("#selTit").offset().top;
                       $("html,body").animate({scrollTop:rolltop},300);
                    }

                    if (!_render.first){ //第一次载入时执行
                        _render.first = 1;
                        _render.out = 1;
                        _$(".bets_num").html(ret.orderAmount/200);
                        $("#gameCn").html("玩法：" + ret.gameCn);
                        $("#passWayCn").html("过关方式：" + ret.passWayCn);
                        $table.html(tpl_table(ret));

                        $page.pagination(data.result.totalCount,{
                            num_edge_entries: 1,
                            num_display_entries: 4,
                            prev_text:"上一页",
                            next_text:"下一页",
                            callback:_render
                        });
                    }
                }

            });

            return false;
        };

        _render.out = 0;
        _render(0);
        

    }
})