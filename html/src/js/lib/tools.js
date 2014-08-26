define(function () {
    /*
     * 阶乘计算
     * */
    var factorial = function (n) {
        if (n > 0)return(factorial(n - 1) * n);
        return(1);
    };

    /*
     * 常用正则表达式
     * */
    var reg = {
        // 整型
        int: /^-?[1-9]\d*$/
    };
    /*
     * 求排列数
     * m<=n
     * */
    var c = function (n, m) {
        n = +n;
        m = +m;
        var n1 = 1, n2 = 1;
        for (var i = n, j = 1; j <= m; n1 *= i--, n2 *= j++) {
        }
        return n1 / n2
    };

    /*
     * 求任选9组合数
     * a-3,b-2,c-1
    */

    var rx9_c = function(a3,b2,c1,n){
        n || (n = 9);
        if (a3 + b2 + c1 < n) {
            return 0;
        }
        var r = 0;
        for (var i = 0; i <= a3; i++) {
            for (var j = 0; j <= b2; j++) {
                var residue = n - i - j;
                r += Math.pow(3, i) * Math.pow(2, j) * c(a3, i) * c(b2, j) * c(c1, residue);
            }
        }
        return r;
    }

    //浮点数加法运算  
    var FloatAdd = function(arg1, arg2) {
        var r1, r2, m;
        try {
            r1 = arg1.toString().split(".")[1].length
        } catch(e) {
            r1 = 0
        }
        try {
            r2 = arg2.toString().split(".")[1].length
        } catch(e) {
            r2 = 0
        }
        m = Math.pow(10, Math.max(r1, r2));
        return (arg1 * m + arg2 * m) / m;
    };

    //浮点数减法运算  
    var FloatSub = function(arg1, arg2) {
        var r1, r2, m, n;
        try {
            r1 = arg1.toString().split(".")[1].length
        } catch(e) {
            r1 = 0
        }
        try {
            r2 = arg2.toString().split(".")[1].length
        } catch(e) {
            r2 = 0
        }
        m = Math.pow(10, Math.max(r1, r2));
        //动态控制精度长度  
        n = (r1 >= r2) ? r1: r2;
        return ((arg1 * m - arg2 * m) / m).toFixed(n);
    };

    //浮点数乘法运算  
    var FloatMul = function(arg1, arg2) {
        var m = 0,
        s1 = arg1.toString(),
        s2 = arg2.toString();
        try {
            m += s1.split(".")[1].length
        } catch(e) {}
        try {
            m += s2.split(".")[1].length
        } catch(e) {}
        return Number(s1.replace(".", "")) * Number(s2.replace(".", "")) / Math.pow(10, m)
    };

    //浮点数除法运算  
    var FloatDiv = function(arg1, arg2) {
        var t1 = 0,
        t2 = 0,
        r1, r2;
        try {
            t1 = arg1.toString().split(".")[1].length
        } catch(e) {}
        try {
            t2 = arg2.toString().split(".")[1].length
        } catch(e) {}
        with(Math) {
            r1 = Number(arg1.toString().replace(".", ""));
            r2 = Number(arg2.toString().replace(".", ""));
            return (r1 / r2) * pow(10, t2 - t1);
        }
    };

    // 数字 2位小数
    var reg1 = /[^0-9.]/g;
    var reg2 = /\./g
    var enterFloat = function($el,n){
      n || (n = 2);
      var v =$el.val();
      v = v.replace(reg1,"")
           .replace(".","$#$")
           .replace(reg2,"")
           .replace("$#$",".");
      var i = v.indexOf(".");
      if (i>0){
          v = v.slice(0,i) + v.slice(i,i+n+1); // n位置小数
      }
      $el.val(v)
    };

    var _pad = function (val, length) {
        var pre = "",
            negative = (val < 0),
            string = String(Math.abs(val))

        if (string.length < length) {
            pre = (new Array(length - string.length + 1)).join('0')
        }

        return (negative ? "-" : "") + pre + string
    };

    // 格式化日期
    var formatDate = function (source, pattern) {
        if ('string' != typeof pattern) {
            return source.toString()
        }
        var replacer = function (patternPart, result) {
            pattern = pattern.replace(patternPart, result)
        };
        var pad = _pad;
        var year = source.getFullYear();
        var month = source.getMonth() + 1;
        var date2 = source.getDate();
        var hours = source.getHours();
        var minutes = source.getMinutes();
        var seconds = source.getSeconds();
        replacer(/\{yyyy\}/g, pad(year, 4));
        replacer(/\{yy\}/g, pad(parseInt(year.toString().slice(2), 10), 2));
        replacer(/\{MM\}/g, pad(month, 2));
        replacer(/\{M\}/g, month);
        replacer(/\{dd\}/g, pad(date2, 2));
        replacer(/\{d\}/g, date2);
        replacer(/\{HH\}/g, pad(hours, 2));
        replacer(/\{H\}/g, hours);
        replacer(/\{hh\}/g, pad(hours % 12, 2));
        replacer(/\{h\}/g, hours % 12);
        replacer(/\{mm\}/g, pad(minutes, 2));
        replacer(/\{m\}/g, minutes);
        replacer(/\{ss\}/g, pad(seconds, 2));
        replacer(/\{s\}/g, seconds);
        return pattern
    };

    // 转货币格式  currency ...

    var formatCurrency = function (s){
        return s.replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,")
    };

    // tab 切换
    var tabToggle = function (options) {
        this.$t = options.$t;
        this.$c = options.$c;
        this.preventDefault = typeof options.preventDefault =="undefined" ?true : options.preventDefault;
        this.fn = options.fn || function(){};
        this.className = options.className || ".toggle_c";
        this.init()
    };
    tabToggle.prototype = {
        init: function () {
            var _this = this;
            this.$t.on("click", function (e) {
                _this.to($(this).index());
                _this.preventDefault && e.preventDefault()
            })
        },
        to: function (i) {
            var $t = this.$t.eq(i);
            $t.addClass("active");
            $t.siblings().removeClass("active");
            var $c = this.$c.eq(i);
            $c.css("display", "block");
            $c.siblings(this.className).css("display", "none");
            this.fn.call($t,i);
        }
    };

    // 判断是否登陆
    var isLogin = function(fn1,fn2){
        var b = PassportSC.cookieHandle();
        if (b){
            fn1 && fn1(b)
        }else{
            fn2 && fn2()
        }
    };

    var getBytes = function (str) {
        var len = 0
        for (var i = str.length; i--;) {
            if (str.charAt(i) > '~') {
                len += 1
            } else {
                len += .5
            }
        }
        return len
    };
    var scrollToDom = function(selector){
    	var selector = selector || "body",
    	    $dom = $(selector);
    	if($dom.length){
    		$("body,html").animate({
                scrollTop: $dom.offset().top
            }, 100);
    	}
    };
    var moveDom = function($cl, $fr, $to, per, fn) {
    	//var me = arguments.callee;
		if ($cl && $fr && $to) {
			fn = fn || $.noop;
			var w = $fr.offset(), 
			    t = $to.offset(), 
			    i = Math.abs(w.left - t.left), 
			    q = Math.abs(w.top - t.top), 
			    r = w.left, 
			    p = w.top, 
			    g = t.left, 
			    f = t.top, 
                o = 5, 
                m,
			    k = (i * o / per) || 10, 
			    s = q * o / per;
			$cl.css({left : r,top : p,position : "absolute"});
			m = setInterval(function() {
						if (Math.abs(r - g) <= k) {
							clearInterval(m);
							fn();
						} else {
							r > g ? r -= k : r += k;
							p > f ? p -= s : p += s;
						}
						$cl.css({
									left : r,
									top : p,
									position : "absolute"
								})
					}, o)
		}
	};
    return {
        factorial: factorial,
        reg: reg,
        c: c,
        rx9_c:rx9_c,
        tabToggle: tabToggle,
        FloatAdd:FloatAdd,
        FloatSub:FloatSub,
        FloatMul:FloatMul,
        FloatDiv:FloatDiv,
        enterFloat:enterFloat,
        isLogin:isLogin,
        formatDate:formatDate,
        formatCurrency:formatCurrency,
        getBytes:getBytes,
        scrollToDom:scrollToDom,
        moveDom:moveDom
    }
})