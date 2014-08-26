define(function(){
	var formatNum = function(num){
		num = num.toString();
		var money = "";
		var len = num.length;
		
		for(var i=0;i<len;i++){
			var w=len-i-1;
			if(w%3==2 && i>0){
				money += ","+num.charAt(i);
			}else{
				money += num.charAt(i);
			}
		}
		return money;
	}

	var csel = function(len){
		var ret = "";
		for(var i=0;i<=len;i++){
			ret += '<option value="'+i+'">'+i+'个</option>';
		}
		return ret;
	}

	var c = function (n, m) {
		if(n < m) {
			return 0
		}
		n = +n;
		m = +m;
		var n1 = 1, n2 = 1;
		for (var i = n, j = 1; j <= m; n1 *= i--, n2 *= j++) {
		}
		return n1 / n2
	}

	var calcu = function(rule,n1,n2,m1,m2,r,b){

		// 普通计算器使用说明（此计算器可以计算出各奖级中奖注数）
		// n1,n2,m1,m2,r,b 分别代表红球选择数，蓝球选择数，红球命中数，蓝球命中数，玩法中红蓝球个数
		// rule 为各奖级命中规则，格式如["5:2","5:1","5:0","4:2","4:1","4:0,3:2","3:1,2:2","3:0,2:1,1:2,0:2"];

		var ret = []; //输出命中注数

		$.each(rule,function(i,n){// 循环中奖规则，去匹配命中情况

			var num = 0; //中奖注数

			$.each(n.split(","),function(i,m){

				var x = m.split(":")[0]; //规则中要求命中的红球个数
				var y = m.split(":")[1]; //规则中要求命中的蓝球个数
				
				num += c(m1,x)*c((n1-m1),(r-x))*c(m2,y)*c((n2-m2),(b-y));

			});

			ret.push(num);
		});

		return ret;
	}

	var danTuo = function(rule,xr1,xr2,xb1,xb2,mr1,mr2,mb1,mb2,r,b){

		// 胆拖计算器使用说明（此计算器可以计算出胆拖玩法各奖级中奖注数）
		// xr1,xr2,xb,mr1,mr2,mb,r,b ：x选择，r红色，b蓝色， 1胆 2拖 r红球，b蓝球
		// rule 为各奖级命中规则，格式如["5:2","5:1","5:0","4:2","4:1","4:0,3:2","3:1,2:2","3:0,2:1,1:2,0:2"]

		var ret = [];

		$.each(rule,function(i,n){// 循环中奖规则，去匹配命中情况

			var num = 0; //中奖注数

			$.each(n.split(","),function(i,m){

				var x = m.split(":")[0]; //规则中要求命中的红球个数
				var y = m.split(":")[1]; //规则中要求命中的蓝球个数

				if(x >= mr1 && y >= mb1 && (r-xr1+mr1 >= x) && (b-xb1+mb1 >= y)){
					num += c(mr2,(x-mr1))*c((xr2-mr2),(r-xr1-x+mr1))*c(mb2,(y-mb1))*c((xb2-mb2),(b-xb1-y+mb1));
				}

			});

			ret.push(num);
		});

		return ret;
	}

    return {
    	formatNum:formatNum,
    	csel:csel,
    	c:c,
    	calcu:calcu,
    	calcuDt:danTuo
    }
})