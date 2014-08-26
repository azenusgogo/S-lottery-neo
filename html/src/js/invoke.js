define(["src/js/hh_calc.js"],function(C){
	  var matches = [[[6,15.00],[7,34.00],[0,6.20],[37,11.00],[38,4.80]],
		               [[1,3.00],[4,3.60],[6,6.00],[7,9.00]]		               
		               ]
			/*[[[0,1.71],[5,1.87],[3,3.16]],
		               [[0,2.38],[3,5.40]],
		               [[0,2.37],[6,6.00],[7,9.50]]
		               ];*/
		var dan = [];
		var types = ["2串1"];
		var obj = C.hhcalc.calculateBet(matches, dan, types, [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]);

		$("#match").html(matches.length)
		$("#bets").html(obj.count);
		$("#min").html(obj.min*2);
		$("#max").html(obj.max*2);
		$("#money").html(obj.count*2)
  })