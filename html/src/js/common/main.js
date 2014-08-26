define([
    "common/memu",
    "common/addfav",
    "login/updateLogin",
    "login/login_before",
    "lib/alert",
    "lib/tools",
    "lib/jquery.cookie"
], function (memu, addfav, updateLogin, login_before, dialogAlert, tools) {
    return function () {
        // 解决ie6下的背景图片缓存问题
        try {
            document.execCommand("BackgroundImageCache", false, true)
        } catch (e) {
        };

        // console 容错
        !window.console && function(){
            window.console = {};
            window.console.log = function(log){
                /debug=1/g.test(location.search) && alert(log)
            }
        }();


        window.PassportSC || (window.PassportSC = {});
        PassportSC.appid = 2012;
        PassportSC.redirectUrl = "http://" + location.host + "/htm/jump.htm";
        // 导航菜单
        memu();
        // 更新默认登录状态
        updateLogin();
        // 收藏
        addfav();
        // 登录前状态
        login_before();
        var AlertB = dialogAlert("意见反馈");
        $("#feedback").on("click", function () {
            if (document.getElementById("live800iconlink") === undefined) {
                AlertB.show(function () {
                    this.$(".txt-center").html("客服程序正在准备中，请稍后再试");
                });
                //alert("客服程序正在准备中，请稍后再试");
            } else {
                if (document.all) {
                    document.getElementById("live800iconlink").click();
                }
                else {
                    var evt = document.createEvent("MouseEvents");
                    evt.initEvent("click", true, true);
                    document.getElementById("live800iconlink").dispatchEvent(evt);
                }
            }
            return false;
        });
        // 统计渠道号
        if (!$.cookie("fr")){
            var fr = location.hash.split("fr=")[1];
            fr = (fr && fr.length < 10) ? fr : "none";
            $.cookie('fr', fr, { expires: 365, path: '/' });
        
            tools.isLogin(function(){
                // 登录
                $.post("/ajax/system/stat-channel.html");
            },function(){
                // 未登录
                $.cookie('fr_unsend', 1, { expires: 365, path: '/' });
            })
        } else {
            tools.isLogin(function(){
                // 登录
                if ($.cookie('fr_unsend')) {
                    $.post("/ajax/system/stat-channel.html");
                    $.removeCookie('fr_unsend')
                }
            },null)
        }









        var $em_balls = $(".ball-list").find("em[data-model]");
        if($em_balls.length){
        	$em_balls.on("selectstart",function(){return false;});//阻止每个球双击选中文本的默认行为
        }
    }
});