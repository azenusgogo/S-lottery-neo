define(function () {
    var utils = {
        checkNickName: function ($el, msg) {
            var $msg = $el.nextAll(".valid");
            var v = $el.val();
            var v1 = v.match(/[\u4e00-\u9fa5]/g) || [];
            var len = v.length + v1.length;

            if (!/^[\u4e00-\u9fa5\w]+$/g.test(v)) {
                $msg.hide().html("昵称只能使用汉字,字母,数字或下划线").fadeIn();
                return false;
            } else {
                if (len > 3 && len < 17) {
                    return true;
                } else {
                    $msg.hide().html(msg).fadeIn();
                    return false;
                }
            }
        },
        checkCNLength: function ($el, msg) {
            var $msg = $el.nextAll(".valid");
            var v = $el.val();
            if (/^[\u4e00-\u9fa5]{2,8}$/g.test(v)) {
                return true;
            } else {
                $msg.hide().html(msg).fadeIn();
                return false;
            }
        },
        checkSelect: function ($el, msg) {
            var $msg = $el.nextAll(".valid");
            var v = $el.val();
            if (v == 0) {
                $msg.hide().html(msg).fadeIn();
                return false
            }
            $msg.hide().html("");
            return true
        },
        checkEmpty: function ($el, msg) {
            var $msg = $el.nextAll(".valid");
            var v = $el.val();
            if (v.length == 0 || v == "请填写收到的短信验证码") {
                msg && $msg.hide().html(msg).fadeIn();
                return false
            }
            $msg.hide().html("");
            return true
        },
        checkSame: function ($el1, $el2) {
            var $msg = $el2.nextAll(".valid");

            var v1 = $el1.val();
            var v2 = $el2.val();
            if (v1 == v2) {
                $msg.hide().html("新密码需要和原密码不同，请重新输入").fadeIn();
                return false;
            }
            $msg.hide().html("");
            return true
        },
        checkMobile: function ($el, msg) {
            var $msg = $el.nextAll(".valid");
            var v = $el.val();
            if (/^0?(13[0-9]|15[0-9]|18[0-9]|14[57])[0-9]{8}$/.test(v)) {
                return true
            }
            $msg.hide().html(msg).fadeIn();
            return false
        },
        checkEmail: function ($el, msg) {
            var $msg = $el.nextAll(".valid");
            var v = $el.val();
            if (/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/.test(v)) {
                return true
            }
            $msg.hide().html(msg).fadeIn();
            return false
        },
        timer: function (time, type) {
            var t;
            time || (time == 60);
            var time2 = time;
            var lock = 1;
            return function ($el, mobile) {
                if (lock && !$el.hasClass('disabled')) {
                    lock = 0;
                    $el.html("60 秒后重发").addClass('disabled');
                    var $msg = $el.nextAll(".dia-green");
                    var data = {};
                    var url;
                    if (type == 1) {
                        url = "/ajax/login/user/mobile/captcha/bindnew.html"
                    } else if (type == 2) {
                        url = "/ajax/login/user/mobile/captcha/bind.html"
                    } else if (type == 3) {
                        url = "/ajax/login/user/mobile/captcha/find.html"
                    } else {
                        url = ""
                    }

                    if (mobile) {
                        data = {mobile: mobile}
                    }
                    $.post(url, data, function (data) {
                        if (data.retcode == 0) {
                            $msg.html("发送成功").show()
                        } else if (data.retcode == 3010404) {
                            $msg.html("操作过频").addClass('dia-red').show()
                        } else if (data.retcode == 3010403) {
                            $msg.html("发送失败").addClass('dia-red').show()
                        } else {
                            $msg.html("发送失败").addClass('dia-red').show()
                        }
                    }).error(function () {
                        // var data = {"retcode":0,"retdesc":"操作成功","result":{}}
                        // var data = {"retcode":3010401,"retdesc": "绑定手机格式错误":{}}
                        // var data = {"retcode":3010402,"retdesc": "用户未绑定手机":{}}
                        // var data = {"retcode":3010403,"retdesc": "下发验证码失败":{}}
                        // if (data.retcode == 0){
                        //     $msg.html("发送成功").show()
                        // }else{
                        //     $msg.html("发送失败").addClass('dia-red').show()
                        // }
                    });
                    t = setInterval(function () {
                        if (time == 0) {
                            lock = 1;
                            clearInterval(t);
                            time = time2;
                            $el.html("发送验证码").removeClass('disabled');
                            $msg.html("").removeClass('dia-red').hide()
                            return
                        }
                        $el.html(--time + " 秒后重发").addClass('disabled')
                    }, 1000)
                }
            }
        },
        allSame: function (v) {
            v = v.toString();
            var s;
            for (var i = v.length; i--;) {
                s = v.charAt(i - 1);
                if (s && v.charAt(i) != s) {
                    return false
                }
            }
            return true
        },
        checkNewPaypwd: function ($el) {
            var $msg = $el.nextAll(".valid");
            var v = $el.val();
            if (v.length == 0) {
                $msg.hide().html("新密码不能为空，请填写").fadeIn();
                return false
            } else if (v.length < 6 || v.length > 16) {
                $msg.hide().html("请输入6-16位密码").fadeIn();
                return false
            } else if (/^[0-9]*$/.test(v)) {
                $msg.hide().html("密码不能为纯数字").fadeIn();
                return false
            } else if (v.indexOf(" ") > -1) {
                $msg.hide().html("密码中不能含空格").fadeIn();
                checked = false
            } else if (utils.allSame(v)) {
                $msg.hide().html("密码不能为相同字母或符号").fadeIn();
                return false
            }
            $msg.hide().html("");
            return true
        },
        checkNewPaypwd2: function ($el1, $el2) {
            var $msg = $el2.nextAll(".valid");
            var v1 = $el1.val();
            var v2 = $el2.val();
            if (v2.length == 0) {
                $msg.hide().html("请输入确认密码").fadeIn();
                return false
            } else if (v2 != v1) {
                $msg.hide().html("两次输入的新密码不一致，请确认后重新输入").fadeIn();
                return false
            }
            $msg.hide().html("");
            return true
        },
        checkCard: function ($el) {
            var $msg = $el.nextAll(".valid");
            var v = $el.val();
            if (v.length == 0) {
                $msg.hide().html("银行卡号不能为空").fadeIn();
                return false
            }
            var reg = /^\d*$/;
            if (!reg.test(v)) {
                $msg.hide().html("银行卡号格式错误").fadeIn();
                return false
            }
            if (v.length < 16 || v.length > 22) {
                $msg.hide().html("银行卡号长度错误").fadeIn();
                return false
            }
            $msg.hide().html("");
            return true
        },
        checkMoney: function ($el) {
            var v = $el.val();
            if (/^\d+\.?\d{0,2}$/g.test(v)) {
                return true
            }
            return false
        },
        checkMoneyRange: function ($el, min, max) {
            var v = +$el.val();
            return (v <= +max && v >= +min)
        },
        checkIdCard: function ($el, msg) {
            var Wi = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1]; // 加权因子
            var ValideCode = [1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2]; // 身份证验证位值.10代表X
            var $msg = $el.nextAll(".valid");
            var idCard = $el.val();

            if (!/ /g.test(idCard) && IdCardValidate(idCard)) { //返回身份证校验结果
                return true;
            } else {
                $msg.hide().html(msg).fadeIn();
                return false;
            }

            function IdCardValidate(idCard) {
                if (idCard.length == 15) {
                    return isValidityBrithBy15IdCard(idCard); //进行15位身份证的验证
                } else if (idCard.length == 18) {
                    var a_idCard = idCard.split(""); // 得到身份证数组
                    if (isValidityBrithBy18IdCard(idCard) && isTrueValidateCodeBy18IdCard(a_idCard)) { //进行18位身份证的基本验证和第18位的验证
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }

            /**
             * 判断身份证号码为18位时最后的验证位是否正确
             * @param a_idCard 身份证号码数组
             * @return
             */
            function isTrueValidateCodeBy18IdCard(a_idCard) {
                var sum = 0; // 声明加权求和变量
                if (a_idCard[17].toLowerCase() == 'x') {
                    a_idCard[17] = 10; // 将最后位为x的验证码替换为10方便后续操作
                }
                for (var i = 0; i < 17; i++) {
                    sum += Wi[i] * a_idCard[i]; // 加权求和
                }
                valCodePosition = sum % 11; // 得到验证码所位置
                if (a_idCard[17] == ValideCode[valCodePosition]) {
                    return true;
                } else {
                    return false;
                }
            }

            /**
             * 验证18位数身份证号码中的生日是否是有效生日
             * @param idCard 18位书身份证字符串
             * @return
             */
            function isValidityBrithBy18IdCard(idCard18) {
                var year = idCard18.substring(6, 10);
                var month = idCard18.substring(10, 12);
                var day = idCard18.substring(12, 14);
                var temp_date = new Date(year, parseFloat(month) - 1, parseFloat(day));
                // 这里用getFullYear()获取年份，避免千年虫问题
                if (temp_date.getFullYear() != parseFloat(year) || temp_date.getMonth() != parseFloat(month) - 1 || temp_date.getDate() != parseFloat(day)) {
                    return false;
                } else {
                    return true;
                }
            }

            /**
             * 验证15位数身份证号码中的生日是否是有效生日
             * @param idCard15 15位书身份证字符串
             * @return
             */
            function isValidityBrithBy15IdCard(idCard15) {
                var year = idCard15.substring(6, 8);
                var month = idCard15.substring(8, 10);
                var day = idCard15.substring(10, 12);
                var temp_date = new Date(year, parseFloat(month) - 1, parseFloat(day));
                // 对于老身份证中的你年龄则不需考虑千年虫问题而使用getYear()方法   
                if (temp_date.getYear() != parseFloat(year) || temp_date.getMonth() != parseFloat(month) - 1 || temp_date.getDate() != parseFloat(day)) {
                    return false;
                } else {
                    return true;
                }
            }
        }
    }
    return utils
})
