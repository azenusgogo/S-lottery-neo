/**
 * Created by caojungang on 2014/4/10.
 */
define(function () {
    var Slider = function (opts) {
        this.$el = $(opts.el);
        if (this.$el.length < 1) return;
        this.el = this.$el[0];
        this.$main = $(opts.main);
        this.delay = opts.delay || 5000; // 延迟
        this.speed = opts.speed || 1500; // 动画速度
        this.step = opts.step; // 滚动步长
        this.margin = opts.margin || 0;
        this.complate = opts.complate;
        this.onload = opts.onload;
        this.timeout = null;
        this.init()
    };
    Slider.prototype = {
        init: function () {
            var _this = this;
            this.$el.hover(function () {
                _this.stop()
            }, function () {
                _this.start()
            });
            this.onload && this.onload();
            this.start();
        },
        start: function () {
            var height = this.$el.height() + this.margin;
            var $main = this.$main;
            var _this = this;
            clearTimeout(this.timeout);
            this.timeout = setTimeout(function () {
                var $el = $main.find("li:lt(" + _this.step + ")");
                $main.append($el.clone());
                $main.animate({
                    top: -height
                }, _this.speed, function () {
                    $main.css("top", 0);
                    $main.find("li:lt(" + _this.step + ")").remove();
                    _this.complate && _this.complate($main.find(':first-child').attr("data-url"));
                    _this.start();
                })
            }, this.delay)
        },
        stop: function () {
            clearTimeout(this.timeout);
        }

    };
    return Slider
});