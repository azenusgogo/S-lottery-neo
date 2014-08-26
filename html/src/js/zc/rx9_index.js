define([
    "text!zc/ball_item.tpl",
    "lib/tools",
    "pay/pay",
    "update/tradfootball",
    "update/stop_game"
], function (ball_item, tools, pay, updatePage, stop_game) {

    if ($("#zc_rx9_index").length == 0 || stop_game() == 0) return function () {
    };
    return function () {
        // 更新页面数据
        updatePage();

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
            el: "#zc_rx9_index",
            events: {
                "click a[data-row]": "toggleAll",
                "click input[data-row]": "selChecked",
                "click .clear": "clear",
                "click .rnd": "rnd",
                "click .selSubmitBtn": "submit"
            },
            initialize: function () {
                this.$form = this.$("form");
                this.$input = this.$("form input");

                // tr hover
                this.$el.on({
                    "mouseenter": function () {
                        $(this).addClass('hover')
                    },
                    "mouseleave": function () {
                        $(this).removeClass('hover')
                    }
                }, ".cp-area tr");
                this.models = models;
                this.$rnd = this.$(".rnd");
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
                    alert("最多只能选8个胆")
                    return false
                }

                var $this = $(e.currentTarget);
                var arr = this.models.rown(+$this.attr("data-row"));
                var checked = $this.prop("checked") ? true : false;
                _.each(arr, function (model) {
                    model.set("checked", checked, {silent: true})
                });


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
                if (rown > 0 && rown < 9) {
                    var hash = stats.rows;
                    var arr = [];
                    _.each(this.hashView, function (view, key) {
                        if (!hash[key]) arr.push(view)
                    });
                    arr = _.sample(arr, 9 - rown);
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
                if (rown > 0 && rown < 9) {
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
                var n;
                if (models.stats().rown < 9) {
                    alert("您选择的号码是非标准投注号码")
                    return false
                }
                for (var i = 1; i < 15; i++) {
                    var checked = false;
                    var arr2 = models.where({row: i});
                    n = _.map(arr2, function (model) {
                        checked = model.get("checked");
                        return +model.get("number")
                    });

                    number.push((n.length == 0 ? ["-"] : n).join("")+(checked?"$":""))
                }


                // 计算注数

                // var dm = this.models.where({checked:true});
                // var tm = this.models.where({checked:false});

                this.$input.val(number.join(" ")+"#1"); 
                this.$form.trigger("submit");
                this.clear()
                return false
            }
        });

        var models = new Models;
        var appView = new AppView;
    }

})