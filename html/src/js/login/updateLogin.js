/**
 * Created by caojungang on 2014/4/12.
 */
define([
    "lib/tools",
    "text!login/top_login_after.tpl",
    "text!login/index_login_after.tpl"
], function (tools, top_login_after_tpl, index_login_after_tpl) {

    return function () {
        //更新登录状态
        var $el = $("#top_nav_login");
        var $index_login = $("#index_login");
        if ($el.length == 0) return;

        var _$ = function (selector) {
            return $el.find(selector)
        };

        var top_login_after = _.template(top_login_after_tpl);


        var _toggle = function ($btn1, $btn2) {
            // $btn1 首页上面的显示按钮
            // $btn2 首页右面的显示按钮
            var $m1 = $index_login.find(".m1");
            var $m2 = $index_login.find(".m2");
            var $m3 = _$(".m1");
            if (_toggle.st) {
                _toggle.st = 0;
                $btn1.html("显示");
                $btn2.html("显示");
                $m1.html("<span class='red'>********</span>元");
                $m2.html("********")
                $m3.html("********");
            } else {
                $.post("/ajax/login/user/balance/withdraw.html", function (data) {
                    if (data.retcode == 0) {
                        _toggle.st = 1;
                        $btn1.html("隐藏");
                        $btn2.html("隐藏");
                        $m1.html("<span class='red boldred'>" + parseFloat(tools.FloatDiv(data.result.availableAmount, 100)).toFixed(2) + "</span>元");
                        $m2.html("<span class='red boldred'>" + parseFloat(tools.FloatDiv(data.result.withDrawApply, 100)).toFixed(2) + "</span>元");
                        $m3.html("<b>" + parseFloat(tools.FloatDiv(data.result.availableAmount, 100)).toFixed(2) + "</b>");
                    }
                }).error(function() {
                    _toggle.st = 1;
                    $btn1.html("隐藏");
                    $btn2.html("隐藏");
                    $m1.html("<span class='red'>&minus;</span>元");
                    $m2.html("<span class='red'>&minus;</span>元");
                    $m3.html("<b>&minus;</b>");
                });;
            }
        };


        // 是否登录
        tools.isLogin(function (userId) {
            // 已登录
            $el.on("click", ".exit", function () {// 退出登录
                PassportSC.logoutHandle(document.getElementById('login_body'), function () {
                    alert("退出失败")
                }, function () {
                    location.reload()
                });
                return false
            });

            var _update = function(data){
                $el.html(top_login_after(data));

                // 显示金额
                $el.on("click", ".uout", function () {
                    _toggle($(this), $index_login.find(".show"));
                    return false
                });

                // 首页右侧更新
                if ($index_login.length == 0) return;
                var index_login_after = _.template(index_login_after_tpl);
                data.getBytes = tools.getBytes; // 字符串长度 汉子1，英文.5
                $index_login.html(index_login_after(data));

                // 退出
                $index_login.on("click", ".goout", function () {
                    PassportSC.logoutHandle(document.getElementById('login_body'), function () {
                        alert("退出失败")
                    }, function () {
                        location.reload()
                    });
                    return false
                });

                // 显示金额
                $index_login.on("click", ".show", function () {
                    _toggle(_$(".uout"), $(this));
                    return false
                });
            }

            $.post("/ajax/login/user/info/base.html", function (data) {
                if (data.retcode != 0) {
                    var data = {
                        "retcode": 0,
                        "retdesc": "操作成功",
                        "result": {
                            "userId": userId,
                            "nickName": ""
                        }
                    };
                }
                _update(data)
                
            }).error(function(){
                _update({
                    "retcode": 0,
                    "retdesc": "操作成功",
                    "result": {
                        "userId": userId,
                        "nickName": ""
                    }
                })
            });
        },function(){
            // 未登录
            _$(".loading").hide();
            $index_login.find(".loading").hide();
        })
    }
})