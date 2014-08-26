define([
    "lib/alert"
],function(dialogAlert){
    return function(){
        var Alert = dialogAlert("提示");
        var gameStatus = $("#gameStatus").val();
        if (gameStatus == 0){
            Alert.show(function(){
                this.redirect = "/";
                this.$(".txt-center").html("<span style='font-size:12px; line-height:24px;'>您好，该彩种目前无销售期，请您稍等片刻进行投注，大奖即将到来，感谢您对搜狗彩票的理解与支持！</span>");
            })
        }
        return gameStatus
    }
})