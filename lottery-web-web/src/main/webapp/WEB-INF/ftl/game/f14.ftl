<#include "../common/function.ftl" />
<#if !cdnBaseUrl??><#include "../fakeData/f9-data.ftl" /></#if>
<#assign func = "f14" />
<#-- 引入header部分 -->
<#include "../common/header.ftl" />

<div class="cp-infos wrapper">
    <div class="info1">
        <div class="cpname"><h1>${game.gameCn}</h1> 第 <b>${periodNo}</b> 期</div>
        投注还剩：<em id="ball_timer"><#include "../common/timer_placeholder.ftl"></em>
        <#list availablePeriodList as x>
        <#if x?? && x.periodNo == periodNo>
        购彩截止：${x.endTime?date} <b>${x.endTime?time}</b>
        </#if>
        </#list>
        <span>正在销售</span>
    </div>
    <div class="info2 sfc-bg">
        <a href="${domainUrl}f14/" class="ilogo f14-logo ct">${game.gameCn}</a>
        <div class="imenu">
            <a href="${domainUrl}f14/" class="cur">选号投注<span class="ct"></span></a>
        </div>
        <div class="ilink">
            <a href="/help/i-f14-play.html">玩法介绍</a>
        </div>
    </div>
</div>

<div class="wrapper">
    <div class="sfc-type">
        <ul>
            <li><a href="${domainUrl}f14/" class="cur">胜负彩</a></li>
            <li><a href="${domainUrl}f9/">任选九</a></li>
        </ul>
    </div>
