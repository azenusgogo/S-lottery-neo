require.config({
    baseUrl: "src/js",
    urlArgs: "t=123456"
});
require([
    "update/stop_game",
    "common/main",   
    "jczq/bqc/bqc",
    "jczq/zjq/zjq",
    "jczq/theadfix",
    "jczq/toggle_matchTime",
    "jczq/footerfix"
], function (stop_game,common,bqc,zjq,theadfix,toggle_matchTime,footerfix) {
	if (stop_game() == 0) return function() {};
	common();
	bqc(); // 半全场
    zjq(); // 总进球
    theadfix();
    toggle_matchTime();
    footerfix();
});