<#include "../../../common/function.ftl" />
<!doctype html>
<html>
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="${cdnBaseUrl}css/index.css"/>
    <style>html{background:#fff}</style>
</head>
<body>
<div id="zc_rx9_index" class="home_ticai">
    <div class="qici">
        <span>任选九</span>
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
                    <input onclick="window.location.href='?periodNo=${x.periodNo}'" type="radio" name="time" checked />${x.periodNo}
                    <em>${qiciname}</em>
                </label>
                <#else>
                <label>
                    <input onclick="window.location.href='?periodNo=${x.periodNo}'" type="radio" name="time" />${x.periodNo}
                    ${qiciname}
                </label>
                </#if>
            </#if>
        </#list>
        截止时间还有：<em id="ball_timer"></em>
    </div>
    <table cellspacing="0" width="748" class="cp-area">
        <tr>
            <th width="35">场次</th>
            <th width="56">赛事</th>
            <th width="126">比赛时间</th>
            <th width="58">主队</th>
            <th width="58">客队</th>
            <th width="85">数据</th>
            <th width="119">
                <div class="fen-t darr" id="odds_toggle">
                    平均欧赔
                    <div class="sel">
                        <a href="">Bwin</a>
                        <a href="">亚盘</a>
                        <a href="">投注比例</a>
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
            <th width="40">设胆</th>
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
                <td><a href="../../../data/sport/7m/match/${x.gameMatch.partner7mMatchId}/1.html" target="_blank">析</a> <a href="../../../data/sport/7m/match/${x.gameMatch.partner7mMatchId}/2.html" target="_blank">亚</a> <a href="../../../data/sport/7m/match/${x.gameMatch.partner7mMatchId}/3.html" target="_blank">欧</a></td>
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
            <td class="sel-ret"><label><input type="checkbox" disabled data-row="${x_index+1}" /></label></td>
            <td class="sel-ret tdr"><a href="#" data-row="${x_index+1}">包</a></td>
        </tr>
        </#if>
        </#list>
        </#if>
    </table>
    <div class="touinfo">
        <!-- 你选择了<em>0</em>注，共<em>0</em>元 -->
        <a href="" class="clear">清空选号</a>
        <#if .now lt nowPeriod.startTime>
        <a href="javascript:;" class="tou-disabled">尚未开始投注</a>
        <#else>
        <a href="" class="selSubmitBtn home-tou-btn">立即投注</a>
        </#if>
    </div>
    <form action="/f9/" method="post" target="_parent">
        <input type="hidden" name="quickBetNumber">
    </form>
</div>

<div id="login_body"></div>
<input type="hidden" id="gameId" value="${game.gameId}">
<#list availablePeriodList as x>
<#if x?? && x.periodNo == periodNo>
<input type="hidden" id="endTime" value="${x.endTime?long?c}">
</#if>
</#list>
<input type="hidden" id="periodNo" value="${periodNo}">
<input type="hidden" id="serverTime" value="${.now?long?c}">
<input type="hidden" id="gameType" value="${game.gameType}">
<input type="hidden" id="gameStatus" value="${game.gameStatus}">
<input type="hidden" id="periodNextNo" value="${nextPeroid!''}">
<input type="hidden" id="gameStart" value="1">
<!-- <input type="hidden" id="gameStart" value="${(.now gt nowPeriod.startTime)?string('1','0')}"> -->
<script src="http://account.sogou.com/static/api/passport.js"></script>
<script src="${cdnBaseUrl}js/base.js?v=${versionId}"></script>
<script src="${cdnBaseUrl}js/indexzcmain.js?v=${versionId}"></script>
</body>
</html>