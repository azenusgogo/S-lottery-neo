require.config({
    baseUrl: "src/js",
    urlArgs: "t=123456"
});
require([
    'common/main',
    'help/main'
], function (common, help) {
    common();
    help()
});