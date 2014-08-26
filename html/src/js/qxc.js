require.config({
    baseUrl: "src/js",
    urlArgs: "t=123456"
});
require([
    'common/main',
    'qxc/main'
], function (common, qxc) {
    common();
    qxc()
});