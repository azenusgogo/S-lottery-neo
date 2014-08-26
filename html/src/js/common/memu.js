/**
 * Created by caojungang on 2014/4/12.
 */
define(function () {
    return function () {
        // 导航菜单
        $("#nav_memu").hover(function () {
            var $this = $(this);
            var $memu = $this.data("memu");
            if (!$memu) {
                $memu = $this.find("ul");
                $this.data("memu", $memu)
            }
            $memu.show()
        }, function () {
            var $memu = $(this).data("memu");
            $memu && $memu.hide()
        });
    }
})