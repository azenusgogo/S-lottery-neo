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
                    <th colspan="2" class="w2">投注区</th>
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
                    <th class="lt">主胜</th>
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
                    <td colspan="9" class="todayinfo">
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
                    <td>${m.displayHomeName!}</td>
                    <td>${m.displayAwayName!}</td>
                    <td>
                        <a href="#" class="open" matchid="${m.matchNo!}"><span>展开</span><i class="arrud"></i></a>
                    </td>
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
                <tr class="zjq_qbc_bf">
                    <td colspan="9" class="bfsel">
                        <div class="bifendo cf">
                            <dl class="cf">
                                <dt class="h">主胜</dt>
                                <dd class="cf">
                                    <span model='{"type":"bf","index":"24","code":"11310","sp":"${m.sps["11310"]}","matchid":"${m.matchNo!}","hide":1}'>1:0<b>${m.sps["11310"]}</b></span>
                                    <span model='{"type":"bf","index":"25","code":"11320","sp":"${m.sps["11320"]}","matchid":"${m.matchNo!}","hide":1}'>2:0<b>${m.sps["11320"]}</b></span>
                                    <span model='{"type":"bf","index":"26","code":"11321","sp":"${m.sps["11321"]}","matchid":"${m.matchNo!}","hide":1}'>2:1<b>${m.sps["11321"]}</b></span>
                                    <span model='{"type":"bf","index":"27","code":"11330","sp":"${m.sps["11330"]}","matchid":"${m.matchNo!}","hide":1}'>3:0<b>${m.sps["11330"]}</b></span>
                                    <span model='{"type":"bf","index":"28","code":"11331","sp":"${m.sps["11331"]}","matchid":"${m.matchNo!}","hide":1}'>3:1<b>${m.sps["11331"]}</b></span>
                                    <span model='{"type":"bf","index":"29","code":"11332","sp":"${m.sps["11332"]}","matchid":"${m.matchNo!}","hide":1}'>3:2<b>${m.sps["11332"]}</b></span>
                                </dd>
                                <dd class="cf">
                                    <span model='{"type":"bf","index":"30","code":"11340","sp":"${m.sps["11340"]}","matchid":"${m.matchNo!}","hide":1}'>4:0<b>${m.sps["11340"]}</b></span>
                                    <span model='{"type":"bf","index":"31","code":"11341","sp":"${m.sps["11341"]}","matchid":"${m.matchNo!}","hide":1}'>4:1<b>${m.sps["11341"]}</b></span>
                                    <span model='{"type":"bf","index":"32","code":"11342","sp":"${m.sps["11342"]}","matchid":"${m.matchNo!}","hide":1}'>4:2<b>${m.sps["11342"]}</b></span>
                                    <span model='{"type":"bf","index":"33","code":"11350","sp":"${m.sps["11350"]}","matchid":"${m.matchNo!}","hide":1}'>5:0<b>${m.sps["11350"]}</b></span>
                                    <span model='{"type":"bf","index":"34","code":"11351","sp":"${m.sps["11351"]}","matchid":"${m.matchNo!}","hide":1}'>5:1<b>${m.sps["11351"]}</b></span>
                                    <span model='{"type":"bf","index":"35","code":"11352","sp":"${m.sps["11352"]}","matchid":"${m.matchNo!}","hide":1}'>5:2<b>${m.sps["11352"]}</b></span>
                                    <span model='{"type":"bf","index":"36","code":"11390","sp":"${m.sps["11390"]}","matchid":"${m.matchNo!}","hide":1}' class="long">胜其他<b>${m.sps["11390"]}</b></span>
                                </dd>
                            </dl>
                            <dl class="cf">
                                <dt>平</dt>
                                <dd class="cf">
                                    <span model='{"type":"bf","index":"37","code":"11300","sp":"${m.sps["11300"]}","matchid":"${m.matchNo!}","hide":1}'>0:0<b>${m.sps["11300"]}</b></span>
                                    <span model='{"type":"bf","index":"38","code":"11311","sp":"${m.sps["11311"]}","matchid":"${m.matchNo!}","hide":1}'>1:1<b>${m.sps["11311"]}</b></span>
                                    <span model='{"type":"bf","index":"39","code":"11322","sp":"${m.sps["11322"]}","matchid":"${m.matchNo!}","hide":1}'>2:2<b>${m.sps["11322"]}</b></span>
                                    <span model='{"type":"bf","index":"40","code":"11333","sp":"${m.sps["11333"]}","matchid":"${m.matchNo!}","hide":1}'>3:3<b>${m.sps["11333"]}</b></span>
                                    <span model='{"type":"bf","index":"41","code":"11399","sp":"${m.sps["11399"]}","matchid":"${m.matchNo!}","hide":1}'>平其他<b>${m.sps["11399"]}</b></span>
                                </dd>
                            </dl>
                            <dl class="cf">
                                <dt>主负</dt>
                                <dd class="cf">
                                    <span model='{"type":"bf","index":"42","code":"11301","sp":"${m.sps["11301"]}","matchid":"${m.matchNo!}","hide":1}'>0:1<b>${m.sps["11301"]}</b></span>
                                    <span model='{"type":"bf","index":"43","code":"11302","sp":"${m.sps["11302"]}","matchid":"${m.matchNo!}","hide":1}'>0:2<b>${m.sps["11302"]}</b></span>
                                    <span model='{"type":"bf","index":"44","code":"11312","sp":"${m.sps["11312"]}","matchid":"${m.matchNo!}","hide":1}'>1:2<b>${m.sps["11312"]}</b></span>
                                    <span model='{"type":"bf","index":"45","code":"11303","sp":"${m.sps["11303"]}","matchid":"${m.matchNo!}","hide":1}'>0:3<b>${m.sps["11303"]}</b></span>
                                    <span model='{"type":"bf","index":"46","code":"11313","sp":"${m.sps["11313"]}","matchid":"${m.matchNo!}","hide":1}'>1:3<b>${m.sps["11313"]}</b></span>
                                    <span model='{"type":"bf","index":"47","code":"11323","sp":"${m.sps["11323"]}","matchid":"${m.matchNo!}","hide":1}'>2:3<b>${m.sps["11323"]}</b></span>
                                </dd>
                                <dd class="cf">
                                    <span model='{"type":"bf","index":"48","code":"11304","sp":"${m.sps["11304"]}","matchid":"${m.matchNo!}","hide":1}'>0:4<b>${m.sps["11304"]}</b></span>
                                    <span model='{"type":"bf","index":"49","code":"11314","sp":"${m.sps["11314"]}","matchid":"${m.matchNo!}","hide":1}'>1:4<b>${m.sps["11314"]}</b></span>
                                    <span model='{"type":"bf","index":"50","code":"11324","sp":"${m.sps["11324"]}","matchid":"${m.matchNo!}","hide":1}'>2:4<b>${m.sps["11324"]}</b></span>
                                    <span model='{"type":"bf","index":"51","code":"11305","sp":"${m.sps["11305"]}","matchid":"${m.matchNo!}","hide":1}'>0:5<b>${m.sps["11305"]}</b></span>
                                    <span model='{"type":"bf","index":"52","code":"11315","sp":"${m.sps["11315"]}","matchid":"${m.matchNo!}","hide":1}'>1:5<b>${m.sps["11315"]}</b></span>
                                    <span model='{"type":"bf","index":"53","code":"11325","sp":"${m.sps["11325"]}","matchid":"${m.matchNo!}","hide":1}'>2:5<b>${m.sps["11325"]}</b></span>
                                    <span model='{"type":"bf","index":"54","code":"11309","sp":"${m.sps["11309"]}","matchid":"${m.matchNo!}","hide":1}' class="long">负其他<b>${m.sps["11309"]}</b></span>
                                </dd>
                            </dl>
                        </div>
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