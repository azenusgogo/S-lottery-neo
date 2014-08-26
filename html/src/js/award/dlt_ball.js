define([
    "award/common",
    "lib/dialog",
    "lib/alert",
    "text!award/dlt_ball.tpl"
],function (common,Dialog,dialogAlert,dlt_ball){
    var $el = $("#dlt_ball");
    if ($el.length == 0) return function(){};
    // ==========================================================
    var _$ = function(selector) {
        return $el.find(selector)
    };
    var dialog = new Dialog({
    	width:560,
    	el:"#tool_dia",
    	onload:function(){
            this.$kjnum = this.$(".kj_num");
            this.$mynum = this.$(".my_num");
            this.$info = this.$(".info em");
            this.$qici = this.$(".info span");
        }
    });

    return function(){
		var $ball = _$(".ball-sel li");
		var $info = _$(".tou-info i");
		var $calcu = _$(".fbtn:eq(0)");
		var $kj = _$(".t-ret-sel");
		var $zhui = _$(".ball_zhui");
		var $dlt_dia = $("#dlt_ball_ret");
		var jiang = $("#jiangs").val();
		var qici = $("#qicihao").val().split(",");
		var zhui = false;
		var is_new = 0;
		var c = common.c;
		var calcu = common.calcu;

		var red_arr = [],blue_arr = [];
		var aBack = $("#aBack").val().split(";"); //按奖级隔开
		var aZhui = $("#aZhui").val().split(";"); 
		var award_cash = [];
		var Alert = dialogAlert("友情提示");

		var count = function(){//计算注数和金额
			var ret = c(red_arr.length,5)*c(blue_arr.length,2);
			$info.eq(2).html(ret);
			$info.eq(3).html(ret*(zhui?3:2));
		}

		var showAward = function(index,ret){ //改变奖金数据

			var total = 0,
				data = {};
				award_cash = [];

			if(is_new){
				if(zhui){
					award_cash = [
						+aBack[0].split(",")[index] + +aZhui[0].split(",")[index],
						+aBack[1].split(",")[index] + +aZhui[1].split(",")[index],
						+aBack[2].split(",")[index] + +aZhui[2].split(",")[index],
						300,15,5
					]
				}else{
					award_cash = [
						+aBack[0].split(",")[index],
						+aBack[1].split(",")[index],
						+aBack[2].split(",")[index],
						200,10,5
					]
				}
			}else{
				if(zhui){
					award_cash = [
						+aBack[0].split(",")[index] + +aZhui[0].split(",")[index],
						+aBack[1].split(",")[index] + +aZhui[1].split(",")[index],
						+aBack[2].split(",")[index] + +aZhui[2].split(",")[index],
						4500,900,150,15,5
					]
				}else{
					award_cash = [
						+aBack[0].split(",")[index],
						+aBack[1].split(",")[index],
						+aBack[2].split(",")[index],
						3000,600,100,10,5
					]
				}
			}

			$.each(award_cash,function(i,n){
				total += n*ret[i];
			});

			data.zhu = ret;
			data.award = award_cash;
			data.total = total;
			data.is_new = is_new;
			data.format = common.formatNum;

			$dlt_dia.html(_.template(dlt_ball,data));
		}

		$zhui.on("click",function(){//追加投注
			zhui = $zhui.prop("checked");
			var zhu = $(".ball_zhu").html();
			$(".ball_yuan").html(zhu*(zhui?3:2));
			$(".ball_price").html(zhui?3:2);
		});

		$ball.on("click",function(){
			var _this = $(this);
			var haoma = _this.html();
			var ret = _this.parents("div").hasClass("red-ball");
			var add = function(){
				_this.addClass("active");
				ret && red_arr.push(haoma) || blue_arr.push(haoma);
			}
			var move = function(){
				_this.removeClass("active");
				ret && (red_arr = _.difference(red_arr,haoma)) || (blue_arr = _.difference(blue_arr,haoma));
			}

			this.className?move():add();
			ret?$info.eq(0).html(red_arr.length):$info.eq(1).html(blue_arr.length);
			count();
		});

		$calcu.on("click",function(){

			var index = $kj.find(":selected").index();
			var qiciNo = qici[index];
			var rule = [],ret = [];
			var red_ball = $kj.val().split(":")[0].split(" ");
			var blue_ball = $kj.val().split(":")[1].split(" ");
			var red = 0,
				blue = 0,
				redTotal = red_arr.length,
				blueTotal = blue_arr.length;

			if(redTotal < 5 || blueTotal <2 ){
				Alert.show(function(){
					this.$(".txt-center").html("请选择至少5个红球和2个蓝球！");
				});
				return false;
			}

			if(+qiciNo >= 14052){ //判断是否为新规则
				is_new = 1;
				rule = ["5:2","5:1","5:0,4:2","4:1,3:2","4:0,3:1,2:2","3:0,2:1,1:2,0:2"];
			}else{
				is_new = 0;
				rule = ["5:2","5:1","5:0","4:2","4:1","4:0,3:2","3:1,2:2","3:0,2:1,1:2,0:2"];
			}

			$.each(red_ball,function(i,n){ //计算红球命中数
                if($.inArray(n,red_arr)!=-1) red++;
            });

            $.each(blue_ball,function(i,n){ //计算蓝球命中数
                if($.inArray(n,blue_arr)!=-1) blue++;
            });

			ret = calcu(rule,redTotal,blueTotal,red,blue,5,2);//计算各奖级注数
			showAward(index,ret); //重新计算各级奖金 参数:数据索引,其次号,各级注数

			dialog.show(function(){

                var knum = "",ma = "",mb = "";

                //填充开奖号码
                $.each(red_ball,function(i,n){
                    knum += "<em>"+n+"</em> ";
                });

                knum += "<i>+</i> <b>"+blue_ball.join(",").replace(",","</b><b>")+"</b>";

                //填充投注号码
                $.each(red_arr,function(i,n){
                    ma += "<em>"+n+"</em> ";
                });

                $.each(blue_arr,function(i,n){
                    mb += "<b>"+n+"</b>";
                });
                ma += "<i>+</i> "+mb;
                
                this.$qici.html(qiciNo);
                this.$kjnum.html(knum);
                this.$mynum.html(ma);
                this.$info.eq(0).html($info.eq(2).html());
                this.$info.eq(2).html($info.eq(3).html());
            });
			return false;
		});

		$("#nowTou").on("click",function(){

			if(red_arr.length<5 || blue_arr.length<2){
				Alert.show(function(){
					this.$(".txt-center").html("请选择至少5个红球和2个蓝球！");
				});
				return false;
			}

			var bets = red_arr + ":" + blue_arr + "#1";
			bets = bets.replace(/,/g," ");

			$("#quickBet").val(bets);
			$("#tou").submit();

		});

    }
})