<#assign showName = nickName!partyUserNickName!userId!'-' />
<div class="u-menu">
	<div class="u-info">
		<p class="welcome">
			您好，<span title="${showName}">${showName}</span>
		</p>
		可用余额：<b class="mycash">&minus;</b>元<br />
		提款申请：<b class="mycash_t">&minus;</b>元

		<div class="u-charge">
			<a href="${domainUrl}login/charge/pre.html" class="fbtn min-red">充值</a>
			<a href="${domainUrl}login/user/withdraw.html" class="fbtn min-white">提现</a>
		</div>
	</div>
	<ul>
		<li><a href="${domainUrl}login/user/order/bets.html"<#if func?? && func == "order"> class="cur"</#if>>我的订单</a></li>
		<li><a href="${domainUrl}login/user/trans/trans.html"<#if func?? && func == "deal"> class="cur"</#if>>资金明细</a></li>
		<li><a href="${domainUrl}login/user/info.html"<#if func?? && func == "info"> class="cur"</#if>>身份信息</a></li>
		<li><a href="${domainUrl}login/charge/pre.html"<#if func?? && func == "charge"> class="cur"</#if>>充值</a></li>
		<li><a href="${domainUrl}login/user/withdraw.html"<#if func?? && func == "withdraw"> class="cur"</#if>>提款</a></li>
		<li><a href="${domainUrl}login/user/pwd/change.html"<#if func?? && func == "pwd"> class="cur"</#if>>修改密码</a></li>
	</ul>
</div>