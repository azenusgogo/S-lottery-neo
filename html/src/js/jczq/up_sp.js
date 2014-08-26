/**
 * Created by caojungang on 2014/6/6.
 */
define(function() {
    return function(viewArr, gamenid) {
        // var x = 0;
        var _updata = function() {
            $.post("/ajax/sport/" + gamenid + "/sp.html", function(data) {
                _.each(viewArr, function(view) {
                    var model = view.model;
                    var matchid = model.get("matchid");
                    var code = model.get("code");
                    var d = data[matchid];
                    if (d && d[code]) {
                        var sp = pard[code] || 1;
                        var token = "";

                        if (gamenid == "jczqspfp" || gamenid == "jczqrsfp") {
                            if (sp.indexOf("\u2193") > -1) {
                                token = '<span class="green">\u2193</span>'
                            } else if (sp.indexOf("\u2191") > -1) {
                                token = '<span class="red">\u2191</span>'
                            }
                        }

                        sp = sp.replace(/\u2193|\u2191/g, "")
                        model.set("sp", sp ? sp : 1, {
                            silent: true
                        });
                        view.$el.html(sp ? sp : "--" + token)
                    }
                })
            }, "json").error(function(data) {
                //                data = {};
                //                x++;
                //                for (var i = 0; i < 5; i++) {
                //                    for (var j = 0; j < 4; j++) {
                //                        data["2014052570" + i + j] = {
                //                            "11103": "1" + x,
                //                            "11100": "1" + x,
                //                            "11101": "1" + x
                //                        }
                //                    }
                //                }
                //                _.each(viewArr, function (view) {
                //                    var model = view.model;
                //                    var matchid = model.get("matchid");
                //                    var code = model.get("code");
                //                    var d = data[matchid];
                //                    if (d && d[code]) {
                //                        model.set("sp", d[code], {silent: true});
                //                        view.$el.html(d[code])
                //                    }
                //                })
            })
        };

        setInterval(function() {
            _updata()
        }, 180000);
        //1000 * 60 * 3

    }
});
