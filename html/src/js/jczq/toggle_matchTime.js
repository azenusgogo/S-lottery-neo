/**
 * Created by caojungang on 2014/6/6.
 */
define(function () {
    return function () {
        var $toggleMatchTime = $("#toggleMatchTime");
        var $matchTime = $(".matchTime");
        var $officialEndTime = $(".officialEndTime");
        var $text = $toggleMatchTime.find(".text");

        $(document).on("click", function () {
            $toggleMatchTime.removeClass("sel_show")
        });
        $toggleMatchTime.on("click", function () {
            $(this).toggleClass("sel_show");
            return false
        });
        $toggleMatchTime.on("click", "a", function (e) {
            e.preventDefault();
            var i = $(this).index();
            $officialEndTime[i ? "hide" : "show"]();
            $matchTime[i ? "show" : "hide"]();
            $text.html(i ? "比赛时间" : "停售时间")
        })

    }
});