define([
    "lib/md5",
    "lib/alert",
    "lib/check.form",
    "lib/tools"
],function(md5,dialogAlert,checkForm,tools){
    
    var $el = $("#my_cash");
    if ($el.length == 0) return function(){};
    // ==========================================================
    var _$ = function(selector) {
        return $el.find(selector)
    };
    var Alert = dialogAlert("提款申请");

    var $money = _$(".money");
    var $pwd = _$(".pwd");
    var $code = _$(".code");
    var $codePic = $("#codePic");
    var fee_v = $("#fee_v").html();
    var cash_tip = $("#cash_tip").html();
    //var inputFee = _$(".inputFee").val();
    //inputFee || (inputFee = 0);
    
    return function(){
        $money.on("keyup",function(){
            tools.enterFloat($(this))

            // if (!/^\-?([1-9]\d*|0)(\.\d?[1-9])?$/.test(+v)){
            //    
            // }
        })
        $money.on("blur",function(){
            var $this = $(this);
            var v = $this.val();
            
            if (v.slice(-1)=="."){
                $this.val(v.slice(0,-1))
            }

            if(+v >= 5){
                $("#cash_tip").html("预计到账<em>"+(v-fee_v)+"</em>元（扣除"+ fee_v +"元手续费）")
            }else{
                $("#cash_tip").html(cash_tip);
            }
        })

        _$(".submit").on("click",function(){
            var $money_v = $money.val();
            var $pwd_v = $pwd.val();
            var $code_v = $code.val();

            var min = $("#minMoney").val();
            var max = $("#maxMoney").val();

            if (checkForm.checkMoney($money)){
                if (checkForm.checkMoneyRange($money,min,max)){
                    if (checkForm.checkEmpty($pwd)){
                        if (checkForm.checkEmpty($code)){
                            var data = {
                                payPwd:md5($pwd_v),
                                amount:tools.FloatMul($money_v,100),
                                //fee:inputFee,
                                captcha:$code_v
                            }
                            $.post('/ajax/login/user/withdraw/apply.html', data, function(data) {
                                Alert.show(function(){
                                    var retdesc = data.retdesc;
                                    if (data.retcode == 0){
                                        this.redirect = true
                                        retdesc = "您的提款申请已成功提交"
                                    }else{
                                        var src = $codePic.attr("src").split('?')[0]+'?t='+ +new Date();
                                        $codePic.attr("src",src)
                                    }
                                    this.$(".txt-center").html(retdesc);
                                })
                            }).error(function() {
                                // var data = {"retcode":0,"retdesc":"操作成功","result":{}};
                                // var data = {"retcode":3011201, "retdesc":"提现用户名错误","result":{}};
                                // var data = {"retcode":3011202, "retdesc":"请补全账户","result":{}};
                                // var data = {"retcode":3011203, "retdesc":"支付密码连续输入错误5次被冻结","result":{}};
                                // var data = {"retcode":3011204, "retdesc":"支付密码错误","result":{}};
                                // var data = {"retcode":3011205, "retdesc":"提现金额超出范围","result":{}};
                                // var data = {"retcode":3011206, "retdesc":"可提现金额不足","result":{}};
                                // var data = {"retcode":3011207, "retdesc":"收款银行账户错误","result":{}};
                                // var data = {"retcode":3011208, "retdesc":"提现失败","result":{}};

                                // Alert.show(function(){
                                //     var retdesc = data.retdesc;
                                //     if (data.retcode == 0){
                                //         this.redirect = true
                                //         retdesc = "您的提款申请已成功提交"
                                //     }
                                //     this.$(".txt-center").html(retdesc);
                                // })
                            });
                        }else{
                            Alert.show(function(){
                                this.$(".txt-center").html("请输入验证码");
                            })
                        }
                    }else{
                        Alert.show(function(){
                            this.$(".txt-center").html("请输入支付密码");
                        })
                    }
                } else {
                    Alert.show(function(){
                        this.$(".txt-center").html("请确认您的提款金额，单笔最少提款"+min+"元，最多"+max+"元，超出金额请联系客服。");
                    })
                }
            }else{
                Alert.show(function(){
                    this.$(".txt-center").html("请输入正确的提款金额");
                })
            }

            return false
        });
        $money.focus();
    }


    

    
})