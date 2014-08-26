define([
    "text!zc/ball_item.tpl",
    "lib/tools",
    "pay/pay",
    "update/tradfootball",
    "update/stop_game"
],function(ball_item, tools, pay, updatePage, stop_game){

    if ($("#zc_sfc_index").length == 0 || stop_game() == 0) return function(){};

    return function(){
        // 更新页面数据
        updatePage();

        var Model = Backbone.Model.extend();

        var Models =Backbone.Collection.extend({
            rown:function(row){
                return this.where({
                    row:row,
                    done:true
                })
            },
            done:function(){
                return this.where({
                    done:true
                })
            }
        });

        var View =  Backbone.View.extend({
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

                if (this.models.rown(this.model.get("row")).length == 0){
                    this.model.get("$sel").html("包").data("st",false)
                }else if (this.models.rown(this.model.get("row")).length == 3){
                    this.model.get("$sel").html("清").data("st",true)
                }
                //
                return false
            }
        });

        var AppView = Backbone.View.extend({
            el:"#zc_sfc_index",
            events:{
                "click a[data-row]" : "toggleAll",
                "click .clear" : "clear",
                "click .selSubmitBtn" : "submit"
            },
            initialize:function(){
                this.$form = this.$("form");
                this.$input = this.$("form input");


                // tr hover
                this.$el.on({
                    "mouseenter":function(){
                        $(this).addClass('hover')
                    },
                    "mouseleave":function(){
                        $(this).removeClass('hover')
                    }
                },".cp-area tr");
                this.models = models;
                var hashView = this.hashView = {};
                var data;
                var hash;
                var _this = this;
                this.$("a[data-model]").each(function(){
                    var $this = $(this);
                    data = $.parseJSON($this.attr("data-model"));
                    data.done = false;
                    data.$sel = _this.$("a[data-row="+data.row+"]"); // 引用 包、清按钮
                    (hashView[data.row] || (hashView[data.row] = {}))[data.number] = (new View({
                        model:new Model(data),
                        el:$this
                    }));
                });
            },
            toggleAll:function(e){
                var $this = $(e.currentTarget);
                var row = $this.attr("data-row");
                var st = !!$this.data("st"); // false true
                _.each(this.hashView[row],function(view){
                    view.model.set({
                        "done":st,
                        "d":+new Date // 触发cheng事件
                    })
                });
                $this.data("st",!st);
                return false
            },
            clear:function(){
                var arr = this.models.done();
                _.invoke(arr, "destroy");
                return false
            },
            submit:function(){
                var list = this.models.done();

                var number = [];
                var arr;
                var arr1;
                var count = 1;
                for (var i = 1; i<15; i++){
                    arr = this.models.where({row:i});
                    var len = arr.length;
                    if (len == 0){
                        alert("请每场至少选取一项进行投注")
                        // Alert.show(function(){
                        //     this.$(".txt-center").html("请每场至少选取一项进行投注");
                        // });
                        return false
                    };
                    arr1 = [];
                    for (var j=0;j<len;j++){
                        arr1.push(arr[j].get("number"));
                    }
                    count *= len;
                    number.push(arr1.join(""))
                };
                // this.lists.add(
                //     {
                //         number: number,
                //         done: true,
                //         count: count
                //     }
                // );
                this.$input.val(number.join(" ")+"#1"); 
                this.$form.trigger("submit");
                //this.clear()
                return false
            }
        });

        var models = new Models;
        var appView = new AppView;
    }

})