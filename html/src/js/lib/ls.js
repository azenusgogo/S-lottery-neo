define(function() { //["lib/jstorage","lib/json2"], jstorage
    return function(gameid) {
        //jstorage();
        /* **********************组装数据 ************************ */

        var models = this.models.models;

        var i, red, blue, red_dm, str, token, $addinput,
            mul = this.$mul.val() || 1,
            len = models.length,
            arr = [],
            arr1, me = this;
        var _pad = function(v) {
            if (!v) return;
            var i;
            for (i = v.length; i--;) {
                v[i] = +v[i];
                v[i] = v[i].toString().length == 1 ? ("0" + v[i]) : v[i]
            }
        };

        var getBytes = function(str) {
            var len = 0
            for (var i = str.length; i--;) {
                if (str.charAt(i) > '~') {
                    len += 2
                } else {
                    len += 1
                }
            }
            return len
        };
        var _this = this;

        var makeData = function(gameId) {
            if (gameId == "ssq") {
                // 拼数据
                for (i = 0; i < len; i++) {
                    red = models[i].get("red");
                    blue = models[i].get("blue");
                    red_dm = models[i].get("red_dm");
                    _pad(red);
                    _pad(blue);
                    _pad(red_dm);
                    arr[i] || (arr[i] = []);
                    str = red.join(" ") + ":" + blue.join(" ");
                    str = (red_dm && red_dm.length) ? red_dm.join(" ") + "$" + str : str;
                    arr[i].push(str)
                }
            } else if (gameId == "dlt") {
                $addinput = $("#dltAdd");
                token = $addinput.prop("checked") ? "+" : "";
                for (i = 0; i < len; i++) {
                    red = models[i].get("red");
                    blue = models[i].get("blue");
                    red_dm = models[i].get("red_dm");
                    blue_dm = models[i].get("blue_dm");
                    _pad(red);
                    _pad(blue);
                    _pad(red_dm);
                    _pad(blue_dm);
                    arr[i] || (arr[i] = []);
                    str = red.join(" ") + ":" + ((blue_dm && blue_dm.length) > 0 ? blue_dm.join(" ") + "$" + blue.join(" ") : blue.join(" "));
                    str = (red_dm && red_dm.length) > 0 ? token + red_dm.join(" ") + "$" + str : token + str;
                    arr[i].push(str)
                }
            } else if (gameId == "qxc") {
                for (i = 0; i < len; i++) {
                    arr[i] || (arr[i] = []);
                    arr[i].push([
                        models[i].get("row1").join(""),
                        models[i].get("row2").join(""),
                        models[i].get("row3").join(""),
                        models[i].get("row4").join(""),
                        models[i].get("row5").join(""),
                        models[i].get("row6").join(""),
                        models[i].get("row7").join("")
                    ].join(" "))
                }
            } else if (gameId == "qlc") {
                // 拼数据
                for (i = 0; i < len; i++) {
                    red = models[i].get("red");
                    red_dm = models[i].get("red_dm");
                    _pad(red);
                    _pad(red_dm);
                    arr[i] || (arr[i] = []);
                    str = red.join(" ");
                    str = (red_dm && red_dm.length) ? red_dm.join(" ") + "$" + str : str;
                    arr[i].push(str)
                }
            } else if (gameId == "k3" || gameId == "k3js" || gameId == "k3gx" || gameId == "k3jl") {
                var bet_type, number;
                for (i = 0; i < len; i++) {
                    bet_type = models[i].get("bet_type");
                    number = models[i].get("number");
                    red_dm = models[i].get("red_dm");
                    if (bet_type == 0) {
                        str = "HZ_" + number.join(",");
                    } else if (bet_type == 1) {
                        str = "AAA_*"
                    } else if (bet_type == 2) {
                        str = "AAA_" + number.join("");
                    } else if (bet_type == 3) {
                        str = (red_dm && red_dm.length > 0) ? "3BT_" + red_dm.join("") + "$" + number.join("") : "3BT_" + number.join("")
                    } else if (bet_type == 4) {
                        str = "3LH_*"
                    } else if (bet_type == 5) {
                        _.each(number, function(v, i, n) {
                            n[i] = v.replace("*", "")
                        });
                        str = "AA_" + number.join("");
                    } else if (bet_type == 6) {
                        _.each(number, function(v, i, n) {
                            v = v.replace("|", "");
                            n[i] = v.slice(0, 2) + "|" + v.slice(2, 3)
                        })
                        str = "AAX_" + number.join("");
                    } else if (bet_type == 7) {
                        str = (red_dm && red_dm.length > 0) ? "2BT_" + red_dm.join("") + "$" + number.join("") : "2BT_" + number.join("")
                    }
                    arr.push(str);
                }
            } else if (gameId == "f14") {
                for (i = 0; i < len; i++) {
                    arr1 = [];
                    _.each(models[i].get("number"), function(obj) {
                        arr1.push(obj.n.join(""))
                    })
                    arr.push(arr1.join(" "))
                }
            } else if (gameId == "f9") {
                for (i = 0; i < len; i++) {
                    arr1 = [];
                    _.each(models[i].get("number"), function(obj) {
                        arr1.push(obj.n.join("") + (obj.checked ? "$" : ""))
                    })
                    arr.push(arr1.join(" "))
                }
            } else if (gameId == "bqc" || gameId == "zjq") {
                models = me.models;
                 var   passtype = [],matches = [];
                    
                    mul = +me.$mul.val() || 1;
                   // price = +me.$countMoney.html().replace(/[\u4e00-\u9faf]+/g, "");

                _.each(models.count()[1], function(matchid, k) {
                    var sparr = [];
                    var isDan = $(me.dmArr[matchid.toString()]).prop("checked") ? 1 : 0;
                    _.each(models.where({
                        matchid: matchid
                    }), function(model, i) {
                        sparr[i] = model.get("code");
                    });
                    matches[k] = matchid + ":" + sparr.join(".") + ":" + isDan
                });
                me.$("input[passtype]:checked").each(function(i, v) {
                    passtype[i] = $(v).attr("passtype")
                });
                arr = [matches.join(" ") + "|" + passtype.join(",").replace(/串/g, "_")];
            } else if (gameId == "hhgg_spf_rqspf_bf") {
                var obj = _this.models.toJSON();
                var matchObj = {};
                var dmHash = _this.dmHash;
                _.each(obj, function(o) {
                    matchObj[o.matchid] || (matchObj[o.matchid] = []);
                    matchObj[o.matchid].push(o.code)
                });

                _.each(matchObj, function(val, key) {
                    arr.push(key + ":" + val.join(".") + ":" + +dmHash[key].checked)
                });

                arr = [arr.join(" ") + "|" + _this.getChuan().join(",").replace(/串/g, "_").replace("单关", "1_1")];

                mul = +_this.$mul.val();

                var price = parseFloat($.trim(_this.$countMoney.html())) * 100;
            }
            for (var k = 0; k < arr.length; k++) {
                arr[k] = arr[k] + "#" + mul;
            }

            return arr.join(";");
        };
        // return {
        //     setItem: function(key, time) {
        //         var madeData = makeData(gameid),
        //             target = {
        //                 data: madeData,
        //                 gameId: gameid
        //             };
        //         if (getBytes(madeData) <= 60 * 1024) {
        //             var dataStr = JSON.stringify(target);
        //             $.jstorage.set(key, dataStr);
        //             $.jStorage.setTTL(key, time);
        //         } else {
        //             console.log("存储内容大于2K");
        //         }
        //     },
        //     getItem: function(key) {
        //         if ($.jStorage.getTTL(key) < 0) {
        //             $.jStorage.flush();
        //         }
        //         return $.jStorage.get(key);
        //     },
        //     clear: function() {
        //         $.jStorage.flush();
        //     }
        // };


        return {
            "data": makeData(gameid),
            "gameId": gameid
        }
    }
})
