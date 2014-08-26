define([
    "lib/dialog",
    "lib/md5",
    "lib/alert",
    "lib/check.form"
], function (Dialog, md5, dialogAlert, checkForm) {
    var $el = $("#fill_info");
    if ($el.length == 0) return function () {
    };

    // ==========================================================
    var _$ = function (selector) {
        return $el.find(selector)
    };

    var Alert = dialogAlert("补全信息");
    return function () {
        var $pwd = _$(".pwd");
        var $nickname = _$(".nickname");
        var $paypwd1 = _$(".paypwd1");
        var $paypwd2 = _$(".paypwd2");
        var $question = _$(".question");
        var $answer = _$(".answer");
        var $phone_n = _$(".phone_n");
        var $code = _$(".code");
        var $send_code = _$(".send-code");
        //var $email = _$(".email");
        var $uname = _$(".uname");
        var $zj = _$(".zj");
        var $zj_n = _$(".zj_n");
        var $pact = _$(".pact");
        var $submit = _$(".submit");


        $pwd.add($paypwd1).add($paypwd2).add($answer).add($phone_n)
            .add($code).add($uname).add($zj_n).on("focus", function () {
                $(this).nextAll(".valid").html("")
            });

        $pwd.on("blur", function () {
            checkForm.checkEmpty($(this), "登录密码不能为空，请填写")
        });

        $nickname.on("blur", function () {
            var checked = checkForm.checkEmpty($(this), "昵称不能为空，请填写");
            checked && (checkForm.checkNickName($(this), "昵称不符合长度要求，请输入4-16个字符或2-8个汉字"));
        });

        $paypwd1.on("blur", function () {
            checkForm.checkEmpty($(this), "支付密码不能为空，请填写")
        });

        $paypwd2.on("blur", function () {
            checkForm.checkNewPaypwd2($paypwd1, $paypwd2)
        });

        $question.on("change", function () {
            checkForm.checkSelect($(this), "请选择安全问题")
        });

        $answer.on("blur", function () {
            checkForm.checkEmpty($(this), "安全问题答案不能为空，请填写")
        });

        $phone_n.on("blur", function () {
            var checked = checkForm.checkEmpty($(this), "手机号码不能为空，请填写")
            checked && checkForm.checkMobile($(this), "手机号码格式错误")
        });

        var checkFormTimer = checkForm.timer(60, 1);
        $send_code.on("click", function () {
            var checked = checkForm.checkEmpty($phone_n, "手机号码不能为空，请填写");
            checked && (checked = checkForm.checkMobile($phone_n, "手机号码格式错误"));
            if (checked) {
                checkFormTimer($(this), $phone_n.val());
            }
            return false
        })

        $code.on("blur", function () {
            checkForm.checkEmpty($(this), "验证码不能为空，请填写")
        })

        // $email.on("blur", function () {
        //     var checked = checkForm.checkEmpty($(this), "邮箱不能为空，请填写")
        //     checked && checkForm.checkEmail($(this), "邮箱格式错误")
        // })

        $uname.on("blur", function () {
           var checked =  checkForm.checkEmpty($(this), "姓名不能为空，请填写");
               checked && (checked =  checkForm.checkCNLength($(this),"必须是汉字且长度必须在2~8个之间"));            
        });

        $zj.on("change", function () {
            checkForm.checkSelect($(this), "请选择证件类型")
        });

        $zj_n.on("blur", function () {
            checkForm.checkSelect($(this), "证件号码不能为空，请填写")
        });

        $submit.on("click", function () {
        	try{
            	spb_vars && spb_vars.pingback(0, "pbtag=点击提交信息按钮", "extra");
            }catch(e){}
            //var $userid_v = $("#userid").val();
            var $pwd_v = $pwd.val() || "";
            var $nickname_v = $nickname.val();
            var $paypwd1_v = $paypwd1.val();
            var $paypwd2_v = $paypwd2.val();
            var $question_v = $question.find(":selected").html();
            var $question_id_v = $question.val();
            var $answer_v = $answer.val();
            var $phone_n_v = $phone_n.val();
            var $code_v = $code.val();
            //var $email_v = $email.val();
            var $uname_v = $uname.val();
            var $zj_v = $zj.val();
            var $zj_n_v = $zj_n.val();
            var checked = true;

            if($pwd.length != 0){
                checked && (checked = checkForm.checkEmpty($pwd, "登录密码不能为空，请填写"));
            }

            checked && (checked = checkForm.checkEmpty($nickname, "昵称不能为空，请填写"));
            checked && (checked = checkForm.checkNickName($nickname, "昵称不符合长度要求，请输入4-16个字符"));
            checked && (checked = checkForm.checkEmpty($paypwd1, "支付密码不能为空，请填写"));
            checked && (checked = checkForm.checkNewPaypwd2($paypwd1, $paypwd2));
            checked && (checked = checkForm.checkSelect($question, "请选择安全问题"));
            checked && (checked = checkForm.checkEmpty($answer, "安全问题答案不能为空，请填写"));
            checked && (checked = checkForm.checkEmpty($phone_n, "手机号码不能为空，请填写"));
            checked && (checked = checkForm.checkMobile($phone_n, "手机号码格式错误"));
            checked && (checked = checkForm.checkEmpty($code, "验证码不能为空，请填写"));
            //checked && (checked = checkForm.checkEmpty($email, "邮箱不能为空，请填写"));
            //checked && (checked = checkForm.checkEmail($email, "邮箱格式错误"));
            checked && (checked = checkForm.checkEmpty($uname, "姓名不能为空，请填写"));
            checked && (checked = checkForm.checkCNLength($uname,"必须是汉字且长度必须在2~8个之间"));
            checked && (checked = checkForm.checkSelect($zj, "请选择证件类型"));
            checked && (checked = checkForm.checkSelect($zj_n, "证件号码不能为空，请填写"));
            

            if($zj.val() == 1){//证件号码为身份证时进行验证
                checked && (checked = checkForm.checkIdCard($zj_n, "请填写正确的身份证号"));
            }

            if (checked) {
                if (!$pact.is(":checked")) {
                    Alert.show(function () {
                        this.$(".txt-center").html("未满18周岁？");
                    });
                    checked = false
                }
            }

            if (checked) {

                var data = {
                    //"userId": $userid_v,
                    "pwd": md5($pwd_v),
                    "payPwd": md5($paypwd1_v),
                    "payPwdConfirm": md5($paypwd2_v),
                    "idCardType": $zj_v,
                    "idCardNo": $zj_n_v,
                    //"email": $email_v,
                    "mobile": $phone_n_v,
                    "safeQuestion": $question_v,
                    "safeAnswer": md5($answer_v),
                    "questionType": $question_id_v,
                    "trueName": $uname_v,
                    "nickName": $nickname_v,
                    "captcha": $code_v
                };

                $.post("/ajax/login/user/info/add.html", data, function (data) {
                    Alert.show(function () {
                        if (data.retcode == 0){
                            this.redirect = true;                            
                            try{
                            	spb_vars && spb_vars.pingback(0, "pbtag=完成完善个人信息", "extra");
                            }catch(e){}
                        }
                        this.$(".txt-center").html(data.retdesc);
                    });
                }).error(function(){
//                    var data = {"retcode":0,"retdesc":"操作成功","result":{}};
//                    Alert.show(function () {
//                        data.retcode == 0 && (this.redirect = true);
//                        this.$(".txt-center").html(data.retdesc);
//                    });
                })
            }
            return false
        })
    }
})