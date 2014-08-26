require.config({
    baseUrl: "src/js",
    urlArgs: "t=123456"
});
require([
    'ssq/main',
    'dlt/main',
    'qxc/main',
    'qlc/main',
    'k3/main',
    'zc/main',
    'ucenter/main',
    'common/main',
    'award/main',
    'index/main',
    'goucai/main',
    'login/main',
    'help/main'
], function (ssq, dlt, qxc, qlc, k3, zc, ucenter, common, award, index, goucai, login, help) {
    common(); // 公共模块
    ssq(); // 双色球
    dlt(); // 大乐透
    qxc(); // 七星彩
    qlc(); // 七乐彩
    k3(); // 快3
    zc(); // 足彩
    ucenter(); // 用户中心
    award(); // 奖金计算机
    index(); // 首页
    goucai();// 购彩大厅
    login(); // 登录
    help(); //帮助中心
});