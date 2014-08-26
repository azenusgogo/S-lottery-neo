define(function() {
    return function() {
        var _isIE6 = window.VBArray && !window.XMLHttpRequest;
        if (_isIE6) return;
        var $footerfix = $("#footerfix");
        var $footerfixClone = $("#footerfixClone");

        var top = $footerfix.offset().top;
        var $document = $(document);
        var $window = $(window);
        var elHeight = 170;
        var _render = function() {
            if ($footerfixClone.is(":visible")) {
                top = $footerfixClone.offset().top
            } else {
                top = $footerfix.offset().top
            }
            var sTop = $document.scrollTop();
            var height = $window.height();
            var t = sTop + height - elHeight;
            if (t >= top) {
                $footerfixClone.hide();
                $footerfix.css({
                    "position": "relative"
                })
            } else {
                $footerfixClone.show();
                $footerfix.css({
                    "position": "fixed",
                    "bottom": 0
                })
            }
        };

        $(window).on("scroll reset load", function() {
            _render()
        })
    }
});
