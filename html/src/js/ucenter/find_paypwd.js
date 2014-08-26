define([
    "lib/dialog",
    "lib/md5",
    "lib/alert",
    "lib/check.form"
], function (Dialog, md5, dialogAlert, checkForm) {
    var $el = $("#find_paypwd");
    if ($el.length == 0) return function () {
    };

    // ==========================================================
    var _$ = function (selector) {
        return $el.find(selector)
    };

    var Alert = dialogAlert("找回密码");
    var d_find_name = new Dialog({
        el: "#find_name",
        width: 650,
        onload: function () {
            var $name = this.$(".name");
            var $zj = this.$(".zj");
            var $zj_number = this.$(".zj_number");
            var $send_code = this.$(".send_code");
            var $code = this.$(".code");
            var $pwd1 = this.$(".pwd1");
            var $pwd2 = this.$(".pwd2");

            $name.add($zj_number).add($code).add($pwd1).add($pwd2).on("focus", function () {
                $(this).nextAll(".valid").html("")
            });

            $name.on("blur", function () {
                checkForm.checkEmpty($(this), "真实姓名不能为空，请填写")
            });

            $zj.on("change", function () {
                checkForm.checkSelect($(this), "请选择证件类型")
            });

            $zj_number.on("blur", function () {
                checkForm.checkEmpty($(this), "证件号码不能为空，请填写")
            });

            // 发短信
            $send_code.on("click", function () {
                checkForm.timer(60, 3)($(this));
                return false
            });

            $code.on("focus", function () {
                var $this = $(this);
                var v = $this.val();
                if (v == "请填写收到的短信验证码") {
                    $this.val("")
                }
            });

            $code.on("blur", function () {
                var $this = $(this);
                var v = $this.val();
                if (v == "") {
                    checkForm.checkEmpty($(this), "短信验证码不能为空")
                    $this.val("请填写收到的短信验证码")
                }
            });

            $pwd1.on("blur", function () {
                checkForm.checkNewPaypwd($(this))
            });

            $pwd2.on("blur", function () {
                checkForm.checkNewPaypwd2($pwd1, $(this))
            });

            this.$(".submit").on("click", function () {
                var $name_v = $name.val(),
                    $name_msg = $name.nextAll(".valid");
                var $zj_v = $zj.val(),
                    $zj_msg = $zj.nextAll(".valid");
                var $zj_number_v = $zj_number.val(),
                    $zj_number_msg = $zj_number.nextAll(".valid");
                var $code_v = $code.val(),
                    $code_msg = $code.nextAll(".valid");
                var $pwd1_v = $pwd1.val(),
                    $pwd1_msg = $pwd1.nextAll(".valid");
                var $pwd2_v = $pwd2.val(),
                    $pwd2_msg = $pwd2.nextAll(".valid");
                var checked = true;


                checked = checkForm.checkEmpty($name, "真实姓名不能为空，请填写");
                checked && (checked = checkForm.checkSelect($zj));
                checked && (checked = checkForm.checkEmpty($zj_number, "证件号码不能为空，请填写"));
                checked && (checked = checkForm.checkEmpty($code, "短信验证码不能为空"));
                checked && (checked = checkForm.checkNewPaypwd($pwd1));
                checked && (checked = checkForm.checkNewPaypwd2($pwd1, $pwd2));

                if (checked) {
                    var data = {
                        captcha: $code_v,
                        newPassowrdConfirm: md5($pwd2_v),
                        newPassowrd: md5($pwd1_v),
                        trueName: $name_v,
                        idCardNo: $zj_number_v,
                        idCardType: $zj_v
                    };

                    $.post("/ajax/login/user/pwd/find/info.html", data, function (data) {
                        var retcode = data.retcode;
                        var retdesc = data.retdesc;
                        switch (retcode) {
                            case 0:
                                Alert.show(function () {
                                    this.redirect = true;
                                    this.$(".txt-center").html(retdesc);
                                });
                                break;
                            case 3010802:
                                $code_msg.hide().html(retdesc).fadeIn();
                                break;
                            default:
                                Alert.show(function () {
                                    this.$(".txt-center").html(retdesc);
                                });
                        }
                    }).error(function () {
//                        var data = {"retcode":0,"retdesc":"操作成功","result":{}};
                        // var data = {"retcode":4000401,"retdesc":"身份信息验证错误","result":{}};
                        // var data = {"retcode":3010801,"retdesc":"用户未绑定手机","result":{}};
                        // var data = {"retcode":3010802,"retdesc":"验证码错误","result":{}};

//                        var retcode = data.retcode;
//                        var retdesc = data.retdesc;

                        // 测试用
//                        var e = location.search.match(/e=\d+/g);
//                        var h = {
//                            4000401:"身份信息验证错误",
//                            3010801:"用户未绑定手机",
//                            3010802:"验证码错误"
//                        }
//                        retcode = e ? +e[0].replace("e=","") : retcode;
//                        retdesc = h[retcode] || retdesc;

//                        switch(retcode){
//                            case 0:
//                                Alert.show(function(){
//                                    this.redirect = true;
//                                    this.$(".txt-center").html(retdesc);
//                                });
//                                break;
//                            case 3010802:
//                                $code_msg.hide().html(retdesc).fadeIn();
//                                break;
//                            default:
//                                Alert.show(function(){
//                                    this.$(".txt-center").html(retdesc);
//                                });
//                        }
                    })
                }
                return false
            })
        }
    })

    var d_find_question = new Dialog({
        el: "#find_question",
        width: 650,
        onload: function () {
            var $answer = this.$(".answer");
            var $send_code = this.$(".send_code");
            var $code = this.$(".code");
            var $pwd1 = this.$(".pwd1");
            var $pwd2 = this.$(".pwd2");
            var $question = this.$(".exQuestion");

            $answer.add($code).add($pwd1).add($pwd2).on("focus", function () {
                $(this).nextAll(".valid").html("")
            });

            $answer.on("blur", function () {
                checkForm.checkEmpty($answer, "答案不能为空，请填写");
                return false
            });

            // 发短信
            $send_code.on("click", function () {
                checkForm.timer(60, 3)($(this));
                return false
            });

            $code.on("focus", function () {
                var $this = $(this);
                var v = $this.val();
                if (v == "请填写收到的短信验证码") {
                    $this.val("")
                }
            });

            $code.on("blur", function () {
                var $this = $(this);
                var v = $this.val();
                if (v == "") {
                    checkForm.checkEmpty($(this), "短信验证码不能为空")
                    $this.val("请填写收到的短信验证码")
                }
            });

            $pwd1.on("blur", function () {
                checkForm.checkNewPaypwd($(this))
            });

            $pwd2.on("blur", function () {
                checkForm.checkNewPaypwd2($pwd1, $(this))
            });


            this.$(".submit").on("click", function () {


                var $answer_v = $answer.val(),
                    $answer_msg = $answer.nextAll(".valid");
                var $code_v = $code.val(),
                    $code_msg = $code.nextAll(".valid");
                var $pwd1_v = $pwd1.val(),
                    $pwd1_msg = $pwd1.nextAll(".valid");
                var $pwd2_v = $pwd2.val(),
                    $pwd2_msg = $pwd2.nextAll(".valid");
                var checked = true;


                checked = checkForm.checkEmpty($answer, "答案不能为空，请填写");
                checked && (checked = checkForm.checkEmpty($code, "短信验证码不能为空"));
                checked && (checked = checkForm.checkNewPaypwd($pwd1));
                checked && (checked = checkForm.checkNewPaypwd2($pwd1, $pwd2));


                if (checked) {
                    var data = {
                        captcha: $code_v,
                        newPassowrdConfirm: md5($pwd2_v),
                        newPassowrd: md5($pwd1_v),
                        safeAnswer: md5($answer_v)
                    };

                    $.post("/ajax/login/user/pwd/find/safe.html", data, function (data) {
                        var retcode = data.retcode;
                        var retdesc = data.retdesc;
                        switch (retcode) {
                            case 0:
                                Alert.show(function () {
                                    this.redirect = true;
                                    this.$(".txt-center").html(retdesc);
                                });
                                break;
                            case 3010802:
                                $code_msg.hide().html(retdesc).fadeIn();
                                break;
                            case 3010701:
                                $answer_msg.hide().html(retdesc).fadeIn();
                                break;
                            default:
                                Alert.show(function () {
                                    this.$(".txt-center").html(retdesc);
                                });
                        }
                    }).error(function () {
//                        var data = {"retcode":0,"retdesc":"操作成功","result":{}};
                        // var data = {"retcode":4000401, "retdesc":"身份信息验证错误","result":{}};
                        // var data = {"retcode":3010801, "retdesc":"用户未绑定手机","result":{}};
                        // var data = {"retcode":3010802, "retdesc":"验证码错误","result":{}};
                        // var data = {"retcode":3010701, "retdesc":"安全问题答案错误","result":{}};
                        // var data = {"retcode":3010702, "retdesc":"找回支付密码失败","result":{}};
//                        var retcode = data.retcode;
//                        var retdesc = data.retdesc;

                        // 测试用
//                        var e = location.search.match(/e=\d+/g);
//                        var h = {
//                            4000401:"身份信息验证错误",
//                            3010801:"用户未绑定手机",
//                            3010802:"验证码错误",
//                            3010701:"安全问题答案错误",
//                            3010702:"找回支付密码失败"
//                        }
//                        retcode = e ? +e[0].replace("e=","") : retcode;
//                        retdesc = h[retcode] || retdesc;

//                        switch(retcode){
//                            case 0:
//                                Alert.show(function(){
//                                    this.redirect = true;
//                                    this.$(".txt-center").html(retdesc);
//                                });
//                                break;
//                            case 3010802:
//                                $code_msg.hide().html(retdesc).fadeIn();
//                                break;
//                            case 3010701:
//                                $answer_msg.hide().html(retdesc).fadeIn();
//                                break;
//                            default:
//                                Alert.show(function(){
//                                    this.$(".txt-center").html(retdesc);
//                                });
//                        }


                    })
                }
                return false
            });

            $.get("/ajax/login/user/pwd/find/currentsafe.html", function (data) {
                if (data.retcode == 0) {
                    $question.html(data.result.safeQuestion);
                } else {
                    // ??出现错误时二次弹层
                }
            }).error(function () {
                // var data ={"retcode":0,"retdesc":"操作成功","result":{
                //     "userId":"wfones@sohu.com",
                //     "safeQuestion":"您爸爸的名字是什么？"
                // }};
                // $question.html(data.result.safeQuestion);
            });
        }
    })

    var d_reset_answer = new Dialog({
        el: "#reset_answer",
        width: 650,
        onload: function () {
            //var $question = this.$(".question");
            var $answer = this.$(".answer");
            var $new_question = this.$(".new_question");
            var $new_answer = this.$(".new_answer");
            var $pwd = this.$(".pwd");

            // $question.add($new_question).on("change",function(){
            //     checkForm.checkSelect($(this),"请选择安全问题")
            // });
            $new_question.on("change", function () {
                checkForm.checkSelect($(this), "请选择安全问题")
            });

            $answer.add($new_answer).on("blur", function () {
                checkForm.checkEmpty($(this), "请输入安全问题答案")
            }).add($pwd).on("focus", function () {
                $(this).nextAll(".valid").html("")
            });

            $pwd.on("blur", function () {
                checkForm.checkEmpty($(this), "请输入支付密码")
            })

            this.$(".submit").on("click", function () {
                // var $question_v = $question.val(),
                //     $question_msg = $question.nextAll(".valid");
                var $answer_v = $answer.val(),
                    $answer_msg = $answer.nextAll(".valid");
                var $new_question_v = $new_question.val(),
                    $new_question_msg = $new_question.nextAll(".valid");
                var $new_answer_v = $new_answer.val(),
                    $new_answer_msg = $new_answer.nextAll(".valid");
                var $pwd_v = $pwd.val(),
                    $pwd_msg = $pwd.nextAll(".valid");
                var checked = true;


                // checked = checkForm.checkSelect($question,"请选择安全问题");
                checked && (checked = checkForm.checkEmpty($answer, "请输入安全问题答案"));
                checked && (checked = checkForm.checkSelect($new_question, "请选择安全问题"));
                checked && (checked = checkForm.checkEmpty($new_answer, "请输入安全问题答案"));
                checked && (checked = checkForm.checkEmpty($pwd, "请输入支付密码"));

                if (checked) {
                    var data = {
                        payPwd: md5($pwd_v),
                        newQuestion: $new_question_v,
                        newAnswer: md5($new_answer_v),
                        safeAnswer: md5($answer_v)
                    };
                    $.post("/ajax/login/user/safe/change.html", data, function (data) {
                        var retcode = data.retcode;
                        var retdesc = data.retdesc;
                        switch (retcode) {
                            case 0:
                                Alert.show(function () {
                                    this.redirect = true;
                                    this.$(".txt-center").html(data.retdesc);
                                });
                                break;
                            case 3010601:
                                $new_answer_msg.hide().html(retdesc).fadeIn();
                                break;
                            case 3010602:
                                $new_answer_msg.hide().html(retdesc).fadeIn();
                                break;
                            case 3010603:
                                $pwd_msg.hide().html(retdesc).fadeIn();
                                break;
                            case 3010604:
                                $pwd_msg.hide().html(retdesc).fadeIn();
                                break;
                            case 3010605:
                                $answer_msg.hide().html(retdesc).fadeIn();
                                break;
                            default:
                                Alert.show(function () {
                                    this.$(".txt-center").html(retdesc);
                                });
                        }
                    }).error(function () {
//                        var data = {"retcode":0,"retdesc":"操作成功","result":{}};
                        // var data = {"retcode":3010601, "retdesc":"安全问题答案长度不能大于40个字节或20个汉字","result":{}};
                        // var data = {"retcode":3010602, "retdesc":"安全问题长度不能大于40个字节或20个汉字","result":{}};
                        // var data = {"retcode":3010603, "retdesc":"支付密码连续输入错误5次被冻结","result":{}};
                        // var data = {"retcode":3010604, "retdesc":"支付密码错误","result":{}};
                        // var data = {"retcode":3010605, "retdesc":"安全问题答案错误","result":{}};
                        // var data = {"retcode":3010606, "retdesc":"修改安全问题失败","result":{}};


//                        var retcode = data.retcode;
//                        var retdesc = data.retdesc;
//
//                        // 测试用
//                        var e = location.search.match(/e=\d+/g);
//                        var h = {
//                            3010601:"安全问题答案长度不能大于40个字节或20个汉字",
//                            3010602:"安全问题长度不能大于40个字节或20个汉字",
//                            3010603:"支付密码连续输入错误5次被冻结",
//                            3010604:"支付密码错误",
//                            3010605:"安全问题答案错误",
//                            3010606:"修改安全问题失败"
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
//                            case 3010601:
//                                $new_answer_msg.hide().html(retdesc).fadeIn();
//                                break;
//                            case 3010602:
//                                $new_answer_msg.hide().html(retdesc).fadeIn();
//                                break;
//                            case 3010603:
//                                $pwd_msg.hide().html(retdesc).fadeIn();
//                                break;
//                            case 3010604:
//                                $pwd_msg.hide().html(retdesc).fadeIn();
//                                break;
//                            case 3010605:
//                                $answer_msg.hide().html(retdesc).fadeIn();
//                                break;
//                            default:
//                                Alert.show(function(){
//                                    this.$(".txt-center").html(retdesc);
//                                });
//                        }
                    })


                }
                return false
            })
        }
    })


    return function () {
        _$(".find_name").on("click", function () {
            d_find_name.show()
            return
        })
        _$(".find_question").on("click", function () {
            d_find_question.show()
            return
        })
        _$(".reset_answer").on("click", function () {
            d_reset_answer.show()
            return
        })
        var AlertB = dialogAlert("联系客服");
        _$(".btn-white").on("click", function () {
            if (document.getElementById("live800iconlink") === undefined) {
                AlertB.show(function () {
                    this.$(".txt-center").html("客服程序正在准备中，请稍后再试");
                });
            } else {
  
                if (document.all) {
                    document.getElementById("live800iconlink").click();
                    //console.log("involked in document.all执行")
                }
                else {
                    var evt = document.createEvent("MouseEvents");
                    evt.initEvent("click", true, true);
                    document.getElementById("live800iconlink").dispatchEvent(evt);
                }
            }

        })

    }


})


