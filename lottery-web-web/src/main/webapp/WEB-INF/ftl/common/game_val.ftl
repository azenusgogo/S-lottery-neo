<input type="hidden" id="gameId" value="${game.gameId}">
<#if game.gameType == 3>
<input type="hidden" id="periodNo" value="${periodNo}">
<#list availablePeriodList as x>
<#if x?? && x.periodNo == periodNo>
<input type="hidden" id="endTime" value="${x.endTime?long?c}">
</#if>
</#list>
<#else>
<input type="hidden" id="periodNo" value="${availablePeriod.periodNo}">
<input type="hidden" id="endTime" value="${availablePeriod.endTime?long?c}">
</#if>
<input type="hidden" id="serverTime" value="${.now?long?c}">
<input type="hidden" id="gameType" value="${game.gameType}">
<input type="hidden" id="gameStatus" value="${game.gameStatus}">
<#if betNumberList?? && betNumberList?size gt 0>
<#assign defaultBets = "" />
<#list betNumberList as x>
<#assign defaultBets = defaultBets + ";" + x.localBetNumber + "#" + x.betTimes />
</#list>
<#assign defaultBets = defaultBets?substring(1) />
</#if>
<input type="hidden" id="defaultBets" value="${defaultBets!quickBetNumber!''}" />