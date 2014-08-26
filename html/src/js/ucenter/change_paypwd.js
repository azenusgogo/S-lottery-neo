define([
    'lib/md5',
    'lib/alert',
    "lib/check.form"
],function(md5, dialogAlert,checkForm){
    var $el = $("#change_paypwd");
    if ($el.length == 0) return function(){};

    // ==========================================================
    var _$ = function(selector) {
      return $el.find(selector)
    };
    var Alert = dialogAlert("修改支付密码");


    var _check = function($pwd,$pwd1,$pwd2){
        var pwd_v = $pwd.val(),
            $pwd_msg = $pwd.nextAll(".valid");
        var pwd1_v = $pwd1.val(),
            $pwd1_msg = $pwd1.nextAll(".valid");
        var pwd2_v = $pwd2.val();
        var checked = true;
        var data;

        checked = checkForm.checkEmpty($pwd,"请先输入原密码");
        checked && (checked = checkForm.checkNewPaypwd($pwd1));
        checked && (checked = checkForm.checkSame($pwd,$pwd1));
        checked && (checked = checkForm.checkNewPaypwd2($pwd1,$pwd2));

        if (checked){
            data = {
                payPwd : md5(pwd_v),
                newPayPwd :md5(pwd1_v),
                newPayPwdConfirm :md5(pwd2_v)
            };
            $.post('/ajax/login/user/pwd/change.html', data, function(data) {
                var retcode = data.retcode;
                var retdesc = data.retdesc;
                switch(retcode){
                    case 0:
                        Alert.show(function(){
                            this.redirect = true;
                            this.$(".txt-center").html(retdesc);
                        });
                        break;
                    case 4000402:
                        $pwd1_msg.hide().html(retdesc).fadeIn();
                        break;
                    case 4000403:
                        $pwd1_msg.hide().html(retdesc).fadeIn();
                        break;
                    case 4000404:
                        $pwd1_msg.hide().html(retdesc).fadeIn();
                        break;
                    case 3010802:
                        $pwd_msg.hide().html(retdesc).fadeIn();
                        break;
                    default:
                        Alert.show(function(){
                            this.$(".txt-center").html(retdesc);
                        });
                }
            },"json").error(function(){
                /*var data =  {"retcode":0,"retdesc":"操作成功","result":{}};
                // var data = {"retcode":300003, "retdesc": "用户名或密码不正确","result":{}};
                // var data = {"retcode":4000402,"retdesc": "新旧支付密码不能相同","result":{}};
                // var data = {"retcode":4000403,"retdesc": "两次输入的支付密码不相同","result":{}};
                // var data = {"retcode":4000404,"retdesc": "支付密码不能与登录密码相同","result":{}};
                // var data = {"retcode":3010801,"retdesc": "支付密码连续输入错误5次被冻结","result":{}};
                // var data = {"retcode":3010802,"retdesc": "支付密码错误","result":{}};
                // var data = {"retcode":3010803,"retdesc": "账户被禁用","result":{}};
                // var data = {"retcode":3010803,"retdesc": "修改支付密码失败","result":{}};
                // var data = {"retcode":4000301,"retdesc": "用户信息未补全","result":{}};
                        
                        
                var retcode = data.retcode;
                var retdesc = data.retdesc;

                // 测试用
                var e = location.search.match(/e=\d+/g);
                var h = {
                    300003:"用户名或密码不正确",
                    4000402:"新旧支付密码不能相同",
                    4000403:"两次输入的支付密码不相同",
                    4000404:"支付密码不能与登录密码相同",
                    3010801:"支付密码连续输入错误5次被冻结",
                    3010802:"支付密码错误",
                    3010803:"账户被禁用",
                    3010804:"修改支付密码失败",
                    4000301:"用户信息未补全"
                }
                retcode = e ? +e[0].replace("e=","") : retcode;
                retdesc = h[retcode] || retdesc;

                switch(retcode){
                    case 0:
                        Alert.show(function(){
                            this.redirect = true;
                            this.$(".txt-center").html(retdesc);
                        });
                        break;
                    case 4000402:
                        $pwd1_msg.hide().html(retdesc).fadeIn();
                        break;
                    case 4000403:
                        $pwd1_msg.hide().html(retdesc).fadeIn();
                        break;
                    case 4000404:
                        $pwd1_msg.hide().html(retdesc).fadeIn();
                        break;
                    case 3010802:
                        $pwd_msg.hide().html(retdesc).fadeIn();
                        break;
                    default:
                        Alert.show(function(){
                            this.$(".txt-center").html(retdesc);
                        });
                }*/
            });
        }
    }
    


    return function(){
        var $submit = _$(".fbtn");
        var $pwd = _$(".pwd");
        var $pwd1 = _$(".pwd1");
        var $pwd2 = _$(".pwd2");

        $pwd.add($pwd1).add($pwd2).on("focus",function(){
            $(this).nextAll(".valid").html("")
        })

        $pwd.on("blur",function(){
            checkForm.checkEmpty($(this),"请先输入原密码")
        });
        $pwd1.on("blur",function(){
            var cheked = checkForm.checkNewPaypwd($(this));
            cheked && checkForm.checkSame($pwd,$(this));
        });
        $pwd2.on("blur",function(){
            checkForm.checkNewPaypwd2($pwd1,$(this))
        });
        $submit.on("click",function(){
            _check($pwd,$pwd1,$pwd2);
            return false
        })
        

    }
})