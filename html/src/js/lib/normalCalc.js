define(function() {
	var C = {};
	C.DataCache = {};
	Number.prototype.mul = function(c) {
		if (!/\./.test(this) && !/\./.test(c)) {
			return this * c
		}
		var f = this;
		var d = c;
		var b = 0, g = f.toString(), e = d.toString();
		if (g.indexOf(".") >= 0) {
			b += g.split(".")[1].length
		}
		if (e.indexOf(".") >= 0) {
			b += e.split(".")[1].length
		}
		return Number(g.replace(".", "")) * Number(e.replace(".", ""))
				/ Math.pow(10, b)
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
	C.Rule = {		
		toFixed : function(d, h) {
			var g = d.toString(), c = g.split(".");
			var f = c[1] || "";
			if (f.length < h) {
				for (var e = 0, b = h - f.length; e < b; e++) {
					f += "0"
				}
			} else {
				f = f.substring(0, h)
			}
			return {
				str : c[0] + "." + f,
				num : parseFloat(c[0] + "." + f)
			}
		},
		inArray : function(d, c) {
			var f = false;
			for (var e = 0, b = c.length; e < b; e++) {
				if (c[e] == d) {
					f = true;
					break
				}
			}
			return f
		},
		indexOfArray : function(e, d) {
			if (Array.indexOf) {
				return e.indexOf(d)
			}
			for (var c = 0, b = e.length; c < b; ++c) {
				if (e[c] === d) {
					return c
				}
			}
			return -1
		},
		getCombineCount : function(g, d) {
			var e = 1, c = 1;
			for (var f = 1; f <= d; f++) {
				e *= g--;
				c *= f
			}
			return e / c
		},
		getPassTypeObj : function() {
			return {
				"2\u4e321" : [2],
				"3\u4e321" : [3],
				"4\u4e321" : [4],
				"5\u4e321" : [5],
				"6\u4e321" : [6],
				"7\u4e321" : [7],
				"8\u4e321" : [8]
			}
		},
		getAllPassTypeObj : function() {
			return {}
		},
		_countCache : {},
		
		C : function(b, c) {
			var d = [];
			(function e(j, g, k) {
				if (k == 0) {
					return d.push(j)
				}
				for (var h = 0, f = g.length; h <= f - k; h++) {
					e(j.concat(g[h]), g.slice(h + 1), k - 1)
				}
			})([], b, c);
			return d
		},
		combine : function(h, g, m) {
			var l = [];
			if (h.length == 0) {
				return g
			}
			if (g.length == 0) {
				return h
			}
			if (m) {
				for (var f = 0; f < h.length; f++) {
					for (var e = 0; e < g.length; e++) {
						var c = h[f], b = g[e];
						for (var d = 0; d < b.length; d++) {
							c = c.concat(b[d])
						}
						l.push(c)
					}
				}
			} else {
				for (var e = 0; e < g.length; e++) {
					var c = h, b = g[e];
					for (var d = 0; d < b.length; d++) {
						c = c.concat(b[d])
					}
					l.push(c)
				}
			}
			return l
		},
		isArray : function(b) {
			return b && typeof b === "object" && typeof b.length === "number"
					&& typeof b.splice === "function"
					&& !(b.propertyIsEnumerable("length"))
		}
	};
	C.BonusDetail = {
		selectedMatches : [],
		_danCount : 0,
		multi : 1,
		totalAmount : 0,
		_passTypeObj : null,
		mArray : [],
		_maxNumSpArray : [],
		_minNumSpArray : [],
		_bonusCache : {},
		_multiStr : null,
		_calculateBonus : function(L, O, D, M, G) {
			// L:_minNumSpArray/_maxNumSpArray
			// O:selPassTypes
			// D:比赛数/
			// M:胆的数量
			// G:mustDHitCount~胆的数量的每一个值
			var I = this, K = "", N = 0, M = M || 0, G = G || 0, E = L.length;
			for (var H = 0; H < E; H++) {
				K += L[H].info + ","
			}
			var J = O + "|" + D + "|" + M + "|" + G + "|" + K.slice(0, -1), B = C.BonusDetail._bonusCache[J];
			if (B) {
				return B
			}
			if (C.Rule.isArray(O)) {
				_.each(O, function(P) {
							N += Number(I._calculateBonus(L, P, D, M, G))
						});
				return C.BonusDetail._bonusCache[J] = N.toFixed(2)
			}
			var M = M || 0;
			if (M == 0) {
				return C.BonusDetail._bonusCache[J] = I._calculateBonusNoDan(L,
						O, D)
			} else {
				var F = O.charAt(0) == "\u5355" ? 1 : Number(O.charAt(0));
				if (E == F) {
					return C.BonusDetail._bonusCache[J] = I
							._calculateBonusNoDan(L, O, D)
				} else {
					return C.BonusDetail._bonusCache[J] = I._calculateBonusDan(
							L, O, D, M, G)
				}
			}
		},
		_calculateBonusNoDan : function(V, N, K) {
			var I = this, O = "", F = 0, J = V.length;
			for (var R = 0; R < J; R++) {
				O += V[R].info + ","
			}
			var G = N + "|" + K + "|" + O.slice(0, -1), L = C.BonusDetail._bonusCache[G];
			if (L) {
				return L
			}
			var H = I._passTypeObj[N], P = N.charAt(0) == "\u5355" 
			? 1 : Number(N.charAt(0)), M = N.slice(2) == "" ? 1 : Number(N
					.slice(2));
			if (J == P) {
				if (M == 1) {
					if (K < P) {
						return 0
					}
					var S = 1;
					for (var R = 0; R < J; R++) {
						S *= V[R].sp
					}
					F = S * 2
				} else {
					_.each(H, function(X) {
								var W = (X == 1) ? "\u5355\u5173" : X
										+ "\u4e321";
								F += Number(I._calculateBonusNoDan(V, W, K))
							})
				}
			} else {
				var B = [], D = H[H.length - 1], E = (D > P - (J - K)) ? D : P
						- (J - K), Q = (P > K) ? K : P;
				for (var R = E; R <= Q; R++) {
					var U = C.Rule.C(V.slice(0, K), R), T = C.Rule.C(
							V.slice(K), P - R);
					B = C.Rule.combine(U, T, true);
					_.each(B, function(W) {
								F += Number(I._calculateBonusNoDan(W, N, R))
							})
				}
			}
			return C.BonusDetail._bonusCache[G] = F.toFixed(2)
		},
		_calculateBonusDan : function(T, M, J, F, B) {
			var H = this, N = "", I = T.length;
			for (var P = 0; P < I; P++) {
				N += T[P].info + ","
			}
			var G = M + "|" + J + "|" + F + "|" + B + "|" + N.slice(0, -1), L = C.BonusDetail._bonusCache[G];
			if (L) {
				return L
			}
			if ((B > F) || (B > J)) {
				return 0
			}
			var E = 0, D = [], K = J - B, O = M.charAt(0) == "\u5355"
					? 1
					: Number(M.charAt(0)), S = T.slice(0, F);
			for (var P = 0; P <= K; P++) {
				if (P > (O - F) || (I - (F + K)) < (O - P - F)) {
					continue
				}
				var R = C.Rule.C(T.slice(F, F + K), P);
				var Q = C.Rule.C(T.slice(F + K), O - P - F);
				D = C.Rule.combine(R, Q, true);
				D = C.Rule.combine(S, D, false);
				_.each(D, function(V) {
							if (B < F) {
								var U = V.splice(B, (F - B));
								V = V.concat(U)
							}
							E += Number(H._calculateBonusNoDan(V, M, B + P))
						})
			}
			return C.BonusDetail._bonusCache[G] = E.toFixed(2)
		},
		calBonusScope : function(analysisObj, matchesLen, selPassTypes) {
			var H = this, O = H.multi, L = H.mArray[H.mArray.length - 1].m, N = matchesLen, // 选了比赛的场数
			K = 0, G = 0;

			D = C.DataCache._minDHit = analysisObj.mustDHitCount, E = C.DataCache._minHit = analysisObj.mustTHitCount;
			L = Math.max(E, L);
			if (H._danCount == 0) {
				K = H._calculateBonus(H._minNumSpArray, selPassTypes, L);
				G = H._calculateBonus(H._maxNumSpArray, selPassTypes, N)
			} else {
				G = H._calculateBonus(H._maxNumSpArray, selPassTypes, N,
						H._danCount, H._danCount);
				var J = (L < H._danCount) ? L : H._danCount, B = [];
				for (var F = D; F <= J; F++) {
					K = H._calculateBonus(H._minNumSpArray, selPassTypes, L,
							H._danCount, F);
					if (K != 0) {
						B.push(Number(K))
					}
				}
				K = B.sort(function(Q, P) {
							return Q - P
						})[0]
			}
			return {
				min : (K * O).toFixed(2),
				max : (G * O).toFixed(2)
			}
		},
		_calBonusNoRepeat : function(P, F) {
			var H = this;
			var J = F + "|" + P.toString(), D = C.BonusDetail._bonusCache[J];
			if (D) {
				return D
			}
			var I = P.length;
			if (I < F) {
				return C.BonusDetail._bonusCache[J] = {
					count : 0,
					bonus : 0,
					strArr : []
				}
			}
			if (I == F) {
				var M = 1, N = [];
				_.each(P, function(Q) {
							M *= Q.sp;
							N.push(Q.info)
						});
				M *= 2 * C.BonusDetail.multi;
				N = [N.join("×"), C.BonusDetail._multiStr,
						C.Rule.toFixed(M, 2).num, "\u5143"];
				N.bonus = M;
				return C.BonusDetail._bonusCache[J] = {
					count : 1,
					bonus : M,
					strArr : [N]
				}
			} else {
				if (F == 1) {
					var E = 0, O = [], G = 0;
					_.each(P, function(Q) {
								var S = Q.sp * 2 * C.BonusDetail.multi;
								E += S;
								var R = [Q[0], C.BonusDetail._multiStr,
										C.Rule.toFixed(S, 2).num, "\u5143"];
								R.bonus = S;
								G++;
								O.push(R)
							});
					return C.BonusDetail._bonusCache[J] = {
						count : G,
						bonus : E,
						strArr : O
					}
				}
			}
			var B = P.slice(0, I - 1);
			var L = arguments.callee(B, F), K = arguments.callee(B, F - 1);
			var G = L.count + K.count;
			var E = L.bonus + K.bonus * P[I - 1].sp;
			var O = L.strArr.concat();
			_.each(K.strArr, function(S, R) {
						var Q = S.concat();
						Q[0] += "×" + P[I - 1][0];
						var T = S.bonus * P[I - 1].sp;
						Q[2] = C.Rule.toFixed(T, 2).str;
						Q.bonus = T;
						O.push(Q)
					});
			return C.BonusDetail._bonusCache[J] = {
				count : G,
				bonus : E,
				strArr : O
			}
		},
		_danCalCache : {},
		_calculateBonusNoRepeat : function(Q, E) {
			var I = this;
			var J = Q.length;
			var K = J < I._danCount ? J : I._danCount;
			if (J < E) {
				return {
					count : 0,
					bonus : 0,
					strArr : []
				}
			}
			if (K < E) {
				if (K > 0) {
					var O = I._calBonusNoRepeat(Q.slice(K, Q.length), E - K);
					var D, F;
					var H = Q.slice(0, K).toString(), B = I._danCalCache[H];
					if (B) {
						D = B.sp;
						F = B.str
					} else {
						D = 1;
						var N = [];
						for (var G = 0; G < K; G++) {
							D *= Q[G].sp;
							N.push(Q[G].info)
						}
						F = N.join("×");
						I._danCalCache[H] = {
							sp : D,
							str : F
						}
					}
					var M = O.bonus * D;
					var P = O.strArr.concat();
					_.each(P, function(S, R) {
								P[R] = S.concat();
								if (F) {
									P[R][0] = F + "×" + S[0]
								}
								P[R][2] = C.Rule.toFixed(S.bonus * D, 2).num
							});
					return {
						count : O.count,
						bonus : C.Rule.toFixed(M, 2).num,
						strArr : P
					}
				} else {
					var L = I._calBonusNoRepeat(Q, E);
					return {
						count : L.count,
						bonus : C.Rule.toFixed(L.bonus, 2).num,
						strArr : L.strArr
					}
				}
			} else {
				var L = I._calBonusNoRepeat(Q.slice(0, K), E);
				return {
					count : L.count,
					bonus : C.Rule.toFixed(L.bonus, 2).num,
					strArr : L.strArr
				}
			}
		},
		calBonusScopeNoRepeat : function(analysisObj) {
			var G = this, H = [], K = G.multi;
			for (var E = G.selectedMatches.length; E > 0; E--) {
				var D = 0, F = 0, J = 0;
				var I = G._maxNumSpArray.slice(0, E);
				var B = G._minNumSpArray.slice(0, E);
				_.each(G.mArray, function(O) {
							var L = G._calculateBonusNoRepeat(I, O.m);
							var N = G._calculateBonusNoRepeat(B, O.m);
							D += L.bonus * O.times;
							F += N.bonus * O.times;
							var M = L.count * O.times;
							J += M
						});
				if (J == 0) {
					continue
				}
				H.push({
							max : (D * K).toFixed(2),
							min : (F * K).toFixed(2)
						})
			}
			return {
				max : H[0].max,
				min : H[H.length - 1].min
			}
		},
		init : function(count, matchesLen, danCount, danArr, matches,selPassTypes) {
			var L = this;
			if (count <= 0) {
				return;
			}
			L.selectedMatches = [];
			L._danCount = danCount;
			var analysisObj = this.analysisSp(matches, danArr, [0, 0, 0, 0, 0,
							0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
							0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
							0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]);

			L.multi = 1;
			L.totalAmount = count * 2 * L.multi;
			L._passTypeObj = this.passTypeMap;// C.Rule.getAllPassTypeObj();
			var P = [];
			for (var I = 0, N = selPassTypes.length; I < N; I++) {
				var D = L._passTypeObj[selPassTypes[I]];
				for (var G = 0, F = D.length; G < F; G++) {
					var K = D[G] - 1;
					P[K] = P[K] ? (P[K] + 1) : 1
				}
			}
			L.mArray.length = 0;
			for (I = P.length - 1; I >= 0; I--) {
				if (P[I] > 0) {
					L.mArray.push({
								m : I + 1,
								times : P[I]
							})
				}
			}
			_.each(analysisObj.maxArr, function(S, i) {
						var R = {};
						R.info = ["[" + i + "]" + S];
						R.sp = S;
						L._maxNumSpArray.push(R)
					});
			_.each(analysisObj.minArr, function(S, i) {
						var R = {};
						R.info = ["[" + i + "]" + S];
						R.sp = S;
						L._minNumSpArray.push(R);
					});
			if (true/* C.DataCache.hasRepeat */) {
				var Q = L.calBonusScope(analysisObj, matchesLen, selPassTypes)
			} else {
				var Q = L.calBonusScopeNoRepeat(analysisObj, matchesLen, selPassTypes)
			}

			return Q;
		},
		spCache : {},
		countCache : {},
		moneyCache : {},
		passTypeMap : {
			"单关" : [1],
			"2串1" : [2],
			"2串3" : [2, 1],
			"3串1" : [3],
			"3串3" : [2],
			"3串4" : [3, 2],
			"3串7" : [3, 2, 1],
			"4串1" : [4],
			"4串4" : [3],
			"4串5" : [4, 3],
			"4串6" : [2],
			"4串11" : [4, 3, 2],
			"4串15" : [4, 3, 2, 1],
			"5串1" : [5],
			"5串5" : [4],
			"5串6" : [5, 4],
			"5串10" : [2],
			"5串16" : [5, 4, 3],
			"5串20" : [3, 2],
			"5串26" : [5, 4, 3, 2],
			"5串31" : [5, 4, 3, 2, 1],
			"6串1" : [6],
			"6串6" : [5],
			"6串7" : [6, 5],
			"6串15" : [2],
			"6串20" : [3],
			"6串22" : [6, 5, 4],
			"6串42" : [6, 5, 4, 3],
			"6串50" : [4, 3, 2],
			"6串57" : [6, 5, 4, 3, 2],
			"6串63" : [6, 5, 4, 3, 2, 1],
			"7串1" : [7],
			"7串7" : [6],
			"7串8" : [7, 6],
			"7串21" : [5],
			"7串35" : [4],
			"7串120" : [7, 6, 5, 4, 3, 2],
			"7串127" : [7, 6, 5, 4, 3, 2, 1],
			"8串1" : [8],
			"8串8" : [7],
			"8串9" : [8, 7],
			"8串28" : [6],
			"8串56" : [5],
			"8串70" : [4],
			"8串247" : [8, 7, 6, 5, 4, 3, 2],
			"8串255" : [8, 7, 6, 5, 4, 3, 2, 1],
			"9串1" : [9],
			"10串1" : [10],
			"11串1" : [11],
			"12串1" : [12],
			"13串1" : [13],
			"14串1" : [14],
			"15串1" : [15]
		},
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
			// q:bollean h:sp胆数组 d:sp拖数组 n:串关种类 c:胆数量 l:串关数
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
		addArray : function(e, c) { // 两个数组对应项相加,返回一个新数组
			var b = Math.max(e.length, c.length), d = [];
			for (var f = b; --f > -1;) {
				d[f] = this.numFormat(e[f]) + this.numFormat(c[f])
			}
			return d
		},
		numFormat : function(b) {
			return isNaN(b) ? 0 : b
		}

	};return C;
	/**
	 * **************************************** 以下为调用计算器方法************************************************************************************************ */
	 	 
	/*
	 * C.BonusDetail.calCount(arg1,arg2,arg3,arg4,arg5)方法 计算注数
	 * 传参：需传五个参数： 
	 * arg1 ：布尔值    true-自由过关(N串1)    false-组合过关(N串M)
	 * arg2：数组        每场比赛已选的sp
	 * arg3：数组        设胆情况 数组中保存的是已设了胆的比赛场次的index值 如果没有设胆 则传一空数组 
	 * arg4：数组        已选的串关类型 如： ["2串1","3串1","4串1"] 如果过关类型为N串1，arg1应传true。如果过关类型为N串M,arg1应传false
	 * arg5：数组        一场比赛的sp个数，用0站位
	 *  
	 * 
	 * C.BonusDetail.init(arg1,arg2,arg3,arg4,arg5,arg6)方法 计算最小、最大奖金  返回一对象 {min:10,max:100} 
	 * arg1： 数字       注数
	 * arg2： 数字       已选场次数
	 * arg3： 数字       胆的数量
	 * arg4： 数组       设了胆的比赛的index
	 * arg5： 数组       每场比赛已选的sp
	 * arg6： 数组       已选的串关类型 如： ["2串1","3串1","4串1"] 如果过关类型为N串1，arg1应传true。如果过关类型为N串M,arg1应传false
	 
	var matches = [[2.03,3.10,3.25], [2.14,2.95,3.15], [2.01,3.05, 3.35], [1.53,4.00,4.60]];
	var dan = [];
	var types = ["4串5"];
	var count = C.BonusDetail.calCount(false, matches, dan, types, [0, 0, 0])//, 0,0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,0, 0, 0, 0, 0, 0, 0, 0, 0, 0]);
	var award = C.BonusDetail.init(count, matches.length, dan.length, dan,matches, types);

	var money = count * 2;
	$("#match").html(matches.length)
	$("#bets").html(count);
	$("#min").html(award.min);
	$("#max").html(award.max);
	$("#money").html(money)
	*/
});