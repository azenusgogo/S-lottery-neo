define([
    "text!ucenter/bet_page.tpl",
    "lib/jquery.pagination"

],function(bet_page){
    var $el = $("#u_orderDetail");
    if ($el.length == 0) return function(){};

    // ==========================================================
    var _$ = function(selector) {
        return $el.find(selector)
    };

    return function(){

        var $page = _$(".u-page2");
        var $list = _$(".mynum_tbl_list");
        var rollTop = $("#selTit").offset().top;
        var betTotalNum = 0;
        var betOrderId = $("#betOrderId").val();
        var betPageSize = $("#betPageSize").val();
        var data = null;
        var k_red_v = $("#kaiRed").val() || "";
        var k_blue_v = $("#kaiBlue").val() || "";
        var k_hz_v = $("#kaiHz").val() || "";
        var tpl = _.template(bet_page);

        $(".mynum_tbl_list").on({//彩票标识码说明
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

        $(".mynum_tbl_list").on({//彩票标识码列表
            mouseenter:function(){
                var $this = $(this);
                var top = $this.offset().top+25;
                var left = $this.offset().left;
                $this.next().css({
                    top:top,
                    left:left,
                    display:"block"
                });
            },
            mouseleave:function(){
                $(this).next().hide();
            }
        },".cpsign_list");

        var _render = function(index){

            if(_render.out == 1){
                _render.out = 2;
                return;
            } 

            $.post("/ajax/login/user/order/stakebets.html",{"orderId":betOrderId,"pageNo":index+1,"pageSize":betPageSize},function(data){
                if(data.retcode == 0){
                    data.nowPage = index;
                    data.kRed = k_red_v.replace(/\W/g,"|");
                    data.kBlue = k_blue_v.replace(/\W/g,"|");
                    data.kHz = k_hz_v;
                    $list.html(tpl(data));

                    if(_render.first){
                       $("html,body").animate({scrollTop:rollTop},300); 
                    }

                    if (!_render.first){
                        _render.first = 1;
                        _render.out = 1;
                        _$(".bets_num").html(data.result.totalBetNums);
                        $page.pagination(data.result.totalNum,{
                            num_edge_entries: 1,
                            num_display_entries: 4,
                            prev_text:"上一页",
                            next_text:"下一页",
                            callback:_render
                        });
                    }
                }

            }).error(function(){

                // var data = {"retcode":0,"retdesc":"操作成功","result":{"orderId":"14050610UO0000002350","splitFlag":false,"prizeNumber":null,"userId":"sgcaipiao@sogou.com","totalBetNums":16,"orderInfo":{"userOrderId":"14050610UO0000002350","gameId":"k3gx","gameDesc":null,"periodNo":"140506008","followId":null,"followNo":null,"orderType":10,"orderTypeDesc":null,"orderStatus":0,"orderStatusDesc":null,"spiltFlag":false,"prizeStatus":0,"prizeStatusDesc":null,"officialBonus":0,"calculatedBonus":-1,"payerUserId":"sgcaipiao@sogou.com","bettorUserId":"sgcaipiao@sogou.com","bettorName":"黄涛","bettorIdno":"34052419800101001X","userOrderAmount":3200,"refundAmount":0,"deliverable":true,"betTimes":1,"sourceType":1,"totalBetNumbers":0,"succBetNumbers":0,"failedBetNumbers":0,"createTime":1399344253073,"deadline":null,"payOrderId":null},"pageNo":1,"pageSize":10,"totalNum":1,"totalPage":1,"stakeBetInfoDtoList":[{"betNumber":{"betNumberId":-1,"userOrderId":"14050610UO0000002350","stakeOrderId":null,"localBetNumber":"HZ_3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18","chooseType":0,"stakes":16,"overage":"","betTimes":1,"betType":"hz-multiple","playType":"","betNumberStatus":0,"bonus":null,"amount":3200,"gameId":"k3gx","periodNo":"140506008","betTypeDesc":"和值复式"},"stakeOrder":null,"betNumberList":[]}]},"succes":true};
                // if(data.retcode == 0){
                //     data.nowPage = index;
                //     data.kRed = k_red_v.replace(/\W/g,"|");
                //     data.kBlue = k_blue_v.replace(/\W/g,"|");
                //     data.kHz = k_hz_v;
                //     $list.html(tpl(data));

                //     if(_render.first){
                //        $("html,body").animate({scrollTop:rollTop},300); 
                //     }

                //     if (!_render.first){
                //         _render.first = 1;
                //         _render.out = 1;
                //         _$(".bets_num").html(data.result.totalBetNums);
                //         $page.pagination(data.result.totalNum,{
                //             num_edge_entries: 1,
                //             num_display_entries: 4,
                //             prev_text:"上一页",
                //             next_text:"下一页",
                //             callback:_render
                //         });
                //     }
                // }
            });

            return false;
        };

        _render.out = 0;
        _render(0);

    }
})