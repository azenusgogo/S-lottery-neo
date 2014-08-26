<div class="jc-operate cf">
    <div class="nav">
        <div class="tit"><b>1 投注方案</b><i class="arr"></i></div>
        <div class="box s1">
            <p id="step1stat">已选<b class="red">0</b>场比赛，0项投注</p>
            <div class="plan">
                <label><input type="checkbox" class="only_sel" />只看已选</label>
                <a href="#" class="opbtn" id="opbtn">展开查看 <i class="arrud"></i></a>
                <span>已选<b class="red step1num">0</b>场</span>
            </div>
            <div class="show" id="listbox">
                <div class="tit">
                    <b>1 投注方案</b>&nbsp;&nbsp;
                    已选<span class="red step1num">0</span>场
                    <i class="arr"></i>
                </div>
                <div class="th cf">
                    <span class="b1"></span>
                    <span class="b2">场次</span>
                    <span class="b3">主队 VS 客队</span>
                    <span class="b4">设胆</span>
                </div>
                <div class="detail" id="detailScroll">
                    <ul id="listArea">
                        
                    </ul>
                    <div class="tips" id="listTips">
                        请在上方至少选择2场比赛进行投注
                    </div>
                </div>
                <script type="text/template" id="listTemplate">
                <li class="co" id="list_<%=matchid%>">
                    <div class="t cf">
                        <span class="b1"><b matchid="<%=matchid%>" class="removeListItem">×</b></span>
                        <span class="b2"><%=date%></span>
                        <span class="b3"><%=nick%></span>
                        <span class="b4"><input matchid="<%=matchid%>" disabled type="checkbox" class="list_dm"></span>
                    </div>
                    <div class="b"></div>
                </li>
                </script>
            </div>
        </div>
    </div>
    <div class="nav m">
        <div class="tit"><b>2 过关方式</b><i class="arr"></i></div>
        <div class="box s2">
            <p><b>自由过关：</b></p>
            <div class="free cf" id="chuan_tips">请至少选择2场比赛进行投注</div>
            <div class="free cf chuan_n_1" id="chuan_n_1">
                <label for="c_n_2"><input n="2串1" type="checkbox" id="c_n_2" /> 2串1</label>
                <label for="c_n_3"><input n="3串1" type="checkbox" id="c_n_3" /> 3串1</label>
                <label for="c_n_4"><input n="4串1" type="checkbox" id="c_n_4" /> 4串1</label>
                <label for="c_n_5"><input n="5串1" type="checkbox" id="c_n_5" /> 5串1</label>
                <label for="c_n_6"><input n="6串1" type="checkbox" id="c_n_6" /> 6串1</label>
                <label for="c_n_7"><input n="7串1" type="checkbox" id="c_n_7" /> 7串1</label>
                <label for="c_n_8"><input n="8串1" type="checkbox" id="c_n_8" /> 8串1</label>
            </div>
        </div>
    </div>
    <div class="nav r">
        <div class="tit"><b>3 支付</b></div>
        <div class="box s3">
            <ul class="cf">
                <li>当前注数：<span class="red" id="count">0注</span></li>
                <li class="long">
                    方案倍数：
                    <em class="bei cf">
                        <b id="minus">-</b>
                        <input type="text" id="mul" value="1" />
                        <b id="plus">+</b>
                    </em>
                </li>
                <li>投注金额：<span class="red" id="countMoney">0元</span></li>
                <li class="long">奖金预测：<span class="red" id="countRange">0元</span></li>
            </ul>
            <div class="dobtn">
                <a href="#">立即投注</a>
            </div>
        </div>
    </div>
</div>