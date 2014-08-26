require.config({
    baseUrl: "src/js",
    urlArgs: "t=123456"
});
require([
    'common/main',
    'login/main'
], function (common, login) {
    common();
    login()
});