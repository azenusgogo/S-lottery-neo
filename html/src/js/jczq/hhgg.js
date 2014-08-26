define([
    "lib/tools",
    "lib/hh_calc",
    "lib/calc",
    "login/art_login",
    "pay/pay",
    "lib/ls",
    "lib/alert",
    "jczq/up_sp",
    "jczq/toggle_matchTime",
    "jczq/footerfix",
    "jczq/theadfix",
    "lib/jstorage",
    "update/stop_game",
    "lib/jquery.mousewheel",
    "lib/jquery.jscrollpane",
    "lib/json2"
], function (tools, C, C1, art_login, pay, ls, dialogAlert, up_sp, toggle_matchTime, footerfix, theadfix, jstorage, stop_game) {
    return function () {
        if (stop_game() == 0) return function() {};

        jstorage();

        var Alert = dialogAlert("提示");

        var _nickNameHash = {"11103": "胜", "11101": "平", "11100": "负", "11203": "让球胜", "11201": "让球平", "11200": "让球负", "11310": "1:0", "11320": "2:0", "11321": "2:1", "11330": "3:0", "11331": "3:1", "11332": "3:2", "11340": "4:0", "11341": "4:1", "11342": "4:2", "11350": "5:0", "11351": "5:1", "11352": "5:2", "11390": "胜其他", "11300": "0:0", "11311": "1:1", "11322": "2:2", "11333": "3:3", "11399": "平其他", "11301": "0:1", "11302": "0:2", "11312": "1:2", "11303": "0:3", "11313": "1:3", "11323": "2:3", "11304": "0:4", "11314": "1:4", "11324": "2:4", "11305": "0:5", "11315": "1:5", "11325": "2:5", "11309": "负其他", "11400": "0", "11401": "1", "11402": "2", "11403": "3", "11404": "4", "11405": "5", "11406": "6", "11407": "7+", "11533": "胜胜", "11531": "胜平", "11530": "胜负", "11513": "平胜", "11511": "平平", "11510": "平负", "11503": "负胜", "11501": "负平", "11500": "负负" };

        var MatchItemModel = Backbone.Model.extend({
            defaults: {
                done: 0
            }
        });

        var MatchColl = Backbone.Collection.extend({
            model: MatchItemModel,
            count: function () {
                var arr1 = this.pluck("matchid");
                var arr2 = _.uniq(arr1);
                var obj = _.object(arr2, arr2);
                return [arr1, arr2, obj]
            }
        });

        var matchItemView = Backbone.View.extend({
            initialize: function () {
                this.models = matchColl;
                this.listenTo(this.model, "change", this.render);
                this.listenTo(this.model, 'destroy', this.render);
            },
            events: {
                click: "render"
            },
            render: function (opts) {
                var done = this.model.get("done");
                if (!done){
                    var arr = this.models.count()[1];
                    arr.push(this.model.get("matchid"))
                    if (_.uniq(arr).length > 15){
                        Alert.show(function () {
                            this.$(".txt-center").html("选择的比赛不能超过15场")
                        });
                        return false
                    }
                }
                if (done) { // 已选中
                    this.$el.removeClass("cur");
                } else { // 未选中
                    this.$el.addClass("cur");
                }
                opts = opts || {};
                opts = opts.allin ? opts : {};
                this.models[done ? "remove" : "add"](this.model, opts);
                this.model.set("done", !done, {silent: true});


                if (this.model.get("hide")) {
                    var matchid = this.model.get("matchid");
                    var n = this.models.where({matchid: matchid, hide: 1}).length;
                    var html = "";
                    if (n > 0) {
                        html = '<span>已选<em>' + n + '</em></span><i class="arrud"></i>'
                    } else {
                        html = '<span>收起</span><i class="arrud"></i>'
                    }
                    this.$el.parents("tbody").find("a.open").html(html)
                }
                
                if (!done) {
                    var id = this.model.get("code") + "_" + this.model.get("matchid");
                    var $el = appView.listItemView[id].$el;
                    if ($el.length) {
                        // 动画
                        if ($("#listbox:visible").length) {
                            var offset = this.$el.offset();
                            var $el_clone = $("<div></div>");
                            $el_clone
                                .addClass("jc_animation_" + $el.attr("class"))
                                .html($el.html())
                                .css(offset)
                                .appendTo('body')
                            $el_clone.animate($el.offset(), 300, function() {
                                $el_clone.remove();
                                $el.css("visibility", "visible")
                            })
                        } else {
                            $el.css("visibility", "visible")
                        }
                    }
                }

                return false
            }
        });

        var listItemView = Backbone.View.extend({
            tagName: "em",
            events: {
                "click": "destroy"
            },
            initialize: function () {
                this.listenTo(this.model, 'destroy', this.remove)
            },
            destroy: function () {
                this.model.destroy();
            },
            render: function () {
                var code = +this.model.get("code");
                var className = "";

                switch (code) {
                    case 11103:
                        className = "s";
                        break;
                    case 11101:
                        className = "p";
                        break;
                    case 11100:
                        className = "f";
                        break;
                    default:
                        className = "f";
                }
                this.$el
                    .addClass(className)
                    .attr("index", this.model.get("index"))
                    .css("visibility", "hidden")
                    .html(_nickNameHash[code]);
                return this
            }
        });

        var AppView = Backbone.View.extend({
            el: "#jc_hhgg",
            events: {
                "click .selmatch": "matchSel",// 赛事筛选
                "click .todayinfo a": "timeToggle", // 按时间显示隐/藏比赛
                "click .matchbox": "stopPropagation",
                "click .matchbox .close": "matchClose",
                "click .matchbox .all": "showAllLeague",
                "click .matchbox .inverse": "leagueInverse",
                "click .matchbox input": "leagueToggle", // 按联赛显示/隐藏比赛
                "click #showAllLeague": "showAllLeague",
                "click .onlyfive input": "leagueFive",
                "click .open": "matchOpen", // 显示/隐藏总进球、半全场、比分选项
                "click .allin": "allIn", //  全包
                "click input.dm": "render", // 选中/取消胆码
                "click #opbtn": "openList", // 展开收起选项列表
                "click .removeListItem": "removeListItem", //
                "click .list_dm": "render", // 选项列表里的胆码
                "click #chuan_n_1 input": "chuan_n1",
                "click #chuan_n_n input": "chuan_nn",
                "keyup #mul": "checkNumber",
                "keydown #mul": "checkNumber",
                "blur #mul": "checkNumber",
                "click #plus": "plus",
                "click #minus": "minus",
                "click .only_sel": "only_sel",
                "click .submit": "submit",
                "click #freedo": "freedo"
            },
            listTemplate: _.template($("#listTemplate").html()),
            initialize: function () {
                this.gameid = $("#gameId").val();
                this.models = matchColl;
                var viewArr = this.viewArr = [];
                var viewHash = this.viewHash = {};
                this.$("[model]").each(function () {
                    var _this = this;
                    var data = $.parseJSON($(_this).attr("model"));
                    var view = new matchItemView({
                        el: _this,
                        model: new MatchItemModel(data)
                    });
                    viewArr.push(view);
                    viewHash[data.matchid] = viewHash[data.matchid] || [];
                    viewHash[data.matchid].push(view)
                });
                var matchHash = this.matchHash = {};
                var leagueHash = this.leagueHash = {};
                var timeHash = this.timeHash = {};
                this.$(".jc_row").each(function () {
                    var $this = $(this);
                    var data = $.parseJSON($this.attr("data"));
                    data.el = $(this);
                    matchHash[data["matchid"]] = data;
                    (timeHash[data["time"]] = timeHash[data["time"]] || []).push($this);
                    (leagueHash[data["league"]] = leagueHash[data["league"]] || []).push($this);
                });

                var leagueCheckedHash = this.leagueCheckedHash = {};

                this.$(".matchbox input").each(function () {
                    var $this = $(this);
                    var key = $this.val();
                    leagueCheckedHash[key] = $this;
                });

                var dmHash = this.dmHash = {};
                this.$("input.dm").each(function () {
                    dmHash[$(this).attr("matchid")] = this
                });

                var timeBtnHash = this.timeBtnHash = {};
                this.$(".todayinfo a").each(function () {
                    var $this = $(this);
                    var key = $this.attr("data");
                    timeBtnHash[key] = $this;
                });

                this.$onlyfive = this.$(".onlyfive input");

                var leagueFiveHash = this.leagueFiveHash = {};
                var leagueFiveArr = this.$onlyfive.val();
                leagueFiveArr = leagueFiveArr ? leagueFiveArr.split(",") : [];
                _.each(leagueFiveArr, function (key) {
                    leagueFiveHash[key] = true
                });

                this.$selmatch = this.$(".selmatch");

                var _matchClose = _.bind(this.matchClose, this);

                $(document).on("click", _matchClose);

                this.listItemView = {};

                this.$step1stat = this.$("#step1stat");
                this.$step1num = this.$(".step1num");
                this.$listTips = this.$("#listTips");
                this.$listArea = this.$("#listArea");

                this.$listbox = this.$("#listbox");


                this.$chuan_box = this.$("#chuan_box");
                this.$chuan_tips = this.$("#chuan_tips");
                this.$chuan_n1 = this.$("#chuan_n_1");
                this.$chuan_n1_list = this.$("#chuan_n_1 label");
                this.$chuan_nn = this.$("#chuan_n_n");
                this.$chuan_nn_list = this.$("#chuan_n_n label");


                this.$count = this.$("#count");
                this.$countMoney = this.$("#countMoney");
                this.$countRange = this.$("#countRange");

                this.$leagueStat = this.$(".leagueStat");

                this.$mul = this.$("#mul");
                this.$footerfix = this.$("#footerfix");//add by zn on 6.12
                this.cTemplate = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];

                this.$only_sel = this.$(".only_sel");

                this.listenTo(this.models, "add", this.addList);
                this.listenTo(this.models, "remove", this.removeList);
                this.listenTo(this.models, "add", this.render);
                this.listenTo(this.models, "remove", this.render);
                this.listenTo(this.models, "add", this.renderChuan);
                this.listenTo(this.models, "remove", this.renderChuan);

                //更新sp
                up_sp(viewArr, this.gameid)
            },
            stopPropagation: function (e) {
                e.stopPropagation()
            },
            plus: function () {
                var $this = this.$mul;
                var v = $this.val();
                if (v < 9999) {
                    $this.val(++v);
                    this.count()
                }
            },
            minus: function () {
                var $this = this.$mul;
                var v = $this.val();
                if (v > 1) {
                    $this.val(--v);
                    this.count()
                }
            },
            checkNumber: function (e) {
                var keyCode = e.which;
                var $this = $(e.currentTarget);
                var v = $this.val().replace(/[^0-9]/g, "");
                if (!((keyCode <= 57 && keyCode >= 48) || (keyCode <= 105 && keyCode >= 96) || keyCode == 39 || keyCode == 37 || keyCode == 8 || keyCode == 46 || keyCode == 229)) {
                    $this.val(v == 0 ? 1 : v);
                    e.preventDefault()
                } else {
                    if (v <= 0 && v != "") {
                        $this.val(1)
                    } else if (v > 99999) {
                        $this.val(99999)
                    }
                }
                this.count()
            },
            scrollBar: function (ele) {
                var $listArea = this.$listArea;
                var height = $listArea.height();
                if (height > 0) {
                    var data = this.$("#detailScroll").data("jsp");
                    if (height >= 300) {
                        if (!data) {
                            this.$("#detailScroll").css("height", 304).jScrollPane(/*{autoReinitialise: true}*/)
                        } else {
                            data.reinitialise();
                            ele && data.scrollToElement(ele, true)
                        }
                    } else {
                        if (data) {
                            data.destroy();
                            this.$("#detailScroll").css("height", "auto")
                        }
                    }
                }
            },
            addList: function (model) {

                this.$listTips[this.models.length ? "hide" : "show"]();
                var matchid = +model.get("matchid");
                var $listArea = this.$listArea;
                var $parent = $("#list_" + matchid);
                var $list = $listArea.find(".co");
                var $index_el = null;
                var $index_el2 = null;
                if ($parent.length == 0) {
                    $parent = $(this.listTemplate(this.matchHash[matchid]));
                    // 查找插入节点
                    _.some($list, function (el) {
                        var $this = $(el);
                        var id = +$this.attr("id").replace("list_", "");
                        if (id > matchid) {
                            $index_el = $this;
                            return true;
                        }
                    });

                    if ($index_el) {
                        $index_el.before($parent);
                    } else {
                        $listArea.append($parent);
                    }

                }
                var view = new listItemView({model: model});


                // 查找插入节点
                var $list2 = $parent.find(".b");
                _.some($list2.find("em"), function (el) {
                    var $this = $(el);
                    var index = +$this.attr("index");

                    if (index > +model.get("index")) {

                        $index_el2 = $this;
                        return true;
                    }
                });
                if ($index_el2) {
                    $index_el2.before(view.render().el);
                } else {
                    $list2.append(view.render().el);
                }
                this.listItemView[model.get("code") + "_" + matchid] = view;
                this.scrollBar(view.$el.parents(".co"));
            },
            removeList: function (model) {
                var matchid = model.get("matchid");
                var len = this.models.where({matchid: matchid});
                var view = this.listItemView[model.get("code") + "_" + matchid];
                if (len == 0) {
                    view.$el.parents("li").remove();
                    this.$listTips[this.models.length ? "hide" : "show"]()
                }
                view.destroy();
                this.scrollBar();
            },
            removeListItem: function (e) {
                var $this = $(e.currentTarget);
                var matchid = $this.attr("matchid");
                $this.parents("li").remove();
                _.invoke(this.models.where({matchid: matchid}), "destroy")
            },
            openList: function (e) {
                var st = this.$listbox.data("st");
                this.$listbox[st ? "hide" : "show"]();
                this.$listbox.data("st", !st);
                $(e.currentTarget)[st ? "removeClass" : "addClass"]("cur")
                    .html(st ? '展开查看<i class="arrud"></i>' : '收起<i class="arrud"></i>');
                if (!st) {
                    this.scrollBar()
                }
                return false
            },
            matchSel: function (e) {
                e.stopPropagation();
                var $this = $(e.currentTarget);
                var st = $this.data("st");
                $(e.currentTarget)[st ? "removeClass" : "addClass"]("openmatch");
                $this.data("st", !st)
            },
            matchClose: function () {
                var $selmatch = this.$selmatch;
                if ($selmatch.hasClass("openmatch")) {
                    $selmatch.removeClass("openmatch").data("st", false);
                }
            },
            timeToggle: function (e) {
                var $this = $(e.currentTarget);
                var time = $this.attr("data");
                var st = $this.data("st");
                _.each(this.timeHash[time], function (el) {
                    if (st) {
                        if (!el.data("leagueShowSt")) {
                            el.show();
                        }
                        el.data("timeShowSt", 0)
                    } else {
                        el.data("timeShowSt", 1).hide();
                    }
                });
                $this.html(st ? "隐藏<i></i>" : "显示<i></i>");
                $this.data("st", !st);
                this.LeagueStat();
                return false
            },
            leagueToggle: function () {
                var leagueHash = this.leagueHash;
                // var leagueFiveHash = this.leagueFiveHash;
                //var $onlyfive = this.$onlyfive;
                // var fivedone = 0;
                // var i = 0;
                _.each(this.leagueCheckedHash, function (el, key) {
                    var checked = el.prop("checked");
                    // if (checked) {
                    //     // i += 1;
                    //     if (leagueFiveHash[el.attr("league")]) {
                    //         fivedone += 1;
                    //     }
                    // }
                    _.each(leagueHash[key], function (el) {
                        if (checked) {
                            if (!el.data("timeShowSt")) {
                                el.show();
                            }
                            el.data("leagueShowSt", 0)
                        } else {
                            el.data("leagueShowSt", 1).hide();
                        }
                    });
                });

                // if (!(fivedone == 5 && i == 5)) {
                //     $onlyfive.prop("checked", false)
                // }
                this.LeagueStat();
            },
            leagueFive: function (e) {
                if ($(e.currentTarget).prop("checked")) {
                    var leagueFive = this.leagueFiveHash;
                    _.each(this.leagueCheckedHash, function (el) {
                        el.prop("checked", leagueFive[el.val()] ? true : false)
                    });
                    this.leagueToggle()
                } else {
                    this.showAllLeague()
                }
            },
            leagueInverse: function () {
                var leagueHash = this.leagueHash;
                _.each(this.leagueCheckedHash, function (el, key) {
                    var checked = el.prop("checked");
                    _.each(leagueHash[key], function (el) {
                        if (checked) {
                            el.hide().data("leagueShowSt", 1)
                        } else {
                            if (!el.data("timeShowSt")) {
                                el.show();
                            }
                            el.data("leagueShowSt", 0)
                        }
                    });
                    el.prop("checked", !checked)
                });
                this.LeagueStat();
            },
            showAllLeague: function () {
                var timeBtnHash = this.timeBtnHash;
                var done = {};
                _.each(this.matchHash, function (obj) {
                    obj.el.data({
                        "timeShowSt": 0,
                        "leagueShowSt": 0
                    }).show();
                    var time = $.parseJSON(obj.el.attr("data"))["time"];
                    if (done[time]) return;
                    timeBtnHash[time].data("st", 0).html("隐藏<i></i>");
                    done[time] = 1;
                });

                _.each(this.leagueCheckedHash, function (obj) {
                    obj.prop("checked", true);
                });
                this.$onlyfive.prop("checked", false);
                this.$only_sel.prop("checked", false);
                this.LeagueStat()
            },
            LeagueStat: function () {
                var n = this.$(".jc_row:hidden").length;
                this.$leagueStat.html("已隐藏" + n + "场")[n == 0 ? "hide" : "show"]()
            },
            matchOpen: function (e) {
                var $this = $(e.currentTarget);
                var matchid = $this.attr("matchid");
                var st = $this.data("st");
                var $tr = $this.data("tr");
                var $td = $this.data("td");
                if (!$tr) {
                    $tr = $this.parents("tr").nextAll("tr");
                    $this.data("tr", $tr); // 缓存
                }
                if (!$td) {
                    $td = $this.parent();
                    $this.data("td", $td); // 缓存
                }
                $this.parents("tr").nextAll("tr");
                $tr[st ? "hide" : "show"]();
                $this.data("st", !st);

                var n = this.models.where({matchid: matchid, hide: 1}).length;
                if (st) {
                    if (n > 0) {
                        $this.addClass("opencur")
                    } else {
                        $this.removeClass("opencur");
                        $this.html('<span>展开</span><i class="arrud"></i>')
                    }
                    $td.removeClass("kai")
                } else {
                    $this.removeClass("opencur");
                    if (n == 0) {
                        $this.html('<span>收起</span><i class="arrud"></i>')
                    }
                    $td.addClass("kai")
                }

                return false
            },
            allIn: function (e) {
                var $this = $(e.currentTarget);
                var type = $this.attr("data");
                var matchid = $this.attr("matchid");
                var st = $this.data("st");
                _.each(this.viewArr, function (view) {
                    var m = view.model;
                    if (m.get("type") == type && m.get("matchid") == matchid) {
                        m.set("done", st ? 1 : 0, {silent: true});
                        m.trigger("change", {allin: 1})
                    }
                });
                $this.data("st", !st);
                $this.html(st ? "[全包]" : "[全清]");
                this.render();
                return false
            },
            getDmlen: function (arr) {
                return _.reduce(arr, function (num, input) {
                    return ($(input).prop("checked") ? 1 : 0 ) + num
                }, 0);
            },
            renderChuan: function () {
                var max = 7;
                if (this.models.findWhere({type: "bf"})) {
                    max = 3;
                } else if (this.models.findWhere({type: "bqc"})) {
                    max = 3;
                } else if (this.models.findWhere({type: "zjq"})) {
                    max = 5;
                }

                var gameid = this.gameid;

                if (gameid == "jczqbfp"){
                    max = 4;
                }

                var count = this.models.count();
                var length = count[1].length;
                // 更新串关
                var $chuan_tips = this.$chuan_tips;
                var $chuan_n1 = this.$chuan_n1;
                var $chuan_n1_list = this.$chuan_n1_list;

                var $chuan_nn_list = this.$chuan_nn_list;
                // 自由过关
                var n = gameid == "jczqbfp" ? 1 : 2;
                var n1 = gameid == "jczqbfp" ? length : length - 1;
                if (length < n) {
                    $chuan_tips.show();
                    $chuan_n1.hide();
                    $chuan_n1_list.find("input").prop("checked", false);
                } else {
                    $chuan_tips.hide();
                    $chuan_n1.show();
                    $chuan_n1_list.each(function (i) {
                        var $this = $(this);
                        if (i < n1 && i < max) {
                            $this.show()
                        } else {
                            $this.hide().find("input").prop("checked", false)
                        }
                    });
                }
                // 组合过关
                if (length < 3) {
                    $chuan_nn_list.hide().find("input").prop("checked", false);
                } else {
                    $chuan_nn_list.each(function () {
                        var $this = $(this);
                        var $input = $this.find("input");
                        var i = parseInt($input.attr("n"));
                        if (i < (length + 1) && i < max + n) {
                            $this.show()
                        } else {
                            $this.hide();
                            $input.prop("checked", false)
                        }
                    })
                }
            },
            getChuan: function () {
                var arr = [];
                var $list = this.$chuan_nn_list.add(this.$chuan_n1_list);
                $list
                    //.filter(":visible")
                    .find("input:checked")
                    .each(function () {
                        var v = $(this).attr("n");
                        v = v == "1串1" ? "单关" : v;
                        arr.push(v)
                    });
                return arr;
            },
            count: function () {
                var count = this.models.count()[1];
                var chuan = this.getChuan();
                var mul = +this.$mul.val();
                var gameid = this.gameid;
                mul = mul ? mul : 1;
                if (chuan.length == 0 || mul <= 0) {
                    this.$count.html("0注");
                    this.$countMoney.html("0元");
                    this.$countRange.html("0元");
                    return
                }

                var h = {};
                this.models.each(function (model, index) {
                    var matchid = model.get("matchid");
                    h[matchid] = h[matchid] || [];
                    var token = model.get("token");
                    token = token ? token : 0;
                    h[matchid].push(gameid == "jczqmixp" ? [+model.get("index"), +model.get("sp"), +token] : +model.get("sp"))
                });

                var arr = [];
                _.each(h, function (v) {
                    arr.push(_.sortBy(v));
                });

                var dmArr = [];
                var dmHash = this.dmHash;

                _.each(count, function (matchid, index) {
                    if ($(dmHash[matchid]).prop("checked")) {
                        dmArr.push(index)
                    }
                });

                var obj = null;
                var awardRange = null;
                if (gameid == "jczqmixp") {
                    obj = C.hhcalc.calculateBet(arr, dmArr, chuan, this.cTemplate);
                } else {
                    awardRange = C1.JCZQ_Calc.calExtreme(true, arr, dmArr, chuan, this.cTemplate);
                    obj = {
                        count: C1.JCZQ_Calc.calCount(true, arr, dmArr, chuan, this.cTemplate),
                        min: awardRange.min,
                        max: awardRange.max
                    }
                }
                this.$count.html(obj.count + "注");
                this.$countMoney.html(tools.FloatMul(tools.FloatMul(obj.count, 2), mul) + "元");
                // this.$countRange.html(tools.FloatMul(obj.min, mul) + "-" + tools.FloatMul(obj.max, mul) + "元")
                this.$countRange.html(tools.FloatMul(tools.FloatMul(obj.max, mul), 2) + "元")
            },
            only_sel: function () {
                var $this = this.$only_sel;
                var checked = $this.prop("checked");
                if (checked) {
                    var matchids = this.models.count()[2];
                    _.each(this.matchHash, function (obj, matchid) {
                        obj.el[matchids[matchid] ? "show" : "hide"]()
                    });
                    this.LeagueStat()
                } else {
                    this.showAllLeague()
                }
            },
            freedo: function () {
                var length = this.models.count()[1].length;
                if (length < 3) return false;
                var $this = $(this);
                var $chuan_box = this.$chuan_box;
                var $chuan_nn = this.$chuan_nn;
                var st = $this.data("st");
                if (st) {
                    $chuan_box.removeClass('ggopen');
                    $chuan_nn.hide()
                } else {
                    $chuan_box.addClass('ggopen');
                    $chuan_nn.show()
                }
                $this.data("st", !st);
                return false
            },
            chuan_n1: function () {
                this.$chuan_nn_list.find("input").prop("checked", false);
                this.render()
            },
            chuan_nn: function () {
                _.each(this.dmHash, function (el) {
                    $(el).prop({
                        "disabled": true,
                        "checked": false
                    });
                });
                this.$(".list_dm").prop({
                    "disabled": true,
                    "checked": false
                });
                this.$chuan_n1_list.find("input").prop("checked", false);
                this.render()
            },
            render: function (e, models, opts) {
                if (opts && opts.allin) {
                    return
                }

                var count = this.models.count();
                var length = count[1].length;
                var chuan_n = parseInt(this.getChuan()[0]);
                var n = chuan_n ? chuan_n : 8;
                var len = length > n ? n : length;
                var dm_len = this.getDmlen(this.dmHash);
                var $list_dm = this.$(".list_dm");
                var matchid;

                if(length<1){
                	this.$footerfix.hide();
	        		$(window).trigger("scroll")
	        	}else{
	        		this.$footerfix.show();
	        		$(window).trigger("scroll")
	        	}
                //  同步checkbox
                if (e && e.currentTarget) {
                    var $thisInput = $(e.currentTarget);
                    matchid = $thisInput.attr("matchid");
                    if ($thisInput.hasClass("dm")) {
                        $list_dm.filter("[matchid=" + matchid + "]").prop("checked", $thisInput.prop("checked"))
                    } else {
                        $(this.dmHash[matchid]).prop("checked", $thisInput.prop("checked"));
                        dm_len = this.getDmlen($list_dm)
                    }
                }


                // 更新当前checkbox状态
                if (e && !e.currentTarget) {
                    matchid = e.get("matchid");
                    if (!count[2][matchid]) {
                        var $input = $(this.dmHash[matchid]);
                        var checked = $input.prop("checked");
                        $input.prop({
                            "disabled": true,
                            "checked": false
                        });
                        checked && (dm_len = dm_len > 0 ? dm_len - 1 : dm_len)
                    }
                }

                // 更新其他checkbox状态
                _.each(this.dmHash, function (el, matchid) {
                    var $input1 = $(el);
                    var $input2 = $list_dm.filter("[matchid=" + matchid + "]");
                    var $input = $input1.add($input2);
                    var checked = $input.prop("checked");

                    if (count[2][matchid] && len > 1 && chuan_n != length) {
                        if (dm_len >= len) {
                            $input.prop({
                                "disabled": false,
                                "checked": false
                            })
                        } else {
                            if ($("#c_n_1").is(":checked")) {
                                $input.prop({
                                    "disabled": true,
                                    "checked": false
                                })
                            } else {
                                if (dm_len == len - 1 && !checked) {
                                    $input.prop("disabled", true)
                                } else {
                                    if (!this.$chuan_nn_list.find("input").is(":checked")) {
                                        $input.prop("disabled", false)
                                    }
                                }
                            }
                        }
                    } else {
                        $input.prop({
                            "disabled": true,
                            "checked": false
                        });
                    }

                }, this);

                // 更新统计
                this.$step1stat.html('已选<b class="red">' + length + '</b>场比赛，' + count[0].length + '项投注');
                this.$step1num.html(length);
                this.count();
            },
            submit: function (e) {
                var _this = this;
                e.preventDefault();

                var count = this.models.count()[1];
                var chuan = this.getChuan();
                var n = this.gameid == "jczqbfp" ? 1 : 2;
                if (count.length < n) {
                    Alert.show(function () {
                        this.$(".txt-center").html("请至少选择" + n + "场比赛");
                    });
                    return
                }

                if (chuan.length == 0) {
                    Alert.show(function () {
                        this.$(".txt-center").html("请选择过关方式");
                    });
                    return
                }


                tools.isLogin(function () {
                    // 已登陆
                    pay.call(_this, "hhgg_spf_rqspf_bf")
                }, function () {
                    var dataObj = ls.call(_this, "hhgg_spf_rqspf_bf");
                    if (!!dataObj.data && tools.getBytes(dataObj.data) <= 30 * 1024) {
                        dataObj.timestamp = +new Date;
                        var data = JSON.stringify(dataObj);
                        $.jStorage.set("betsData", data);
                        //$.jStorage.setTTL("betsData",60000);
                    }
                    // 未登录
                    art_login.show(function () {
                        this.successCallfn = function () {
                            this.hide();
                            pay.call(_this, "hhgg_spf_rqspf_bf")
                        }
                    })
                })
            }
        });

        var matchColl = new MatchColl;
        var appView = new AppView;
        toggle_matchTime();
        theadfix();
        footerfix();


        // 默认投注
        (function () {
            var outerData = $("#defaultBets").val();
            var fillData = function (data) {
                var a = data.split("#");
                var arr = a[0].split("|");
                var list = arr[0].split(" ");
                var checkedArr = [];
                // 倍数
                appView.$mul.val(a[1]);
                // 场次
                _.each(list, function (str) {
                    var arr = str.split(":");
                    var matchid = arr[0];
                    var codeArr = arr[1].split(".");
                    arr[2] == 1 && checkedArr.push(matchid);
                    _.each(appView.viewHash[matchid], function (view) {
                        var model = view.model;
                        if (_.indexOf(codeArr, model.get("code")) > -1) {
                            model.set("done", false)
                        }
                    })
                });

                // 胆码
                _.each(checkedArr, function (macthid) {
                    if (appView.dmHash[macthid]) {
                        $(appView.dmHash[macthid]).trigger("click")
                    }
                });

                // 过关
                _.each(arr[1].split(","), function (str) {
                    var n = str.split("_")[1];
                    var selector = n == 1 ? "$chuan_n1_list" : "$chuan_nn_list";
                    appView[selector]
                        .find("input[n='" + str.replace("_", "串") + "']")
                        .prop("checked", true)
                });

                appView.render()
            };
            tools.isLogin(function (userId) {
                if (userId.indexOf("qq.sohu.com") > -1 && $.jStorage.index().length > 0) {
                    var dataObj = $.jStorage.get("betsData");
                    dataObj = JSON.parse(dataObj);
                    if (Math.abs(+new Date - ( +dataObj.timestamp)) > 60 * 1000) {
                        $.jStorage.flush();
                    } else if (dataObj && dataObj.gameId === "hhgg_spf_rqspf_bf") {
                        fillData(dataObj.data);
                    }
                } else if (outerData) {
                    fillData(outerData);
                }
            }, function () { //login not yet
                $.jStorage.flush();
                if (outerData) {
                    fillData(outerData);
                }
            });
        }());

    }
});