/*
头部 加入收藏 设为首页
Created by hanlongfei on 2014-02-18.
*/

define(["lib/alert"], function (dialogAlert){

	return function(){
		var $addFav = $("#topFunc").find(".addfav");
		var $setHome = $("#topFunc").find(".sethome");
		var Alert = dialogAlert("提示");

		//var $todesktop = $("#topFunc").find(".todesktop");
		// var toDesktop = function (sUrl,sName){

		// 	try{
		// 		var WshShell = new ActiveXObject("WScript.Shell");
		// 		var oUrlLink = WshShell.CreateShortcut(WshShell.SpecialFolders("Desktop") +
		// 			"\\" + sName + ".url");
		// 		oUrlLink.TargetPath = sUrl;
		// 		oUrlLink.Save();
		// 	}
		// 	catch(e){
		// 		Alert.show(function () {
		// 			this.$(".txt-center").html("当前浏览器安全级别不允许操作！请设置后在操作");
		// 		});
		// 	}
		// }

		var addfavorite = function (){
			var url = "http://cp.sogou.com";
			var title = "搜狗彩票";

			if( window.external ) { // IE chrome
				try{
					window.external.AddFavorite( url, title);
				}
				catch(e){
					Alert.show(function () {
	                    this.$(".txt-center").html("您的浏览器不支持自动收藏，请使用CTRL+D");
	                });
				}
				
			}else if(window.sidebar.addPanel) { // Firefox
				window.sidebar.addPanel(title, url,"");
			}else {
				Alert.show(function () {
                    this.$(".txt-center").html("您的浏览器不支持自动收藏，请使用CTRL+D");
                });
			}
		}

		var setHomepage = function (){ // 设置首页

			var url = "http://cp.sogou.com/";

			if (document.all) {
				document.body.style.behavior = 'url(#default#homepage)';
				document.body.setHomePage(url);
			}else{
				Alert.show(function () {
                    this.$(".txt-center").html("抱歉，您的浏览器不支持此操作，<br />\
                    	您可以在浏览器选项中手动添加！");
                });
			}
		}

		$addFav.on("click",function(){
			addfavorite();
			return false;
		});

		$setHome.on("click",function(){
			setHomepage();
			return false;
		});
	}
});