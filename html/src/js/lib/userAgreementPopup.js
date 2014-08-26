define(["lib/dialog"],function(Dialog){
	var userAgreementPopup = function(cntId,title){
		var id = _.uniqueId("DIALOG_"),		 
	    cnt = document.getElementById(cntId).innerHTML,
	    wrapperClass = cntId ==="agentProtocal" ? "userAgreement":"risk", //wrapperClass为内容外层容器的className
	    dialogWidth = cntId ==="agentProtocal" ? 800 : 600,
        struct = '<div  id='+id+' class="dialog"><div class="modal"><div class="modcon"><div class="title"><i title="关闭" class="dialog-close">×</i>'+title+'</div><div class="'+wrapperClass+'"></div></div></div></div>';
    (new Dialog({width:dialogWidth,el:struct,onshow:function(){
    	 if ($("#" + id).length) return;
         this.$el.prependTo("body");
    	 this.$("."+wrapperClass).html(cnt);
    }})).show();    
    return false
	}
	return userAgreementPopup;
})