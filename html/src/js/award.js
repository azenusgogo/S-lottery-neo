require.config({
    baseUrl: "src/js",
    urlArgs: "t=123456"
});
require([
    'common/main',
    'award/main'
], function (common, award) {
    common();
    award()
});