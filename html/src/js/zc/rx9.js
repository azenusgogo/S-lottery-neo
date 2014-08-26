define([
    "text!zc/ball_item.tpl",
    "lib/tools",
    "lib/timer",
    "login/art_login",
    "lib/alert",
    "pay/pay",
    "update/tradfootball",
    "update/stop_game",
    "lib/tab",
    "lib/userAgreementPopup",
    "lib/ls",
    "lib/jstorage",
    "lib/json2"
], function (ball_item, tools, Timer, art_login, dialogAlert, pay, updatePage, stop_game, Tab, userAgreementPopup,ls,jstorage) {

    if ($("#zc_rx9_ball_list").length == 0 || stop_game() == 0) return function () {
    };

    // 更新页面数据
    updatePage();
    jstorage();
    new Tab({
        $t: $("#jf_tab span"),
        $c: $(".ls-fen-tbl"),
        tClassName: "cur"
    });

    var isLogin = tools.isLogin;

    var Alert = dialogAlert("提示");

    var Model = Backbone.Model.extend();

    var Models = Backbone.Collection.extend({
        rown: function (row) {
            return this.where({
                row: row,
                done: true
            })
        },
        done: function () {
            return this.where({
                done: true
            })
        },
        stats: function () {
            var n = 0;
            var hash = {};
            var row;
            this.map(function (model) {
                row = model.get("row");
                if (!hash[row]) {
                    n += 1;
                    hash[row] = row
                }
            });
            return {
                rown: n, //  选了几场
                rows: hash
            }

        },
        getCount: function () {
            var models = this;
            var a3 = 0;
            var b2 = 0;
            var c1 = 0;

            for (var i = 1; i < 15; i++) {
                var arr1 = models.where({row: i, checked: false});
                if (arr1.length == 1) {
                    c1 += 1
                } else if (arr1.length == 2) {
                    b2 += 1
                } else if (arr1.length == 3) {
                    a3 += 1
                }
            }


            var dm_n = 1;
            var dm = 0;
            _.each(models.where({ checked: true}), function (model) {
                dm += 1;
                dm_n *= models.where({row: +model.get("row")}).length
            });


//            _.each(appView.checkedHash, function (el, key) {
//                if ($(el).prop("checked")) {
//                    dm += 1;
//                    dm_n *= models.where({row: +key}).length
//                }
//            })


            return tools.rx9_c(a3, b2, c1, 9 - dm) * dm_n
        }
    });

    var View = Backbone.View.extend({
        events: {
            "click": "render"
        },
        initialize: function () {
            this.models = models; // 保存集合
            this.listenTo(this.model, "change", this.render);
            this.listenTo(this.model, "destroy", this.render);
        },
        render: function () {
            var done = this.model.get("done"); // 选中标识
            var $sel = this.model.get("$sel");
            var $checkinput = this.model.get("$checkinput");
            var checked = false;
            if (done) {
                this.$el.removeClass("sel-hover");
                this.models.remove(this.model)
            } else {
                this.$el.addClass("sel-hover");
                this.models.add(this.model);
                $checkinput.is(":checked") && (checked = true)
            }

            this.model.set({
                "done": !done,
                "checked": checked
            }, {silent: true});


            var rown = this.models.rown(this.model.get("row")).length;
            if (rown > 0) {
                $checkinput.prop("disabled", false);
                if (rown == 3) {
                    $sel.html("清").data("st", true)
                }
                ;
            } else {
                $sel.html("包").data("st", false);
                $checkinput.prop({
                    "checked": false,
                    "disabled": true
                });
                $checkinput.prop("disabled", true)
            }
            return false
        }
    });

    var AppView = Backbone.View.extend({
        el: "#zc_rx9",
        events: {
            "click a[data-row]": "toggleAll",
            "click input[data-row]": "selChecked",
            "click .clear": "clear",
            "click .rnd": "rnd",
            "click .selSubmitBtn": "submit"
        },
        initialize: function () {
            // tr hover
            this.$el.on({
                "mouseenter": function () {
                    $(this).addClass('hover')
                },
                "mouseleave": function () {
                    $(this).removeClass('hover')
                }
            }, ".cp-area tr");
            this.lists = lists;
            this.models = models;
            this.$rnd = this.$(".rnd");
            this.$rnd.hover(
                function () {
                    $(this).next().show();
                },
                function () {
                    $(this).next().hide();
                }
            );
            var hashView = this.hashView = {};
            var data;
            var hash;
            var checkedHash = this.checkedHash = {};
            this.$("input[data-row]").each(function () {
                var $this = $(this);
                var row = $this.attr("data-row");
                checkedHash[row] = $this;
            });
            var _this = this;
            this.$("a[data-model]").each(function () {
                var $this = $(this);
                data = $.parseJSON($this.attr("data-model"));
                data.done = false;
                data.checked = false;
                data.$sel = _this.$("a[data-row=" + data.row + "]"); // 引用 包、清按钮
                data.$checkinput = _this.$("input[data-row=" + data.row + "]");  // 引用 胆码input
                (hashView[data.row] || (hashView[data.row] = {}))[data.number] = (new View({
                    model: new Model(data),
                    el: $this
                }));
            });
            this.listenTo(this.models, "add", this.render);
            this.listenTo(this.models, "remove", this.render);
        },
        toggleAll: function (e) {
            var $this = $(e.currentTarget);
            var row = $this.attr("data-row");
            var st = !!$this.data("st"); // false true
            _.each(this.hashView[row], function (view) {
                view.model.set({
                    "done": st,
                    "d": +new Date // 触发cheng事件
                })
            });
            $this.data("st", !st);
            return false
        },
        clear: function () {
            var arr = this.models.done();
            _.invoke(arr, "destroy");
            return false
        },
        selChecked: function (e) {
            var i = 0;
            var checkedHash = this.checkedHash;
            _.each(checkedHash, function ($checkinput) {
                $checkinput.prop("checked") && (i += 1);
            });
            if (i > 8) {
                Alert.show(function () {
                    this.$(".txt-center").html("最多只能选8个胆");
                });
                return false
            }

            var $this = $(e.currentTarget);
            var arr = this.models.rown(+$this.attr("data-row"));
            var checked = $this.prop("checked") ? true : false;
            _.each(arr, function (model) {
                model.set("checked", checked, {silent: true})
            });

            this.render();
            // if (i==8){
            //     _.each(checkedHash,function($checkinput){
            //         if (!$checkinput.prop("checked")){
            //             $checkinput.prop("disabled",true)
            //         }
            //     });
            // }else {
            //     _.each(checkedHash,function($checkinput){
            //         if (!$checkinput.prop("checked")){
            //             $checkinput.prop("disabled",false)
            //         }
            //     });
            // }
        },
        rnd: function (e) {
            var $this = $(e.currentTarget);
            if ($this.hasClass("disabled")) return false;
            var stats = this.models.stats();
            var rown = stats.rown;
            if (rown > 0 && rown < 10) {
                var hash = stats.rows;
                var arr = [];
                _.each(this.hashView, function (view, key) {
                    if (!hash[key]) arr.push(view)
                });
                arr = _.sample(arr, 10 - rown);
                var original = [3, 1, 0];
                _.each(arr, function (view) {
                    view[_.sample(original)].render()
                })
            }
            return false
        },
        render: function () {
            var $rnd = this.$rnd;
            var rown = this.models.stats().rown;

            var checkdm = 0;
            var checkedHash = this.checkedHash;
            _.each(checkedHash, function ($checkinput) {
                $checkinput.prop("checked") && (checkdm += 1);
            });

            if (rown > 0 && rown < 10 && checkdm > 0) {
                $rnd.removeClass("disabled")
            } else {
                $rnd.addClass("disabled")
            }

            // var checkedHash = this.checkedHash;
            // var len = this.models.stats().rown;


            // if (len>9){ // 选了9场以上
            //     this.models.each(function(model){
            //         var $checkinput = checkedHash[model.get("row")];
            //         $checkinput.prop("disabled",false)
            //     })
            // }else{
            //     _.each(checkedHash,function($checkinput){
            //         $checkinput.prop({
            //             "disabled":true,
            //             "checked":false
            //         });
            //     });
            //     // this.models.each(function(model){
            //     //     model.set("checkbox",false)
            //     // })
            // }
        },
        submit: function () {
            var models = this.models;
            var number = [];
            var rown = models.stats().rown;
            var n;
            if (rown < 9) {
                Alert.show(function () {
                    this.$(".txt-center").html("您选择的号码是非标准投注号码，可尝试定胆随机");
                });
                return false
            }


            var checkdm = 0;
            var checkedHash = this.checkedHash;
            _.each(checkedHash, function ($checkinput) {
                $checkinput.prop("checked") && (checkdm += 1);
            });


            if (rown == 9 && checkdm > 0) {
                Alert.show(function () {
                    this.$(".txt-center").html("设胆玩法必须选择10场或10场以上的比赛");
                });
                return false
            }

            for (var i = 1; i < 15; i++) {
                var checked = false;
                var arr2 = models.where({row: i});
                n = _.map(arr2, function (model) {
                    checked = model.get("checked");
                    return +model.get("number")
                });
                number.push({
                    n: n.length == 0 ? ["-"] : _.sortBy(n),
                    checked: checked
                })
            }


            // 计算注数

            // var dm = this.models.where({checked:true});
            // var tm = this.models.where({checked:false});


            this.lists.add(
                {
                    number: number,
                    done: true,
                    count: models.getCount()
                }
            );
            this.clear()
            return false
        }
    });

    // --------------------------------------------------------------------------------

    var Lists = Backbone.Collection.extend({
        model: Model,
        done: function () {
            return this.where({done: true})
        },
        count: function () {
            return _.reduce(this.models, function (memo, model) {
                return memo + model.get("count");
            }, 0);
        }
    });

    var Item = Backbone.View.extend({
        tagName: "li",
        template: _.template(ball_item),
        events: {
            "click .del": "destroy"
        },
        initialize: function () {
            this.listenTo(this.model, 'destroy', this.remove);
            this.listenTo(this.model, 'change', this.render);
        },
        render: function () {
            var data = this.model.toJSON();
            this.$el.html(this.template(data));
            this.$el.removeClass("libgred");
            ballsView.render(); // 更新统计
            return this
        },
        destroy: function () {
            this.model.destroy();
            ballsView.render(); // 更新统计
            return false
        }
    });

    var BallsView = Backbone.View.extend({
        el: "#zc_rx9_ball_list",
        events: {
            "click .rndBtn": "rnd",
            "click .clear": "clear",
            "keyup .mul": "checkInput",
            "keyup .rnd_n": "checkInput",
            "blur .mul": "blurInput",
            "blur .rnd_n": "blurInput",
            "click #ballSubmit": "submit",
            "click .read_protocal": "popupProtocal"
        },
        popupProtocal: function () {
            userAgreementPopup("agentProtocal", "用户委托投注协议");
            return false;
        },
        initialize: function () {
            this.$rnd_n = this.$(".rnd_n");
            this.$parent = this.$("#item_parent");
            this.$read = this.$(".ball-submit input[type='checkbox']");
            // 集合
            this.models = lists;
            // 随机original
            this.original = [0, 1, 3];
            this.rnd_original = _.range(0, 14);

            // 更新统计
            this.$mul = this.$(".mul");
            this.$count = this.$(".count");
            this.$money = this.$(".money");

            this.listenTo(this.models, "add", this.addOne)
            // tips
            this.$rndTips = this.$(".ball-item .ball-list-tips");
            var $payMethodTips = this.$(".ball-pay-method .ball-list-tips");
            this.$(".ball-pay-method .tips i").hover(function () {
                $payMethodTips.show()
            }, function () {
                $payMethodTips.hide()
            });
            this.$rndTips.css({position:"absolute",display:"block"});
        },
        rnd: function (e) {
            var n = $(e.currentTarget).attr("data-n") || this.$rnd_n.val();
            var number;

            var r = _.sample(this.rnd_original, 9);

            r = _.object(r, r);
            for (var i = 0; i < n; i++) {
                number = [];
                for (var j = 0; j < 14; j++) {
                    if (typeof r[j] != "undefined") {
                        number.push({
                            n: [_.sample(this.original, 1)],
                            checked: false
                        })
                    } else {
                        number.push({
                            n: ["-"],
                            checked: false
                        })
                    }
                }

                this.models.add(
                    {
                        number: number,
                        done: true,
                        count: 1 // 随机都是1
                    }
                )
            }
            this.$rndTips.hide();
            return false

        },
        addOne: function (model, models, options) {
            var view = new Item({model: model });
            view.upview = options.upview;
            this.$parent.prepend(view.render().el);
        },
        clear: function () {
            _.invoke(this.models.done(), "destroy");
            this.render()
            return false
        },
        render: function () {
            var count = lists.count();
            this.$money.html(count * this.$mul.val() * 2);
            this.$count.html(count)
        },
        blurInput: function (e) {
            var $this = $(e.currentTarget);
            var v = $.trim($this.val());
            (v == 0) && $this.val(1);
            this.render()
        },
        checkInput: function (e) {
            var $this = $(e.currentTarget);
            var v = $.trim($this.val());
            if (v.length == 0) {
                $this.val("")
            } else if (v > 99) {
                $this.val(99)
            } else if (v.length == 1) {
                $this.val(v.replace(/[^1-9]/g, ''))
            } else {
                $this.val(v.replace(/\D/g, ''))
            }
            this.render()
        },
        submit: function () {
            var _this = this;
            if (this.models.models.length == 0) {
                Alert.show(function () {
                    this.$(".txt-center").html("至少选择1注号码才能投注");
                });
                return false
            }

            if (!this.$read.prop("checked")) {
                Alert.show(function () {
                    this.$(".txt-center").html("请先阅读并同意\u300a用户委托投注协议\u300b");
                });
                return false
            }
           
            isLogin(function () {
                // 已登陆
                pay.call(_this, "f9")
            }, function () {
                // 未登录
             var  dataObj = ls.call(_this,"f9");
           	 if(!!dataObj.data && tools.getBytes(dataObj.data)<=30*1024){
           		dataObj.timestamp = +new Date;             		              		
           		var data = JSON.stringify(dataObj); 
           		$.jStorage.set("betsData",data); 
           		//$.jStorage.setTTL("betsData",60000);
           	 }
                art_login.show(function () {
                    this.successCallfn = function () {
                        this.hide();
                        pay.call(_this, "f9")
                    }
                })
            });
            return false
        }
    });

    // list
    var lists = new Lists;
    var ballsView = new BallsView;

    var models = new Models;
    var appView = new AppView;

    (function () {

    	var outerData = $("#defaultBets").val();
        var fillData = function(data){
        	var list = data.split(";");
            for (var i = 0, len = list.length; i < len; i++) {

                var arr = list[i].split("#");
                var mul = arr[1];
                var arr1 = arr[0].split(" ");
                var number = [];
                var a3 = 0, b2 = 0, c1 = 0, dm = 0, dm_n = 1;
                for (var j = 0; j < 14; j++) {
                    var checked = arr1[j].indexOf("$") > -1 ? true : false;
                    var n = arr1[j].replace("$", "").split("");
                    if (arr1[j] != "-") {
                        if (n.length == 1) {
                            c1 += 1
                        } else if (n.length == 2) {
                            b2 += 1
                        } else if (n.length == 3) {
                            a3 += 1
                        }
                        if (checked) {
                            dm += 1;
                            dm_n *= n.length
                        }
                    }
                    number.push({
                        n: _.sortBy(n),
                        checked: checked
                    })
                }
                lists.add(
                    {
                        number: number,
                        done: true,
                        count: tools.rx9_c(a3, b2, c1, 9 - dm) * dm_n // 随机都是1
                    }
                );


            }
            $("#zc_rx9_ball_list .mul").val(mul); // 倍数都一样
            ballsView.render();
            $.jStorage.flush();
            
        }

       
        isLogin(function(userId) {
            if (userId.indexOf("qq.sohu.com") > -1 && $.jStorage.index().length > 0) {
                var dataObj = $.jStorage.get("betsData");
                dataObj = JSON.parse(dataObj);
                if (Math.abs( + new Date - ( + dataObj.timestamp)) > 60 * 1000) { //  $.jStorage.getTTL("betsData")<0
                    $.jStorage.flush();
                } else if (dataObj && dataObj.gameId === "f9") {
                    fillData(dataObj.data);
                }
            } else if (outerData) {
                fillData(outerData);
                tools.scrollToDom(".cp-infos")
            }
        },
        function() { //login not yet
            $.jStorage.flush();
            if (outerData) {
                fillData(outerData);
                tools.scrollToDom(".cp-infos")
            }
        });


        
    })();

    return function () {

    }

})