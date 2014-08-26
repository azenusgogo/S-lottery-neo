<#if awardList?? && awardList?size gt 0>
<#assign qici = availablePeriod.periodNo?number />
<#assign yishou = qici?substring(qici?length-2)?number-1 />
<#if game.gameId = "k3jl">
<#assign zongqi = 79 />
<#elseif game.gameId = "k3js">
<#assign zongqi = 82 />
<#elseif game.gameId = "k3gx">
<#assign zongqi = 78 />
</#if>
<div class="k3-kj-info" id="up_pool">
    <#if openingAwardPeriodNo?? && openingAwardPeriodNo != "">
    <h4>${game.gameCn} 第<span>${openingAwardPeriodNo}</span>期 开奖</h4>
    <div>开奖号码获取中...</div>
    <#else>
    <h4>${game.gameCn} 第 <span>${awardList[0].award.periodNo}</span> 期 开奖</h4>
    <div>
        <#list awardList[0].award.prizeNumber?split(",")[0]?split(" ") as x>
        <i class="r_${x}"></i>
        </#list>
    </div>
    </#if>
    <p>今天已售 ${yishou} 期，还剩 ${zongqi-yishou} 期</p>
</div>
<div id="up_award" class="k3-hislog">
<table class="k3-old-tbl">
    <tr>
        <th>期次</th>
        <th>开奖号</th>
        <th>和值</th>
        <th>类型</th>
    </tr>
    <#list (0..awardList?size-1) as x>
    <#if (awardList[x].award.prizeNumber)?? && (awardList[x].award.prizeNumber) != "">
    <#assign kj = awardList[x].award.prizeNumber?split(",") />
    <tr>
        <td>${awardList[x].award.periodNo}</td>
        <td class="red">${kj[0]}</td>
        <td>${kj[1]}</td>
        <#if kj[6] == "1">
        <td class="orange">三连号</td>
        <#elseif kj[4] == "1">
        <td class="blue">三不同</td>
        <#elseif kj[5] == "1">
        <td class="green">三同号</td>
        <#else>
        <td class="gray">二同号</td>
        </#if>
    </tr>
    </#if>
    </#list>
</table>
</div>
<!-- <div class="k3-nbdr">
    <a href="">查询历史开奖</a>
</div> -->
</#if>
<#if formStatList??>
<h2 class="k3-rtit"><b>形态统计</b></h2>
<div class="cf k3-stat" id="k3_side_tab_t1">
   <span class="cur">今天<i class="ct"></i></span>
   <span>昨天<i class="ct"></i></span>
   <span>前天<i class="ct"></i></span>
</div>
<div class="k3-stat-info" id="k3_side_tab_c1">
    <#assign intro = ["今天","昨天","前天"] />
	<#list formStatList as ul>
	<ul<#if ul_index == 0> style="display:block"</#if>>
        <li class="intro">统计以下几种形态，${intro[ul_index]}出现多少次</li>
    <#if !ul?? || ul == "">
        <li>
            <span>三同号</span>
            <div class="progress"><div class="bar ct" style="width:0"></div></div>
            <em><b>0</b>次</em>
        </li>
        <li>
            <span>三不同号</span>
            <div class="progress"><div class="bar ct" style="width:0"></div></div>
            <em><b>0</b>次</em>
        </li>
        <li>
            <span>三连号</span>
            <div class="progress"><div class="bar ct" style="width:0"></div></div>
            <em><b>0</b>次</em>
        </li>
        <li>
            <span>二同号</span>
            <div class="progress"><div class="bar ct" style="width:0"></div></div>
            <em><b>0</b>次</em>
        </li>
        <li>
            <span>二不同号</span>
            <div class="progress"><div class="bar ct" style="width:0"></div></div>
            <em><b>0</b>次</em>
        </li>
    <#else>
        <#assign times = ul?replace("([二三不同连号:])","","r")?split(",")>
        <#assign times_max = max(times) />
		<#list ul?split(",") as li>
        <li>
            <span>${li?split(":")[0]}</span>
            <div class="progress">
                <#if li?split(":")[1]?number == times_max?number>
                <div class="bar ct cblue" style="width:100%"></div>
                <#else>
                <div class="bar ct" style="width:${(times[li_index]?number/times_max?number)?string.percent}"></div>
                </#if>
            </div>
            <em><b>${li?split(":")[1]}</b>次</em>
        </li>
        <#if li_index == 4><#break></#if>
		</#list>
    </#if>
	</ul>
	</#list>
</div>
</#if>
<#if sumStatList?? && sumStatList?size gt 0>
<h2 class="k3-rtit"><b>和值统计</b></h2>
<div class="cf k3-stat" id="k3_side_tab_t2">
   <span class="cur">今天<i class="ct"></i></span>
   <span>昨天<i class="ct"></i></span>
   <span>前天<i class="ct"></i></span>
</div>
<div class="k3-stat-info"  id="k3_side_tab_c2">
    <#list sumStatList as ul>
    <ul<#if ul_index == 0> style="display:block;"</#if>>
        <li class="intro">统计和值号码，${intro[ul_index]}出现多少次</li>
        <#if ul?? && ul != "">
        <#assign sum_times = ul?replace("([0-9]+:)","","r")?split(",") />
        <#assign sumTimes_max = max(sum_times) />
        <#list ul?split(",") as li>
        <li>
            <i>${li?split(":")[0]}</i>
            <div class="progress sum-bar">
                <#if li?split(":")[1]?number == sumTimes_max?number>
                <div class="bar ct cblue" style="width:100%"></div>
                <#else>
                <div class="bar ct" style="width:${(sum_times[li_index]?number/sumTimes_max?number)?string.percent}"></div>
                </#if>
           </div>
           <em><b>${li?split(":")[1]}</b>次</em>
        </li>
        </#list>
        <#else>
        <#list (3..18) as x>
        <li>
            <i>${x}</i>
            <div class="progress sum-bar">
                <div class="bar ct cblue" style="width:0"></div>
           </div>
           <em><b>0</b>次</em>
        </li>
        </#list>
        </#if>
    </ul>
    </#list>
    <div class="k3-show-all">
       <a href="#" id="k3_side_tab_show_all">显示全部号码</a>
    </div>
</div>
</#if>
<h2 class="k3-rtit"><a pbflag="wc_0_帮助中心" href="/help/" class="h3r">更多</a><b>帮助中心</b></h2>
<ul class="fc-list">
    <li><a class="anchor_hover" href="/help/i-${game.gameId!''}-play.html">${game.gameCn!''}玩法介绍及奖项规则</a></li>
    <#list helpList as h>
    <li><a class="anchor_hover" href="${h.url}">${h.title}</a></li>
    </#list>
</ul>