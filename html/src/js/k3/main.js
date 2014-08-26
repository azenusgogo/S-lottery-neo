/**
 * Created by caojungang on 14-2-19.
 */
define([
    "text!k3/sel_stats.tpl",
    "text!k3/ball_item.tpl",
    "lib/tools",
    "login/art_login",
    "lib/alert",
    "pay/pay",
    "update/highfrequency",
    "lib/tab",
    "update/stop_game",
    "lib/userAgreementPopup",
    "lib/game_yl_tips",
    "lib/ls",
    "lib/jstorage",
    "lib/json2"
], function (sel_stats_tpl, ball_item, tools, art_login, dialogAlert, pay, updatePage, Tab, stop_game, userAgreementPopup, game_yl_tips,ls,jstorage) {

    if ($("#k3_ball_list").length == 0 || stop_game() == 0) return function () {
    };

    // 更新页面数据
    updatePage();
    // 遗漏 最大遗漏 tip
    game_yl_tips();
    jstorage();

    var isLogin = tools.isLogin;

    var Alert = dialogAlert("提示");

    var toNav = new tools.tabToggle({
        $t: $("#k3-toggle li"),
        $c: $(".toggle_c"),
        fn: function (i) {
            ballsView.bet_type = i;
        }
    });

    // 侧边栏tab切换
    new Tab({
        $t: $("#k3_side_tab_t1 span"),
        $c: $("#k3_side_tab_c1 ul"),
        tClassName: "cur"
    });
    new Tab({
        $t: $("#k3_side_tab_t2 span"),
        $c: $("#k3_side_tab_c2 ul"),
        tClassName: "cur",
        onload: function () {
            var _this = this;
            $("#k3_side_tab_show_all").on("click", function () {
                var st = $(this).data("st");
                _this.$c.css("height", st ? 155 : "auto");
                $(this).data("st", st ? 0 : 1).html(st ? "显示全部号码" : "显示部分号码");
                return false
            })
        }
    });
    // 切换开奖列表
    var $award_list = $("#award_list");
    $("#toggle_award_list").on("click", function () {
        var st = $(this).data("st");
        $award_list.css("display", st ? "block" : "none")
        $(this).data("st", st ? 0 : 1).html(st ? "收起" : "展开");
    });

    var toSubNav = new tools.tabToggle({
        $t: $("#k3_3bt_toggle label"),
        $c: $("#k3_3bt_play .bet-toggle"),
        preventDefault: false,
        className: ".bet-toggle",
        fn: function (i) {
            this.find("input").prop("checked", true)
        }
    });

    var toSubNav2Bt = new tools.tabToggle({
        $t: $("#k3_2bt_toggle label"),
        $c: $("#k3_2bt_play .bet-toggle"),
        preventDefault: false,
        className: ".bet-toggle",
        fn: function () {
            this.find("input").prop("checked", true)
        }
    });

    var BallModel = Backbone.Model.extend();

    // 和值
    var HzBalls = Backbone.Collection.extend({
        model: BallModel,
        initialize: function () {
            this.bonus = {
                "3": 240,
                "4": 80,
                "5": 40,
                "6": 25,
                "7": 16,
                "8": 12,
                "9": 10,
                "10": 9,
                "11": 9,
                "12": 10,
                "13": 12,
                "14": 16,
                "15": 25,
                "16": 40,
                "17": 80,
                "18": 240
            }
        },
        count: function () {
            // 统计
            var bonus = this.bonus;
            var arr = _.map(this.toArr("red"), function (v) {
                return bonus[v]
            });
            return {
                count: arr.length,
                max: _.max(arr),
                min: _.min(arr)
            }
        },
        ball: function (color) {
            return this.where({color: color})
        },
        done: function () {
            return this.where({done: true})
        },
        toArr: function (color) {
            var arr = color == "red" ? this.ball(color) : this.done();
            return _.map(arr, function (model) {
                return +model.get("number")
            })
        }
    });

    var HzBallView = Backbone.View.extend({
        events: {
            "click": "render"
        },
        initialize: function () {
            this.models = hzBalls; // 保存集合
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

    var HzPlayView = Backbone.View.extend({
        el: "#k3_hz_play",
        upModel: null,
        template: _.template(sel_stats_tpl),
        events: {
            "click .clear": "clear",
            "click .selSubmitBtn": "submit"
        },
        initialize: function () {
            //var _this = this;

            this.models = hzBalls;
            this.lists = lists;
            this.$balls = this.$(".ball em");
            this.$stats = this.$(".sel-info");
            this.$submitBtn = this.$(".selSubmitBtn");

            // 保存每个球的view
            var hash = this.redHash = {}; // 保存每个球的view
            this.$balls.each(function (i, el) {
                var data = $.parseJSON($(el).attr("data-model"));
                data.done = false;
                hash[data.number] = new HzBallView({
                    model: new BallModel(data),
                    el: el
                })
            });
            // 监听集合变化
            this.listenTo(this.models, "add", this.render);
            this.listenTo(this.models, "remove", this.render);

        },
        clear: function () {
            var arr = this.models.done();
            _.invoke(arr, "destroy");
            return false
        },
        submit: function () {
            var models = this.models;
            var arr = models.toArr("red");
            if (arr.length == 0) {
                Alert.show(function () {
                    this.$(".txt-center").html("请至少选一注号码");
                });
                return false
            }
            //var _this = this;
            var data = {
                number: arr,
                bet_type: 0, // 和值
                done: true,
                count: arr.length
            };
            if (this.upModel) {
                // 修改
                data.t = +new Date;
                this.upModel.set(data);
                this.upModel = null
            } else {
                // 添加
                this.lists.add(data, {upview: hzPlayView})
            }
            this.toggle();
            this.clear();
            return false
        },
        render: function () {
            // 更新view
            this.$stats.html(this.template(this.models.count()))
        },
        toggle: function () {
            this.$submitBtn.html(this.upModel ? "确认修改" : "确认选号")
        }
    });

    // 3同号通选

    var Thtx3Balls = Backbone.Collection.extend({
        model: BallModel,
        initialize: function () {

        },
        count: function () {
            // 统计
            var arr = this.ball("red");
            return {
                count: arr.length,
                max: 40,
                min: 40
            }
        },
        ball: function (color) {
            return this.where({color: color})
        },
        done: function () {
            return this.where({done: true})
        },
        toArr: function (color) {
            var arr = color == "red" ? this.ball(color) : this.done();
            return _.map(arr, function (model) {
                return model.get("number")
            })
        }
    });

    var Thtx3BallView = Backbone.View.extend({
        events: {
            "click": "render"
        },
        initialize: function () {
            this.models = thtx3Balls; // 保存集合
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

    var Thtx3PlayView = Backbone.View.extend({
        el: "#k3_3thtx_play",
        upModel: null,
        template: _.template(sel_stats_tpl),
        events: {
            "click .clear": "clear",
            "click .selSubmitBtn": "submit"
        },
        initialize: function () {
            this.models = thtx3Balls;
            this.lists = lists;
            this.$stats = this.$(".sel-info");
            this.$balls = this.$(".ball em");
            this.$submitBtn = this.$(".selSubmitBtn");
            // 监听集合变化
            this.listenTo(this.models, "add", this.render);
            this.listenTo(this.models, "remove", this.render);


            // 保存每个球的view
            var hash = this.redHash = {}; // 保存每个球的view
            this.$balls.each(function (i, el) {
                var data = $.parseJSON($(el).attr("data-model"));
                data.done = false;
                hash[data.number] = new Thtx3BallView({
                    model: new BallModel(data),
                    el: el
                })
            });

        },
        clear: function () {
            var arr = this.models.done();
            _.invoke(arr, "destroy");
            return false
        },
        submit: function () {
            var models = this.models;
            var arr = models.toArr("red");
            if (arr.length == 0) {
                Alert.show(function () {
                    this.$(".txt-center").html("请至少选一注号码");
                });
                return false
            }

            var data = {
                number: arr,
                bet_type: 1, // 3同号通选
                done: true,
                count: arr.length
            };

            // 添加
            this.lists.add(data, {upview: thtx3PlayView});
            this.clear();
            return false
        },
        render: function () {
            // 更新view
            this.$stats.html(this.template(this.models.count()))
        }
    });

    // 3同号单选
    var Thdx3Balls = Backbone.Collection.extend({
        model: BallModel,
        initialize: function () {

        },
        count: function () {
            // 统计
            var arr = this.ball("red");
            return {
                count: arr.length,
                max: 240,
                min: 240
            }
        },
        ball: function (color) {
            return this.where({color: color})
        },
        done: function () {
            return this.where({done: true})
        },
        toArr: function (color) {
            var arr = color == "red" ? this.ball(color) : this.done();
            return _.map(arr, function (model) {
                return model.get("number")
            })
        }
    });

    var Thdx3BallView = Backbone.View.extend({
        events: {
            "click": "render"
        },
        initialize: function () {
            this.models = thdx3Balls; // 保存集合
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

    var Thdx3PlayView = Backbone.View.extend({
        el: "#k3_3thdx_play",
        upModel: null,
        template: _.template(sel_stats_tpl),
        events: {
            "click .clear": "clear",
            "click .selSubmitBtn": "submit"
        },
        initialize: function () {
            this.models = thdx3Balls;
            this.lists = lists;
            this.$stats = this.$(".sel-info");
            this.$balls = this.$(".ball em");
            this.$submitBtn = this.$(".selSubmitBtn");
            // 监听集合变化
            this.listenTo(this.models, "add", this.render);
            this.listenTo(this.models, "remove", this.render);


            // 保存每个球的view
            var hash = this.redHash = {}; // 保存每个球的view
            this.$balls.each(function (i, el) {
                var data = $.parseJSON($(el).attr("data-model"));
                data.done = false;
                hash[data.number] = new Thdx3BallView({
                    model: new BallModel(data),
                    el: el
                })
            });

        },
        clear: function () {
            var arr = this.models.done();
            _.invoke(arr, "destroy");
            return false
        },
        submit: function () {
            var models = this.models;
            var arr = models.toArr("red");
            if (arr.length == 0) {
                Alert.show(function () {
                    this.$(".txt-center").html("请至少选一注号码");
                });
                return false
            }


            // 添加
            for (var i = arr.length; i--;) {
                this.lists.add({
                    number: [arr[i]],
                    bet_type: 2, // 3同号单选
                    done: true,
                    count: 1
                }, {upview: thdx3PlayView})
            }

            this.clear();
            return false
        },
        render: function () {
            // 更新view
            this.$stats.html(this.template(this.models.count()))
        }
    });

    // 3不同 任选

    var Btrx3Balls = Backbone.Collection.extend({
        model: BallModel,
        count: function () {
            // 统计
            var red_n = this.ball("red").length;
            var count = red_n > 2 ? tools.c(red_n, 3) : 0;
            return {
                count: count,
                min: 40,
                max: 40
            }
        },
        ball: function (color) {
            return this.where({color: color})
        },
        done: function () {
            return this.where({done: true})
        },
        toArr: function (color) {
            var arr = color == "red" ? this.ball(color) : this.done();
            return _.map(arr, function (model) {
                return +model.get("number")
            })
        }
    });

    var Btrx3BallView = Backbone.View.extend({
        events: {
            "click": "render"
        },
        initialize: function () {
            this.models = btrx3Balls; // 保存集合
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

    var Btrx3PlayView = Backbone.View.extend({
        el: "#k3_3bt_rx",
        upModel: null,
        template: _.template(sel_stats_tpl),
        events: {
            "click .clear": "clear",
            "click .query": "query",
            "click .selSubmitBtn": "submit"
        },
        query: function (e) {
            var n = $(e.currentTarget).attr("data-n");
            var arr;
            switch (n) {
                case "all":
                    arr = [1, 2, 3, 4, 5, 6];
                    break;
                case "max":
                    arr = [4, 5, 6];
                    break;
                case "min":
                    arr = [1, 2, 3];
                    break;
                case "odd":
                    arr = [1, 3, 5];
                    break;
                case "even":
                    arr = [2, 4, 6];
                    break;
            }
            this.clear();
            var hash = this.redHash;
            _.each(arr, function (n) {
                hash[n].render()
            })


        },
        initialize: function () {
            // var _this = this;
            this.models = btrx3Balls;
            this.lists = lists;
            this.$balls = this.$(".item em");
            this.$stats = this.$(".sel-info");
            this.$submitBtn = this.$(".selSubmitBtn");
            // 保存每个球的view
            var hash = this.redHash = {}; // 保存每个球的view
            this.$balls.each(function (i, el) {
                var data = $.parseJSON($(el).attr("data-model"));
                hash[data.number] = new Btrx3BallView({
                    model: new BallModel(data),
                    el: el
                })
            });
            // 监听集合变化
            this.listenTo(this.models, "add", this.render);
            this.listenTo(this.models, "remove", this.render);
        },
        clear: function () {
            var arr = this.models.done();
            _.invoke(arr, "destroy");
            return false
        },
        submit: function () {
            var models = this.models;
            var arr = models.toArr("red");
            if (arr.length < 3) {
                Alert.show(function () {
                    this.$(".txt-center").html("请至少选3个号码");
                });
                return false
            }

            var data = {
                number: arr,
                bet_type: 3, // 3不用号
                done: true,
                count: tools.c(arr.length, 3)
            };
            if (this.upModel) {
                // 修改
                data.t = +new Date;
                this.upModel.set(data);
                this.upModel = null
            } else {
                // 添加
                this.lists.add(data, {upview: btrx3PlayView})
            }
            this.toggle();
            this.clear();
            return false

        },
        render: function () {
            // 更新view
            this.$stats.html(this.template(this.models.count()))
        },
        toggle: function () {
            this.$submitBtn.html(this.upModel ? "确认修改" : "确认选号")
        }
    });

    // 3不同 胆拖

    var Btdt3Balls = Backbone.Collection.extend({
        model: BallModel,
        count: function () {
            // 统计
            var red_n = this.ball("red").length;
            var red_dm_n = this.ball("red_dm").length;
            var count = (red_dm_n > 0 && (red_dm_n + red_n) > 2) ? tools.c(red_n, 3 - red_dm_n) : 0;
            return {
                count: count,
                min: 40,
                max: 40
            }
        },
        ball: function (color) {
            return this.where({color: color})
        },
        done: function () {
            return this.where({done: true})
        },
        toArr: function (color) {
            var arr = (color == "red" || color == "red_dm") ? this.ball(color) : this.done();
            return _.map(arr, function (model) {
                return +model.get("number")
            })
        }
    });

    var Btdt3BallView = Backbone.View.extend({
        events: {
            "click": "render"
        },
        initialize: function () {
            this.models = btdt3Balls; // 保存集合
            this.listenTo(this.model, "change", this.render);
            this.listenTo(this.model, "destroy", this.render);
        },
        render: function () {
            var model = this.model;
            var models = this.models;
            var map = this.map;
            var done = model.get("done");// 选中标识
            var color, dmLen;
            if (done) {
                this.$el.removeClass("active");
                map && map.$el.removeClass("libgred");
                models.remove(model);
            } else {
                color = model.get("color");
                if (color == "red_dm") { // 减少where查询
                    dmLen = models.ball("red_dm").length;
                    if (dmLen > 1) {
                        Alert.show(function () {
                            this.$(".txt-center").html("最多只能选2个胆码");
                        });
                        return
                    }
                }
                if (map && map.model.get("done")) {
                    map.$el.removeClass("active");
                    models.remove(map.model);
                    map.model.set("done", false, {silent: true })
                }
                models.add(this.model);
                this.$el.addClass("active");
                this.$el.removeClass("libgred");
                map && map.$el.addClass("libgred");
            }
            model.set("done", !done, {silent: true});

        }
    });

    var Btdt3PlayView = Backbone.View.extend({
        el: "#k3_3bt_dt",
        upModel: null,
        events: {
            "click .clear": "clear",
            "click .dm_all": "selAll",
            "click .selSubmitBtn": "submit"
        },
        template: _.template(sel_stats_tpl),
        initialize: function () {
            // var _this = this;

            this.models = btdt3Balls;
            this.lists = lists;
            this.$balls = this.$(".item em");
            this.$stats = this.$(".sel-info");
            this.$submitBtn = this.$(".selSubmitBtn");
            // 保存每个球的view
            var redHash = this.redHash = {}; // 保存每个球的view
            var dmHash = this.dmHash = {};
            this.$balls.each(function (i, el) {
                var data = $.parseJSON($(el).attr("data-model"));
                var hash = data.color == "red" ? redHash : dmHash;
                hash[data.number] = new Btdt3BallView({
                    model: new BallModel(data),
                    el: el
                });
            });
            // 缓存对应关系
            _.each(redHash, function (v, k, o) {
                o[k].map = dmHash[k];
                dmHash[k].map = o[k]
            });
            this.listenTo(this.models, "add", this.render);
            this.listenTo(this.models, "remove", this.render);
        },
        clear: function (e) {
            // red dm_red all arre
            var color = e || "all";
            (color == "all") || (color = $(e.currentTarget).attr("data-color") || color);
            var arr = (color == "all") ? this.models.done() : this.models.ball(color);
            _.invoke(arr, "destroy");
            return false
        },
        selAll: function () {
            this.clear("red");
            var red_dm = this.models.ball("red_dm");
            var redHash = this.redHash;
            var red_dm_hash = _.groupBy(red_dm, function (model) {
                return model.get("number")
            });
            _.each(redHash, function (view) {
                if (!red_dm_hash[view.model.get("number")]) {
                    view.render()
                }
            })
        },
        submit: function () {
            var models = this.models;
            var redNum = models.ball("red").length;
            var dmNum = models.ball("red_dm").length;
            if (dmNum == 0) {
                Alert.show(function () {
                    this.$(".txt-center").html("请至少选择1个胆码");
                });
                return false
            }
            if (redNum == 0) {
                Alert.show(function () {
                    this.$(".txt-center").html("请至少选择1个拖码");
                });
                return false
            }
            if ((dmNum + redNum) < 3) {
                Alert.show(function () {
                    this.$(".txt-center").html("胆码加拖码至少选3个");
                });
                return false
            }

            var count = tools.c(redNum, 3 - dmNum);

            if (count < 2) {
                Alert.show(function () {
                    this.$(".txt-center").html("胆拖玩法请至少选择2注号码");
                });
                return false
            }

            var _this = this;

            var data = {
                red_dm: _this.models.toArr("red_dm"),
                number: _this.models.toArr("red"),
                bet_type: 3,
                done: true,
                count: count
            };
            if (this.upModel) {
                // 修改
                data.t = +new Date;
                this.upModel.set(data);
                this.upModel = null
            } else {
                // 添加
                this.lists.add(data, {upview: btdt3PlayView})
            }
            this.toggle();
            this.clear();
            return false
        },
        render: function () {
            // 更新view
            this.$stats.html(this.template(this.models.count()))
        },
        toggle: function () {
            this.$submitBtn.html(this.upModel ? "确认修改" : "确认选号")
        }
    });

    // 3连号通选

    var Lhtx3Balls = Backbone.Collection.extend({
        model: BallModel,
        initialize: function () {

        },
        count: function () {
            // 统计
            var arr = this.ball("red");
            return {
                count: arr.length,
                max: 10,
                min: 10
            }
        },
        ball: function (color) {
            return this.where({color: color})
        },
        done: function () {
            return this.where({done: true})
        },
        toArr: function (color) {
            var arr = color == "red" ? this.ball(color) : this.done();
            return _.map(arr, function (model) {
                return model.get("number")
            })
        }
    });

    var Lhtx3BallView = Backbone.View.extend({
        events: {
            "click": "render"
        },
        initialize: function () {
            this.models = lhtx3Balls; // 保存集合
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

    var Lhtx3PlayView = Backbone.View.extend({
        el: "#k3_3lhtx_play",
        upModel: null,
        template: _.template(sel_stats_tpl),
        events: {
            "click .clear": "clear",
            "click .selSubmitBtn": "submit"
        },
        initialize: function () {
            this.models = lhtx3Balls;
            this.lists = lists;
            this.$stats = this.$(".sel-info");
            this.$balls = this.$(".ball em");
            this.$submitBtn = this.$(".selSubmitBtn");
            // 监听集合变化
            this.listenTo(this.models, "add", this.render);
            this.listenTo(this.models, "remove", this.render);


            // 保存每个球的view
            var hash = this.redHash = {}; // 保存每个球的view
            this.$balls.each(function (i, el) {
                var data = $.parseJSON($(el).attr("data-model"));
                data.done = false;
                hash[data.number] = new Lhtx3BallView({
                    model: new BallModel(data),
                    el: el
                })
            });

        },
        clear: function () {
            var arr = this.models.done();
            _.invoke(arr, "destroy");
            return false
        },
        submit: function () {
            var models = this.models;
            var arr = models.toArr("red");
            if (arr.length == 0) {
                Alert.show(function () {
                    this.$(".txt-center").html("请至少选一注号码");
                });
                return false
            }

            var data = {
                number: arr,
                bet_type: 4, // 3同号通选
                done: true,
                count: arr.length
            };

            // 添加
            this.lists.add(data, {upview: lhtx3PlayView});
            this.clear();
            return false
        },
        render: function () {
            // 更新view
            this.$stats.html(this.template(this.models.count()))
        }
    });

    // 2同号复选
    var Thfx2Balls = Backbone.Collection.extend({
        model: BallModel,
        initialize: function () {

        },
        count: function () {
            // 统计
            var arr = this.ball("red");
            return {
                count: arr.length,
                max: 15,
                min: 15
            }
        },
        ball: function (color) {
            return this.where({color: color})
        },
        done: function () {
            return this.where({done: true})
        },
        toArr: function (color) {
            var arr = color == "red" ? this.ball(color) : this.done();
            return _.map(arr, function (model) {
                return model.get("number")
            })
        }
    });

    var Thfx2BallView = Backbone.View.extend({
        events: {
            "click": "render"
        },
        initialize: function () {
            this.models = thfx2Balls; // 保存集合
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

    var Thfx2PlayView = Backbone.View.extend({
        el: "#k3_2thfx_play",
        upModel: null,
        template: _.template(sel_stats_tpl),
        events: {
            "click .clear": "clear",
            "click .selSubmitBtn": "submit"
        },
        initialize: function () {
            this.models = thfx2Balls;
            this.lists = lists;
            this.$stats = this.$(".sel-info");
            this.$balls = this.$(".ball em");
            this.$submitBtn = this.$(".selSubmitBtn");
            // 监听集合变化
            this.listenTo(this.models, "add", this.render);
            this.listenTo(this.models, "remove", this.render);


            // 保存每个球的view
            var hash = this.redHash = {}; // 保存每个球的view
            this.$balls.each(function (i, el) {
                var data = $.parseJSON($(el).attr("data-model"));
                data.done = false;
                hash[data.number] = new Thfx2BallView({
                    model: new BallModel(data),
                    el: el
                })
            });

        },
        clear: function () {
            var arr = this.models.done();
            _.invoke(arr, "destroy");
            return false
        },
        submit: function () {
            var models = this.models;
            var arr = models.toArr("red");
            if (arr.length == 0) {
                Alert.show(function () {
                    this.$(".txt-center").html("请至少选一注号码");
                });
                return false
            }


            // 添加
            for (var i = arr.length; i--;) {
                this.lists.add({
                    number: [arr[i]],
                    bet_type: 5, // 2同号复选
                    done: true,
                    count: 1
                }, {upview: thfx2PlayView})
            }

            this.clear();
            return false
        },
        render: function () {
            // 更新view
            this.$stats.html(this.template(this.models.count()))
        }
    });

    // 2同号单选

    var Thdx2Balls = Backbone.Collection.extend({
        model: BallModel,
        initialize: function () {
        },
        count: function () {
            // 统计
            var th = this.ball("red_th").length;
            var bt = this.ball("red_bt").length;
            return {
                count: (th && bt) ? th * bt : 0,
                max: 80,
                min: 80
            }
        },
        ball: function (color) {
            return this.where({color: color})
        },
        done: function () {
            return this.where({done: true})
        },
        toArr: function (color) {
            var arr = ( color == "red_th" || color == "red_bt"  ) ? this.ball(color) : this.done();
            return _.map(arr, function (model) {
                return model.get("number")
            })
        }
    });

    var Thdx2BallView = Backbone.View.extend({
        events: {
            "click": "render"
        },
        initialize: function () {
            this.models = thdx2Balls; // 保存集合
            this.listenTo(this.model, "change", this.render);
            this.listenTo(this.model, "destroy", this.render);
        },
        render: function () {
            var model = this.model;
            var models = this.models;
            var map = this.map;
            var done = model.get("done");// 选中标识
            var color; //, dmLen;
            if (done) {
                this.$el.removeClass("active");
                //map && map.$el.removeClass("libgred");
                models.remove(model);
            } else {
                color = model.get("color");
                if (map && map.model.get("done")) {
                    map.$el.removeClass("active");
                    models.remove(map.model);
                    map.model.set("done", false, {silent: true })
                }
                models.add(this.model);
                this.$el.addClass("active");
                //this.$el.removeClass("libgred");
                //map && map.$el.addClass("libgred");
            }
            model.set("done", !done, {silent: true});

        }
    });

    var Thdx2PlayView = Backbone.View.extend({
        el: "#k3_2thdx_play",
        upModel: null,
        events: {
            "click .clear": "clear",
            "click .selSubmitBtn": "submit"
        },
        template: _.template(sel_stats_tpl),
        initialize: function () {
            // var _this = this;

            this.models = thdx2Balls;
            this.lists = lists;
            this.$balls = this.$(".ball em");
            this.$stats = this.$(".sel-info");
            this.$submitBtn = this.$(".selSubmitBtn");
            // 保存每个球的view
            // var thHash = this.thHash = {}; // 保存每个球的view
            // var btHash = this.btHash = {};
            var thHash = {};
            var btHash = {};
            this.$balls.each(function (i, el) {
                var data = $.parseJSON($(el).attr("data-model"));
                var hash = data.color == "red_th" ? thHash : btHash;
                hash[data.number] = new Thdx2BallView({
                    model: new BallModel(data),
                    el: el
                });
            });
            // 缓存对应关系
            _.each(thHash, function (v, k, o) {
                var k2 = k.split("")[0];
                o[k].map = btHash[k2];
                btHash[k2].map = o[k]
            });
            this.listenTo(this.models, "add", this.render);
            this.listenTo(this.models, "remove", this.render);
        },
        clear: function () {
            var arr = this.models.done();
            _.invoke(arr, "destroy");
            return false
        },
        submit: function () {
            var models = this.models;
            var red_th = models.toArr("red_th");
            var red_bt = models.toArr("red_bt");
            var red_th_n = models.toArr("red_th").length;
            var red_bt_n = models.toArr("red_bt").length;
            if (red_th_n == 0 || red_bt_n == 0) {
                Alert.show(function () {
                    this.$(".txt-center").html("请至少选一注号码");
                });
                return false
            }
            // 添加
            for (var i = 0; i < red_th_n; i++) {
                for (var j = 0; j < red_bt_n; j++) {
                    this.lists.add({
                        number: [red_th[i] + red_bt[j]],
                        bet_type: 6,
                        done: true,
                        count: 1
                    }, {upview: thdx2PlayView})
                }
            }
            this.clear();
            return false
        },
        render: function () {
            // 更新view
            this.$stats.html(this.template(this.models.count()))
        }
    });

    // 2不同 任选
    var Btrx2Balls = Backbone.Collection.extend({
        model: BallModel,
        count: function () {
            // 统计
            var red_n = this.ball("red").length;
            var count = red_n > 1 ? tools.c(red_n, 2) : 0;
            return {
                count: count,
                min: 8,
                max: 8
            }
        },
        ball: function (color) {
            return this.where({color: color})
        },
        done: function () {
            return this.where({done: true})
        },
        toArr: function (color) {
            var arr = color == "red" ? this.ball(color) : this.done();
            return _.map(arr, function (model) {
                return +model.get("number")
            })
        }
    });

    var Btrx2BallView = Backbone.View.extend({
        events: {
            "click": "render"
        },
        initialize: function () {
            this.models = btrx2Balls; // 保存集合
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

    var Btrx2PlayView = Backbone.View.extend({
        el: "#k3_2bt_rx",
        upModel: null,
        template: _.template(sel_stats_tpl),
        events: {
            "click .clear": "clear",
            "click .query": "query",
            "click .selSubmitBtn": "submit"
        },
        query: function (e) {
            var n = $(e.currentTarget).attr("data-n");
            var arr;
            switch (n) {
                case "all":
                    arr = [1, 2, 3, 4, 5, 6];
                    break;
                case "max":
                    arr = [4, 5, 6];
                    break;
                case "min":
                    arr = [1, 2, 3];
                    break;
                case "odd":
                    arr = [1, 3, 5];
                    break;
                case "even":
                    arr = [2, 4, 6];
                    break;
            }

            this.clear();
            var hash = this.redHash;
            _.each(arr, function (n) {
                hash[n].render()
            })


        },
        initialize: function () {
            //var _this = this;
            this.models = btrx2Balls;
            this.lists = lists;
            this.$balls = this.$(".item em");
            this.$stats = this.$(".sel-info");
            this.$submitBtn = this.$(".selSubmitBtn");
            // 保存每个球的view
            var hash = this.redHash = {}; // 保存每个球的view
            this.$balls.each(function (i, el) {
                var data = $.parseJSON($(el).attr("data-model"));
                hash[data.number] = new Btrx2BallView({
                    model: new BallModel(data),
                    el: el
                })
            });
            // 监听集合变化
            this.listenTo(this.models, "add", this.render);
            this.listenTo(this.models, "remove", this.render);
        },
        clear: function () {
            var arr = this.models.done();
            _.invoke(arr, "destroy");
            return false
        },
        submit: function () {
            var models = this.models;
            var arr = models.toArr("red");
            if (arr.length < 2) {
                Alert.show(function () {
                    this.$(".txt-center").html("请至少选2个号码");
                });
                return false
            }

            var data = {
                number: arr,
                bet_type: 7, // 3不用号
                done: true,
                count: tools.c(arr.length, 2)
            };
            if (this.upModel) {
                // 修改
                data.t = +new Date;
                this.upModel.set(data);
                this.upModel = null
            } else {
                // 添加
                this.lists.add(data, {upview: btrx2PlayView})
            }
            this.toggle();
            this.clear();
            return false

        },
        render: function () {
            // 更新view
            this.$stats.html(this.template(this.models.count()))
        },
        toggle: function () {
            this.$submitBtn.html(this.upModel ? "确认修改" : "确认选号")
        }
    });

    // 2不同 胆拖

    var Btdt2Balls = Backbone.Collection.extend({
        model: BallModel,
        count: function () {
            // 统计
            var red_n = this.ball("red").length;
            var red_dm_n = this.ball("red_dm").length;
            var count = (red_dm_n > 0 && (red_dm_n + red_n) > 1) ? tools.c(red_n, 2 - red_dm_n) : 0;
            return {
                count: count,
                min: 8,
                max: 8
            }
        },
        ball: function (color) {
            return this.where({color: color})
        },
        done: function () {
            return this.where({done: true})
        },
        toArr: function (color) {
            var arr = (color == "red" || color == "red_dm") ? this.ball(color) : this.done();
            return _.map(arr, function (model) {
                return +model.get("number")
            })
        }
    });

    var Btdt2BallView = Backbone.View.extend({
        events: {
            "click": "render"
        },
        initialize: function () {
            this.models = btdt2Balls; // 保存集合
            this.listenTo(this.model, "change", this.render);
            this.listenTo(this.model, "destroy", this.render);
        },
        render: function () {
            var model = this.model;
            var models = this.models;
            var map = this.map;
            var done = model.get("done");// 选中标识
            var color, dmLen;
            if (done) {
                this.$el.removeClass("active");
                map && map.$el.removeClass("libgred");
                models.remove(model);
            } else {
                color = model.get("color");
                if (color == "red_dm") { // 减少where查询
                    dmLen = models.ball("red_dm").length;
                    if (dmLen > 0) {
                        Alert.show(function () {
                            this.$(".txt-center").html("最多只能选1个胆码");
                        });
                        return
                    }
                }
                if (map && map.model.get("done")) {
                    map.$el.removeClass("active");
                    models.remove(map.model);
                    map.model.set("done", false, {silent: true })
                }
                models.add(this.model);
                this.$el.addClass("active");
                this.$el.removeClass("libgred");
                map && map.$el.addClass("libgred");
            }
            model.set("done", !done, {silent: true});

        }
    });

    var Btdt2PlayView = Backbone.View.extend({
        el: "#k3_2bt_dt",
        upModel: null,
        events: {
            "click .clear": "clear",
            "click .dm_all": "selAll",
            "click .selSubmitBtn": "submit"
        },
        template: _.template(sel_stats_tpl),
        initialize: function () {
            //var _this = this;

            this.models = btdt2Balls;
            this.lists = lists;
            this.$balls = this.$(".item em");
            this.$stats = this.$(".sel-info");
            this.$submitBtn = this.$(".selSubmitBtn");
            // 保存每个球的view
            var redHash = this.redHash = {}; // 保存每个球的view
            var dmHash = this.dmHash = {};
            this.$balls.each(function (i, el) {
                var data = $.parseJSON($(el).attr("data-model"));
                var hash = data.color == "red" ? redHash : dmHash;
                hash[data.number] = new Btdt2BallView({
                    model: new BallModel(data),
                    el: el
                });
            });
            // 缓存对应关系
            _.each(redHash, function (v, k, o) {
                o[k].map = dmHash[k];
                dmHash[k].map = o[k]
            });
            this.listenTo(this.models, "add", this.render);
            this.listenTo(this.models, "remove", this.render);
        },
        clear: function (e) {
            var color = e || "all";
            (color == "all") || (color = $(e.currentTarget).attr("data-color") || color);
            var arr = (color == "all") ? this.models.done() : this.models.ball(color);
            _.invoke(arr, "destroy");
            return false
        },
        selAll: function () {
            this.clear("red");
            var red_dm = this.models.ball("red_dm");
            var redHash = this.redHash;
            var red_dm_hash = _.groupBy(red_dm, function (model) {
                return model.get("number")
            });
            _.each(redHash, function (view) {
                if (!red_dm_hash[view.model.get("number")]) {
                    view.render()
                }
            })
        },
        submit: function () {
            var models = this.models;
            var redNum = models.ball("red").length;
            var dmNum = models.ball("red_dm").length;
            if (dmNum == 0) {
                Alert.show(function () {
                    this.$(".txt-center").html("请至少选择1个胆码");
                });
                return false
            }
            if (redNum == 0) {
                Alert.show(function () {
                    this.$(".txt-center").html("请至少选择1个拖码");
                });
                return false
            }

            var count = tools.c(redNum, 2 - dmNum);

            if (count < 2) {
                Alert.show(function () {
                    this.$(".txt-center").html("胆拖玩法请至少选择2注号码");
                });
                return false
            }

            var _this = this;

            var data = {
                red_dm: _this.models.toArr("red_dm"),
                number: _this.models.toArr("red"),
                bet_type: 7,
                done: true,
                count: count
            };
            if (this.upModel) {
                // 修改
                data.t = +new Date;
                this.upModel.set(data);
                this.upModel = null
            } else {
                // 添加
                this.lists.add(data, {upview: btdt2PlayView})
            }
            this.toggle();
            this.clear();
            return false
        },
        render: function () {
            // 更新view
            this.$stats.html(this.template(this.models.count()))
        },
        toggle: function () {
            this.$submitBtn.html(this.upModel ? "确认修改" : "确认选号")
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
            if (!upview) return false;

            upview.upModel = this.model;
            upview.toggle();
            // 渲染
            upview.clear();
            _.each(this.model.get("number"), function (n) {
                upview.redHash[+n].render()
            });

            _.each(this.model.get("red_dm"), function (n) {
                upview.dmHash[+n].render()
            });
            // 切换对应tab
            if (upview == hzPlayView) {
                toNav.to(0)
            } else if (upview == btrx3PlayView) {
                toNav.to(3);
                toSubNav.to(0);
            } else if (upview == btdt3PlayView) {
                toNav.to(3);
                toSubNav.to(1);
            } else if (upview == btrx2PlayView) {
                toNav.to(7);
                toSubNav2Bt.to(0);
            } else if (upview == btdt2PlayView) {
                toNav.to(7);
                toSubNav2Bt.to(1);
            }
            return false
        },
        render: function () {
            var data = this.model.toJSON();
            // 1 => 01
            data = $.extend(true, {}, data);
            data.number = _.sortBy(data.number);
            this.$el.html(this.template(data));
            this.$el.removeClass("libgred");
            ballsView.render(); // 更新统计
            return this
        },
        destroy: function () {
            this.model.destroy();
            var upview = this.upview;
            if (upview && upview.upModel == this.model) { // 是否删除当前修改
                upview.upModel = null;
                upview.toggle();
                upview.clear("all");
            }

            ballsView.render(); // 更新统计
            return false
        }
    });

    var BallsView = Backbone.View.extend({
        el: "#k3_ball_list",
        /*
         * 0 和值
         * 1 三同号通选
         * 2 三同号单选
         * 3 三不同号
         * 4 三连号通选
         * 5 二同号复选
         * 6 二同号单选
         * 7 二不同号
         * */
        bet_type: 3,
        events: {
            "click .rndBtn": "rnd",
            "click .clear": "clear",
            "keyup .mul": "checkInput",
            "keyup .rnd_n": "checkInput",
            "blur .mul": "blurInput",
            "blur .rnd_n": "blurInput",
            "click #ballSubmit": "submit",
            "click .read_protocal":"popupProtocal",
            "click .risk_anotation":"popupRiskAnnotation"
        },
        popupProtocal:function(){
        	userAgreementPopup("agentProtocal","用户委托投注协议");
        	return false;
        },
        popupRiskAnnotation:function(){
        	userAgreementPopup("riskAnnotation","限号风险投注须知");
        	return false;
        },        
        initialize: function () {
            this.$rnd_n = this.$(".rnd_n");
            this.$parent = this.$("#item_parent");
            this.$read = this.$(".ball-submit input[type='checkbox']");
            // 集合
            this.models = lists;

            // 更新统计
            this.$mul = this.$(".mul");
            this.$count = this.$(".count");
            this.$money = this.$(".money");

            // 2不同单选 original
            var th = ["11", "22", "33", "44", "55", "66"];
            var bt = ["1", "2", "3", "4", "5", "6"];
            var arr = [];
            for (var i = th.length; i--;) {
                for (var j = bt.length; j--;) {
                    if (th[i].indexOf(bt[j]))
                        arr.push(th[i] + bt[j])
                }
            }

            this.original_2bt = arr;
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
        original: function () {
            var original, original_n;
            switch (this.bet_type) {
                case 0:
                    original = _.range(3, 19);
                    original_n = 1;
                    break;
                case 1:
                    original = ["三同号通选"];
                    original_n = 1;
                    break;
                case 2:
                    original = [111, 222, 333, 444, 555, 666];
                    original_n = 1;
                    break;
                case 3:
                    original = _.range(1, 7);
                    original_n = 3;
                    break;
                case 4:
                    original = ["三连号通选"];
                    original_n = 1;
                    break;
                case 5:
                    original = ["11*", "22*", "33*", "44*", "55*", "66*"];
                    original_n = 1;
                    break;
                case 6:
                    original = this.original_2bt;
                    original_n = 1;
                    break;
                case 7:
                    original = _.range(1, 7);
                    original_n = 2;
                    break;
            }
            return {
                original: original,
                original_n: original_n
            }
        },
        rnd: function (e) {
            var bet_type = this.bet_type;
            var n = $(e.currentTarget).attr("data-n") || this.$rnd_n.val();
            var upview = null;
            if (bet_type == 0) {
                upview = hzPlayView
            } else if (bet_type == 3) {
                upview = btrx3PlayView
            } else if (bet_type == 7) {
                upview = btrx2PlayView
            }
            for (var i = 0; i < n; i++) {
                this.models.add(
                    {
                        number: _.sample(this.original().original, this.original().original_n),
                        bet_type: bet_type,
                        done: true,
                        count: 1 // 随机都是1
                    },
                    {
                        upview: upview
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
            this.render();
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

            if (!this.$read.eq(0).prop("checked")) {
                Alert.show(function () {
                    this.$(".txt-center").html("请先阅读并同意\u300a用户委托投注协议\u300b和\u300a限号投注风险须知\u300b");
                });
                return false
            }

            // if (!this.$read.eq(1).prop("checked")) {
            //     Alert.show(function () {
            //         this.$(".txt-center").html("请先阅读并同意\u300a限号投注风险须知\u300b");
            //     });
            //     return false
            // }
           
            isLogin(function () {
                // 已登陆
                pay.call(_this, "k3")
            }, function () {
                // 未登录
            	var  dataObj = ls.call(_this,"k3");
           	 if(!!dataObj.data && tools.getBytes(dataObj.data)<=30*1024){
           		dataObj.timestamp = +new Date;             		              		
           		var data = JSON.stringify(dataObj); 
           		$.jStorage.set("betsData",data); 
           		//$.jStorage.setTTL("betsData",60000);
           	 }
                art_login.show(function () {
                    this.successCallfn = function () {
                        this.hide();
                        pay.call(_this, "k3")
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

    // 和值
    var hzBalls = new HzBalls;
    var hzPlayView = new HzPlayView;

    // 3同号通选
    var thtx3Balls = new Thtx3Balls;
    var thtx3PlayView = new Thtx3PlayView;


    // 3同号单选
    var thdx3Balls = new Thdx3Balls;
    var thdx3PlayView = new Thdx3PlayView;

    // 3不同任选
    var btrx3Balls = new Btrx3Balls;
    var btrx3PlayView = new Btrx3PlayView;

    // 3不同胆拖
    var btdt3Balls = new Btdt3Balls;
    var btdt3PlayView = new Btdt3PlayView;

    // 3连号通选
    var lhtx3Balls = new Lhtx3Balls;
    var lhtx3PlayView = new Lhtx3PlayView;

    // 2同号复选
    var thfx2Balls = new Thfx2Balls;
    var thfx2PlayView = new Thfx2PlayView;

    // 2同号单选
    var thdx2Balls = new Thdx2Balls;
    var thdx2PlayView = new Thdx2PlayView;

    // 2不同任选
    var btrx2Balls = new Btrx2Balls;
    var btrx2PlayView = new Btrx2PlayView;

    // 2不同胆拖
    var btdt2Balls = new Btdt2Balls;
    var btdt2PlayView = new Btdt2PlayView;

    // 投注

    (function () {

    	var outerData = $("#defaultBets").val();          	    
        // HZ_8#2;HZ_8,9,11#2;AAA_*#2;AAA_111;3BT_1234

        var _getNumber = function (str) {

            var bet_type;
            var number;
            var count;
            var numberArr;
            var red_dm = [];
            var arr = str.split("#")[0].split("_");
            var mul = str.split("#")[1];
            var upview = null;
            if (arr[0] == "HZ") {
                bet_type = 0;
                number = arr[1].split(",");
                count = number.length
            } else if (arr[0] == "AAA") {
                if (arr[1] == "*") {
                    bet_type = 1;
                    number = ["三同号通选"];
                    count = number.length
                } else {
                    bet_type = 2;
                    number = [arr[1]];
                    count = number.length
                }
            } else if (arr[0] == "3BT") {
                bet_type = 3;
                numberArr = arr[1].split("$");
                number = numberArr.length == 2 ? numberArr[1].split("") : numberArr[0].split("");
                red_dm = numberArr.length == 2 ? numberArr[0].split("") : red_dm;
                count = tools.c(number.length, 3 - red_dm.length);
            } else if (arr[0] == "3LH") {
                bet_type = 4;
                number = ["三连号通选"];
                count = number.length
            } else if (arr[0] == "AA") {
                bet_type = 5;
                number = [arr[1] + "*"];
                count = number.length
            } else if (arr[0] == "AAX") {
                bet_type = 6;
                number = [arr[1].replace("|", "")];
                count = number.length
            } else if (arr[0] == "2BT") {
                bet_type = 7;
                numberArr = arr[1].split("$");
                number = numberArr.length == 2 ? numberArr[1].split("") : numberArr[0].split("");
                red_dm = numberArr.length == 2 ? numberArr[0].split("") : red_dm;
                count = tools.c(number.length, 2 - red_dm.length);
            }

            if (bet_type == 0) {
                upview = hzPlayView
            } else if (bet_type == 3) {
                upview = red_dm.length > 0 ? btdt3PlayView : btrx3PlayView;

            } else if (bet_type == 7) {
                upview = red_dm.length > 0 ? btdt2PlayView : btrx2PlayView;
            }

            return {
                number: number,
                bet_type: bet_type,
                upview: upview,
                mul: mul,
                count: count,
                red_dm: red_dm
            }
        };
        var fillData = function(data){
        	 var list = data.split(";");
             for (var i = 0, len = list.length; i < len; i++) {
                 var d = _getNumber(list[i]);
                 lists.add(
                     {
                         red_dm: d.red_dm,
                         number: d.number,
                         bet_type: d.bet_type,
                         done: true,
                         count: d.count // 随机都是1
                     },
                     {
                         upview: d.upview
                     }
                 );

             }
             $("#k3_ball_list .mul").val(d.mul); // 倍数都一样
             ballsView.render();
             $.jStorage.flush();             
        };

        isLogin(function(userId) {
            if (userId.indexOf("qq.sohu.com") > -1 && $.jStorage.index().length > 0) {
                var dataObj = $.jStorage.get("betsData");
                dataObj = JSON.parse(dataObj);
                if (Math.abs( + new Date - ( + dataObj.timestamp)) > 60 * 1000) { //  $.jStorage.getTTL("betsData")<0
                    $.jStorage.flush();
                } else if (dataObj && (dataObj.gameId === "k3" || dataObj.gameId == "k3js" || dataObj.gameId == "k3gx" || dataObj.gameId == "k3jl")) {
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
    // 切换默认tab
    (function () {
        var index = location.hash.replace("#", "");
        var gameId = $("#gameId").val();
        var bonus_detail_gx = $(".bonus_detail");
        var bonus_detail_js = $(".bonus_detail_js");
        if (index.length > 0) {
            index = +index;
            if (_.isNumber(index) && index >= 0 && index < 8) {
                toNav.to(index)
            }
        }
        $(".bonus_info").hover(function(){
        	if(gameId=="k3gx"){
         	   bonus_detail_gx.show();   
            }
        	if(gameId=="k3js"){
        		bonus_detail_js.show();
        	}
        },function(){
        	if(gameId=="k3gx"){
         	   bonus_detail_gx.hide();   
            }
        	if(gameId=="k3js"){
        		bonus_detail_js.hide();
        	}
        });
               
    })();
    return function () {

    }
});