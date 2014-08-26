define([
    "lib/dialog"
],function(Dialog){
    var id = _.uniqueId("DIALOG_");
    var tpl = '\
        <div class="dialog index_reg_ad">\
            <div class="pic"></div>\
            <a href="#" class="ct close dialog-close">关闭</a>\
            <a href="#" class="ct btn dialog-close">立即体验</a>\
        </div>\
    ';
    $(tpl).attr("id",id).prependTo("body");
    var dialog = new Dialog({
        el:"#"+id,
        width:679,
        onload:function(){
            
        }
    })
    return dialog
})