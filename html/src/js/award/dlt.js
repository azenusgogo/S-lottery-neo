define([
    "award/common",
    "lib/alert",
    "text!award/dlt_com.tpl"
],function (common,dialogAlert,dlt_com){
    var $el = $("#dlt_award");
    if ($el.length == 0) return function(){};
    // ==========================================================
    var _$ = function(selector) {
        return $el.find(selector)
    };

    return function(){
    	var url = "";
		var type = 0,zhui = false,is_new = 0;
		var $period = _$(".d_period");
		var $calcu = _$(".fbtn:eq(0)");
		var $a_cash = _$(".a_cash");
		var $ssqret = _$(".t-ret b");
		var $zhui = _$(".zhui");
		var $spend = _$(".s_red,.s_blue");
		var $cash1 = _$(".cash_1");
		var $cash2 = _$(".cash_2");
		var $cash3 = _$(".cash_3");

		var $rdm = _$(".s_red_dm");
		var $rtm = _$(".s_red_tm");
		var $bdm = _$(".s_blue_dm");
		var $btm = _$(".s_blue_tm");
		var $mz_rdm = _$(".t_red_dm");
		var $mz_rtm = _$(".t_red_tm");
		var $mb1 = _$(".t_blue_dm");
		var $jiang = _$(".dlt_jiang");
		var $award_tbl = $("#dlt_award_tbl");
		var aBack = $("#aBack").val().split(";"); //按奖级隔开
		var aZhui = $("#aZhui").val().split(";");

		var award_cash = []; //初始化奖级

		var c = common.c;
		var csel = common.csel;
		var calcu = common.calcu;
		var calcuDt = common.calcuDt;
		var format = common.formatNum;
		var Alert = dialogAlert("友情提示");

		var showAward = function(period,zhu){

			var index = +period,
				total = 0,
				data = {};
				award_cash = [];

			if(is_new){
				if(zhui){
					award_cash = [
						+aBack[0].split(",")[period] + +aZhui[0].split(",")[period],
						+aBack[1].split(",")[period] + +aZhui[1].split(",")[period],
						+aBack[2].split(",")[period] + +aZhui[2].split(",")[period],
						300,15,5
					]
				}else{
					award_cash = [
						+aBack[0].split(",")[period],
						+aBack[1].split(",")[period],
						+aBack[2].split(",")[period],
						200,10,5
					]
				}
			}else{
				if(zhui){
					award_cash = [
						+aBack[0].split(",")[period] + +aZhui[0].split(",")[period],
						+aBack[1].split(",")[period] + +aZhui[1].split(",")[period],
						+aBack[2].split(",")[period] + +aZhui[2].split(",")[period],
						4500,900,150,15,5
					]
				}else{
					award_cash = [
						+aBack[0].split(",")[period],
						+aBack[1].split(",")[period],
						+aBack[2].split(",")[period],
						3000,600,100,10,5
					]
				}
			}

			data.award = award_cash;
			data.zhuNo = zhu;
			data.is_new = is_new;
			data.format = format;

			$.each(award_cash,function(i,n){
				total += n*zhu[i];
			});

			$ssqret.html(format(total));
			$award_tbl.html(_.template(dlt_com,data));
		}

		$period.on("change",function(){ //选择期次
			zhui = $zhui.eq(type).prop("checked");

			var $this = $(this);
			var index = $this.val();
			_$(".bet_num").eq(index).show().siblings(".bet_num").hide();

			$calcu.click();
		})

		$(".d_type").on("click",function(){
			type = $(this).val();
			$(".tz_type").eq(type).show().siblings(".tz_type").hide();
			$calcu.click();
		});

		$zhui.on("click",function(){//追加投注
			zhui = $zhui.eq(type).prop("checked");

			var zhu = $(".zhu_stat").eq(type).html();
			var index = $period.val();

			$(".yuan_stat").eq(type).html(zhu*(zhui?3:2));
			$calcu.click();
		});

		$spend.on("change",function(){//普通投注 计算注数和金额
			if(type == 0){
				var zhu = c($(".s_red:eq(0)").val(),5)*c($(".s_blue:eq(0)").val(),2);
			}else{
				var zhu = c($rtm.val(),6-$(".s_red_dm").val())*$(".s_blue:eq(1)").val();
			}
			$(".zhu_stat:eq(0)").html(zhu);
			$(".yuan_stat:eq(0)").html(zhu*(zhui?3:2))
		});

		$rdm.on("change",function(){//胆拖select控制

			var a = $(this).val()*1; //选择红胆码
			var b = $rtm.val()*1; //选择红拖码
			var c = $mz_rdm.val()*1; //命中红胆码

			$mz_rdm.html(csel(a));

			$(this).siblings("select").html(csel(35-a));

		}).add($rtm).add($btm).on("change",function(){//计算注数和金额

			var v1 = $rdm.val()*1;
			var v2 = $rtm.val()*1;
			var v3 = $btm.val()*1;
			var v4 = $mz_rdm.val()*1;
			var zhu = c(v2,(5-v1))*v3;

			$(".zhu_stat:eq(1)").html(zhu);
			$(".yuan_stat:eq(1)").html(zhu*(zhui?3:2));

		});

		$rtm.add($mz_rdm).on("change",function(){//控制前区拖码命中数select
			var s1 = +$rtm.val();
			var s2 = +$mz_rdm.val();
			if(s1+s2 > 5){
				$mz_rtm.html(csel(5-s2));
			}else{
				$mz_rtm.html(csel(s1));
			}
		});

		$mb1.add($btm).on("change",function(){

			var v1 = +$mb1.val();
			var v2 = +$btm.val();
			var $mb2 = _$(".t_blue_tm");
			if(v1+v2 > 2){
				$mb2.html(csel(2-v1));
			}else{
				$mb2.html(csel(v2));
			}

		});

		$calcu.on("click",function(){
			var zhu_arr,rule = [];

			if(+$period.find(":selected").text() >= 14052){ //判断是否启用新规则
				is_new = 1;
			}else{
				is_new = 0;
			}

			if(is_new){
				rule = ["5:2","5:1","5:0,4:2","4:1,3:2","4:0,3:1,2:2","3:0,2:1,1:2,0:2"];
			}else{
				rule = ["5:2","5:1","5:0","4:2","4:1","4:0,3:2","3:1,2:2","3:0,2:1,1:2,0:2"];
			}
			
			var index = $period.val();
			zhui = $zhui.eq(type).prop("checked");

			

			if(type == 0){
				var n1 = _$(".s_red").val();
				var n2 = _$(".s_blue").val();
				var m1 = _$(".t_red").val();
				var m2 = _$(".t_blue").val();

				zhu_arr = calcu(rule,n1,n2,m1,m2,5,2);

			}else{
				var xr1 = $rdm.val()*1;
				var xr2 = $rtm.val()*1;
				var xb1 = $bdm.val()*1;
				var xb2 = $btm.val()*1;
				var mr1 = _$(".t_red_dm").val()*1;
				var mr2 = _$(".t_red_tm").val()*1;
				var mb1 = _$(".t_blue_dm").val()*1;
				var mb2 = _$(".t_blue_tm").val()*1;

				if(xr1 + xr2 < 5){
					Alert.show(function(){
						this.$(".txt-center").html("选择的前区胆码加拖码个数不能少于5个!");
					});
					return;
				}

				zhu_arr = calcuDt(rule,xr1,xr2,xb1,xb2,mr1,mr2,mb1,mb2,5,2);

			}

			showAward(index,zhu_arr);
			return false;
		});

		$("#nowTry").on("click",function(){ //试试手气

			var redArr = _.sortBy(_.sample(_.range(1, 36), 5));
			var blueArr = _.sample(_.range(1, 13), 2);
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

		});

		$calcu.click();//初始化页面

    }
})