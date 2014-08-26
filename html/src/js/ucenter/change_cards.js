define([
    "lib/dialog",
    "lib/md5",
    "lib/alert",
    "lib/check.form"
],function(Dialog,md5,dialogAlert,checkForm){
    var $el = $("#my_info,#my_cash");
    if ($el.length == 0) return function(){};
    // ==========================================================
    var _$ = function(selector) {
        return $el.find(selector)
    };
    var Alert = dialogAlert("修改银行卡");
    var dialog1 = new Dialog({
        el:"#change_card1",
        width:560,
        onload:function(){
            var $bank = this.$(".bank");
            var $bank_n = this.$(".bank_n");
            var $bind = this.$(".bind");
            var $bank_tips = this.$(".bankcard-tip");
            var $bank_tips_v = this.$(".bankcard-tip span");
            $bank_n.on("focus",function(){
                $(this).nextAll(".valid").html("")
            });
            $bank_n.on("keyup",function(){
                var $this = $(this);
                var v = $this.val();
                v = v.replace(/\D/g,"").slice(0,19);
                $this.val(v);
                if (v.length>0){
                    $bank_tips_v.html(v.slice(0,4)+" "+v.slice(4,8)+" "+v.slice(8,12)+" "+v.slice(12,16)+" "+v.slice(16,19));
                    $bank_tips.show()
                }else{
                    $bank_tips.hide()
                }
            })

            $bank.on("change",function(){
                checkForm.checkSelect($(this),"请选择银行卡")
            });

            $bank_n.on("blur",function(){
                checkForm.checkCard($(this),"请输入银行卡卡号")
            })

            $bind.on("click",function(){
                var $bank_v = $bank.val(),
                    $bank_msg = $bank.nextAll(".valid");
                var $bank_n_v = $bank_n.val(),
                    $bank_n_msg = $bank_n.nextAll(".valid");
                var checked = checkForm.checkSelect($bank,"请选择银行卡");
                checked && (checked = checkForm.checkCard($bank_n,"请输入银行卡卡号"));

                if (checked){
                    dialog1.hide();
                    dialog2.show(function(){
                        this._data = {
                            bankCardNo:$bank_n_v,
                            bankId:$bank_v
                        };
                        this.$(".bank_name").html($bank.find("option:selected").html());
                        this.$(".bank_n").html($bank_n_v)
                    })
                }
                return false
            })

        }
    })
    var dialog2 = new Dialog({
        el:"#change_card2",
        width:450,
        onload:function(){
            var $submit = this.$(".submit");
            var $back = this.$(".back");
            $back.on("click",function(){
                dialog2.hide();
                dialog1.show();
                return false
            })
            var _this = this;
            $submit.on("click",function(){
                $.post('/ajax/login/user/withdraw/bank/bind.html', _this._data, function(data) {
                    Alert.show(function(){
                        if (data.retcode==0){
                            dialog2.hide();
                            this.redirect = true
                        }
                        this.$(".txt-center").html(data.retdesc);
                    })
                }).error(function(){
//                    var data = {"retcode":0,"retdesc":"操作成功","result":{}};
                    //data = {"retcode":4000301,"retdesc":"用户信息未补全"}
//                    Alert.show(function(){
//                        if (data.retcode==0){
//                            dialog2.hide();
//                            this.redirect = true
//                        }
//                        this.$(".txt-center").html(data.retdesc);
//                    })
                });
                return false
            })
        }
    })

    _$(".change_card").on("click",function(){
        dialog1.show()
        return false
    })


    return function(){

    }
})