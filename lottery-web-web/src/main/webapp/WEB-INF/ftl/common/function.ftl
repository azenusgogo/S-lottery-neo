<#--定义时间格式-->
<#setting time_format="HH:mm"/>
<#setting date_format="yyyy-MM-dd"/>

<#function max arr>
<#local max = 0>
<#list arr as i>
	<#if i?number gt max?number>
	<#local max = i>
	</#if>
</#list>
<#return max>
</#function>

<#function pout arr>
<#if arr != "">
<#return arr>
<#else>
<#return "- -">
</#if>
</#function>

<#assign helpList = [
	{
		"title":"如何快速注册搜狗彩票用户？",
		"url":"/help/p-all.html#hot01"
	},
	{
		"title":"注册新用户收费吗？",
		"url":"/help/p-all.html#hot02"
	},
	{
		"title":"忘记登录密码怎么办？",
		"url":"/help/p-all.html#hot03"
	},
	{
		"title":"已开通网银，如何进行网上充值？",
		"url":"/help/f-recharge.html#rec03"
	},
	{
		"title":"搜狗彩票支持哪些充值方式？",
		"url":"/help/f-recharge.html#rec01"
	},
	{
		"title":"网上充值安全吗？",
		"url":"/help/f-recharge.html#rec04"
	},
	{
		"title":"搜狗彩票的投注流程是什么？",
		"url":"/help/f-bet.html#bet01"
	},
	{
		"title":"搜狗彩票的支付流程是什么？",
		"url":"/help/f-pay.html#pay01"
	},
	{
		"title":"什么是支付密码？",
		"url":"/help/f-pay.html#pay03"
	},
	{
		"title":"忘记支付密码如何找回？",
		"url":"/help/f-pay.html#pay06"
	}
] />