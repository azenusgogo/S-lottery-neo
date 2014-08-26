<#if !cdnBaseUrl??><#include "../../fakeData/extra.ftl" /></#if>
<#assign func = "help" />
<#assign m1 = "i" />
<#assign m2 = "qxc" />
<#assign seoTitle = "七星彩玩法介绍-帮助中心-搜狗彩票" />
<#assign seoKeywords = "七星彩玩法介绍" />
<#assign seoDescription = "七星彩投注区分为七位(第一、二、三、四、五、六、七位)，各位号码范围为0～9。每期从各位上开出1个号码作为中奖号码，即开奖号码为7位数。七星彩玩法即是竟猜7位开奖号码，且顺序一致。每注金额人民币2元，每周销售三期。有单式和复式两种投注类型。"/>
<#include "../../common/header.ftl" />

<div class="help_main">
	<div class="sitepath">
		您所在的位置：<a href="/">搜狗彩票</a> &gt; 帮助中心
	</div>
	<div class="hcont cf" id="help_center">
		<div class="help_menu">
			<#include "../include/menu.ftl" />
		</div>

		<div class="hright">
			<!--切换标签-->
			<div class="tab cf">
				<a href="/help/i-qxc-rule.html">玩法规则</a>
				<a href="/help/i-qxc-play.html" class="cur">玩法介绍</a>
				<a href="/help/i-qxc-award.html">奖项规则</a>
			</div>
			<div class="cont">
<!--内容.start-->
<p>
	七星彩投注区分为七位(第一、二、三、四、五、六、七位)，各位号码范围为0～9。每期从各位上开出1个号码作为中奖号码，即开奖号码为7位数。七星彩玩法即是竟猜7位开奖号码，且顺序一致。每注金额人民币2元，每周销售三期。有单式和复式两种投注类型。
</p>
<p class="top">
	<b>七星彩单式投注</b>：通过自选或机选从七位的0-9号码中各选择一个号码，组合为一注投注号码的投注，即为单式投注。
</p>
<p>
	<img src="${cdnBaseUrl}img/help/introduction/qxc01.jpg" />
</p>
<p class="top">
	<b>七星彩复试投注</b>：当从七位号码中至少有一位选择号码的个数多余一个，组合为多注投注号码的投注，即为复试投注。
</p>
<p>
	<img src="${cdnBaseUrl}img/help/introduction/qxc02.jpg" /> 
</p>

<!--内容.end-->
			</div>
		</div>
	</div>
</div>
<#include "../../common/footer.ftl" />
</body>
</html>