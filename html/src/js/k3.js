require.config({
    baseUrl: "src/js",
    urlArgs: "t=123456"
});
require([
    'common/main',
    'k3/main'
], function (common, k3) {
    common();
    k3()
});