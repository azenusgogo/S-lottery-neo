<#assign fmSplitFlag = (fmOrderInfo.splitFlag!false)?string("1","0") />
<#assign fmBetsList = orderDetail.stakeBetInfoDtoList![] />
<#if fmSplitFlag == "0">
<tr>
    <th width="150">第${fmOrderInfo.periodNo}期</th>
    <th class="lth">选号方案</th>
    <th>方式</th>
    <th>倍数</th>
</tr>
<#list fmBetsList as x>
<#if x??>
<#assign fmLocalBetNumber = x.betNumber.localBetNumber />
<tr>
    <td>${x_index+1}</td>
    <td class="mynum">
		<#if "dlt,ssq"?contains(fmOrderInfo.gameId)>
			${fmLocalBetNumber?split(":")[0]} + 
			<em>${fmLocalBetNumber?split(":")[1]}</em>
		<#elseif "qlc,f9,f14"?contains(fmOrderInfo.gameId)>
			${fmLocalBetNumber}
		<#elseif fmOrderInfo.gameId == "qxc">
			${fmLocalBetNumber?replace(" "," | ")}
		<#elseif fmOrderInfo.gameId?contains("k3")>
			<span>选号：[
			<#if fmLocalBetNumber?contains("HZ_")>
				和值
			<#elseif fmLocalBetNumber?contains("AAA_")>
				<#if fmLocalBetNumber?contains("AAA_*")>
				三同号通选
				<#else>
				三同号单选
				</#if>
			<#elseif fmLocalBetNumber?contains("3BT_")>
				<#if fmLocalBetNumber?split("_")[1]?length == 3>
				三不同单式
				<#else>
				三不同复式
				</#if>
			<#elseif fmLocalBetNumber?contains("3LH_*")>
				三连号通选
			<#elseif fmLocalBetNumber?contains("AA_")>
				二同号复选
			<#elseif fmLocalBetNumber?contains("AAX_")>
				二同号单选
			<#elseif fmLocalBetNumber?contains("2BT_")>
				<#if fmLocalBetNumber?contains("$")>
				二不同
				<#elseif fmLocalBetNumber?split("_")[1]?length == 2>
				二不同单式
				<#else>
				二不同复式
				</#if>
			</#if>
			<#if fmLocalBetNumber?contains(",")>复式</#if>
			<#if fmLocalBetNumber?contains("$")>胆拖</#if>
			]</span>
			${fmLocalBetNumber?split("_")[1]!"-"}
		</#if>
    </td>
    <td>${x.betNumber.betTypeDesc!"-"}</td>
    <td>${x.betNumber.betTimes!"-"}倍</td>
</tr>
</#if>
</#list>
<#else>
<#list fmBetsList as x>
<#if x??>
<tbody>
	<tr>
	    <td colspan="4" class="mynum-stat"><b>出票状态：</b>${x.stakeOrder.stakeOrderStatusDesc}&nbsp;&nbsp;&nbsp;&nbsp;<b>彩票标识码：</b>${x.stakeOrder.officialTicketId} <i></i>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>中奖状态：</b>${x.stakeOrder.stakePrizeStatusDesc}</td>
	</tr>
	<tr>
	    <th width="150">第${fmOrderInfo.periodNo}期</th>
	    <th class="lth">选号方案</th>
	    <th>方式</th>
	    <th>倍数</th>
	</tr>
	<#list x.betNumberList as i>
	<#if i??>
	<tr>
	    <td>${i_index+1}</td>
	    <td class="mynum">
			<#if "dlt,ssq"?contains(fmOrderInfo.gameId)>
				${i.localBetNumber?split(":")[0]} + 
				<em>${i.localBetNumber?split(":")[1]}</em>
			<#elseif "qlc,f9,f14"?contains(fmOrderInfo.gameId)>
				${i.localBetNumber}
			<#elseif fmOrderInfo.gameId == "qxc">
				${i.localBetNumber?replace(" "," | ")}
			<#elseif fmOrderInfo.gameId?contains("k3")>
				<span>选号：[
				<#if i.localBetNumber?contains("HZ_")>
					和值
				<#elseif i.localBetNumber?contains("AAA_")>
					<#if i.localBetNumber?contains("AAA_*")>
					三同号通选
					<#else>
					三同号单选
					</#if>
				<#elseif i.localBetNumber?contains("3BT_")>
					<#if i.localBetNumber?split("_")[1]?length == 3>
					三不同单式
					<#else>
					三不同复式
					</#if>
				<#elseif i.localBetNumber?contains("3LH_*")>
					三连号通选
				<#elseif i.localBetNumber?contains("AA_")>
					二同号复选
				<#elseif i.localBetNumber?contains("AAX_")>
					二同号单选
				<#elseif i.localBetNumber?contains("2BT_")>
					<#if i.localBetNumber?contains("$")>
					二不同
					<#elseif i.localBetNumber?split("_")[1]?length == 2>
					二不同单式
					<#else>
					二不同复式
					</#if>
				</#if>
				<#if i.localBetNumber?contains(",")>复式</#if>
				<#if i.localBetNumber?contains("$")>胆拖</#if>
				]</span>
				${i.localBetNumber?split("_")[1]!"-"}
			</#if>
	    </td>
	    <td>${x.betNumber.betTypeDesc!"-"}</td>
	    <td>${x.betNumber.betTimes!"-"}倍</td>
	</tr>
	</#if>
	</#list>
</tbody>
</#if>
</#list>
</#if>