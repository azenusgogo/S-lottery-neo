define(function(){	
		return function(options) {
			var defaults = {
				wraperId:"gotop",
				offsetHeight : 350,
				speed : 300,
				duration:{fadein:500,fadeout:400},
				norClass:"anchor_nor",
				hoverClass :"anchor_in",
				shresholdClass :".hot-links"
			};
			var options = $.extend(defaults,options);
			$("body").prepend("<div id='"+options.wraperId+"' class='"+options.norClass+"' title='返回顶部'></div>");
			var $toTop = $(this)||$(window);
			var $top = $('#'+options.wraperId);	
			var preStyleObj = $top[0].style;
		    var _shresholdDom = $(options.shresholdClass);
		    var total_height = _shresholdDom.offset().top;
		    var domStyle = function(dom,key){
		        if(dom.style[key]){
		            return dom.style[key];
		        }
		        else if(dom.currentStyle){
		            return dom.currentStyle[key];
		        }
		        else if(document.defaultView && document.defaultView.getComputedStyle){
		            style = key.replace(/([A-Z])/g, '-$1').toLowerCase();		            
		            return document.defaultView.getComputedStyle(dom, null).getPropertyValue(key);
		        }
		        
		        return null;
		    };
			var isShow = function(w){
			    var scrolltop = w.scrollTop(),me=arguments.callee;			    
				if(scrolltop<options.offsetHeight){				
					$top.fadeOut(options.duration.fadeout);
				}else if((scrolltop-$top.height())<=total_height-w.height() && scrolltop>=options.offsetHeight){
					if($top.css("display")!=="block"){
						$top.fadeIn(options.duration.fadein);
					}
					if(!me.setCssed){
						$top[0].style.cssText = "_background-color:url(about:blank);_background-attachment:fixed;display:block;bottom:30px;position:fixed;top:'';_position: absolute;_top: expression(600+((e=document.documentElement.scrollTop)?e:document.body.scrollTop)+'px');_right: expression(505+((e=document.documentElement.scrollLeft)?e:document.body.scrollLeft)+'px')";//{"bottom":domStyle($top[0],"bottom"),"position":"fixed","top":""}
						me.setCssed = 1;
					}
				}else if(total_height-scrolltop<w.height()){					
					$top.css({"top":total_height+"px","position":"absolute"});
					if($top.css("display")!=="block"){
						$top.fadeIn(options.duration.fadein);
					}
					me.setCssed = 0;
				}								
			};
			$toTop.scroll(function(){
				var $this = $(this);
				isShow($this);
			});	
			$top.hover(function(){ 		
				$(this).toggleClass(options.norClass).toggleClass(options.hoverClass);
			},function(){					
				$(this).toggleClass(options.hoverClass).toggleClass(options.norClass);
			});	
			$top.click(function(){				
				$("html,body").animate({scrollTop: 0},options.speed);
				return false;
			});
			isShow($toTop);
		};
	
})