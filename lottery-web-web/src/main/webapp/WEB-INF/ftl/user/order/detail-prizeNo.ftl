<#if orderDetail.prizeNumber?? && orderDetail.prizeNumber!= "">
<#assign fmPrizeNo = orderDetail.prizeNumber />
	<#if fmOrderInfo.gameId?contains("k3")>
		<#list fmPrizeNo?split(",")[0]?split(" ") as x>
		<em>${x}</em>
		</#list>
		<input type="hidden" id="kaiRed" value="${fmPrizeNo?split(',')[0]}" />
		<input type="hidden" id="kaiHz" value="${fmPrizeNo?split(',')[1]}" />
	<#elseif "ssq,dlt,qlc"?contains(fmOrderInfo.gameId)>
		<#list fmPrizeNo?split(":")[0]?split(" ") as x>
		<em>${x}</em>
		</#list>
		<#list fmPrizeNo?split(":")[1]?split(" ") as x>
		<em class="blue">${x}</em>
		</#list>
		<input type="hidden" id="kaiRed" value="${fmPrizeNo?split(':')[0]}" />
		<input type="hidden" id="kaiBlue" value="${fmPrizeNo?split(':')[1]}" />
	<#elseif "qxc,f9,f14"?contains(fmOrderInfo.gameId)>
		<#list fmPrizeNo?split(" ") as x>
		<em>${x}</em>
		</#list>
		<input type="hidden" id="kaiRed" value="${fmPrizeNo?split(':')[0]}" />
	</#if>
<#else>
&minus;
</#if>