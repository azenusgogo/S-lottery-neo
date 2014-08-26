define(function(){
    var _$window = $(window);
    var _$document = $(document);
    var _$html = $("html");
    var _elem = document.documentElement;
    var _body = document.body;
    var _isIE6 = window.VBArray && !window.XMLHttpRequest;

    var Mask = function(options){
        options || (options = {});
        this.bgc = options.bgc || "#000";
        this.opacity = options.opacity || .3;
        this.zIndex = options.zIndex || 9999;
        this.id = options.id || _.uniqueId("MASK_");
        this.init()
    }
    Mask.prototype = {
        constructor : Mask,
        show:function(){
            var $el = this.$el;
            if (_isIE6){
                _$window.on("resize.re",_.debounce(function(){
                    // .hide().show() 解决iframe 高度不变 bug
                    $el.css("height",_$window.height()).hide().show()
                }, 300));
                $el.css("height",_$window.height());
                this.el.style.setExpression('top', 'eval(document.documentElement.scrollTop)');
            }
            $el.show();
        },
        hide:function(){
            if (this.el){
                if (_isIE6){
                    this.el.style.removeExpression('top');
                    _$window.off("resize.re")
                }
                this.$el.hide();
            }
        },
        init:function(){
            !this.el && this.render();
            var $el = this.$el;
            if (_isIE6){
                if (_$html.css("backgroundAttachment") != 'fixed'){
                    _$html.css({
                        zoom: 1,// 避免偶尔出现body背景图片异常的情况
                        backgroundImage: 'url(about:blank)',
                        backgroundAttachment: 'fixed'
                    });
                }
                
            }
        },
        render:function(){
            var cssOptions = {
                width:"100%",
                backgroundColor:this.bgc,
                left:0,
                top:0,
                display:"none",
                zIndex:this.zIndex
            }
            if (_isIE6){
                cssOptions.position = "absolute";
                cssOptions.filter = "alpha(opacity="+this.opacity*100+")"
            }else{
                cssOptions.height = "100%";
                cssOptions.position = "fixed";
                cssOptions.opacity = this.opacity
            }
            var $el = $("<div>",{
                id:this.id,
                css:cssOptions
            });
            if (_isIE6){
                $el.append('\
                    <iframe src="about:blank" style="width:100%;height:100%;position:absolute;\
                    top:0;left:0;z-index:-1;filter:alpha(opacity=0)"></iframe>\
                ')
            }
            this.$el = $el;
            this.el = $el[0];
            $el.prependTo("body");
        }
    }
    return Mask

})