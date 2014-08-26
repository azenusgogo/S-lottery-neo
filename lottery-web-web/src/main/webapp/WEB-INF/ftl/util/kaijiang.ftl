<#include "../common/function.ftl" />
<#if !cdnBaseUrl??><#include "../fakeData/extra.ftl" /></#if>
<#-- 本页标识 -->
<#assign func = "kaijiang" />
<#assign seoTitle = "彩票开奖_彩票开奖结果_彩票开奖查询– 搜狗彩票" />
<#assign seoKeywords = "彩票开奖，彩票开奖结果，彩票开奖查询" />
<#assign seoDescription = "搜狗彩票开奖中心提供福彩、体彩、足彩和高频彩票的开奖结果查询。包括双色球、超级大乐透、快乐8、3D、排列三、排列五、七星彩、七乐彩等彩票，查询最新彩票开奖结果上搜狗彩票"/>
<#-- 引入header部分 -->
<#include "../common/header.ftl" />

<div class="annoc wrapper">
    <div class="sitepath">
        您当前的位置：<a href="/">彩票首页</a> &gt; 彩票开奖
    </div>
    <div class="title">
        <h1>最新彩票开奖公告<span>${today!""}</span></h1>
        <div class="fr">
            <a href="/help/f-bet.html#bet01" target="_blank">如何购彩？</a> | 
            <a href="/help/f-reward.html#rew01" target="_blank">如何领奖？</a>
        </div>
    </div>
    <#if todayOpenAwardGameList?? && todayOpenAwardGameList?size gt 0>
    <div class="today" pbflag="wc_0_今日开奖">
        今日开奖：
        <#list todayOpenAwardGameList as x>
        <a href="${domainUrl}${x.gameId}/">${x.gameCn}</a>
        </#list>
    </div>
    </#if>
    <h2><b>数字彩票</b></h2>
    <table class="g-kj-ret">
        <#list commonOpenAwardDtoList as x>
        <tr<#if x_index%2 == 0> class="bg"</#if> pbflag="wc_0_${x.gameCn!'-'}">
            <td class="kj"><#if x.isTodayOpen!false><div>今日开奖</div></#if></td>
            <td class="type"><a href="${domainUrl}${x.gameId!''}/">${x.gameCn!'-'}</a></td>
            <td class="qici">第${x.periodNo!'-'}期<br /><span>${x.openAwardDate!'-'}</span></td>
            <td class="num">
                <div class="kj-num">
                <#if x.prizeNumber??>
                    <#list x.prizeNumber?split(":") as m>
                        <#list m?split(" ") as n>
                        <i<#if m_index == 1> class="blue-ball-min"</#if>>${n}</i>
                        </#list>
                    </#list>
                    <#if "ssq,dlt"?contains(x.gameId!'null')><a target="_blank" href="${domainUrl}calculator/${x.gameId}/">计算奖金</a></#if>
                </#if>
                </div>
            </td>
            <td class="intro">
                <#if x.topBonus??>一等奖：
                    <#list x.topBonus?split("_") as n><#if n != "0"><em>${n}</em><#if n_index == 0>亿<#elseif n_index == 1>万<#else>元</#if></#if></#list>&nbsp;&nbsp;&nbsp;&nbsp;
                </#if>
                <#if x.bonusPool?? && x.bonusPool != "0_0_0">奖池：
                    <#list x.bonusPool?split("_") as n><#if n != "0"><em>${n}</em><#if n_index == 0>亿<#elseif n_index == 1>万<#else>元</#if></#if></#list>
                </#if>
            </td>
            <td class="do"><a href="${domainUrl}${x.gameId!''}/" class="fbtn">立即投注</a></td>
        </tr>
        </#list>
    </table>
    <h2><b>竞技体育</b></h2>
    <table class="g-kj-ret">
        <#list sportOpenAwardDtoList as x>
        <#if x?? && x.gameId??>
        <tr<#if x_index%2 == 0> class="bg"</#if> pbflag="wc_0_${x.gameCn!'-'}">
            <td class="kj"></td>
            <td class="type"><a href="${domainUrl}${x.gameId}/">${x.gameCn!"-"}</a></td>
            <td class="qici">第${x.periodNo!"-"}期<br /><span>${x.openAwardDate!"-"}</span></td>
            <td class="num"><b>${x.prizeNumber!"-"}</b></td>
            <td class="intro">
                <#if x.topBonus??>一等奖：
                    <#list x.topBonus?split("_") as n><#if n != "0"><em>${n}</em><#if n_index == 0>亿<#elseif n_index == 1>万<#else>元</#if></#if></#list>&nbsp;&nbsp;&nbsp;&nbsp;
                </#if>&nbsp;&nbsp;&nbsp;&nbsp;${x.recentNews!""}</td>
            <td class="do"><a href="${domainUrl}${x.gameId}/" class="fbtn">立即投注</a></td>
        </tr>
        </#if>
        </#list>
    </table>
    <h2><b>高频彩</b></h2>
    <table class="g-kj-ret">
        <#list hfOpenAwardDtoList as x>
        <tr<#if x_index%2 == 0> class="bg"</#if>  pbflag="wc_0_${x.gameCn!'-'}">
            <td class="kj"></td>
            <td class="type"><a href="${domainUrl}${x.gameId}/">${x.gameCn}</a></td>
            <td class="qici">第${x.periodNo}期<br /><span>${x.openAwardDate}</span></td>
            <td class="num">
                <div class="kj-num">
                <#if x.prizeNumber??>
                    <#list x.prizeNumber?split(",")[0]?split(" ") as m>
                    <i>${m}</i>
                    </#list>&nbsp;&nbsp;
                    <#assign sum = x.prizeNumber?split(",")[1]?number />
                    和值：<em>${sum}</em>
                    <#if sum gt 10>
                    <i class="ico i-da"></i>
                    <#else>
                    <i class="ico i-xiao"></i>
                    </#if>
                    <#if sum%2 == 0>
                    <i class="ico i-shuang"></i>
                    <#else>
                    <i class="ico i-dan"></i>
                    </#if>
                </#if>
                </div>
            </td>
            <td class="intro">
                <#if x.hotPlayMethod??>
                <#assign tabs = "和值,三同号通选,三同号单选,三不同号,三连号通选,二同号复选,二同号单选,二不同号" />
                <#list tabs?split(",") as k>
                    <#if k == x.hotPlayMethod>
                    <#assign tab_index = "#" + k_index />
                    </#if>
                </#list>
                <a href="${domainUrl}${x.gameId}/${tab_index!''}">${x.hotPlayMethod}</a>
                </#if>
            </td>
            <td class="do"><a href="${domainUrl}${x.gameId}/" class="fbtn">立即投注</a></td>
        </tr>
        </#list>
    </table>
</div>
<#include "../common/footer.ftl" />
</body>
</html>