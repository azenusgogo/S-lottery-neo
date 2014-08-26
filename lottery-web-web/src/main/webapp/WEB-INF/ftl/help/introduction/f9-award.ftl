<#if !cdnBaseUrl??><#include "../../fakeData/extra.ftl" /></#if>
<#assign func = "help" />
<#assign m1 = "i" />
<#assign m2 = "f9" />
<#assign seoTitle = "任选九奖项规则-帮助中心-搜狗彩票" />
<#assign seoKeywords = "任选九奖项规则" />
<#assign seoDescription = "奖项设置：“任选9场”只设1个浮动奖级，即一等奖，单注奖金封顶500万元。" />
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
				<a href="/help/i-f9-rule.html">玩法规则</a>
				<a href="/help/i-f9-play.html">玩法介绍</a>
				<a href="/help/i-f9-award.html" class="cur">奖项规则</a>
			</div>
			<div class="cont">
<!--内容.start-->
<p>
	<b>奖项设置</b>：“任选<span>9</span>场”只设<span>1</span>个浮动奖级，即一等奖，单注奖金封顶<span>500</span>万元。
</p>
<p>
	<img src="${cdnBaseUrl}img/help/introduction/f902.jpg" />
</p>
<!--内容.end-->
			</div>
		</div>
	</div>
</div>
<#include "../../common/footer.ftl" />
</body>
</html>