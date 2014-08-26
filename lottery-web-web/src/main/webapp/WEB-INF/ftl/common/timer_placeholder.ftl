<#macro echoTime endTime>
	<#local remained = (endTime?long - .now?long)/1000 />
	<#local days = ((remained/86400)%30)?int>
	<#local hours = ((remained/3600)%24)?int>
	<#local minutes = ((remained/60)%60)?int>
	<#local sec = (remained%60)?int>
	<#if days gt 0><b>${days}</b>天</#if><#t>
	<#if (hours + days) gt 0><b>${hours?string("00")}</b>小时</#if><#t>
	<#if (minutes + hours + days) gt 0><b>${minutes?string("00")}</b>分</#if><#t>
	<#if sec gt 0><b>${sec?string("00")}</b>秒</#if><#t>
</#macro>
<#if (game.gameId)?? && (game.gameId == "f9" || game.gameId == "f14")>
  <#list availablePeriodList as item>
     <#if (item.periodNo)?? && item.periodNo == periodNo>
        <#assign availablePeriod = item />
     </#if>
  </#list>
</#if>
<#if (availablePeriod.endTime)??>
<@echoTime endTime = availablePeriod.endTime />
<#else>
<b>--</b>小时<b>--</b>分<b>--</b>秒
</#if>