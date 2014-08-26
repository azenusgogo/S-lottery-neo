define(["lib/dialog"], function (Dialog) {

    var tpl = '\
        <div class="dialog">\
            <div class="modal">\
                <div class="modcon">\
                    <div class="title">\
                        <i title="关闭" class="close dialog-close">&times;</i>\
                        <span class="dialog-title"></span>\
                    </div>\
                    <div class="modbox">\
                       <p class="txt-center"></p>\
                       <div class="modbtn"><a class="fbtn long" href="#">确 定</a></div>\
                    </div>\
                </div>\
            </div>\
        </div>\
    ';

    return function (title) {
        var id = _.uniqueId("DIALOG_");
        var $el = $(tpl).attr("id", id);
        var dialog = new Dialog({
            el: $el,
            width: 366,
            zIndex: 100001,
            mask: {
                zIndex: 100000
            },
            onload: function () {
                var _this = this;
                this.redirect = false; // 是否刷新
                this.$(".dialog-title").html(title);
                this.$(".fbtn").add(this.$(".dialog-close")).on("click", function () {
                    _this.hide(); // del ......
                    if (_this.redirect !== false) {
                        if (_this.redirect === true) {
                            location.reload()
                        } else {
                            location.href = _this.redirect
                        }
                    }
                    return false
                })
            },
            onshow: function () {
                if ($("#" + id).length) return;
                $el.prependTo("body")
            }
        })
        return dialog
    }
})
