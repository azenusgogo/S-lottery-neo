require.config({
    baseUrl: "src/js",
    urlArgs: "t=123456"
});
require([
    'common/main',
    'goucai/main'
], function (common, goucai) {
    common();
    goucai()
});