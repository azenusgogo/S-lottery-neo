define(["lib/mask"], function (Mask) {
    var _pluginMask = new Mask;

    var _$window = $(window);
    var _$document = $(document);
    var _$html = $("html");
    var _elem = document.documentElement;
    var _body = document.body;
    var _isIE6 = window.VBArray && !window.XMLHttpRequest;

    var Dialog = function (options) {
        this.$el = $(options.el);
        this.el = this.$el[0];
        this.top = options.top || "auto";
        this.left = options.left || "auto";
        this.width = options.width || "auto";
        this.height = options.height || "auto";
        this.zIndex = options.zIndex || 99999;
        this.onload = options.onload || function () {
        };
        this.onbeforeshow = options.onbeforeshow || function () {
        };
        this.onshow = options.onshow || function () {
        };
        this.onbeforehide = options.onbeforehide || function () {
        };
        this.onhide = options.onhide || function () {
        };
        this.mask = options.mask ? new Mask(options.mask) : _pluginMask;
        this.init()
    };
    Dialog.prototype = {
        constructor: Dialog,
        $: function (selector) {
            return this.$el.find(selector);
        },
        init: function () {
            var _this = this;
            this.$(".dialog-close").on("click", function () {
                _this.hide();
                return false
            });
            this.onload()
        },
        show: function (fn) {
            var _this = this;
            var r = this.onbeforeshow();
            if (r == false) return;
            _$window.on("resize.set", _.debounce(function () {
                _this.set()
            }, 300));
            _$window.on("scroll.set", _.debounce(function () {
                _this.set()
            }, 300));
            fn && fn.call(this);
            this.mask.show();
            this.onshow();
            this.$el.show();
            this.set(0);
        },
        hide: function (fn) {
            var r = this.onbeforehide();
            if (r == false) return;
            _$window.off("resize.set");
            _$window.off("scroll.set");
            fn && fn.call(this);
            this.$el.hide();
            this.mask.hide();
            this.onhide()
        },
        set: function (n) {
            n = typeof n == "undefined" ? 300 : n;
            var width = this.width; //if (this.width == "auto") width =  this.$el.width()
            var height = this.$el.height(); // if (this.height == "auto") height =  this.$el.height()
            var top = this.top == "auto" ? (_$window.height() - height) * 0.382 : this.top;
            var left = this.left == "auto" ? (_$window.width() - width) / 2 : this.left;
            var sTop = _$document.scrollTop();
            this.$el.stop().css({
                width: width,
                position: "absolute",
                zIndex: this.zIndex
            }).animate({
                left: left,
                top: top + sTop
            }, n)
        }
    };
    return Dialog
})