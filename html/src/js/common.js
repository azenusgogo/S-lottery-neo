require.config({
    baseUrl: "src/js",
    urlArgs: "t=123456"
});
require([
    'common/main'
], function (common) {
    common();
});