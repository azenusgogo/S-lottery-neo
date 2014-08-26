<div class="row-right">
    <h2><b>${game.gameCn}奖池</b></h2>
    <div class="pool-nav" id="up_pool">
        <p>
        <#macro showPool gameId Pool="">
        <#if Pool != "">
            <#local awp = Pool?split(":")[0]?split("_") />
            <#local zhu = Pool?split(":")[1] />
            <#local rmb = ["亿","万","元"] />
            <strong>
                <#list [0,1,2] as x>
                <#if awp[x]?number gt 0>
                ${awp[x]}<em>${rmb[x]}</em><#t>
                </#if>
                </#list>
            </strong><br />
            <span>至少可开出<i>${zhu}</i>注500万大奖</span>
        <#else>
            <b>等待公布...</b><br />
            <#if gameId == "ssq">
            每周二、四、日晚上21:30开奖
            <#elseif gameId == "dlt">
            每周一、三、六晚上20:30开奖
            <#elseif gameId == "qxc">
            每周二、五、日晚上20:30开奖
            <#elseif gameId == "qlc">
            每周一、三、五晚上21:30开奖
            </#if>
        </#if>
        </#macro>
        <@showPool game.gameId awardPool />
        </p>
    </div>
    <#if awardList?? && awardList?size gt 0>
    <h2><b>开奖公告</b></h2>
    <div class="kj-nav" id="up_award">
        <strong>${awardList[0].period.periodNo}</strong>期 <em>${awardList[0].period.offcialAwardTime?date}</em>
        <div class="kj-num">
        <#if (awardList[0].award.prizeNumber)??>
            <#list awardList[0].award.prizeNumber?split(":") as x>
                <#if x_index == 0>
                <#list x?split(" ") as i>
                <i>${i}</i>
                </#list>
                <#else>
                <#list x?split(" ") as j>
                <i class="blue-ball-min">${j}</i>
                </#list>
                </#if>
            </#list>
        </#if>
        </div>
        <#if (awardList[0].award.bonusLevelDetail)??>
        <#macro greed gameId bonus>
        <#if bonus != "">
            <#local greed1 = bonus?split(";")[0] />
            <#if gameId == "dlt">
                <#local greed2 = bonus?split(";")[2] />
                <#local greed3 = bonus?split(";")[4] />
            <#else>
                <#local greed2 = bonus?split(";")[1] />
                <#local greed3 = bonus?split(";")[2] />
            </#if>
        <p>
            一等奖：<em>${greed1?split("_")[1]}</em>注，每注<em>${greed1?split("_")[2]?number/100}</em>元<br />
            二等奖：<em>${greed2?split("_")[1]}</em>注，每注<em>${greed2?split("_")[2]?number/100}</em>元
            <#if gameId =="dlt" || gameId =="qlc"><br />
            三等奖：<em>${greed3?split("_")[1]}</em>注，每注<em>${greed3?split("_")[2]?number/100}</em>元
            </#if>
        </p>
        </#if>
        </#macro>
        <@greed game.gameId awardList[0].award.bonusLevelDetail />
        </#if>

        <table cellspacing="0" width="198">
            <tr>
                <th>期次</th>
                <th>开奖号码</th>
            </tr>
        <#list (0..(awardList?size-1)) as x>
        <#if (awardList[x].award.prizeNumber)??>
            <tr>
                <td>${awardList[x].award.periodNo!"-"}期</td>
                <td class="wq-num">
            	<#list awardList[x].award.prizeNumber?split(":") as m>
            	<#if m_index == 0>
				${m}
            	<#else>
				<b>${m}</b>
				</#if>
            	</#list>
                </td>
            </tr>
            <#if x == 4><#break></#if>
        </#if>
        </#list>
        </table>
    </div>
    </#if>
    <h2 pbflag="wc_0_帮助中心"><a href="/help/" class="h3r">更多</a><b>帮助中心</b></h2>
    <ul class="fc-list" pbflag="wc_0_帮助中心">
        <li><a class="anchor_hover" href="/help/i-${(game.gameId)!'ssq'}-play.html">${(game.gameCn)!'双色球'}玩法介绍及奖项规则</a></li>
        <#list helpList as h>
        <li><a class="anchor_hover" href="${h.url}">${h.title}</a></li>
        </#list>
    </ul>
</div>