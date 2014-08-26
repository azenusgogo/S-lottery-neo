define(function(){
	return function(){
		var TopView =  Backbone.View.extend({
	    	el:".jctop",
	    	events:{
	    		"click .selmatch":"showMatchesFilter", //弹出赛事赛选
	    		"click .onlyfive input[type=checkbox]":"showFiveLeagues",  // 只显示五大联赛
	    		"click .showall":"showAllMatches",    //显示全部 
	    		"click .close":"closeMatchBox",       //关闭赛事筛选
	    		"click .selAll":"selAllCheckbox",     //全选
	    		"click .deselAll":"deselAllCheckbox", //反选
	    		"click .region" :"toggleRegion",      //选择/去掉赛事
	    		"click .onlyfive input[type=checkbox]":"showFiveLeagues"   //只显示五大联赛
	    	},
	    	initialize:function(){
	         this.$selMatch = this.$el.find(".selmatch");
	   		 this.$matchbox = this.$el.find(".matchbox");
	   		 this.$ofive = this.$el.find("#ofive");
	   		 this.$only_sel = $(".only_sel");
	   		 //this.$matchbox.find("input[type=checkbox]").prop("checked",true);
	   		 this.hidenMatchNum = 0;
	   		 this.$hidenMatchNum = this.$el.find(".hidenMatchNum");
	   		 this.$hidenInfo = this.$el.find(".hidenInfo");	
	   		 this.allMatches = $(".jcarea").find("tr[matchtype]");
	   		 this.allMatchesNum = this.allMatches.length;
	   		 var me = this;
	   		 this.allMatches.attr("mainflag","0");
	   		$(".expOrclap").on("click",function(e){
	   	    	var ts = $(this).attr("timestamp"),
	   	    	    cont = $(this).find("span").html(),
	   	    	    $table = $(".jcarea");

	   	    	if(cont ==="显示"){
	   	    		var $tbody = $("tbody[timestamp="+ts+"]");
	   	    		var hiddenTr = $tbody.find("tr[flag=1]"); 
	   	    		if(hiddenTr.length>0){
	   	    			$tbody.find("tr:not([flag=1])").attr("mainflag","0").show();
	   	    		}else{
	   	    			$tbody.find("tr[mainflag=1]").attr("mainflag","0").show();
	   	    		}
	   	        	$(this).find("span").html("隐藏");
	   	    	}
	   	    	else if(cont ==="隐藏"){
	   	    		var $tbody = $("tbody[timestamp="+ts+"]");
	   	    		var hiddenTr = $tbody.find("tr[flag=1]");
	   	    		if(hiddenTr.length>0){
	   	    			$tbody.find("tr:not([flag=1])").attr("mainflag","1").hide()
	   	    		}else{
	   	    			$tbody.find("tr[mainflag=0]").attr("mainflag","1").hide();
	   	    		}	   	    			   	    	   
	   	        	$(this).find("span").html("显示");
	   	    	}  
	   	    	var hidenum =  $table.find("tr:hidden").length;
	   	    	if(hidenum==0){
	    			me.$hidenInfo.html('[已隐藏<i class="hidenMatchNum">'+hidenum+'</i>场]').hide();
	    		}else{
	    			me.$hidenInfo.html('[已隐藏<i class="hidenMatchNum">'+hidenum+'</i>场]').show();
	    		}
	   	    });
	    	},
	    	toggleRegion:function(e){
	    		var  me = this,
	    		     $this = $(e.currentTarget),
	    		     matchtype = $this.val(),
	    		     isChecked = $this.prop("checked"),    		    
	    		     $table = $(".jcarea");
	    		if(isChecked){
	    			$table.find("tr[matchtype="+matchtype+"]").attr("flag","0").show();	    			
	    		}else{
	    			$table.find("tr[matchtype="+matchtype+"]").attr("flag","1").hide();	
	    		}
	    		var hidenum =  $table.find("tr:hidden").length;
	    		if(hidenum==0){
	    			me.$hidenInfo.html('[已隐藏<i class="hidenMatchNum">'+hidenum+'</i>场]').hide();
	    		}else{
	    			me.$hidenInfo.html('[已隐藏<i class="hidenMatchNum">'+hidenum+'</i>场]').show();
	    		}
	    		e.stopPropagation();e.cancelBubble = true;
	    	},  
	    	showMatchesFilter:function(e){
	    		 var $this = $(e.currentTarget);
	    		 this.$selMatch.addClass("openmatch");
	    		 e.stopPropagation();
	    	},
	    	closeMatchBox:function(e){
	    		this.$selMatch.removeClass("openmatch");
	    		e.stopPropagation();e.cancelBubble = true;
	    	},
	    	selAllCheckbox:function(e){  
	        var $this = $(e.currentTarget),
	    		$matchbox = $this.closest(".matchbox");
	    		$matchbox.find("input").each(function(i,v){
	    			if(!$(v).prop("checked")){
	    				$(v).trigger("click");
	    			}
	    		});
	    		e.stopPropagation();e.cancelBubble = true;
	    	},
	    	deselAllCheckbox:function(e){
	    		var $this = $(e.currentTarget),
	    		    $checkbox = $this.closest(".matchbox");
	    		$checkbox.find("input").trigger("click");
	    		e.stopPropagation();e.cancelBubble = true;
	    	},  	    	  	
	    	showFiveLeagues:function(e){
	    		var $this = $(e.currentTarget),
	    		    leagues = $this.val(),
	    		    $checkbox = $this.closest(".onlyfive").prev().find("input[type=checkbox]");    		    
	    		    if($this.prop("checked")){//英超西甲德甲意甲法甲
	    		    	$checkbox.each(function(i,v){
	    		    		if(leagues.indexOf(v.value)>-1 && $(v).prop("checked") == false){
	    		    			$(v).trigger("click");
	    		    		}
	    		    		if(leagues.indexOf(v.value)<0 && $(v).prop("checked") == true){
	    		    		    $(v).trigger("click");
	    		    	    }
	    		    	});
	    		    	
	    		    }else{
	    		    	$checkbox.each(function(i,v){    		    		
	    		    		if($(v).prop("checked") == false){
	    		    		    $(v).trigger("click");
	    		    	    }
	    		    	});
	    		    }
	    		    e.stopPropagation();e.cancelBubble = true; 
	    	},
	    	showAllMatches:function(e){    			    		
	    		var $this = $(e.currentTarget),
	    		    $checkbox = this.$matchbox.find("input[type=checkbox]"); 
	    		    $checkbox.each(function(i,v){    		    		
		    		if($(v).prop("checked") == false){
		    		    $(v).trigger("click");
		    	    }
		    	});
	    		$(".expOrclap").each(function(i,v){
	    			if($(v).find("span").html()==="显示"){
	    				$(v).trigger("click");
	    			}
	    		});
	    		$(".jcarea").find("tr[matchid]:hidden").show();
	    		this.$hidenInfo.html('[已隐藏<i class="hidenMatchNum">0</i>场]').hide();
	    		this.$ofive.prop("checked",false);
	    		this.$only_sel.prop("checked",false);
	    		e.stopPropagation();e.cancelBubble = true;    
	    	}
	    });
	    var topView = new TopView();
	}
})