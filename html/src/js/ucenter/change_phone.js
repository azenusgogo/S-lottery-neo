define([
    "lib/dialog",
    "lib/md5",
    "lib/alert",
    "lib/check.form"
], function (Dialog, md5, dialogAlert, checkForm) {
    var $el = $("#my_info");
    if ($el.length == 0) return function () {
    };

    // ==========================================================
    var Alert = dialogAlert("修改手机号码");
    var _$ = function (selector) {
        return $el.find(selector)
    };
    var dialog1 = new Dialog({
        el: "#change_phone1",
        width: 460,
        onload: function () {
            var $send_code = this.$(".send_code");
            var $code = this.cache$code = this.$(".code");
            var $pwd = this.cache$pwd = this.$(".pwd");
            var $next = this.$(".next");

            $code.add($pwd).on("focus", function () {
                $(this).nextAll(".valid").html("")
            });

            $send_code.on("click", function () {
                checkForm.timer(60, 2)($(this));
                return false
            });

            $code.on("blur", function () {
                checkForm.checkEmpty($(this), "验证码不能为空，请填写")
            });
            $pwd.on("blur", function () {
                checkForm.checkEmpty($(this), "支付密码不能为空，请填写")
            });

            $next.on("click", function () {
                var $pwd_v = $pwd.val(),
                    $pwd_msg = $pwd.nextAll(".valid");
                var $code_v = $code.val(),
                    $code_msg = $code.nextAll(".valid");
                var checked = true
                checked = checkForm.checkEmpty($code, "验证码不能为空，请填写");
                checked && (checked = checkForm.checkEmpty($pwd, "支付密码不能为空，请填写"));
                if (checked) {
                    var data = {
                        payPwd: md5($pwd_v),
                        captcha: $code_v
                    };
                    $.post("/ajax/login/user/mobile/change/auth.html", data, function (data) {
                        var retcode = data.retcode;
                        var retdesc = data.retdesc;
                        switch (retcode) {
                            case 0:
                                dialog1.hide();
                                dialog2.show();
                                break;
                            case 3010501:
                                $code_msg.hide().html(retdesc).fadeIn();
                                break;
                            case 3010502:
                                $pwd_msg.hide().html(retdesc).fadeIn();
                                break;
                            case 3010503:
                                $pwd_msg.hide().html("验证手机失败，您可以等待60秒后重新发送验证码").fadeIn();
                                break;
                            default:
                                Alert.show(function () {
                                    this.$(".txt-center").html(retdesc);
                                });
                        }

                    }).error(function () {
//                        var data = {"retcode":0,"retdesc":"操作成功","result":{}};
                        // var data = {"retcode":3010501,"retdesc":"验证码错误","result":{}};
                        // var data = {"retcode":3010502,"retdesc":"支付密码连续输入错误5次被冻结","result":{}};
                        // var data = {"retcode":3010503,"retdesc":"支付密码错误","result":{}};
                        // var data = {"retcode":3010504,"retdesc":"绑定手机失败","result":{}};
//                        var retcode = data.retcode;
//                        var retdesc = data.retdesc;

                        // 测试用
//                        var e = location.search.match(/e=\d+/g);
//                        var h = {
//                            3010501:"验证码错误",
//                            3010502:"支付密码连续输入错误5次被冻结",
//                            3010503:"支付密码错误",
//                            3010504:"绑定手机失败"
//                        }
//                        retcode = e ? +e[0].replace("e=","") : retcode;
//                        retdesc = h[retcode] || retdesc;

//                        switch(retcode){
//                            case 0:
//                                dialog1.hide();
//                                dialog2.show();
//                                break;
//                            case 3010501:
//                                $code_msg.hide().html(retdesc).fadeIn();
//                                break;
//                            case 3010502:
//                                $pwd_msg.hide().html(retdesc).fadeIn();
//                                break;
//                            case 3010503:
//                                $pwd_msg.hide().html(retdesc).fadeIn();
//                                break;
//                            default:
//                                Alert.show(function(){
//                                    this.$(".txt-center").html(retdesc);
//                                });
//                        }
                    });
                }
                return false
            })
        },
        onhide: function () {
            this.cache$code && this.cache$code.val("");
            this.cache$pwd && this.cache$pwd.val("")
        }
    })

    var dialog2 = new Dialog({
        el: "#change_phone2",
        width: 460,
        onload: function () {
            var $phone = this.$(".phone");
            var $send_code = this.$(".send-code");
            var $code = this.cache$code = this.$(".code");
            var $pwd = this.cache$pwd = this.$(".pwd");
            var $submit = this.$(".submit");

            $send_code.on("click", function () {
                var $this = $(this);
                var checked = checkForm.checkEmpty($phone, "请先输入您想要新绑定的手机号码");
                checked && (checked = checkForm.checkMobile($phone, "手机号码格式错误"));
                checked && checkForm.timer(60, 1)($this, $phone.val());
                return false
            });

            $code.on("blur", function () {
                checkForm.checkEmpty($(this), "验证码不能为空，请填写")
            })
            $pwd.on("blur", function () {
                checkForm.checkEmpty($(this), "支付密码不能为空，请填写")
            })

            $submit.on("click", function () {
                var $phone_v = $phone.val(),
                    $phone_msg = $phone.nextAll(".valid");
                var $code_v = $code.val(),
                    $code_msg = $code.nextAll(".valid");
                var $pwd_v = $pwd.val(),
                    $pwd_msg = $pwd.nextAll(".valid");

                var checked = checkForm.checkEmpty($phone, "请先输入您想要新绑定的手机号码");
                checked && (checked = checkForm.checkMobile($phone, "手机号码格式错误"));
                checked && (checked = checkForm.checkEmpty($code, "验证码不能为空，请填写"));
                checked && (checked = checkForm.checkEmpty($pwd, "支付密码不能为空，请填写"));
                if (checked) {
                    var data = {
                        payPwd: md5($pwd_v),
                        mobile: $phone_v,
                        captcha: $code_v
                    };

                    $.post("/ajax/login/user/mobile/change/bind.html", data, function (data) {
                        var retcode = data.retcode;
                        var retdesc = data.retdesc;
                        switch (retcode) {
                            case 0:
                                Alert.show(function () {
                                    this.redirect = true;
                                    this.$(".txt-center").html(data.retdesc);
                                });
                                break;
                            case 3010501:
                                $code_msg.hide().html(retdesc).fadeIn();
                                break;
                            case 3010502:
                                $pwd_msg.hide().html(retdesc).fadeIn();
                                break;
                            case 3010503:
                                $pwd_msg.hide().html("验证手机失败，您可以等待60秒后重新发送验证码").fadeIn();
                                break;
                            default:
                                Alert.show(function () {
                                    this.$(".txt-center").html(retdesc);
                                });
                        }
                    }).error(function () {
//                        var data = {"retcode":0,"retdesc":"操作成功","result":{}};
                        // var data = {"retcode":3010501,"retdesc":"验证码错误","result":{}};
                        // var data = {"retcode":3010502,"retdesc":"支付密码连续输入错误5次被冻结","result":{}};
                        // var data = {"retcode":3010503,"retdesc":"支付密码错误","result":{}};
                        // var data = {"retcode":3010504,"retdesc":"绑定手机失败","result":{}};
//                        var retcode = data.retcode;
//                        var retdesc = data.retdesc;
//
//                        // 测试用
//                        var e = location.search.match(/e=\d+/g);
//                        var h = {
//                            3010501:"验证码错误",
//                            3010502:"支付密码连续输入错误5次被冻结",
//                            3010503:"支付密码错误",
//                            3010504:"绑定手机失败"
//                        }
//                        retcode = e ? +e[0].replace("e=","") : retcode;
//                        retdesc = h[retcode] || retdesc;

//                        switch(retcode){
//                            case 0:
//                                Alert.show(function(){
//                                    this.redirect = true;
//                                    this.$(".txt-center").html(data.retdesc);
//                                });
//                                break;
//                            case 3010501:
//                                $code_msg.hide().html(retdesc).fadeIn();
//                                break;
//                            case 3010502:
//                                $pwd_msg.hide().html(retdesc).fadeIn();
//                                break;
//                            case 3010503:
//                                $pwd_msg.hide().html(retdesc).fadeIn();
//                                break;
//                            default:
//                                Alert.show(function(){
//                                    this.$(".txt-center").html(retdesc);
//                                });
//                        }
                    });
                }
                return false
            })
        },
        onhide: function () {
            this.cache$code && this.cache$code.val("");
            this.cache$pwd && this.cache$pwd.val("")
        }
    })

    _$(".change_phone").on("click", function () {
        dialog1.show()
        return false
    })


    return function () {

    }
})