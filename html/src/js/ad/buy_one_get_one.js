define([
    "lib/dialog"
],function(Dialog){
    return function(){
        // 买一送一
        var id = _.uniqueId("DIALOG_");
        var tpl = '\
          <div class="dialog buy_one_get_one">\
            <div class="pic">\
    	      <div class="cxt">\
    	        <span class="honey">亲爱的</span>&nbsp;<span class="nick_name"></span><br />\
    	               恭喜您！获得一个<span class="hongbao">2元红包</span>，奖励已派发请您注意查收<br />\
    	               您可以<a href="/goucai/" class="blue">继续购彩</a>，也可以查看&nbsp;<a class="blue" href="/login/user/trans/trans.html">资金明细</a>\
    	      </div>\
    	    </div>\
            <a href="javascript:;"  class="btn">确&nbsp;定</a>\
            <a href="javascript:;" class="ct dialog-close">关闭</a>\
           </div>\
        ';
        $(tpl).attr("id",id).prependTo("body");
        var buy1get1 = new Dialog({
            el:"#"+id,
            width:414,
            onload:function(){
                var  me = this;
                     this.$close = me.$(".dialog-close");
                     this.$nick_name = me.$(".nick_name");
                     this.$btn = me.$(".btn");
                     this.$btn.on("click",function(){
                    	me.$close.trigger("click");                   	
                	    return false;
                    })
            },
            onshow:function(){
            	var data  = this.data;
            	this.$nick_name.html(data.nickName);             	    
            },
            onhide:function(){
            	this.complete.show();
            }
        });       
        return buy1get1;
       
    }
})