define([
    "award/common",
    "lib/alert"
],function (common,dialogAlert ){
    var $el = $("#ssq_award");
    if ($el.length == 0) return function(){};
    // ==========================================================
    var _$ = function(selector) {
        return $el.find(selector)
    };
    return function(){
		var url = "";
		var type = 0;
		var $period = _$(".d_period");
		var $calcu = _$(".fbtn:eq(0)");
		var $a_cash = _$(".a_cash");
		var $ssqret = _$(".t-ret b");
		var $dt_sred = _$(".s_red:eq(1)");
		var $dt_tred = _$(".t_red:eq(1)");
		var $spend = _$(".s_red,.s_blue,.s_red_dm");
		var $bet_num = _$(".bet_num");
		var $jiang = _$(".ssq_jiang");

		var jiang = $("#jiangs").val().split(";");//各期次一二等奖数据
		var award_cash = [jiang[0].split(",")[0]*1,jiang[1].split(",")[0]*1,3000,200,10,5];
		var c = common.c;
		var csel = common.csel;
		var calcu = common.calcu;
		var calcuDt = common.calcuDt;
		var formatNum = common.formatNum;
		var Alert = dialogAlert("友情提示");

		var showAward = function(award){
			$.each(award,function(i,n){
				if(n==0){
					$jiang.eq(i).html("&minus;");
				}else{
					$jiang.eq(i).html(formatNum(n) + "元");
				}
				
			})
		}

		$period.on("change",function(){ //切换期次

			var index = $(this).val();
			$bet_num.eq(index).show().siblings(".bet_num").hide();

			award_cash[0] = jiang[0].split(",")[index]*1;
			award_cash[1] = jiang[1].split(",")[index]*1;

			showAward(award_cash);
			$calcu.click();

		})

		$(".d_type").on("click",function(){ //切换普通，胆拖 玩法
			type = $(this).val();
			$(".tz_type").eq(type).show().siblings(".tz_type").hide();
		});

		$(".s_red_dm").on("change",function(){
			var val = $(this).val();
			$(".t_red_dm").html(csel(val));
			$(".s_red").html(csel(33-val));
		});

		$(".t_red_dm").on("change",function(){
			var val = $(this).val()*1;
			var red = $dt_sred.val()*1;
			if((val+red) <= 6){
				$dt_tred.html(csel(red));
			}else{
				$dt_tred.html(csel(6-val));
			}
		});

		$dt_sred.on("change",function(){
			var val = $(this).val()*1;
			var tdm = $(".t_red_dm").val()*1;

			if(val+tdm <= 6){
				$dt_tred.html(csel(val));
			}else{
				$dt_tred.html(csel(6-tdm));
			}
		})

		$spend.on("change",function(){//计算注数和金额
			if(type == 0){
				var zhu = c($(".s_red:eq(0)").val(),6)*$(".s_blue:eq(0)").val();
			}else{
				var zhu = c($dt_sred.val(),6-$(".s_red_dm").val())*$(".s_blue:eq(1)").val();
			}
			$(".zhu_stat").eq(type).html(zhu);
			$(".yuan_stat").eq(type).html(zhu*2);
		});

		$calcu.on("click",function(){
			var total = 0,zhu_arr;
			
			var rule = ["6:1","6:0","5:1","5:0,4:1","4:0,3:1","2:1,1:1,0:1"];

			if(type == 0){
				var redTotal = $(".s_red:eq(0)").val();
				var blueTotal = $(".s_blue:eq(0)").val();
				var red = $(".t_red:eq(0)").val();
				var blue = $(".t_blue:eq(0)").val();

				zhu_arr = calcu(rule,redTotal,blueTotal,red,blue,6,1);

			}else{
				var xr1 = _$(".s_red_dm").val()*1;
				var xr2 = $dt_sred.val()*1;
				var xb1 = 0;
				var xb2 = _$(".s_blue:eq(1)").val()*1;
				var mr1 = _$(".t_red_dm").val()*1;
				var mr2 = $dt_tred.val()*1;
				var mb1 = 0;
				var mb2 = _$(".t_blue:eq(1)").val()*1;

				if(xr1 + xr2 < 6){
					Alert.show(function(){
						this.$(".txt-center").html("选择的胆码加拖码个数不能少于6个!");
					});
					return;
				}
				
				zhu_arr = calcuDt(rule,xr1,xr2,xb1,xb2,mr1,mr2,mb1,mb2,6,1);

			}

			$.each(zhu_arr,function(i,n){
				total += award_cash[i]*n;
			});

			$ssqret.html(common.formatNum(total));

			$.each(zhu_arr,function(i,n){//填充中奖详情
				if(n>0){
					$(".t_zhu").eq(i).html("<em>"+n+"</em>注");
					$(".t_cash").eq(i).html("<em>"+common.formatNum(n*award_cash[i])+"</em>元");
				}else{
					$(".t_zhu").eq(i).html("0注");
					$(".t_cash").eq(i).html("0元");
				}
			})
			return false;
		});

		$("#nowTry").on("click",function(){

			var redArr = _.sortBy(_.sample(_.range(1, 34), 6));
			var blueArr = _.sample(_.range(1, 17), 1);
			var bets = "";

			$.each(redArr,function(i,n){
				if(n < 10){
					redArr[i] = "0" + n;
				}
			});

			$.each(blueArr,function(i,n){
				if(n < 10){
					blueArr[i] = "0" + n;
				}
			});

			bets = redArr + ":" + blueArr + "#1";
			bets = bets.replace(/,/g," ");

			$("#tryBet").val(bets);
			$("#try").submit();

		})
    }
})