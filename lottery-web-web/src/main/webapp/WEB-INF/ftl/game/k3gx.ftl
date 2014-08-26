<#include "../common/function.ftl" />
<#if !cdnBaseUrl??><#include "../fakeData/k3-data.ftl" /></#if>
<#assign func = "k3gx" />
<#-- 引入header部分 -->
<#include "../common/header.ftl" />

<div class="cp-infos wrapper">
    <div class="info1" id="up_top">
        <div class="cpname"><h1>${game.gameCn}</h1> 第 <b>${availablePeriod.periodNo}</b> 期</div>
        投注还剩：<em id="ball_timer"><#include "../common/timer_placeholder.ftl"></em>&nbsp;&nbsp;
        销售时间：9:28～22:28（78期）10分钟开奖 返奖率<b>59%</b>　
    </div>
    <div class="info2 k3-bg">
        <a href="" class="ilogo k3-logo ct">${game.gameCn}</a>
        <div class="imenu">
            <a href="" class="cur">选号投注<span class="ct"></span></a>
        </div>
        <div class="ilink">
            <a href="/help/i-k3gx-play.html">玩法介绍</a></a>
        </div>
    </div>
</div>
<div class="wrapper">
    <div class="cp-sub-nav">
        <ul id="k3-toggle">
            <li>
                <a href="#" rel="nofollow">和值</a>
            </li>
            <li>
                <a href="#" rel="nofollow">三同号通选</a>
            </li>
            <li>
                <a href="#" rel="nofollow">三同号单选</a>
            </li>
            <li class="active">
                <a href="#" rel="nofollow">三不同号</a>
            </li>
            <li>
                <a href="#" rel="nofollow">三连号通选</a>
            </li>
            <li>
                <a href="#" rel="nofollow">二同号复选</a>
            </li>
            <li>
                <a href="#" rel="nofollow">二同号单选</a>
            </li>
            <li>
                <a href="#" rel="nofollow">二不同号</a>
            </li>
        </ul>
    </div>
