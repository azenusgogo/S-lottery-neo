define([
    "text!zc/ball_item.tpl",
    "lib/tools",
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
], function (ball_item, tools, art_login, dialogAlert, pay, updatePage, stop_game, Tab,userAgreementPopup,ls,jstorage) {

    if ($("#zc_sfc_ball_list").length == 0 || stop_game() == 0) return function () {
    };

    // 联赛积分榜 tab切换

    new Tab({
        $t: $("#jf_tab span"),
        $c: $(".ls-fen-tbl"),
        tClassName: "cur"
    });
    // 更新页面数据
    updatePage();
    jstorage();
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
            if (done) {
                this.$el.removeClass("sel-hover");
                this.models.remove(this.model)
            } else {
                this.$el.addClass("sel-hover");
                this.models.add(this.model)
            }
            this.model.set("done", !done, {silent: true});

            if (this.models.rown(this.model.get("row")).length == 0) {
                this.model.get("$sel").html("包").data("st", false)
            } else if (this.models.rown(this.model.get("row")).length == 3) {
                this.model.get("$sel").html("清").data("st", true)
            }
            //
            return false
        }
    });

    var AppView = Backbone.View.extend({
        el: "#zc_sfc",
        events: {
            "click a[data-row]": "toggleAll",
            "click .clear": "clear",
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
            var hashView = this.hashView = {};
            var data;
            var hash;
            var _this = this;
            this.$("a[data-model]").each(function () {
                var $this = $(this);
                data = $.parseJSON($this.attr("data-model"));
                data.done = false;
                data.$sel = _this.$("a[data-row=" + data.row + "]"); // 引用 包、清按钮
                (hashView[data.row] || (hashView[data.row] = {}))[data.number] = (new View({
                    model: new Model(data),
                    el: $this
                }));
            });
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
        submit: function () {
            var list = this.models.done();

            var number = [];
            var arr;
            var arr1;
            var count = 1;
            for (var i = 1; i < 15; i++) {
                arr = this.models.where({row: i});
                var len = arr.length;
                if (len == 0) {
                    Alert.show(function () {
                        this.$(".txt-center").html("请每场至少选取一项进行投注");
                    });
                    return false
                }
                ;
                arr1 = [];
                for (var j = 0; j < len; j++) {
                    arr1.push(arr[j].get("number"));
                }
                count *= len;
                number.push({
                    n: _.sortBy(arr1),
                    checked: false
                })
            }
            this.lists.add(
                {
                    number: number,
                    done: true,
                    count: count
                }
            );
            this.clear()
            return false
        }
    })

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
        el: "#zc_sfc_ball_list",
        events: {
            "click .rndBtn": "rnd",
            "click .clear": "clear",
            "keyup .mul": "checkInput",
            "keyup .rnd_n": "checkInput",
            "blur .mul": "blurInput",
            "blur .rnd_n": "blurInput",
            "click #ballSubmit": "submit",
            "click .read_protocal":"popupProtocal"
        },
        popupProtocal:function(){
        	userAgreementPopup("agentProtocal","用户委托投注协议");
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
            for (var i = 0; i < n; i++) {
                number = [];
                for (var j = 0; j < 14; j++) {
                    number.push({
                        n: [_.sample(this.original, 1)],
                        checked: false
                    })
                }
                this.models.add(
                    {
                        number: number,
                        done: true,
                        count: 1 // 随机都是1
                    }
                );
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
            if (v == 0) {
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
                pay.call(_this, "f14")
            }, function () {
                // 未登录
            	var  dataObj = ls.call(_this,"f14");
              	 if(!!dataObj.data && tools.getBytes(dataObj.data)<=30*1024){
              		dataObj.timestamp = +new Date;             		              		
              		var data = JSON.stringify(dataObj); 
              		$.jStorage.set("betsData",data); 
              		//$.jStorage.setTTL("betsData",60000);
              	 }
                art_login.show(function () {
                    this.successCallfn = function () {
                        this.hide();
                        pay.call(_this, "f14")
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
    var fillData = function(data) {
        var list = data.split(";");
        for (var i = 0,
        len = list.length; i < len; i++) {

            var arr = list[i].split("#");
            var mul = arr[1];
            var arr1 = arr[0].split(" ");
            var number = [];
            var count = 1;
            for (var j = 0; j < 14; j++) {
                number.push({
                    n: _.sortBy(arr1[j].split("")),
                    checked: false
                });
                count *= arr1[j].split("").length
            }
            lists.add({
                number: number,
                done: true,
                count: count // 随机都是1
            });

        }
        $("#zc_sfc_ball_list .mul").val(mul); // 倍数都一样
        ballsView.render();
        $.jStorage.flush();
    };

    isLogin(function(userId) {
        if (userId.indexOf("qq.sohu.com") > -1 && $.jStorage.index().length > 0) {
            var dataObj = $.jStorage.get("betsData");
            dataObj = JSON.parse(dataObj);
            if (Math.abs( + new Date - ( + dataObj.timestamp)) > 60 * 1000) { //  $.jStorage.getTTL("betsData")<0
                $.jStorage.flush();
            } else if (dataObj && dataObj.gameId === "f14") {
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