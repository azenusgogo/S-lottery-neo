require.config({
    baseUrl: "src/js",
    urlArgs: "t=123456"
});
require([
    "common/main",
    "jczq/hhgg"
], function (common, hhgg) {
    common() // 公用
    hhgg(); // 混合过关
});