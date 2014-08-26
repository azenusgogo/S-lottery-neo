define([
    "lib/dialog"
], function (Dialog) {
    var $el = $("#user_recharge2");
    if ($el.length == 0) return function () {
    };

    // ==========================================================
    var _$ = function (selector) {
        return $el.find(selector)
    };
    var dialog = new Dialog({
        el: "#charge_complete",
        width: 420
    });

    return function () {
        var $submit = _$(".submit");
        $submit.on("click", function () {            
            $el.submit();
            dialog.show();  
            try{
            	spb_vars && spb_vars.pingback(0, "pbtag=立即充值", "extra");
            }catch(e){}
            return false
        })
    }
})