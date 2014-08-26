<#-- balls,needs arg:color,num,missing,dm(_dm) -->
<#macro Balls color num missing="" dm="">
<#if missing != "">
	<#if color == "red">
	<#local ball_missing = missing?split(":")[0]?split(" ") />
	<#else>
	<#local ball_missing = missing?split(":")[1]?split(" ") />
	</#if>
	<#local ball_max = max(ball_missing) />
	<#list (1..num) as x>
	<li><#rt>
		<em data-model='{"color":"${color}${dm}","number":"${x}"}'>${x?string("00")}</em><#t>
		<span<#if ball_missing[x-1] == ball_max> class="red"</#if>>${ball_missing[x-1]}</span><#t>
	</li><#lt>
	</#list>
<#else>
	<#list (1..num) as x>
	<li><em data-model='{"color":"${color}${dm}","number":"${x}"}'>${x?string("00")}</em><span>-</span></li>
	</#list>
</#if>
</#macro>