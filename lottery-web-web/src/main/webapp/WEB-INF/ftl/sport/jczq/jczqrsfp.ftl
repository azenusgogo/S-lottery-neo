<#include "../../common/function.ftl" />
<#if !cdnBaseUrl??><#include "../fakeData/jczq.ftl" /></#if>
<#assign func = "jczq" />
<#-- 引入header部分 -->
<#include "../../common/header.ftl" />

<#include "include/jcinfo.ftl" />
<div class="wrapper" id="jc_hhgg">
    <div class="jczq">
    	<#include "include/jctop.ftl" />
        <table class="jcarea">
            <thead>
                <tr class="th1">
                    <th colspan="5" class="w1">对阵信息区</th>
                    <th colspan="4" class="w2">投注区</th>
                    <th colspan="2">分析区</th>
                </tr>
                <tr class="th2">
                    <th class="num">编号</th>
                    <th class="match">赛事</th>
                    <th class="time">
                        <div class="sel">
                            <span>比赛时间</span><i class="arrud"></i>
                            <div class="list"><a href="#">比赛时间</a><a href="#">停售时间</a></div>
                        </div>
                    </th>
                    <th class="t1">主队</th>
                    <th class="t2">客队</th>
                    <th class="mt">主胜</th>
                    <th class="mt">平</th>
                    <th class="mt">主负</th>
                    <th class="dan">胆</th>
                    <th class="spf">
                        <div class="ge cf">
                            <span class="h">平均欧赔</span>
                            <span>主胜</span>
                            <span class="m">平</span>
                            <span>主负</span>
                        </div>
                    </th>
                    <th class="data">数据</th>
                </tr>
            </thead>
            <#list currentMatchList as x>
            <#if x?? && x?size gt 0>
            <tbody>
                <tr>
                    <td colspan="11" class="todayinfo">
                        <a href="#" data="${x[0].matchTime?string("yyyyMMdd")}">隐藏<i></i></a>
                        ${x[0].matchTime?string("yyyy年M月d日")} ${x[0].weekCn!}  共有 <b>${x?size}</b> 场比赛可以投注
                    </td>
                </tr>
            </tbody>
            <#list x as m>
            <tbody class="jc_row" data='{"date":"${m.matchNoCn}","nick":"${m.displayHomeName!} VS ${m.displayAwayName!}","matchid":"${m.matchNo!}","league":"${m.leagueName!}","time":"${m.matchTime?string("yyyyMMdd")}"}'>
                <tr>
                    <td class="bh">${m.matchNoCn?split(" ")[0]}<br>${m.matchNoCn?split(" ")[1]}</td>
                    <td style="background:${m.leagueColor}" class="match">${m.displayLeagueName!"-"}</td>
                    <td><span>${m.matchTime?string("HH:mm")}</span><span style="display:none">${m.officialEndTime?string("HH:mm")}</span></td>
                    <td>
                        <#assign let = (m.sps.let)!"--" />
                        (<span <#if let?contains("+")>class="red"<#else>class="green"</#if>>${let}</span>) ${m.displayHomeName!}
                    </td>
                    <td>${m.displayAwayName!}</td>
                    <td class="dosel"><a model='{"type":"rqspf","token":"${let}","index":"1","code":"11103","sp":"${m.sps["11103"]}","matchid":"${m.matchNo!}"}' href="#">${m.sps["11103"]}</a></td>
                    <td class="dosel"><a model='{"type":"rqspf","token":"${let}","index":"2","code":"11101","sp":"${m.sps["11101"]}","matchid":"${m.matchNo!}"}' href="#" class="m">${m.sps["11101"]}</a></td>
                    <td class="dosel"><a model='{"type":"rqspf","token":"${let}","index":"3","code":"11100","sp":"${m.sps["11100"]}","matchid":"${m.matchNo!}"}' href="#">${m.sps["11100"]}</a></td>
                    <td><label><input type="checkbox" class="dm" disabled="" matchid="${m.matchNo!}"></label></td>
                    <td class="spv">
                        <span>${m.oddsWin!"--"}</span>
                        <span class="m">${m.oddsDraw!"--"}</span>
                        <span>${m.oddsLose!"--"}</span>
                    </td>
                    <td class="fx">
                        <a href="">析</a>
                        <a href="">亚</a>
                        <a href="">欧</a>
                    </td>
                </tr>
            </tbody>
            </#list>
            </#if>
            </#list>
        </table>
    </div>
    <#include "include/float.ftl" />
</div>
<#include "../../common/footer.ftl" />
</body>
</html>