/**
 * Created by caojungang on 2014/4/14.
 */
define([
    "pay/pay"
], function (pay) {
    return function () {
        var $ucenter_pay_btn = $(".ucenter_pay_btn");
        if ($ucenter_pay_btn.length == 0) return;
        $ucenter_pay_btn.on("click", function () {
            var payorderid = $(this).attr("data-payorderid");
            payorderid && pay(null, payorderid);
            return false
        })

    }
});