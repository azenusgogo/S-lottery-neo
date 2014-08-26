require.config({
    baseUrl: "src/js",
    urlArgs: "t=123456"
});
require([
    'common/main',
    'ucenter/main'
], function (common, ucenter) {
    common();
    ucenter()
});