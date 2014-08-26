define([
    "ucenter/change_paypwd",
    "ucenter/find_paypwd",
    "ucenter/change_phone",
    "ucenter/change_cards",
    "ucenter/cash",
    "ucenter/recharge",
    "ucenter/recharge2",
    "ucenter/fill_info",
    "ucenter/common",
    "ucenter/bet_page",
    "ucenter/pay",
    "ucenter/jc_order"
], function (change_paypwd, find_paypwd, change_phone, change_cards, cash, recharge, recharge2, fill_info, common, bet_page, pay, jc_order) {
    return function () {
        common(); // 获取账户余额等公用功能
        change_paypwd(); // 修改支付密码
        find_paypwd(); // 找回支付密码
        change_phone(); // 修改手机号
        change_cards(); // 修改银行卡
        cash(); // 提款
        recharge(); //充值
        recharge2(); //充值第二步
        fill_info(); // 补全用户信息
        bet_page(); // 订单详情AJAX分页
        pay(); // 支付
        jc_order(); //竞彩订单详情
    }
})