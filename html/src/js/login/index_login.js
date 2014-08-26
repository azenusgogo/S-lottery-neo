define([
    "login/check",
    "login/updateLogin"
],function(check, updateLogin){
    return function(){
        var $el = $("#index_login");
        if ($el.length == 0) return;
        var _$ = function(selector) {
          return $el.find(selector)
        };


        var $submit = _$(".submit");
        var $uname = _$(".uname");
        var $upwd = _$(".upwd");
        var $captcha_el = _$(".valid-nav");
        var $captcha = $captcha_el.find(".valid");
        var $captcha_img = $captcha_el.find("img");
        var $captcha_huan = $captcha_el.find("a");
        var $uname_msg = _$(".tip");
        var $upwd_msg = $uname_msg;
        var $captcha_msg = $uname_msg;;
        var $tips = $uname_msg;
        var $qqlogin = _$(".qqconnect");
        var _check = function(){
            check({
                user        : $uname,
                upwd        : $upwd,
                captcha     : $captcha,
                captcha_img : $captcha_img,
                captcha_el  : $captcha_el,
                userMsg     : $uname_msg,
                upwdMsg     : $upwd_msg,
                captchaMsg  : $captcha_msg,
                submitMsg   : $tips
            },function(){
                updateLogin()
            })
        }

        $uname.add($upwd).add($captcha).on("keyup",function(e){
            if (e.keyCode == 13){
                _check()
            }
        });
        $submit.on("click",function(){
            _check()
            return false
        });

        $captcha_huan.add($captcha_img).on('click', function() {
            var src = $captcha_img.attr("src");
            $captcha_img.attr("src",src.split('?')[0]+'?t='+ +new Date())
            return false;
        });


        $uname.on("focus",function(){
            $uname_msg.hide()
        });
        $upwd.on("focus",function(){
            $upwd_msg.hide()
        });
        $captcha.on("focus",function(){
            $captcha_msg.hide()
        });

        $qqlogin.on("click", function () {
            PassportSC.authHandle('qq', 'page', null, location.href);
            spb_vars && spb_vars.pingback(0, "pbtag=点击qq登录", "extra");
            return false
        });

    }
})