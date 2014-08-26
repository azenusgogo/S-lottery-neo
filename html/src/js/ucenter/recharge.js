define([
    "lib/alert",
    "lib/check.form",
    "lib/tools"
],function(dialogAlert,checkForm,tools){
    var $el = $("#user_recharge");
    if ($el.length == 0) return function(){};

    // ==========================================================
    var _$ = function(selector) {
        return $el.find(selector)
    };

    var Alert = dialogAlert("充值中心");


    return function(){
        var $money = _$(".money");
        var $amount = _$(".amount");
        var $banks = _$(".bank-ico").parent();
        var $banks_ico = _$(".bank-ico");
        var $submit = _$(".submit");
        var $radio = _$("input[name='bankId']");
        var $show_bank = _$(".show_bank");

        $money.on("keyup",function(){
            tools.enterFloat($(this))
        });

        $money.on("blur",function(){
            var $this = $(this);
            var v = $this.val();
            if (v.slice(-1)=="."){
                $this.val(v.slice(0,-1))
            }
        });


        // 点击 span 会冒泡到input 执行2次 。。。。。。
        $banks.on("click",function(){
            $banks_ico.removeClass('cur');
            $(this).find('span').addClass('cur')
        });


        $show_bank.on("click",function(){
            $(this).parent().hide().prev().css("height",240);

            return false
        });

        $submit.on("click",function(){

            var min = $("#minAmount").val();
            var max = $("#maxAmount").val();

            if (checkForm.checkMoney($money,min)){
                if (checkForm.checkMoneyRange($money,min,max)) {
                    if ($radio.is(":checked")){
                        if(+$money.val() > +max){
                            Alert.show(function(){
                                this.$(".txt-center").html("充值金额不得超过"+ max +"元");
                            });
                            return false;
                        }
                        $amount.val(tools.FloatMul($money.val(),100));
                        spb_vars && spb_vars.pingback(0, "pbtag=点击去充值按钮", "extra");
                        $el.submit();
                    }else{
                        Alert.show(function(){
                            this.$(".txt-center").html("请选择银行");
                        });
                    }
                } else {
                    Alert.show(function(){
                        this.$(".txt-center").html("请确认您的充值金额，单笔最少充值"+min+"元，最多充值"+max+"元");
                    });
                }
            }else{
                Alert.show(function(){
                    this.$(".txt-center").html("请输入正确的充值金额");
                });
            }
            return false;
        })

    }
    
})