require.config({
    baseUrl: "src/js",
    urlArgs: "t=123456"
});
require([
    'common/main',
    'dlt/main'
], function (common, dlt) {
    common();
    dlt()
});