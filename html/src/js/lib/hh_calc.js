define(function(){
	var C = {};
	Array.prototype.filter = function(e) {
		var b = [], d = arguments[1];
		for (var c = 0; c < this.length; c++) {
			if (e.call(d, this[c])) {
				b.push(this[c])
			}
		}
		return b
	};
	C.SBonus = {
			betArray : [],
			betMap : {},
			init : function() {
				var h = this.betArray, b = this.betMap;
				for (var e = 0; e < 6; e++) {
					for (var d = 0; d < 6; d++) {
						if ((e == 3 && d > 3) || (e > 3 && d > 2)) {
							continue
						}
						h.push({
									name : e + "" + d,
									sum : e + d,
									diff : Math.abs(e - d),
									spf : (e > d) ? 3 : (e < d ? 0 : 1)
								})
					}
				}
				h.push({
							name : "90",
							sum : 7,
							spf : 3
						}, {
							name : "99",
							sum : 7,
							spf : 1
						}, {
							name : "09",
							sum : 7,
							spf : 0
						});
				for (e = h.length; e--;) {
					var k = h[e], g = {}, f = k.sum, c = k.spf;
					g["bf-" + k.name] = 1;
					g["jqs-" + f] = 1;
					g["spf-" + c] = 1;
					if (c == 3) {
						if (f > 2) {
							g["bqc-03"] = 1
						}
						g["bqc-13"] = 1;
						g["bqc-33"] = 1
					} else {
						if (c == 1) {
							if (f > 1) {
								g["bqc-01"] = 1;
								g["bqc-31"] = 1
							}
							g["bqc-11"] = 1
						} else {
							if (c == 0) {
								g["bqc-00"] = 1;
								g["bqc-10"] = 1;
								if (f > 2) {
									g["bqc-30"] = 1
								}
							}
						}
					}
					b[k.name] = g
				}
			},
			getBonusRange : function(c, d) {
				var b = this.getHitList(c);
				return {
					min : this.getMinBonus(b, d),
					max : this.getMaxBonus(b, d)
				}
			},
			getMinBonus : function(b, e) {
				var c = e.sort()[0].slice(0, 1), d = this.sortHitList(b.min, false);
				d.tList = d.tList.slice(0, c - d.dList.length);
				return this.getBonus(d, c)
			},
			getMaxBonus : function(d, h) {
				var g = this.sortHitList(d.max, true), f = 0;
				for (var e = 0, c = h.length; e < c; e++) {
					var b = Number(h[e].slice(0, 1));
					b = isNaN(b) ? 1 : b;
					f += this.getBonus(g, b)
				}
				return f
			},
			getBonus : function(p, c) {
				var q = p.dList.length, o, m, d, t, b, s = 0;
				if (q) {
					m = this.combine(p.dList)
				}
				d = this.combine2(p.tList, c - q);
				o = q ? this.combine([m, d]) : d;
				for (var h = 0, e = o.length; h < e; h++) {
					t = o[h].toString().split(",");
					b = 1;
					for (var g = 0, f = t.length; g < f; g++) {
						b *= parseFloat(t[g])
					}
					s += b
				}
				return s
			},
			sortHitList : function(c, f) {
				var e = [], d = [], g = 0, b = c.length, h;
				f = f ? -1 : 1;
				c.sort(function(j, i) {
							if (j.isdan === i.isdan) {
								return (j.sum > i.sum ? 1 : -1) * f
							} else {
								return j.isdan ? -1 : 1
							}
						});
				for (; g < b; g++) {
					h = c[g];
					if (h.isdan) {
						e.push(h)
					} else {
						d.push(h)
					}
				}
				return {
					list : c,
					dList : e,
					tList : d
				}
			},
			getHitList : function(e) {

				var h = [], c = [], g, d;
//                console.log(JSON.stringify(e));
//                console.log(JSON.stringify(this.betMap))
				for (var f = 0, b = e.length; f < b; f++) {
					g = e[f];
					if (g) {												
						d = this.getSgBound(g);
						h.push(d.min);
						c.push(d.max)
					}
				}
				h.sort(function(j, i) {
							return j.sum > i.sum ? 1 : -1
						});
				c.sort(function(j, i) {
							return j.sum > i.sum ? -1 : 1
						});
				return {
					min : h,
					max : c
				}
			},
			getSgBound : function(t) {
				var v = this.betArray, e = t.split("|"), y = t.indexOf("D") > -1, w = 9000000000, h = -1, x, c, b, g;				
				for (var u = 0, q = v.length; u < q; u++) {
					var f = v[u], m = this.combine(this.filterInvalidOpts(e, f)), d, o;
					if (m.length) {
						for (var s = 0, r = m.length; s < r; s++) {
							o = m[s];
							d = 0;
							for (var p = o.length; p--;) {
								o[p] = parseFloat(o[p].split("#")[1]) || 1;
								d += o[p]
							}
							if (d > h) {
								h = d;
								c = o.concat();
								g = f.name
							}
							if (d < w) {
								w = d;
								x = o.concat();
								b = f.name
							}
						}
					}
				}
				x.sum = w;
				x.bf = b;
				c.sum = h;
				c.bf = g;
				x.isdan = c.isdan = y;
				return {
					min : x,
					max : c
				}
			},
			filterInvalidOpts : function(e, h) {
				var c = this, d = [], g = 0, b = e.length;
				for (; g < b; g++) {
					var f = e[g].split(",").filter(function(i) {
								return c.testByBf(i, h)
							});
					if (f.length) {
						d.push(f)
					}
				}
				return d
			},
			testByBf : function(d, b) {
				if (d.indexOf("rqspf") === 0) {
					return this.testRqSpfByBf(d, b)
				}
				var c = this.betMap[b.name];
				return d.split("#")[0] in c
			},
			testRqSpfByBf : function(d, b) {
				var c = parseInt(d.split("#")[0].split("@")[1], 10);
				if (c > 0) {
					if (b.name == "09") {
						if (c === 1) {
							return d.indexOf("rqspf-0") === 0
									|| d.indexOf("rqspf-1") === 0
						}
						return d.indexOf("rqspf-") === 0
					}
					if (b.spf < 1) {
						if (c < b.diff) {
							return d.indexOf("rqspf-0") === 0
						} else {
							if (c === b.diff) {
								return d.indexOf("rqspf-1") === 0
							}
						}
					}
					return d.indexOf("rqspf-3") === 0
				} else {
					c = Math.abs(c);
					if (b.name == "90") {
						if (c === 1) {
							return d.indexOf("rqspf-3") === 0
									|| d.indexOf("rqspf-1") === 0
						}
						return d.indexOf("rqspf-") === 0
					}
					if (b.spf > 0) {
						if (c < b.diff) {
							return d.indexOf("rqspf-3") === 0
						} else {
							if (c === b.diff) {
								return d.indexOf("rqspf-1") === 0
							}
						}
					}
					return d.indexOf("rqspf-0") === 0
				}
			},
			combine : function(d, f) {
				var h = 0, e = [], c = [], b = (typeof f === "function");
				function g(k, p) {
					if (p >= k.length) {
						if (!b || f(code) !== false) {
							e.push(c.concat())
						}
						c.length = p - 1
					} else {
						var o = k[p];
						for (var m = 0, l = o.length; m < l; m++) {
							c.push(o[m]);
							g(k, p + 1)
						}
						if (p) {
							c.length = p - 1
						}
					}
				}
				if (d.length) {
					g(d, h)
				}
				return e
			},
			combine2 : function(g, d, h) {
				var c = 0, f = [], j = [], e = g.length, b = (typeof h === "function");
				function i(l, r) {
					if (j.length >= d) {
						if (!b || h(code) !== false) {
							f.push(j.concat())
						}
						j.length--
					} else {
						for (var m = r; m < e; m++) {
							var q = l[m];
							for (var p = 0, o = q.length; p < o; p++) {
								j.push(q[p]);
								i(l, m + 1)
							}
						}
						if (r) {
							j.length--
						}
					}
				}
				if (e && e >= d) {
					i(g, c)
				}
				return f
			}
		};


	Number.prototype.mul = function(D) { // //浮点数乘法运算
		if (!/\./.test(this) && !/\./.test(D)) {
			return this * D
		}
		var G = this;
		var E = D;
		var B = 0, H = G.toString(), F = E.toString();
		if (H.indexOf(".") >= 0) {
			B += H.split(".")[1].length
		}
		if (F.indexOf(".") >= 0) {
			B += F.split(".")[1].length
		}
		return Number(H.replace(".", "")) * Number(F.replace(".", ""))
				/ Math.pow(10, B)
	};
	C.Tools = {
			C : function(n, m) {// 从n场比赛中中任选m场的组合数
				var arr = [];
				(function F(j, h, k) {
					if (k == 0) {
						return arr.push(j)
					}
					for (var i = 0, len = h.length; i <= len - k; i++) {
						F(j.concat(h[i]), h.slice(i + 1), k - 1);
					}
				})([], n, m);
				return arr;
			},
			P : function(n, m) { // 从n场比赛中中任选m场的排列数
				var arr = [];
				(function F(j, h, k) {
					if (k == 0) {
						return arr.push(j);
					}
					for (var i = 0, len = h.length; i < len; i++) {
						F(j.concat(h[i]), h.slice(0, i).concat(h.slice(i + 1)), k
										- 1);
					}
				})([], n, m);
				return arr;
			},
			F : function(B) { // 阶乘计算
				var D = 1;
				(function E(G) {
					if (G < 0) {
						return D
					}
					for (var F = G; F > 0; F--) {
						D *= F
					}
				})(B);
				return D
			},
			Sum : function(E) { // 传入一个数组 返回数组中每一项的和
				var B = 0;
				(function D(G) {
					for (var H = 0, F = G.length; H < F; H++) {
						B += Number(G[H])
					}
				})(E);
				return B
			},
			Product : function(E) { // 传入一个数组 返回数组中每一项浮点数的乘积，用于计算sp
				var B = 1;
				(function D(G) {
					for (var H = 0, F = G.length; H < F; H++) {
						B = B.mul(Number(G[H]))
					}
				})(E);
				return B
			},
			dtC : function(D, I, F) {
				var H = this.C(D, F - I.length);
				var B = [];
				for (var G = 0; G < H.length; G++) {
					var E = I.concat(H[G]);
					B.push(E)
				}
				return B
			}
		};
		C.SBetCalculator = {
			spCache : {},
			countCache : {},
			moneyCache : {},
			passTypeMap :{ "单关" : [ 1 ], "2串1" : [ 2 ], "2串3" : [ 2, 1 ],
							  "3串1" : [ 3 ], "3串3" : [ 2 ], "3串4" : [ 3, 2 ], "3串7" : [
							  3, 2, 1 ], "4串1" : [ 4 ], "4串4" : [ 3 ], "4串5" : [ 4,
							  3 ], "4串6" : [ 2 ], "4串11" : [ 4, 3, 2 ], "4串15" : [
							  4, 3, 2, 1 ], "5串1" : [ 5 ], "5串5" : [ 4 ], "5串6" : [
							  5, 4 ], "5串10" : [ 2 ], "5串16" : [ 5, 4, 3 ], "5串20" : [
							  3, 2 ], "5串26" : [ 5, 4, 3, 2 ], "5串31" : [ 5, 4, 3,
							  2, 1 ], "6串1" : [ 6 ], "6串6" : [ 5 ], "6串7" : [ 6, 5 ],
							  "6串15" : [ 2 ], "6串20" : [ 3 ], "6串22" : [ 6, 5, 4 ],"6串35":[3,2],
							  "6串42" : [ 6, 5, 4, 3 ], "6串50" : [ 4, 3, 2 ], "6串57" : [
							  6, 5, 4, 3, 2 ], "6串63" : [ 6, 5, 4, 3, 2, 1 ], "7串1" : [
							  7 ], "7串7" : [ 6 ], "7串8" : [ 7, 6 ], "7串21" : [ 5 ],
							  "7串35" : [ 4 ], "7串120" : [ 7, 6, 5, 4, 3, 2 ],
							  "7串127" : [ 7, 6, 5, 4, 3, 2, 1 ], "8串1" : [ 8 ],
							  "8串8" : [ 7 ], "8串9" : [ 8, 7 ], "8串28" : [ 6 ],
							  "8串56" : [ 5 ], "8串70" : [ 4 ], "8串247" : [ 8, 7, 6,
							  5, 4, 3, 2 ], "8串255" : [ 8, 7, 6, 5, 4, 3, 2, 1 ],
							  "9串1" : [ 9 ], "10串1" : [ 10 ], "11串1" : [ 11 ],
							  "12串1" : [ 12 ], "13串1" : [ 13 ], "14串1" : [ 14 ],
							  "15串1" : [ 15 ] },						  
			analysisSp : function(y, f, e) {
				var A = y.toString() + "|" + f.toString(), p = this.spCache[A], d = false;
				if (typeof p !== "undefined") {
					return p
				}
				_.some(f, function(i) {
							if (y[i].length === 0) {
								d = true;
								return false
							}
						});
				if (d) {
					return null
				}
				this.reg = e;
				var s = e.length - 1, l = e.concat(), m = e.concat(), t = [], c = [], z = [], x = [], o = [], n = [], q = [], j = [], g = 0, k = function(
						B, i) {
					return B - i
				}, r = function(B, i) {
					return i - B
				}, v, h, b;
				for (var u = 0, w = y.length; u < w; u++) {
					v = y[u];
					h = v.length - 1;
					if (h > -1) {
						b = (h === s);
						if (_.indexOf(f, u) < 0) {
							m[h] = m[h] + 1;
							if (b) {
								z.push(v[0]);
								q.push(v[h])
							} else {
								t.push(v[0]);
								o.push(v[h])
							}
						} else {
							l[h] = l[h] + 1;
							if (b) {
								x.push(v[0]);
								j.push(v[h])
							} else {
								c.push(v[0]);
								n.push(v[h])
							}
						}
						g++
					}
				}
				t = t.sort(k);
				c = c.sort(k);
				z = z.sort(k);
				x = x.sort(k);
				o = o.sort(r);
				n = n.sort(r);
				q = q.sort(r);
				j = j.sort(r);
				return this.spCache[A] = {
					minArr : x.concat(c).concat(z).concat(t),
					maxArr : j.concat(n).concat(q).concat(o),
					dSpArr : l,
					tSpArr : m,
					mustDHitCount : x.length,
					mustTHitCount : z.length,
					danCount : x.length + c.length,
					count : g
				}
			},
			_calBaseCount : function(c, b) {
				var f = 0;
				if (c.length === 0 || b === 0) {
					return 0
				}
				var h = c.toString() + "|" + b, e = this.countCache[h];
				if (typeof e === "number") {
					return e
				}
				if (b === 1) { // 单关
					for (var g = c.length; --g > -1;) {
						f += c[g] * (g + 1)
					}
					return this.countCache[h] = f
				}
				var j = C.Tools.Sum(c);
				if (j === b) {
					f = 1;
					for (var g = c.length; --g > -1;) {
						f *= Math.pow(g + 1, c[g])
					}
					return this.countCache[h] = f
				}
				for (var g = c.length; --g > -1;) {
					if (c[g] > 0) {
						var d = c.concat();
						--d[g];
						f = (g + 1) * arguments.callee.apply(this, [d, b - 1])
								+ arguments.callee.apply(this, [d, b]);
						return this.countCache[h] = f
					}
				}
				return this.countCache[h] = f
			},
			calCount : function(c, e, d, g, f) {
				/*
				 * args: c:布尔值 e:已选比赛各场选择的sp值 eg：[[1.53,3.35],[1.92],[2.00,3.15]]
				 * d:已设过胆的数组 eg：[0,3,4,7] g:串关种类 eg：["2串1","3串1","3串3","3串4"] f:模板
				 */

				var b = this.analysisSp(e, d, f);
				if (!b) {
					return 0
				}
				return this
						._calCount(c, b.dSpArr, b.tSpArr, g, b.danCount, b.count)
			},
			_calCount : function(q, h, d, n, c, l) {
				// q:boolean h:sp胆数组 d:sp拖数组 n:串关种类 c:胆数量 l:串关数
				var j = 0, e;
				if (_.isArray(n)) {
					for (var g = n.length; --g > -1;) {
						j += arguments.callee.apply(this, [q, h, d, n[g], c, l]);
					}
					return j
				}
				var o = h.toString() + "|" + d.toString() + "|" + n, b = this.countCache[o];
				if (typeof b !== "undefined") {
					return b
				}
				e = isNaN(parseInt(n)) ? 1 : parseInt(n);
				if ((!q) && (l > e)) {
					var p = this.separateArray(d, e - c);
					for (var g = p.length; --g > -1;) {
						j += arguments.callee.apply(this, [true, [],
										this.addArray(p[g], h), n, 0, e]); 
					}
					return this.countCache[o] = j
				}
				var k = this.passTypeMap[n], f = 0;
				for (var g = k.length; --g > -1;) {
					if (c >= k[g]) {
						j += this._calBaseCount(h, k[g])
					} else {
						if (c < k[g] && c > 0) {
							f = this._calBaseCount(h, c);
							j += f * this._calBaseCount(d, k[g] - c)
						} else {
							j += this._calBaseCount(d, k[g])
						}
					}
				}
				return this.countCache[o] = j
			},
			separateArray : function(d, f) {
				var e = C.Tools.C(this.transArrayToMatch(d), f), c = [];
				for (var g = e.length; --g > -1;) {
					c.push(this.transArrayToNum(e[g]))
				}
				return c
			},
			transArrayToMatch : function(b) {
				var c = [];
				for (var e = b.length; --e > -1;) {
					for (var d = b[e]; --d > -1;) {
						c.push(e + 1)
					}
				}
				return c
			},
			transArrayToNum : function(b) {
				var c = this.reg.concat();
				for (var d = b.length; --d > -1;) {
					c[b[d] - 1]++
				}
				return c
			},
			addArray : function(e, c) {
				var b = Math.max(e.length, c.length), d = [];
				for (var f = b; --f > -1;) {
					d[f] = this.numFormat(e[f]) + this.numFormat(c[f])
				}
				return d
			},
			numFormat : function(b) {
				return isNaN(b) ? 0 : b
			}
					
		};
	 C.hhcalc = {
			unitBonus : true,
			unitMoney : 2,
			awardPercentage : 0.65,
			analysisSp : function(g, I) {
				var O = ["3", "1", "0", "3", "1", "0", "10", "20", "21",
							"30", "31", "32", "40", "41", "42", "50", "51", "52", "90",
							"00", "11", "22", "33", "99", "01", "02", "12", "03", "13",
							"23", "04", "14", "24", "05", "15", "25", "09", "0", "1",
							"2", "3", "4", "5", "6", "7", "33", "31", "30", "13", "11",
							"10", "03", "01", "00"], 
							concede=0,P = [], o = [], H = [], m = [], s = [], D = [], G = [], v = [], x = [], L = [], q, z, M, y, h, p, K, N, r, A, n, E, l, R, Q, F, t, w, J, k, u, S, B;
				for (R = g.length; --R > -1;) {
					isMust = false;
					M = H[R] = [];
					y = D[R] = [];
					h = G[R] = [];
					p = v[R] = [];
					K = x[R] = [];
					N = L[R] = [];
					q = g[R];
					z = q     // z:每场比赛选择的sp数组
					if (z.length > 0) {
						S = [];
						r = [];
						A = [];
						n = [];
						E = [];
						l = [];
						for (Q = -1; ++Q < z.length;) {
							F = z[Q][0]; //F:每场比赛每一个选择的index值
							t = z[Q][1]//t: 每场比赛每一个选择的sp值
							M[Q] = t;
							B = O[F];
							if (F >= 0 && F <= 2) {
								y.push(t);
								r.push("spf-" + B + "#" + t)
							} else {
								if (F >= 3 && F <= 5) {
									h.push(t);
									A
											.push("rqspf-" + B + "@" + z[Q][2]//每场比赛每一个选择的让球值
													+ "#" + t)
								} else {
									if (F >= 6 && F <= 36) {
										p.push(t);
										n.push("bf-" + B + "#" + t)
									} else {
										if (F >= 37 && F <= 44) {
											K.push(t);
											E.push("jqs-" + B + "#" + t)
										} else {
											N.push(t);
											l.push("bqc-" + B + "#" + t)
										}
									}
								}
							}
						}
						J = (_.indexOf(I, R) >= 0);
						if (r.length) {
							S.push(r.join(","))
						}
						if (A.length) {
							S.push(A.join(","))
						}
						if (n.length) {
							S.push(n.join(","))
						}
						if (E.length) {
							S.push(E.join(","))
						}
						if (l.length) {
							S.push(l.join(","))
						}
						P.push(S.join("|") + (J ? "D" : ""));
						o[o.length] = u
					}
				}

				return {
					_spSelected : o,
					spSelected : H,
					spfSpSelected : D,
					rqspfSpSelected : G,
					bfSpSelected : v,
					jqsSpSelected : x,
					bqcSpSelected : L,
					betOptions : P
				}
			},
			calculateBet : function(m, o,i,template) { //i:过关类型   o：胆数量
				var n = 0, j = 0, k = 0, p = 0, l, h, g = C.SBonus;
				g.init();
				if (m.length >= 2 && i.length) {
					h = this.analysisSp(m, o);
					n = this.getBetCount(m, o, i, h,template);
					l = g.getBonusRange(h.betOptions, i);
					k = (l.min).toFixed(2);
					p = (l.max).toFixed(2);
				}
	            return {count:n,min:k,max:p};
			},			
			getBetCount : function(k, n, m, g,t) {
				var l = C.SBetCalculator;
				return l.calCount(true, g.spSelected, n, m, t)
			}
	};
	return C;
	/*混合过关计算器使用规则：需传四个参数
	 * @matches  格式：[
	 *                   [
	 *                     [6,15.00],[7,34.00,+1],[0,6.20]  //每场比赛选择的sp值以及此sp值在比赛中的index 格式为：[index,sp] index从0开始。在让球胜平负玩法中 如果主队让1球 就在选择的sp数组中再加入一个成员-1，受让一球就是+1
	 *                   ], //每一场比赛
	 *                   [
	 *                     [1,3.00],[4,3.60],[6,6.00],[7,9.00]  //每场比赛选择的sp值以及此sp值在比赛中的index
	 *                   ] //每一场比赛
	 *                 ] //已选比赛的list，是一个数组
	 * 
	 * @dan       格式：[0,2,3] //设了胆的比赛的index 从0开始算起
	 * @types     格式：["2串1","3串1"]  //所选的过关种类
	 * @占位模板     固定格式：[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]  //54个0
	 * */
})