/**
 * Created by caojungang on 2014/4/10.
 */
require.config({
    baseUrl: "src/js",
    urlArgs: "t=123456"
});
require([
    "zc/rx9_index",
    "zc/sfc_index"
], function (rx9_index, sfc_index) {
    // 任选九、胜负彩
    rx9_index();
    sfc_index();
});