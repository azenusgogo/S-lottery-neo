define([
    "lib/tools",
    "login/art_login",
    "reg/art_reg"
], function (tools, art_login, art_reg) {
    return function () {
        var $el = $("#top_nav_login");
        if ($el.length == 0) return;
        var $index_login = $("#index_login");
        tools.isLogin(null, function () {
            // 未登录
            $el.on("click", ".top_login_btn", function () {// 登录
                art_login.show();
                return false
            });

            art_reg.onbeforeshow = function () {
                var _this = this;
                var $toggle = _this.$toggle || function(){
                    _this.$toggle = _this.$(".toggle");
                    return _this.$toggle
                }();
                var bind = $toggle.data("bind");

                if (!bind){
                    $toggle.on("click" ,function(){
                        _this.hide();
                        art_login.show();
                        return false
                    });
                    $toggle.data("bind", 1)
                }
            };


            $el.on("click", ".top_reg_btn", function () {// 注册
           
                art_reg.show();
                return false
            });
            if ($index_login.length) {
                $index_login.on("click", ".index_reg_btn", function () {
                    art_reg.show();
                    return false
                })
            }
        })
    }
})