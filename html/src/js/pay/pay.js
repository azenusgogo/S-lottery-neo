define([
    "lib/tools",
    "lib/alert",
    "lib/dialog",
    "lib/md5",
    "text!pay/confirm_pay.tpl",
    "text!pay/complete_pay.tpl",
    "text!pay/funds_lack.tpl",
    "text!pay/fill_info.tpl",
    "ad/buy_one_get_one"
], function (tools, dialogAlert, Dialog, md5, confirm_tpl, complete_tpl, funds_lack_tpl, fill_info_tpl,buy_one_get_one) {
    return function (gameId, payOrderId) {
        var Alert = dialogAlert("提示");

        var id1 = _.uniqueId("DIALOG_");
        var id2 = _.uniqueId("DIALOG_");
        var id3 = _.uniqueId("DIALOG_");
        var id4 = _.uniqueId("DIALOG_");
        $(confirm_tpl).attr("id", id1).prependTo("body");
        $(complete_tpl).attr("id", id2).prependTo("body");
        $(funds_lack_tpl).attr("id", id3).prependTo("body");
        $(fill_info_tpl).attr("id", id4).prependTo("body");
        
        var confirm = new Dialog({
            el: "#" + id1,
            width: 460,
            onload: function () {
                this.$period = this.$(".period");
                this.$pay_amount = this.$(".pay_amount");
                this.$balance_amount = this.$(".balance_amount");
                $pwd = this.$(".pwd");
                $msg = this.$(".msg");
                $submit = this.$(".submit");
                var _this = this;
                var lock = 0;
                var _check = function () {
                    var pwd_v = $pwd.val();
                    if (pwd_v.length == 0) {
                        $msg.hide().html("支付密码不能为空").fadeIn();
                        return
                    }
                    var data = {
                        payOrderId: _this.data.payOrderId,
                        payPwd: md5(pwd_v)
                    };
                    if (lock) return;
                    lock = 1;
                    $.post("/ajax/login/pay/order.html", data, function (data) {
                        lock = 0;
                        var retcode = data.retcode;
                        var retdesc = data.retdesc;
                        switch (retcode) {
                            case 0:
                                _this.hide();
                                $.get("/promotion/user/activity/ajax/isfirstpay.html?t="+new Date().getTime(), function (data) {            		
                        	    	//var data = {nickName:"小hui",ifshow:1};
                                	var ifshow = data.ifshow;
                                	if(ifshow){
                                		buy1get1.show(function(){
                                			this.data = data;
                                			this.complete = complete
                                		})
                                	}else{
                                		complete.show();
                                	}                       
                                }).error(function(data){
                                	/*var data = {nickName:"小南",ifshow:1};
                                	var ifshow = data.ifshow;
                                	if(ifshow){
                                		buy1get1.show(function(){
                                			this.data = data;
                                			this.complete = complete
                                		})
                                	}else{
                                		complete.show();
                                	}*/
                                })
                                
                                try {
                                    spb_vars && spb_vars.pingback(0, "pbtag=立即支付", "extra");
                                } catch (e) {
                                }
                                break;
                            case 4000007:
                                $msg.hide().html(retdesc).fadeIn();
                                break;
                            case 3011002:
                                $msg.hide().html(retdesc).fadeIn();
                                break;
                            default:
                                _this.hide();
                                Alert.show(function () {
                                    this.$(".txt-center").html(retdesc);
                                });
                        }
                    }).error(function () {
                        //lock = 0;
                        /*var data = {
                         "retcode": 0,
                         "retdesc": "操作成功",

                         "result": {
                         "payOrderId": "xxx",
                         "cashAmount": 1000000, // 支付金额
                         "paySystemId": "xx2",
                         "paySystemTime": "1234567890123",
                         "pwdErrorTimes":0, // //支付密码错误次数
                         "systemTime":1234567890123 // 系统时间

                         }
                         };*/
                        // var data = {"retcode":4000100, "retdesc":"订单不存在","result":{}};
                        // var data = {"retcode":4000001, "retdesc":"支付用户不存在","result":{}};
                        // var data = {"retcode":4000102, "retdesc":"订单已完成支付","result":{}};
                        // var data = {"retcode":4000103, "retdesc":"订单已关闭","result":{}};
                        // var data = {"retcode":4000104, "retdesc":"支付超时，等待退款","result":{}};
                        // var data = {"retcode":4000105, "retdesc":"支付订单已截止支付","result":{}};
                        // var data = {"retcode":4000106, "retdesc":"支付订单已不能继续支付","result":{}};
                        // var data = {"retcode":4000107, "retdesc":"订单所投期次不存在","result":{}};
                        // var data = {"retcode":4000007, "retdesc":"支付密码已经连续输入超过5次，请明天再试","result":{}};


                        /*var retcode = data.retcode;
                         var retdesc = data.retdesc;*/
                        // var result = data.result
                        // 测试用
                        /*var e = location.search.match(/e1=\d+/g);
                         var h = {
                         4000100:"订单不存在",
                         4000001:"支付用户不存在",
                         4000102:"订单已完成支付",
                         4000103:"订单已关闭",
                         4000104:"支付超时，等待退款",
                         4000105:"支付订单已截止支付",
                         4000106:"支付订单已不能继续支付",
                         4000107:"订单所投期次不存在",
                         4000007:"支付密码已经连续输入超过5次，请明天再试",
                         3011002:"支付密码错误"
                         };
                         retcode = e ? +e[0].replace("e1=","") : retcode;
                         retdesc = h[retcode] || retdesc;*/

                        /*switch (retcode){
                         case 0:
                         _this.hide();
                         complete.show();
                         break;
                         case 4000007:
                         $msg.hide().html(retdesc).fadeIn();
                         break;
                         case 3011002:
                         $msg.hide().html(retdesc).fadeIn();
                         break;
                         default:
                         _this.hide();
                         Alert.show(function(){
                         this.$(".txt-center").html(retdesc);
                         });
                         }*/

                    });
                };
                $pwd.on("keyup", function (e) {
                    if (e.keyCode == 13) {
                        _check()
                    }
                });
                $submit.on("click", function () {
                    _check();

                    return false
                })
            },
            onshow: function () {
                var data = this.data;
                if (!data) return;
                var $period = this.$period;
                var $pay_amount = this.$pay_amount;
                var $balance_amount = this.$balance_amount;

                var gameId = data.gameId;
                var jczq_id = {
                    "jczqspfp": 1,
                    "jczqrsfp": 1,
                    "jczqbfp": 1,
                    "jczqzjqp": 1,
                    "jczqbqcp": 1,
                    "jczqmixp": 1
                };
                var periodHtml = jczq_id[gameId] ? data.gameCn : data.gameCn + " 第" + data.periodNo + "期";
                $period.html(periodHtml);

                $pay_amount.html((data.payAmount / 100).toFixed(2));
                $balance_amount.html((data.balanceAmount / 100).toFixed(2))
            },
            onhide: function () {
                this.$(".pwd").val("");
            }
        });
        var buy1get1 = buy_one_get_one();
        var complete = new Dialog({
            el: "#" + id2,
            width: 430,
            onload: function () {
                // 关闭刷新
                this.$(".submit,.close").on("click", function () {
                    location.reload();
                	
                    return false
                });
            },
            onshow: function () {
                var $time = this.$(".time");
                var i = 5;
                $time.html(i);
                var t = this.t = setInterval(function () {
                    $time.html(--i);
                    if (i <= 0) {
                        clearInterval(t);
                        location.reload();
                    }
                }, 1000)
            },
            onhide: function () {
                this.t && clearInterval(this.t)            	
            }
        });
        
        var fundsLack = new Dialog({
            el: "#" + id3,
            width: 420,
            onload: function () {
                this.$submit = this.$(".submit");
                this.$nickName = this.$(".warn-cont span");
                this.$userId = this.$(".warn-cont em");
                this.$sum = this.$(".warn-cont b");
            }
        });
        var fill = new Dialog({
            el: "#" + id4,
            width: 370
        });

        if (payOrderId) { // 个人中心支付流程
            $.post("/ajax/login/pay/check.html", {payOrderId: payOrderId}, function (data) {
                var retcode = data.retcode;
                var retdesc = data.retdesc;
                var result = data.result

                if (retcode == 0) {
                    // 确认付款
                    confirm.show(function () {
                        this.data = result
                    });
                } else if (retcode == 4000101) {
                    fill.show();
                } else if (retcode == 4000109) {
                    fundsLack.show(function () {
                        var nickName = result.nickName || result.userId;
                        nickName = nickName.length > 16 ? (nickName.slice(0, 16) + "...") : nickName;
                        var amount = result.chargeAmount || "";
                        this.$nickName.html(nickName);
                        amount && this.$sum.html(tools.FloatDiv(amount, 100));
                        this.$submit.attr("href", "/login/charge/pre.html?payOrderId=" + result.payOrderId + "&amount=" + amount)
                    });
                } else {
                    Alert.show(function () {
                        this.$(".txt-center").html(retdesc);
                    });
                }
            }).error(function () {
                // var data = {
                //     "retcode": 0,
                //     "retdesc": "操作成功",
                //     "result": {
                //         "payOrderId": "xxx",
                //         "cashAmount": 1000000, // 支付金额
                //         "paySystemId": "xx2",
                //         "paySystemTime": "1234567890123",
                //         "pwdErrorTimes":0, // //支付密码错误次数
                //         "systemTime":1234567890123 // 系统时间

                //     }
                //  };

                // var retcode = data.retcode;
                // var retdesc = data.retdesc;
                // var result = data.result

                // if (retcode == 0){
                //     // 确认付款
                //     confirm.show(function () {
                //         this.data = result
                //     });
                // }else if (retcode == 4000109) {
                //     fundsLack.show(function () {
                //         this.$nickName.html(result.nickName);
                //         this.$userId.html(result.userId);
                //         this.$submit.attr("href", "/charge/pre.html?payOrderId=" + result.payOrderId)
                //     });
                // } else {
                //     Alert.show(function () {
                //         this.$(".txt-center").html(retdesc);
                //     });
                // }

            });
            return
        }

        var _pad = function (v) {
            if (!v) return;
            var i;
            for (i = v.length; i--;) {
                v[i] = +v[i];
                v[i] = v[i].toString().length == 1 ? ("0" + v[i]) : v[i]
            }
        };
        var models = this.models.models;
        var i, red, blue, red_dm, str, token, $addinput,
            mul = this.$mul.val(),
            price = this.models.count() * mul * 2 * 100,
            len = models.length,
            arr = [], arr1;
        if (price > 99000000 && gameId != "f14" && gameId == "f9") {
            Alert.show(function () {
                this.$(".txt-center").html("支付金额超过上限<b class=\"dia-red\">990000</b>元，请返回重新选择");
            })
            return
        }
        if (gameId == "ssq") {
            // 拼数据
            for (i = 0; i < len; i++) {
                red = models[i].get("red");
                blue = models[i].get("blue");
                red_dm = models[i].get("red_dm");
                _pad(red);
                _pad(blue);
                _pad(red_dm);
                arr[i] || (arr[i] = []);
                str = red.join(" ") + ":" + blue.join(" ");
                str = (red_dm && red_dm.length) ? red_dm.join(" ") + "$" + str : str;
                arr[i].push(str)
            }
        } else if (gameId == "dlt") {
            $addinput = $("#dltAdd");
            token = $addinput.prop("checked") ? "+" : "";
            for (i = 0; i < len; i++) {
                red = models[i].get("red");
                blue = models[i].get("blue");
                red_dm = models[i].get("red_dm");
                blue_dm = models[i].get("blue_dm");
                _pad(red);
                _pad(blue);
                _pad(red_dm);
                _pad(blue_dm);
                arr[i] || (arr[i] = []);
                str = red.join(" ") + ":" + ((blue_dm && blue_dm.length) > 0 ? blue_dm.join(" ") + "$" + blue.join(" ") : blue.join(" "));
                str = (red_dm && red_dm.length) > 0 ? token + red_dm.join(" ") + "$" + str : token + str;
                arr[i].push(str)
            }
        } else if (gameId == "qxc") {
            for (i = 0; i < len; i++) {
                arr[i] || (arr[i] = []);
                arr[i].push([
                    models[i].get("row1").join(""),
                    models[i].get("row2").join(""),
                    models[i].get("row3").join(""),
                    models[i].get("row4").join(""),
                    models[i].get("row5").join(""),
                    models[i].get("row6").join(""),
                    models[i].get("row7").join("")
                ].join(" "))
            }
        } else if (gameId == "qlc") {
            // 拼数据
            for (i = 0; i < len; i++) {
                red = models[i].get("red");
                red_dm = models[i].get("red_dm");
                _pad(red);
                _pad(red_dm);
                arr[i] || (arr[i] = []);
                str = red.join(" ");
                str = (red_dm && red_dm.length) ? red_dm.join(" ") + "$" + str : str;
                arr[i].push(str)
            }
        } else if (gameId == "k3" || gameId == "k3js" || gameId == "k3gx" || gameId == "k3jl") {
            var bet_type, number;
            for (i = 0; i < len; i++) {
                bet_type = models[i].get("bet_type");
                number = models[i].get("number");
                red_dm = models[i].get("red_dm");
                if (bet_type == 0) {
                    str = "HZ_" + number.join(",");
                } else if (bet_type == 1) {
                    str = "AAA_*"
                } else if (bet_type == 2) {
                    str = "AAA_" + number.join("");
                } else if (bet_type == 3) {
                    str = (red_dm && red_dm.length > 0) ? "3BT_" + red_dm.join("") + "$" + number.join("") : "3BT_" + number.join("")
                } else if (bet_type == 4) {
                    str = "3LH_*"
                } else if (bet_type == 5) {
                    _.each(number, function (v, i, n) {
                        n[i] = v.replace("*", "")
                    });
                    str = "AA_" + number.join("");
                } else if (bet_type == 6) {
                    _.each(number, function (v, i, n) {
                        v = v.replace("|", "");
                        n[i] = v.slice(0, 2) + "|" + v.slice(2, 3)
                    });
                    str = "AAX_" + number.join("");
                } else if (bet_type == 7) {
                    str = (red_dm && red_dm.length > 0) ? "2BT_" + red_dm.join("") + "$" + number.join("") : "2BT_" + number.join("")
                }
                arr.push(str);
            }
        } else if (gameId == "f14") {
            for (i = 0; i < len; i++) {
                arr1 = [];
                _.each(models[i].get("number"), function (obj) {
                    arr1.push(obj.n.join(""))
                });
                arr.push(arr1.join(" "))
            }
        } else if (gameId == "f9") {
            for (i = 0; i < len; i++) {
                arr1 = [];
                _.each(models[i].get("number"), function (obj) {
                    arr1.push(obj.n.join("") + (obj.checked ? "$" : ""))
                });
                arr.push(arr1.join(" "))
            }
        } else if (gameId == "bqc" || gameId == "zjq") {
            var passtype = [];
            var matches = [];
            var me = this;
            models = this.models;
            mul = +this.$mul.val();
            price = +this.$countMoney.html().replace(/[\u4e00-\u9faf]+/g, "") * 100;
            _.each(models.count()[1], function (matchid, k) {
                var sparr = [];
                var isDan = $(me.dmArr[matchid.toString()]).prop("checked") ? 1 : 0;
                _.each(models.where({matchid: matchid}), function (model, i) {
                    sparr[i] = model.get("code");
                });
                matches[k] = matchid + ":" + sparr.join(".") + ":" + isDan
            });
            this.$("input[passtype]:checked").each(function (i, v) {
                passtype[i] = $(v).attr("passtype")
            });
            arr = [matches.join(" ") + "|" + passtype.join(",").replace(/串/g, "_")];
        } else if (gameId == "hhgg_spf_rqspf_bf") {
            var obj = this.models.toJSON();
            var matchObj = {};
            var dmHash = this.dmHash;
            _.each(obj, function (o) {
                matchObj[o.matchid] || (matchObj[o.matchid] = []);
                matchObj[o.matchid].push(o.code)
            });

            _.each(matchObj, function (val, key) {
                arr.push(key + ":" + val.join(".") + ":" + +dmHash[key].checked)
            });

            arr = [arr.join(" ") + "|" + this.getChuan().join(",").replace(/串/g, "_").replace("单关", "1_1")];

            mul = +this.$mul.val();

            price = parseFloat($.trim(this.$countMoney.html())) * 100;

        }

        data = {
            sourceType: 1,// 投注号码来源 意义待定，如 1头图 2导航 3定向推广 等
            gameId: $("#gameId").val(), // 彩种id
            periodNo: $("#periodNo").val() || "", // 投注期次
            rawBetNumbers: arr.join(";"), // 投注号码
            betTimes: mul, // 此号码的投注倍数
            price: price || 0 // 价格 单位:分
        };

        $.post("/ajax/login/order/bet.html", data, function (data) {
            var retcode = data.retcode;
            var retdesc = data.retdesc;
            var result = data.result;
            switch (retcode) {
                case 0:
                    confirm.show(function () {
                        this.data = result
                    });
                    try {
                        spb_vars && spb_vars.pingback(0, "pbtag=点击提交注册", "extra");
                    } catch (e) {
                    }
                    break;
                case 4000001:
                    Alert.show(function () {
                        this.$(".txt-center").html("支付金额超过上限<b class=\"dia-red\">990000</b>元，请返回重新选择");
                    });
                    break;
                case 4000003:
                    fill.show();
                    break;
                case 4000006:
                    fundsLack.show(function () {
                        var nickName = result.nickName || result.userId;
                        nickName = nickName.length > 16 ? (nickName.slice(0, 16) + "...") : nickName;
                        var amount = result.chargeAmount || "";
                        this.$nickName.html(nickName);
                        amount && this.$sum.html(tools.FloatDiv(amount, 100));
                        this.$submit.attr("href", "/login/charge/pre.html?payOrderId=" + result.payOrderId + "&amount=" + amount)
                    });
                    break;
                default:
                    Alert.show(function () {
                        this.$(".txt-center").html(retdesc);
                    });
            }
        }).error(function () {
//            var data = {
//                "retcode":0,
//                "retdesc":"操作成功",
//                "result":{
//                    "gameId": "ssq", // 彩种id
//                    "gameCn":"双色球",
//                    "periodNo": "20140326", // 投注期次
//                    "pwdErrorTimes": 0, // 支付密码错误次数
//                    "payOrderId": "140326UO1234324344", // 订单号
//                    "payAmount": 1234, // 金额
//                    "balanceAmount": 12232, // 余额
//                    "systemTime": "1234567890123" // 系统时间
//                }
//            };

            // var data = {"retcode":4000001, "retdesc":"支付金额超过上限","result":{}};
            // var data = {"retcode":4000002, "retdesc":"投注号码格式错误","result":{}};
            // var data = {"retcode":4000003, "retdesc":"用户信息不存在","result":{}};
            // var data = {"retcode":4000004, "retdesc":"投注期次不存在","result":{}};
            // var data = {"retcode":4000005, "retdesc":"投注期次/场次不可用或已过期","result":{}};
            // var data = {"retcode":4000006, "retdesc":"余额不足","result":{}};
            // var data = {"retcode":4000007, "retdesc":"支付密码已经连续输入超过5次，请明天再试","result":{}};
//            var retcode = data.retcode;
//            var retdesc = data.retdesc;
//            var result = data.result
            // 测试用
//            var e = location.search.match(/e=\d+/g);
//            var h = {
//                4000001:"支付金额超过上限",
//                4000002:"投注号码格式错误",
//                4000003:"用户信息不存在",
//                4000004:"投注期次不存在",
//                4000005:"投注期次/场次不可用或已过期",
//                4000006:"余额不足",
//                4000007:"支付密码已经连续输入超过5次，请明天再试"
//            };
//            retcode = e ? +e[0].replace("e=","") : retcode;
//            retdesc = h[retcode] || retdesc;

//            switch (retcode){
//                case 0:
//                    confirm.show(function(){
//                        this.data = result
//                    });
//                    break;
//                case 4000001:
//                    Alert.show(function(){
//                        this.$(".txt-center").html("支付金额超过上限<b class=\"dia-red\">990000</b>元，请返回重新选择");
//                    });
//                    break;
//                case 4000003:
//                    fill.show();
//                    break;
//                case 4000006:
//                    fundsLack.show(function(){
//                        this.$submit.attr("href","#?payOrderId="+result.payOrderId)
//                    });
//                    break;
//                default:
//                    Alert.show(function(){
//                        this.$(".txt-center").html(retdesc);
//                    });
//            }

        })
    }
})