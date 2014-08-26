define([
    "text!jczq/zjq/zjq_list_item.tpl",
    "jczq/up_sp",
    "jczq/topView",
    "lib/tools",
	"login/art_login",
	"lib/alert",
	"pay/pay",
    "update/stop_game",
    "lib/calc",
	"lib/ls",
	"lib/jstorage",
	"lib/jquery.mousewheel",
    "lib/jquery.jscrollpane",
	"lib/json2"
],function(zjq_list_item,updateSP,topView,tools,art_login,dialogAlert,pay,stop_game,C,ls,jstorage){
	if ($("#jczqzjq").length == 0) return function () {
    };
    // 更新页面数据
    //updatePage();
    return function(){
    	jstorage();
    	 
        var isLogin = tools.isLogin;

        var Alert = dialogAlert("提示"); 
        
        var _nickNameHash = {"11400": "0球", "11401": "1球", "11402": "2球", "11403": "3球", "11404": "4球", "11405": "5球", "11406": "6球", "11407": "7球+"};
                    
        topView();
     /* %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% */
        var Model = Backbone.Model.extend({defaults: {
            done: false
        }});
        var Models = Backbone.Collection.extend({
            rown: function (matchid) {
                return this.where({
                	matchid: matchid,
                    done: true
                })
            },
            done: function () {
                return this.where({
                    done: true
                })
            },        
            count: function () {
                var arr1 = this.pluck("matchid");
                var arr2 = _.uniq(arr1);
                var obj = _.object(arr2, arr2);
                return [arr1, arr2, obj]
            }
          
        });
        var View = Backbone.View.extend({
            events: {
                "click": "render"
            },
            initialize: function () {
                this.models = models; // 保存集合
                this.listenTo(this.model, "change", this.render);
                this.listenTo(this.model, 'destroy', this.render);
            },
            render: function () {
                var done = this.model.get("done"); // 选中标识
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
                this.models[done ? "remove" : "add"](this.model);
                this.model.set("done", !done, {silent: true});
                          
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
                var code = this.model.get("code");           
                this.$el.addClass("f jcem").attr("index", this.model.get("index")).html(_nickNameHash[code]);
                return this
            }
        });
        var AppView = Backbone.View.extend({
            el: "#jczqzjq",
            events: {
                "click input[matchid]": "render", //设胆    
                "click .toggleBets":"toggleBets",      //展开/隐藏投注方案
                "click .b1":"removeMatch",        //删除已选比赛
                "click .only_sel":"onlySel",      //只看已选
                "click input[betsmatchid]":"setDan", //投注篮设胆
                "click .combopass":"toggleCombopass", //自由过关/组合过关
                "click input[passtype]":"updatePassType",//更新过关方式
                "keyup #mul": "checkNumber",
                "keydown #mul": "checkNumber",
                "blur #mul": "checkNumber",
                "click #plus": "plus",
                "click #minus": "minus",
                "click .submit":"submit"             //立即投注
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
                this.calc();
            },
            initialize: function () {
                // tr hover
               /* this.$el.on({
                    "mouseenter": function () {
                        $(this).addClass('hover')
                    },
                    "mouseleave": function () {
                        $(this).removeClass('hover')
                    }
                }, ".jcarea tr[matchtype]");*/
                this.models = models;
                var hashView = this.hashView = [];
                
                var dmArr = this.dmArr = {};
                this.$("input[matchid]").each(function () {
                    dmArr[$(this).attr("matchid")] = this
                });
                
                var betsDmArr = this.betsDmArr = {};           
                
                var matchHash = this.matchHash = {};
                this.$("input[matchid]").each(function () {
                    var data = {};
                        data.matchid = $(this).attr("matchid");
                        data.time = $(this).attr("time");
                        data.seq = $(this).attr("seq");
                        data.versus = $(this).attr("versus");
                    matchHash[data.matchid] = data
                });
                
                this.$step1stat = this.$("#step1stat");
	            this.$step1num = this.$(".step1num");
	            this.$showBets = this.$(".show");
	            this.listItemView = {};
	            this.$listArea = this.$("#listArea");
	            this.$mul = this.$("#mul");
	            this.$count = this.$("#count");
	            this.$countMoney = this.$("#countMoney");
	            this.$countRange = this.$("#countRange");
	            this.$chuan_tips = this.$("#chuantips");
	            this.$free = this.$("#free");
	            this.gameId = $("#gameId").val();
	            this.$hidenInfo = this.$(".hidenInfo");
	            this.$matchbox = this.$(".matchbox");
	            this.$listbox = this.$("#listbox");
	            this.$footerfix = this.$("#footerfix");
	            this.$toggleBetsBtn  = this.$(".toggleBets");
                
                this.$("a[data-model]").each(function () {
                    var _this = this;
                    var data = $.parseJSON($(_this).attr("data-model"));
    	                data.done = false;
    	                data.checked = false;
    	                hashView.push(new View({
                        el: _this,
                        model: new Model(data)
                    }))
                });
                this.listenTo(this.models, "add", this.render);
                this.listenTo(this.models, "remove", this.render);
                this.listenTo(this.models, "add", this.addList);
                this.listenTo(this.models, "remove", this.removeList);
                updateSP(hashView,"jczqzjqp");
            },                    
            toggleBets:function(e){
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
            listTemplate: _.template(zjq_list_item),
            plus: function () {
                var $this = this.$mul;
                var v = $this.val();
                if (v < 9999) {
                    $this.val(++v);
                    this.calc()
                }
            },
            minus: function () {
                var $this = this.$mul;
                var v = $this.val();
                if (v > 1) {
                    $this.val(--v);
                    this.calc()
                }
            },
            calc:function(){
            	var  count = this.models.count(),me = this,models=this.models,types=[];
            	var mul = +this.$mul.val();
            	var isVisibile = this.$(".s2 label").hasClass("visibile");
            	var $inputs = isVisibile && this.$(".s2 label");
                mul = mul ? mul : 1;
            	//以下是计算
                if( isVisibile && $inputs.find("input[passtype]:checked").length>0 && mul>0){
                	var matches=[],dans=[];
                	_.each(count[1],function(matchid,k){
                		var matchsp=[];
                		_.each(models.where({matchid:matchid}),function(model,i){
                			matchsp[i] = +model.get("sp");
                		});
                		matchsp = _.sortBy(matchsp);
                		matches[k] = matchsp;
                		if(me.$("input[betsmatchid="+matchid+"]").prop("checked")==true){
                			dans.push(k);
                		}
                	});
                	$.each($inputs.find("input[passtype]:checked"),function(i,v){
                		types[i] = $(v).attr("passtype");
                	})
                	var count = C.JCZQ_Calc.calCount(true, matches, dans, types, [0,0,0,0,0,0,0,0]);
                	award = C.JCZQ_Calc.calExtreme(true, matches, dans, types, [0,0,0,0,0,0,0,0]);
                	this.$count.html(count+"注");
                	this.$countMoney.html(tools.FloatMul(tools.FloatMul(count, 2), mul)+"元");
                	this.$countRange.html(tools.FloatMul(award.max*2, mul)+"元");
                	
                }else{
                	this.$count.html("0注");
                	this.$countMoney.html("0元");
                	this.$countRange.html("0元");
                }
            },
            addList: function (model) {           
                var matchid = model.get("matchid");
                var $listArea = this.$listArea;
                var $parent = $("#list_" + matchid);
                var $list = $listArea.find(".co");
                var $index_el = null;
                var $index_el2 = null;
                if ($parent.length==0) {
                    $parent = $(this.listTemplate(this.matchHash[matchid]));             
                    _.some($list, function (el) { // 查找插入节点
                        var $this = $(el);
                        var id = $this.attr("id").replace("list_", "");
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
                    $listArea.append($parent);
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
                if(this.$toggleBetsBtn.hasClass("cur")){
        			this.betsAnimation(model,view.$el);
        		}
                this.listItemView[matchid + "_" + model.get("code")] = view;
                this.calc();
            },
            betsAnimation:function(model,$em){
	        	var matchid = model.get("matchid"),
	        	    code    = model.get("code"),
	        	    index   = model.get("index"),
	        	    me      = this,
	        	    $betsOption;
	        	    _.some(this.$("tr[matchid="+matchid+"]").find("a[data-model]"),function(v){
	        	    	var data = $.parseJSON($(v).attr("data-model"));
	        	    	if(data["code"] == code){
	        	    		$betsOption = $(v);
	        	    		return true;
	        	    	}
	        	    });
	        	    var $target = this.$listbox.find("#list_"+matchid).find("em[index="+index+"]");
	        	    var clone = $('<em class="'
							+ 	$em.attr("class")
							+ '"style="position:absolute;left:-9999px;top:-9999px;z-index:999;">'
							+ $em.html() + "</em>");	
	        	    
	        	    $target.css("visibility","hidden");
	        	    $("body").append(clone);
	        	    tools.moveDom(clone,$betsOption,$target,100,function(){
	        	    	$target.css("visibility","visible");	        	    	
	        	        me.scrollBar($target.closest(".co"));
	        	    	clone.remove();
	        	    })
	        },
	        scrollBar: function (elem) {
	            var $listArea = this.$listArea;
	            var height = $listArea.height();
	            var t = "";
	            if (height > 0) {
	                var data = this.$("#detailScroll").data("jsp");
	                if (height >= 300) {
	                    if (!data) {
	                        this.$("#detailScroll").css("height", 304).jScrollPane();//{autoReinitialise: true}                      	                        
	                    }else{
	                    	data.reinitialise();
	                    	elem && data.scrollToElement(elem,false);	                    	
	                    }
	                } else {
	                    if (data) {
	                        data.destroy();
	                        this.$("#detailScroll").css("height", "auto")
	                    }
	                }
	            }
	        },
            removeList: function (model) {
            	var matchid = model.get("matchid");
                this.listItemView[model.get("matchid")+ "_" + model.get("code")].destroy();
                if(this.models.where({matchid:+matchid}).length==0){
                	this.$el.find("#list_"+matchid).remove();
                }
                this.scrollBar();
                return false;
            },
            removeMatch:function(e){
            	var $this = $(e.currentTarget),me = this;
            	var $parent = $this.closest(".co");
            	var matchid = $parent.attr("id").split("_")[1];
            	_.invoke(this.models.where({matchid:+matchid}),'destroy');
            	$parent.remove();
            	return false;
            },
            onlySel:function(e){
	        	var $this = $(e.currentTarget),me = this;
	        	if(this.models.count()[1].length==0) return;
	        	if($this.prop("checked")){
	        		me.$el.find("tr[matchid]").hide();
	        		_.each(this.models.count()[1],function(matchid){        			
	            		me.$el.find("tr[matchid="+matchid+"]").show();
	            	});
	        		var hiddenum = me.$el.find("tr[matchid]:hidden").length;
	        		if(hiddenum>0){
	        			this.$hidenInfo.html('[已隐藏<i class="hidenMatchNum">'+hiddenum+'</i>场]').show();
	        		}else{
	        			this.$hidenInfo.html('[已隐藏<i class="hidenMatchNum">0</i>场]').hide();
	        		}
	        	}else{
	        		me.$el.find("tr[matchid]").show();
	        		me.$el.find("tr[flag=1]").each(function(i,v){
	        			$(v).hide();
	        		});
	        		var hiddenum = me.$el.find("tr[matchid]:hidden").length;
	        		if(hiddenum>0){
	        			this.$hidenInfo.html('[已隐藏<i class="hidenMatchNum">'+hiddenum+'</i>场]').show();
	        		}else{
	        			this.$hidenInfo.html('[已隐藏<i class="hidenMatchNum">0</i>场]').hide();
	        		}
	        		
	        	}
	        	
	        },
            toggleCombopass:function(e){
	        	var $this = $(e.currentTarget),me = this;
	        	    if(me.models.count()[1].length<3){
	        	    	return false;
	        	    }
	        	    if(!$this.closest(".m").hasClass("ggopen")){
	        	    	$this.closest(".m").addClass("ggopen");
	        	    	$this.find("i").css("background-position","0 -330px");
	        	    	//me.$(".jc-operate .ggopen .s2").css("height","auto");
	        	    }else{
	        	    	$this.closest(".m").removeClass("ggopen");
	        	    	$this.find("i").css("background-position","-9px -330px");
	        	    	//me.$(".jc-operate .nav .s2").css("height","98px");
	        	    }
	        	    return false;  
	        },
            setDan:function(e){
            	var $this = $(e.currentTarget);
            	var betsmatchid = $this.attr("betsmatchid");	
            	    this.$el.find("input[matchid="+betsmatchid+"]").trigger("click");
            	    
            	    var min_pass_num = +this.$("input[passtype]:checked").first().val();//最小串关数
            	    var count = this.models.count();
                    var len = count[1].length;
                    len = len > 6 ? 6 : len;
                    var dm_len = this.getBetsDmArr();
                    if (!e.currentTarget) {
                        var betsmatchid = e.get("betsmatchid");
                        if (!count[2][matchid]) {
                            var $input = $(this.betsDmArr[betsmatchid]);
                            var checked = $input.prop("checked");
                            $input.prop({
                                "disabled": true,
                                "checked": false
                            });
                            checked && (dm_len = dm_len > 0 ? dm_len - 1 : dm_len)
                        }
                    }
                    // 更新其他checkbox状态
                    _.each(this.betsDmArr, function (el, matchid) {
                        var $input = $(el);
                        var checked = $input.prop("checked");
                        if (count[2][matchid] && len > 1) {
                            if (dm_len == len) {
                                $input.prop("checked", false);
                            } else {
                                if ( (dm_len == min_pass_num - 1 && !checked)|| (dm_len == len - 1 && !checked)) {
                                    $input.prop("disabled", true)
                                } else {
                                    $input.prop("disabled", false)
                                }
                            }
                        } else {
                            $input.prop({
                                "disabled": true,
                                "checked": false
                            });
                        }
                    }, this);           
                    
            },
            getDmlen: function () {
                return _.reduce(this.dmArr, function (num, input) {
                    return ($(input).prop("checked") ? 1 : 0 ) + num
                }, 0);
            },
            getBetsDmArr: function () {
                return _.reduce(this.betsDmArr, function (num, input) {
                    return ($(input).prop("checked") ? 1 : 0 ) + num
                }, 0);
            },  
            updatePassType:function(e){
           	 var me = this,
          	    $this = $(e.currentTarget);
           	 
           	  if($this.attr("type")=="checkbox"){
           		 
           		  if(this.$("#component input[passtype]:checked").length>0){
           			  this.$("#component input[passtype]").prop({"checked":false,"disabled":false});
           			  this.$("#free input[passtype]:disabled").prop("disabled",false);
           			  _.each(this.models.count()[1],function(matchid){
           				  me.$("input[betsmatchid="+matchid+"]").prop({"disabled":false,"checked":false});
           				  me.$("input[matchid="+matchid+"]").prop({"disabled":false,"checked":false});
           			  })
           		  }
           		  
                  var matchNum = this.models.count()[1].length,                   //选择比赛数
                	    $betsDmArr = this.$("input[betsmatchid]:checked"),
                	    $dmlen = $betsDmArr.length,                                // 设了胆的数量
                	    $min_passtype = this.$("input[passtype]:checked").first(); //获取最小过关dom 
           		  if($dmlen>0){
           			  if($this.prop("checked")){
           				  if(+($min_passtype.val()) == $dmlen+1){
                       			_.each(me.$("input[betsmatchid]"),function(v){
                       				if(!$(v).prop("checked")){
                       					$(v).prop("disabled",true);
                       					var matchid = $(v).attr("betsmatchid");
                       					me.$("input[matchid="+matchid+"]").prop("disabled",true);
                       				}
                       			});
                       		}
                       		if(+($min_passtype.val()) == matchNum){                      //如果等于比赛数
                       			this.$("input[betsmatchid]").prop({"disabled":true,"checked":false});
                       			_.each(this.models.count()[1],function(matchid){
                       				me.$("input[matchid="+matchid+"]").prop({"disabled":true,"checked":false});
                       			});
                       			$this.closest("label").prevAll().find("input[passtype]").prop("disabled",false);
                       		}
               		  }else {
               			 
               				 if(+$this.val()==$dmlen+1){
               					 _.each(this.$("input[betsmatchid]"),function(v){
                          				if(!$(v).prop("checked")){	
                          					$(v).prop("disabled",false);
                          					var matchid = $(v).attr("betsmatchid");
                          					me.$("input[matchid="+matchid+"]").prop("disabled",false);
                          				}
                          			});
               				 }
               				 if(+($min_passtype.val()) == matchNum){                      //如果等于比赛数
                        			this.$("input[betsmatchid]").prop({"disabled":true,"checked":false});
                        			_.each(this.models.count()[1],function(matchid){
                        				me.$("input[matchid="+matchid+"]").prop({"disabled":true,"checked":false});
                        			});
                        			$this.closest("label").prevAll().find("input[passtype]").prop("disabled",false);
                        		}
               			  
               		  }              		             			 
               		 
           		  }else{
           			 if(!$this.prop("checked") && +($min_passtype.val()) == matchNum){
           				 this.$("input[betsmatchid]").prop({"disabled":false,"checked":false});
                			_.each(this.models.count()[1],function(matchid){
                				me.$("input[matchid="+matchid+"]").prop({"disabled":false,"checked":false});
                			});
           			 }
           			 if($this.prop("checked")){
           				 if(+($min_passtype.val()) == matchNum){
           					 this.$("input[betsmatchid]").prop({"disabled":true,"checked":false});
                     			_.each(this.models.count()[1],function(matchid){
                     				me.$("input[matchid="+matchid+"]").prop({"disabled":true,"checked":false});
                     			});
           				 }else{
           					if(this.$("input[betsmatchid]:enabled").length==0){
           						this.$("input[betsmatchid]").prop({"disabled":false,"checked":false});
                         			_.each(this.models.count()[1],function(matchid){
                         				me.$("input[matchid="+matchid+"]").prop({"disabled":false,"checked":false});
                         			});
           					} 
           				 }
           			 }else{
           				 if(+($min_passtype.val()) == matchNum){
           					 this.$("input[betsmatchid]").prop({"disabled":true,"checked":false});
                     			_.each(this.models.count()[1],function(matchid){
                     				me.$("input[matchid="+matchid+"]").prop({"disabled":true,"checked":false});
                     			});
           				 }
           				 if($min_passtype.length==0){
           					 this.$("input[betsmatchid]").prop({"disabled":false,"checked":false});
                      			_.each(this.models.count()[1],function(matchid){
                      				me.$("input[matchid="+matchid+"]").prop({"disabled":false,"checked":false});
                      			});
           				 }
           			 }
           			 
           		  }
           		  
           	  }else if($this.attr("type")=="radio"){        		         		  
           		  
           		  /*选混合过关时不可以设胆*/
           			  this.$("#free input[passtype]").prop({"checked":false,"disabled":false});
           			  this.$("#component input[passtype]:disabled").prop("disabled",false);
           			  _.each(this.models.count()[1],function(matchid){
           				  me.$("input[betsmatchid="+matchid+"]").prop({"disabled":true,"checked":false});
           				  me.$("input[matchid="+matchid+"]").prop({"disabled":true,"checked":false});
           			  })
           		
           		  
           	  }
           	  this.calc();
           },
            render: function (e,models) {
            	var count = this.models.count(),
            	me = this,
                len = count[1].length,
                $chuan_tips = this.$chuan_tips,
                $free = this.$free,
                dm_len = this.getDmlen();
            	if(len<1){
	        		$("#listTips").show();
	        		this.$footerfix.hide();
	        		$(window).trigger("scroll")
	        	}else{
	        		$("#listTips").hide();
	        		this.$footerfix.show();
	        		$(window).trigger("scroll")
	        	}
                this.$step1stat.html('已选<b class="red">' + count[1].length + '</b>场比赛，' + count[0].length + '项投注');
                this.$step1num.html(count[1].length);
                
                var dmnum = me.$("input[matchid]:checked").length;//每次设胆 都要检查已设胆的数量
                if(dmnum>1){
                	var n1pass = me.$("#free").find("input[value="+dmnum+"]").prop("disabled",true).closest("label");
                	    n1pass.prevAll().find("input[passtype]").prop("disabled",true);
                	    n1pass.nextAll().find("input[passtype]").prop("disabled",false);
                	if(dmnum>=2){
                		var nmpass = me.$("#component").find("input[value="+dmnum+"]").last().prop("disabled",true).closest("label");
                	    nmpass.prevAll().find("input[passtype]").prop("disabled",true);
                	    nmpass.nextAll().find("input[passtype]").prop("disabled",false);
                	}
                }else{
                	me.$("input[passtype]").prop("disabled",false);
                }
                
                var min_pass_num = +this.$("input[passtype]:checked").first().val();//最小串关数
                if (e && !e.currentTarget) {
                    var matchid = e.get("matchid");
                    if (!count[2][matchid]) {
                        var $input = $(this.dmArr[matchid]);
                        var checked = $input.prop("checked");
                        $input.prop({
                            "disabled": true,
                            "checked": false
                        });
                        checked && (dm_len = dm_len > 0 ? dm_len - 1 : dm_len)
                    }
                }
                _.each(this.dmArr, function (el, matchid) {
                    var $input = $(el);
                    var checked = $input.prop("checked");
                    if (count[2][matchid] && len > 1) {
                        if (dm_len == len) {
                            $input.prop("checked", false);
                        } else {
                            if ( (dm_len == min_pass_num - 1 && !checked)|| (dm_len == len - 1 && !checked)) {  
                                $input.prop("disabled", true)
                            } else {
                                $input.prop("disabled", false)
                            }
                        }
                    } else {
                        $input.prop({
                            "disabled": true,
                            "checked": false
                        });
                    }
                }, this);
                if(min_pass_num && len==min_pass_num){
                	_.each(count[1],function(matchid){
                		me.$("input[matchid="+matchid+"]").prop({"disabled":true,"checked":false})
                	})
                }
                _.each(count[1],function(matchid){ //更新投注方案中的checkbox状态
                	$originInput = me.$("input[matchid="+matchid+"]");
                	me.$("input[betsmatchid="+matchid+"]").prop("checked",$originInput.prop("checked"));
                	me.$("input[betsmatchid="+matchid+"]").prop("disabled",$originInput.prop("disabled"));
                });
                len = len > 6 ? 6 : len;
                if(len>1){ //隐藏/展示过关方式                	
                	var n1 = me.$("#free").find("input[value="+len+"]").last().closest("label");
                	    me.$("#free,#component").find("label").removeClass();
                	    n1.addClass("visibile").prevAll().addClass("visibile").end().nextAll().addClass("hidden").find("input").prop("checked",false);  
                	var nm = me.$("#component").find("input[value="+len+"]").last().closest("label");
                	    nm.addClass("visibile").prevAll().addClass("visibile").end().nextAll().addClass("hidden");  
                }else{
                	me.$("#free,#component").find("label").removeClass("visibile").addClass("hidden");
                }
                if(len<2){            	             	 
               	 $chuan_tips.show();
               	 $free.hide();
                }else{
               	var chuannum = +this.$("#component input:checked").val();
               	if(len<3){               	 
              		 this.$(".m").removeClass("ggopen");
               	}
               	if(len<chuannum){
               		this.$("#component input:checked").prop("checked",false)
               	}
               	$chuan_tips.hide();
               	$free.show();
               }
                this.calc();                                           
            },
            submit:function(e){
            	var _this = this;
                e.stopPropagation();
                if (this.models.count()[1] < 2) {
                    Alert.show(function () {
                        this.$(".txt-center").html("至少选择2注号码才能投注");
                    });
                    return false
                }
                var isVisibile = this.$(".s2 label").hasClass("visibile");
            	var $inputs = isVisibile && this.$(".s2 label");
                if(isVisibile && $inputs.find("input[passtype]:checked").length==0){
                	Alert.show(function () {
                        this.$(".txt-center").html("请选择过关方式");
                    });
                    return false
                }
                isLogin(function () {
                    // 已登陆
                    pay.call(_this, "zjq");
                }, function () {
                    // 未登录
                 var  dataObj = ls.call(_this,"zjq");
               	 if(!!dataObj.data && tools.getBytes(dataObj.data)<=30*1024){
               		dataObj.timestamp = +new Date;             		              		
               		var data = JSON.stringify(dataObj); 
               		$.jStorage.set("betsData",data); 
               		//$.jStorage.setTTL("betsData",60000);
               	 }
                    art_login.show(function () {
                        this.successCallfn = function () {
                            this.hide();
                            pay.call(_this, "zjq")
                        }
                    })
                });
                
            }
                  
        });
       
        // --------------------------------------------------------------------------------
        
        var models = new Models;
        var appView = new AppView;
        (function () {
	       	var outerData = $("#defaultBets").val();
	          var fillData = function(data){
	        	var $wraper = $(".jcarea"),
	        	    $show = $(".show"),
	        	    $s2 = $(".s2"),
	           	    raw = data.split("#"),
	           	    betstr = raw[0],
	           	    mul = raw[1],
	           	    raw1 = betstr.split("|"),
	           	    matches = raw1[0],
	           	    passtype = raw1[1].split(","),
	           	    danHash = {};
	           	    _.each(matches.split(" "),function(match){
	           		   var sec = match.split(":"); 
	           		   var matchid = sec[0];
	           		   var codeArr = sec[1].split(".");
	           		   var isDan = !!(+sec[2]);	           		  
	           		   $wraper.find("tr[matchid="+matchid+"]").find("a[data-model]").each(function(i,v){
	           			   var model = JSON.parse($(v).attr("data-model"));
	           			       if(_.contains(codeArr,model["code"].toString())){
	           			    	  $(v).trigger("click"); 
	           			       }
	           		   });
	           		   danHash[matchid] = isDan;
	           	   });
	           	   for(var key in danHash){
	           		   if(danHash[key]){
	           			$show.find("input[betsmatchid="+key+"]").trigger("click"); 
	           		   }
	           	   }
	           	   _.each(passtype,function(pt){
	           		   pt = pt.replace("_","串");
	           		   $s2.find("input[passtype="+pt+"]").trigger("click");
	           	   })
	               
	               $("#mul").val(mul); // 倍数都一样
	               $.jStorage.flush();           
	           }

	           isLogin(function(userId) {
	               if (userId.indexOf("qq.sohu.com") > -1 && $.jStorage.index().length > 0) {
	                   var dataObj = $.jStorage.get("betsData");
	                   dataObj = JSON.parse(dataObj);
	                   if (Math.abs( + new Date - ( + dataObj.timestamp)) > 60 * 1000) {
	                       $.jStorage.flush();
	                   } else if (dataObj && dataObj.gameId === "zjq") {
	                       fillData(dataObj.data);
	                   }
	               } else if (outerData) {
	                   fillData(outerData);
	               }
	           },
	           function() { //login not yet
	               $.jStorage.flush();
	               if (outerData) {
	                   fillData(outerData);
	               }
	           });	             	           
	       })();
    }

})