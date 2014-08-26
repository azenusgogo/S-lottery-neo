/**
 * Created by caojungang on 14-2-19.
 */
define([
    "text!qxc/ball_item.tpl",
    "text!qxc/sel_stats.tpl",
    "lib/tools",
    "login/art_login",
    "lib/alert",
    "pay/pay",
    "update/common",
    "update/stop_game",
    "lib/userAgreementPopup",
    "lib/game_yl_tips",
    "lib/ls",    
    "lib/jstorage",
    "lib/json2"
], function (ball_item, sel_stats_tpl, tools, art_login, dialogAlert, pay, updatePage, stop_game, userAgreementPopup, game_yl_tips,ls,jstorage) {

    if ($("#qxc_ball_list").length == 0 || stop_game() == 0) return function () {
    };

    // 更新页面数据
    updatePage();
    // 遗漏 最大遗漏 tip
    game_yl_tips();
    jstorage();
    var isLogin = tools.isLogin;

    var Alert = dialogAlert("提示");

    var BallModel = Backbone.Model.extend();

    // 普通玩法
    var Balls = Backbone.Collection.extend({
        model: BallModel,
        count: function () {
            // 统计
            var len, n = 1, count = true;
            for (var i = 1; i < 8; i++) {
                len = this.done(i).length;
                if (len == 0) {
                    count = false
                }
                n *= this.done(i).length
            }
            return {
                count: count ? n : 0
            }
        },
        done: function (row) {
            var data = {};
            row && (data.row = row) || (data.done = true);
            return this.where(data)
        },
        toArr: function (row) {
            var arr = _.filter(this.models, function (model) {
                return model.get("row") == row
            });


            return _.map(arr, function (model) {
                return +model.get("number")
            })
        }
    });

    var BallView = Backbone.View.extend({
        events: {
            "click": "render"
        },
        initialize: function () {
            this.models = balls; // 保存集合
            this.listenTo(this.model, "change", this.render);
            this.listenTo(this.model, "destroy", this.render);
        },
        render: function () {
            var done = this.model.get("done"); // 选中标识
            if (done) {
                this.$el.removeClass("active");
                this.models.remove(this.model)
            } else {
                this.$el.addClass("active");
                this.models.add(this.model)
            }
            this.model.set("done", !done, {silent: true});

        }
    });

    var PtPlayView = Backbone.View.extend({
        el: "#qxc_py_play",
        events: {
            "click .sel-ball .clear": "clear",
            "click .qxc-item .r span": "query",
            "click .qxc-item .clear": "clearRow",
            "click .selSubmitBtn": "submit"
        },
        template: _.template(sel_stats_tpl),
        initialize: function () {
            this.lists = lists;
            this.models = balls;
            this.$stats = this.$(".sel-info");
            this.$submitBtn = this.$(".selSubmitBtn");
            var $balls = this.$balls = this.$(".qxc-item em");
            this.queryhash = {
                "all": {"0": 1, "1": 1, "2": 1, "3": 1, "4": 1, "5": 1, "6": 1, "7": 1, "8": 1, "9": 1},
                "max": {"0": 0, "1": 0, "2": 0, "3": 0, "4": 0, "5": 1, "6": 1, "7": 1, "8": 1, "9": 1},
                "min": {"0": 1, "1": 1, "2": 1, "3": 1, "4": 1, "5": 0, "6": 0, "7": 0, "8": 0, "9": 0},
                "odd": {"0": 0, "1": 1, "2": 0, "3": 1, "4": 0, "5": 1, "6": 0, "7": 1, "8": 0, "9": 1},
                "even": {"0": 1, "1": 0, "2": 1, "3": 0, "4": 1, "5": 0, "6": 1, "7": 0, "8": 1, "9": 0}
            };
            var hash = this.ballHash = {
                "1": {},
                "2": {},
                "3": {},
                "4": {},
                "5": {},
                "6": {},
                "7": {}
            };

            $balls.each(function (i, el) {
                var data = $.parseJSON($(el).attr("data-model"));
                data.done = false;
                hash[data.row][data.number] = new BallView({
                    model: new BallModel(data),
                    el: el
                })
            });

            this.listenTo(this.models, "add", this.render);
            this.listenTo(this.models, "remove", this.render);
        },
        clear: function () {
            var arr = this.models.done();
            _.invoke(arr, "destroy");
            return false
        },
        query: function (e) {
            var data = $.parseJSON($(e.currentTarget).attr("data-model"));
            var arr = this.ballHash[data["row"]];
            var number = this.queryhash[data["type"]];
            _.each(arr, function (view) {
                var model = view.model;
                var n = model.get("number");
                if (number[n]) {
                    model.set("done", false, {silent: true})
                    view.render()
                } else {
                    model.set("done", true, {silent: true})
                    view.render()
                }
            })
            return false
        },
        clearRow: function (e) {
            var row = +$(e.currentTarget).attr("data-row");
            var arr = this.models.done(row);
            _.invoke(arr, "destroy");
            return false
        },
        render: function () {
            // 更新view
            this.$stats.html(this.template(this.models.count()))
        },
        toggle: function () {
            this.$submitBtn.html(this.upModel ? "确认修改" : "确认选号")
        },
        submit: function () {
            var text = ["一", "二", "三", "四", "五", "六", "七"];
            var row = [], _row;
            for (var i = 0; i < 7; i++) {
                _row = this.models.toArr(i + 1);
                if (_row.length == 0) {
                    Alert.show(function () {
                        this.$(".txt-center").html("第" + text[i] + "位至少选择一个号码");
                    });
                    return false
                } else {
                    row.push(_row)
                }
            }
            var data = {done: true}, count = 1;
            for (var j = 0, len = row.length; j < len; j++) {
                data["row" + (j + 1)] = _.sortBy(row[j]);
                count *= row[j].length;
            }

            data.count = count;
            if (this.upModel) {
                // 修改
                data.t = +new Date;
                this.upModel.set(data)
                this.upModel = null
            } else {
                // 添加
                this.lists.add(data, {upview: ptPlayView})
            }
            this.toggle();
            this.clear();
            return false
        }
    });

    /*=========================================================================================================*/

    var Lists = Backbone.Collection.extend({
        model: BallModel,
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
            "click .edit": "update",
            "click .del": "destroy"
        },
        initialize: function () {
            this.listenTo(this.model, 'destroy', this.remove);
            this.listenTo(this.model, 'change', this.render);
        },
        update: function () {
            // 更新ui
            this.$el.addClass("libgred");
            this.$el.siblings().removeClass("libgred");

            // 修改
            var upview = this.upview;
            upview.upModel = this.model;
            upview.toggle();

            // 渲染
            // upview.clear("all");
            // _.each(this.model.get("red"), function (n) {
            //     upview.redHash[+n].render()
            // });
            // _.each(this.model.get("blue"), function (n) {
            //     upview.blueHash[+n].render()
            // });
            // _.each(this.model.get("red_dm"), function (n) {
            //     upview.dmHash[+n].render()
            // });

            upview.clear();
            var model = this.model, row, arr;
            for (var i = 1; i < 8; i++) {
                row = upview.ballHash[i];
                arr = model.get("row" + i);
                for (var j = arr.length; j--;) {
                    row[arr[j]].render()
                }

            }


            return false
        },
        render: function () {
            var data = this.model.toJSON();
            // 1 => 01
            data = $.extend(true, {}, data);
            this.$el.html(this.template(data));
            this.$el.removeClass("libgred");
            ballsView.render(); // 更新统计
            return this
        },
        destroy: function () {
            this.model.destroy();
            var upview = this.upview;

            if (upview.upModel == this.model) { // 是否删除当前修改
                upview.upModel = null;
                upview.toggle();
                upview.clear("all");
            }

            ballsView.render(); // 更新统计
            return false
        }
    });

    var BallsView = Backbone.View.extend({
        el: "#qxc_ball_list",
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
            this.original = _.range(0, 10);

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
            for (var i = 0; i < n; i++) {
                this.models.add(
                    {
                        "row1": _.sample(this.original, 1),
                        "row2": _.sample(this.original, 1),
                        "row3": _.sample(this.original, 1),
                        "row4": _.sample(this.original, 1),
                        "row5": _.sample(this.original, 1),
                        "row6": _.sample(this.original, 1),
                        "row7": _.sample(this.original, 1),
                        done: true,
                        count: 1 // 随机都是1
                    },
                    {
                        upview: ptPlayView
                    }
                );
            }
            this.$rndTips.hide();
            var _scrollTarget = $(".cp-sub-nav");
            if(!_scrollTarget.data("onceScroll")){
            	tools.scrollToDom(".cp-infos");
            	_scrollTarget.data("onceScroll",1);
            }
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
                    this.$(".txt-center").html("请先选取投注号码");
                })
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
                pay.call(_this, "qxc")
            }, function () {
                // 未登录
            	var  dataObj = ls.call(_this,"qxc");
             	 if(!!dataObj.data && tools.getBytes(dataObj.data)<=30*1024){
             		dataObj.timestamp = +new Date;             		              		
             		var data = JSON.stringify(dataObj); 
             		$.jStorage.set("betsData",data); 
             		//$.jStorage.setTTL("betsData",60000);
             	 }
                art_login.show(function () {
                    this.successCallfn = function () {
                        this.hide();
                        pay.call(_this, "qxc")
                    }
                })
            });
            return false
        }
    });

    /*==========================================================================================================*/

    // list
    var lists = new Lists;
    var ballsView = new BallsView;

    // 普通玩法
    var balls = new Balls;
    var ptPlayView = new PtPlayView;

    // 投注

    (function () {

    	var outerData = $("#defaultBets").val();    	
    	var fillData = function(data){
            var list = data.split(";");
            for (var i = 0, len = list.length; i < len; i++) {
                var arr = list[i].split("#");
                var rowArr = arr[0].split(" ");
                var count = 1;
                for (var j = rowArr.length; j--;) {
                    count *= rowArr[j].length
                }
                var mul = arr[1];
                lists.add(
                    {
                        "row1": _.sortBy(rowArr[0].split("")),
                        "row2": _.sortBy(rowArr[1].split("")),
                        "row3": _.sortBy(rowArr[2].split("")),
                        "row4": _.sortBy(rowArr[3].split("")),
                        "row5": _.sortBy(rowArr[4].split("")),
                        "row6": _.sortBy(rowArr[5].split("")),
                        "row7": _.sortBy(rowArr[6].split("")),
                        done: true,
                        count: count  // 随机都是1
                    },
                    {
                        upview: ptPlayView
                    }
                );
            }
            $("#qxc_ball_list .mul").val(mul); // 倍数都一样
            ballsView.render();
            $.jStorage.flush();      
    	};

        isLogin(function(userId) {
            if (userId.indexOf("qq.sohu.com") > -1 && $.jStorage.index().length > 0) {
                var dataObj = $.jStorage.get("betsData");
                dataObj = JSON.parse(dataObj);
                if (Math.abs( + new Date - ( + dataObj.timestamp)) > 60 * 1000) { //  $.jStorage.getTTL("betsData")<0
                    $.jStorage.flush();
                } else if (dataObj && dataObj.gameId === "qxc") {
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