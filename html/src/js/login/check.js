define(function(){
    return function(el,fn){
        // user            用户名
        // upwd            密码
        // captcha         验证码
        // captcha_img     验证码图片
        // captcha_el      验证码div
        // userMsg         用户名错误提示
        // upwdMsg         密码错误提示
        // captchaMsg      验证码错误提示
        // submitMsg       用户名或密码错误提示
        // text            提示文案

        var text = ["请输入用户名","请输入密码","请输入验证码","验证码错误","用户名或密码错误"];
        var user_v = el.user.val();
        var upwd_v = el.upwd.val();
        var captcha_v = el.captcha.val();

        if (user_v.length == 0){
            el.userMsg.html(text[0]).hide().fadeIn();
            return
        }

        if (upwd_v.length == 0){
            el.upwdMsg.html(text[1]).hide().fadeIn();
            return
        }

        if (el.captcha_el.is(":visible") && captcha_v.length == 0){
            el.submitMsg.hide();
            el.captchaMsg.html(text[2]).hide().fadeIn();
            return
        }

        if (el.captcha_el.is(":visible") && captcha_v.length !=5 ){
            el.submitMsg.hide();
            el.captchaMsg.html(text[3]).hide().fadeIn();
            return
        }
        PassportSC.loginHandle(user_v, upwd_v, captcha_v, 0, document.getElementById('login_body'), function (data) {
            // 显示验证码
            if(+data.needcaptcha){
                el.captcha_el.show();
                el.captcha_img.attr("src",data.captchaimg)
            }
            if (data.status == 20221){
                // 验证码错误
                el.submitMsg.hide();
                el.captchaMsg.html(text[3]).hide().fadeIn();
            }else{
                // 用户名或密码错误
                el.submitMsg.html(text[4]).hide().fadeIn();
            }
        }, function () {
            // 登录成功
            fn && fn()
        })
    }
});