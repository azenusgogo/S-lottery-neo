<#include "../common/function.ftl" />
<#include "../lib/game.ftl" />
<#if !cdnBaseUrl??><#include "../fakeData/ssq-data.ftl" /></#if>
<#assign func = "ssq" />
<#-- 引入header部分 -->
<#include "../common/header.ftl" />
<div class="cp-infos wrapper">
    <div class="info1" id="up_top">
        <div class="cpname"><h1>${game.gameCn}</h1> 第 <b>${availablePeriod.periodNo}</b> 期</div>
        投注还剩：<em id="ball_timer"><#include "../common/timer_placeholder.ftl"></em>　
        购彩截止：${availablePeriod.endTime?date} <b>${availablePeriod.endTime?time}</b>　
        周二、四、日 <b>21:30</b> 开奖
        <span><#if todayOpenAward == 1>今日开奖<#else>正在销售</#if></span>
    </div>
    <div class="info2 ssq-bg">
        <a href="" class="ilogo ssq-logo ct">双色球</a>
        <div class="imenu">
            <a href="" class="cur">选号投注<span class="ct"></span></a>
        </div>
        <div class="ilink">
            <a href="/help/i-ssq-play.html">玩法介绍</a>　|　<a href="/calculator/ssq/">奖金计算</a>
        </div>
    </div>
</div>
<div class="wrapper">
    <div class="cp-sub-nav">
        <ul id="toggle_t">
            <li class="active">
                <a href="#" rel="nofollow">普通投注</a>
            </li>
            <li>
                <a href="#" rel="nofollow">胆拖投注</a>
            </li>
            <li>
                <a href="#" rel="nofollow">杀号定胆</a>
            </li>
        </ul>
    </div>