</div>
<div class="wrapper">
    <div class="row cf">
        <div class="row-left">
            <!--和值-->
            <div pbflag="wc_0_和值" style="display:none" class="toggle_c ball-list k3-hz-ball-list cf" id="k3_hz_play">
                <div class="play-tips">
                    从3～18中任选1个或多个号码，所选号码与开奖的3个号码相加之和相同，即中<em>9～240</em>元
                    <i class="tips-ico">
                        玩法
                        <span class="ct"></span>
                    </i>
                </div>
                <table class="k3-bet-tip">
                    <tr>
                        <th>和值数</th>
                        <th>3或18</th>
                        <th>4或17</th>
                        <th>5或16</th>
                        <th>6或15</th>
                        <th>7或14</th>
                        <th>8或13</th>
                        <th>9或12</th>
                        <th>10或11</th>
                    </tr>
                    <tr>
                        <td>奖金（元）</td>
                        <td><span>240</span></td>
                        <td><span>80</span></td>
                        <td><span>40</span></td>
                        <td><span>25</span></td>
                        <td><span>16</span></td>
                        <td><span>12</span></td>
                        <td><span>10</span></td>
                        <td><span>9</span></td>
                    </tr>
                    <tr>
                        <td>中奖概率</td>
                        <td>1/216</td>
                        <td>1/72</td>
                        <td>1/36</td>
                        <td>1/21.6</td>
                        <td>1/14.4</td>
                        <td>1/10.29</td>
                        <td>1/8.64</td>
                        <td>1/8</td>
                    </tr>
                </table>
                <div class="tips-i">
                    <i class="tips-ico">
                        和值
                        <span class="ct"></span>
                    </i>
                    <em class="game-yl-tip2">当前遗漏<br>最大遗漏</em>
                </div>
                <div class="red-ball">
                    <div class="h">
                        <em>选号区</em>
                        至少选择1
                    </div>
                    <ul class="ball cf">
                        <#if presentMissingStr?? && maxMissingStr??>
                        <#assign hz_missing = presentMissingStr?split(":")[0]?split(" ")>
                        <#assign hzMax_missing = maxMissingStr?split(":")[0]?split(" ")>
                        <#assign hz_max = max(hz_missing) />
                        <#assign hzMax_max = max(hzMax_missing) />
                        <#list (3..18) as m>
                        <li><em data-model='{"color":"red","number":"${m}"}'>${m}</em><span<#if hz_missing[m-3] == hz_max> class="red"</#if>>${hz_missing[m-3]}</span><span<#if hzMax_missing[m-3] == hzMax_max> class="red"</#if>>${hzMax_missing[m-3]}</span></li>
                        </#list>
                        <#else>
                        <#list (3..18) as m>
                        <li><em data-model='{"color":"red","number":"${m}"}'>${m}</em><span>-</span><span>-</span></li>
                        </#list>
                        </#if>
                    </ul>
                </div>
                <div class="sel-info">
                    您选了<em>0</em>注，共<em>0</em>元
                </div>
                <div class="sel-ball cf">
                    <a href="#" rel="nofollow" class="btn selSubmitBtn" data-btn="add">确认选号</a>
                    <a href="#" rel="nofollow" class="clear" data-color="all">清空上方选号</a>
                </div>
            </div>
            <!--三同号通选-->
            <div pbflag="wc_0_三同号通选" style="display:none" class="toggle_c ball-list k3-3thtx-ball-list cf" id="k3_3thtx_play">
                <div class="play-tips">
                    当开奖号码为三同号（111、222、333、444、555、666）中任意一个时，选号即中奖<em>40</em>元
                    <i class="tips-ico">
                        玩法
                        <span class="ct"></span>
                    </i>
                </div>
                <div class="tips-i">
                    <i class="tips-ico">
                        号码
                        <span class="ct"></span>
                    </i>
                    <em class="game-yl-tip2">当前遗漏<br>最大遗漏</em>
                </div>
                <div class="red-ball">
                    <div class="h">
                        <em>选号区</em>
                        至少选择1
                    </div>
                    <ul class="ball cf">
                        <#if presentMissingStr?? && maxMissingStr??>
                        <li><em data-model='{"color":"red","number":"三同号通选"}'>三同号通选</em>
                            <span>${presentMissingStr?split(":")[1]}</span><span>${maxMissingStr?split(":")[1]}</span>
                        </li>
                        <#else>
                        <li><em data-model='{"color":"red","number":"三同号通选"}'>三同号通选</em>
                            <span>-</span><span>-</span>
                        </#if>
                    </ul>
                </div>
                <div class="sel-info">
                    您选了<em>0</em>注，共<em>0</em>元
                </div>
                <div class="sel-ball cf">
                    <a href="#" rel="nofollow" class="btn selSubmitBtn" data-btn="add">确认选号</a>
                    <a href="#" rel="nofollow" class="clear" data-color="all">清空上方选号</a>
                </div>
            </div>
            <!--三同号单选-->
            <div pbflag="wc_0_三同号单选" style="display:none" class="toggle_c ball-list k3-3thdx-ball-list cf" id="k3_3thdx_play">
                <div class="play-tips">
                    从111～666中任选1个或多个号码，所选号码与开奖号码相同，选号即中奖<em>240</em>元
                    <i class="tips-ico">
                        玩法
                        <span class="ct"></span>
                    </i>
                </div>
                <div class="tips-i">
                    <i class="tips-ico">
                        同号
                        <span class="ct"></span>
                    </i>
                    <em class="game-yl-tip2">当前遗漏<br>最大遗漏</em>
                </div>
                <div class="red-ball">
                    <div class="h">
                        <em>选号区</em>
                        至少选择1
                    </div>
                    <ul class="ball cf">
                        <#if presentMissingStr?? && maxMissingStr??>
                        <#assign sth_missing = presentMissingStr?split(":")[2]?split(" ")>
                        <#assign sthMax_missing = maxMissingStr?split(":")[2]?split(" ")>
                        <#assign sth_max = max(sth_missing) />
                        <#assign sthMax_max = max(sthMax_missing) />
                        <#list (1..6) as m>
                        <li>
                            <em data-model='{"color":"red","number":"${m}${m}${m}"}'>${m}${m}${m}</em>
                            <span<#if sth_missing[m-1] == sth_max> class="red"</#if>>${sth_missing[m-1]}</span><span<#if sthMax_missing[m-1] == sthMax_max> class="red"</#if>>${sthMax_missing[m-1]}</span>
                        </li>
                        </#list>
                        <#else>
                        <#list (1..6) as m>
                        <li>
                            <em data-model='{"color":"red","number":"${m}${m}${m}"}'>${m}${m}${m}</em>
                            <span>-</span><span>-</span>
                        </li>
                        </#list>
                        </#if>
                    </ul>
                </div>
                <div class="sel-info">
                    您选了<em>0</em>注，共<em>0</em>元
                </div>
                <div class="sel-ball cf">
                    <a href="#" rel="nofollow" class="btn selSubmitBtn" data-btn="add">确认选号</a>
                    <a href="#" rel="nofollow" class="clear" data-color="all">清空上方选号</a>
                </div>
            </div>
            <!--三不同号-->
            <div class="toggle_c ball-list k3-3bt-ball-list cf" id="k3_3bt_play">
                <div class="bet-3bt" id="k3_3bt_toggle">
                    <label>
                        <input type="radio" checked name="k3-3bt-radio">
                        任选投注
                    </label>
                    <label>
                        <input type="radio" name="k3-3bt-radio">
                        胆拖投注
                    </label>
                </div>
                <div pbflag="wc_0_三不同号任选" class="bet-toggle" id="k3_3bt_rx">
                    <div class="play-tips">
                        从1～6中任选3个或多个号码，所选号码与开奖号码的3个号码相同，即中奖<em>40</em>元
                        <i class="tips-ico">
                            玩法
                            <span class="ct"></span>
                        </i>
                    </div>
                    <div class="item cf">
                        <div class="l">
                            <p><span>选号区</span>至少选3个</p>
                        </div>
                        <div class="r">
                            <p><span>快捷选号</span></p>
                        </div>
                    </div>
                    <div class="item cf">
                        <i class="tips-ico">
                            号码
                            <span class="ct"></span>
                        </i>
                        <i class="tips game-yl-tip1">遗漏</i>
                        <ul class="l">
                            <#if presentMissingStr??>
                            <#assign sbt_missing = presentMissingStr?split(":")[3]?split(" ") />
                            <#assign sbt_max = max(sbt_missing) />
                            <#list (1..6) as x>
                            <li>
                                <em data-model='{"color":"red","number":${x}}'>${x}</em>
                                <span<#if sbt_missing[x-1] == sbt_max> class="red"</#if>>${sbt_missing[x-1]}</span>
                            </li>
                            </#list>
                            <#else>
                            <#list (1..6) as x>
                            <li>
                                <em data-model='{"color":"red","number":${x}}'>${x}</em>
                                <span>-</span>
                            </li>
                            </#list>
                            </#if>
                        </ul>
                        <ul class="r">
                            <li>
                                <span class="query" data-n="all">全</span>
                            </li>
                            <li>
                                <span class="query" data-n="max">大</span>
                            </li>
                            <li>
                                <span class="query" data-n="min">小</span>
                            </li>
                            <li>
                                <span class="query" data-n="odd">奇</span>
                            </li>
                            <li>
                                <span class="query" data-n="even">偶</span>
                            </li>
                            <li>
                                <a href="#" rel="nofollow" class="clear">清除</a>
                            </li>
                        </ul>
                    </div>
                    <div class="sel-info">
                        您选了<em>0</em>注，共<em>0</em>元
                    </div>
                    <div class="sel-ball cf">
                        <a href="#" rel="nofollow" class="btn selSubmitBtn" data-btn="add">确认选号</a>
                        <a href="#" rel="nofollow" class="clear">清空上方选号</a>
                    </div>
                </div>
                <div pbflag="wc_0_三不同号胆拖" class="bet-toggle" id="k3_3bt_dt" style="display:none">
                    <div class="play-tips">
                        选1~2个胆码，选2~5个拖码，胆码+拖码不少于4个；选号与奖号相同，即中奖<em>40</em>元
                        <i class="tips-ico">
                            玩法
                            <span class="ct"></span>
                        </i>
                    </div>
                    <div class="item item_dm cf">
                        <div class="l">
                            <p><span>胆码区</span>至少选1个，最多选2个</p>
                        </div>
                    </div>
                    <div class="item item_dm cf">
                        <i class="tips-ico">
                            胆码
                            <span class="ct"></span>
                        </i>
                        <i class="tips game-yl-tip1">遗漏</i>
                        <ul class="l">
                        	<#if presentMissingStr??>
                        	<#list (1..6) as x>
                            <li>
                                <em data-model='{"color":"red_dm","number":${x}}'>${x}</em>
                                <span<#if sbt_missing[x-1] == sbt_max> class="red"</#if>>${sbt_missing[x-1]}</span>
                            </li>
                            </#list>
                        	<#else>
                        	<#list (1..6) as x>
                            <li>
                                <em data-model='{"color":"red_dm","number":${x}}'>${x}</em>
                                <span>-</span>
                            </li>
                            </#list>
                        	</#if>
                        </ul>
                    </div>
                    <div class="item item_dm cf">
                        <div class="l">
                            <p><span>拖码区</span>至少选2个</p>
                        </div>
                         <div class="r r-tm">
                            <p><span>快捷选号</span></p>
                        </div>
                    </div>
                    <div class="item item_dm cf">
                        <i class="tips-ico">
                            拖码
                            <span class="ct"></span>
                        </i>
                        <ul class="l">
                        	<#if presentMissingStr??>
                            <#list (1..6) as x>
                            <li>
                                <em data-model='{"color":"red","number":${x}}'>${x}</em>
                                <span<#if sbt_missing[x-1] == sbt_max> class="red"</#if>>${sbt_missing[x-1]}</span>
                            </li>
                            </#list>
                            <#else>
                            <#list (1..6) as x>
                            <li>
                                <em data-model='{"color":"red","number":${x}}'>${x}</em>
                                <span>-</span>
                            </li>
                            </#list>
                            </#if>
                        </ul>
                        <ul class="r r-tm">
                            <li>
                                <span class="dm_all">拖码全包</span>
                            </li>
                            <li>
                                <a href="#" rel="nofollow" class="clear" data-color="red">清除</a>
                            </li>
                        </ul>
                    </div>
                    <div class="sel-info">
                        您选了<em>0</em>注，共<em>0</em>元
                    </div>
                    <div class="sel-ball cf">
                        <a href="#" rel="nofollow" class="btn selSubmitBtn" data-btn="add">确认选号</a>
                        <a href="#" rel="nofollow" class="clear" data-color="all">清空上方选号</a>
                    </div>
                </div>
            </div>
            <!--三连号通选-->
            <div pbflag="wc_0_三连号通选" style="display:none" class="toggle_c ball-list k3-3thtx-ball-list cf" id="k3_3lhtx_play">
                <div class="play-tips">
                    当开奖号码为三连号（123、234、345、456）中任意一个时，选号即中奖<em>10</em>元
                    <i class="tips-ico">
                        玩法
                        <span class="ct"></span>
                    </i>
                </div>
                <div class="tips-i">
                    <i class="tips-ico">
                         号码
                        <span class="ct"></span>
                    </i>
                    <em class="game-yl-tip2">当前遗漏<br>最大遗漏</em>
                </div>
                <div class="red-ball">
                    <div class="h">
                        <em>选号区</em>
                        至少选择1
                    </div>
                    <ul class="ball cf">
                    	<#if presentMissingStr?? && maxMissingStr??>
                        <li><em data-model='{"color":"red","number":"三连号通选"}'>三连号通选</em>
                            <span>${presentMissingStr?split(":")[4]}</span><span>${maxMissingStr?split(":")[4]}</span>
                        </li>
                        <#else>
                        <li><em data-model='{"color":"red","number":"三连号通选"}'>三连号通选</em>
                            <span>-</span><span>-</span>
                        </li>
                        </#if>
                    </ul>
                </div>
                <div class="sel-info">
                    您选了<em>0</em>注，共<em>0</em>元
                </div>
                <div class="sel-ball cf">
                    <a href="#" rel="nofollow" class="btn selSubmitBtn" data-btn="add">确认选号</a>
                    <a href="#" rel="nofollow" class="clear" data-color="all">清空上方选号</a>
                </div>
            </div>
            <!--二同号复选-->
            <div pbflag="wc_0_二同号复选" style="display:none" class="toggle_c ball-list k3-3thdx-ball-list cf" id="k3_2thfx_play">
                <div class="play-tips">
                    从11～66中任选1个或多个号码，选号与奖号（包含11～66，不限顺序）相同，即中奖<em>15</em>元
                    <i class="tips-ico">
                        玩法
                        <span class="ct"></span>
                    </i>
                </div>
                <div class="tips-i">
                    <i class="tips-ico">
                        同号
                        <span class="ct"></span>
                    </i>
                    <em class="game-yl-tip2">当前遗漏<br>最大遗漏</em>
                </div>
                <div class="red-ball">
                    <div class="h">
                        <em>选号区</em>
                        至少选择1
                    </div>
                    <ul class="ball cf">
                    	<#if presentMissingStr?? && maxMissingStr??>
                    	<#assign eth_missing = presentMissingStr?split(":")[5]?split(" ") />
                    	<#assign ethMax_missing = maxMissingStr?split(":")[5]?split(" ") />
                    	<#assign eth_max = max(eth_missing) />
                    	<#assign ethMax_max = max(ethMax_missing) />
                    	<#list (1..6) as m>
                    	<li>
                            <em data-model='{"color":"red","number":"${m}${m}*"}'>${m}${m}*</em>
                            <span<#if eth_missing[m-1] == eth_max> class="red"</#if>>${eth_missing[m-1]}</span><span<#if ethMax_missing[m-1] == ethMax_max> class="red"</#if>>${ethMax_missing[m-1]}</span>
                        </li>
                        </#list>
                    	<#else>
                    	<#list (1..6) as m>
                    	<li>
                            <em data-model='{"color":"red","number":"${m}${m}*"}'>${m}${m}*</em>
                            <span>-</span><span>-</span>
                        </li>
                        </#list>
                    	</#if>
                        
                    </ul>
                </div>
                <div class="sel-info">
                    您选了<em>0</em>注，共<em>0</em>元
                </div>
                <div class="sel-ball cf">
                    <a href="#" rel="nofollow" class="btn selSubmitBtn" data-btn="add">确认选号</a>
                    <a href="#" rel="nofollow" class="clear" data-color="all">清空上方选号</a>
                </div>
            </div>
            <!--二同号单选-->
            <div pbflag="wc_0_二同号单选" style="display:none" class="toggle_c ball-list k3-3thdx-ball-list cf" id="k3_2thdx_play">
                <div class="play-tips">
                    选择1对相同号码和1个不同号码投注，选号与奖号相同（顺序不限），即中奖<em>80</em>元
                    <i class="tips-ico">
                        玩法
                        <span class="ct"></span>
                    </i>
                </div>
                <div class="tips-i">
                    <i class="tips-ico">
                        同号
                        <span class="ct"></span>
                    </i>
                    <em class="game-yl-tip1">遗漏</em>
                </div>
                <div class="red-ball">
                    <div class="h">
                        <em>选号区</em>
                        至少选择1
                    </div>
                    <ul class="ball cf">
                    	<#if presentMissingStr?? && maxMissingStr??>
                    	<#assign eth2_missing = presentMissingStr?split(":")[6]?split(" ")?chunk(6) />
                    	<#assign eth2_max1 = max(eth2_missing[0]) />
                    	<#assign eth2_max2 = max(eth2_missing[1]) />
                    	<#list (1..6) as m>
                    	<li>
                            <em data-model='{"color":"red_th","number":"${m}${m}"}'>${m}${m}</em>
                            <span<#if eth2_missing[0][m-1] == eth2_max1> class="red"</#if>>${eth2_missing[0][m-1]}</span>
                        </li>
                    	</#list>
                    	<#else>
                    	<#list (1..6) as m>
                    	<li>
                            <em data-model='{"color":"red_th","number":"${m}${m}"}'>${m}${m}</em>
                            <span>-</span>
                        </li>
                    	</#list>
                    	</#if>
                    </ul>
                </div>
                <div class="tips-i tips-i2">
                    <i class="tips-ico">
                        不同
                        <span class="ct"></span>
                    </i>
                    <em class="game-yl-tip1">遗漏</em>
                </div>
                <div class="red-ball">
                    <ul class="ball cf">
                    	<#if presentMissingStr?? && maxMissingStr??>
                    	<#list (1..6) as m>
                    	<li>
                            <em data-model='{"color":"red_bt","number":"${m}"}'>${m}</em>
                            <span<#if eth2_missing[1][m-1] == eth2_max2> class="red"</#if>>${eth2_missing[1][m-1]}</span>
                        </li>
                    	</#list>
                    	<#else>
                    	<#list (1..6) as m>
                    	<li>
                            <em data-model='{"color":"red_bt","number":"${m}"}'>${m}</em>
                            <span>-</span>
                        </li>
                    	</#list>
                    	</#if>
                    </ul>
                </div>
                <div class="sel-info">
                    您选了<em>0</em>注，共<em>0</em>元
                </div>
                <div class="sel-ball cf">
                    <a href="#" rel="nofollow" class="btn selSubmitBtn" data-btn="add">确认选号</a>
                    <a href="#" rel="nofollow" class="clear" data-color="all">清空上方选号</a>
                </div>
            </div>
            <!--二不同号-->
            <div style="display:none" class="toggle_c ball-list k3-3bt-ball-list cf" id="k3_2bt_play">
                <div class="bet-3bt" id="k3_2bt_toggle">
                    <label>
                        <input type="radio" checked name="k3-2bt-radio">
                        任选投注
                    </label>
                    <label>
                        <input type="radio" name="k3-2bt-radio">
                        胆拖投注
                    </label>
                </div>
                <div pbflag="wc_0_二不同号任选" class="bet-toggle" id="k3_2bt_rx">
                    <div class="play-tips">
                        从1～6中任选2个或多个号码，所选号码与开奖号码任意2个号码相同，即中奖<em>8</em>元
                        <i class="tips-ico">
                            玩法
                            <span class="ct"></span>
                        </i>
                    </div>
                    <div class="item cf">
                        <div class="l">
                            <p><span>选号区</span>至少选2个</p>
                        </div>
                        <div class="r">
                            <p><span>快捷选号</span></p>
                        </div>
                    </div>
                    <div class="item cf">
                        <i class="tips-ico">
                            号码
                            <span class="ct"></span>
                        </i>
                        <i class="tips game-yl-tip1">遗漏</i>
                        <ul class="l">
                        	<#if presentMissingStr?? && maxMissingStr??>
                        	<#assign ebt_missing = presentMissingStr?split(":")[7]?split(" ") />
                        	<#assign ebt_max = max(ebt_missing) />
                        	<#list (1..6) as m>
                        	<li>
                                <em data-model='{"color":"red","number":${m}}'>${m}</em>
                                <span<#if ebt_missing[m-1] == ebt_max> class="red"</#if>>${ebt_missing[m-1]}</span>
                            </li>
                        	</#list>
                        	<#else>
                        	<#list (1..6) as m>
                        	<li>
                                <em data-model='{"color":"red","number":${m}}'>${m}</em>
                                <span>-</span>
                            </li>
                        	</#list>
                        	</#if>
                        </ul>
                        <ul class="r">
                            <li>
                                <span class="query" data-n="all">全</span>
                            </li>
                            <li>
                                <span class="query" data-n="max">大</span>
                            </li>
                            <li>
                                <span class="query" data-n="min">小</span>
                            </li>
                            <li>
                                <span class="query" data-n="odd">奇</span>
                            </li>
                            <li>
                                <span class="query" data-n="even">偶</span>
                            </li>
                            <li>
                                <a href="#" rel="nofollow" class="clear">清除</a>
                            </li>
                        </ul>
                    </div>
                    <div class="sel-info">
                        您选了<em>0</em>注，共<em>0</em>元
                    </div>
                    <div class="sel-ball cf">
                        <a href="#" rel="nofollow" class="btn selSubmitBtn" data-btn="add">确认选号</a>
                        <a href="#" rel="nofollow" class="clear">清空上方选号</a>
                    </div>
                </div>
                <div pbflag="wc_0_二不同号胆拖" class="bet-toggle" id="k3_2bt_dt" style="display:none">
                    <div class="play-tips">
                        选1个胆码，选2~5个拖码，胆码加拖码不少于3个；选号与奖号任意2号相同，即中奖<em>8</em>元
                        <i class="tips-ico">
                            玩法
                            <span class="ct"></span>
                        </i>
                    </div>
                    <div class="item item_dm cf">
                        <div class="l">
                            <p><span>胆码区</span>至少选1个</p>
                        </div>
                    </div>
                    <div class="item item_dm cf">
                        <i class="tips-ico">
                            胆码
                            <span class="ct"></span>
                        </i>
                        <i class="tips game-yl-tip1">遗漏</i>
                        <ul class="l">
                        	<#if presentMissingStr?? && maxMissingStr??>
                        	<#list (1..6) as m>
                        	<li>
                                <em data-model='{"color":"red_dm","number":${m}}'>${m}</em>
                                <span<#if ebt_missing[m-1] == ebt_max> class="red"</#if>>${ebt_missing[m-1]}</span>
                            </li>
                        	</#list>
                        	<#else>
                        	<#list (1..6) as m>
                        	<li>
                                <em data-model='{"color":"red_dm","number":${m}}'>${m}</em>
                                <span>-</span>
                            </li>
                        	</#list>
                        	</#if>
                        </ul>
                    </div>
                    <div class="item item_dm cf">
                        <div class="l">
                            <p><span>拖码区</span>至少选2个</p>
                        </div>
                         <div class="r r-tm">
                            <p><span>快捷选号</span></p>
                        </div>
                    </div>
                    <div class="item item_dm cf">
                        <i class="tips-ico">
                            拖码
                            <span class="ct"></span>
                        </i>
                        <ul class="l">
                        	<#if presentMissingStr?? && maxMissingStr??>
                        	<#list (1..6) as m>
                        	<li>
                                <em data-model='{"color":"red","number":${m}}'>${m}</em>
                                <span<#if ebt_missing[m-1] == ebt_max> class="red"</#if>>${ebt_missing[m-1]}</span>
                            </li>
                        	</#list>
                        	<#else>
                        	<#list (1..6) as m>
                        	<li>
                                <em data-model='{"color":"red","number":${m}}'>${m}</em>
                                <span>-</span>
                            </li>
                        	</#list>
                        	</#if>
                        </ul>
                        <ul class="r r-tm">
                            <li>
                                <span class="dm_all">拖码全包</span>
                            </li>
                            <li>
                                <a href="#" rel="nofollow" class="clear" data-color="red">清除</a>
                            </li>
                        </ul>
                    </div>
                    <div class="sel-info">
                        您选了<em>0</em>注，共<em>0</em>元
                    </div>
                    <div class="sel-ball cf">
                        <a href="#" rel="nofollow" class="btn selSubmitBtn" data-btn="add">确认选号</a>
                        <a href="#" rel="nofollow" class="clear" data-color="all">清空上方选号</a>
                    </div>
                </div>
            </div>
            <div pbflag="wc_0_确认选号" class="ball-item-list" id="k3_ball_list">
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
                        <span>我已经阅读并同意<a href="#" class="read_protocal">《用户委托投注协议》</a>和<a href="#" class="risk_anotation">《限号投注风险须知》</a></span>
                    </label><br/>
                    <!-- <label class="cf read2">
                        <input type="checkbox" checked>
                        <span>我已经阅读并同意<a href="#" class="risk_anotation">《限号投注风险须知》</a></span>
                    </label> -->
                </div>
                <div class="k3-kj-nav">
                    <h2><b>今日开奖</b><i id="toggle_award_list">收起</i></h2>
                    <div id="award_list">
                        <table>
                            <tr>
                                <th>
                                    <span>期号</span>
                                    <em>开奖号</em>
                                    &nbsp;和值&nbsp;&nbsp;&nbsp;类型
                                </th>
                                 <th>
                                    <span>期号</span>
                                    <em>开奖号</em>
                                    &nbsp;和值&nbsp;&nbsp;&nbsp;类型
                                </th>
                                 <th>
                                    <span>期号</span>
                                    <em>开奖号</em>
                                    &nbsp;和值&nbsp;&nbsp;&nbsp;类型
                                </th>
                                 <th>
                                    <span>期号</span>
                                    <em>开奖号</em>
                                    &nbsp;和值&nbsp;&nbsp;&nbsp;类型
                                </th>
                            </tr>
                            <#list (1..20) as x>
                            <tr<#if x%2 == 0> class="bg"</#if>>
                            	<#list (0..3) as i>
                            	<#assign key = i*20+x />
                                <td>
                                    <span class="gray">${(key gt 78)?string("--",key?string("00"))}</span>
                                <#if todayAwardList[key-1]??>
                                    <#assign pno = todayAwardList[key-1].award.prizeNumber />
                                    <em class="code">${pno?split(",")[0]}</em>
                                    <em class="sum">${pno?split(",")[1]}</em>
                                    <#if pno?split(",")[6] == "1">
                                    <em class="orange">三连号</em>
                                    <#elseif pno?split(",")[4] == "1">
                                    <em class="blue">三不同</em>
                                    <#elseif pno?split(",")[5] == "1">
                                    <em class="green">三同号</em>
                                    <#else>
                                    <em class="gray">二同号</em>
                                    </#if>
                                <#else>
                                    <em class="code">--</em>
                                    <em class="sum"></em>
                                    <em class="gray"></em>
                                </#if>
                                </td>
                                </#list>
    						</tr>
                            </#list>
                        </table>
                    </div>
                </div>
                <div class="k3-zj-nav">
                    <h2><b>中奖说明</b></h2>
                    <table>
                        <tr>
                            <th width="88">玩法</th>
                            <th width="95">开奖号码示例</th>
                            <th width="115">投注号码示例</th>
                            <th width="250">中奖规则</th>
                            <th>单注奖金</th>
                        </tr>
                        <tr>
                            <td>和值</td>
                            <td rowspan="3">1 1 1</td>
                            <td>3（1+1+1）</td>
                            <td>选1个号码，与开奖号码之和（相加）相等即中奖</td>
                            <td>每个和值中奖概率不同，奖金<b>9～240</b>元不等</td>
                        </tr>
                        <tr>
                            <td>三同号通选</td>
                            <td>"三同号通选"</td>
                            <td>选"三同号通选"，只要开奖的3个号码为同一个号即中奖</td>
                            <td><b>40元</b></td>
                        </tr>
                        <tr>
                            <td>三同号单选</td>
                            <td>1 1 1</td>
                            <td>选1组三同号，与开奖号码全部相同即中奖</td>
                            <td><b>240元</b></td>
                        </tr>
                        <tr>
                            <td>二同号复选</td>
                            <td rowspan="2">1 1 2</td>
                            <td>1 1 *</td>
                            <td>选1组二同号，开奖号码有两个号码相同且与所选号码一致即中奖</td>
                            <td><b>15元</b></td>
                        </tr>
                        <tr>
                            <td>二同号单选</td>
                            <td>1 1 2</td>
                            <td>选1个二同号和1个单号，与开奖号码全部相同即中奖</td>
                            <td><b>80元</b></td>
                        </tr>
                        <tr>
                            <td>三不同号</td>
                            <td rowspan="3">1 2 3</td>
                            <td>1 2 3</td>
                            <td>选3个号码，与开奖号码全部相同即中奖</td>
                            <td><b>40元</b></td>
                        </tr>
                        <tr>
                            <td>二不同号</td>
                            <td>1 3或1 2或2 3开112，12也中奖</td>
                            <td>选2个号码，与开奖号码有2个相同即中奖 </td>
                            <td><b>8元</b></td>
                        </tr>
                        <tr>
                            <td>三连号通选</td>
                            <td>"三连号通选"</td>
                            <td>选"三连号通选"，只要开奖号码为连号（123，234，345，456）即中奖</td>
                            <td><b>10元</b></td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
        <div class="row-right">
        	<#include "../common/k3_side.ftl" />
        </div>
    </div>
</div>
<input type="hidden" id="openingAwardPeriodNo" value="${openingAwardPeriodNo!''}">
<#include "../common/game_val.ftl" />
<#include "../common/footer.ftl" />
</body>
</html>