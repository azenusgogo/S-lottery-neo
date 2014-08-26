define([
    "lib/dialog",
    "lib/alert",
    "login/updateLogin",
    "text!reg/art_reg.tpl"
], function (Dialog, dialogAlert, updateLogin, tpl) {
    var Alert = dialogAlert("温馨提示");
    var id = _.uniqueId("DIALOG_");
    var $el = $(tpl).attr("id", id);
    var dialog = new Dialog({
        el: $el,
        width: 480,
        onshow: function () {
            $.get("/ajax/register/pre.html");
            this.$(".validpic").attr("src", "/captcha/register/gen.html?t="+ +new Date);
            if ($("#" + id).length) return;
            $el.prependTo("body");
            var $submit = this.$(".submit");

            var $uname = this.$(".uname");
            var $upwd1 = this.$(".upwd1");
            var $upwd2 = this.$(".upwd2");
            var $validcode = this.$(".validcode");
            var $validpic = this.$(".validpic");
            var $change = this.$(".change");
            var $read = this.$(".read");
            
            var $unameMsg = $uname.nextAll("span");
            var $upwd1Msg = $upwd1.nextAll("span");
            var $upwd2Msg = $upwd2.nextAll("span");
            var $captchaMsg = $validcode.nextAll("span");
            var $readMsg = $read.nextAll("span");

            var $unameIco = $uname.nextAll("i");
            var $upwd1Ico = $upwd1.nextAll("i");
            var $upwd2Ico = $upwd2.nextAll("i");
            var $captchaIco = $validcode.nextAll("i");

            var errText = [
                "请填写用户名",
                "用户名长度为4-16位",
                "字母开头的数字、字母、下划线或组合",
                "用户名已经存在",

                "请填写密码",
                "密码长度为6-16位",

                "请填写确认密码",
                "两次密码输入不一致",

                "请填写验证码",
                "验证码验证失败",

                "请确认阅读并同意服务条款"
            ];

            var user1 = {};
            var user2 = {};

            var checkform = {
                cUname: function () {
                    var $uname_v = $.trim($uname.val());
                    var checked = 0;
                    var _this = this;


                    if ($uname_v.length == 0) {
                        $unameMsg.html(errText[0]).addClass('red');
                        checked = 1;
                    } else if ($uname_v.length < 4 || $uname_v.length > 16) {
                        $unameMsg.html(errText[1]).addClass('red');
                        checked = 1;
                    } else if (!/^[a-zA-Z]([a-zA-Z0-9_.]{3,15})$/.test($uname_v)) {
                        $unameMsg.html(errText[2]).addClass('red');
                        checked = 1;
                    } else {

                        if (user1[$uname_v]) {// 用户名存在
                            $unameMsg.html(errText[3]).addClass('red');
                            $unameIco[0].className = "reg-error";
                            $unameIco.css("display", "inline-block");
                            checked = 1;
                        } else if (user2[$uname_v]) {
                            $unameIco[0].className = "reg-right";
                            $unameIco.css("display", "inline-block");
                        } else {
                            // 用户不存在

                            $.post("/ajax/register/check.html", {userId: $uname_v}, function (data) {
                                if (data.retcode == 0) {
                                    // 用户名不存在
                                    user2[$uname_v] = 1;
                                    $unameIco[0].className = "reg-right";
                                } else if (data.retcode == 3000102 || data.retcode == 3000103) {
                                    // 用户名存在
                                    user1[$uname_v] = 1;
                                    $unameMsg.html(errText[3]).addClass('red');
                                    $unameIco[0].className = "reg-error";
                                } else {
                                    $unameMsg.html("系统繁忙").addClass('red');
                                    $unameIco[0].className = "reg-error";
                                }
                                $unameIco.css("display", "inline-block");
                            }).error(function () {
                                // var data = {
                                //      retcode:1
                                // }
                                // if (data.retcode == 0) {
                                //     // 用户名存在
                                //     user1[$uname_v] = 1;
                                //     $unameMsg.html(errText[3]).addClass('red');
                                //     $unameIco[0].className = "reg-error";
                                // } else {
                                //     // 用户名不存在
                                //     user2[$uname_v] = 1;
                                //     $unameIco[0].className = "reg-right";
                                // }
                                // $unameIco.css("display", "inline-block");
                            });
                        }

                        return !checked
                    }
                    if (checked) {
                        $unameIco[0].className = "reg-error";
                        $unameIco.css("display", "inline-block")
                    }
                    return !checked
                },
                cUpwd1: function () {
                    var $upwd1_v = $.trim($upwd1.val());
                    var checked = 0
                    if ($upwd1_v.length == 0) {
                        $upwd1Msg.html(errText[4]).addClass('red');
                        checked = 1
                    } else if ($upwd1_v.length < 4 || $upwd1_v.length > 16) {
                        $upwd1Msg.html(errText[5]).addClass('red');
                        checked = 1
                    }
                    if (checked) {
                        $upwd1Ico[0].className = "reg-error";
                    } else {
                        $upwd1Ico[0].className = "reg-right";
                    }
                    $upwd1Ico.css("display", "inline-block");
                    return !checked
                },
                cUpwd2: function () {
                    var $upwd1_v = $.trim($upwd1.val());
                    var $upwd2_v = $.trim($upwd2.val());
                    var checked = 0
                    if ($upwd2_v.length == 0) {
                        $upwd2Msg.html(errText[6]).addClass('red');
                        checked = 1
                    } else if ($upwd1_v != $upwd2_v) {
                        $upwd2Msg.html(errText[7]).addClass('red');
                        checked = 1
                    }
                    if (checked) {
                        $upwd2Ico[0].className = "reg-error";
                    } else {
                        $upwd2Ico[0].className = "reg-right";
                    }

                    $upwd2Ico.css("display", "inline-block")
                    return !checked
                },
                cCaptcha: function () {
                    var $validcode_v = $.trim($validcode.val());
                    var checked = 0
                    if ($validcode_v.length == 0) {
                        $captchaMsg.html(errText[8]).addClass('red');
                        checked = 1
                        //$captchaIco[0].className = "reg-error";
                    } else {
                        //$captchaIco[0].className = "reg-right";
                    }
                    //$captchaIco.css("display","inline-block")
                    return !checked
                },
                cRead: function () {
                    var checked = $read.prop("checked");
                    if (!checked) {
                        $readMsg.addClass('red').html(errText[10])
                    }
                    return checked;
                }
            };

            var _changeCode = function () {
                var src = $validpic.attr("src");
                $validpic.attr("src", src.split('?')[0] + '?t=' + +new Date())
            };
            var _this = this;
            var _check = function () {
                var checked = 1;
                $readMsg.removeClass('red').html("");
                _.each(checkform, function (fn) {
                    if (typeof fn == "function") {
                        checked && (checked = fn()) || fn()
                    }
                });
                if (checked) {
                    var userId = $uname.val();
                    var upwd1 = $upwd1.val();
                    var data = {
                        userId: userId,
                        pwd: upwd1,
                        pwdConfirm: $upwd2.val(),
                        captcha: $validcode.val()
                    };
                    $.post("/ajax/register/do.html", data, function (data) {
                        var retcode = data.retcode;
                        if (retcode == 0) {
                            _this.hide();
                            Alert.show(function () {
                                this.onhide = function () {
                                    PassportSC.loginHandle(userId, upwd1, 0, document.getElementById('login_body'), function (data) {

                                    }, function () {
                                        // 登录成功
                                        var redirectUrl = $("#redirectUrl").val();
                                        if (redirectUrl) {
                                            location.href = redirectUrl
                                        } else {
                                            updateLogin()
                                        }
                                    });
                                };
                                this.$(".txt-center").html("恭喜\"" + userId + "\",注册成功!");
                                
                            });
                            spb_vars && spb_vars.pingback(0, "pbtag=完成注册", "extra");
                        } else {
                            if (retcode == 4000306 || retcode == 3000101) {
                                $unameMsg.html("字母开头的数字、字母、下划线或组合").addClass('red');
                                $unameIco[0].className = "reg-error";
                                $unameIco.css("display", "inline-block")
                            } else if (retcode == 3000102 || retcode == 3000103) {
                                $unameMsg.html("用户名已经存在").addClass('red');
                                $unameIco[0].className = "reg-error";
                                $unameIco.css("display", "inline-block")
                            } else if (retcode == 4000701) {
                                $captchaMsg.html(errText[9]).addClass('red');
                            } else {
                                $readMsg.addClass('red').html(data.retdesc)
                            }
                            _changeCode()

                        }

                    }).error(function () {
                        // var data = {"retcode": 0, "retdesc": "操作成功", "result": {}};
                        // var retcode = data.retcode;
                        // if (retcode == 0) {
                        //     _this.hide();
                        //     Alert.show(function () {
                        //         this.onhide = function () {
                        //             PassportSC.loginHandle(userId, upwd1, 0, document.getElementById('login_body'), function (data) {

                        //             }, function () {
                        //                 // 登录成功
                        //                 updateLogin()
                        //             });
                        //         };
                        //         this.$(".txt-center").html("恭喜\"" + userId + "\",注册成功!");
                        //     });
                        // } else {
                        //     if (retcode == 4000306 || retcode == 3000101) {
                        //         $unameMsg.html("字母开头的数字、字母、下划线或组合").addClass('red');
                        //         $unameIco[0].className = "reg-error";
                        //         $unameIco.css("display", "inline-block")
                        //     } else if (retcode == 3000102 || retcode == 3000103) {
                        //         $unameMsg.html("用户名已经存在").addClass('red');
                        //         $unameIco[0].className = "reg-error";
                        //         $unameIco.css("display", "inline-block")
                        //     } else {
                        //         $readMsg.addClass('red').html(data.retdesc)
                        //     }
                        // }


                        // 4000306, "您的昵称包含不适当内容,请重新选择"
                        // 3000101, "您注册的用户名不符合规定，请更正后再试", "非法userId"
                        // 3000102, "该帐号已经注册，请您尝试其他组合", "帐号已经注册，请直接登录"
                        // 3000103, "该帐号已经注册，请您尝试其他组合", "用户名存在 "
                        // 3000104, "系统繁忙，请您稍后重试", "当前注册ip次数已达上限或该ip已在黑名单中"
                        // 3000105, "注册失败，请您重新注册", "创建用户失败"
                        // 3000106,  "该手机号已绑定其他账号"
                        // 3000107, "系统繁忙，请您稍后重试", "接口调用频次超限（5分钟调用超过了1000次）"
                        // 3000108, "系统繁忙，请您稍后重试", "当日邮件发送次数已达上限"
                        // 3000109, "系统繁忙，请您稍后重试", "激活链接已失效"

                    })
                }
            };

            $submit.on("click", function () {
                _check();
                try{
                	spb_vars && spb_vars.pingback(0, "pbtag=点击提交注册", "extra");
                }catch(e){}
                return false
            });

            $uname.add($upwd1).add($upwd2).add($validcode).on({
                "focus": function () {
                    var $this = $(this);
                    var $tips = $this.nextAll("span");
                    var text = $tips.attr("placeholder");
                    $tips.removeClass('red').html(text);
                    $this.nextAll("i").hide()
                },
                "blur": function () {
                    checkform[$(this).attr("check")]();
                },
                "keyup": function(e) {
                    if (e.keyCode == 13) {
                        _check();
                        try{
                            spb_vars && spb_vars.pingback(0, "pbtag=回车提交注册", "extra");
                        }catch(e){}
                    }
                }
            });

            $read.on("click", function () {
                if ($(this).prop("checked")) {
                    $readMsg.removeClass('red').html("")
                } else {
                    $readMsg.addClass('red').html(errText[10])
                }
            });

            $validpic.add($change).on("click", function () {
                _changeCode();
                return false;
            });

        },
        onhide: function () {

        }
    })
    return dialog
});