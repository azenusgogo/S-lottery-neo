<#if !cdnBaseUrl??><#include "../../fakeData/extra.ftl" /></#if>
<#assign func = "help" />
<#assign m1 = "i" />
<#assign m2 = "k3js" />
<#assign seoTitle = "老快3奖项规则-帮助中心-搜狗彩票" />
<#assign seoKeywords = "老快3奖项规则" />
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
				<a href="/help/i-k3js-rule.html">玩法规则</a>
				<a href="/help/i-k3js-play.html">玩法介绍</a>
				<a href="/help/i-k3js-award.html" class="cur">奖项规则</a>
			</div>
			<div class="cont">
<!--内容.start-->
<p>
	<img src="${cdnBaseUrl}img/help/introduction/k3js09.jpg" />
</p>
<!--内容.end-->
			</div>
		</div>
	</div>
</div>
<#include "../../common/footer.ftl" />
</body>
</html>