</div>
<div class="wrapper">
    <div class="cf">
        <div class="row-left2">
            <div class="jjc-nav sfc-main">
                <div pbflag="wc_0_投注区" id="zc_sfc">
                    <p class="tips">
                    	<#list availablePeriodList as x>
                    		<#if x??>
	                    		<#if x_index == 0>
	                    		<#assign qiciname = "当前期" />
	                    		<#else>
	                    		<#assign qiciname = "预售期" />
	                    		</#if>
	                    		<#if x.periodNo == periodNo>
                                <#assign nowPeriod = x />
                                <#if availablePeriodList[x_index+1]??>
                                <#assign nextPeroid = availablePeriodList[x_index+1].periodNo />
                                </#if>
	                    		<label>
		                            <input onclick="window.location.href='?periodNo=${x.periodNo}'" type="radio" name="time" checked /><b>${x.periodNo}</b>
		                            <span>${qiciname}</span>
		                        </label>
	                    		<#else>
	                    		<label>
		                            <input onclick="window.location.href='?periodNo=${x.periodNo}'" type="radio" name="time" /><b>${x.periodNo}</b>
		                            ${qiciname}
		                        </label>
	                    		</#if>
                    		</#if>
                    	</#list><br />
                        <b>玩法提示：</b>竞猜14场球队的比赛结果，每场比赛至少选择一个比赛结果，单注最高奖金<span>500万</span>元
                    </p>
                    <table cellspacing="0" width="706" class="cp-area">
                        <tr>
                            <th width="40">场次</th>
                            <th width="56">赛事</th>
                            <th width="105">比赛时间</th>
                            <th width="58">主队</th>
                            <th width="58">客队</th>
                            <th width="102">数据</th>
                            <th width="119">
                                <div class="fen-t darr" id="odds_toggle">
                                    平均欧赔
                                    <div class="sel">
                                        <a href="">威廉希尔</a>
                                        <a href="">立博</a>
                                        <a href="">Bet365</a>
                                        <a href="">Bwin</a>
                                        <a href="">亚盘</a>
                                    </div>
                                </div>
                                <div class="fen-l">胜</div>
                                <div class="fen-m">平</div>
                                <div class="fen-r">负</div>
                            </th>
                            <th width="119" colspan="3">
                                <div class="fen-t">选号区</div>
                                <div class="fen-l">胜</div>
                                <div class="fen-m">平</div>
                                <div class="fen-r">负</div>
                            </th>
                            <th width="38" class="tdr">包</th>
                        </tr>
                        <#if gameMatchList??>
                        <#list gameMatchList as x>
                        <#if x??>
                        <tr>
                            <td>${x.gameMatch.gameMatchNo}</td>
                            <td bgcolor="${x.match.labelColor}" class="type-name">${x.match.leagueName}</td>
                            <td>${x.match.kickOffTime?string("MM-dd HH:mm")}</td>
                            <td>${x.match.homeTeamName}</td>
                            <td>${x.match.awayTeamName}</td>
                            <#if x.gameMatch.partner7mMatchId?? && x.gameMatch.partner7mMatchId?length gt 0>
                            <td><a href="/data/sport/7m/match/${x.gameMatch.partner7mMatchId}/1.html" target="_blank">析</a> <a href="/data/sport/7m/match/${x.gameMatch.partner7mMatchId}/2.html" target="_blank">亚</a> <a href="/data/sport/7m/match/${x.gameMatch.partner7mMatchId}/3.html" target="_blank">欧</a></td>
                            <#else>
                            <td><a href="javascript:void(0)" class="greyColor">析</a> <a href="javascript:void(0)" class="greyColor">亚</a> <a href="javascript:void(0)" class="greyColor">欧</a></td>
                            </#if>
                            <td>
                            <#if (x.gameMatch.odds)??>
                            	<#list x.gameMatch.odds?split(";") as odd>
                                <div class="peilv"<#if odd_index == 0> style="display:block"</#if>>
                                	<#if odd?split(",")?size gt 1>
                                	<span>${pout(odd?split(",")[0])}</span>|<span>${pout(odd?split(",")[1])}</span>|<span>${pout(odd?split(",")[2])}</span>
                                	<#else>
                                	<span>- -</span>|<span>- -</span>|<span>- -</span>
                                	</#if>
                                </div>
                                </#list>
                            <#else>
                                <div class="peilv" style="display:block">
                                    <span>- -</span>|<span>- -</span>|<span>- -</span>
                                </div>
                            </#if>
                            </td>
                            <td width="40" class="sel-ret fcu"><a href="#" data-model='{"row":${x_index+1},"number":3}'>3</a></td>
                            <td width="40" class="sel-ret fcu"><a href="#" data-model='{"row":${x_index+1},"number":1}'>1</a></td>
                            <td width="40" class="sel-ret fcu"><a href="#" data-model='{"row":${x_index+1},"number":0}'>0</a></td>
                            <td class="sel-ret tdr"><a href="#" data-row="${x_index+1}">包</a></td>
                        </tr>
                        </#if>
                        </#list>
                        </#if>
                    </table>
                    <div class="jjc-sel-ball cf">
                        <#if .now lt nowPeriod.startTime>
                        <a href="javascript:;" rel="nofollow" class="btn">尚未开始投注</a>
                        <#else>
                        <a href="#" rel="nofollow" class="btn selSubmitBtn">确认选号</a>
                        <a href="#" class="clear">清空上方选号</a>
                        </#if>
                    </div>
                </div>
                
                <div pbflag="wc_0_确认选号" class="ball-item-list" id="zc_sfc_ball_list"<#if .now lt nowPeriod.startTime> style="display:none"</#if>>
                    <div class="ball-item cf">
                        <div class="list">
                            <ul class="cf" id="item_parent"></ul>
                        </div>
                        <div class="aside">
                            <ul class="cf">
                                <li>
                                    <a href="#" class="rndBtn" data-n="1">机选1注</a>
                                </li>
                                <li>
                                    <a href="#" class="rndBtn" data-n="5">机选5注</a>
                                </li>
                                <li>
                                    <input type="text" value="10" class="rnd_n">
                                    <span>注</span>
                                    <a href="#" class="random rndBtn">机选</a>
                                </li>
                                <li>
                                    <a href="#" class="clear">清空列表</a>
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
                            <input type="radio" checked="">
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
                        <a href="#" class="btn ct" id="ballSubmit">立即投注</a>
                        <label class="cf">
                            <input type="checkbox" checked>
                            <span>我已经阅读并同意<a href="#" class="read_protocal">《用户委托投注协议》</a></span>
                        </label>
                    </div>
                </div>
            </div>
            <div class="jjc-cont">
                <h2><b>胜负彩中奖说明及常见问题</b></h2>
                <div class="cont cf">
                    <table class="cp-intro" cellspacing="0" width="310">
                        <tr>
                            <th>奖级</th>
                            <th>中奖条件</th>
                            <th>奖金</th>
                        </tr>
                        <tr>
                            <td>一等奖</td>
                            <td>猜中全部14场比赛胜平负结果</td>
                            <td class="red">最高500万元</td>
                        </tr>
                        <tr>
                            <td>二等奖</td>
                            <td>猜中13场比赛胜平负结果</td>
                            <td class="red">均分30%奖池</td>
                        </tr>
                    </table>
                    <ul class="about-intro">
                        <li>
                            <b><i></i>关于派奖时间</b>
                            <p>奖期截止后第二日开奖。开奖结果通过媒体向社会公布。</p>
                        </li>
                        <li>
                            <b><i></i>关于比赛延期或中断</b>
                            <p>如比赛在下期足球彩票胜负开奖时间前补赛，则该场比赛的开奖结果以实际比赛结果为准。</p>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
        <div class="row-right2">
            <#include "../common/sf_side.ftl" />
        </div>
    </div>
</div>
<input type="hidden" id="periodNextNo" value="${nextPeroid!''}">
<input type="hidden" id="gameStart" value="${(.now gt nowPeriod.startTime)?string('1','0')}">
<#include "../common/game_val.ftl" />
<#include "../common/footer.ftl" />
</body>
</html>