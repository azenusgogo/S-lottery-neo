require.config({
    baseUrl: "src/js",
    urlArgs: "t=123456"
});
require([
    'common/main',
    'zc/main'
], function (common, zc) {
    common();
    zc()
});