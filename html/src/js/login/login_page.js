/**
 * Created by caojungang on 2014/4/12.
 */
define([
    "login/check",
    "reg/art_reg"
], function (check,art_reg) {

    var $el = $("#login_page");
    if ($el.length == 0) return function () {
    };
    // ==========================================================
    var _$ = function (selector) {
        return $el.find(selector)
    };

    return function () {
        var $uname = _$(".uname");
        var $uname_msg = $uname.next(".tip");
        var $upwd = _$(".upwd");
        var $upwd_msg = $upwd.next(".tip");

        var $captcha_el = _$(".captchaimg"); // 验证码显示/隐藏
        var $captcha = $captcha_el.find(".validcode"); // input
        var $captcha_img = $captcha_el.find(".validpic"); //img
        var $captcha_msg = $captcha_el.find(".tip"); // tips
        var $captcha_huan = $captcha_el.find(".changevalid"); //换一换

        var $submit = _$(".submit");
        var $tips = $submit.nextAll(".tip");
        var $qqconnect = _$(".qqconnect");
        var redirectUrl = $("#redirectUrl").val() || "/";
        var $u_cover = $(".u_cover");

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
                location.href = redirectUrl
            })
        }


        $uname.add($upwd).add($captcha).on("keyup", function (e) {
            if (e.keyCode == 13) {
                _check()
            }
        });

        $captcha_huan.add($captcha_img).on('click', function() {
            var src = $captcha_img.attr("src");
            $captcha_img.attr("src",src.split('?')[0]+'?t='+ +new Date())
            return false;
        });


        $uname.on("focus",function(){
            $uname_msg.hide()
        })
        $upwd.on("focus",function(){
            $upwd_msg.hide()
        })
        $captcha.on("focus",function(){
            $captcha_msg.hide()
        })



        $submit.on("click", function () {
            _check();
            return false
        });

        $qqconnect.on("click", function () {
            PassportSC.authHandle('qq', 'page', null, redirectUrl);
            try{
            	spb_vars && spb_vars.pingback(0, "pbtag=qq登录", "extra");
            }catch(e){}
            return false
        });
        if($u_cover.length){
        	$u_cover.on("click", ".btn-white", function () {
                art_reg.show();
                return false
            })
        }
        


    }
});
