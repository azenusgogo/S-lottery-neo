define([
    "text!login/art_login.tpl",
    "lib/dialog",
    "login/check",
    "login/updateLogin",
    "reg/art_reg"
],function(tpl,Dialog,check,updateLogin,art_reg){
    var id = _.uniqueId("DIALOG_");
    $(tpl).attr("id",id).prependTo("body");
    var dialog = new Dialog({
        el:"#"+id,
        width:420,
        onload:function(){
            var $submit = this.$(".submit");
            var $uname = this.$(".uname");
            var $upwd = this.$(".upwd");
            var $captcha = this.$(".validcode");
            var $captcha_img = this.$(".validpic");
            var $captcha_el = this.$(".captchaimg");
            var $captcha_huan = this.$(".change");
            var $uname_msg = $uname.nextAll(".valid");
            var $upwd_msg = $upwd.nextAll(".valid");
            var $captcha_msg = $captcha.nextAll(".tip");
            var $tips = $submit.nextAll(".tip");
            var $qqlogin = this.$(".qqlogin");
            var $redBtn = this.$(".red_btn");
            var _this = this;
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
                    var redirectUrl = $("#redirectUrl").val();
                    if (redirectUrl){
                        location.href = redirectUrl
                    }else{
                        updateLogin();
                        _this.hide();
                        _this.successCallfn()
                    }
                    
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
                try{
                	spb_vars && spb_vars.pingback(0, "pbtag=qq登录", "extra");
                }catch(e){}
                return false
            });


            // 注册
            $redBtn.on("click", function(){
                _this.hide();
                art_reg.show();
                return false
            })

        },
        onhide:function(){
            this.$(".upwd").val("");
            this.$(".valid").html("");
        }
    })
    return dialog
})