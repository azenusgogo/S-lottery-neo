<#include "../common/function.ftl" />
<#if !cdnBaseUrl??><#include "../fakeData/qxc-data.ftl" /></#if>
<#assign func = "qxc" />
<#-- 引入header部分 -->
<#include "../common/header.ftl" />
<div class="cp-infos wrapper">
    <div class="info1" id="up_top">
        <div class="cpname"><h1>${game.gameCn}</h1> 第 <b>${availablePeriod.periodNo}</b> 期</div>
        投注还剩：<em id="ball_timer"><#include "../common/timer_placeholder.ftl"></em>
        购彩截止：${availablePeriod.endTime?date} <b>${availablePeriod.endTime?time}</b>　
        周二、五、日 <b>20:30</b> 开奖
        <span><#if todayOpenAward == 1>今日开奖<#else>正在销售</#if></span>
    </div>
    <div class="info2 dlt-bg">
        <a href="" class="ilogo qxc-logo ct">七星彩</a>
        <div class="imenu">
            <a href="" class="cur">选号投注<span class="ct"></span></a>
        </div>
        <div class="ilink">
            <a href="/help/i-qxc-play.html">玩法介绍</a>
        </div>
    </div>
</div>
<div class="wrapper">
    <div class="cp-sub-nav cp-sub-nav-dlt">
        <ul>
            <li class="active">
                <a href="#" rel="nofollow">普通投注</a>
            </li>
        </ul>
    </div>
</div>
<div class="wrapper">
    <div class="row cf">
        <div class="row-left">
            <div pbflag="wc_0_普通" class="ball-list cf" id="qxc_py_play">
                <div class="play-tips">
                    第一位至第七位，每位至少选择一个号码，选号与奖号相同（顺序一致），即中一等，最高奖金<em>500</em>万
                    <i class="tips-ico">
                        玩法
                        <span class="ct"></span>
                    </i>
                </div>
                <div class="qxc-item cf">
                    <div class="l"><p><span>选号区</span> 每位至少选择1个号码</p></div>
                    <div class="r"><p><span>快捷选号</span></div>
                </div>
                <#if presentMissingStr??>
                <#list presentMissingStr?split(":") as m>
                <#assign missing = m?split(" ") />
                <#assign max_num = max(missing) />
                <div class="qxc-item cf">
                    <ul class="l">
                        <#list (0..9) as n>
                        <li>
                            <em data-model='{"row":${m_index+1},"number":${n}}'>${n}</em>
                            <span<#if missing[n] == max_num> class="red"</#if>>${missing[n]}</span>
                        </li>
                        </#list>
                    </ul>
                    <ul class="r">
                        <li><span data-model='{"row":${m_index+1},"type":"all"}'>全</span></li>
                        <li><span data-model='{"row":${m_index+1},"type":"max"}'>大</span></li>
                        <li><span data-model='{"row":${m_index+1},"type":"min"}'>小</span></li>
                        <li><span data-model='{"row":${m_index+1},"type":"odd"}'>奇</span></li>
                        <li><span data-model='{"row":${m_index+1},"type":"even"}'>偶</span></li>
                        <li><a href="#" rel="nofollow" data-row="${m_index+1}" class="clear">清除</a></li>
                    </ul>
                </div>
                </#list>
                <#else>
                <#list (1..7) as m>
                <div class="qxc-item cf">
                    <ul class="l">
                        <#list (0..9) as n>
                        <li>
                            <em data-model='{"row":${m},"number":${n}}'>${n}</em>
                            <span>-</span>
                        </li>
                        </#list>
                    </ul>
                    <ul class="r">
                        <li><span data-model='{"row":${m},"type":"all"}'>全</span></li>
                        <li><span data-model='{"row":${m},"type":"max"}'>大</span></li>
                        <li><span data-model='{"row":${m},"type":"min"}'>小</span></li>
                        <li><span data-model='{"row":${m},"type":"odd"}'>奇</span></li>
                        <li><span data-model='{"row":${m},"type":"even"}'>偶</span></li>
                        <li><a href="#" rel="nofollow" data-row="${m}" class="clear">清除</a></li>
                    </ul>
                </div>
                </#list>
                </#if>
                <div class="sel-info qxc-sel-info">
                    您选了<em>0</em>注，<em>0</em>元
                </div>
                <div class="sel-ball cf">
                    <a href="#" rel="nofollow" class="btn selSubmitBtn">确认选号</a>
                    <a href="#" rel="nofollow" class="clear">清空上方选号</a>
                </div>
                <i class="tips-ico ti3">
                    第一位
                    <span class="ct"></span>
                </i>
                <i class="tips-ico ti4 game-yl-tip1">
                    遗漏
                    <span class="ct"></span>
                </i>
            </div>
            <div pbflag="wc_0_确认选号" class="ball-item-list" id="qxc_ball_list">
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
                    <h2><b>七星彩中奖说明</b></h2>
                    <table cellspacing="0" width="300">
                        <tr>
                            <th width="65">奖级</th>
                            <th width="150">中奖条件</th>
                            <th width="85">奖金</th>
                        </tr>
                        <tr>
                            <td align="center">一等奖</td>
                            <td align="left"><p>投注号码与开奖号码全部相符且顺序一致</p></td>
                            <td align="right">最高<em>500</em>万元</td>
                        </tr>
                        <tr>
                            <td align="center">二等奖</td>
                            <td align="left"><p>连续6位号码与开奖号码相同位置的连续6位号码相同</p></td>
                            <td align="right"><em>10%</em>奖池</td>
                        </tr>
                        <tr>
                            <td align="center">三等奖</td>
                            <td align="left"><p>连续5位号码与开奖号码相同位置的连续5位号码相同</p></td>
                            <td align="right"><em>1800</em>元</td>
                        </tr>
                        <tr>
                            <td align="center">四等奖</td>
                            <td align="left"><p>连续4位号码与开奖号码相同位置的连续4位号码相同</p></td>
                            <td align="right"><em>300</em>元</td>
                        </tr>
                        <tr>
                            <td align="center">五等奖</td>
                            <td align="left"><p>连续3位号码与开奖号码相同位置的连续3位号码相同</p></td>
                            <td align="right"><em>20</em>元</td>
                        </tr>
                        <tr>
                            <td align="center">六等奖</td>
                            <td align="left"><p>连续2位号码与开奖号码相同位置的连续2位号码相同</p></td>
                            <td align="right"><em>5</em>元</td>
                        </tr>
                    </table>
                    <ul class="fc-faq">
                        <li>
                            <strong>中奖了如何领取奖金？</strong>
                            <p><i></i>七星彩单注奖金最高500万元，属于大奖，须缴税20%，搜狗彩票可代为领奖，在各中心办理代领奖手续后，领取的税后奖金直接打入用户的搜狗彩票账户中，若提款则由专人协助办理提款，确保资金安全到达中奖者的银行卡中。</p>
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