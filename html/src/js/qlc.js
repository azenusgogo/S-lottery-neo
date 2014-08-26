require.config({
    baseUrl: "src/js",
    urlArgs: "t=123456"
});
require([
    'common/main',
    'qlc/main'
], function (common, qlc) {
    common();
    qlc()
});