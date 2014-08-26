require.config({
    baseUrl: "src/js",
    urlArgs: "t=123456"
});
require([
    'common/main',
    'index/main'
], function (common, index) {
    common();
    index()
});