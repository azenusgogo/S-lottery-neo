define(function () {
    var Tab = function (options) {
        options || (options = {});
        this.$t = options.$t;
        this.$c = options.$c;
        this.fn = options.fn;
        this.i = 0;
        this.onload = options.onload;
        this.events = options.events || "click";
        this.tClassName = options.tClassName || "";
        this.cClassName = options.cClassName || "";
        this.delay = options.delay;
        this.preventDefault = typeof options.preventDefault == "undefined" ? true : options.preventDefault;
        this.init()
    };
    Tab.prototype = {
        constructor: Tab,
        $: function (selector) {
            return this.$c.find(selector);
        },
        _$: function (selector) {
            return this.$t.find(selector);
        },
        init: function () {
            var _this = this;
            this.$t.each(function () {
                var t;
                var evt = _this.events == "mouseover" ? "mouseenter" : _this.events;
                $(this).on(evt, function (e) {
                    var $this = $(this);
                    if (_this.delay) {
                        t = setTimeout(function () {
                            _this.to($this.index());
                        }, _this.delay)
                    } else {
                        _this.to($this.index());
                    }
                    _this.preventDefault && e.preventDefault()
                });
                if (_this.events == "mouseover") {
                    $(this).on("mouseleave", function (e) {
                        t && clearTimeout(t)
                    });
                }
            });
            this.onload && this.onload()
        },
        to: function (i) {
            typeof i || (i = 0);
            var $t = this.$t;
            var $c = this.$c;
            $t.removeClass(this.tClassName)
            $t.eq(i).addClass(this.tClassName)
            $c.removeClass(this.cClassName).hide();
            $c.eq(i).addClass(this.cClassName).show();
            this.fn && this.fn(i);
            this.i = i
        }
    }
    return Tab
})