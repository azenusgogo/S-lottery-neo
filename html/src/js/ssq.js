require.config({
    baseUrl: "src/js",
    urlArgs: "t=123456"
});
require([
    'common/main',
    'ssq/main'
], function (common, ssq) {
    common();
    ssq()
});