</div>
<div class="wrapper">
    <div class="row cf">
        <div class="row-left">
            <div pbflag="wc_0_普通" class="toggle_c ball-list cf" id="py_play">
                <div class="play-tips">
                    至少选6个红球和1个篮球组合成一注彩票，单注最高奖金<em>1000</em>万元
                    <i class="tips-ico">
                        玩法
                        <span class="ct"></span>
                    </i>
                </div>
                <div class="red-ball">
                    <div class="h">
                        <em>红球区</em>
                        至少选择6个红球
                    </div>
                    <ul class="ball cf">
                        <@Balls color="red" num=33 missing=presentMissingStr />
                    </ul>
                    <ul class="f cf">
                        <li>
                            <select class="red-n" data-color="red">
                                <option value="6">6</option>
                                <option value="7">7</option>
                                <option value="8">8</option>
                                <option value="9">9</option>
                                <option value="10">10</option>
                                <option value="11">11</option>
                                <option value="12">12</option>
                                <option value="13">13</option>
                                <option value="14">14</option>
                                <option value="15">15</option>
                                <option value="16">16</option>
                                <option value="17">17</option>
                                <option value="18">18</option>
                                <option value="19">19</option>
                                <option value="20">20</option>
                            </select>
                        </li>
                        <li>
                            <a href="#" rel="nofollow" class="btn rndBtn" data-color="red">机选红球</a>
                        </li>
                        <li>
                            <a href="#" rel="nofollow" class="clear" data-color="red">清空</a>
                        </li>
                    </ul>
                </div>
                <div class="hr ct"></div>
                <div class="blue-ball">
                    <div class="h">
                        <em>蓝球区</em>
                        至少选择1个蓝球
                    </div>
                    <ul class="ball cf">
                        <@Balls color="blue" num=16 missing=presentMissingStr />
                        <li><a href="#" rel="nofollow" class="allbtn blueAll">全选</a></li>
                    </ul>
                    <ul class="f">
                        <li>
                            <select class="blue-n" data-color="blue">
                                <option value="1">1</option>
                                <option value="2">2</option>
                                <option value="3">3</option>
                                <option value="4">4</option>
                                <option value="5">5</option>
                                <option value="6">6</option>
                                <option value="7">7</option>
                                <option value="8">8</option>
                                <option value="9">9</option>
                                <option value="10">10</option>
                                <option value="11">11</option>
                                <option value="12">12</option>
                                <option value="13">13</option>
                                <option value="14">14</option>
                                <option value="15">15</option>
                                <option value="16">16</option>
                            </select>
                        </li>
                        <li>
                            <a href="#" rel="nofollow" class="btn rndBtn" data-color="blue">机选蓝球</a>
                        </li>
                        <li>
                            <a href="#" rel="nofollow" class="clear" data-color="blue">清空</a>
                        </li>
                    </ul>
                </div>
                <div class="sel-info">
                    您选了<em>0</em>个红球，<span>0</span>个蓝球，共<em>0</em>注，<em>0</em>元
                </div>
                <div class="sel-ball cf">
                    <a href="#" rel="nofollow" class="btn selSubmitBtn" data-btn="add">确认选号</a>
                    <a href="#" rel="nofollow" class="clear" data-color="all">清空上方选号</a>
                </div>
                <i class="tips-ico ti1">
                    选号
                    <span class="ct"></span>
                </i>
                <i class="tips-ico ti2 game-yl-tip1">
                    遗漏
                    <span class="ct"></span>
                </i>
            </div>
            <div pbflag="wc_0_胆拖" style="display: none" class="toggle_c ball-list ball-list-dt cf" id="dt_play">
                <div class="play-tips">
                    选1～5个红球胆码、选至少1个红球拖码（胆码+拖码≥7个），至少1个蓝球，单注最高奖金<em>1000</em>万元
                    <i class="tips-ico">
                        玩法
                        <span class="ct"></span>
                    </i>
                </div>
                <div class="red-ball">
                    <div class="h">
                        <em>红球胆码区</em>
                        我认为可能出的号码，至少选1个。
                    </div>
                    <ul class="ball cf">
                        <@Balls color="red" num=33 missing=presentMissingStr dm="_dm" />
                    </ul>
                </div>
                <div class="red-ball">
                    <div class="h">
                        <em>红球拖码区</em>
                        我认为可能出的号码 可选1或多个
                    </div>
                    <ul class="ball cf">
                        <@Balls color="red" num=33 missing=presentMissingStr />
                    </ul>
                </div>
                <div class="blue-ball">
                    <div class="h">
                        <em>蓝球区</em>
                        至少选择1个蓝球
                    </div>
                    <ul class="ball cf">
                        <@Balls color="blue" num=16 missing=presentMissingStr />
                    </ul>
                </div>
                <div class="sel-info">
                    您选了<em>0</em>个红球（<em>0</em>个红球胆码，<em>0</em>个红球拖码），<span>0</span>个蓝球，共<em>0</em>注，<em>0</em>元
                </div>
                <div class="sel-ball cf">
                    <a href="#" rel="nofollow" class="btn selSubmitBtn" data-btn="add">确认选号</a>
                    <a href="#" rel="nofollow" class="clear" data-color="all">清空上方选号</a>
                </div>
                <i class="tips-ico ti1">
                    选号
                    <span class="ct"></span>
                </i>
                <i class="tips-ico ti2 game-yl-tip1">
                    遗漏
                    <span class="ct"></span>
                </i>
            </div>
            <div pbflag="wc_0_杀号" style="display: none" class="toggle_c ball-list cf" id="sh_play">
                <div class="play-tips">
                    同一个号码点击一下为"定胆"、点击两下为"杀号"、点击三下"还原"
                    <i class="tips-ico">
                        玩法
                        <span class="ct"></span>
                    </i>
                </div>
                <div class="red-ball">
                    <div class="h">
                        <em>红球区</em>
                        最多可设5个胆码，超出后，点击选号默认为杀号
                    </div>
                    <ul class="ball cf">
                        <@Balls color="red" num=33 missing=presentMissingStr />
                    </ul>
                </div>
                <div class="hr ct"></div>
                <div class="blue-ball">
                    <div class="h">
                        <em>蓝球区</em>
                        至少选择1个蓝球
                    </div>
                    <ul class="ball cf">
                        <@Balls color="blue" num=16 missing=presentMissingStr />
                        <li><a href="#" rel="nofollow" class="allbtn blueAll">全选</a></li>
                    </ul>
                </div>
                <div class="rnd-sel">
                    我要选
                    <select class="sel-redn">
                        <option value="7">7</option>
                        <option value="8">8</option>
                        <option value="9">9</option>
                        <option value="10">10</option>
                        <option value="11">11</option>
                        <option value="12">12</option>
                        <option value="13">13</option>
                        <option value="14">14</option>
                        <option value="15">15</option>
                        <option value="16">16</option>
                        <option value="17">17</option>
                        <option value="18">18</option>
                        <option value="19">19</option>
                        <option value="20">20</option>
                        <option value="21">21</option>
                        <option value="22">22</option>
                        <option value="23">23</option>
                        <option value="24">24</option>
                        <option value="25">25</option>
                        <option value="26">26</option>
                    </select>
                    个红球（包含胆码），
                    <select class="sel-bluen">
                        <option value="1">1</option>
                        <option value="2">2</option>
                        <option value="3">3</option>
                        <option value="4">4</option>
                        <option value="5">5</option>
                        <option value="6">6</option>
                        <option value="7">7</option>
                        <option value="8">8</option>
                        <option value="9">9</option>
                        <option value="10">10</option>
                        <option value="11">11</option>
                        <option value="12">12</option>
                        <option value="13">13</option>
                        <option value="14">14</option>
                        <option value="15">15</option>
                        <option value="16">16</option>
                    </select>
                    个篮球，
                    <select class="sel-mul">
                        <option value="1">1</option>
                        <option value="2">2</option>
                        <option value="3">3</option>
                        <option value="4">4</option>
                        <option value="5" selected>5</option>
                        <option value="6">6</option>
                        <option value="7">7</option>
                        <option value="8">8</option>
                        <option value="9">9</option>
                        <option value="10">10</option>
                        <option value="15">15</option>
                        <option value="20">20</option>
                        <option value="30">30</option>
                        <option value="40">40</option>
                        <option value="50">50</option>
                        <option value="60">60</option>
                        <option value="70">70</option>
                        <option value="80">80</option>
                        <option value="90">90</option>
                        <option value="100">100</option>
                    </select>
                    组
                </div>
                <div class="sel-info">
                    您选了共 <em>5</em> 注， <em>10</em> 元
                </div>
                <div class="sel-ball cf">
                    <a href="#" rel="nofollow" class="btn selSubmitBtn" data-btn="add">确认选号</a>
                    <a href="#" rel="nofollow" class="clear" data-color="all">清空上方选号</a>
                </div>
                <i class="tips-ico ti1">
                    选号
                    <span class="ct"></span>
                </i>
                <i class="tips-ico ti2 game-yl-tip1">
                    遗漏
                    <span class="ct"></span>
                </i>
            </div>
            <div pbflag="wc_0_确认选号" class="ball-item-list" id="ball_list">
                <div class="ball-item cf">
                    <div class="list">
                        <ul class="cf" id="item_parent"></ul>
                    </div>
                    <div class="aside">
                        <ul class="cf">
                            <li>
                                <a href="#" rel="nofollow" class="rndBtn" data-n="1">机选1注</a>
                            </li>
                            <li>
                                <a href="#" rel="nofollow" class="rndBtn" data-n="5">机选5注</a>
                            </li>
                            <li>
                                <input type="text" value="10" class="rnd_n">
                                <span>注</span>
                                <a href="#" rel="nofollow" class="random rndBtn">机选</a>
                            </li>
                            <li>
                                <a href="#" rel="nofollow" class="clear">清空列表</a>
                            </li>
                        </ul>
                    </div>
                    <div class="ball-list-tips">
                        <span class="bottom1 ct"></span>
                        <span class="bottom2 ct"></span>
                        <p>手气不错！机选一注试试</p>
                    </div>
                </div>
                <div class="ball-count">
                    您选了 <span class="count">0</span> 注，倍投 <input type="text" value="1" class="mul"> 倍，共 <span class="money">0</span> 元
                </div>
                <div class="cf ball-pay-method">
                    <b>购买方式:</b>
                    <label>
                        <input type="radio" checked>
                        <span>自购</span>
                    </label>
                    <div class="tips">
                        由购买人自行全额购买彩票
                        <i></i>
                        <div class="ball-list-tips">
                            <span class="top1 ct"></span>
                            <span class="top2 ct"></span>
                            <p><em>自购</em>：选好投注号码后，由自己全额支付购彩款；中奖后，自己独享全部税后奖金</p>
                        </div>
                    </div>
                </div>
                <div class="ball-submit">
                    <a href="#" rel="nofollow" class="btn ct" id="ballSubmit">立即投注</a>
                    <label class="cf">
                        <input type="checkbox" checked>
                        <span>我已经阅读并同意<a href="#" class="read_protocal">《用户委托投注协议》</a></span>
                    </label>
                </div>
                <div class="ssq-intro cf">
                    <h2><b>双色球中奖说明</b></h2>
                    <table cellspacing="0" width="270">
                        <tr>
                            <th width="80">奖级</th>
                            <th>中奖条件</th>
                            <th>奖金</th>
                        </tr>
                        <tr>
                            <td align="center">一等奖</td>
                            <td align="left"><span class="ct">6+1</span></td>
                            <td align="right">浮动</td>
                        </tr>
                        <tr>
                            <td align="center">二等奖</td>
                            <td align="left"><span class="ct iball-60">6+0</span></td>
                            <td align="right">浮动</td>
                        </tr>
                        <tr>
                            <td align="center">三等奖</td>
                            <td align="left"><span class="ct iball-51">5+1</span></td>
                            <td align="right">3000元</td>
                        </tr>
                        <tr>
                            <td align="center">四等奖</td>
                            <td align="left"><span class="ct iball-41">4+1</span><span class="ct iball-50">5+0</span></td>
                            <td align="right">200元</td>
                        </tr>
                        <tr>
                            <td align="center">五等奖</td>
                            <td align="left"><span class="ct iball-31">3+1</span><span class="ct iball-40">4+0</span></td>
                            <td align="right">10元</td>
                        </tr>
                        <tr>
                            <td align="center">六等奖</td>
                            <td align="left"><span class="ct iball-21">2+1</span><span class="ct iball-11">1+1</span><span class="ct iball-01">0+1</span></td>
                            <td align="right">5元</td>
                        </tr>
                    </table>
                    <ul class="fc-faq">
                        <li>
                            <strong>中奖了如何领取奖金？</strong>
                            <p><i></i><b>中小奖：</b>除一等奖之外的其余奖项，开奖后2-3个小时，奖金就会派到您的搜狗彩票账户里。</p>
                            <p><i></i><b>一等奖：</b>搜狗会第一时间联系您协商领奖方式，可选择委托搜狗代领奖或亲自到中心兑奖。
                            <p><i></i><b>奖金</b>无须消费可直接提款，单注中奖1万以上需要缴税20%。</p>
                        </li>
                        
                        <li>
                            <strong>双色球派奖时间？</strong>
                            <p><i></i>官方开奖以后，系统预计会在当晚23：50至次日凌晨2:00将中奖方案的金额派送到用户账户。</p>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
        <#include "../common/common_side.ftl" />
    </div>
</div>
<#include "../common/game_val.ftl" />
<#include "../common/footer.ftl" />
</body>
</html>