define([
    "lib/dialog",
    "text!update/art.tpl"
],function(Dialog, art_tpl){
    var id = _.uniqueId("DIALOG_");
    $(art_tpl).attr("id",id).prependTo("body");

    var _redirect = function(){
        if (this.url !== false){
            if (this.url === true){
                location.reload()
            }else{
                loaction.href = this.url
            }
        }
    }


    var art = new Dialog({
        el:"#"+id,
        width:420,
        onload:function(){
            var _this = this;
            this.$time = this.$(".time");
            this.$prev_period = this.$(".prev_period");
            this.$period = this.$(".period");
            this.url = false;
            this.$(".dialog-close").on("click",function(){
                _redirect.call(_this)
                return false
            });
        },
        onshow:function(){
            var $time = this.$time;
            var i = 10;
            $time.html(i);
            this.t && clearInterval(this.t);
            var _this = this;
            this.t = setInterval(function(){
                $time.html(--i);
                if (i<0){
                    _this.hide();
                    _redirect.call(_this)
                }
            },1000)
        },
        onhide:function(){
            this.t && clearInterval(this.t);
        }
    });
    return art

})