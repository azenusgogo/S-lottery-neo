define([
    "award/common",
    "lib/dialog",
    "lib/alert"
],function (common,Dialog,dialogAlert){
    var $el = $("#ssq_ball");
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
    		this.$zjnum = this.$(".nbd em");
    		this.$total = this.$(".total em");
    		this.$info = this.$(".info em");
    		this.$qici = this.$(".info span");
    	}
    });

    return function(){
		var $ball = _$(".ball-sel li");
		var $info = _$(".tou-info i");
		var $calcu = _$(".fbtn:eq(0)");
		var $kj = _$(".t-ret-sel");
		var $jiang = $(".ball_jiang");
		var jiang = $("#jiangs").val().split(";");
		var qici = $("#qicihao").val().split(",");
		var c = common.c;
		var calcu = common.calcu;

		var red_arr = [],blue_arr = [];
		var award_cash = [jiang[0].split(",")[0]*1,jiang[1].split(",")[0]*1,3000,200,10,5];
		var Alert = dialogAlert("友情提示");

		var count = function(){//计算注数和金额
			var ret = c(red_arr.length,6)*blue_arr.length;
			$info.eq(2).html(ret);
			$info.eq(3).html(ret*2);
		}

		$kj.on("change",function(){
			var index = $(this).find(":selected").index();
			var jiang1 = jiang[0].split(",")[index]*1;
			var jiang2 = jiang[1].split(",")[index]*1;

			$jiang.eq(0).html((jiang1 > 0) ? jiang1 : "&minus;");
			$jiang.eq(1).html((jiang2 > 0) ? jiang2 : "&minus;");

			award_cash[0] = jiang1;
			award_cash[1] = jiang2;
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
			var rule = ["6:1","6:0","5:1","5:0,4:1","4:0,3:1","2:1,1:1,0:1"];
			var ret = [];
			var red_ball = $kj.val().split(":")[0].split(" ");
			var blue_ball = $kj.val().split(":")[1];
			var red = 0,
				blue = 0,
				redTotal = red_arr.length,
				blueTotal = blue_arr.length;

			if(red_arr.length<6 || blue_arr.length<1){
				Alert.show(function(){
					this.$(".txt-center").html("请选择至少6个红球和1个蓝球！");
				});

				return false;
			}

			$.each(red_ball,function(i,n){
				if($.inArray(n,red_arr)!=-1) red++;
			});

			if($.inArray(blue_ball,blue_arr)!=-1) blue=1;
			
			ret = calcu(rule,redTotal,blueTotal,red,blue,6,1);

			dialog.show(function(){
				var knum = "",ma = "",mb = "",total = 0;
				$.each(red_ball,function(i,n){
					knum += "<em>"+n+"</em> ";
				});
				knum += "<i>+</i> <b>"+blue_ball+"</b>";

				$.each(red_arr,function(i,n){
					ma += "<em>"+n+"</em> ";
				});

				$.each(blue_arr,function(i,n){
					mb += "<b>"+n+"</b>";
				});
				ma += "<i>+</i> "+mb;
				var $kjret = this.$zjnum;

				$.each(ret,function(i,n){

					$kjret.eq(i).html(n);
					total += award_cash[i]*n;
				});

				this.$qici.html(qiciNo);
				this.$kjnum.html(knum);
				this.$mynum.html(ma);
				this.$info.eq(0).html($info.eq(2).html());
				this.$info.eq(2).html($info.eq(3).html());
				this.$total.html(total);
			});
			return false;
		});

		$("#nowTou").on("click",function(){

			if(red_arr.length<6 || blue_arr.length<1){
				Alert.show(function(){
					this.$(".txt-center").html("请选择至少6个红球和1个蓝球！");
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