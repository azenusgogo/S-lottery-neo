define(["lib/tools"], function (tools) {
    return function () {

        var $mycash = $(".mycash");
        var $mycash_t = $(".mycash_t");

        if ($mycash.length == 0) return;

        $.post("/ajax/login/user/balance/withdraw.html", function (data) {
            if (data.retcode == 0) {

                $mycash.html(tools.FloatDiv(data.result.availableAmount,100));
                $mycash_t.html(tools.FloatDiv(data.result.withDrawApply,100));

            } else {
                $mycash.html("&minus;");
                $mycash_t.html("&minus;");
            }
        }).error(function () {
            // var Alert = dialogAlert("提示");
            // var data = {"retcode":0,"retdesc":"操作成功","result":{
            // 	"availableAmount":100,
            // 	"frozenAmount":5,
            // 	"availableWithDrawAmount":100
            // }};//成功返回数据

            // var data = {"retcode":3010201,"retdesc":"查询余额参数错误"};//查询错误
            // if(data.retcode == 0){
            // 	var yu = (data.result.availableAmount/100).toFixed(2);
            // 	$(".u-info b").html(yu);
            // }else{
            // 	Alert.show(function(){
            // 		this.$(".txt-center").html(data.retdesc);
            // 	});
            // }
        });

        if($("#order_bets").length != 0 ){
            $.get("/ajax/login/user/order/nopay/count.html",function(data){
                if(data.retcode == 0){
                    $(".nopay_num").html("["+data.result+"]");
                }
            })
        }

    }
})